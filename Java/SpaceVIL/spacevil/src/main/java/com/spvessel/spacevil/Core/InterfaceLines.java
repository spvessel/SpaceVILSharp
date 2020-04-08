package com.spvessel.spacevil.Core;

import java.awt.Color;
import java.util.List;

/**
 * An interface that discribes such type of items 
 * that are adjacent points are considered lines (for drawing graphs).
 */
public interface InterfaceLines extends InterfacePosition {
    /**
     * Setting thickness of lines.
     * @param thickness Line thickness.
     */
    public void setLineThickness(float thickness);

    /**
     * Getting lines thickness.
     * @return Lines thickness.
     */
    public float getLineThickness();

    /**
     * Setting lines color.
     * @param color Line color.
     */
    public void setLineColor(Color color);

    /**
     * Getting lines color.
     * @return Lines color.
     */
    public Color getLineColor();

    /**
     * Getting adjacent points are considered lines.
     * @return Points list as List of float[2] array.
     */
    public List<float[]> getPoints();

    /**
     * Setting adjacent points are considered lines.
     * @param coord Points list as List of float[2] array.
     */
    public void setPointsCoord(List<float[]> coord);
}