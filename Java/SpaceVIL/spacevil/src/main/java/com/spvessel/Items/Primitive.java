package com.spvessel.Items;

import java.util.List;
import com.spvessel.Decorations.*;
import com.spvessel.Flags.ItemAlignment;

abstract public class Primitive extends BaseItem {

    public Primitive() {
        this(0, 0, 0, 0, "Primitive_");
    }

    public Primitive(int xpos, int ypos, int width, int height, String name) {
        setItemName(name);
        setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
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
}