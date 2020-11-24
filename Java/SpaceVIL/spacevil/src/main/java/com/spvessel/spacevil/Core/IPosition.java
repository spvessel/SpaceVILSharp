package com.spvessel.spacevil.Core;

/**
 * An interface that describes the attributes of the position of a shape.
 * <p> This interface is part of com.spvessel.spacevil.Core.IBaseItem.
 */
public interface IPosition {
    /**
     * Method for setting X coordinate of the left-top corner of a shape.
     * @param x X coordinate.
     */
    public void setX(int x);

    /**
     * Method for getting X coordinate of the left-top corner of a shape.
     * @return X coordinate.
     */
    public int getX();

    /**
     * Method for setting Y coordinate of the left-top corner of a shape.
     * @param y Y coordinate.
     */
    public void setY(int y);

    /**
     * Method for getting Y coordinate of the left-top corner of a shape.
     * @return Y coordinate.
     */
    public int getY();
}