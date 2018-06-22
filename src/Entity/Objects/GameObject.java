package Entity.Objects;

import Entity.Entity;
import TileMap.TileMap;

import java.awt.*;

/**
 * Different useable objects on the map
 */
public abstract class GameObject extends Entity
{
    protected boolean remove;
    public GameObject(final TileMap tm) {
	super(tm);
    }

    public void update() {
	// update position
	getNextPosition();
	checkTileMapCollision();
	setPosition(xtemp, ytemp);
    }


    public abstract void use();

    @Override public void draw(final Graphics2D g2d) {
	setMapPosition();
	super.draw(g2d);
    }

    public boolean shouldRemove() {
	return remove;
    }
}
