package renderEngine;

import Shaders.*;
import entities.Camera;
import entities.Entity;
import entities.Particles;
import entities.UntexturedEntity;
import models.PlaneData;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import toolbox.Maths;
import toolbox.Matrix4f;
import toolbox.Vector3f;

/**
 * Renders models on the display.
 */
public class Renderer
{
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    // enables 3d projection
    private Matrix4f projectionMatrix;

    /**
     * Creates a new renderer able to draw 3D objects to the screen.
     *
     * @param staticShader Shader for multicolored objects
     * @param monoShader Shader for monocolored objects
     */
    public Renderer(StaticShader staticShader, MonoShader monoShader)
    {
        createProjectionMatrix();

        staticShader.start();
        staticShader.loadProjectionMatrix(projectionMatrix);
        staticShader.stop();

        monoShader.start();
        monoShader.loadProjectionMatrix(projectionMatrix);
        monoShader.stop();
    }

    public Matrix4f getProjectionMatrix()
    {
        return projectionMatrix;
    }


    /**
     * Clears the screen this frame.
     */
    public void prepare()
    {
        GL11.glClearColor(0.1f, 0.1f, 0.11f, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Renders an entity to the screen with the staticshader
     *
     * @param entity the entity
     * @param shader the static shader
     */
    public void render(Entity entity, StaticShader shader)
    {
        TexturedModel texturedModel  = entity.getModel();

        // The model data from the VAO, vertices and textureCoords/UVs
        RawModel model = texturedModel.getRawModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        // entities place in 3D space
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScaleX(), entity.getScaleY(),
                entity.getScaleZ());
        shader.loadTransformationMatrix(transformationMatrix);

        // uses the texture from the texturedModel
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getModelTexture().getID());

        // draws the textured model to the screen
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

        // unbinds the VBOs and VOA so new one can be used later
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    /**
     * Renders an entity to the screen with the spriteshader
     *
     * @param entity the entity
     * @param shader the sprite shader
     */
    public void render(Entity entity, SpriteShader shader)
    {
        shader.start();

        TexturedModel texturedModel  = entity.getModel();

        // The model data from the VAO, vertices and textureCoords/UVs
        RawModel model = texturedModel.getRawModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        // uses the texture from the texturedModel
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getModelTexture().getID());

        // draws the textured model to the screen
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

        // unbinds the VBOs and VOA so new one can be used later
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);

        shader.stop();
    }

    public void render(Entity entity, MonoShader shader, Camera camera)
    {
        shader.start();
        shader.loadViewMatrix(camera);

        TexturedModel texturedModel  = entity.getModel();

        // The model data from the VAO, vertices and textureCoords/UVs
        RawModel model = texturedModel.getRawModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        // the entities place in 3D space
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScaleX(),
                entity.getScaleY(), entity.getScaleZ());
        shader.loadTransformationMatrix(transformationMatrix);

        // uses the texture from the texturedModel
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getModelTexture().getID());

        // draws the textured model to the screen
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

        // unbinds the VBOs and VOA so new one can be used later
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);

        shader.stop();
    }

    /**
     * Renders an untextured entity with the monoshader
     *
     * @param entity the untextured entity
     * @param shader the shader
     */
    public void render(UntexturedEntity entity, MonoShader shader, Camera camera)
    {
        shader.start();
        shader.loadViewMatrix(camera);

        // The model data from the VAO, vertices and textureCoords/UVs
        RawModel model = entity.getModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        // the entities place in 3D space
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScaleX(),
                entity.getScaleY(), entity.getScaleZ());
        shader.loadTransformationMatrix(transformationMatrix);

        // draws the textured model to the screen
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

        // unbinds the VBOs and VOA so new one can be used later
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        shader.stop();
    }

    /**
     * Creates a projection matrix used for rendering 3D objects
     */
    private void createProjectionMatrix()
    {
        // variable in projection
        float aspectRatio = (float) DisplayManager.WIDTH / (float) DisplayManager.HEIGHT;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        // fills the matrix
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }
}
