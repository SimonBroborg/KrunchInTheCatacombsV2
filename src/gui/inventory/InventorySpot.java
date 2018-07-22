package gui.inventory;

import entity.Sprite;
import entity.objects.pickups.Pickup;

import java.awt.*;

/**
 * The spots in the inventory where items are.
 */
public class InventorySpot {
    // dimensions
    private int width;
    private int height;

    // position
    private int x;
    private int y;

    private Sprite sprite;

    // The pickup which is in the spot
    private Pickup pickup = null;

    public InventorySpot(int x, int y, int width, int height) {
        sprite = new Sprite("resources/Sprites/Misc/inventorySpot.png");
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Sets the pickup which is shown in the inventory spot
     *
     * @param p the pickup
     */
    public void setPickup(Pickup p) {
        pickup = p;
    }

    public boolean isEmpty() {
        return pickup == null;
    }

    private void clear() {
        pickup = null;
    }

    public void draw(Graphics2D g2d) {

        g2d.drawImage(sprite.getImage(), x, y, width, height, null);
        if (pickup != null) {
            g2d.drawImage(pickup.getSprite().getImage(), x + Math.abs(width - pickup.getSprite().getWidth()) / 2, y + Math.abs(height - pickup.getSprite().getHeight()) / 2, null);
        }
    }
}
