package entity;

import map.Tile;
import map.TileMap;

import java.awt.*;

/**
 * An object which has a position and a size.
 */
@SuppressWarnings("MagicNumber")
public abstract class Entity {
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

    // Tile stuff
    protected TileMap tm;
    protected double xmap;
    protected double ymap;

    /**
     * Creates an entity object
     *
     * @param tm the tile map which helps the pickaxe to keep track of collisions.
     */
    protected Entity(TileMap tm) {
        this.tm = tm;
        tileSize = tm.getTileSize();
        facingRight = true;
        bounceSpeed = -5;
        solid = true;
    }

    /**
     * Returns the collision rectangle of the entity
     *
     * @return a rectangle with the size and position of the entity
     * @see javafx.scene.shape.Rectangle
     */
    public Rectangle getRectangle() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    /**
     * Get the x-position.
     *
     * @return the x-position as an integer
     */
    public int getX() {
        return (int) x;
    }

    /**
     * Get the y-position
     *
     * @return the y-position as an integer
     */
    public int getY() {
        return (int) y;
    }

    /**
     * Set's the x-and y-positions
     *
     * @param x the x-position
     * @param y the y-position
     */
    public void setPosition(double x, double y) {
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

    /**
     * Checks which corners of the entity which collides with the tile map
     *
     * @param x the entities x-destination
     * @param y the entities y-destination
     */
    public void calculateCorners(double x, double y) {
        int leftTile = (int) (x - cwidth / 2) / tileSize;
        int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
        int topTile = (int) (y - cheight / 2) / tileSize;
        int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;
        if (topTile < 0 || bottomTile >= tm.getNumRows() || leftTile < 0 || rightTile >= tm.getNumCols()) {
            topLeft = false;
            topRight = false;
            bottomLeft = false;
            bottomRight = false;
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
        currCol = (int) x / tileSize;
        currRow = (int) y / tileSize;

        xdest = x + dx;
        ydest = y + dy;

        xtemp = x;
        ytemp = y;

        calculateCorners(x, ydest);
        if (dy < 0) {
            if ((topLeft || topRight) && solid) {
                dy = 0;
                ytemp = currRow * tileSize + (float) cheight / 2;

            } else {
                ytemp += dy;
            }
        }
        if (dy > 0) {
            if ((bottomLeft || bottomRight) && solid) {
                dy = 0;
                falling = false;
                ytemp = (currRow + 1) * tileSize - (float) cheight / 2;
            } else {
                ytemp += dy;
            }
        }

        calculateCorners(xdest, y);
        if (dx < 0) {
            if ((topLeft || bottomLeft) && solid) {
                dx = 0;
                xtemp = currCol * tileSize + (float) cwidth / 2;
            } else {
                xtemp += dx;
            }
        }
        if (dx > 0) {
            if ((topRight || bottomRight) && solid) {
                dx = 0;
                xtemp = (currCol + 1) * tileSize - (float) cwidth / 2;
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

    /*

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

    public void setJumping(boolean b) {
        jumping = b;
    }

    public void draw(Graphics2D g2d) {
        setMapPosition();

        if (facingRight) {
            g2d.drawImage(sprite.getImage(), (int) (x + xmap - width / 2), (int) (y + ymap - height / 2), width, height, null);
        } else {
            g2d.drawImage(sprite.getImage(), (int) (x + xmap - width / 2 + width), (int) (y + ymap - height / 2), -width, height, null);
        }
    }

    public Sprite getSprite() {
        return sprite;
    }
}
