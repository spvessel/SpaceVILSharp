package com.spvessel.Core;

import com.spvessel.Flags.InputState;
import com.spvessel.Flags.KeyMods;
import com.spvessel.Flags.MouseButton;

public final class MouseArgs extends InputEventArgs
{
    public MouseButton button;
    public InputState state;
    public KeyMods mods;
    public Pointer position = new Pointer();
    
    @Override
    public void clear()
        {
            button = MouseButton.UNKNOWN;
            state = InputState.RELEASE;
            mods = KeyMods.NO;
            position.clear();
        }
}