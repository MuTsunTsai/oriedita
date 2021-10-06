package origami.crease_pattern.element;

import origami.crease_pattern.OritaCalc;
import origami_editor.sortingbox.SortingBox;
import origami_editor.sortingbox.WeightedValue;

public class Polygon {
    int vertexCount;             //How many vertices

    Point[] vertices;//vertex

    public Polygon(int _vertexCount) {
        vertexCount = _vertexCount;
        Point[] t0 = new Point[vertexCount + 1];   //vertex
        for (int i = 0; i <= vertexCount; i++) {
            t0[i] = new Point();
        }
        // red=255;green=0;blue=0;
        vertices = t0;
    }

    public Polygon(Point p1, Point p2, Point p3, Point p4) {
        this(4);

        set(1, p1);
        set(2, p2);
        set(3, p3);
        set(4, p4);
    }

    //Set the i-th vertex of the polygon
    public void set(int i, Point p) {
        vertices[i].set(p);
    }

    // 0, when all of the line segment s0 exists outside the convex polygon (the boundary line is not considered inside)
    // When the line segment s0 exists both outside the convex polygon and across the boundary line 1,
    // When the line segment s0 exists inside the convex polygon, the boundary line, and the outside 2,
    // When all of the line segments s0 are on the boundary of the convex polygon 3,
    // When the line segment s0 exists both inside the convex polygon and across the boundary line 4,
    // When all of the line segment s0 exists inside the convex polygon (the boundary line is not considered to be inside) 5,
    //return it
    public Intersection inside_outside_check(LineSegment s0) {
        SortingBox<Point> nbox = new SortingBox<>();

        int i_intersection = 0;

        Point[] intersection = new Point[vertexCount * 2 + 3];
        for (int i = 0; i <= vertexCount * 2 + 2; i++) {
            intersection[i] = new Point();
        }

        i_intersection++;
        intersection[i_intersection].set(s0.getA());

        i_intersection++;
        intersection[i_intersection].set(s0.getB());

        LineSegment.Intersection kh; //oc.senbun_kousa_hantei(s0,s)の値の格納用

        LineSegment s = new LineSegment();

        for (int i = 1; i <= vertexCount; i++) {
            if (i == vertexCount) {
                s.set(vertices[vertexCount], vertices[1]); //Line segment
            } else {
                s.set(vertices[i], vertices[i + 1]);
            } //Line segment

            kh = OritaCalc.determineLineSegmentIntersection(s0, s);

            if (kh == LineSegment.Intersection.INTERSECTS_1) {
                i_intersection++;
                intersection[i_intersection].set(OritaCalc.findIntersection(s0, s));
            }
            if (kh == LineSegment.Intersection.INTERSECTS_TSHAPE_S2_VERTICAL_BAR_27) {
                i_intersection++;
                intersection[i_intersection].set(OritaCalc.findIntersection(s0, s));
            }
            if (kh == LineSegment.Intersection.INTERSECTS_TSHAPE_S2_VERTICAL_BAR_28) {
                i_intersection++;
                intersection[i_intersection].set(OritaCalc.findIntersection(s0, s));
            }
            if (kh == LineSegment.Intersection.PARALLEL_START_OF_S1_CONTAINS_START_OF_S2_321) {
                i_intersection++;
                intersection[i_intersection].set(s.getB());
            }
            if (kh == LineSegment.Intersection.PARALLEL_START_OF_S1_CONTAINS_END_OF_S2_331) {
                i_intersection++;
                intersection[i_intersection].set(s.getA());
            }
            if (kh == LineSegment.Intersection.PARALLEL_END_OF_S1_CONTAINS_START_OF_S2_341) {
                i_intersection++;
                intersection[i_intersection].set(s.getB());
            }
            if (kh == LineSegment.Intersection.PARALLEL_END_OF_S1_CONTAINS_END_OF_S2_351) {
                i_intersection++;
                intersection[i_intersection].set(s.getA());
            }

            if (kh == LineSegment.Intersection.PARALLEL_S1_INCLUDES_S2_361) {
                i_intersection++;
                intersection[i_intersection].set(s.getA());
                i_intersection++;
                intersection[i_intersection].set(s.getB());
            }
            if (kh == LineSegment.Intersection.PARALLEL_S1_INCLUDES_S2_362) {
                i_intersection++;
                intersection[i_intersection].set(s.getA());
                i_intersection++;
                intersection[i_intersection].set(s.getB());
            }

            if (kh == LineSegment.Intersection.PARALLEL_S1_END_OVERLAPS_S2_START_371) {
                i_intersection++;
                intersection[i_intersection].set(s.getA());
            }
            if (kh == LineSegment.Intersection.PARALLEL_S1_END_OVERLAPS_S2_START_371) {
                i_intersection++;
                intersection[i_intersection].set(s.getB());
            }
            if (kh == LineSegment.Intersection.PARALLEL_S1_START_OVERLAPS_S2_END_373) {
                i_intersection++;
                intersection[i_intersection].set(s.getB());
            }
            if (kh == LineSegment.Intersection.PARALLEL_S1_START_OVERLAPS_S2_START_374) {
                i_intersection++;
                intersection[i_intersection].set(s.getA());
            }

        }

        for (int i = 1; i <= i_intersection; i++) {
            nbox.container_i_smallest_first(new WeightedValue<>(intersection[i], intersection[i].distance(s0.getA())));
        }

        // 0, when all of the line segment s0 exists outside the convex polygon (the boundary line is not considered inside)
        // When the line segment s0 exists both outside the convex polygon and across the boundary line 1,
        // When the line segment s0 exists inside the convex polygon, the boundary line, and the outside 2,
        // When all of the line segments s0 are on the boundary of the convex polygon 3,
        // When the line segment s0 exists both inside the convex polygon and across the boundary line 4,
        // When all of the line segment s0 exists inside the convex polygon (the boundary line is not considered to be inside) 5,

        // naibu (Temp) {// 0 = external, 1 = boundary, 2 = internal

        boolean outside = false;
        boolean border = false;
        boolean inside = false;

        Intersection i_nai;

        for (int i = 0; i < nbox.getTotal(); i++) {

            i_nai = inside(nbox.getValue(i));
            if (i_nai == Intersection.OUTSIDE) {
                outside = true;
            }
            if (i_nai == Intersection.BORDER) {
                border = true;
            }
            if (i_nai == Intersection.BORDER) {
                inside = true;
            }

            if (i != nbox.getTotal() - 1) {
                i_nai = inside(OritaCalc.midPoint(nbox.getValue(i), nbox.getValue(i + 1)));
                if (i_nai == Intersection.OUTSIDE) {
                    outside = true;
                }
                if (i_nai == Intersection.BORDER) {
                    border = true;
                }
                if (i_nai == Intersection.INSIDE) {
                    inside = true;
                }
            }
        }


        return Polygon.Intersection.create(outside, border, inside);
    }

