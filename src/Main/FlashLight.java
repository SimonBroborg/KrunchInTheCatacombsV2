package Main;

import Entity.Player;
import TileMap.Tile;
import TileMap.TileMap;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */

@SuppressWarnings("OverlyComplexMethod")
public class FlashLight {
    private static final int RANGE = 300;

    private TileMap tm;

    private int x;
    private int y;
    private int targetX;
    private int targetY;

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

        // How broad the flashlight is.
        offsetAngle = 20;
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
                // If the flashlight can collide with the tile
                if (tile.isSolid() && player.inRange((int) tile.getX(), (int) tile.getY(), RANGE + 50)) {
                    // Get the bounding box rect from the tile
                    Rectangle rect = tile.getRectangle();

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

    public void setIntersections() {
        intersections = new ArrayList<>();
        for (int i = 0; i < uniAngles.size(); i++) {
            float angle = uniAngles.get(i);

            // Calculate dx & dy from angle
            double dx = Math.toDegrees(Math.cos(angle));
            double dy = Math.toDegrees(Math.sin(angle));

            // Find closest intersection
            Point closestIntersection = null;
            double closestDistance = 0;
            for (int j = 0; j < segments.size(); j++) {
                Segment segment = segments.get(j);

                if (closestIntersection != null)
                    closestDistance = Math.hypot(x - closestIntersection.x, y - closestIntersection.y);

                Point intersect = checkLineIntersect(segment.getStart(), segment.getEnd(), new Point(x, y),
                        new Point((int) (targetX + dx), (int) (targetY + dy)));
                if (intersect == null) {
                    continue;
                }
                if (closestIntersection == null || Math.hypot(x - intersect.x, y - intersect.y) < closestDistance) {
                    closestIntersection = intersect;
                }
            }

            if (closestIntersection != null && closestDistance < RANGE &&
                    getAngle(new Point(closestIntersection.x, closestIntersection.y), new Point(x, y)) <
                            targetAngle + offsetAngle &&
                    getAngle(new Point(closestIntersection.x, closestIntersection.y), new Point(x, y)) > targetAngle - offsetAngle)
                intersections.add(closestIntersection);

        }


        intersections.add(new Point(x + (int) (-RANGE * Math.cos(Math.toRadians(targetAngle - offsetAngle))),
                y + (int) (-RANGE * Math.sin(Math.toRadians(targetAngle - offsetAngle)))));

        intersections.add(new Point(x + (int) (-RANGE * Math.cos(Math.toRadians(targetAngle + offsetAngle))),
                y + (int) (-RANGE * Math.sin(Math.toRadians(targetAngle + offsetAngle)))));

        intersections.add(new Point(x + (int) (-RANGE * Math.cos(Math.toRadians(targetAngle))),
                y + (int) (-RANGE * Math.sin(Math.toRadians(targetAngle)))));

        int offset = 0;
        if(normalAbsoluteAngleDegrees(targetAngle - offsetAngle) > normalAbsoluteAngleDegrees(targetAngle + offsetAngle)){
            offset = 180;
            System.out.println("YES");
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

        Polygon poly = new Polygon();

        //System.out.println("Start");
        // set the points to draw the polygon
        for (int i = 0; i < intersections.size(); i++) {
            poly.addPoint(intersections.get(i).x, intersections.get(i).y);
            //System.out.println(intersections.get(i).x + " _ " +intersections.get(i).y);
            //System.out.println((getAngle(intersections.get(i), new Point(x, y))));

        }
        poly.addPoint(x, y);

        //System.out.println("End");
        System.out.println(targetAngle);

	/*for(int i = 0; i < segments.size() ; i++){
	    segments.get(i).draw(g2d);
	}*/

	/*Point2D center;
	float[] dist;
	Color[] colors;

	RadialGradientPaint p;
	float radius = 200;

	dist = new float[] { 0.0f, 1.0f };
	colors = new Color[] { new Color(0.0f, 0.0f, 0.0f, 0.0f), Color.black };

	center = new Point2D.Float(x, y);

	p = new RadialGradientPaint(center, radius, dist, colors);
	g2d.setPaint(p);
	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));

	*/

        Area outer = new Area(
                new Rectangle(0, 0, GameComponent.WIDTH * GameComponent.SCALE, GameComponent.HEIGHT * GameComponent.SCALE));
        outer.subtract(new Area(poly));

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));
        g2d.setColor(Color.BLACK);
        g2d.fill(outer);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

       // g2d.draw(poly);

        g2d.setStroke(new BasicStroke(2));
        for (int i = 0; i < intersections.size(); i++) {
            Point intersect = intersections.get(i);
            //g2d.setColor(Color.red);
            g2d.drawLine(x, y, intersect.x, intersect.y);
            //g2d.fillOval((int) intersect.get("x") - 5, (int) intersect.get("y") - 5, 10, 10);
        }
    }


    // get the angle between the
    public double getAngle(Point p1, Point p2) {
        double angle = Math.toDegrees(Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        return normalAbsoluteAngleDegrees(angle);
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
}