package Entity.Objects.Pickups;

import Entity.Objects.GameObject;
import Entity.Player;
import Entity.Sprite;
import TileMap.TileMap;

import java.util.concurrent.ThreadLocalRandom;

public class Pickup extends GameObject
{
    private boolean bouncedOnce;
    private boolean pickedUp;

    public Pickup(final TileMap tm) {
	super(tm);
	sprite = new Sprite("resources/Sprites/Objects/Pickups/test.png");
	fallSpeed = 0.5;
	maxFallSpeed = 10;
	height = 20;
	width = 20;
	cheight = 10;
	cwidth = 10;
	bouncedOnce = false;
	bounceSpeed = -10;
	usable = true;
    }

    public void bounce() {
	int randomNum = ThreadLocalRandom.current().nextInt(45, 135 + 1);
	setVector(Math.cos(randomNum), bounceSpeed);
	bouncedOnce = true;
    }

    @Override public void update() {
	if (isOnGround()) {
	    setVector(0, 0);
	}
	super.update();
    }

    @Override public void use(final Player player) {
	pickedUp = true;
    }

    public boolean hasBounced() {
	return bouncedOnce;
    }

    public boolean isPickedUp() {
	return pickedUp;
    }
}
