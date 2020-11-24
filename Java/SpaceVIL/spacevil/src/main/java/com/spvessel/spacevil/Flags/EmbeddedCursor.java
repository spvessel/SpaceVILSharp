package com.spvessel.spacevil.Flags;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum of types of embedded mouse cursors.
 * <p> Values: ARROW, IBEAM, CROSSHAIR, HAND, RESIZE_X, RESIZE_Y, RESIZE_XY.
 */
public enum EmbeddedCursor {
    Arrow(0x00036001), IBeam(0x00036002), Crosshair(0x00036003), Hand(0x00036004), ResizeX(0x00036005),
    ResizeY(0x00036006), ResizeXY(0x00036007);

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