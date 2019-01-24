package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.awt.Color;

import com.spvessel.spacevil.Prototype;

public class DialogItem extends Prototype {
    static int count = 0;
    public Frame window = new Frame();

    /// <summary>
    /// Constructs a MessageItem
    /// </summary>
    public DialogItem() {
        setItemName("DialogItem_" + count);
        count++;
        // setStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.DialogItem)));

        // view
        setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        setBackground(0, 0, 0, 150);
        setPassEvents(false);
    }

    @Override
    public void initElements() {
        // simple attr
        window.setSize(300, 150);
        window.setMinSize(300, 150);
        window.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        window.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        window.setPadding(2, 2, 2, 2);
        window.setBackground(45, 45, 45);
        window.setShadow(5, 3, 3, new Color(0, 0, 0, 180));

        // adding
        addItem(window);
    }

    WindowLayout _handler = null;

    /// <summary>
    /// Show MessageBox
    /// </summary>
    public void show(WindowLayout handler) {
        _handler = handler;
        _handler.addItem(this);
    }

    public void close() {
        _handler.getWindow().removeItem(this);
    }

    public EventCommonMethod OnCloseDialog = new EventCommonMethod();

    @Override
    public void setStyle(Style style) {

    }
}
