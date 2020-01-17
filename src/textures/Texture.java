/*
Some of the image loading code here was taken from this video by YouTube user "Warmful Development":
https://www.youtube.com/watch?v=crOzRjzqI-o
 */

package textures;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.BufferUtils;
import toolbox.HeightMapGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A Texture usable by OpenGL from a PNG image file.
 */
public class Texture
{
    private int id;
    private int width;
    private int height;

    private ByteBuffer bufferRGBA;
    private float[] pixelCoordinates;

    /**
     * Constructs a OpenGL texture from a png file.
     *
     * @param filePath The file path to teh png image.
     */
    public Texture(String filePath)
    {
        BufferedImage bi;
        try
        {
            // reads image from filePath
            bi = ImageIO.read(new File(filePath));

            // pixel dimensions of the image
            width = bi.getWidth();
            height = bi.getHeight();

            // array of RGB data
            int[] pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);

            // blank byte buffer
            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);


            int counter = 0;
            pixelCoordinates = new float[width * height * 2];

            // fills the byte buffer with RGB pixel data
            for(int i = 0; i < width; i++)
            {
                for(int j = 0; j < height; j++)
                {

                    pixelCoordinates[counter] = i;
                    counter++;
                    pixelCoordinates[counter] = j;
                    counter++;


                    int pixel = pixels_raw[i * width + j];
                    pixels.put((byte)((pixel >> 16) & 0xFF));   // red
                    pixels.put((byte)((pixel >> 8) & 0xFF));    // green
                    pixels.put((byte)(pixel & 0xFF));           // blue
                    pixels.put((byte)((pixel >> 24) & 0xFF));   // alpha
                }
            }

            bufferRGBA = pixels;

            // OpenGL needs the buffer to be flipped for whatever reason
            pixels.flip();

            // bank texture id
            id = glGenTextures();

            // we are now using this texture ID
            glBindTexture(GL_TEXTURE_2D, id);

            // the texture will be rendered with GL_NEAREST algorithm
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            // puts the byte buffer into OpenGL at the textureID
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Gets the ID of the texture.
     *
     * @return The ID of the texture
     */
    public int getID()
    {
        return id;
    }

    public float[] getPixelCoordinates()
    {
        return pixelCoordinates;
    }

    public ByteBuffer getBufferRGBA()
    {
        return bufferRGBA;
    }
}
