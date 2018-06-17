package GameState;


import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * The state of the game when the player is using the main menu
 */
public class MenuState extends GameState
{

    public MenuState(GameStateManager gsm){
        this.gsm = gsm;
        init();
    }

    @Override public void init() {

    }

    @Override public void update() {

    }

    @Override public void draw(final Graphics2D g) {

    }

     @Override public void keyPressed(final int k) {
        switch (k){
            case KeyEvent.VK_ESCAPE:
               	gsm.setState(GameStateManager.LEVEL1STATE);
                break;

        }
    }

    @Override public void keyReleased(final int k) {

    }
}
