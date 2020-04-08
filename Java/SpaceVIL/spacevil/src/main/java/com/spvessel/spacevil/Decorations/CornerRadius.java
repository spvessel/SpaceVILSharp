package com.spvessel.spacevil.Decorations;

/**
 * A class that store radius values for each corner of the rectangle shape.
 */
public class CornerRadius {
    /**
     * Left-top corner radius
     */
    public float leftTop;

    /**
     * Right-top corner radius
     */
    public float rightTop;

    /**
     * Left-bottom corner radius
     */
    public float leftBottom;

    /**
     * Right-bottom corner radius
     */
    public float rightBottom;

    /**
     * Checking if all corner radiuses is 0.
     * @return True: if all corner radiuses is 0. False: if one of the corner radiuses is not 0.
     */
    public boolean isCornersZero() {
        if (leftTop != 0) {
            return false;
        }
        if (rightTop != 0) {
            return false;
        }
        if (rightBottom != 0) {
            return false;
        }
        if (leftBottom != 0) {
            return false;
        }
        return true;
    }

    public boolean isEqual() {
        if (leftTop == rightTop && rightTop == rightBottom && rightBottom == leftBottom)
            return true;
        return false;
    }
    
    /**
     * Constructs a CornerRadius with the radius values from other CornerRadius object.
     * @param radius Radius values as com.spvessel.spacevil.Decorations.CornerRadius.
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
     * Constructs a CornerRadius with the same radius values for each corner of the rectangle object.
     * @param radius Radius of the item's corners.
     */
    public CornerRadius(float radius) {
        leftTop = radius;
        rightTop = radius;
        leftBottom = radius;
        rightBottom = radius;
    }
    
    /**
     * Constructs a CornerRadius with the radius values for each corner of the rectangle object.
     * @param leftTop Left-top corner radius.
     * @param rightTop Right-top corner radius.
     * @param leftBottom Left-bottom corner radius.
     * @param rightBottom Right-bottom corner radius.
     */
    public CornerRadius(float leftTop, float rightTop, float leftBottom, float rightBottom) {
        this.leftTop = leftTop;
        this.rightTop = rightTop;
        this.leftBottom = leftBottom;
        this.rightBottom = rightBottom;
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
