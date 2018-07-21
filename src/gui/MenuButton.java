package gui;

import entity.Sprite;
import state.GameStateManager;

import java.awt.event.MouseEvent;

/**
 *
 */
public class MenuButton extends AbstractButton {
    private GameStateManager gsm;
    private int gameState;
    private int maxWidth;
    private int minWidth;
    private int growSpeed;

    public MenuButton(int x, int y, int gs, Sprite sprite, GameStateManager gsm) {
        super(x, y);
        gameState = gs;
        this.gsm = gsm;
        setSprite(sprite);
        maxWidth = width + 50;
        minWidth = width;

        growSpeed = 5;
    }

    @Override
    public void update() {
        if (isHovered()) {
            width += growSpeed;
            x -= growSpeed / 2;
            if (width >= maxWidth) {
                width = maxWidth;
                x += growSpeed / 2;
            }
        } else {
            width -= growSpeed;
            x += growSpeed / 2;
            if (width <= minWidth) {
                width = minWidth;
                x -= growSpeed / 2;
            }
        }
        //super.update();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isHovered()) {
            gsm.setState(gameState);
        }
    }
}
