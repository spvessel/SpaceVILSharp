package com.spvessel.Core;

import com.spvessel.Core.InterfaceSize;

public class Geometry implements InterfaceSize {
    public Geometry() {

    }

    // width
    private int _minWidth = 0;
    private int _width = 0;
    private int _maxWidth = 65535;// Integer.MAX_VALUE;// glfw dont like Int32.MaxValue

    public  void setWidth(int width) {
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

    public  int getWidth() {
        return _width;
    }

    public  void setMinWidth(int width) {
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

    public  int getMinWidth() {
        return _minWidth;
    }

    public  void setMaxWidth(int width) {
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

    public  int getMaxWidth() {
        return _maxWidth;
    }

    // height
    private int _minHeight = 0;
    private int _height = 0;
    private int _maxHeight = 65535;// glfw dont like Int32.MaxValue

    public  void setHeight(int height) {
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

    public  int getHeight() {
        return _height;
    }

    public  void setMinHeight(int height) {
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

    public  int getMinHeight() {
        return _minHeight;
    }

    public  void setMaxHeight(int height) {
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

    public  int getMaxHeight() {
        return _maxHeight;
    }

    // size
    public  void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public  int[] getSize() {
        return new int[] { _width, _height };
    }
}