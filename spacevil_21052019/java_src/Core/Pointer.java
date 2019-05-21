package com.spvessel.spacevil.Core;

public class Pointer {
    public Pointer() {

    }

    public Pointer(int x, int y) {
        setX(x);
        setY(y);
    }

    private final int defPointValue = -1;

    private Boolean _is_set = false;

    /**
     * Has pointer real position (true) or default (false)
     */
    public Boolean isSet() {
        return _is_set;
    }

    private int X = defPointValue;

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

    private int Y = defPointValue;

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

    private int PrevX = defPointValue;
    private int PrevY = defPointValue;

    /**
     * Set pointer position
     * @param x X position of the pointer
     * @param y Y position of the pointer
     */
    public  void setPosition(float x, float y) {
        if (PrevX == defPointValue || PrevY == defPointValue) {
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
        if (PrevX == defPointValue || PrevY == defPointValue) {
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
        PrevX = defPointValue;
        PrevY = defPointValue;
        X = defPointValue;
        Y = defPointValue;
        _is_set = false;
    }
}