package com.spvessel;

public class WindowAnchor extends VisualItem implements InterfaceWindowAnchor {
    private static int count = 0;

    public WindowAnchor() {
        setItemName("WindowAnchor_" + count);
        count++;
    }
}