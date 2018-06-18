package TileMap;

import Entity.Sprite;
import Main.GameComponent;
import TileMap.Tiles.EmptyTile;
import TileMap.Tiles.NormalTile;
import TileMap.Tiles.WallTile;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileMap
{
    // Map converting
    private String[][] map; // Convert the text file to a 2D-array
    private Tile[][] tileMap;   // Convert the 2D-array containing the map to a List with tiles

    private int numRows;
    private int numCols;
    private int width;
    private int height;

    // position
    private double x;
    private double y;

    private double tween;

    //bounds
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;

    private int rowOffset;
    private int colOffset;


    private int tileSize;

    public TileMap(int tileSize) {
	this.tileSize = tileSize;
	tween = 0.7;
    }

    // Loads a "map file" into a 2D-array
    public void loadMapFile(String mapPath) {

	try {
	    InputStream in = new FileInputStream(new File(mapPath));
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));

	    numCols = Integer.parseInt(br.readLine());
	    numRows = Integer.parseInt(br.readLine());
	    map = new String[numRows][numCols];
	    width = numCols * tileSize;
	    height = numRows * tileSize;

	    xmin = GameComponent.WIDTH - width;
	    xmax = 0;
	    ymin = GameComponent.HEIGHT - height;
	    ymax = 0;

	    String delims = "\\s+";
	    for (int row = 0; row < numRows; row++) {
		String line = br.readLine();
		String[] tokens = line.split(delims);
		for (int col = 0; col < numCols; col++) {
		    map[row][col] = tokens[col];
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    // Loads the world by creating all the tiles
    public void loadTileMap() {
	tileMap = new Tile[numRows][numCols];
	System.out.println(numCols + " " + numRows);
	// Loops through the text-map and adds a tile to the tile map for each non-empty tile.
	for (int y = 0; y < map.length; y++) {
	    for (int x = 0; x < map[y].length; x++) {

		switch (Integer.parseInt(map[y][x])) {
		    case 02:
			tileMap[y][x] =
				new EmptyTile(new Sprite("resources/Sprites/Tiles/normalTile.png"), x * tileSize, y * tileSize,
					      this);

			break;
		    case 01:
			tileMap[y][x] = new NormalTile(new Sprite("resources/Sprites/Tiles/normalTile2.png"), x * tileSize,
						       y * tileSize, this);
			break;
		    case 00:
			tileMap[y][x] =
				new WallTile(new Sprite("resources/Sprites/Tiles/normalTile.png"), x * tileSize, y * tileSize,
					     this);
			break;

		    default:
			tileMap[y][x] =
				new EmptyTile(new Sprite("resources/Sprites/Tiles/normalTile.png"), x * tileSize, y * tileSize,
					      this);
			System.out.println("Bad tile type! Creating empty tile!");

		}
	    }
	}

    }

    public void draw(Graphics2D g2d) {
	for (int i = 0; i < tileMap.length; i++) {
	    for (int j = 0; j < tileMap[i].length; j++) {
		Tile tile = tileMap[i][j];
		if (!(tile.isTransparent())) {
		    tile.draw(g2d);
		}
	    }
	}
    }

    public void setPosition(double x, double y) {
	this.x = x;
	this.y = y;

	//fixBounds();

	colOffset = (int) -this.x / tileSize;
	rowOffset = (int) -this.y / tileSize;
    }

    private void fixBounds() {
	if (x < xmin) x = xmin;
	if (y < xmin) y = ymin;
	if (x > xmax) x = xmax;
	if (y > ymax) y = ymax;
    }

    public Tile[][] getTiles() {
	return tileMap;
    }

    public int getTileSize() {
	return tileSize;
    }

    public int getNumCols() {
	return numCols;
    }

    public int getNumRows() {
	return numRows;
    }


    public double getX() {
	return x;
    }

    public double getY() {
	return y;
    }
}
