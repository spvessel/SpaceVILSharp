package com.spvessel.spacevil;

import java.util.List;

import com.spvessel.spacevil.Decorations.CornerRadius;

public class Rectangle extends Primitive {
    private static int count = 0;
    private CornerRadius _border_radius = new CornerRadius();

    public void setBorderRadius(int radius) {
        _border_radius.leftTop = radius;
        _border_radius.rightTop = radius;
        _border_radius.leftBottom = radius;
        _border_radius.rightBottom = radius;
    }

    public void setBorderRadius(CornerRadius radius) {
        _border_radius = radius;
    }

    /**
     * Constructs a Rectangle
     */
    public Rectangle() {
        setItemName("Rectangle_" + count);
        count++;
    }

    /**
     * Make rectangle with width, height, X, Y
     */
    @Override
    public void makeShape() {
        setTriangles(GraphicsMathService.getRoundSquare(_border_radius, getWidth(), getHeight(), 0, 0));
    }
}