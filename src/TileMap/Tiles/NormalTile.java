package TileMap.Tiles;

import Entity.Sprite;
import TileMap.*;

public class NormalTile extends Tile
{
    public NormalTile(Sprite sprite, int x, int y, TileMap tm){
        super("Normal", x, y, tm);
        transparent = false;
        solid = true;

        this.sprite = sprite;
    }
}
