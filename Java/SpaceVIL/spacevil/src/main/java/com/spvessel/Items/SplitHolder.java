package com.spvessel.Items;

import com.spvessel.Cores.*;
import com.spvessel.Flags.Orientation;

public class SplitHolder extends VisualItem {
    private static int count = 0;
    private Orientation _orientation;

    public Orientation getOrientation() {
        return _orientation;
    }

    public SplitHolder() {
        setItemName("SplitHolder_" + count);
        count++;
    }
}