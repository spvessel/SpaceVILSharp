package com.spvessel.spacevil.Core;

public interface InterfaceSize {

    void setSize(int width, int height);

    Size getSize();

    void setMinWidth(int width);

    void setWidth(int width);

    void setMaxWidth(int width);

    int getMinWidth();

    int getWidth();

    int getMaxWidth();

    void setMinHeight(int height);

    void setHeight(int height);

    void setMaxHeight(int height);

    int getMinHeight();

    int getHeight();

    int getMaxHeight();
}