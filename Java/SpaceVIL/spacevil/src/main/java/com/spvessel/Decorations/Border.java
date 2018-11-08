package com.spvessel.Decorations;

import java.awt.*;

public class Border {
    // private CornerRadius _border_radius;
    // public CornerRadius Radius
    // {
    // get { return _border_radius; }
    // set { _border_radius = value; }
    // }

    private int _border_radius;

    public int getRadius() {
        return _border_radius;
    }

    public void setRadius(int value) {
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
        _border_radius = -1;
    }
}
