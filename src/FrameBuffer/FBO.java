package FrameBuffer;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import toolbox.Vector3f;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;

/**
 * A frame buffer object which is rendered to a texture.
 */
public class FBO
{
    private int id;
    private final int WIDTH = 128;
    private final int HEIGHT = 72;

    /**
     * Constructs a new FBO.
     */
    public FBO()
    {
        id = EXTFramebufferObject.glGenFramebuffersEXT();
    }

    public int getID()
    {
        return id;
    }

    /**
     * Tells the FBO what texture to render to.
     *
     * @return ID of the texture.
     */
    public int attachTexture()
    {
        // use this FBO
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, id);

        // a new texture
        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        // texture settings
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, WIDTH, HEIGHT, 0,GL_RGBA, GL_INT,
                (java.nio.ByteBuffer) null);

        // attaches the texture
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL_TEXTURE_2D,
                textureID, 0);

        int depthRenderBufferID = glGenRenderbuffersEXT();


        // initialize depth renderbuffer
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, WIDTH, HEIGHT);
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,GL_DEPTH_ATTACHMENT_EXT,GL_RENDERBUFFER_EXT,
                depthRenderBufferID);


        // unbinds the FBO
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

        return textureID;
    }

    /**
     * Clears the FBO with a certain color.
     *
     * @param color The clear color
     */
    public void clear(Vector3f color)
    {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, id);
        glClearColor(color.x, color.y,color.z, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    /**
     * Binds the FBO.
     */
    public void start()
    {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, id);
        glPushAttrib(GL_VIEWPORT_BIT);
        glViewport(0, 0, WIDTH, HEIGHT);
    }

    /**
     * Unbinds the FBO.
     */
    public void stop()
    {
        glPopAttrib();
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }
}
