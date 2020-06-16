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
    /// <summary>
    /// Common type of events. No arguments.
    /// </summary>
    public delegate void EventCommonMethod();
    /// <summary>
    /// Common type of events with one argument: sender.
    /// </summary>
    /// <param name="sender">Sender as SpaceVIL.Core.IItem.</param>
    public delegate void EventCommonMethodState(IItem sender);
    /// <summary>
    /// Mouse type of event with two arguments: sender, args.
    /// </summary>
    /// <param name="sender">Sender as SpaceVIL.Core.IItem.</param>
    /// <param name="args">Mouse arguments as SpaceVIL.Core.MouseArgs.</param>
    public delegate void EventMouseMethodState(IItem sender, MouseArgs args);
    /// <summary>
    /// Key type of event with two arguments: sender, args.
    /// </summary>
    /// <param name="sender">Sender as SpaceVIL.Core.IItem.</param>
    /// <param name="args">Key arguments as SpaceVIL.Core.KeyArgs.</param>
    public delegate void EventKeyMethodState(IItem sender, KeyArgs args);
    /// <summary>
    /// Text input type of event with two arguments: sender, args.
    /// </summary>
    /// <param name="sender">Sender as SpaceVIL.Core.IItem.</param>
    /// <param name="args">Text input arguments as SpaceVIL.Core.TextInputArgs.</param>
    public delegate void EventInputTextMethodState(IItem sender, TextInputArgs args);

    /// <summary>
    /// Drop type of event with two arguments: sender, args.
    /// </summary>
    /// <param name="sender">Sender as SpaceVIL.Core.IItem.</param>
    /// <param name="args">Drop arguments as SpaceVIL.Core.DropArgs.</param>
    public delegate void EventWindowDropMethod(IItem sender, DropArgs args);

    internal class VisualItem : BaseItem
    {
        private Object _locker = new Object();
        internal Prototype prototype;

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
            Monitor.Enter(_locker);
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
                Monitor.Exit(_locker);
            }
        }
        internal void SetContent(List<IBaseItem> content)
        {
            Monitor.Enter(_locker);
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
                Monitor.Exit(_locker);
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

        internal override void RemoveItemFromListeners()
        {
            Prototype parent = GetParent();
            parent.RemoveEventListener(GeometryEventType.ResizeWidth, this.prototype);
            parent.RemoveEventListener(GeometryEventType.ResizeHeight, this.prototype);
            parent.RemoveEventListener(GeometryEventType.MovedX, this.prototype);
            parent.RemoveEventListener(GeometryEventType.MovedY, this.prototype);
        }

        internal virtual void AddItem(IBaseItem item)
        {
            Monitor.Enter(_locker);
            try
            {
                if (item == null)
                {
                    Console.WriteLine("SpaceVILWarning: Trying to add null item!");
                    return;
                }
                if (item.Equals(this))
                {
                    Console.WriteLine("SpaceVILWarning: Trying to add current item in himself.");
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
                Monitor.Exit(_locker);
            }
        }
        internal virtual void InsertItem(IBaseItem item, Int32 index)
        {
            Monitor.Enter(_locker);
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
                Monitor.Exit(_locker);
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
            Monitor.Enter(_locker);
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
                Monitor.Exit(_locker);
            }
        }

        internal virtual void Clear()
        {
            Monitor.Enter(_locker);
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
                Monitor.Exit(_locker);
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
        internal Border border = new Border();
        internal void SetBorderDirect(Border border)
        {
            this.border = border;
        }

        internal Border GetBorderDirect()
        {
            return border;
        }

        internal void SetBorder(Border border)
        {
            this.border = border;
            GetState(ItemStateType.Base).Border = this.border;
            UpdateState();
        }

        internal void SetBorderFill(Color fill)
        {
            border.SetFill(fill);
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
            border.SetRadius(radius);
            GetState(ItemStateType.Base).Border.SetRadius(radius);
            UpdateState();
        }
        internal void SetBorderThickness(int thickness)
        {
            border.SetThickness(thickness);
            GetState(ItemStateType.Base).Border.SetThickness(thickness);
            UpdateState();
        }
        internal CornerRadius GetBorderRadius()
        {
            return border.GetRadius();
        }
        internal int GetBorderThickness()
        {
            return border.GetThickness();
        }
        internal Color GetBorderFill()
        {
            return border.GetFill();
        }


        internal void SetBorder(Color fill, CornerRadius radius, int thickness)
        {
            border = new Border(fill, radius, thickness);
            GetState(ItemStateType.Base).Border = border;
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
        internal void SetPosition(int x, int y)
        {
            this.SetX(x);
            this.SetY(y);
        }
        public override void SetX(int x)
        {
            int value = x - GetX();
            if (value != 0)
            {
                base.SetX(x);
                eventManager.NotifyListeners(GeometryEventType.MovedX, value);
            }
        }
        public override void SetY(int y)
        {
            int value = y - GetY();
            if (value != 0)
            {
                base.SetY(y);
                eventManager.NotifyListeners(GeometryEventType.MovedY, value);
            }
        }

        // common properties
        private InputEventType _blockedEvents = 0x00;

        internal bool IsPassEvents()
        {
            if (_blockedEvents == 0x00)
                return true;
            return false;
        }

        internal bool IsPassEvents(InputEventType e)
        {
            if (_blockedEvents.HasFlag(e))
                return false;
            return true;
        }

        internal List<InputEventType> GetBlockedEvents()
        {
            List<InputEventType> list = new List<InputEventType>();
            foreach (InputEventType type in Enum.GetValues(typeof(InputEventType)))
            {
                if (_blockedEvents.HasFlag(type))
                {
                    list.Add(type);
                }
            }

            return list;
        }

        internal List<InputEventType> GetPassEvents()
        {
            List<InputEventType> list = new List<InputEventType>();
            foreach (InputEventType type in Enum.GetValues(typeof(InputEventType)))
            {
                if (!_blockedEvents.HasFlag(type))
                {
                    list.Add(type);
                }
            }

            return list;
        }

        internal void SetPassEvents(bool value)
        {
            if (!value)
            {
                foreach (InputEventType e in Enum.GetValues(typeof(InputEventType)))
                {
                    _blockedEvents |= e;
                }
            }
            else
            {
                _blockedEvents = 0x00;
            }
        }

        internal void SetPassEvents(bool value, InputEventType e)
        {
            if (!value)
            {
                _blockedEvents |= e;
            }
            else
            {
                if (_blockedEvents.HasFlag(e))
                    _blockedEvents &= ~e;
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

        Figure IsCustom = null;
        internal Figure IsCustomFigure()
        {
            return IsCustom;
        }
        internal void SetCustomFigure(Figure figure)
        {
            IsCustom = figure;
            ItemsRefreshManager.SetRefreshShape(this.prototype);
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
