package Main;

import Entity.Player;
import TileMap.Tile;
import TileMap.TileMap;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@SuppressWarnings("OverlyComplexMethod")
public class FlashLight
{
    public static final int RANGE = 800;
    private List<Segment> segments;
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
	// contains all the line segments
	segments = new ArrayList<>();

	// create segments from each tile
	for (Tile[] tiles : tm.getTiles()) {
	    for (Tile tile : tiles) {
		// If the flashlight can collide with the tile
		if (tile.isSolid() && player.inRange((int) tile.getX(), (int) tile.getY(), RANGE)) {
		    // Get the bounding box rect from the tile
		    Rectangle rect = tile.getRectangle();
		    g2d.draw(rect);
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
	segments.add(new Segment(new Point(0, 0), new Point(GameComponent.SCALED_WIDTH, 0)));
	segments.add(new Segment(new Point(GameComponent.SCALED_WIDTH, 0),
				 new Point(GameComponent.SCALED_WIDTH, GameComponent.SCALED_HEIGHT)));
	segments.add(new Segment(new Point(GameComponent.SCALED_WIDTH, GameComponent.SCALED_HEIGHT),
				 new Point(0, GameComponent.SCALED_HEIGHT)));
	segments.add(new Segment(new Point(0, GameComponent.SCALED_HEIGHT), new Point(0, 0)));
    }

    public void draw(Graphics2D g2d) {
	// Points where the intersections happen
	intersections = new ArrayList<>();

	// Create segments from tiles
	setSegments(g2d);

	// Sets points in the corners of the tiles
	ArrayList<Point> points = new ArrayList<>();
	for (Segment segment : segments) {
	    points.add(segment.getStart());
	    points.add(segment.getEnd());
	}

	// The angles in which the lines should be checked
	List<Float> uniAngles = new ArrayList<>();

	double targetAngle = getAngle(new Point(targetX, targetY), new Point(x, y));
	targetAngle += 360;
	uniAngles.add(Float.valueOf((float) Math.toRadians(targetAngle)));
	// draw the target ine
	g2d.drawLine(x, y, x + (int) (-100 * Math.cos(Math.toRadians(targetAngle))),
		     y + (int) (-100 * Math.sin(Math.toRadians(targetAngle))));
	g2d.drawLine(x, y, x + (int) (-100 * Math.cos(Math.toRadians(targetAngle + 45))),
		     y + (int) (-100 * Math.sin(Math.toRadians(targetAngle + 45))));
	g2d.drawLine(x, y, x + (int) (-100 * Math.cos(Math.toRadians(targetAngle - 45))),
		     y + (int) (-100 * Math.sin(Math.toRadians(targetAngle - 45))));

	// creates the angles between a x and y position and the set points
	for (int i = 0; i < points.size(); i++) {
	    Point uniquePoint = points.get(i);
	    double angle = (getAngle(new Point(uniquePoint.x, uniquePoint.y), new Point(x, y)));
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
	    for (Segment segment : segments) {
		//g2d.drawLine(segment.getStart().x, segment.getStart().y, segment.getEnd().x, segment.getEnd().y);
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

	dist = new float[] { 0.0f, 1.0f };
	colors = new Color[] { new Color(0.0f, 0.0f, 0.0f, 0.0f), Color.black };

	center = new Point2D.Float(x, y);

	p = new RadialGradientPaint(center, radius, dist, colors);
	g2d.setPaint(p);
	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));

	//g2d.fillPolygon(xPoints, yPoints, intersections.size() - 2);

	Polygon poly = new Polygon();
	poly.npoints = intersections.size() - 2;
	poly.xpoints = xPoints;
	poly.ypoints = yPoints;
	Area outer = new Area(
		new Rectangle(0, 0, GameComponent.WIDTH * GameComponent.SCALE, GameComponent.HEIGHT * GameComponent.SCALE));
	outer.subtract(new Area(poly));


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
    public Map<String, Integer> getIntersection(double tx, double ty, Segment segment) {
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