package Entity;

import Entity.Objects.Chest;
import Entity.Objects.GameObject;
import TileMap.TileMap;

import java.awt.*;
import java.util.List;

public class Player extends Entity
{
    private int useRange;
    private boolean using;

    public Player(TileMap tm) {
	super(tm);
	width = 40;
	height = 40;
	cwidth = 30;
	cheight = 30;

	moveSpeed = 0.7;
	maxSpeed = 3;
	stopSpeed = 0.4;
	fallSpeed = 0.5;
	maxFallSpeed = 10;
	jumpStart = -10;
	stopJumpSpeed = 0.3;

	useRange = 40;

	sprite = new Sprite("resources/Sprites/Player/player.png");

    }

    public void checkAct(List<GameObject> objects) {
	for (GameObject o : objects) {
	    if (using) {
	        if(facingRight) {
		    if (o.getX() > x && o.getX() < x + useRange && o.getY() > y - height / 2 && o.getY() < y + height / 2) {
			o.use(this);
		    }
		}else {

		    if (o.getX() < x && o.getX() > x - useRange && o.getY() > y - height / 2 && o.getY() < y + height / 2) {
		        o.use(this);
		    }
		}
	    }
	}
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
