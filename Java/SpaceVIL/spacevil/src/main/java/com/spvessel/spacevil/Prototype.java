package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.EventCommonMethodState;
import com.spvessel.spacevil.Core.EventInputTextMethodState;
import com.spvessel.spacevil.Core.EventKeyMethodState;
import com.spvessel.spacevil.Core.EventMouseMethodState;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Core.Size;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Figure;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Spacing;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.EmbeddedCursor;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemHoverRule;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

/**
 * The Prototype is an abstract implementation of InterfaceBaseItem for complex
 * interactive items.
 * <p>
 * Contains all the necessary methods for rendering objects and interacting with
 * them.
 * <p>
 * Examples of subclasses: com.spvessel.spacevil.ButtonCore,
 * com.spvessel.spacevil.TextEdit, com.spvessel.spacevil.ListBox and etc.
 */
abstract public class Prototype implements InterfaceBaseItem {

    private VisualItem _core = new VisualItem();

    VisualItem getCore() {
        return _core;
    }

    void setCore(VisualItem core) {
        _core = core;
    }

    private static int count = 0;

    /**
     * Default constructor of Prototype class.
     */
    public Prototype() {
        _core.setItemName("VisualItem_" + count);
        count++;
        _core.prototype = this;
    }

    EventCommonMethodState eventFocusGet = new EventCommonMethodState();
    EventCommonMethodState eventFocusLost = new EventCommonMethodState();
    /**
     * Event that is invoked when an item is resizing.
     */
    public EventCommonMethodState eventResize = new EventCommonMethodState();
    /**
     * Event that is invoked when an item is destroyed (removed).
     */
    public EventCommonMethodState eventDestroy = new EventCommonMethodState();
    /**
     * Event that is invoked when mouse cursor enters inside an item area.
     */
    public EventMouseMethodState eventMouseHover = new EventMouseMethodState();
    /**
     * Event that is invoked when mouse cursor leaves inside an item area.
     */
    public EventMouseMethodState eventMouseLeave = new EventMouseMethodState();
    /**
     * Event that is invoked when mouse click (release) on an item.
     */
    public EventMouseMethodState eventMouseClick = new EventMouseMethodState();
    /**
     * Event that is invoked when mouse double click on an item.
     */
    public EventMouseMethodState eventMouseDoubleClick = new EventMouseMethodState();
    /**
     * Event that is invoked when mouse press on an item.
     */
    public EventMouseMethodState eventMousePress = new EventMouseMethodState();
    /**
     * Event that is invoked when mouse drag on an item.
     */
    public EventMouseMethodState eventMouseDrag = new EventMouseMethodState();
    /**
     * Event that is invoked when mouse drop on an item.
     */
    public EventMouseMethodState eventMouseDrop = new EventMouseMethodState();
    /**
     * Event that is invoked when mouse wheel scrolls up on an item.
     */
    public EventMouseMethodState eventScrollUp = new EventMouseMethodState();
    /**
     * Event that is invoked when mouse wheel scrolls down on an item.
     */
    public EventMouseMethodState eventScrollDown = new EventMouseMethodState();
    /**
     * Event that is invoked when key of keyboard is pressed.
     */
    public EventKeyMethodState eventKeyPress = new EventKeyMethodState();
    /**
     * Event that is invoked when key of keyboard is released.
     */
    public EventKeyMethodState eventKeyRelease = new EventKeyMethodState();
    /**
     * Event that is invoked when typing text on the keyboard.
     */
    public EventInputTextMethodState eventTextInput = new EventInputTextMethodState();

    void freeEvents() {
        eventFocusGet.clear();
        eventFocusLost.clear();
        eventResize.clear();
        eventDestroy.clear();

        eventMouseHover.clear();
        eventMouseLeave.clear();
        eventMouseClick.clear();
        eventMouseDoubleClick.clear();
        eventMousePress.clear();
        eventMouseDrag.clear();
        eventMouseDrop.clear();
        eventScrollUp.clear();
        eventScrollDown.clear();

        eventKeyPress.clear();
        eventKeyRelease.clear();

        eventTextInput.clear();
    }

    /**
     * Method to describe disposing item's resources if the item was removed.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    public void release() {
    }

    /**
     * Setting the window to which the item will belong.
     * 
     * @param handler Window as com.spvessel.spacevil.CoreWindow.
     */
    public void setHandler(CoreWindow handler) {
        _core.setHandler(handler);
    }

