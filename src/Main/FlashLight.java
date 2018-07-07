package Main;

import Entity.Player;
import TileMap.*;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;


/**
 *
 */
@SuppressWarnings("OverlyComplexMethod")
public class FlashLight {
    public static final int RANGE = 800;
    private List<int[]> segments;

    private TileMap tm;

    private List<Map<String, Integer>> intersections;

    private int x;
    private int y;
    private int targetX;
    private int targetY;

    private Player player;

    public FlashLight(TileMap tm, Player player) {
        this.tm = tm;
        this.player = player;
        segments = new ArrayList<>();
        intersections = new ArrayList<>();
        x = player.getX() + (int) tm.getX();
        y = player.getY() + (int) tm.getY();
        targetX = 0;
        targetY = 0;
    }

    public void update() {

        // set to the players position
        x = player.getX() + (int) tm.getX();
        y = player.getY() + (int) tm.getY();
    }

    public void setSegments(Graphics2D g2d) {
        segments = new ArrayList<>();


        g2d.setColor(Color.red);

        // create segments from tiles
        for (Tile[] tiles : tm.getTiles()) {
            for (Tile tile : tiles) {
                if (tile.isSolid() && player.inRange((int) tile.getX(), (int) tile.getY(), RANGE)) {
                    Rectangle rect = tile.getRectangle();
                    // Only left
                    if (x < (int) (rect.x + tm.getX()) && y < (int) (rect.y + tm.getY()) + rect.height && y > (int) (rect.y + tm.getY())) {
                        segments.add(new int[]{(int) (rect.x + tm.getX()), (int) (rect.y + tm.getY()) + rect.height, (int) (rect.x + tm.getX()), (int) (rect.y + tm.getY())});
                        // Only right
                    } else if (x > (int) (rect.x + tm.getX()) + rect.width && y < (int) (rect.y + tm.getY()) + rect.height && y > (int) (rect.y + tm.getY())) {
                        segments.add(new int[]{(int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY()), (int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY()) + rect.height});
                        // Only top
                    } else if (y < (int) (rect.y + tm.getY()) && x > (int) (rect.x + tm.getX()) && x < (int) (rect.x + tm.getX()) + rect.width) {
                        segments.add(new int[]{(int) (rect.x + tm.getX()), (int) (rect.y + tm.getY()), (int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY())});
                        // Only bottom
                    } else if (y > (int) (rect.y + tm.getY()) + rect.height && x > (int) (rect.x + tm.getX()) && x < (int) (rect.x + tm.getX()) + rect.width) {
                        segments.add(new int[]{(int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY()) + rect.height, (int) (rect.x + tm.getX()), (int) (rect.y + tm.getY()) + rect.height});
                        // Upper left
                    } else if (x < (int) (rect.x + tm.getX()) + rect.width && y < (int) (rect.y + tm.getY()) + rect.height) {
                        segments.add(new int[]{(int) (rect.x + tm.getX()), (int) (rect.y + tm.getY()), (int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY())});
                        segments.add(new int[]{(int) (rect.x + tm.getX()), (int) (rect.y + tm.getY()) + rect.height, (int) (rect.x + tm.getX()), (int) (rect.y + tm.getY())});
                    }
                    // upper right
                    else if (x > (int) (rect.x + tm.getX()) && y < (int) (rect.y + tm.getY()) + rect.height) {
                        segments.add(new int[]{(int) (rect.x + tm.getX()), (int) (rect.y + tm.getY()), (int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY())});
                        segments.add(new int[]{(int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY()), (int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY()) + rect.height});
                    }
                    // bottom right
                    else if (x > (int) (rect.x + tm.getX()) && y > (int) (rect.y + tm.getY())) {
                        segments.add(new int[]{(int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY()) + rect.height, (int) (rect.x + tm.getX()), (int) (rect.y + tm.getY()) + rect.height});
                        segments.add(new int[]{(int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY()), (int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY()) + rect.height});
                    }
                    // bottom left
                    else if (x < (int) (rect.x + tm.getX()) + rect.width && y > (int) (rect.y + tm.getY())) {
                        segments.add(new int[]{(int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY()) + rect.height, (int) (rect.x + tm.getX()), (int) (rect.y + tm.getY()) + rect.height});
                        segments.add(new int[]{(int) (rect.x + tm.getX()), (int) (rect.y + tm.getY()) + rect.height, (int) (rect.x + tm.getX()), (int) (rect.y + tm.getY())});
                    } else {
                        // top
                        segments.add(new int[]{(int) (rect.x + tm.getX()), (int) (rect.y + tm.getY()), (int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY())});
                        // right side
                        segments.add(new int[]{(int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY()), (int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY()) + rect.height});
                        // bottom
                        segments.add(new int[]{(int) (rect.x + tm.getX()) + rect.width, (int) (rect.y + tm.getY()) + rect.height, (int) (rect.x + tm.getX()), (int) (rect.y + tm.getY()) + rect.height});
                        // left side
                        segments.add(new int[]{(int) (rect.x + tm.getX()), (int) (rect.y + tm.getY()) + rect.height, (int) (rect.x + tm.getX()), (int) (rect.y + tm.getY())});
                    }
                }
            }
        }
        // The border segments
        segments.add(new int[]{0, 0, GameComponent.WIDTH * GameComponent.SCALE, 0});
        segments.add(new int[]{GameComponent.WIDTH * GameComponent.SCALE, 0, GameComponent.WIDTH * GameComponent.SCALE, GameComponent.HEIGHT * GameComponent.SCALE});
        segments.add(new int[]{GameComponent.WIDTH * GameComponent.SCALE, GameComponent.HEIGHT * GameComponent.SCALE, 0, GameComponent.HEIGHT * GameComponent.SCALE});
        segments.add(new int[]{0, GameComponent.HEIGHT * GameComponent.SCALE, 0, 0});
    }

