package entity;


import entity.objects.Chest;
import entity.objects.GameObject;
import entity.objects.pickups.Pickaxe;
import entity.objects.pickups.Pickup;
import gui.inventory.Inventory;
import map.TileMap;

import java.awt.*;
import java.util.List;
import java.util.ListIterator;

/**
 *
 */
@SuppressWarnings({"AssignmentToSuperclassField", "MagicNumber"})
public class Player extends Entity {
    private int activRange;
    private Inventory inventory;
    private Pickup currentItem;

    public Player(TileMap tm) {
        super(tm);

        // dimensions
        width = 40;
        height = 40;
        cwidth = width - 10;
        cheight = height - 10;

        // movement
        moveSpeed = 0.7;
        maxSpeed = 3;
        stopSpeed = 0.4;
        fallSpeed = 0.5;
        maxFallSpeed = 10;
        jumpStart = -10;
        stopJumpSpeed = 0.3;

        // the range in which the player can activate game objects
        activRange = 40;

        sprite = new Sprite("resources/Sprites/Player/player.png");

        inventory = new Inventory();

        currentItem = new Pickaxe(tm, inventory);
    }

    /**
     * Checks if any object can be activated by the player
     *
     * @param objects the game objects which is on the map
     */
    public void activate(List<GameObject> objects) {
        ListIterator<GameObject> iter = objects.listIterator();
        while (iter.hasNext()) {

            GameObject o = iter.next();

            // If the object can be activated
            if (o.isActivatable()) {
                // Check if the object can be used ( is in range for the player )
                if (inRange(o.getX(), o.getY(), activRange)) {
                    o.activate();

                    // Check if a chest is used and add its content to the world
                    if (o instanceof Chest) {
                        for (Pickup p : ((Chest) o).getContent())
                            iter.add(p);
                    }
                }
            }
        }
    }

    /**
     * Checks if an object is positioned within a specific range to the player.
     *
     * @param ox    the objects x-position
     * @param oy    the objects y-position
     * @param range the range (in pixels) which the objects has to be
     */
    public boolean inRange(int ox, int oy, int range) {
        return Math.hypot(ox - x, oy - y) < range;
    }

    /**
     * Use the currently chosen item.
     */
    public void useItem() {
        currentItem.use();
    }


    /**
     * Update the position of the player.
     * Also updates the players inventory.
     */
    public void update() {
        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        if (right) facingRight = true;
        if (left) facingRight = false;

        inventory.update();
    }

    /**
     * Draws the player.
     *
     * @param g2d the drawing object.
     */
    public void draw(Graphics2D g2d) {
        // Inventory isn't drawn here cause of z-index
        setMapPosition();
        super.draw(g2d);
    }

    /**
     * Get the players inventory.
     * @return a list containing pickups.
     */
    public Inventory getInventory() {
        return inventory;
    }
}