    /**
     * Getting the window to which the item belongs.
     * 
     * @return Window as com.spvessel.spacevil.CoreWindow.
     */
    public CoreWindow getHandler() {
        return _core.getHandler();
    }

    /**
     * Getting tooltip text of the item.
     * <p>
     * Tooltip is hint about an item that appears when you hold the mouse cursor
     * over an item long enough.
     * 
     * @return Tolltip text.
     */
    public String getToolTip() {
        return _core.getToolTip();
    }

    /**
     * Setting tooltip text of the item.
     * <p>
     * Tooltip is hint about an item that appears when you hold the mouse cursor
     * over an item long enough.
     * 
     * @param text Tooltip text.
     */
    public void setToolTip(String text) {
        _core.setToolTip(text);
    }

    /**
     * Getting the parent of the item.
     * 
     * @return Parent as com.spvessel.spacevil.Prototype (Prototype is container and
     *         can contains children).
     */
    public Prototype getParent() {
        return _core.getParent();
    }

    /**
     * Setting the parent of the item.
     * 
     * @param parent Parent as com.spvessel.spacevil.Prototype (Prototype is
     *               container and can contains children).
     */
    public void setParent(Prototype parent) {
        _core.setParent(parent);
    }

    /**
     * Getting indents between children of a container type item.
     * 
     * @return Indents between children as
     *         com.spvessel.spacevil.Decorations.Spacing.
     */
    public Spacing getSpacing() {
        return _core.getSpacing();
    }

    /**
     * Setting indents between children of a container type item.
     * 
     * @param spacing Spacing as com.spvessel.spacevil.Decorations.Spacing.
     */
    public void setSpacing(Spacing spacing) {
        _core.setSpacing(spacing);
    }

    /**
     * Setting indents between children of a container type item.
     * 
     * @param horizontal Horizontal indent. Default: 0.
     * @param vertical   Vertical indent. Default: 0.
     */
    public void setSpacing(int horizontal, int vertical) {
        _core.setSpacing(horizontal, vertical);
    }

    /**
     * Getting indents of an item for offset its children.
     * 
     * @return Padding indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getPadding() {
        return _core.getPadding();
    }

    /**
     * Setting indents of an item to offset its children.
     * 
     * @param padding Padding indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setPadding(Indents padding) {
        _core.setPadding(padding);
    }

    /**
     * Setting indents of an item to offset its children.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setPadding(int left, int top, int right, int bottom) {
        _core.setPadding(left, top, right, bottom);
    }

    /**
     * Getting the indents of an item to offset itself relative to its container.
     * 
     * @return Margin as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getMargin() {
        return _core.getMargin();
    }

    /**
     * Setting the indents of an item to offset itself relative to its container.
     * 
     * @param margin Margin as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setMargin(Indents margin) {
        _core.setMargin(margin);
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
        _core.setMargin(left, top, right, bottom);
    }

    /**
     * Setting border of an item's shape. Border consist of corner radiuses,
     * thickness and color.
     * 
     * @param border Border as com.spvessel.spacevil.Decorations.Border.
     */
    public void setBorder(Border border) {
        _core.setBorder(border);
    }

    /**
     * Setting the border color of an item's shape.
     * 
     * @param fill Border color as java.awt.Color.
     */
    public void setBorderFill(Color fill) {
        _core.setBorderFill(fill);
    }

    /**
     * Getting the border color oa an item's shape.
     * 
     * @return Border color as java.awt.Color.
     */
    public Color getBorderFill() {
        return _core.getBorderFill();
    }

    /**
     * Setting the border color of an item's shape in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setBorderFill(int r, int g, int b) {
        _core.setBorderFill(r, g, b);
    }

    /**
     * Setting the border color of an item's shape in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setBorderFill(int r, int g, int b, int a) {
        _core.setBorderFill(r, g, b, a);
    }

    /**
     * Setting the border color of an item's shape in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setBorderFill(float r, float g, float b) {
        _core.setBorderFill(r, g, b);
    }

    /**
     * Setting the border color of an item's shape in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setBorderFill(float r, float g, float b, float a) {
        _core.setBorderFill(r, g, b, a);
    }

    /**
     * Setting radius of the border's corners.
     * 
     * @param radius Radiuses of the border's corners as
     *               com.spvessel.spacevil.Decorations.CornerRadius.
     */
    public void setBorderRadius(CornerRadius radius) {
        _core.setBorderRadius(radius);
    }

