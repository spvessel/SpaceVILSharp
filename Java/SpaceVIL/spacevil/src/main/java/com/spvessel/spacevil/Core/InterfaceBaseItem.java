package com.spvessel.spacevil.Core;

import java.awt.Color;

import com.spvessel.spacevil.CoreWindow;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;

public interface InterfaceBaseItem
        extends InterfaceItem, InterfaceSize, InterfacePosition, InterfaceEventUpdate, InterfaceBehavior {

    void setHandler(CoreWindow handler);

    CoreWindow getHandler();

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

    void setShadowRadius(int radius);

    int getShadowRadius();

    Color getShadowColor();

    void setShadowColor(Color color);

    Position getShadowPos();

    int[] getShadowExtension();

    void setShadow(int radius, int x, int y, Color color);

    int[] getConfines();

    void release();
}