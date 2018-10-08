package com.spvessel.Items;

import java.util.LinkedList;
import java.util.List;

import com.spvessel.Cores.EventCommonMethod;
import com.spvessel.Cores.InterfaceDraggable;
import com.spvessel.Cores.InterfaceItem;
import com.spvessel.Cores.InterfaceMouseMethodState;
import com.spvessel.Cores.MouseArgs;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.SizePolicy;

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
    private int _diff_x = 0;
    private int _diff_y = 0;

    public ResizableItem() {
        setItemName("ResizableItem_" + count);
        setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);

        //InterfaceMouseMethodState press = (sender, args) -> onMousePress(sender, args);
        eventMousePressed.add(this::onMousePress); //press);
        //InterfaceMouseMethodState dragg = (sender, args) -> onDragging(sender, args);
        eventMouseDrag.add(this::onDragging); //dragg);
        count++;
    }

    protected void onMousePress(InterfaceItem sender, MouseArgs args) {
        if (isLocked)
            return;

        _pressed_x = args.position.X;
        _pressed_y = args.position.Y;

        _diff_x = args.position.X - getX();
        _diff_y = args.position.Y - getY();

        getSides(_diff_x, _diff_y);

        if (_sides.size() == 0) {
            _is_moved = Moving.TRUE;
        } else {
            _width = getWidth();
            _height = getHeight();
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
                offset_x = args.position.X - _diff_x;
                setX(offset_x);
            }
            if (isVFloating) {
                offset_y = args.position.Y - _diff_y;
                setY(offset_y);
            }
            positionChanged.execute();
            setConfines();
            break;

        case FALSE:
            if (!isResizable)
                break;

            int x = getX();
            int y = getY();
            int w = getWidth();
            int h = getHeight();

            if (_sides.contains(ItemAlignment.LEFT)) {
                if (!(getMinWidth() == getWidth() && (args.position.X - _pressed_x) >= 0)) {
                    x += (args.position.X - _pressed_x);
                    w -= (args.position.X - _pressed_x);
                }
                _pressed_x = args.position.X;
            }
            if (_sides.contains(ItemAlignment.RIGHT)) {
                if (!(args.position.X < getMinWidth() && getWidth() == getMinWidth())) {
                    w += (args.position.X - _pressed_x);
                }
                _pressed_x = args.position.X;
            }
            if (_sides.contains(ItemAlignment.TOP)) {
                if (!(getMinHeight() == getHeight() && (args.position.Y - _pressed_y) >= 0)) {
                    y += (args.position.Y - _pressed_y);
                    h -= (args.position.Y - _pressed_y);
                }
                _pressed_y = args.position.Y;
            }
            if (_sides.contains(ItemAlignment.BOTTOM)) {
                if (!(args.position.Y < getMinHeight() && getHeight() == getMinHeight())) {
                    h += (args.position.Y - _pressed_y);
                }
                _pressed_y = args.position.Y;
            }

            if (_sides.size() != 0) {
                if (_sides.contains(ItemAlignment.LEFT) || _sides.contains(ItemAlignment.TOP)) {
                    setX(x);
                    setY(y);
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

    public void setWidth(int width) {
        super.setWidth(width);
        sizeChanged.execute();
    }

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