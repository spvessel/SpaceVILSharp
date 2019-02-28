package com.spvessel.spacevil;

import java.awt.Color;
import java.util.List;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

class SelectionItem extends Prototype {
    private static int count = 0;
    InterfaceBaseItem _item;

    public SelectionItem(InterfaceBaseItem content) {
        _item = content;
        isFocusable = false;
        setItemName("SelectionItem_" + count);
        count++;

        setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        setBackground(0, 0, 0, 0);
        addItemState(ItemStateType.TOGGLED, new ItemState(new Color(255, 255, 255, 50)));

        eventMouseClick.add((sender, args) -> { 
            if (getContent() instanceof Prototype) {
                ((Prototype) getContent()).setFocus();
            }

            if (isToggled())
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
        addItem(_item);
        setSize(_item.getWidth() + _item.getMargin().left + _item.getMargin().right,
                _item.getHeight() + _item.getMargin().bottom + _item.getMargin().top);
        setMinSize(_item.getMinWidth() + _item.getMargin().left + _item.getMargin().right,
                _item.getMinHeight() + _item.getMargin().bottom + _item.getMargin().top);
        _item.setParent(getParent());
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
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
    }
}