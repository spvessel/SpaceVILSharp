package com.spvessel.spacevil.Core;

import java.awt.*;
import java.util.List;

public interface InterfacePoints {
    public void setPointThickness(float thickness);

    public float getPointThickness();

    public void setPointColor(Color color);

    public Color getPointColor();

    public void setPointShape(List<float[]> shape);

    public List<float[]> getPointShape();

    public List<float[]> getPoints();

    public void setPoints(List<float[]> coord);
}