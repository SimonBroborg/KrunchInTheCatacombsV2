package Main;

import Entity.Player;
import TileMap.Tile;
import TileMap.TileMap;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 *
 */

@SuppressWarnings("OverlyComplexMethod")
public class FlashLight {
    private static final int RANGE = 400;

    private TileMap tm;

    private int x;
    private int y;
    private int targetX;
    private int targetY;

    private Polygon poly;

    private int offsetAngle;
    // The angle to which the cursor is pointing
    private double targetAngle;

    // The angles in which the lines should be checked
    private List<Float> uniAngles;
    private List<Segment> segments;
    private List<Point> points;
    private List<Point> intersections;

    private Player player;

    public FlashLight(TileMap tm, Player player) {
        this.tm = tm;
        this.player = player;
        x = player.getX() + (int) tm.getX();
        y = player.getY() + (int) tm.getY();
        targetX = 0;
        targetY = 0;

        segments = new ArrayList<>();
        uniAngles = new ArrayList<>();
        points = new ArrayList<>();
        intersections = new ArrayList<>();

        poly = new Polygon(new int[]{0}, new int[]{0}, 0);

        // How broad the flashlight is.
        offsetAngle = 10;
    }

    public void update() {
        // set to the players position
        x = player.getX() + (int) tm.getX();
        y = player.getY() + (int) tm.getY();

        targetAngle = (getAngle(new Point(targetX, targetY), new Point(x, y)));

        // Create segments from tiles
        setSegments();

    }

    public static double normalAbsoluteAngleDegrees(double angle) {
        return (angle %= 360) >= 0 ? angle : (angle + 360);
    }

    public void setSegments() {
        segments = new ArrayList<>();
        // create segments from each tile
        for (Tile[] tiles : tm.getTiles()) {
            for (Tile tile : tiles) {
                Rectangle rect = tile.getRectangle();
                // If the flashlight can collide with the tile
                if (tile.isSolid() && player.inRange((int) tile.getX(), (int) tile.getY(), RANGE + 10)) {
                    // Get the bounding box rect from the tile


                    Point upperLeft = new Point(rect.x + (int) tm.getX(), rect.y + (int) tm.getY());
                    Point lowerLeft = new Point(rect.x + (int) tm.getX(), rect.y + (int) tm.getY() + rect.height);
                    Point upperRight = new Point(rect.x + (int) tm.getX() + rect.width, rect.y + (int) tm.getY());
                    Point lowerRight = new Point(rect.x + (int) tm.getX() + rect.width, rect.y + (int) tm.getY() + rect.height);

                    Segment left = new Segment(upperLeft, lowerLeft);
                    Segment top = new Segment(upperLeft, upperRight);
                    Segment bottom = new Segment(lowerLeft, lowerRight);
                    Segment right = new Segment(upperRight, lowerRight);

                    // Only left
                    if (x < upperLeft.x && y < lowerLeft.y && y > upperLeft.y) {
                        segments.add(left);
                    }
                    // Only right
                    else if (x > upperRight.x && y < lowerRight.y && y > upperRight.y) {
                        segments.add(right);
                    }
                    // Only top
                    else if (y < upperLeft.y && x > upperLeft.x && x < upperRight.x) {
                        segments.add(top);
                    }
                    // Only bottom
                    else if (y > lowerLeft.y && x > lowerLeft.x && x < lowerRight.x) {
                        segments.add(bottom);
                    }
                    // Upper left
                    else if (x < upperRight.x && y < lowerLeft.y) {
                        segments.add(top);
                        segments.add(left);
                    }
                    // Upper right
                    else if (x > upperLeft.x && y < lowerRight.y) {
                        segments.add(top);
                        segments.add(right);
                    }
                    // Lower right
                    else if (x > lowerLeft.x && y > upperRight.y) {
                        segments.add(bottom);
                        segments.add(right);
                    }
                    //Lower left
                    else if (x < upperLeft.x && y > lowerRight.y) {
                        segments.add(bottom);
                        segments.add(left);
                    }
                }
            }
        }

        setPoints();
        setAngles();
        setIntersections();
    }

    public void setPoints() {
        points = new ArrayList<>();
        // Sets points in the points of the segments
        for (int i = 0; i < segments.size(); i++) {
            Segment segment = segments.get(i);
            points.add(segment.getStart());
            points.add(segment.getEnd());
        }
    }

