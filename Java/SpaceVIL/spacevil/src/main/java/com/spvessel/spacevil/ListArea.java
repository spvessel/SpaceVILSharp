package com.spvessel.spacevil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Style;

/**
 * ListArea is a scrollable container for other elements with ability of
 * selection. ListArea is part of com.spvessel.spacevil.ListBox which controls
 * scrolling, resizing and etc.
 * <p>
 * Supports all events except drag and drop.
 */
public class ListArea extends Prototype implements InterfaceVLayout {

    /**
     * Event that is invoked when one of the element is selected.
     */
    public EventCommonMethod selectionChanged = new EventCommonMethod();

    /**
     * Event that is invoked when one of the set of elements is changed.
     */
    public EventCommonMethod itemListChanged = new EventCommonMethod();

    /**
     * Disposing ListArea resources if it was removed.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void release() {
        selectionChanged.clear();
        itemListChanged.clear();
    }

    private int _step = 30;

    /**
     * Setting scroll movement step.
     * 
     * @param value Scroll step.
     */
    public void setStep(int value) {
        _step = value;
    }

    /**
     * Getting scroll movement step.
     * 
     * @return Scroll step.
     */
    public int getStep() {
        return _step;
    }

    private int _selection = -1;

    /**
     * Getting index of selected item.
     * 
     * @return Index of selected item.
     */
    public int getSelection() {
        return _selection;
    }

    private SelectionItem _selectionItem;

