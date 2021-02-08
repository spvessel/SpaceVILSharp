package com.spvessel.spacevil.Core;

import java.awt.Color;
import com.spvessel.spacevil.Decorations.CornerRadius;

/**
 * IBorder is an interface for creating classes that decorates item's shape with
 * border.
 */
public interface IBorder extends IEffect {
    /**
     * Getting border radiuses.
     * 
     * @return Border radiuses as com.spvessel.spacevil.Decorations.CornerRadius.
     */
    CornerRadius getRadius();

    /**
     * Setting radius of the border's corners.
     * 
     * @param value Radiuses of the border's corners as
     *              com.spvessel.spacevil.Decorations.CornerRadius.
     */
    void setRadius(CornerRadius value);

    /**
     * Getting the border color oa an item's shape.
     * 
     * @return Border color as java.awt.Color.
     */
    Color getColor();

    /**
     * Setting the border color of an item's shape.
     * 
     * @param color Border color as java.awt.Color.
     */
    void setColor(Color color);

    /**
     * Getting border thickness of an item's shape.
     * 
     * @return Border thickness.
     */
    int getThickness();

    /**
     * Setting border thickness of an item's shape.
     * 
     * @param thickness Border thickness.
     */
    void setThickness(int thickness);
}
