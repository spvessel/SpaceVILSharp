using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    abstract public class BaseItem : IBaseItem
    {
        internal int _confines_x_0 = 0;
        internal int _confines_x_1 = 0;
        internal int _confines_y_0 = 0;
        internal int _confines_y_1 = 0;

        private CoreWindow _handler;

        /// <param name="handler"> WindowLayout handler - window that 
        /// contains the BaseItem </param>
        public void SetHandler(CoreWindow handler)
        {
            _handler = handler;
        }
        public CoreWindow GetHandler()
        {
            return _handler;
        }

        //parent
        private Prototype _parent = null;

        /// <summary>
        /// BaseItem's parent item
        /// </summary>
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
            Prototype itemParent = item.GetParent();
            if (itemParent != null)
                itemParent.RemoveItem(item);

            item.SetParent((this as VisualItem)._main);
            itemParent = item.GetParent();

            if (itemParent is IVLayout)
            {
                AddEventListener(GeometryEventType.ResizeWidth, item);
                AddEventListener(GeometryEventType.Moved_X, item);
                CastAndUpdate(item);
                return;
            }
            if (itemParent is IHLayout)
            {
                AddEventListener(GeometryEventType.ResizeHeight, item);
                AddEventListener(GeometryEventType.Moved_Y, item);
                CastAndUpdate(item);
                return;
            }
            if (itemParent is IGrid)
                return;

            if (itemParent is IFree)
                return;

            AddEventListener(GeometryEventType.ResizeWidth, item);
            AddEventListener(GeometryEventType.ResizeHeight, item);
            AddEventListener(GeometryEventType.Moved_X, item);
            AddEventListener(GeometryEventType.Moved_Y, item);
            CastAndUpdate(item);
        }

        internal virtual void AddEventListener(GeometryEventType type, IBaseItem listener) { }

        internal virtual void RemoveEventListener(GeometryEventType type, IBaseItem listener) { }

        /// <summary>
        /// Item will not react on parent's changes
        /// </summary>
        public virtual void RemoveItemFromListeners()
        {
            Prototype parent = GetParent();
            parent.RemoveEventListener(GeometryEventType.ResizeWidth, this);
            parent.RemoveEventListener(GeometryEventType.ResizeHeight, this);
            parent.RemoveEventListener(GeometryEventType.Moved_X, this);
            parent.RemoveEventListener(GeometryEventType.Moved_Y, this);
        }

        /// <summary>
        /// Initialization and adding of all elements in the BaseItem
        /// </summary>
        public virtual void InitElements() { }

        private Item _item = new Item();

        private Indents _margin = new Indents();

        /// <summary>
        /// BaseItem margin
        /// </summary>
        public Indents GetMargin()
        {
            return _margin;
        }
        public void SetMargin(Indents margin)
        {
            _margin = margin;
            UpdateGeometry();
            Prototype parent = GetParent();
            if (parent != null)
            {
                var hLayout = parent as IHLayout;
                var vLayout = parent as IVLayout;
                var grid = parent as IGrid;

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
        public void SetMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _margin.Left = left;
            _margin.Top = top;
            _margin.Right = right;
            _margin.Bottom = bottom;
            UpdateGeometry();
            Prototype parent = GetParent();
            if (parent != null)
            {
                var hLayout = parent as IHLayout;
                var vLayout = parent as IVLayout;
                var grid = parent as IGrid;

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

        /// <returns>triangles list of the BaseItem's shape</returns>
        public List<float[]> GetTriangles()
        {
            return _item.GetTriangles();
        }

        /// <summary>
        /// Sets BaseItem's shape as triangles list
        /// </summary>
        public virtual void SetTriangles(List<float[]> triangles)
        {
            _item.SetTriangles(triangles);
        }

        /// <returns>shape points list in GL coordinates, using triangles 
        /// from getTriangles()</returns>
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

        /// <summary>
        /// Background color of the BaseItem
        /// </summary>
        public virtual void SetBackground(Color color)
        {
            _item.SetBackground(color);
        }
        public virtual void SetBackground(int r, int g, int b)
        {
            _item.SetBackground(GraphicsMathService.ColorTransform(r, g, b));
        }
        public virtual void SetBackground(int r, int g, int b, int a)
        {
            _item.SetBackground(GraphicsMathService.ColorTransform(r, g, b, a));
        }
        public virtual void SetBackground(float r, float g, float b)
        {
            _item.SetBackground(GraphicsMathService.ColorTransform(r, g, b));
        }
        public virtual void SetBackground(float r, float g, float b, float a)
        {
            _item.SetBackground(GraphicsMathService.ColorTransform(r, g, b, a));
        }
        public virtual Color GetBackground()
        {
            return _item.GetBackground();
        }

        /// <summary>
        /// BaseItem's name
        /// </summary>
        public void SetItemName(string name)
        {
            _item.SetItemName(name);
        }
        public string GetItemName()
        {
            return _item.GetItemName();
        }

        private bool _drawable = true;

        /// <summary>
        /// If BaseItem will drawn by engine
        /// </summary>
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

        /// <summary>
        /// If BaseItem is visible
        /// </summary>
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

        /// <summary>
        /// Width of the BaseItem
        /// </summary>
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

        /// <summary>
        /// Height of the BaseItem
        /// </summary>
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

        /// <summary>
        /// Size (width and height) of the BaseItem
        /// </summary>
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
        public int[] GetMinSize()
        {
            return new int[] { _itemGeometry.GetMinWidth(), _itemGeometry.GetMinHeight() };
        }
        public int[] GetMaxSize()
        {
            return new int[] { _itemGeometry.GetMaxWidth(), _itemGeometry.GetMaxHeight() };
        }

        //behavior
        private Behavior _itemBehavior = new Behavior();

        /// <summary>
        /// BaseItem alignment
        /// </summary>
        public void SetAlignment(ItemAlignment alignment)
        {
            _itemBehavior.SetAlignment(alignment);
            UpdateGeometry();

            Prototype parent = GetParent();
            if (parent != null)
            {
                var hLayout = parent as IHLayout;
                var vLayout = parent as IVLayout;
                var grid = parent as IGrid;

                if (hLayout == null
                && vLayout == null
                && grid == null)
                    UpdateBehavior();

                if (hLayout != null)
                    hLayout.UpdateLayout();
                if (vLayout != null)
                    vLayout.UpdateLayout();
                if (grid != null)
                    grid.UpdateLayout();
            }
        }
        public void SetAlignment(params ItemAlignment[] alignment)
        {
            ItemAlignment common = alignment.ElementAt(0);
            if (alignment.Length > 1)
            {
                for (int i = 1; i < alignment.Length; i++)
                {
                    common |= alignment.ElementAt(i);
                }
            }
            SetAlignment(common);
        }

        public ItemAlignment GetAlignment()
        {
            return _itemBehavior.GetAlignment();
        }

        internal void UpdateBehavior()
        {
            Prototype parent = GetParent();
            if (parent == null)
                return;

            if ((this as VisualItem) != null)
            {
                ProtoUpdateBehavior(parent);
                return;
            }

            ItemAlignment alignment = GetAlignment();

            if (alignment.HasFlag(ItemAlignment.Left))
            {
                SetX(parent.GetX() + parent.GetPadding().Left + GetMargin().Left);//
            }
            if (alignment.HasFlag(ItemAlignment.Right))
            {
                SetX(parent.GetX() + parent.GetWidth() - GetWidth() - parent.GetPadding().Right - GetMargin().Right);//
            }
            if (alignment.HasFlag(ItemAlignment.Top))
            {
                SetY(parent.GetY() + parent.GetPadding().Top + GetMargin().Top);//
            }
            if (alignment.HasFlag(ItemAlignment.Bottom))
            {
                SetY(parent.GetY() + parent.GetHeight() - GetHeight() - parent.GetPadding().Bottom - GetMargin().Bottom);//
            }
            if (alignment.HasFlag(ItemAlignment.HCenter))
            {
                SetX(parent.GetX() + (parent.GetWidth() - GetWidth()) / 2 + GetMargin().Left - GetMargin().Right);//
            }
            if (alignment.HasFlag(ItemAlignment.VCenter))
            {
                SetY(parent.GetY() + (parent.GetHeight() - GetHeight()) / 2 + GetMargin().Top - GetMargin().Bottom);//
            }
        }

        private void ProtoUpdateBehavior(Prototype parent)
        {
            Prototype prt = (this as VisualItem)._main;

            ItemAlignment alignment = prt.GetAlignment();

            if (alignment.HasFlag(ItemAlignment.Left))
            {
                prt.SetX(parent.GetX() + parent.GetPadding().Left + prt.GetMargin().Left);//
            }
            if (alignment.HasFlag(ItemAlignment.Right))
            {
                prt.SetX(parent.GetX() + parent.GetWidth() - prt.GetWidth() - parent.GetPadding().Right - prt.GetMargin().Right);//
            }
            if (alignment.HasFlag(ItemAlignment.Top))
            {
                prt.SetY(parent.GetY() + parent.GetPadding().Top + prt.GetMargin().Top);//
            }
            if (alignment.HasFlag(ItemAlignment.Bottom))
            {
                prt.SetY(parent.GetY() + parent.GetHeight() - prt.GetHeight() - parent.GetPadding().Bottom - prt.GetMargin().Bottom);//
            }
            if (alignment.HasFlag(ItemAlignment.HCenter))
            {
                prt.SetX(parent.GetX() + (parent.GetWidth() - prt.GetWidth()) / 2 + prt.GetMargin().Left - prt.GetMargin().Right);//
            }
            if (alignment.HasFlag(ItemAlignment.VCenter))
            {
                prt.SetY(parent.GetY() + (parent.GetHeight() - prt.GetHeight()) / 2 + prt.GetMargin().Top - prt.GetMargin().Bottom);//
            }
        }

        /// <summary>
        /// BaseItem size (width and height) policy - FIXED or EXPAND
        /// </summary>
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

                VisualItem vItem = this as VisualItem;
                Prototype protoItem = null;
                if (vItem != null)
                    protoItem = vItem._main;
                IFloating floatingItem = protoItem as IFloating;
                if (floatingItem != null)
                {
                    if (policy == SizePolicy.Expand)
                        ItemsLayoutBox.SubscribeWindowSizeMonitoring(protoItem, GeometryEventType.ResizeWidth);
                    else
                        ItemsLayoutBox.UnsubscribeWindowSizeMonitoring(protoItem, GeometryEventType.ResizeWidth);
                    UpdateGeometry();
                }

                Prototype parent = GetParent();
                if (parent != null)
                {
                    var hLayout = parent as IHLayout;
                    var vLayout = parent as IVLayout;
                    var grid = parent as IGrid;

                    if (hLayout == null
                    && vLayout == null
                    && grid == null)
                        UpdateBehavior();

                    if (hLayout != null)
                        hLayout.UpdateLayout();
                    if (vLayout != null)
                        vLayout.UpdateLayout();
                    if (grid != null)
                        grid.UpdateLayout();
                }
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

                VisualItem vItem = this as VisualItem;
                Prototype protoItem = null;
                if (vItem != null)
                    protoItem = vItem._main;
                IFloating floatingItem = protoItem as IFloating;
                if (floatingItem != null)
                {
                    if (policy == SizePolicy.Expand)
                        ItemsLayoutBox.SubscribeWindowSizeMonitoring(this, GeometryEventType.ResizeHeight);
                    else
                        ItemsLayoutBox.UnsubscribeWindowSizeMonitoring(this, GeometryEventType.ResizeHeight);

                    UpdateGeometry();
                }
                Prototype parent = GetParent();
                if (parent != null)
                {
                    var hLayout = parent as IHLayout;
                    var vLayout = parent as IVLayout;
                    var grid = parent as IGrid;

                    if (hLayout == null
                    && vLayout == null
                    && grid == null)
                        UpdateBehavior();

                    if (hLayout != null)
                        hLayout.UpdateLayout();
                    if (vLayout != null)
                        vLayout.UpdateLayout();
                    if (grid != null)
                        grid.UpdateLayout();
                }
            }
        }
        public SizePolicy GetHeightPolicy()
        {
            return _itemBehavior.GetHeightPolicy();
        }

        //position
        private Position _itemPosition = new Position();

        /// <summary>
        /// BaseItem (x, y) position
        /// </summary>
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

        // internal bool IsOutConfines()
        // {
        //     if (
        //         GetX() >= _confines_x_1 ||
        //         GetX() + GetWidth() <= _confines_x_0 ||
        //         GetY() >= _confines_y_1 ||
        //         GetY() + GetHeight() <= _confines_y_0
        //        )
        //         return true;
        //     return false;
        // }

        /// <summary>
        /// Update BaseItem's state
        /// </summary>
        public void Update(GeometryEventType type, int value = 0)
        {
            Prototype parent = GetParent();
            if (parent == null)
                return;

            if ((this as VisualItem) != null)
            {
                ProtoUpdate(type, value, parent);
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
                            SetX(parent.GetX() + parent.GetWidth() - GetWidth() - parent.GetPadding().Right - GetMargin().Right);//
                        }
                        if (GetAlignment().HasFlag(ItemAlignment.HCenter))
                        {
                            SetX(parent.GetX() + (parent.GetWidth() - GetWidth()) / 2 + GetMargin().Left - GetMargin().Right);
                        }
                    }
                    else if (GetWidthPolicy() == SizePolicy.Expand)
                    {
                        int prefered = parent.GetWidth() - parent.GetPadding().Left - parent.GetPadding().Right - GetMargin().Right - GetMargin().Left;//
                        prefered = (prefered > GetMaxWidth()) ? GetMaxWidth() : prefered;
                        prefered = (prefered < GetMinWidth()) ? GetMinWidth() : prefered;
                        SetWidth(prefered);

                        if (prefered + parent.GetPadding().Left + parent.GetPadding().Right + GetMargin().Right + GetMargin().Left == parent.GetWidth())//
                        {
                            SetX(parent.GetX() + parent.GetPadding().Left + GetMargin().Left);//
                        }
                        else if (prefered + parent.GetPadding().Left + parent.GetPadding().Right + GetMargin().Right + GetMargin().Left < parent.GetWidth())//
                        {
                            if (GetAlignment().HasFlag(ItemAlignment.Right))
                            {
                                SetX(parent.GetX() + parent.GetWidth() - GetWidth() - parent.GetPadding().Right - GetMargin().Right);//
                            }
                            if (GetAlignment().HasFlag(ItemAlignment.HCenter))
                            {
                                SetX(parent.GetX() + (parent.GetWidth() - GetWidth()) / 2 + GetMargin().Left);//
                            }
                        }
                        else if (prefered + parent.GetPadding().Left + parent.GetPadding().Right + GetMargin().Right + GetMargin().Left > parent.GetWidth())//
                        {
                            //никогда не должен зайти
                            SetX(parent.GetX() + parent.GetPadding().Left + GetMargin().Left);//
                            prefered = parent.GetWidth() - parent.GetPadding().Left - parent.GetPadding().Right - GetMargin().Left - GetMargin().Right;//
                            SetWidth(prefered);
                        }
                    }
                    break;

                case GeometryEventType.ResizeHeight:
                    if (GetHeightPolicy() == SizePolicy.Fixed)
                    {
                        if (GetAlignment().HasFlag(ItemAlignment.Bottom))
                        {
                            SetY(parent.GetY() + parent.GetHeight() - GetHeight() - parent.GetPadding().Bottom - GetMargin().Bottom);//
                        }
                        if (GetAlignment().HasFlag(ItemAlignment.VCenter))
                        {
                            SetY(parent.GetY() + (parent.GetHeight() - GetHeight()) / 2 + GetMargin().Top - GetMargin().Bottom);
                        }
                    }
                    else if (GetHeightPolicy() == SizePolicy.Expand)
                    {
                        int prefered = parent.GetHeight() - parent.GetPadding().Top - parent.GetPadding().Bottom - GetMargin().Bottom - GetMargin().Top;//
                        prefered = (prefered > GetMaxHeight()) ? GetMaxHeight() : prefered;
                        prefered = (prefered < GetMinHeight()) ? GetMinHeight() : prefered;
                        SetHeight(prefered);

                        if (prefered + parent.GetPadding().Top + parent.GetPadding().Bottom + GetMargin().Bottom + GetMargin().Top == parent.GetHeight())//
                        {
                            SetY(parent.GetY() + parent.GetPadding().Top + GetMargin().Top);//
                        }
                        else if (prefered + parent.GetPadding().Top + parent.GetPadding().Bottom + GetMargin().Bottom + GetMargin().Top < parent.GetHeight())//
                        {
                            if (GetAlignment().HasFlag(ItemAlignment.Bottom))
                            {
                                SetY(parent.GetY() + parent.GetHeight() - GetHeight() - parent.GetPadding().Bottom - GetMargin().Bottom);//
                            }
                            if (GetAlignment().HasFlag(ItemAlignment.VCenter))
                            {
                                SetY(parent.GetY() + (parent.GetHeight() - GetHeight()) / 2 + GetMargin().Top);//
                            }
                        }
                        else if (prefered + parent.GetPadding().Top + parent.GetPadding().Bottom + GetMargin().Bottom + GetMargin().Top > parent.GetHeight())//
                        {
                            //никогда не должен зайти
                            SetY(parent.GetY() + parent.GetPadding().Top + GetMargin().Top);//
                            prefered = parent.GetHeight() - parent.GetPadding().Top - parent.GetPadding().Bottom - GetMargin().Top - GetMargin().Bottom;//
                            SetHeight(prefered);
                        }
                    }
                    break;

                default:
                    break;
            }
        }

        private void ProtoUpdate(GeometryEventType type, int value, Prototype parent)
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
                            prt.SetX(parent.GetX() + parent.GetWidth() - prt.GetWidth() - parent.GetPadding().Right - prt.GetMargin().Right);//
                        }
                        if (prt.GetAlignment().HasFlag(ItemAlignment.HCenter))
                        {
                            prt.SetX(parent.GetX() + (parent.GetWidth() - prt.GetWidth()) / 2 + prt.GetMargin().Left - prt.GetMargin().Right);
                        }
                    }
                    else if (prt.GetWidthPolicy() == SizePolicy.Expand)
                    {
                        int prefered = parent.GetWidth() - parent.GetPadding().Left - parent.GetPadding().Right - prt.GetMargin().Right - prt.GetMargin().Left;//
                        prefered = (prefered > prt.GetMaxWidth()) ? prt.GetMaxWidth() : prefered;
                        prefered = (prefered < prt.GetMinWidth()) ? prt.GetMinWidth() : prefered;
                        prt.SetWidth(prefered);

                        if (prefered + parent.GetPadding().Left + parent.GetPadding().Right + prt.GetMargin().Right + prt.GetMargin().Left == parent.GetWidth())//
                        {
                            prt.SetX(parent.GetX() + parent.GetPadding().Left + prt.GetMargin().Left);//
                        }
                        else if (prefered + parent.GetPadding().Left + parent.GetPadding().Right + prt.GetMargin().Right + prt.GetMargin().Left < parent.GetWidth())//
                        {
                            if (prt.GetAlignment().HasFlag(ItemAlignment.Right))
                            {
                                prt.SetX(parent.GetX() + parent.GetWidth() - prt.GetWidth() - parent.GetPadding().Right - prt.GetMargin().Right);//
                            }
                            if (prt.GetAlignment().HasFlag(ItemAlignment.HCenter))
                            {
                                prt.SetX(parent.GetX() + (parent.GetWidth() - prt.GetWidth()) / 2 + prt.GetMargin().Left);//
                            }
                        }
                        else if (prefered + parent.GetPadding().Left + parent.GetPadding().Right + prt.GetMargin().Right + prt.GetMargin().Left > parent.GetWidth())//
                        {
                            //никогда не должен зайти
                            prt.SetX(parent.GetX() + parent.GetPadding().Left + prt.GetMargin().Left);//
                            prefered = parent.GetWidth() - parent.GetPadding().Left - parent.GetPadding().Right - prt.GetMargin().Left - prt.GetMargin().Right;//
                            prt.SetWidth(prefered);
                        }
                    }
                    break;

                case GeometryEventType.ResizeHeight:
                    if (prt.GetHeightPolicy() == SizePolicy.Fixed)
                    {
                        if (prt.GetAlignment().HasFlag(ItemAlignment.Bottom))
                        {
                            prt.SetY(parent.GetY() + parent.GetHeight() - prt.GetHeight() - parent.GetPadding().Bottom - prt.GetMargin().Bottom);//
                        }
                        if (prt.GetAlignment().HasFlag(ItemAlignment.VCenter))
                        {
                            prt.SetY(parent.GetY() + (parent.GetHeight() - prt.GetHeight()) / 2 + prt.GetMargin().Top - prt.GetMargin().Bottom);
                        }
                    }
                    else if (prt.GetHeightPolicy() == SizePolicy.Expand)
                    {
                        int prefered = parent.GetHeight() - parent.GetPadding().Top - parent.GetPadding().Bottom - prt.GetMargin().Bottom - prt.GetMargin().Top;//
                        prefered = (prefered > prt.GetMaxHeight()) ? prt.GetMaxHeight() : prefered;
                        prefered = (prefered < prt.GetMinHeight()) ? prt.GetMinHeight() : prefered;
                        prt.SetHeight(prefered);

                        if (prefered + parent.GetPadding().Top + parent.GetPadding().Bottom + prt.GetMargin().Bottom + prt.GetMargin().Top == parent.GetHeight())//
                        {
                            prt.SetY(parent.GetY() + parent.GetPadding().Top + prt.GetMargin().Top);//
                        }
                        else if (prefered + parent.GetPadding().Top + parent.GetPadding().Bottom + prt.GetMargin().Bottom + prt.GetMargin().Top < parent.GetHeight())//
                        {
                            if (prt.GetAlignment().HasFlag(ItemAlignment.Bottom))
                            {
                                prt.SetY(parent.GetY() + parent.GetHeight() - prt.GetHeight() - parent.GetPadding().Bottom - prt.GetMargin().Bottom);//
                            }
                            if (prt.GetAlignment().HasFlag(ItemAlignment.VCenter))
                            {
                                prt.SetY(parent.GetY() + (parent.GetHeight() - prt.GetHeight()) / 2 + prt.GetMargin().Top);//
                            }
                        }
                        else if (prefered + parent.GetPadding().Top + parent.GetPadding().Bottom + prt.GetMargin().Bottom + prt.GetMargin().Top > parent.GetHeight())//
                        {
                            //никогда не должен зайти
                            prt.SetY(parent.GetY() + parent.GetPadding().Top + prt.GetMargin().Top);//
                            prefered = parent.GetHeight() - parent.GetPadding().Top - parent.GetPadding().Bottom - prt.GetMargin().Top - prt.GetMargin().Bottom;//
                            prt.SetHeight(prefered);
                        }
                    }
                    break;

                default:
                    break;
            }
        }

        internal virtual void UpdateGeometry()
        {
            Update(GeometryEventType.ResizeWidth);
            Update(GeometryEventType.ResizeHeight);
            Update(GeometryEventType.Moved_X);
            Update(GeometryEventType.Moved_Y);
        }

        /// <summary>
        /// Style of the BaseItem
        /// </summary>
        public virtual void SetStyle(Style style) { }
        public abstract Style GetCoreStyle();

        // public virtual Style GetCoreStyle() { throw new NotImplementedException(); }

        /// <summary>
        /// Check and set BaseItem default style
        /// </summary>
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
        private int _shadow_radius = 1;
        private Color _shadow_color = Color.Black;
        private Position _shadow_pos = new Position();

        /// <summary>
        /// BaseItem's shadow parameters. Is item has shadow
        /// </summary>
        public bool IsShadowDrop()
        {
            return _is_shadow_drop;
        }
        public void SetShadowDrop(bool value)
        {
            _is_shadow_drop = value;
        }

        /// <summary>
        /// BaseItem's shadow parameters. Shadow corners radius
        /// </summary>
        public void SetShadowRadius(int radius)
        {
            _shadow_radius = radius;
        }
        public int GetShadowRadius()
        {
            return _shadow_radius;
        }

        /// <summary>
        /// BaseItem's shadow parameters. Shadow color
        /// </summary>
        public Color GetShadowColor()
        {
            return _shadow_color;
        }
        public void SetShadowColor(Color color)
        {
            _shadow_color = color;
        }

        /// <summary>
        /// BaseItem's shadow parameters. Shadow position
        /// </summary>
        public Position GetShadowPos()
        {
            return _shadow_pos;
        }
        /// <summary>
        /// Set BaseItem's shadow with parameters
        /// </summary>
        /// <param name="radius">Shadow corners radius (same for all corners)</param>
        /// <param name="x">Shadow X position</param>
        /// <param name="y">Shadow Y position</param>
        /// <param name="color">Shadow color</param>
        public void SetShadow(int radius, int x, int y, Color color)
        {
            _is_shadow_drop = true;
            _shadow_radius = radius;
            _shadow_color = color;
            _shadow_pos.SetX(x);
            _shadow_pos.SetY(y);
        }
        public void SetShadow(Shadow shadow)
        {
            _is_shadow_drop = shadow.IsDrop();
            _shadow_radius = shadow.GetRadius();
            _shadow_color = shadow.GetColor();
            _shadow_pos.SetX(shadow.GetXOffset());
            _shadow_pos.SetY(shadow.GetYOffset());
        }

        //update
        /// <summary>
        /// BaseItem confines
        /// </summary>
        public virtual void SetConfines()
        {
            Prototype parent = GetParent();
            if (parent == null)
                return;
            _confines_x_0 = parent.GetX() + parent.GetPadding().Left;
            _confines_x_1 = parent.GetX() + parent.GetWidth() - parent.GetPadding().Right;
            _confines_y_0 = parent.GetY() + parent.GetPadding().Top;
            _confines_y_1 = parent.GetY() + parent.GetHeight() - parent.GetPadding().Bottom;
        }
        public void SetConfines(int x0, int x1, int y0, int y1)
        {
            _confines_x_0 = x0;
            _confines_x_1 = x1;
            _confines_y_0 = y0;
            _confines_y_1 = y1;
        }
        public int[] GetConfines()
        {
            return new int[] { _confines_x_0, _confines_x_1, _confines_y_0, _confines_y_1 };
        }

        public virtual void Release() { }
    }
}
