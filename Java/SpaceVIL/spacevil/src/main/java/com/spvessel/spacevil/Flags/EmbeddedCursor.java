package com.spvessel.spacevil.Flags;

import java.util.HashMap;
import java.util.Map;

public enum EmbeddedCursor {
    ARROW(0x00036001), IBEAM(0x00036002), CROSSHAIR(0x00036003), HAND(0x00036004), RESIZE_X(0x00036005),
    RESIZE_Y(0x00036006);

    private int value;
    private static Map<Integer, EmbeddedCursor> map = new HashMap<>();

    private EmbeddedCursor(int value) {
        this.value = value;
    }

    static {
        for (EmbeddedCursor cursor : EmbeddedCursor.values()) {
            map.put(cursor.value, cursor);
        }
    }

    public static EmbeddedCursor valueOf(int type) {
        return (EmbeddedCursor) map.get(type);
    }

    public int getValue() {
        return value;
    }
}