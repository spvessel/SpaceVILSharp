package com.spvessel.spacevil;

import java.util.List;

public class Ellipse extends Primitive {
    private static int count = 0;
    public int quality = 16;

    /**
     * Constructs an Ellipse
     */
    public Ellipse() {
        setItemName("Ellipse_" + count);
        count++;
    }

    /**
     * Constructs an Ellipse
     * @param quality Ellipse quality (points count)
     */
    public Ellipse(int quality) {
        this();
        this.quality = quality;
    }

    /**
     * Make shape with triangles and convert to GL coordinates
     */
    @Override
    public List<float[]> makeShape() {
        setTriangles(GraphicsMathService.getEllipse(getWidth(), getHeight(), getX(), getY(), quality));
        return GraphicsMathService.toGL(this, getHandler());
    }
}