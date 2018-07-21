package gui;

import entity.Sprite;
import state.GameStateManager;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 *
 */
/*

 */
@SuppressWarnings("MagicNumber")
public abstract class AbstractButton {
    // misc
    protected GameStateManager gsm = null;
    protected Sprite sprite = null;

    // position
    protected int x;
    protected int y;

    // dimensions
    protected int width;
    protected int height;

    protected boolean hovered;
    protected boolean pulsing;

    protected int growth;
    protected int growSpeed;
    protected int growthLimit;


    protected AbstractButton(int x, int y) {
        this.x = x;
        this.y = y;

        growSpeed = 2;
        growthLimit = 20;
    }

    public void update() {
        if (pulsing) {
            width += growSpeed;
            height += growSpeed;
            x -= growSpeed / 2;
            y -= growSpeed / 2;
            growth += growSpeed;

            if (growth >= growthLimit) {
                growSpeed = -growSpeed;
            }
            if (growth == 0) {
                pulsing = false;
                growSpeed = -growSpeed;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(sprite.getImage(), x, y, width, height, null);
    }

    public void checkHover(Point p) {
        hovered = p.x > x && p.x < x + width && p.y > y && p.y < y + height;
    }

    public void pulse() {
        pulsing = true;
    }

    public abstract void mouseClicked(MouseEvent e);

    public boolean isHovered() {
        return hovered;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setSprite(final Sprite sprite) {
        this.sprite = sprite;
        width = sprite.getWidth();
        height = sprite.getHeight();
    }
}

