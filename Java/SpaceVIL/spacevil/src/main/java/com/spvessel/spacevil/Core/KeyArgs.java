package com.spvessel.spacevil.Core;

import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.Flags.InputState;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.KeyMods;

/**
 * A class that describes keyboard key input.
 */
public final class KeyArgs implements InterfaceInputEventArgs {
    /**
     * Key code as com.spvessel.spacevil.Flags.KeyCode.
     */
    public KeyCode key;

    /**
     * Scancode of key.
     */
    public int scancode;

    /**
     * State of input as com.spvessel.spacevil.Flags.InputState. Values: RELEASE, PRESS, REPEAT.
     */
    public InputState state;

    /**
     * Used modifiers while typing.
     */
    public List<KeyMods> mods;

    /**
     * Clearing KeyArgs.
     */
    public void clear() {
        key = KeyCode.UNKNOWN;
        scancode = -1;
        state = InputState.RELEASE;
        mods = new LinkedList<>();
        mods.add(KeyMods.NO);
    }
}