    public void setAngles() {
        uniAngles = new ArrayList<>();
        // creates the angles between a x and y position and the set points
        for (int i = 0; i < points.size(); i++) {
            Point uniquePoint = points.get(i);
            double angle = (getAngle(new Point(uniquePoint.x, uniquePoint.y), new Point(x, y)));
            angle = Math.toRadians(angle);
            uniAngles.add(Float.valueOf((float) angle));
            uniAngles.add(Float.valueOf((float) (angle - 0.00001)));
            uniAngles.add(Float.valueOf((float) (angle + 0.00001)));
        }
    }

    // Check if the angle collides with a segment. Returns true or false.
    public boolean checkIntersection(float angle) {
        // Calculate dx & dy from angle

        // Find closest intersection
        Point closestIntersection = null;
        double closestDistance = 0;
        for (int j = 0; j < segments.size(); j++) {
            Segment segment = segments.get(j);

            if (closestIntersection != null)
                closestDistance = Math.hypot(closestIntersection.x - x, closestIntersection.y - y);
            Point intersect = simonInter(new Segment(new Point(x + (int) (-RANGE * Math.cos(Math.toRadians(angle))),
                    y + (int) (-RANGE * Math.sin(Math.toRadians(angle)))), new Point(x, y)), segment);
            if (intersect == null) {
                continue;
            }
            if (closestIntersection == null || Math.hypot(x - intersect.x, y - intersect.y) < closestDistance) {
                closestIntersection = intersect;
            }
        }

        if (closestIntersection != null && closestDistance < RANGE + 10 && getAngle(new Point(closestIntersection.x, closestIntersection.y), new Point(x, y)) <= targetAngle + offsetAngle + 10 &&
                getAngle(new Point(closestIntersection.x, closestIntersection.y), new Point(x, y)) >= targetAngle - offsetAngle - 10) {
            intersections.add(closestIntersection);
            return true;
        }
        return false;
    }

    public void setIntersections() {
        intersections = new ArrayList<>();

        /*for (int i = 0; i < uniAngles.size(); i++) {
            checkIntersection(uniAngles.get(i));
        }*/

        for (double i = -offsetAngle; i < offsetAngle; i += offsetAngle * 2 / 20) {
            double rounded = (Math.pow(Math.abs(i * 0.5), 2));
            if (!checkIntersection((float) (normalAbsoluteAngleDegrees(targetAngle + i)))) {
                intersections.add(new Point(x + (int) (-(RANGE - rounded) * Math.cos(Math.toRadians((normalAbsoluteAngleDegrees(targetAngle + i))))),
                        y + (int) (-(RANGE - rounded) * Math.sin(Math.toRadians((normalAbsoluteAngleDegrees(targetAngle + i)))))));
            }
        }



        // How much the angles should turn when sorting them.
        int offset = 0;
        // Fixes a problem when the angle goes between 0 and 360 degrees
        if (normalAbsoluteAngleDegrees(targetAngle - offsetAngle) > normalAbsoluteAngleDegrees(targetAngle + offsetAngle)) {
            offset = 180;
        }
        // Sort the angles of the intersections based on the outer angle
        for (int i = 0; i < intersections.size(); i++) {
            for (int j = 1; j < intersections.size(); j++) {
                if (normalAbsoluteAngleDegrees(getAngle(intersections.get(j - 1), new Point(x, y)) + offset) <
                        normalAbsoluteAngleDegrees(getAngle(intersections.get(j), new Point(x, y)) + offset)) {
                    Collections.swap(intersections, j, j - 1);
                }
            }
        }
    }

