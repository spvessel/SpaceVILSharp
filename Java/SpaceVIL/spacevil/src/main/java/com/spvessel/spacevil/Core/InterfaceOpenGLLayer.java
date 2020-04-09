package com.spvessel.spacevil.Core;

/**
 * Interface for providing OpenGL within SpaceVIL environment.
 */
public interface InterfaceOpenGLLayer {
    /**
     * Method for initializing OpenGL components before drawing (shaders, vbo and etc.).
     */
    public void initialize();

    /**
     * Method for checking initializing status of current OpenGL item.
     * @return Should be True: if an item is already initialized. Should be False: if an item is not initialed yed.
     */
    public boolean isInitialized();

    /**
     * Method to discribe logic of drawing OpenGL objects.
     */
    public void draw();

    /**
     * Method to describe disposing OpenGL resources if the item was removed.
     */
    public void free();
}