    /**
     * Setting border radius with the same values for each corner of the rectangle
     * object.
     * 
     * @param radius Radius of the border's corners.
     */
    public void setBorderRadius(int radius) {
        _core.setBorderRadius(new CornerRadius(radius));
    }

    /**
     * Getting border radiuses.
     * 
     * @return Border radiuses as com.spvessel.spacevil.Decorations.CornerRadius.
     */
    public CornerRadius getBorderRadius() {
        return _core.getBorderRadius();
    }

    /**
     * Setting border thickness of an item's shape.
     * 
     * @param thickness Border thickness.
     */
    public void setBorderThickness(int thickness) {
        _core.setBorderThickness(thickness);
    }

    /**
     * Getting border thickness of an item's shape.
     * 
     * @return Border thickness.
     */
    public int getBorderThickness() {
        return _core.getBorderThickness();
    }

    /**
     * Initializing children and their attributes.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    public void initElements() {
        _core.initElements();
    }

    /**
     * Getting triangles of item's shape.
     * 
     * @return Points list of the shape as List of float[2] array (2D).
     */
    public List<float[]> getTriangles() {
        return _core.getTriangles();
    }

    /**
     * Setting triangles as item's shape.
     * 
     * @param triangles Points list of the shape as List of float[2] array (2D).
     */
    public void setTriangles(List<float[]> triangles) {
        _core.setTriangles(triangles);
    }

    /**
     * Making default item's shape. Use in conjunction with getTriangles() and
     * setTriangles() methods.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    public void makeShape() {
        _core.makeShape();
    }

    /**
     * Setting background color of an item's shape.
     * 
     * @param color Background color as java.awt.Color.
     */
    public void setBackground(Color color) {
        _core.setBackground(color);
    }

    /**
     * Setting background color of an item's shape in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setBackground(int r, int g, int b) {
        _core.setBackground(r, g, b);
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
        _core.setBackground(r, g, b, a);
    }

    /**
     * Setting background color of an item in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setBackground(float r, float g, float b) {
        _core.setBackground(r, g, b);
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
        _core.setBackground(r, g, b, a);
    }

    /**
     * Getting background color of an item.
     * 
     * @return Background color as java.awt.Color.
     */
    public Color getBackground() {
        return _core.getBackground();
    }

    /**
     * Setting the name of the item.
     * 
     * @param name Item name as java.lang.String.
     */
    public void setItemName(String name) {
        _core.setItemName(name);
    }

    /**
     * Getting the name of the item.
     * 
     * @return Item name as java.lang.String.
     */
    public String getItemName() {
        return _core.getItemName();
    }

    /**
     * Setting the minimum width limit. Actual width cannot be less than this limit.
     * 
     * @param width Minimum width limit of the item.
     */
    public void setMinWidth(int width) {
        _core.setMinWidth(width);
    }

    /**
     * Getting the minimum width limit.
     * 
     * @return Minimum width limit of the item.
     */
    public int getMinWidth() {
        return _core.getMinWidth();
    }

    /**
     * Setting item width. If the value is greater/less than the maximum/minimum
     * value of the width, then the width becomes equal to the maximum/minimum
     * value.
     * 
     * @param width Width of the item.
     */
    public void setWidth(int width) {
        _core.setWidth(width);
    }

    /**
     * Getting item width.
     * 
     * @return Width of the item.
     */
    public int getWidth() {
        return _core.getWidth();
    }

    /**
     * Setting the maximum width limit. Actual width cannot be greater than this
     * limit.
     * 
     * @param width Maximum width limit of the item.
     */
    public void setMaxWidth(int width) {
        _core.setMaxWidth(width);
    }

    /**
     * Getting the maximum width limit.
     * 
     * @return Maximum width limit of the item.
     */
    public int getMaxWidth() {
        return _core.getMaxWidth();
    }

    /**
     * Setting the minimum height limit. Actual height cannot be less than this
     * limit.
     * 
     * @param height Minimum height limit of the item.
     */
    public void setMinHeight(int height) {
        _core.setMinHeight(height);
    }

