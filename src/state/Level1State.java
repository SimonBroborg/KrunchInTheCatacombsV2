package state;

import entity.Player;
import entity.objects.Chest;
import entity.objects.pickups.Gun;
import entity.objects.pickups.Pickaxe;
import main.FlashLight;

import java.awt.event.KeyEvent;

/**
 * Game state for level 1 of the game.
 */
@SuppressWarnings("MagicNumber")
public class Level1State extends LevelState {

    public Level1State(String mapPath) {
        super(mapPath);

	this.loadLevel();
    }

    @Override
    public void loadLevel() {
        // Loads the tile map
        super.loadLevel();

        // Create the different objects
        player = new Player(tm);
        player.setPosition(200, 150);
        fl = new FlashLight(tm, player);

        // Add objects to the level
        Chest c = new Chest(tm, new Pickaxe(tm, player.getInventory()));
        c.setPosition(200, player.getYMap());
        objects.add(c);
        c = new Chest(tm, new Gun(tm, player.getInventory()));
        c.setPosition(400, player.getYMap());
        objects.add(c);
    }

    @Override
    public void keyPressed(int k) {
        super.keyPressed(k);
        if (k == KeyEvent.VK_4) {
            gsm.setState(GameStates.LEVEL_2_STATE);
        }
    }
}
