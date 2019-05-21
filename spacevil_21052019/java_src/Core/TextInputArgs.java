package com.spvessel.spacevil.Core;

import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.Flags.KeyMods;

public final class TextInputArgs extends InputEventArgs {
    public Integer character;
    public List<KeyMods> mods;

    @Override
    public void clear() {
        character = 0;
        mods = new LinkedList<>();
        mods.add(KeyMods.NO);
    }
}