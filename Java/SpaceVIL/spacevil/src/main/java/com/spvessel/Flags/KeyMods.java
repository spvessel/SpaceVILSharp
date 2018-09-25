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
}