package Entity.Objects.Pickups;

import Entity.Objects.GameObject;
import Entity.Sprite;
import HUD.Inventory;
import TileMap.TileMap;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Pickup extends GameObject
{
    private boolean bouncedOnce;
    private boolean pickedUp;

    public Pickup(final TileMap tm) {
	super(tm);
	sprite = new Sprite("resources/Sprites/Objects/Pickups/pickaxe2.png");

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
	usable = false;
	remove = false;
    }

    public void exitChest() {
	int randomNum = ThreadLocalRandom.current().nextInt(45, 135 + 1);
	setVector(Math.cos(randomNum), bounceSpeed);
	bouncedOnce = true;
    }

    @Override public void update() {
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

    @Override public void use() {
	pickedUp = true;
    }

    // The object flies to the position of the inventory
    public void addToInventory(Inventory inventory) {
	Point p = new Point(inventory.getButton().getX() - (int) tm.getX(), inventory.getButton().getY() - (int) tm.getY());
	setVector(15 * Math.cos(Math.toRadians(getAngle(p))), 15 * Math.sin(Math.toRadians(getAngle(p))));

	Rectangle rect =
		new Rectangle(inventory.getButton().getX() - (int) tm.getX(), inventory.getButton().getY() - (int) tm.getY(),
			      inventory.getButton().getWidth(), inventory.getButton().getHeight());
	if (this.getRectangle().intersects(rect)) {
	    remove = true;
	    inventory.add(this);
	}
    }


    public boolean hasBounced() {
	return bouncedOnce;
    }

    public boolean isPickedUp() {
	return pickedUp;
    }

}
