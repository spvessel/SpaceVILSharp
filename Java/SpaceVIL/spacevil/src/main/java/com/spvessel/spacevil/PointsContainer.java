package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfacePoints;

import java.util.List;
import java.util.LinkedList;
import java.awt.Color;

public class PointsContainer extends Primitive implements InterfacePoints {
    private static int count = 0;

    /**
     * Constructs a PointsContainer
     */
    public PointsContainer() {
        setItemName("PointsContainer_" + count);
        setBackground(0, 0, 0, 0);
        count++;
    }

    private float _thickness = 1.0f;

    /**
     * Points thickness
     */
    public void setPointThickness(float thickness) {
        _thickness = thickness;
    }

    public float getPointThickness() {
        return _thickness;
    }

    private Color _color = new Color(255, 255, 255);

    /**
     * Points color
     */
    public void setPointColor(Color color) {
        _color = color;
    }

    public Color getPointColor() {
        return _color;
    }

    private List<float[]> _shape_pointer;

    /**
     * Shape of the points
     */
    public void setShapePointer(List<float[]> shape) {
        if (shape == null)
            return;
        _shape_pointer = shape;

    }

    public List<float[]> getShapePointer() {
        if (_shape_pointer == null)
            _shape_pointer = GraphicsMathService.getEllipse(getPointThickness() / 2.0f, 16);
        return _shape_pointer;
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
}