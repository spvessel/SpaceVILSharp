package com.spvessel;

import com.spvessel.Core.*;
import com.spvessel.Decorations.*;
import com.spvessel.Flags.GeometryEventType;
import com.spvessel.Flags.InputEventType;
import com.spvessel.Flags.ItemStateType;
import com.spvessel.Flags.LayoutType;
import com.spvessel.Flags.SizePolicy;

import java.awt.Color;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.*;
import java.util.Arrays;
import java.util.HashMap;

public final class VisualItem extends BaseItem {

    protected Prototype _main;

    protected VisualItem() {
        this("VisualItem_");
    }

    protected VisualItem(String name) {
        ItemState base = new ItemState();
        base.background = getBackground();
        states.put(ItemStateType.BASE, base);

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

            if (getParent() != null && getWidthPolicy() == SizePolicy.FIXED) {
                boolean layout = getParent() instanceof InterfaceHLayout;
                boolean grid = getParent() instanceof InterfaceGrid;

                if (!layout && !grid)
                    updateBehavior();

                if (layout)
                    ((InterfaceHLayout) getParent()).updateLayout();
                if (grid)
                    ((InterfaceGrid) getParent()).updateLayout();
            }
            eventManager.notifyListeners(GeometryEventType.RESIZE_WIDTH, value);
        }
    }

    @Override
    public void setHeight(int height) {
        int value = height - getHeight();
        if (value != 0) {
            super.setHeight(height);

            if (getParent() != null && getHeightPolicy() == SizePolicy.FIXED) {
                boolean layout = getParent() instanceof InterfaceVLayout;
                boolean grid = getParent() instanceof InterfaceGrid;

                if (!layout && !grid)
                    updateBehavior();

                if (layout)
                    ((InterfaceVLayout) getParent()).updateLayout();
                if (grid)
                    ((InterfaceGrid) getParent()).updateLayout();
            }
            eventManager.notifyListeners(GeometryEventType.RESIZE_HEIGHT, value);
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
            eventManager.notifyListeners(GeometryEventType.MOVED_X, value);
        }
    }

    @Override
    public void setY(int _y) {
        int value = _y - getY();
        if (value != 0) {
            super.setY(_y);
            eventManager.notifyListeners(GeometryEventType.MOVED_Y, value);
        }
    }

    // item
    private Border _border = new Border();

    protected void setBorder(Border border) {
        _border = border;
        getState(ItemStateType.BASE).border = _border;
        updateState();
    }

    protected void setBorderFill(Color fill) {
        _border.setFill(fill);
        getState(ItemStateType.BASE).border.setFill(fill);
        updateState();
    }

    protected void setBorderRadius(CornerRadius radius) {
        _border.setRadius(radius);
        getState(ItemStateType.BASE).border.setRadius(radius);
        updateState();
    }

    protected void setBorderThickness(int thickness) {
        _border.setThickness(thickness);
        getState(ItemStateType.BASE).border.setThickness(thickness);
        updateState();
    }

    protected CornerRadius getBorderRadius() {
        return _border.getRadius();
    }

    protected int getBorderThickness() {
        return _border.getThickness();
    }

    protected Color getBorderFill() {
        return _border.getFill();
    }

    protected Map<ItemStateType, ItemState> states = new HashMap<ItemStateType, ItemState>();
    protected ItemStateType _state = ItemStateType.BASE;

    protected void setState(ItemStateType state) {
        _state = state;
        updateState();
    }

    protected ItemStateType getCurrentState() {
        return _state;
    }

    private String _tooltip = "";

    protected String getToolTip() {
        return _tooltip;
    }

    protected void setToolTip(String text) {
        _tooltip = text;
    }

    // container
    private Spacing _spacing = new Spacing();

    protected Spacing getSpacing() {
        return _spacing;
    }

    protected void setSpacing(Spacing spacing) {
        _spacing = spacing;
    }

    protected void setSpacing(int horizontal, int vertical) {
        _spacing = new Spacing(horizontal, vertical);
    }

    private Indents _padding = new Indents();

    protected Indents getPadding() {
        return _padding;
    }

    protected void setPadding(Indents padding) {
        _padding = padding;
    }

    protected void setPadding(int left, int top, int right, int bottom) {
        _padding = new Indents(left, top, right, bottom);
    }

    protected EventManager eventManager = null;
    private List<InterfaceBaseItem> _content = new LinkedList<InterfaceBaseItem>();

    protected List<InterfaceBaseItem> getItems() {
        return _content;
    }

    protected void setContent(List<InterfaceBaseItem> content) {
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
        getHandler().engineLocker.lock();
        try {
            if (item.equals(this)) {
                System.out.println("Trying to add current item in himself.");
                return;
            }
            item.setHandler(getHandler());
            addChildren(item);
            _content.add(item);
            ItemsLayoutBox.addItem(getHandler(), item, LayoutType.STATIC);
            // needs to force update all attributes
            castAndUpdate(item);
            item.initElements();
            // if (item instanceof VisualItem) {
            // ((VisualItem) item).updateState();
            // }
        } catch (Exception ex) {
            System.out.println(item.getItemName() + "\n" + ex.toString());
        } finally {
            getHandler().engineLocker.unlock();
        }
    }

    protected void insertItem(InterfaceBaseItem item, int index) {
        getHandler().engineLocker.lock();
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
                ItemsLayoutBox.addItem(getHandler(), item, LayoutType.STATIC);
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
            getHandler().engineLocker.unlock();
        }
    }

    private void cascadeRemoving(InterfaceBaseItem item, LayoutType type) {
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
        getHandler().engineLocker.lock();
        try {
            LayoutType type;
            if (item instanceof InterfaceFloating) {
                cascadeRemoving(item, LayoutType.FLOATING);
                type = LayoutType.FLOATING;
            } else {
                cascadeRemoving(item, LayoutType.STATIC);
                type = LayoutType.STATIC;
            }

            // removing
            _content.remove(item);
            ItemsLayoutBox.removeItem(getHandler(), item, type);

            castAndRemove(item);
        } catch (Exception ex) {
            System.out.println(item.getItemName() + "\n" + ex.toString());
        } finally {
            getHandler().engineLocker.unlock();
        }
    }

    @Override
    protected void addEventListener(GeometryEventType type, InterfaceBaseItem listener) {
        eventManager.subscribe(type, listener);
    }

    @Override
    protected void removeEventListener(GeometryEventType type, InterfaceBaseItem listener) {
        eventManager.unsubscribe(type, listener);
    }

    protected void addItemState(ItemStateType type, ItemState state) {
        if (states.containsKey(type)) {
            state.value = true;
            states.replace(type, state);
        } else {
            states.put(type, state);
        }
    }

    protected ItemState getState(ItemStateType type) {
        if (states.containsKey(type))
            return states.get(type);
        return null;
    }

    protected Map<ItemStateType, ItemState> getAllStates() {
        return states;
    }

    protected void removeItemState(ItemStateType type) {
        if (type == ItemStateType.BASE)
            return;
        if (states.containsKey(type))
            states.remove(type);
    }

    protected void removeAllItemStates() {
        List<ItemStateType> itemsToRemove = states.entrySet().stream().filter(i -> i.getKey() != ItemStateType.BASE)
                .map(Map.Entry::getKey).collect(Collectors.toList());
        for (ItemStateType item : itemsToRemove)
            states.remove(item);
    }

    @Override
    public void setBackground(Color color) {
        getState(ItemStateType.BASE).background = color;
        updateState();
    }

    @Override
    public void setBackground(int r, int g, int b) {
        super.setBackground(r, g, b);
        getState(ItemStateType.BASE).background = getBackground();
        updateState();
    }

    @Override
    public void setBackground(int r, int g, int b, int a) {
        super.setBackground(r, g, b, a);
        getState(ItemStateType.BASE).background = getBackground();
        updateState();
    }

    @Override
    public void setBackground(float r, float g, float b) {
        super.setBackground(r, g, b);
        getState(ItemStateType.BASE).background = getBackground();
        updateState();
    }

    @Override
    public void setBackground(float r, float g, float b, float a) {
        super.setBackground(r, g, b, a);
        getState(ItemStateType.BASE).background = getBackground();
        updateState();
    }

    // common properties
    private List<InputEventType> _pass_events = new LinkedList<>();

    protected boolean isPassEvents() {
        if (_pass_events.size() == 0)
            return true;
        return false;
    }

    protected List<InputEventType> getPassEvents() {
        List<InputEventType> result = Arrays.asList(InputEventType.values());
        return result.stream().filter(e -> !_pass_events.contains(e)).collect(Collectors.toList());
    }

    protected List<InputEventType> getNonPassEvents() {
        return _pass_events;
    }

    protected void setPassEvents(boolean value) {
        if (!value) {
            for (InputEventType e : Arrays.asList(InputEventType.values())) {
                _pass_events.add(e);
            }
        } else {
            _pass_events.clear();
        }
    }

    protected void setPassEvents(boolean value, InputEventType e) {
        if (!value) {
            if (!_pass_events.contains(e))
                _pass_events.add(e);
        } else {
            if (_pass_events.contains(e))
                _pass_events.remove(e);
        }
    }

    protected void setPassEvents(boolean value, List<InputEventType> event_set) {
        if (!value) {
            _pass_events = event_set;
        } else {
            for (InputEventType e : event_set) {
                if (_pass_events.contains(e))
                    _pass_events.remove(e);
            }
        }
    }

    private boolean _disabled;

    protected boolean isDisabled() {
        return _disabled;
    }

    protected void setDisabled(boolean value) {
        if (_disabled == value)
            return;
        _disabled = value;
        updateState();
    }

    private boolean _hover;

    protected boolean isMouseHover() {
        return _hover;
    }

    protected void setMouseHover(boolean value) {
        if (_hover == value)
            return;
        _hover = value;
        updateState();
    }

    private boolean _pressed;

    protected boolean isMousePressed() {
        return _pressed;
    }

    protected void setMousePressed(boolean value) {
        if (_pressed == value)
            return;
        _pressed = value;
        updateState();
    }

    private boolean _focused;
    protected boolean isFocusable = true;

    protected boolean isFocused() {
        return _focused;
    }

    protected void setFocused(boolean value) {
        if (_focused == value)
            return;
        _focused = value;
        updateState();
    }

    @Override
    protected void updateInnersDrawable(boolean value) {
        for (InterfaceBaseItem item : _content) {
            item.setVisible(value);
        }
    }

    protected void updateState() {
        ItemState s_base = getState(ItemStateType.BASE);
        ItemState current = getState(_state);
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

        ItemState s_disabled = getState(ItemStateType.DISABLED);
        if (isDisabled() && s_disabled != null) {
            updateVisualProperties(s_disabled, s_base);
            return;
        }
        ItemState s_focused = getState(ItemStateType.FOCUSED);
        if (isFocused() && s_focused != null) {
            updateVisualProperties(s_focused, s_base);
            s_base = s_focused;
        }
        ItemState s_hover = getState(ItemStateType.HOVERED);
        if (isMouseHover() && s_hover != null) {
            updateVisualProperties(s_hover, s_base);
            s_base = s_hover;
        }
        ItemState s_pressed = getState(ItemStateType.PRESSED);
        if (isMousePressed() && s_pressed != null) {
            updateVisualProperties(s_pressed, s_base);
            s_base = s_pressed;
        }
    }

    private void updateVisualProperties(ItemState state, ItemState prev_state) {
        ItemState current = getState(_state);
        super.setBackground(GraphicsMathService.mixColors(current.background, state.background));
        _border = cloneBorder(state.border);

        if (_border.getRadius() == null)
            _border.setRadius(prev_state.border.getRadius());
        if (_border.getRadius() == null)
            _border.setRadius(getState(ItemStateType.BASE).border.getRadius());

        if (_border.getThickness() < 0)
            _border.setThickness(prev_state.border.getThickness());
        if (_border.getThickness() < 0)
            _border.setThickness(getState(ItemStateType.BASE).border.getThickness());

        if (_border.getFill().getAlpha() == 0)
            _border.setFill(prev_state.border.getFill());
        if (_border.getFill().getAlpha() == 0)
            _border.setFill(getState(ItemStateType.BASE).border.getFill());

        if (state.shape != null)
            setCustomFigure(state.shape);
    }

    private Border cloneBorder(Border border) {
        Border clone = new Border();
        clone.setFill(border.getFill());
        clone.setRadius(border.getRadius());
        clone.setThickness(border.getThickness());
        return clone;
    }

    protected boolean getHoverVerification(float xpos, float ypos) {
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

    private CustomFigure _is_custom = null;

    protected CustomFigure isCustomFigure() {
        return _is_custom;
    }

    protected void setCustomFigure(CustomFigure figure) {
        _is_custom = figure;
    }

    @Override
    public List<float[]> makeShape() {
        if (isCustomFigure() != null) {
            setTriangles(isCustomFigure().getFigure());
            if (getState(ItemStateType.BASE).shape == null)
                getState(ItemStateType.BASE).shape = isCustomFigure();

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
        setPadding(style.padding);
        setSpacing(style.spacing);
        setMargin(style.margin);
        setVisible(style.isVisible);
        removeAllItemStates();

        ItemState core_state = new ItemState(style.background);
        core_state.border.setRadius(style.borderRadius);
        core_state.border.setThickness(style.borderThickness);
        core_state.border.setFill(style.borderFill);

        for (Map.Entry<ItemStateType, ItemState> state : style.getAllStates().entrySet()) {
            addItemState(state.getKey(), state.getValue());
        }
        if (style.shape != null) {
            setCustomFigure(new CustomFigure(style.isFixedShape, style.shape));
            core_state.shape = isCustomFigure();
        }
        addItemState(ItemStateType.BASE, core_state);

        setBorderRadius(style.borderRadius);
        setBorderThickness(style.borderThickness);
        setBorderFill(style.borderFill);
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
        style.padding = new Indents(getPadding().left, getPadding().top, getPadding().right, getPadding().bottom);
        style.margin = new Indents(getMargin().left, getMargin().top, getMargin().right, getMargin().bottom);
        style.spacing = new Spacing(getSpacing().horizontal, getSpacing().vertical);
        style.alignment = getAlignment();
        style.borderFill = _border.getFill();
        style.borderRadius = _border.getRadius();
        style.borderThickness = _border.getThickness();
        style.isVisible = isVisible();
        if (isCustomFigure() != null) {
            style.shape = isCustomFigure().getFigure();
            style.isFixedShape = isCustomFigure().isFixed();
        }
        for (Map.Entry<ItemStateType, ItemState> state : states.entrySet()) {
            style.addItemState(state.getKey(), state.getValue());
        }

        return style;
    }
}