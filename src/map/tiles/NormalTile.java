package map.tiles;

import entity.Sprite;
import map.Tile;
import map.TileMap;


/**
 *
 */
public class NormalTile extends Tile
{
    public NormalTile(Sprite sprite, int x, int y, TileMap tm) {
        super(true, false, sprite, x, y, tm);
    }
}

