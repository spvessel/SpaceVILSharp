package com.spvessel.spacevil.Core;

import java.awt.*;
import java.util.List;

public interface InterfacePoints {
    public void setPointThickness(float thickness);

    public float getPointThickness();

    public void setPointColor(Color color);

    public Color getPointColor();

    public void setShapePointer(List<float[]> shape);

    public List<float[]> getShapePointer();

    public List<float[]> getPoints();

    public void setPointsCoord(List<float[]> coord);
}