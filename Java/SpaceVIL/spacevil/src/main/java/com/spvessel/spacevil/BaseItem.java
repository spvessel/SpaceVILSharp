package com.spvessel.spacevil;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.spvessel.spacevil.Core.Geometry;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceFloating;
import com.spvessel.spacevil.Core.InterfaceFreeLayout;
import com.spvessel.spacevil.Core.InterfaceHLayout;
import com.spvessel.spacevil.Core.InterfaceVLayout;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Core.Size;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemHoverRule;
import com.spvessel.spacevil.Flags.SizePolicy;

public abstract class BaseItem implements InterfaceBaseItem {

    int _confines_x_0 = 0;
    int _confines_x_1 = 0;
    int _confines_y_0 = 0;
    int _confines_y_1 = 0;

    private CoreWindow _coreWindow;

    /**
     * CoreWindow handler - window that contains the BaseItem
     */
    public void setHandler(CoreWindow handler) {
        _coreWindow = handler;
    }

    public CoreWindow getHandler() {
        return _coreWindow;
    }

    // parent
    private Prototype _parent = null;

    /**
     * BaseItem's parent item
     */
    public Prototype getParent() {
        return _parent;
    }

    public void setParent(Prototype parent) {
        _parent = parent;
    }

    void addChildren(InterfaceBaseItem item) {
        Prototype itemParent = item.getParent();
        if (itemParent != null)
            itemParent.removeItem(item);

        item.setParent(((VisualItem) this).prototype);
        itemParent = item.getParent();

        // refactor events verification
        if (itemParent instanceof InterfaceFreeLayout) {
            return;
        }

        if (itemParent instanceof InterfaceVLayout) {
            addEvents(item, GeometryEventType.RESIZE_WIDTH, GeometryEventType.MOVED_X);
            return;
        }
        if (itemParent instanceof InterfaceHLayout) {
            addEvents(item, GeometryEventType.RESIZE_HEIGHT, GeometryEventType.MOVED_Y);
            return;
        }

        addEvents(item, GeometryEventType.RESIZE_WIDTH, GeometryEventType.RESIZE_HEIGHT, GeometryEventType.MOVED_X,
                GeometryEventType.MOVED_Y);
    }

    private void addEvents(InterfaceBaseItem listener, GeometryEventType... types) {
        for (GeometryEventType t : types) {
            addEventListener(t, listener);
        }
        BaseItemStatics.castToUpdateBehavior(listener);
    }

    void addEventListener(GeometryEventType type, InterfaceBaseItem listener) {
    }

    void removeEventListener(GeometryEventType type, InterfaceBaseItem listener) {
    }

    /**
     * Item will not react on parent's changes
     */
    void removeItemFromListeners() {
        Prototype parent = getParent();
        parent.removeEventListener(GeometryEventType.RESIZE_WIDTH, this);
        parent.removeEventListener(GeometryEventType.RESIZE_HEIGHT, this);
        parent.removeEventListener(GeometryEventType.MOVED_X, this);
        parent.removeEventListener(GeometryEventType.MOVED_Y, this);
    }

    /**
     * Initialization and adding of all elements in the BaseItem
     */
    public void initElements() {
        // do nothing
    }

    private Item _item = new Item();

    private Indents _margin = new Indents();

    /**
     * BaseItem margin
     */
    public Indents getMargin() {
        return _margin;
    }

    public void setMargin(Indents margin) {
        _margin = margin;
        updateGeometry();
        BaseItemStatics.updateAllLayout(this);
    }

    public void setMargin(int left, int top, int right, int bottom) {
        setMargin(new Indents(left, top, right, bottom));
    }

    /**
     * @return triangles list of the BaseItem's shape
     */
    public List<float[]> getTriangles() {
        return _item.getTriangles();
    }

    /**
     * Sets BaseItem's shape as triangles list
     */
    public void setTriangles(List<float[]> triangles) {
        _item.setTriangles(triangles);
    }

    /**
     * @return shape points list in GL coordinates, using triangles from
     *         getTriangles()
     */
    public void makeShape() {

    }

    List<float[]> updateShape() {
        return BaseItemStatics.updateShape(this);
    }

    /**
     * Background color of the BaseItem
     */
    public void setBackground(Color color) {
        _item.setBackground(color);
    }

    public void setBackground(int r, int g, int b) {
        _item.setBackground(GraphicsMathService.colorTransform(r, g, b));
    }

