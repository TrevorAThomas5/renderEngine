package models;

/**
 * Data making up a plane.
 */
public class PlaneData
{
    // vertices, indices, and texture uv data for square
    private static float[] vertices = {
            -1f, 1f, 0f,    // V0
            -1f, -1f, 0f,   // V1
            1f, -1f, 0f,    // V2
            1f, 1f, 0f      // V3
    };
    private static float[] textureCoords = {
            0, 0,
            0, 1,
            1, 1,
            1, 0
    };
    private static int[] indices = {
            0, 1, 3,    // top left tri
            3, 1, 2,     // bottom right tri
    };

    public static float[] getVertices()
    {
        return vertices;
    }

    public static float[] getTextureCoords()
    {
        return textureCoords;
    }

    public static int[] getIndices()
    {
        return indices;
    }
}
