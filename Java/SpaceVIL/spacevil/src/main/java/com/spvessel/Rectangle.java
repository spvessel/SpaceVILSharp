package com.spvessel;

import java.util.List;

import com.spvessel.Decorations.CornerRadius;

public class Rectangle extends Primitive {
    static int count = 0;
    private CornerRadius _border_radius = new CornerRadius();

    public void SetBorderRadius(int radius) {
        _border_radius.leftTop = radius;
        _border_radius.rightTop = radius;
        _border_radius.leftBottom = radius;
        _border_radius.rightBottom = radius;
    }

    public void setBorderRadius(CornerRadius radius) {
        _border_radius = radius;
    }

    public Rectangle() {
        setItemName("Rectangle_" + count);
        count++;
    }

    @Override
    public List<float[]> makeShape() {
        setTriangles(GraphicsMathService.getRoundSquare(_border_radius, getWidth(), getHeight(), getX(), getY()));
        return GraphicsMathService.toGL(this, getHandler());
    }
}