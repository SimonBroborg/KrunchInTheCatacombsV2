package TileMap;

import Entity.Sprite;

import java.awt.*;

public class Tile
{
    // position
    protected double x;
    protected double y;

    protected Sprite sprite;

    protected String type;
    protected int tileSize;

    protected boolean solid;
    protected boolean transparent;

    public Tile(String type, double x, double y, int tileSize) {
	this.type = type;
	this.x = x;
	this.y = y;
	this.tileSize = tileSize;
    }

    public boolean isSolid() {
	return solid;
    }

    public boolean isTransparent() {
	return transparent;
    }

    public String getType() {
	return type;
    }

    public double getX() {
	return x;
    }

    public double getY() {
	return y;
    }

    public Rectangle getRectangle() {
	return new Rectangle((int) x, (int) y, tileSize, tileSize);
    }

    public void draw(Graphics2D g2d) {
	g2d.drawImage(sprite.getImage(), (int) x, (int) y, tileSize, tileSize,null);
    }
}
