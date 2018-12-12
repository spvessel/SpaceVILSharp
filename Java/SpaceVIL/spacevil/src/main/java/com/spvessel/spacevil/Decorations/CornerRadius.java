package com.spvessel.spacevil.Decorations;

public class CornerRadius {
    /**
     * A class that store radius values for each corner of the rectangle object
     */

    public float leftTop;
    public float rightTop;
    public float leftBottom;
    public float rightBottom;

    /**
     * Constructs a CornerRadius with the radius values for each corner of the rectangle object
     */
    public CornerRadius(float leftTop, float rightTop, float leftBottom, float rightBottom) {
        this.leftTop = leftTop;
        this.rightTop = rightTop;
        this.leftBottom = leftBottom;
        this.rightBottom = rightBottom;
    }

    /**
     * Constructs a CornerRadius with the radius values from other CornerRadius object
     */
    public CornerRadius(CornerRadius radius) {
        leftTop = radius.leftTop;
        rightTop = radius.rightTop;
        leftBottom = radius.leftBottom;
        rightBottom = radius.rightBottom;
    }

    /**
     * Constructs a CornerRadius with default radius values ( = 0) for each corner
     */
    public CornerRadius() {
        this(0);
    }

    /**
     * Constructs a CornerRadius with the same radius values for each corner of the rectangle object
     */
    public CornerRadius(float radius) {
        leftTop = radius;
        rightTop = radius;
        leftBottom = radius;
        rightBottom = radius;
    }

}
