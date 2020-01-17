package toolbox;

import java.util.Vector;

/**
 * A vector in 3D space.
 */
public class Vector3f
{
    public float x, y, z;

    /**
     * Constructs a point in 3D space.
     *
     * @param x X position
     * @param y Y position
     * @param z Z position
     */
    public Vector3f(float x, float y, float z)
    {
        set(x, y, z);
    }

    public Vector3f()
    {
        set(0, 0,0 );
    }

    public static Vector3f normalize(Vector3f vector) {
        float l = vector.length();

        vector.x /= l;
        vector.y /= l;
        vector.z /= l;

        return vector;
    }

    public static Vector3f subtract(Vector3f left, Vector3f right)
    {
        return new Vector3f(left.x - right.x, left.y - right.y, left.z - right.z);
    }

    public static Vector3f add(Vector3f left, Vector3f right)
    {
        return new Vector3f(left.x + right.x, left.y + right.y, left.z + right.z);
    }

    public static Vector3f cross(Vector3f left, Vector3f right)
    {
        return new Vector3f(
                left.y * right.z - left.z * right.y,
                right.x * left.z - right.z * left.x,
                left.x * right.y - left.y * right.x
        );
    }

    public static float dot(Vector3f left, Vector3f right)
    {
        return (left.x * right.x) + (left.y * right.y) + (left.z * right.z);
    }

    public float length()
    {
        return (float)Math.sqrt((x*x) + (y*y) + (z*z));
    }

    public void set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString()
    {
        return this.x + " " + this.y + " " + this.z;
    }
}
