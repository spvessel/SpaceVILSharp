package com.spvessel.spacevil;

import java.awt.Color;
import java.util.List;

import com.spvessel.spacevil.Flags.SizePolicy;

public class Graph extends Prototype {
    private static int count = 0;

    PointsContainer points;
    LinesContainer lines;

    public boolean isHover = false;

    @Override
    protected boolean getHoverVerification(float xpos, float ypos) {
        if (isHover)
            return super.getHoverVerification(xpos, ypos);
        return false;
    }

    public Graph() {
        setItemName("Graph_" + count);
        setBackground(0, 0, 0, 0);
        count++;

        points = new PointsContainer();
        lines = new LinesContainer();

        isFocusable = false;
    }

    public Graph(boolean hover) {
        this();
        isHover = hover;
    }

    @Override
    public void initElements() {
        lines.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        points.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        addItems(lines, points);
    }

    public void setPointThickness(float thickness) {
        points.setPointThickness(thickness);
    }

    public float getPointThickness() {
        return points.getPointThickness();
    }

    public void setPointColor(Color color) {
        points.setPointColor(color);
    }

    public Color getPointColor() {
        return points.getPointColor();
    }

    public void setShapePointer(List<float[]> shape) {
        points.setShapePointer(shape);
    }

    public List<float[]> getShapePointer() {
        return points.getShapePointer();
    }

    public void setPointsCoord(List<float[]> coord) {
        points.setPointsCoord(coord);
        lines.setPointsCoord(coord);
    }

    public List<float[]> getPointsCoord() {
        return points.getTriangles();
    }


    public void setLineThickness(float thickness) {
        lines.setLineThickness(thickness);
    }

    public float getLineThickness() {
        return lines.getLineThickness();
    }

    public void setLineColor(Color color) {
        lines.setLineColor(color);
    }

    public Color getLineColor() {
        return lines.getLineColor();
    }
}