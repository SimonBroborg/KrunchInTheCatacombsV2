package Entity;

import TileMap.Tile;
import TileMap.TileMap;

import java.awt.*;

public abstract class Entity
{
    // position and vector
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;

    // dimensions
    protected int width;
    protected int height;
    protected int cwidth;
    protected int cheight;

    // collision
    protected int tileSize;
    protected int currRow;
    protected int currCol;
    protected double xdest;
    protected double ydest;
    protected double xtemp;
    protected double ytemp;
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;

    // movement
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean jumping;
    protected boolean falling;

    // movement attributes
    protected double moveSpeed;
    protected double maxSpeed;
    protected double stopSpeed;
    protected double fallSpeed;
    protected double maxFallSpeed;
    protected double jumpStart;
    protected double stopJumpSpeed;

    // Sprite / Animation
    protected Sprite sprite;

    // Tile stuff
    protected TileMap tm;
    protected double xmap;
    protected double ymap;

    public Entity(TileMap tm) {
	this.tm = tm;
	tileSize = tm.getTileSize();
    }

    public boolean intersects(Entity e) {
	return getRectangle().intersects(e.getRectangle());
    }

    public Rectangle getRectangle() {
	return new Rectangle((int) x - cwidth, (int) y - cheight, cwidth, cheight);
    }

    public int getX() {
	return (int) x;
    }

    public int getY() {
	return (int) y;
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public int getCwidth() {
	return cwidth;
    }

    public int getCheight() {
	return cheight;
    }

    public void setPosition(double x, double y) {
	this.x = x;
	this.y = y;
    }

    public void setMapPosition() {
	xmap = tm.getX();
	ymap = tm.getY();
    }

    public void setVector(double dx, double dy) {
	this.dx = dx;
	this.dy = dy;
    }

    public void calculateCorners(double x, double y) {
	int leftTile = (int) (x - cwidth / 2) / tileSize;
	int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
	int topTile = (int) (y - cheight / 2) / tileSize;
	int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;
	if (topTile < 0 || bottomTile >= tm.getNumRows() || leftTile < 0 || rightTile >= tm.getNumCols()) {
	    topLeft = topRight = bottomLeft = bottomRight = false;
	    return;
	}
	Tile tl = tm.getTiles()[topTile][leftTile];
	Tile tr = tm.getTiles()[topTile][rightTile];
	Tile bl = tm.getTiles()[bottomTile][leftTile];
	Tile br = tm.getTiles()[bottomTile][rightTile];
	topLeft = tl.isSolid();
	topRight = tr.isSolid();
	bottomLeft = bl.isSolid();
	bottomRight = br.isSolid();

    }

    public void checkTileMapCollision() {
	currCol = (int) x / tileSize;
	currRow = (int) y / tileSize;

	xdest = x + dx;
	ydest = y + dy;

	xtemp = x;
	ytemp = y;

	calculateCorners(x, ydest);
	if (dy < 0) {
	    if (topLeft || topRight) {
		dy = 0;
		ytemp = currRow * tileSize + cheight / 2;

	    } else {
		ytemp += dy;
	    }
	}
	if (dy > 0) {
	    if (bottomLeft || bottomRight) {
		dy = 0;
		falling = false;
		ytemp = (currRow + 1) * tileSize - cheight / 2;
	    } else {
		ytemp += dy;
	    }
	}

	calculateCorners(xdest, y);
	if (dx < 0) {
	    if (topLeft || bottomLeft) {
		dx = 0;
		xtemp = currCol * tileSize + cwidth / 2;
	    } else {
		xtemp += dx;
	    }
	}
	if (dx > 0) {
	    if (topRight || bottomRight) {
		dx = 0;
		xtemp = (currCol + 1) * tileSize - cwidth / 2;
	    } else {
		xtemp += dx;
	    }
	}

	if (!falling) {
	    calculateCorners(x, ydest + 1);
	    if (!bottomLeft && !bottomRight) {
		falling = true;
	    }
	}
    }

    public void setLeft(boolean b) { left = b;}

    public void setRight(boolean b) { right = b;}

    public void setUp(boolean b) { up = b;}

    public void setDown(boolean b) { down = b;}

    public void setJumping(boolean b) { jumping = b;}

    public void draw(Graphics2D g2d) {
	g2d.drawImage(sprite.getImage(), (int) (x + xmap - width / 2), (int) (y + ymap - height / 2), width, height, null);
    }
}
