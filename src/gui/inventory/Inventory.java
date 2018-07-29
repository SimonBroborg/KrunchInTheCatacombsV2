package gui.inventory;

import entity.Player;
import entity.Sprite;
import entity.objects.pickups.Pickup;
import gui.AbstractButton;
import gui.InventoryButton;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@SuppressWarnings("MagicNumber")
public class Inventory {
    private List<Pickup> inventory;
    private InventoryButton button;

    private int maxSpace;

    // flags
    private boolean opened;

    private int offsetX;

    private int spotHeight;

    private List<InventorySpot> spots;
    private int activeSpot;

    public Inventory() {
        maxSpace = 3;

        //dimensions
        int spotWidth = 50;
        spotHeight = 50;

        spots = new ArrayList<>();

        inventory = new ArrayList<>();
        button = new InventoryButton(40, 40);

        offsetX = button.getX() + button.getWidth();

        //position
        int x = 40;
        int y = 40;

        // This counts as index 0, but I use "1" for more clarity
        activeSpot = 1;

        for (int i = 0; i < maxSpace; i++) {
            spots.add(new InventorySpot(x + offsetX + spots.size() * spotWidth, y, spotWidth, spotHeight));
        }
    }

    // Close and open the inventory ( decides if it should be drawn )
    public void toggle() {
        if (!opened) {
            open();
            button.setSprite(new Sprite("resources/Sprites/objects/Chest/AChest1_opened.png"));
        } else {
            close();
            button.setSprite(new Sprite("resources/Sprites/objects/Chest/AChest1.png"));
        }
    }

    public void open() {
        opened = true;
    }

    public void close() {
        opened = false;
    }

    public void add(Pickup p) {
        inventory.add(p);
        for (InventorySpot spot : spots) {
            if (spot.isEmpty()) {
                spot.setPickup(p);
                break;
            }
        }
        button.pulse();
    }

    public void update() {
        button.update();
        for (InventorySpot s : spots) {
            if (!s.isEmpty()) s.getPickup().updateExtras();
        }

        // Sets the active spot to active, and not-active for the rest
        for (int i = 0; i < spots.size(); i++) {
            spots.get(i).setActive(i == activeSpot - 1);
        }
    }

    public void draw(Graphics2D g2d) {
        for (InventorySpot s : spots) {
            if (!s.isEmpty()) s.getPickup().drawExtras(g2d);
        }

        if (opened) {
            showInventory(g2d);
        }
        button.draw(g2d);
    }

    public void useActive(Player player, Point point) {
        if (!spots.get(activeSpot - 1).isEmpty()) {
            spots.get(activeSpot - 1).getPickup().use(player, point);
        }
    }

    private void showInventory(Graphics2D g2d) {
        for (int i = 0; i < spots.size(); i++) {
            spots.get(i).draw(g2d);
        }
    }

    public void setActiveSpot(int activeSpot) {
        this.activeSpot = activeSpot;
    }

    public boolean isFull() {
        return inventory.size() >= maxSpace;
    }

    public AbstractButton getButton() {
        return button;
    }
}
