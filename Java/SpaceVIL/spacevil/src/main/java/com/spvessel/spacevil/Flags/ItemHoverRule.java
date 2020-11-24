package com.spvessel.spacevil.Flags;

/**
 * Item hovering rule types enum.
 * <p> Values: LAZY, STRICT.
 */
public enum ItemHoverRule {
    /**
     * Hover function will return True if mouse cursor located inside rectangle area of its shape even if shape is not a rectangle.
     * <p> Example: Function will return True If shape is triangle and mouse cursor located outside this triangle, 
     * but inside rectangle area that bounds this triangle.
     */
    Lazy,

    /**
     * Hover function will return True only if mouse cursor located inside the shape of the item.
     */
    Strict
}