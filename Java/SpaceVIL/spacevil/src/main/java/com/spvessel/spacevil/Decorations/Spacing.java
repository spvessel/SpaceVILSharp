package com.spvessel.spacevil.Decorations;

/**
 * A class that store vertical and horizontal spacing values of the item.
 */
public class Spacing {
    /**
     * Horizontal indent between items.
     */
    public int horizontal;

    /**
     * Vertical indent between items.
     */
    public int vertical;

    /**
     * Constructs a Spacing with default values (zero)
     */
    public Spacing() {
        horizontal = 0;
        vertical = 0;
    }

    /**
     * Constructs a Spacing with strict horizontal and vertical spacing values.
     * @param horizontal Horizontal indent between items.
     * @param vertical Vertical indent between items.
     */
    public Spacing(int horizontal, int vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
}