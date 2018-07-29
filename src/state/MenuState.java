package state;

import entity.Sprite;
import gui.MenuButton;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The state of the game when the player is using the main menu
 */
public class MenuState implements GameState {
    private GameStateManager gsm = null;
    private List<MenuButton> buttons;

    public MenuState() {
        buttons = new ArrayList<>();
    }

    @Override
    public void init(GameStateManager gsm) {
        this.gsm = gsm;

        // Loads the first level
        MenuButton resumeButton = new MenuButton(100, 100, GameStates.LEVEL_1_STATE, new Sprite("resources/Sprites/Player/player.png"), gsm);
        buttons.add(resumeButton);

        // Exits the game
        MenuButton exitButton = new MenuButton(100, 150, GameStates.QUIT_STATE, new Sprite("resources/Sprites/tiles/normalTile.png"), gsm);
        buttons.add(exitButton);
    }

    @Override
    public void update(Point mousePos) {
        for (MenuButton b : buttons) {
            b.update();
        }
    }

    @Override
    public void draw(final Graphics2D g2d) {
        for (MenuButton b : buttons) {
            b.draw(g2d);
        }
    }

    @Override
    public void keyPressed(final int k) {
        switch (k) {
            case KeyEvent.VK_ESCAPE:
                gsm.setState(gsm.getPrevState());
                break;
            case KeyEvent.VK_S:
                break;

        }
    }

    @Override
    public void keyReleased(final int k) {

    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        for (MenuButton b : buttons) {
            b.mouseClicked(e);
        }
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        for (MenuButton b : buttons) {
            b.checkHover(e.getPoint());
        }
    }


}
