package entity.objects;

import entity.objects.pickups.Pickup;
import entity.Sprite;
import map.TileMap;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
/*

 */
@SuppressWarnings({"MagicNumber", "AssignmentToSuperclassField"})
public class Chest extends GameObject {
    private boolean opened;
    private List<Pickup> content;


    public Chest(final TileMap tm, Pickup pickup) {
        super(tm);
        sprite = new Sprite("resources/Sprites/objects/Chest/AChest1.png");
        content = new ArrayList<>();
        addContent(pickup);
        //addContent(new Pickup(tm));
        //addContent(new Pickup(tm));
        //addContent(new Pickup(tm));

        // Movement
        fallSpeed = 0.5;
        maxFallSpeed = 10;

        // Dimensions
        width = 65;
        height = 44;
        cwidth = 55;
        cheight = 34;

        // Flags
        opened = false;
        usable = true;
    }

    public void addContent(Pickup p) {
        content.add(p);
    }

    @Override
    public void pickUp() {

        // The chest will open if it was closed
        if (!opened) {
            sprite = new Sprite("resources/Sprites/objects/Chest/AChest1_opened.png");
            height = 68;
            dy = bounceSpeed;  // prevents the chest from getting stuck in the ground
            cheight = 58;
            opened = true;

            for (int i = 0; i < content.size(); i++) {
                content.get(i).setPosition(this.x, this.y);
            }
            //content = new Pickup(tm);
            //content.setPosition(this.x, this.y);
        }
    }

    @Override
    public void update() {

        if (opened) {
            for (int i = 0; i < content.size(); i++) {
                if (!content.get(i).hasBounced()) {
                    content.get(i).exitChest();
                }
            }
            canUse = false;
            usable = false;
        }

        super.update();
    }

    public List<Pickup> getContent() {
        return content;
    }

}
