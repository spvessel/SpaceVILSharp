package com.spvessel.spacevil.Core;

import java.awt.Color;
import java.util.List;

public interface InterfaceLine extends InterfacePosition {
    public void setLineThickness(float thickness);

    public float getLineThickness();

    public void setLineColor(Color color);

    public Color getLineColor();

    public List<float[]> getPoints();

    public void setPointsCoord(List<float[]> coord);
}