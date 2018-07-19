package gui;

import entity.Sprite;
import state.GameStateManager;

import java.awt.event.MouseEvent;

/**
 *
 */
@SuppressWarnings("AssignmentToSuperclassField")
public class InventoryButton extends GameButton {
    public InventoryButton(int x, int y) {
        super(x, y);
        sprite = new Sprite("resources/Sprites/objects/Chest/AChest1.png");

        width = sprite.getWidth();
        height = sprite.getHeight();

        growSpeed = 2;
    }


    @Override
    public void mouseClicked(final MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }


}
