package toolbox;

import entities.Camera;

/**
 * Some useful math functions.
 */
public class Maths
{
    /**
     * Creates a transformation matrix
     *
     * @param translation the movement
     * @param rx rotation
     * @param ry rotation
     * @param rz rotation
     * @param scaleX size change in X
     * @param scaleY size change in Y
     * @param scaleZ size change in Z
     * @return the transformation matrix
     */
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
                                                          float rz, float scaleX, float scaleY, float scaleZ)
    {
        Matrix4f matrix = new Matrix4f();

        Matrix4f.translate(translation, matrix, matrix);

        Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);

        Matrix4f.scale(new Vector3f(scaleX, scaleY, scaleZ), matrix, matrix);
        return matrix;
    }

    /**
     * Creates a view matrix
     *
     * @param camera camera object
     * @return the view matrix
     */
    public static Matrix4f createViewMatrix(Camera camera)
    {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0),
                viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0),
                viewMatrix, viewMatrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }
}