    public void setBackground(int r, int g, int b, int a) {
        _item.setBackground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    public void setBackground(float r, float g, float b) {
        _item.setBackground(GraphicsMathService.colorTransform(r, g, b));
    }

    public void setBackground(float r, float g, float b, float a) {
        _item.setBackground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    public Color getBackground() {
        return _item.getBackground();
    }

    /**
     * BaseItem's name
     */
    public void setItemName(String name) {
        _item.setItemName(name);
    }

    public String getItemName() {
        return _item.getItemName();
    }

    private boolean _drawable = true;

    /**
     * If BaseItem will drawn by engine
     */
    public boolean isDrawable() {
        return _drawable;
    }

    public void setDrawable(boolean value) {
        if (_drawable == value)
            return;
        _drawable = value;
    }

    private boolean _visible = true;

    /**
     * If BaseItem is visible
     */
    public boolean isVisible() {
        return _visible;
    }

    public void setVisible(boolean value) {
        if (_visible == value)
            return;
        _visible = value;
    }

    void updateInnersDrawable(boolean value) {
    }

    // geometry
    private Geometry _itemGeometry = new Geometry();

    /**
     * Width of the BaseItem
     */
    public void setMinWidth(int width) {
        _itemGeometry.setMinWidth(width);
    }

    public void setWidth(int width) {
        _itemGeometry.setWidth(width);
        ItemsRefreshManager.setRefreshShape(this);
    }

    public void setMaxWidth(int width) {
        _itemGeometry.setMaxWidth(width);
    }

    public int getMinWidth() {
        return _itemGeometry.getMinWidth();
    }

    public int getWidth() {
        return _itemGeometry.getWidth();
    }

    public int getMaxWidth() {
        return _itemGeometry.getMaxWidth();
    }

    /**
     * Height of the BaseItem
     */
    public void setMinHeight(int height) {
        _itemGeometry.setMinHeight(height);
    }

    public void setHeight(int height) {
        _itemGeometry.setHeight(height);
        ItemsRefreshManager.setRefreshShape(this);
    }

    public void setMaxHeight(int height) {
        _itemGeometry.setMaxHeight(height);
    }

    public int getMinHeight() {
        return _itemGeometry.getMinHeight();
    }

    public int getHeight() {
        return _itemGeometry.getHeight();
    }

    public int getMaxHeight() {
        return _itemGeometry.getMaxHeight();
    }

    /**
     * Size (width and height) of the BaseItem
     */
    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void setMinSize(int width, int height) {
        setMinWidth(width);
        setMinHeight(height);
    }

    public void setMaxSize(int width, int height) {
        setMaxWidth(width);
        setMaxHeight(height);
    }

    public Size getSize() {
        return _itemGeometry.getSize();
    }

    public Size getMinSize() {
        return new Size(_itemGeometry.getMinWidth(), _itemGeometry.getMinHeight());
    }

    public Size getMaxSize() {
        return new Size(_itemGeometry.getMaxWidth(), _itemGeometry.getMaxHeight());
    }

    // behavior
    private Behavior _itemBehavior = new Behavior();

    /**
     * BaseItem alignment
     */
    public void setAlignment(ItemAlignment... alignment) {
        setAlignment(Arrays.asList(alignment));
    }

    public void setAlignment(List<ItemAlignment> alignment) {
        _itemBehavior.setAlignment(alignment);
        updateGeometry();
        BaseItemStatics.updateAllLayout(this);
    }

    public List<ItemAlignment> getAlignment() {
        return _itemBehavior.getAlignment();
    }

    /**
     * BaseItem size (width and height) policy - FIXED or EXPAND
     */
    public void setSizePolicy(SizePolicy width, SizePolicy height) {
        setWidthPolicy(width);
        setHeightPolicy(height);
    }

    public void setWidthPolicy(SizePolicy policy) {
        if (_itemBehavior.getWidthPolicy() != policy) {
            _itemBehavior.setWidthPolicy(policy);
            ItemsRefreshManager.setRefreshShape(this);

            if (this instanceof VisualItem) {
                VisualItem vItem = (VisualItem) this;
                Prototype protoItem = vItem.prototype;

                if (protoItem instanceof InterfaceFloating) {
                    if (policy == SizePolicy.EXPAND)
                        ItemsLayoutBox.subscribeWindowSizeMonitoring(protoItem, GeometryEventType.RESIZE_WIDTH);
                    else
                        ItemsLayoutBox.unsubscribeWindowSizeMonitoring(protoItem, GeometryEventType.RESIZE_WIDTH);
                    updateGeometry();
                }
            }
            BaseItemStatics.updateAllLayout(this);
        }
    }

    public SizePolicy getWidthPolicy() {
        return _itemBehavior.getWidthPolicy();
    }

    public void setHeightPolicy(SizePolicy policy) {
        if (_itemBehavior.getHeightPolicy() != policy) {
            _itemBehavior.setHeightPolicy(policy);
            ItemsRefreshManager.setRefreshShape(this);

            if (this instanceof VisualItem) {
                VisualItem vItem = (VisualItem) this;
                Prototype protoItem = vItem.prototype;

                if (protoItem instanceof InterfaceFloating) {
                    if (policy == SizePolicy.EXPAND)
                        ItemsLayoutBox.subscribeWindowSizeMonitoring(protoItem, GeometryEventType.RESIZE_HEIGHT);
                    else
                        ItemsLayoutBox.unsubscribeWindowSizeMonitoring(protoItem, GeometryEventType.RESIZE_HEIGHT);
                    updateGeometry();
                }
            }
            BaseItemStatics.updateAllLayout(this);
        }
    }

    public SizePolicy getHeightPolicy() {
        return _itemBehavior.getHeightPolicy();
    }

    // position
    private Position _itemPosition = new Position();

    /**
     * BaseItem (x, y) position
     */
    public void setX(int x) {
        _itemPosition.setX(x);
    }

    public int getX() {
        return _itemPosition.getX();
    }

    public void setY(int y) {
        _itemPosition.setY(y);
    }

    public int getY() {
        return _itemPosition.getY();
    }

    // protected boolean IsOutConfines() {
    // if (getX() >= _confines_x_1 || getX() + getWidth() <= _confines_x_0 || getY()
    // >= _confines_y_1
    // || getY() + getHeight() <= _confines_y_0)
    // return true;
    // return false;
    // }

    /**
     * Update BaseItem's state
     */
    public void update(GeometryEventType type, int value) {
        BaseItemStatics.updateGeometryAttr(this, type, value);
    }

    void updateGeometry() {
        BaseItemStatics.updateGeometry(this);
    }

    /**
     * Style of the BaseItem
     */
    public void setStyle(Style style) {
    }

    public abstract Style getCoreStyle();

    /**
     * Check and set BaseItem default style
     */
    public void CheckDefaults() {
        // checking all attributes
        // setStyle(default theme)
        // foreach inners setStyle(from item default style)

        setDefaults();
    }

    public void setDefaults() {
    }

    public ItemHoverRule HoverRule = ItemHoverRule.LAZY;

    // shadow
    private boolean _is_shadow_drop = false;
    private int _shadow_radius = 1;
    private Color _shadow_color = new Color(0, 0, 0);
    private Position _shadow_pos = new Position();

    /**
     * BaseItem's shadow parameters. Is item has shadow
     */
    public boolean isShadowDrop() {
        return _is_shadow_drop;
    }

    public void setShadowDrop(boolean value) {
        _is_shadow_drop = value;
    }

    /**
     * BaseItem's shadow parameters. Shadow corners radius
     */
    public void setShadowRadius(int radius) {
        _shadow_radius = radius;
    }

    public int getShadowRadius() {
        return _shadow_radius;
    }

    /**
     * BaseItem's shadow parameters. Shadow color
     */
    public Color getShadowColor() {
        return _shadow_color;
    }

    public void setShadowColor(Color color) {
        _shadow_color = color;
    }

    /**
     * BaseItem's shadow parameters. Shadow position
     */
    public Position getShadowPos() {
        return _shadow_pos;
    }

    private int _xExtension = 0;
    private int _yExtension = 0;

    public int[] getShadowExtension() {
        return new int[] { _xExtension, _yExtension };
    }

    public void setShadowExtension(int wExtension, int hExtension) {
        _xExtension = wExtension;
        _yExtension = hExtension;
    }

    /**
     * Set BaseItem's shadow with parameters
     *
     * @param radius Shadow corners radius (same for all corners)
     * @param x      Shadow X position
     * @param y      Shadow Y position
     * @param color  Shadow color
     */
    public void setShadow(int radius, int x, int y, Color color) {
        _is_shadow_drop = true;
        _shadow_radius = radius;
        _shadow_color = color;
        _shadow_pos.setX(x);
        _shadow_pos.setY(y);
    }

    public void setShadow(Shadow shadow) {
        _is_shadow_drop = shadow.isDrop();
        _shadow_radius = shadow.getRadius();
        _shadow_color = shadow.getColor();
        _shadow_pos.setX(shadow.getXOffset());
        _shadow_pos.setY(shadow.getYOffset());
    }

    // update

    /**
     * BaseItem confines
     */
    public void setConfines() {
        Prototype parent = getParent();
        if (parent == null)
            return;
        _confines_x_0 = parent.getX() + parent.getPadding().left;
        _confines_x_1 = parent.getX() + parent.getWidth() - parent.getPadding().right;
        _confines_y_0 = parent.getY() + parent.getPadding().top;
        _confines_y_1 = parent.getY() + parent.getHeight() - parent.getPadding().bottom;
    }

    public void setConfines(int x0, int x1, int y0, int y1) {
        _confines_x_0 = x0;
        _confines_x_1 = x1;
        _confines_y_0 = y0;
        _confines_y_1 = y1;
    }

    protected int[] getConfines() {
        return new int[] { _confines_x_0, _confines_x_1, _confines_y_0, _confines_y_1 };
    }

    public void release() {
    }
}