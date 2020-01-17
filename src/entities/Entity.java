package entities;

import models.RawModel;
import models.TexturedModel;
import toolbox.Vector3f;

/**
 * An entity able to be rendered by the engine.
 */
public class Entity
{
    private TexturedModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scaleX, scaleY, scaleZ;

    public boolean isHighlighted = false;

    /**
     * Constructs a new entity.
     *
     * @param model The entity's model.
     * @param position The entity's position.
     * @param rotX The entity's rotation.
     * @param rotY The entity's rotation.
     * @param rotZ The entity's rotation.
     */
    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY,
                  float rotZ, float scaleX, float scaleY, float scaleZ)
    {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    /**
     * Moves the position a certain amount.
     *
     * @param dx change in x
     * @param dy change in y
     * @param dz change in z
     */
    public void increasePosition(float dx, float dy, float dz)
    {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    /**
     * Rotates a certain amount.
     *
     * @param dx change in x
     * @param dy change in y
     * @param dz change in z
     */
    public void increaseRotation(float dx, float dy, float dz)
    {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    public TexturedModel getModel()
    {
        return model;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public float getRotX()
    {
        return rotX;
    }

    public float getRotY()
    {
        return rotY;
    }

    public float getRotZ()
    {
        return rotZ;
    }

    public float getScaleX()
    {
        return scaleX;
    }

    public float getScaleY()
    {
        return scaleY;
    }

    public float getScaleZ()
    {
        return scaleZ;
    }

    public void setModel(TexturedModel model)
    {
        this.model = model;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public void setRotX(float rotX)
    {
        this.rotX = rotX;
    }

    public void setRotY(float rotY)
    {
        this.rotY = rotY;
    }

    public void setRotZ(float rotZ)
    {
        this.rotZ = rotZ;
    }

    public void setScaleX(float scale)
    {
        this.scaleX = scaleX;
    }

    public void setScaleY(float scale)
    {
        this.scaleY = scaleY;
    }

    public void setScaleZ(float scale)
    {
        this.scaleZ = scaleZ;
    }
}
