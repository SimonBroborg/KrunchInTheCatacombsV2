package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class GameState
{
    protected GameStateManager gsm;

    public abstract void init();

    public abstract void update();

    public abstract void draw(Graphics2D g2d);

    public abstract void keyPressed(int k);

    public abstract void keyReleased(int k);

    public abstract void mouseClicked(MouseEvent e);

    public abstract void mouseMoved(MouseEvent e);
}
