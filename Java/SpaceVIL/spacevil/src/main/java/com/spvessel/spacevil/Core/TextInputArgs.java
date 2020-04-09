package com.spvessel.spacevil.Core;

import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.Flags.KeyMods;

/**
 * A class that describe keyboard text typing input.
 */
public final class TextInputArgs implements InterfaceInputEventArgs {
    /**
     * Character code.
     */
    public Integer character;

    /**
     * Used modifiers while typing.
     */
    public List<KeyMods> mods;

    /**
     * Clearing TextInputArgs.
     */
    public void clear() {
        character = 0;
        mods = new LinkedList<>();
        mods.add(KeyMods.NO);
    }
}