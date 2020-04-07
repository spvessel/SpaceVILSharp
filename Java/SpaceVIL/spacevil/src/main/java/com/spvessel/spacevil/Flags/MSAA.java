package com.spvessel.spacevil.Flags;

/**
 * Multisample anti-aliasing enum.
 * <p> Values: No, MSAA2x, MSAA4x, MSAA8x.
 */
public enum MSAA {
    NO(0), MSAA_2X(2), MSAA_4X(4), MSAA_8X(8);

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
            return MSAA.NO;
        case 2:
            return MSAA.MSAA_2X;
        case 4:
            return MSAA.MSAA_4X;
        case 8:
            return MSAA.MSAA_8X;
        default:
            return MSAA.NO;
        }
    }
}
