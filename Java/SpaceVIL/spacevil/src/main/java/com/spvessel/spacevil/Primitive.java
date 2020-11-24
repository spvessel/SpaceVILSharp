package com.spvessel.spacevil;

import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;

/**
 * The Primitive is an abstract extension of BaseItem for primitive
 * non-interactive items.
 * <p>
 * Examples of subclasses: com.spvessel.spacevil.Ellipse,
 * com.spvessel.spacevil.Rectangle, com.spvessel.spacevil.Triangle and etc.
 */
abstract public class Primitive extends BaseItem {
    /**
     * Default constructor of Primitive class.
     */
    public Primitive() {
        this("Primitive_");
    }

    /**
     * Constructs a Primitive with the specified name.
     * 
     * @param name Item name of Primitive.
     */
    public Primitive(String name) {
        setItemName(name);
        setAlignment(ItemAlignment.Top, ItemAlignment.Left);
    }

    /**
     * Setting item position.
     * 
     * @param x X position of the left-top corner.
     * @param y Y position of the left-top corner.
     */
    public void setPosition(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    // style
    Boolean _isStyleSet = false;

    /**
     * Setting a style that describes the appearance of an item.
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;

        _isStyleSet = true;
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
     * Getting the core (only appearance properties without inner styles) style of
     * an item.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
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