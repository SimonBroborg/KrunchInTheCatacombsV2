package gui;

import entity.Sprite;
import state.GameStateManager;
import state.GameStates;

import java.awt.event.MouseEvent;

/**
 *
 */
@SuppressWarnings("MagicNumber")
public class MenuButton extends AbstractButton {
    private GameStates gameState;
    private int maxWidth;
    private int minWidth;

    protected GameStateManager gsm;

    /**
     * Creates a MenuButton object
     *
     * @param x      x-position of the object
     * @param y      y-position of the object
     * @param state     GameStates which the object will change the game to
     * @param sprite the sprite which the button has
     * @param gsm    a game state manager so the button can change game state
     */
    public MenuButton(int x, int y, GameStates state, Sprite sprite, GameStateManager gsm) {
        super(x, y);
        this.gsm = gsm;
        gameState = state;
        setSprite(sprite);
        maxWidth = width + 50;
        minWidth = width;
    }

    /**
     * Updates the buttons look and position
     */
    @Override
    public void update() {
        if (isHovered()) {
            width = maxWidth;
        } else {
            width = minWidth;
        }

    }

    /**
     * Change to the corresponding game state when the button is clicked
     *
     * @param e information about the event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (isHovered()) {
            gsm.setState(gameState);
        }
    }
}
