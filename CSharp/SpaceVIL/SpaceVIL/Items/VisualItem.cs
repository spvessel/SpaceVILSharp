using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;

namespace SpaceVIL
{
    public delegate void CallBackMethodState(IItem sender);

    abstract public class VisualItem : BaseItem
    {
        //container
        private Spacing _spacing = new Spacing();
        public Spacing GetSpacing()
        {
            return _spacing;
        }
        public void SetSpacing(Spacing spacing)
        {
            _spacing = spacing;
        }
        public void SetSpacing(int horizontal = 0, int vertical = 0)
        {
            _spacing.Horizontal = horizontal;
            _spacing.Vertical = vertical;
        }
        private Padding _padding = new Padding();
        public Padding GetPadding()
        {
            return _padding;
        }
        public void SetPadding(Padding padding)
        {
            _padding = padding;
        }
        public void SetPadding(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _padding.Left = left;
            _padding.Top = top;
            _padding.Right = right;
            _padding.Bottom = bottom;
        }
        public EventManager eventManager = null;
        private List<BaseItem> _content = new List<BaseItem>();
        public List<BaseItem> GetItems()
        {
            return _content;
        }
        public void AddItems(params BaseItem[] items)
        {
            foreach (var item in items)
            {
                this.AddItem(item);
            }
        }
        public virtual void AddItem(BaseItem item)
        {
            if (item.Equals(this))
            {
                Console.WriteLine("Trying to add current item in himself.");
                return;
            }
            item.SetHandler(GetHandler());

            AddChildren(item);
            _content.Add(item);

            try
            {
                ItemsLayoutBox.AddItem(GetHandler(), item);
            }
            catch (Exception ex)
            {
                Console.WriteLine(item.GetItemName());
                throw ex;
            }

            //needs to force update all attributes
            item.UpdateGeometry();
            item.InitElements();
        }
        public void RemoveItem(BaseItem item)
        {
            _content.Remove(item);
            item.RemoveItemFromListeners();
        }
        protected override void AddEventListener(int eventType, BaseItem listener)
        {
            eventManager.Subscribe(eventType, listener);
        }
        protected override void RemoveEventListener(int eventType, BaseItem listener)
        {
            eventManager.Unsubscribe(eventType, listener);
        }

        //item
        public Border Border = new Border();
        protected Dictionary<ItemStateType, ItemState> states = new Dictionary<ItemStateType, ItemState>();
        protected ItemStateType _state = ItemStateType.Base;
        protected void SetState(ItemStateType state)
        {
            _state = state;
            UpdateState();
        }
        public VisualItem(
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
                Text = null,
                ImageUri = null
            });

            //common default prop
            eventManager = new EventManager();
            SetItemName(name);

