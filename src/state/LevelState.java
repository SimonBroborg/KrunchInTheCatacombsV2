package state;

import entity.Player;
import entity.objects.GameObject;
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

/**
 * Abstract class which sets common things for level states
 */
public abstract class LevelState implements GameState {
    // The size of all the tiles
    private static final int TILE_SIZE = 40;

    protected Player player = null;
    protected TileMap tm;

    protected List<GameObject> objects = null;
    protected List<Message> messages = null;
    protected List<AbstractButton> buttons = null;

    protected String mapPath;

    protected FlashLight fl = null;

    protected boolean restart;

    protected boolean showInfo;
    protected Background bg;

    protected GameStateManager gsm = null;

    protected LevelState(String mapPath) {
        this.mapPath = mapPath;
        tm = new TileMap(TILE_SIZE);
        bg = new Background("resources/Backgrounds/background.jpg", 0);
    }

    public void init(GameStateManager gsm) {
        this.gsm = gsm;
    }

    public void loadLevel() {
        restart = false;

        // init lists
        objects = new ArrayList<>();
        messages = new ArrayList<>();
        buttons = new ArrayList<>();

        tm.loadMapFile(mapPath);
        tm.loadTileMap();
    }

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

        if (showInfo) {
            showInfo(g2d);
        }

        g2d.dispose();

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
        Iterator<GameObject> iter = objects.iterator();
        while (iter.hasNext()) {
            GameObject o = iter.next();
            o.update(player);
            if (o.shouldRemove()) {
                iter.remove();
            }
        }
    }

    public void showInfo(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.setColor(Color.white);

        g2d.drawString("Jumping: " + Boolean.toString(player.isJumping()), 10, 100);
        g2d.drawString("Falling: " + Boolean.toString(player.isFalling()), 10, 130);
        g2d.drawString("DX: " + (player.getDx()), 10, 160);
        g2d.drawString("DY: " + (player.getDy()), 10, 190);
        g2d.drawString("X: " + (player.getX()), 10, 220);
        g2d.drawString("Y: " + (player.getY()), 10, 250);
    }


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
            case KeyEvent.VK_F3:
                showInfo = !showInfo;
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


    @Override
    public void mouseClicked(final MouseEvent e) {
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).isHovered()) {
                buttons.get(i).mouseClicked(e);
            }
        }
        player.useItem(e.getPoint());
    }


    @Override
    public void mouseMoved(final MouseEvent e) {

    }

}
