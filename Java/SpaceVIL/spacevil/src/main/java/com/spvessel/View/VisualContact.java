package com.spvessel.View;

import java.awt.Color;

import com.spvessel.*;
import com.spvessel.Core.InterfaceMouseMethodState;
import com.spvessel.Decorations.CustomFigure;
import com.spvessel.Decorations.ItemState;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.ItemStateType;
import com.spvessel.Flags.SizePolicy;

public class VisualContact extends Prototype {
    public static int _count = 0;

    public VisualContact() {
        // self view attr
        setItemName("VC_" + _count);
        setBackground(80, 80, 80);
        setMinSize(250, 60);
        setSize(100, 60);
        setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        setBorderRadius(15);
        ItemState hover = new ItemState();
        hover.background = new Color(255, 255, 255, 15);
        addItemState(ItemStateType.HOVERED, hover);
        setPadding(10, 0, 5, 0);
        setMargin(1, 1, 1, 1);
        _count++;
    }

    @Override
    public void initElements() {
        // contact image border
        Ellipse border = new Ellipse();
        border.setBackground(255, 180, 100, 100);
        border.setHeight(45);
        border.setHeightPolicy(SizePolicy.FIXED);
        border.setWidth(45);
        border.setWidthPolicy(SizePolicy.FIXED);
        border.setAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
        border.Quality = 32;
        // border.setTriangles(GraphicsMathService.getRoundSquare(50, 50, 25, 0, 0));
        // border.setTriangles(GraphicsMathService.getEllipse(50, 50, 0, 0, 32));

        // contact name
        Label name = new Label(getItemName() + " contact");
        name.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
        name.setBackground(255, 255, 255, 32);
        name.setForeground(210, 210, 210);
        name.setHeight(30);
        name.setHeightPolicy(SizePolicy.FIXED);
        name.setWidthPolicy(SizePolicy.EXPAND);
        name.setMargin(60, 0, 30, 5);
        name.setPadding(20, 0, 0, 0);
        name.setAlignment(ItemAlignment.BOTTOM, ItemAlignment.LEFT);
        name.setBorderRadius(10);

        // contact close
        ButtonCore close = new ButtonCore();
        close.setBackground(40, 40, 40);
        close.setWidth(14);
        close.setWidthPolicy(SizePolicy.FIXED);
        close.setHeight(14);
        close.setHeightPolicy(SizePolicy.FIXED);
        close.setAlignment(ItemAlignment.TOP, ItemAlignment.RIGHT);
        close.setMargin(0, 5, 0, 0);
        close.setCustomFigure(new CustomFigure(false, GraphicsMathService.getCross(14, 14, 5, 45)));
        ItemState hover = new ItemState();
        hover.background = new Color(255, 255, 255, 125);
        close.addItemState(ItemStateType.HOVERED, hover);
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