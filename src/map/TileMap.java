package map;

import entity.Sprite;
import map.tiles.EmptyTile;
import map.tiles.NormalTile;
import map.tiles.WallTile;

import java.awt.*;
import java.io.*;

/**
 *
 */
/*

 */
public class TileMap {
    // Map converting
    private String[][] map = null; // Convert the text file to a 2D-array
    private Tile[][] tileMap = null;   // Convert the 2D-array containing the map to a List with tiles

    private int numRows;
    private int numCols;

    // position
    private double x;
    private double y;

    private int tileSize;

    public TileMap(int tileSize) {
        this.tileSize = tileSize;
    }

    // Loads a "map file" into a 2D-array
    public void loadMapFile(String mapPath) {
        try {
            try (InputStream in = new FileInputStream(new File(mapPath))) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

                    numCols = Integer.parseInt(br.readLine());
                    numRows = Integer.parseInt(br.readLine());
                    map = new String[numRows][numCols];

                    String delims = "\\s+";
                    for (int row = 0; row < numRows; row++) {
                        String line = br.readLine();
                        String[] tokens = line.split(delims);
                        System.arraycopy(tokens, 0, map[row], 0, numCols);
                    }
                }
            }
        } catch (IOException e) {
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
                    case 2:
                        tileMap[y][x] =
                                new EmptyTile(new Sprite("resources/Sprites/tiles/normalTile.png"), x * tileSize, y * tileSize,
                                        this);

                        break;
                    case 1:
                        tileMap[y][x] = new NormalTile(new Sprite("resources/Sprites/tiles/normalTile2.png"), x * tileSize,
                                y * tileSize, this);
                        break;
                    case 0:
                        tileMap[y][x] =
                                new WallTile(new Sprite("resources/Sprites/tiles/normalTile.png"), x * tileSize, y * tileSize,
                                        this);
                        break;

                    default:
                        tileMap[y][x] =
                                new EmptyTile(new Sprite("resources/Sprites/tiles/normalTile.png"), x * tileSize, y * tileSize,
                                        this);
                        System.out.println("Bad tile type! Creating empty tile!");

                }
            }
        }
    }

    public void draw(Graphics2D g2d) {
        for(Tile[] tiles : tileMap){
            for(Tile tile : tiles){
                if(!(tile.isTransparent())){
                    tile.draw(g2d);
                }
            }
        }
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;

        //fixBounds();
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
