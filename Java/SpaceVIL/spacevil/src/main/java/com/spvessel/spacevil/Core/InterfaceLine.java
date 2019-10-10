package com.spvessel.spacevil.Core;

import java.awt.Color;
import java.util.List;

public interface InterfaceLine extends InterfacePosition {
    void setLineThickness(float thickness);

    float getLineThickness();

    void setLineColor(Color color);

    Color getLineColor();

    List<float[]> getPoints();

    void setPointsCoord(List<float[]> coord);
}