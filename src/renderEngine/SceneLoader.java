package renderEngine;

import entities.Entity;
import entities.Particles;
import entities.UntexturedEntity;
import javafx.scene.chart.ScatterChart;
import models.CubeData;
import models.RawModel;
import models.TexturedModel;
import toolbox.Vector3f;

import java.io.*;
import java.util.ArrayList;

public class SceneLoader
{
    private ArrayList<Entity> entities;

    public SceneLoader(String file, TexturedModel cubeModel)
    {
        entities =  new ArrayList<Entity>();

        try {
            FileReader reader = new FileReader("res/" + file);

            int i = reader.read();
            while(i != -1)
            {
                // the following data is cube data
                if((char)i == 'C')
                {
                    i = reader.read();
                    //i = reader.read();

                    // scaleX
                    String strScaleX = "";
                    i = reader.read();
                    while((char)i != ' ') {
                        strScaleX += (char)i;
                        i = reader.read();
                    }
                    float scaleX = Float.parseFloat(strScaleX);

                    // scaleY
                    String strScaleY = "";
                    i = reader.read();
                    while((char)i != ' ') {
                        strScaleY += (char)i;
                        i = reader.read();
                    }
                    float scaleY = Float.parseFloat(strScaleY);

                    // scaleZ
                    String strScaleZ = "";
                    i = reader.read();
                    while((char)i != '\n') {
                        strScaleZ += (char)i;
                        i = reader.read();
                    }
                    float scaleZ = Float.parseFloat(strScaleZ);

                    // rotX
                    String strRotX = "";
                    i = reader.read();
                    while((char)i != ' ') {
                        strRotX += (char)i;
                        i = reader.read();
                    }
                    float rotX = Float.parseFloat(strRotX);

                    // rotY
                    String strRotY = "";
                    i = reader.read();
                    while((char)i != ' ') {
                        strRotY += (char)i;
                        i = reader.read();
                    }
                    float rotY = Float.parseFloat(strRotY);

                    // rotZ
                    String strRotZ = "";
                    i = reader.read();
                    while((char)i != '\n') {
                        strRotZ += (char)i;
                        i = reader.read();
                    }
                    float rotZ = Float.parseFloat(strRotZ);

                    // positionX
                    String strPosX = "";
                    i = reader.read();
                    while((char)i != ' ') {
                        strPosX += (char)i;
                        i = reader.read();
                    }
                    float positionX = Float.parseFloat(strPosX);

                    // positionY
                    String strPosY = "";
                    i = reader.read();
                    while((char)i != ' ') {
                        strPosY += (char)i;
                        i = reader.read();
                    }
                    float positionY = Float.parseFloat(strPosY);

                    // positionZ
                    String strPosZ = "";
                    i = reader.read();
                    while((char)i != '\n') {
                        strPosZ += (char)i;
                        i = reader.read();
                    }
                    float positionZ = Float.parseFloat(strPosZ);

                    // -1 or C or another letter
                    i = reader.read();

                    // add this cube to the list of entities
                    Entity entity = new Entity(cubeModel, new Vector3f(positionX, positionY,
                            positionZ), rotX, rotY, rotZ, scaleX, scaleY, scaleZ);
                    entities.add(entity);
                }
                else
                    break;
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("FileNotFoundException");
        }
        catch(IOException e)
        {
            System.out.println("IOException");
        }
    }

    public ArrayList<Entity> getEntities()
    {
        return entities;
    }
}
