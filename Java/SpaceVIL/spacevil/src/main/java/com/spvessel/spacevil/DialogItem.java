package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Decorations.Style;

import java.awt.Color;

import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.Common.DefaultsService;

public class DialogItem extends Prototype {
    static int count = 0;
    public Frame window = new Frame();

    public DialogItem() {
        setItemName("DialogItem_" + count);
        count++;
        setPassEvents(false);
        setStyle(DefaultsService.getDefaultStyle(DialogItem.class));
    }

    @Override
    public void initElements() {
        window.setShadow(5, 3, 3, new Color(0, 0, 0, 180));
        addItem(window);
    }

    WindowLayout _handler = null;

    public void show(WindowLayout handler) {
        _handler = handler;
        _handler.addItem(this);
        _handler.setFocusedItem(this);
    }

    public void close() {
        _handler.getWindow().removeItem(this);
    }

    public EventCommonMethod onCloseDialog = new EventCommonMethod();

    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        Style inner_style = style.getInnerStyle("window");
        if (inner_style != null) {
            window.setStyle(inner_style);
        }
    }
}
