package com.spvessel;

public final class TextInputArgs extends InputEventArgs {
    public Integer character;
    public KeyMods mods;

    @Override
    public void clear() {
        character = 0;
        mods = KeyMods.NO;
    }
}