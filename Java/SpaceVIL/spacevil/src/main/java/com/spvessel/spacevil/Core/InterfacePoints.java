package com.spvessel.spacevil.Core;

import java.awt.*;
import java.util.List;

public interface InterfacePoints {
    void setPointThickness(float thickness);

    float getPointThickness();

    void setPointColor(Color color);

    Color getPointColor();

    void setShapePointer(List<float[]> shape);

    List<float[]> getShapePointer();

    List<float[]> getPoints();

    void setPointsCoord(List<float[]> coord);
}