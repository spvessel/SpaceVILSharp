package com.spvessel.spacevil.Decorations;

import java.awt.Color;

import com.spvessel.spacevil.GraphicsMathService;

/**
 * Class that is the shadow of an item.
 */
public final class Shadow {
    private int _radius = 0;
    private int _maxAvailableRadius = 10;

    /**
     * Setting the specified blur radius of the shadow.
     * <p> Default: 0.
     * @param value Blur radius of the shadow. Min value: 0. Max value: 10.
     */
    public void setRadius(int value) {
        if (value < 0 || value > _maxAvailableRadius) {
            return;
        }
        _radius = value;
    }

    /**
     * Getting the shadow blur raduis.
     * @return The blur radius of the shadow. Min value: 0. Max value: 10.
     */
    public int getRadius() {
        return _radius;
    }

    private int _x = 0;

    /**
     * Setting X shift of the shadow.
     * @param value Shift by X-axis.
     */
    public void setXOffset(int value) {
        _x = value;
    }

    /**
     * Getting X shift of the shadow.
     * @return Shift by X-axis.
     */
    public int getXOffset() {
        return _x;
    }

    private int _y = 0;

    /**
     * Setting Y shift of the shadow.
     * @param value Shift by Y-axis.
     */
    public void setYOffset(int value) {
        _y = value;
    }

    /**
     * Setting Y shift of the shadow.
     * @return Shift by Y-axis.
     */
    public int getYOffset() {
        return _y;
    }

    private Color _color = Color.BLACK;

    /**
     * Setting shadow color.
     * @param color Shadow color as java.awt.Color.
     */
    public void setColor(Color color) {
        _color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * Setting shadow color in RGB format.
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setColor(int r, int g, int b) {
        _color = GraphicsMathService.colorTransform(r, g, b);
    }

    /**
     * Setting shadow color in byte RGBA format.
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setColor(int r, int g, int b, int a) {
        _color = GraphicsMathService.colorTransform(r, g, b, a);
    }

    /**
     * Setting shadow color in float RGB format.
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setColor(float r, float g, float b) {
        _color = GraphicsMathService.colorTransform(r, g, b);
    }

    /**
     * Setting shadow color in float RGBA format.
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setColor(float r, float g, float b, float a) {
        _color = GraphicsMathService.colorTransform(r, g, b, a);
    }

    /**
     * Getting shadow color.
     * @return Returns the shadow color as java.awt.Color.
     */
    public Color getColor() {
        return new Color(_color.getRed(), _color.getGreen(), _color.getBlue(), _color.getAlpha());
    }

    private boolean _isDrop;

    /**
     * Setting drop shadow flag.
     * @param value True: allow shadow dropping. False: not allow shadow dropping.
     */
    public void setDrop(boolean value) {
        _isDrop = value;
    }

    /**
     * Getting shadow drop flag.
     * @return True: allow shadow dropping. False: not allow shadow dropping.
     */
    public boolean isDrop() {
        return _isDrop;
    }

    /**
     * Default Shadow class constructor. Allow shadow dropping.
     */
    public Shadow() {
        _isDrop = true;
    }

    /**
     * Shadow class constructor with specified blur radius, axis shifts, shadow color. Allow shadow dropping.
     * @param radius A blur radius of the shadow.
     * @param x X shift of the shadow.
     * @param y Y shift of the shadow.
     * @param color A shadow color as java.awt.Color.
     */
    public Shadow(int radius, int x, int y, Color color) {
        this();
        _radius = radius;
        _x = x;
        _y = y;
        _color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
}