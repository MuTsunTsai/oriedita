package oriedita.editor.export;

import fold.io.CustomFoldWriter;
import fold.model.Edge;
import fold.model.Face;
import fold.model.FoldEdgeAssignment;
import fold.model.FoldFile;
import fold.model.FoldFrame;
import fold.model.Vertex;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import oriedita.editor.export.api.FileExporter;
import oriedita.editor.save.OrieditaFoldFile;
import oriedita.editor.save.Save;
import oriedita.editor.tools.ResourceUtil;
import origami.crease_pattern.LineSegmentSet;
import origami.crease_pattern.PointSet;
import origami.crease_pattern.element.LineColor;
import origami.crease_pattern.element.Point;
import origami.crease_pattern.worker.WireFrame_Worker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FoldExporter implements FileExporter {
    @Inject
    public FoldExporter() {
    }

    private FoldEdgeAssignment getAssignment(LineColor lineColor) {
        switch (lineColor) {
            case BLACK_0:
                return FoldEdgeAssignment.BORDER;
            case RED_1:
                return FoldEdgeAssignment.MOUNTAIN_FOLD;
            case BLUE_2:
                return FoldEdgeAssignment.VALLEY_FOLD;
            case CYAN_3:
            case ORANGE_4:
            case MAGENTA_5:
            case GREEN_6:
            case YELLOW_7:
            case PURPLE_8:
            case OTHER_9:
                return FoldEdgeAssignment.FLAT_FOLD;
            default:
                return FoldEdgeAssignment.UNASSIGNED;
        }
    }

    private void exportFile(Save save, LineSegmentSet lineSegmentSet, File file) throws InterruptedException, IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            CustomFoldWriter<FoldFile> foldFileCustomFoldWriter = new CustomFoldWriter<>(fileOutputStream);
            foldFileCustomFoldWriter.write(toFoldSave(save, lineSegmentSet));
        }
    }

    public OrieditaFoldFile toFoldSave(Save save) throws InterruptedException {
        LineSegmentSet s = new LineSegmentSet();
        s.setSave(save);
        return toFoldSave(save, s);
    }

    public OrieditaFoldFile toFoldSave(Save save, LineSegmentSet lineSegmentSet) throws InterruptedException {
        WireFrame_Worker wireFrame_worker = new WireFrame_Worker(3.0);
        wireFrame_worker.setLineSegmentSetWithoutFaceOccurence(lineSegmentSet);
        PointSet pointSet = wireFrame_worker.get();
        boolean includeFaces = pointSet.calculateFaces();

        OrieditaFoldFile foldFile = new OrieditaFoldFile();
        foldFile.setCreator("oriedita");
        FoldFrame rootFrame = foldFile.getRootFrame();

        for (int i = 1; i <= pointSet.getNumPoints(); i++) {
            Vertex vertex = new Vertex();
            vertex.setX(pointSet.getPoint(i).getX());
            vertex.setY(pointSet.getPoint(i).getY());
            rootFrame.getVertices().add(vertex);
        }

        for (int i = 1; i <= pointSet.getNumLines(); i++) {
            Edge edge = new Edge();
            edge.setAssignment(getAssignment(pointSet.getColor(i)));
            edge.setFoldAngle(getFoldAngle(pointSet.getColor(i)));
            Vertex startVertex = rootFrame.getVertices().get(pointSet.getBegin(i) - 1);
            Vertex endVertex = rootFrame.getVertices().get(pointSet.getEnd(i) - 1);

            edge.setStart(startVertex);
            edge.setEnd(endVertex);

            rootFrame.getEdges().add(edge);
        }

        if (includeFaces) {
            for (int i = 1; i <= pointSet.getNumFaces(); i++) {
                origami.folding.element.Face pface = pointSet.getFace(i);
                Face face = new Face();

                ArrayList<Vertex> faceVertices = new ArrayList<Vertex>();
                ArrayList<Edge> faceEdges = new ArrayList<Edge>();
                Vertex vertexFirst = rootFrame.getVertices().get(pface.getPointId(1) - 1);
                Vertex vertexLast = rootFrame.getVertices().get(pface.getPointId(pface.getNumPoints()) - 1);
                faceVertices.add(vertexFirst);
                faceEdges.add(findEdge(vertexFirst, vertexLast, rootFrame.getEdges()));
                for (int j = 2; j <= pface.getNumPoints(); j++) {
                    Vertex currentVertex = rootFrame.getVertices().get(pface.getPointId(j) - 1);
                    Vertex previousVertex = rootFrame.getVertices().get(pface.getPointId(j - 1) - 1);
                    faceVertices.add(currentVertex);
                    faceEdges.add(findEdge(currentVertex, previousVertex, rootFrame.getEdges()));
                }
                face.setVertices(faceVertices);
                face.setEdges(faceEdges);

                rootFrame.getFaces().add(face);
            }
        }

        foldFile.setCircles(save.getCircles());
        foldFile.setTexts(save.getTexts());
        foldFile.setVersion(ResourceUtil.getVersionFromManifest());

        return foldFile;
    }

    private Edge findEdge(Vertex v1, Vertex v2, List<Edge> edges) {
        Optional<Edge> foundEdge = edges.stream().filter(e -> (e.getStart() == v1 && e.getEnd() == v2) || e.getStart() == v2 && e.getEnd() == v1).findFirst();

        if (foundEdge.isPresent()) {
            return foundEdge.get();
        }

        throw new IllegalStateException("Edge in face not found");
    }

    private double getFoldAngle(LineColor color) {
        switch (color) {
            case BLUE_2:
                return 180;
            case RED_1:
                return -180;
            default:
                return 0;
        }
    }

    @Override
    public boolean supports(File filename) {
        return filename.getName().endsWith(".fold");
    }

    @Override
    public void doExport(Save save, File file) throws IOException {
        try {
            LineSegmentSet s = new LineSegmentSet();
            s.setSave(save);
            if (s.getNumLineSegments() == 0) {
                s.addLine(new Point(0, 0), new Point(0, 0), LineColor.BLACK_0);
            }
            exportFile(save, s, file);
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String getName() {
        return "FOLD";
    }

    @Override
    public String getExtension() {
        return ".fold";
    }
}
