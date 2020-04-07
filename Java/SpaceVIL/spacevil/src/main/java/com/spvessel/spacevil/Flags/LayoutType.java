package com.spvessel.spacevil.Flags;

/**
 * Enum of items types.
 * <p> Values: Static, Floating, Dialog.
 */
public enum LayoutType {
    /**
     * Items whose parent LayoutType is Static.
     */
    STATIC,
    
    /**
     * Items whose root parent LayoutType is Floating.
     */
    FLOATING,
    
    /**
     * Items whose root parent LayoutType is Dialog.
     */
    DIALOG
}