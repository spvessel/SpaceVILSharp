using System;
using System.Text;
using Glfw3;

namespace SpaceVIL
{
    /*public enum CallbackType
    {
        MouseMove,
        MouseClick,
        MouseScroll,
        KeyPress,
        TextInput,
        Close,
        Position,
        Focus,
        Resize,
    }*/
    internal class GLWHandler
    {
        //cursors 
        Glfw.Cursor _arrow;
        Glfw.Cursor _input;
        Glfw.Cursor _hand;
        Glfw.Cursor _resize_h;
        Glfw.Cursor _resize_v;
        Glfw.Cursor _resize_all;
        ///////////////////////////////////////////////

        internal Glfw.WindowSizeFunc ResizeCallback;
        internal Glfw.CursorPosFunc MouseMoveCallback;
        internal Glfw.MouseButtonFunc MouseClickCallback;
        internal Glfw.ScrollFunc MouseScrollCallback;
        internal Glfw.WindowCloseFunc WindowCloseCallback;
        internal Glfw.WindowPosFunc WindowPosCallback;
        internal Glfw.KeyFunc KeyPressCallback;
        internal Glfw.CharModsFunc KeyInputText;
        internal Glfw.WindowFocusFunc WindowFocusCallback;
        ///////////////////////////////////////////////

        internal bool BorderHidden;
        internal bool AppearInCenter;
        internal bool Focusable;
        internal bool Focused = true;
        internal bool Resizeble;
        internal bool Visible;
        internal bool AlwaysOnTop;
        internal Pointer WPosition = new Pointer();
        ///////////////////////////////////////////////

        private WindowLayout _w_layout;
        internal WindowLayout GetLayout()
        {
            return _w_layout;
        }
        Glfw.Window _window;
        internal Glfw.Window GetWindow()
        {
            return _window;
        }
        internal uint[] GVAO = new uint[1];

        internal GLWHandler() { }
        internal GLWHandler(WindowLayout handler)
        {
            _w_layout = handler;
            WPosition.X = 0;
            WPosition.Y = 0;
        }

        internal void InitGlfw()
        {
            //path to c++ glfw3.dll (x32 or x64)
            //Glfw.ConfigureNativesDirectory(AppDomain.CurrentDomain.BaseDirectory);
            if (!Glfw.Init())
            {
                Console.WriteLine("Init window fail - " + GetLayout().GetWindowTitle());
                Environment.Exit(-1);
            }

            //cursors
            _arrow = Glfw.CreateStandardCursor(Glfw.CursorType.Arrow);
            _input = Glfw.CreateStandardCursor(Glfw.CursorType.Beam);
            _hand = Glfw.CreateStandardCursor(Glfw.CursorType.Hand);
            _resize_h = Glfw.CreateStandardCursor(Glfw.CursorType.ResizeX);
            _resize_v = Glfw.CreateStandardCursor(Glfw.CursorType.ResizeY);
            _resize_all = Glfw.CreateStandardCursor(Glfw.CursorType.Crosshair);
        }
        internal void CreateWindow()
        {
            Glfw.WindowHint(Glfw.Hint.OpenglDebugContext, true);
            Glfw.WindowHint(Glfw.Hint.Samples, 4);
            Glfw.WindowHint(Glfw.Hint.OpenglForwardCompat, true);
            Glfw.WindowHint(Glfw.Hint.ContextVersionMajor, 4);
            Glfw.WindowHint(Glfw.Hint.ContextVersionMinor, 3);
            Glfw.WindowHint(Glfw.Hint.Resizable, Resizeble);
            Glfw.WindowHint(Glfw.Hint.Decorated, !BorderHidden);//make borderless window
            Glfw.WindowHint(Glfw.Hint.Focused, Focusable);
            Glfw.WindowHint(Glfw.Hint.Floating, AlwaysOnTop);
            Glfw.WindowHint(Glfw.Hint.Visible, Visible);
            //Glfw.WindowHint(Glfw.Hint.DepthBits, 16);
            //Glfw.WindowHint(Glfw.Hint.TranspatentFramebuffer, true);
            _window = Glfw.CreateWindow(_w_layout.GetWidth(), _w_layout.GetHeight(), _w_layout.GetWindowTitle());
            if (!_window)
            {
                Console.WriteLine("Create window fail - " + GetLayout().GetWindowTitle());
                Glfw.Terminate();
                Environment.Exit(-1);
            }

            if (AppearInCenter)
            {
                WPosition.X = (Glfw.GetVideoMode(Glfw.GetPrimaryMonitor()).Width - _w_layout.GetWidth()) / 2;
                WPosition.Y = (Glfw.GetVideoMode(Glfw.GetPrimaryMonitor()).Height - _w_layout.GetHeight()) / 2;
            }
            else
            {
                WPosition.X = _w_layout.GetX();
                WPosition.Y = _w_layout.GetY();
            }

            Glfw.MakeContextCurrent(_window);
            Glfw.SetWindowSizeLimits(_window, _w_layout.GetMinWidth(), _w_layout.GetMinHeight(), _w_layout.GetMaxWidth(), _w_layout.GetMaxHeight());
        }

