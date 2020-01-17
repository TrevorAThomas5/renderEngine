package models;

/**
 * Data making up a cube.
 */
public class CubeData
{
    // vertices, indices, and texture uv data for cube
    private static float[] vertices = {
            // facing viewer
            -0.5f,0.5f,-0.5f,
            -0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,0.5f,-0.5f,

            // facing away from viewer
            -0.5f,0.5f,0.5f,
            -0.5f,-0.5f,0.5f,
            0.5f,-0.5f,0.5f,
            0.5f,0.5f,0.5f,

            // facing right
            0.5f,0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,0.5f,
            0.5f,0.5f,0.5f,

            // facing left
            -0.5f,0.5f,-0.5f,
            -0.5f,-0.5f,-0.5f,
            -0.5f,-0.5f,0.5f,
            -0.5f,0.5f,0.5f,

            // facing up
            -0.5f,0.5f,0.5f,
            -0.5f,0.5f,-0.5f,
            0.5f,0.5f,-0.5f,
            0.5f,0.5f,0.5f,

            // facing down
            -0.5f,-0.5f,0.5f,
            -0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,0.5f
    };
    private static float[] textureCoords = {
            0, 0,
            0, 0.3333f,
            0.3333f, 0.3333f,
            0.3333f, 0,

            0.3333f, 0,
            0.3333f, 0.333f,
            0.6666f, 0.3333f,
            0.6666f, 0,

            0.6666f, 0,
            0.6666f, 0.3333f,
            1, 0.3333f,
            1, 0,

            0, 0.3333f,
            0, 0.6666f,
            0.3333f, 0.6666f,
            0.3333f, 0.3333f,

            0.3333f, 0.3333f,
            0.3333f, 0.6666f,
            0.6666f, 0.6666f,
            0.6666f, 0.3333f,

            0.6666f, 0.3333f,
            0.6666f, 0.6666f,
            1, 0.6666f,
            1, 0.3333f
    };
    private static float[] normals = {
            0, 0, -1,
            0, 0, -1,
            0, 0, -1,
            0, 0, -1,

            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,

            1, 0, 0,
            1, 0, 0,
            1, 0, 0,
            1, 0, 0,

            -1, 0, 0,
            -1, 0, 0,
            -1, 0, 0,
            -1, 0, 0,

            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,

            0, -1, 0,
            0, -1, 0,
            0, -1, 0,
            0, -1, 0
    };
    private static int[] indices = {
            0,1,3,
            3,1,2,
            4,5,7,
            7,5,6,
            8,9,11,
            11,9,10,
            12,13,15,
            15,13,14,
            16,17,19,
            19,17,18,
            20,21,23,
            23,21,22
    };

    public static float[] getVertices()
    {
        return vertices;
    }

    public static float[] getTextureCoords()
    {
        return textureCoords;
    }

    public static float[] getNormals()
    {
        return normals;
    }

    public static int[] getIndices()
    {
        return indices;
    }
}
