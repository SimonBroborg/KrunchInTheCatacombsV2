package map;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class TMXParser
{
    // Dimensions
    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;

    // The TXM-file
    private Document doc = null;

    private String[][] textMap;

    public TMXParser() {
    }

    public void loadTMXFile(String tmxPath) {
	try {
	    File xmlFile = new File(tmxPath);

	    // sets up so the XML file can be read
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    doc = dBuilder.parse(xmlFile);

	    doc.getDocumentElement().normalize();

	    width = Integer.parseInt(getTagAttribute("map", "width"));
	    height = Integer.parseInt(getTagAttribute("map", "height"));
	    tileWidth = Integer.parseInt(getTagAttribute("map", "tilewidth"));
	    tileHeight = Integer.parseInt(getTagAttribute("map", "tileheight"));

	    // Set the size of the map (number of tiles)
	    textMap = new String[height][width];

	    // Get the numbers representing the tiles
	    Node tiles = doc.getElementsByTagName("data").item(0);

	    // the tile map as a string
	    String tileString = tiles.getTextContent();
	    // splits the string into lines
	    String[] lines = tileString.split("\n");

	    for (int i = 1; i < height + 1; i++) {
		String[] tokens = lines[i].split(",");
		textMap[i - 1] = tokens;
	    }

	    for (int i = 0; i < height; i++) {
		for (int j = 0; j < width; j++) {
		    System.out.print(textMap[i][j] + " ");
		}
		System.out.println("");
	    }

	    //System.out.println(tiles.getTextContent());

	    while (true) ;
	    /*NodeList nList = doc.getElementsByTagName("staff");

	    System.out.println("--------------------------------------");

	    for (int i = 0; i < nList.getLength(); i++) {
		Node nNode = nList.item(i);

		System.out.println("\nCurrent Element : " + nNode.getNodeName());

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		    Element eElement = (Element) nNode;

		    System.out.println("Staff id : " + eElement.getAttribute("id"));
		    System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
		    System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
		    System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
		    System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
		}
	    }
	    */
	} catch (ParserConfigurationException | IOException | SAXException e) {
	    e.printStackTrace();
	}

    }

    private String getTagAttribute(String tag, String attribute) {
	String value = "0";

	try {
	    value = doc.getElementsByTagName(tag).item(0).getAttributes().getNamedItem(attribute).getNodeValue();
	} catch (Exception e) {
	    System.out.println("Couldn't find the attribute \"" + attribute + "\" in the tag \"" + tag + "\" ");
	}
	return value;
    }

    public String[][] getTextMap() {
	return textMap;
    }

    /*
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

*/
    /**
     * Loads the world by creating all the tiles
     */
        /*
        public void loadTileMap() {
            tileMap = new Tile[numRows][numCols];
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
        */
}
