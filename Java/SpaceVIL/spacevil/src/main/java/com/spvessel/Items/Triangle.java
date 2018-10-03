package com.spvessel.Items;

import java.util.List;
import com.spvessel.Engine.*;

public class Triangle extends Primitive {
    static int count = 0;
    public int RotationAngle = 32;

    public Triangle() {
        setItemName("Triangle_" + count);
        count++;
    }

    public Triangle(int angle) {
        this();
        RotationAngle = angle;
    }

    @Override
    public List<float[]> makeShape() {
        setTriangles(GraphicsMathService.getTriangle(getWidth(), getHeight(), getX(), getY(), RotationAngle));
        return GraphicsMathService.toGL(updateShape(), getHandler());
    }
}