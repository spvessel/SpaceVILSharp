package com.spvessel.spacevil.Core;

/**
 * Area class represents a rectangular region with a specified position.
 */
public class Area {
    private int _x, _y, _w, _h;

    /**
     * Default constructor. All values are zero.
     */
    public Area() {
        _x = _y = _w = _h = 0;
    }

    /**
     * Constructs a Area with specified position and size.
     * @param x X position.
     * @param y Y position.
     * @param w Area width.
     * @param h Area height.
     */
    public Area(int x, int y, int w, int h) {
        _x = x;
        _y = y;
        _w = w;
        _h = h;
    }

    /**
     * Setting X position.
     * @param value X position.
     */
    public void setX(int value) {
        _x = value;
    }

    /**
     * Setting Y position.
     * @param value Y position.
     */
    public void setY(int value) {
        _y = value;
    }

    /**
     * Setting area width.
     * @param value An area width.
     */
    public void setWidth(int value) {
        _w = value;
    }

    /**
     * Setting area height.
     * @param value An area height.
     */
    public void setHeight(int value) {
        _h = value;
    }

    /**
     * Getting X position.
     * @return Current X position value.
     */
    public int getX() {
        return _x;
    }

    /**
     * Getting Y position.
     * @return Current Y position value.
     */
    public int getY() {
        return _y;
    }

    /**
     * Getting area width.
     * @return Current area width.
     */
    public int getWidth() {
        return _w;
    }

    /**
     * Getting area height.
     * @return Current area height.
     */
    public int getHeight() {
        return _h;
    }

    /**
     * Setting all area attributes.
     * @param x X position.
     * @param y Y position.
     * @param w Area width.
     * @param h Area height.
     */
    public void setAttr(int x, int y, int w, int h) {
        _x = x;
        _y = y;
        _w = w;
        _h = h;
    }

    @Override
    public String toString() {
        return _x + " " + _y + " " + _w + " " + _h;
    }
}