    // Even a part of the line segment s0 is inside the convex polygon (the boundary line is not regarded as the inside)
    // Returns 1 if present, 0 otherwise
    public boolean convex_inside(LineSegment s0) {
        int iflag = 0;//
        LineSegment.Intersection kh; //For storing the value of oc.line_intersect_decide (s0, s)

        LineSegment s = new LineSegment();
        for (int i = 1; i <= vertexCount - 1; i++) {
            s.set(vertices[i], vertices[i + 1]); //線分
            kh = OritaCalc.determineLineSegmentIntersection(s0, s);
            if (kh == LineSegment.Intersection.INTERSECTS_1) {
                return true;
            }
            if (kh == LineSegment.Intersection.INTERSECT_AT_POINT_4) {
                return false;
            }
            if (kh == LineSegment.Intersection.INTERSECT_AT_POINT_S1_5) {
                return false;
            }
            if (kh == LineSegment.Intersection.INTERSECT_AT_POINT_S2_6) {
                return false;
            }
            if (kh.getState() >= 30) {
                return false;
            }
            if (kh.getState() >= 20) {
                iflag = iflag + 1;
            }// This is actually executed when kh is 20 or more and less than 30.
        }

        s.set(vertices[vertexCount], vertices[1]); //Line segment
        kh = OritaCalc.determineLineSegmentIntersection(s0, s);
        if (kh == LineSegment.Intersection.INTERSECTS_1) {
            return true;
        }
        if (kh == LineSegment.Intersection.INTERSECT_AT_POINT_4) {
            return false;
        }
        if (kh == LineSegment.Intersection.INTERSECT_AT_POINT_S1_5) {
            return false;
        }
        if (kh == LineSegment.Intersection.INTERSECT_AT_POINT_S2_6) {
            return false;
        }
        if (kh.isParallel()) {
            return false;
        }
        if (kh.isEndpointIntersection()) {
            iflag = iflag + 1;
        }

        if (iflag == 0) {
            return inside(new Point(0.5, s0.getA(), 0.5, s0.getB())) == Intersection.INSIDE;
        }

        if (iflag == 1) {
            return inside(new Point(0.5, s0.getA(), 0.5, s0.getB())) == Intersection.INSIDE;
        }

        if (iflag == 2) {
            if (inside(new Point(0.5, s0.getA(), 0.5, s0.getB())) == Intersection.INSIDE) {
                return true;
            }
            if (inside(s0.getA()) == Intersection.INSIDE) {
                return true;
            }
            return inside(s0.getB()) == Intersection.INSIDE;
        }

        if (iflag == 3) {
            return true;
        }
        return iflag == 4;//In reality, there should be no situation where you can reach this point.
    }

