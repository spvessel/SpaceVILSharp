package com.spvessel.spacevil;

import java.util.Map;

import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.IFreeLayout;
import com.spvessel.spacevil.Core.IItem;
import com.spvessel.spacevil.Core.KeyArgs;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.Orientation;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.HashMap;
import java.util.List;

/**
 * WrapArea is a scrollable container for other elements with ability of
 * selection. WrapArea groups elements in cells of a certain size. It can be
 * oriented vertically or horizontally. WrapArea is part of
 * com.spvessel.spacevil.WrapGrid which controls scrolling, resizing and etc.
 * <p>
 * Supports all events except drag and drop.
 */
public class WrapArea extends Prototype implements IFreeLayout {
    Map<IBaseItem, SelectionItem> _mapContent = new HashMap<>();

    /**
     * Event that is invoked when one of the element is selected.
     */
    public EventCommonMethod selectionChanged = new EventCommonMethod();

    /**
     * Event that is invoked when one of the set of elements is changed.
     */
    public EventCommonMethod itemListChanged = new EventCommonMethod();

    /**
     * Disposing WrapArea resources if it was removed.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void release() {
        selectionChanged.clear();
        itemListChanged.clear();
    }

    int rows = 0;
    int columns = 0;

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

    private boolean _isStretch = false;

    /**
     * Returns True if WrapArea allocates all available space between cells to
     * achieve smooth streching, otherwise returns False.
     * 
     * @return True: if WrapArea allocates all available space between cells. False:
     *         if space between cells is fixed.
     */
    public boolean isStretch() {
        return _isStretch;
    }

