package com.spvessel.spacevil.Core;

import com.spvessel.spacevil.*;

/**
 * Class Geometry describes all geometry properties of the item.
 */
public class Geometry implements ISize {

    public Geometry() {

    }

    // width
    private int _minWidth = 0;
    private int _width = 0;
    private int _maxWidth = SpaceVILConstants.sizeMaxValue;// Integer.MAX_VALUE;// glfw dont like Int32.MaxValue

    /**
     * Setting item width. If the value is greater/less than the maximum/minimum 
     * value of the width, then the width becomes equal to the maximum/minimum value.
     * @param width Width of the item.
     */
    public void setWidth(int width) {
        if (width < 0) {
            _width = 0;
            return;
        }

        if (width < _minWidth) {
            width = _minWidth;
        }
        if (width > _maxWidth) {
            width = _maxWidth;
        }

        _width = width;
    }

    /**
     * Getting item width.
     * @return Width of the item.
     */
    public int getWidth() {
        return _width;
    }

    /**
     * Setting the minimum width limit. Actual width cannot be less than this limit.
     * @param width Minimum width limit of the item.
     */
    public void setMinWidth(int width) {
        if (width < 0) {
            _minWidth = 0;
            return;
        }

        if (width > _width) {
            _width = width;
        }
        if (width > _maxWidth) {
            _maxWidth = width;
        }

        _minWidth = width;
    }

    /**
     * Getting the minimum width limit.
     * @return Minimum width limit of the item.
     */
    public int getMinWidth() {
        return _minWidth;
    }

    /**
     * Setting the maximum width limit. Actual width cannot be greater than this limit.
     * @param width Maximum width limit of the item.
     */
    public void setMaxWidth(int width) {
        if (width < 0) {
            _maxWidth = 0;
            return;
        }

        if (width < _width) {
            _width = width;
        }
        if (width < _minWidth) {
            _minWidth = width;
        }

        _maxWidth = width;
    }

    /**
     * Getting the maximum width limit.
     * @return Maximum width limit of the item.
     */
    public int getMaxWidth() {
        return _maxWidth;
    }

    // height
    private int _minHeight = 0;
    private int _height = 0;
    private int _maxHeight = SpaceVILConstants.sizeMaxValue;// glfw dont like Int32.MaxValue

    /**
     * Setting item height. If the value is greater/less than the maximum/minimum 
     * value of the height, then the height becomes equal to the maximum/minimum value.
     * @param height Height of the item.
     */
    public void setHeight(int height) {
        if (height < 0) {
            _height = 0;
            return;
        }

        if (height < _minHeight) {
            height = _minHeight;
        }
        if (height > _maxHeight) {
            height = _maxHeight;
        }

        _height = height;
    }

    /**
     * Getting item height.
     * @return Height of the item.
     */
    public int getHeight() {
        return _height;
    }

    /**
     * Setting the minimum height limit. Actual height cannot be less than this limit.
     * @param height Minimum height limit of the item.
     */
    public void setMinHeight(int height) {
        if (height < 0) {
            _minHeight = 0;
            return;
        }

        if (height > _height) {
            _height = height;
        }
        if (height > _maxHeight) {
            _maxHeight = height;
        }

        _minHeight = height;
    }

    /**
     * Getting the minimum height limit.
     * @return Minimum height limit of the item.
     */
    public int getMinHeight() {
        return _minHeight;
    }

    /**
     * Setting the maximum height limit. Actual height cannot be greater than this limit.
     * @param height Maximum height limit of the item.
     */
    public void setMaxHeight(int height) {
        if (height < 0) {
            _maxHeight = SpaceVILConstants.sizeMaxValue;
            return;
        }

        if (height < _height) {
            _height = height;
        }
        if (height < _minHeight) {
            _minHeight = height;
        }

        _maxHeight = height;
    }

    /**
     * Getting the maximum height limit.
     * @return Maximum height limit of the item.
     */
    public int getMaxHeight() {
        return _maxHeight;
    }

    // size
    /**
     * Setting item size (width and height).
     * @param width Width of the item.
     * @param height Height of the item.
     */
    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    /**
     * Getting current item size.
     * @return Item size as com.spvessel.spacevil.Core.Size.
     */
    public Size getSize() {
        return new Size(_width, _height);
    }
}