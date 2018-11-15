package com.spvessel;

import java.awt.Color;
import java.util.List;

import com.spvessel.Core.EventCommonMethodState;
import com.spvessel.Core.EventInputTextMethodState;
import com.spvessel.Core.EventKeyMethodState;
import com.spvessel.Core.EventMouseMethodState;
import com.spvessel.Core.InterfaceBaseItem;
import com.spvessel.Core.Position;
import com.spvessel.Decorations.Border;
import com.spvessel.Decorations.CornerRadius;
import com.spvessel.Decorations.CustomFigure;
import com.spvessel.Decorations.Indents;
import com.spvessel.Decorations.ItemState;
import com.spvessel.Decorations.Spacing;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.GeometryEventType;
import com.spvessel.Flags.InputEventType;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.ItemRule;
import com.spvessel.Flags.ItemStateType;
import com.spvessel.Flags.SizePolicy;

abstract public class Prototype implements InterfaceBaseItem {
    private VisualItem _core = new VisualItem();

    protected VisualItem getCore() {
        return _core;
    }

    protected void setCore(VisualItem core) {
        _core = core;
    }

    static int count = 0;

    public Prototype() {
        _core.setItemName("VisualItem_" + count);
        count++;
        _core._main = this;
    }

    // common events
    public EventCommonMethodState eventFocusGet = new EventCommonMethodState();
    public EventCommonMethodState eventFocusLost = new EventCommonMethodState();
    public EventCommonMethodState eventResized = new EventCommonMethodState();
    public EventCommonMethodState eventDestroyed = new EventCommonMethodState();
    // mouse input
    public EventMouseMethodState eventMouseHover = new EventMouseMethodState();
    public EventMouseMethodState eventMouseClick = new EventMouseMethodState();
    public EventMouseMethodState eventMousePressed = new EventMouseMethodState();
    public EventMouseMethodState eventMouseRelease = new EventMouseMethodState();
    public EventMouseMethodState eventMouseDrag = new EventMouseMethodState();
    public EventMouseMethodState eventMouseDrop = new EventMouseMethodState();
    public EventMouseMethodState eventScrollUp = new EventMouseMethodState();
    public EventMouseMethodState eventScrollDown = new EventMouseMethodState();
    // keyboard input
    public EventKeyMethodState eventKeyPress = new EventKeyMethodState();
    public EventKeyMethodState eventKeyRelease = new EventKeyMethodState();
    // text input
    public EventInputTextMethodState eventTextInput = new EventInputTextMethodState();

    public void setHandler(WindowLayout handler) {
        _core.setHandler(handler);
    }

    public WindowLayout getHandler() {
        return _core.getHandler();
    }

    public String getToolTip() {
        return _core.getToolTip();
    }

    public Prototype getParent() {
        return _core.getParent();
    }

    public void setParent(Prototype parent) {
        _core.setParent(parent);
    }

    public void setToolTip(String text) {
        _core.setToolTip(text);
    }

    public Spacing getSpacing() {
        return _core.getSpacing();
    }

    public void setSpacing(Spacing spacing) {
        _core.setSpacing(spacing);
    }

    public void setSpacing(int horizontal, int vertical) {
        _core.setSpacing(horizontal, vertical);
    }

    public Indents getPadding() {
        return _core.getPadding();
    }

    public void setPadding(Indents padding) {
        _core.setPadding(padding);
    }

    public void setPadding(int left, int top, int right, int bottom) {
        _core.setPadding(left, top, right, bottom);
    }

    public Indents getMargin() {
        return _core.getMargin();
    }

    public void setMargin(Indents padding) {
        _core.setMargin(padding);
    }

    public void setMargin(int left, int top, int right, int bottom) {
        _core.setMargin(left, top, right, bottom);
    }

    public void setBorder(Border border) {
        _core.setBorder(border);
    }

    public void setBorderFill(Color fill) {
        _core.setBorderFill(fill);
    }

    public void setBorderRadius(CornerRadius radius) {
        _core.setBorderRadius(radius);
    }

    public void setBorderRadius(int radius) {
        _core.setBorderRadius(new CornerRadius(radius));
    }

    public void setBorderThickness(int thickness) {
        _core.setBorderThickness(thickness);
    }

    public CornerRadius getBorderRadius() {
        return _core.getBorderRadius();
    }

    public int getBorderThickness() {
        return _core.getBorderThickness();
    }

    public Color getBorderFill() {
        return _core.getBorderFill();
    }

    public void initElements() {
        _core.initElements();
    }

    public List<float[]> getTriangles() {
        return _core.getTriangles();
    }

    public void setTriangles(List<float[]> triangles) {
        _core.setTriangles(triangles);
    }

    public List<float[]> makeShape() {
        return _core.makeShape();
    }

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

    public void setItemName(String name) {
        _core.setItemName(name);
    }

    public String getItemName() {
        return _core.getItemName();
    }

    public void setMinWidth(int width) {
        _core.setMinWidth(width);
    }

    public void setWidth(int width) {
        _core.setWidth(width);
    }

    public void setMaxWidth(int width) {
        _core.setMaxWidth(width);
    }

    public void setMinHeight(int height) {
        _core.setMinHeight(height);
    }

    public void setHeight(int height) {
        _core.setHeight(height);
    }

    public void setMaxHeight(int height) {
        _core.setMaxHeight(height);
    }

    public int getMinWidth() {
        return _core.getMinWidth();
    }

    public int getWidth() {
        return _core.getWidth();
    }

    public int getMaxWidth() {
        return _core.getMaxWidth();
    }

    public int getMinHeight() {
        return _core.getMinHeight();
    }

    public int getHeight() {
        return _core.getHeight();
    }

    public int getMaxHeight() {
        return _core.getMaxHeight();
    }

