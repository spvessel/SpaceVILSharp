package com.spvessel.Cores;

import java.awt.*;
import java.util.List;
import java.util.LinkedList;

public class Item implements InterfaceItem {
    private String _name;

    public void setItemName(String name) {
        _name = name;
    }

    public String getItemName() {
        return _name;
    }

    private Color _bg = new Color(255, 255, 255, 255);

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