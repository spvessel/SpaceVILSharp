using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Reflection;
using System.Threading;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public delegate void EventCommonMethod();
    public delegate void EventCommonMethodState(IItem sender);
    public delegate void EventMouseMethodState(IItem sender, MouseArgs args);
    public delegate void EventKeyMethodState(IItem sender, KeyArgs args);
    public delegate void EventInputTextMethodState(IItem sender, TextInputArgs args);

    internal class VisualItem : BaseItem
    {
        private Object Locker = new Object();
        internal Prototype _main;

        private String _tooltip = String.Empty;
        internal String GetToolTip()
        {
            return _tooltip;
        }
        internal void SetToolTip(String text)
        {
            _tooltip = text;
        }
        //container
        private Spacing _spacing = new Spacing();
        internal Spacing GetSpacing()
        {
            return _spacing;
        }
        internal void SetSpacing(Spacing spacing)
        {
            _spacing = spacing;
            UpdateGeometry();

            if (GetParent() != null)
            {
                var hLayout = GetParent() as IHLayout;
                var vLayout = GetParent() as IVLayout;
                var grid = GetParent() as IGrid;

                if (hLayout == null && vLayout == null && grid == null)
                    UpdateBehavior();

                if (hLayout != null)
                    hLayout.UpdateLayout();
                if (vLayout != null)
                    vLayout.UpdateLayout();
                if (grid != null)
                    grid.UpdateLayout();
            }
        }
        internal void SetSpacing(int horizontal = 0, int vertical = 0)
        {
            _spacing.Horizontal = horizontal;
            _spacing.Vertical = vertical;
            UpdateGeometry();

            if (GetParent() != null)
            {
                var hLayout = GetParent() as IHLayout;
                var vLayout = GetParent() as IVLayout;
                var grid = GetParent() as IGrid;

                if (hLayout == null && vLayout == null && grid == null)
                    UpdateBehavior();

                if (hLayout != null)
                    hLayout.UpdateLayout();
                if (vLayout != null)
                    vLayout.UpdateLayout();
                if (grid != null)
                    grid.UpdateLayout();
            }
        }
        private Indents _padding = new Indents();
        internal Indents GetPadding()
        {
            return _padding;
        }
        internal void SetPadding(Indents padding)
        {
            _padding = padding;
            UpdateGeometry();

            if (GetParent() != null)
            {
                var hLayout = GetParent() as IHLayout;
                var vLayout = GetParent() as IVLayout;
                var grid = GetParent() as IGrid;

                if (hLayout == null && vLayout == null && grid == null)
                    UpdateBehavior();

                if (hLayout != null)
                    hLayout.UpdateLayout();
                if (vLayout != null)
                    vLayout.UpdateLayout();
                if (grid != null)
                    grid.UpdateLayout();
            }
        }
        internal void SetPadding(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _padding.Left = left;
            _padding.Top = top;
            _padding.Right = right;
            _padding.Bottom = bottom;
            UpdateGeometry();

            if (GetParent() != null)
            {
                var hLayout = GetParent() as IHLayout;
                var vLayout = GetParent() as IVLayout;
                var grid = GetParent() as IGrid;

                if (hLayout == null && vLayout == null && grid == null)
                    UpdateBehavior();

                if (hLayout != null)
                    hLayout.UpdateLayout();
                if (vLayout != null)
                    vLayout.UpdateLayout();
                if (grid != null)
                    grid.UpdateLayout();
            }
            // UpdateGeometry();
        }
        internal EventManager eventManager = null;
        private List<IBaseItem> _content = new List<IBaseItem>();
        internal virtual List<IBaseItem> GetItems()
        {
            Monitor.Enter(Locker);
            try
            {
                return _content;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);
                return null;
            }
            finally
            {
                Monitor.Exit(Locker);
            }
        }
        internal void SetContent(List<IBaseItem> content)
        {
            Monitor.Enter(Locker);
            try
            {
                _content = content;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);
            }
            finally
            {
                Monitor.Exit(Locker);
            }
        }

        private void CastAndUpdate(IBaseItem item)
        {
            Prototype prototype = item as Prototype;
            if (prototype != null)
                prototype.GetCore().UpdateGeometry();
            else
                (item as BaseItem).UpdateGeometry();
        }
        private void CastAndRemove(IBaseItem item)
        {
            Prototype prototype = item as Prototype;
            if (prototype != null)
            {
                prototype.GetCore().RemoveItemFromListeners();
                prototype.FreeEvents();
            }
            else
                (item as BaseItem).RemoveItemFromListeners();
        }
        internal virtual void AddItem(IBaseItem item)
        {
            Monitor.Enter(Locker);
            try
            {
                if (item.Equals(this))
                {
                    Console.WriteLine("Trying to add current item in himself.");
                    return;
                }
                item.SetHandler(GetHandler());
                AddChildren(item);
                _content.Add(item);
                ItemsLayoutBox.AddItem(GetHandler(), item, LayoutType.Static);

                //needs to force update all attributes
                CastAndUpdate(item);
                item.InitElements();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - AddItem: " + ((item == null) ? "item is null" : item.GetItemName()));
                Console.WriteLine(ex.StackTrace);
            }
            finally
            {
                Monitor.Exit(Locker);
            }
        }
        internal virtual void InsertItem(IBaseItem item, Int32 index)
        {
            Monitor.Enter(Locker);
            try
            {
                if (item.Equals(this))
                {
                    Console.WriteLine("Trying to add current item in himself.");
                    return;
                }
                item.SetHandler(GetHandler());
                AddChildren(item);
                if (index > _content.Count)
                    _content.Add(item);
                else
                    _content.Insert(index, item);
                ItemsLayoutBox.AddItem(GetHandler(), item, LayoutType.Static);
                //needs to force update all attributes
                CastAndUpdate(item);
                item.InitElements();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - InsertItem: " + ((item == null) ? "item is null" : item.GetItemName()));
                Console.WriteLine(ex.StackTrace);
            }
            finally
            {
                Monitor.Exit(Locker);
            }
        }

        internal void CascadeRemoving(IBaseItem item, LayoutType type)
        {
            Prototype container = item as Prototype;// предполагаю что элемент контейнер
            if (container != null)//и если это действительно контейнер
            {
                //то каждому вложенному элементу вызвать команду удалить своих вложенных элементов
                List<IBaseItem> tmp = new List<IBaseItem>(container.GetItems());
                while (tmp.Count > 0)
                {
                    IBaseItem child = tmp.ElementAt(0);
                    container.RemoveItem(child);
                    tmp.Remove(child);
                }
            }
        }

        internal virtual bool RemoveItem(IBaseItem item)
        {
            Monitor.Enter(Locker);
            try
            {
                //reset focus
                Prototype tmp = item as Prototype;
                if (tmp != null)
                {
                    if (tmp.IsFocused())
                        GetHandler().ResetItems();
                }

                LayoutType type;
                if (item is IFloating)
                {
                    CascadeRemoving(item, LayoutType.Floating);
                    type = LayoutType.Floating;
                }
                else
                {
                    CascadeRemoving(item, LayoutType.Static);
                    type = LayoutType.Static;
                }

                //removing
                bool contentRemove = _content.Remove(item);
                bool layoutBoxRemove = ItemsLayoutBox.RemoveItem(GetHandler(), item, type);
                CastAndRemove(item);
                item.Release();
                item.SetParent(null);
                return (contentRemove && layoutBoxRemove);
            }
            catch (Exception ex)
            {
                Console.WriteLine((item == null) ? "item is null" : item.GetItemName() + "\n" + ex.ToString());
                Console.WriteLine(ex.StackTrace);
                return false;
            }
            finally
            {
                Monitor.Exit(Locker);
            }
        }

        internal virtual void Clear()
        {
            Monitor.Enter(Locker);
            try
            {
                while (_content.Count > 0)
                    RemoveItem(_content.First());
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - Clear");
                Console.WriteLine(ex.StackTrace);
            }
            finally
            {
                Monitor.Exit(Locker);
            }
        }

        internal override void AddEventListener(GeometryEventType type, IBaseItem listener)
        {
            eventManager.Subscribe(type, listener);
        }
        internal override void RemoveEventListener(GeometryEventType type, IBaseItem listener)
        {
            eventManager.Unsubscribe(type, listener);
        }

        //item
        private Border _border = new Border();
        internal void SetBorder(Border border)
        {
            _border = border;
            GetState(ItemStateType.Base).Border = _border;
            UpdateState();
        }

        internal void SetBorderFill(Color fill)
        {
            _border.SetFill(fill);
            GetState(ItemStateType.Base).Border.SetFill(fill);
            UpdateState();
        }
        internal virtual void SetBorderFill(int r, int g, int b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            SetBorderFill(Color.FromArgb(255, r, g, b));
        }
        internal virtual void SetBorderFill(int r, int g, int b, int a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            SetBorderFill(Color.FromArgb(a, r, g, b));
        }
        internal virtual void SetBorderFill(float r, float g, float b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            SetBorderFill(Color.FromArgb(255, (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f)));
        }
        internal virtual void SetBorderFill(float r, float g, float b, float a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            SetBorderFill(Color.FromArgb((int)(a * 255.0f), (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f)));
        }

        internal void SetBorderRadius(CornerRadius radius)
        {
            _border.SetRadius(radius);
            GetState(ItemStateType.Base).Border.SetRadius(radius);
            UpdateState();
        }
        internal void SetBorderThickness(int thickness)
        {
            _border.SetThickness(thickness);
            GetState(ItemStateType.Base).Border.SetThickness(thickness);
            UpdateState();
        }
        internal CornerRadius GetBorderRadius()
        {
            return _border.GetRadius();
        }
        internal int GetBorderThickness()
        {
            return _border.GetThickness();
        }
        internal Color GetBorderFill()
        {
            return _border.GetFill();
        }


        internal void SetBorder(Color fill, CornerRadius radius, int thickness)
        {
            _border = new Border(fill, radius, thickness);
            GetState(ItemStateType.Base).Border = _border;
            UpdateState();
        }

        protected Dictionary<ItemStateType, ItemState> states = new Dictionary<ItemStateType, ItemState>();
        protected ItemStateType _state = ItemStateType.Base;
        internal ItemStateType GetCurrentState()
        {
            return _state;
        }
        internal void SetState(ItemStateType state)
        {
            _state = state;
            UpdateState();
        }

        internal VisualItem(
        int xpos = 0,
        int ypos = 0,
        int width = 0,
        int height = 0,
        string name = "VisualItem")
        {
            /*if (GetParent().GetHandler() != null)
                SetHandler(GetParent().GetHandler());*/
            //create state
            states.Add(ItemStateType.Base, new ItemState()
            {
                Value = true,
                Background = GetBackground(),
                // Text = null,
                // ImageUri = null
            });

            //common default prop
            eventManager = new EventManager();
            SetItemName(name);
        }

        //overrides
        public override void SetWidth(int width)
        {
            int value = width - GetWidth();
            if (value != 0)
            {
                base.SetWidth(width);

                if (GetParent() != null && GetWidthPolicy() == SizePolicy.Fixed)
                {
                    var layout = GetParent() as IHLayout;
                    var grid = GetParent() as IGrid;

                    if (layout == null && grid == null)
                        UpdateBehavior();

                    if (layout != null)
                        layout.UpdateLayout();
                    if (grid != null)
                        grid.UpdateLayout();
                }
                eventManager.NotifyListeners(GeometryEventType.ResizeWidth, value);
            }
        }
        public override void SetHeight(int height)
        {
            int value = height - GetHeight();
            if (value != 0)
            {
                base.SetHeight(height);

                if (GetParent() != null && GetHeightPolicy() == SizePolicy.Fixed)
                {
                    var layout = GetParent() as IVLayout;
                    var grid = GetParent() as IGrid;

                    if (layout == null && grid == null)
                        UpdateBehavior();

                    if (layout != null)
                        layout.UpdateLayout();
                    if (grid != null)
                        grid.UpdateLayout();
                }
                eventManager.NotifyListeners(GeometryEventType.ResizeHeight, value);
            }
        }
        internal void SetPosition(int _x, int _y)
        {
            this.SetX(_x);
            this.SetY(_y);
        }
        public override void SetX(int _x)
        {
            int value = _x - GetX();
            if (value != 0)
            {
                base.SetX(_x);

                if (GetParent() != null && GetWidthPolicy() == SizePolicy.Fixed)
                {
                    var layout = GetParent() as IHLayout;
                    var grid = GetParent() as IGrid;

                    if (layout != null)
                        layout.UpdateLayout();
                    if (grid != null)
                        if ((GetParent() as IFree) == null)
                            grid.UpdateLayout();
                }
                eventManager.NotifyListeners(GeometryEventType.Moved_X, value);
            }
        }
        public override void SetY(int _y)
        {
            int value = _y - GetY();
            if (value != 0)
            {
                base.SetY(_y);

                if (GetParent() != null && GetHeightPolicy() == SizePolicy.Fixed)
                {
                    var layout = GetParent() as IVLayout;
                    var grid = GetParent() as IGrid;

                    if (layout != null)
                        layout.UpdateLayout();

                    if (grid != null)
                        if ((GetParent() as IFree) == null)
                            grid.UpdateLayout();
                }
                eventManager.NotifyListeners(GeometryEventType.Moved_Y, value);
            }
        }

        // common properties
        private InputEventType _pass_events = 0x00;

        internal bool IsPassEvents()
        {
            if (_pass_events == 0x00)
                return true;
            return false;
        }

        internal InputEventType GetPassEvents()
        {
            return _pass_events;
        }

        internal void SetPassEvents(bool value)
        {
            if (!value)
            {
                List<InputEventType> list = Enum.GetValues(typeof(InputEventType)).Cast<InputEventType>().ToList();
                foreach (InputEventType e in list)
                {
                    _pass_events |= e;
                }
            }
            else
            {
                _pass_events = 0x00;
            }
        }

        internal void SetPassEvents(bool value, InputEventType e)
        {
            if (!value)
            {
                _pass_events |= e;
            }
            else
            {
                if (_pass_events.HasFlag(e))
                    _pass_events &= ~e;
            }
        }

        internal void SetPassEvents(bool value, params InputEventType[] e)
        {
            foreach (var item in e)
            {
                SetPassEvents(value, item);
            }
        }

        private bool _disabled;

        internal bool IsDisabled()
        {
            return _disabled;
        }

        internal void SetDisabled(bool value)
        {
            if (_disabled == value)
                return;
            _disabled = value;
            UpdateState();
        }

        private bool _hover;

        internal bool IsMouseHover()
        {
            return _hover;
        }

        internal void SetMouseHover(bool value)
        {
            if (_hover == value)
                return;
            _hover = value;
            UpdateState();
        }

        private bool _pressed;
        internal bool IsMousePressed()
        {
            return _pressed;
        }

        internal void SetMousePressed(bool value)
        {
            if (_pressed == value)
                return;
            _pressed = value;
            UpdateState();
        }

        private bool _focused;
        internal bool IsFocused()
        {
            return _focused;
        }

        internal void SetFocused(bool value)
        {
            if (_focused == value)
                return;
            _focused = value;
            UpdateState();
        }

        private bool _focusable = true;
        internal bool IsFocusable()
        {
            return _focusable;
        }
        internal void SetFocusable()
        {
            //foreach inner item focusable value set?
        }

        protected override void UpdateInnersDrawable(bool value)
        {
            foreach (var item in _content)
            {
                item.SetVisible(value);
            }
        }
        //common methods
        internal void AddItemState(ItemStateType type, ItemState state)
        {
            if (states.ContainsKey(type))
            {
                state.Value = true;
                states[type] = state;
            }
            else
            {
                states.Add(type, state);
            }
        }
        internal void RemoveItemState(ItemStateType type)
        {
            if (type == ItemStateType.Base)
                return;
            if (states.ContainsKey(type))
                states.Remove(type);
        }
        internal void RemoveAllItemStates()
        {
            var itemsToRemove = states.Where(f => f.Key != ItemStateType.Base).ToArray();
            foreach (var item in itemsToRemove)
                states.Remove(item.Key);
        }
        internal ItemState GetState(ItemStateType type)
        {
            if (states.ContainsKey(type))
                return states[type];
            return null;
        }
        public override void SetBackground(Color color)
        {
            GetState(ItemStateType.Base).Background = color;
            UpdateState();
        }
        public override void SetBackground(int r, int g, int b)
        {
            base.SetBackground(r, g, b);
            GetState(ItemStateType.Base).Background = GetBackground();
            UpdateState();
        }
        public override void SetBackground(int r, int g, int b, int a)
        {
            base.SetBackground(r, g, b, a);
            GetState(ItemStateType.Base).Background = GetBackground();
            UpdateState();
        }
        public override void SetBackground(float r, float g, float b)
        {
            base.SetBackground(r, g, b);
            GetState(ItemStateType.Base).Background = GetBackground();
            UpdateState();
        }
        public override void SetBackground(float r, float g, float b, float a)
        {
            base.SetBackground(r, g, b, a);
            GetState(ItemStateType.Base).Background = GetBackground();
            UpdateState();
        }

        internal void UpdateState()
        {
            ItemState s_base = GetState(ItemStateType.Base);
            ItemState current = GetState(_state);
            base.SetBackground(current.Background);
            _border = CloneBorder(current.Border);

            if (_border.GetRadius() == null)
                _border.SetRadius(s_base.Border.GetRadius());
            if (_border.GetThickness() < 0)
                _border.SetThickness(s_base.Border.GetThickness());
            if (_border.GetFill().A == 0)
                _border.SetFill(s_base.Border.GetFill());

            if (current.Shape != null)
                IsCustom = current.Shape;

            ItemState s_disabled = GetState(ItemStateType.Disabled);
            if (IsDisabled())
            {
                if (s_disabled != null)
                    UpdateVisualProperties(s_disabled, s_base);
                return;
            }
            ItemState s_focused = GetState(ItemStateType.Focused);
            if (IsFocused() && s_focused != null)
            {
                UpdateVisualProperties(s_focused, s_base);
                s_base = s_focused;
            }
            ItemState s_hover = GetState(ItemStateType.Hovered);
            if (IsMouseHover() && s_hover != null)
            {
                UpdateVisualProperties(s_hover, s_base);
                s_base = s_hover;
            }
            ItemState s_pressed = GetState(ItemStateType.Pressed);
            if (IsMousePressed() && s_pressed != null)
            {
                UpdateVisualProperties(s_pressed, s_base);
                s_base = s_pressed;
            }
        }

        internal void UpdateVisualProperties(ItemState state, ItemState prev_state)
        {
            ItemState current = GetState(_state);
            base.SetBackground(GraphicsMathService.MixColors(current.Background, state.Background));
            _border = CloneBorder(state.Border);

            if (_border.GetRadius() == null)
                _border.SetRadius(prev_state.Border.GetRadius());
            if (_border.GetRadius() == null)
                _border.SetRadius(GetState(ItemStateType.Base).Border.GetRadius());

            if (_border.GetThickness() < 0)
                _border.SetThickness(prev_state.Border.GetThickness());
            if (_border.GetThickness() < 0)
                _border.SetThickness(GetState(ItemStateType.Base).Border.GetThickness());

            if (_border.GetFill().A == 0)
                _border.SetFill(prev_state.Border.GetFill());
            if (_border.GetFill().A == 0)
                _border.SetFill(GetState(ItemStateType.Base).Border.GetFill());

            if (state.Shape != null)
                IsCustom = state.Shape;
        }

        private Border CloneBorder(Border border)
        {
            Border clone = new Border();
            clone.SetFill(border.GetFill());
            clone.SetRadius(border.GetRadius());
            clone.SetThickness(border.GetThickness());
            return clone;
        }
        internal virtual bool GetHoverVerification(float xpos, float ypos)
        {
            switch (HoverRule)
            {
                case ItemRule.Lazy:
                    return LazyHoverVerification(xpos, ypos);
                case ItemRule.Strict:
                    return StrictHoverVerification(xpos, ypos);
                default:
                    return false;
            }
        }
        private bool StrictHoverVerification(float xpos, float ypos)
        {
            List<float[]> tmp = UpdateShape();
            if (tmp == null)
                return false;

            float Ax, Ay, Bx, By, Cx, Cy, Px, Py, m, l;
            bool result = false;

            for (int point = 0; point < tmp.Count; point += 3)
            {
                Px = xpos;
                Py = ypos;
                Ax = tmp[point][0];
                Ay = tmp[point][1];
                Bx = tmp[point + 1][0];
                By = tmp[point + 1][1];
                Cx = tmp[point + 2][0];
                Cy = tmp[point + 2][1];


                Bx = Bx - Ax; By = By - Ay;
                Cx = Cx - Ax; Cy = Cy - Ay;
                Px = Px - Ax; Py = Py - Ay;
                Ax = 0; Ay = 0;

                m = (Px * By - Bx * Py) / (Cx * By - Bx * Cy);
                if (m >= 0)
                {
                    l = (Px - m * Cx) / Bx;
                    if (l >= 0 && (m + l) <= 1)
                    {
                        result = true;
                        return result;
                    }
                }
            }

            return result;
        }
        private bool LazyHoverVerification(float xpos, float ypos)
        {
            bool result = false;

            float minx = GetX();
            float maxx = GetX() + GetWidth();
            float miny = GetY();
            float maxy = GetY() + GetHeight();

            if (_confines_x_0 > minx)
                minx = _confines_x_0;

            if (_confines_x_1 < maxx)
                maxx = _confines_x_1;

            if (_confines_y_0 > miny)
                miny = _confines_y_0;

            if (_confines_y_1 < maxy)
                maxy = _confines_y_1;

            if (xpos >= minx
                && xpos <= maxx
                && ypos >= miny
                && ypos <= maxy)
            {
                result = true;
            }
            else
            {
            }
            return result;
        }

        CustomFigure IsCustom = null;
        internal CustomFigure IsCustomFigure()
        {
            return IsCustom;
        }
        internal void SetCustomFigure(CustomFigure figure)
        {
            IsCustom = figure;
        }

        public override List<float[]> MakeShape()
        {
            if (IsCustom != null)
            {
                SetTriangles(IsCustom.GetFigure());

                if (IsCustom.IsFixed())
                    return GraphicsMathService.ToGL(IsCustom.UpdatePosition(GetX(), GetY()), GetHandler());
                else
                    return GraphicsMathService.ToGL(UpdateShape(), GetHandler());
            }

            SetTriangles(GraphicsMathService.GetRoundSquare(GetBorderRadius(), GetWidth(), GetHeight(), GetX(), GetY()));
            return GraphicsMathService.ToGL(this as IBaseItem, GetHandler());
        }

        //style
        // internal bool _is_style_Set = false;
        // internal virtual void SetInnerStyle(String element, Style style)
        // {

        // }
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;

            // _is_style_Set = true;
            SetPosition(style.X, style.Y);
            SetSize(style.Width, style.Height);
            SetSizePolicy(style.WidthPolicy, style.HeightPolicy);
            SetPadding(style.Padding);
            SetMargin(style.Margin);
            SetAlignment(style.Alignment);

            SetSpacing(style.Spacing);

            SetMinSize(style.MinWidth, style.MinHeight);
            SetMaxSize(style.MaxWidth, style.MaxHeight);

            SetBackground(style.Background);
            SetBorderRadius(style.BorderRadius);
            SetBorderThickness(style.BorderThickness);
            SetBorderFill(style.BorderFill);
            SetVisible(style.IsVisible);
            RemoveAllItemStates();

            ItemState core_state = new ItemState(style.Background);
            core_state.Border.SetRadius(style.BorderRadius);
            core_state.Border.SetThickness(style.BorderThickness);
            core_state.Border.SetFill(style.BorderFill);

            foreach (var state in style.GetAllStates())
            {
                AddItemState(state.Key, state.Value);
            }
            if (style.Shape != null)
            {
                IsCustom = new CustomFigure(style.IsFixedShape, style.Shape);
                core_state.Shape = IsCustom;
                // GetState(ItemStateType.Base).Shape = IsCustom;
            }
            AddItemState(ItemStateType.Base, core_state);
        }
        public override Style GetCoreStyle()
        {
            Style style = new Style();
            style.SetSize(GetWidth(), GetHeight());
            style.SetSizePolicy(GetWidthPolicy(), GetHeightPolicy());
            style.Background = GetBackground();
            style.MinWidth = GetMinWidth();
            style.MinHeight = GetMinHeight();
            style.MaxWidth = GetMaxWidth();
            style.MaxHeight = GetMaxHeight();
            style.X = GetX();
            style.Y = GetY();
            style.Padding = new Indents(GetPadding().Left, GetPadding().Top, GetPadding().Right, GetPadding().Bottom);
            style.Margin = new Indents(GetMargin().Left, GetMargin().Top, GetMargin().Right, GetMargin().Bottom);
            style.Spacing = new Spacing(GetSpacing().Horizontal, GetSpacing().Vertical);
            style.Alignment = GetAlignment();
            style.BorderFill = GetBorderFill();
            style.BorderRadius = GetBorderRadius();
            style.BorderThickness = GetBorderThickness();
            style.IsVisible = IsVisible();
            if (IsCustom != null)
            {
                style.Shape = IsCustom.GetFigure();
                style.IsFixedShape = IsCustom.IsFixed();
            }
            foreach (var state in states)
            {
                style.AddItemState(state.Key, state.Value);
            }

            return style;
        }

        public override void InitElements() { }
    }
}
