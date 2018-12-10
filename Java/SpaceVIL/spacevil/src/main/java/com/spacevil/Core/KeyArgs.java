package com.spacevil.Core;

public final class KeyArgs extends InputEventArgs
{
    public com.spacevil.Flags.KeyCode key;
    public int scancode;
    public com.spacevil.Flags.InputState state;
    public com.spacevil.Flags.KeyMods mods;

    @Override
    public void clear() {
        key = com.spacevil.Flags.KeyCode.UNKNOWN;
        scancode = -1;
        state = com.spacevil.Flags.InputState.RELEASE;
        mods = com.spacevil.Flags.KeyMods.NO;
    }
}