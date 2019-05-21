using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public abstract class CoreWindow
    {
        private static int count = 0;
        private Guid windowGuid;

        /// <summary>
        /// Constructs a CoreWindow
        /// </summary>
        public CoreWindow()
        {
            windowGuid = Guid.NewGuid();
            SetWindowName("Window_" + count);
            SetWindowTitle("Window_" + count);
            SetDefaults();
            SetHandler(new WindowLayout(this));
            SetSize(300, 300);
            SetMinSize(150, 100);
            count++;
        }

        public void SetParameters(String name, String title)
        {
            SetWindowName(name);
            SetWindowTitle(title);
        }

        public void SetParameters(String name, String title, int width, int height)
        {
            SetWindowName(name);
            SetWindowTitle(title);
            SetSize(width, height);
        }

        public void SetParameters(String name, String title, int width, int height, bool isBorder)
        {
            SetWindowName(name);
            SetWindowTitle(title);
            SetSize(width, height);
            IsBorderHidden = !isBorder;
        }

        private WindowLayout windowLayout;

        /// <summary>
        /// Parent item for the CoreWindow
        /// </summary>
        internal WindowLayout GetLayout()
        {
            return windowLayout;
        }

        /// <summary>
        /// Parent item for the CoreWindow
        /// </summary>
        internal void SetHandler(WindowLayout wl)
        {
            if (wl == null)
            {
                throw new SpaceVILException("Window handler can't be null");
            }
            windowLayout = wl;
            wl.SetCoreWindow();
        }

        /// <summary>
        /// Show the CoreWindow
        /// </summary>
        public virtual void Show()
        {
            windowLayout.Show();
        }

        /// <summary>
        /// Close the CoreWindow
        /// </summary>
        public virtual void Close()
        {
            windowLayout.Close();
        }

        /// <summary>
        /// Initialize the window
        /// </summary>
        abstract public void InitWindow();

        /// <returns> count of all CoreWindows </returns>
        public int GetCount() { return count; }

        /// <returns> CoreWindow unique ID </returns>
        public Guid GetWindowGuid() { return windowGuid; }

        // ------------------------------------------------------------------------------------------
        public void SetBackground(Color color)
        {
            windowLayout.GetContainer().SetBackground(color);
        }

        public void SetBackground(int r, int g, int b)
        {
            windowLayout.GetContainer().SetBackground(GraphicsMathService.ColorTransform(r, g, b));
        }

        public void SetBackground(int r, int g, int b, int a)
        {
            windowLayout.GetContainer().SetBackground(GraphicsMathService.ColorTransform(r, g, b, a));
        }

        public void SetBackground(float r, float g, float b)
        {
            windowLayout.GetContainer().SetBackground(GraphicsMathService.ColorTransform(r, g, b));
        }

        public void SetBackground(float r, float g, float b, float a)
        {
            windowLayout.GetContainer().SetBackground(GraphicsMathService.ColorTransform(r, g, b, a));
        }

        public Color GetBackground()
        {
            return windowLayout.GetContainer().GetBackground();
        }

        public void SetPadding(Indents padding)
        {
            windowLayout.GetContainer().SetPadding(padding);
        }

        public void SetPadding(int left, int top, int right, int bottom)
        {
            windowLayout.GetContainer().SetPadding(left, top, right, bottom);
        }

        public List<IBaseItem> GetItems()
        {
            return windowLayout.GetContainer().GetItems();
        }

        public void AddItem(IBaseItem item)
        {
            windowLayout.GetContainer().AddItem(item);
        }

        public void AddItems(params IBaseItem[] items)
        {
            foreach (IBaseItem item in items)
            {
                windowLayout.GetContainer().AddItem(item);
            }
        }

        public void InsertItem(IBaseItem item, int index)
        {
            windowLayout.GetContainer().InsertItem(item, index);
        }

        public bool RemoveItem(IBaseItem item)
        {
            return windowLayout.GetContainer().RemoveItem(item);
        }

        public void clear()
        {
            windowLayout.GetContainer().Clear();
        }

        private String _name;

        public void SetWindowName(String value)
        {
            _name = value;
        }

        public String GetWindowName()
        {
            return _name;
        }

        private String _title;

        public void SetWindowTitle(String title)
        {
            _title = title;
        }

        public String GetWindowTitle()
        {
            return _title;
        }

        // geometry
        private Geometry _itemGeometry = new Geometry();

        public void SetMinWidth(int width)
        {
            _itemGeometry.SetMinWidth(width);
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetMinWidth(width);
        }

        public void SetWidth(int width)
        {
            _itemGeometry.SetWidth(width);
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetWidth(width);
        }

        public void SetMaxWidth(int width)
        {
            _itemGeometry.SetMaxWidth(width);
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetMaxWidth(width);
        }

        public void SetMinHeight(int height)
        {
            _itemGeometry.SetMinHeight(height);
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetMinHeight(height);
        }

        public void SetHeight(int height)
        {
            _itemGeometry.SetHeight(height);
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetHeight(height);
        }

        public void SetMaxHeight(int height)
        {
            _itemGeometry.SetMaxHeight(height);
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetMaxHeight(height);
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

        // position
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

        private void SetDefaults()
        {
            IsDialog = false;
            IsClosed = true;
            IsHidden = false;
            IsResizable = true;
            IsAlwaysOnTop = false;
            IsBorderHidden = false;
            IsCentered = false;
            IsFocusable = true;
            IsOutsideClickClosable = false;
            IsMaximized = false;
            IsTransparent = false;
        }

        public bool IsDialog;
        public bool IsClosed;
        public bool IsHidden;
        public bool IsResizable;
        public bool IsAlwaysOnTop;
        public bool IsBorderHidden;
        public bool IsCentered;
        public bool IsFocusable;
        public bool IsOutsideClickClosable;
        public bool IsMaximized;
        public bool IsTransparent;

        public bool IsFocused;

        internal MSAA _msaa = MSAA.MSAA4x;

        public void SetAntiAliasingQuality(MSAA msaa)
        {
            _msaa = msaa;
        }

        internal void SetFocusable(bool value)
        {
            windowLayout.SetFocusable(value);
        }

        public void SetFocus(Boolean value)
        {
            if (IsFocused == value)
                return;
            IsFocused = value;
            if (value)
                windowLayout.SetFocus();
        }

        public void SetWindowFocused()
        {
            windowLayout.SetFocus();
        }

        public void Minimize()
        {
            windowLayout.Minimize();
        }

        public void Maximize()
        {
            windowLayout.Maximize();
        }

        public Prototype GetFocusedItem()
        {
            return windowLayout.GetFocusedItem();
        }

        public void SetFocusedItem(Prototype item)
        {
            windowLayout.SetFocusedItem(item);
        }

        public void SetFocus()
        {
            windowLayout.GetContainer().SetFocus();
        }

        public void ResetItems()
        {
            windowLayout.ResetItems();
        }

        public void ResetFocus()
        {
            windowLayout.ResetFocus();
        }

        public void SetIcon(Image icon_big, Image icon_small)
        {
            windowLayout.SetIcon(icon_big, icon_small);
        }

        public void SetHidden(Boolean value)
        {
            windowLayout.SetHidden(value);
            IsHidden = value;
        }

        public void SetRenderFrequency(RedrawFrequency value)
        {
            WindowManager.SetRenderFrequency(value);
        }

        public RedrawFrequency GetRenderFrequency()
        {
            return WindowManager.GetRenderFrequency();
        }

        public EventCommonMethod EventClose;
        public EventCommonMethod EventMinimize;
        public EventCommonMethod EventHide;

        protected internal void Release()
        {
            EventClose = null;
            EventMinimize = null;
            EventHide = null;
            FreeEvents();
        }

        internal void SetWindow(WContainer window)
        {
            windowLayout.SetWindow(window);
        }

        internal float[] GetDpiScale()
        {
            return windowLayout.GetDpiScale();
        }

        internal void SetDpiScale(float w, float h)
        {
            windowLayout.SetDpiScale(w, h);
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

        internal EventCommonMethodState EventFocusGet;
        internal EventCommonMethodState EventFocusLost;
        public EventCommonMethodState EventResize;
        public EventCommonMethodState EventDestroy;
        public EventMouseMethodState EventMouseHover;
        public EventMouseMethodState EventMouseLeave;
        public EventMouseMethodState EventMouseClick;
        public EventMouseMethodState EventMouseDoubleClick;
        public EventMouseMethodState EventMousePress;
        public EventMouseMethodState EventMouseDrag;
        public EventMouseMethodState EventMouseDrop;
        public EventMouseMethodState EventScrollUp;
        public EventMouseMethodState EventScrollDown;
        public EventKeyMethodState EventKeyPress;
        public EventKeyMethodState EventKeyRelease;
        public EventInputTextMethodState EventTextInput;
        public EventWindowDropMethod EventDrop;

        void FreeEvents()
        {
            EventFocusGet = null;
            EventFocusLost = null;
            EventResize = null;
            EventDestroy = null;

            EventMouseHover = null;
            EventMouseClick = null;
            EventMouseDoubleClick = null;
            EventMousePress = null;
            EventMouseDrag = null;
            EventMouseDrop = null;
            EventScrollUp = null;
            EventScrollDown = null;

            EventKeyPress = null;
            EventKeyRelease = null;

            EventTextInput = null;
        }


        public void SetBorder(Border border)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorder(border);
        }

        public void SetBorderFill(Color fill)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderFill(fill);
        }
        public void SetBorderFill(int r, int g, int b)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderFill(r, g, b);
        }
        public void SetBorderFill(int r, int g, int b, int a)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderFill(r, g, b, a);
        }
        public void SetBorderFill(float r, float g, float b)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderFill(r, g, b);
        }
        public void SetBorderFill(float r, float g, float b, float a)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderFill(r, g, b, a);
        }

        public void SetBorderRadius(CornerRadius radius)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderRadius(radius);
        }
        public void SetBorderRadius(int radius)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderRadius(new CornerRadius(radius));
        }

        public void SetBorderThickness(int thickness)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderThickness(thickness);
        }

        public CornerRadius GetBorderRadius()
        {
            if (windowLayout.GetContainer() != null)
                return windowLayout.GetContainer().GetBorderRadius();
            return null;
        }
        public int GetBorderThickness()
        {
            if (windowLayout.GetContainer() != null)
                return windowLayout.GetContainer().GetBorderThickness();
            return 0;
        }
        public Color GetBorderFill()
        {
            if (windowLayout.GetContainer() != null)
                return windowLayout.GetContainer().GetBorderFill();
            return Color.Transparent;
        }

        public Glfw3.Glfw.Window GetGLWID()
        {
            return GetLayout().GetGLWID();
        }

        internal bool InitEngine()
        {
            return windowLayout.InitEngine();
        }
        internal void Render()
        {
            windowLayout.Render();
        }

        internal void Dispose()
        {
            windowLayout.Dispose();
        }

        internal CoreWindow GetPairForCurrentWindow()
        {
            return windowLayout.GetPairForCurrentWindow();
        }

        public void SetShadeColor(Color color)
        {
            windowLayout.SetShadeColor(color);
        }

        public void SetShadeColor(int r, int g, int b)
        {
            windowLayout.SetShadeColor(r, g, b);
        }

        public void SetShadeColor(int r, int g, int b, int a)
        {
            windowLayout.SetShadeColor(r, g, b, a);
        }

        public void SetShadeColor(float r, float g, float b)
        {
            windowLayout.SetShadeColor(r, g, b);
        }

        public void SetShadeColor(float r, float g, float b, float a)
        {
            windowLayout.SetShadeColor(r, g, b, a);
        }

        public Color GetShadeColor()
        {
            return windowLayout.GetShadeColor();
        }
    }
}
