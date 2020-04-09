package com.spvessel.spacevil;

/**
 * Triangle is a subclass that extends from com.spvessel.spacevil.Primitive for
 * rendering an triangle shape.
 */
public class Triangle extends Primitive {
    private static int count = 0;
    /**
     * Rotation angle in degrees of an triangle shape.
     * <p>
     * Default: 0.
     */
    public int rotationAngle = 0;

    /**
     * Default Triangle constructor.
     */
    public Triangle() {
        setItemName("Triangle_" + count);
        count++;
    }

    /**
     * Constructs an Triangle with specified rotation angle of an triangle shape.
     * 
     * @param angle Rotation angle of an triangle shape.
     */
    public Triangle(int angle) {
        this();
        rotationAngle = angle;
    }

    /**
     * Overridden method for stretching the triangle shape relative to the current
     * size. Use in conjunction with getTriangles() and setTriangles() methods.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void makeShape() {
        setTriangles(GraphicsMathService.getTriangle(getWidth(), getHeight(), 0, 0, rotationAngle));
    }
}