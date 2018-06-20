package Entity;

import Entity.Objects.Chest;
import Entity.Objects.GameObject;
import Entity.Objects.Pickups.Pickup;
import HUD.InventoryButton;
import TileMap.TileMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@SuppressWarnings("AssignmentToSuperclassField")
public class Player extends Entity
{
    private int useRange;
    private boolean using;
    private boolean pickingUp;

    public Player(TileMap tm) {
	super(tm);

	// dimensions
	width = 40;
	height = 90;
	cwidth = 30;
	cheight = 80;

	// movement
	moveSpeed = 0.7;
	maxSpeed = 3;
	stopSpeed = 0.4;
	fallSpeed = 0.5;
	maxFallSpeed = 10;
	jumpStart = -10;
	stopJumpSpeed = 0.3;

	// use
	useRange = 40;

	sprite = new Sprite("resources/Sprites/Player/player.png");

    }

    public void checkAct(List<GameObject> objects, InventoryButton b) {
	boolean canUse = false;
	List<GameObject> newObjects = new ArrayList<>();
	for (GameObject o : objects) {

	    if (o.isUsable()) {
		// Check if the object can be used
		if (((o.getX() > x && o.getX() < x + useRange) || (o.getX() < x && o.getX() > x - useRange)) &&
		    o.getY() > y - height / 2 && o.getY() < y + height / 2) {
		    canUse = true;
		}

		// Use the object
		if (canUse) {
		    if (using) {
			o.use(this, b);
			// Check if a chest is used and get its content
			if(o instanceof Chest){
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
	objects.addAll(newObjects);
	newObjects.clear();
    }

    public void update() {
	// update position
	getNextPosition();
	checkTileMapCollision();
	setPosition(xtemp, ytemp);
	if (right) facingRight = true;
	if (left) facingRight = false;
    }

    public void draw(Graphics2D g2d) {
	setMapPosition();
	super.draw(g2d);
    }

    public void setActing(final boolean acting) {
	this.using = acting;
    }
}
