package Entity.Objects;

import Entity.Player;
import Entity.Sprite;
import TileMap.TileMap;

import java.awt.*;

public class Chest extends GameObject
{
    private boolean opened;


    public Chest(final TileMap tm) {
	super(tm);
	sprite = new Sprite("resources/Sprites/Objects/chest.png");
	fallSpeed = 0.5;
	maxFallSpeed = 10;

	width = 65;
	height = 44;
	cwidth = 55;
	cheight = 34;
	opened = false;
    }

    @Override public void use(Player player) {
	if (!opened) {
	    sprite = new Sprite("resources/Sprites/Objects/openedChest.png");
	    height = 68;
	    dy = bounceSpeed;  // prevents the chest from getting stuck in the ground
	    cheight = 58;
	    opened = true;
	}
    }

    public boolean isOpened() {
	return opened;
    }
}
