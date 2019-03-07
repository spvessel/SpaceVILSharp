package com.spvessel.spacevil;

import java.awt.Color;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.Side;
import com.spvessel.spacevil.Flags.SizePolicy;

public class SideArea extends Prototype {
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
        _size = size;
    }

    public SideArea(Side attachSide) {
        setItemName("SideArea_" + count++);
        setPassEvents(false);
        _close = new ButtonCore();
        window = new ResizableItem();
        setStyle(DefaultsService.getDefaultStyle(SideArea.class));
        _attachSide = attachSide;
        applyAttach();
        eventMouseClick.add((sender, args) -> {
            close();
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
            close();
        });
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
        if (width < window.getWidth())
            window.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        if (height < window.getHeight())
            window.setHeight(height);
    }

    WindowLayout _handler = null;

    public void show(WindowLayout handler) {
        _handler = handler;
        _handler.addItem(this);
        _handler.setFocusedItem(this);

        // Thread task = new Thread(() -> {
        // int v = -_size;
        // int a = 10;
        // while (v < 0) {
        // v += 60;

        // if (_attachSide == ItemAlignment.LEFT)
        // window.setMargin(v, 0, 0, 0);
        // else if (_attachSide == ItemAlignment.RIGHT)
        // window.setMargin(0, 0, v, 0);
        // else if (_attachSide == ItemAlignment.TOP)
        // window.setMargin(0, v, 0, 0);
        // else if (_attachSide == ItemAlignment.BOTTOM)
        // window.setMargin(0, 0, 0, v);

        // setBackground(0, 0, 0, a += 10);
        // try {
        // Thread.sleep(20);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // }
        // });
        // task.start();
    }

    public void close() {
        // Thread task = new Thread(() -> {
        // int v = 0;
        // int a = getBackground().getAlpha() - 10;
        // while (v != -_size) {
        // v -= 60;

        // if (_attachSide == ItemAlignment.LEFT)
        // window.setMargin(v, 0, 0, 0);
        // else if (_attachSide == ItemAlignment.RIGHT)
        // window.setMargin(0, 0, v, 0);
        // else if (_attachSide == ItemAlignment.TOP)
        // window.setMargin(0, v, 0, 0);
        // else if (_attachSide == ItemAlignment.BOTTOM)
        // window.setMargin(0, 0, 0, v);

        // setBackground(0, 0, 0, a -= 10);
        // try {
        // Thread.sleep(20);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // }
        // _handler.getWindow().removeItem(this);
        // });
        // task.start();
        _handler.getWindow().removeItem(this);
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