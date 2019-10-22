package com.spvessel.spacevil.Core;

public class Size {
    int _w = 0;
    int _h = 0;

    public Size(int w, int h) {
        _w = w;
        _h = h;
    }

    public int getWidth() {
        return _w;
    }

    public void setWidth(int value) {
        _w = value;
    }

    public int getHeight() {
        return _h;
    }

    public void setHeight(int value) {
        _h = value;
    }

    public void setSize(int w, int h) {
        setWidth(w);
        setHeight(h);
    }
}
