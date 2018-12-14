package com.spvessel.spacevil;

//import InterfaceBaseItem;

import java.util.*;

import com.spvessel.spacevil.Core.InterfaceBaseItem;

final class ItemsLayout {
    ItemsLayout(UUID layoutId) {
        _id = layoutId;
    }

    private UUID _id;

    UUID getId() {
        return _id;
    }

    void setId(UUID value) {
        _id = value;
    }

    List<InterfaceBaseItem> _items = new LinkedList<>();

    List<InterfaceBaseItem> getItems() {
        return _items;
    }

    void setItems(List<InterfaceBaseItem> value) {
        _items = value;
    }

    List<InterfaceBaseItem> _float_items = new LinkedList<>();

    List<InterfaceBaseItem> getFloatItems() {
        return _float_items;
    }

    void setFloatItems(List<InterfaceBaseItem> value) {
        _float_items = value;
    }
}