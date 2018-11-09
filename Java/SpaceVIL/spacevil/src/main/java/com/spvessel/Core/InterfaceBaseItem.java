package com.spvessel.Core;

import java.awt.Color;

import com.spvessel.Prototype;
import com.spvessel.WindowLayout;
import com.spvessel.Decorations.Indents;
import com.spvessel.Decorations.Style;

public interface InterfaceBaseItem
        extends InterfaceItem, InterfaceSize, InterfacePosition, InterfaceEventUpdate, InterfaceBehavior {

    void setHandler(WindowLayout handler);

    WindowLayout getHandler();

    void setParent(Prototype parent);

    Prototype getParent();

    void setConfines();

    void setConfines(int x0, int x1, int y0, int y1);

    void setMargin(Indents padding);

    void setMargin(int left, int top, int right, int bottom);

    Indents getMargin();

    void initElements();

    void setStyle(Style style);

    Style getCoreStyle();

    boolean isDrawable();

    void setDrawable(boolean value);

    boolean isVisible();

    void setVisible(boolean value);

    boolean isShadowDrop();

    void setShadowDrop(boolean value);

    void setShadowRadius(float radius);

    float getShadowRadius();

    Color getShadowColor();

    void setShadowColor(Color color);

    Position getShadowPos();

    void setShadow(float radius, int x, int y, Color color);

    int[] getConfines();
}