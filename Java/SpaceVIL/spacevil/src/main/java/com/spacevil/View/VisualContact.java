package com.spacevil.View;

import java.awt.Color;

import com.spacevil.*;
import com.spacevil.Core.InterfaceMouseMethodState;

public class VisualContact extends Prototype {
    public static int _count = 0;

    public VisualContact() {
        // self view attr
        setItemName("VC_" + _count);
        setBackground(80, 80, 80);
        setMinSize(250, 60);
        setSize(100, 60);
        setSizePolicy(com.spacevil.Flags.SizePolicy.EXPAND, com.spacevil.Flags.SizePolicy.FIXED);
        setBorderRadius(15);
        com.spacevil.Decorations.ItemState hover = new com.spacevil.Decorations.ItemState();
        hover.background = new Color(255, 255, 255, 15);
        addItemState(com.spacevil.Flags.ItemStateType.HOVERED, hover);
        setPadding(10, 0, 5, 0);
        setMargin(1, 1, 1, 1);
        setShadow(10, 3, 3, new Color(0, 0, 0, 160));
        _count++;
    }

    @Override
    public void initElements() {
        // contact image border
        Ellipse border = new Ellipse();
        border.setBackground(255, 180, 100, 100);
        border.setHeight(45);
        border.setHeightPolicy(com.spacevil.Flags.SizePolicy.FIXED);
        border.setWidth(45);
        border.setWidthPolicy(com.spacevil.Flags.SizePolicy.FIXED);
        border.setAlignment(com.spacevil.Flags.ItemAlignment.VCENTER, com.spacevil.Flags.ItemAlignment.LEFT);
        border.quality = 32;
        // border.setTriangles(GraphicsMathService.getRoundSquare(50, 50, 25, 0, 0));
        // border.setTriangles(GraphicsMathService.getEllipse(50, 50, 0, 0, 32));

        // contact name
        Label name = new Label(getItemName() + " contact");
        name.setTextAlignment(com.spacevil.Flags.ItemAlignment.VCENTER, com.spacevil.Flags.ItemAlignment.LEFT);
        name.setBackground(255, 255, 255, 32);
        name.setForeground(210, 210, 210);
        name.setHeight(30);
        name.setHeightPolicy(com.spacevil.Flags.SizePolicy.FIXED);
        name.setWidthPolicy(com.spacevil.Flags.SizePolicy.EXPAND);
        name.setMargin(60, 0, 30, 5);
        name.setPadding(20, 0, 0, 0);
        name.setAlignment(com.spacevil.Flags.ItemAlignment.BOTTOM, com.spacevil.Flags.ItemAlignment.LEFT);
        name.setBorderRadius(10);

        // contact close
        ButtonCore close = new ButtonCore();
        close.setBackground(40, 40, 40);
        close.setWidth(14);
        close.setWidthPolicy(com.spacevil.Flags.SizePolicy.FIXED);
        close.setHeight(14);
        close.setHeightPolicy(com.spacevil.Flags.SizePolicy.FIXED);
        close.setAlignment(com.spacevil.Flags.ItemAlignment.TOP, com.spacevil.Flags.ItemAlignment.RIGHT);
        close.setMargin(0, 5, 0, 0);
        close.setCustomFigure(new com.spacevil.Decorations.CustomFigure(false, GraphicsMathService.getCross(14, 14, 5, 45)));
        com.spacevil.Decorations.ItemState hover = new com.spacevil.Decorations.ItemState();
        hover.background = new Color(255, 255, 255, 125);
        close.addItemState(com.spacevil.Flags.ItemStateType.HOVERED, hover);
        InterfaceMouseMethodState click = (sender, args) -> disposeSelf();
        close.eventMouseClick.add(click);

        // adding
        addItem(border);
        addItem(name);
        addItem(close);
    }

    public void disposeSelf() {
        getParent().removeItem(this);
    }
}