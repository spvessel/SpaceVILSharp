package com.spvessel.Decorations;

public class Indents {
    public int left;
    public int top;
    public int right;
    public int bottom;

    public Indents() {
        left = 0;
        top = 0;
        right = 0;
        bottom = 0;
    }

    public Indents(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}