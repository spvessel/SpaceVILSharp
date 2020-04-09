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

/**
 * ResisableItem is a special container that can move and resize by mouse input
 * events while inside another container.
 * <p>
 * Te get full functionality try to use ResizableItem with
 * com.spvessel.spacevil.FreeArea container.
 * <p>
 * Supports all events including drag and drop.
 */
public class ResizableItem extends Prototype implements InterfaceDraggable {

    private List<Side> _sidesExclude = new LinkedList<>();

    /**
     * Specify which sides will be excluded, and these sides can no longer be
     * dragged to resize the ResizableItem.
     * 
     * @param sides Sides for exclusion as sequence of
     *              com.spvessel.spacevil.Flags.Side.
     */
    public void excludeSides(Side... sides) {
        for (Side side : sides) {
            if (!_sidesExclude.contains(side))
                _sidesExclude.add(side);
        }
    }

    /**
     * Getting exclused sides. These sides cannot be dragged to resize the
     * ResizableItem.
     * 
     * @return Sides for exclusion as list of com.spvessel.spacevil.Flags.Side.
     */
    public List<Side> getExcludedSides() {
        return _sidesExclude;
    }

    /**
     * Removing all exclused sides. After that all sides can be dragged to resize
     * the ResizableItem.
     */
    public void clearExcludedSides() {
        _sidesExclude.clear();
    }

    List<Side> _sides = new LinkedList<>();

    /**
     * Event that is invoked when ResizableItem moves.
     */
    public EventCommonMethod positionChanged = new EventCommonMethod();

    /**
     * Event that is invoked when ResizableItem resizes.
     */
    public EventCommonMethod sizeChanged = new EventCommonMethod();

    /**
     * Disposing all resources if the item was removed.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void release() {
        positionChanged.clear();
        sizeChanged.clear();
    }

    /**
     * Property to lock ResizableItem movement and resizing.
     * <p>
     * True: to lock. False: to unlock.
     * <p>
     * Default: False.
     */
    public boolean isLocked = false;

    /**
     * Property to lock ResizableItem resizing by X axis.
     * <p>
     * True: to unlock. False: to lock.
     * <p>
     * Default: True.
     */
    public boolean isXResizable = true;

    /**
     * Property to lock ResizableItem resizing by Y axis.
     * <p>
     * True: to unlock. False: to lock.
     * <p>
     * Default: True.
     */
    public boolean isYResizable = true;

    /**
     * Property to lock ResizableItem movement by X axis.
     * <p>
     * True: to unlock. False: to lock.
     * <p>
     * Default: True.
     */
    public boolean isXFloating = true;

    /**
     * Property to lock ResizableItem movement by Y axis.
     * <p>
     * True: to unlock. False: to lock.
     * <p>
     * Default: True.
     */
    public boolean isYFloating = true;

    private boolean _isMoved;

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
     * Default ResizableItem constructor.
     */
    public ResizableItem() {
        setItemName("ResizableItem_" + count++);
        setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);

        eventMousePress.add(this::onMousePress);
        eventMouseDrag.add(this::onDragging);
        eventMouseHover.add(this::onHover);
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

    protected void onMousePress(InterfaceItem sender, MouseArgs args) {
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
            _isMoved = true;
        } else {
            _isMoved = false;
        }
    }

    protected void onDragging(InterfaceItem sender, MouseArgs args) {
        if (isLocked)
            return;

        int offset_x;
        int offset_y;

        if (_isMoved) {
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
        }

        else {
            if (!isXResizable && !isYResizable)
                return;

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
                if (isXResizable && w != getWidth()) {
                    setWidth(w);
                    flag = true;
                }
                if (isYResizable && h != getHeight())
                    setHeight(h);
                if (flag)
                    sizeChanged.execute();
            }
            setConfines();
        }
    }

    /**
     * Setting ResizableItem width. If the value is greater/less than the
     * maximum/minimum value of the width, then the width becomes equal to the
     * maximum/minimum value.
     * 
     * @param width Width of the ResizableItem.
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        sizeChanged.execute();
    }

    /**
     * Setting ResizableItem height. If the value is greater/less than the
     * maximum/minimum value of the height, then the height becomes equal to the
     * maximum/minimum value.
     * 
     * @param height Height of the ResizableItem.
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