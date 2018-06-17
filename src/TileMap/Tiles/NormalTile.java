package TileMap.Tiles;

import Entity.Sprite;
import TileMap.*;

public class NormalTile extends Tile
{
    public NormalTile(Sprite sprite, int x, int y, int tileSize){
        super("Normal", x, y, tileSize);
        transparent = false;
        solid = true;

        this.sprite = sprite;
    }
}
