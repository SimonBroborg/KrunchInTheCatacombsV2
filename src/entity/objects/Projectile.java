package entity.objects;

import entity.Entity;
import entity.Sprite;
import map.TileMap;

/**
 *
 */
public class Projectile extends UsableObject {

    public Projectile(Entity origin, float angle, TileMap tm) {
        super(tm);

        sprite = new Sprite("resources/Sprites/Objects/Pickups/milk.png");

        width = sprite.getWidth();
        height = sprite.getHeight();
        cwidth = width - 10;
        cheight = height - 10;

        moveSpeed = 10;
        maxSpeed = 5;
        maxFallSpeed = 10;
        fallSpeed = 0.3;

        setPosition(origin.getX(), origin.getY());
        //System.out.println("WOWO");
        setVector(moveSpeed * Math.cos(Math.toRadians(angle)), moveSpeed * Math.sin(Math.toRadians(angle)));

    }

    @Override
    public void update() {
        if (isOnGround()) {
            remove = true;
        }
        super.update();
    }

    @Override
    public void activate() {

    }
}
