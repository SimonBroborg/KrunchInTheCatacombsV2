package main;

import entity.Player;
import map.Tile;
import map.TileMap;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 *
 */

@SuppressWarnings("MagicNumber")
public class FlashLight {
    // The range of the flashlight
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
    //private List<Float> uniAngles;
    private List<Segment> segments;
    //private List<Point> points;
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
        intersections = new ArrayList<>();

        poly = new Polygon(new int[]{0}, new int[]{0}, 0);

        // How broad the flashlight is.
        offsetAngle = 10;
    }

    public void update() {
        // reset the lists
        segments.clear();
        intersections.clear();


        // set to the players position
        x = player.getX() + (int) tm.getX();
        y = player.getY() + (int) tm.getY();

        targetAngle = (getAngle(new Point(targetX, targetY), new Point(x, y)));

        // Create segments from tiles
        setSegments();

        // The poly which will be the flashlight
        poly = new Polygon();

        // set the points to draw the polygon
        for (int i = 0; i < intersections.size(); i++) {
            poly.addPoint(intersections.get(i).x, intersections.get(i).y);
        }
        // One point has to be the players position
        poly.addPoint(x, y);

    }


    /*

     */
    private void setSegments() {
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
                    /*if (x < upperLeft.x && y < lowerLeft.y && y > upperLeft.y) {
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
                    }*/

                    // Upper left
                    if (x < upperRight.x && y < lowerLeft.y) {
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
        setIntersections();
    }

    /*
    Check if the angle collides with a segment. Returns true or false.
     */
    public boolean checkIntersection(float angle) {
        // Calculate dx & dy from angle

        // Find closest intersection
        Point closestIntersection = null;
        double closestDistance = 0;
        for (int j = 0; j < segments.size(); j++) {
            Segment segment = segments.get(j);

            if (closestIntersection != null)
                closestDistance = Math.hypot(closestIntersection.x - x, closestIntersection.y - y);
            Point intersect = getIntersection(new Segment(new Point(x + (int) (-RANGE * Math.cos(Math.toRadians(angle))),
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


    /*
    Sets the intersection points
     */
    public void setIntersections() {
        /*for (int i = 0; i < uniAngles.size(); i++) {
            checkIntersection(uniAngles.get(i));
        }*/

        for (double i = -offsetAngle; i < offsetAngle; i += (float)offsetAngle * 2 / 20) {
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
        // Sort the intersections based on their distance clockwise from the outer angle
        for (int i = 0; i < intersections.size(); i++) {
            for (int j = 1; j < intersections.size(); j++) {
                // Swaps the position of the intersections based on their angle ( smallest first )
                if (normalAbsoluteAngleDegrees(getAngle(intersections.get(j - 1), new Point(x, y)) + offset) <
                        normalAbsoluteAngleDegrees(getAngle(intersections.get(j), new Point(x, y)) + offset)) {
                    Collections.swap(intersections, j, j - 1);
                }
            }
        }
    }


    /*
    Get the angle between two points
    Returns a value between 0 - 360 degrees
     */
    public double getAngle(Point p1, Point p2) {
        double angle = Math.toDegrees(Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        return normalAbsoluteAngleDegrees(angle);
    }


    /*
    Makes sure an angle is positive between 0 - 360 degrees
     */
    public static double normalAbsoluteAngleDegrees(double angle) {
        angle %= 360;
        if (angle >= 0) return angle;
        else return angle + 360;
    }


    /*
    Checks if two line segments intersect and returns the intersection point if so
    */
    public Point getIntersection(Segment s1, Segment s2) {
        // Get the point if the lines intersect
        if (s1.intersects(s2)) {
            return findIntersection(s1.getLine(), s2.getLine());
        }
        return null;
    }


    /*
    Get the point where two lines intersect
     */
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


    /*
    Draw the intersection lines and intersection points
     */
    @SuppressWarnings("unused")
    private void drawIntersections(Graphics2D g2d) {
        g2d.setColor(Color.red);
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i < intersections.size(); i++) {
            Point intersect = intersections.get(i);
            g2d.drawLine(x, y, intersect.x, intersect.y);
            g2d.fillOval(intersect.x - 5, intersect.y - 5, 10, 10);
        }
    }


    /*
    Draw the flashlight
     */
    public void draw(Graphics2D g2d) {
        // This creates the "fade-out" flashlight effect
        float radius = RANGE - 20;
        float[] dist = new float[]{0.0f, 1.0f};
        Color[] colors = new Color[]{new Color(0.0f, 0.0f, 0.0f, 0.0f), Color.black};
        Point2D center = new Point2D.Float(x, y);
        RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
        g2d.setPaint(p);

        // Sets the opacity of the black foreground which covers the screen
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.90f));

        // The flashlight light
        g2d.fillPolygon(poly);

        g2d.setColor(Color.BLACK);

        // Sets an area which is the entire frame except the flashlight
        Area outer = new Area(
                new Rectangle(0, 0, GameComponent.WIDTH * GameComponent.SCALE, GameComponent.HEIGHT * GameComponent.SCALE));
        outer.subtract(new Area(poly));
        g2d.fill(outer);

        // Reset the alpha-channel
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        //drawIntersections(g2d);
    }


    /*
    Setters and getters
     */
    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }
}

    /*
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
     */