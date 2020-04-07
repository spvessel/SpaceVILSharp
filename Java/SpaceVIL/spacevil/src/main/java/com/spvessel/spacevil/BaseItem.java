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

/**
 * Abstract class implementation of com.spvessel.spacevil.Core.InterfaceBaseItem
 * interface.
 * <p>
 * com.spvessel.spacevil.Core.InterfaceBaseItem is the main interface of SpaceVIL
 * environment.
 * <p>
 * Contains all the necessary methods for rendering objects and interacting with
 * them.
 */
public abstract class BaseItem implements InterfaceBaseItem {

    int _confinesX0 = 0;
    int _confinesX1 = 0;
    int _confinesY0 = 0;
    int _confinesY1 = 0;

    private CoreWindow _coreWindow;

    /**
     * Setting the window to which the item will belong.
     * 
     * @param handler Window as com.spvessel.spacevil.CoreWindow.
     */
    public void setHandler(CoreWindow handler) {
        _coreWindow = handler;
    }

    /**
     * Getting the window to which the item belong.
     * 
     * @return Window as com.spvessel.spacevil.CoreWindow.
     */
    public CoreWindow getHandler() {
        return _coreWindow;
    }

    private Prototype _parent = null;

    /**
     * Getting the parent of the item.
     * 
     * @return Parent as com.spvessel.spacevil.Prototype (Prototype is container and
     *         can contains children).
     */
    public Prototype getParent() {
        return _parent;
    }

    /**
     * Setting the parent of the item.
     * 
     * @param parent Parent as com.spvessel.spacevil.Prototype (Prototype is
     *               container and can contains children).
     */
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

    void removeItemFromListeners() {
        Prototype parent = getParent();
        parent.removeEventListener(GeometryEventType.RESIZE_WIDTH, this);
        parent.removeEventListener(GeometryEventType.RESIZE_HEIGHT, this);
        parent.removeEventListener(GeometryEventType.MOVED_X, this);
        parent.removeEventListener(GeometryEventType.MOVED_Y, this);
    }

    /**
     * Initializing children if this BaseItem is container
     * (com.spvessel.spacevil.Prototype).
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    public void initElements() {
        // do nothing
    }

    private Item _item = new Item();

    private Indents _margin = new Indents();

    /**
     * Getting the indents of an item to offset itself relative to its container.
     * 
     * @return Margin as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getMargin() {
        return _margin;
    }

    /**
     * Setting the indents of an item to offset itself relative to its container.
     * 
     * @param margin Margin as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setMargin(Indents margin) {
        _margin = margin;
        updateGeometry();
        BaseItemStatics.updateAllLayout(this);
    }

    /**
     * Setting the indents of an item to offset itself relative to its container.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setMargin(int left, int top, int right, int bottom) {
        setMargin(new Indents(left, top, right, bottom));
    }

    /**
     * Getting triangles of item's shape.
     * 
     * @return Points list of the shape as List of float[2] array (2D).
     */
    public List<float[]> getTriangles() {
        return _item.getTriangles();
    }

    /**
     * Setting triangles as item's shape.
     * 
     * @param triangles Points list of the shape as List of float[2] array (2D).
     */
    public void setTriangles(List<float[]> triangles) {
        _item.setTriangles(triangles);
    }

    /**
     * Making default item's shape. Use in conjunction with getTriangles() and
     * setTriangles() methods.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    public void makeShape() {

    }

    List<float[]> updateShape() {
        return BaseItemStatics.updateShape(this);
    }

    /**
     * Setting background color of an item's shape.
     * 
     * @param color Background color as java.awt.Color.
     */
    public void setBackground(Color color) {
        _item.setBackground(color);
    }

    /**
     * Setting background color of an item's shape in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setBackground(int r, int g, int b) {
        _item.setBackground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting background color of an item in byte RGBA format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setBackground(int r, int g, int b, int a) {
        _item.setBackground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Setting background color of an item in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setBackground(float r, float g, float b) {
        _item.setBackground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting background color of an item in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setBackground(float r, float g, float b, float a) {
        _item.setBackground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Getting background color of an item.
     * 
     * @return Background color as java.awt.Color.
     */
    public Color getBackground() {
        return _item.getBackground();
    }

