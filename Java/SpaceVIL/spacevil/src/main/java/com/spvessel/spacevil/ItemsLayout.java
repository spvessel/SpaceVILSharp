package com.spvessel.spacevil;

import java.util.*;

import com.spvessel.spacevil.Core.IBaseItem;

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

    List<IBaseItem> _items = new LinkedList<>();

    List<IBaseItem> getItems() {
        return _items;
    }

    void setItems(List<IBaseItem> value) {
        _items = value;
    }

    List<IBaseItem> _float_items = new LinkedList<>();

    List<IBaseItem> getFloatItems() {
        return _float_items;
    }

    void setFloatItems(List<IBaseItem> value) {
        _float_items = value;
    }

    List<IBaseItem> _dialog_items = new LinkedList<>();

    List<IBaseItem> getDialogItems() {
        return _dialog_items;
    }

    void setDialogItems(List<IBaseItem> value) {
        _dialog_items = value;
    }
}