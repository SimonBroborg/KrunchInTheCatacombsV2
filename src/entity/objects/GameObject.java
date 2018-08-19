package entity.objects;

import entity.Entity;
import map.TileMap;

import java.awt.*;

/**
 * Objects which can be used by the player
 */
public abstract class GameObject extends Entity {
    protected boolean activatable;
    protected boolean highlight;

    /**
     * Creates a game object
     *
     * @param tm the tile map which helps the pickaxe to keep track of collisions.
     */
    protected GameObject(final TileMap tm) {
        super(tm);

        // The object can be activated by default.
        activatable = true;
    }

    /**
     * Updates the position of the game object.
     */
    public void update() {
        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition((int) xtemp, (int) ytemp);

    }

    /**
     * Activates the object.
     */
    public abstract void activate();

    /**
     * Tells if the object is activatable
     *
     * @return true or false based on if the object can be activated.
     */
    public boolean isActivatable() {
        return activatable;
    }


    public void checkHover(Point p) {
        /*if (p != null) {
            activatable = getRectangle().intersects(p.getX(), p.getY(), 1, 1);
        }*/
    }


    /**
     * Draws the object to the frame
     *
     * @param g2d the drawing object
     * @see Graphics2D
     */
    @Override
    public void draw(final Graphics2D g2d) {
        setMapPosition();
        if (highlight) {
            //g2d.drawImage(activSprite.getImage(), x + xmap - 20, y + ymap - 20, null);
            g2d.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
            g2d.fillRect(x + xmap, y + ymap, width, height);
        }
        super.draw(g2d);
    }


}
