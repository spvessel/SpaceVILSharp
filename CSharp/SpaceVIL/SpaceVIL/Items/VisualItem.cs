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
            BaseItemStatics.UpdateAllLayout(this);
        }
        internal void SetSpacing(int horizontal = 0, int vertical = 0)
        {
            SetSpacing(new Spacing(horizontal, vertical));
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
            BaseItemStatics.UpdateAllLayout(this);
        }
        internal void SetPadding(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            SetPadding(new Indents(left, top, right, bottom));
        }
        internal EventManager eventManager = null;
        // private List<IBaseItem> _content = new List<IBaseItem>();
        private LinkedHashSet<IBaseItem> _content = new LinkedHashSet<IBaseItem>();
        internal virtual List<IBaseItem> GetItems()
        {
            Monitor.Enter(Locker);
            try
            {
                return new List<IBaseItem>(_content);
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
                // _content = content;
                _content = new LinkedHashSet<IBaseItem>(content);
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

        public override void RemoveItemFromListeners()
        {
            Prototype parent = GetParent();
            parent.RemoveEventListener(GeometryEventType.ResizeWidth, this._main);
            parent.RemoveEventListener(GeometryEventType.ResizeHeight, this._main);
            parent.RemoveEventListener(GeometryEventType.MovedX, this._main);
            parent.RemoveEventListener(GeometryEventType.MovedY, this._main);
        }

        internal virtual void AddItem(IBaseItem item)
        {
            Monitor.Enter(Locker);
            try
            {
                if (item == null)
                {
                    Console.WriteLine("Trying to add null item");
                    return;
                }
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
                BaseItemStatics.CastToUpdateGeometry(item);
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
                if (item == null)
                {
                    Console.WriteLine("Trying to insert null item");
                    return;
                }
                if (index < 0)
                {
                    Console.WriteLine("Invalid index");
                    return;
                }
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
                {
                    List<IBaseItem> list = new List<IBaseItem>(_content);
                    list.Insert(index, item);
                    _content = new LinkedHashSet<IBaseItem>(list);
                }
                ItemsLayoutBox.AddItem(GetHandler(), item, LayoutType.Static);
                //needs to force update all attributes
                BaseItemStatics.CastToUpdateGeometry(item);
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
            Prototype container = item as Prototype;
            if (container != null)
            {
                List<IBaseItem> tmp = container.GetItems();
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
                if (!_content.Contains(item))
                    return false;
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
                CastAndRemove(item);
                bool contentRemove = _content.Remove(item);
                bool layoutBoxRemove = ItemsLayoutBox.RemoveItem(GetHandler(), item, type);
                item.SetParent(null);
                item.Release();
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
        internal Border _border = new Border();
        internal void SetBorderDirect(Border border)
        {
            _border = border;
        }

        internal Border GetBorderDirect()
        {
            return _border;
        }

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
            SetBorderFill(GraphicsMathService.ColorTransform(r, g, b));
        }
        internal virtual void SetBorderFill(int r, int g, int b, int a)
        {
            SetBorderFill(GraphicsMathService.ColorTransform(r, g, b, a));
        }
        internal virtual void SetBorderFill(float r, float g, float b)
        {
            SetBorderFill(GraphicsMathService.ColorTransform(r, g, b));
        }
        internal virtual void SetBorderFill(float r, float g, float b, float a)
        {
            SetBorderFill(GraphicsMathService.ColorTransform(r, g, b, a));
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

        internal Dictionary<ItemStateType, ItemState> states = new Dictionary<ItemStateType, ItemState>();
        internal ItemStateType _state = ItemStateType.Base;
        internal ItemStateType GetCurrentStateType()
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
            states.Add(ItemStateType.Base, new ItemState()
            {
                Value = true,
                Background = GetBackground(),
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
                BaseItemStatics.UpdateHLayout(this);
                eventManager.NotifyListeners(GeometryEventType.ResizeWidth, value);
            }
        }
        public override void SetHeight(int height)
        {
            int value = height - GetHeight();
            if (value != 0)
            {
                base.SetHeight(height);
                BaseItemStatics.UpdateVLayout(this);
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
                eventManager.NotifyListeners(GeometryEventType.MovedX, value);
            }
        }
        public override void SetY(int _y)
        {
            int value = _y - GetY();
            if (value != 0)
            {
                base.SetY(_y);
                eventManager.NotifyListeners(GeometryEventType.MovedY, value);
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

        internal void SetBackgroundDirect(Color color)
        {
            base.SetBackground(color);
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
            VisualItemStatics.UpdateState(this);
        }

        internal virtual bool GetHoverVerification(float xpos, float ypos)
        {
            return VisualItemStatics.GetHoverVerification(this, xpos, ypos);
        }

        CustomFigure IsCustom = null;
        internal CustomFigure IsCustomFigure()
        {
            return IsCustom;
        }
        internal void SetCustomFigure(CustomFigure figure)
        {
            IsCustom = figure;
            SetRemakeRequest(true);
        }

        public override void MakeShape()
        {
            if (IsCustomFigure() != null)
            {
                SetTriangles(IsCustomFigure().GetFigure());
                if (GetState(ItemStateType.Base).Shape == null)
                    GetState(ItemStateType.Base).Shape = IsCustomFigure();

                if (!IsCustomFigure().IsFixed())
                    SetTriangles(UpdateShape());
            }
            else
            {
                SetTriangles(GraphicsMathService.GetRoundSquare(GetBorderRadius(), GetWidth(), GetHeight(), 0, 0));
            }
        }

        public override void SetStyle(Style style)
        {
            VisualItemStatics.SetStyle(this, style);
        }

        public override Style GetCoreStyle()
        {
            return VisualItemStatics.ExtractCoreStyle(this);
        }

        public override void InitElements() { }
    }
}
