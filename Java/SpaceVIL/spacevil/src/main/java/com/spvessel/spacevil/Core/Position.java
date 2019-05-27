package com.spvessel.spacevil.Core;

public class Position implements InterfacePosition {

    public Position() {

    }

    public Position(int x, int y) {
        _x = x;
        _y = y;
    }

    /**
     * Class describes position of the item
     */
    private int _x = 0;
    private int _y = 0;

    /**
     * @param x X position of the item
     */
    public void setX(int x) {
        _x = x;
    }

    /**
     * @return X position of the item
     */
    public int getX() {
        return _x;
    }

    /**
     * @param y Y position of the item
     */
    public void setY(int y) {
        _y = y;
    }

    /**
     * @return Y position of the item
     */
    public int getY() {
        return _y;
    }

    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }
}