package com.spvessel.spacevil.Flags;

/**
 * Multisample anti-aliasing enum.
 * <p> Values: NO, MSAA_2X, MSAA_4X, MSAA_8X.
 */
public enum MSAA {
    No(0), MSAA2x(2), MSAA4x(4), MSAA8x(8);

    private final int state;

    MSAA(int state) {
        this.state = state;
    }

    public int getValue() {
        return state;
    }

    public static MSAA getEnum(int state) {
        switch (state) {
        case 0:
            return MSAA.No;
        case 2:
            return MSAA.MSAA2x;
        case 4:
            return MSAA.MSAA4x;
        case 8:
            return MSAA.MSAA8x;
        default:
            return MSAA.No;
        }
    }
}
