/*
This program uses LWJGL, here is its license:

Copyright © 2012-present Lightweight Java Game Library
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following
disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
disclaimer in the documentation and/or other materials provided with the distribution.
Neither the name Lightweight Java Game Library nor the names of its contributors may be used to endorse or promote
products derived from this software wisthout specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
Some of this code was created while following the "OpenGL 3D Game Tutorial" series on youtube by user "ThinMatrix":
https://www.youtube.com/watch?v=VS8wlS9hF8E
 */

package engineTester;

import static org.lwjgl.glfw.GLFW.*;

import Editor.Editor;
import FrameBuffer.FBO;
import Shaders.*;
import entities.Camera;
import entities.Entity;
import entities.Particles;
import entities.UntexturedEntity;
import models.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import renderEngine.*;
import textures.ModelTexture;
import textures.Texture;
import textures.TextureWriter;
import toolbox.HeightMapGenerator;
import toolbox.Vector2f;
import toolbox.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The main game loop, runs the came until the user exits.
 */
public class MainGameLoop
{
    // key input values; whether WASD is being pressed or not
    public static boolean E_W, E_S, E_A, E_D, E_E, E_Q = false;

    // camera controls
    public static Camera camera = new Camera();
    public static boolean firstMouse = true;
    public static float lastX = DisplayManager.WIDTH / 2.0f;
    public static float lastY = DisplayManager.HEIGHT / 2.0f;
    public static Vector3f worldUp = new Vector3f(0, 1, 0);

    // render mode
    public static boolean lowResMode = false;

    // cursor mode
    public static boolean cursorOn = false;

    public static ArrayList<Entity> entities;
    public static Editor editor;

    public static TextureWriter textureWriter;

    public static Loader loader;

    /**
     * Runs the game
     *
     * @param args Unused
     */
    public static void main(String[] args)
    {
        try {
            BufferedImage image = ImageIO.read(new File("res/cube.png"));
            textureWriter = new TextureWriter(image, "cube");
        }
        catch(IOException e)
        {
            System.out.println("Failed to read image in main");
            return;
        }

        // creates the window where the game will be ran
        DisplayManager.createDisplay(camera);

        // loader to load models, renderer to render models, shader to color the models
        loader = new Loader();
        StaticShader staticShader = new StaticShader();
        MonoShader monoShader = new MonoShader();
        SpriteShader spriteShader = new SpriteShader();

        Renderer renderer = new Renderer(staticShader, monoShader);

        // The RaWModel to be rendered, with mesh data in form of VAO
        RawModel cubeModel = loader.loadToVAO(CubeData.getVertices(), CubeData.getTextureCoords(),
                CubeData.getNormals(), CubeData.getIndices());
        ModelTexture cubeModelTexture = new ModelTexture(loader.loadTexture("cube"));
        TexturedModel cubeTexturedModel = new TexturedModel(cubeModel, cubeModelTexture);

        SceneLoader sceneLoader = new SceneLoader("scene", cubeTexturedModel);

        // FBO that renders information to a texture
        FBO fbo = new FBO();
        int fboTextureID = fbo.attachTexture();

        entities = sceneLoader.getEntities();

        RawModel planeModel = loader.loadToVAO(PlaneData.getVertices(), PlaneData.getTextureCoords(),
                PlaneData.getIndices());
        ModelTexture planeTexture = new ModelTexture(fboTextureID);
        TexturedModel planeTexturedModel = new TexturedModel(planeModel, planeTexture);
        Entity spritePlane = new Entity(planeTexturedModel, new Vector3f(0,0,0),
                0, 0, 0, 1, 1, 1);

        monoShader.loadCubeColor(new Vector3f(1,1,1));


        editor = new Editor(camera, renderer.getProjectionMatrix());

        // main game loop
        while(!glfwWindowShouldClose(DisplayManager.window))
        {
            if(cursorOn == false) {
                // whether or not the user to inputting movement
                if (E_W)
                    camera.moveForward();
                if (E_S)
                    camera.moveBackward();
                if (E_A)
                    camera.moveLeft();
                if (E_D)
                    camera.moveRight();
                if (E_Q)
                    camera.moveDown();
                if (E_E)
                    camera.moveUp();
            }
            else
            {
                if (E_E)
            {
                for(int i = 0; i < entities.size(); i++)
                {
                    if(entities.get(i).isHighlighted)
                        entities.get(i).increasePosition(0,1,0);
                }
            }
                if (E_Q)
                {
                    for(int i = 0; i < entities.size(); i++)
                    {
                        if(entities.get(i).isHighlighted)
                            entities.get(i).increasePosition(0,-1,0);
                    }
                }
                if (E_W)
                {
                    for(int i = 0; i < entities.size(); i++)
                    {
                        if(entities.get(i).isHighlighted)
                            entities.get(i).increasePosition(0,0,-1);
                    }
                }
                if (E_S)
                {
                    for(int i = 0; i < entities.size(); i++)
                    {
                        if(entities.get(i).isHighlighted)
                            entities.get(i).increasePosition(0,0,1);
                    }
                }
                if (E_A)
                {
                    for(int i = 0; i < entities.size(); i++)
                    {
                        if(entities.get(i).isHighlighted)
                            entities.get(i).increasePosition(-1,0,0);
                    }
                }
                if (E_D)
                {
                    for(int i = 0; i < entities.size(); i++)
                    {
                        if(entities.get(i).isHighlighted)
                            entities.get(i).increasePosition(1,0,0);
                    }
                }
            }

            double xpos[] = new double[1];
            double ypos[] = new double[1];
            glfwGetCursorPos(DisplayManager.window, xpos, ypos);
            //System.out.println(xpos[0] + " " + ypos[0]);
            editor.update((float)xpos[0], (float)ypos[0]);

            /*
            System.out.println(editor.getCurrentRay().x + " " + editor.getCurrentRay().y
                        + " " + editor.getCurrentRay().z);
             */

            if(lowResMode) {
                // first FBO
                // clears the FBO, ie the cube's texture, with a certain color
                fbo.clear(new Vector3f(0.1f, 0.1f, 0.11f));
                // render screen to the FBO, which is then used to texture the cube
                fbo.start();
                // render scene loader from file
                for(int i = 0; i < entities.size(); i++)
                {
                    if(entities.get(i).isHighlighted)
                        monoShader.loadCubeColor(new Vector3f(0, 0, 1));
                    else
                        monoShader.loadCubeColor(new Vector3f(1,1,1));
                    renderer.render(entities.get(i), monoShader, camera);
                }
                // stop rendering to the FBO
                fbo.stop();
                renderer.prepare();
                renderer.render(spritePlane, spriteShader);
            }
            else {
                // clears screen with a background color
                renderer.prepare();

                for(int i = 0; i < entities.size(); i++)
                {
                    if(entities.get(i).isHighlighted)
                        monoShader.loadCubeColor(new Vector3f(0, 0, 1));
                    else
                        monoShader.loadCubeColor(new Vector3f(1,1,1));
                    renderer.render(entities.get(i), monoShader, camera);
                }
            }

            // gets user input from display
            DisplayManager.updateDisplay();
        }

        // clears memory and closes the window
        staticShader.cleanUp();
        monoShader.cleanUp();
        spriteShader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

        // write scene changes to the scene data file
        SceneWriter writer = new SceneWriter("scene", entities);
    }
}