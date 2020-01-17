package toolbox;

import java.util.Random;

public class HeightMapGenerator
{
    public static int numOctaves = 7;
    public static float persistence = .5f;
    public static float lacunarity = 2;
    public static float initialScale = 2;

    /**
     * Generates a heightmap that is a 2d array of floats using Perlin noise.
     *
     * @param mapSize
     * @return
     */
    public static float[] GenerateHeightMap(int mapSize, Vector2f offset)
    {
        PerlinNoiseGenerator noiseGenerator = new PerlinNoiseGenerator(0);
        Random generator = new Random(1);


        float[] map = new float[mapSize * mapSize];

        Vector2f[] offsets = new Vector2f[numOctaves];
        for (int i = 0; i < numOctaves; i++)
        {
            offsets[i] = new Vector2f(((generator.nextFloat() * 2000) - 1000) + offset.x,
                    ((generator.nextFloat() * 2000) - 1000) + offset.y);
        }

        float minValue = Float.MAX_VALUE;
        float maxValue = Float.MIN_VALUE;

        for (int y = 0; y < mapSize; y++)
        {
            for (int x = 0; x < mapSize; x++)
            {
                float noiseValue = 0;
                float scale = initialScale;
                float weight = 1;
                for (int i = 0; i < numOctaves; i++)
                {
                    Vector2f p = new Vector2f(((x / (float) mapSize) * scale) + offsets[i].x,
                            ((y / (float) mapSize) * scale) + offsets[i].y);
                    noiseValue += noiseGenerator.noise2(p.x, p.y) * weight;
                    weight *= persistence;
                    scale *= lacunarity;
                }

                map[y * mapSize + x] = noiseValue;
                minValue = Math.min(noiseValue, minValue);
                maxValue = Math.max(noiseValue, maxValue);
            }
        }

        // Normalize
        if (maxValue != minValue)
        {
            for (int i = 0; i < map.length; i++)
            {
                map[i] = (map[i] - minValue) / (maxValue - minValue);
            }
        }

        return map;
    }
}
