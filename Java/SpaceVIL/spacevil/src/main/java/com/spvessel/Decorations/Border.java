package com.spvessel.Decorations;

import java.awt.*;

public class Border {

    private CornerRadius _border_radius = null;

    public CornerRadius getRadius() {
        return _border_radius;
    }

    public void setRadius(CornerRadius value) {
        _border_radius = value;
    }

    private Color _border_color;

    public Color getFill() {
        return _border_color;
    }

    public void setFill(Color color) {
        _border_color = color;
    }

    private int _border_thickness;

    public int getThickness() {
        return _border_thickness;
    }

    public void setThickness(int value) {
        _border_thickness = value;
    }

    public Border() {
        _border_color = new Color(0, 0, 0, 0);
        _border_thickness = -1;
    }
}
