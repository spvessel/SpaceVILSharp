using System;

namespace SpaceVIL.Core
{
    /// <summary>
    /// Multisample anti-aliasing enum.
    /// <para/> Values: No, MSAA2x, MSAA4x, MSAA8x.
    /// </summary>
    public enum MSAA
    {
        No = 0,
        MSAA2x = 2,
        MSAA4x = 4,
        MSAA8x = 8
    }

    /// <summary>
    /// Operating system types enum.
    /// <para/> Values: Windows, Linux, Mac.
    /// </summary>
    public enum OSType
    {
        Windows,
        Linux,
        Mac
    }

    /// <summary>
    /// Alignment types enum.
    /// <para/> Values: Left, Top, Right, Bottom, HCenter, VCenter.
    /// </summary>
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

    /// <summary>
    /// Size policy types enum.
    /// <para/> Values: Fixed, Expand.
    /// </summary>
    [Flags]
    public enum SizePolicy
    {
        /// <summary>
        /// Size is fixed and cannot be changed.
        /// </summary>
        Fixed = 0x01,

        /// <summary>
        /// The form will be stretched inside the container to all available space.
        /// </summary>
        Expand = 0x04
    }

    /// <summary>
    /// Item state types enum.
    /// <para/> Values: Base, Hovered, Pressed, Toggled, Focused, Disabled.
    /// </summary>
    [Flags]
    public enum ItemStateType
    {
        /// <summary>
        /// Base static item's condition. 
        /// <para/> Where Item is class extended from SpaceVIL.Prototype.
        /// </summary>
        Base = 0x01,

        /// <summary>
        /// Item's condition when mouse cursor inside items area. 
        /// <para/> Where Item is class extended from SpaceVIL.Prototype.
        /// </summary>
        Hovered = 0x02,

        /// <summary>
        /// Item's condition when mouse cursor inside items area and any mouse button is pressed.
        /// <para/> Where Item is class extended from SpaceVIL.Prototype.
        /// </summary>
        Pressed = 0x04,

        /// <summary>
        /// Item's condition when it is toggled.
        /// <para/> Where Item is class extended from SpaceVIL.Prototype.
        /// </summary>
        Toggled = 0x08,

        /// <summary>
        /// Item's condition when it is focused.
        /// <para/> Where Item is class extended from SpaceVIL.Prototype.
        /// </summary>
        Focused = 0x10,

        /// <summary>
        /// Item's condition when it is disabled.
        /// <para/> Where Item is class extended from SpaceVIL.Prototype.
        /// </summary>
        Disabled = 0x20
    };

    /// <summary>
    /// Orientation enum. 
    /// <para/> Used in such items as scroll bars, sliders, wrap grid and etc.
    /// <para/> Values: Vertical, Horizontal.
    /// </summary>
    public enum Orientation
    {
        Vertical,
        Horizontal
    }

    /// <summary>
    /// Visibility types of item enum. Used in such items as scroll bars.
    /// <para/> Values: Always, AsNeeded, Never.
    /// </summary>
    public enum VisibilityPolicy
    {
        /// <summary>
        /// Item is always visible.
        /// </summary>
        Always,

        /// <summary>
        /// Item can be visible in some circumstances.
        /// </summary>
        AsNeeded,

        /// <summary>
        /// Item is always invisible.
        /// </summary>
        Never
    }

    /// <summary>
    /// Item hovering rule types enum.
    /// <para/> Values: Lazy, Strict.
    /// </summary>
    public enum ItemHoverRule
    {
        /// <summary>
        /// Hover function will return True if mouse cursor located inside rectangle area of its shape even if shape is not a rectangle.
        /// <para/> Example: Function will return True If shape is triangle and mouse cursor located outside this triangle, 
        /// but inside rectangle area that bounds this triangle.
        /// </summary>
        Lazy,
        
        /// <summary>
        /// Hover function will return True only if mouse cursor located inside the shape of the item.
        /// </summary>
        Strict
    }

    /// <summary>
    /// Enum of SpaceVIL embedded fonts.
    /// </summary>
    public enum EmbeddedFont
    {
        Ubuntu
    }

    /// <summary>
    /// Enum of event types directly related to events that change the size and position of an item.
    /// <para/> Where Item is class extended from SpaceVIL.Prototype.
    /// <para/> Values: Focused, MovedX, MovedY, ResizeWidth, ResizeHeight.
    /// </summary>
    public enum GeometryEventType
    {
        Focused = 0x01,
        MovedX = 0x02,
        MovedY = 0x04,
        ResizeWidth = 0x08,
        ResizeHeight = 0x10
    }

    /// <summary>
    /// Enum of types of horizontal directions.
    /// <para/> Values: FromLeftToRight, FromRightToLeft.
    /// </summary>
    public enum HorizontalDirection
    {
        FromLeftToRight,
        FromRightToLeft,
    }

