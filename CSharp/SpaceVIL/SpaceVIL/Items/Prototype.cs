using System;
using System.Collections.Generic;
using System.Drawing;
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
        public Prototype()
        {
            _core.SetItemName("VisualItem_" + count);
            count++;
            _core._main = this;
        }

        //common events
        internal EventCommonMethodState EventFocusGet;
        internal EventCommonMethodState EventFocusLost;
        public EventCommonMethodState EventResized;
        public EventCommonMethodState EventDestroyed;
        //mouse input
        public EventMouseMethodState EventMouseHover;
        public EventMouseMethodState EventMouseClick;
        public EventMouseMethodState EventMousePressed;
        public EventMouseMethodState EventMouseRelease;
        public EventMouseMethodState EventMouseDrag;
        public EventMouseMethodState EventMouseDrop;
        public EventMouseMethodState EventScrollUp;
        public EventMouseMethodState EventScrollDown;
        //keyboard input
        public EventKeyMethodState EventKeyPress;
        public EventKeyMethodState EventKeyRelease;
        public EventInputTextMethodState EventTextInput;

        public void SetHandler(WindowLayout handler)
        {
            _core.SetHandler(handler);
        }
        
        public WindowLayout GetHandler()
        {
            return _core.GetHandler();
        }
        
        public String GetToolTip()
        {
            return _core.GetToolTip();
        }

        public Prototype GetParent()
        {
            return _core.GetParent();
        }

        public void SetParent(Prototype parent)
        {
            _core.SetParent(parent);
        }

        public void SetToolTip(String text)
        {
            _core.SetToolTip(text);
        }

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
        public Indents GetMargin()
        {
            return _core.GetMargin();
        }
        public void SetMargin(Indents padding)
        {
            _core.SetMargin(padding);
        }
        public void SetMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _core.SetMargin(left, top, right, bottom);
        }
        public void SetBorder(Border border)
        {
            _core.SetBorder(border);
        }

        public void SetBorderFill(Color fill)
        {
            _core.SetBorderFill(fill);
        }
        public void SetBorderRadius(CornerRadius radius)
        {
            _core.SetBorderRadius(radius);
        }
        public void SetBorderRadius(int radius)
        {
            _core.SetBorderRadius(new CornerRadius(radius));
        }
        public void SetBorderThickness(int thickness)
        {
            _core.SetBorderThickness(thickness);
        }
        public CornerRadius GetBorderRadius()
        {
            return _core.GetBorderRadius();
        }
        public int GetBorderThickness()
        {
            return _core.GetBorderThickness();
        }
        public Color GetBorderFill()
        {
            return _core.GetBorderFill();
        }

        public virtual void InitElements()
        {
            _core.InitElements();
        }
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
        public void SetItemName(string name)
        {
            _core.SetItemName(name);
        }
        public string GetItemName()
        {
            return _core.GetItemName();
        }
        public void SetMinWidth(int width)
        {
            _core.SetMinWidth(width);
        }
        public virtual void SetWidth(int width)
        {
            _core.SetWidth(width);
        }
        public void SetMaxWidth(int width)
        {
            _core.SetMaxWidth(width);
        }
        public void SetMinHeight(int height)
        {
            _core.SetMinHeight(height);
        }
        public virtual void SetHeight(int height)
        {
            _core.SetHeight(height);
        }
        public void SetMaxHeight(int height)
        {
            _core.SetMaxHeight(height);
        }
        public int GetMinWidth()
        {
            return _core.GetMinWidth();
        }
        public virtual int GetWidth()
        {
            return _core.GetWidth();
        }
        public int GetMaxWidth()
        {
            return _core.GetMaxWidth();
        }
        public int GetMinHeight()
        {
            return _core.GetMinHeight();
        }
        public virtual int GetHeight()
        {
            return _core.GetHeight();
        }
        public int GetMaxHeight()
        {
            return _core.GetMaxHeight();
        }
        public void SetSize(int width, int height)
        {
            _core.SetSize(width, height);
        }
        public void SetMinSize(int width, int height)
        {
            _core.SetMinSize(width, height);
        }
        public void SetMaxSize(int width, int height)
        {
            _core.SetMaxSize(width, height);
        }
        public int[] GetSize()
        {
            return _core.GetSize();
        }
        public void SetAlignment(ItemAlignment alignment)
        {
            _core.SetAlignment(alignment);
        }
        public ItemAlignment GetAlignment()
        {
            return _core.GetAlignment();
        }
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
        public virtual void SetConfines()
        {
            _core.SetConfines();
        }
        public virtual void SetConfines(int x0, int x1, int y0, int y1)
        {
            _core._confines_x_0 = x0;
            _core._confines_x_1 = x1;
            _core._confines_y_0 = y0;
            _core._confines_y_1 = y1;
        }
        public virtual void SetStyle(Style style)
        {
            _core.SetStyle(style);
        }
        // public virtual void SetInnerStyle(String element, Style style)
        // {
        //     _core.SetInnerStyle(element, style);
        // }
        public virtual Style GetCoreStyle()
        {
            return _core.GetCoreStyle();
        }
        public bool IsShadowDrop()
        {
            return _core.IsShadowDrop();
        }
        public void SetShadowDrop(bool value)
        {
            _core.SetShadowDrop(value);
        }
        public void SetShadowRadius(int radius)
        {
            _core.SetShadowRadius(radius);
        }
        public int GetShadowRadius()
        {
            return _core.GetShadowRadius();
        }
        public Color GetShadowColor()
        {
            return _core.GetShadowColor();
        }
        public void SetShadowColor(Color color)
        {
            _core.SetShadowColor(color);
        }
        public Position GetShadowPos()
        {
            return _core.GetShadowPos();
        }
        public void SetShadow(int radius, int x, int y, Color color)
        {
            _core.SetShadow(radius, x, y, color);
        }
        public void SetFocus()
        {
            if (IsFocusable)
                GetHandler().SetFocusedItem(this);
        }
        public void AddItemState(ItemStateType type, ItemState state)
        {
            _core.AddItemState(type, state);
        }
        public void RemoveItemState(ItemStateType type)
        {
            _core.RemoveItemState(type);
        }
        public void RemoveAllItemStates()
        {
            _core.RemoveAllItemStates();
        }
        public ItemState GetState(ItemStateType type)
        {
            return _core.GetState(type);
        }
        protected virtual void UpdateState()
        {
            _core.UpdateState();
        }
        public virtual void InsertItem(IBaseItem item, Int32 index)
        {
            _core.InsertItem(item, index);
        }
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
        public void Update(GeometryEventType type, int value = 0)
        {
            _core.Update(type, value);
        }

        public virtual bool IsDrawable()
        {
            return _core.IsDrawable();
        }

        public void SetDrawable(bool value)
        {
            _core.SetDrawable(value);
        }

        public virtual bool IsVisible()
        {
            return _core.IsVisible();
        }

        public virtual void SetVisible(bool value)
        {
            _core.SetVisible(value);
        }
        public virtual bool IsPassEvents()
        {
            return _core.IsPassEvents();
        }

        public bool IsPassEvents(InputEventType e)
        {
            if (_core.GetPassEvents().HasFlag(e))
                return false;
            return true;
        }

        public void SetPassEvents(bool value)
        {
            _core.SetPassEvents(value);
        }

        public void SetPassEvents(bool value, InputEventType e)
        {
            _core.SetPassEvents(value, e);
        }

        public virtual bool IsDisabled()
        {
            return _core.IsDisabled();
        }

        public virtual void SetDisabled(bool value)
        {
            _core.SetDisabled(value);
        }

        public virtual bool IsMouseHover()
        {
            return _core.IsMouseHover();
        }

        public virtual void SetMouseHover(bool value)
        {
            _core.SetMouseHover(value);
        }

        public virtual bool IsMousePressed()
        {
            return _core.IsMousePressed();
        }

        public virtual void SetMousePressed(bool value)
        {
            _core.SetMousePressed(value);
        }
        public bool IsFocusable = true;
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
        public virtual List<IBaseItem> GetItems()
        {
            return _core.GetItems();
        }

        public virtual void RemoveItem(IBaseItem item)
        {
            _core.RemoveItem(item);
        }
        internal void AddEventListener(GeometryEventType type, IBaseItem listener)
        {
            _core.AddEventListener(type, listener);
        }
        internal void RemoveEventListener(GeometryEventType type, IBaseItem listener)
        {
            _core.RemoveEventListener(type, listener);
        }

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
        public void SetContent(List<IBaseItem> content)
        {
            _core.SetContent(content);
        }

        public CustomFigure IsCustom
        {
            get
            {
                return _core.IsCustom;
            }
            set
            {
                _core.IsCustom = value;
            }
        }
        public ItemRule HoverRule
        {
            get
            {
                return _core.HoverRule;
            }
            set
            {
                _core.HoverRule = value;
            }
        }
    }
}
