package com.spvessel.Items;

import com.spvessel.Flags.*;
import com.spvessel.Cores.InterfaceDraggable;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.Orientation;

public class SplitHolder extends VisualItem implements InterfaceDraggable {
    private static int count = 0;
    private Orientation _orientation;

    public Orientation getOrientation() {
        return _orientation;
    }

    public SplitHolder(Orientation orientation) {
        _orientation = orientation;
        setItemName("SplitHolder_" + count);
        count++;
        makeHolderShape();
    }

    private int _spacerSize = 6;

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
            setMinWidth(_spacerSize);
            break;

        case HORIZONTAL:
            setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
            setHeight(_spacerSize);
            setMinHeight(_spacerSize);
            break;
        }
    }

    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        setBackground(style.background);
    }
}