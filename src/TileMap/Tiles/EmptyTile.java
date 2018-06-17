package TileMap.Tiles;

import TileMap.*;

public class EmptyTile extends Tile
{
    public EmptyTile() {
	super("", 0, 0, 0);
	transparent = true;
	solid = false;
    }
}
