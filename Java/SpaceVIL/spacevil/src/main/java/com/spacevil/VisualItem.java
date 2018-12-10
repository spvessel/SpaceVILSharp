package com.spacevil;

import com.spacevil.Core.*;
import com.spacevil.Decorations.Border;
import com.spacevil.Decorations.CornerRadius;

import java.awt.Color;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.*;
import java.util.Arrays;
import java.util.HashMap;

final class VisualItem extends BaseItem {

    private Lock locker = new ReentrantLock();

    Prototype _main;

    VisualItem() {
        this("VisualItem_");
    }

    VisualItem(String name) {
        com.spacevil.Decorations.ItemState base = new com.spacevil.Decorations.ItemState();
        base.background = getBackground();
        states.put(com.spacevil.Flags.ItemStateType.BASE, base);

        // common default prop
        eventManager = new EventManager();
        setItemName(name);
    }

    // overrides
    @Override
    public void setWidth(int width) {
        int value = width - getWidth();
        if (value != 0) {
            super.setWidth(width);

            if (getParent() != null && getWidthPolicy() == com.spacevil.Flags.SizePolicy.FIXED) {
                boolean layout = getParent() instanceof InterfaceHLayout;
                boolean grid = getParent() instanceof InterfaceGrid;

                if (!layout && !grid)
                    updateBehavior();

                if (layout)
                    ((InterfaceHLayout) getParent()).updateLayout();
                if (grid)
                    ((InterfaceGrid) getParent()).updateLayout();
            }
            eventManager.notifyListeners(com.spacevil.Flags.GeometryEventType.RESIZE_WIDTH, value);
        }
    }

    @Override
    public void setHeight(int height) {
        int value = height - getHeight();
        if (value != 0) {
            super.setHeight(height);

            if (getParent() != null && getHeightPolicy() == com.spacevil.Flags.SizePolicy.FIXED) {
                boolean layout = getParent() instanceof InterfaceVLayout;
                boolean grid = getParent() instanceof InterfaceGrid;

                if (!layout && !grid)
                    updateBehavior();

                if (layout)
                    ((InterfaceVLayout) getParent()).updateLayout();
                if (grid)
                    ((InterfaceGrid) getParent()).updateLayout();
            }
            eventManager.notifyListeners(com.spacevil.Flags.GeometryEventType.RESIZE_HEIGHT, value);
        }
    }

    protected void setPosition(int _x, int _y) {
        this.setX(_x);
        this.setY(_y);
    }

    @Override
    public void setX(int _x) {
        int value = _x - getX();
        if (value != 0) {
            super.setX(_x);
            eventManager.notifyListeners(com.spacevil.Flags.GeometryEventType.MOVED_X, value);
        }
    }

    @Override
    public void setY(int _y) {
        int value = _y - getY();
        if (value != 0) {
            super.setY(_y);
            eventManager.notifyListeners(com.spacevil.Flags.GeometryEventType.MOVED_Y, value);
        }
    }

    // item
    private com.spacevil.Decorations.Border _border = new com.spacevil.Decorations.Border();

    void setBorder(com.spacevil.Decorations.Border border) {
        _border = border;
        getState(com.spacevil.Flags.ItemStateType.BASE).border = _border;
        updateState();
    }

    void setBorderFill(Color fill) {
        _border.setFill(fill);
        getState(com.spacevil.Flags.ItemStateType.BASE).border.setFill(fill);
        updateState();
    }

    public void setBorderFill(int r, int g, int b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        setBorderFill(new Color(r, g, b));
    }

    public void setBorderFill(int r, int g, int b, int a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        setBorderFill(new Color(r, g, b, a));
    }

