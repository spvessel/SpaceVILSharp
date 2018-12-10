package com.spvessel.spacevil.Core;

import com.spvessel.spacevil.Flags.KeyMods;

public final class TextInputArgs extends InputEventArgs {
    public Integer character;
    public KeyMods mods;

    @Override
    public void clear() {
        character = 0;
        mods = KeyMods.NO;
    }
}