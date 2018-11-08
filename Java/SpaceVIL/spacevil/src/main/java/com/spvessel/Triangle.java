package com.spvessel;

import java.util.List;

public class Triangle extends Primitive {
    static int count = 0;
    public int rotationAngle = 0;

    public Triangle() {
        setItemName("Triangle_" + count);
        count++;
    }

    public Triangle(int angle) {
        this();
        rotationAngle = angle;
    }

    @Override
    public List<float[]> makeShape() {
        setTriangles(GraphicsMathService.getTriangle(getWidth(), getHeight(), getX(), getY(), rotationAngle));
        return GraphicsMathService.toGL(updateShape(), getHandler());
    }
}