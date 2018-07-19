package entity.objects.pickups;

import map.TileMap;

/**
 *
 */
public class Pickaxe extends Pickup
{
    public Pickaxe(final TileMap tm) {
	super(tm, "resources/Sprites/objects/pickups/pickaxe2.png");
    }

    @Override
    public void use() {
        System.out.println("You want to use the pickaxe");
    }
}
