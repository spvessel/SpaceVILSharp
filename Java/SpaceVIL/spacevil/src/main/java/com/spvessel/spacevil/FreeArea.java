package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.*;

import java.util.Map;
import java.util.HashMap;

/**
 * FreeArea is class representing an unbounded area with free location of inner
 * items. FreeArea implements com.spvessel.spacevil.Core.InterfaceFreeLayout and
 * com.spvessel.spacevil.Core.InterfaceDraggable. FreeArea is supposed to be
 * used with com.spvessel.spacevil.ResizableItem.
 * <p>
 * Supports all events including drag and drop.
 */
public class FreeArea extends Prototype implements InterfaceDraggable, InterfaceFreeLayout {

    private static int count = 0;
    private int _xPress = 0;
    private int _yPress = 0;
    private int _diffX = 0;
    private int _diffY = 0;
    private Map<InterfaceBaseItem, int[]> _storedItemsCoords;

    /**
     * Default FreeArea constructor.
     */
    public FreeArea() {
        setItemName("FreeArea_" + count);
        count++;
        _storedItemsCoords = new HashMap<>();

        eventMousePress.add(this::onMousePress);
        eventMouseDrag.add(this::onDragging);

        setStyle(DefaultsService.getDefaultStyle(FreeArea.class));
    }

    private void onMousePress(InterfaceItem sender, MouseArgs args) {
        _xPress = args.position.getX();
        _yPress = args.position.getY();
        _diffX = (int) _xOffset;
        _diffY = (int) _yOffset;
    }

    private void onDragging(InterfaceItem sender, MouseArgs args) {
        _xOffset = _diffX - _xPress + args.position.getX();
        _yOffset = _diffY + args.position.getY() - _yPress;
        updateLayout();
    }

    private long _yOffset = 0;
    private long _xOffset = 0;

    /**
     * Getting Y axis offset of an unbounded area of FreArea.
     * <p>
     * Default: 0.
     * 
     * @return Y axis offset of an unbounded area.
     */
    public long getVScrollOffset() {
        return _yOffset;
    }

    /**
     * Setting Y axis offset of an unbounded area of FreArea.
     * 
     * @param value Y axis offset of an unbounded area.
     */
    public void setVScrollOffset(long value) {
        _yOffset = value;
        updateLayout();
    }

    /**
     * Getting X axis offset of an unbounded area of FreArea.
     * <p>
     * Default: 0.
     * 
     * @return X axis offset of an unbounded area.
     */
    public long getHScrollOffset() {
        return _xOffset;
    }

    /**
     * Setting X axis offset of an unbounded area of FreArea.
     * 
     * @param value X axis offset of an unbounded area.
     */
    public void setHScrollOffset(long value) {
        _xOffset = value;
        updateLayout();
    }

    // overrides
    /**
     * Adding item to the FreeArea.
     * <p>
     * Notice: Make sure the item is in the correct position to be visible.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        super.addItem(item);
        _storedItemsCoords.put(item, new int[] { item.getX(), item.getY() });
        if (item instanceof ResizableItem) {
            ResizableItem wanted = (ResizableItem) item;
            wanted.setPassEvents(false);
            wanted.positionChanged.add(() -> correctPosition(wanted));
        }
        updateLayout();
    }

    /**
     * Remove item from the FreeArea.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    @Override
    public boolean removeItem(InterfaceBaseItem item) {
        boolean b = super.removeItem(item);
        synchronized (this) {
            _storedItemsCoords.remove(item);
        }
        updateLayout();
        return b;
    }

    /**
     * Updating all children positions (implementation of
     * com.spvessel.spacevil.Core.InterfaceFreeLayout).
     */
    public void updateLayout() {
        for (InterfaceBaseItem child : getItems()) {
            child.setX((int) _xOffset + getX() + getPadding().left + _storedItemsCoords.get(child)[0]
                    + child.getMargin().left);
            child.setY((int) _yOffset + getY() + getPadding().top + _storedItemsCoords.get(child)[1]
                    + child.getMargin().top);
        }
    }

    private void correctPosition(ResizableItem item) {
        int actual_x = item.getX();
        int actual_y = item.getY();
        _storedItemsCoords.remove(item);
        _storedItemsCoords.put(item,
                new int[] { actual_x - (int) _xOffset - getX() - getPadding().left - item.getMargin().left,
                        actual_y - (int) _yOffset - getY() - getPadding().top - item.getMargin().top });
    }
}