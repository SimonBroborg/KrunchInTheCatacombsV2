package Entity.Objects;

import Entity.Objects.Pickups.Pickup;
import Entity.Player;
import Entity.Sprite;
import TileMap.TileMap;

import java.awt.*;

public class Chest extends GameObject
{
    private boolean opened;
    private Pickup content;

    public Chest(final TileMap tm) {
	super(tm);
	sprite = new Sprite("resources/Sprites/Objects/chest.png");
	fallSpeed = 0.5;
	maxFallSpeed = 10;
	content = null;
	width = 65;
	height = 44;
	cwidth = 55;
	cheight = 34;
	opened = false;
	usable = true;
    }

    @Override public void use(Player player) {

        // The chest will open if it was closed
	if (!opened) {
	    sprite = new Sprite("resources/Sprites/Objects/openedChest.png");
	    height = 68;
	    dy = bounceSpeed;  // prevents the chest from getting stuck in the ground
	    cheight = 58;
	    opened = true;
	    content = new Pickup(tm);
	    content.setPosition(this.x, this.y);
	}
    }

    @Override public void update() {
	if (content != null) {
	    if (!content.hasBounced()) {
		content.bounce();
	    }
	}

	if(opened){
	    canUse = false;
	    usable = false;
	}

	super.update();
    }

    public Pickup getContent() {
	return content;
    }

    @Override public void draw(final Graphics2D g2d) {
	super.draw(g2d);
    }

    public boolean isOpened() {
	return opened;
    }
}
