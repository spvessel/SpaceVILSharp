package com.spvessel;

import com.spvessel.Core.InterfaceLine;
import com.spvessel.Core.InterfacePoints;

import java.util.List;
import java.util.LinkedList;
import java.awt.Color;

public class PointsContainer extends Primitive implements InterfacePoints, InterfaceLine {
    static int count = 0;

    public PointsContainer() {
        setItemName("PointsContainer_" + count);
        setBackground(0, 0, 0, 0);
        count++;
    }

    float _thickness = 1.0f;

    public void setPointThickness(float thickness) {
        _thickness = thickness;
    }

    public float getPointThickness() {
        return _thickness;
    }

    Color _color = new Color(255, 255, 255);

    public void setPointColor(Color color) {
        _color = color;
    }

    public Color getPointColor() {
        return _color;
    }

    List<float[]> _shape_pointer;

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

    public void setPointsCoord(List<float[]> coord) {
        List<float[]> tmp = new LinkedList<float[]>();
        for (float[] item : coord) {
            tmp.add(item);
        }
        setTriangles(tmp);
    }
    
    @Override
    public List<float[]> makeShape() {
        if (getTriangles() == null || getTriangles().size() < 2)
            return null;
        return updateShape();
    }

    float _line_thickness = 1.0f;

    public void setLineThickness(float thickness) {
        _line_thickness = thickness;
    }

    public float getLineThickness() {
        return _line_thickness;
    }

    Color _line_color = new Color(0, 0, 255);

    public void setLineColor(Color color) {
        _line_color = color;
    }

    public Color getLineColor() {
        return _line_color;
    }
}