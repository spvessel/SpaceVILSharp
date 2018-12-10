package com.spacevil.Core;

import java.awt.*;
import java.util.List;

interface InterfaceShape {
    void setItemName(String name);

    String getItemName();

    void setBackground(Color color);

    Color getBackground();

    List<float[]> getTriangles();

    void setTriangles(List<float[]> triangles);

    List<float[]> makeShape();
}