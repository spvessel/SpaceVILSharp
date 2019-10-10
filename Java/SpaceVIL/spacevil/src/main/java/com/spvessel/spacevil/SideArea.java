package com.spvessel.spacevil;

import java.awt.Color;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceFloating;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;
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

        Color shadowColor = new Color(0, 0, 0, 150);
        int shadowRadius = 5;
        int shadowIndent = 2;

        switch (_attachSide) {
        case LEFT:
            window.isWResizable = true;
            window.setWidthPolicy(SizePolicy.FIXED);
            window.setWidth(_size);
            window.excludeSides(Side.LEFT, Side.BOTTOM, Side.TOP);
            window.setAlignment(ItemAlignment.LEFT);
            window.setShadow(shadowRadius, shadowIndent, 0, shadowColor);
            break;

        case TOP:
            window.isHResizable = true;
            window.setHeightPolicy(SizePolicy.FIXED);
            window.setHeight(_size);
            window.excludeSides(Side.LEFT, Side.RIGHT, Side.TOP);
            window.setAlignment(ItemAlignment.TOP);
            window.setShadow(shadowRadius, 0, shadowIndent, shadowColor);
            break;

        case RIGHT:
            window.isWResizable = true;
            window.setWidthPolicy(SizePolicy.FIXED);
            window.setWidth(_size);
            window.excludeSides(Side.RIGHT, Side.BOTTOM, Side.TOP);
            window.setShadow(shadowRadius, -shadowIndent, 0, shadowColor);
            window.setAlignment(ItemAlignment.RIGHT);
            break;

        case BOTTOM:
            window.isHResizable = true;
            window.setHeightPolicy(SizePolicy.FIXED);
            window.setHeight(_size);
            window.excludeSides(Side.LEFT, Side.RIGHT, Side.BOTTOM);
            window.setShadow(shadowRadius, 0, -shadowIndent, shadowColor);
            window.setAlignment(ItemAlignment.BOTTOM);
            break;

        default:
            window.setWidth(_size);
            window.setAlignment(ItemAlignment.LEFT);
            window.setShadow(shadowRadius, shadowIndent, 0, shadowColor);
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

    public SideArea(CoreWindow handler, Side attachSide) {
        ItemsLayoutBox.addItem(handler, this, LayoutType.FLOATING);
        setItemName("SideArea_" + count++);
        _close = new ButtonCore();
        window = new ResizableItem();
        setStyle(DefaultsService.getDefaultStyle(SideArea.class));
        _attachSide = attachSide;
        applyAttach();
        eventMouseClick.add((sender, args) -> {
            hide();
        });
        setVisible(false);
        setPassEvents(false);
        
        eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.ESCAPE)
                hide();
        });
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
    public boolean removeItem(InterfaceBaseItem item) {
        return window.removeItem(item);
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
        setFocus();
    }

    public void show(InterfaceItem sender, MouseArgs args) {
        show();
    }

    public void hide() {
        setVisible(false);
    }

    public void hide(MouseArgs args) {
        hide();
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