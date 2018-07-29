package main;

import entity.Entity;
import map.Tile;

/**
 * Collision checking for different objects
 */
public final class CollisionHandler {
    public static boolean intersectsTile(Entity e, Tile t) {
        return e.getRectangle().intersects(t.getRectangle());
    }
}
