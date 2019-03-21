package com.spvessel.spacevil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Style;

public class ListArea extends Prototype implements InterfaceVLayout {
    private Lock _lock = new ReentrantLock();

    public EventCommonMethod selectionChanged = new EventCommonMethod();
    public EventCommonMethod itemListChanged = new EventCommonMethod();

    @Override
    public void release() {
        selectionChanged.clear();
        itemListChanged.clear();
    }

    private int _step = 30;

    /**
     * ScrollBar moving step
     */
    public void setStep(int value) {
        _step = value;
    }

    public int getStep() {
        return _step;
    }

    private int _selection = -1;

    /**
     * @return Number of the selected item
     */
    public int getSelection() {
        return _selection;
    }

    private SelectionItem _selectionItem;

    /**
     * @return selected item
     */
    public InterfaceBaseItem getSelectedItem() {
        if (_selectionItem != null)
            return _selectionItem.getContent();
        return null;
    }

    SelectionItem getTrueSelection() {
        return _selectionItem;
    }

    /**
     * Set selected item by index
     */
    public void setSelection(int index) {
        if (!_isSelectionVisible)
            return;
        _selection = index;
        _selectionItem = ((SelectionItem) getItems().get(index));
        _selectionItem.setToggled(true);
        unselectOthers(_selectionItem);
        if (_selectionItem.getContent() instanceof Prototype) {
            ((Prototype) _selectionItem.getContent()).setFocus();
        }
        selectionChanged.execute();
    }

    private void unselectOthers(SelectionItem sender) {
        List<InterfaceBaseItem> items = getItems();
        for (InterfaceBaseItem item : items) {
            if (!item.equals(sender)) {
                ((SelectionItem) item).setToggled(false);
            }
        }
    }

    /**
     * Unselect all items
     */
    public void unselect() {
        _selection = -1;
        if (_selectionItem != null) {
            _selectionItem.setToggled(false);
            _selectionItem = null;
        }
    }

    private boolean _isSelectionVisible = true;

    /**
     * Is selection changes view of the item or not
     */
    public void setSelectionVisible(boolean value) {
        _isSelectionVisible = value;
        if (!_isSelectionVisible)
            unselect();
        for (SelectionItem item : _mapContent.values()) {
            item.setToggleVisible(_isSelectionVisible);
        }
    }

    public boolean isSelectionVisible() {
        return _isSelectionVisible;
    }

    private static int count = 0;

    /**
     * Constructs a ListArea
     */
    public ListArea() {
        setItemName("ListArea_" + count);
        count++;
        eventMouseClick.add(this::onMouseClick);
        eventMouseDoubleClick.add(this::onMouseDoubleClick);
        eventMouseHover.add(this::onMouseHover);
        eventKeyPress.add(this::onKeyPress);
    }

    private void onMouseClick(InterfaceItem sender, MouseArgs args) {

    }

    private void onMouseDoubleClick(InterfaceItem sender, MouseArgs args) {

    }

    private void onMouseHover(InterfaceItem sender, MouseArgs args) {

    }

    private void onKeyPress(InterfaceItem sender, KeyArgs args) {
        int index = _selection;
        boolean changed = false;

        switch (args.key) {
        case UP:
            while (index > 0) {
                index--;
                _selectionItem = ((SelectionItem) getItems().get(index));
                if (_selectionItem.isVisible()) {
                    changed = true;
                    break;
                }
            }
            if (changed)
                setSelection(index);
            break;
        case DOWN:
            while (index < super.getItems().size() - 1) {
                index++;
                _selectionItem = ((SelectionItem) getItems().get(index));
                if (_selectionItem.isVisible()) {
                    changed = true;
                    break;
                }
            }
            if (changed)
                setSelection(index);
            break;
        case ESCAPE:
            unselect();
            break;
        default:
            break;
        }
    }

    /**
     * If something changes when mouse hovered
     */
    @Override
    public void setMouseHover(boolean value) {
        super.setMouseHover(value);
    }

    Map<InterfaceBaseItem, SelectionItem> _mapContent = new HashMap<>();

