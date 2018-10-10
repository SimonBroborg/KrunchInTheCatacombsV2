package entity;

import entity.objects.Chest;
import entity.objects.GameObject;
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
        activRange = 100;

        sprite = new Sprite("resources/Sprites/Player/player.png");

        inventory = new Inventory();

    }

    /**
     * Checks if any object can be activated by the player
     *
     * @param objects the game objects which is on the map
     */
    public void activate(List<GameObject> objects) {
	System.out.println("Activate");
        ListIterator<GameObject> iter = objects.listIterator();
        while (iter.hasNext()) {

            GameObject o = iter.next();

	    Entity e = new Player(tm);

            // If the object can be activated
            if (o.isActivatable()) {
                // Check if the object can be used ( is in range for the player )
                if (inRange(o.getX() + o.getWidth() / 2, o.getY() + o.getHeight() / 2, activRange)) {
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
        return Math.hypot(ox - x + (float) width / 2, oy - y + (float) height / 2) < range;
    }

    /**
     * Use the currently chosen item.
     */
    public void useItem(Point point) {
        inventory.useActive(this, point);
    }


    /**
     * Update the position of the player.
     * Also updates the players inventory.
     */
    public void update() {
        // update position
        super.update();
        inventory.update();
    }

    /**
     * Get the players inventory.
     * @return a list containing pickups.
     */
    public Inventory getInventory() {
        return inventory;
    }


    public int getActivRange() {
        return activRange;
    }
}
