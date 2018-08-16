package entity.objects.pickups;

import entity.Player;
import gui.inventory.Inventory;
import map.TileMap;

import java.awt.*;

/**
 * The pickaxe is an item which the player can use to break certain tiles.
 */
public class Pickaxe extends Pickup {
    /**
     * Creates a pickaxe.
     *
     * @param tm the tile map which helps the pickaxe to keep track of collisions.
     */
    public Pickaxe(final TileMap tm, Inventory pInventory) {
        super(tm, "resources/Sprites/objects/pickups/pickaxe2.png", pInventory);
    }


    @Override
    public void drawExtras(Graphics2D g2d) {

    }

    @Override
    public void updateExtras() {

    }

    /**
     * Uses the pickaxe.
     */
    @Override
    public void use(Player player, Point point) {

    }
}
