package com.spvessel.spacevil;

import com.spvessel.spacevil.Decorations.CornerRadius;

/**
 * Rectangle is a subclass that extends from com.spvessel.spacevil.Primitive for
 * rendering a rectangle shape.
 */
public class Rectangle extends Primitive {
    private static int count = 0;
    private CornerRadius _borderRadius = new CornerRadius();

    /**
     * Setting the same radius values for each corner of the rectangle object.
     * 
     * @param radius Radii of the corners as
     *               com.spvessel.spacevil.Decorations.CornerRadius.
     */
    public void setBorderRadius(int radius) {
        _borderRadius.leftTop = radius;
        _borderRadius.rightTop = radius;
        _borderRadius.leftBottom = radius;
        _borderRadius.rightBottom = radius;
    }

    /**
     * Setting the radii of corners.
     * 
     * @param radius Radii of the corners as
     *               com.spvessel.spacevil.Decorations.CornerRadius.
     */
    public void setBorderRadius(CornerRadius radius) {
        _borderRadius = radius;
    }

    /**
     * Default Rectangle constructor. Radii of the corners are 0.
     */
    public Rectangle() {
        super("Rectangle_" + count++);
    }

    /**
     * Constructs an Rectangle with specified corner radii.
     * 
     * @param radius Radii of the corners as
     *               com.spvessel.spacevil.Decorations.CornerRadius.
     */
    public Rectangle(CornerRadius radius) {
        this();
        setBorderRadius(radius);
    }

    /**
     * Overridden method for stretching the rectangle shape relative to the current
     * size. Use in conjunction with getTriangles() and setTriangles() methods.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void makeShape() {
        setTriangles(GraphicsMathService.getRoundSquare(_borderRadius, getWidth(), getHeight(), 0, 0));
    }
}