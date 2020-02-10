package com.spvessel.spacevil.Core;

import java.awt.Color;
import java.util.List;

public interface InterfaceItem {
    public void setItemName(String name);

    public String getItemName();

    public void setBackground(Color color);

    public Color getBackground();

    public List<float[]> getTriangles();

    public void setTriangles(List<float[]> triangles);

    public void makeShape();
}