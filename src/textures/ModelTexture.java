package textures;

/**
 * A model's texture.
 */
public class ModelTexture
{
    private int textureID;

    /**
     * Constructs the texture of a model.
     *
     * @param id The OpenGL ID of the model's texture.
     */
    public ModelTexture(int id)
    {
        this.textureID = id;
    }

    /**
     * Gets the ID of the model's texture.
     *
     * @return OpenGL ID of the model's texture.
     */
    public int getID()
    {
        return textureID;
    }
}
