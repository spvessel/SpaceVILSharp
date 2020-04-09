package com.spvessel.spacevil.Core;

import java.util.List;

import com.spvessel.spacevil.Decorations.Figure;
import com.spvessel.spacevil.Flags.ItemAlignment;

/**
 * An interface that describes visual effect which cuts specified shape from original item's shape.
 */
public interface InterfaceSubtractFigure {
    /**
     * Method for setting shape for subtraction.
     * @param figure Figure for subtraction as com.spvessel.spacevil.Decorations.Figure.
     */
    public void setSubtractFigure(Figure figure);

    /**
     * Method for getting the current figure for subtraction.
     * @return Figure for subtraction as com.spvessel.spacevil.Decorations.Figure.
     */
    public Figure getSubtractFigure();

    /**
     * Method for setting shape's shift by X, Y axis.
     * @param x X axis shift.
     * @param y Y axis shift.
     */
    public void setPositionOffset(int x, int y);

    /**
     * Method for setting shape's scaling factors for width and height.
     * @param wScale Scaling factor for width.
     * @param hScale Scaling factor for height.
     */
    public void setSizeScale(float wScale, float hScale);

    /**
     * Method for getting shape's shift by X-axis.
     * @return X axis shift.
     */
    public int getXOffset();

    /**
     * Method for getting shape's shift by Y-axis.
     * @return Y axis shift.
     */
    public int getYOffset();

    /**
     * Method for getting width scaling.
     * @return Width scaling.
     */
    public float getWidthScale();

    /**
     * Method for getting height scaling.
     * @return Height scaling.
     */
    public float getHeightScale();

    /**
     * Method for setting shape's allignment within the item.
     * @param alignment Alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setAlignment(ItemAlignment... alignment);

    /**
     * Method for getting shape's allignment within the item.
     * @return Alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public List<ItemAlignment> getAlignment();
}
