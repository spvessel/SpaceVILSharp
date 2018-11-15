using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    [Flags]
    public enum ItemAlignment
    {
        Left = 0x01,
        Top = 0x02,
        Right = 0x04,
        Bottom = 0x08,
        HCenter = 0x10,
        VCenter = 0x20
    }

    [Flags]
    public enum SizePolicy
    {
        Fixed = 0x01,//размер фиксированный, никаких изменений фигуры, только выравнивание
        Stretch = 0x02,//размер фиксированный, фигура растягивается в пропорциях фигуры к окну, выравнивае работает некорректно
        Expand = 0x04,//размер плавающий, размер фигуры подстраивается под все доступное пространство, выравнивание работает корректно
        Ignored = 0x08//не используется (будет работать как Fixed)
    }

    [Flags]
    public enum SizeType
    {
        Width,
        Height
    };

    [Flags]
    public enum ItemStateType
    {
        Base = 0x01,
        Hovered = 0x02,
        Pressed = 0x04,
        Toggled = 0x08,
        Focused = 0x10,
        Disabled = 0x20
    };

    public enum UpdateType
    {
        Critical,
        CoordsOnly
    }

    public enum Orientation
    {
        Vertical,
        Horizontal
    }

    public enum ScrollBarVisibility
    {
        Always,
        AsNeeded,
        Never
    }

    [Flags]
    public enum ListPosition
    {
        No = 0x00,
        Top = 0x01,
        Bottom = 0x02,
        Left = 0x04,
        Right = 0x08,
    }

    public enum ItemRule
    {
        Lazy,
        Strict
    }

    // public enum PrimitiveType
    // {
    //     No,
    //     Rectangle,
    //     RoundedRectangle,
    //     Ellipse,
    //     Triangle,
    //     Star,
    //     CustomShape
    // }

    public enum EmbeddedFont
    {
        Ubuntu
    }

    public enum GeometryEventType
    {
        Focused = 0x01,
        Moved_X = 0x02,
        Moved_Y = 0x04,
        ResizeWidth = 0x08,
        ResizeHeight = 0x10
    }

    public enum HorizontalDirection
    {
        FromLeftToRight,
        FromRightToLeft,
    }

    [Flags]
    public enum InputEventType
    {
        FocusGet = 0x01,
        FocusLost = 0x02,
        Resized = 0x04,
        Destroy = 0x08,
        ValueChanged = 0x10,
        SelectionChanged = 0x20,
        IndexChanged = 0x40,

        MouseMove = 0x80,
        MouseHover = 0x400000,
        MouseDrag = 0x800000,
        MousePressed = 0x100,
        MouseRelease = 0x200,
        MouseScroll = 0x400,

        KeyPress = 0x800,
        KeyRepeat = 0x1000,
        KeyRelease = 0x2000,
        TextInput = 0x200000,

        WindowResize = 0x4000,
        WindowMove = 0x8000,
        WindowMinimize = 0x10000,
        WindowRestore = 0x20000,
        WindowClose = 0x40000,
        WindowGetFocus = 0x80000,
        WindowLostFocus = 0x100000,
    }

    public enum InputState
    {
        Release = 0,
        Press = 1,
        Repeat = 2
    }

    public enum KeyCode
    {
        Unknown = -1,

        // Printable keys
        Space = 32,
        Apostrophe = 39,  // '
        Comma = 44,  // ,
        Minus = 45,  // -
        Period = 46,  // .
        Slash = 47,  // /
        Alpha0 = 48,
        Alpha1 = 49,
        Alpha2 = 50,
        Alpha3 = 51,
        Alpha4 = 52,
        Alpha5 = 53,
        Alpha6 = 54,
        Alpha7 = 55,
        Alpha8 = 56,
        Alpha9 = 57,
        SemiColon = 59,  // ;
        Equal = 61,  // =
        A = 65,
        B = 66,
        C = 67,
        D = 68,
        E = 69,
        F = 70,
        G = 71,
        H = 72,
        I = 73,
        J = 74,
        K = 75,
        L = 76,
        M = 77,
        N = 78,
        O = 79,
        P = 80,
        Q = 81,
        R = 82,
        S = 83,
        T = 84,
        U = 85,
        V = 86,
        W = 87,
        X = 88,
        Y = 89,
        Z = 90,

        a = 97,
        b = 98,
        c = 99,
        d = 100,
        e = 101,
        f = 102,
        g = 103,
        h = 104,
        i = 105,
        j = 106,
        k = 107,
        l = 108,
        m = 109,
        n = 110,
        o = 111,
        p = 112,
        q = 113,
        r = 114,
        s = 115,
        t = 116,
        u = 117,
        v = 118,
        w = 119,
        x = 120,
        y = 121,
        z = 122,

        LeftBracket = 91,  // [
        Backslash = 92,  // \
        RightBracket = 93,  // ]
        GraveAccent = 96,  // `
        World1 = 161, // Non-US #1
        World2 = 162, // Non-US #2

        // Function keys
        Escape = 256,
        Enter = 257,
        Tab = 258,
        Backspace = 259,
        Insert = 260,
        Delete = 261,
        Right = 262,
        Left = 263,
        Down = 264,
        Up = 265,
        PageUp = 266,
        PageDown = 267,
        Home = 268,
        End = 269,
        CapsLock = 280,
        ScrollLock = 281,
        NumLock = 282,
        PrintScreen = 283,
        Pause = 284,
        F1 = 290,
        F2 = 291,
        F3 = 292,
        F4 = 293,
        F5 = 294,
        F6 = 295,
        F7 = 296,
        F8 = 297,
        F9 = 298,
        F10 = 299,
        F11 = 300,
        F12 = 301,
        F13 = 302,
        F14 = 303,
        F15 = 304,
        F16 = 305,
        F17 = 306,
        F18 = 307,
        F19 = 308,
        F20 = 309,
        F21 = 310,
        F22 = 311,
        F23 = 312,
        F24 = 313,
        F25 = 314,
        Numpad0 = 320,
        Numpad1 = 321,
        Numpad2 = 322,
        Numpad3 = 323,
        Numpad4 = 324,
        Numpad5 = 325,
        Numpad6 = 326,
        Numpad7 = 327,
        Numpad8 = 328,
        Numpad9 = 329,
        NumpadDecimal = 330,
        NumpadDivide = 331,
        NumpadMultiply = 332,
        NumpadSubtract = 333,
        NumpadAdd = 334,
        NumpadEnter = 335,
        NumpadEqual = 336,
        LeftShift = 340,
        LeftControl = 341,
        LeftAlt = 342,
        LeftSuper = 343,
        RightShift = 344,
        RightControl = 345,
        RightAlt = 346,
        RightSuper = 347,
        Menu = 348
    }

    [Flags]
    public enum KeyMods
    {
        Shift = 0x0001,
        Control = 0x0002,
        Alt = 0x0004,
        Super = 0x0008
    }

    public enum MouseButton
    {
        Unknown = -1,
        Button1 = 0,
        Button2 = 1,
        Button3 = 2,
        Button4 = 3,
        Button5 = 4,
        Button6 = 5,
        Button7 = 6,
        Button8 = 7,
        ButtonLast = Button8,
        ButtonLeft = Button1,
        ButtonRight = Button2,
        ButtonMiddle = Button3
    }

    internal enum LayoutType
    {
        Static,
        Floating
    }

    public enum TreeItemType
    {
        Leaf,
        Branch
    }

    public enum InputRestriction
    {
        IntNumbers, DoubleNumbers, Letters, All
    }
}
