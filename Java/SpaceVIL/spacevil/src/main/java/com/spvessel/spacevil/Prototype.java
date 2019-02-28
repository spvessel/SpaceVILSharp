package com.spvessel.spacevil;

import java.awt.Color;
import java.util.List;

import com.spvessel.spacevil.Core.EventCommonMethodState;
import com.spvessel.spacevil.Core.EventInputTextMethodState;
import com.spvessel.spacevil.Core.EventKeyMethodState;
import com.spvessel.spacevil.Core.EventMouseMethodState;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.CustomFigure;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Spacing;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemRule;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

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
     * Constructs a Prototype
     */
    public Prototype() {
        _core.setItemName("VisualItem_" + count);
        count++;
        _core._main = this;
    }

    /**
     * Common events (resize, destroy)
      */
    EventCommonMethodState eventFocusGet = new EventCommonMethodState();
    EventCommonMethodState eventFocusLost = new EventCommonMethodState();
    public EventCommonMethodState eventResize = new EventCommonMethodState();
    public EventCommonMethodState eventDestroy = new EventCommonMethodState();
    /**
     * Mouse input events
     */
    public EventMouseMethodState eventMouseHover = new EventMouseMethodState();
    public EventMouseMethodState eventMouseClick = new EventMouseMethodState();
    public EventMouseMethodState eventMouseDoubleClick = new EventMouseMethodState();
    public EventMouseMethodState eventMousePress = new EventMouseMethodState();
    // public EventMouseMethodState eventMouseRelease = new EventMouseMethodState();
    public EventMouseMethodState eventMouseDrag = new EventMouseMethodState();
    public EventMouseMethodState eventMouseDrop = new EventMouseMethodState();
    public EventMouseMethodState eventScrollUp = new EventMouseMethodState();
    public EventMouseMethodState eventScrollDown = new EventMouseMethodState();
    /**
     * Keyboard input events
     */
    public EventKeyMethodState eventKeyPress = new EventKeyMethodState();
    public EventKeyMethodState eventKeyRelease = new EventKeyMethodState();
    /**
     * Text input events
     */
    public EventInputTextMethodState eventTextInput = new EventInputTextMethodState();

    /**
     * Set parent window for the Prototype
     */
    public void setHandler(WindowLayout handler) {
        _core.setHandler(handler);
    }
    public WindowLayout getHandler() {
        return _core.getHandler();
    }

    /**
     * @return tooltip of the Prototype
     */
    public String getToolTip() {
        return _core.getToolTip();
    }
    public void setToolTip(String text) {
        _core.setToolTip(text);
    }

    /**
     * Prototype parent item
     */
    public Prototype getParent() {
        return _core.getParent();
    }
    public void setParent(Prototype parent) {
        _core.setParent(parent);
    }

    /**
     * Spacing(horizontal, vertical) of the Prototype
     */
    public Spacing getSpacing() {
        return _core.getSpacing();
    }
    public void setSpacing(Spacing spacing) {
        _core.setSpacing(spacing);
    }
    public void setSpacing(int horizontal, int vertical) {
        _core.setSpacing(horizontal, vertical);
    }

    /**
     * Prototype padding (left, top, right, bottom)
     */
    public Indents getPadding() {
        return _core.getPadding();
    }
    public void setPadding(Indents padding) {
        _core.setPadding(padding);
    }
    public void setPadding(int left, int top, int right, int bottom) {
        _core.setPadding(left, top, right, bottom);
    }

    /**
     * Margin of the Prototype (left, top, right, bottom)
     */
    public Indents getMargin() {
        return _core.getMargin();
    }
    public void setMargin(Indents margin) {
        _core.setMargin(margin);
    }
    public void setMargin(int left, int top, int right, int bottom) {
        _core.setMargin(left, top, right, bottom);
    }

    /**
     * @param border Border of the Prototype
     */
    public void setBorder(Border border) {
        _core.setBorder(border);
    }

    /**
     * Prototype border color
     */
    public void setBorderFill(Color fill) {
        _core.setBorderFill(fill);
    }
    public Color getBorderFill() {
        return _core.getBorderFill();
    }
    public void setBorderFill(int r, int g, int b) {
        _core.setBorderFill(r, g, b);
    }    
    public void setBorderFill(int r, int g, int b, int a) {
        _core.setBorderFill(r, g, b, a);
    }    
    public void setBorderFill(float r, float g, float b) {
        _core.setBorderFill(r, g, b);
    }    
    public void setBorderFill(float r, float g, float b, float a) {
        _core.setBorderFill(r, g, b, a);
    }    
    
    /**
     * Radius of the border's corners
     */
    public void setBorderRadius(CornerRadius radius) {
        _core.setBorderRadius(radius);
    }
    public void setBorderRadius(int radius) {
        _core.setBorderRadius(new CornerRadius(radius));
    }
    public CornerRadius getBorderRadius() {
        return _core.getBorderRadius();
    }

    /**
     * Border thickness of the Prototype
     */
    public void setBorderThickness(int thickness) {
        _core.setBorderThickness(thickness);
    }
    public int getBorderThickness() {
        return _core.getBorderThickness();
    }

    /**
     * Initialization and adding of all elements in the Prototype
     */
    public void initElements() {
        _core.initElements();
    }

    /**
     * Make/get shape of the item using triangles list
     */
    public List<float[]> getTriangles() {
        return _core.getTriangles();
    }
    public void setTriangles(List<float[]> triangles) {
        _core.setTriangles(triangles);
    }
    public List<float[]> makeShape() {
        return _core.makeShape();
    }

    /**
     * Text color in the Prototype
     */
    public void setBackground(Color color) {
        _core.setBackground(color);
    }
    public void setBackground(int r, int g, int b) {
        _core.setBackground(r, g, b);
    }
    public void setBackground(int r, int g, int b, int a) {
        _core.setBackground(r, g, b, a);
    }
    public void setBackground(float r, float g, float b) {
        _core.setBackground(r, g, b);
    }
    public void setBackground(float r, float g, float b, float a) {
        _core.setBackground(r, g, b, a);
    }
    public Color getBackground() {
        return _core.getBackground();
    }

    /**
     * Name of the Prototype
     */
    public void setItemName(String name) {
        _core.setItemName(name);
    }
    public String getItemName() {
        return _core.getItemName();
    }

    /**
     * Prototype minimum width
     */
    public void setMinWidth(int width) {
        _core.setMinWidth(width);
    }
    public int getMinWidth() {
        return _core.getMinWidth();
    }
    /**
     * Prototype width
     */
    public void setWidth(int width) {
        _core.setWidth(width);
    }
    public int getWidth() {
        return _core.getWidth();
    }

    /**
     * Prototype maximum width
     */
    public void setMaxWidth(int width) {
        _core.setMaxWidth(width);
    }
    public int getMaxWidth() {
        return _core.getMaxWidth();
    }

    /**
     * Prototype minimum height
     */
    public void setMinHeight(int height) {
        _core.setMinHeight(height);
    }
    public int getMinHeight() {
        return _core.getMinHeight();
    }

    /**
     * Prototype height
     */
    public void setHeight(int height) {
        _core.setHeight(height);
    }
    public int getHeight() {
        return _core.getHeight();
    }

    /**
     * Prototype maximum height
     */
    public void setMaxHeight(int height) {
        _core.setMaxHeight(height);
    }
    public int getMaxHeight() {
        return _core.getMaxHeight();
    }

    /**
     * Prototype size (width, height)
     */
    public void setSize(int width, int height) {
        _core.setSize(width, height);
    }
    public int[] getSize() {
        return _core.getSize();
    }

    /**
     * Prototype minimum size (width, height)
     */
    public void setMinSize(int width, int height) {
        _core.setMinSize(width, height);
    }
    public int[] getMinSize() {
        return _core.getMinSize();
    }

    /**
     * Prototype maximum size (width, height)
     */
    public void setMaxSize(int width, int height) {
        _core.setMaxSize(width, height);
    }
    public int[] getMaxSize() {
        return _core.getMaxSize();
    }

    /**
     * Prototype alignment
     */
    public void setAlignment(List<ItemAlignment> alignment) {
        _core.setAlignment(alignment);
    }
    public void setAlignment(ItemAlignment... alignment) {
        _core.setAlignment(alignment);
    }
    public List<ItemAlignment> getAlignment() {
        return _core.getAlignment();
    }

    /**
     * Prototype size policy (FIXED, EXPAND) by width and height
     */
    public void setSizePolicy(SizePolicy width, SizePolicy height) {
        _core.setSizePolicy(width, height);
    }
    public void setWidthPolicy(SizePolicy policy) {
        _core.setWidthPolicy(policy);
    }
    public SizePolicy getWidthPolicy() {
        return _core.getWidthPolicy();
    }
    public void setHeightPolicy(SizePolicy policy) {
        _core.setHeightPolicy(policy);
    }
    public SizePolicy getHeightPolicy() {
        return _core.getHeightPolicy();
    }

    /**
     * Prototype position
     * @param x X position of the left top corner
     * @param y Y position of the left top corner
     */
    public void setPosition(int x, int y) {
        _core.setX(x);
        _core.setY(y);
    }
    public void setX(int x) {
        _core.setX(x);
    }
    public int getX() {
        return _core.getX();
    }
    public void setY(int y) {
        _core.setY(y);
    }
    public int getY() {
        return _core.getY();
    }

    /**
     * Set default confines of the Prototype based on parent properties
     */
    public void setConfines() {
        _core.setConfines();
    }

    /**
     * Set Prototype confines
     */
    public void setConfines(int x0, int x1, int y0, int y1) {
        _core._confines_x_0 = x0;
        _core._confines_x_1 = x1;
        _core._confines_y_0 = y0;
        _core._confines_y_1 = y1;
    }

    /**
     * Set style of the Prototype
     */
    public void setStyle(Style style) {
        _core.setStyle(style);
    }

    /**
     * Generate all item properties in one style
     */
    public Style getCoreStyle() {
        return _core.getCoreStyle();
    }

    /**
     * @return if Prototype has shadow
     */
    public boolean isShadowDrop() {
        return _core.isShadowDrop();
    }
    public void setShadowDrop(boolean value) {
        _core.setShadowDrop(value);
    }

    /**
     * Radius of the shadows corners
     */
    public void setShadowRadius(int radius) {
        _core.setShadowRadius(radius);
    }
    public int getShadowRadius() {
        return _core.getShadowRadius();
    }

    /**
     * Shadow color
     */
    public Color getShadowColor() {
        return _core.getShadowColor();
    }
    public void setShadowColor(Color color) {
        _core.setShadowColor(color);
    }

    /**
     * Prototype's shadow position
     */
    public Position getShadowPos() {
        return _core.getShadowPos();
    }

    /**
     * Set Prototype's shadow
     * @param radius Radius of the shadow's corners
     * @param x X position of the shadow
     * @param y Y Position of the shadow
     * @param color Shadow color
     */
    public void setShadow(int radius, int x, int y, Color color) {
        _core.setShadow(radius, x, y, color);
    }

    /**
     * Set focus on the Prototype if its focusable
     */
    public void setFocus() {
        if (isFocusable)
            getHandler().setFocusedItem(this);
    }

    /**
     * Add new item state for one of the item state types (BASE,
     *     HOVERED, PRESSED, TOGGLED, FOCUSED, DISABLED)
     */
    public void addItemState(ItemStateType type, ItemState state) {
        _core.addItemState(type, state);
    }

    /**
     * Remove item state
     */
    public void removeItemState(ItemStateType type) {
        _core.removeItemState(type);
    }

    /**
     * Remove all item states
     */
    public void removeAllItemStates() {
        _core.removeAllItemStates();
    }

    /**
     * Returns ItemState for the ItemStateType type
     */
    public ItemState getState(ItemStateType type) {
        return _core.getState(type);
    }

    /**
     * Update Prototype's state according to its ItemStateType
     */
    protected void updateState() {
        _core.updateState();
    }

    /**
     * Insert item to the Prototype. If Prototype has more items
     * than index replace existing item, else add new item
     */
    public void insertItem(InterfaceBaseItem item, int index) {
        _core.insertItem(item, index);
    }

    /**
     * Add items into the Prototype
     */
    public void addItems(InterfaceBaseItem... items) {
        for (InterfaceBaseItem item : items) {
            this.addItem(item);
        }
    }
    public void addItem(InterfaceBaseItem item) {
        _core.addItem(item);
    }

    /**
     * Update Prototype size or position depending on the GeometryEventType
     */
    public void update(GeometryEventType type, int value) {
        _core.update(type, value);
    }

    /**
     * Is Prototype and its inner items drawable
     */
    public boolean isDrawable() {
        return _core.isDrawable();
    }
    public void setDrawable(boolean value) {
        _core.setDrawable(value);
    }

    /**
     * Is Prototype visible
     */
    public boolean isVisible() {
        return _core.isVisible();
    }
    public void setVisible(boolean value) {
        _core.setVisible(value);
    }

    /**
     * Is Prototype pass events throw itself
     */
    public boolean isPassEvents() {
        return _core.isPassEvents();
    }

    /**
     * Is Prototype pass the InputEventType throw
     */
    public boolean isPassEvents(InputEventType e) {
        if (_core.getNonPassEvents().contains(e))
            return false;
        return true;
    }

    /**
     * @return list of the InputEventType passed throw Prototype
     */
    public List<InputEventType> getPassEvents() {
        return _core.getPassEvents();
    }

    /**
     * @return list of the InputEventType non passed throw Prototype
     */
    public List<InputEventType> getNonPassEvents() {
        return _core.getNonPassEvents();
    }

    /**
     * Sets all InputEventType passed or non passed (value)
     */
    public void setPassEvents(boolean value) {
        _core.setPassEvents(value);
    }

    /**
     * @param e InputEventType
     * @param value passed or non passed
     */
    public void setPassEvents(boolean value, InputEventType e) {
        _core.setPassEvents(value, e);
    }

    /**
     * @param events_set list of the InputEventTypes
     * @param value passed or non passed for all list events_set
     */
    public void setPassEvents(boolean value, List<InputEventType>  events_set) {
        _core.setPassEvents(value, events_set);
    }
    public void setPassEvents(boolean value, InputEventType...  events_set) {
        for (InputEventType e : events_set) {
            _core.setPassEvents(value, e);
        }
    }

    /**
     * Is Prototype disabled
     */
    public boolean isDisabled() {
        return _core.isDisabled();
    }
    public void setDisabled(boolean value) {
        _core.setDisabled(value);
    }

    /**
     * Is mouse hover on the Prototype
     */
    public boolean isMouseHover() {
        return _core.isMouseHover();
    }
    public void setMouseHover(boolean value) {
        _core.setMouseHover(value);
    }

    /**
     * Is mouse pressed on the Prototype
     */
    public boolean isMousePressed() {
        return _core.isMousePressed();
    }
    public void setMousePressed(boolean value) {
        _core.setMousePressed(value);
    }

    /**
     * Is Prototype focusable
     */
    public boolean isFocusable = true;

    /**
     * Is Prototype focused
     */
    public boolean isFocused() {
        return _core.isFocused();
    }
    public void setFocused(boolean value) {
        if (isFocusable) {
            _core.setFocused(value);
        }
    }

    boolean getHoverVerification(float xpos, float ypos) {
        return _core.getHoverVerification(xpos, ypos);
    }

    /**
     * Returns list of the Prototype's inner items
     */
    public List<InterfaceBaseItem> getItems() {
        return _core.getItems();
    }

    /**
     * Remove item from the Prototype
     */
    public void removeItem(InterfaceBaseItem item) {
        _core.removeItem(item);
    }

    void addEventListener(GeometryEventType type, InterfaceBaseItem listener) {
        _core.addEventListener(type, listener);
    }

    void removeEventListener(GeometryEventType type, InterfaceBaseItem listener) {
        _core.removeEventListener(type, listener);
    }

    /**
     * @return Prototype confines
     */
    public int[] getConfines() {
        return _core.getConfines();
    }

    ItemStateType getCurrentState() {
        return _core.getCurrentState();
    }
    
    void setState(ItemStateType state) {
        _core.setState(state);
    }

    // /**
    //  * Set list of the Prototype's inner items. Old items will be removed
    //  */
    // public void setContent(List<InterfaceBaseItem> content) {
    //     _core.setContent(content);
    // }

    /**
     * Is Prototype has CustomFigure shape, return it
     */
    public CustomFigure isCustomFigure() {
        return _core.isCustomFigure();
    }

    /**
     * Sets shape of the Prototype as CustomFigure
     */
    public void setCustomFigure(CustomFigure figure) {
        _core.setCustomFigure(figure);
    }

    /**
     * Hover rule of the Prototype
     */
    public ItemRule getHoverRule() {
        return _core.HoverRule;
    }
    public void setHoverRule(ItemRule rule) {
        _core.HoverRule = rule;
    }
}
