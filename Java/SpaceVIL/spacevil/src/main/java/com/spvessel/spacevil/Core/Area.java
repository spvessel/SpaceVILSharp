package com.spvessel.spacevil.Core;

public class Area {
    private int _x, _y, _w, _h;

    public Area() {
        _x = _y = _w = _h = 0;
    }

    public Area(int x, int y, int w, int h) {
        _x = x;
        _y = y;
        _w = w;
        _h = h;
    }

    public void setX(int value) {
        _x = value;
    }

    public void setY(int value) {
        _y = value;
    }

    public void setWidth(int value) {
        _w = value;
    }

    public void setHeight(int value) {
        _h = value;
    }

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    public int getWidth() {
        return _w;
    }

    public int getHeight() {
        return _h;
    }

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