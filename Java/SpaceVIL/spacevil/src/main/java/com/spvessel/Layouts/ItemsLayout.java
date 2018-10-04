package com.spvessel.Layouts;

import com.spvessel.Items.*;
import java.util.*;

public final class ItemsLayout {
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

    List<BaseItem> _items = new LinkedList<BaseItem>();

    public List<BaseItem> getItems() {
        return _items;
    }

    protected void setItems(List<BaseItem> value) {
        _items = value;
    }

    List<BaseItem> _float_items = new LinkedList<BaseItem>();

    public List<BaseItem> getFloatItems() {
        return _float_items;
    }

    protected void setFloatItems(List<BaseItem> value) {
        _float_items = value;
    }
}