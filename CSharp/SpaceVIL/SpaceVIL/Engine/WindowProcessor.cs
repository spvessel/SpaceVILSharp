using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.Runtime.InteropServices;
using Glfw3;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal class WindowProcessor
    {
        private Geometry _geometryForRestore = new Geometry();
        private Position _positionForRestore = new Position();
        private Glfw.Monitor _monitor;
        private Glfw.VideoMode _vid;
        CommonProcessor _commonProcessor;
        internal WindowProcessor(CommonProcessor commonProcessor)
        {
            _commonProcessor = commonProcessor;
        }

        internal void SetWindowSize(int width, int height, Scale scale)
        {
            if (WindowsBox.GetCurrentFocusedWindow() != _commonProcessor.Window)
                Glfw.FocusWindow(_commonProcessor.Window.GetGLWID());

            if (_commonProcessor.Window.IsKeepAspectRatio)
            {
                float currentW = width;
                float currentH = height;
                float ratioW = _commonProcessor.Window.RatioW;
                float ratioH = _commonProcessor.Window.RatioH;
                float xScale = (currentW / ratioW);
                float yScale = (currentH / ratioH);
                float ratio = 0;
                Side handlerContainerSides = _commonProcessor.RootContainer._sides;
                if (handlerContainerSides.HasFlag(Side.Left)
                        || handlerContainerSides.HasFlag(Side.Right))
                    ratio = xScale;
                else
                    ratio = yScale;

                width = (int)(ratioW * ratio);
                height = (int)(ratioH * ratio);
            }

            if (width > _commonProcessor.Window.GetMaxWidth())
                width = _commonProcessor.Window.GetMaxWidth();

            if (height > _commonProcessor.Window.GetMaxHeight())
                height = _commonProcessor.Window.GetMaxHeight();

            Glfw.SetWindowSize(_commonProcessor.Handler.GetWindowId(),
                    (int)(width * scale.GetXScale()), (int)(height * scale.GetYScale()));

            _commonProcessor.Events.SetEvent(InputEventType.WindowResize);
        }

        internal void SetWindowPos(int x, int y)
        {
            Glfw.SetWindowPos(_commonProcessor.Handler.GetWindowId(), x, y);
            _commonProcessor.Events.SetEvent(InputEventType.WindowMove);
        }

        internal void Drop(Int64 glfwwnd, int count, string[] paths)
        {
            DropArgs dargs = new DropArgs();
            dargs.Count = count;
            dargs.Paths = new List<String>(paths);
            dargs.Item = _commonProcessor.HoveredItem;
            _commonProcessor.Manager.AssignActionsForSender(InputEventType.WindowDrop, dargs, _commonProcessor.RootContainer,
                _commonProcessor.UnderFocusedItems, false);
        }

        internal void MinimizeWindow()
        {
            _commonProcessor.InputLocker = true;
            _commonProcessor.Events.SetEvent(InputEventType.WindowMinimize);
            Glfw.IconifyWindow(_commonProcessor.Handler.GetWindowId());
            _commonProcessor.InputLocker = false;
        }

        internal void MaximizeWindow(Scale scale)
        {
            _commonProcessor.InputLocker = true;
            if (_commonProcessor.Window.IsMaximized)
            {
                Glfw.RestoreWindow(_commonProcessor.Handler.GetWindowId());
                _commonProcessor.Events.SetEvent(InputEventType.WindowRestore);
                _commonProcessor.Window.IsMaximized = false;
            }
            else
            {
                Glfw.MaximizeWindow(_commonProcessor.Handler.GetWindowId());
                _commonProcessor.Events.SetEvent(InputEventType.WindowMaximize);
                _commonProcessor.Window.IsMaximized = true;
            }

            int width = _commonProcessor.Window.GetWidth();
            int height = _commonProcessor.Window.GetHeight();

            Glfw.SetWindowSize(_commonProcessor.Handler.GetWindowId(),
                    (int)(width * scale.GetXScale()), (int)(height * scale.GetYScale()));

            _commonProcessor.InputLocker = false;
        }

        internal void FullScreenWindow()
        {
            _commonProcessor.InputLocker = true;
            _monitor = Glfw.GetPrimaryMonitor();
            if (!_commonProcessor.Window.IsFullScreen)
            {
                _vid = Glfw.GetVideoMode(_monitor);
                _geometryForRestore.SetSize(_commonProcessor.Window.GetWidth(), _commonProcessor.Window.GetHeight());
                _positionForRestore.SetPosition(_commonProcessor.Window.GetX(), _commonProcessor.Window.GetY());
                Glfw.SetWindowMonitor(_commonProcessor.Handler.GetWindowId(), _monitor, 0, 0, _vid.Width, _vid.Height, _vid.RefreshRate);
                Glfw.FocusWindow(_commonProcessor.Handler.GetWindowId());
                _commonProcessor.Window.SetWidthDirect(_vid.Width);
                _commonProcessor.Window.SetHeightDirect(_vid.Height);
                _commonProcessor.Window.IsFullScreen = true;
            }
            else
            {
                Glfw.SetWindowMonitor(_commonProcessor.Handler.GetWindowId(), Glfw.Monitor.None,
                        _positionForRestore.GetX(), _positionForRestore.GetY(),
                        _geometryForRestore.GetWidth(), _geometryForRestore.GetHeight(), 0);
                _commonProcessor.Window.SetWidthDirect(_geometryForRestore.GetWidth());
                _commonProcessor.Window.SetHeightDirect(_geometryForRestore.GetHeight());
                _commonProcessor.Window.IsFullScreen = false;
            }
            _commonProcessor.InputLocker = false;
        }

        internal void Focus(Int64 wnd, bool value)
        {
            if (!_commonProcessor.Handler.Focusable)
                return;

            _commonProcessor.Events.ResetAllEvents();
            _commonProcessor.Tooltip.InitTimer(false);

            if (value)
            {
                if (_commonProcessor.Handler.Focusable)
                {
                    WindowsBox.SetCurrentFocusedWindow(_commonProcessor.Window);
                    _commonProcessor.Window.SetFocus(value);
                    _commonProcessor.Handler.Focused = value;
                }
            }
            else
            {
                if (_commonProcessor.Window.IsDialog)
                {
                    _commonProcessor.Window.SetFocus(true);
                    _commonProcessor.Handler.Focused = true;
                }
                else
                {
                    _commonProcessor.Window.SetFocus(value);
                    _commonProcessor.Handler.Focused = value;
                    
                    if (_commonProcessor.Window.IsOutsideClickClosable)
                    {
                        _commonProcessor.ResetItems();
                        _commonProcessor.Window.Close();
                    }
                }
            }
        }

        private Glfw.Image _iconBig;
        private Glfw.Image _iconSmall;

        internal void SetIcons(Bitmap ibig, Bitmap ismall)
        {
            if (_iconBig.Pixels == null)
            {
                _iconBig.Width = ibig.Width;
                _iconBig.Height = ibig.Height;
                _iconBig.Pixels = GetImagePixels(ibig);
            }
            if (_iconSmall.Pixels == null)
            {
                _iconSmall.Width = ismall.Width;
                _iconSmall.Height = ismall.Height;
                _iconSmall.Pixels = GetImagePixels(ismall);
            }
        }

        internal void ApplyIcon()
        {
            if ((_iconBig.Pixels == null) || (_iconSmall.Pixels == null))
                return;
            Glfw.Image[] images = new Glfw.Image[2];
            images[0] = _iconBig;
            images[1] = _iconSmall;
            Glfw.SetWindowIcon(_commonProcessor.Handler.GetWindowId(), images);
        }

        private byte[] GetImagePixels(Bitmap icon)
        {
            if (icon == null)
                return null;

            // List<byte> _map = new List<byte>();
            // Bitmap bmp = new Bitmap(icon);
            // for (int j = 0; j < icon.Height; j++)
            // {
            //     for (int i = 0; i < icon.Width; i++)
            //     {
            //         Color pixel = bmp.GetPixel(i, j);
            //         _map.Add(pixel.R);
            //         _map.Add(pixel.G);
            //         _map.Add(pixel.B);
            //         _map.Add(pixel.A);
            //     }
            // }
            // return _map.ToArray();

            int imageWidth = icon.Width;
            int imageHeight = icon.Height;
            int size = imageWidth * imageHeight;
            BitmapData bitData = icon.LockBits(
                new System.Drawing.Rectangle(System.Drawing.Point.Empty, icon.Size), ImageLockMode.ReadOnly, PixelFormat.Format32bppArgb);
            int[] pixels = new int[size];
            Marshal.Copy(bitData.Scan0, pixels, 0, size);
            icon.UnlockBits(bitData);
            byte[] result = new byte[size * 4];
            int index = 0;
            foreach (int pixel in pixels)
            {
                byte a = (byte)((pixel & 0xFF000000) >> 24);
                byte r = (byte)((pixel & 0x00FF0000) >> 16);
                byte g = (byte)((pixel & 0x0000FF00) >> 8);
                byte b = (byte)(pixel & 0x000000FF);
                result[index++] = r;
                result[index++] = g;
                result[index++] = b;
                result[index++] = a;
            }
            return result;
        }

        internal static void ToggleToFullScreenMode(CoreWindow window)
        {
            Glfw.Monitor monitor = Glfw.GetPrimaryMonitor();
            Glfw.VideoMode vid = Glfw.GetVideoMode(monitor);
            Glfw.SetWindowMonitor(window.GetGLWID(), monitor, 0, 0, vid.Width, vid.Height, vid.RefreshRate);
        }
        internal static void ToggleToWindowedMode(Int64 window)
        {
            Glfw.SetWindowMonitor(window, Glfw.Monitor.None, 0, 0, 500, 300, 60);
        }
    }
}