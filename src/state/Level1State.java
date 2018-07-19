package state;

import entity.objects.Chest;
import entity.objects.GameObject;
import entity.objects.pickups.Pickaxe;
import entity.objects.pickups.Pickup;
import entity.Player;
import gui.GameButton;
import main.FlashLight;
import main.GameComponent;
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
 *
 */
@SuppressWarnings("MagicNumber")
public class Level1State implements GameState {
    private Player player = null;
    private TileMap tm = null;

    private List<GameObject> objects = null;
    private List<Message> messages = null;
    private List<GameButton> buttons = null;

    private FlashLight fl = null;

    private boolean restart;

    private Background bg = null;

    private GameStateManager gsm;

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        //init();
    }

    private void loadLevel() {
        restart = false;

        // init lists
        objects = new ArrayList<>();
        messages = new ArrayList<>();
        buttons = new ArrayList<>();

        player = new Player(tm);
        player.setPosition(100, 100);

        // Add objects to the level
        Chest c = new Chest(tm, new Pickaxe(tm));
        c.setPosition(200, player.getY());
        objects.add(c);

        c = new Chest(tm, new Pickaxe(tm));
        c.setPosition(400, player.getY() - 200);
        objects.add(c);

        tm.loadMapFile("Resources/Maps/level1.txt");
        tm.loadTileMap();

        fl = new FlashLight(tm, player);
    }

    @Override
    public void init() {
        restart = false;

        tm = new TileMap(40);
        bg = new Background("resources/Backgrounds/background.jpg", 0);

        loadLevel();
    }

    @Override
    public void update() {
        player.update();
        fl.update();

        bg.update();

        // update lists
        updateObjects();
        updateMessages();
        updateButtons();

        tm.setPosition((double) GameComponent.WIDTH / 2 * GameComponent.SCALE - player.getX(),
                (double) GameComponent.HEIGHT / 2 * GameComponent.SCALE - player.getY());
        bg.setPosition(tm.getX(), tm.getY());

        player.checkAct(objects);

        if (restart) {
            loadLevel();
        }
    }

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

    private void updateButtons() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).update();
        }
    }

    private void updateObjects() {
        Iterator<GameObject> iter = objects.iterator();
        while (iter.hasNext()) {
            GameObject o = iter.next();
            o.update();
            if (o instanceof Pickup) {
                if (((Pickup) o).isPickedUp()) {
                    ((Pickup) o).addToInventory(player.getInventory());
                }
            }
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

    @Override
    public void keyPressed(final int k) {
        if (k == KeyEvent.VK_A) player.setLeft(true);
        if (k == KeyEvent.VK_D) player.setRight(true);
        if (k == KeyEvent.VK_SPACE) player.setJumping(true);

        if (k == KeyEvent.VK_E) player.setActing(true);
        if (k == KeyEvent.VK_ESCAPE) gsm.setState(GameStateManager.MENUSTATE);
        if (k == KeyEvent.VK_P) restart = true;
        if (k == KeyEvent.VK_TAB) player.getInventory().toggle();
    }

    @Override
    public void keyReleased(final int k) {
        if (k == KeyEvent.VK_A) player.setLeft(false);
        if (k == KeyEvent.VK_D) player.setRight(false);
        if (k == KeyEvent.VK_SPACE) player.setJumping(false);
        if (k == KeyEvent.VK_E) player.setActing(false);

        if (k == KeyEvent.VK_Q) player.useItem();

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
        player.useItem();
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).checkHover(e.getPoint());
        }
        fl.setTargetX(e.getX());
        fl.setTargetY(e.getY());
    }
}