package com.spvessel.spacevil;

import java.util.List;

/**
 * CustomShape is a subclass that extends from com.spvessel.spacevil.Primitive
 * and can be any type of shapes.
 * <p>
 * You must provide the correct 2D vertices (triangles) of your figure to wark
 * with this class.
 */
public class CustomShape extends Primitive {
    static int count = 0;

    /**
     * Default CustomShape constructor.
     */
    public CustomShape() {
        setItemName("CustomShape_" + count);
        count++;
    }

    /**
     * Constructs a CustomShape with the specified shape.
     * 
     * @param shape Shape as list of tringles (points list of the shape as List of
     *              float[2] array).
     */
    public CustomShape(List<float[]> shape) {
        this();
        setTriangles(shape);
    }

    /**
     * Overridden method for stretching the shape of the current item relative to
     * the current size. Use in conjunction with getTriangles() and getTriangles()
     * methods.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void makeShape() {
        if (getTriangles() == null || getTriangles().size() == 0)
            return;
        setTriangles(updateShape());
    }
}