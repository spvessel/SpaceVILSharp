package com.spvessel.spacevil.Core;

import java.awt.Color;
import java.util.List;

public interface InterfaceLine {
    void setLineThickness(float thickness);

    float getLineThickness();

    void setLineColor(Color color);

    Color getLineColor();

    List<float[]> makeShape();

    void setPointsCoord(List<float[]> coord);
}