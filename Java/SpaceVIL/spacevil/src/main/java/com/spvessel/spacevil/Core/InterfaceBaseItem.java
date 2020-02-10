package com.spvessel.spacevil.Core;

import java.awt.Color;

import com.spvessel.spacevil.CoreWindow;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;

public interface InterfaceBaseItem
        extends InterfaceItem, InterfaceSize, InterfacePosition, InterfaceEventUpdate, InterfaceBehavior {

    public void setHandler(CoreWindow handler);

    public CoreWindow getHandler();

    public void setParent(Prototype parent);

    public Prototype getParent();

    public void setConfines();

    public void setConfines(int x0, int x1, int y0, int y1);

    public void setMargin(Indents padding);

    public void setMargin(int left, int top, int right, int bottom);

    public Indents getMargin();

    public void initElements();

    public void setStyle(Style style);

    public Style getCoreStyle();

    public boolean isDrawable();

    public void setDrawable(boolean value);

    public boolean isVisible();

    public void setVisible(boolean value);

    public boolean isShadowDrop();

    public void setShadowDrop(boolean value);

    public void setShadowRadius(int radius);

    public int getShadowRadius();

    public Color getShadowColor();

    public void setShadowColor(Color color);

    public Position getShadowPos();

    public int[] getShadowExtension();

    public void setShadow(int radius, int x, int y, Color color);

    public void release();
}