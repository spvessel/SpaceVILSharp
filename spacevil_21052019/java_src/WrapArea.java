package com.spvessel.spacevil;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceGrid;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.KeyArgs;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.Orientation;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WrapArea extends Prototype implements InterfaceGrid {
    Map<InterfaceBaseItem, SelectionItem> _mapContent = new HashMap<>();
    private Lock _lock = new ReentrantLock();
    public EventCommonMethod selectionChanged = new EventCommonMethod();
    public EventCommonMethod itemListChanged = new EventCommonMethod();

    @Override
    public void release() {
        selectionChanged.clear();
        itemListChanged.clear();
    }

    int _rows = 0;
    int _columns = 0;

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

    private boolean _isStretch = false;

    public boolean isStretch() {
        return _isStretch;
    }

    public void setStretch(boolean value) {
        if (value == _isStretch)
            return;
        _isStretch = value;
        updateLayout();
    }

    private int _selection = -1;

    /**
     * @return Index of the selected item
     */
    public int getSelection() {
        return _selection;
    }

    private SelectionItem _selectionItem;

    /**
     * @return selected item
     */
    public InterfaceBaseItem getSelectionItem() {
        if (_selectionItem != null)
            return _selectionItem.getContent();
        return null;
    }

    SelectionItem getTrueSelection() {
        return _selectionItem;
    }

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

    static int count = 0;
    int _cellWidth = 0;
    int _cellHeight = 0;

    void setCellSize(int cellWidth, int cellHeight) {
        _cellWidth = cellWidth;
        _cellHeight = cellHeight;
        updateLayout();
    }

    /**
     * Constructs a ListArea
     */
    public WrapArea(int cellWidth, int cellHeight, Orientation orientation) {
        setItemName("WrapArea_" + count);
        count++;
        _orientation = orientation;
        _cellWidth = cellWidth;
        _cellHeight = cellHeight;
        eventMouseClick.add(this::onMouseClick);
        eventMouseDoubleClick.add(this::onMouseDoubleClick);
        eventMouseHover.add(this::onMouseHover);
        eventKeyPress.add(this::onKeyPress);
    }

    void onMouseClick(InterfaceItem sender, MouseArgs args) {
    }

    void onMouseDoubleClick(InterfaceItem sender, MouseArgs args) {
    }

    void onMouseHover(InterfaceItem sender, MouseArgs args) {
    }

    void onKeyPress(InterfaceItem sender, KeyArgs args) {
        int index = _selection;
        int x, y;
        if (_orientation == Orientation.HORIZONTAL) {
            x = _selection % _columns;
            y = _selection / _columns;
        } else {
            x = _selection / _rows;
            y = _selection % _rows;
        }
        List<InterfaceBaseItem> list = getItems();
        switch (args.key) {
        case UP:
            y--;
            if (y < 0)
                y = 0;
            index = getIndexByCoord(x, y);
            if (index != _selection)
                setSelection(index);
            break;
        case DOWN:
            y++;
            if (y >= _rows)
                y = _rows - 1;
            index = getIndexByCoord(x, y);
            if (index >= list.size())
                index = list.size() - 1;
            if (index != _selection)
                setSelection(index);
            break;

        case LEFT:
            x--;
            if (x < 0)
                x = 0;
            index = getIndexByCoord(x, y);
            if (index != _selection)
                setSelection(index);
            break;
        case RIGHT:
            x++;
            if (x >= _columns)
                x = _columns - 1;
            index = getIndexByCoord(x, y);
            if (index >= list.size())
                index = list.size() - 1;
            if (index != _selection)
                setSelection(index);
            break;
        case ESCAPE:
            unselect();
            break;
        default:
            break;
        }
    }

    private int getIndexByCoord(int x, int y) {
        if (_orientation == Orientation.HORIZONTAL) {
            return (x + y * _columns);
        } else {
            return (y + x * _rows);
        }
    }

    private SelectionItem getWrapper(InterfaceBaseItem item) {
        SelectionItem wrapper = new SelectionItem(item);
        wrapper.setStyle(_selectedStyle);
        wrapper.setToggleVisible(_isSelectionVisible);
        wrapper.setSize(_cellWidth, _cellHeight);
        wrapper.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        wrapper.eventMouseClick.add((sender, args) -> {
            int index = 0;
            _selectionItem = _mapContent.get(item);
            for (InterfaceBaseItem var : getItems()) {
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
        _mapContent.put(item, wrapper);
        updateLayout();

        if (index <= _selection)
            setSelection(_selection + 1);
    }

    /**
     * Add item to the ListArea
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        SelectionItem wrapper = getWrapper(item);
        super.addItem(wrapper);
        _mapContent.put(item, wrapper);
        updateLayout();
    }

    public void setListContent(List<InterfaceBaseItem> content)
    {
        removeAllItems();
        for (InterfaceBaseItem item : content) {
            SelectionItem wrapper = getWrapper(item);
            super.addItem(wrapper);
            _mapContent.put(item, wrapper);
        }
        updateLayout();
        itemListChanged.execute();
    }

    /**
     * Remove item from the ListArea
     */
    @Override
    public boolean removeItem(InterfaceBaseItem item) {
        boolean restore = false;
        SelectionItem currentSelection = null;

        unselect();
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

    @Override
    public void clear() {
        removeAllItems();
    }

    void removeAllItems() {
        _lock.lock();
        try {
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
            updateLayout();
            itemListChanged.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            _lock.unlock();
        }
    }

    /**
     * Set X position of the ListArea
     */
    @Override
    public void setX(int _x) {
        super.setX(_x);
        updateLayout();
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
    Orientation _orientation = Orientation.HORIZONTAL;
    private long _yOffset = 0;
    private long _xOffset = 0;

    /// <summary>
    /// Vertical scroll offset in the WrapArea
    /// </summary>
    public long getScrollOffset() {
        if (_orientation == Orientation.HORIZONTAL)
            return _yOffset;
        else
            return _xOffset;
    }

    public void setScrollOffset(long value) {
        if (_orientation == Orientation.HORIZONTAL)
            _yOffset = value;
        else
            _xOffset = value;
        updateLayout();
    }

    private boolean _isUpdating = false;

    /**
     * Update all children and ListArea sizes and positions according to confines
     */
    // Update Layout
    public void updateLayout() {
        List<InterfaceBaseItem> list = getItems();
        if (list == null || list.size() == 0 || _isUpdating)
            return;
        _isUpdating = true;

        long offset = (-1) * getScrollOffset();
        long x = getX() + getPadding().left;
        long y = getY() + getPadding().top;
        if (_orientation == Orientation.HORIZONTAL) {
            // update
            long globalY = y + offset;
            int w = getWidth() - getPadding().left - getPadding().right;
            int itemCount = (w + getSpacing().horizontal) / (_cellWidth + getSpacing().horizontal);
            int column = 0;
            int row = 0;
            _columns = (itemCount > list.size()) ? list.size() : itemCount;
            if (_columns == 0) {
                _columns = 1;
                itemCount = 1;
            }

            // stretch
            int xOffset = 0;
            if (_isStretch && itemCount < list.size()) {
                int freeSpace = w - ((_cellWidth + getSpacing().horizontal) * _columns) - getSpacing().horizontal;
                xOffset = freeSpace / _columns;
                if (_columns > 1)
                    xOffset = freeSpace / (_columns - 1);
            }

            for (InterfaceBaseItem item : list) {
                if (!item.isVisible())
                    continue;
                item.setSize(_cellWidth, _cellHeight);
                item.setX((int) (x + (_cellWidth + getSpacing().horizontal + xOffset) * column));
                int itemY = (int) (globalY + (_cellHeight + getSpacing().vertical) * row);
                item.setY(itemY);
                item.setConfines();
                column++;
                if (column == itemCount) {
                    column = 0;
                    row++;
                }
                // top check
                if (itemY < y) {
                    if (itemY + _cellHeight <= y)
                        item.setDrawable(false);
                    else
                        item.setDrawable(true);
                    continue;
                }
                // bottom check
                if (itemY + _cellHeight > getY() + getHeight() - getPadding().bottom) {
                    if (itemY >= getY() + getHeight() - getPadding().bottom)
                        item.setDrawable(false);
                    else
                        item.setDrawable(true);
                    continue;
                }
                item.setDrawable(true);
            }
            if (list.size() % itemCount == 0)
                row--;
            _rows = row + 1;

        } else if (_orientation == Orientation.VERTICAL) {
            // update
            long globalX = x + offset;
            int h = getHeight() - getPadding().top - getPadding().bottom;
            int itemCount = (h + getSpacing().vertical) / (_cellHeight + getSpacing().vertical);
            int column = 0;
            int row = 0;
            _rows = (itemCount > list.size()) ? list.size() : itemCount;
            if (_rows == 0) {
                _rows = 1;
                itemCount = 1;
            }

            // stretch
            int yOffset = 0;
            if (_isStretch && itemCount < list.size()) {
                int freeSpace = h - ((_cellHeight + getSpacing().vertical + yOffset) * _rows) - getSpacing().vertical;
                yOffset = freeSpace / _rows;
                if (_rows > 1)
                    yOffset = freeSpace / (_rows - 1);
            }

            for (InterfaceBaseItem item : list) {
                if (!item.isVisible())
                    continue;
                item.setSize(_cellWidth, _cellHeight);
                item.setY((int) (y + (_cellHeight + getSpacing().vertical + yOffset) * row));

                int itemX = (int) (globalX + (_cellWidth + getSpacing().horizontal) * column);
                item.setX(itemX);
                item.setConfines();
                row++;
                if (row == itemCount) {
                    row = 0;
                    column++;
                }
                // left check
                if (itemX < x) {
                    if (itemX + _cellWidth <= x)
                        item.setDrawable(false);
                    else
                        item.setDrawable(true);
                    continue;
                }
                // right check
                if (itemX + _cellWidth > getX() + getWidth() - getPadding().left) {
                    if (itemX >= getX() + getWidth() - getPadding().left)
                        item.setDrawable(false);
                    else
                        item.setDrawable(true);
                    continue;
                }
                item.setDrawable(true);
            }
            if (list.size() % itemCount == 0)
                column--;
            _columns = column + 1;
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
            List<InterfaceBaseItem> list = getItems();
            for (InterfaceBaseItem item : list) {
                item.setStyle(_selectedStyle);
            }
        }
    }
}
