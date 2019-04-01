package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.LayoutType;

import java.awt.Color;

import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.Common.DefaultsService;

abstract public class DialogItem extends Prototype {
    static int count = 0;
    public ResizableItem window = new ResizableItem();

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
        // setHandler(handler);
        // initElements();
        // ItemsLayoutBox.addItem(handler, this, LayoutType.DIALOG);
        _handler.addItem(this);
        this.setFocus();
    }

    public void close() {
        // ItemsLayoutBox.addItem(getHandler(), this, LayoutType.DIALOG);
        _handler.getWindow().removeItem(this);
    }

    public EventCommonMethod onCloseDialog = new EventCommonMethod();
    
    @Override
    public void release() {
        onCloseDialog.clear();
    }

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