    private SelectionItem getWrapper(InterfaceBaseItem item) {
        SelectionItem wrapper = new SelectionItem(item);
        wrapper.setStyle(_selectedStyle);
        wrapper.setToggleVisible(_isSelectionVisible);
        wrapper.eventMouseClick.add((sender, args) -> {
            int index = 0;
            if (_mapContent.get(item) != null)
                _selectionItem = _mapContent.get(item);
            for (InterfaceBaseItem var : super.getItems()) {
                if (var.equals(_selectionItem)) {
                    _selection = index;
                    selectionChanged.execute();
                    return;
                }
                index++;
            }
        });
        return wrapper;
    }

    /**
     * Insert item into the ListArea by index
     */
    @Override
    public void insertItem(InterfaceBaseItem item, int index) {
        SelectionItem wrapper = getWrapper(item);
        super.insertItem(wrapper, index);
        wrapper.updateSize();
        _mapContent.put(item, wrapper);
        updateLayout();
    }

    /**
     * Add item to the ListArea
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        SelectionItem wrapper = getWrapper(item);
        super.addItem(wrapper);
        wrapper.updateSize();
        _mapContent.put(item, wrapper);
        updateLayout();
    }

    /**
     * Remove item from the ListArea
     */
    @Override
    public void removeItem(InterfaceBaseItem item) {
        unselect();
        if (item instanceof SelectionItem) {
            SelectionItem tmp = (SelectionItem) item;
            _mapContent.remove(tmp.getContent());
            tmp.clearContent();
            super.removeItem(tmp);
        } else {
            SelectionItem tmp = _mapContent.get(item);
            _mapContent.remove(item);
            tmp.clearContent();
            super.removeItem(tmp);
        }
        updateLayout();
        itemListChanged.execute();
    }

    @Override
    public void clear() {
        removeAllItems();
    }

    void removeAllItems() {
        _lock.lock();
        try {
            unselect();
            List<InterfaceBaseItem> list = new LinkedList<>(getItems());

            if (list == null || list.size() == 0)
                return;

            while (!list.isEmpty()) {
                ((SelectionItem) list.get(0)).clearContent();
                super.removeItem(list.get(0));
                list.remove(0);
            }
            _mapContent.clear();
            updateLayout();
            itemListChanged.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            _lock.unlock();
        }
    }

    /**
     * Set Y position of the ListArea
     */
    @Override
    public void setY(int _y) {
        super.setY(_y);
        updateLayout();
    }

    // update content position
    private long _yOffset = 0;
    private long _xOffset = 0;

    /**
     * Vertical scroll offset in the ListArea
     */
    public long getVScrollOffset() {
        return _yOffset;
    }

    public void setVScrollOffset(long value) {
        _yOffset = value;
        updateLayout();
    }

    /**
     * Horizontal scroll offset in the ListArea
     */
    public long getHScrollOffset() {
        return _xOffset;
    }

    public void setHScrollOffset(long value) {
        _xOffset = value;
        updateLayout();
    }

    private boolean _isUpdating = false;

    /**
     * Update all children and ListArea sizes and positions according to confines
     */
    public void updateLayout() {
        if (getItems().size() == 0 || _isUpdating)
            return;
        _isUpdating = true;

        long offset = (-1) * getVScrollOffset();
        int startY = getY() + getPadding().top;
        int child_X = (-1) * (int) _xOffset + getX() + getPadding().left;
        for (InterfaceBaseItem child : super.getItems()) {
            if (!child.isVisible())
                continue;

            child.setX(child_X + child.getMargin().left);

            long child_Y = startY + offset + child.getMargin().top;
            offset += child.getHeight() + getSpacing().vertical;
            child.setY((int) child_Y);
            child.setConfines();

            // top checking
            if (child_Y < startY) {
                if (child_Y + child.getHeight() <= startY)
                    child.setDrawable(false);
                else
                    child.setDrawable(true);
                continue;
            }
            // bottom checking
            if (child_Y + child.getHeight() + child.getMargin().bottom > getY() + getHeight() - getPadding().bottom) {
                if (child_Y >= getY() + getHeight() - getPadding().bottom)
                    child.setDrawable(false);
                else
                    child.setDrawable(true);
                continue;
            }
            child.setDrawable(true);
        }
        _isUpdating = false;
    }

    private Style _selectedStyle;

    /**
     * Set style of the ListArea
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("selection");
        if (inner_style != null) {
            _selectedStyle = inner_style.clone();
            List<InterfaceBaseItem> list = new LinkedList<>(getItems());
            for (InterfaceBaseItem item : list) {
                item.setStyle(_selectedStyle);
            }
        }
    }
}