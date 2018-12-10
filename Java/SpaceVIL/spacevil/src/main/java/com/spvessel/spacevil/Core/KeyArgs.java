package com.spvessel.spacevil.Core;

import com.spvessel.spacevil.Flags.InputState;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.KeyMods;

public final class KeyArgs extends InputEventArgs
{
    public KeyCode key;
    public int scancode;
    public InputState state;
    public KeyMods mods;

    @Override
    public void clear() {
        key = KeyCode.UNKNOWN;
        scancode = -1;
        state = InputState.RELEASE;
        mods = KeyMods.NO;
    }
}