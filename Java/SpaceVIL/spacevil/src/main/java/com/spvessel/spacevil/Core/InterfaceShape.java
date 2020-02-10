package com.spvessel.spacevil.Core;

import java.awt.*;
import java.util.List;

public interface InterfaceShape {
    public void setItemName(String name);

    public String getItemName();

    public void setBackground(Color color);

    public Color getBackground();

    public List<float[]> getTriangles();

    public void setTriangles(List<float[]> triangles);

    public List<float[]> makeShape();
}