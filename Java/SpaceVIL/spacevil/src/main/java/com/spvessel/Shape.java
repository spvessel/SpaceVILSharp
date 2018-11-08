package com.spvessel;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Shape implements InterfaceShape {
    private String _name;

    public void setItemName(String name) {
        _name = name;
    }

    public String getItemName() {
        return _name;
    }

    public Color _bg;

    public void setBackground(Color color) {
        _bg = color;
    }

    public Color getBackground() {
        return _bg;
    }

    private List<float[]> _triangles = new LinkedList<float[]>();

    public void setTriangles(List<float[]> triangles) {
        _triangles = triangles;
    }

    public List<float[]> getTriangles() {
        return _triangles;
    }

    public List<float[]> makeShape() {
        return getTriangles();
    }
}