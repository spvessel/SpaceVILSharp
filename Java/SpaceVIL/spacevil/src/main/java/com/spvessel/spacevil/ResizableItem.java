package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Core.InterfaceDraggable;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.Side;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.LinkedList;
import java.util.List;

public class ResizableItem extends Prototype implements InterfaceDraggable {
    enum Moving {
        TRUE, FALSE
    }

    public List<Side> _sidesExclude = new LinkedList<>();

    public void excludeSides(Side... sides) {
        for (Side side : sides) {
            if (!_sidesExclude.contains(side))
                _sidesExclude.add(side);
        }
    }

    public List<Side> getExcludedSides() {
        return _sidesExclude;
    }
    public void clearExcludedSides() {
        _sidesExclude.clear();
    }

    List<ItemAlignment> _sides = new LinkedList<>();

    public EventCommonMethod positionChanged = new EventCommonMethod();
    public EventCommonMethod sizeChanged = new EventCommonMethod();

    public boolean isLocked = false;
    public boolean isWResizable = true;
    public boolean isHResizable = true;
    public boolean isXFloating = true;
    public boolean isYFloating = true;

    private Moving _is_moved;

    private static int count = 0;
    private int _pressed_x = 0;
    private int _pressed_y = 0;
    private int _width = 0;
    private int _height = 0;
    private int _x_global = 0;
    private int _y_global = 0;
    private int _diff_x = 0;
    private int _diff_y = 0;

    /**
     * Constructs a ResizableItem
     */
    public ResizableItem() {
        setItemName("ResizableItem_" + count);
        setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);

        eventMousePress.add(this::onMousePress);
        eventMouseDrag.add(this::onDragging);
        count++;
    }

    private void onMousePress(InterfaceItem sender, MouseArgs args) {
        if (isLocked)
            return;

        _pressed_x = args.position.getX();
        _pressed_y = args.position.getY();
        _x_global = getX();
        _y_global = getY();
        _diff_x = args.position.getX() - getX();
        _diff_y = args.position.getY() - getY();
        _width = getWidth();
        _height = getHeight();

        getSides(_diff_x, _diff_y);

        if (_sides.size() == 0) {
            _is_moved = Moving.TRUE;
        } else {
            _is_moved = Moving.FALSE;
        }
    }

    private void onDragging(InterfaceItem sender, MouseArgs args) {
        if (isLocked)
            return;

        int offset_x;
        int offset_y;

        switch (_is_moved) {
        case TRUE:
            if (isXFloating) {
                offset_x = args.position.getX() - _diff_x;
                setX(offset_x);
            }
            if (isYFloating) {
                offset_y = args.position.getY() - _diff_y;
                setY(offset_y);
            }
            positionChanged.execute();
            setConfines();
            break;

        case FALSE:
            if (!isWResizable && !isHResizable)
                break;

            int x_handler = getX();
            int y_handler = getY();
            int x_release = args.position.getX();
            int y_release = args.position.getY();
            int w = getWidth();
            int h = getHeight();

            if (_sides.contains(ItemAlignment.LEFT)) {
                if (!(getMinWidth() == getWidth() && (args.position.getX() - _pressed_x) >= 0)) {
                    int diff = _x_global - x_release;
                    x_handler = _x_global - diff;
                    w = _width + diff;
                }
            }
            if (_sides.contains(ItemAlignment.RIGHT)) {
                if (!(args.position.getX() < getMinWidth() && getWidth() == getMinWidth())) {
                    w = args.position.getX() - getX();
                }
                // _pressed_x = args.position.getX();
            }
            if (_sides.contains(ItemAlignment.TOP)) {
                if (!(getMinHeight() == getHeight() && (args.position.getY() - _pressed_y) >= 0)) {
                    int diff = _y_global - y_release;
                    y_handler = _y_global - diff;
                    h = _height + diff;
                }
            }
            if (_sides.contains(ItemAlignment.BOTTOM)) {
                if (!(args.position.getY() < getMinHeight() && getHeight() == getMinHeight())) {
                    h = args.position.getY() - getY();
                }
            }

            if (_sides.size() != 0) {
                if (_sides.contains(ItemAlignment.LEFT) || _sides.contains(ItemAlignment.TOP)) {
                    setX(x_handler);
                    setY(y_handler);
                    positionChanged.execute();
                }

                boolean flag = false;
                if (isWResizable && w != getWidth()) {
                    setWidth(w);
                    flag = true;
                }
                if (isHResizable && h != getHeight())
                    setHeight(h);
                if (flag)
                    sizeChanged.execute();
            }
            setConfines();
            break;
        }
    }

    /**
     * Set width of the ResizableItem
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        sizeChanged.execute();
    }

    /**
     * Set height of the ResizableItem
     */
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        sizeChanged.execute();
    }

    void getSides(float xpos, float ypos) {
        _sides.clear();
        if (xpos <= 10 && !_sidesExclude.contains(Side.LEFT)) {
            _sides.add(ItemAlignment.LEFT);
        }
        if (xpos >= getWidth() - 10 && !_sidesExclude.contains(Side.RIGHT)) {
            _sides.add(ItemAlignment.RIGHT);
        }

        if (ypos <= 10 && !_sidesExclude.contains(Side.TOP)) {
            _sides.add(ItemAlignment.TOP);
        }
        if (ypos >= getHeight() - 10 && !_sidesExclude.contains(Side.BOTTOM)) {
            _sides.add(ItemAlignment.BOTTOM);
        }
    }
}