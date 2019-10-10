package com.spvessel.spacevil;

import java.util.List;

public class CustomShape extends Primitive {
    static int count = 0;

    /**
     * Constructs a CustomShape
     */
    public CustomShape() {
        setItemName("CustomShape_" + count);
        count++;
    }

    /**
     * Constructs a CustomShape with trinagles list
     */
    public CustomShape(List<float[]> shape) {
        this();
        setTriangles(shape);
    }

    /**
     * Make shape with triangles and convert to GL coordinates
     */
    @Override
    public void makeShape() {
        if (getTriangles() == null || getTriangles().size() == 0)
            return;
        setTriangles(updateShape());
    }
}