    public void setSize(int width, int height) {
        _core.setSize(width, height);
    }

    public void setMinSize(int width, int height) {
        _core.setMinSize(width, height);
    }

    public void setMaxSize(int width, int height) {
        _core.setMaxSize(width, height);
    }

    public int[] getSize() {
        return _core.getSize();
    }

    public void setAlignment(List<ItemAlignment> alignment) {
        _core.setAlignment(alignment);
    }

    public void setAlignment(ItemAlignment... alignment) {
        _core.setAlignment(alignment);
    }

    public List<ItemAlignment> getAlignment() {
        return _core.getAlignment();
    }

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

    public void setConfines() {
        _core.setConfines();
    }

    public void setConfines(int x0, int x1, int y0, int y1) {
        _core._confines_x_0 = x0;
        _core._confines_x_1 = x1;
        _core._confines_y_0 = y0;
        _core._confines_y_1 = y1;
    }

    public void setStyle(Style style) {
        _core.setStyle(style);
    }

    public Style getCoreStyle() {
        return _core.getCoreStyle();
    }

    public boolean isShadowDrop() {
        return _core.isShadowDrop();
    }

    public void setShadowDrop(boolean value) {
        _core.setShadowDrop(value);
    }

    public void setShadowRadius(int radius) {
        _core.setShadowRadius(radius);
    }

    public int getShadowRadius() {
        return _core.getShadowRadius();
    }

    public Color getShadowColor() {
        return _core.getShadowColor();
    }

    public void setShadowColor(Color color) {
        _core.setShadowColor(color);
    }

    public Position getShadowPos() {
        return _core.getShadowPos();
    }

    public void setShadow(int radius, int x, int y, Color color) {
        _core.setShadow(radius, x, y, color);
    }

    public void setFocus() {
        if (isFocusable)
            getHandler().setFocusedItem(this);
    }

    public void addItemState(ItemStateType type, ItemState state) {
        _core.addItemState(type, state);
    }

    public void removeItemState(ItemStateType type) {
        _core.removeItemState(type);
    }

    public void removeAllItemStates() {
        _core.removeAllItemStates();
    }

    public ItemState getState(ItemStateType type) {
        return _core.getState(type);
    }

    protected void updateState() {
        _core.updateState();
    }

    public void insertItem(InterfaceBaseItem item, int index) {
        _core.insertItem(item, index);
    }

    public void addItems(InterfaceBaseItem... items) {
        for (InterfaceBaseItem item : items) {
            this.addItem(item);
        }
    }

    public void addItem(InterfaceBaseItem item) {
        _core.addItem(item);
    }

    public void update(GeometryEventType type, int value) {
        _core.update(type, value);
    }

    public boolean isDrawable() {
        return _core.isDrawable();
    }

    public void setDrawable(boolean value) {
        _core.setDrawable(value);
    }

    public boolean isVisible() {
        return _core.isVisible();
    }

    public void setVisible(boolean value) {
        _core.setVisible(value);
    }

    public boolean isPassEvents() {
        return _core.isPassEvents();
    }

    public boolean isPassEvents(InputEventType e) {
        if (_core.getNonPassEvents().contains(e))
            return false;
        return true;
    }

    public List<InputEventType> getPassEvents() {
        return _core.getPassEvents();
    }

    public List<InputEventType> getNonPassEvents() {
        return _core.getNonPassEvents();
    }

    public void setPassEvents(boolean value) {
        _core.setPassEvents(value);
    }

    public void setPassEvents(boolean value, InputEventType e) {
        _core.setPassEvents(value, e);
    }

    public void setPassEvents(boolean value, List<InputEventType>  events_set) {
        _core.setPassEvents(value, events_set);
    }

    public void setPassEvents(boolean value, InputEventType...  events_set) {
        for (InputEventType e : events_set) {
            _core.setPassEvents(value, e);
        }
    }

    public boolean isDisabled() {
        return _core.isDisabled();
    }

    public void setDisabled(boolean value) {
        _core.setDisabled(value);
    }

    public boolean isMouseHover() {
        return _core.isMouseHover();
    }

    public void setMouseHover(boolean value) {
        _core.setMouseHover(value);
    }

    public boolean isMousePressed() {
        return _core.isMousePressed();
    }

    public void setMousePressed(boolean value) {
        _core.setMousePressed(value);
    }

    public boolean isFocusable = true;

    public boolean isFocused() {
        return _core.isFocused();
    }

    public void setFocused(boolean value) {
        if (isFocusable) {
            _core.setFocused(value);
        }
    }

    protected boolean getHoverVerification(float xpos, float ypos) {
        return _core.getHoverVerification(xpos, ypos);
    }

    public List<InterfaceBaseItem> getItems() {
        return _core.getItems();
    }

    public void removeItem(InterfaceBaseItem item) {
        _core.removeItem(item);
    }

    protected void addEventListener(GeometryEventType type, InterfaceBaseItem listener) {
        _core.addEventListener(type, listener);
    }

    protected void removeEventListener(GeometryEventType type, InterfaceBaseItem listener) {
        _core.removeEventListener(type, listener);
    }

    public int[] getConfines() {
        return _core.getConfines();
    }

    protected ItemStateType getCurrentState() {
        return _core.getCurrentState();
    }

    protected void setState(ItemStateType state) {
        _core.setState(state);
    }

    public void setContent(List<InterfaceBaseItem> content) {
        _core.setContent(content);
    }

    public CustomFigure isCustomFigure() {
        return _core.isCustomFigure();
    }

    public void setCustomFigure(CustomFigure figure) {
        _core.setCustomFigure(figure);
    }

    public ItemRule getHoverRule() {
        return _core.HoverRule;
    }

    public void setHoverRule(ItemRule rule) {
        _core.HoverRule = rule;
    }
}
