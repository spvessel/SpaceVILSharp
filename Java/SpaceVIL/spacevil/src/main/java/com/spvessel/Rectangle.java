package com.spvessel;

import java.util.List;

public class Rectangle extends Primitive {
    static int count = 0;

    public Rectangle() {
        setItemName("Rectangle_" + count);
        count++;
    }

    @Override
    public List<float[]> makeShape() {
        setTriangles(GraphicsMathService.getRectangle(getWidth(), getHeight(), getX(), getY()));
        return GraphicsMathService.toGL((BaseItem) this, getHandler());
    }
}