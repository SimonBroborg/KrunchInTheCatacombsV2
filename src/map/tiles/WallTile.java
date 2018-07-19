package map.tiles;

import entity.Sprite;
import map.Tile;
import map.TileMap;


/**
 *
 */
public class WallTile extends Tile {
    public WallTile(Sprite sprite, int x, int y, TileMap tm) {
        super(false, false, sprite, x, y, tm);
    }
}
