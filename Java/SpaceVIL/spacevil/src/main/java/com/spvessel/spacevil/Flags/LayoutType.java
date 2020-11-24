package com.spvessel.spacevil.Flags;

/**
 * Enum of items types.
 * <p> Values: STATIC, FLOATING, DIALOG.
 */
public enum LayoutType {
    /**
     * Items whose parent LayoutType is STATIC.
     */
    Static,
    
    /**
     * Items whose root parent LayoutType is FLOATING.
     */
    Floating,
    
    /**
     * Items whose root parent LayoutType is DIALOG.
     */
    Dialog
}