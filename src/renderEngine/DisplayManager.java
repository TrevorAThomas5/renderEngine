package renderEngine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.system.MemoryUtil.*;

import Editor.Editor;
import engineTester.MainGameLoop;
import entities.Camera;
import models.CubeData;
import models.PlaneData;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.Callbacks.*;
import org.lwjgl.system.*;

import java.awt.*;
import java.nio.*;
import java.util.MissingFormatArgumentException;

import static org.lwjgl.system.MemoryStack.*;
import org.lwjgl.opengl.*;
import textures.ModelTexture;
import textures.TextureWriter;
import toolbox.Vector3f;

/**
 * Handles window creation, updates, and destruction
 */
public class DisplayManager
{
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static long window;

    /**
     * Creates the window and initiates glfw
     */
    public static void createDisplay(Camera camera)
    {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // initialize glfw
        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // create the display window
        window = glfwCreateWindow(WIDTH, HEIGHT, "First Window", NULL, NULL);
        if(window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // tell GLFW to capture our mouse
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        // Setup a key callback. It will be called every time a key is pressed,
        // repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->
        {
            // closes the window if the escape button is pressed
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);

            // controls camera movement with WASD
            if(key == GLFW_KEY_Y)
            {
                if(action == GLFW_RELEASE) {
                    int x = (int)(Math.random() * 255);
                    int y = (int)(Math.random() * 255);
                    MainGameLoop.textureWriter.drawPixel(x, y, new Color(x, 255-x, y),
                            TextureWriter.Side.FRONT);
                    MainGameLoop.loader.loadTexture("bed");
                    for(int i = 0; i < MainGameLoop.entities.size(); i++)
                    {
                        RawModel cubeModel = MainGameLoop.loader.loadToVAO(CubeData.getVertices(),
                                CubeData.getTextureCoords(), CubeData.getNormals(), CubeData.getIndices());
                        ModelTexture cubeModelTexture =
                                new ModelTexture(MainGameLoop.loader.loadTexture("cube"));
                        TexturedModel cubeTexturedModel = new TexturedModel(cubeModel, cubeModelTexture);
                        MainGameLoop.entities.get(i).setModel(cubeTexturedModel);
                    }

                    System.out.println("drew pixel");
                }
            }

            // controls camera movement with WASD
            if(key == GLFW_KEY_W)
            {
                if(action == GLFW_PRESS)
                    MainGameLoop.E_W = true;
                if(action == GLFW_RELEASE)
                    MainGameLoop.E_W = false;
            }
            if(key == GLFW_KEY_S)
            {
                if(action == GLFW_PRESS)
                    MainGameLoop.E_S = true;
                if(action == GLFW_RELEASE)
                    MainGameLoop.E_S = false;
            }
            if(key == GLFW_KEY_A)
            {
                if(action == GLFW_PRESS)
                    MainGameLoop.E_A = true;
                if(action==GLFW_RELEASE)
                    MainGameLoop.E_A = false;
            }
            if(key == GLFW_KEY_D)
            {
                if(action==GLFW_PRESS)
                    MainGameLoop.E_D = true;
                if(action == GLFW_RELEASE)
                    MainGameLoop.E_D = false;
            }
            if(key == GLFW_KEY_Q)
            {
                if(action==GLFW_PRESS)
                    MainGameLoop.E_Q = true;
                if(action == GLFW_RELEASE)
                    MainGameLoop.E_Q = false;
            }
            if(key == GLFW_KEY_E)
            {
                if(action==GLFW_PRESS)
                    MainGameLoop.E_E = true;
                if(action == GLFW_RELEASE)
                    MainGameLoop.E_E = false;
            }
            if(key == GLFW_KEY_M)
            {
                if(action == GLFW_RELEASE) {
                    if (MainGameLoop.lowResMode) {
                        MainGameLoop.lowResMode = false;
                        System.out.println("Entering HighRes Mode");
                    }
                    else {
                        MainGameLoop.lowResMode = true;
                        System.out.println("Entering LowRes Mode");
                    }
                }
            }
            if(key == GLFW_KEY_N)
            {
                if(action == GLFW_RELEASE) {
                    if (MainGameLoop.cursorOn) {
                        MainGameLoop.cursorOn = false;
                        MainGameLoop.firstMouse = true;
                        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
                        System.out.println("Entering View Mode");
                    }
                    else {
                        MainGameLoop.cursorOn = true;
                        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                        System.out.println("Entering Editor Mode");
                    }
                }
            }
        });

        glfwSetMouseButtonCallback(window, (window, button, action, mods) ->
        {
            if(button == GLFW_MOUSE_BUTTON_LEFT)
            {
                if(action == GLFW_RELEASE)
                {
                    // check is mouse clicked on any entities
                    MainGameLoop.editor.selectEntity(MainGameLoop.entities);
                }
            }
        });

        glfwSetCursorPosCallback(window, (window, xpos, ypos) ->
        {
            if(MainGameLoop.cursorOn == false) {
                if (MainGameLoop.firstMouse) {
                    MainGameLoop.lastX = (float) xpos;
                    MainGameLoop.lastY = (float) ypos;
                    MainGameLoop.firstMouse = false;
                }

                float xoffset = (float) xpos - MainGameLoop.lastX;
                float yoffset = MainGameLoop.lastY - (float) ypos;
                MainGameLoop.lastX = (float) xpos;
                MainGameLoop.lastY = (float) ypos;

                float sensitivity = 0.05f;
                xoffset *= sensitivity;
                yoffset *= sensitivity;

                MainGameLoop.camera.yaw += xoffset;
                MainGameLoop.camera.pitch -= yoffset;

                if (MainGameLoop.camera.pitch > 89.0f)
                    MainGameLoop.camera.pitch = 89.0f;
                if (MainGameLoop.camera.pitch < -89.0f)
                    MainGameLoop.camera.pitch = -89.0f;

                Vector3f front = new Vector3f(0, 0, 0);
                front.x = (float) Math.cos(Math.toRadians(MainGameLoop.camera.yaw)) *
                        (float) Math.cos(Math.toRadians(MainGameLoop.camera.pitch));
                front.y = (float) Math.sin(Math.toRadians(MainGameLoop.camera.pitch));
                front.z = (float) Math.sin(Math.toRadians(MainGameLoop.camera.yaw)) *
                        (float) Math.cos(Math.toRadians(MainGameLoop.camera.pitch));
                MainGameLoop.camera.front = Vector3f.normalize(front);
                MainGameLoop.camera.right = Vector3f.normalize(Vector3f.cross(MainGameLoop.camera.front,
                        MainGameLoop.worldUp));
                MainGameLoop.camera.up = Vector3f.normalize(Vector3f.cross(MainGameLoop.camera.right,
                        MainGameLoop.camera.front));
            }
        });

        // Get the thread stack and push a new frame
        try(MemoryStack stack = stackPush())
        {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // set focus on the window just created
        glfwMakeContextCurrent(window);

        // disable vsync
        //glfwSwapInterval(1);

        // shows the window
        glfwShowWindow(window);

        // very important, but idk what it does
        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Updates the window, swaps buffers and polls events
     */
    public static void updateDisplay()
    {
        // swap the color buffers
       glfwSwapBuffers(window);

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }

    /**
     * Closes the window and terminates glfw, called when the game is over
     */
    public static void closeDisplay()
    {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate glfw and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}

