package Shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import toolbox.Matrix4f;
import toolbox.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

/**
 * Combined vertex and fragment shader program. Basically the rendering algorithm of the GPU.
 */
public abstract class ShaderProgram
{
    private int programID;
    private int vertexShaderID;
    private int geometryShaderID;
    private int fragmentShaderID;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    /**
     * Constructs a new shader program from a GLSL vertex and fragment shader.
     *
     * @param vertexFile Path to the vertex shader.
     * @param fragmentFile Path to the fragment shader.
     */
    public ShaderProgram(String vertexFile, String fragmentFile, String geometryFile)
    {
        // compiles ths two GLSL shaders
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        geometryShaderID = loadShader(geometryFile, GL32.GL_GEOMETRY_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);

        // a blank  full shader program
        programID = GL20.glCreateProgram();

        // attaches the vertex and fragment shaders to the shader program
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, geometryShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();

        // links the shader program to the program ID
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);

        getAllUniformLocations();
    }

    /**
     * Constructs a new shader program from a GLSL vertex and fragment shader.
     *
     * @param vertexFile Path to the vertex shader.
     * @param fragmentFile Path to the fragment shader.
     */
    public ShaderProgram(String vertexFile, String fragmentFile)
    {
        // compiles ths two GLSL shaders
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);

        // a blank  full shader program
        programID = GL20.glCreateProgram();

        // attaches the vertex and fragment shaders to the shader program
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();

        // links the shader program to the program ID
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);

        getAllUniformLocations();
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName)
    {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    /**
     * Compiles a vertex and fragment shader.
     *
     * @param file Path to the shader file.
     * @param type vertex or fragment.
     * @return ID of the shader.
     */
    private static int loadShader(String file, int type)
    {
        StringBuilder shaderSource = new StringBuilder();

        try
        {
            // reads from the shader file
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            // appends newlines
            while ((line = reader.readLine()) != null)
            {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        }
        catch(IOException e)
        {
            System.err.println("Could not read file.");
            e.printStackTrace();
            System.exit(-1);
        }

        // a blank shader
        int shaderID = GL20.glCreateShader(type);

        // compiles the GLSL shader
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        // if the shader failed to compile
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
        {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader.");
            System.exit(-1);
        }

        return shaderID;
    }

    /**
     * Uses this fully built shader program.
     */
    public void start()
    {
        GL20.glUseProgram(programID);
    }

    /**
     * Stops this shader program.
     */
    public void stop()
    {
        GL20.glUseProgram(0);
    }

    /**
     * garbage collection
     */
    public void cleanUp()
    {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);

        if(geometryShaderID != 0)
            GL20.glDetachShader(programID, geometryShaderID);

        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);

        if(geometryShaderID != 0)
            GL20.glDeleteShader(geometryShaderID);

        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    /**
     * Binds multiple attributes to the shader program.
     */
    protected abstract void bindAttributes();

    /**
     * Binds an attribute of the VAO to the shader, ie sets the "in"
     * variables
     *
     * @param attribute The number of the VBO on the VAO
     * @param variableName The name of the attribute in the shader program.
     */
    protected void bindAttribute(int attribute, String variableName)
    {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    /**
     * Loads a float uniform to the shader program.
     *
     * @param location location on shader program
     * @param value float value
     */
    protected void loadFloat(int location, float value)
    {
        GL20.glUniform1f(location, value);
    }

    /**
     * Loads a vector uniform to the shader program.
     *
     * @param location location on the shader program
     * @param vector vector value
     */
    protected void loadVector(int location, Vector3f vector)
    {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    /**
     * Loads a boolean uniform to the shader program.
     *
     * @param location location on the shader program
     * @param value boolean value
     */
    protected void loadBoolean(int location, boolean value)
    {
        float toLoad = 0;
        if(value)
            toLoad = 1;
        GL20.glUniform1f(location, toLoad);
    }

    /**
     * Loads a mat4 uniform to the shader program.
     *
     * @param location location on the shader program
     * @param matrix loaded matrix
     */
    protected void loadMatrix(int location, Matrix4f matrix)
    {
        matrix.store(matrixBuffer);
        matrixBuffer.flip();

        GL20.glUniformMatrix4fv(location, false, matrixBuffer);
    }
}