    /**
     * Getting the minimum height limit.
     * 
     * @return Minimum height limit of the item.
     */
    public int getMinHeight() {
        return _core.getMinHeight();
    }

    /**
     * Setting item height. If the value is greater/less than the maximum/minimum
     * value of the height, then the height becomes equal to the maximum/minimum
     * value.
     * 
     * @param height Height of the item.
     */
    public void setHeight(int height) {
        _core.setHeight(height);
    }

    /**
     * Getting item height.
     * 
     * @return Height of the item.
     */
    public int getHeight() {
        return _core.getHeight();
    }

    /**
     * Setting the maximum height limit. Actual height cannot be greater than this
     * limit.
     *
     * @param height Maximum height limit of the item.
     */
    public void setMaxHeight(int height) {
        _core.setMaxHeight(height);
    }

    /**
     * Getting the maximum height limit.
     * 
     * @return Maximum height limit of the item.
     */
    public int getMaxHeight() {
        return _core.getMaxHeight();
    }

    /**
     * Setting item size (width and height).
     * 
     * @param width  Width of the item.
     * @param height Height of the item.
     */
    public void setSize(int width, int height) {
        _core.setSize(width, height);
    }

    /**
     * Getting current item size.
     * 
     * @return Item size as com.spvessel.spacevil.Core.Size.
     */
    public Size getSize() {
        return _core.getSize();
    }

    /**
     * Setting minimum item size limit (width and height limits).
     * 
     * @param width  Minimum width limit of the item.
     * @param height Minimum height limit of the item.
     */
    public void setMinSize(int width, int height) {
        _core.setMinSize(width, height);
    }

    /**
     * Getting current item minimum size limit.
     * 
     * @return Minimum item size limit as com.spvessel.spacevil.Core.Size.
     */
    public Size getMinSize() {
        return _core.getMinSize();
    }

    /**
     * Setting maximum item size limit (width and height limits).
     * 
     * @param width  Maximum width limit of the item.
     * @param height Maximum height limit of the item.
     */
    public void setMaxSize(int width, int height) {
        _core.setMaxSize(width, height);
    }

    /**
     * Getting current item maximum size limit.
     * 
     * @return Minimum item size limit as com.spvessel.spacevil.Core.Size.
     */
    public Size getMaxSize() {
        return _core.getMaxSize();
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
        _core.setAlignment(alignment);
    }

    /**
     * Setting an alignment of an item's shape relative to its container. Combines
     * with alignment by vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT,
     * HCENTER, RIGHT).
     * 
     * @param alignment Alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setAlignment(ItemAlignment... alignment) {
        _core.setAlignment(alignment);
    }

    /**
     * Getting an alignment of an item's shape relative to its container.
     * 
     * @return Alignment as List of com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public List<ItemAlignment> getAlignment() {
        return _core.getAlignment();
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
        _core.setSizePolicy(width, height);
    }

    /**
     * Setting width policy of an item's shape. Can be FIXED (shape not changes its
     * size) or EXPAND (shape is stretched to all available space).
     * 
     * @param policy Width policy as com.spvessel.spacevil.Flags.SizePolicy.
     */
    public void setWidthPolicy(SizePolicy policy) {
        _core.setWidthPolicy(policy);
    }

    /**
     * Getting width policy of an item's shape.Can be FIXED (shape not changes its
     * size) or EXPAND (shape is stretched to all available space).
     * 
     * @return Width policy as com.spvessel.spacevil.Flags.SizePolicy.
     */
    public SizePolicy getWidthPolicy() {
        return _core.getWidthPolicy();
    }

    /**
     * Setting height policy of an item's shape. Can be FIXED (shape not changes its
     * size) or EXPAND (shape is stretched to all available space).
     * 
     * @param policy Height policy as com.spvessel.spacevil.Flags.SizePolicy.
     */
    public void setHeightPolicy(SizePolicy policy) {
        _core.setHeightPolicy(policy);
    }

    /**
     * Getting height policy of an item's shape.Can be FIXED (shape not changes its
     * size) or EXPAND (shape is stretched to all available space).
     * 
     * @return Height policy as com.spvessel.spacevil.Flags.SizePolicy.
     */
    public SizePolicy getHeightPolicy() {
        return _core.getHeightPolicy();
    }

