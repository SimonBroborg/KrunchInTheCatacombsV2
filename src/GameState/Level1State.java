package GameState;

import Entity.Objects.Chest;
import Entity.Objects.GameObject;
import Entity.Player;
import Main.GameComponent;
import TileMap.Background;
import TileMap.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 */
public class Level1State extends GameState
{
    private Player player;
    private TileMap tm;

    private ArrayList<GameObject> objects;

    private Background bg;

    public Level1State(GameStateManager gsm) {
	this.gsm = gsm;
	init();
    }

    @Override public void init() {
	tm = new TileMap(40);
	player = new Player(tm);
	player.setPosition(100, 100);

	bg = new Background("resources/Backgrounds/cave.jpg", 0.1);

	objects = new ArrayList<>();
	Chest c = new Chest(tm);
	c.setPosition(200, player.getY());
	objects.add(c);


	c = new Chest(tm);
	c.setPosition(400, player.getY() - 200);
	objects.add(c);
	tm.loadMapFile("Resources/Maps/level1.txt");
	tm.loadTileMap();
    }

    @Override public void update() {
	player.update();
	for (GameObject o : objects) {
	    o.update();
	}
	tm.setPosition(GameComponent.WIDTH / 2 * GameComponent.SCALE - player.getX(), GameComponent.HEIGHT / 2);
	bg.setPosition(tm.getX(), tm.getY());

	player.checkAct(objects);
    }

    @Override public void draw(final Graphics2D g2d) {
	bg.draw(g2d);
	tm.draw(g2d);
	for (GameObject o : objects) {
	    o.draw(g2d);
	}
	player.draw(g2d);
    }

    @Override public void keyPressed(final int k) {
	if (k == KeyEvent.VK_A) player.setLeft(true);
	if (k == KeyEvent.VK_D) player.setRight(true);
	if (k == KeyEvent.VK_W) player.setUp(true);
	if (k == KeyEvent.VK_S) player.setDown(true);
	if (k == KeyEvent.VK_SPACE) player.setJumping(true);
	if (k == KeyEvent.VK_E) player.setActing(true);
    }

    @Override public void keyReleased(final int k) {
	if (k == KeyEvent.VK_A) player.setLeft(false);
	if (k == KeyEvent.VK_D) player.setRight(false);
	if (k == KeyEvent.VK_W) player.setUp(false);
	if (k == KeyEvent.VK_S) player.setDown(false);
	if (k == KeyEvent.VK_SPACE) player.setJumping(false);
	if (k == KeyEvent.VK_E) player.setActing(false);
    }
}
