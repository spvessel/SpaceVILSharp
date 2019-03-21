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
    public sealed class WindowLayout : ISize, IPosition
    //where TLayout : Prototype
    {
        private Object wndLock = new Object();

        public EventCommonMethod EventClose;
        public EventCommonMethod EventMinimize;
        public EventCommonMethod EventHide;

        public void Release()
        {
            EventClose = null;
            EventMinimize = null;
            EventHide = null;
        }

        internal Object EngineLocker = new Object();
        private CoreWindow handler;
        public CoreWindow GetCoreWindow()
        {
            return handler;
        }
        internal void SetCoreWindow(CoreWindow window)
        {
            if (handler != null && handler.Equals(window)) return;
            handler = window;
            Id = handler.GetWindowGuid();
            Monitor.Enter(wndLock);
            try
            {
                WindowLayoutBox.InitWindow(this);
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

        public WindowLayout(
            string name,
            string title,
            int width = 300,
            int height = 300,
            bool border_hidden = false
            )
        {
            SetWindowName(name);
            SetWindowTitle(title);

            SetWidth(width);
            SetHeight(height);

            IsDialog = false;
            IsBorderHidden = border_hidden;
            IsClosed = true;
            IsHidden = false;
            IsResizeble = true;
            IsCentered = false;
            IsFocusable = true;
            IsAlwaysOnTop = false;
            IsOutsideClickClosable = false;
            IsMaximized = false;

            manager = new ActionManager(this);
            engine = new DrawEngine(this);


        }

        private WContainer _window;
        public WContainer GetWindow()
        {
            return _window;
        }
        public void SetWindow(WContainer window)
        {
            _window = window;
        }
        public void SetBackground(Color color)
        {
            _window.SetBackground(color);
        }
        public void SetBackground(int r, int g, int b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            _window.SetBackground(Color.FromArgb(255, r, g, b));
        }
        public void SetBackground(int r, int g, int b, int a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            _window.SetBackground(Color.FromArgb(a, r, g, b));
        }
        public void SetBackground(float r, float g, float b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            _window.SetBackground(Color.FromArgb(255, (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f)));
        }
        public void SetBackground(float r, float g, float b, float a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            _window.SetBackground(Color.FromArgb((int)(a * 255.0f), (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f)));
        }
        public Color GetBackground()
        {
            return _window.GetBackground();
        }
        public void SetPadding(Indents padding)
        {
            _window.SetPadding(padding);
        }
        public void SetPadding(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _window.SetPadding(left, top, right, bottom);
        }
        public void AddItem(IBaseItem item)
        {
            _window.AddItem(item);
        }
        public void AddItems(params IBaseItem[] items)
        {
            foreach (var item in items)
            {
                _window.AddItem(item);
            }
        }

        internal Guid Id { get; set; }

        private string _name;
        internal void SetWindowName(string name)
        {
            _name = name;
        }
        public string GetWindowName()
        {
            return _name;
        }

        private string _title;
        public void SetWindowTitle(string title)
        {
            _title = title;
        }
        public string GetWindowTitle()
        {
            return _title;
        }

        //geometry
        private Geometry _itemGeometry = new Geometry();
        public void SetMinWidth(int width)
        {
            _itemGeometry.SetMinWidth(width);
            _window?.SetMinWidth(width);
        }
        public void SetWidth(int width)
        {
            _itemGeometry.SetWidth(width);
            if (_window != null)
                _window.SetWidth(width);
        }
        public void SetMaxWidth(int width)
        {
            _itemGeometry.SetMaxWidth(width);
            _window?.SetMaxWidth(width);
        }
        public void SetMinHeight(int height)
        {
            _itemGeometry.SetMinHeight(height);
            _window?.SetMinHeight(height);
        }
        public void SetHeight(int height)
        {
            _itemGeometry.SetHeight(height);
            if (_window != null)
                _window.SetHeight(height);
        }
        public void SetMaxHeight(int height)
        {
            _itemGeometry.SetMaxHeight(height);
            _window?.SetMaxHeight(height);
        }
        public int GetMinWidth()
        {
            return _itemGeometry.GetMinWidth();
        }
        public int GetWidth()
        {
            return _itemGeometry.GetWidth();
        }
        public int GetMaxWidth()
        {
            return _itemGeometry.GetMaxWidth();
        }
        public int GetMinHeight()
        {
            return _itemGeometry.GetMinHeight();
        }
        public int GetHeight()
        {
            return _itemGeometry.GetHeight();
        }
        public int GetMaxHeight()
        {
            return _itemGeometry.GetMaxHeight();
        }
        public void SetSize(int width, int height)
        {
            SetWidth(width);
            SetHeight(height);
        }
        public void SetMinSize(int width, int height)
        {
            SetMinWidth(width);
            SetMinHeight(height);
        }
        public void SetMaxSize(int width, int height)
        {
            SetMaxWidth(width);
            SetMaxHeight(height);
        }
        public int[] GetSize()
        {
            return _itemGeometry.GetSize();
        }

        //position
        private Position _itemPosition = new Position();
        public void SetX(int x)
        {
            _itemPosition.SetX(x);
        }
        public int GetX()
        {
            return _itemPosition.GetX();
        }
        public void SetY(int y)
        {
            _itemPosition.SetY(y);
        }
        public int GetY()
        {
            return _itemPosition.GetY();
        }

        private bool _is_main = false;

        public void SetMainThread()
        {
            _is_main = true;
        }
        //methods
        public void Show()
        {
            if (IsHidden)
                SetHidden(false);
            IsClosed = false;

            engine._handler.Maximized = IsMaximized;
            engine._handler.Visible = !IsHidden;
            engine._handler.Resizeble = IsResizeble;
            engine._handler.BorderHidden = IsBorderHidden;
            engine._handler.AppearInCenter = IsCentered;
            engine._handler.Focusable = IsFocusable;
            engine._handler.AlwaysOnTop = IsAlwaysOnTop;
            engine._handler.GetPointer().SetX(GetX());
            engine._handler.GetPointer().SetY(GetY());

            thread_manager = new Task(() => manager.StartManager());
            thread_manager.Start();

            if (CommonService.GetOSType() == OSType.Mac)
            {
                if (!WindowLayoutBox.IsAnyWindowRunning())
                {
                    WindowLayoutBox.SetWindowRunning(this);
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

            if (IsDialog)
            {
                Monitor.Enter(wndLock);
                try
                {
                    ParentGUID = WindowLayoutBox.LastFocusedWindow.Id;
                    WindowLayoutBox.AddToWindowDispatcher(this);
                    WindowLayoutBox.GetWindowInstance(ParentGUID).engine._handler.Focusable = false;
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

        public void Close()
        {
            if (CommonService.GetOSType() == OSType.Mac)
            {
                engine.Close();
                WindowLayoutBox.SetWindowRunning(null);
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
            IsClosed = true;

            EventClose?.Invoke();
        }

        private void closeInsideNewThread()
        {
            if (IsDialog)
            {
                engine.Close();
                SetWindowFocused();
                Monitor.Enter(wndLock);
                try
                {
                    WindowLayoutBox.RemoveWindow(this);
                    WindowLayoutBox.RemoveFromWindowDispatcher(this);
                }
                finally
                {
                    Monitor.Exit(wndLock);
                }
            }
            else
            {
                if (thread_engine != null && thread_engine.IsAlive)
                    engine.Close();
            }
        }

        internal MSAA _msaa = MSAA.MSAA4x;

        public void SetAntiAliasingQuality(MSAA msaa)
        {
            _msaa = msaa;
        }

        public bool IsDialog { get; set; }
        public bool IsClosed { get; set; }
        public bool IsHidden { get; set; }
        public bool IsResizeble { get; set; }
        public bool IsAlwaysOnTop { get; set; }
        public bool IsBorderHidden { get; set; }
        public bool IsCentered { get; set; }
        public bool IsFocusable { get; set; }
        public bool IsOutsideClickClosable { get; set; }
        public bool IsFocused
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
                if (WindowLayoutBox.GetWindowInstance(ParentGUID) != null)
                    WindowLayoutBox.GetWindowInstance(ParentGUID).engine._handler.Focusable = true;
            }
            finally
            {
                Monitor.Exit(wndLock);
            }
        }
        public void Minimize()
        {
            engine.MinimizeWindow();
        }
        public bool IsMaximized = false;
        public void Maximize()
        {
            // engine.MaximizeWindow();
            engine.MaximizeRequest = true;
        }

        public void IsFixed(bool flag)
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

        public void SetFocusedItem(Prototype item)
        {
            engine.SetFocusedItem(item);
        }
        public Prototype GetFocusedItem()
        {
            return engine.GetFocusedItem();
        }
        public void SetFocus(bool value)
        {
            engine.Focus(engine._handler.GetWindowId(), value);
        }
        public void ResetItems()
        {
            engine.ResetItems();
        }
        public void ResetFocus()
        {
            engine.ResetFocus();
        }

        public void SetIcon(Image icon_big, Image icon_small)
        {
            engine.SetIcons(icon_big, icon_small);
        }

        public void SetHidden(bool value)
        {
            engine._handler.SetHidden(value);
            IsHidden = value;
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

        public void SetRenderFrequency(RedrawFrequency value)
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
        public RedrawFrequency GetRenderFrequency()
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
        public void SetAspectRatio(int w, int h)
        {
            IsKeepAspectRatio = true;
            RatioW = w;
            RatioH = h;
        }
    }
}
