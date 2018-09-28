package com.spvessel.Cores;

import com.spvessel.Flags.*;

public final class KeyArgs extends InputEventArgs
{
    public KeyCode key;
    public Integer scancode;
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