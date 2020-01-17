package Editor;

import entities.Camera;
import entities.Entity;
import entities.UntexturedEntity;
import renderEngine.DisplayManager;
import toolbox.*;

import java.util.ArrayList;
import java.util.Vector;

public class Editor {
    // ray from camera position to where mouse is pointing in world space
    private Vector3f currentRay;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;

    public Editor(Camera camera, Matrix4f projectionMatrix) {
        this.camera = camera;
        this.projectionMatrix = projectionMatrix;
        this.viewMatrix = Maths.createViewMatrix(camera);
    }

    public void selectEntity(ArrayList<Entity> entities) {
        Entity selectedEntity = null;
        float closestDistanceFromEntity = Float.POSITIVE_INFINITY;

        // ASSUMES ALL ENTITIES ARE CUBES
        for (int i = 0; i < entities.size(); i++) {
            Entity thisEntity = entities.get(i);
            boolean thisEntityIntersects = false;
            float closestDistanceFromSide = Float.POSITIVE_INFINITY;

            // thisEntities world space information
            Vector3f position = thisEntity.getPosition();
            float scaleX = thisEntity.getScaleX();
            float scaleY = thisEntity.getScaleY();
            float scaleZ = thisEntity.getScaleZ();
            float rotX = thisEntity.getRotX();
            float rotY = thisEntity.getRotY();
            float rotZ = thisEntity.getRotZ();

            Matrix4f transformationMatrix = Maths.createTransformationMatrix(position, rotX, rotY, rotZ,
                    scaleX, scaleY, scaleZ);

            // vertexXYZ, vertices in world space
            Vector3f vertexLeftTopBack = Matrix4f.mul(transformationMatrix,
                    new Vector4f(-0.5f, 0.5f, -0.5f, 1f));
            vertexLeftTopBack = Vector3f.add(vertexLeftTopBack, position);
            Vector3f vertexLeftTopFront = Matrix4f.mul(transformationMatrix,
                    new Vector4f(-0.5f, 0.5f, 0.5f, 1f));
            vertexLeftTopFront = Vector3f.add(vertexLeftTopFront, position);
            Vector3f vertexRightTopBack = Matrix4f.mul(transformationMatrix,
                    new Vector4f(0.5f, 0.5f, -0.5f, 1f));
            vertexRightTopBack = Vector3f.add(vertexRightTopBack, position);
            Vector3f vertexRightTopFront = Matrix4f.mul(transformationMatrix,
                    new Vector4f(0.5f, 0.5f, 0.5f, 1f));
            vertexRightTopFront = Vector3f.add(vertexRightTopFront, position);
            Vector3f vertexLeftBottomBack = Matrix4f.mul(transformationMatrix,
                    new Vector4f(-0.5f, -0.5f, -0.5f, 1f));
            vertexLeftBottomBack = Vector3f.add(vertexLeftBottomBack, position);
            Vector3f vertexLeftBottomFront = Matrix4f.mul(transformationMatrix,
                    new Vector4f(-0.5f, -0.5f, 0.5f, 1f));
            vertexLeftBottomFront = Vector3f.add(vertexLeftBottomFront, position);
            Vector3f vertexRightBottomBack = Matrix4f.mul(transformationMatrix,
                    new Vector4f(0.5f, -0.5f, -0.5f, 1f));
            vertexRightBottomBack = Vector3f.add(vertexRightBottomBack, position);
            Vector3f vertexRightBottomFront = Matrix4f.mul(transformationMatrix,
                    new Vector4f(0.5f, -0.5f, 0.5f, 1f));
            vertexRightBottomFront = Vector3f.add(vertexRightBottomFront, position);


            // for each side of the cube
            for (int j = 0; j < 6; j++)
            {
                //if(currentRay not parallel with plane)

                Vector3f vertexA;
                Vector3f vertexB;
                Vector3f vertexC;
                Vector3f vertexD;

                // ONLY THE TOP SIDE WORKS!!!!
                if (j == 0) {
                    // top side
                    vertexA = vertexLeftTopBack;
                    vertexB = vertexRightTopBack;
                    vertexC = vertexRightTopFront;
                    vertexD = vertexLeftTopFront;
                } else if (j == 1) {
                    // bottom side
                    vertexA = vertexLeftBottomBack;
                    vertexB = vertexRightBottomBack;
                    vertexC = vertexRightBottomFront;
                    vertexD = vertexLeftBottomFront;
                } else if (j == 2) {
                    // left side
                    vertexA = vertexLeftTopBack;
                    vertexB = vertexLeftTopFront;
                    vertexC = vertexLeftBottomFront;
                    vertexD = vertexLeftBottomBack;
                } else if (j == 3) {
                    // right side
                    vertexA = vertexRightTopBack;
                    vertexB = vertexRightBottomBack;
                    vertexC = vertexRightBottomFront;
                    vertexD = vertexRightTopFront;
                } else if (j == 4) {
                    // back side
                    vertexA = vertexLeftTopBack;
                    vertexB = vertexRightTopBack;
                    vertexC = vertexRightBottomBack;
                    vertexD = vertexLeftBottomBack;
                } else {
                    // front side
                    vertexA = vertexLeftTopFront;
                    vertexB = vertexRightTopFront;
                    vertexC = vertexRightBottomFront;
                    vertexD = vertexLeftBottomFront;
                }

                Vector3f edge1 = Vector3f.subtract(vertexD, vertexA);
                Vector3f edge2 = Vector3f.subtract(vertexB, vertexA);

                // MAKE SURE BOTH NORMALIZED EDGES AND THE NORMALIZED VIEWRAY AREN'T EQUAL/PARALLEL

                // PROBLEM: IT IS A PARAMETRIC EQUATION SO IT GETS THE CLOSEST OBJECT EVEN IF IT IS BEHIND YOU?
                // PROBLEM: CURRENTRAY CASTS FROM 0,0,0???

                Vector3f normal = Vector3f.cross(edge1, edge2);

                float N1p1 = normal.x * camera.position.x;
                float N1a1 = normal.x * vertexA.x;
                float N1d1 = normal.x * currentRay.x;

                float N2p2 = normal.y * camera.position.y;
                float N2a2 = normal.y * vertexA.y;
                float N2d2 = normal.y * currentRay.y;

                float N3p3 = normal.z * camera.position.z;
                float N3a3 = normal.z * vertexA.z;
                float N3d3 = normal.z * currentRay.z;

                float t = (N1p1 - N1a1 + N2p2 - N2a2 + N3p3 - N3a3) / (-N1d1 - N2d2 - N3d3);

                // intersection with infinite plane
                float intersectionX = camera.position.x + (currentRay.x * t);
                float intersectionY = camera.position.y + (currentRay.y * t);
                float intersectionZ = camera.position.z + (currentRay.z * t);

                Vector3f intersection = new Vector3f(intersectionX, intersectionY, intersectionZ);

                Vector3f bSubA = Vector3f.subtract(vertexB, vertexA);
                Vector3f dSubA = Vector3f.subtract(vertexD, vertexA);

                // MUST BE SOMETHING WRONG WITH THIS/THE ORDER OF ABCD
                //if(t >= 0) {
                    // if the point is in the rectangle on the plane
                    if (Vector3f.dot(vertexA, bSubA) <= Vector3f.dot(intersection, bSubA) &&
                            Vector3f.dot(intersection, bSubA) <= Vector3f.dot(vertexB, bSubA)) {
                        if (Vector3f.dot(vertexA, dSubA) <= Vector3f.dot(intersection, dSubA) &&
                                Vector3f.dot(intersection, dSubA) <= Vector3f.dot(vertexD, dSubA)) {
                            // ray intersects with entity
                            if (Vector3f.subtract(intersection, camera.position).length() < closestDistanceFromSide) {
                                closestDistanceFromSide = Vector3f.subtract(intersection, camera.position).length();
                                thisEntityIntersects = true;

                                System.out.println("intersected with cube " + i +" at t=" + t);
                            }
                        }
                    }
                //}
            }

            //SALKFMKSL BAD SET THING TO FLASE/ZERO
            if (thisEntityIntersects && closestDistanceFromEntity > closestDistanceFromSide) {
                closestDistanceFromEntity = closestDistanceFromSide;
                selectedEntity = thisEntity;
            }
        }

        // highlight closest entity with shader
        if (selectedEntity != null) {
            if(selectedEntity.isHighlighted)
                selectedEntity.isHighlighted = false;
            else
                selectedEntity.isHighlighted = true;
        }
        System.out.println();
    }






    public Vector3f getCurrentRay()
    {
        return currentRay;
    }

    private Vector3f calculateMouseRay(float mouseX, float mouseY)
    {
        Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        Vector3f worldRay = toWorldCoords(eyeCoords);
        return worldRay;
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords)
    {
        Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
        Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay = Vector3f.normalize(mouseRay);
        return mouseRay;
    }

    private Vector4f toEyeCoords(Vector4f clipCoords)
    {
        Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
        Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);

    }

    private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY)
    {
        float x = (2f * mouseX) / DisplayManager.WIDTH - 1f;
        float y = (2f * mouseY) / DisplayManager.HEIGHT - 1f;
        return new Vector2f(x, -y);
    }

    public void update(float mouseX, float mouseY)
    {
        viewMatrix = Maths.createViewMatrix(camera);
        currentRay = calculateMouseRay(mouseX, mouseY);
    }
}
