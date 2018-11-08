package com.spvessel;

import java.awt.*;
import java.util.List;

public interface InterfacePoints {
    void setPointThickness(float thickness);

    float getPointThickness();

    void setPointColor(Color color);

    Color getPointColor();

    void setShapePointer(List<float[]> shape);

    List<float[]> getShapePointer();

    List<float[]> makeShape();

    void setPointsCoord(List<float[]> coord);
}