package entity.objects.pickups;

import entity.Player;
import entity.objects.Projectile;
import gui.inventory.Inventory;
import map.TileMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static main.FlashLight.normalAbsoluteAngleDegrees;

/**
 * The pickaxe is an item which the player can use to break certain tiles.
 */
public class Gun extends Pickup {
    private List<Projectile> projectiles;

    /**
     * Creates a pickaxe.
     *
     * @param tm the tile map which helps the pickaxe to keep track of collisions.
     */
    public Gun(final TileMap tm, Inventory pInventory) {

        super(tm, "resources/Sprites/tiles/normalTile2.png", pInventory);
        projectiles = new ArrayList<>();

    }

    @Override
    public void use(Player player, Point point) {
        projectiles.add(new Projectile(player, (float) getAngle(new Point(player.getXMap(), player.getYMap()), point), tm));
    }


    @Override
    public void drawExtras(Graphics2D g2d) {
        Iterator<Projectile> iter = projectiles.listIterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.draw(g2d);
        }
    }

    @Override
    public void updateExtras() {
        Iterator<Projectile> iter = projectiles.iterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.update();
            if (p.shouldRemove()) {
                iter.remove();
            }
        }
    }

    public double getAngle(Point p1, Point p2) {
        double angle = Math.toDegrees(Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        return normalAbsoluteAngleDegrees(angle);
    }
}
