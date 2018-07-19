package entity.objects.pickups;

import entity.objects.GameObject;
import entity.Sprite;
import gui.Inventory;
import map.TileMap;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 */
@SuppressWarnings({"MagicNumber", "AssignmentToSuperclassField"})
public abstract class Pickup extends GameObject {
    private boolean bouncedOnce;
    private boolean pickedUp;
    protected Pickup(final TileMap tm, String spritePath) {
        super(tm);
        sprite = new Sprite(spritePath);

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

    public void exitChest() {
        int randomNum = ThreadLocalRandom.current().nextInt(45, 135 + 1);
        setVector(Math.cos(randomNum), bounceSpeed);
        bouncedOnce = true;
    }

    @Override
    public void update() {
        // Makes sure the pickup stops moving
        if (isOnGround()) {
            setVector(0, 0);
            usable = true;
        }
        if (pickedUp) {
            usable = false;
            canUse = false;
            solid = false;
        }

        super.update();
    }

    @Override
    public void pickUp() {
        pickedUp = true;
    }

    // The object flies to the position of the inventory
    public void addToInventory(Inventory inventory) {
        if (!inventory.isFull()) {
            int speed = 30;
            Point p = new Point(inventory.getButton().getX() - (int) tm.getX(), inventory.getButton().getY() - (int) tm.getY());
            setVector(speed * Math.cos(Math.toRadians(getAngle(p))), speed * Math.sin(Math.toRadians(getAngle(p))));

            Rectangle rect = new Rectangle(inventory.getButton().getX() - (int) tm.getX(),
                    inventory.getButton().getY() - (int) tm.getY(), inventory.getButton().getWidth(),
                    inventory.getButton().getHeight());
            if (this.getRectangle().intersects(rect)) {
                remove = true;
                inventory.add(this);
            }
        } else {
            pickedUp = false;
        }
    }

    public abstract void use();

    public boolean hasBounced() {
        return bouncedOnce;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

}
