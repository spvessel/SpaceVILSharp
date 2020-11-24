package com.spvessel.spacevil.Flags;

/**
 * Enum of types of input conditions.
 * <p> Values: RELEASE, PRESS, REPEAT.
 */
public enum InputState {
    Release(0), Press(1), Repeat(2);

    private final int state;

    InputState(int state) {
        this.state = state;
    }

    public int getValue() {
        return state;
    }

    public static InputState getEnum(int state) {
        switch (state) {
        case 0:
            return InputState.Release;
        case 1:
            return InputState.Press;
        case 2:
            return InputState.Repeat;
        default:
            return InputState.Release;
        }
    }
}