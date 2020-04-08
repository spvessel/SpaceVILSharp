package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceLines;

import java.util.List;
import java.util.LinkedList;
import java.awt.Color;

public class LinesContainer extends Primitive implements InterfaceLines {
    private static int count = 0;

    /**
     * Constructs a LinesContainer
     */
    public LinesContainer() {
        setItemName("LinesContainer_" + count);
        setBackground(0, 0, 0, 0);
        count++;
    }

    /**
     * List of the points coordinates
     */
    public void setPointsCoord(List<float[]> coord) {
        List<float[]> tmp = new LinkedList<>(coord);
        setTriangles(tmp);
    }

    /**
     * Make shape according to triangles list assigned with setTriangles
     */
    @Override
    public void makeShape() {
        if (getTriangles() != null)
            setTriangles(updateShape());
    }

    @Override
    public List<float[]> getPoints() {
        if (getTriangles() == null || getTriangles().size() < 2)
            return null;
        return getTriangles();
    }

    private float _line_thickness = 1.0f;

    /**
     * Thickness of the line between the points
     */
    public void setLineThickness(float thickness) {
        _line_thickness = thickness;
    }

    public float getLineThickness() {
        return _line_thickness;
    }

    private Color _line_color = new Color(0, 0, 255);

    /**
     * The line color
     */
    public void setLineColor(Color color) {
        _line_color = color;
    }

    public Color getLineColor() {
        return _line_color;
    }
}