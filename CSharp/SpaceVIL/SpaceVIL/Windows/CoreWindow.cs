using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// CoreWindow is an abstract class containing an implementation of common functionality for a window.
    /// </summary>
    public abstract class CoreWindow
    {
        private static int _count = 0;
        private Guid windowGuid;

        /// <summary>
        /// Constructs a CoreWindow.
        /// </summary>
        public CoreWindow()
        {
            windowGuid = Guid.NewGuid();
            SetWindowName("Window_" + _count);
            SetWindowTitle("Window_" + _count);
            SetDefaults();
            SetHandler(new WindowLayout(this));
            SetSize(300, 300);
            SetMinSize(150, 100);
            _count++;
        }

        /// <summary>
        /// Setting basic window attributes.
        /// </summary>
        /// <param name="name">Window name.</param>
        /// <param name="title">Title text.</param>
        public void SetParameters(String name, String title)
        {
            SetWindowName(name);
            SetWindowTitle(title);
        }

        /// <summary>
        /// Setting basic window attributes
        /// </summary>
        /// <param name="name">Window name.</param>
        /// <param name="title">Title text.</param>
        /// <param name="width">Window width in pixels.</param>
        /// <param name="height">Window height  in pixels.</param>
        public void SetParameters(String name, String title, int width, int height)
        {
            SetWindowName(name);
            SetWindowTitle(title);
            SetSize(width, height);
        }

        /// <summary>
        /// Setting basic window attributes.
        /// </summary>
        /// <param name="name">Window name.</param>
        /// <param name="title">Title text.</param>
        /// <param name="width">Window width in pixels.</param>
        /// <param name="height">Window height  in pixels.</param>
        /// <param name="isBorder">A flag that shows/hides native window border decoration.</param>
        public void SetParameters(String name, String title, int width, int height, bool isBorder)
        {
            SetWindowName(name);
            SetWindowTitle(title);
            SetSize(width, height);
            IsBorderHidden = !isBorder;
        }

        private WindowLayout windowLayout;

        /// <summary>
        /// Parent item for the CoreWindow.
        /// </summary>
        internal WindowLayout GetLayout()
        {
            return windowLayout;
        }

        /// <summary>
        /// Parent item for the CoreWindow.
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
        /// Show the CoreWindow.
        /// </summary>
        public virtual void Show()
        {
            windowLayout.Show();
        }

        /// <summary>
        /// Close the CoreWindow.
        /// </summary>
        public virtual void Close()
        {
            windowLayout.Close();
        }

        /// <summary>
        /// This abstract method should provide the initial window attributes, content, events. 
        /// </summary>
        abstract public void InitWindow();

        /// <returns> Count of all CoreWindows </returns>
        public int GetCount() { return _count; }

        /// <returns> CoreWindow unique ID </returns>
        public Guid GetWindowGuid() { return windowGuid; }

        /// <summary>
        /// Setting window background color.
        /// </summary>
        /// <param name="color">System.Drawing.Color.FromARGB(alpha, red, green, blue)</param>
        public void SetBackground(Color color)
        {
            windowLayout.GetContainer().SetBackground(color);
        }

        /// <summary>
        /// Setting window background color.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        public void SetBackground(int r, int g, int b)
        {
            windowLayout.GetContainer().SetBackground(GraphicsMathService.ColorTransform(r, g, b));
        }

        /// <summary>
        /// Setting window background color.
        /// </summary>
        /// <param name="r">Red bits of a color. Range: (0 - 255)</param>
        /// <param name="g">Green bits of a color. Range: (0 - 255)</param>
        /// <param name="b">Blue bits of a color. Range: (0 - 255)</param>
        /// <param name="a">Alpha bits of a color. Range: (0 - 255)</param>
        public void SetBackground(int r, int g, int b, int a)
        {
            windowLayout.GetContainer().SetBackground(GraphicsMathService.ColorTransform(r, g, b, a));
        }

        /// <summary>
        /// Setting window background color.
        /// </summary>
        /// <param name="r">Red (0.0f - 1.0f)</param>
        /// <param name="g">Green (0.0f - 1.0f)</param>
        /// <param name="b">Blur (0.0f - 1.0f)</param>
        public void SetBackground(float r, float g, float b)
        {
            windowLayout.GetContainer().SetBackground(GraphicsMathService.ColorTransform(r, g, b));
        }

        /// <summary>
        /// Setting window background color.
        /// </summary>
        /// <param name="r">Red (0.0f - 1.0f)</param>
        /// <param name="g">Green (0.0f - 1.0f)</param>
        /// <param name="b">Blur (0.0f - 1.0f)</param>
        /// <param name="a">Alpha (0.0f - 1.0f)</param>
        public void SetBackground(float r, float g, float b, float a)
        {
            windowLayout.GetContainer().SetBackground(GraphicsMathService.ColorTransform(r, g, b, a));
        }

        /// <summary>
        /// Getting window background color.
        /// </summary>
        /// <returns>Returns background color as System.Drawing.Color</returns>
        public Color GetBackground()
        {
            return windowLayout.GetContainer().GetBackground();
        }
        /// <summary>
        /// Setting padding indents. Padding is the space that’s inside the element between the element and the border.
        /// </summary>
        /// <param name="padding">SpaceVIL.Decorations.Indents(int left, int top, int right, int bottom)</param>
        public void SetPadding(Indents padding)
        {
            windowLayout.GetContainer().SetPadding(padding);
        }
        /// <summary>
        /// Setting padding indents. Padding is the space that’s inside the element between the element and the border.
        /// </summary>
        /// <param name="left">Left indent.</param>
        /// <param name="top">Top indent.</param>
        /// <param name="right">Right indent.</param>
        /// <param name="bottom">Bottom indent.</param>
        public void SetPadding(int left, int top, int right, int bottom)
        {
            windowLayout.GetContainer().SetPadding(left, top, right, bottom);
        }

        /// <summary>
        /// Getting items as a list of IBaseItem items.
        /// </summary>
        /// <returns>Returns a list of contained items in the window.</returns>
        public List<IBaseItem> GetItems()
        {
            return windowLayout.GetContainer().GetItems();
        }

        /// <summary>
        /// Adding an item to the window.
        /// </summary>
        /// <param name="item">An instance of any IBaseItem class.</param>
        public void AddItem(IBaseItem item)
        {
            windowLayout.GetContainer().AddItem(item);
        }

        /// <summary>
        /// Allows to add multiple items to the window.
        /// </summary>
        /// <param name="items">An instance of any IBaseItem class.</param>
        public void AddItems(params IBaseItem[] items)
        {
            foreach (IBaseItem item in items)
            {
                windowLayout.GetContainer().AddItem(item);
            }
        }
        /// <summary>
        /// Allows you to insert an item at a specified position.
        /// </summary>
        /// <param name="item">An instance of any IBaseItem class.</param>
        /// <param name="index">Index of position.</param>
        public void InsertItem(IBaseItem item, int index)
        {
            windowLayout.GetContainer().InsertItem(item, index);
        }
        /// <summary>
        /// Removing a specified item.
        /// </summary>
        /// <param name="item">An instance of any IBaseItem class.</param>
        /// <returns>True: if the window cantained the specified item and it was successfully removed. 
        /// False: if the window did not cantain the specified item.</returns>
        public bool RemoveItem(IBaseItem item)
        {
            return windowLayout.GetContainer().RemoveItem(item);
        }
        /// <summary>
        /// Removing all containing items in the window.
        /// </summary>
        public void Clear()
        {
            windowLayout.GetContainer().Clear();
        }

        private String _name;

        /// <summary>
        /// Setting the window name. The window name is the string ID of the window and may differ from the window title.
        /// </summary>
        /// <param name="value">Window name.</param>
        public void SetWindowName(String value)
        {
            _name = value;
            if (windowLayout != null)
                windowLayout.GetContainer().SetItemName(_name);
        }
        /// <summary>
        /// Getting the window name.
        /// </summary>
        /// <returns>Window name.</returns>
        public String GetWindowName()
        {
            return _name;
        }

        private String _title;

        /// <summary>
        /// Setting the window title text.
        /// </summary>
        /// <param name="title">Title text.</param>
        public void SetWindowTitle(String title)
        {
            _title = title;
        }
        /// <summary>
        /// Getting the title text.
        /// </summary>
        /// <returns>Title text.</returns>
        public String GetWindowTitle()
        {
            return _title;
        }

        // geometry
        private Geometry _itemGeometry = new Geometry();

        internal void SetWidthDirect(int width)
        {
            _itemGeometry.SetWidth(width);
            windowLayout.GetContainer().SetWidth(width);
        }
        /// <summary>
        /// Setting the window width.
        /// </summary>
        /// <param name="width">Width in pixels.</param>
        public void SetWidth(int width)
        {
            _itemGeometry.SetWidth(width);
            windowLayout.GetContainer().SetWidth(width);
            if (windowLayout.IsGLWIDValid())
            {
                windowLayout.UpdateSize();
            }
        }

        internal void SetHeightDirect(int height)
        {
            _itemGeometry.SetHeight(height);
            windowLayout.GetContainer().SetHeight(height);
        }
        /// <summary>
        /// Setting the window height.
        /// </summary>
        /// <param name="height">Height in pixels.</param>
        public void SetHeight(int height)
        {
            _itemGeometry.SetHeight(height);
            windowLayout.GetContainer().SetHeight(height);
            if (windowLayout.IsGLWIDValid())
            {
                windowLayout.UpdateSize();
            }
        }
        /// <summary>
        /// Setting the window size in pixels: width and height.
        /// </summary>
        /// <param name="width">Width in pixels.</param>
        /// <param name="height">Height in pixels.</param>
        public void SetSize(int width, int height)
        {
            _itemGeometry.SetWidth(width);
            windowLayout.GetContainer().SetWidth(width);
            _itemGeometry.SetHeight(height);
            windowLayout.GetContainer().SetHeight(height);

            if (windowLayout.IsGLWIDValid())
            {
                windowLayout.UpdateSize();
            }
        }
        /// <summary>
        /// Setting the window minimum width.
        /// </summary>
        /// <param name="width">Minimum width in pixels.</param>
        public void SetMinWidth(int width)
        {
            _itemGeometry.SetMinWidth(width);
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetMinWidth(width);
        }
        /// <summary>
        /// Setting the window minimum height.
        /// </summary>
        /// <param name="width">Minimum height in pixels.</param>
        public void SetMinHeight(int height)
        {
            _itemGeometry.SetMinHeight(height);
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetMinHeight(height);
        }
        /// <summary>
        /// Setting the minimum window size in pixels: width and height.
        /// </summary>
        /// <param name="width">Minimum width in pixels.</param>
        /// <param name="height">Minimum height in pixels.</param>
        public void SetMinSize(int width, int height)
        {
            SetMinWidth(width);
            SetMinHeight(height);
        }

        /// <summary>
        /// Setting the window maximum width.
        /// </summary>
        /// <param name="width">Maximum width in pixels.</param>
        public void SetMaxWidth(int width)
        {
            _itemGeometry.SetMaxWidth(width);
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetMaxWidth(width);
        }
        /// <summary>
        /// Setting the window maximum height.
        /// </summary>
        /// <param name="width">Maximum height in pixels.</param>
        public void SetMaxHeight(int height)
        {
            _itemGeometry.SetMaxHeight(height);
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetMaxHeight(height);
        }
        /// <summary>
        /// Setting the maximum window size in pixels: width and height.
        /// </summary>
        /// <param name="width">Maximum width in pixels.</param>
        /// <param name="height">Maximum height in pixels.</param>
        public void SetMaxSize(int width, int height)
        {
            SetMaxWidth(width);
            SetMaxHeight(height);
        }

        /// <summary>
        /// Getting the current minimum window width.
        /// </summary>
        /// <returns>Current minimum width in pixels.</returns>
        public int GetMinWidth()
        {
            return _itemGeometry.GetMinWidth();
        }
        /// <summary>
        /// Getting the current minimum window width.
        /// </summary>
        /// <returns>Current minimum width in pixels.</returns>
        public int GetWidth()
        {
            return _itemGeometry.GetWidth();
        }
        /// <summary>
        /// Getting the current maximum window width.
        /// </summary>
        /// <returns>Current maximum width in pixels.</returns>
        public int GetMaxWidth()
        {
            return _itemGeometry.GetMaxWidth();
        }
        /// <summary>
        /// Getting the current minimum window height.
        /// </summary>
        /// <returns>Current minimum height in pixels.</returns>
        public int GetMinHeight()
        {
            return _itemGeometry.GetMinHeight();
        }
        /// <summary>
        /// Getting the current window height.
        /// </summary>
        /// <returns>Current height in pixels.</returns>
        public int GetHeight()
        {
            return _itemGeometry.GetHeight();
        }
        /// <summary>
        /// Getting the current maximum window height.
        /// </summary>
        /// <returns>Current maximum height in pixels.</returns>
        public int GetMaxHeight()
        {
            return _itemGeometry.GetMaxHeight();
        }
        /// <summary>
        /// Getting the current window size.
        /// </summary>
        /// <returns>Current window size as SpaceVIL.Core.Size.</returns>
        public Core.Size GetSize()
        {
            return _itemGeometry.GetSize();
        }

        // position
        private Position _itemPosition = new Position(50, 50);

        internal void SetXDirect(int x)
        {
            _itemPosition.SetX(x);
        }
        /// <summary>
        /// Setting the window x-coordinate (the upper left window corner). Relocating the window at specified x-coordinate.
        /// </summary>
        /// <param name="x">X-Coordinate.</param>
        public void SetX(int x)
        {
            SetXDirect(x);
            if (windowLayout.IsGLWIDValid())
                windowLayout.UpdatePosition();
        }
        /// <summary>
        /// Getting the current window x-coordinate.
        /// </summary>
        /// <returns>Current x-coordinate.</returns>
        public int GetX()
        {
            return _itemPosition.GetX();
        }

        public void SetYDirect(int y)
        {
            _itemPosition.SetY(y);
        }

        /// <summary>
        /// Setting the window y-coordinate (the upper left window corner). Relocating the window at specified y-coordinate.
        /// </summary>
        /// <param name="y">Y-Coordinate.</param>
        public void SetY(int y)
        {
            SetYDirect(y);
            if (windowLayout.IsGLWIDValid())
                windowLayout.UpdatePosition();
        }
        /// <summary>
        /// Getting the current window y-coordinate.
        /// </summary>
        /// <returns>Current y-coordinate.</returns>
        public int GetY()
        {
            return _itemPosition.GetY();
        }

        /// <summary>
        /// Setting the window x-coordinate and y-coordinate (the upper left window corner). Relocating the window at specified coordinates.
        /// </summary>
        /// <param name="x">X-Coordinate.</param>
        /// <param name="y">Y-Coordinate.</param>
        public void SetPosition(int x, int y)
        {
            _itemPosition.SetPosition(x, y);

            if (windowLayout.IsGLWIDValid())
                windowLayout.UpdatePosition();
        }
        /// <summary>
        /// Setting the window x-coordinate and y-coordinate (the upper left window corner). Relocating the window at specified coordinates.
        /// </summary>
        /// <param name="position">X-coordinate and Y-coordinate provided as SpaceVIL.Core.Position</param>
        public void SetPosition(Position position)
        {
            _itemPosition.SetPosition(position.GetX(), position.GetY());

            if (windowLayout.IsGLWIDValid())
                windowLayout.UpdatePosition();
        }
        /// <summary>
        /// Getting the current window position.
        /// </summary>
        /// <returns>X-coordinate and Y-coordinate provided as SpaceVIL.Core.Position</returns>
        public Position GetPosition()
        {
            return _itemPosition;
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
            IsFullScreen = false;
        }

        /// <summary>
        /// <para/>A flag that determines whether the current window is dialog or not.
        /// <para/>True: window is dialog. False: window is NOT dialog.
        /// <para/>Default: False.
        /// </summary>
        public bool IsDialog;
        /// <summary>
        /// <para/>A flag that determines whether the current window is in closed state or not.
        /// <para/>True: window is closed. False: window is opened.
        /// <para/>Default: True.
        /// </summary>
        public bool IsClosed;
        /// <summary>
        /// <para/>A flag that determines whether the current window is in hidden state or not.
        /// <para/>True: window is hidden. False: window is unhidden.
        /// <para/>Default: False.
        /// </summary>
        public bool IsHidden;
        /// <summary>
        /// <para/>A flag that determines whether the current window can be resize or not.
        /// <para/>True: window is resizable. False: window is NOT resizable.
        /// <para/>Default: True.
        /// </summary>
        public bool IsResizable;
        /// <summary>
        /// <para/>A flag that determines whether the current window is always on top of all other windows or not.
        /// <para/>True: window is on top. False: window is NOT on top.
        /// <para/>Default: False.
        /// </summary>
        public bool IsAlwaysOnTop;
        /// <summary>
        /// <para/>A flag that shows/hides native the current window border decoration.
        /// <para/>True: native window border is HIDDEN. False: native window border is SHOWN.
        /// <para/>Default: False.
        /// </summary>
        public bool IsBorderHidden;
        /// <summary>
        /// <para/>A flag that determines whether the current window will first appear in the center of the screen or not.
        /// <para/>True: window is centered. False: window is NOT centered.
        /// <para/>Default: True.
        /// </summary>
        public bool IsCentered;
        /// <para/>A flag that determines whether the current window can be in focused state or not.
        /// <para/>True: window is focusable. False: window is NOT focusable.
        /// <para/>Default: True.
        public bool IsFocusable;
        /// <summary>
        /// <para/>A flag that determines whether the current window can be closed if the mouse is clicked outside of the current window or not.
        /// <para/>True: window can be closed if the mouse is clicked outside. False: window can NOT be closed if the mouse is clicked outside.
        /// <para/>Default: False.
        /// </summary>
        public bool IsOutsideClickClosable;
        /// <summary>
        /// <para/>A flag that determines whether the current window will first appear maximized or not.
        /// <para/>True: window will first appear maximized. False: window will NOT first appear maximized.
        /// <para/>Default: False.
        /// </summary>
        public bool IsMaximized;
        /// <summary>
        /// <para/>A flag that determines whether the current window can be transparent or not.
        /// <para/>True: window can be transparent. False: window can NOT be transparent.
        /// <para/>Default: False.
        /// </summary>
        public bool IsTransparent;
        /// <summary>
        /// <para/>A flag that determines whether the current window will first appear in fullscreen mode or not.
        /// <para/>True: window will first appear in fullscreen mode. False: window will NOT first appear in fullscreen mode.
        /// <para/>Default: False.
        /// </summary>
        internal bool IsFullScreen;


        internal MSAA AntiAliasingMode = MSAA.MSAA4x;

        /// <summary>
        /// Setting the anti aliasing quality (off, x2, x4, x8).
        /// Default: MSAA.MSAA4x
        /// </summary>
        /// <param name="msaa">SpaceVIL.Core.MSAA anti aliasing quality.</param>
        public void SetAntiAliasingQuality(MSAA msaa)
        {
            AntiAliasingMode = msaa;
        }

        internal void SetFocusable(bool value)
        {
            windowLayout.SetFocusable(value);
        }

        private bool _isFocused;
        /// <summary>
        /// Lets to know if the current window is focused or not.
        /// </summary>
        /// <returns>True: if the current window is focused. False: if the current window is unfocused.</returns>
        public bool IsFocused()
        {
            return _isFocused;
        }

        /// <summary>
        /// Lets to manage focus state ot the current window.
        /// </summary>
        /// <param name="value">True: if you want the window to be focused. False: if you want the window to be unfocused.</param>
        public void SetFocus(bool value)
        {
            if (_isFocused == value)
                return;
            _isFocused = value;
            if (value)
                windowLayout.SetFocus();
        }

        /// <summary>
        /// Sets the window focused.
        /// </summary>
        public void SetWindowFocused()
        {
            windowLayout.SetFocus();
        }

        /// <summary>
        /// Sets the window minimized.
        /// </summary>
        public void Minimize()
        {
            windowLayout.Minimize();
        }

        /// <summary>
        /// Sets the window maximized.
        /// </summary>
        public void Maximize()
        {
            if (Common.CommonService.GetOSType() != OSType.Mac)
                windowLayout.Maximize();
            else
                MacOSMaximize();
        }

        private Area _savedArea = new Area();

        private void MacOSMaximize()
        {
            if (!IsMaximized)
            {
                _savedArea.SetAttr(GetX(), GetY(), GetWidth(), GetHeight());
                Area area = GetWorkArea();
                SetPosition(area.GetX(), area.GetY());
                SetSize(area.GetWidth(), area.GetHeight());
                IsMaximized = true;
            }
            else
            {
                SetPosition(_savedArea.GetX(), _savedArea.GetY());
                SetSize(_savedArea.GetWidth(), _savedArea.GetHeight());
                IsMaximized = false;
            }
        }

        /// <summary>
        /// Toggles the window to full screen mode or windowed mode.
        /// </summary>
        public void ToggleFullScreen()
        {
            windowLayout.ToggleFullScreen();
        }

        /// <summary>
        /// Getting the current focused item in the current window.
        /// </summary>
        /// <returns>SpaceVIL.Prototype (abstract class for interactive items).</returns>
        public Prototype GetFocusedItem()
        {
            return windowLayout.GetFocusedItem();
        }
        /// <summary>
        /// Setting the specified item to the focused state.
        /// </summary>
        /// <param name="item">Any item that can be focused and extends of SpaceVIL.Prototype (abstract class for interactive items).</param>
        public void SetFocusedItem(Prototype item)
        {
            windowLayout.SetFocusedItem(item);
        }

        internal void ResetItems()
        {
            windowLayout.ResetItems();
        }

        /// <summary>
        /// Returns focus to the root item of the window.
        /// </summary>
        /// 
        public void ResetFocus()
        {
            windowLayout.ResetFocus();
        }

        /// <summary>
        /// Sets the icons of the current window.
        /// </summary>
        /// <param name="iconBig">Task bar icon.</param>
        /// <param name="iconSmall">Title bar icon.</param>
        public void SetIcon(Bitmap iconBig, Bitmap iconSmall)
        {
            windowLayout.SetIcon(iconBig, iconSmall);
        }

        /// <summary>
        /// Hides of unhides the current window.
        /// </summary>
        /// <param name="value">True: if you want to hide the window. False: if you want tu unhide the window.</param>
        public void SetHidden(bool value)
        {
            windowLayout.SetHidden(value);
            IsHidden = value;
        }

        /// <summary>
        /// Lets to set the rendering frequency.
        /// Default: SpaceVIL.Core.RedrawFrequency.Low
        /// </summary>
        /// <param name="value"></param>
        public void SetRenderFrequency(RedrawFrequency value)
        {
            WindowManager.SetRenderFrequency(value);
        }
        /// <summary>
        /// Getting the current rendering frequency.
        /// </summary>
        /// <returns>Rendering frequency as SpaceVIL.Core.RedrawFrequency</returns>
        public RedrawFrequency GetRenderFrequency()
        {
            return WindowManager.GetRenderFrequency();
        }

        /// <summary>
        /// Lets to describe the actions when the window starts.
        /// </summary>
        public EventCommonMethod EventOnStart;
        /// <summary>
        /// Lets to describe the actions when closing the window.
        /// </summary>
        public EventCommonMethod EventClose;
        /// <summary>
        /// Lets to describe the actions when you drag&drop files/folders to the current window.
        /// </summary>
        public EventWindowDropMethod EventDrop;

        internal void Release()
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

        internal int RatioW = -1;
        internal int RatioH = -1;
        internal bool IsKeepAspectRatio = false;

        /// <summary>
        /// Lets to set aspect ratio.
        /// </summary>
        /// <param name="w">Width value.</param>
        /// <param name="h">Height value.</param>
        public void SetAspectRatio(int w, int h)
        {
            IsKeepAspectRatio = true;
            RatioW = w;
            RatioH = h;
        }

        private EventCommonMethod EventMinimize;
        private EventCommonMethod EventHide;

        internal EventCommonMethodState EventFocusGet;
        internal EventCommonMethodState EventFocusLost;

        /// <summary>
        /// ATTENTION!
        /// Not implemented yet.
        /// </summary>
        public EventCommonMethodState EventResize;
        /// <summary>
        /// ATTENTION!
        /// Not implemented yet.
        /// </summary>
        public EventCommonMethodState EventDestroy;

        /// <summary>
        /// Lets to describe the actions when mouse cursor hovers the root item of the window.
        /// </summary>
        public EventMouseMethodState EventMouseHover;
        /// <summary>
        /// Lets to describe the actions when mouse cursor leaves the root item of the window.
        /// </summary>
        public EventMouseMethodState EventMouseLeave;
        /// <summary>
        /// Lets to describe the actions when the root item of the window was clicked.
        /// </summary>
        public EventMouseMethodState EventMouseClick;
        /// <summary>
        /// Lets to describe the actions when the root item of the window was double clicked.
        /// </summary>
        public EventMouseMethodState EventMouseDoubleClick;
        /// <summary>
        /// Lets to describe the actions when the root item of the window was pressed.
        /// </summary>
        public EventMouseMethodState EventMousePress;
        /// <summary>
        /// Lets to describe the actions when the mouse button was pressed and moved inside the root item of the window.
        /// </summary>
        public EventMouseMethodState EventMouseDrag;
        /// <summary>
        /// Lets to describe the actions when the mouse button was released after dragging.
        /// </summary>
        public EventMouseMethodState EventMouseDrop;
        /// <summary>
        /// Lets to describe the actions when mouse wheel scrolls up.
        /// </summary>
        public EventMouseMethodState EventScrollUp;
        /// <summary>
        /// Lets to describe the actions when mouse wheel scrolls down.
        /// </summary>
        public EventMouseMethodState EventScrollDown;
        /// <summary>
        /// Lets to describe the actions when a keyboard key was pressed.
        /// </summary>
        public EventKeyMethodState EventKeyPress;
        /// <summary>
        /// Lets to describe the actions when a keyboard key was released.
        /// </summary>
        public EventKeyMethodState EventKeyRelease;
        /// <summary>
        /// Lets to describe the actions when you type text.
        /// </summary>
        public EventInputTextMethodState EventTextInput;

        private void FreeEvents()
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


        /// <summary>
        /// Setting the border of the root item of the window.
        /// </summary>
        /// <param name="border">Border as SpaceVIL.Decorations.Border</param>
        public void SetBorder(Border border)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorder(border);
        }
        /// <summary>
        /// Setting the color of the window border.
        /// </summary>
        /// <param name="fill">Color as System.Drawing.Color</param>
        public void SetBorderFill(Color fill)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderFill(fill);
        }
        /// <summary>
        /// Setting the color of the window border.
        /// </summary>
        /// <param name="r">Red (0 - 255)</param>
        /// <param name="g">Green (0 - 255)</param>
        /// <param name="b">Blur (0 - 255)</param>
        public void SetBorderFill(int r, int g, int b)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderFill(r, g, b);
        }
        /// <summary>
        /// Setting the color of the window border.
        /// </summary>
        /// <param name="r">Red (0 - 255)</param>
        /// <param name="g">Green (0 - 255)</param>
        /// <param name="b">Blur (0 - 255)</param>
        /// <param name="a">Alpha (0 - 255)</param>
        public void SetBorderFill(int r, int g, int b, int a)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderFill(r, g, b, a);
        }
        /// <summary>
        /// Setting the color of the window border.
        /// </summary>
        /// <param name="r">Red (0.0f - 1.0f)</param>
        /// <param name="g">Green (0.0f - 1.0f)</param>
        /// <param name="b">Blur (0.0f - 1.0f)</param>
        public void SetBorderFill(float r, float g, float b)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderFill(r, g, b);
        }
        /// <summary>
        /// Setting the color of the window border.
        /// </summary>
        /// <param name="r">Red (0.0f - 1.0f)</param>
        /// <param name="g">Green (0.0f - 1.0f)</param>
        /// <param name="b">Blur (0.0f - 1.0f)</param>
        /// <param name="a">Alpha (0.0f - 1.0f)</param>
        public void SetBorderFill(float r, float g, float b, float a)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderFill(r, g, b, a);
        }
        /// <summary>
        /// Setting the corner radii of the window border.
        /// </summary>
        /// <param name="radius">Corner radii as SpaceVIL.Decorations.CornerRadius</param>
        public void SetBorderRadius(CornerRadius radius)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderRadius(radius);
        }
        /// <summary>
        /// Setting the common corner radius of the window border.
        /// </summary>
        /// <param name="radius">The corner radius.</param>
        public void SetBorderRadius(int radius)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderRadius(new CornerRadius(radius));
        }
        /// <summary>
        /// Setting the window border thickness.
        /// </summary>
        /// <param name="thickness">The border thickness.</param>
        public void SetBorderThickness(int thickness)
        {
            if (windowLayout.GetContainer() != null)
                windowLayout.GetContainer().SetBorderThickness(thickness);
        }
        /// <summary>
        /// Getting the current window border corner radii.
        /// </summary>
        /// <returns>Corner radii as SpaceVIL.Decorations.CornerRadius</returns>
        public CornerRadius GetBorderRadius()
        {
            if (windowLayout.GetContainer() != null)
                return windowLayout.GetContainer().GetBorderRadius();
            return null;
        }
        /// <summary>
        /// Getting the current window border thickness.
        /// </summary>
        /// <returns>The current thickness.</returns>
        public int GetBorderThickness()
        {
            if (windowLayout.GetContainer() != null)
                return windowLayout.GetContainer().GetBorderThickness();
            return 0;
        }
        /// <summary>
        /// Getting the current window border color.
        /// </summary>
        /// <returns>The border color as System.Drawing.Color</returns>
        public Color GetBorderFill()
        {
            if (windowLayout.GetContainer() != null)
                return windowLayout.GetContainer().GetBorderFill();
            return Color.Transparent;
        }
        /// <summary>
        /// Getting the GLFW ID of the window.
        /// </summary>
        /// <returns>ID of the window.</returns>
        public Int64 GetGLWID()
        {
            return GetLayout().GetGLWID();
        }

        internal bool InitEngine()
        {
            return windowLayout.InitEngine();
        }
        internal void UpdateScene()
        {
            windowLayout.UpdateScene();
        }

        internal void Dispose()
        {
            windowLayout.Dispose();
        }

        internal CoreWindow GetPairForCurrentWindow()
        {
            return windowLayout.GetPairForCurrentWindow();
        }

        /// <summary>
        /// Setting the dimmer color of the window. The dimmer appears when the current window opens a dialog window.
        /// </summary>
        /// <param name="color">The dimmer color as System.Drawing.Color</param>
        public void SetShadeColor(Color color)
        {
            windowLayout.SetShadeColor(color);
        }
        /// <summary>
        /// Setting the dimmer color of the window. The dimmer appears when the current window opens a dialog window.
        /// </summary>
        /// <param name="r">Red (0 - 255)</param>
        /// <param name="g">Green (0 - 255)</param>
        /// <param name="b">Blur (0 - 255)</param>
        public void SetShadeColor(int r, int g, int b)
        {
            windowLayout.SetShadeColor(r, g, b);
        }
        /// <summary>
        /// Setting the dimmer color of the window. The dimmer appears when the current window opens a dialog window.
        /// </summary>
        /// <param name="r">Red (0 - 255)</param>
        /// <param name="g">Green (0 - 255)</param>
        /// <param name="b">Blur (0 - 255)</param>
        /// <param name="a">Alpha (0 - 255)</param>
        public void SetShadeColor(int r, int g, int b, int a)
        {
            windowLayout.SetShadeColor(r, g, b, a);
        }
        /// <summary>
        /// Setting the dimmer color of the window. The dimmer appears when the current window opens a dialog window.
        /// </summary>
        /// <param name="r">Red (0.0f - 1.0f)</param>
        /// <param name="g">Green (0.0f - 1.0f)</param>
        /// <param name="b">Blur (0.0f - 1.0f)</param>
        public void SetShadeColor(float r, float g, float b)
        {
            windowLayout.SetShadeColor(r, g, b);
        }
        /// <summary>
        /// Setting the dimmer color of the window. The dimmer appears when the current window opens a dialog window.
        /// </summary>
        /// <param name="r">Red (0.0f - 1.0f)</param>
        /// <param name="g">Green (0.0f - 1.0f)</param>
        /// <param name="b">Blur (0.0f - 1.0f)</param>
        /// <param name="a">Alpha (0.0f - 1.0f)</param>
        public void SetShadeColor(float r, float g, float b, float a)
        {
            windowLayout.SetShadeColor(r, g, b, a);
        }
        /// <summary>
        /// Getting the current dimmer color.
        /// </summary>
        /// <returns>The dimmer color as System.Drawing.Color</returns>
        public Color GetShadeColor()
        {
            return windowLayout.GetShadeColor();
        }

        internal void FreeVRAMResource<T>(T resource)
        {
            GetLayout().FreeVRAMResource(resource);
        }
        /// <summary>
        /// Getting the area of a primary monitor. The work area not occupied by global task bars or menu bars.
        /// </summary>
        /// <returns></returns>
        public Area GetWorkArea()
        {
            Glfw3.Glfw.Monitor monitor = Glfw3.Glfw.GetPrimaryMonitor();
            if (monitor != null)
            {
                int x, y, w, h;
                Glfw3.Glfw.GetMonitorWorkArea(monitor, out x, out y, out w, out h);
                return new Area(x, y, w, h);
            }
            return null;
        }

        private Scale _windowScale = new Scale();
        /// <summary>
        /// Get DPI scale for the current window.
        /// </summary>
        /// <returns>DPI scale as SpaceVIL.Core.Scale</returns>
        public Scale GetDpiScale()
        {
            return _windowScale;
        }

        internal void SetWindowScale(float x, float y)
        {
            _windowScale.SetScale(x, y);
        }
    }
}
