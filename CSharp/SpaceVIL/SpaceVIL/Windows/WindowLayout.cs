using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class WindowLayout : ISize, IPosition
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
            SetMaxWidth(1920); //wide of screen
            SetHeight(height);
            SetMinHeight(0);
            SetMaxHeight(1080); //height of screen
            IsBorderHidden = !border;
            IsHidden = true;
            IsCentered = true;
            IsFocusable = true;
            IsAlwaysOnTop = false;
            IsOutsideClickClosable = false;
        }
        public void UpdatePosition()
        {
            if (engine != null)
                engine.MoveWindowPos();
        }
        public void UpdateSize()
        {
            if (engine != null)
                engine.SetWindowSize();
        }
        public Frame Window { get; internal set; }
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
        }
        public virtual void SetWidth(int width)
        {
            _itemGeometry.SetWidth(width);
            if (Window != null)
                Window.SetWidth(width);
        }
        public void SetMaxWidth(int width)
        {
            _itemGeometry.SetMaxWidth(width);
        }
        public void SetMinHeight(int height)
        {
            _itemGeometry.SetMinHeight(height);
        }
        public virtual void SetHeight(int height)
        {
            _itemGeometry.SetHeight(height);
            if (Window != null)
                Window.SetHeight(height);
        }
        public void SetMaxHeight(int height)
        {
            _itemGeometry.SetMaxHeight(height);
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

        //methods
        public void Show()
        {
            if (thread != null && thread.IsAlive)
                return;

            engine = new DrawEngine(this)
            {
                borderHidden = IsBorderHidden,
                appearInCenter = IsCentered,
                focusable = IsFocusable,
                alwaysOnTop = IsAlwaysOnTop
            };
            engine.window_position.X = GetX();
            engine.window_position.Y = GetY();

            thread = new Thread(() => engine.Init());
            thread.Start();

            WindowLayoutBox.ActiveWindow = Id;
            IsHidden = false;
        }
        public bool IsHidden { get; set; }
        public void Close()
        {
            if (thread != null && thread.IsAlive)
            {
                thread.Abort();
            }
            IsHidden = true;
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
    }
}
