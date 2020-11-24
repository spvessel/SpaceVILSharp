package com.spvessel.spacevil.Core;

/**
 * Size is a class that describes the width and height of an object.
 */
public class Size {
    int _w = 0;
    int _h = 0;

    /**
     * Default Size constructor with width and height equal to 0.
     */
    public Size() {
    }

    /**
     * Constructs Size with specified width and height.
     * 
     * @param w Width of the object.
     * @param h Height of the object.
     */
    public Size(int w, int h) {
        _w = w;
        _h = h;
    }

    /**
     * Getting the width of the object.
     * 
     * @return Width of the object.
     */
    public int getWidth() {
        return _w;
    }

    /**
     * Setting the width of the object.
     * 
     * @param value Width of the object.
     */
    public void setWidth(int value) {
        _w = value;
    }

    /**
     * Getting the height of the object.
     * 
     * @return Height of the object.
     */
    public int getHeight() {
        return _h;
    }

    /**
     * Setting the height of the object.
     * 
     * @param value Height of the object.
     */
    public void setHeight(int value) {
        _h = value;
    }

    /**
     * Setting the size of the object.
     * 
     * @param w Width of the object.
     * @param h Height of the object.
     */
    public void setSize(int w, int h) {
        setWidth(w);
        setHeight(h);
    }
}
