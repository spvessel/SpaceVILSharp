package com.spvessel.spacevil.Core;

import java.awt.Color;
import java.util.List;

public interface InterfaceLines extends InterfacePosition {
    public void setLineThickness(float thickness);

    public float getLineThickness();

    public void setLineColor(Color color);

    public Color getLineColor();

    public List<float[]> getPoints();

    public void setPoints(List<float[]> coord);
}