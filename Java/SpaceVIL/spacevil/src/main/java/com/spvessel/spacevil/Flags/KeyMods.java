package com.spvessel.spacevil.Flags;

import java.util.List;
import java.util.LinkedList;

/**
 * Enum of keyboard modifiers.
 */
public enum KeyMods {
    No(0),
    Shift(1),
    Control(2),
    Alt(4),
    /**
     * Can be Windows key (in WinOS), Command key (in MacOS)
     */
    Super(8);

    private final int mods;

    KeyMods(int mods) {
        this.mods = mods;
    }

    public int getValue() {
        return mods;
    }

    public static KeyMods getEnum(int mod) {
        for (KeyMods k : KeyMods.values()) {
            if (k.getValue() == mod)
                return k;
        }
        return No;
    }

    public static List<KeyMods> getEnums(int mod) {
        List<KeyMods> list = new LinkedList<>();
        for (KeyMods k : KeyMods.values()) {
            if ((k.getValue() & mod) > 0)
                list.add(k);
        }
        if (list.size() > 0)
            return list;

        list.add(KeyMods.No);
        return list;
    }
}