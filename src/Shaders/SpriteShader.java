package Shaders;

import entities.Camera;
import toolbox.Maths;
import toolbox.Matrix4f;

public class SpriteShader extends ShaderProgram
{
    private static final String VERTEX_FILE = "src/Shaders/spriteVertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/Shaders/spriteFragmentShader.glsl";

    /**
     * Constructs the shader program.
     */
    public SpriteShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Binds the positions VBO and the textureCoords VBO to the
     * shader program.
     */
    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    /**
     * Gets the location of all uniforms in the shader program.
     */
    @Override
    protected void getAllUniformLocations()
    {
    }
}
