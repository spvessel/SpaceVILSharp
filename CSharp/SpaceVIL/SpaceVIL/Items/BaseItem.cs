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

        internal void AddChildren(IBaseItem item)
        {
            Prototype itemParent = item.GetParent();
            if (itemParent != null)
                itemParent.RemoveItem(item);

            item.SetParent((this as VisualItem).prototype);
            itemParent = item.GetParent();

            if (itemParent is IFreeLayout)
            {
                return;
            }

            if (itemParent is IVLayout)
            {
                AddEvents(item, GeometryEventType.ResizeWidth, GeometryEventType.MovedX);
                return;
            }
            if (itemParent is IHLayout)
            {
                AddEvents(item, GeometryEventType.ResizeHeight, GeometryEventType.MovedY);
                return;
            }

            AddEvents(item, GeometryEventType.ResizeWidth, GeometryEventType.ResizeHeight,
                GeometryEventType.MovedX, GeometryEventType.MovedY);
        }

        private void AddEvents(IBaseItem listener, params GeometryEventType[] types)
        {
            foreach (GeometryEventType t in types)
                AddEventListener(t, listener);
            BaseItemStatics.CastToUpdateBehavior(listener);
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
            parent.RemoveEventListener(GeometryEventType.MovedX, this);
            parent.RemoveEventListener(GeometryEventType.MovedY, this);
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
            BaseItemStatics.UpdateAllLayout(this);
        }
        public void SetMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            SetMargin(new Indents(left, top, right, bottom));
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
        public virtual void MakeShape() { }

        internal List<float[]> UpdateShape()
        {
            return BaseItemStatics.UpdateShape(this);
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
            ItemsRefreshManager.SetRefreshShape(this);
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
            ItemsRefreshManager.SetRefreshShape(this);
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
        public Core.Size GetSize()
        {
            return _itemGeometry.GetSize();
        }
        public Core.Size GetMinSize()
        {
            return new Core.Size(_itemGeometry.GetMinWidth(), _itemGeometry.GetMinHeight());
        }
        public Core.Size GetMaxSize()
        {
            return new Core.Size(_itemGeometry.GetMaxWidth(), _itemGeometry.GetMaxHeight());
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
            BaseItemStatics.UpdateAllLayout(this);
        }
        public void SetAlignment(params ItemAlignment[] alignment)
        {
            ItemAlignment common = alignment.ElementAt(0);
            if (alignment.Length > 1)
                for (int i = 1; i < alignment.Length; i++)
                    common |= alignment.ElementAt(i);
            SetAlignment(common);
        }

        public ItemAlignment GetAlignment()
        {
            return _itemBehavior.GetAlignment();
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
                ItemsRefreshManager.SetRefreshShape(this);

                VisualItem vItem = this as VisualItem;
                Prototype protoItem = null;
                if (vItem != null)
                    protoItem = vItem.prototype;
                IFloating floatingItem = protoItem as IFloating;
                if (floatingItem != null)
                {
                    if (policy == SizePolicy.Expand)
                        ItemsLayoutBox.SubscribeWindowSizeMonitoring(protoItem, GeometryEventType.ResizeWidth);
                    else
                        ItemsLayoutBox.UnsubscribeWindowSizeMonitoring(protoItem, GeometryEventType.ResizeWidth);
                    UpdateGeometry();
                }
                BaseItemStatics.UpdateAllLayout(this);
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
                ItemsRefreshManager.SetRefreshShape(this);

                VisualItem vItem = this as VisualItem;
                Prototype protoItem = null;
                if (vItem != null)
                    protoItem = vItem.prototype;
                IFloating floatingItem = protoItem as IFloating;
                if (floatingItem != null)
                {
                    if (policy == SizePolicy.Expand)
                        ItemsLayoutBox.SubscribeWindowSizeMonitoring(this, GeometryEventType.ResizeHeight);
                    else
                        ItemsLayoutBox.UnsubscribeWindowSizeMonitoring(this, GeometryEventType.ResizeHeight);

                    UpdateGeometry();
                }
                BaseItemStatics.UpdateAllLayout(this);
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
            BaseItemStatics.UpdateGeometryAttr(this, type, value);
        }

        internal virtual void UpdateGeometry()
        {
            BaseItemStatics.UpdateGeometry(this);
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

        private int _xExtension = 0;
        private int _yExtension = 0;
        public int[] GetShadowExtension()
        {
            return new int[] { _xExtension, _yExtension };
        }
        public void SetShadowExtension(int wExtension, int hExtension)
        {
            _xExtension = wExtension;
            _yExtension = hExtension;
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
        protected internal int[] GetConfines()
        {
            return new int[] { _confines_x_0, _confines_x_1, _confines_y_0, _confines_y_1 };
        }

        public virtual void Release() { }
    }
}
