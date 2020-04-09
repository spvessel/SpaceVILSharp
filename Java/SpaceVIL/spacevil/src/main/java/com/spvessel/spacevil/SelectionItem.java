package com.spvessel.spacevil;

import java.util.List;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Flags.ItemStateType;

/**
 * SelectionItem is designed to be a wrapper (selection showing) of items in
 * special containers that supports item selection such as
 * com.spvessel.spacevil.ListBox, com.spvessel.spacevil.TreeView,
 * com.spvessel.spacevil.WrapGrid.
 * <p>
 * Can resize by size of wrapped item.
 * <p>
 * Supports all events except drag and drop.
 */
public class SelectionItem extends Prototype {
    private static int count = 0;
    InterfaceBaseItem _item;
    private boolean _visibility = true;

    void setToggleVisible(boolean value) {
        _visibility = value;
    }

    private SelectionItem() {
        isFocusable = false;
        setItemName("SelectionItem_" + count);
        count++;
    }

    /**
     * Constructs SelectionItem with given item for wrapping.
     * 
     * @param content Item for wrapping as
     *                com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    public SelectionItem(InterfaceBaseItem content) {
        this();
        _item = content;

        eventMouseClick.add((sender, args) -> {
            if (isSelected() || !_visibility)
                return;
            setSelected(true);
            unselectOthers(sender);
        });
    }

    /**
     * Getting wrapped item of SelectionItem.
     * 
     * @return Wrapped item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    public InterfaceBaseItem getContent() {
        return _item;
    }

    /**
     * Initializing all elements in the SelectionItem.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        setVisible(_item.isVisible());
        addItem(_item);
    }

    /**
     * Updating size of SelectionItem according to wrapped item size.
     */
    public void updateSize() {
        setSize(_item.getWidth() + _item.getMargin().left + _item.getMargin().right,
                _item.getHeight() + _item.getMargin().bottom + _item.getMargin().top + 2);
        setMinSize(_item.getMinWidth() + _item.getMargin().left + _item.getMargin().right,
                _item.getMinHeight() + _item.getMargin().bottom + _item.getMargin().top);
    }

    /**
     * Updating width of SelectionItem according to wrapped item width.
     */
    public void updateWidth() {
        setWidth(_item.getWidth() + _item.getMargin().left + _item.getMargin().right);
        setMinWidth(_item.getMinWidth() + _item.getMargin().left + _item.getMargin().right);
    }

    /**
     * Updating height of SelectionItem according to wrapped item height.
     */
    public void updateHeight() {
        setHeight(_item.getHeight() + _item.getMargin().bottom + _item.getMargin().top + 2);
        setMinHeight(_item.getMinHeight() + _item.getMargin().bottom + _item.getMargin().top);
    }

    private boolean _isSelected = false;

    /**
     * Returns True if SelectionItem is selected otherwise returns False.
     * 
     * @return True: SelectionItem is selected. False: SelectionItem is unselected.
     */
    public boolean isSelected() {
        return _isSelected;
    }

    /**
     * Setting SelectionItem selected or unselected.
     * 
     * @param value True: if you want SelectionItem to be selected. False: if you
     *              want SelectionItem to be unselected.
     */
    public void setSelected(boolean value) {
        _isSelected = value;
        if (value == true)
            setState(ItemStateType.TOGGLED);
        else
            setState(ItemStateType.BASE);
    }

    private void unselectOthers(InterfaceItem sender) {
        List<InterfaceBaseItem> items = getParent().getItems();
        for (InterfaceBaseItem item : items) {
            if (item instanceof SelectionItem && !item.equals(this)) {
                ((SelectionItem) item).setSelected(false);
            }
        }
    }

    /**
     * Removing the specified item from SelectionItem.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    @Override
    public boolean removeItem(InterfaceBaseItem item) {
        if (_item != null)
            return getParent().removeItem(item);
        else
            return false;
    }

    /**
     * Remove wrapped item from SelectionItem.
     */
    public void clearContent() {
        super.removeItem(_item);
        _item = null;
    }

    /**
     * Setting this item hovered (mouse cursor located within item's shape).
     * 
     * @param value True: if you want this item be hovered. False: if you want this
     *              item be not hovered.
     */
    @Override
    public void setMouseHover(boolean value) {
        if (_visibility)
            super.setMouseHover(value);
    }
}