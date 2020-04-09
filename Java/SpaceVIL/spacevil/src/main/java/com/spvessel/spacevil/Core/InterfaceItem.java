package com.spvessel.spacevil.Core;

import java.awt.Color;
import java.util.List;

/**
 * An interface that describes common item's properties.
 * <p> This interface is part of com.spvessel.spacevil.Core.InterfaceBaseItem.
 */
public interface InterfaceItem {
    /**
     * Method for setting the name of the item.
     * @param name Item name as java.lang.String.
     */
    public void setItemName(String name);

    /**
     * Method for getting the name of the item.
     * @return Item name as java.lang.String.
     */
    public String getItemName();

    /**
     * Method for setting background color.
     * @param color Background color as java.awt.Color.
     */
    public void setBackground(Color color);

    /**
     * Method for getting background color.
     * @return Background color as java.awt.Color.
     */
    public Color getBackground();

    /**
     * Method for getting triangles of item's shape.
     * @return Points list of the shape as List of float[2] array (2D).
     */
    public List<float[]> getTriangles();

    /**
     * Method for setting triangles as item's shape.
     * @param triangles Points list of the shape as List of float[2] array (2D).
     */
    public void setTriangles(List<float[]> triangles);

    /**
     * Method for making default item's shape. Use in conjunction with 
     * getTriangles() and setTriangles() methods.
     */
    public void makeShape();
}