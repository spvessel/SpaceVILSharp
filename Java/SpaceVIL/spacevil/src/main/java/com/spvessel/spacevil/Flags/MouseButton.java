package com.spvessel.spacevil.Flags;

/**
 * Enum of button codes of mouse.
 */
public enum MouseButton {
    Unknown(-1),
    Button4(3), Button5(4), Button6(5), Button7(6), Button8(7),
    ButtonLeft(0), ButtonRight(1), ButtonMiddle(2);

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
            return MouseButton.ButtonLeft;
        case 1:
            return MouseButton.ButtonRight;
        case 2:
            return MouseButton.ButtonMiddle;
        case 3:
            return MouseButton.Button4;
        case 4:
            return MouseButton.Button5;
        case 5:
            return MouseButton.Button6;
        case 6:
            return MouseButton.Button7;
        case 7:
            return MouseButton.Button8;
        default:
            return MouseButton.Unknown;
        }
    }
}