            //bind events
            EventMouseHover += EmptyEvent;
            EventMousePressed += EmptyEvent;
            EventFocusGet += EmptyEvent;
            EventFocusLost += EmptyEvent;
            EventMouseDrop += EmptyEvent;
            EventResized += EmptyEvent;
            EventDestroyed += EmptyEvent;
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
                eventManager.NotifyListeners(EventManager.ResizeWidth, value);
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
                eventManager.NotifyListeners(EventManager.ResizeHeight, value);
            }
        }
        public override void SetX(int _x)
        {
            int value = _x - GetX();
            if (value != 0)
            {
                base.SetX(_x);
                eventManager.NotifyListeners(EventManager.Moved_X, value);
            }
        }
        public override void SetY(int _y)
        {
            int value = _y - GetY();
            if (value != 0)
            {
                base.SetY(_y);
                eventManager.NotifyListeners(EventManager.Moved_Y, value);
            }
        }

        //common events
        public CallBackMethodState EventMouseHover;
        public CallBackMethodState EventMouseClick;
        public CallBackMethodState EventMousePressed;
        public CallBackMethodState EventMouseRelease;
        public CallBackMethodState EventMouseDrag;
        public CallBackMethodState EventMouseDrop;
        public CallBackMethodState EventFocusGet;
        public CallBackMethodState EventFocusLost;
        public CallBackMethodState EventResized;
        public CallBackMethodState EventDestroyed;

        //common properties
        private bool _disabled;
        public bool Disabled
        {
            get { return _disabled; }
            set { _disabled = value; }
        }
        private bool _hover;
        public virtual bool IsMouseHover
        {
            get { return _hover; }
            set
            {
                if (_hover == value)
                    return;
                _hover = value;
                UpdateState();
            }
        }
        private bool _pressed;
        public virtual bool IsMousePressed
        {
            get { return _pressed; }
            set { _pressed = value; }
        }
        private bool _focused;
        public virtual bool IsFocused
        {
            get { return _focused; }
            set { _focused = value; }
        }

        //common methods
        public void AddItemState(bool value, ItemStateType type, ItemState state)
        {
            if (states.ContainsKey(type))
            {
                state.Value = value;
                states[type] = state;
            }
            else
            {
                states.Add(type, state);
            }
        }
        public void RemoveItemState(ItemStateType type)
        {
            if (type == ItemStateType.Base)
                return;
            if (states.ContainsKey(type))
                states.Remove(type);
        }
        public ItemState GetState(ItemStateType type)
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

        protected virtual void UpdateState()
        {
            base.SetBackground(GetState(_state).Background);
            if (IsMouseHover && states.ContainsKey(ItemStateType.Hovered))
                base.SetBackground(GraphicsMathService.MixColors(GetState(_state).Background, GetState(ItemStateType.Hovered).Background));
        }

        protected internal Pointer _mouse_ptr = new Pointer();
        protected internal virtual bool GetHoverVerification(float xpos, float ypos)
        {
            //parent border
            float left_border = (GetParent().GetX() / (float)GetHandler().GetWidth()) * 2.0f - 1.0f;
            float right_border = ((GetParent().GetWidth() + GetParent().GetX()) / (float)GetHandler().GetWidth()) * 2.0f - 1.0f;
            float top_border = (((GetParent().GetHeight() + GetParent().GetY()) / (float)GetHandler().GetHeight()) * 2.0f - 1.0f) * (-1.0f);
            float bottom_border = ((GetParent().GetY() / (float)GetHandler().GetHeight()) * 2.0f - 1.0f) * (-1.0f);

            //item border
            float minx = (GetX() / (float)GetHandler().GetWidth()) * 2.0f - 1.0f;
            minx = (minx < left_border) ? left_border : minx;
            float maxx = ((GetWidth() + GetX()) / (float)GetHandler().GetWidth()) * 2.0f - 1.0f;
            maxx = (maxx > right_border) ? right_border : maxx;
            float miny = (((GetHeight() + GetY()) / (float)GetHandler().GetHeight()) * 2.0f - 1.0f) * (-1.0f);
            miny = (miny < top_border) ? top_border : miny;
            float maxy = ((GetY() / (float)GetHandler().GetHeight()) * 2.0f - 1.0f) * (-1.0f);
            maxy = (maxy > bottom_border) ? bottom_border : maxy;

            //current mouse cursor
            float x_gl = (xpos / (float)GetHandler().GetWidth()) * 2.0f - 1.0f;
            float y_gl = (((float)ypos / (float)GetHandler().GetHeight()) * 2.0f - 1.0f) * (-1.0f);

            if (x_gl >= minx
                && x_gl <= maxx
                && y_gl >= miny
                && y_gl <= maxy)
            {
                IsMouseHover = true;
                _mouse_ptr.SetPosition(xpos, ypos);
            }
            else
            {
                IsMouseHover = false;
                _mouse_ptr.Clear();
            }
            return IsMouseHover;
        }
        private bool _is_custom = false;
        public virtual bool IsCustom { get => _is_custom; set => _is_custom = value; }

        public override List<float[]> MakeShape()
        {
            if (IsCustom)
            {
                if (GetTriangles() == null || GetTriangles().Count == 0)
                    return null;

                return GraphicsMathService.ToGL(UpdateShape(), GetHandler());
            }

            SetTriangles(GraphicsMathService.GetRoundSquare(GetWidth(), GetHeight(), Border.Radius, GetX(), GetY()));
            return GraphicsMathService.ToGL(this as BaseItem, GetHandler());
        }
        public override void InvokePoolEvents()
        {
            //do nothing
        }
    }
}
