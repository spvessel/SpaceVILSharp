package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.*;
import com.spvessel.spacevil.Flags.*;

import java.awt.Color;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;

final class VisualItem extends BaseItem {

    private Lock locker = new ReentrantLock();

    Prototype prototype;

    VisualItem() {
        this("VisualItem_");
    }

    VisualItem(String name) {
        ItemState base = new ItemState();
        base.background = getBackground();
        states.put(ItemStateType.Base, base);

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
            BaseItemStatics.updateHLayout(this);
            eventManager.notifyListeners(GeometryEventType.ResizeWidth, value);
        }
    }

    @Override
    public void setHeight(int height) {
        int value = height - getHeight();
        if (value != 0) {
            super.setHeight(height);
            BaseItemStatics.updateVLayout(this);
            eventManager.notifyListeners(GeometryEventType.ResizeHeight, value);
        }
    }

    void setPosition(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public void setX(int x) {
        int value = x - getX();
        if (value != 0) {
            super.setX(x);
            eventManager.notifyListeners(GeometryEventType.MovedX, value);
        }
    }

    @Override
    public void setY(int y) {
        int value = y - getY();
        if (value != 0) {
            super.setY(y);
            eventManager.notifyListeners(GeometryEventType.MovedY, value);
        }
    }

    // item
    Border border = new Border();

    void setBorderDirect(Border border) {
        this.border = border;
    }

    Border getBorder() {
        return border;
    }

    void setBorder(Border border) {
        this.border = border;
        getState(ItemStateType.Base).border = border;
        updateState();
    }

    void setBorderFill(Color color) {
        border.setColor(color);
        getState(ItemStateType.Base).border.setColor(color);
        updateState();
    }

    void setBorderFill(int r, int g, int b) {
        setBorderFill(GraphicsMathService.colorTransform(r, g, b));
    }

    void setBorderFill(int r, int g, int b, int a) {
        setBorderFill(GraphicsMathService.colorTransform(r, g, b, a));
    }

    void setBorderFill(float r, float g, float b) {
        setBorderFill(GraphicsMathService.colorTransform(r, g, b));
    }

    void setBorderFill(float r, float g, float b, float a) {
        setBorderFill(GraphicsMathService.colorTransform(r, g, b, a));
    }

    void setBorderRadius(CornerRadius radius) {
        border.setRadius(radius);
        getState(ItemStateType.Base).border.setRadius(radius);
        updateState();
    }

    void setBorderThickness(int thickness) {
        border.setThickness(thickness);
        getState(ItemStateType.Base).border.setThickness(thickness);
        updateState();
    }

    CornerRadius getBorderRadius() {
        return border.getRadius();
    }

    int getBorderThickness() {
        return border.getThickness();
    }

    Color getBorderFill() {
        return border.getColor();
    }

    void SetBorder(Color color, CornerRadius radius, int thickness) {
        border.setColor(color);
        border.setRadius(radius);
        border.setThickness(thickness);
        getState(ItemStateType.Base).border = border;
        updateState();
    }

    Map<ItemStateType, ItemState> states = new HashMap<>();
    ItemStateType _state = ItemStateType.Base;

    void setState(ItemStateType state) {
        _state = state;
        updateState();
    }

    ItemStateType getCurrentStateType() {
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
    private Spacing _spacing = new Spacing();

    Spacing getSpacing() {
        return _spacing;
    }

    void setSpacing(Spacing spacing) {
        _spacing = spacing;
        updateGeometry();
        BaseItemStatics.updateAllLayout(this);
    }

    void setSpacing(int horizontal, int vertical) {
        setSpacing(new Spacing(horizontal, vertical));
    }

    private Indents _padding = new Indents();

    Indents getPadding() {
        return _padding;
    }

    void setPadding(Indents padding) {
        _padding = padding;
        updateGeometry();
        BaseItemStatics.updateAllLayout(this);
    }

    void setPadding(int left, int top, int right, int bottom) {
        setPadding(new Indents(left, top, right, bottom));
    }

    EventManager eventManager = null;
    private Set<IBaseItem> _content = new LinkedHashSet<>();

    List<IBaseItem> getItems() {
        locker.lock();
        try {
            return new LinkedList<IBaseItem>(_content);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            locker.unlock();
        }
    }

    void setContent(List<IBaseItem> content) {
        locker.lock();
        try {
            _content = new LinkedHashSet<>(content);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            locker.unlock();
        }
    }

    private void castAndRemove(IBaseItem item) {
        if (item instanceof Prototype) {
            Prototype prototype = ((Prototype) item);
            prototype.getCore().removeItemFromListeners();
            prototype.freeEvents();
        } else {
            ((BaseItem) item).removeItemFromListeners();
        }
    }

    @Override
    void removeItemFromListeners() {
        Prototype parent = getParent();
        parent.removeEventListener(GeometryEventType.ResizeWidth, this.prototype);
        parent.removeEventListener(GeometryEventType.ResizeHeight, this.prototype);
        parent.removeEventListener(GeometryEventType.MovedX, this.prototype);
        parent.removeEventListener(GeometryEventType.MovedY, this.prototype);
    }

    void addItem(IBaseItem item) {
        locker.lock();
        try {
            if (item == null) {
                System.out.println("Trying to add null item");
                return;
            }
            if (item.equals(this)) {
                System.out.println("Trying to add current item in himself.");
                return;
            }
            item.setHandler(getHandler());
            addChildren(item);
            _content.add(item);
            ItemsLayoutBox.addItem(getHandler(), item, LayoutType.Static);
            // needs to force update all attributes
            BaseItemStatics.castToUpdateGeometry(item);
            item.initElements();
        } catch (Exception ex) {
            System.out.println("Method - AddItem: " + ((item == null) ? "item is null" : item.getItemName()));
            ex.printStackTrace();
        } finally {
            locker.unlock();
        }
    }

    void insertItem(IBaseItem item, int index) {
        locker.lock();
        try {
            if (item == null) {
                System.out.println("Trying to insert null item");
                return;
            }
            if (index < 0) {
                System.out.println("Invalid index");
                return;
            }
            if (item.equals(this)) {
                System.out.println("Trying to add current item in himself.");
                return;
            }
            item.setHandler(getHandler());

            addChildren(item);

            if (index > _content.size())
                _content.add(item);
            else {
                List<IBaseItem> list = new LinkedList<>(_content);
                list.add(index, item);
                _content = new LinkedHashSet<>(list);
            }

            try {
                ItemsLayoutBox.addItem(getHandler(), item, LayoutType.Static);
            } catch (Exception ex) {
                System.out.println(item.getItemName());
                throw ex;
            }

            // needs to force update all attributes
            BaseItemStatics.castToUpdateGeometry(item);
            item.initElements();

            // if (item instanceof VisualItem) {
            // ((VisualItem) item).updateState();
            // }
        } catch (Exception ex) {
            System.out.println("Method - InsertItem: " + ((item == null) ? "item is null" : item.getItemName()));
            ex.printStackTrace();
        } finally {
            locker.unlock();
        }
    }

    void cascadeRemoving(IBaseItem item, LayoutType type) {
        if (item instanceof Prototype)// и если это действительно контейнер
        {
            Prototype container = (Prototype) item;// предполагаю что элемент контейнер
            List<IBaseItem> tmp = container.getItems();
            while (tmp.size() > 0) {
                IBaseItem child = tmp.get(0);
                container.removeItem(child);
                tmp.remove(child);
            }
        }
    }

    boolean removeItem(IBaseItem item) {
        locker.lock();
        try {
            if (!_content.contains(item))
                return false;
            if (item instanceof Prototype) {
                Prototype tmp = ((Prototype) item);
                if (tmp.isFocused())
                    getHandler().resetItems();
            }

            LayoutType type;
            if (item instanceof IFloating) {
                cascadeRemoving(item, LayoutType.Floating);
                type = LayoutType.Floating;
            } else {
                cascadeRemoving(item, LayoutType.Static);
                type = LayoutType.Static;
            }

            // removing
            castAndRemove(item);
            boolean contentRemove = _content.remove(item);
            boolean layoutBoxRemove = ItemsLayoutBox.removeItem(getHandler(), item, type);
            item.setParent(null);
            item.release();
            return (contentRemove && layoutBoxRemove);
        } catch (Exception ex) {
            System.out.println("Method - RemoveItem: " + ((item == null) ? "item is null" : item.getItemName()));
            ex.printStackTrace();
            return false;
        } finally {
            locker.unlock();
        }
    }

    void clear() {
        locker.lock();
        try {
            while (!_content.isEmpty()) {
                removeItem(_content.iterator().next());
            }
        } catch (Exception ex) {
            System.out.println("Method - Clear");
            ex.printStackTrace();
        } finally {
            locker.unlock();
        }
    }

    @Override
    void addEventListener(GeometryEventType type, IBaseItem listener) {
        eventManager.subscribe(type, listener);
    }

    @Override
    void removeEventListener(GeometryEventType type, IBaseItem listener) {
        eventManager.unsubscribe(type, listener);
    }

    void addItemState(ItemStateType type, ItemState state) {
        if (states.containsKey(type)) {
            state.value = true;
            states.replace(type, state);
        } else {
            states.put(type, state);
        }
    }

    ItemState getState(ItemStateType type) {
        if (states.containsKey(type))
            return states.get(type);
        return null;
    }

    Map<ItemStateType, ItemState> getAllStates() {
        return states;
    }

    void removeItemState(ItemStateType type) {
        if (type == ItemStateType.Base)
            return;
        if (states.containsKey(type))
            states.remove(type);
    }

    void removeAllItemStates() {
        List<ItemStateType> itemsToRemove = states.entrySet().stream().filter(i -> i.getKey() != ItemStateType.Base)
                .map(Map.Entry::getKey).collect(Collectors.toList());
        for (ItemStateType item : itemsToRemove)
            states.remove(item);
    }

    final void setBackgroundDirect(Color color) {
        super.setBackground(color);
    }

    @Override
    public void setBackground(Color color) {
        getState(ItemStateType.Base).background = color;
        updateState();
    }

    @Override
    public void setBackground(int r, int g, int b) {
        super.setBackground(r, g, b);
        getState(ItemStateType.Base).background = getBackground();
        updateState();
    }

    @Override
    public void setBackground(int r, int g, int b, int a) {
        super.setBackground(r, g, b, a);
        getState(ItemStateType.Base).background = getBackground();
        updateState();
    }

    @Override
    public void setBackground(float r, float g, float b) {
        super.setBackground(r, g, b);
        getState(ItemStateType.Base).background = getBackground();
        updateState();
    }

    @Override
    public void setBackground(float r, float g, float b, float a) {
        super.setBackground(r, g, b, a);
        getState(ItemStateType.Base).background = getBackground();
        updateState();
    }

    // common properties
    private List<InputEventType> _blockedEvents = new LinkedList<>();

    boolean isPassEvents() {
        return (_blockedEvents.size() == 0);
    }

    List<InputEventType> getPassEvents() {
        List<InputEventType> result = Arrays.asList(InputEventType.values());
        return result.stream().filter(e -> !_blockedEvents.contains(e)).collect(Collectors.toList());
    }

    List<InputEventType> getBlockedEvents() {
        return _blockedEvents;
    }

    void setPassEvents(boolean value) {
        if (!value) {
            for (InputEventType e : Arrays.asList(InputEventType.values())) {
                _blockedEvents.add(e);
            }
        } else {
            _blockedEvents.clear();
        }
    }

    void setPassEvents(boolean value, InputEventType e) {
        if (!value) {
            if (!_blockedEvents.contains(e))
                _blockedEvents.add(e);
        } else {
            if (_blockedEvents.contains(e))
                _blockedEvents.remove(e);
        }
    }

    void setPassEvents(boolean value, List<InputEventType> event_set) {
        if (!value) {
            _blockedEvents = event_set;
        } else {
            for (InputEventType e : event_set) {
                if (_blockedEvents.contains(e))
                    _blockedEvents.remove(e);
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
    // boolean isFocusable = true;

    boolean isFocused() {
        return _focused;
    }

    void setFocused(boolean value) {
        if (_focused == value)
            return;
        _focused = value;
        updateState();
    }

    // private boolean _focusable = true;

    // public boolean isFocusable() {
    // return _focusable;
    // }

    // public void setFocusable() {
    // // foreach inner item focusable value set?
    // }

    void updateState() {
        VisualItemStatics.updateState(this);
    }

    boolean getHoverVerification(float xpos, float ypos) {
        return VisualItemStatics.getHoverVerification(this, xpos, ypos);
    }

    private Figure _is_custom = null;

    final Figure isCustomFigure() {
        return _is_custom;
    }

    void setCustomFigure(Figure figure) {
        _is_custom = figure;
        ItemsRefreshManager.setRefreshShape(this.prototype);
    }

    @Override
    public void makeShape() {
        if (isCustomFigure() != null) {
            setTriangles(isCustomFigure().getFigure());
            if (getState(ItemStateType.Base).shape == null)
                getState(ItemStateType.Base).shape = isCustomFigure();

            if (!isCustomFigure().isFixed())
                setTriangles(updateShape());
        } else {
            setTriangles(GraphicsMathService.getRoundSquare(getBorderRadius(), getWidth(), getHeight(), 0, 0));
        }
    }

    @Override
    public void setStyle(Style style) {
        VisualItemStatics.setStyle(this, style);
    }

    @Override
    public Style getCoreStyle() {
        return VisualItemStatics.extractCoreStyle(this);
    }
}