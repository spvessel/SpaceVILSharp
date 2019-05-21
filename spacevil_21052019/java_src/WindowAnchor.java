package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceWindowAnchor;

public class WindowAnchor extends Prototype implements InterfaceWindowAnchor {
    private static int count = 0;

    public WindowAnchor() {
        setItemName("WindowAnchor_" + count);
        count++;
        // isFocusable = false;
    }
}