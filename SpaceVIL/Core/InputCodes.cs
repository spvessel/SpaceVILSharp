using System;
namespace SpaceVIL.Core
{
    internal enum InputMode
    {
        Cursor = 0x00033001,
        StickyKeys = 0x00033002,
        StickyMouseButton = 0x00033003
    }

    internal enum Joystick
    {
        Joystick1 = 0,
        Joystick2 = 1,
        Joystick3 = 2,
        Joystick4 = 3,
        Joystick5 = 4,
        Joystick6 = 5,
        Joystick7 = 6,
        Joystick8 = 7,
        Joystick9 = 8,
        Joystick10 = 9,
        Joystick11 = 10,
        Joystick12 = 11,
        Joystick13 = 12,
        Joystick14 = 13,
        Joystick15 = 14,
        Joystick16 = 15,
        JoystickLast = Joystick16
    }
}