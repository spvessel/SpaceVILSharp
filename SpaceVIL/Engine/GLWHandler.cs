#define OS_LINUX 

using System;
using System.Text;
using Glfw3;
using SpaceVIL.Common;
using SpaceVIL.Core;
using static Glfw3.Glfw;

namespace SpaceVIL
{
    internal sealed class GLWHandler
    {
        ///////////////////////////////////////////////
        Glfw.CursorPosFunc MouseMoveCallback;
        Glfw.MouseButtonFunc MouseClickCallback;
        Glfw.ScrollFunc MouseScrollCallback;

        Glfw.KeyFunc KeyPressCallback;
        Glfw.CharModsFunc KeyInputText;

        Glfw.WindowCloseFunc WindowCloseCallback;
        Glfw.WindowPosFunc WindowPosCallback;
        Glfw.WindowFocusFunc WindowFocusCallback;
        Glfw.WindowSizeFunc WindowResizeCallback;
        Glfw.WindowRefreshFunc WindowRefreshCallback;
        Glfw.WindowIconifyFunc WindowIconifyCallback;

        Glfw.DropFunc DropCallback;
        Glfw.FramebufferSizeFunc FramebufferCallback;
        Glfw.WindowContentScaleFunc ContentScaleCallback;
        ///////////////////////////////////////////////

        internal bool BorderHidden;
        internal bool AppearInCenter;
        internal bool Focusable;
        internal bool Focused = true;
        internal bool Resizeble;
        internal bool Visible;
        internal bool AlwaysOnTop;
        internal bool Maximized;
        internal bool Transparent;
        private Position _wndPosition = new Position();
        internal Position GetPointer()
        {
            return _wndPosition;
        }
        ///////////////////////////////////////////////

        private CoreWindow _coreWindow;
        internal CoreWindow GetCoreWindow()
        {
            return _coreWindow;
        }
        Int64 _window;
        internal Int64 GetWindowId()
        {
            return _window;
        }
        internal uint[] GVAO = new uint[1];

        internal GLWHandler() { }
        internal GLWHandler(CoreWindow handler)
        {
            _coreWindow = handler;
            _wndPosition.SetX(0);
            _wndPosition.SetY(0);
        }

        internal void CreateWindow()
        {
            //important!!! may be the best combination of WINDOW HINTS!!!
            Glfw.DefaultWindowHints();
            // Glfw.WindowHint(Glfw.Hint.AutoIconify, false);
            Glfw.WindowHint(Glfw.Hint.OpenglForwardCompat, true);
            Glfw.WindowHint(Glfw.Hint.OpenglProfile, Glfw.OpenGLProfile.Core);
            Glfw.WindowHint(Glfw.Hint.Samples, _coreWindow.AntiAliasingMode);
            Glfw.WindowHint(Glfw.Hint.ContextVersionMajor, 3);
            Glfw.WindowHint(Glfw.Hint.ContextVersionMinor, 3);

            // scaling window's content (does not affect on Mac OS)
            Glfw.WindowHint(Glfw.Hint.ScaleToMonitor, true);

            Glfw.WindowHint(Glfw.Hint.Resizable, Resizeble);
            Glfw.WindowHint(Glfw.Hint.Decorated, !BorderHidden);//make borderless window
            Glfw.WindowHint(Glfw.Hint.Focused, Focused);
            Glfw.WindowHint(Glfw.Hint.Floating, AlwaysOnTop);
            Glfw.WindowHint(Glfw.Hint.TranspatentFramebuffer, Transparent);
            Glfw.WindowHint(Glfw.Hint.Visible, false);

            _window = Glfw.CreateWindow(_coreWindow.GetWidth(), _coreWindow.GetHeight(), _coreWindow.GetWindowTitle());

            if (_window == 0)
            {
                throw new SpaceVILException("Create window fail - " + GetCoreWindow().GetWindowTitle());
            }

            WindowManager.SetContextCurrent(_coreWindow);

            float xScale, yScale;
            Glfw.GetWindowContentScale(_window, out xScale, out yScale);
            _coreWindow.SetWindowScale(xScale, yScale);

            int actualWndWidth = _coreWindow.GetWidth();
            int actualWndHeight = _coreWindow.GetHeight();
            float xActualScale = 1f;
            float yActualScale = 1f;

            Area workArea = _coreWindow.GetWorkArea();

            if (AppearInCenter)
            {
                if (CommonService.GetOSType() != OSType.Mac)
                {
                    actualWndWidth = (int)(_coreWindow.GetWidth() * _coreWindow.GetDpiScale().GetXScale());
                    actualWndHeight = (int)(_coreWindow.GetHeight() * _coreWindow.GetDpiScale().GetYScale());
                }
                GetPointer().SetX(workArea.GetX() + (workArea.GetWidth() - actualWndWidth) / 2);
                GetPointer().SetY(workArea.GetY() + (workArea.GetHeight() - actualWndHeight) / 2);
            }
            else
            {
                if (CommonService.GetOSType() != OSType.Mac)
                {
                    xActualScale = _coreWindow.GetDpiScale().GetXScale();
                    yActualScale = _coreWindow.GetDpiScale().GetYScale();
                }
                _coreWindow.SetXDirect((int)(_coreWindow.GetX() * xActualScale));
                _coreWindow.SetYDirect((int)(_coreWindow.GetY() * yActualScale));

                GetPointer().SetX(_coreWindow.GetX() + workArea.GetX());
                GetPointer().SetY(_coreWindow.GetY() + workArea.GetY());
            }
            Glfw.SetWindowPos(_window, GetPointer().GetX(), GetPointer().GetY());

            Glfw.SetWindowSizeLimits(_window,
                (int)(_coreWindow.GetMinWidth() * xActualScale),
                (int)(_coreWindow.GetMinHeight() * yActualScale),
                (int)(_coreWindow.GetMaxWidth() * xActualScale),
                (int)(_coreWindow.GetMaxHeight() * yActualScale));

            if (_coreWindow.IsKeepAspectRatio)
            {
                Glfw.SetWindowAspectRatio(_window, _coreWindow.RatioW, _coreWindow.RatioH);
            }

            if (Visible)
            {
                Glfw.ShowWindow(_window);
            }
        }

        internal void SwitchContext()
        {
            Glfw.MakeContextCurrent(0);
            WindowManager.SetContextCurrent(_coreWindow);
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
            WindowResizeCallback = null;
            FramebufferCallback = null;
            WindowRefreshCallback = null;
            DropCallback = null;
            ContentScaleCallback = null;
            WindowIconifyCallback = null;
        }

        internal void SetCursorType(EmbeddedCursor type)
        {
            switch (type)
            {
                case EmbeddedCursor.Arrow:
                    Glfw.SetCursor(_window, CommonService.CursorArrow);
                    break;
                case EmbeddedCursor.IBeam:
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
            WindowResizeCallback = function;
            Glfw.SetWindowSizeCallback(_window, WindowResizeCallback);
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
        internal void SetCallbackContentScale(Glfw.WindowContentScaleFunc function)
        {
            ContentScaleCallback = function;
            Glfw.SetWindowContentScaleCallback(_window, ContentScaleCallback);
        }
        internal void SetCallbackIconify(Glfw.WindowIconifyFunc function)
        {
            WindowIconifyCallback = function;
            Glfw.SetWindowIconifyCallback(_window, WindowIconifyCallback);
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