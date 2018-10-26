package entity;

import map.Tile;
import map.TileMap;

import java.awt.*;

/**
 * An object which has a position and a size.
 */
@SuppressWarnings("MagicNumber")
public abstract class Entity
{
    // position and vector
    protected int x;
    protected int y;
    protected double dx;
    protected double dy;

    // dimensions
    protected int width;
    protected int height;
    protected int cwidth;
    protected int cheight;

    // collision
    protected double xdest;
    protected double ydest;
    protected double xtemp;
    protected double ytemp;

    // for bouncing
    protected int bounceSpeed;

    protected boolean solid;

    // movement
    protected boolean left;
    protected boolean right;
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
    protected Sprite sprite = null;

    protected boolean facingRight;
    protected boolean remove;

    // Tile stuff
    protected TileMap tm;
    protected int xmap;
    protected int ymap;

    /**
     * Creates an entity object
     *
     * @param tm the tile map which helps the pickaxe to keep track of collisions.
     */
    protected Entity(TileMap tm) {
	this.tm = tm;
	facingRight = true;
	bounceSpeed = -5;
	solid = true;
    }

    public void draw(Graphics2D g2d) {
	if (facingRight) {
	    g2d.drawImage(sprite.getImage(), (x + xmap), (y + ymap), width, height, null);
	} else {
	    g2d.drawImage(sprite.getImage(), (x + xmap + width), (y + ymap), -width, height, null);
	}
    }

    public void update() {
	setMapPosition();
	getNextPosition();
	checkTileMapCollision();
	setPosition((int) xtemp, (int) ytemp);
	if (right) facingRight = true;
	if (left) facingRight = false;
    }


    // Sets the movement vectors based on the players current movement
    public void getNextPosition() {
	// movement
	if (left) {
	    dx -= moveSpeed;
	    if (dx < -maxSpeed) {
		dx = -maxSpeed;
	    }

	} else if (right) {
	    dx += moveSpeed;
	    if (dx > maxSpeed) {
		dx = maxSpeed;

	    }
	} else {
	    if (dx > 0) {
		dx -= stopSpeed;
		if (dx < 0) {
		    dx = 0;
		}
	    } else if (dx < 0) {
		dx += stopSpeed;
		if (dx > 0) {
		    dx = 0;
		}
	    }
	}
	// jumping
	if (jumping && !falling) {
	    dy = jumpStart;
	    falling = true;
	}
	// falling
	if (falling) {
	    dy += fallSpeed;
	    if (dy > 0) {
		jumping = false;
	    }
	    if (dy < 0 && !jumping) {
		dy += stopJumpSpeed;
	    }
	    if (dy > maxFallSpeed) {
		dy = maxFallSpeed;
	    }
	}
    }


    public void checkTileMapCollision() {
	xdest = x + dx;
	ydest = y + dy;

	xtemp = x;
	ytemp = y;

	falling = true;
	Rectangle cRect = new Rectangle(x + tm.getX(), (int) ydest + tm.getY() + 1, width, height);
	for (Tile[] tiles : tm.getTiles()) {
	    for (Tile tile : tiles) {
		if (cRect.intersects(tile.getRectangle()) && tile.isSolid() && solid) {
		    if ((int) ydest - dy + height <= tile.getY()) {
			ytemp = tile.getY() - height;
			dy = 0;
			falling = false;
		    } else if (ydest - dy >= tile.getY() + (int) tile.getRectangle().getHeight()) {
			ytemp = tile.getY() + (int) tile.getRectangle().getHeight();
			dy = fallSpeed;
			falling = true;
		    }
		}
	    }
	}
	cRect = new Rectangle((int) xdest + tm.getX(), y + tm.getY(), width, height);
	for (Tile[] tiles : tm.getTiles()) {
	    for (Tile tile : tiles) {
		if (cRect.intersects(tile.getRectangle()) && tile.isSolid() && solid) {
		    if (x + width <= tile.getX()) {
			xtemp = tile.getX() - width;
			dx = 0;
		    }
		    // Moving to the left
		    else if (x >= tile.getX() + (int) tile.getRectangle().getWidth()) {
			xtemp = tile.getX() + (int) tile.getRectangle().getWidth();
			dx = 0;
		    }
		}
	    }
	}

	ytemp += dy;
	xtemp += dx;
    }

    /**
     * Checks if the entity is on the ground
     *
     * @return Boolean telling if the entity is in ground
     */
    public boolean isOnGround() {
	return (!falling && !jumping && dy == 0);
    }


    public double getAngle(Point p) {
	double angle = Math.toDegrees(Math.atan2(p.getY() - y, p.getX() - x));

	if (angle < 0) {
	    angle += 360;
	}

	return angle;

    }

    public void setLeft(boolean b) {
	left = b;
    }

    public void setRight(boolean b) {
	right = b;
    }

    /**
     * Returns the collision rectangle of the entity
     *
     * @return a rectangle with the size and position of the entity
     * @see javafx.scene.shape.Rectangle
     */
    public Rectangle getRectangle() {
	return new Rectangle(x + xmap, y + ymap, width, height);
    }

    /**
     * Set's the x-and y-positions
     *
     * @param x the x-position
     * @param y the y-position
     */
    public void setPosition(int x, int y) {
	this.x = x;
	this.y = y;
    }

    /**
     * Get's the maps position. Used to place the entity based on the "camera".
     */
    public void setMapPosition() {
	xmap = tm.getX();
	ymap = tm.getY();
    }

    /**
     * Set a movement vector.
     *
     * @param dx vector for the x-position.
     * @param dy vector for the y-position.
     */
    public void setVector(double dx, double dy) {
	this.dx = dx;
	this.dy = dy;
    }

    public Sprite getSprite() {
	return sprite;
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }


    /**
     * Tells if the object should be removed from the map.
     *
     * @return true of false based on if it should be removed.
     */
    public boolean shouldRemove() {
	return remove;
    }


    /**
     * Get the x-position.
     *
     * @return the x-position as an integer
     */
    public int getXMap() {
	return x + tm.getX();
    }

    /**
     * Get the y-position
     *
     * @return the y-position as an integer
     */
    public int getYMap() {
	return y + tm.getY();
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public boolean isJumping() {
	return jumping;
    }

    public void setJumping(boolean b) {
	jumping = b;
    }

    public boolean isFalling() {
	return falling;
    }

    public double getDy() {
	return dy;
    }

    public double getDx() {
	return dx;
    }
}
