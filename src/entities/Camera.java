package entities;

import toolbox.Vector3f;

import java.util.Vector;

/**
 * The user's perspective into the scene.
 */
public class Camera
{
    public Vector3f position;
    public float pitch;
    public float yaw;
    public float roll;

    public Vector3f up;
    public Vector3f front;
    private Vector3f target;
    public Vector3f right;

    /**
     * Blank constructor.
     */
    public Camera()
    {
        position = new Vector3f(0,0,0);
        target = new Vector3f(0,0,0);
        front = Vector3f.normalize(Vector3f.subtract(position, target));
        up = new Vector3f(0,1,0);
        right = Vector3f.normalize(Vector3f.cross(up, front));
        up = Vector3f.cross(front, right);
    }

    /**
     * Moves the camera forward.
     */
    public void moveForward()
    {
        position.x -= right.x;
        position.y -= right.y;
        position.z -= right.z;
    }

    /**
     * Moves the camera backward.
     */
    public void moveBackward()
    {
        position.x += right.x;
        position.y += right.y;
        position.z += right.z;
    }

    /**
     * Moves the camera right.
     */
    public void moveRight()
    {
        position.x += front.x;
        position.y += front.y;
        position.z += front.z;
    }

    /**
     * Moves the camera left.
     */
    public void moveLeft()
    {
        position.x -= front.x;
        position.y -= front.y;
        position.z -= front.z;
    }

    public void moveUp()
    {
        position.y += 1;
    }

    public void moveDown()
    {
        position.y -= 1;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public float getPitch()
    {
        return pitch;
    }

    public float getYaw()
    {
        return yaw;
    }

    public float getRoll()
    {
        return roll;
    }
}
