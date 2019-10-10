package com.spvessel.spacevil;

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
     * 
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
    public void makeShape() {
        setTriangles(GraphicsMathService.getEllipse(getWidth(), getHeight(), 0, 0, quality));
        // return getTriangles();
    }
}