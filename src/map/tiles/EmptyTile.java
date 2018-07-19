package map.tiles;

import entity.Sprite;
import map.Tile;
import map.TileMap;

/**
 *
 */
public class EmptyTile extends Tile {
    public EmptyTile(Sprite sprite, int x, int y, TileMap tm) {
        super(false, true, sprite, x, y, tm);
    }
}