        internal void ClearEventsCallbacks()
        {
            MouseMoveCallback = null;
            MouseClickCallback = null;
            MouseScrollCallback = null;
            KeyPressCallback = null;
            KeyInputText = null;
            WindowCloseCallback = null;
            WindowPosCallback = null;
            WindowFocusCallback = null;
            ResizeCallback = null;
        }

        internal void SetCursorType(Glfw.CursorType type)
        {
            switch (type)
            {
                case Glfw.CursorType.Arrow:
                    Glfw.SetCursor(_window, _arrow);
                    break;
                case Glfw.CursorType.Beam:
                    Glfw.SetCursor(_window, _input);
                    break;
                case Glfw.CursorType.Crosshair:
                    Glfw.SetCursor(_window, _resize_all);
                    break;
                case Glfw.CursorType.Hand:
                    Glfw.SetCursor(_window, _hand);
                    break;
                case Glfw.CursorType.ResizeX:
                    Glfw.SetCursor(_window, _resize_h);
                    break;
                case Glfw.CursorType.ResizeY:
                    Glfw.SetCursor(_window, _resize_v);
                    break;
                default:
                    Glfw.SetCursor(_window, _arrow);
                    break;
            }
        }

        internal bool IsClosing()
        {
            return Glfw.WindowShouldClose(_window);
        }
        internal void Destroy()
        {
            Glfw.DestroyWindow(_window);
        }
        internal void Swap()
        {
            Glfw.SwapBuffers(_window);
        }
        internal void SetToClose()
        {
            Glfw.SetWindowShouldClose(_window, true);
        }
        internal void SetCallbackMouseMove(Glfw.CursorPosFunc function)
        {
            MouseMoveCallback = function;
            Glfw.SetCursorPosCallback(_window, MouseMoveCallback);
        }
        internal void SetCallbackMouseClick(Glfw.MouseButtonFunc function)
        {
            MouseClickCallback = function;
            Glfw.SetMouseButtonCallback(_window, MouseClickCallback);
        }
        internal void SetCallbackMouseScroll(Glfw.ScrollFunc function)
        {
            MouseScrollCallback = function;
            Glfw.SetScrollCallback(_window, MouseScrollCallback);
        }
        internal void SetCallbackKeyPress(Glfw.KeyFunc function)
        {
            KeyPressCallback = function;
            Glfw.SetKeyCallback(_window, KeyPressCallback);
        }
        internal void SetCallbackTextInput(Glfw.CharModsFunc function)
        {
            KeyInputText = function;
            Glfw.SetCharModsCallback(_window, KeyInputText);
        }
        internal void SetCallbackClose(Glfw.WindowCloseFunc function)
        {
            WindowCloseCallback = function;
            Glfw.SetWindowCloseCallback(_window, WindowCloseCallback);
        }
        internal void SetCallbackPosition(Glfw.WindowPosFunc function)
        {
            WindowPosCallback = function;
            Glfw.SetWindowPosCallback(_window, WindowPosCallback);
        }
        internal void SetCallbackFocus(Glfw.WindowFocusFunc function)
        {
            WindowFocusCallback = function;
            Glfw.SetWindowFocusCallback(_window, WindowFocusCallback);
        }
        internal void SetCallbackResize(Glfw.WindowSizeFunc function)
        {
            ResizeCallback = function;
            Glfw.SetWindowSizeCallback(_window, ResizeCallback);
        }

        internal void SetOpacity(float level)
        {
            Glfw.SetWindowOpacity(_window, level);
        }

        internal void SetHidden(bool value)
        {
            if (value)
                Glfw.HideWindow(_window);
            else
                Glfw.ShowWindow(_window);
        }
    }
}