    /**
     * Setting the name of the item.
     * 
     * @param name Item name as java.lang.String.
     */
    public void setItemName(String name) {
        _item.setItemName(name);
    }

    /**
     * Getting the name of the item.
     * 
     * @return Item name as java.lang.String.
     */
    public String getItemName() {
        return _item.getItemName();
    }

    private boolean _drawable = true;

    /**
     * Getting the drawable (visibility) status of an item. This property used in
     * conjunction with the isVisible() property.
     * <p>
     * Explanation: an item can be visible and invisible, in some cases the item can
     * be located outside the container (example: com.spvessel.spacevil.ListBox),
     * and it must be invisible so as not to waste CPU / GPU resources, but in some
     * cases you must control the visibility of elements that are inside container
     * and should be invisible (example: com.spvessel.spacevil.TreeView).
     * 
     * @return True: if item is drawable (visible). False: if item is not drawable
     *         (invisible).
     */
    public boolean isDrawable() {
        return _drawable;
    }

    /**
     * Setting the drawable (visibility) status of an item. This property used in
     * conjunction with the isVisible() property.
     * <p>
     * Explanation: an item can be visible and invisible, in some cases the item can
     * be located outside the container (example: com.spvessel.spacevil.ListBox),
     * and it must be invisible so as not to waste CPU / GPU resources, but in some
     * cases you must control the visibility of elements that are inside container
     * and should be invisible (example: com.spvessel.spacevil.TreeView).
     * 
     * @param value True: if item should be drawable (visible). False: if item
     *              should not be drawable (invisible).
     */
    public void setDrawable(boolean value) {
        if (_drawable == value)
            return;
        _drawable = value;
    }

    private boolean _visible = true;

    /**
     * Getting the visibility status of an item. This property may used in
     * conjunction with the isDrawable() property.
     * 
     * @return True: if item is visible. False: if item is invisible.
     */
    public boolean isVisible() {
        return _visible;
    }

    /**
     * Setting the visibility status of an item. This property may used in
     * conjunction with the isDrawable() property.
     * 
     * @param value True: if item should be visible. False: if item should be
     *              invisible.
     */
    public void setVisible(boolean value) {
        if (_visible == value)
            return;
        _visible = value;
    }

    // geometry
    private Geometry _itemGeometry = new Geometry();

    /**
     * Setting the minimum width limit. Actual width cannot be less than this limit.
     * 
     * @param width Minimum width limit of the item.
     */
    public void setMinWidth(int width) {
        _itemGeometry.setMinWidth(width);
    }

    /**
     * Setting item width. If the value is greater/less than the maximum/minimum
     * value of the width, then the width becomes equal to the maximum/minimum
     * value.
     * 
     * @param width Width of the item.
     */
    public void setWidth(int width) {
        _itemGeometry.setWidth(width);
        ItemsRefreshManager.setRefreshShape(this);
    }

    /**
     * Setting the maximum width limit. Actual width cannot be greater than this
     * limit.
     * 
     * @param width Maximum width limit of the item.
     */
    public void setMaxWidth(int width) {
        _itemGeometry.setMaxWidth(width);
    }

    /**
     * Getting the minimum width limit.
     * 
     * @return Minimum width limit of the item.
     */
    public int getMinWidth() {
        return _itemGeometry.getMinWidth();
    }

    /**
     * Getting item width.
     * 
     * @return Width of the item.
     */
    public int getWidth() {
        return _itemGeometry.getWidth();
    }

    /**
     * Getting the maximum width limit.
     * 
     * @return Maximum width limit of the item.
     */
    public int getMaxWidth() {
        return _itemGeometry.getMaxWidth();
    }

