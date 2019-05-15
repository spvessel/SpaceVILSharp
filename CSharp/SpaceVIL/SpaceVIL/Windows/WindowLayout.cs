using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using SpaceVIL.Common;

namespace SpaceVIL
{
    internal sealed class WindowLayout
    //where TLayout : Prototype
    {
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

        private Guid ParentGUID;

        private Thread thread_engine;
        private DrawEngine engine;

        private Task thread_manager;
        private ActionManager manager;

        internal WindowLayout(CoreWindow cWindow)
        {
            _coreWindow = cWindow;
            manager = new ActionManager(_coreWindow);
            engine = new DrawEngine(_coreWindow);
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
            if (_coreWindow.IsHidden)
                SetHidden(false);
            _coreWindow.IsClosed = false;

            engine._handler.Transparent = _coreWindow.IsTransparent;
            engine._handler.Maximized = _coreWindow.IsMaximized;
            engine._handler.Visible = !_coreWindow.IsHidden;
            engine._handler.Resizeble = _coreWindow.IsResizable;
            engine._handler.BorderHidden = _coreWindow.IsBorderHidden;
            engine._handler.AppearInCenter = _coreWindow.IsCentered;
            engine._handler.Focusable = _coreWindow.IsFocusable;
            engine._handler.AlwaysOnTop = _coreWindow.IsAlwaysOnTop;
            engine._handler.GetPointer().SetX(_coreWindow.GetX());
            engine._handler.GetPointer().SetY(_coreWindow.GetY());

            thread_manager = new Task(() => manager.StartManager());
            thread_manager.Start();

            if (CommonService.GetOSType() == OSType.Mac)
            {
                if (!WindowsBox.IsAnyWindowRunning())
                {
                    WindowsBox.SetWindowRunning(_coreWindow);
                    engine.Init();
                }
            }
            else
            {
                ShowInsideNewThread();
            }
        }

        private void ShowInsideNewThread()
        {
            if (thread_engine != null && thread_engine.IsAlive)
                return;

            thread_engine = new Thread(() => engine.Init());

            if (_coreWindow.IsDialog)
            {
                Monitor.Enter(wndLock);
                try
                {
                    ParentGUID = WindowsBox.LastFocusedWindow.GetWindowGuid();
                    WindowsBox.AddToWindowDispatcher(_coreWindow);
                    WindowsBox.GetWindowInstance(ParentGUID).SetFocusable(false);
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.ToString());
                }
                finally
                {
                    Monitor.Exit(wndLock);
                }

                thread_engine.Start();
                thread_engine.Join();
            }
            else
            {
                thread_engine.Start();
            }
        }

        internal void Close()
        {
            if (CommonService.GetOSType() == OSType.Mac)
            {
                engine._handler.SetToClose();
                WindowsBox.SetWindowRunning(null);
            }
            else
            {
                closeInsideNewThread();
            }

            if (thread_manager != null && thread_manager.Status == TaskStatus.Running)
            {
                manager.StopManager();
                manager.Execute.Set();
            }
            _coreWindow.IsClosed = true;
        }

        private void closeInsideNewThread()
        {
            if (_coreWindow.IsDialog)
            {
                engine._handler.SetToClose();
                SetWindowFocused();
                Monitor.Enter(wndLock);
                try
                {
                    WindowsBox.RemoveWindow(_coreWindow);
                    WindowsBox.RemoveFromWindowDispatcher(_coreWindow);
                }
                finally
                {
                    Monitor.Exit(wndLock);
                }
            }
            else
            {
                if (thread_engine != null && thread_engine.IsAlive)
                    engine._handler.SetToClose();
            }
        }

        internal bool IsFocused
        {
            get
            {
                if (engine != null)
                    return engine._handler.Focused;
                return false;
            }
            set
            {
                if (engine != null)
                    engine._handler.Focused = value;
            }
        }
        internal void SetWindowFocused()
        {
            Monitor.Enter(wndLock);
            try
            {
                if (WindowsBox.GetWindowInstance(ParentGUID) != null)
                    WindowsBox.GetWindowInstance(ParentGUID).SetFocusable(true);
            }
            finally
            {
                Monitor.Exit(wndLock);
            }
        }

        internal void SetFocusable(bool value)
        {
            engine._handler.Focusable = value;
        }

        internal void Minimize()
        {
            engine.MinimizeWindow();
        }
        internal bool IsMaximized = false;
        internal void Maximize()
        {
            // engine.MaximizeWindow();
            engine.MaximizeRequest = true;
        }

        internal void IsFixed(bool flag)
        {
            _window._is_fixed = flag;
        }
        internal void SetEventTask(EventTask task)
        {
            //manager.StackEvents.Enqueue(task);
            manager.AddTask(task);
        }
        volatile bool set = true;

        internal void ExecutePollActions()
        {
            set = manager.Execute.IsSet;
            if (!set)
                manager.Execute.Set();
        }

        internal void SetFocusedItem(Prototype item)
        {
            engine.SetFocusedItem(item);
        }
        internal Prototype GetFocusedItem()
        {
            return engine.GetFocusedItem();
        }
        internal void SetFocus(bool value)
        {
            engine.Focus(engine._handler.GetWindowId(), value);
        }
        internal void ResetItems()
        {
            engine.ResetItems();
        }
        internal void ResetFocus()
        {
            engine.ResetFocus();
        }

        internal void SetIcon(Image icon_big, Image icon_small)
        {
            engine.SetIcons(icon_big, icon_small);
        }

        internal void SetHidden(bool value)
        {
            engine._handler.SetHidden(value);
            _coreWindow.IsHidden = value;
        }

        private float _scaleWidth = 1.0f;
        private float _scaleHeight = 1.0f;

        internal float[] GetDpiScale()
        {
            return new float[] { _scaleWidth, _scaleHeight };
        }

        internal void SetDpiScale(float w, float h)
        {
            // _scaleWidth = w * 2;
            // _scaleHeight = h * 2;
            _scaleWidth = w;
            _scaleHeight = h;
        }

        internal void SetRenderFrequency(RedrawFrequency value)
        {
            Monitor.Enter(EngineLocker);
            try
            {
                engine.SetFrequency(value);
            }
            finally
            {
                Monitor.Exit(EngineLocker);
            }
        }
        internal RedrawFrequency GetRenderFrequency()
        {
            Monitor.Enter(EngineLocker);
            try
            {
                return engine.GetRedrawFrequency();
            }
            finally
            {
                Monitor.Exit(EngineLocker);
            }
        }

        internal int RatioW = -1;
        internal int RatioH = -1;
        internal bool IsKeepAspectRatio = false;
        internal void SetAspectRatio(int w, int h)
        {
            IsKeepAspectRatio = true;
            RatioW = w;
            RatioH = h;
        }

        internal Glfw3.Glfw.Window GetGLWID()
        {
            if (engine == null)
                return new Glfw3.Glfw.Window(IntPtr.Zero);
            return engine._handler.GetWindowId();
        }
    }
}
