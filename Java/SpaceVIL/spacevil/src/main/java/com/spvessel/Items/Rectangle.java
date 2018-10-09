package com.spvessel.Items;

import java.util.List;
import com.spvessel.Engine.*;

public class Rectangle extends Primitive {
    static int count = 0;

    public Rectangle() {
        setItemName("Rectangle_" + count);
        count++;
    }

    @Override
    public List<float[]> makeShape() {
        setTriangles(GraphicsMathService.getRectangle(getWidth(), getHeight(), getX(), getY()));
        return GraphicsMathService.toGL(this, getHandler());
    }
}