package com.spvessel.spacevil.Decorations;

import java.awt.*;

public class Border {
    /**
     * Border of the item
     */

    private CornerRadius _border_radius = null;

    /**
     * @return Radius of the border's corners
     */
    public CornerRadius getRadius() {
        return _border_radius;
    }

    /**
     * Set radius of the border's corners
     */
    public void setRadius(CornerRadius value) {
        _border_radius = value;
    }

    private Color _border_color;

    /**
     * Border color
     */
    public Color getFill() {
        return _border_color;
    }

	/**
     * Border color
     */
    public void setFill(Color color) {
        _border_color = color;
    }

    private int _border_thickness;

    /**
     * Border thickness
     */
    public int getThickness() {
        return _border_thickness;
    }

	/**
     * Border thickness
     */
    public void setThickness(int value) {
        _border_thickness = value;
    }

    /**
     * Is border visible
     */
    public boolean isVisible = false;

    /**
     * Constructs a Border
     */
    public Border() {
        _border_color = new Color(0, 0, 0, 0);
        _border_thickness = -1;
    }

    /**
     * Constructs a Border with color, radius and thickness
     */
    public Border(Color fill, CornerRadius radius, int thickness) {
        setFill(fill);
        setRadius(radius);
        setThickness(thickness);
    }
}
