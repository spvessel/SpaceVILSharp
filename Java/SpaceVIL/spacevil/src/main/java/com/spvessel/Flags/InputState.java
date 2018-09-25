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
}