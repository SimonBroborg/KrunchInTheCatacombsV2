package gui.inventory;

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

    //position
    private int x;
    private int y;
    private int offsetX;

    //dimensions
    private int spotWidth;
    private int spotHeight;

    private List<InventorySpot> spots;

    public Inventory() {
        maxSpace = 3;
        spotWidth = 50;
        spotHeight = 50;

        spots = new ArrayList<>();

        inventory = new ArrayList<>();
        button = new InventoryButton(40, 40);

        offsetX = button.getX() + button.getWidth();
        x = 40;
        y = 40;

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
    }

    public void draw(Graphics2D g2d) {
        if (opened) {
            showInventory(g2d);
        }
        button.draw(g2d);
    }

    private void showInventory(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        //g2d.drawRect(x + offsetX, y, width, height);

        for (int i = 0; i < spots.size(); i++) {
            spots.get(i).draw(g2d);
            /*
            g2d.drawRect(x + (i * spotWidth) + offsetX, y, spotWidth, spotHeight);

            // Places the item sprite in the middle of the rect
            g2d.drawImage(inventory.get(i).getSprite().getImage(), x + (i * spotWidth) + offsetX + Math.abs(spotWidth - inventory.get(i).getSprite().getWidth()) / 2, y,
                    inventory.get(i).getSprite().getWidth(), inventory.get(i).getSprite().getHeight(), null);
            */
        }
    }

    public boolean isFull() {
        return inventory.size() >= maxSpace;
    }

    public AbstractButton getButton() {
        return button;
    }
}
