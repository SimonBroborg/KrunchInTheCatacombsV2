package Main;

import java.awt.*;

public class Segment
{
    private Point start;
    private Point end;

    public Segment(Point start, Point end) {
	this.start = start;
	this.end = end;
    }

    public Point getStart() {
	return start;
    }

    public Point getEnd() {
	return end;
    }

    public void draw(Graphics2D g2d) {
	g2d.drawLine(start.x, start.y, end.x, end.y);
    }
}
