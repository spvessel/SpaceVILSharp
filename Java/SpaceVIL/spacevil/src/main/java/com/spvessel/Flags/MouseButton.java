package com.spvessel.Flags;

public enum MouseButton {
    UNKNOWN(-1), BUTTON_1(0), BUTTON_2(1), BUTTON_3(2), BUTTON_4(3), BUTTON_5(4), BUTTON_6(5), BUTTON_7(6), BUTTON_8(7),
    BUTTON_LAST(7), BUTTON_LEFT(0), BUTTON_RIGHT(1), BUTTON_MIDDLE(2);

    private final int button;

    MouseButton(int button) {
        this.button = button;
    }

    public int getValue() {
        return button;
    }
}