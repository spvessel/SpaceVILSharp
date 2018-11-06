package com.spvessel;

import java.util.List;

public class CustomShape extends Primitive {
    static int count = 0;
    public int RotationAngle = 32;

    public CustomShape() {
        setItemName("CustomShape_" + count);
        count++;
    }

    public CustomShape(List<float[]> shape) {
        this();
        setTriangles(shape);
    }

    @Override
    public List<float[]> makeShape() {
        if (getTriangles() == null || getTriangles().size() == 0)
            return null;
        return GraphicsMathService.toGL(updateShape(), getHandler());
    }
}