package entities;

public class Particles
{
    private int vaoID;
    private int numParticles;

    public Particles(int vaoID, int numParticles)
    {
        this.vaoID = vaoID;
        this.numParticles = numParticles;
    }

    public int getVaoID()
    {
        return vaoID;
    }

    public int getNumParticles()
    {
        return numParticles;
    }

}
