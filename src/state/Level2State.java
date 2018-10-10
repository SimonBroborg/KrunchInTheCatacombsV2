package state;

import java.awt.event.KeyEvent;

/**
 *
 */
public class Level2State extends LevelState {
    public Level2State(String mapPath) {
        super(mapPath);

        loadLevel();
    }

    @Override
    public void loadLevel() {
        // Loads the tile map
        super.loadLevel();

      /*  // Create the different objects
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
        */
    }

    @Override
    public void keyPressed(int k) {
        super.keyPressed(k);
        if (k == KeyEvent.VK_4) {
            gsm.setState(GameStates.LEVEL_1_STATE);
        }
    }
}