    /**
     * Setting the minimum height limit. Actual height cannot be less than this
     * limit.
     * 
     * @param height Minimum height limit of the item.
     */
    public void setMinHeight(int height) {
        _itemGeometry.setMinHeight(height);
    }

    /**
     * Setting item height. If the value is greater/less than the maximum/minimum
     * value of the height, then the height becomes equal to the maximum/minimum
     * value.
     * 
     * @param height Height of the item.
     */
    public void setHeight(int height) {
        _itemGeometry.setHeight(height);
        ItemsRefreshManager.setRefreshShape(this);
    }

    /**
     * Setting the maximum height limit. Actual height cannot be greater than this
     * limit.
     *
     * @param height Maximum height limit of the item.
     */
    public void setMaxHeight(int height) {
        _itemGeometry.setMaxHeight(height);
    }

    /**
     * Getting the minimum height limit.
     * 
     * @return Minimum height limit of the item.
     */
    public int getMinHeight() {
        return _itemGeometry.getMinHeight();
    }

    /**
     * Getting item height.
     * 
     * @return Height of the item.
     */
    public int getHeight() {
        return _itemGeometry.getHeight();
    }

    /**
     * Getting the maximum height limit.
     * 
     * @return Maximum height limit of the item.
     */
    public int getMaxHeight() {
        return _itemGeometry.getMaxHeight();
    }

    /**
     * Setting item size (width and height).
     * 
     * @param width  Width of the item.
     * @param height Height of the item.
     */
    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    /**
     * Setting minimum item size limit (width and height limits).
     * 
     * @param width  Minimum width limit of the item.
     * @param height Minimum height limit of the item.
     */
    public void setMinSize(int width, int height) {
        setMinWidth(width);
        setMinHeight(height);
    }

    /**
     * Setting maximum item size limit (width and height limits).
     * 
     * @param width  Maximum width limit of the item.
     * @param height Maximum height limit of the item.
     */
    public void setMaxSize(int width, int height) {
        setMaxWidth(width);
        setMaxHeight(height);
    }

    /**
     * Getting current item size.
     * 
     * @return Item size as com.spvessel.spacevil.Core.Size.
     */
    public Size getSize() {
        return _itemGeometry.getSize();
    }

    /**
     * Getting current item minimum size limit.
     * 
     * @return Minimum item size limit as com.spvessel.spacevil.Core.Size.
     */
    public Size getMinSize() {
        return new Size(_itemGeometry.getMinWidth(), _itemGeometry.getMinHeight());
    }

    /**
     * Getting current item maximum size limit.
     * 
     * @return Minimum item size limit as com.spvessel.spacevil.Core.Size.
     */
    public Size getMaxSize() {
        return new Size(_itemGeometry.getMaxWidth(), _itemGeometry.getMaxHeight());
    }

    // behavior
    private Behavior _itemBehavior = new Behavior();

    /**
     * Setting an alignment of an item's shape relative to its container. Combines
     * with alignment by vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT,
     * HCENTER, RIGHT).
     * 
     * @param alignment Alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setAlignment(ItemAlignment... alignment) {
        setAlignment(Arrays.asList(alignment));
    }

    /**
     * Setting an alignment of an item's shape relative to its container. Combines
     * with alignment by vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT,
     * HCENTER, RIGHT).
     * 
     * @param alignment Alignment as List of
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setAlignment(List<ItemAlignment> alignment) {
        _itemBehavior.setAlignment(alignment);
        updateGeometry();
        BaseItemStatics.updateAllLayout(this);
    }

    /**
     * Getting an alignment of an item's shape relative to its container.
     * 
     * @return Alignment as List of com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public List<ItemAlignment> getAlignment() {
        return _itemBehavior.getAlignment();
    }

    /**
     * Setting the size policy of an item's shape. Can be FIXED (shape not changes
     * its size) or EXPAND (shape is stretched to all available space).
     * 
     * @param width  Width policy of an item's shape as
     *               com.spvessel.spacevil.Flags.SizePolicy.
     * @param height Height policy of an item's shape as
     *               com.spvessel.spacevil.Flags.SizePolicy.
     */
    public void setSizePolicy(SizePolicy width, SizePolicy height) {
        setWidthPolicy(width);
        setHeightPolicy(height);
    }

