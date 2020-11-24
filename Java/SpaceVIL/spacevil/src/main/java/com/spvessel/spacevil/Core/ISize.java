package com.spvessel.spacevil.Core;

/**
 * An interface that describes the attributes of the size of a shape.
 * <p> This interface is part of com.spvessel.spacevil.Core.IBaseItem.
 */
public interface ISize {
    /**
     * Method setting size of an item's shape.
     * @param width Width of a shape.
     * @param height Height of a shape.
     */
    public void setSize(int width, int height);

    /**
     * Method getting size of an item's shape.
     * @return Size of the shape as com.spvessel.spacevil.Core.Size.
     */
    public Size getSize();

    /**
     * Method setting the minimum width limit. Actual width cannot be less than this limit.
     * @param width Minimum width limit of the item.
     */
    public void setMinWidth(int width);

    /**
     * Method setting item width. If the value is greater/less than the maximum/minimum 
     * value of the width, then the width becomes equal to the maximum/minimum value.
     * @param width Width of the item.
     */
    public void setWidth(int width);

    /**
     * Method setting the maximum width limit. Actual width cannot be greater than this limit.
     * @param width Maximum width limit of the item.
     */
    public void setMaxWidth(int width);

    /**
     * Method getting the minimum width limit.
     * @return Minimum width limit of the item.
     */
    public int getMinWidth();

    /**
     * Method fetting item width.
     * @return Width of the item.
     */
    public int getWidth();

    /**
     * Method getting the maximum width limit.
     * @return Maximum width limit of the item.
     */
    public int getMaxWidth();

    /**
     * Method for setting the minimum height limit. Actual height cannot be less than this limit.
     * @param height Minimum height limit of the item.
     */
    public void setMinHeight(int height);

    /**
     * Method for setting item height. If the value is greater/less than the maximum/minimum 
     * value of the height, then the height becomes equal to the maximum/minimum value.
     * @param height Height of the item.
     */
    public void setHeight(int height);

    /**
     * Method for setting the maximum height limit. Actual height cannot be greater than this limit.
     * @param height Maximum height limit of the item.
     */
    public void setMaxHeight(int height);

    /**
     * Method for getting the minimum height limit.
     * @return Minimum height limit of the item.
     */
    public int getMinHeight();

    /**
     * Method for getting item height.
     * @return Height of the item.
     */
    public int getHeight();

    /**
     * Method for getting the maximum height limit.
     * @return Maximum height limit of the item.
     */
    public int getMaxHeight();
}