    /**
     * Setting item position.
     * 
     * @param x X position of the left-top corner
     * @param y Y position of the left-top corner
     */
    public void setPosition(int x, int y) {
        _core.setX(x);
        _core.setY(y);
    }

    /**
     * Setting X coordinate of the left-top corner of a shape.
     * 
     * @param x X position of the left-top corner.
     */
    public void setX(int x) {
        _core.setX(x);
    }

    /**
     * Getting X coordinate of the left-top corner of a shape.
     * 
     * @return X position of the left-top corner.
     */
    public int getX() {
        return _core.getX();
    }

    /**
     * Setting Y coordinate of the left-top corner of a shape.
     * 
     * @param y Y position of the left-top corner.
     */
    public void setY(int y) {
        _core.setY(y);
    }

    /**
     * Getting Y coordinate of the left-top corner of a shape.
     * 
     * @return Y position of the left-top corner.
     */
    public int getY() {
        return _core.getY();
    }

    /**
     * Setting the confines of the item relative to its parent's size and position.
     * <p>
     * Example: items can be partially (or completely) outside the container
     * (example: ListBox), in which case the part that is outside the container
     * should not be visible and should not interact with the user.
     */
    public void setConfines() {
        _core.setConfines();
    }

    /**
     * Setting the confines of the item relative to its parent's size and position.
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
        _core.setConfines(x0, x1, y0, y1);
    }

    /**
     * Setting a style that describes the appearance of an item.
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    public void setStyle(Style style) {
        _core.setStyle(style);
    }

    /**
     * Getting the core (only appearance properties without inner styles) style of
     * an item.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public Style getCoreStyle() {
        return _core.getCoreStyle();
    }

    /**
     * Getting the shadow visibility status of an item.
     * 
     * @return True: if shadow is visible. False: if shadow is invisible.
     */
    public boolean isShadowDrop() {
        return _core.isShadowDrop();
    }

    /**
     * Setting the shadow visibility status of an item.
     * 
     * @param value True: if shadow should be visible. False: if shadow should be
     *              invisible.
     */
    public void setShadowDrop(boolean value) {
        _core.setShadowDrop(value);
    }

    /**
     * Setting the specified blur radius of the shadow.
     * <p>
     * Default: 0.
     * 
     * @param radius The blur radius of the shadow.
     */
    public void setShadowRadius(int radius) {
        _core.setShadowRadius(radius);
    }

    /**
     * Getting the shadow blur raduis.
     * 
     * @return The blur radius of the shadow.
     */
    public int getShadowRadius() {
        return _core.getShadowRadius();
    }

    /**
     * Getting shadow color.
     * 
     * @return Returns the shadow color as java.awt.Color.
     */
    public Color getShadowColor() {
        return _core.getShadowColor();
    }

    /**
     * Setting shadow color.
     * 
     * @param color Shadow color as java.awt.Color.
     */
    public void setShadowColor(Color color) {
        _core.setShadowColor(color);
    }

    /**
     * Getting the offset of the shadow relative to the position of the item.
     * 
     * @return Shadow offset as com.spvessel.spacevil.Core.Position.
     */
    public Position getShadowPos() {
        return _core.getShadowPos();
    }

    /**
     * Getting the values of shadow extensions in pixels.
     * 
     * @return The values of shadow extensions. 0 - width extension, 1 - height
     *         extension.
     */
    public int[] getShadowExtension() {
        return _core.getShadowExtension();
    }

    /**
     * Setting the values of shadow extensions in pixels.
     * 
     * @param wExtension Extension by width.
     * @param hExtension Extension by height.
     */
    public void setShadowExtension(int wExtension, int hExtension) {
        _core.setShadowExtension(wExtension, hExtension);
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
        _core.setShadow(radius, x, y, color);
    }

    /**
     * Adding visual state for an item.
     * <p>
     * Type can be BASE, HOVERED, PRESSED, TOGGLED, FOCUSED, DISABLED.
     * 
     * @param type  Type as com.spvessel.spacevil.Flags.ItemStateType.
     * @param state Visual state as com.spvessel.spacevil.Decorations.ItemState.
     */
    public void addItemState(ItemStateType type, ItemState state) {
        _core.addItemState(type, state);
    }

