package com.spvessel.spacevil.Core;

/**
 * Position is a class representing the location of a 2D coordinate integer point.
 */
public class Position implements IPosition {
    /**
     * Default Position constructor.
     */
    public Position() {

    }

    /**
     * Position constructor with specified X and Y coordinates.
     * @param x X position of the 2D point.
     * @param y Y position of the 2D point.
     */
    public Position(int x, int y) {
        _x = x;
        _y = y;
    }

    private int _x = 0;
    private int _y = 0;

    /**
     * Setting X position of the 2D point.
     * @param x X position of the 2D point.
     */
    public void setX(int x) {
        _x = x;
    }

    /**
     * Getting X position of the 2D point.
     * @return X position of the 2D point.
     */
    public int getX() {
        return _x;
    }

    /**
     * Setting Y position of the 2D point.
     * @param y Y position of the 2D point.
     */
    public void setY(int y) {
        _y = y;
    }

    /**
     * Getting Y position of the 2D point.
     * @return Y position of the 2D point.
     */
    public int getY() {
        return _y;
    }

    /**
     * Setting 2D point position.
     * @param x X position of the 2D point.
     * @param y Y position of the 2D point.
     */
    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }
}