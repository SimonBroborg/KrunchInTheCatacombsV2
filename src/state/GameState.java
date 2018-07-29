package state;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 *
 */
public interface GameState
{
    /**
     * Initialize the state
     *
     * @param gsm the game state manager which controls the current state
     */
    public void init(GameStateManager gsm);

    /**
     * Updates everything in the state ( positions etc. )
     *
     * @param mousePos the position of the mouse
     */
    public void update(Point mousePos);

    /**
     * Draw's everything to the screen
     *
     * @param g2d the drawing object
     * @see Graphics2D
     */
    public void draw(Graphics2D g2d);

    public void keyPressed(int k);

    public void keyReleased(int k);

    public void mouseClicked(MouseEvent e);

    public void mouseMoved(MouseEvent e);


}
