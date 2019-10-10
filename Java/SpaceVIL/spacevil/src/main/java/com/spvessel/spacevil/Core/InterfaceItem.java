package com.spvessel.spacevil.Core;

import java.awt.Color;
import java.util.List;

public interface InterfaceItem {
    void setItemName(String name);

    String getItemName();

    void setBackground(Color color);

    Color getBackground();

    List<float[]> getTriangles();

    void setTriangles(List<float[]> triangles);

    void makeShape();
}