package com.spvessel.spacevil.Decorations;

/**
 * A structure that store indents of the item.
 */
public class Indents {
    /**
     * Indent from left side of the item.
     */
    public int left;

    /**
     * Indent from top side of the item.
     */
    public int top;

    /**
     * Indent from right side of the item.
     */
    public int right;

    /**
     * Indent from bottom side of the item.
     */
    public int bottom;
    
    /**
     * Constructs a Indents with default values (zero for each side)
     */
    public Indents() {
        left = 0;
        top = 0;
        right = 0;
        bottom = 0;
    }
    
    /**
     * Constructs a Indents with strict values for each side.
     * @param left Indent from left side of the item.
     * @param top Indent from top side of the item.
     * @param right Indent from right side of the item.
     * @param bottom Indent from bottom side of the item.
     */
    public Indents(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}