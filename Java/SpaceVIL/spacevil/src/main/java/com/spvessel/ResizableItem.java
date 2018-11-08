package com.spvessel;

import java.util.LinkedList;
import java.util.List;

public class ResizableItem extends VisualItem implements InterfaceDraggable {
    enum Moving {
        TRUE, FALSE
    }

    protected List<ItemAlignment> _sides = new LinkedList<>();

    public EventCommonMethod positionChanged = new EventCommonMethod();
    public EventCommonMethod sizeChanged = new EventCommonMethod();

    public boolean isLocked = false;
    public boolean isResizable = true;
    public boolean isHFloating = true;
    public boolean isVFloating = true;

    private Moving _is_moved;

    static int count = 0;
    private int _pressed_x = 0;
    private int _pressed_y = 0;
    private int _width = 0;
    private int _height = 0;
    private int _x_global = 0;
    private int _y_global = 0;
    private int _diff_x = 0;
    private int _diff_y = 0;

    public ResizableItem() {
        setItemName("ResizableItem_" + count);
        setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);

        InterfaceMouseMethodState press = (sender, args) -> onMousePress(sender, args);
        eventMousePressed.add(press);
        InterfaceMouseMethodState dragg = (sender, args) -> onDragging(sender, args);
        eventMouseDrag.add(dragg);
        count++;
    }

    protected void onMousePress(InterfaceItem sender, MouseArgs args) {
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

    protected void onDragging(InterfaceItem sender, MouseArgs args) {
        if (isLocked)
            return;

        int offset_x;
        int offset_y;

        switch (_is_moved) {
        case TRUE:
            if (isHFloating) {
                offset_x = args.position.getX() - _diff_x;
                setX(offset_x);
            }
            if (isVFloating) {
                offset_y = args.position.getY() - _diff_y;
                setY(offset_y);
            }
            positionChanged.execute();
            setConfines();
            break;

        case FALSE:
            if (!isResizable)
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
                setWidth(w);
                setHeight(h);
                sizeChanged.execute();
            }
            setConfines();
            break;
        }
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        sizeChanged.execute();
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        sizeChanged.execute();
    }

    public void getSides(float xpos, float ypos) {
        _sides.clear();
        if (xpos <= 5) {
            _sides.add(ItemAlignment.LEFT);
        }
        if (xpos > getWidth() - 5) {
            _sides.add(ItemAlignment.RIGHT);
        }

        if (ypos <= 5) {
            _sides.add(ItemAlignment.TOP);
        }
        if (ypos > getHeight() - 5) {
            _sides.add(ItemAlignment.BOTTOM);
        }
    }
}