package com.spvessel.spacevil.Core;

import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.Flags.InputState;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.MouseButton;

/**
 * A class that describes mouse inputs.
 */
public final class MouseArgs implements InterfaceInputEventArgs {
    /**
     * Mouse button as com.spvessel.spacevil.Flags.MouseButton.
     */
    public MouseButton button;

    /**
     * State of input as com.spvessel.spacevil.Flags.InputState. Values: RELEASE, PRESS, REPEAT.
     */
    public InputState state;

    /**
     * Used modifiers while mouse input.
     */
    public List<KeyMods> mods;

    /**
     * Mouse cursor position.
     */
    public Position position = new Position();

    /**
     * Clearing MouseArgs
     */
    public void clear() {
        button = MouseButton.UNKNOWN;
        state = InputState.RELEASE;
        mods = new LinkedList<>();
        mods.add(KeyMods.NO);
        position.setPosition(0, 0);
    }
}