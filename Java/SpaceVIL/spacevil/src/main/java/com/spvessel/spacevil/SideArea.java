package com.spvessel.spacevil;

import java.awt.Color;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceFloating;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.LayoutType;
import com.spvessel.spacevil.Flags.Side;
import com.spvessel.spacevil.Flags.SizePolicy;

public class SideArea extends Prototype implements InterfaceFloating {

    private boolean _init = false;
    private boolean _outside = false;

    public boolean isOutsideClickClosable() {
        return _outside;
    }

    public void setOutsideClickClosable(boolean value) {
        _outside = value;
    }

    static int count = 0;
    private ButtonCore _close;
    public ResizableItem window;

    private Side _attachSide = Side.LEFT;

    public Side getAttachSide() {
        return _attachSide;
    }

    public void setAttachSide(Side side) {
        if (_attachSide == side)
            return;

        _attachSide = side;
        applyAttach();
    }

    private void applyAttach() {
        window.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        window.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
        window.isWResizable = false;
        window.isHResizable = false;
        window.clearExcludedSides();

        switch (_attachSide) {
        case LEFT:
            window.isWResizable = true;
            window.setWidthPolicy(SizePolicy.FIXED);
            window.setWidth(_size);
            window.excludeSides(Side.LEFT, Side.BOTTOM, Side.TOP);
            window.setAlignment(ItemAlignment.LEFT);
            window.setShadow(5, 3, 0, new Color(0, 0, 0, 150));
            break;

        case TOP:
            window.isHResizable = true;
            window.setHeightPolicy(SizePolicy.FIXED);
            window.setHeight(_size);
            window.excludeSides(Side.LEFT, Side.RIGHT, Side.TOP);
            window.setAlignment(ItemAlignment.TOP);
            window.setShadow(5, 0, 3, new Color(0, 0, 0, 150));
            break;

        case RIGHT:
            window.isWResizable = true;
            window.setWidthPolicy(SizePolicy.FIXED);
            window.setWidth(_size);
            window.excludeSides(Side.RIGHT, Side.BOTTOM, Side.TOP);
            window.setShadow(5, -3, 0, new Color(0, 0, 0, 150));
            window.setAlignment(ItemAlignment.RIGHT);
            break;

        case BOTTOM:
            window.isHResizable = true;
            window.setHeightPolicy(SizePolicy.FIXED);
            window.setHeight(_size);
            window.excludeSides(Side.LEFT, Side.RIGHT, Side.BOTTOM);
            window.setShadow(5, 0, -3, new Color(0, 0, 0, 150));
            window.setAlignment(ItemAlignment.BOTTOM);
            break;

        default:
            window.setWidth(_size);
            window.setAlignment(ItemAlignment.LEFT);
            window.setShadow(5, 3, 0, new Color(0, 0, 0, 150));
            break;
        }
    }

    private int _size = 300;

    public int getAreaSize() {
        return _size;
    }

    public void setAreaSize(int size) {
        if (size == _size)
            return;
        _size = size;
        applyAttach();
    }

    public SideArea(WindowLayout handler, Side attachSide) {
        setHandler(handler);
        setItemName("SideArea_" + count++);
        _close = new ButtonCore();
        window = new ResizableItem();
        setStyle(DefaultsService.getDefaultStyle(SideArea.class));
        _attachSide = attachSide;
        applyAttach();
        eventMouseClick.add((sender, args) -> {
            hide();
        });
        ItemsLayoutBox.addItem(getHandler(), this, LayoutType.FLOATING);
        setVisible(false);
        setPassEvents(false);   
    }

    @Override
    public void initElements() {
        super.addItem(window);
        window.addItem(_close);
        window.setPassEvents(false);
        window.isXFloating = false;
        window.isYFloating = false;
        _close.eventMouseClick.add((sender, args) -> {
            hide();
        });
        _init = true;
    }

    @Override
    public void addItem(InterfaceBaseItem item) {
        window.addItem(item);
    }

    @Override
    public void insertItem(InterfaceBaseItem item, int index) {
        window.insertItem(item, index);
    }

    @Override
    public void removeItem(InterfaceBaseItem item) {
        window.removeItem(item);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
    }

    public void show() {
        if (!_init)
            initElements();
        setVisible(true);
    }

    public void show(InterfaceItem sender, MouseArgs args) {
        show();
    }

    public void hide() {
        setVisible(false);
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
        inner_style = style.getInnerStyle("closebutton");
        if (inner_style != null) {
            _close.setStyle(inner_style);
        }
    }
}