    public void draw(Graphics2D g2d) {
        // Points where the intersections happen
        intersections = new ArrayList<>();

        // Create segments from tiles
        setSegments(g2d);

        // Sets points in the corners of the tiles
        ArrayList<int[]> points = new ArrayList<>();
        for (int[] segment : segments) {
            points.add(new int[]{segment[0], segment[1]});
            points.add(new int[]{segment[2], segment[3]});
        }

        // The angles in which the lines should be checked
        List<Float> uniAngles = new ArrayList<>();

        double targetAngle = getAngle(new Point(targetX, targetY), new Point(x, y));
        targetAngle += 360;
        uniAngles.add(Float.valueOf((float) Math.toRadians(targetAngle)));
        // draw the target ine
        //g2d.drawLine(x, y, x + (int) (-100 * Math.cos(Math.toRadians(targetAngle))), y + (int) (-100 * Math.sin(Math.toRadians(targetAngle))));
        g2d.drawLine(x, y, x + (int) (-100 * Math.cos(Math.toRadians(targetAngle + 45))), y + (int) (-100 * Math.sin(Math.toRadians(targetAngle + 45))));
        g2d.drawLine(x, y, x + (int) (-100 * Math.cos(Math.toRadians(targetAngle - 45))), y + (int) (-100 * Math.sin(Math.toRadians(targetAngle - 45))));

        // creates the angles between a x and y position and the set points
        for (int i = 0; i < points.size(); i++) {
            int[] uniquePoint = points.get(i);
            double angle = (getAngle(new Point(uniquePoint[0], uniquePoint[1]), new Point(x, y)));
            angle = Math.toRadians(angle);
            uniAngles.add(Float.valueOf((float) angle));
            uniAngles.add(Float.valueOf((float) (angle - 0.00001)));
            uniAngles.add(Float.valueOf((float) (angle + 0.00001)));
        }

        // for each angle
        for (Float uniAngle : uniAngles) {
            float angle = uniAngle;
            // Calculate dx & dy from angle
            double dx = Math.toDegrees(Math.cos(angle));
            double dy = Math.toDegrees(Math.sin(angle));


            // Find closest intersection
            Map<String, Integer> closestIntersection = null;
            for (int[] segment : segments) {
                //g2d.drawLine(segment[0], segment[1], segment[2], segment[3]);
                Map<String, Integer> intersect = getIntersection(targetX + dx, targetY + dy, segment);
                if (intersect == null) {
                    continue;
                }
                if (closestIntersection == null || intersect.get("param") < (int) closestIntersection.get("param")) {
                    closestIntersection = intersect;
                }
            }
            assert closestIntersection != null;
            closestIntersection.put("angle", Integer.valueOf((int) Math.toDegrees(angle)));
            intersections.add(closestIntersection);
        }

        //Collections.sort(intersections, new MapComparator());

        // Points of the intersections
        int[] xPoints = new int[intersections.size()];
        int[] yPoints = new int[intersections.size()];

        // set the points to draw the polygon
        for (int i = 0; i < intersections.size(); i++) {
            xPoints[i] = intersections.get(i).get("x");
            yPoints[i] = intersections.get(i).get("y");
        }

        Point2D center;
        float[] dist;
        Color[] colors;


        RadialGradientPaint p;
        float radius = 200;

        dist = new float[]{0.0f, 1.0f};
        colors = new Color[]{new Color(0.0f, 0.0f, 0.0f, 0.0f), Color.black};

        center = new Point2D.Float(x, y);

        p = new RadialGradientPaint(center, radius, dist, colors);
        g2d.setPaint(p);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));

        //g2d.fillPolygon(xPoints, yPoints, intersections.size() - 2);

        Polygon poly = new Polygon();
        poly.npoints = intersections.size() - 2;
        poly.xpoints = xPoints;
        poly.ypoints = yPoints;
        Area outer = new Area(new Rectangle(0, 0, GameComponent.WIDTH * GameComponent.SCALE, GameComponent.HEIGHT * GameComponent.SCALE));
        outer.subtract(new Area(poly));


        //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));
        g2d.setColor(Color.BLACK);
        g2d.fill(outer);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        g2d.setStroke(new BasicStroke(2));

        for (Map intersect : intersections) {
            g2d.setColor(Color.red);
            g2d.drawLine(x, y, (int) intersect.get("x"), (int) intersect.get("y"));
            //g2d.fillOval((int) intersect.get("x") - 5, (int) intersect.get("y") - 5, 10, 10);
        }
    }


    // get the angle between the
    public double getAngle(Point p1, Point p2) {
        double angle = Math.toDegrees(Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        return angle;
    }



    // Check where a line intersects with a segment
    public Map<String, Integer> getIntersection(double tx, double ty, int[] segment) {
        // ray in parametric: Point + Direction * T1
        float r_px = x;
        float r_py = y;
        double r_dx = tx - x;
        double r_dy = ty - y;

        // segment in parametric: Point + Direction * T1
        float s_px = segment[0];
        float s_py = segment[1];
        float s_dx = segment[2] - segment[0];
        float s_dy = segment[3] - segment[1];

        double r_mag = Math.sqrt(r_dx * r_dx + r_dy * r_dy);
        double s_mag = Math.sqrt(s_dx * s_dx + s_dy * s_dy);
        if (r_dx / r_mag == s_dx / s_mag && r_dy / r_mag == s_dy / s_mag) {
            // Unit vectors are the same.
            return null;
        }

        double T2 = (r_dx * (s_py - r_py) + r_dy * (r_px - s_px)) / (s_dx * r_dy - s_dy * r_dx);
        double T1 = (s_px + s_dx * T2 - r_px) / r_dx;


        // Must be within parametic whatevers for RAY/SEGMENT
        if (T1 < 0) return null;
        if (T2 < 0 || T2 > 1) return null;

        Map<String, Integer> map = new HashMap<>();
        map.put("x", (int) (r_px + r_dx * T1));
        map.put("y", (int) (r_py + r_dy * T1));
        map.put("param", (int) T1);

        return map;
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

    /**
     * Length (angular) of a shortest way between two angles.
     * It will be in range [0, 180].
     */
    private int distance(int alpha, int beta) {
        int phi = Math.abs(beta - alpha) % 360;       // This is either the distance or 360 - distance
        int distance = phi > 180 ? 360 - phi : phi;
        return distance;
    }
}