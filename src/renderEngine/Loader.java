package renderEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import entities.Particles;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import textures.Texture;

/**
 * Loads data into a format usable boy OpenGL. This includes loading data into VAO
 * format and loading a png as a texture.
 */
public class Loader
{
    // Lists of data used to free the data after the engine closes.
    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();

    /**
     * Takes positions, texture uv coordinates, and indices and puts them in a VAO.
     *
     * @param positions The positions of vertices in 3D space.
     * @param textureCoords The uv coordinates for textures.
     * @param indices The order the vertices will be connected as triangles.
     * @return RawModel VAO data that can be rendered by the engine.
     */
    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices)
    {
        // creates a blank VAO
        int vaoID = createVAO();

        // binds the indices as a VBO to the VAO
        bindIndicesBuffer(indices);

        // vertex position VBO
        storeDataInAttributeList(0, 3, positions);

        // Texture UV VBO
        storeDataInAttributeList(1, 2, textureCoords);

        // Texture UV VBO
        storeDataInAttributeList(2, 3, normals);

        // unbinds the VAO so that new VAOs with different IDs can be made later.
        unbindVAO();

        return new RawModel(vaoID, positions.length);
    }



    public RawModel loadToVAO(float[] positions, float[] textureCoords, int[] indices)
    {
        // creates a blank VAO
        int vaoID = createVAO();

        // binds the indices as a VBO to the VAO
        bindIndicesBuffer(indices);

        // vertex position VBO
        storeDataInAttributeList(0, 3, positions);

        // Texture UV VBO
        storeDataInAttributeList(1, 2, textureCoords);

        // unbinds the VAO so that new VAOs with different IDs can be made later.
        unbindVAO();

        return new RawModel(vaoID, positions.length);
    }

    public Particles loadToVAO(float[] positions)
    {
        // creates a blank VAO
        int vaoID = createVAO();

        // vertex position VBO
        storeDataInAttributeList(0, 3, positions);

        // unbinds the VAO so that new VAOs with different IDs can be made later.
        unbindVAO();

        return new Particles(vaoID, 0);
    }

    public Particles loadToVAO(ByteBuffer buffer, float[] gridCoordinates, float[] heightMap)
    {
        // creates a blank VAO
        int vaoID = createVAO();

        // vertex position VBO
        storeByteBufferInAttributeList(0, 4, buffer);
        storeDataInAttributeList(1,2, gridCoordinates);
        storeDataInAttributeList(2,1, heightMap);

        // unbinds the VAO so that new VAOs with different IDs can be made later.
        unbindVAO();

        return new Particles(vaoID, gridCoordinates.length / 2);
    }


    /**
     * Creates a blank VAO.
     *
     * @return The ID of the new VAO.
     */
    private int createVAO()
    {
        // a blank VAO
        int vaoID = GL30.glGenVertexArrays();

        // for garbage collection
        vaos.add(vaoID);

        // this the the VAO the we will now add VBOs to
        GL30.glBindVertexArray(vaoID);

        return vaoID;
    }

    /**
     * Binds the VBO of indice data to teh current  VAO.
     *
     * @param indices Order the vertices will be connected into triangles
     */
    private void bindIndicesBuffer(int[] indices)
    {
        // a blank VBO
        int vboID = GL15.glGenBuffers();

        // for garbage collection
        vbos.add(vboID);

        // binds this blank buffer to the VAO as an element array buffer
        // (different than normal array buffer used for the other VBOs)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);

        // A buffer with the indices in it.
        IntBuffer buffer = storeDataInIntBuffer(indices);

        // Puts the indices into the element array buffer on teh VAO
        // also tells it to static draw, ie the indices will not be changed
        // at runtime
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    /**
     * Puts integer data into a buffer.
     *
     * @param data The integers that will be put into the buffer.
     * @return The filled buffer of integers.
     */
    private IntBuffer storeDataInIntBuffer(int[] data)
    {
        // A blank buffer of integers the size of the data.
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);

        // fills the buffer
        buffer.put(data);

        // OpenGL requires the buffer to be flipped.
        buffer.flip();

        return buffer;
    }

    /**
     * Stores data in a VBO on a VAO
     *
     * @param attributeNumber The VBO's order on the VAO
     * @param coordinateSize The number of elements per cycle, ie vertices have three, uvs have 2
     * @param data The actual data in the VBO
     */
    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data)
    {
        // a blank VBO
        int vboID = GL15.glGenBuffers();

        // for garbage collection
        vbos.add(vboID);

        // tells OpenGL that this is the buffer we will be using for now
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

        // puts data into a float buffer
        FloatBuffer buffer = storeDataInFloatBuffer(data);

        // Puts the buffer data into the VBO on the VAO, static draw = data won't change
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

        // tells the VAO how to interpret this VBO
        // ie with a certain attribute number and a number of coordinates/elements
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize,
                GL11.GL_FLOAT, false, 0, 0);

        // unbinds this buffer so we can use a new on later
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }


    private void storeByteBufferInAttributeList(int attributeNumber, int coordinateSize, ByteBuffer buffer)
    {
        // a blank VBO
        int vboID = GL15.glGenBuffers();

        // for garbage collection
        vbos.add(vboID);

        // tells OpenGL that this is the buffer we will be using for now
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

        // Puts the buffer data into the VBO on the VAO, static draw = data won't change
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

        // tells the VAO how to interpret this VBO
        // ie with a certain attribute number and a number of coordinates/elements
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize,
                GL11.GL_BYTE, false, 0, 0);

        // unbinds this buffer so we can use a new on later
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Puts floating point data into a buffer.
     *
     * @param data The data to be put into the buffer
     * @return the now filled buffer
     */
    private FloatBuffer storeDataInFloatBuffer(float[] data)
    {
        // a blank float buffer with the size of the data
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);

        // puts the floating point data into the buffer
        buffer.put(data);

        // OpenGL need the buffer to be flipped.
        buffer.flip();

        return buffer;
    }

    /**
     * Unbinds the VAO.
     */
    private void unbindVAO()
    {
        // unbinds the currently bound VAO
        GL30.glBindVertexArray(0);
    }

    /**
     * Takes a fileName to a png file and returns the OpenGL ID of the png
     * as a texture.
     *
     * @param fileName The name of the texture in the res folder.
     * @return OpenGL ID of the loaded texture.
     */
    public int loadTexture(String fileName)
    {
        // Gets a texture from a PNG image in the res folder
        Texture texture = new Texture("res/" + fileName + ".png");

        //  the OpenGL ID of the texture
        int textureID = texture.getID();

        // for garbage collection
        textures.add(textureID);

        return textureID;
    }

    public Texture getTexture(String fileName)
    {
        // Gets a texture from a PNG image in teh res folder
        Texture texture = new Texture("res/" + fileName + ".png");

        return texture;
    }

    /**
     * Garbage collection
     */
    public void cleanUp()
    {
        // Frees VAO memory
        for(int vao:vaos)
        {
            GL30.glDeleteVertexArrays(vao);
        }

        // Frees VBO memory
        for(int vbo:vbos)
        {
            GL15.glDeleteBuffers(vbo);
        }

        // Frees texture memory
        for(int texture:textures)
        {
            GL11.glDeleteTextures(texture);
        }
    }
}