    /**
     * Setting width policy of an item's shape. Can be FIXED (shape not changes its
     * size) or EXPAND (shape is stretched to all available space).
     * 
     * @param policy Width policy as com.spvessel.spacevil.Flags.SizePolicy.
     */
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

    /**
     * Getting width policy of an item's shape.Can be FIXED (shape not changes its
     * size) or EXPAND (shape is stretched to all available space).
     * 
     * @return Width policy as com.spvessel.spacevil.Flags.SizePolicy.
     */
    public SizePolicy getWidthPolicy() {
        return _itemBehavior.getWidthPolicy();
    }

    /**
     * Setting height policy of an item's shape. Can be FIXED (shape not changes its
     * size) or EXPAND (shape is stretched to all available space).
     * 
     * @param policy Height policy as com.spvessel.spacevil.Flags.SizePolicy.
     */
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

    /**
     * Getting height policy of an item's shape.Can be FIXED (shape not changes its
     * size) or EXPAND (shape is stretched to all available space).
     * 
     * @return Height policy as com.spvessel.spacevil.Flags.SizePolicy.
     */
    public SizePolicy getHeightPolicy() {
        return _itemBehavior.getHeightPolicy();
    }

    // position
    private Position _itemPosition = new Position();

    /**
     * Setting X coordinate of the left-top corner of a shape.
     * 
     * @param x X position of the left-top corner.
     */
    public void setX(int x) {
        _itemPosition.setX(x);
    }

    /**
     * Getting X coordinate of the left-top corner of a shape.
     * 
     * @return X position of the left-top corner.
     */
    public int getX() {
        return _itemPosition.getX();
    }

    /**
     * Setting Y coordinate of the left-top corner of a shape.
     * 
     * @param y Y position of the left-top corner.
     */
    public void setY(int y) {
        _itemPosition.setY(y);
    }

    /**
     * Getting Y coordinate of the left-top corner of a shape.
     * 
     * @return Y position of the left-top corner.
     */
    public int getY() {
        return _itemPosition.getY();
    }

    /**
     * Updating an item size or/and position.
     * 
     * @param type  Type of event as com.spvessel.spacevil.Flags.GeometryEventType.
     * @param value Value of a property that was changed.
     */
    public void update(GeometryEventType type, int value) {
        BaseItemStatics.updateGeometryAttr(this, type, value);
    }

    void updateGeometry() {
        BaseItemStatics.updateGeometry(this);
    }

    /**
     * Setting a style that describes the appearance of an item.
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    public void setStyle(Style style) {
    }

    /**
     * Getting the core (only appearance properties without inner styles) style of
     * an item.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public abstract Style getCoreStyle();

    /**
     * Hovering rule propetry of this item.
     * <p>
     * Can be ItemHoverRule.LAZY or ItemHoverRule.STRICT (see
     * com.spvessel.spacevil.Flags.ItemHoverRule).
     */
    public ItemHoverRule HoverRule = ItemHoverRule.LAZY;

    // shadow
    private boolean _isShadowDrop = false;
    private int _shadowRadius = 1;
    private Color _shadowColor = new Color(0, 0, 0);
    private Position _shadowPos = new Position();

    /**
     * Getting the shadow visibility status of an item.
     * 
     * @return True: if shadow is visible. False: if shadow is invisible.
     */
    public boolean isShadowDrop() {
        return _isShadowDrop;
    }

    /**
     * Setting the shadow visibility status of an item.
     * 
     * @param value True: if shadow should be visible. False: if shadow should be
     *              invisible.
     */
    public void setShadowDrop(boolean value) {
        _isShadowDrop = value;
    }

    /**
     * Setting the specified blur radius of the shadow.
     * <p>
     * Default: 0.
     * 
     * @param radius The blur radius of the shadow.
     */
    public void setShadowRadius(int radius) {
        _shadowRadius = radius;
    }

