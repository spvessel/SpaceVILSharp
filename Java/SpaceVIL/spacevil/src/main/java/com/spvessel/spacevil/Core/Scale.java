package com.spvessel.spacevil.Core;

public class Scale {

    private float _x = 1f;
    private float _y = 1f;

    public Scale() {
    }

    public Scale(float xScale, float yScale) {
        setScale(xScale, yScale);
    }

    public void setScale(float xScale, float yScale) {
        if (xScale <= 0 || yScale <= 0)
            return;
        _x = xScale;
        _y = yScale;
    }

    public float getXScale() {
        return _x;
    }

    public float getYScale() {
        return _y;
    }

    @Override
    public String toString()
    {
        return "XScale: " + _x + " YScale: " + _y;
    }
}
