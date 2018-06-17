package TileMap.Tiles;

import Entity.Sprite;
import TileMap.Tile;
import TileMap.TileMap;

public class EmptyTile extends Tile
{
    public EmptyTile(Sprite sprite, int x, int y, TileMap tm) {
	super("Empty", x, y, tm);
	transparent = false;
	solid = false;

	this.sprite = sprite;
    }
}
