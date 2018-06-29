package HUD;

import Entity.Sprite;

import java.awt.event.MouseEvent;

/**
 *
 */
public class InventoryButton extends GameButton
{
    public InventoryButton(int x, int y) {
	super(x, y);
	sprite = new Sprite("resources/Sprites/Objects/Chest/AChest1.png");

	width = sprite.getWidth();
	height = sprite.getHeight();

	growSpeed = 2;
    }


    @Override public void mouseClicked(final MouseEvent e) {
    }


}