    /**
     * Removing visual state of an item by type.
     * <p>
     * Type can be BASE, HOVERED, PRESSED, TOGGLED, FOCUSED, DISABLED.
     * 
     * @param type Type as com.spvessel.spacevil.Flags.ItemStateType.
     */
    public void removeItemState(ItemStateType type) {
        _core.removeItemState(type);
    }

    /**
     * Removing all item visual states.
     */
    public void removeAllItemStates() {
        _core.removeAllItemStates();
    }

    /**
     * Getting item visual state by its type.
     * <p>
     * Type can be BASE, HOVERED, PRESSED, TOGGLED, FOCUSED, DISABLED.
     * 
     * @param type Type as com.spvessel.spacevil.Flags.ItemStateType.
     * @return Item visual state as com.spvessel.spacevil.Decorations.ItemState.
     */
    public ItemState getState(ItemStateType type) {
        return _core.getState(type);
    }

    /**
     * Updating Prototype's state according to its ItemStateType.
     */
    protected void updateState() {
        _core.updateState();
    }

    /**
     * Inserting item to the container (this). If the count of container elements is
     * less than the index, then the element is added to the end of the list.
     * 
     * @param item  Child as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @param index Index of insertion.
     */
    public void insertItem(InterfaceBaseItem item, int index) {
        _core.insertItem(item, index);
    }

    /**
     * Adding sequence of items into the container (this).
     * 
     * @param items Sequence of items.
     */
    public void addItems(InterfaceBaseItem... items) {
        for (InterfaceBaseItem item : items) {
            this.addItem(item);
        }
    }

