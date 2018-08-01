package entity.objects;

import entity.Sprite;
import entity.objects.pickups.Pickup;
import map.TileMap;

import java.util.ArrayList;
import java.util.List;

/**
 * The chest contains pickups for the player to collect.
 */
@SuppressWarnings({"MagicNumber", "AssignmentToSuperclassField"})
public class Chest extends UsableObject {
    private boolean opened;
    private List<Pickup> content;

    /**
     * Creates a chest object.
     *
     * @param tm     the tile map which helps the chest to keep track of collisions.
     * @param pickup specifies which type of pickup the chets contains.
     */
    public Chest(final TileMap tm, Pickup pickup) {
        super(tm);
        sprite = new Sprite("resources/Sprites/objects/Chest/AChest1.png");
        content = new ArrayList<>();
        addContent(pickup);

        // Movement
        fallSpeed = 0.5;
        maxFallSpeed = 10;

        // Dimensions
        width = sprite.getWidth();
        height = sprite.getHeight();
        cwidth = 55;
        cheight = 34;

        // Flags
        opened = false;
        activatable = true;
    }

    /**
     * Adds a new pickup object to the chests content list
     *
     * @param p the type of pickup which should be added.
     */
    public void addContent(Pickup p) {
        content.add(p);
    }

    /**
     * Opens the chest and reveals it's content.
     */
    @Override
    public void activate() {

        // The chest will open if it was closed
        if (!opened) {
            sprite = new Sprite("resources/Sprites/objects/Chest/AChest1_opened.png");
            height = 68;
            y -= 10;
            dy = bounceSpeed;  // prevents the chest from getting stuck in the ground
            cheight = 58;
            opened = true;

            for (int i = 0; i < content.size(); i++) {
                content.get(i).setPosition(this.x, this.y);
            }

        }
    }

    /**
     * Updates the chests position and checks if it's emptied.
     */
    @Override
    public void update() {
        if (opened) {
            for (int i = 0; i < content.size(); i++) {
                if (!content.get(i).hasBounced()) {
                    content.get(i).exitChest();
                }
            }
            activatable = false;
        }

        super.update();
    }

    /**
     * Get's the  content of the chest
     *
     * @return a list containing the content
     */
    public List<Pickup> getContent() {
        return content;
    }

}
