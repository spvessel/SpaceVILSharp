package com.spacevil.Core;

public final class MouseArgs extends InputEventArgs
{
    public com.spacevil.Flags.MouseButton button;
    public com.spacevil.Flags.InputState state;
    public com.spacevil.Flags.KeyMods mods;
    public Pointer position = new Pointer();
    
    @Override
    public void clear()
        {
            button = com.spacevil.Flags.MouseButton.UNKNOWN;
            state = com.spacevil.Flags.InputState.RELEASE;
            mods = com.spacevil.Flags.KeyMods.NO;
            position.clear();
        }
}