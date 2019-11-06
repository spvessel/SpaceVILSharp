package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceTextImage;

class TextPrinter implements InterfaceTextImage {
    private byte[] texture = null;
    private int xShift = 0;
    private int yShift = 0;
    private int width = 0;
    private int height = 0;

    TextPrinter() {
        texture = null;
    }

    TextPrinter(byte[] bb) {
        texture = bb;
    }

    void setAttr(int width, int height, int xOffset, int yOffset) {
        this.width = width;
        this.height = height;
        xShift = xOffset;
        yShift = yOffset;
    }

    void setPosition(int xOffset, int yOffset) {
        xShift = xOffset;
        yShift = yOffset;
    }

    void setXOffset(int x) {
        xShift = x;
    }

    void setYOffset(int y) {
        yShift = y;
    }

    void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public byte[] getBytes() {
        return texture;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getXOffset() {
        return xShift;
    }

    @Override
    public int getYOffset() {
        return yShift;
    }

    @Override
    public boolean isEmpty() {
        if (texture == null || texture.length == 0)
            return true;
        return false;
    }
}