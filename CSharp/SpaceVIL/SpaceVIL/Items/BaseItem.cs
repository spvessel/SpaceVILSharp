using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;

namespace SpaceVIL
{
    public abstract class BaseItem : IGeometry, IReaction, IBehavior, IAncestor, IEventUpdate, IItem
    {
        private WindowLayout _handler;
        public void SetHandler(WindowLayout handler)
        {
            _handler = handler;
        }
        public WindowLayout GetHandler()
        {
            return _handler;
        }
        abstract public void InvokePoolEvents();
        public void EmptyEvent(IItem sender) { }

        //parent
        private VisualItem _parent = null;
        public VisualItem GetParent()
        {
            return _parent;
        }
        internal void SetParent(VisualItem parent)
        {
            _parent = parent;
        }
        protected void AddChildren(BaseItem item)
        {
            if (item.GetParent() != null)
                item.GetParent().RemoveItem(item);

            item.SetParent(this as VisualItem);

            //refactor events verification
            var v_stack = item.GetParent() as IVLayout;
            if (v_stack != null)
            {
                AddEventListener(GeometryEventType.ResizeWidth, item);
                AddEventListener(GeometryEventType.Moved_X, item);
                item.UpdateBehavior();
                return;
            }
            var h_stack = item.GetParent() as IHLayout;
            if (h_stack != null)
            {
                AddEventListener(GeometryEventType.ResizeHeight, item);
                AddEventListener(GeometryEventType.Moved_Y, item);
                item.UpdateBehavior();
                return;
            }
            var grid_stack = item.GetParent() as IGrid;
            if (grid_stack != null)
            {
                return;
            }
            var flow_stack = item.GetParent() as IFlow;
            if (flow_stack != null)
            {
                return;
            }

            AddEventListener(GeometryEventType.ResizeWidth, item);
            AddEventListener(GeometryEventType.ResizeHeight, item);
            AddEventListener(GeometryEventType.Moved_X, item);
            AddEventListener(GeometryEventType.Moved_Y, item);
            item.UpdateBehavior();
        }
        protected virtual void AddEventListener(GeometryEventType type, BaseItem listener) { }
        protected virtual void RemoveEventListener(GeometryEventType type, BaseItem listener) { }
        public void RemoveItemFromListeners()
        {
            GetParent().RemoveEventListener(GeometryEventType.ResizeWidth, this);
            GetParent().RemoveEventListener(GeometryEventType.ResizeHeight, this);
            GetParent().RemoveEventListener(GeometryEventType.Moved_X, this);
            GetParent().RemoveEventListener(GeometryEventType.Moved_Y, this);
        }

