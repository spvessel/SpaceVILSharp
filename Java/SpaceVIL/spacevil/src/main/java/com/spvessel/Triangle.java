package com.spvessel;

import java.util.List;

public class Triangle extends Primitive {
    private static int count = 0;
    /**
     * Rotate triangle on rotationAngle (degrees)
     */
    public int rotationAngle = 0;

    /**
     * Constructs a Triangle
     */
    public Triangle() {
        setItemName("Triangle_" + count);
        count++;
    }

    /**
     * Constructs a Triangle
     * @param angle Triangle rotation angle (degrees)
     */
    public Triangle(int angle) {
        this();
        rotationAngle = angle;
    }

    /**
     * Make Triangle shape as list of floats
     */
    @Override
    public List<float[]> makeShape() {
        setTriangles(GraphicsMathService.getTriangle(getWidth(), getHeight(), getX(), getY(), rotationAngle));
        return GraphicsMathService.toGL(updateShape(), getHandler());
    }
}