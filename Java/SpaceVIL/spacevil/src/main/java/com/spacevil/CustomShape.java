package com.spacevil;

import java.util.List;

public class CustomShape extends Primitive {
    static int count = 0;
    public int RotationAngle = 32;

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
    public List<float[]> makeShape() {
        if (getTriangles() == null || getTriangles().size() == 0)
            return null;
        return GraphicsMathService.toGL(updateShape(), getHandler());
    }
}