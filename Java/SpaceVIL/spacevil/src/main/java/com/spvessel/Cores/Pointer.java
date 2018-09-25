package com.spvessel.Cores;

public class Pointer {
    private Boolean _is_set = false;

    public Boolean isSet() {
        return _is_set;
    }

    public int X = -1;
    public int Y = -1;
    public int PrevX = -1;
    public int PrevY = -1;

    public void setPosition(float x, float y) {
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

    public void setPosition(int x, int y) {
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

    public void clear() {
        PrevX = -1;
        PrevY = -1;
        X = -1;
        Y = -1;
        _is_set = false;
    }
}