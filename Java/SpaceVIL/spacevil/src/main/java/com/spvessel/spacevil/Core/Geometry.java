package com.spvessel.spacevil.Core;

public class Geometry implements InterfaceSize {
    /**
     * Class Geometry describes all geometry properties of the item
     */

    public Geometry() {

    }

    // width
    private int _minWidth = 0;
    private int _width = 0;
    private int _maxWidth = 65535;// Integer.MAX_VALUE;// glfw dont like Int32.MaxValue

    /**
     * @param width width of the item
     */
    public void setWidth(int width) {
        if (width < 0) {
            _width = 0;
            return;
        }

        if (width < _minWidth)
            width = _minWidth;
        if (width > _maxWidth)
            width = _maxWidth;

        _width = width;
    }

    /**
     * @return width of the item
     */
    public int getWidth() {
        return _width;
    }

    /**
     * @param width minimum width of the item
     */
    public void setMinWidth(int width) {
        if (width < 0) {
            _minWidth = 0;
            return;
        }

        if (width > _width)
            _width = width;
        if (width > _maxWidth)
            _maxWidth = width;

        _minWidth = width;
    }

    /**
     * @return minimum width of the item
     */
    public int getMinWidth() {
        return _minWidth;
    }

    /**
     * @param width maximum width of the item
     */
    public void setMaxWidth(int width) {
        if (width < 0) {
            _maxWidth = 0;
            return;
        }

        if (width < _width)
            _width = width;
        if (width < _minWidth)
            _minWidth = width;

        _maxWidth = width;
    }

    /**
     * @return maximum width of the item
     */
    public int getMaxWidth() {
        return _maxWidth;
    }

    // height
    private int _minHeight = 0;
    private int _height = 0;
    private int _maxHeight = 65535;// glfw dont like Int32.MaxValue

    /**
     * @param height height of the item
     */
    public void setHeight(int height) {
        if (height < 0) {
            _height = 0;
            return;
        }

        if (height < _minHeight)
            height = _minHeight;
        if (height > _maxHeight)
            height = _maxHeight;

        _height = height;
    }

    /**
     * @return height of the item
     */
    public int getHeight() {
        return _height;
    }

    /**
     * @param height minimum height of the item
     */
    public void setMinHeight(int height) {
        if (height < 0) {
            _minHeight = 0;
            return;
        }

        if (height > _height)
            _height = height;
        if (height > _maxHeight)
            _maxHeight = height;

        _minHeight = height;
    }

    /**
     * @return minimum height of the item
     */
    public int getMinHeight() {
        return _minHeight;
    }

    /**
     * @param height maximum height of the item
     */
    public void setMaxHeight(int height) {
        if (height < 0) {
            _maxHeight = 65535;
            return;
        }

        if (height < _height)
            _height = height;
        if (height < _minHeight)
            _minHeight = height;

        _maxHeight = height;
    }

    /**
     * @return maximum height of the item
     */
    public int getMaxHeight() {
        return _maxHeight;
    }

    // size
    /**
     * Set size (width and height) of the item
     * @param width width of the item
     * @param height height of the item
     */
    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    /**
     * @return width amd height of the item
     */
    public int[] getSize() {
        return new int[] { _width, _height };
    }
}