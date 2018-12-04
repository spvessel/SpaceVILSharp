package com.spvessel;

import com.spvessel.Core.InterfaceLine;
import com.spvessel.Core.InterfacePoints;

import java.util.List;
import java.util.LinkedList;
import java.awt.Color;

public class PointsContainer extends Primitive implements InterfacePoints, InterfaceLine {
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
        List<float[]> tmp = new LinkedList<>();
        for (float[] item : coord) {
            tmp.add(item);
        }
        setTriangles(tmp);
    }

    /**
     * Make shape according to triangles list assigned with setTriangles
     */
    @Override
    public List<float[]> makeShape() {
        if (getTriangles() == null || getTriangles().size() < 2)
            return null;
        return updateShape();
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