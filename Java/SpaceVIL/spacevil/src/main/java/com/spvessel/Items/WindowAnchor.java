package com.spvessel.Items;

import com.spvessel.Cores.InterfaceWindowAnchor;

public class WindowAnchor extends VisualItem implements InterfaceWindowAnchor {
    private static int count = 0;

    public WindowAnchor() {
        setItemName("WindowAnchor_" + count);
        count++;
    }
}