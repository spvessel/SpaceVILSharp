package com.spvessel;

import java.util.List;

public class Rectangle extends Primitive {
    private static int count = 0;

    /**
     * Constructs a Rectangle
     */
    public Rectangle() {
        setItemName("Rectangle_" + count);
        count++;
    }

    /**
     * Make rectangle with width, height, X, Y
     */
    @Override
    public List<float[]> makeShape() {
        setTriangles(GraphicsMathService.getRectangle(getWidth(), getHeight(), getX(), getY()));
        return GraphicsMathService.toGL(this, getHandler());
    }
}