    public void draw(Graphics2D g2d) {

        poly = new Polygon();

        // set the points to draw the polygon
        for (int i = 0; i < intersections.size(); i++) {
            poly.addPoint(intersections.get(i).x, intersections.get(i).y);
        }
        poly.addPoint(x, y);

        /*for (int i = 0; i < segments.size(); i++) {
            segments.get(i).draw(g2d);
        }*/

        Point2D center;
        float[] dist;
        Color[] colors;

        RadialGradientPaint p;
        float radius = RANGE - 20;

        dist = new float[]{0.0f, 1.0f};
        colors = new Color[]{new Color(0.0f, 0.0f, 0.0f, 0.0f), Color.black};

        center = new Point2D.Float(x, y);

        p = new RadialGradientPaint(center, radius, dist, colors);
        g2d.setPaint(p);


        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));
        g2d.fillPolygon(poly);

        Area outer = new Area(
                new Rectangle(0, 0, GameComponent.WIDTH * GameComponent.SCALE, GameComponent.HEIGHT * GameComponent.SCALE));
        outer.subtract(new Area(poly));

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));
        g2d.setColor(Color.BLACK);
        g2d.fill(outer);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        // g2d.draw(poly);

        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i < intersections.size(); i++) {
            Point intersect = intersections.get(i);
            //g2d.setColor(Color.red);
            //g2d.drawLine(x, y, intersect.x, intersect.y);
            //g2d.fillOval((int) intersect.x - 5, (int) intersect.y - 5, 10, 10);
        }
    }


    // get the angle between the
    public double getAngle(Point p1, Point p2) {
        double angle = Math.toDegrees(Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        return normalAbsoluteAngleDegrees(angle);
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point simonInter(Segment s1, Segment s2) {
        if (s1.intersects(s2)) {
            return findIntersection(s1.getLine(), s2.getLine());
        }
        return null;
    }

    private static Point findIntersection(Line2D l1, Line2D l2) {
        double a1 = l1.getY2() - l1.getY1();
        double b1 = l1.getX1() - l1.getX2();
        double c1 = a1 * l1.getX1() + b1 * l1.getY1();

        double a2 = l2.getY2() - l2.getY1();
        double b2 = l2.getX1() - l2.getX2();
        double c2 = a2 * l2.getX1() + b2 * l2.getY1();

        double delta = a1 * b2 - a2 * b1;
        return new Point((int) ((b2 * c1 - b1 * c2) / delta), (int) ((a1 * c2 - a2 * c1) / delta));
    }

    public Point getIntersection(double tx, double ty, Segment segment) {

        // ray in parametric: Point + Direction * T1
        float r_px = x;
        float r_py = y;
        double r_dx = tx - x;
        double r_dy = ty - y;

        // segment in parametric: Point + Direction * T1
        float s_px = segment.getStart().x;
        float s_py = segment.getStart().y;
        float s_dx = segment.getEnd().x - segment.getStart().x;
        float s_dy = segment.getEnd().y - segment.getStart().y;

        double r_mag = Math.sqrt(r_dx * r_dx + r_dy * r_dy);
        double s_mag = Math.sqrt(s_dx * s_dx + s_dy * s_dy);
        if (r_dx / r_mag == s_dx / s_mag && r_dy / r_mag == s_dy / s_mag) {
            // Unit vectors are the same.
            return null;
        }

        double T2 = (r_dx * (s_py - r_py) + r_dy * (r_px - s_px)) / (s_dx * r_dy - s_dy * r_dx);
        double T1 = (s_px + s_dx * T2 - r_px) / r_dx;

        // Must be within parametric whatevers for RAY/SEGMENT
        if (T1 < 0) return null;
        if (T2 < 0 || T2 > 1) return null;

        return new Point((int) (r_px + r_dx * T1), (int) (r_py + r_dy * T1));
    }


    public boolean onSegment(Point p, Point q, Point r) {
        return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);

    }

    public float orientation(Point p, Point q, Point r) {
        // https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
        float val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;  // colinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    public Point checkLineIntersect(Point p1, Point q1, Point p2, Point q2) {
        // Find the four orientations needed for general and
        // special cases
        float o1 = orientation(p1, q1, p2);
        float o2 = orientation(p1, q1, q2);
        float o3 = orientation(p2, q2, p1);
        float o4 = orientation(p2, q2, q1);

        double a1 = q1.y - p1.y;
        double b1 = p1.x - q1.x;
        double c1 = a1 * (p1.x) + b1 * (p1.y);

        // Line CD represented as a2x + b2y = c2
        double a2 = q2.y - p2.y;
        double b2 = p2.x - q2.x;
        double c2 = a2 * (p2.x) + b2 * (p2.y);

        double determinant = a1 * b2 - a2 * b1;

        double x = (b2 * c1 - b1 * c2) / determinant;
        double y = (a1 * c2 - a2 * c1) / determinant;

        Point point = new Point((int) x, (int) y);

        // General case
        if (o1 != o2 && o3 != o4) return point;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return point;

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return point;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return point;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return point;

        return null; // Doesn't fall in any of the above cases
    }

    public static Point detect(Segment l1, Segment l2) {
        double dyline1, dxline1;
        double dyline2, dxline2, e, f;
        double x1line1, y1line1, x2line1, y2line1;
        double x1line2, y1line2, x2line2, y2line2;
        if (!intersects(l1, l2)) {
            return null;
        }
        /*
         * first, check to see if the segments intersect by parameterization on
         * s and t; if s and t are both between [0,1], then the segments
         * intersect
         */
        x1line1 = (double) l1.getStart().x;
        y1line1 = (double) l1.getStart().y;
        x2line1 = (double) l1.getEnd().x;
        y2line1 = (double) l1.getEnd().y;
        x1line2 = (double) l2.getStart().x;
        y1line2 = (double) l2.getStart().y;
        x2line2 = (double) l2.getEnd().x;
        y2line2 = (double) l2.getEnd().y;

        /*
         * check to see if the segments have any endpoints in common. If they
         * do, then return the endpoints as the intersection point
         */
        if ((x1line1 == x1line2) && (y1line1 == y1line2)) {
            return (new Point((int) x1line1, (int) y1line1));
        }

        if ((x1line1 == x2line2) && (y1line1 == y2line2)) {
            return (new Point((int) x1line1, (int) y1line1));
        }

        if ((x2line1 == x1line2) && (y2line1 == y1line2)) {
            return (new Point((int) x2line1, (int) y2line1));
        }

        if ((x2line1 == x2line2) && (y2line1 == y2line2)) {
            return (new Point((int) x2line1, (int) y2line1));
        }

        dyline1 = -(y2line1 - y1line1);
        dxline1 = x2line1 - x1line1;
        dyline2 = -(y2line2 - y1line2);
        dxline2 = x2line2 - x1line2;

        e = -(dyline1 * x1line1) - (dxline1 * y1line1);
        f = -(dyline2 * x1line2) - (dxline2 * y1line2);

        /*
         * compute the intersection point using ax+by+e = 0 and cx+dy+f = 0 If
         * there is more than 1 intersection point between two lines,
         */
        if ((dyline1 * dxline2 - dyline2 * dxline1) == 0)
            return null;

        return (new Point((int) (-(e * dxline2 - dxline1 * f) / (dyline1 * dxline2 - dyline2 * dxline1)),
                (int) (-(dyline1 * f - dyline2 * e) / (dyline1 * dxline2 - dyline2 * dxline1))));
    }


    private static boolean intersects(Segment l1, Segment l2) {
        Point start1 = l1.getStart();
        Point end1 = l1.getEnd();
        Point start2 = l2.getStart();
        Point end2 = l2.getEnd();
        // First find Ax+By=C values for the two lines
        double A1 = end1.y - start1.y;
        double B1 = start1.x - end1.x;
        double C1 = A1 * start1.x + B1 * start1.y;

        double A2 = end2.y - start2.y;
        double B2 = start2.x - end2.x;
        double C2 = A2 * start2.x + B2 * start2.y;

        double det = (A1 * B2) - (A2 * B1);

        if (det == 0) {
            // Lines are either parallel, are collinear (the exact same
            // segment), or are overlapping partially, but not fully
            // To see what the case is, check if the endpoints of one line
            // correctly satisfy the equation of the other (meaning the two
            // lines have the same y-intercept).
            // If no endpoints on 2nd line can be found on 1st, they are
            // parallel.
            // If any can be found, they are either the same segment,
            // overlapping, or two segments of the same line, separated by some
            // distance.
            // Remember that we know they share a slope, so there are no other
            // possibilities

            // Check if the segments lie on the same line
            // (No need to check both points)
            if ((A1 * start2.x) + (B1 * start2.y) == C1) {
                // They are on the same line, check if they are in the same
                // space
                // We only need to check one axis - the other will follow
                if ((Math.min(start1.x, end1.x) < start2.x) && (Math.max(start1.x, end1.x) > start2.x))
                    return true;

                // One end point is ok, now check the other
                if ((Math.min(start1.x, end1.x) < end2.x) && (Math.max(start1.x, end1.x) > end2.x))
                    return true;

                // They are on the same line, but there is distance between them
                return false;
            }

            // They are simply parallel
            return false;
        } else {
            // Lines DO intersect somewhere, but do the line segments intersect?
            double x = (B2 * C1 - B1 * C2) / det;
            double y = (A1 * C2 - A2 * C1) / det;

            // Make sure that the intersection is within the bounding box of
            // both segments
            if ((x >= Math.min(start1.x, end1.x) && x <= Math.max(start1.x, end1.x))
                    && (y >= Math.min(start1.y, end1.y) && y <= Math.max(start1.y, end1.y))) {
                // We are within the bounding box of the first line segment,
                // so now check second line segment
                if ((x >= Math.min(start2.x, end2.x) && x <= Math.max(start2.x, end2.x))
                        && (y >= Math.min(start2.y, end2.y) && y <= Math.max(start2.y, end2.y))) {
                    // The line segments do intersect
                    return true;
                }
            }

            // The lines do intersect, but the line segments do not
            return false;
        }
    }

}