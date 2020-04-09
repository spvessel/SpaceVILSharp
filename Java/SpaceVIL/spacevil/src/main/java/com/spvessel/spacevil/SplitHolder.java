package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceDraggable;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.EmbeddedCursor;
import com.spvessel.spacevil.Flags.Orientation;
import com.spvessel.spacevil.Flags.SizePolicy;

/**
 * SplitHolder is part of SpaceVIL.HorizontalSplitArea and
 * com.spvessel.spacevil.VerticalSplitArea. SplitHolder is responsible for 
 * handler dragging.
 * <p> Supports all events including drag and drop.
 */
public class SplitHolder extends Prototype implements InterfaceDraggable {
    private static int count = 0;
    private Orientation _orientation;
    private int _spacerSize = 6;

    /**
     * Constructs a SplitHolder with the specified orientation.
     * <p> Orientation can be Orientation.HORIZONTAL 
     * or Orientation.VERTICAL.
     * @param orientation Orientation of SplitHolder.
     */
    public SplitHolder(Orientation orientation) {
        _orientation = orientation;
        setItemName("SplitHolder_" + count);
        count++;
        isFocusable = false;
        makeHolderShape();
    }

    /**
     * Setting thickness of SplitHolder divider.
     * @param thickness Thickness of SplitHolder divider.
     */
    public void setDividerSize(int thickness) {
        if (_spacerSize != thickness) {
            _spacerSize = thickness;
            makeHolderShape();
        }
    }

    /**
     * Getting thickness of SplitHolder divider.
     * @return Thickness of SplitHolder divider.
     */
    public int getDividerSize() {
        return _spacerSize;
    }

    private void makeHolderShape() {
        switch (_orientation) {
        case VERTICAL:
            setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
            setWidth(_spacerSize);
            setCursor(EmbeddedCursor.RESIZE_X);
            // setMinWidth(_spacerSize);
            break;
            
            case HORIZONTAL:
            setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
            setHeight(_spacerSize);
            setCursor(EmbeddedCursor.RESIZE_Y);
            // setMinHeight(_spacerSize);
            break;
        }
    }

    /**
     * Getting SplitHolder orientation.
     * <p> Orientation can be Orientation.HORIZONTAL 
     * or Orientation.VERTICAL.
     * @return Current SplitHolder orientation.
     */
    public Orientation getOrientation() {
        return _orientation;
    }

    /**
     * Set style of the SplitHolder.
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null) {
            return;
        }
        setBackground(style.background);
    }
}