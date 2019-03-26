#define OS_LINUX 

using System;
using System.Text;
using Glfw3;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal sealed class GLWHandler
    {
        // private float _scaleWidth = 1.0f;
        // private float _scaleHeight = 1.0f;
        // float[] GetDpiScale()
        // {
        //     return new float[] { _scaleWidth, _scaleHeight };
        // }
        private void SetDpiScale(float w, float h)
        {
            // _scaleWidth = w;
            // _scaleHeight = h;
            // Console.WriteLine(String.Format("{0:0.0} {1:0.0}", w, h));
            DisplayService.SetDisplayDpiScale(w);
            _w_layout.SetDpiScale(w, h);
        }

        ///////////////////////////////////////////////
        Glfw.WindowSizeFunc ResizeCallback;
        Glfw.CursorPosFunc MouseMoveCallback;
        Glfw.MouseButtonFunc MouseClickCallback;
        Glfw.ScrollFunc MouseScrollCallback;
        Glfw.WindowCloseFunc WindowCloseCallback;
        Glfw.WindowPosFunc WindowPosCallback;
        Glfw.KeyFunc KeyPressCallback;
        Glfw.CharModsFunc KeyInputText;
        Glfw.WindowFocusFunc WindowFocusCallback;
        Glfw.FramebufferSizeFunc FramebufferCallback;
        Glfw.WindowRefreshFunc WindowRefreshCallback;
        Glfw.DropFunc DropCallback;
        ///////////////////////////////////////////////

        internal bool BorderHidden;
        internal bool AppearInCenter;
        internal bool Focusable;
        internal bool Focused = true;
        internal bool Resizeble;
        internal bool Visible;
        internal bool AlwaysOnTop;
        internal bool Maximized;
        private Pointer WPosition = new Pointer();
        internal Pointer GetPointer()
        {
            return WPosition;
        }
        ///////////////////////////////////////////////

        private WindowLayout _w_layout;
        internal WindowLayout GetLayout()
        {
            return _w_layout;
        }
        Glfw.Window _window;
        internal Glfw.Window GetWindowId()
        {
            return _window;
        }
        internal uint[] GVAO = new uint[1];

        internal GLWHandler() { }
        internal GLWHandler(WindowLayout handler)
        {
            _w_layout = handler;
            WPosition.SetX(0);
            WPosition.SetY(0);
        }

        internal void CreateWindow()
        {
            //important!!! may be the best combination of WINDOW HINTS!!!

            // Glfw.DefaultWindowHints();
            Glfw.WindowHint(Glfw.Hint.OpenglForwardCompat, true);
            Glfw.WindowHint(Glfw.Hint.OpenglProfile, Glfw.OpenGLProfile.Core);
            Glfw.WindowHint(Glfw.Hint.Samples, _w_layout._msaa);
            Glfw.WindowHint(Glfw.Hint.ContextVersionMajor, 3);
            Glfw.WindowHint(Glfw.Hint.ContextVersionMinor, 3);

            Glfw.WindowHint(Glfw.Hint.Resizable, Resizeble);
            Glfw.WindowHint(Glfw.Hint.Decorated, !BorderHidden);//make borderless window
            Glfw.WindowHint(Glfw.Hint.Focused, Focused);
            Glfw.WindowHint(Glfw.Hint.Floating, AlwaysOnTop);
            Glfw.WindowHint(Glfw.Hint.Maximized, Maximized);
            Glfw.WindowHint(Glfw.Hint.Visible, false);

            _window = Glfw.CreateWindow(_w_layout.GetWidth(), _w_layout.GetHeight(), _w_layout.GetWindowTitle());

            if (!_window)
            {
                LogService.Log().LogText("Create window fail - " + GetLayout().GetWindowTitle());
                throw new SpaceVILException("SpaceVILException: Create window fail - " + GetLayout().GetWindowTitle());
            }
            Glfw.MakeContextCurrent(_window);

            // Console.WriteLine("VIDSIZE: " + Glfw.GetVideoMode(Glfw.GetPrimaryMonitor()).Width
            //             + " " + Glfw.GetVideoMode(Glfw.GetPrimaryMonitor()).Height);

            // Console.WriteLine("WSIZE: " + _w_layout.GetWidth() + " " + _w_layout.GetHeight());
            int w, h;
            Glfw.GetFramebufferSize(_window, out w, out h);
            // Console.WriteLine("FBSIZE: " + w + " " + h);

            SetDpiScale((float)w / (float)_w_layout.GetWidth(), (float)h / (float)_w_layout.GetHeight());

            if (AppearInCenter)
            {
                GetPointer().SetX((Glfw.GetVideoMode(Glfw.GetPrimaryMonitor()).Width - _w_layout.GetWidth()) / 2);
                GetPointer().SetY((Glfw.GetVideoMode(Glfw.GetPrimaryMonitor()).Height - _w_layout.GetHeight()) / 2);
                // _w_layout.SetX(WPosition.GetX());
                // _w_layout.SetY(WPosition.GetY());
            }
            else
            {
                // WPosition.SetX(_w_layout.GetX());
                // WPosition.SetY(_w_layout.GetY());
                _w_layout.SetX(200);
                _w_layout.SetY(50);
                GetPointer().SetX(200);
                GetPointer().SetY(50);
            }
            Glfw.SetWindowSizeLimits(_window, _w_layout.GetMinWidth(), _w_layout.GetMinHeight(), _w_layout.GetMaxWidth(), _w_layout.GetMaxHeight());
            Glfw.SetWindowPos(_window, WPosition.GetX(), WPosition.GetY());

            if (_w_layout.IsKeepAspectRatio)
                Glfw.SetWindowAspectRatio(_window, _w_layout.RatioW, _w_layout.RatioH);

            if (Visible)
                Glfw.ShowWindow(_window);
        }

        internal void SwitchContext()
        {
            Glfw.MakeContextCurrent(Glfw.Window.None);
            Glfw.MakeContextCurrent(_window);
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
            FramebufferCallback = null;
            WindowRefreshCallback = null;
            DropCallback = null;
        }

        internal void SetCursorType(EmbeddedCursor type)
        {
            switch (type)
            {
                case EmbeddedCursor.Arrow:
                    Glfw.SetCursor(_window, CommonService.CursorArrow);
                    break;
                case EmbeddedCursor.Beam:
                    Glfw.SetCursor(_window, CommonService.CursorInput);
                    break;
                case EmbeddedCursor.Crosshair:
                    Glfw.SetCursor(_window, CommonService.CursorResizeAll);
                    break;
                case EmbeddedCursor.Hand:
                    Glfw.SetCursor(_window, CommonService.CursorHand);
                    break;
                case EmbeddedCursor.ResizeX:
                    Glfw.SetCursor(_window, CommonService.CursorResizeH);
                    break;
                case EmbeddedCursor.ResizeY:
                    Glfw.SetCursor(_window, CommonService.CursorResizeV);
                    break;
                default:
                    Glfw.SetCursor(_window, CommonService.CursorArrow);
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
        internal void SetCallbackFramebuffer(Glfw.FramebufferSizeFunc function)
        {
            FramebufferCallback = function;
            Glfw.SetFramebufferSizeCallback(_window, FramebufferCallback);
        }
        internal void SetCallbackRefresh(Glfw.WindowRefreshFunc function)
        {
            WindowRefreshCallback = function;
            Glfw.SetWindowRefreshCallback(_window, WindowRefreshCallback);
        }
        internal void SetCallbackDrop(Glfw.DropFunc function)
        {
            DropCallback = function;
            Glfw.SetDropCallback(_window, DropCallback);
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