    /**
     * Setting strech mode for WrapArea. WrapArea can allocates all available space
     * between cells or uses fixed space between cells.
     * 
     * @param value True: if you want to WrapArea allocates all available space
     *              between cells. False: if you want space between cells to be
     *              fixed.
     */
    public void setStretch(boolean value) {
        if (value == _isStretch)
            return;
        _isStretch = value;
        updateLayout();
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
     * @return Selected item as com.spvessel.spacevil.Core.IBaseItem
     */
    public IBaseItem getSelectedItem() {
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
        List<IBaseItem> items = getItems();
        for (IBaseItem item : items) {
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
     * Enable or disable selection ability of WrapArea.
     * 
     * @param value True: if you want selection ability of WrapArea to be enabled.
     *              False: if you want selection ability of WrapArea to be disabled.
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
     * Returns True if selection ability of WrapArea is enabled otherwise returns
     * False.
     * 
     * @return True: selection ability of WrapArea is enabled. False: selection
     *         ability of WrapArea is disabled.
     */
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
     * Constructs a WrapGrid with specified cell width, height and orientation.
     * 
     * @param cellWidth   Width of each cell.
     * @param cellHeight  Height of each cell.
     * @param orientation Orientation of layout as
     *                    com.spvessel.spacevil.Flags.Orientation.
     */
    public WrapArea(int cellWidth, int cellHeight, Orientation orientation) {
        setItemName("WrapArea_" + count);
        count++;
        this.orientation = orientation;
        _cellWidth = cellWidth;
        _cellHeight = cellHeight;
        eventMouseClick.add(this::onMouseClick);
        eventMouseDoubleClick.add(this::onMouseDoubleClick);
        eventMouseHover.add(this::onMouseHover);
        eventKeyPress.add(this::onKeyPress);
    }

    void onMouseClick(IItem sender, MouseArgs args) {
    }

    void onMouseDoubleClick(IItem sender, MouseArgs args) {
    }

    void onMouseHover(IItem sender, MouseArgs args) {
    }

    void onKeyPress(IItem sender, KeyArgs args) {
        int index = _selection;
        int x, y;
        if (orientation == Orientation.Horizontal) {
            x = _selection % columns;
            y = _selection / columns;
        } else {
            x = _selection / rows;
            y = _selection % rows;
        }
        List<IBaseItem> list = getItems();
        switch (args.key) {
            case Up:
                y--;
                if (y < 0)
                    y = 0;
                index = getIndexByCoord(x, y);
                if (index != _selection)
                    setSelection(index);
                break;
            case Down:
                y++;
                if (y >= rows)
                    y = rows - 1;
                index = getIndexByCoord(x, y);
                if (index >= list.size())
                    index = list.size() - 1;
                if (index != _selection)
                    setSelection(index);
                break;

            case Left:
                x--;
                if (x < 0)
                    x = 0;
                index = getIndexByCoord(x, y);
                if (index != _selection)
                    setSelection(index);
                break;
            case Right:
                x++;
                if (x >= columns)
                    x = columns - 1;
                index = getIndexByCoord(x, y);
                if (index >= list.size())
                    index = list.size() - 1;
                if (index != _selection)
                    setSelection(index);
                break;
            case Escape:
                unselect();
                break;
            default:
                break;
        }
    }

    private int getIndexByCoord(int x, int y) {
        if (orientation == Orientation.Horizontal) {
            return (x + y * columns);
        } else {
            return (y + x * rows);
        }
    }

    private SelectionItem getWrapper(IBaseItem item) {
        SelectionItem wrapper = new SelectionItem(item);
        wrapper.setStyle(_selectedStyle);
        wrapper.setToggleVisible(_isSelectionVisible);
        wrapper.setSize(_cellWidth, _cellHeight);
        wrapper.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        wrapper.eventMouseClick.add((sender, args) -> {
            int index = 0;
            _selectionItem = _mapContent.get(item);
            for (IBaseItem var : getItems()) {
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
     * Insert item into the WrapArea by index.
     * 
     * @param item  Item as com.spvessel.spacevil.Core.IBaseItem.
     * @param index Index of insertion.
     */
    @Override
    public void insertItem(IBaseItem item, int index) {
        SelectionItem wrapper = getWrapper(item);
        super.insertItem(wrapper, index);
        _mapContent.put(item, wrapper);
        updateLayout();

        if (index <= _selection)
            setSelection(_selection + 1);
    }

    /**
     * Adding item to the WrapArea.
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     */
    @Override
    public void addItem(IBaseItem item) {
        SelectionItem wrapper = getWrapper(item);
        super.addItem(wrapper);
        _mapContent.put(item, wrapper);
        updateLayout();
    }

    /**
     * Adding all elements in the WrapArea from the given list.
     * 
     * @param content List of items as
     *                List&lt;com.spvessel.spacevil.Core.IBaseItem&gt;
     */
    public void setListContent(List<IBaseItem> content) {
        removeAllItems();
        for (IBaseItem item : content) {
            SelectionItem wrapper = getWrapper(item);
            super.addItem(wrapper);
            _mapContent.put(item, wrapper);
        }
        updateLayout();
        itemListChanged.execute();
    }

    /**
     * Removing the specified item from the WrapArea.
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    @Override
    public boolean removeItem(IBaseItem item) {
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

    /**
     * Removing all items from the WrapArea.
     */
    @Override
    public void clear() {
        removeAllItems();
        updateLayout();
        itemListChanged.execute();
    }

    private void removeAllItems() {
        unselect();
        List<IBaseItem> list = getItems();

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
     * Setting X coordinate of the left-top corner of the WrapArea.
     * 
     * @param x X position of the left-top corner.
     */
    @Override
    public void setX(int x) {
        super.setX(x);
        updateLayout();
    }

    /**
     * Setting Y coordinate of the left-top corner of the WrapArea.
     * 
     * @param y Y position of the left-top corner.
     */
    @Override
    public void setY(int y) {
        super.setY(y);
        updateLayout();
    }

    // update content position
    Orientation orientation = Orientation.Horizontal;
    private long _yOffset = 0;
    private long _xOffset = 0;

    /**
     * Getting scroll offset in the WrapArea.
     * 
     * @return Scroll offset.
     */
    public long getScrollOffset() {
        if (orientation == Orientation.Horizontal)
            return _yOffset;
        else
            return _xOffset;
    }

    /**
     * Setting scroll offset of the WrapArea.
     * 
     * @param value Scroll offset.
     */
    public void setScrollOffset(long value) {
        if (orientation == Orientation.Horizontal)
            _yOffset = value;
        else
            _xOffset = value;
        updateLayout();
    }

    private boolean _isUpdating = false;

    /**
     * Updating all children positions (implementation of
     * com.spvessel.spacevil.Core.IFreeLayout).
     */
    // Update Layout
    public void updateLayout() {
        List<IBaseItem> list = getItems();
        if (list == null || list.size() == 0 || _isUpdating)
            return;
        _isUpdating = true;

        long offset = (-1) * getScrollOffset();
        long x = getX() + getPadding().left;
        long y = getY() + getPadding().top;
        if (orientation == Orientation.Horizontal) {
            // update
            long globalY = y + offset;
            int w = getWidth() - getPadding().left - getPadding().right;
            int itemCount = (w + getSpacing().horizontal) / (_cellWidth + getSpacing().horizontal);
            int column = 0;
            int row = 0;
            columns = (itemCount > list.size()) ? list.size() : itemCount;
            if (columns == 0) {
                columns = 1;
                itemCount = 1;
            }

            // stretch
            int xOffset = 0;
            if (_isStretch && itemCount < list.size()) {
                int freeSpace = w - ((_cellWidth + getSpacing().horizontal) * columns) - getSpacing().horizontal;
                xOffset = freeSpace / columns;
                if (columns > 1)
                    xOffset = freeSpace / (columns - 1);
            }

            for (IBaseItem item : list) {
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
            rows = row + 1;

        } else if (orientation == Orientation.Vertical) {
            // update
            long globalX = x + offset;
            int h = getHeight() - getPadding().top - getPadding().bottom;
            int itemCount = (h + getSpacing().vertical) / (_cellHeight + getSpacing().vertical);
            int column = 0;
            int row = 0;
            rows = (itemCount > list.size()) ? list.size() : itemCount;
            if (rows == 0) {
                rows = 1;
                itemCount = 1;
            }

            // stretch
            int yOffset = 0;
            if (_isStretch && itemCount < list.size()) {
                int freeSpace = h - ((_cellHeight + getSpacing().vertical + yOffset) * rows) - getSpacing().vertical;
                yOffset = freeSpace / rows;
                if (rows > 1)
                    yOffset = freeSpace / (rows - 1);
            }

            for (IBaseItem item : list) {
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
            columns = column + 1;
        }
        _isUpdating = false;
    }

    private Style _selectedStyle;

    /**
     * Setting style of the WrapArea.
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

        Style inner_style = style.getInnerStyle("selection");
        if (inner_style != null) {
            _selectedStyle = inner_style.clone();
            List<IBaseItem> list = getItems();
            for (IBaseItem item : list) {
                item.setStyle(_selectedStyle);
            }
        }
    }
}
