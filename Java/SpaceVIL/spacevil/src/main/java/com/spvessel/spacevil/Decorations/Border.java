package com.spvessel.spacevil.Decorations;

import java.awt.*;

/**
 * Border is a class that decorates item's shape with border.
 */
public class Border {
    private CornerRadius _border_radius = null;

    /**
     * Getting border radiuses.
     * @return Border radiuses as com.spvessel.spacevil.Decorations.CornerRadius.
     */
    public CornerRadius getRadius() {
        return _border_radius;
    }

    /**
     * Setting radius of the border's corners.
     * @param value Radiuses of the border's corners as com.spvessel.spacevil.Decorations.CornerRadius.
     */
    public void setRadius(CornerRadius value) {
        _border_radius = value;
    }

    private Color _border_color;

    /**
     * Getting the border color oa an item's shape.
     * @return Border color as java.awt.Color.
     */
    public Color getFill() {
        return _border_color;
    }

    /**
     * Setting the border color of an item's shape.
     * @param color Border color as java.awt.Color.
     */
    public void setFill(Color color) {
        _border_color = color;
    }

    private int _border_thickness;

    /**
     * Getting border thickness of an item's shape.
     * @return Border thickness.
     */
    public int getThickness() {
        return _border_thickness;
    }

    /**
     * Setting border thickness of an item's shape.
     * @param value Border thickness.
     */
    public void setThickness(int value) {
        _border_thickness = value;
    }

    /**
     * Propery that defines if border is visible
     */
    public boolean isVisible = false;

    /**
     * Default Border constructor.
     */
    public Border() {
        _border_color = new Color(0, 0, 0, 0);
        _border_thickness = -1;
        _border_radius = new CornerRadius();
    }

    /**
     * Constructs a Border  with specified color, radius and thickness.
     * @param fill Border color as java.awt.Color.
     * @param radius Radiuses of the border's corners as com.spvessel.spacevil.Decorations.CornerRadius.
     * @param thickness Border thickness.
     */
    public Border(Color fill, CornerRadius radius, int thickness) {
        setFill(fill);
        setRadius(radius);
        setThickness(thickness);
    }
}