    /// <summary>
    /// Enums of types of input events.
    /// </summary>
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
        MouseLeave = 0x4000000,
        MouseDrag = 0x800000,
        MousePress = 0x100,
        MouseRelease = 0x200,
        MouseDoubleClick = 0x1000000,
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
        WindowDrop = 0x2000000,
        WindowMaximize = 0x4000000,
    }

    /// <summary>
    /// Enum of types of input conditions.
    /// <para/> Values: Release, Press, Repeat.
    /// </summary>
    public enum InputState
    {
        Release = 0,
        Press = 1,
        Repeat = 2
    }

    /// <summary>
    /// Enum of key codes of keyboard.
    /// </summary>
    public enum KeyCode
    {
        Unknown = -1,

        // Printable keys
        Space = 32,
        Apostrophe = 39,
        Comma = 44,
        Minus = 45,
        Period = 46,
        Slash = 47,
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
        SemiColon = 59,
        Equal = 61,
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

        LeftBracket = 91,
        Backslash = 92,
        RightBracket = 93,
        GraveAccent = 96,
        World1 = 161,
        World2 = 162,

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

    /// <summary>
    /// Enum of keyboard modifiers.
    /// </summary>
    [Flags]
    public enum KeyMods
    {
        Shift = 0x0001,
        Control = 0x0002,
        Alt = 0x0004,
        /// <summary>
        /// Can be Windows key (in WinOS), Command key (in MacOS)
        /// </summary>
        Super = 0x0008
    }

    /// <summary>
    /// Enum of button codes of mouse.
    /// </summary>
    public enum MouseButton
    {
        Unknown = -1,
        ButtonLeft = 0,
        ButtonRight = 1,
        ButtonMiddle = 2,
        Button4 = 3,
        Button5 = 4,
        Button6 = 5,
        Button7 = 6,
        Button8 = 7,
    }

    /// <summary>
    /// Enum of items types.
    /// <para/> Values: Static, Floating, Dialog.
    /// </summary>
    public enum LayoutType
    {
        /// <summary>
        /// Items whose parent LayoutType is Static.
        /// </summary>
        Static,

        /// <summary>
        /// Items whose root parent LayoutType is Floating.
        /// </summary>
        Floating,

        /// <summary>
        /// Items whose root parent LayoutType is Dialog.
        /// </summary>
        Dialog
    }

    /// <summary>
    /// Enum of types of TreeItems. Used in TreeView and TreeItem.
    /// </summary>
    public enum TreeItemType
    {
        Leaf,
        Branch
    }

    // public enum InputRestriction
    // {
    //     IntNumbers, DoubleNumbers, Letters, All
    // }

    /// <summary>
    /// Enum of SpaceVIL embedded images.
    /// </summary>
    public enum EmbeddedImage
    {
        Add, ArrowLeft, ArrowUp, Eye, File, Folder, FolderPlus, Gear, Import, Lines, Loupe, RecycleBin, Refresh, Pencil, Diskette,
        Eraser, Home, User, Drive, Filter, LoadCircle
    }
    /// <summary>
    /// Enum of SpaceVIL embedded images sizes.
    /// </summary>
    public enum EmbeddedImageSize
    {
        Size32x32, Size64x64
    }
    /// <summary>
    /// Enum of file system entry types.
    /// <para/> Values: File, Directory.
    /// </summary>
    public enum FileSystemEntryType
    {
        File, Directory//, Network, Drive
    }
    /// <summary>
    /// Enum of open dialog types.
    /// <para/> Values: Open, Save.
    /// </summary>
    public enum OpenDialogType
    {
        Open, 
        Save
    }
    /// <summary>
    /// Enum of types of sides.
    /// <para/> Values: Left, Top, Right, Bottom.
    /// </summary>
    public enum Side
    {
        Left = 0x01,
        Top = 0x02,
        Right = 0x04,
        Bottom = 0x08,
    }
    /// <summary>
    /// Enum of types of frequencies for redraw.
    /// <para/> Values: VeryLow, Low, Medium, High, Ultra.
    /// </summary>
    public enum RedrawFrequency
    {
        VeryLow, Low, Medium, High, Ultra
    }
    /// <summary>
    /// Enum of types of embedded mouse cursors.
    /// <para/> Values: Arrow, IBeam, Crosshair, Hand, ResizeX, ResizeY, ResizeXY.
    /// </summary>
    public enum EmbeddedCursor
    {
        Arrow = 0x00036001,
        IBeam = 0x00036002,
        Crosshair = 0x00036003,
        Hand = 0x00036004,
        ResizeX = 0x00036005,
        ResizeY = 0x00036006,
        ResizeXY = 0x00036007
    }
    /// <summary>
    /// Enum of types render.
    /// <para/> Values: IfNeeded, Periodic, Always.
    /// </summary>
    public enum RenderType
    {
        /// <summary>
        /// The scene is redrawn only if any input event occurs (mouse move, mouse click, 
        /// keyboard key press, window resizing and etc.).
        /// </summary>
        IfNeeded,
        /// <summary>
        /// The scene is redrawn according to the current render frequency type 
        /// (See SetRenderFrequency(type)) in idle and every time when any input event occurs.
        /// </summary>
        Periodic,
        /// <summary>
        /// The scene is constantly being redrawn.
        /// </summary>
        Always
    }
}
