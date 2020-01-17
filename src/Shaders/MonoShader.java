package Shaders;

import entities.Camera;
import toolbox.Maths;
import toolbox.Matrix4f;
import toolbox.Vector3f;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * A test shader.
 */
public class MonoShader extends ShaderProgram
{
    private static final String VERTEX_FILE = "src/Shaders/monoVertexShader.glsl";
    private static final String GEOMETRY_FILE = "src/Shaders/monoGeometryShader.glsl";
    private static final String FRAGMENT_FILE = "src/Shaders/monoFragmentShader.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_cubeColor;

    /**
     * Constructs the shader program.
     */
    public MonoShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE, GEOMETRY_FILE);
    }

    /**
     * Binds the position VBO to the shader program.
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
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_cubeColor = super.getUniformLocation("cubeColor");
    }

    /**
     * Loads the transformation matrix uniform on the shader program.
     *
     * @param matrix The transformation matrix.
     */
    public void loadTransformationMatrix(Matrix4f matrix)
    {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    /**
     * Loads the projection matrix uniform on the shader program.
     *
     * @param matrix The projection matrix.
     */
    public void loadProjectionMatrix(Matrix4f matrix)
    {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    /**
     * Loads the view matrix uniform on the shader program.
     *
     * @param camera The transformation matrix.
     */
    public void loadViewMatrix(Camera camera)
    {
        // crate a view matrix from the camera
        Matrix4f matrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, matrix);
    }

    public void loadCubeColor(Vector3f color)
    {
        super.start();
        super.loadVector(location_cubeColor, color);
        super.stop();
    }
}