    /**
     * Getting selected item.
     * 
     * @return Selected item as com.spvessel.spacevil.Core.InterfaceBaseItem.
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
     * Select item by index.
     * 
     * @param index Index of selection.
     */
    public void setSelection(int index) {
        if (!_isSelectionVisible)
            return;
        _selection = index;
        _selectionItem = ((SelectionItem) getItems().get(index));
        _selectionItem.setSelected(true);
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
                ((SelectionItem) item).setSelected(false);
            }
        }
    }

    /**
     * Unselect selected item.
     */
    public void unselect() {
        _selection = -1;
        if (_selectionItem != null) {
            _selectionItem.setSelected(false);
            _selectionItem = null;
        }
    }

    private boolean _isSelectionVisible = true;

    /**
     * Enable or disable selection ability of ListArea.
     * 
     * @param value True: if you want selection ability of ListArea to be enabled.
     *              False: if you want selection ability of ListArea to be disabled.
     */
    public void setSelectionVisible(boolean value) {
        _isSelectionVisible = value;
        if (!_isSelectionVisible)
            unselect();
        for (SelectionItem item : _mapContent.values()) {
            item.setToggleVisible(_isSelectionVisible);
        }
    }

    /**
     * Returns True if selection ability of ListArea is enabled otherwise returns
     * False.
     * 
     * @return True: selection ability of ListArea is enabled. False: selection
     *         ability of ListArea is disabled.
     */
    public boolean isSelectionVisible() {
        return _isSelectionVisible;
    }

    private static int count = 0;

    /**
     * Default ListArea constructor.
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
        List<InterfaceBaseItem> list = getItems();

        switch (args.key) {
            case UP:
                while (index > 0) {
                    index--;
                    _selectionItem = ((SelectionItem) list.get(index));
                    if (_selectionItem.isVisible()) {
                        changed = true;
                        break;
                    }
                }
                if (changed)
                    setSelection(index);
                break;
            case DOWN:
                while (index < list.size() - 1) {
                    index++;
                    _selectionItem = ((SelectionItem) list.get(index));
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
     * Insert item into the ListArea by index.
     * 
     * @param item  Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @param index Index of insertion.
     */
    @Override
    public void insertItem(InterfaceBaseItem item, int index) {
        SelectionItem wrapper = getWrapper(item);
        super.insertItem(wrapper, index);
        wrapper.updateHeight();
        _mapContent.put(item, wrapper);
        updateLayout();

        if (index <= _selection)
            setSelection(_selection + 1);
    }

    /**
     * Add item to the ListArea.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        SelectionItem wrapper = getWrapper(item);
        super.addItem(wrapper);
        wrapper.updateHeight();
        _mapContent.put(item, wrapper);
        updateLayout();
    }

    /**
     * Adding all elements in the ListArea from the given list.
     * 
     * @param content List of items as
     *                List&lt;com.spvessel.spacevil.Core.InterfaceBaseItem&gt;
     */
    public void setListContent(List<InterfaceBaseItem> content) {
        removeAllItems();
        for (InterfaceBaseItem item : content) {
            SelectionItem wrapper = getWrapper(item);
            super.addItem(wrapper);
            wrapper.updateSize();
            _mapContent.put(item, wrapper);
        }
    }

    /**
     * Removing the specified item from the ListArea.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    @Override
    public boolean removeItem(InterfaceBaseItem item) {
        boolean restore = false;
        SelectionItem currentSelection = null;

        boolean b;
        if (item instanceof SelectionItem) {
            SelectionItem tmp = (SelectionItem) item;
            if (getTrueSelection() != null) {
                currentSelection = getTrueSelection();
                restore = !currentSelection.equals(tmp);
            }
            unselect();
            _mapContent.remove(tmp.getContent());
            tmp.clearContent();
            b = super.removeItem(tmp);
        } else {
            SelectionItem tmp = _mapContent.get(item);
            if (getTrueSelection() != null) {
                currentSelection = getTrueSelection();
                restore = !currentSelection.getContent().equals(item);
            }
            unselect();
            _mapContent.remove(item);
            tmp.clearContent();
            b = super.removeItem(tmp);
        }
        updateLayout();

        if (restore)
            setSelection(getItems().indexOf(currentSelection));

        itemListChanged.execute();
        return b;
    }

    /**
     * Removing all items from the ListArea.
     */
    @Override
    public void clear() {
        removeAllItems();
        updateLayout();
        itemListChanged.execute();
    }

    private void removeAllItems() {
        unselect();
        List<InterfaceBaseItem> list = getItems();

        if (list == null || list.size() == 0)
            return;

        while (!list.isEmpty()) {
            ((SelectionItem) list.get(0)).clearContent();
            super.removeItem(list.get(0));
            list.remove(0);
        }
        _mapContent.clear();
    }

    /**
     * Setting Y coordinate of the left-top corner of the ListArea.
     * 
     * @param y Y position of the left-top corner.
     */
    @Override
    public void setY(int y) {
        super.setY(y);
        updateLayout();
    }

    // update content position
    private long _yOffset = 0;
    private long _xOffset = 0;

    /**
     * Getting vertical scroll offset in the ListArea.
     * 
     * @return Vertical scroll offset.
     */
    public long getVScrollOffset() {
        return _yOffset;
    }

    /**
     * Setting vertical scroll offset of the ListArea.
     * 
     * @param value Vertical scroll offset.
     */
    public void setVScrollOffset(long value) {
        _yOffset = value;
        updateLayout();
    }

    /**
     * Getting horizontal scroll offset in the ListArea.
     * 
     * @return Horizontal scroll offset.
     */
    public long getHScrollOffset() {
        return _xOffset;
    }

    /**
     * Setting horizontal scroll offset of the ListArea.
     * 
     * @param value Horizontal scroll offset.
     */
    public void setHScrollOffset(long value) {
        _xOffset = value;
        updateLayout();
    }

    private boolean _isUpdating = false;

    /**
     * Updating all children positions (implementation of
     * com.spvessel.spacevil.Core.InterfaceVLayout).
     */
    public void updateLayout() {
        List<InterfaceBaseItem> list = getItems();
        if (list == null || list.size() == 0 || _isUpdating)
            return;
        _isUpdating = true;

        long offset = (-1) * getVScrollOffset();
        int startY = getY() + getPadding().top;
        int child_X = (-1) * (int) _xOffset + getX() + getPadding().left;
        for (InterfaceBaseItem child : list) {
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
     * Setting style of the ListArea.
     * <p>
     * Inner styles: "selection".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style innerStyle = style.getInnerStyle("selection");
        if (innerStyle != null) {
            _selectedStyle = innerStyle.clone();
            List<InterfaceBaseItem> list = getItems();
            for (InterfaceBaseItem item : list) {
                item.setStyle(_selectedStyle);
            }
        }
    }
}