package models;

public class ParticleData
{
    // vertices, indices, and texture uv data for square
    private static float[] points = {
            -0.7f, 1f, -2f,
            -1f, -1f, 0f,
            0.4f, 0f, 0.2f,
            1f, -1.1f, 0.5f
    };


    public static float[] getPoints()
    {
        return points;
    }


}
