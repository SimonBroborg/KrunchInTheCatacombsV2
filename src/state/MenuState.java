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
    private GameStateManager gsm;
    private List<MenuButton> buttons = null;

    public MenuState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    @Override
    public void init() {
        buttons = new ArrayList<>();
        MenuButton b = new MenuButton(100, 100, GameStateManager.LEVEL1STATE, new Sprite("resources/Sprites/Player/player.png"), gsm);
        buttons.add(b);
        MenuButton b1 = new MenuButton(100, 150, GameStateManager.LEVEL1STATE, new Sprite("resources/Sprites/tiles/normalTile.png"), gsm);
        buttons.add(b1);
        MenuButton b2 = new MenuButton(100, 200, GameStateManager.LEVEL1STATE, new Sprite("resources/Sprites/tiles/normalTile2.png"), gsm);
        buttons.add(b2);
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
                gsm.setState(GameStateManager.LEVEL1STATE);
                break;
            case KeyEvent.VK_SPACE:
                gsm.setState(GameStateManager.MENUSTATE);
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
