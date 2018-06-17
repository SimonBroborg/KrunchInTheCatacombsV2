package Entity.Objects;

import Entity.*;
import TileMap.TileMap;

import java.awt.*;

/**
 * Different useable objects on the map
 */
public class GameObject extends Entity
{
    public GameObject(final TileMap tm) {
	super(tm);
    }

    public void update() {
	// update position
	getNextPosition();
	checkTileMapCollision();
	setPosition(xtemp, ytemp);
    }


    public void use(Player player){}

    @Override public void draw(final Graphics2D g2d) {
	setMapPosition();
	super.draw(g2d);
    }
}
