package com.spvessel.Decorations;

public class Spacing {
    /**
     * A class that store vertical and horizontal spacing values of the object
     */
    public int horizontal;
    public int vertical;

    /**
     * Constructs a Spacing with default values (zero)
     */
    public Spacing() {
        horizontal = 0;
        vertical = 0;
    }

    /**
     * Constructs a Spacing with strict horizontal and vertical spacing values
     */
    public Spacing(int horizontal, int vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
}