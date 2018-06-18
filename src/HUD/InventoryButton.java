package HUD;

import Entity.Sprite;
import GameState.GameStateManager;

import java.awt.event.MouseEvent;

public class InventoryButton extends GameButton
{
    public InventoryButton(int x, int y, GameStateManager gsm) {
	super(x, y, gsm);
	sprite = new Sprite("resources/Sprites/Buttons/invButton.png");

	width = sprite.getWidth();
	height = sprite.getHeight();
    }

    @Override public void update() {

    }

    @Override public void mouseClicked(final MouseEvent e) {
	gsm.setState(GameStateManager.MENUSTATE);
    }


}
