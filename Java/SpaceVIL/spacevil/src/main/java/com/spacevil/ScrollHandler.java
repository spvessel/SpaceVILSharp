package com.spacevil;

import com.spacevil.Core.InterfaceDraggable;
import com.spacevil.Core.InterfaceItem;
import com.spacevil.Core.MouseArgs;

public class ScrollHandler extends Prototype implements InterfaceDraggable {
    private static int count = 0;
    public com.spacevil.Flags.Orientation direction;
    private int _offset = 0;
    private int _diff = 0;

    /**
     * Constructs a ScrollHandler
     */
    public ScrollHandler() {
        setItemName("ScrollHandler_" + count);
        eventMousePress.add(this::onMousePress);
        eventMouseDrag.add(this::onDragging);
        count++;
        isFocusable = false;
    }

    private void onMousePress(InterfaceItem sender, MouseArgs args) {
        if (direction == com.spacevil.Flags.Orientation.HORIZONTAL)
            _diff = args.position.getX() - getX();
        else
            _diff = args.position.getY() - getY();
    }

    private void onDragging(InterfaceItem sender, MouseArgs args) {
        int offset;

        if (direction == com.spacevil.Flags.Orientation.HORIZONTAL)
            offset = args.position.getX() - getParent().getX() - _diff;
        else
            offset = args.position.getY() - getParent().getY() - _diff;

        setOffset(offset);
    }

    /**
     * Set offset of the ScrollHandler
     */
    public void setOffset(int offset) {
        if (getParent() == null)
            return;

        _offset = offset;

        if (direction == com.spacevil.Flags.Orientation.HORIZONTAL)
            setX(_offset + getParent().getX());
        else
            setY(_offset + getParent().getY());
    }
}