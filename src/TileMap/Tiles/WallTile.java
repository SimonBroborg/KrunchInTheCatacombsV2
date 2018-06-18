package TileMap.Tiles;

import Entity.Sprite;
import TileMap.Tile;
import TileMap.TileMap;

public class WallTile extends Tile
{
    public WallTile(Sprite sprite, int x, int y, TileMap tm) {
	super("Wall", x, y, tm);
	transparent = false;
	solid = false;

	this.sprite = sprite;
    }
}
