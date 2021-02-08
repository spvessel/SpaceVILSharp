namespace Glfw3
{
    using System;
    using System.Runtime.InteropServices;
    using SpaceVIL.Core;

    internal static partial class Glfw
    {
        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void CharFunc(Int64 window, uint codepoint);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void CharModsFunc(Int64 window, uint codepoint, KeyMods mods);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void CursorEnterFunc(Int64 window, [MarshalAs(UnmanagedType.Bool)] bool entered);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void CursorPosFunc(Int64 window, double xpos, double ypos);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void DropFunc(Int64 window, int count, [MarshalAs(UnmanagedType.LPArray, ArraySubType = UnmanagedType.LPStr, SizeParamIndex = 1)] string[] paths);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl, CharSet = CharSet.Ansi)]
        internal delegate void ErrorFunc(ErrorCode error, string description);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void FramebufferSizeFunc(Int64 window, int width, int height);        

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void KeyFunc(Int64 window, KeyCode key, int scancode, InputState state, KeyMods mods);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void JoystickFunc(Joystick joy, ConnectionEvent evt);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void MonitorFunc(Monitor monitor, ConnectionEvent evt);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void MouseButtonFunc(Int64 window, MouseButton button, InputState state, KeyMods mods);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void ScrollFunc(Int64 window, double xoffset, double yoffset);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void WindowCloseFunc(Int64 window);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void WindowFocusFunc(Int64 window, [MarshalAs(UnmanagedType.Bool)] bool focused);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void WindowIconifyFunc(Int64 window, [MarshalAs(UnmanagedType.Bool)] bool focused);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void WindowPosFunc(Int64 window, int xpos, int ypos);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void WindowRefreshFunc(Int64 window);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void WindowSizeFunc(Int64 window, int width, int height);

        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        internal delegate void WindowContentScaleFunc(Int64 window, float xscale, float yscale);
    }
}
