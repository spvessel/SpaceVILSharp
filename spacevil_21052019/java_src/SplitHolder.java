package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceDraggable;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.EmbeddedCursor;
import com.spvessel.spacevil.Flags.Orientation;
import com.spvessel.spacevil.Flags.SizePolicy;

public class SplitHolder extends Prototype implements InterfaceDraggable {
    private static int count = 0;
    private Orientation _orientation;

    /**
     * @return Orientation of the SplitHolder (HORIZONTAL or VERTICAL)
     */
    public Orientation getOrientation() {
        return _orientation;
    }

    /**
     * Constructs a SplitHolder with orientation (HORIZONTAL or VERTICAL)
     */
    public SplitHolder(Orientation orientation) {
        _orientation = orientation;
        setItemName("SplitHolder_" + count);
        count++;
        isFocusable = false;
        makeHolderShape();
    }

    private int _spacerSize = 6;

    /**
     * SplitHolder size (height for the HORIZONTAL orientation,
     * width for the VERTICAL orientation)
     */
    public void setSpacerSize(int spSize) {
        if (_spacerSize != spSize) {
            _spacerSize = spSize;
            makeHolderShape();
        }
    }
    public int getHolderSize() {
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
     * Set style of the SplitHolder
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        setBackground(style.background);
    }
}