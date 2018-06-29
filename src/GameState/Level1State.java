package GameState;

import Entity.Objects.Chest;
import Entity.Objects.GameObject;
import Entity.Objects.Pickups.Flashlight;
import Entity.Objects.Pickups.Pickaxe;
import Entity.Objects.Pickups.Pickup;
import Entity.Player;
import HUD.GameButton;
import Main.GameComponent;
import Main.Message;
import TileMap.Background;
import TileMap.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Level1State extends GameState
{
    private Player player;
    private TileMap tm;

    private List<GameObject> objects;
    private List<Message> messages;
    private List<GameButton> buttons;

    private boolean restart;

    private Background bg;

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
	c = new Chest(tm, new Flashlight(tm));
	c.setPosition(850, player.getY() - 200);
	objects.add(c);

	Message m = new Message("WOW", 12);
	messages.add(m);
	tm.loadMapFile("Resources/Maps/level1.txt");
	tm.loadTileMap();
    }

    @Override public void init() {
	restart = false;

	tm = new TileMap(40);
	bg = new Background("resources/Backgrounds/background.jpg", 0);

	loadLevel();
    }

    @Override public void update() {
	player.update();

	// update lists
	updateObjects();
	//updateMessages();
	updateButtons();

	tm.setPosition(GameComponent.WIDTH / 2 * GameComponent.SCALE - player.getX(),
		       GameComponent.HEIGHT / 2 * GameComponent.SCALE - player.getY());
	bg.setPosition(tm.getX(), tm.getY());

	player.checkAct(objects);

	if (restart) {
	    loadLevel();
	}
    }

    private void updateMessages() {
	for (int i = 0; i < messages.size(); i++) {
	    messages.get(i).update();
	}
    }

    private void updateButtons() {
	for (int i = 0; i < buttons.size(); i++) {
	    buttons.get(i).update();
	}
    }

    private void updateObjects() {
	for (int i = 0; i < objects.size(); i++) {
	    GameObject o = objects.get(i);
	    o.update();
	    if (o instanceof Pickup) {
		if (((Pickup) o).isPickedUp()) {
		    ((Pickup) o).addToInventory(player.getInventory());
		}
	    }
	    if (o.shouldRemove()) {
		objects.remove(i);
	    }
	}
    }

    @Override public void draw(final Graphics2D g2d) {
	bg.draw(g2d);
	tm.draw(g2d);

	for (int i = 0; i < objects.size(); i++) {
	    objects.get(i).draw(g2d);
	}

	player.draw(g2d);

	// flashlight around player effect 
	Point2D center = new Point2D.Float((int) tm.getX() + player.getX(), (int) tm.getY() + player.getY());
	float[] dist = { 0.0f, 1.0f };
	Color[] colors = { new Color(0.0f, 0.0f, 0.0f, 0.0f), Color.black };
	RadialGradientPaint p = new RadialGradientPaint(center, 200, dist, colors);
	g2d.setPaint(p);
	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .70f));
	g2d.fillRect(0, 0, GameComponent.WIDTH * GameComponent.SCALE, GameComponent.HEIGHT * GameComponent.SCALE);
	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));


	for (int i = 0; i < messages.size(); i++) {
	    messages.get(i).draw(g2d);
	}

	for (int i = 0; i < buttons.size(); i++) {
	    buttons.get(i).draw(g2d);
	}

	player.getInventory().draw(g2d);

	g2d.dispose();

    }

    @Override public void keyPressed(final int k) {
	if (k == KeyEvent.VK_A) player.setLeft(true);
	if (k == KeyEvent.VK_D) player.setRight(true);
	if (k == KeyEvent.VK_W) player.setUp(true);
	if (k == KeyEvent.VK_S) player.setDown(true);
	if (k == KeyEvent.VK_SPACE) player.setJumping(true);

	if (k == KeyEvent.VK_E) player.setActing(true);
	if (k == KeyEvent.VK_ESCAPE) gsm.setState(GameStateManager.MENUSTATE);
	if (k == KeyEvent.VK_P) restart = true;
	if (k == KeyEvent.VK_TAB) player.getInventory().toggle();
    }

    @Override public void keyReleased(final int k) {
	if (k == KeyEvent.VK_A) player.setLeft(false);
	if (k == KeyEvent.VK_D) player.setRight(false);
	if (k == KeyEvent.VK_W) player.setUp(false);
	if (k == KeyEvent.VK_S) player.setDown(false);
	if (k == KeyEvent.VK_SPACE) player.setJumping(false);
	if (k == KeyEvent.VK_E) player.setActing(false);
    }

    @Override public void mouseClicked(final MouseEvent e) {
	for (int i = 0; i < buttons.size(); i++) {
	    if (buttons.get(i).isHovered()) {
		buttons.get(i).mouseClicked(e);
	    }
	}

    }

    @Override public void mouseMoved(final MouseEvent e) {
	for (int i = 0; i < buttons.size(); i++) {
	    buttons.get(i).checkHover(e.getPoint());
	}
    }
}
