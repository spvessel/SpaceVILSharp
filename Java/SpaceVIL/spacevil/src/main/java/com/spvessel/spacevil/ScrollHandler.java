package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.IDraggable;
import com.spvessel.spacevil.Core.IItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Flags.Orientation;

/**
 * ScrollHandler is part of SpaceVIL.HorizontalSlider and
 * SpaceVIL.VerticalSlider. ScrollHandler is responsible for handler dragging.
 * <p>
 * Supports all events including drag and drop.
 */
public class ScrollHandler extends Prototype implements IDraggable {
    private static int count = 0;

    /**
     * Specify orientation of ScrollHandler.
     * <p>
     * Can be com.spvessel.spacevil.Flags.Orientation.Vertical or
     * com.spvessel.spacevil.Flags.Orientation.Horizontal.
     */
    public Orientation orientation;
    private int _offset = 0;
    private int _diff = 0;

    /**
     * Default ScrollHandler constructor.
     */
    public ScrollHandler() {
        setItemName("ScrollHandler_" + count);
        eventMousePress.add(this::onMousePress);
        eventMouseDrag.add(this::onDragging);
        count++;
        isFocusable = false;
    }

    private void onMousePress(IItem sender, MouseArgs args) {
        if (orientation == Orientation.Horizontal)
            _diff = args.position.getX() - getX();
        else
            _diff = args.position.getY() - getY();
    }

    private void onDragging(IItem sender, MouseArgs args) {
        int offset;
        Prototype parent = getParent();
        if (orientation == Orientation.Horizontal)
            offset = args.position.getX() - parent.getX() - _diff;
        else
            offset = args.position.getY() - parent.getY() - _diff;

        setOffset(offset);
    }

    /**
     * Setting offset of the ScrollHandler by X axis or Y axis depending on
     * "orientation" property.
     * 
     * @param offset Offset of the ScrollHandler.
     */
    public void setOffset(int offset) {
        Prototype parent = getParent();
        if (parent == null)
            return;

        _offset = offset;

        if (orientation == Orientation.Horizontal)
            setX(_offset + parent.getX());
        else
            setY(_offset + parent.getY());
    }
}