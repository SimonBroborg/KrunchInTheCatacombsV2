package map;

import entity.Sprite;

import java.awt.*;

/**
 *
 */
/*

 */
public class Tile {
    // position
    protected int x;
    protected int y;

    protected Sprite sprite;

    protected int width;
    protected int height;

    protected boolean solid;
    protected boolean transparent;
    protected boolean highlight;

    protected TileMap tm;

    public Tile(boolean solid, boolean transparent, Sprite sprite, int x, int y, TileMap tm) {
        this.solid = solid;
        this.transparent = transparent;
        this.sprite = sprite;
        this.x = x;
        this.y = y;
	this.width = tm.getTileWidth();
	this.height = tm.getTileHeight();
        this.tm = tm;
        this.highlight = false;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getXMap() {
        return x + tm.getX();
    }

    public int getHeight() {
	return height;
    }

    public int getWidth() {
	return width;
    }

    public double getYMap() {
        return y + tm.getY();
    }

    public Rectangle getRectangle() {
	return new Rectangle((int) this.getXMap(), (int) this.getYMap(), width, height);
    }

    public void draw(Graphics2D g2d) {
	g2d.drawImage(sprite.getImage(), (int) getXMap(), (int) getYMap(), width, height, null);
        g2d.setColor(Color.RED);

    }
}
