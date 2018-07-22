package state;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 *
 */
public class QuitState implements GameState {
    public QuitState() {
    }

    @Override
    public void init() {

    }

    @Override
    public void update(Point mousePos) {
        System.exit(0);
    }

    @Override
    public void draw(Graphics2D g2d) {

    }

    @Override
    public void keyPressed(int k) {

    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
