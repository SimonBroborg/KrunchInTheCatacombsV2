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
	sprite = new Sprite("resources/Sprites/Objects/Pickups/pickaxe.png");
	fallSpeed = 0.5;
	maxFallSpeed = 10;
	height = sprite.getImage().getHeight();
	width = sprite.getImage().getWidth();
	cheight = height - 10;
	cwidth = width - 10;
	bouncedOnce = false;
	bounceSpeed = -10;
	usable = false;
    }

    public void bounce() {
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
