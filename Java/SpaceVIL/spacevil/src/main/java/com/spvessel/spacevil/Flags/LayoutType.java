package com.spvessel.spacevil.Flags;

/**
 * Enum of items types.
 * <p> Values: STATIC, FLOATING, DIALOG.
 */
public enum LayoutType {
    /**
     * Items whose parent LayoutType is STATIC.
     */
    STATIC,
    
    /**
     * Items whose root parent LayoutType is FLOATING.
     */
    FLOATING,
    
    /**
     * Items whose root parent LayoutType is DIALOG.
     */
    DIALOG
}