    /**
     * Adding item into the container (this).
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    public void addItem(InterfaceBaseItem item) {
        _core.addItem(item);
    }

    /**
     * Updating an item size or/and position.
     * 
     * @param type  Type of event as com.spvessel.spacevil.Flags.GeometryEventType.
     * @param value Value of a property that was changed.
     */
    public void update(GeometryEventType type, int value) {
        _core.update(type, value);
    }

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
        return _core.isDrawable();
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
        _core.setDrawable(value);
    }

    /**
     * Setting the visibility status of an item. This property may used in
     * conjunction with the isDrawable() property.
     * 
     * @return True: if item is visible. False: if item is invisible.
     */
    public boolean isVisible() {
        return _core.isVisible();
    }

    /**
     * Setting the visibility status of an item. This property may used in
     * conjunction with the isDrawable() property.
     * 
     * @param value True: if item should be visible. False: if item should be
     *              invisible.
     */
    public void setVisible(boolean value) {
        _core.setVisible(value);
    }

    /**
     * Getting boolean value to know if this item can pass further any input events
     * (mouse, keyboard and etc.).
     * <p>
     * Tip: Need for filtering input events.
     * 
     * @return True: if this item pass on any input events. False: If this item do
     *         not pass any input events.
     */
    public boolean isPassEvents() {
        return _core.isPassEvents();
    }

    /**
     * Getting boolean value to know if this item can pass further the specified
     * type of input events (mouse, keyboard and etc.).
     * 
     * @param e Type of input events as com.spvessel.spacevil.Flags.InputEventType.
     * @return True: if this item pass on the specified type of input events. False:
     *         If this item do not pass the specified type of input events.
     */
    public boolean isPassEvents(InputEventType e) {
        if (_core.getBlockedEvents().contains(e))
            return false;
        return true;
    }

    /**
     * Getting all allowed input events.
     * 
     * @return Allowed input events as
     *         List&lt;com.spvessel.spacevil.Flags.InputEventType&gt;
     */
    public List<InputEventType> getPassEvents() {
        return _core.getPassEvents();
    }

    /**
     * Getting all blocked input events.
     * 
     * @return Blocked input events as
     *         List&lt;com.spvessel.spacevil.Flags.InputEventType&gt;
     */
    public List<InputEventType> GetBlockedEvents() {
        return _core.getBlockedEvents();
    }

    /**
     * Setting on or off so that this item can pass further any input events (mouse,
     * keyboard and etc.).
     * 
     * @param value True: if you want that this item may to pass on any input
     *              events. False: if you want that this item cannot to pass on any
     *              input events.
     */
    public void setPassEvents(boolean value) {
        _core.setPassEvents(value);
    }

    /**
     * Setting on or off so that this item can pass further the specified type of
     * input events (mouse, keyboard and etc.).
     * 
     * @param value True: if you want this item can pass further the specified type
     *              of input events. False: if you want this item connot pass
     *              further the specified type of input events.
     * @param e     Type of input events as
     *              com.spvessel.spacevil.Flags.InputEventType.
     */
    public void setPassEvents(boolean value, InputEventType e) {
        _core.setPassEvents(value, e);
    }

    /**
     * Setting on or off so that this item can pass further the specified types of
     * input events (mouse, keyboard and etc.).
     * 
     * @param value  True: if you want this item can pass further the specified
     *               types of input events. False: if you want this item connot pass
     *               further the specified types of input events.
     * @param events List of input event types as
     *               com.spvessel.spacevil.Flags.InputEventType.
     */
    public void setPassEvents(boolean value, List<InputEventType> events) {
        _core.setPassEvents(value, events);
    }

    /**
     * Setting on or off so that this item can pass further the specified types of
     * input events (mouse, keyboard and etc.).
     * 
     * @param value  True: if you want this item can pass further the specified
     *               types of input events. False: if you want this item connot pass
     *               further the specified types of input events.
     * @param events Sequence of input event types as
     *               com.spvessel.spacevil.Flags.InputEventType.
     */
    public void setPassEvents(boolean value, InputEventType... events) {
        for (InputEventType e : events) {
            _core.setPassEvents(value, e);
        }
    }

    /**
     * Returns True if this item is disabled (non-interactive) otherwise returns
     * False.
     * 
     * @return True: this item is disabled. False: this item is enabled.
     */
    public boolean isDisabled() {
        return _core.isDisabled();
    }

    /**
     * Setting this item disabled (become non-interactive) or enabled.
     * 
     * @param value True: if you want to disable this item. False: if you want to
     *              enable this item.
     */
    public void setDisabled(boolean value) {
        _core.setDisabled(value);
    }

    /**
     * Returns True if this item is hovered otherwise returns False.
     * 
     * @return True: this item is hovered. False: this item is not hovered.
     */
    public boolean isMouseHover() {
        return _core.isMouseHover();
    }

    /**
     * Setting this item hovered (mouse cursor located within item's shape).
     * 
     * @param value True: if you want this item be hovered. False: if you want this
     *              item be not hovered.
     */
    public void setMouseHover(boolean value) {
        _core.setMouseHover(value);
    }

    /**
     * Returns True if mouse is pressed on this item (mouse cursor located within
     * item's shape and any of the mouse button is pressed) otherwise False.
     * 
     * @return True: if mouse is pressed on this item. False: if mouse is not
     *         pressed on this item.
     */
    public boolean isMousePressed() {
        return _core.isMousePressed();
    }

    /**
     * Setting True if you want that mouse is pressed on this item (mouse cursor
     * located within item's shape and any of the mouse button is pressed) otherwise
     * False.
     * 
     * @param value True: if you want this item be mouse pressed. False: if you want
     *              this item be not mouse pressed.
     */
    public void setMousePressed(boolean value) {
        _core.setMousePressed(value);
    }

    /**
     * Item's focusable property.
     * <p>
     * True: this item can get focus. False: this item cannot get focus.
     */
    public boolean isFocusable = true;

    /**
     * Returns True if this item gets focus otherwise False.
     * 
     * @return True: if this item is focused. False: if this item is not focused.
     */
    public boolean isFocused() {
        return _core.isFocused();
    }

    protected void setFocused(boolean value) {
        if (isFocusable) {
            _core.setFocused(value);
        }
    }

    /**
     * Setting focus on this item if it is focusable.
     */
    public void setFocus() {
        if (isFocusable) {
            getHandler().setFocusedItem(this);
        }
    }

    protected boolean getHoverVerification(float xpos, float ypos) {
        return _core.getHoverVerification(xpos, ypos);
    }

    /**
     * Getting list of the Prototype's inner items (children).
     * 
     * @return List of children as
     *         List&lt;com.spvessel.spacevil.Core.InterfaceBaseItem&gt;
     */
    public List<InterfaceBaseItem> getItems() {
        return _core.getItems();
    }

    /**
     * Removing the specified item from container (this).
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    public boolean removeItem(InterfaceBaseItem item) {
        return _core.removeItem(item);
    }

    /**
     * Removing all children.
     */
    public void clear() {
        _core.clear();
    }

    void addEventListener(GeometryEventType type, InterfaceBaseItem listener) {
        _core.addEventListener(type, listener);
    }

    void removeEventListener(GeometryEventType type, InterfaceBaseItem listener) {
        _core.removeEventListener(type, listener);
    }

    protected int[] getConfines() {
        return _core.getConfines();
    }

    ItemStateType getCurrentStateType() {
        return _core.getCurrentStateType();
    }

    protected void setState(ItemStateType state) {
        _core.setState(state);
    }

    /**
     * Setting content for this item.
     * <p>
     * Note: this method is only for sorting children i.e. Prototype.getItems()
     * contains equal set of children as input argument:
     * List&lt;com.spvessel.spacevil.Core.InterfaceBaseItem&gt; content. If content
     * is different this method do nothing.
     * 
     * @param content Sorted (in any way) content of this item.
     */
    public void setContent(List<InterfaceBaseItem> content) {
        List<InterfaceBaseItem> oldContent = getItems();
        if (oldContent.size() != content.size())
            return;

        for (InterfaceBaseItem ibi : oldContent) {
            if (!content.contains(ibi))
                return;
        }

        _core.setContent(content);
    }

    /**
     * Getting the custom shape if it is set. You can set any shape using
     * Prototype.setCustomFigure(Figure) and it will replace the default rectangle
     * shape.
     * 
     * @return Custom shape as com.spvessel.spacevil.Decorations.Figure.
     */
    public Figure isCustomFigure() {
        return _core.isCustomFigure();
    }

    /**
     * Setting the custom shape to replace the default rectangle shape.
     * 
     * @param figure Custom shape as com.spvessel.spacevil.Decorations.Figure.
     */
    public void setCustomFigure(Figure figure) {
        _core.setCustomFigure(figure);
    }

    /**
     * Getting the hovering rule of this item.
     * <p>
     * Can be ItemHoverRule.LAZY or ItemHoverRule.STRICT (see
     * com.spvessel.spacevil.Flags.ItemHoverRule).
     * 
     * @return Hovering rule as com.spvessel.spacevil.Flags.ItemHoverRule.
     */
    public ItemHoverRule getHoverRule() {
        return _core.HoverRule;
    }

    /**
     * Setting the hovering rule for this item.
     * 
     * @param rule Hovering rule as com.spvessel.spacevil.Flags.ItemHoverRule.
     */
    public void setHoverRule(ItemHoverRule rule) {
        _core.HoverRule = rule;
    }

    private CursorImage _cursor = DefaultsService.getDefaultCursor();

    /**
     * Getting the mouse cursor image of this item.
     * 
     * @return Mouse cursor image as com.spvessel.spacevil.Decorations.CursorImage.
     */
    public CursorImage getCursor() {
        return _cursor;
    }

    /**
     * Setting mouse cursor image for this item from embedded cursors.
     * 
     * @param cursor Mouse cursor type as
     *               com.spvessel.spacevil.Flags.EmbeddedCursor.
     */
    public void setCursor(EmbeddedCursor cursor) {
        _cursor = new CursorImage(cursor);
    }

    /**
     * Setting mouse cursor image for this item.
     * 
     * @param cursor Mouse cursor image as com.spvessel.spacevil.CursorImage.
     */
    public void setCursor(CursorImage cursor) {
        _cursor = cursor;
    }

    /**
     * Creating and setting mouse cursor image for this item from specified bitmap
     * image.
     * 
     * @param bitmap Bitmap for mouse cursor image as java.awt.image.BufferedImage.
     */
    public void setCursor(BufferedImage bitmap) {
        _cursor = new CursorImage(bitmap);
    }

    /**
     * Creating and setting mouse cursor image for this item from specified scaled
     * bitmap image.
     * 
     * @param bitmap Bitmap for mouse cursor image as java.awt.image.BufferedImage.
     * @param width  New width of mouse cursor image.
     * @param height New height of mouse cursor image.
     */
    public void setCursor(BufferedImage bitmap, int width, int height) {
        _cursor = new CursorImage(bitmap, width, height);
    }
}
