using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;

namespace SpaceVIL
{
    abstract public class BaseItem : IBaseItem
    {
        internal int _confines_x_0 = 0;
        internal int _confines_x_1 = 0;
        internal int _confines_y_0 = 0;
        internal int _confines_y_1 = 0;

        private WindowLayout _handler;
        public void SetHandler(WindowLayout handler)
        {
            _handler = handler;
        }
        public WindowLayout GetHandler()
        {
            return _handler;
        }

        //parent
        private Prototype _parent = null;
        public Prototype GetParent()
        {
            return _parent;
        }
        public void SetParent(Prototype parent)
        {
            _parent = parent;
        }
        private void CastAndUpdate(IBaseItem item)
        {
            Prototype prototype = item as Prototype;
            if (prototype != null)
                prototype.GetCore().UpdateBehavior();
            else
                (item as BaseItem).UpdateBehavior();
        }
        internal void AddChildren(IBaseItem item)
        {
            if (item.GetParent() != null)
                item.GetParent().RemoveItem(item);

            //Console.WriteLine(this.GetItemName() + " " + (this is BaseItem) + " or " + (this is Prototype));

            item.SetParent((this as VisualItem)._main);

            //refactor events verification
            // Console.WriteLine(item.GetParent().GetType());

            var v_stack = item.GetParent() as IVLayout;
            if (v_stack != null)
            {
                AddEventListener(GeometryEventType.ResizeWidth, item);
                AddEventListener(GeometryEventType.Moved_X, item);
                CastAndUpdate(item);
                return;
            }
            var h_stack = item.GetParent() as IHLayout;
            if (h_stack != null)
            {
                AddEventListener(GeometryEventType.ResizeHeight, item);
                AddEventListener(GeometryEventType.Moved_Y, item);
                CastAndUpdate(item);
                return;
            }
            var grid_stack = item.GetParent() as IGrid;
            if (grid_stack != null)
            {
                // Console.WriteLine(item.GetParent().GetType() + " what?");
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
            CastAndUpdate(item);
        }
        internal virtual void AddEventListener(GeometryEventType type, IBaseItem listener) { }
        internal virtual void RemoveEventListener(GeometryEventType type, IBaseItem listener) { }
        public void RemoveItemFromListeners()
        {
            GetParent().RemoveEventListener(GeometryEventType.ResizeWidth, this);
            GetParent().RemoveEventListener(GeometryEventType.ResizeHeight, this);
            GetParent().RemoveEventListener(GeometryEventType.Moved_X, this);
            GetParent().RemoveEventListener(GeometryEventType.Moved_Y, this);
        }

        // abstract public void InitElements();
        public virtual void InitElements() { }

        private Item _item = new Item();
        private Indents _margin = new Indents();
        public Indents GetMargin()
        {
            return _margin;
        }
        public void SetMargin(Indents margin)
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
            if (GetTriangles().Count == 0)
                return null;

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

        private bool _drawable = true;
        public virtual bool IsDrawable()
        {
            return _drawable;
        }
        public virtual void SetDrawable(bool value)
        {
            if (_drawable == value)
                return;
            _drawable = value;
            // UpdateInnersDrawable(value);
        }
        private bool _visible = true;
        public virtual bool IsVisible()
        {
            return _visible;
        }
        public virtual void SetVisible(bool value)
        {
            if (_visible == value)
                return;
            _visible = value;
        }

        protected virtual void UpdateInnersDrawable(bool value)
        {

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

            if ((this as VisualItem) != null)
            {
                ProtoUpdateBehavior();
                return;
            }

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

        private void ProtoUpdateBehavior()
        {
            Prototype prt = (this as VisualItem)._main;

            ItemAlignment alignment = prt.GetAlignment();

            if (alignment.HasFlag(ItemAlignment.Left))
            {
                prt.SetX(prt.GetParent().GetX() + prt.GetParent().GetPadding().Left + prt.GetMargin().Left);//
            }
            if (alignment.HasFlag(ItemAlignment.Right))
            {
                prt.SetX(prt.GetParent().GetX() + prt.GetParent().GetWidth() - prt.GetWidth() - prt.GetParent().GetPadding().Right - prt.GetMargin().Right);//
            }
            if (alignment.HasFlag(ItemAlignment.Top))
            {
                prt.SetY(prt.GetParent().GetY() + prt.GetParent().GetPadding().Top + prt.GetMargin().Top);//
            }
            if (alignment.HasFlag(ItemAlignment.Bottom))
            {
                prt.SetY(prt.GetParent().GetY() + prt.GetParent().GetHeight() - prt.GetHeight() - prt.GetParent().GetPadding().Bottom - prt.GetMargin().Bottom);//
            }
            if (alignment.HasFlag(ItemAlignment.HCenter))
            {
                prt.SetX(prt.GetParent().GetX() + (prt.GetParent().GetWidth() - prt.GetWidth()) / 2 + prt.GetMargin().Left - prt.GetMargin().Right);//
            }
            if (alignment.HasFlag(ItemAlignment.VCenter))
            {
                prt.SetY(prt.GetParent().GetY() + (prt.GetParent().GetHeight() - prt.GetHeight()) / 2 + prt.GetMargin().Top - prt.GetMargin().Bottom);//
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
        public virtual void SetConfines()
        {
            _confines_x_0 = GetParent().GetX() + GetParent().GetPadding().Left;
            _confines_x_1 = GetParent().GetX() + GetParent().GetWidth() - GetParent().GetPadding().Right;
            _confines_y_0 = GetParent().GetY() + GetParent().GetPadding().Top;
            _confines_y_1 = GetParent().GetY() + GetParent().GetHeight() - GetParent().GetPadding().Bottom;
        }

        public void SetConfines(int x0, int x1, int y0, int y1)
        {
            _confines_x_0 = x0;
            _confines_x_1 = x1;
            _confines_y_0 = y0;
            _confines_y_1 = y1;
        }

        internal bool IsOutConfines()
        {
            if (
                GetX() >= _confines_x_1 ||
                GetX() + GetWidth() <= _confines_x_0 ||
                GetY() >= _confines_y_1 ||
                GetY() + GetHeight() <= _confines_y_0
               )
                return true;
            return false;
        }

        public void Update(GeometryEventType type, int value = 0)
        {
            if ((this as VisualItem) != null)
            {
                ProtoUpdate(type, value);
                return;
            }

            SetConfines();
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

        private void ProtoUpdate(GeometryEventType type, int value = 0)
        {
            Prototype prt = (this as VisualItem)._main;
            prt.SetConfines();
            switch (type)
            {
                case GeometryEventType.Moved_X:
                    prt.SetX(GetX() + value);
                    break;

                case GeometryEventType.Moved_Y:
                    prt.SetY(GetY() + value);
                    break;

                case GeometryEventType.ResizeWidth:
                    if (prt.GetWidthPolicy() == SizePolicy.Fixed)
                    {
                        if (prt.GetAlignment().HasFlag(ItemAlignment.Right))
                        {
                            prt.SetX(prt.GetParent().GetX() + prt.GetParent().GetWidth() - prt.GetWidth() - prt.GetParent().GetPadding().Right - prt.GetMargin().Right);//
                        }
                        if (prt.GetAlignment().HasFlag(ItemAlignment.HCenter))
                        {
                            prt.SetX(prt.GetParent().GetX() + (prt.GetParent().GetWidth() - prt.GetWidth()) / 2 + prt.GetMargin().Left - prt.GetMargin().Right);
                        }
                    }
                    else if (prt.GetWidthPolicy() == SizePolicy.Expand)
                    {
                        int prefered = prt.GetParent().GetWidth() - prt.GetParent().GetPadding().Left - prt.GetParent().GetPadding().Right - prt.GetMargin().Right - prt.GetMargin().Left;//
                        prefered = (prefered > prt.GetMaxWidth()) ? prt.GetMaxWidth() : prefered;
                        prefered = (prefered < prt.GetMinWidth()) ? prt.GetMinWidth() : prefered;
                        prt.SetWidth(prefered);

                        if (prefered + prt.GetParent().GetPadding().Left + prt.GetParent().GetPadding().Right + prt.GetMargin().Right + prt.GetMargin().Left == prt.GetParent().GetWidth())//
                        {
                            prt.SetX(prt.GetParent().GetX() + prt.GetParent().GetPadding().Left + prt.GetMargin().Left);//
                        }
                        else if (prefered + prt.GetParent().GetPadding().Left + prt.GetParent().GetPadding().Right + prt.GetMargin().Right + prt.GetMargin().Left < prt.GetParent().GetWidth())//
                        {
                            if (prt.GetAlignment().HasFlag(ItemAlignment.Right))
                            {
                                prt.SetX(prt.GetParent().GetX() + prt.GetParent().GetWidth() - prt.GetWidth() - prt.GetParent().GetPadding().Right - prt.GetMargin().Right);//
                            }
                            if (prt.GetAlignment().HasFlag(ItemAlignment.HCenter))
                            {
                                prt.SetX(prt.GetParent().GetX() + (prt.GetParent().GetWidth() - prt.GetWidth()) / 2 + prt.GetMargin().Left);//
                            }
                        }
                        else if (prefered + prt.GetParent().GetPadding().Left + prt.GetParent().GetPadding().Right + prt.GetMargin().Right + prt.GetMargin().Left > prt.GetParent().GetWidth())//
                        {
                            //никогда не должен зайти
                            prt.SetX(prt.GetParent().GetX() + prt.GetParent().GetPadding().Left + prt.GetMargin().Left);//
                            prefered = prt.GetParent().GetWidth() - prt.GetParent().GetPadding().Left - prt.GetParent().GetPadding().Right - prt.GetMargin().Left - prt.GetMargin().Right;//
                            prt.SetWidth(prefered);
                        }
                    }
                    break;

                case GeometryEventType.ResizeHeight:
                    if (prt.GetHeightPolicy() == SizePolicy.Fixed)
                    {
                        if (prt.GetAlignment().HasFlag(ItemAlignment.Bottom))
                        {
                            prt.SetY(prt.GetParent().GetY() + prt.GetParent().GetHeight() - prt.GetHeight() - prt.GetParent().GetPadding().Bottom - prt.GetMargin().Bottom);//
                        }
                        if (prt.GetAlignment().HasFlag(ItemAlignment.VCenter))
                        {
                            prt.SetY(prt.GetParent().GetY() + (prt.GetParent().GetHeight() - prt.GetHeight()) / 2 + prt.GetMargin().Top - prt.GetMargin().Bottom);
                        }
                    }
                    else if (prt.GetHeightPolicy() == SizePolicy.Expand)
                    {
                        int prefered = prt.GetParent().GetHeight() - prt.GetParent().GetPadding().Top - prt.GetParent().GetPadding().Bottom - prt.GetMargin().Bottom - prt.GetMargin().Top;//
                        prefered = (prefered > prt.GetMaxHeight()) ? prt.GetMaxHeight() : prefered;
                        prefered = (prefered < prt.GetMinHeight()) ? prt.GetMinHeight() : prefered;
                        prt.SetHeight(prefered);

                        if (prefered + prt.GetParent().GetPadding().Top + prt.GetParent().GetPadding().Bottom + prt.GetMargin().Bottom + prt.GetMargin().Top == prt.GetParent().GetHeight())//
                        {
                            prt.SetY(prt.GetParent().GetY() + prt.GetParent().GetPadding().Top + prt.GetMargin().Top);//
                        }
                        else if (prefered + prt.GetParent().GetPadding().Top + prt.GetParent().GetPadding().Bottom + prt.GetMargin().Bottom + prt.GetMargin().Top < prt.GetParent().GetHeight())//
                        {
                            if (prt.GetAlignment().HasFlag(ItemAlignment.Bottom))
                            {
                                prt.SetY(prt.GetParent().GetY() + prt.GetParent().GetHeight() - prt.GetHeight() - prt.GetParent().GetPadding().Bottom - prt.GetMargin().Bottom);//
                            }
                            if (prt.GetAlignment().HasFlag(ItemAlignment.VCenter))
                            {
                                prt.SetY(prt.GetParent().GetY() + (prt.GetParent().GetHeight() - prt.GetHeight()) / 2 + prt.GetMargin().Top);//
                            }
                        }
                        else if (prefered + prt.GetParent().GetPadding().Top + prt.GetParent().GetPadding().Bottom + prt.GetMargin().Bottom + prt.GetMargin().Top > prt.GetParent().GetHeight())//
                        {
                            //никогда не должен зайти
                            prt.SetY(prt.GetParent().GetY() + prt.GetParent().GetPadding().Top + prt.GetMargin().Top);//
                            prefered = prt.GetParent().GetHeight() - prt.GetParent().GetPadding().Top - prt.GetParent().GetPadding().Bottom - prt.GetMargin().Top - prt.GetMargin().Bottom;//
                            prt.SetHeight(prefered);
                        }
                    }
                    break;

                default:
                    break;
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
        public abstract Style GetCoreStyle();
        // public virtual Style GetCoreStyle() { throw new NotImplementedException(); }
        public virtual void CheckDefaults()
        {
            //checking all attributes
            //SetStyle(default theme)
            //foreach inners SetStyle(from item default style)

            SetDefaults();
        }
        public virtual void SetDefaults() { }
        public ItemRule HoverRule = ItemRule.Lazy;

        //shadow
        private bool _is_shadow_drop = false;
        public bool IsShadowDrop()
        {
            return _is_shadow_drop;
        }
        public void SetShadowDrop(bool value)
        {
            _is_shadow_drop = value;
        }
        private float _shadow_radius = 1.0f;
        public void SetShadowRadius(float radius)
        {
            _shadow_radius = radius;
        }
        public float GetShadowRadius()
        {
            return _shadow_radius;
        }
        private Color _shadow_color = Color.Black;
        public Color GetShadowColor()
        {
            return _shadow_color;
        }
        public void SetShadowColor(Color color)
        {
            _shadow_color = color;
        }
        private Position _shadow_pos = new Position();
        public Position GetShadowPos()
        {
            return _shadow_pos;
        }
        public void SetShadow(float radius, int x, int y, Color color)
        {
            _is_shadow_drop = true;
            _shadow_radius = radius;
            _shadow_color = color;
            _shadow_pos.SetX(x);
            _shadow_pos.SetY(y);
        }

        public int[] GetConfines()
        {
            return new int[] { _confines_x_0, _confines_x_1, _confines_y_0, _confines_y_1 };
        }
    }
}
