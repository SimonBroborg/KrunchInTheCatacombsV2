package Main;

import java.awt.*;

/**
 *
 */
public class Message
{
    private String text;
    private int width;
    private int height;
    private int x;
    private int y;
    private int ms;
    private int fontSize;

    public Message(String text, int fontSize){
        this.text = text;
        y = GameComponent.HEIGHT * GameComponent.SCALE;
        x = 0;
        ms = -1;
        width = 100;
        height = 50;
        this.fontSize = fontSize;
    }

    public void update(){
        y += ms;
    }

    public void draw(Graphics2D g2d){
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
        g2d.drawString(text, x, y);
        g2d.drawRect(x, y, width, height);
    }
}
