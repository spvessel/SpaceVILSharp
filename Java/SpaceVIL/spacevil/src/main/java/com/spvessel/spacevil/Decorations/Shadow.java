package com.spvessel.spacevil.Decorations;

import java.awt.Color;

import com.spvessel.spacevil.GraphicsMathService;

public final class Shadow {
    private int _radius = 0;

    public void setRadius(int value) {
        if (value < 0 || value > 10)
            return;
        _radius = value;
    }

    public int getRadius() {
        return _radius;
    }

    private int _x = 0;

    public void setXOffset(int value) {
        _x = value;
    }

    public int getXOffset() {
        return _x;
    }

    private int _y = 0;

    public void setYOffset(int value) {
        _y = value;
    }

    public int getYOffset() {
        return _y;
    }

    private Color _color = Color.BLACK;

    public void setColor(Color color) {
        _color = new Color(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
    }

    public void setColor(int r, int g, int b) {
        _color = GraphicsMathService.colorTransform(r, g, b);
    }

    public void setColor(int r, int g, int b, int a) {
        _color = GraphicsMathService.colorTransform(r, g, b, a);
    }

    public void setColor(float r, float g, float b) {
        _color = GraphicsMathService.colorTransform(r, g, b);
    }

    public void setColor(float r, float g, float b, float a) {
        _color = GraphicsMathService.colorTransform(r, g, b, a);
    }

    public Color getColor() {
        return new Color(_color.getAlpha(), _color.getRed(), _color.getGreen(), _color.getBlue());
    }

    private boolean _isDrop;

    public void setDrop(boolean value) {
        _isDrop = value;
    }

    public boolean isDrop() {
        return _isDrop;
    }

    public Shadow() {
        _isDrop = true;
    }

    public Shadow(int radius, int x, int y, Color color) {
        this();
        _radius = radius;
        _x = x;
        _y = y;
        _color = new Color(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
    }
}