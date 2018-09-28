package com.spvessel.Cores;

import com.spvessel.Flags.*;

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