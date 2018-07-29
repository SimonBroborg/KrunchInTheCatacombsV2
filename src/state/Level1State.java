package state;

import entity.Player;
import entity.objects.Chest;
import entity.objects.UsableObject;
import entity.objects.pickups.Gun;
import entity.objects.pickups.Pickaxe;
import gui.AbstractButton;
import main.FlashLight;
import main.Message;
import map.Background;
import map.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Game state for level 1 of the game.
 */
@SuppressWarnings("MagicNumber")
public class Level1State implements GameState {
    private Player player = null;
    private TileMap tm;

    private List<UsableObject> objects = null;
    private List<Message> messages = null;
    private List<AbstractButton> buttons = null;

    private FlashLight fl = null;

    private boolean restart;

    private Background bg;

    private GameStateManager gsm = null;

    public Level1State() {
        restart = false;
        tm = new TileMap(40);
        bg = new Background("resources/Backgrounds/background.jpg", 0);

        loadLevel();
    }


    @Override
    public void init(GameStateManager gsm) {
        this.gsm = gsm;
    }

    private void loadLevel() {
        restart = false;

        // init lists
        objects = new ArrayList<>();
        messages = new ArrayList<>();
        buttons = new ArrayList<>();

        tm.loadMapFile("Resources/Maps/level1.txt");
        tm.loadTileMap();

        player = new Player(tm);
        player.setPosition(100, 100);
        fl = new FlashLight(tm, player);

        // Add objects to the level
        Chest c = new Chest(tm, new Pickaxe(tm, player.getInventory()));
        c.setPosition(200, player.getY());
        objects.add(c);
        c = new Chest(tm, new Gun(tm, player.getInventory()));
        c.setPosition(400, player.getY());
        objects.add(c);
    }


    @Override
    public void update(Point mousePos) {
        updateMouse(mousePos);

        player.update();
        fl.update();

        tm.update(player);

        bg.update();

        // update lists
        updateObjects();
        updateMessages();
        updateButtons();

        if (restart) {
            loadLevel();
        }
    }

    private void updateMouse(Point mousePos) {
        if (mousePos != null) {
            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).checkHover(mousePos);
            }

            for (int i = 0; i < objects.size(); i++) {
                objects.get(i).checkHover(mousePos);
            }
            // Sets the target point for the flashlight
            fl.setTargetX((int) mousePos.getX());
            fl.setTargetY((int) mousePos.getY());
        }
    }

    /**
     * Updates the messages in the message list
     */
    private void updateMessages() {
        Iterator<Message> iter = messages.iterator();
        while (iter.hasNext()) {
            Message m = iter.next();
            m.update();
            if (m.shouldRemove()) {
                iter.remove();
            }
        }
    }

    /**
     * Updates the buttons in the button list
     */
    private void updateButtons() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).update();
        }
    }


    /**
     * Updates the objects in the object list
     */
    private void updateObjects() {
        ListIterator<UsableObject> iter = objects.listIterator();
        while (iter.hasNext()) {
            UsableObject o = iter.next();
            o.update();
            if (o.shouldRemove()) {
                iter.remove();
            }
        }
    }


    @Override
    public void draw(final Graphics2D g2d) {
        bg.draw(g2d);
        tm.draw(g2d);

        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).draw(g2d);
        }

        fl.draw(g2d);
        player.draw(g2d);

        for (int i = 0; i < messages.size(); i++) {
            messages.get(i).draw(g2d);
        }

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).draw(g2d);
        }

        // Makes sure the inventory isn't covered by the darkness
        player.getInventory().draw(g2d);

        g2d.dispose();

    }

    /**
     * Things that happen when the player presses a keyboard button
     *
     * @param k the number of the key pressed
     */
    @Override
    public void keyPressed(final int k) {
        switch (k) {
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJumping(true);
                break;
            case KeyEvent.VK_E:
                player.activate(objects);
                break;
            case KeyEvent.VK_ESCAPE:
                gsm.setState(GameStates.MENU_STATE);
                break;
            case KeyEvent.VK_P:
                restart = true;
                break;
            case KeyEvent.VK_TAB:
                player.getInventory().toggle();
                break;

            case KeyEvent.VK_1:
                player.getInventory().setActiveSpot(1);
                break;
            case KeyEvent.VK_2:
                player.getInventory().setActiveSpot(2);
                break;
            case KeyEvent.VK_3:
                player.getInventory().setActiveSpot(3);
                break;
        }
    }

    /**
     * Things that heppen when the player releases a keyboard button
     *
     * @param k the number of the key pressed
     */
    @Override
    public void keyReleased(final int k) {
        if (k == KeyEvent.VK_A) player.setLeft(false);
        if (k == KeyEvent.VK_D) player.setRight(false);
        if (k == KeyEvent.VK_SPACE) player.setJumping(false);

        if (k == KeyEvent.VK_R) {
            Message m = new Message("WOW", 12);
            messages.add(m);
        }
    }

    /**
     * Things that happen when the player clicks a mouse button
     *
     * @param e information about the event
     */
    @Override
    public void mouseClicked(final MouseEvent e) {
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).isHovered()) {
                buttons.get(i).mouseClicked(e);
            }
        }
        player.useItem(e.getPoint());
    }

    /**
     * Things that happen when the player moves the mouse
     *
     * @param e information about the event
     */
    @Override
    public void mouseMoved(final MouseEvent e) {

    }


}
