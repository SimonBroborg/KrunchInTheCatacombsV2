package gui;

import entity.Sprite;
import state.GameStateManager;

import java.awt.event.MouseEvent;

/**
 *
 */
@SuppressWarnings("MagicNumber")
public class MenuButton extends AbstractButton {
    private int gameState;
    private int maxWidth;
    private int minWidth;

    protected GameStateManager gsm;
    private boolean active;

    /**
     * Creates a MenuButton object
     *
     * @param x      x-position of the object
     * @param y      y-position of the object
     * @param gs     GameState which the object will change the game to
     * @param sprite the sprite which the button has
     * @param gsm    a game state manager so the button can change game state
     */
    public MenuButton(int x, int y, int gs, Sprite sprite, GameStateManager gsm) {
        super(x, y);
        this.gsm = gsm;
        gameState = gs;
        setSprite(sprite);
        maxWidth = width + 50;
        minWidth = width;

        active = false;
    }

    /**
     * Updates the buttons look and position
     */
    @Override
    public void update() {
        if (isHovered()) {
            active = true;
            width = maxWidth;
        } else {
            width = minWidth;
            active = false;
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
            gsm.changeState(gameState);
        }
    }
}
