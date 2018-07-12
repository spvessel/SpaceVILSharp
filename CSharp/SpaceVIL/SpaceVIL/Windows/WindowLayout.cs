using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class WindowLayout : ISize, IPosition
    //where TLayout : VisualItem
    {
        private Thread thread;
        private DrawEngine engine;

        public WindowLayout(
            string name = "WindowLayout",
            string title = "WindowLayout",
            int width = 300,
            int height = 300,
            bool border = true
            )
        {
            //refactor for unique Id
            Id = GetHashCode();
            SetWindowName(name);
            SetWindowTitle(title);
            SetWidth(width);
            SetMinWidth(0);
            SetMaxWidth(7680); //wide of screen
            SetHeight(height);
            SetMinHeight(0);
            SetMaxHeight(4320); //height of screen
            IsBorderHidden = !border;
            IsHidden = true;
            IsCentered = true;
            IsFocusable = true;
            IsAlwaysOnTop = false;
            IsOutsideClickClosable = false;

            //InitWindow
            WindowLayoutBox.InitWindow(this);
        }
        public void UpdatePosition()
        {
            if (engine != null)
                engine.SetWindowPos();
        }
        public void UpdateSize()
        {
            if (engine != null)
                engine.SetWindowSize();
        }
        public void UpdateScene()
        {
            //Console.WriteLine("update");
            engine.Update();
        }
        private WContainer _window;
        internal WContainer GetWindow()
        {
            return _window;
        }
        internal void SetWindow(WContainer window)
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
        public void SetPadding(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _window.SetPadding(left, top, right, bottom);
        }
        public void AddItem(BaseItem item)
        {
            _window.AddItem(item);
        }
        public void AddItems(params BaseItem[] items)
        {
            foreach (var item in items)
            {
                _window.AddItem(item);
            }
        }

        public int Id { get; set; }

        private string _name;
        public void SetWindowName(string name)
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
        public virtual void SetWidth(int width)
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
        public virtual void SetHeight(int height)
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
        public virtual int GetWidth()
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
        public virtual int GetHeight()
        {
            return _itemGeometry.GetHeight();
        }
        public int GetMaxHeight()
        {
            return _itemGeometry.GetMaxHeight();
        }
        public void SetSize(int width, int height)
        {
            _itemGeometry.SetWidth(width);
            _itemGeometry.SetHeight(height);
        }
        public int[] GetSize()
        {
            return _itemGeometry.GetSize();
        }

        //position
        private Position _itemPosition = new Position();
        public virtual void SetX(int x)
        {
            _itemPosition.SetX(x);
        }
        public virtual int GetX()
        {
            return _itemPosition.GetX();
        }
        public virtual void SetY(int y)
        {
            _itemPosition.SetY(y);
        }
        public virtual int GetY()
        {
            return _itemPosition.GetY();
        }

        public bool IsDialog = false;
        //methods
        public void Show()
        {
            engine = new DrawEngine(this)
            {
                borderHidden = IsBorderHidden,
                appearInCenter = IsCentered,
                focusable = IsFocusable,
                alwaysOnTop = IsAlwaysOnTop
            };
            engine.window_position.X = GetX();
            engine.window_position.Y = GetY();

            WindowLayoutBox.ActiveWindow = Id;
            IsHidden = false;

            if (IsDialog)
            {
                if (thread != null && thread.IsAlive)
                    return;

                thread = new Thread(() => engine.Init());
                thread.Start();
                thread.Join();
            }
            else
            {
                if (thread != null && thread.IsAlive)
                    return;

                thread = new Thread(() => engine.Init());
                thread.Start();
            }
        }
        public bool IsHidden { get; set; }
        public void Close()
        {
            if (!IsDialog)
            {
                if (thread != null && thread.IsAlive)
                {
                    thread.Abort();
                }
                IsHidden = true;
            }
            else
            {
                engine.Close();
                WindowLayoutBox.RemoveWindow(this);
            }
        }
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
                    return engine.focused;
                return false;
            }
            set
            {
                if (engine != null)
                    engine.focused = value;
            }
        }

        public void Minimize()
        {
            engine.MinimizeWindow();
        }

        public void IsFixed(bool flag)
        {
            _window._is_fixed = flag;
        }
    }
}
