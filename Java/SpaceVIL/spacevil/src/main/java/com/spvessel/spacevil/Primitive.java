package com.spvessel.spacevil;

import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;

abstract public class Primitive extends BaseItem {
    /**
     * Constructs a Primitive
     */
    public Primitive() {
        this("Primitive_");
    }

    /**
     * Constructs a Primitive with name
     */
    public Primitive(String name) {
        setItemName(name);
        setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
    }

    /**
     * Set item position
     * @param _x X position of the item left top corner
     * @param _y Y position of the item left top corner
     */
    public void setPosition(int _x, int _y) {
        this.setX(_x);
        this.setY(_y);
    }

    // style
    Boolean _is_style_set = false;

    /**
     * Set style of the Primitive
     */
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

    /**
     * @return style of the Primitive
     */
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
        style.isVisible = isVisible();

        return style;
    }
}