package GameState;

import Entity.Objects.*;
import Entity.Objects.Pickups.Pickup;
import Entity.Player;
import Main.GameComponent;
import TileMap.*;

import java.awt.*;
import java.awt.event.KeyEvent;
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

    private Background bg;

    public Level1State(GameStateManager gsm) {
	this.gsm = gsm;
	//init();
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
	c = new Chest(tm);
		c.setPosition(850, player.getY() - 200);
		objects.add(c);
	tm.loadMapFile("Resources/Maps/level1.txt");
	tm.loadTileMap();
    }

    @Override public void update() {
	player.update();
	for (int i = 0; i < objects.size(); i++) {
	    GameObject o = objects.get(i);
	    o.update();
	    if (o instanceof Pickup) {
		if (((Pickup) o).isPickedUp()){
		    objects.remove(i);
		    i--;
		}
	    }
	} tm.setPosition(GameComponent.WIDTH / 2 * GameComponent.SCALE - player.getX(), GameComponent.HEIGHT / 2 * GameComponent.SCALE - player.getY());
	bg.setPosition(tm.getX(), tm.getY());

	player.checkAct(objects);

    }

    @Override public void draw(final Graphics2D g2d) {
	bg.draw(g2d);
	tm.draw(g2d);
	for (int i = 0; i < objects.size(); i++) {
	    objects.get(i).draw(g2d);
	}
	player.draw(g2d);

	g2d.setColor(new Color(0, 0, 0, 150));
	g2d.fillRect(0,0,GameComponent.WIDTH * GameComponent.SCALE, GameComponent.HEIGHT * GameComponent.SCALE);
    }

    @Override public void keyPressed(final int k) {
	if (k == KeyEvent.VK_A) player.setLeft(true);
	if (k == KeyEvent.VK_D) player.setRight(true);
	if (k == KeyEvent.VK_W) player.setUp(true);
	if (k == KeyEvent.VK_S) player.setDown(true);
	if (k == KeyEvent.VK_SPACE) player.setJumping(true);
	if (k == KeyEvent.VK_E) player.setActing(true);
	if(k == KeyEvent.VK_ESCAPE)gsm.setState(GameStateManager.MENUSTATE);
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
