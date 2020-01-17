package models;

import textures.ModelTexture;

/**
 * A model with a texture.
 */
public class TexturedModel
{
    private RawModel rawModel;
    private ModelTexture texture;

    /**
     * Constructs a new TexturedModel.
     *
     * @param model The RawModel data
     * @param texture its texture
     */
    public TexturedModel(RawModel model, ModelTexture texture)
    {
        this.rawModel = model;
        this.texture = texture;
    }

    public RawModel getRawModel()
    {
        return rawModel;
    }

    public ModelTexture getModelTexture()
    {
        return texture;
    }
}
