package HUD;

import Entity.Objects.Pickups.Pickup;
import Entity.Sprite;

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
    private int offsetX;
    private int offsetY;

    //dimensions
    private int width;
    private int height;


    public Inventory() {
	width = 100;
	height = 100;

	maxSpace = 2;

	inventoryList = new ArrayList<>();
	button = new InventoryButton(40, 40);

	offsetX = button.getX() + button.getWidth();
	offsetY = button.getY() + button.getHeight();
	x = 40;
	y = 40;
    }

    // Close and open the inventory ( decides if it should be drawn )
    public void toggle() {
	if (!opened) {
	    open();
	    button.setSprite(new Sprite("resources/Sprites/Objects/Chest/AChest1_opened.png"));
	} else {
	    close();
	    button.setSprite(new Sprite("resources/Sprites/Objects/Chest/AChest1.png"));
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
	    showInventory(g2d);
	}
	button.draw(g2d);
    }

    private void showInventory(Graphics2D g2d) {
	g2d.setColor(Color.RED);
	g2d.drawRect(x + offsetX, y, width, height);

	for (int i = 0; i < inventoryList.size(); i++) {
	    g2d.drawImage(inventoryList.get(i).getSprite().getImage(), x * (i + 1) + offsetX, y,
			  inventoryList.get(i).getSprite().getWidth(), inventoryList.get(i).getSprite().getHeight(), null);
	}
    }

    public boolean isFull() {
	return inventoryList.size() >= maxSpace;
    }

    public List<Pickup> getInventory() {
	return inventoryList;
    }

    public void setMaxSpace(final int maxSpace) {
	this.maxSpace = maxSpace;
    }

    public GameButton getButton() {
	return button;
    }
}
