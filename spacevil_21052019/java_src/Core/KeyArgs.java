package com.spvessel.spacevil.Core;

import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.Flags.InputState;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.KeyMods;

public final class KeyArgs extends InputEventArgs {
    public KeyCode key;
    public int scancode;
    public InputState state;
    public List<KeyMods> mods;

    @Override
    public void clear() {
        key = KeyCode.UNKNOWN;
        scancode = -1;
        state = InputState.RELEASE;
        mods = new LinkedList<>();
        mods.add(KeyMods.NO);
    }
}