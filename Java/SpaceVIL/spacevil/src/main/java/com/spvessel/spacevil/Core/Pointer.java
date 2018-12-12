package com.spvessel.spacevil.Core;

public class Pointer {
    private Boolean _is_set = false;

    /**
     * Has pointer real position (true) or default (false)
     */
    public Boolean isSet() {
        return _is_set;
    }

    private int X = -1;

    /**
     * @return X position of the pointer
     */
    public  int getX() {
        return X;
    }

    /**
     * @param x X position of the pointer
     */
    public  void setX(int x) {
        X = x;
    }

    private int Y = -1;

    /**
     * @return Y position of the pointer
     */
    public  int getY() {
        return Y;
    }

    /**
     * @param y Y position of the pointer
     */
    public  void setY(int y) {
        Y = y;
    }

    private int PrevX = -1;
    private int PrevY = -1;

    /**
     * Set pointer position
     * @param x X position of the pointer
     * @param y Y position of the pointer
     */
    public  void setPosition(float x, float y) {
        if (PrevX == -1 || PrevY == -1) {
            PrevX = (int) x;
            PrevY = (int) y;
        } else {
            PrevX = X;
            PrevY = Y;
        }
        X = (int) x;
        Y = (int) y;
        _is_set = true;
    }

    /**
     * Set pointer position
     * @param x X position of the pointer
     * @param y Y position of the pointer
     */
    public  void setPosition(int x, int y) {
        if (PrevX == -1 || PrevY == -1) {
            PrevX = x;
            PrevY = y;
        } else {
            PrevX = X;
            PrevY = Y;
        }
        X = x;
        Y = y;
        _is_set = true;
    }

    /**
     * Set all pointer positions default
     */
    public void clear() {
        PrevX = -1;
        PrevY = -1;
        X = -1;
        Y = -1;
        _is_set = false;
    }
}