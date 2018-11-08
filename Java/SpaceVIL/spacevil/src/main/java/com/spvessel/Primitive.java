package com.spvessel;

import com.spvessel.Decorations.Indents;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.ItemAlignment;

import java.util.List;

abstract public class Primitive extends BaseItem {

    public Primitive() {
        this("Primitive_");
    }

    public Primitive(int xpos, int ypos, int width, int height, String name) {
        setItemName(name);
        setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
    }

    public Primitive(String name) {
        this(0, 0, 0, 0, name);
    }

    @Override
    public List<float[]> makeShape() {
        return getTriangles();
    }

    public void setPosition(int _x, int _y) {
        this.setX(_x);
        this.setY(_y);
    }

    // style
    Boolean _is_style_set = false;

    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;

        _is_style_set = true;
        setBackground(style.background);
        setSizePolicy(style.widthPolicy, style.heightPolicy);
        setSize(style.width, style.height);
        setMinSize(style.minWidth, style.minHeight);
        setMaxSize(style.maxWidth, style.maxHeight);
        setAlignment(style.alignment);
        setPosition(style.x, style.y);
        setMargin(style.margin);
        if (style.shape != null)
            setTriangles(style.shape);
        setVisible(style.isVisible);
    }
    
    @Override
    public Style getCoreStyle() {
        Style style = new Style();
        style.setSize(getWidth(), getHeight());
        style.setSizePolicy(getWidthPolicy(), getHeightPolicy());
        style.background = getBackground();
        style.minWidth = getMinWidth();
        style.minHeight = getMinHeight();
        style.maxWidth = getMaxWidth();
        style.maxHeight = getMaxHeight();
        style.x = getX();
        style.y = getY();
        style.margin = new Indents(getMargin().left, getMargin().top, getMargin().right, getMargin().bottom);
        style.alignment = getAlignment();
        style.isVisible = getVisible();

        return style;
    }
}