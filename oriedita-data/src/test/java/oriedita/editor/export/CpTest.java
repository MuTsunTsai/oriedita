package oriedita.editor.export;

import fold.io.CreasePatternReader;
import fold.model.Edge;
import fold.model.FoldFile;
import javax.enterprise.context.ApplicationScoped;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import oriedita.editor.save.Save;
import oriedita.editor.save.SaveProvider;
import origami.crease_pattern.element.LineSegment;
import origami.crease_pattern.element.Point;

import javax.swing.JFrame;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

@ApplicationScoped
class CpTest {
    @Test
    void testLoadAndSaveCpFile() throws IOException {
        URL birdbase = getClass().getClassLoader().getResource("square.cp"); // very simple file so rounding doesnt make the test fail
        assert birdbase != null;
        InputStream is = birdbase.openStream();
        Save save1 = SaveProvider.createInstance();

        CreasePatternReader creasePatternReader = new CreasePatternReader(is);

        FoldFile foldFile = creasePatternReader.read();

        for (Edge edge : foldFile.getRootFrame().getEdges()) {
            save1.addLineSegment(new LineSegment(new Point(edge.getStart().getX(), edge.getStart().getY()), new Point(edge.getEnd().getX(), edge.getEnd().getY()), FoldImporter.getColor(edge.getAssignment())));
        }

        File saveFile = File.createTempFile("export", ".cp");
        new CpExporter(JFrame::new).doExport(save1, saveFile);

        String expected = Files.readString(saveFile.toPath()).replace("\r","");

        String actual = Files.readString(new File(birdbase.getFile()).toPath()).replace("\r","");

        Assertions.assertEquals(expected, actual);
    }

    /**
     * A file for which faces cannot be computed should still save.
     */
    @Test
    void testLoadAndSaveCpFileWithoutFaces() throws IOException {
        URL faceless = getClass().getClassLoader().getResource("faceless.cp"); // very simple file so rounding doesnt make the test fail
        assert faceless != null;
        InputStream is = faceless.openStream();
        Save save = SaveProvider.createInstance();

        CreasePatternReader creasePatternReader = new CreasePatternReader(is);

        FoldFile foldFile = creasePatternReader.read();

        for (Edge edge : foldFile.getRootFrame().getEdges()) {
            save.addLineSegment(new LineSegment(new Point(edge.getStart().getX(), edge.getStart().getY()), new Point(edge.getEnd().getX(), edge.getEnd().getY()), FoldImporter.getColor(edge.getAssignment())));
        }

        File saveFile = File.createTempFile("export_faceless", ".cp");
        new CpExporter(JFrame::new).doExport(save, saveFile);

        String expected = Files.readString(saveFile.toPath()).replace("\r","");

        String actual = Files.readString(new File(faceless.getFile()).toPath()).replace("\r","");

        Assertions.assertEquals(expected, actual);
    }
}
