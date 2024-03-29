﻿using System;
using System.Threading;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal sealed class WindowLayout
    {
        internal void RestoreView()
        {
            if (_engine != null)
            {
                _engine.RestoreView();
            }
        }

        internal void RestoreCommonGLSettings()
        {
            if (_engine != null)
            {
                _engine.RestoreCommonGLSettings();
            }
        }

        internal void SetGLLayerViewport(IOpenGLLayer layer)
        {
            if (_engine != null)
            {
                _engine.SetGLLayerViewport(layer);
            }
        }

        internal Object EngineLocker = new Object();
        private Object wndLock = new Object();

        private Guid _id;

        Guid getId()
        {
            return _id;
        }

        void setId(Guid value)
        {
            _id = value;
        }

        private CoreWindow _coreWindow;

        internal CoreWindow GetCoreWindow()
        {
            return _coreWindow;
        }

        internal void SetCoreWindow()
        {
            _id = _coreWindow.GetWindowGuid();
            Monitor.Enter(wndLock);
            try
            {
                WindowsBox.InitWindow(_coreWindow);
                SetFocusedItem(_window);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return;
            }
            finally
            {
                Monitor.Exit(wndLock);
            }
        }

        private DrawEngine _engine;

        private Task _threadActionManager;
        private ActionManager _actionManager;

        internal WindowLayout(CoreWindow cWindow)
        {
            _coreWindow = cWindow;
            _actionManager = new ActionManager(_coreWindow);
            _engine = new DrawEngine(_coreWindow);
            _coreWindow.EventClose += Close;
        }

        private WContainer _window;
        internal WContainer GetContainer()
        {
            return _window;
        }
        internal void SetWindow(WContainer window)
        {
            _window = window;
        }

        //methods
        internal void Show()
        {
            if (!WindowManager.IsRunning())
                WindowManager.StartWith(_coreWindow);
            else
                WindowManager.AddWindow(_coreWindow);
        }

        internal void Close()
        {
            WindowManager.CloseWindow(_coreWindow);
        }

        internal void SetFocusable(bool value)
        {
            _engine.GLWHandler.Focusable = value;
        }

        internal void Minimize()
        {
            _engine.MinimizeRequest = true;
        }

        internal void Maximize()
        {
            _engine.MaximizeRequest = true;
        }

        internal void ToggleFullScreen()
        {
            _engine.FullScreenRequest = true;
        }

        internal void UpdatePosition()
        {
            _engine.UpdatePositionRequest = true;
        }

        internal void UpdateSize()
        {
            _engine.UpdateSizeRequest = true;
        }

        internal void IsFixed(bool flag)
        {
            _window._is_fixed = flag;
        }

        internal void SetEventTask(EventTask task)
        {
            if (!_hold)
                _actionManager.AddTask(task);
        }

        private volatile bool _set = true;

        internal void ExecutePollActions()
        {
            if (!_hold)
            {
                _set = _actionManager.Execute.IsSet;
                if (!_set)
                    _actionManager.Execute.Set();
            }
        }

        internal Prototype GetFocusedItem()
        {
            return _engine.GetFocusedItem();
        }

        internal void SetFocusedItem(Prototype item)
        {
            _engine.SetFocusedItem(item);
        }

        internal void ResetItems()
        {
            _engine.ResetItems();
        }
        internal void ResetFocus()
        {
            _engine.ResetFocus();
        }

        internal void SetIcon(Bitmap icon_big, Bitmap icon_small)
        {
            _engine.SetIcons(icon_big, icon_small);
        }

        internal void SetHidden(bool value)
        {
            _engine.GLWHandler.SetHidden(value);
            _coreWindow.IsHidden = value;
        }

        internal Int64 GetGLWID()
        {
            return _engine.GLWHandler.GetWindowId();
        }

        internal bool IsGLWIDValid()
        {
            if (GetGLWID() == 0)
                return false;
            return true;
        }

        internal bool InitEngine()
        {
            if (_coreWindow.IsHidden)
                SetHidden(false);
            _coreWindow.IsClosed = false;

            _engine.GLWHandler.Transparent = _coreWindow.IsTransparent;
            _engine.GLWHandler.Maximized = _coreWindow.IsMaximized;
            _engine.GLWHandler.Visible = !_coreWindow.IsHidden;
            _engine.GLWHandler.Resizeble = _coreWindow.IsResizable;
            _engine.GLWHandler.BorderHidden = _coreWindow.IsBorderHidden;
            _engine.GLWHandler.AppearInCenter = _coreWindow.IsCentered;
            _engine.GLWHandler.Focusable = _coreWindow.IsFocusable;
            _engine.GLWHandler.AlwaysOnTop = _coreWindow.IsAlwaysOnTop;

            _engine.GLWHandler.GetPointer().SetX(_coreWindow.GetX());
            _engine.GLWHandler.GetPointer().SetY(_coreWindow.GetY());

            _threadActionManager = new Task(() => _actionManager.StartManager());
            _threadActionManager.Start();

            CreateWindowsPair();

            return _engine.Init();
        }

        void CreateWindowsPair()
        {
            WindowsBox.CreateWindowsPair(_coreWindow);
            if (_coreWindow.IsDialog)
            {
                WindowsBox.GetWindowPair(_coreWindow)?.SetFocusable(false);
            }
        }

        internal void Dispose()
        {
            CoreWindow currentPair = GetPairForCurrentWindow();
            DestroyWindowsPair();
            if (_threadActionManager != null && _threadActionManager.Status == TaskStatus.Running)
            {
                _actionManager.StopManager();
                _actionManager.Execute.Set();
            }
            _engine.FreeOnClose();
            if (currentPair != null && !currentPair.IsClosed)
                currentPair.Focus();
        }

        void DestroyWindowsPair()
        {
            if (_coreWindow.IsDialog)
            {
                CoreWindow pair = WindowsBox.GetWindowPair(_coreWindow);
                if (pair != null)
                    pair.SetFocusable(true);
                WindowsBox.RemoveWindow(_coreWindow);
            }
            WindowsBox.RemoveFromWindowDispatcher(_coreWindow);
        }

        internal CoreWindow GetPairForCurrentWindow()
        {
            return WindowsBox.GetWindowPair(_coreWindow);
        }

        internal void UpdateScene()
        {
            _engine.DrawScene();
        }

        internal void Render()
        {
            _engine.Render();
        }

        internal void SetFocus()
        {
            SetFocusable(true);
            _engine.FocusRequest = true;
        }

        private Color _shadeColor = Color.FromArgb(200, 0, 0, 0);

        internal void SetShadeColor(Color color)
        {
            Monitor.Enter(wndLock);
            try
            {
                _shadeColor = Color.FromArgb(color.A, color.R, color.G, color.B);
            }
            finally
            {
                Monitor.Exit(wndLock);
            }
        }

        internal void SetShadeColor(int r, int g, int b)
        {
            SetShadeColor(GraphicsMathService.ColorTransform(r, g, b));
        }

        internal void SetShadeColor(int r, int g, int b, int a)
        {
            SetShadeColor(GraphicsMathService.ColorTransform(r, g, b, a));
        }

        internal void SetShadeColor(float r, float g, float b)
        {
            SetShadeColor(GraphicsMathService.ColorTransform(r, g, b));
        }

        internal void SetShadeColor(float r, float g, float b, float a)
        {
            SetShadeColor(GraphicsMathService.ColorTransform(r, g, b, a));
        }

        internal Color GetShadeColor()
        {
            Monitor.Enter(wndLock);
            try
            {
                return _shadeColor;
            }
            finally
            {
                Monitor.Exit(wndLock);
            }
        }

        internal void FreeVRAMResource<T>(T resource)
        {
            _engine.FreeVRAMResource(resource);
        }

        internal ToolTipItem GetToolTip()
        {
            return _engine.GetToolTip();
        }

        bool _hold = false;

        internal void Hold()
        {
            _hold = true;
        }

        internal void Proceed()
        {
            _hold = false;
        }
    }
}
