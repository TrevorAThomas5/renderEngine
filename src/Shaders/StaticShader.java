package Shaders;

import entities.Camera;
import toolbox.Maths;
import toolbox.Matrix4f;
import toolbox.Vector3f;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * A test shader.
 */
public class StaticShader extends ShaderProgram
{
      private static final String VERTEX_FILE = "src/Shaders/vertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/Shaders/fragmentShader.glsl";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;

    /**
     * Constructs the shader program.
     */
    public StaticShader()
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
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
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
        // Creates a view matrix from the camera.
        //Matrix4f matrix = Maths.createViewMatrix(camera);

        float radius = 5.0f;
        float camX = (float)Math.sin(glfwGetTime()) * radius;
        float camZ = (float)Math.cos(glfwGetTime()) * radius;
        Matrix4f matrix;
        matrix = Matrix4f.lookAt(new Vector3f(camX, 0, camZ), new Vector3f(0, 0, 0),
                new Vector3f(0, 1, 0));


        super.loadMatrix(location_viewMatrix, matrix);
    }

    /**
     * loads teh light position uniform
     *
     * @param lightPosition position of light source in 3D space
     */
    public void loadLightPosition(Vector3f lightPosition)
    {
        super.loadVector(location_lightPosition, lightPosition);
    }
}
