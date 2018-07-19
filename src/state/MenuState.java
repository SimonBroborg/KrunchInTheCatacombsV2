package state;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * The state of the game when the player is using the main menu
 */
public class MenuState implements GameState
{
    private GameStateManager gsm;
    public MenuState(GameStateManager gsm){
        this.gsm = gsm;
        init();
    }

    @Override public void init() {

    }

    @Override public void update() {

    }

    @Override public void draw(final Graphics2D g2d) {

    }

     @Override public void keyPressed(final int k) {
        switch (k){
            case KeyEvent.VK_ESCAPE:
               	gsm.setState(GameStateManager.LEVEL1STATE);
                break;
            case KeyEvent.VK_SPACE:
                gsm.setState(GameStateManager.MENUSTATE);
                break;

        }
    }

    @Override public void keyReleased(final int k) {

    }

    @Override public void mouseClicked(final MouseEvent e) {

    }

    @Override public void mouseMoved(final MouseEvent e) {

    }
}
