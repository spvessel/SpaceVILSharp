package com.spvessel.spacevil.Core;

/**
 * Scale is a class that describes the scaling factors along the X and Y axes.
 */
public class Scale {

    private float _x = 1f;
    private float _y = 1f;

    /**
     * Default Scale constructor. All scaling factors are 1.
     */
    public Scale() {
    }

    /**
     * Constructs Scale with specified scaling factors.
     * @param xScale Scaling factor by X axis.
     * @param yScale Scaling factor by Y axis.
     */
    public Scale(float xScale, float yScale) {
        setScale(xScale, yScale);
    }

    /**
     * Setting scaling factors.
     * @param xScale Scaling factor by X axis.
     * @param yScale Scaling factor by Y axis.
     */
    public void setScale(float xScale, float yScale) {
        if (xScale <= 0 || yScale <= 0) {
            return;
        }
        _x = xScale;
        _y = yScale;
    }

    /**
     * Getting scaling factor by X axis.
     * @return Scaling factor by X axis.
     */
    public float getXScale() {
        return _x;
    }

    /**
     * Getting scaling factor by Y axis.
     * @return Scaling factor by Y axis.
     */
    public float getYScale() {
        return _y;
    }

    @Override
    public String toString()
    {
        return "XScale: " + _x + " YScale: " + _y;
    }
}
