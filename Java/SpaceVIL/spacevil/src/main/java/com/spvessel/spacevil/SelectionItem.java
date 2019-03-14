package com.spvessel.spacevil;

import java.util.List;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Flags.ItemStateType;

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
        setStyle(DefaultsService.getDefaultStyle(SelectionItem.class));
    }

    public SelectionItem(InterfaceBaseItem content) {
        this();
        _item = content;

        eventMouseClick.add((sender, args) -> {
            if (isToggled() || !_visibility)
                return;
            setToggled(true);
            uncheckOthers(sender);
        });
    }

    InterfaceBaseItem getContent() {
        return _item;
    }

    @Override
    public void initElements() {
        setVisible(_item.isVisible());
        addItem(_item);
    }

    public void updateSize() {
        setSize(_item.getWidth() + _item.getMargin().left + _item.getMargin().right,
                _item.getHeight() + _item.getMargin().bottom + _item.getMargin().top + 2);
        setMinSize(_item.getMinWidth() + _item.getMargin().left + _item.getMargin().right,
                _item.getMinHeight() + _item.getMargin().bottom + _item.getMargin().top);
        // _item.setParent(getParent());
    }

    // private for class
    private boolean _toggled = false;

    /**
     * Is ButtonToggle toggled (boolean)
     */
    public boolean isToggled() {
        return _toggled;
    }

    public void setToggled(boolean value) {
        _toggled = value;
        if (value == true)
            setState(ItemStateType.TOGGLED);
        else
            setState(ItemStateType.BASE);
    }

    private void uncheckOthers(InterfaceItem sender) {
        List<InterfaceBaseItem> items = getParent().getItems();
        for (InterfaceBaseItem item : items) {
            if (item instanceof SelectionItem && !item.equals(this)) {
                ((SelectionItem) item).setToggled(false);
            }
        }
    }

    @Override
    public void removeItem(InterfaceBaseItem item) {
        getParent().removeItem(item);
    }

    @Override
    public void setMouseHover(boolean value) {
        if (_visibility)
            super.setMouseHover(value);
    }
}