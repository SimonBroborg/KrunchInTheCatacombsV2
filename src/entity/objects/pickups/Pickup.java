package entity.objects.pickups;

import entity.Sprite;
import entity.objects.GameObject;
import gui.Inventory;
import map.TileMap;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Pickups are items which the player can pick up and use for their special purposes.
 */
@SuppressWarnings({"MagicNumber", "AssignmentToSuperclassField"})
public abstract class Pickup extends GameObject {
    private boolean bouncedOnce;
    private boolean pickedUp;
    private Inventory pInventory;

    /**
     * Creates a pickup.
     *
     * @param tm         the tile map which helps the pickaxe to keep track of collisions.
     * @param spritePath path to the pickups sprite resource.
     * @param pInventory the players inventory.
     */
    protected Pickup(final TileMap tm, String spritePath, Inventory pInventory) {
        super(tm);
        sprite = new Sprite(spritePath);

        this.pInventory = pInventory;

        // dimensions
        height = sprite.getImage().getHeight();
        width = sprite.getImage().getWidth();
        cheight = height - 10;
        cwidth = width - 10;

        // movement
        fallSpeed = 0.5;
        maxFallSpeed = 10;
        bounceSpeed = -10;

        // flags
        bouncedOnce = false;
    }

    /**
     * The pickup "bounces" out of the chest in a random upwards direction.
     */
    public void exitChest() {
        int randomNum = ThreadLocalRandom.current().nextInt(45, 135 + 1);
        setVector(Math.cos(randomNum), bounceSpeed);
        bouncedOnce = true;
    }

    /**
     * Updates position and if the pickup has been picked up.
     */
    @Override
    public void update() {
        // Makes sure the pickup stops moving
        if (isOnGround()) {
            setVector(0, 0);
        }
        if (pickedUp) {
            // The pickup can no longer be picked up
            activatable = false;

            // Makes sure the pickup can go through tiles without colliding
            solid = false;

            addToPInventory();
        }

        super.update();
    }

    /**
     * Set the pickup to be picked up.
     */
    @Override
    public void activate() {
        pickedUp = true;
    }

    /**
     * The pickup fliest to the inventory's position and get's added to the inventory.
     * Tells if the pickup should be removed.
     */
    public void addToPInventory() {
        if (!pInventory.isFull()) {
            // The speed in which the pickup travels
            int speed = 30;

            // Get the position of the inventory icon
            Point p = new Point(pInventory.getButton().getX() - (int) tm.getX(), pInventory.getButton().getY() - (int) tm.getY());

            // Set the vector towards the inventory icon
            setVector(speed * Math.cos(Math.toRadians(getAngle(p))), speed * Math.sin(Math.toRadians(getAngle(p))));

            // The inventories collision rectangle.
            Rectangle rect = new Rectangle(pInventory.getButton().getX() - (int) tm.getX(),
                    pInventory.getButton().getY() - (int) tm.getY(), pInventory.getButton().getWidth(),
                    pInventory.getButton().getHeight());

            // Checks if the inventory
            if (this.getRectangle().intersects(rect)) {
                remove = true;
                pInventory.add(this);
            }

        }
    }

    /**
     * Use the pickup when it's in the players inventory.
     */
    public abstract void use();

    /**
     * Check if the pickup has bounced once.
     *
     * @return true or false based on if it has bounced
     */
    public boolean hasBounced() {
        return bouncedOnce;
    }

}
