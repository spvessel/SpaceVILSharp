package com.spvessel.Flags;

public enum KeyMods {
    NO(0), SHIFT(1), CONTROL(2), ALT(4), SUPER(8);

    private final int mods;

    KeyMods(int mods) {
        this.mods = mods;
    }

    public int getValue() {
        return mods;
    }

    public static KeyMods getEnum(int mod) {
        switch (mod) {
        case 0:
            return KeyMods.NO;
        case 1:
            return KeyMods.SHIFT;
        case 2:
            return KeyMods.CONTROL;
        case 4:
            return KeyMods.ALT;
        case 8:
            return KeyMods.SUPER;
        default:
            return KeyMods.NO;
        }
    }
}