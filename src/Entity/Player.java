package Entity;

import Entity.Objects.Chest;
import Entity.Objects.GameObject;
import Entity.Objects.Pickups.Flashlight;
import HUD.Inventory;
import TileMap.TileMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@SuppressWarnings({"AssignmentToSuperclassField", "MagicNumber"})
public class Player extends Entity {
    private int useRange;
    private boolean using;
    private Inventory inventory;

    private Flashlight flashlight;

    private float startTime;

    public Player(TileMap tm) {
        super(tm);
        startTime = System.nanoTime();

        // dimensions
        width = 40;
        height = 40;
        cwidth = 30;
        cheight = 30;

        // movement
        moveSpeed = 0.7;
        maxSpeed = 3;
        stopSpeed = 0.4;
        fallSpeed = 0.5;
        maxFallSpeed = 10;
        jumpStart = -10;
        stopJumpSpeed = 0.3;

        // pickUp of things
        useRange = 40;

        sprite = new Sprite("resources/Sprites/Player/player.png");

        inventory = new Inventory();

        flashlight = new Flashlight(tm);
    }

    public void checkAct(List<GameObject> objects) {
        boolean canUse = false;
        List<GameObject> newObjects = new ArrayList<>();
        for (GameObject o : objects) {

            // If the object can be used in some way
            if (o.isUsable()) {

                // Check if the object can be used ( is in range for the player )
                if (inRange(o.getX(), o.getY(), useRange)) {
                    canUse = true;
                }

                // Use the object
                if (canUse) {
                    if (using) {
                        o.pickUp();
                        // Check if a chest is used and get its content
                        if (o instanceof Chest) {
                            newObjects.addAll(((Chest) o).getContent());
                        }
                    }
                    o.setCanUse(canUse);
                    canUse = false;
                } else {
                    o.setCanUse(canUse);
                }
            }
        }

        // Add new objects to the already existing object list
        objects.addAll(newObjects);
        newObjects.clear();
    }

    public boolean inRange(int ox, int oy, int range){
        return ((ox > x && ox < x + range) || (ox < x && ox > x - range)) &&
                oy  > y - height / 2  && oy < y + height / 2;
    }

    public void useItem(){
        flashlight.use();
    }
    public void update() {
        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        if (right) facingRight = true;
        if (left) facingRight = false;

        flashlight.update((int) x, (int) y);
        inventory.update();
    }

    public void draw(Graphics2D g2d) {
        // Inventory isn't drawn here cause of z-index
        setMapPosition();
        flashlight.draw(g2d);
        super.draw(g2d);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setActing(final boolean acting) {
        using = acting;
    }
}
