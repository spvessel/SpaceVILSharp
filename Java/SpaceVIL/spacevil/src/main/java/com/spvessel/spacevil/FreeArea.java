package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.*;

import java.util.Map;
import java.util.HashMap;

public class FreeArea extends Prototype implements InterfaceDraggable, InterfaceFreeLayout {
    /**
     * Unbounded area with free location of inner items
     */
    private static int count = 0;
    private int _xPress = 0;
    private int _yPress = 0;
    private int _diffX = 0;
    private int _diffY = 0;
    private Map<InterfaceBaseItem, int[]> _storedItemsCoords;

    /**
     * Constructs a FreeArea
     */
    public FreeArea() {
        setItemName("FreeArea_" + count);
        count++;
        _storedItemsCoords = new HashMap<>();

        eventMousePress.add(this::onMousePress);
        eventMouseDrag.add(this::onDragging);

        setStyle(DefaultsService.getDefaultStyle(FreeArea.class));
    }

    protected void onMousePress(InterfaceItem sender, MouseArgs args) {
        _xPress = args.position.getX();
        _yPress = args.position.getY();
        _diffX = (int) _xOffset;
        _diffY = (int) _yOffset;
    }

    protected void onDragging(InterfaceItem sender, MouseArgs args) {
        _xOffset = _diffX - _xPress + args.position.getX();
        _yOffset = _diffY + args.position.getY() - _yPress;
        updateLayout();
    }

    private long _yOffset = 0;
    private long _xOffset = 0;

    public long getVScrollOffset() {
        return _yOffset;
    }

    public void setVScrollOffset(long value) {
        _yOffset = value;
        updateLayout();
    }

    public long getHScrollOffset() {
        return _xOffset;
    }

    public void setHScrollOffset(long value) {
        _xOffset = value;
        updateLayout();
    }

    // overrides
    /**
     * Add item to the FreeArea
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
     * Remove item from the FreeArea
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
     * Update all children elements positions
     */
    public void updateLayout() {
        for (InterfaceBaseItem child : getItems()) {
            child.setX(
                    (int) _xOffset + getX() + getPadding().left + _storedItemsCoords.get(child)[0] + child.getMargin().left);
            child.setY((int) _yOffset + getY() + getPadding().top + _storedItemsCoords.get(child)[1] + child.getMargin().top);
        }
    }

    // ContexMenu
    private void correctPosition(ResizableItem item) {
        int actual_x = item.getX();
        int actual_y = item.getY();
        _storedItemsCoords.remove(item);
        _storedItemsCoords.put(item,
                new int[] { actual_x - (int) _xOffset - getX() - getPadding().left - item.getMargin().left,
                        actual_y - (int) _yOffset - getY() - getPadding().top - item.getMargin().top });
    }
}