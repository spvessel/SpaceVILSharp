package com.spvessel;

import java.util.List;

public class Ellipse extends Primitive {
    static int count = 0;
    public int Quality = 16;
    public Ellipse() {
        setItemName("Ellipse_" + count);
        count++;
    }
    public Ellipse(int n) {
        this();
        Quality = n;
    }

    @Override
    public List<float[]> makeShape() {
        setTriangles(GraphicsMathService.getEllipse(getWidth(), getHeight(), getX(), getY(), Quality));
        return GraphicsMathService.toGL((BaseItem) this, getHandler());
    }
}