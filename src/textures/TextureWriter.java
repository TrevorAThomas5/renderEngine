package textures;

import toolbox.Vector3f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TextureWriter
{

    public enum Side
    {
        FRONT, BACK, TOP, BOTTOM, LEFT, RIGHT
    }


    BufferedImage image;
    File file;


    public TextureWriter(BufferedImage image, String file)
    {
        this.image = image;
        this.file = new File("res/"+file+".png");
    }


    public void writeToFile()
    {
        try
        {
            ImageIO.write(image, "png", file);
        }
        catch(IOException e)
        {
            System.out.println("Failed to write image: " + e.getMessage());
        }
    }


    public void drawPixel(int x, int y, Color color, Side side)
    {
        int rgb = color.getRGB();

        int actualX, actualY;

        switch(side)
        {
            case FRONT:
                actualX = x / 3;
                actualY = y / 3;
                image.setRGB(actualX, actualY, rgb);
                break;
            case BACK:
                actualX = (x / 3) + (int)(0.3333f * image.getWidth());
                actualY = (y / 3);
                image.setRGB(actualX, actualY, rgb);
                break;
            case RIGHT:
                actualX = (x / 3) + (int)(0.6666f * image.getWidth());
                actualY = (y / 3);
                image.setRGB(actualX, actualY, rgb);
                break;
            case LEFT:
                actualX = (x / 3);
                actualY = (y / 3) + (int)(0.3333f * image.getHeight());
                image.setRGB(actualX, actualY, rgb);
                break;
            case TOP:
                actualX = (x / 3) + (int)(0.3333f * image.getWidth());
                actualY = (y / 3) + (int)(0.3333f * image.getHeight());
                image.setRGB(actualX, actualY, rgb);
                break;
            case BOTTOM:
                actualX = (x / 3) + (int)(0.6666f * image.getWidth());
                actualY = (y / 3) + (int)(0.3333f * image.getHeight());
                image.setRGB(actualX, actualY, rgb);
                break;
        }

        writeToFile();
    }
}
