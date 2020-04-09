package com.spvessel.spacevil;

/**
 * Ellipse is a subclass that extends from com.spvessel.spacevil.Primitive for
 * rendering an ellipse shape.
 */
public class Ellipse extends Primitive {
    private static int count = 0;
    /**
     * Property to specify number of edges in an ellipse shape.
     * <p>
     * Default: 16.
     */
    public int quality = 16;

    /**
     * Default Ellipse constructor.
     */
    public Ellipse() {
        setItemName("Ellipse_" + count);
        count++;
    }

    /**
     * Constructs an Ellipse with specified number of edges in an ellipse shape.
     * 
     * @param quality Number of edges.
     */
    public Ellipse(int quality) {
        this();
        this.quality = quality;
    }

    /**
     * Overridden method for stretching the ellipse shape relative to the current
     * size. Use in conjunction with getTriangles() and setTriangles() methods.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void makeShape() {
        setTriangles(GraphicsMathService.getEllipse(getWidth(), getHeight(), 0, 0, quality));
    }
}