    public boolean totu_boundary_inside(Circle c) {
        LineSegment s = new LineSegment();
        for (int i = 1; i <= vertexCount - 1; i++) {
            s.set(vertices[i], vertices[i + 1]); //線分
            if (OritaCalc.determineLineSegmentDistance(c.determineCenter(), s) <= c.getRadius()) {
                if ((OritaCalc.distance(s.getA(), c.determineCenter()) >= c.getRadius()) || (OritaCalc.distance(s.getA(), c.determineCenter()) >= c.getRadius())) {
                    return true;
                }
            }
        }

        s.set(vertices[vertexCount], vertices[1]); //線分
        if (OritaCalc.determineLineSegmentDistance(c.determineCenter(), s) <= c.getRadius()) {
            if ((OritaCalc.distance(s.getA(), c.determineCenter()) >= c.getRadius()) || (OritaCalc.distance(s.getA(), c.determineCenter()) >= c.getRadius())) {
                return true;
            }
        }

        return totu_boundary_inside(new LineSegment(c.determineCenter(), c.determineCenter()));
    }

    // Even a part of the line segment s0 is inside the convex polygon (the boundary line is also regarded as the inside)
    // Returns 1 if present, 0 otherwise
    public boolean totu_boundary_inside(LineSegment s0) {// Returns 1 if even part of s0 touches a polygon.
        LineSegment.Intersection kh; //oc.line_intersect_decide(s0,s)の値の格納用

        LineSegment s = new LineSegment();
        for (int i = 1; i <= vertexCount - 1; i++) {
            s.set(vertices[i], vertices[i + 1]); //線分
            kh = OritaCalc.determineLineSegmentIntersection(s0, s);
            if (kh != LineSegment.Intersection.NO_INTERSECTION_0) {
                return true;
            }
        }

        s.set(vertices[vertexCount], vertices[1]); //線分
        kh = OritaCalc.determineLineSegmentIntersection(s0, s);
        if (kh != LineSegment.Intersection.NO_INTERSECTION_0) {
            return true;
        }

        return inside(new Point(0.5, s0.getA(), 0.5, s0.getB())) == Intersection.INSIDE;
    }

    //A function that determines if a point is inside this polygon (true) or not (false)----------------------------------
    public Intersection inside(Point p) {      //0 = outside, 1 = boundary, 2 = inside
        LineSegment s = new LineSegment();
        LineSegment sq = new LineSegment();
        Point q = new Point();

        int kousakaisuu = 0;
        int jyuuji_kousakaisuu;
        boolean appropriate = false;
        double rad = 0.0;//A radian used to make sure that there is an external point.

        //First, it is determined whether the point p is on the boundary line of the polygon.
        for (int i = 1; i <= vertexCount - 1; i++) {
            s.set(vertices[i], vertices[i + 1]);
            if (OritaCalc.determineLineSegmentDistance(p, s) < 0.01) {
                return Intersection.BORDER;
            }
        }
        s.set(vertices[vertexCount], vertices[1]);
        if (OritaCalc.determineLineSegmentDistance(p, s) < 0.01) {
            return Intersection.OUTSIDE;
        }

        //点pが多角形の境界線上に無い場合、内部にあるか外部にあるか判定する

        while (!appropriate) {   //交差回数が0または、すべての交差が十字路型なら適切。
            kousakaisuu = 0;
            jyuuji_kousakaisuu = 0;

            //確実に外部にある点qと、点pで線分を作る。
            rad += 1.0;
            q.set((100000.0 * Math.cos(rad)), (100000.0 * Math.sin(rad))); //<<<<<<<<<<<<<<<<<<

            sq.set(p, q);

            for (int i = 1; i <= vertexCount - 1; i++) {
                s.set(vertices[i], vertices[i + 1]); //線分
                if (OritaCalc.determineLineSegmentIntersection(sq, s, 0.0, 0.0).isIntersection()) {
                    kousakaisuu++;
                }
                if (OritaCalc.determineLineSegmentIntersection(sq, s, 0.0, 0.0) == LineSegment.Intersection.INTERSECTS_1) {
                    jyuuji_kousakaisuu++;
                }
            }

            s.set(vertices[vertexCount], vertices[1]); //線分
            if (OritaCalc.determineLineSegmentIntersection(sq, s, 0.0, 0.0).isIntersection()) {
                kousakaisuu++;
            }
            if (OritaCalc.determineLineSegmentIntersection(sq, s, 0.0, 0.0) == LineSegment.Intersection.INTERSECTS_1) {
                jyuuji_kousakaisuu++;
            }

            if (kousakaisuu == jyuuji_kousakaisuu) {
                appropriate = true;
            }
        }

        if (kousakaisuu % 2 == 1) {
            return Intersection.INSIDE;
        } //交差回数が奇数なら内部

        return Intersection.OUTSIDE;
    }

