package com.spvessel.spacevil.Core;

import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.Flags.InputState;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.MouseButton;

public final class MouseArgs extends InputEventArgs {
    public MouseButton button;
    public InputState state;
    public List<KeyMods> mods;
    public Pointer position = new Pointer();

    @Override
    public void clear() {
        button = MouseButton.UNKNOWN;
        state = InputState.RELEASE;
        mods = new LinkedList<>();
        mods.add(KeyMods.NO);
        position.clear();
    }
}