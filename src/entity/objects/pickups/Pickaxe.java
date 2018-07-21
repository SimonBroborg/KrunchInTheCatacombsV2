package entity.objects.pickups;

import gui.Inventory;
import map.TileMap;

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


    /**
     * Uses the pickaxe.
     */
    @Override
    public void use() {
        System.out.println("You want to use the pickaxe");
    }
}