    //Find the area when the vertex coordinates of the polygon are clockwise (x1, y1), (x2, y2), ..., (xn, yn).
    public double calculateArea() {
        double area = 0.0;

        area = area + (vertices[vertexCount].getX() - vertices[2].getX()) * vertices[1].getY();
        for (int i = 2; i <= vertexCount - 1; i++) {
            area = area + (vertices[i - 1].getX() - vertices[i + 1].getX()) * vertices[i].getY();
        }
        area = area + (vertices[vertexCount - 1].getX() - vertices[1].getX()) * vertices[vertexCount].getY();
        area = -0.5 * area;

        return area;
    }

    //Find the distance between a point and a polygon (the minimum value of the distance between a point and a point on the boundary of the polygon)
    public double findDistance(Point tn) {
        double distance;
        distance = OritaCalc.determineLineSegmentDistance(tn, vertices[vertexCount], vertices[1]);
        for (int i = 1; i <= vertexCount - 1; i++) {
            if (OritaCalc.determineLineSegmentDistance(tn, vertices[i], vertices[i + 1]) < distance) {
                distance = OritaCalc.determineLineSegmentDistance(tn, vertices[i], vertices[i + 1]);
            }
        }

        return distance;
    }

    //Find the points inside the polygon
    public Point insidePoint_find() {
        Point tn = new Point();
        Point tr = new Point();
        double distance = -10.0;

        for (int i = 2; i <= vertexCount - 1; i++) {
            tn.set(OritaCalc.center(vertices[i - 1], vertices[i], vertices[i + 1]));
            if ((distance < findDistance(tn)) && (inside(tn) == Intersection.INSIDE)) {
                distance = findDistance(tn);
                tr.set(tn);
            }
        }
        //
        tn.set(OritaCalc.center(vertices[vertexCount - 1], vertices[vertexCount], vertices[1]));
        if ((distance < findDistance(tn)) && (inside(tn) == Intersection.INSIDE)) {
            distance = findDistance(tn);
            tr.set(tn);
        }
        //
        tn.set(OritaCalc.center(vertices[vertexCount], vertices[1], vertices[2]));
        if ((distance < findDistance(tn)) && (inside(tn) == Intersection.INSIDE)) {
            distance = findDistance(tn);
            tr.set(tn);
        }
        //
        return tr;
    }

    public double getXMin() {
        double r;
        r = vertices[1].getX();
        for (int i = 2; i <= vertexCount; i++) {
            if (r > vertices[i].getX()) {
                r = vertices[i].getX();
            }
        }
        return r;
    }//多角形のx座標の最小値を求める

    public double getXMax() {
        double r;
        r = vertices[1].getX();
        for (int i = 2; i <= vertexCount; i++) {
            if (r < vertices[i].getX()) {
                r = vertices[i].getX();
            }
        }
        return r;
    }//多角形のx座標の最大値を求める

    public double getYMin() {
        double r;
        r = vertices[1].getY();
        for (int i = 2; i <= vertexCount; i++) {
            if (r > vertices[i].getY()) {
                r = vertices[i].getY();
            }
        }
        return r;
    }//多角形のy座標の最小値を求める

    public double getYMax() {
        double r;
        r = vertices[1].getY();
        for (int i = 2; i <= vertexCount; i++) {
            if (r < vertices[i].getY()) {
                r = vertices[i].getY();
            }
        }
        return r;
    }//多角形のy座標の最大値を求める

    /**
     * A Point or LineSegment intersecting a Polygon
     */
    public enum Intersection {
        /**
         * When all of the line segment s0 exists outside the convex polygon (the boundary line is not considered inside)
         */
        OUTSIDE,
        /**
         * When all of the line segments s0 are on the boundary of the convex polygon
         */
        BORDER,
        /**
         * When all of the line segment s0 exists inside the convex polygon (the boundary line is not considered to be inside)
         */
        INSIDE,
        /**
         * When the line segment s0 exists both outside the convex polygon and across the boundary line
         */
        OUTSIDE_BORDER,
        /**
         * When the line segment s0 exists inside the convex polygon, the boundary line, and the outside
         */
        OUTSIDE_BORDER_INSIDE,
        /**
         * When the line segment s0 exists both inside the convex polygon and across the boundary line
         */
        BORDER_INSIDE,
        ;

        public static Intersection create(boolean outside, boolean border, boolean inside) {
            if (outside && border && inside) return OUTSIDE_BORDER_INSIDE;
            if (outside && border) return OUTSIDE_BORDER;
            if (border && inside) return BORDER_INSIDE;
            if (outside) return OUTSIDE;
            if (inside) return INSIDE;
            if (border) return BORDER;

            throw new IllegalArgumentException();
        }
    }
}
