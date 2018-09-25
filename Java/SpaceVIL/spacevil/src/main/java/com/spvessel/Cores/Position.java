package com.spvessel.Cores;

public class Position implements InterfacePosition {

    private int _x = 0;
    private int _y = 0;

    public void setX(int x) {
        _x = x;
    }

    public int getX() {
        return _x;
    }

    public void setY(int y) {
        _y = y;
    }

    public int getY() {
        return _y;
    }
}