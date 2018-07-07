package Main;

import Entity.Sprite;
import GameState.GameStateManager;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
@SuppressWarnings("BusyWait")
public class GameComponent extends JComponent {
    // Dimensions
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    public static final int SCALE = 2;

    public static final int SCALED_HEIGHT = HEIGHT * SCALE;
    public static final int SCALED_WIDTH = WIDTH * SCALE;

    public volatile boolean running;
    private int FPS = 60;
    private long targetTime = 1000 / FPS;

    JFrame frame = new JFrame("Krunch in the Catacombs");


    // Game state manager
    private GameStateManager gsm = null;

    public GameComponent() {
        init();
    }

    private void init() {
        gsm = new GameStateManager();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Cursor gameCursor = Toolkit.getDefaultToolkit()
                .createCustomCursor(new Sprite("resources/Sprites/Misc/sketchedCursor.png").getImage(), new Point(0, 0),
                        "Game cursor");
        frame.setCursor(gameCursor);
        frame.setResizable(false);


        this.addMouseListener(new MouseHandler(gsm));
        this.addMouseMotionListener(new MouseHandler(gsm));
        frame.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        frame.setLayout(new BorderLayout());
        frame.addKeyListener(new InputHandler(gsm));
        frame.setFocusTraversalKeysEnabled(false); // this enables TAB to be listened to
        frame.add(this, BorderLayout.CENTER);
        frame.pack();
        frame.setFocusable(true);

        frame.setVisible(true);
    }

    public void run() {

        long start;
        long elapsed;
        long wait;
        running = true;

        // game loop
        while (running) {
            start = System.nanoTime();

            update();
            this.repaint();


            elapsed = System.nanoTime() - start;

            wait = targetTime - elapsed / 1000000;

            if (wait < 0) wait = 5;

            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        gsm.update();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gsm.draw(g2d);
        g2d.setColor(Color.RED);
    }
}
