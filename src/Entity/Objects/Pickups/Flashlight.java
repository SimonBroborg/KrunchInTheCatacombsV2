package Entity.Objects.Pickups;

import HUD.Inventory;
import Main.GameComponent;
import TileMap.TileMap;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 *
 */
public class Flashlight extends Pickup {
    private Point2D center;
    private float[] dist;
    private Color[] colors;

    private RadialGradientPaint p;
    private float maxRadius;
    private float minRadius;
    private float radius;

    private boolean on;
    private float batteryPower;

    private float startTime;

    public Flashlight(final TileMap tm) {
        super(tm, "resources/Sprites/Player/player.png");
        dist = new float[]{0.0f, 1.0f};
        colors = new Color[]{new Color(0.0f, 0.0f, 0.0f, 0.0f), Color.black};

        center = new Point2D.Float(0, 0);
        maxRadius = 200;
        radius = maxRadius;
        minRadius = 60;
        batteryPower = 100;


        startTime = System.nanoTime();

        on = false;
    }

    public void update(int x, int y) {
        radius = maxRadius * batteryPower / 100;

        // Lower the battery power every second
        if(on) {
            float elapsed = (System.nanoTime() - startTime) / 1000000;
            if (elapsed > 1000) {
                batteryPower -= 0.8;
                startTime = System.nanoTime();
            }
        }else{
            startTime = System.nanoTime();
        }

        // Makes sure the flashlight will always have little power
        if (radius <= minRadius) {
            radius = minRadius;
        }
        // Turn off the flashlight (the radius has to be > 0 )
        if (!on) {
            radius = 1;
        }

        // Set the new position of the flashlight (based on the players position)
        center.setLocation((int) tm.getX() + x, (int) tm.getY() + y);
    }

    @Override
    public void addToInventory(Inventory inventory) {
        if (!inventory.contains(this)) {
            super.addToInventory(inventory);
        }
        else{
            System.out.println("You already own a flashlight");
        }
    }

    @Override
    public void use() {

        // Toggle the flashlight on and off
        if (on) {
            on = false;
        } else {
            on = true;
        }
    }

    public void draw(Graphics2D g2d) {
       /*p = new RadialGradientPaint(center, radius, dist, colors);
        g2d.setPaint(p);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));
        g2d.fillRect(0, 0, GameComponent.WIDTH * GameComponent.SCALE, GameComponent.HEIGHT * GameComponent.SCALE);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
*/
        super.draw(g2d);
    }
}
