package com.spvessel.spacevil.Flags;

/**
 * Enums of types of input events.
 */
public enum InputEventType {
    Empty(0), FocusGet(0x01), FocusLost(0x02), Resized(0x04), Destroy(0x08), ValueChanged(0x10),
    SelectionChanged(0x20), IndexChanged(0x40),

    MouseMove(0x80), MouseHover(0x400000), MouseLeave(0x4000000), MouseDrag(0x800000), MousePress(0x100), MouseRelease(0x200),
    MouseScroll(0x400), MouseDoubleClick(0x1000000),

    KeyPress(0x800), KeyRepeat(0x1000), KeyRelease(0x2000), TextInput(0x200000),

    WindowResize(0x4000), WindowMove(0x8000), WindowMinimize(0x10000), WindowRestore(0x20000),
    WindowClose(0x40000), WindowGetFocus(0x80000), WindowLostFocus(0x100000), WindowDrop(0x2000000), WindowMaximize(0x4000000);

    private final int type;

    InputEventType(int type) {
        this.type = type;
    }

    public int getValue() {
        return type;
    }
}