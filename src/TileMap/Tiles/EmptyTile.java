package TileMap.Tiles;

import TileMap.*;

public class EmptyTile extends Tile
{
    public EmptyTile(TileMap tm) {
	super("", 0, 0, tm);
	transparent = true;
	solid = false;
    }
}
