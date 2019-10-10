package com.spvessel.spacevil.Decorations;

public class CornerRadius {
    /**
     * A class that store radius values for each corner of the rectangle object
     */

    public float leftTop;
    public float rightTop;
    public float leftBottom;
    public float rightBottom;

    public boolean isCornersZero() {
        if (leftTop != 0)
            return false;
        if (rightTop != 0)
            return false;
        if (rightBottom != 0)
            return false;
        if (leftBottom != 0)
            return false;
        return true;
    }

    public boolean isEqual() {
        if (leftTop == rightTop && rightTop == rightBottom && rightBottom == leftBottom)
            return true;
        return false;
    }

    /**
     * Constructs a CornerRadius with the radius values for each corner of the
     * rectangle object
     */
    public CornerRadius(float leftTop, float rightTop, float leftBottom, float rightBottom) {
        this.leftTop = leftTop;
        this.rightTop = rightTop;
        this.leftBottom = leftBottom;
        this.rightBottom = rightBottom;
    }

    /**
     * Constructs a CornerRadius with the radius values from other CornerRadius
     * object
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
     * Constructs a CornerRadius with the same radius values for each corner of the
     * rectangle object
     */
    public CornerRadius(float radius) {
        leftTop = radius;
        rightTop = radius;
        leftBottom = radius;
        rightBottom = radius;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) 
            return true;
        if (obj == null || obj.getClass() != this.getClass()) 
            return false;

        CornerRadius raduis = (CornerRadius) obj;
        if (raduis.leftBottom == this.leftBottom && raduis.rightBottom == this.rightBottom
                && raduis.leftTop == this.leftTop && raduis.rightTop == this.rightTop)
            return true;
        else
            return false;
    }
}
