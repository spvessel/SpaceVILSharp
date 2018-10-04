package com.spvessel.Flags;

public enum ListPosition {
    TOP(0x01), BOTTOM(0x02), LEFT(0x04), RIGHT(0x08);

    private final int pos;

    ListPosition(int pos) {
        this.pos = pos;
    }

    public int getValue() {
        return pos;
    }
}