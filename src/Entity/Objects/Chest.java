package Entity.Objects;

import Entity.Objects.Pickups.Pickup;
import Entity.Player;
import Entity.Sprite;
import TileMap.TileMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Chest extends GameObject
{
    private boolean opened;
    private Pickup content;
    private List<Pickup> contents;


    public Chest(final TileMap tm) {
	super(tm);
	sprite = new Sprite("resources/Sprites/Objects/Chest/AChest1.png");
	contents = new ArrayList<>();
	addContent(new Pickup(tm));
	//addContent(new Pickup(tm));
	//addContent(new Pickup(tm));
	//addContent(new Pickup(tm));
	content = null;

	// Movement
	fallSpeed = 0.5;
	maxFallSpeed = 10;

	// Dimensions
	width = 65;
	height = 44;
	cwidth = 55;
	cheight = 34;

	// Flags
	opened = false;
	usable = true;
    }

    public void addContent(Pickup p) {
	contents.add(p);
    }

    @Override public void use(Player player) {

	// The chest will open if it was closed
	if (!opened) {
	    sprite = new Sprite("resources/Sprites/Objects/Chest/AChest1_opened.png");
	    height = 68;
	    dy = bounceSpeed;  // prevents the chest from getting stuck in the ground
	    cheight = 58;
	    opened = true;

	    for (int i = 0; i < contents.size(); i++) {
		contents.get(i).setPosition(this.x, this.y);
	    }
	    //content = new Pickup(tm);
	    //content.setPosition(this.x, this.y);
	}
    }

    @Override public void update() {

	if (opened) {
	    for (int i = 0; i < contents.size(); i++) {
		if (!contents.get(i).hasBounced()) {
		    contents.get(i).exitChest();
		}
	    }
	    canUse = false;
	    usable = false;
	}

	super.update();
    }

    public List<Pickup> getContent() {
	return contents;
    }

    @Override public void draw(final Graphics2D g2d) {
	super.draw(g2d);
    }

    public boolean isOpened() {
	return opened;
    }
}
