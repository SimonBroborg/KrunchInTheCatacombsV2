package Entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class Sprite
{
    private BufferedImage image;
    private int width;
    private int height;

    public Sprite(String spritePath){
        image = null;
        try{
            image = ImageIO.read(new File(spritePath));
	}
	catch (IOException ignored){
            System.out.println("Sprite could not get loaded!");
	}

	if(image != null){
            width = image.getWidth();
            height = image.getHeight();
	}else{
            width = 0;
            height = 0;
	}
    }

    public BufferedImage getImage() {
	return image;
    }

    public int getHeight() {
	return height;
    }

    public int getWidth() {
	return width;
    }
}
