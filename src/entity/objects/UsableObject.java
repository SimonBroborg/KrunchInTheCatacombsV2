package entity.objects;

import entity.Entity;
import entity.Sprite;
import map.TileMap;

import java.awt.*;

/**
 * Objects which can be used by the player
 */
@SuppressWarnings("MagicNumber")
public abstract class UsableObject extends Entity {
    protected boolean remove;
    protected boolean activatable;
    private Sprite activSprite;

    /**
     * Creates a game object
     *
     * @param tm the tile map which helps the pickaxe to keep track of collisions.
     */
    protected UsableObject(final TileMap tm) {
        super(tm);
        activSprite = new Sprite("resources/Sprites/Misc/activSprite.png");
    }

    /**
     * Updates the position of the game object.
     */
    public void update() {
        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
    }


    /**
     * Activates the object.
     */
    public abstract void activate();

    /**
     * Tells if the object is activatable
     *
     * @return true or false based on if the object can be used.
     */
    public boolean isActivatable() {
        return activatable;
    }


    public void checkHover(Point p) {
        if (p != null)
            activatable = getRectangle().intersects(p.getX() + (float) width / 2, p.getY() + (float) height / 2, 1, 1);
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
        if (activatable) {
            g2d.drawImage(activSprite.getImage(), (int) (x + xmap - width / 2) - 20, (int) (y + ymap - height / 2) - 20, null);
        }
        super.draw(g2d);
    }


    /**
     * Tells if the object should be removed from the map.
     *
     * @return true of false based on if it should be removed.
     */
    public boolean shouldRemove() {
        return remove;
    }
}
