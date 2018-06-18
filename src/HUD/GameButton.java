package HUD;

import Entity.Sprite;
import GameState.GameStateManager;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class GameButton
{
    // misc
    protected GameStateManager gsm;
    protected Sprite sprite;

    // position
    protected int x;
    protected int y;

    // dimensions
    protected int width;
    protected int height;

    protected boolean hovered;

    public GameButton(int x, int y, GameStateManager gsm){
        this.gsm = gsm;
        this.x = x;
        this.y = y;
    }

    public abstract void update();

    public void draw(Graphics2D g2d){
        if(hovered){
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(Color.BLUE);
            g2d.drawRect(x, y, width, height);
        }
        g2d.drawImage(sprite.getImage(), x, y, null);
    }

    public void checkHover(Point p){
        if(p.x > x && p.x < x + width && p.y > y && p.y < y+height){
            hovered = true;
        }
        else{
            hovered = false;
        }
    }

    public abstract void mouseClicked(MouseEvent e);

    public boolean isHovered() {
        return hovered;
    }
}
