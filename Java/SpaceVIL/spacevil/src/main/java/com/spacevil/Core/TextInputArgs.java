package com.spacevil.Core;

public final class TextInputArgs extends InputEventArgs {
    public Integer character;
    public com.spacevil.Flags.KeyMods mods;

    @Override
    public void clear() {
        character = 0;
        mods = com.spacevil.Flags.KeyMods.NO;
    }
}