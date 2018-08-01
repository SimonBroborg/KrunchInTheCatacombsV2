package main;

import entity.Entity;
import map.Tile;

/**
 * Collision checking for different objects
 */
public final class CollisionHandler {
    private CollisionHandler() {
    }

    public static boolean intersects(Entity objA, Tile objB) {
        // If one of these if-statements are true there is no intersection
        // If object A is above object B
        if (objA.getY() + objA.getHeight() <= objB.getY()) {
            return false;
        }
        // If object A is underneath object B
        if (objA.getY() >= objB.getY() + objB.getHeight()) {
            return false;
        }
        // If object A is to the left of object B
        if (objA.getX() + objA.getWidth() <= objB.getX()) {
            return false;
        }
        // If object A is to the right of object B
        return objA.getX() < objB.getX() + objB.getWidth();
    }
}
