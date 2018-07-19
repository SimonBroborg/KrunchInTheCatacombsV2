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
    protected double x;
    protected double y;

    protected Sprite sprite;

    protected int tileSize;

    protected boolean solid;
    protected boolean transparent;

    protected TileMap tm;

    public Tile(boolean solid, boolean transparent, Sprite sprite, double x, double y, TileMap tm) {
        this.solid = solid;
        this.transparent = transparent;
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.tileSize = tm.getTileSize();
        this.tm = tm;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isTransparent() {
        return transparent;
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
        g2d.drawImage(sprite.getImage(), (int) (x + tm.getX()), (int) (y + tm.getY()), tileSize, tileSize, null);
    }
}
