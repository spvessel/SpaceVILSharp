package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Core.InterfaceDraggable;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Flags.EmbeddedCursor;
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

    List<Side> _sides = new LinkedList<>();

    public EventCommonMethod positionChanged = new EventCommonMethod();
    public EventCommonMethod sizeChanged = new EventCommonMethod();

    @Override
    public void release() {
        positionChanged.clear();
        sizeChanged.clear();
    }

    public boolean isLocked = false;
    public boolean isWResizable = true;
    public boolean isHResizable = true;
    public boolean isXFloating = true;
    public boolean isYFloating = true;

    private Moving _isMoved;

    private static int count = 0;
    private int _pressedX = 0;
    private int _pressedY = 0;
    private int _width = 0;
    private int _height = 0;
    private int _globalX = 0;
    private int _globalY = 0;
    private int _diffX = 0;
    private int _diffY = 0;

    /**
     * Constructs a ResizableItem
     */
    public ResizableItem() {
        setItemName("ResizableItem_" + count);
        setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);

        eventMousePress.add(this::onMousePress);
        eventMouseDrag.add(this::onDragging);
        eventMouseHover.add(this::onHover);
        count++;
    }

    protected void onHover(InterfaceItem sender, MouseArgs args) {
        if (isLocked)
            return;

        getSides(args.position.getX() - getX(), args.position.getY() - getY());

        if (_sides.contains(Side.LEFT) || _sides.contains(Side.RIGHT)) {
            if (_sides.contains(Side.TOP) || _sides.contains(Side.BOTTOM))
                setCursor(EmbeddedCursor.CROSSHAIR);
            else
                setCursor(EmbeddedCursor.RESIZE_X);
        } else if (_sides.contains(Side.TOP) || _sides.contains(Side.BOTTOM)) {
            if (_sides.contains(Side.LEFT) || _sides.contains(Side.RIGHT))
                setCursor(EmbeddedCursor.CROSSHAIR);
            else
                setCursor(EmbeddedCursor.RESIZE_Y);
        } else
            setCursor(DefaultsService.getDefaultCursor());
    }

    private void onMousePress(InterfaceItem sender, MouseArgs args) {
        if (isLocked)
            return;

        _pressedX = args.position.getX();
        _pressedY = args.position.getY();
        _globalX = getX();
        _globalY = getY();
        _diffX = args.position.getX() - getX();
        _diffY = args.position.getY() - getY();
        _width = getWidth();
        _height = getHeight();

        getSides(_diffX, _diffY);

        if (_sides.size() == 0) {
            _isMoved = Moving.TRUE;
        } else {
            _isMoved = Moving.FALSE;
        }
    }

    private void onDragging(InterfaceItem sender, MouseArgs args) {
        if (isLocked)
        return;
        
        int offset_x;
        int offset_y;
        
        switch (_isMoved) {
            case TRUE:
            if (isXFloating) {
                offset_x = args.position.getX() - _diffX;
                setX(offset_x);
            }
            if (isYFloating) {
                offset_y = args.position.getY() - _diffY;
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

            if (_sides.contains(Side.LEFT)) {
                if (!(getMinWidth() == getWidth() && (args.position.getX() - _pressedX) >= 0)) {
                    int diff = _globalX - x_release;
                    x_handler = _globalX - diff;
                    w = _width + diff;
                }
            }
            if (_sides.contains(Side.RIGHT)) {
                if (!(args.position.getX() < getMinWidth() && getWidth() == getMinWidth())) {
                    w = args.position.getX() - getX();
                }
            }
            if (_sides.contains(Side.TOP)) {
                if (!(getMinHeight() == getHeight() && (args.position.getY() - _pressedY) >= 0)) {
                    int diff = _globalY - y_release;
                    y_handler = _globalY - diff;
                    h = _height + diff;
                }
            }
            if (_sides.contains(Side.BOTTOM)) {
                if (!(args.position.getY() < getMinHeight() && getHeight() == getMinHeight())) {
                    h = args.position.getY() - getY();
                }
            }

            if (_sides.size() != 0) {
                if (_sides.contains(Side.LEFT) || _sides.contains(Side.TOP)) {
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
        if (xpos <= SpaceVILConstants.borderCursorTolerance && !_sidesExclude.contains(Side.LEFT)) {
            _sides.add(Side.LEFT);
        }
        if (xpos >= getWidth() - SpaceVILConstants.borderCursorTolerance && !_sidesExclude.contains(Side.RIGHT)) {
            _sides.add(Side.RIGHT);
        }

        if (ypos <= SpaceVILConstants.borderCursorTolerance && !_sidesExclude.contains(Side.TOP)) {
            _sides.add(Side.TOP);
        }
        if (ypos >= getHeight() - SpaceVILConstants.borderCursorTolerance && !_sidesExclude.contains(Side.BOTTOM)) {
            _sides.add(Side.BOTTOM);
        }
    }
}