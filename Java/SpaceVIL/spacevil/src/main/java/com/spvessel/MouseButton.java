package com.spvessel;

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

    public static MouseButton getEnum(int button) {
        switch (button) {
        case 0:
            return MouseButton.BUTTON_1;
        case 1:
            return MouseButton.BUTTON_2;
        case 2:
            return MouseButton.BUTTON_3;
        case 3:
            return MouseButton.BUTTON_4;
        case 4:
            return MouseButton.BUTTON_5;
        case 5:
            return MouseButton.BUTTON_6;
        case 6:
            return MouseButton.BUTTON_7;
        case 7:
            return MouseButton.BUTTON_8;
        default:
            return MouseButton.UNKNOWN;
        }
    }
}