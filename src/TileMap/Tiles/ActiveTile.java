package TileMap.Tiles;

import TileMap.Tile;

import java.awt.*;

public class ActiveTile extends Tile
{
    private boolean activated;
    public ActiveTile(final String type, final double x, final double y, final int tileSize) {
	super(type, x, y, tileSize);
	transparent = false;
	solid = false;
    }

    @Override public void draw(final Graphics2D g2d) {

    }
}
