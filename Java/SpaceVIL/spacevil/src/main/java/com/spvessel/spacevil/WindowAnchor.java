package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.IWindowAnchor;

/**
 * WindowAnchor is class representing the draggable window type of an item.
 * Supports all events except drag and drop despite that this class is draggable
 * type.
 */
public class WindowAnchor extends Prototype implements IWindowAnchor {
    private static int count = 0;

    /**
     * Default WindowAnchor constructor.
     */
    public WindowAnchor() {
        setItemName("WindowAnchor_" + count);
        count++;
    }
}