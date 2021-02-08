package com.spvessel.spacevil.Decorations;

import java.awt.*;

import com.spvessel.spacevil.Core.IBorder;
/**
 * Border is a class that decorates item's shape with border.
 */
public class Border implements IBorder {

    /// <summary>
    /// Getting the effect name.
    /// </summary>
    /// <returns>Returns name Shadow effect as System.String.</returns>
    /**
     * Getting the effect name.
     * 
     * @return Returns name Shadow effect as java.lang.String.
     */
    public String getEffectName() {
        return this.getClass().getName();
    }

    private CornerRadius _radius = null;

    /**
     * Getting border radiuses.
     * 
     * @return Border radiuses as com.spvessel.spacevil.Decorations.CornerRadius.
     */
    public CornerRadius getRadius() {
        return _radius;
    }

    /**
     * Setting radius of the border's corners.
     * 
     * @param value Radiuses of the border's corners as
     *              com.spvessel.spacevil.Decorations.CornerRadius.
     */
    public void setRadius(CornerRadius value) {
        _radius = value;
    }

    private Color _color;

    /**
     * Getting the border color oa an item's shape.
     * 
     * @return Border color as java.awt.Color.
     */
    public Color getColor() {
        return _color;
    }

    /**
     * Setting the border color of an item's shape.
     * 
     * @param color Border color as java.awt.Color.
     */
    public void setColor(Color color) {
        _color = color;
    }

    private int _thickness;

    /**
     * Getting border thickness of an item's shape.
     * 
     * @return Border thickness.
     */
    public int getThickness() {
        return _thickness;
    }

    /**
     * Setting border thickness of an item's shape.
     * 
     * @param value Border thickness.
     */
    public void setThickness(int value) {
        _thickness = value;
    }

    private boolean _isApplied = true;

    /**
     * Getting the visibility status of a border.
     * 
     * @return True: if border is visible. False: if border is invisible.
     */
    public boolean isApplied() {
        return _isApplied;
    }

    /**
     * Setting the visibility status of a border.
     * 
     * @param value True: if border should be visible. False: if border should be
     *              invisible.
     */
    public void setApplied(boolean value) {
        _isApplied = value;
    }

    /**
     * Default Border constructor.
     */
    public Border() {
        _color = new Color(0, 0, 0, 0);
        _thickness = -1;
        _radius = new CornerRadius();
    }

    /**
     * Constructs a Border with specified color, radius and thickness.
     * 
     * @param color     Border color as java.awt.Color.
     * @param radius    Radiuses of the border's corners as
     *                  com.spvessel.spacevil.Decorations.CornerRadius.
     * @param thickness Border thickness.
     */
    public Border(Color color, CornerRadius radius, int thickness) {
        setColor(color);
        setRadius(radius);
        setThickness(thickness);
    }

    /**
     * Clones current Border class instance.
     * 
     * @return Copy of current Border.
     */
    public Border clone() {
        Border clone = new Border(
            new Color(_color.getRed(), _color.getGreen(), _color.getBlue(), _color.getAlpha()),
            new CornerRadius(_radius), _thickness
        );

        return clone;
    }
}
