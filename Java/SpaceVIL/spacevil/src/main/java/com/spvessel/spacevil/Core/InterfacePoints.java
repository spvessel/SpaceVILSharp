package com.spvessel.spacevil.Core;

import java.awt.*;
import java.util.List;

/**
 * An interface that discribes such type of items 
 * that are points (for drawing graphs).
 */
public interface InterfacePoints {
    /**
     * Setting thickness of points.
     * @param thickness Point thickness.
     */
    public void setPointThickness(float thickness);

    /**
     * Getting points thickness.
     * @return Point thickness.
     */
    public float getPointThickness();

    /**
     * Setting points color.
     * @param color Points color as java.awt.Color.
     */
    public void setPointColor(Color color);

    /**
     * Getting points color.
     * @return Points color as java.awt.Color.
     */
    public Color getPointColor();

    /**
     * Setting custom shape for points (if one want to use other shape than circle).
     * @param shape Points list of the shape as List of float[2] array.
     */
    public void setShapePointer(List<float[]> shape);

    /**
     * Getting current shape of points. Default: circle shape.
     * @return Points list of the shape as List of float[2] array.
     */
    public List<float[]> getShapePointer();

    /**
     * Getting points coordinates.
     * @return Points list as List of float[2] array.
     */
    public List<float[]> getPoints();

    /**
     * Setting points coordinates.
     * @param coord Points list as List of float[2] array.
     */
    public void setPointsCoord(List<float[]> coord);
}