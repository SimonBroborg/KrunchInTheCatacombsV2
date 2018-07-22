package state;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 *
 */
/*

 */
public interface GameState
{
    public void init();

    public void update(Point mousePos);

    public void draw(Graphics2D g2d);

    public void keyPressed(int k);

    public void keyReleased(int k);

    public void mouseClicked(MouseEvent e);

    public void mouseMoved(MouseEvent e);
}
