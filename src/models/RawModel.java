package models;

/**
 * Raw data for a model, including the amount of vertices and the ID of the VAO.
 * The VAO includes vertex positions, indice buffer, and texture uv coordinates.
 */
public class RawModel
{
    private int vaoID;
    private int vertexCount;

    /**
     * Constructs a RawModel that has a vaoID and a number of vertices.
     *
     * @param vaoID ID to the VAO with the mesh data of the model.
     * @param vertexCount Number of vertices in the mesh.
     */
    public RawModel(int vaoID, int vertexCount)
    {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    /**
     * Gets the ID of the VAO.
     *
     * @return The ID of the mesh's VAO.
     */
    public int getVaoID()
    {
        return vaoID;
    }

    /**
     * Gets the number of vertices in the mesh.
     *
     * @return The number of vertices in the mesh.
     */
    public int getVertexCount()
    {
        return vertexCount;
    }
}
