package renderEngine;

import entities.Entity;
import entities.UntexturedEntity;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SceneWriter
{
    /*
    public SceneWriter(String file, ArrayList<UntexturedEntity> entities)
    {
        try {
            FileWriter writer = new FileWriter("res/" + file);

            for(int i = 0; i < entities.size(); i++)
            {
                UntexturedEntity thisEntity = entities.get(i);

                writer.write("C\n");
                writer.write(thisEntity.getScaleX() + " " + thisEntity.getScaleY() + " "
                        + thisEntity.getScaleZ() + "\n");
                writer.write(thisEntity.getRotX() + " " + thisEntity.getRotY() + " "
                        + thisEntity.getRotZ() + "\n");
                writer.write(thisEntity.getPosition().x + " " + thisEntity.getPosition().y + " "
                        + thisEntity.getPosition().z + "\n");
            }

            writer.close();
        }
        catch(IOException e)
        {
            System.out.println("Failed to write scene file.");
        }
    }
     */

    public SceneWriter(String file, ArrayList<Entity> entities)
    {
        try {
            FileWriter writer = new FileWriter("res/" + file);

            for(int i = 0; i < entities.size(); i++)
            {
                Entity thisEntity = entities.get(i);

                writer.write("C\n");
                writer.write(thisEntity.getScaleX() + " " + thisEntity.getScaleY() + " "
                        + thisEntity.getScaleZ() + "\n");
                writer.write(thisEntity.getRotX() + " " + thisEntity.getRotY() + " "
                        + thisEntity.getRotZ() + "\n");
                writer.write(thisEntity.getPosition().x + " " + thisEntity.getPosition().y + " "
                        + thisEntity.getPosition().z + "\n");
            }

            writer.close();
        }
        catch(IOException e)
        {
            System.out.println("Failed to write scene file.");
        }
    }
}
