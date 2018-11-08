package com.spvessel;

import com.spvessel.Core.InterfaceWindowAnchor;

public class WindowAnchor extends VisualItem implements InterfaceWindowAnchor {
    private static int count = 0;

    public WindowAnchor() {
        setItemName("WindowAnchor_" + count);
        count++;
    }
}