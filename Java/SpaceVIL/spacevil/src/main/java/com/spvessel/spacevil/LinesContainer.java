package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceLines;

import java.util.List;
import java.util.LinkedList;
import java.awt.Color;

/**
 * LinesContainer is class for rendering lines in graph.
 */
public class LinesContainer extends Primitive implements InterfaceLines {
    private static int count = 0;

    /**
     * Default LinesContainer constructor.
     */
    public LinesContainer() {
        setItemName("LinesContainer_" + count);
        setBackground(0, 0, 0, 0);
        count++;
    }

    /**
     * Setting points coordinates.
     * 
     * @param coord Points list as List of float[2] array.
     */
    public void setPoints(List<float[]> coord) {
        List<float[]> tmp = new LinkedList<>(coord);
        setTriangles(tmp);
    }

    /**
     * Getting points coordinates.
     * 
     * @return Points list as List of float[2] array.
     */
    @Override
    public List<float[]> getPoints() {
        if (getTriangles() == null || getTriangles().size() < 2)
            return null;
        return getTriangles();
    }

    /**
     * Overridden method for stretching the points position relative to the current
     * size of the item. Use in conjunction with getTriangles() and setTriangles()
     * methods.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void makeShape() {
        if (getTriangles() != null)
            setTriangles(updateShape());
    }

    private float _lineThickness = 1.0f;

    /**
     * Setting thickness of lines.
     * 
     * @param thickness Line thickness.
     */
    public void setLineThickness(float thickness) {
        _lineThickness = thickness;
    }

    /**
     * Getting lines thickness.
     * 
     * @return Lines thickness.
     */
    public float getLineThickness() {
        return _lineThickness;
    }

    private Color _lineColor = new Color(0, 0, 255);

    /**
     * Setting lines color.
     * <p>
     * Default: Blue.
     * 
     * @param color Line color.
     */
    public void setLineColor(Color color) {
        _lineColor = color;
    }

    /**
     * Getting lines color.
     * 
     * @return Lines color.
     */
    public Color getLineColor() {
        return _lineColor;
    }
}