    /**
     * Getting the shadow blur raduis.
     * 
     * @return The blur radius of the shadow.
     */
    public int getShadowRadius() {
        return _shadowRadius;
    }

    /**
     * Getting shadow color.
     * 
     * @return Returns the shadow color as java.awt.Color.
     */
    public Color getShadowColor() {
        return _shadowColor;
    }

    /**
     * Setting shadow color.
     * 
     * @param color Shadow color as java.awt.Color.
     */
    public void setShadowColor(Color color) {
        _shadowColor = color;
    }

    /**
     * Getting the offset of the shadow relative to the position of the item.
     * 
     * @return Shadow offset as com.spvessel.spacevil.Core.Position.
     */
    public Position getShadowPos() {
        return _shadowPos;
    }

    private int _xExtension = 0;
    private int _yExtension = 0;

    /**
     * Getting the values of shadow extensions in pixels.
     * 
     * @return The values of shadow extensions. 0 - width extension, 1 - height
     *         extension.
     */
    public int[] getShadowExtension() {
        return new int[] { _xExtension, _yExtension };
    }

    /**
     * Setting the values of shadow extensions in pixels.
     * 
     * @param wExtension Extension by width.
     * @param hExtension Extension by height.
     */
    public void setShadowExtension(int wExtension, int hExtension) {
        _xExtension = wExtension;
        _yExtension = hExtension;
    }

    /**
     * Setting the shadow with specified blur radius, axis shifts, shadow color.
     * 
     * @param radius A blur radius of the shadow.
     * @param x      X shift of the shadow.
     * @param y      Y shift of the shadow.
     * @param color  A shadow color as java.awt.Color.
     */
    public void setShadow(int radius, int x, int y, Color color) {
        _isShadowDrop = true;
        _shadowRadius = radius;
        _shadowColor = color;
        _shadowPos.setX(x);
        _shadowPos.setY(y);
    }

    /**
     * Setting the shadow of an item.
     * 
     * @param shadow Shadow as com.spvessel.spacevil.Decorations.Shadow.
     */
    public void setShadow(Shadow shadow) {
        _isShadowDrop = shadow.isDrop();
        _shadowRadius = shadow.getRadius();
        _shadowColor = shadow.getColor();
        _shadowPos.setX(shadow.getXOffset());
        _shadowPos.setY(shadow.getYOffset());
    }

    // update

    /**
     * Setting the confines of the item relative to its parent's size and position.
     * <p>
     * Example: items can be partially (or completely) outside the container
     * (example: ListBox), in which case the part that is outside the container
     * should not be visible and should not interact with the user.
     */
    public void setConfines() {
        Prototype parent = getParent();
        if (parent == null)
            return;
        _confinesX0 = parent.getX() + parent.getPadding().left;
        _confinesX1 = parent.getX() + parent.getWidth() - parent.getPadding().right;
        _confinesY0 = parent.getY() + parent.getPadding().top;
        _confinesY1 = parent.getY() + parent.getHeight() - parent.getPadding().bottom;
    }

    /**
     * Setting the confines of the item relative to specified bounds.
     * <p>
     * Example: items can be partially (or completely) outside the container
     * (example: ListBox), in which case the part that is outside the container
     * should not be visible and should not interact with the user.
     * 
     * @param x0 Left X begin position.
     * @param x1 Right X end position.
     * @param y0 Top Y begin position.
     * @param y1 Bottom Y end position.
     */
    public void setConfines(int x0, int x1, int y0, int y1) {
        _confinesX0 = x0;
        _confinesX1 = x1;
        _confinesY0 = y0;
        _confinesY1 = y1;
    }

    protected int[] getConfines() {
        return new int[] { _confinesX0, _confinesX1, _confinesY0, _confinesY1 };
    }

    /**
     * Method to describe disposing item's resources if the item was removed.
     */
    public void release() {
    }
}