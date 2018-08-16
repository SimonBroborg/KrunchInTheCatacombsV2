package map;

import entity.Player;
import entity.Sprite;
import main.GameComponent;
import map.tiles.EmptyTile;
import map.tiles.NormalTile;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class TileMap
{
    // Map converting
    private String[][] textMap = null; // Convert the text file to a 2D-array
    private Tile[][] tileMap = null;   // Convert the 2D-array containing the textMap to a List with tiles

    // position
    private int x;
    private int y;

    private int tileWidth;
    private int tileHeight;

    private int width;
    private int height;

    private Map<Integer, String> spritePaths;
    private String mapPath;


    public TileMap(String mapPath) {
	this.mapPath = mapPath;
	spritePaths = new HashMap<>();
    }

    public void load() {
	MapParser parser = new MapParser();

	parser.loadTMXFile(mapPath);

	this.textMap = parser.getTextMap();

	this.width = parser.getWidth();
	this.height = parser.getHeight();
	this.tileWidth = parser.getTileWidth();
	this.tileHeight = parser.getTileHeight();
	this.spritePaths = parser.getSpritePaths();

	loadTileMap();
    }

    /**
     * Updates the tile textMap's position
     *
     * @param player the player object which the position of the textMap is based on
     */
    public void update(Player player) {
	setPosition(GameComponent.WIDTH / 2 * GameComponent.SCALE - player.getX(),
		    GameComponent.HEIGHT / 2 * GameComponent.SCALE - player.getY());
    }


    public void setTextMap(final String[][] textMap) {
	this.textMap = textMap;
    }

    /**
     * Loads the world by creating all the tiles
     */
    private void loadTileMap() {
	tileMap = new Tile[width][height];
	// Loops through the text-textMap and adds a tile to the tile textMap for each non-empty tile.
	for (int y = 0; y < textMap.length; y++) {
	    for (int x = 0; x < textMap[y].length; x++) {
		//System.out.println(spritePaths.get(Integer.valueOf(textMap[y][x])));

		if (spritePaths.get(Integer.valueOf(Integer.parseInt(textMap[y][x]) - 1)) != null) {
		    tileMap[y][x] =
			    new NormalTile(new Sprite(spritePaths.get(Integer.valueOf(Integer.parseInt(textMap[y][x]) - 1))),
					   x * tileWidth, y * tileHeight, this);
		}
		// If the tile doesn't exist a empty tile is created
		else {
		    tileMap[y][x] =
			    new EmptyTile(new Sprite("resources/Sprites/tiles/normalTile.png"), x * tileWidth, y * tileHeight,
					  this);
		}

		/*
		switch (Integer.parseInt(textMap[y][x])) {
		    case 0:
			tileMap[y][x] = new EmptyTile(new Sprite("resources/Sprites/tiles/normalTile.png"), x * tileWidth,
						      y * tileHeight, this);

			break;
		    case 1:
			tileMap[y][x] = new NormalTile(new Sprite("resources/Sprites/tiles/normalTile2.png"), x * tileWidth,
						       y * tileHeight, this);
			break;

                    default:
			tileMap[y][x] = new EmptyTile(new Sprite("resources/Sprites/tiles/normalTile.png"), x * tileWidth,
						      y * tileHeight, this);
			System.out.println("Bad tile type! Creating empty tile!");

		}
		*/

	    }
	}
    }

    public void draw(Graphics2D g2d) {
	for (Tile[] tiles : tileMap) {
	    for (Tile tile : tiles) {
		if (!(tile.isTransparent())) {
		    tile.draw(g2d);
		}
	    }
	}
    }

    public void setPosition(int x, int y) {
	this.x = x;
	this.y = y;
    }

    public Tile[][] getTiles() {
	return tileMap;
    }

    public int getTileWidth() {
	return tileWidth;
    }

    public int getTileHeight() {
	return tileHeight;
    }


    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }
}
