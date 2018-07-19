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


    public abstract void init();

    public abstract void update();

    public abstract void draw(Graphics2D g2d);

    public abstract void keyPressed(int k);

    public abstract void keyReleased(int k);

    public abstract void mouseClicked(MouseEvent e);

    public abstract void mouseMoved(MouseEvent e);
}
