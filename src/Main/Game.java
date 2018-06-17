package Main;

import Entity.Sprite;

import javax.swing.*;
import java.awt.*;

/**
 * Starting the game
 */
public class Game extends JFrame
{
    public Game() {}

    public void run() {
	init();
    }

    // Creates a window and adds a new GameComponent to it which handels the rest
    private void init() {
	GameComponent comp = new GameComponent();
	comp.run();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.run();
    }

}
