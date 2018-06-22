package HUD;

import Entity.Objects.Pickups.Pickup;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Inventory
{
    private List<Pickup> inventoryList;
    private InventoryButton button;

    private int maxSpace;
    // flags
    private boolean opened;
    private boolean full; // if there is no more space

    //position
    private int x;
    private int y;

    //dimensions
    private int width;
    private int height;

    public Inventory() {
	width = 100;
	height = 100;
	x = 50;
	y = 50;

	maxSpace = 2;

	inventoryList = new ArrayList<>();
	button = new InventoryButton(40, 40);
    }

    // Close and open the inventory ( decides if it should be drawn )
    public void toggle() {
	if (!opened) {
	    open();
	} else {
	    close();
	}
    }

    public void open() {
	opened = true;
    }

    public void close() {
	opened = false;
    }

    public void add(Pickup p) {
	inventoryList.add(p);
	button.pulse();
    }

    public void update() {
	button.update();
    }

    public void draw(Graphics2D g2d) {
	if (opened) {
	    g2d.setColor(Color.RED);
	    g2d.drawRect(x, y, width, height);
	}
	button.draw(g2d);

    }

    public boolean isFull() {
	return full;
    }

    public List<Pickup> getInventory() {
	return inventoryList;
    }

    public GameButton getButton() {
	return button;
    }
}