    public void setBorderFill(float r, float g, float b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        setBorderFill(new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f)));
    }

    public void setBorderFill(float r, float g, float b, float a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        setBorderFill(new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f)));
    }

    void setBorderRadius(CornerRadius radius) {
        _border.setRadius(radius);
        getState(com.spacevil.Flags.ItemStateType.BASE).border.setRadius(radius);
        updateState();
    }

    void setBorderThickness(int thickness) {
        _border.setThickness(thickness);
        getState(com.spacevil.Flags.ItemStateType.BASE).border.setThickness(thickness);
        updateState();
    }

    CornerRadius getBorderRadius() {
        return _border.getRadius();
    }

    int getBorderThickness() {
        return _border.getThickness();
    }

    Color getBorderFill() {
        return _border.getFill();
    }

    Map<com.spacevil.Flags.ItemStateType, com.spacevil.Decorations.ItemState> states = new HashMap<>();
    com.spacevil.Flags.ItemStateType _state = com.spacevil.Flags.ItemStateType.BASE;

    void setState(com.spacevil.Flags.ItemStateType state) {
        _state = state;
        updateState();
    }

    com.spacevil.Flags.ItemStateType getCurrentState() {
        return _state;
    }

    private String _tooltip = "";

    String getToolTip() {
        return _tooltip;
    }

    void setToolTip(String text) {
        _tooltip = text;
    }

    // container
    private com.spacevil.Decorations.Spacing _spacing = new com.spacevil.Decorations.Spacing();

    com.spacevil.Decorations.Spacing getSpacing() {
        return _spacing;
    }

    void setSpacing(com.spacevil.Decorations.Spacing spacing) {
        _spacing = spacing;
    }

    void setSpacing(int horizontal, int vertical) {
        _spacing = new com.spacevil.Decorations.Spacing(horizontal, vertical);
    }

    private com.spacevil.Decorations.Indents _padding = new com.spacevil.Decorations.Indents();

    com.spacevil.Decorations.Indents getPadding() {
        return _padding;
    }

    void setPadding(com.spacevil.Decorations.Indents padding) {
        _padding = padding;
    }

    void setPadding(int left, int top, int right, int bottom) {
        _padding = new com.spacevil.Decorations.Indents(left, top, right, bottom);
    }

    EventManager eventManager = null;
    private List<InterfaceBaseItem> _content = new LinkedList<>();

    List<InterfaceBaseItem> getItems() {
        return _content;
    }

    void setContent(List<InterfaceBaseItem> content) {
        _content = content;
    }

    private void castAndUpdate(InterfaceBaseItem item) {
        if (item instanceof Prototype)
            ((Prototype) item).getCore().updateGeometry();
        else
            ((BaseItem) item).updateGeometry();
    }

    private void castAndRemove(InterfaceBaseItem item) {
        if (item instanceof Prototype)
            ((Prototype) item).getCore().removeItemFromListeners();
        else
            ((BaseItem) item).removeItemFromListeners();
    }

    protected void addItem(InterfaceBaseItem item) {
        locker.lock();
        try {
            if (item.equals(this)) {
                System.out.println("Trying to add current item in himself.");
                return;
            }
            item.setHandler(getHandler());
            addChildren(item);
            _content.add(item);
            ItemsLayoutBox.addItem(getHandler(), item, com.spacevil.Flags.LayoutType.STATIC);
            // needs to force update all attributes
            castAndUpdate(item);
            item.initElements();
            // if (item instanceof VisualItem) {
            // ((VisualItem) item).updateState();
            // }
        } catch (Exception ex) {
            System.out.println(item.getItemName() + "\n" + ex.toString());
        } finally {
            locker.unlock();
        }
    }

    void insertItem(InterfaceBaseItem item, int index) {
        locker.lock();
        try {
            if (item.equals(this)) {
                System.out.println("Trying to add current item in himself.");
                return;
            }
            item.setHandler(getHandler());

            addChildren(item);

            if (index > _content.size())
                _content.add(item);
            else
                _content.add(index, item);

            try {
                ItemsLayoutBox.addItem(getHandler(), item, com.spacevil.Flags.LayoutType.STATIC);
            } catch (Exception ex) {
                System.out.println(item.getItemName());
                throw ex;
            }

            // needs to force update all attributes
            castAndUpdate(item);
            item.initElements();

            // if (item instanceof VisualItem) {
            // ((VisualItem) item).updateState();
            // }
        } catch (Exception ex) {
            System.out.println(item.getItemName() + "\n" + ex.toString());
        } finally {
            locker.unlock();
        }
    }

    void cascadeRemoving(InterfaceBaseItem item, com.spacevil.Flags.LayoutType type) {
        if (item instanceof VisualItem)// и если это действительно контейнер
        {
            VisualItem container = (VisualItem) item;// предполагаю что элемент контейнер
            List<InterfaceBaseItem> tmp = container.getItems();
            while (tmp.size() > 0) {
                InterfaceBaseItem child = container.getItems().get(0);
                // container.cascadeRemoving(child, type);
                // container.getItems().remove(child);
                // child.removeItemFromListeners();
                // ItemsLayoutBox.removeItem(getHandler(), child, type);

                container.removeItem(child);
                tmp.remove(child);
            }
        }
    }

    protected void removeItem(InterfaceBaseItem item) {
        locker.lock();
        try {
            com.spacevil.Flags.LayoutType type;
            if (item instanceof InterfaceFloating) {
                cascadeRemoving(item, com.spacevil.Flags.LayoutType.FLOATING);
                type = com.spacevil.Flags.LayoutType.FLOATING;
            } else {
                cascadeRemoving(item, com.spacevil.Flags.LayoutType.STATIC);
                type = com.spacevil.Flags.LayoutType.STATIC;
            }

            // removing
            _content.remove(item);
            ItemsLayoutBox.removeItem(getHandler(), item, type);

            castAndRemove(item);
        } catch (Exception ex) {
            System.out.println(item.getItemName() + "\n" + ex.toString());
        } finally {
            locker.unlock();
        }
    }

    @Override
    void addEventListener(com.spacevil.Flags.GeometryEventType type, InterfaceBaseItem listener) {
        eventManager.subscribe(type, listener);
    }

    @Override
    void removeEventListener(com.spacevil.Flags.GeometryEventType type, InterfaceBaseItem listener) {
        eventManager.unsubscribe(type, listener);
    }

    void addItemState(com.spacevil.Flags.ItemStateType type, com.spacevil.Decorations.ItemState state) {
        if (states.containsKey(type)) {
            state.value = true;
            states.replace(type, state);
        } else {
            states.put(type, state);
        }
    }

    com.spacevil.Decorations.ItemState getState(com.spacevil.Flags.ItemStateType type) {
        if (states.containsKey(type))
            return states.get(type);
        return null;
    }

    Map<com.spacevil.Flags.ItemStateType, com.spacevil.Decorations.ItemState> getAllStates() {
        return states;
    }

    void removeItemState(com.spacevil.Flags.ItemStateType type) {
        if (type == com.spacevil.Flags.ItemStateType.BASE)
            return;
        if (states.containsKey(type))
            states.remove(type);
    }

    void removeAllItemStates() {
        List<com.spacevil.Flags.ItemStateType> itemsToRemove = states.entrySet().stream().filter(i -> i.getKey() != com.spacevil.Flags.ItemStateType.BASE)
                .map(Map.Entry::getKey).collect(Collectors.toList());
        for (com.spacevil.Flags.ItemStateType item : itemsToRemove)
            states.remove(item);
    }

    @Override
    public void setBackground(Color color) {
        getState(com.spacevil.Flags.ItemStateType.BASE).background = color;
        updateState();
    }

    @Override
    public void setBackground(int r, int g, int b) {
        super.setBackground(r, g, b);
        getState(com.spacevil.Flags.ItemStateType.BASE).background = getBackground();
        updateState();
    }

    @Override
    public void setBackground(int r, int g, int b, int a) {
        super.setBackground(r, g, b, a);
        getState(com.spacevil.Flags.ItemStateType.BASE).background = getBackground();
        updateState();
    }

    @Override
    public void setBackground(float r, float g, float b) {
        super.setBackground(r, g, b);
        getState(com.spacevil.Flags.ItemStateType.BASE).background = getBackground();
        updateState();
    }

    @Override
    public void setBackground(float r, float g, float b, float a) {
        super.setBackground(r, g, b, a);
        getState(com.spacevil.Flags.ItemStateType.BASE).background = getBackground();
        updateState();
    }

    // common properties
    private List<com.spacevil.Flags.InputEventType> _pass_events = new LinkedList<>();

    boolean isPassEvents() {
//        if (_pass_events.size() == 0)
//            return true;
//        return false;

        return (_pass_events.size() == 0);
    }

    List<com.spacevil.Flags.InputEventType> getPassEvents() {
        List<com.spacevil.Flags.InputEventType> result = Arrays.asList(com.spacevil.Flags.InputEventType.values());
        return result.stream().filter(e -> !_pass_events.contains(e)).collect(Collectors.toList());
    }

    List<com.spacevil.Flags.InputEventType> getNonPassEvents() {
        return _pass_events;
    }

    void setPassEvents(boolean value) {
        if (!value) {
            for (com.spacevil.Flags.InputEventType e : Arrays.asList(com.spacevil.Flags.InputEventType.values())) {
                _pass_events.add(e);
            }
        } else {
            _pass_events.clear();
        }
    }

    void setPassEvents(boolean value, com.spacevil.Flags.InputEventType e) {
        if (!value) {
            if (!_pass_events.contains(e))
                _pass_events.add(e);
        } else {
            if (_pass_events.contains(e))
                _pass_events.remove(e);
        }
    }

    void setPassEvents(boolean value, List<com.spacevil.Flags.InputEventType> event_set) {
        if (!value) {
            _pass_events = event_set;
        } else {
            for (com.spacevil.Flags.InputEventType e : event_set) {
                if (_pass_events.contains(e))
                    _pass_events.remove(e);
            }
        }
    }

    private boolean _disabled;

    boolean isDisabled() {
        return _disabled;
    }

    void setDisabled(boolean value) {
        if (_disabled == value)
            return;
        _disabled = value;
        updateState();
    }

    private boolean _hover;

    boolean isMouseHover() {
        return _hover;
    }

    void setMouseHover(boolean value) {
        if (_hover == value)
            return;
        _hover = value;
        updateState();
    }

    private boolean _pressed;

    boolean isMousePressed() {
        return _pressed;
    }

    void setMousePressed(boolean value) {
        if (_pressed == value)
            return;
        _pressed = value;
        updateState();
    }

    private boolean _focused;
    boolean isFocusable = true;

    boolean isFocused() {
        return _focused;
    }

    void setFocused(boolean value) {
        if (_focused == value)
            return;
        _focused = value;
        updateState();
    }

    private boolean _focusable = true;

    public boolean isFocusable() {
        return _focusable;
    }

    public void setFocusable() {
        // foreach inner item focusable value set?
    }

    @Override
    void updateInnersDrawable(boolean value) {
        for (InterfaceBaseItem item : _content) {
            item.setVisible(value);
        }
    }

    void updateState() {
        com.spacevil.Decorations.ItemState s_base = getState(com.spacevil.Flags.ItemStateType.BASE);
        com.spacevil.Decorations.ItemState current = getState(_state);
        super.setBackground(current.background);
        _border = cloneBorder(current.border);

        if (_border.getRadius() == null)
            _border.setRadius(s_base.border.getRadius());
        if (_border.getThickness() < 0)
            _border.setThickness(s_base.border.getThickness());
        if (_border.getFill().getAlpha() == 0)
            _border.setFill(s_base.border.getFill());

        if (current.shape != null)
            setCustomFigure(current.shape);

        com.spacevil.Decorations.ItemState s_disabled = getState(com.spacevil.Flags.ItemStateType.DISABLED);
        if (isDisabled() && s_disabled != null) {
            updateVisualProperties(s_disabled, s_base);
            return;
        }
        com.spacevil.Decorations.ItemState s_focused = getState(com.spacevil.Flags.ItemStateType.FOCUSED);
        if (isFocused() && s_focused != null) {
            updateVisualProperties(s_focused, s_base);
            s_base = s_focused;
        }
        com.spacevil.Decorations.ItemState s_hover = getState(com.spacevil.Flags.ItemStateType.HOVERED);
        if (isMouseHover() && s_hover != null) {
            updateVisualProperties(s_hover, s_base);
            s_base = s_hover;
        }
        com.spacevil.Decorations.ItemState s_pressed = getState(com.spacevil.Flags.ItemStateType.PRESSED);
        if (isMousePressed() && s_pressed != null) {
            updateVisualProperties(s_pressed, s_base);
            s_base = s_pressed;
        }
    }

    private void updateVisualProperties(com.spacevil.Decorations.ItemState state, com.spacevil.Decorations.ItemState prev_state) {
        com.spacevil.Decorations.ItemState current = getState(_state);
        super.setBackground(GraphicsMathService.mixColors(current.background, state.background));
        _border = cloneBorder(state.border);

        if (_border.getRadius() == null)
            _border.setRadius(prev_state.border.getRadius());
        if (_border.getRadius() == null)
            _border.setRadius(getState(com.spacevil.Flags.ItemStateType.BASE).border.getRadius());

        if (_border.getThickness() < 0)
            _border.setThickness(prev_state.border.getThickness());
        if (_border.getThickness() < 0)
            _border.setThickness(getState(com.spacevil.Flags.ItemStateType.BASE).border.getThickness());

        if (_border.getFill().getAlpha() == 0)
            _border.setFill(prev_state.border.getFill());
        if (_border.getFill().getAlpha() == 0)
            _border.setFill(getState(com.spacevil.Flags.ItemStateType.BASE).border.getFill());

        if (state.shape != null)
            setCustomFigure(state.shape);
    }

    private com.spacevil.Decorations.Border cloneBorder(com.spacevil.Decorations.Border border) {
        com.spacevil.Decorations.Border clone = new Border();
        clone.setFill(border.getFill());
        clone.setRadius(border.getRadius());
        clone.setThickness(border.getThickness());
        return clone;
    }

    boolean getHoverVerification(float xpos, float ypos) {
        switch (HoverRule) {
        case LAZY:
            return lazyHoverVerification(xpos, ypos);
        case STRICT:
            return strictHoverVerification(xpos, ypos);
        default:
            return false;
        }
    }

    private boolean strictHoverVerification(float xpos, float ypos) {
        List<float[]> tmp = updateShape();
        if (tmp == null)
            return false;

        float Ax, Ay, Bx, By, Cx, Cy, Px, Py, m, l;
        boolean result = false;

        for (int point = 0; point < tmp.size(); point += 3) {
            Px = xpos;
            Py = ypos;
            Ax = tmp.get(point)[0];
            Ay = tmp.get(point)[1];
            Bx = tmp.get(point + 1)[0];
            By = tmp.get(point + 1)[1];
            Cx = tmp.get(point + 2)[0];
            Cy = tmp.get(point + 2)[1];

            Bx = Bx - Ax;
            By = By - Ay;
            Cx = Cx - Ax;
            Cy = Cy - Ay;
            Px = Px - Ax;
            Py = Py - Ay;
            Ax = 0;
            Ay = 0;

            m = (Px * By - Bx * Py) / (Cx * By - Bx * Cy);
            if (m >= 0) {
                l = (Px - m * Cx) / Bx;
                if (l >= 0 && (m + l) <= 1) {
                    result = true;
                    // _mouse_ptr.setPosition(xpos, ypos);
                    return result;
                }
            }
        }

        // _mouse_ptr.clear();
        return result;
    }

    private boolean lazyHoverVerification(float xpos, float ypos) {
        // if(this instanceof ContextMenu)
        // {
        // System.out.println("context menu");
        // System.out.println(
        // _confines_x_0 + " " +
        // _confines_x_1 + " " +
        // _confines_y_0 + " " +
        // _confines_y_1 + " "
        // );
        // }
        boolean result = false;
        float minx = getX();
        float maxx = getX() + getWidth();
        float miny = getY();
        float maxy = getY() + getHeight();

        if (_confines_x_0 > minx)
            minx = _confines_x_0;

        if (_confines_x_1 < maxx)
            maxx = _confines_x_1;

        if (_confines_y_0 > miny)
            miny = _confines_y_0;

        if (_confines_y_1 < maxy)
            maxy = _confines_y_1;

        if (xpos >= minx && xpos <= maxx && ypos >= miny && ypos <= maxy) {
            result = true;
            // _mouse_ptr.setPosition(xpos, ypos);
        }
        // else {
        // _mouse_ptr.clear();
        // }
        return result;
    }

    private com.spacevil.Decorations.CustomFigure _is_custom = null;

    com.spacevil.Decorations.CustomFigure isCustomFigure() {
        return _is_custom;
    }

    void setCustomFigure(com.spacevil.Decorations.CustomFigure figure) {
        _is_custom = figure;
    }

    @Override
    public List<float[]> makeShape() {
        if (isCustomFigure() != null) {
            setTriangles(isCustomFigure().getFigure());
            if (getState(com.spacevil.Flags.ItemStateType.BASE).shape == null)
                getState(com.spacevil.Flags.ItemStateType.BASE).shape = isCustomFigure();

            if (isCustomFigure().isFixed())
                return GraphicsMathService.toGL(isCustomFigure().updatePosition(getX(), getY()), getHandler());
            else
                return GraphicsMathService.toGL(updateShape(), getHandler());
        }
        setTriangles(GraphicsMathService.getRoundSquare(getBorderRadius(), getWidth(), getHeight(), getX(), getY()));
        return GraphicsMathService.toGL(this, getHandler());
    }

    // style
    private boolean _is_style_set = false;

    @Override
    public void setStyle(com.spacevil.Decorations.Style style) {
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
        setPadding(style.padding);
        setSpacing(style.spacing);
        setMargin(style.margin);
        setVisible(style.isVisible);
        removeAllItemStates();

        com.spacevil.Decorations.ItemState core_state = new com.spacevil.Decorations.ItemState(style.background);
        core_state.border.setRadius(style.borderRadius);
        core_state.border.setThickness(style.borderThickness);
        core_state.border.setFill(style.borderFill);

        for (Map.Entry<com.spacevil.Flags.ItemStateType, com.spacevil.Decorations.ItemState> state : style.getAllStates().entrySet()) {
            addItemState(state.getKey(), state.getValue());
        }
        if (style.shape != null) {
            setCustomFigure(new com.spacevil.Decorations.CustomFigure(style.isFixedShape, style.shape));
            core_state.shape = isCustomFigure();
        }
        addItemState(com.spacevil.Flags.ItemStateType.BASE, core_state);

        setBorderRadius(style.borderRadius);
        setBorderThickness(style.borderThickness);
        setBorderFill(style.borderFill);
    }

    @Override
    public com.spacevil.Decorations.Style getCoreStyle() {
        com.spacevil.Decorations.Style style = new com.spacevil.Decorations.Style();
        style.setSize(getWidth(), getHeight());
        style.setSizePolicy(getWidthPolicy(), getHeightPolicy());
        style.background = getBackground();
        style.minWidth = getMinWidth();
        style.minHeight = getMinHeight();
        style.maxWidth = getMaxWidth();
        style.maxHeight = getMaxHeight();
        style.x = getX();
        style.y = getY();
        style.padding = new com.spacevil.Decorations.Indents(getPadding().left, getPadding().top, getPadding().right, getPadding().bottom);
        style.margin = new com.spacevil.Decorations.Indents(getMargin().left, getMargin().top, getMargin().right, getMargin().bottom);
        style.spacing = new com.spacevil.Decorations.Spacing(getSpacing().horizontal, getSpacing().vertical);
        style.alignment = getAlignment();
        style.borderFill = _border.getFill();
        style.borderRadius = _border.getRadius();
        style.borderThickness = _border.getThickness();
        style.isVisible = isVisible();
        if (isCustomFigure() != null) {
            style.shape = isCustomFigure().getFigure();
            style.isFixedShape = isCustomFigure().isFixed();
        }
        for (Map.Entry<com.spacevil.Flags.ItemStateType, com.spacevil.Decorations.ItemState> state : states.entrySet()) {
            style.addItemState(state.getKey(), state.getValue());
        }

        return style;
    }
}