package com.spvessel;

//import InterfaceBaseItem;

import java.util.*;

import com.spvessel.Core.InterfaceBaseItem;

final class ItemsLayout {
    protected ItemsLayout(UUID layoutId) {
        _id = layoutId;
    }

    private UUID _id;

    protected UUID getId() {
        return _id;
    }

    protected void setId(UUID value) {
        _id = value;
    }

    List<InterfaceBaseItem> _items = new LinkedList<InterfaceBaseItem>();

    public List<InterfaceBaseItem> getItems() {
        return _items;
    }

    protected void setItems(List<InterfaceBaseItem> value) {
        _items = value;
    }

    List<InterfaceBaseItem> _float_items = new LinkedList<InterfaceBaseItem>();

    public List<InterfaceBaseItem> getFloatItems() {
        return _float_items;
    }

    protected void setFloatItems(List<InterfaceBaseItem> value) {
        _float_items = value;
    }
}