        public virtual void InitElements()
        {
            //do nothing
        }
        private Item _item = new Item();
        private Margin _margin = new Margin();
        public Margin GetMargin()
        {
            return _margin;
        }
        public void SetMargin(Margin margin)
        {
            _margin = margin;
        }
        public void SetMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _margin.Left = left;
            _margin.Top = top;
            _margin.Right = right;
            _margin.Bottom = bottom;
        }
        public List<float[]> GetTriangles()
        {
            return _item.GetTriangles();
        }
        public virtual void SetTriangles(List<float[]> triangles)
        {
            _item.SetTriangles(triangles);
        }
        public virtual List<float[]> MakeShape()
        {
            return _item.MakeShape();
        }
        internal List<float[]> UpdateShape()
        {
            //clone triangles
            List<float[]> result = new List<float[]>();
            for (int i = 0; i < GetTriangles().Count; i++)
            {
                result.Add(new float[] { GetTriangles().ElementAt(i)[0], GetTriangles().ElementAt(i)[1], GetTriangles().ElementAt(i)[2] });
            }
            //max and min
            float maxX = result.Select(_ => _[0]).Max();
            float maxY = result.Select(_ => _[1]).Max();
            float minX = result.Select(_ => _[0]).Min();
            float minY = result.Select(_ => _[1]).Min();

            //to the left top corner
            foreach (var item in result)
            {
                item[0] = (item[0] - minX) * GetWidth() / (maxX - minX) + GetX();
                item[1] = (item[1] - minY) * GetHeight() / (maxY - minY) + GetY();
            }

            return result;
        }
        public virtual void SetBackground(Color color)
        {
            _item.SetBackground(color);
        }
        public virtual void SetBackground(int r, int g, int b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            _item.SetBackground(Color.FromArgb(255, r, g, b));
        }
        public virtual void SetBackground(int r, int g, int b, int a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            _item.SetBackground(Color.FromArgb(a, r, g, b));
        }
        public virtual void SetBackground(float r, float g, float b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            _item.SetBackground(Color.FromArgb(255, (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f)));
        }
        public virtual void SetBackground(float r, float g, float b, float a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            _item.SetBackground(Color.FromArgb((int)(a * 255.0f), (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f)));
        }
        public virtual Color GetBackground()
        {
            return _item.GetBackground();
        }
        public void SetItemName(string name)
        {
            _item.SetItemName(name);
        }
        public string GetItemName()
        {
            return _item.GetItemName();
        }
        private bool _visible = true;
        public virtual bool IsVisible
        {
            get { return _visible; }
            set
            {
                if (_visible == value)
                    return;
                _visible = value;
            }
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
        public void SetMinSize(int width, int height)
        {
            _itemGeometry.SetMinWidth(width);
            _itemGeometry.SetMinHeight(height);
        }
        public void SetMaxSize(int width, int height)
        {
            _itemGeometry.SetMaxWidth(width);
            _itemGeometry.SetMaxHeight(height);
        }
        public int[] GetSize()
        {
            return _itemGeometry.GetSize();
        }

        //behavior
        private Behavior _itemBehavior = new Behavior();
        public void SetAlignment(ItemAlignment alignment)
        {
            _itemBehavior.SetAlignment(alignment);
            UpdateBehavior();
        }

        internal void UpdateBehavior()
        {
            if (GetParent() == null)
                return;

            ItemAlignment alignment = GetAlignment();

            if (alignment.HasFlag(ItemAlignment.Left))
            {
                SetX(GetParent().GetX() + GetParent().GetPadding().Left + GetMargin().Left);//
            }
            if (alignment.HasFlag(ItemAlignment.Right))
            {
                SetX(GetParent().GetX() + GetParent().GetWidth() - GetWidth() - GetParent().GetPadding().Right - GetMargin().Right);//
            }
            if (alignment.HasFlag(ItemAlignment.Top))
            {
                SetY(GetParent().GetY() + GetParent().GetPadding().Top + GetMargin().Top);//
            }
            if (alignment.HasFlag(ItemAlignment.Bottom))
            {
                SetY(GetParent().GetY() + GetParent().GetHeight() - GetHeight() - GetParent().GetPadding().Bottom - GetMargin().Bottom);//
            }
            if (alignment.HasFlag(ItemAlignment.HCenter))
            {
                SetX(GetParent().GetX() + (GetParent().GetWidth() - GetWidth()) / 2 + GetMargin().Left - GetMargin().Right);//
            }
            if (alignment.HasFlag(ItemAlignment.VCenter))
            {
                SetY(GetParent().GetY() + (GetParent().GetHeight() - GetHeight()) / 2 + GetMargin().Top - GetMargin().Bottom);//
            }
        }
        public ItemAlignment GetAlignment()
        {
            return _itemBehavior.GetAlignment();
        }
        public void SetSizePolicy(SizePolicy width, SizePolicy height)
        {
            SetWidthPolicy(width);
            SetHeightPolicy(height);
        }
        public void SetWidthPolicy(SizePolicy policy)
        {
            if (_itemBehavior.GetWidthPolicy() != policy)
            {
                _itemBehavior.SetWidthPolicy(policy);
            }
        }
        public SizePolicy GetWidthPolicy()
        {
            return _itemBehavior.GetWidthPolicy();
        }
        public void SetHeightPolicy(SizePolicy policy)
        {
            if (_itemBehavior.GetHeightPolicy() != policy)
            {
                _itemBehavior.SetHeightPolicy(policy);
            }
        }
        public SizePolicy GetHeightPolicy()
        {
            return _itemBehavior.GetHeightPolicy();
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

        //update
        public void Update(GeometryEventType type, int value = 0)
        {
            //lock (DrawEngine.engine_locker)
            {
                switch (type)
                {
                    case GeometryEventType.Moved_X:
                        SetX(GetX() + value);
                        break;

                    case GeometryEventType.Moved_Y:
                        SetY(GetY() + value);
                        break;

                    case GeometryEventType.ResizeWidth:
                        if (GetWidthPolicy() == SizePolicy.Fixed)
                        {
                            if (GetAlignment().HasFlag(ItemAlignment.Right))
                            {
                                SetX(GetParent().GetX() + GetParent().GetWidth() - GetWidth() - GetParent().GetPadding().Right - GetMargin().Right);//
                            }
                            if (GetAlignment().HasFlag(ItemAlignment.HCenter))
                            {
                                SetX(GetParent().GetX() + (GetParent().GetWidth() - GetWidth()) / 2 + GetMargin().Left - GetMargin().Right);
                            }
                        }
                        else if (GetWidthPolicy() == SizePolicy.Expand)
                        {
                            int prefered = GetParent().GetWidth() - GetParent().GetPadding().Left - GetParent().GetPadding().Right - GetMargin().Right - GetMargin().Left;//
                            prefered = (prefered > GetMaxWidth()) ? GetMaxWidth() : prefered;
                            prefered = (prefered < GetMinWidth()) ? GetMinWidth() : prefered;
                            SetWidth(prefered);

                            if (prefered + GetParent().GetPadding().Left + GetParent().GetPadding().Right + GetMargin().Right + GetMargin().Left == GetParent().GetWidth())//
                            {
                                SetX(GetParent().GetX() + GetParent().GetPadding().Left + GetMargin().Left);//
                            }
                            else if (prefered + GetParent().GetPadding().Left + GetParent().GetPadding().Right + GetMargin().Right + GetMargin().Left < GetParent().GetWidth())//
                            {
                                if (GetAlignment().HasFlag(ItemAlignment.Right))
                                {
                                    SetX(GetParent().GetX() + GetParent().GetWidth() - GetWidth() - GetParent().GetPadding().Right - GetMargin().Right);//
                                }
                                if (GetAlignment().HasFlag(ItemAlignment.HCenter))
                                {
                                    SetX(GetParent().GetX() + (GetParent().GetWidth() - GetWidth()) / 2 + GetMargin().Left);//
                                }
                            }
                            else if (prefered + GetParent().GetPadding().Left + GetParent().GetPadding().Right + GetMargin().Right + GetMargin().Left > GetParent().GetWidth())//
                            {
                                //никогда не должен зайти
                                SetX(GetParent().GetX() + GetParent().GetPadding().Left + GetMargin().Left);//
                                prefered = GetParent().GetWidth() - GetParent().GetPadding().Left - GetParent().GetPadding().Right - GetMargin().Left - GetMargin().Right;//
                                SetWidth(prefered);
                            }
                        }
                        break;

                    case GeometryEventType.ResizeHeight:
                        if (GetHeightPolicy() == SizePolicy.Fixed)
                        {
                            if (GetAlignment().HasFlag(ItemAlignment.Bottom))
                            {
                                SetY(GetParent().GetY() + GetParent().GetHeight() - GetHeight() - GetParent().GetPadding().Bottom - GetMargin().Bottom);//
                            }
                            if (GetAlignment().HasFlag(ItemAlignment.VCenter))
                            {
                                SetY(GetParent().GetY() + (GetParent().GetHeight() - GetHeight()) / 2 + GetMargin().Top - GetMargin().Bottom);
                            }
                        }
                        else if (GetHeightPolicy() == SizePolicy.Expand)
                        {
                            int prefered = GetParent().GetHeight() - GetParent().GetPadding().Top - GetParent().GetPadding().Bottom - GetMargin().Bottom - GetMargin().Top;//
                            prefered = (prefered > GetMaxHeight()) ? GetMaxHeight() : prefered;
                            prefered = (prefered < GetMinHeight()) ? GetMinHeight() : prefered;
                            SetHeight(prefered);

                            if (prefered + GetParent().GetPadding().Top + GetParent().GetPadding().Bottom + GetMargin().Bottom + GetMargin().Top == GetParent().GetHeight())//
                            {
                                SetY(GetParent().GetY() + GetParent().GetPadding().Top + GetMargin().Top);//
                            }
                            else if (prefered + GetParent().GetPadding().Top + GetParent().GetPadding().Bottom + GetMargin().Bottom + GetMargin().Top < GetParent().GetHeight())//
                            {
                                if (GetAlignment().HasFlag(ItemAlignment.Bottom))
                                {
                                    SetY(GetParent().GetY() + GetParent().GetHeight() - GetHeight() - GetParent().GetPadding().Bottom - GetMargin().Bottom);//
                                }
                                if (GetAlignment().HasFlag(ItemAlignment.VCenter))
                                {
                                    SetY(GetParent().GetY() + (GetParent().GetHeight() - GetHeight()) / 2 + GetMargin().Top);//
                                }
                            }
                            else if (prefered + GetParent().GetPadding().Top + GetParent().GetPadding().Bottom + GetMargin().Bottom + GetMargin().Top > GetParent().GetHeight())//
                            {
                                //никогда не должен зайти
                                SetY(GetParent().GetY() + GetParent().GetPadding().Top + GetMargin().Top);//
                                prefered = GetParent().GetHeight() - GetParent().GetPadding().Top - GetParent().GetPadding().Bottom - GetMargin().Top - GetMargin().Bottom;//
                                SetHeight(prefered);
                            }
                        }
                        break;

                    default:
                        break;
                }
            }
        }
        public virtual void UpdateGeometry()
        {
            Update(GeometryEventType.ResizeWidth);
            Update(GeometryEventType.ResizeHeight);
            Update(GeometryEventType.Moved_X);
            Update(GeometryEventType.Moved_Y);
        }

        public virtual void SetStyle(Style style) { }
        public virtual void CheckDefaults() 
        { 
            //checking all attributes
            //SetStyle(default theme)
            //foreach inners SetStyle(from item default style)
             
            SetDefaults();
        }
        public virtual void SetDefaults() { }
        //public StencilBehaviour CutBehaviour = StencilBehaviour.Strict;
    }
}
