package Entity;

import TileMap.TileMap;

import java.awt.*;

public class Player extends Entity
{
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

	sprite = new Sprite("resources/Sprites/Player/player.png");

    }

    private void getNextPosition() {
	// movement
	if (left) {
	    dx -= moveSpeed;
	    if (dx < -maxSpeed) {
		dx = -maxSpeed;
	    }
	} else if (right) {
	    dx += moveSpeed;
	    if (dx > maxSpeed) {
		dx = maxSpeed;
	    }
	} else {
	    if (dx > 0) {
		dx -= stopSpeed;
		if (dx < 0) {
		    dx = 0;
		}
	    } else if (dx < 0) {
		dx += stopSpeed;
		if (dx > 0) {
		    dx = 0;
		}
	    }
	}
	// jumping
	if (jumping && !falling) {
	    dy = jumpStart;
	    falling = true;
	}
	// falling
	if (falling) {
	    dy += fallSpeed;
	    if (dy > 0) {
		jumping = false;
	    }
	    if (dy < 0 && !jumping) {
		dy += stopJumpSpeed;
	    }
	    if (dy > maxFallSpeed) {
		dy = maxFallSpeed;
	    }
	}
    }

    public void update() {
	// update position
	getNextPosition();
	checkTileMapCollision();
	setPosition(xtemp, ytemp);
    }

    public void draw(Graphics2D g2d) {
    setMapPosition();
    super.draw(g2d);
    }
}
