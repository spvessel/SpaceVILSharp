using System;
using System.Collections.Generic;
using System.Drawing;
using Glfw3;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    abstract public class Prototype : IBaseItem
    {
        private VisualItem _core = new VisualItem();
        internal VisualItem GetCore()
        {
            return _core;
        }
        internal void SetCore(VisualItem core)
        {
            _core = core;
        }
        static int count = 0;

        /// <summary>
        /// Constructs a Prototype
        /// </summary>
        public Prototype()
        {
            _core.SetItemName("VisualItem_" + count);
            count++;
            _core._main = this;
        }

        /// <summary>
        /// Common events (resize, destroy)
        /// </summary>        
        internal EventCommonMethodState EventFocusGet;
        internal EventCommonMethodState EventFocusLost;
        public EventCommonMethodState EventResize;
        public EventCommonMethodState EventDestroy;

        /// <summary>
        /// Mouse input events
        /// </summary>
        public EventMouseMethodState EventMouseHover;
        public EventMouseMethodState EventMouseLeave;
        public EventMouseMethodState EventMouseClick;
        public EventMouseMethodState EventMouseDoubleClick;
        public EventMouseMethodState EventMousePress;
        public EventMouseMethodState EventMouseDrag;
        public EventMouseMethodState EventMouseDrop;
        public EventMouseMethodState EventScrollUp;
        public EventMouseMethodState EventScrollDown;

        /// <summary>
        /// Keyboard input events
        /// </summary>
        public EventKeyMethodState EventKeyPress;
        public EventKeyMethodState EventKeyRelease;

        /// <summary>
        /// Text input events
        /// </summary>
        public EventInputTextMethodState EventTextInput;

        internal void FreeEvents()
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

        public virtual void Release() { }

        /// <summary>
        /// Set parent window for the Prototype
        /// </summary>
        public void SetHandler(WindowLayout handler)
        {
            _core.SetHandler(handler);
        }
        public WindowLayout GetHandler()
        {
            return _core.GetHandler();
        }

        /// <summary>
        /// tooltip of the Prototype
        /// </summary>
        public String GetToolTip()
        {
            return _core.GetToolTip();
        }
        public void SetToolTip(String text)
        {
            _core.SetToolTip(text);
        }

        /// <summary>
        /// Prototype parent item
        /// </summary>
        public Prototype GetParent()
        {
            return _core.GetParent();
        }
        public void SetParent(Prototype parent)
        {
            _core.SetParent(parent);
        }

        /// <summary>
        /// Spacing(horizontal, vertical) of the Prototype
        /// </summary>
        public Spacing GetSpacing()
        {
            return _core.GetSpacing();
        }
        public void SetSpacing(Spacing spacing)
        {
            _core.SetSpacing(spacing);
        }
        public void SetSpacing(int horizontal = 0, int vertical = 0)
        {
            _core.SetSpacing(horizontal, vertical);
        }

        /// <summary>
        /// Prototype padding (left, top, right, bottom)
        /// </summary>
        public Indents GetPadding()
        {
            return _core.GetPadding();
        }
        public void SetPadding(Indents padding)
        {
            _core.SetPadding(padding);
        }
        public void SetPadding(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _core.SetPadding(left, top, right, bottom);
        }

        /// <summary>
        /// Margin of the Prototype (left, top, right, bottom)
        /// </summary>
        public Indents GetMargin()
        {
            return _core.GetMargin();
        }
        public void SetMargin(Indents margin)
        {
            _core.SetMargin(margin);
        }
        public void SetMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _core.SetMargin(left, top, right, bottom);
        }

        /// <param name="border"> Border of the Prototype </param>
        public void SetBorder(Border border)
        {
            _core.SetBorder(border);
        }

        /// <summary>
        /// Prototype border color
        /// </summary>
        public void SetBorderFill(Color fill)
        {
            _core.SetBorderFill(fill);
        }
        public Color GetBorderFill()
        {
            return _core.GetBorderFill();
        }
        public void SetBorderFill(int r, int g, int b, int a = 255)
        {
            _core.SetBorderFill(r, g, b, a);
        }
        public void SetBorderFill(float r, float g, float b, float a = 1.0f)
        {
            _core.SetBorderFill(r, g, b, a);
        }

        /// <summary>
        /// Radius of the border's corners
        /// </summary>
        public void SetBorderRadius(CornerRadius radius)
        {
            _core.SetBorderRadius(radius);
        }
        public void SetBorderRadius(int radius)
        {
            _core.SetBorderRadius(new CornerRadius(radius));
        }
        public CornerRadius GetBorderRadius()
        {
            return _core.GetBorderRadius();
        }

        /// <summary>
        /// Border thickness of the Prototype
        /// </summary>
        public void SetBorderThickness(int thickness)
        {
            _core.SetBorderThickness(thickness);
        }
        public int GetBorderThickness()
        {
            return _core.GetBorderThickness();
        }

        /// <summary>
        /// Initialization and adding of all elements in the Prototype
        /// </summary>
        public virtual void InitElements()
        {
            _core.InitElements();
        }

        /// <summary>
        /// Make/get shape of the item using triangles list
        /// </summary>
        public List<float[]> GetTriangles()
        {
            return _core.GetTriangles();
        }
        public virtual void SetTriangles(List<float[]> triangles)
        {
            _core.SetTriangles(triangles);
        }
        public virtual List<float[]> MakeShape()
        {
            return _core.MakeShape();
        }

        /// <summary>
        /// Text color in the Prototype
        /// </summary>
        public virtual void SetBackground(Color color)
        {
            _core.SetBackground(color);
        }
        public virtual void SetBackground(int r, int g, int b)
        {
            _core.SetBackground(r, g, b);
        }
        public virtual void SetBackground(int r, int g, int b, int a)
        {
            _core.SetBackground(r, g, b, a);
        }
        public virtual void SetBackground(float r, float g, float b)
        {
            _core.SetBackground(r, g, b);
        }
        public virtual void SetBackground(float r, float g, float b, float a)
        {
            _core.SetBackground(r, g, b, a);
        }
        public virtual Color GetBackground()
        {
            return _core.GetBackground();
        }

        /// <summary>
        /// Name of the Prototype
        /// </summary>
        public void SetItemName(string name)
        {
            _core.SetItemName(name);
        }
        public string GetItemName()
        {
            return _core.GetItemName();
        }

        /// <summary>
        /// Prototype minimum width
        /// </summary>
        public void SetMinWidth(int width)
        {
            _core.SetMinWidth(width);
        }
        public int GetMinWidth()
        {
            return _core.GetMinWidth();
        }

        /// <summary>
        /// Prototype width
        /// </summary>
        public virtual void SetWidth(int width)
        {
            _core.SetWidth(width);
        }
        public virtual int GetWidth()
        {
            return _core.GetWidth();
        }

        /// <summary>
        /// Prototype maximum width
        /// </summary>
        public void SetMaxWidth(int width)
        {
            _core.SetMaxWidth(width);
        }
        public int GetMaxWidth()
        {
            return _core.GetMaxWidth();
        }

        /// <summary>
        /// Prototype minimum height
        /// </summary>
        public void SetMinHeight(int height)
        {
            _core.SetMinHeight(height);
        }
        public int GetMinHeight()
        {
            return _core.GetMinHeight();
        }

        /// <summary>
        /// Prototype height
        /// </summary>
        public virtual void SetHeight(int height)
        {
            _core.SetHeight(height);
        }
        public virtual int GetHeight()
        {
            return _core.GetHeight();
        }

        /// <summary>
        /// Prototype maximum height
        /// </summary>
        public void SetMaxHeight(int height)
        {
            _core.SetMaxHeight(height);
        }
        public int GetMaxHeight()
        {
            return _core.GetMaxHeight();
        }

        /// <summary>
        /// Prototype size (width, height)
        /// </summary>
        public virtual void SetSize(int width, int height)
        {
            _core.SetSize(width, height);
        }
        public int[] GetSize()
        {
            return _core.GetSize();
        }

        /// <summary>
        /// Prototype minimum size (width, height)
        /// </summary>
        public void SetMinSize(int width, int height)
        {
            _core.SetMinSize(width, height);
        }
        public int[] GetMinSize()
        {
            return _core.GetMinSize();
        }

        /// <summary>
        /// Prototype maximum size (width, height)
        /// </summary>
        public void SetMaxSize(int width, int height)
        {
            _core.SetMaxSize(width, height);
        }
        public int[] GetMaxSize()
        {
            return _core.GetMaxSize();
        }

        /// <summary>
        /// Prototype alignment
        /// </summary>
        public void SetAlignment(ItemAlignment alignment)
        {
            _core.SetAlignment(alignment);
        }
        public void SetAlignment(params ItemAlignment[] alignment)
        {
            _core.SetAlignment(alignment);
        }
        public ItemAlignment GetAlignment()
        {
            return _core.GetAlignment();
        }

        /// <summary>
        /// Prototype size policy (FIXED, EXPAND) by width and height
        /// </summary>
        public void SetSizePolicy(SizePolicy width, SizePolicy height)
        {
            _core.SetSizePolicy(width, height);
        }
        public void SetWidthPolicy(SizePolicy policy)
        {
            _core.SetWidthPolicy(policy);
        }
        public SizePolicy GetWidthPolicy()
        {
            return _core.GetWidthPolicy();
        }
        public void SetHeightPolicy(SizePolicy policy)
        {
            _core.SetHeightPolicy(policy);
        }
        public SizePolicy GetHeightPolicy()
        {
            return _core.GetHeightPolicy();
        }

        /// <summary>
        /// Prototype position
        /// </summary>
        /// <param name="x"> X position of the left top corner </param>
        /// <param name="y"> Y position of the left top corner </param>
        public virtual void SetPosition(int x, int y)
        {
            _core.SetX(x);
            _core.SetY(y);
        }
        public virtual void SetX(int x)
        {
            _core.SetX(x);
        }
        public virtual int GetX()
        {
            return _core.GetX();
        }
        public virtual void SetY(int y)
        {
            _core.SetY(y);
        }
        public virtual int GetY()
        {
            return _core.GetY();
        }

        /// <summary>
        /// Set default confines of the Prototype based on parent properties
        /// </summary>
        public virtual void SetConfines()
        {
            _core.SetConfines();
        }

        /// <summary>
        /// Set Prototype confines
        /// </summary>
        public virtual void SetConfines(int x0, int x1, int y0, int y1)
        {
            _core._confines_x_0 = x0;
            _core._confines_x_1 = x1;
            _core._confines_y_0 = y0;
            _core._confines_y_1 = y1;
        }

        /// <summary>
        /// Set style of the Prototype
        /// </summary>
        public virtual void SetStyle(Style style)
        {
            _core.SetStyle(style);
        }

        /// <summary>
        /// Generate all item properties in one style
        /// </summary>
        public virtual Style GetCoreStyle()
        {
            return _core.GetCoreStyle();
        }

        // public virtual void SetInnerStyle(String element, Style style)
        // {
        //     _core.SetInnerStyle(element, style);
        // }

        /// <returns> if Prototype has shadow </returns>
        public bool IsShadowDrop()
        {
            return _core.IsShadowDrop();
        }
        public void SetShadowDrop(bool value)
        {
            _core.SetShadowDrop(value);
        }

        /// <summary>
        /// Radius of the shadows corners
        /// </summary>
        public void SetShadowRadius(int radius)
        {
            _core.SetShadowRadius(radius);
        }
        public int GetShadowRadius()
        {
            return _core.GetShadowRadius();
        }

        /// <summary>
        /// Shadow color
        /// </summary>
        public Color GetShadowColor()
        {
            return _core.GetShadowColor();
        }
        public void SetShadowColor(Color color)
        {
            _core.SetShadowColor(color);
        }

        /// <summary>
        /// Prototype's shadow position
        /// </summary>
        public Position GetShadowPos()
        {
            return _core.GetShadowPos();
        }

        /// <summary>
        /// Set Prototype's shadow
        /// </summary>
        /// <param name="radius"> Radius of the shadow's corners </param>
        /// <param name="x"> X position of the shadow </param>
        /// <param name="y"> Y Position of the shadow </param>
        /// <param name="color"> Shadow color </param>
        public void SetShadow(int radius, int x, int y, Color color)
        {
            _core.SetShadow(radius, x, y, color);
        }

        /// <summary>
        /// Set focus on the Prototype if its focusable
        /// </summary>
        public void SetFocus()
        {
            if (IsFocusable)
                GetHandler().SetFocusedItem(this);
        }

        /// <summary>
        /// Add new item state for one of the item state types (BASE,
        /// HOVERED, PRESSED, TOGGLED, FOCUSED, DISABLED)
        /// </summary>
        public void AddItemState(ItemStateType type, ItemState state)
        {
            _core.AddItemState(type, state);
        }

        /// <summary>
        /// Remove item state
        /// </summary>
        public void RemoveItemState(ItemStateType type)
        {
            _core.RemoveItemState(type);
        }

        /// <summary>
        /// Remove all item states
        /// </summary>
        public void RemoveAllItemStates()
        {
            _core.RemoveAllItemStates();
        }

        /// <returns> Returns ItemState for the ItemStateType type </returns>
        public ItemState GetState(ItemStateType type)
        {
            return _core.GetState(type);
        }

        /// <summary>
        /// Update Prototype's state according to its ItemStateType
        /// </summary>
        protected virtual void UpdateState()
        {
            _core.UpdateState();
        }

        /// <summary>
        /// Insert item to the Prototype. If Prototype has more items
        /// than index replace existing item, else add new item
        /// </summary>
        public virtual void InsertItem(IBaseItem item, Int32 index)
        {
            _core.InsertItem(item, index);
        }

        /// <summary>
        /// Add items into the Prototype
        /// </summary>
        public virtual void AddItems(params IBaseItem[] items)
        {
            foreach (var item in items)
            {
                this.AddItem(item);
            }
        }
        public virtual void AddItem(IBaseItem item)
        {
            _core.AddItem(item);
        }

        /// <summary>
        /// Update Prototype size or position depending on the GeometryEventType
        /// </summary>
        public void Update(GeometryEventType type, int value = 0)
        {
            _core.Update(type, value);
        }

        /// <summary>
        /// Is Prototype and its inner items drawable
        /// </summary>
        public virtual bool IsDrawable()
        {
            return _core.IsDrawable();
        }
        public void SetDrawable(bool value)
        {
            _core.SetDrawable(value);
        }

        /// <summary>
        /// Is Prototype visible
        /// </summary>
        public virtual bool IsVisible()
        {
            return _core.IsVisible();
        }
        public virtual void SetVisible(bool value)
        {
            _core.SetVisible(value);
        }

        /// <summary>
        /// Is Prototype pass events throw itself
        /// </summary>
        public virtual bool IsPassEvents()
        {
            return _core.IsPassEvents();
        }

        /// <summary>
        /// Is Prototype pass the InputEventType throw
        /// </summary>
        public bool IsPassEvents(InputEventType e)
        {
            if (_core.GetPassEvents().HasFlag(e))
                return false;
            return true;
        }

        /// <summary>
        /// Sets all InputEventType passed or non passed (value)
        /// </summary>
        public void SetPassEvents(bool value)
        {
            _core.SetPassEvents(value);
        }

        /// <param name="value"> passed or non passed </param>
        /// <param name="e"> InputEventType </param>
        public void SetPassEvents(bool value, InputEventType e)
        {
            _core.SetPassEvents(value, e);
        }
        /// <param name="value"> passed or non passed </param>
        /// <param name="e"> InputEventType </param>
        public void SetPassEvents(bool value, params InputEventType[] e)
        {
            _core.SetPassEvents(value, e);
        }

        /// <summary>
        /// Is Prototype disabled
        /// </summary>
        public virtual bool IsDisabled()
        {
            return _core.IsDisabled();
        }
        public virtual void SetDisabled(bool value)
        {
            _core.SetDisabled(value);
        }

        /// <summary>
        /// Is mouse hover on the Prototype
        /// </summary>
        public virtual bool IsMouseHover()
        {
            return _core.IsMouseHover();
        }
        public virtual void SetMouseHover(bool value)
        {
            _core.SetMouseHover(value);
        }

        /// <summary>
        /// Is mouse pressed on the Prototype
        /// </summary>
        public virtual bool IsMousePressed()
        {
            return _core.IsMousePressed();
        }
        public virtual void SetMousePressed(bool value)
        {
            _core.SetMousePressed(value);
        }

        /// <summary>
        /// Is Prototype focusable
        /// </summary>
        public bool IsFocusable = true;

        /// <summary>
        /// Is Prototype focused
        /// </summary>
        public virtual bool IsFocused()
        {
            return _core.IsFocused();
        }
        public virtual void SetFocused(bool value)
        {
            if (IsFocusable)
            {
                _core.SetFocused(value);
            }
        }

        internal virtual bool GetHoverVerification(float xpos, float ypos)
        {
            return _core.GetHoverVerification(xpos, ypos);
        }

        /// <summary>
        /// Returns list of the Prototype's inner items
        /// </summary>
        public virtual List<IBaseItem> GetItems()
        {
            return _core.GetItems();
        }

        /// <summary>
        /// Remove item from the Prototype
        /// </summary>
        public virtual void RemoveItem(IBaseItem item)
        {
            _core.RemoveItem(item);
        }

        public virtual void Clear()
        {
            _core.Clear();
        }

        internal void AddEventListener(GeometryEventType type, IBaseItem listener)
        {
            _core.AddEventListener(type, listener);
        }
        internal void RemoveEventListener(GeometryEventType type, IBaseItem listener)
        {
            _core.RemoveEventListener(type, listener);
        }

        /// <summary>
        /// Prototype confines
        /// </summary>
        public int[] GetConfines()
        {
            return _core.GetConfines();
        }

        internal ItemStateType GetCurrentState()
        {
            return _core.GetCurrentState();
        }

        internal void SetState(ItemStateType state)
        {
            _core.SetState(state);
        }

        // /// <summary>
        // /// Set list of the Prototype's inner items. Old items will be removed
        // /// </summary>
        internal void SetContent(List<IBaseItem> content)
        {
            List<IBaseItem> oldContent = GetItems();
            if (oldContent.Count != content.Count)
                return;

            foreach (IBaseItem ibi in oldContent)
            {
                if (!content.Contains(ibi))
                    return;
            }

            _core.SetContent(content);
        }

        /// <summary>
        /// Is Prototype has CustomFigure shape, return it        
        /// </summary>
        public CustomFigure IsCustomFigure()
        {
            return _core.IsCustomFigure();
        }

        /// <summary>
        /// Sets shape of the Prototype as CustomFigure
        /// </summary>
        public void SetCustomFigure(CustomFigure figure)
        {
            _core.SetCustomFigure(figure);
        }

        /// <summary>
        /// Hover rule of the Prototype
        /// </summary>
        public ItemRule GetHoverRule()
        {
            return _core.HoverRule;
        }
        public void SetHoverRule(ItemRule rule)
        {
            _core.HoverRule = rule;
        }

        private bool _isUserCursor = false;
        private EmbeddedCursor _cursor = EmbeddedCursor.Arrow;
        public EmbeddedCursor GetCursor()
        {
            return _cursor;
        }
        public void SetCursor(EmbeddedCursor cursor)
        {
            _cursor = cursor;
            if (cursor == EmbeddedCursor.Arrow)
                _isUserCursor = false;
            else
                _isUserCursor = true;
        }
        internal bool IsUserCursor()
        {
            return _isUserCursor;
        }
    }
}
