package com.spvessel.spacevil.Decorations;

public class Indents {
    /**
     * A class that store indents of the object
     */
    public int left;
    public int top;
    public int right;
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
     * Constructs a Indents with strict values for each side
     */
    public Indents(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}