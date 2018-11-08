package com.spvessel.Flags;

public enum InputState {
    RELEASE(0), PRESS(1), REPEAT(2);

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
            return InputState.RELEASE;
        case 1:
            return InputState.PRESS;
        case 2:
            return InputState.REPEAT;
        default:
            return InputState.RELEASE;
        }
    }
}