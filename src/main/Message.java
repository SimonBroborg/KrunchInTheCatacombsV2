package main;

import java.awt.*;

/**
 *
 */
@SuppressWarnings("MagicNumber")
public class Message
{
    private String text;

    // Dimensions
    private float width;
    private float height;
    private float maxHeight;

    private boolean remove;

    // Position
    private float x;
    private float y;

    private int fontSize;

    private float startTime;

    public Message(String text, int fontSize){
        this.text = text;
        y = (float)GameComponent.HEIGHT * GameComponent.SCALE / 2  - 70;
        x = 20;

        width = 100;
        height = 0;
        maxHeight = 50;

        this.fontSize = fontSize;
    }

    public void update(){

        if(height >= maxHeight){
            height = maxHeight;

            float elapsed = (System.nanoTime() - startTime) / 1000000;
            if( elapsed > 5000){
                remove = true;
            }
        }else{
            height += 2;
            y -= 1;

            startTime = System.nanoTime();
        }

    }

    public void draw(Graphics2D g2d){
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
        g2d.drawString(text, x, y);
        g2d.drawRect((int)x, (int) y, (int) width, (int) height);
    }

    public boolean shouldRemove(){
        return remove;
    }
}
