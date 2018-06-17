package GameState;

import Entity.Player;
import Main.GameComponent;
import TileMap.Background;
import TileMap.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 *
 */
public class Level1State extends GameState
{
    private Player player;
    private TileMap tm;

    private Background bg;

    public Level1State(GameStateManager gsm) {
	this.gsm = gsm;
	init();
    }

    @Override public void init() {
	tm = new TileMap(40);
	player = new Player(tm);
	player.setPosition(100, 100);

	bg = new Background("resources/Backgrounds/space.png", 0.1);

	tm.loadMapFile("Resources/Maps/level1.txt");
	tm.loadTileMap();
    }

    @Override public void update() {
	player.update();
	tm.setPosition(GameComponent.WIDTH / 2 - player.getX(), GameComponent.HEIGHT / 2 - player.getY());
	bg.setPosition(tm.getX(), tm.getY());
    }

    @Override public void draw(final Graphics2D g2d) {
        bg.draw(g2d);
	tm.draw(g2d);
	player.draw(g2d);
    }

    @Override public void keyPressed(final int k) {
	if (k == KeyEvent.VK_A) player.setLeft(true);
	if (k == KeyEvent.VK_D) player.setRight(true);
	if (k == KeyEvent.VK_W) player.setUp(true);
	if (k == KeyEvent.VK_S) player.setDown(true);
	if (k == KeyEvent.VK_SPACE) player.setJumping(true);
    }

    @Override public void keyReleased(final int k) {
	if (k == KeyEvent.VK_A) player.setLeft(false);
	if (k == KeyEvent.VK_D) player.setRight(false);
	if (k == KeyEvent.VK_W) player.setUp(false);
	if (k == KeyEvent.VK_S) player.setDown(false);
	if (k == KeyEvent.VK_SPACE) player.setJumping(false);

    }
}
