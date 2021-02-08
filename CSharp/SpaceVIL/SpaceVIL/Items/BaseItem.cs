using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// Abstract class implementation of SpaceVIL.Core.IBaseItem interface. 
    /// <para/> SpaceVIL.Core.IBaseItem is the main interface of SpaceVIL environment. 
    /// <para/> Contains all the necessary methods for rendering objects and interacting with them.
    /// </summary>
    abstract public class BaseItem : IBaseItem
    {
        internal int _confinesX0 = 0;
        internal int _confinesX1 = 0;
        internal int _confinesY0 = 0;
        internal int _confinesY1 = 0;

        private CoreWindow _handler;

        /// <summary>
        /// Setting the window to which the item will belong.
        /// </summary>
        /// <param name="handler">Window as SpaceVIL.CoreWindow.</param>
        public void SetHandler(CoreWindow handler)
        {
            _handler = handler;
        }
        /// <summary>
        /// Getting the window to which the item will belong.
        /// </summary>
        /// <returns>Window as SpaceVIL.CoreWindow.</returns>
        public CoreWindow GetHandler()
        {
            return _handler;
        }
        private Prototype _parent = null;
        /// <summary>
        /// Getting the parent of the item.
        /// </summary>
        /// <returns>Parent as SpaceVIL.Prototype 
        /// (Prototype is container and can contains children).</returns>
        public Prototype GetParent()
        {
            return _parent;
        }
        /// <summary>
        /// Setting the parent of the item.
        /// </summary>
        /// <param name="parent">Parent as SpaceVIL.Prototype 
        /// (Prototype is container and can contains children).</param>
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

        internal virtual void RemoveItemFromListeners()
        {
            Prototype parent = GetParent();
            parent.RemoveEventListener(GeometryEventType.ResizeWidth, this);
            parent.RemoveEventListener(GeometryEventType.ResizeHeight, this);
            parent.RemoveEventListener(GeometryEventType.MovedX, this);
            parent.RemoveEventListener(GeometryEventType.MovedY, this);
        }

        /// <summary>
        /// Initializing children if this BaseItem is container (SpaceVIL.Prototype).
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public virtual void InitElements() { }

        private Item _item = new Item();

        private Indents _margin = new Indents();

        /// <summary>
        /// Getting the indents of an item to offset itself relative to its container.
        /// </summary>
        /// <returns>Margin as SpaceVIL.Decorations.Indents.</returns>
        public Indents GetMargin()
        {
            return _margin;
        }
        /// <summary>
        /// Setting the indents of an item to offset itself relative to its container.
        /// </summary>
        /// <param name="margin">Margin as SpaceVIL.Decorations.Indents.</param>
        public void SetMargin(Indents margin)
        {
            _margin = margin;
            UpdateGeometry();
            BaseItemStatics.UpdateAllLayout(this);
        }
        /// <summary>
        /// Setting the indents of an item to offset itself relative to its container.
        /// </summary>
        /// <param name="left">Indent on the left.</param>
        /// <param name="top">Indent on the top.</param>
        /// <param name="right">Indent on the right.</param>
        /// <param name="bottom">Indent on the bottom.</param>
        public void SetMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            SetMargin(new Indents(left, top, right, bottom));
        }

        /// <summary>
        /// Getting triangles of item's shape.
        /// </summary>
        /// <returns>Points list of the shape as List of float[2] array (2D).</returns>
        public List<float[]> GetTriangles()
        {
            return _item.GetTriangles();
        }
        /// <summary>
        /// Setting triangles as item's shape.
        /// </summary>
        /// <param name="triangles">Points list of the shape as List of float[2] array (2D).</param>
        public virtual void SetTriangles(List<float[]> triangles)
        {
            _item.SetTriangles(triangles);
        }
        /// <summary>
        /// Making default item's shape. Use in conjunction with 
        /// GetTriangles() and SetTriangles() methods.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public virtual void MakeShape() { }

        internal List<float[]> UpdateShape()
        {
            return BaseItemStatics.UpdateShape(this);
        }

        /// <summary>
        /// Setting background color of an item's shape.
        /// </summary>
        /// <param name="color">Background color as System.Drawing.Color.</param>
        public virtual void SetBackground(Color color)
        {
            _item.SetBackground(color);
        }
        /// <summary>
        /// Setting background color of an item's shape in byte RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        public virtual void SetBackground(int r, int g, int b)
        {
            _item.SetBackground(GraphicsMathService.ColorTransform(r, g, b));
        }
        /// <summary>
        /// Setting background color of an item in byte RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        /// <param name="a">Alpha color component. Range: (0 - 255)</param>
        public virtual void SetBackground(int r, int g, int b, int a)
        {
            _item.SetBackground(GraphicsMathService.ColorTransform(r, g, b, a));
        }
        /// <summary>
        /// Setting background color of an item in float RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        public virtual void SetBackground(float r, float g, float b)
        {
            _item.SetBackground(GraphicsMathService.ColorTransform(r, g, b));
        }
        /// <summary>
        /// Setting background color of an item in float RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha color component. Range: (0.0f - 1.0f)</param>
        public virtual void SetBackground(float r, float g, float b, float a)
        {
            _item.SetBackground(GraphicsMathService.ColorTransform(r, g, b, a));
        }
        /// <summary>
        /// Getting background color of an item.
        /// </summary>
        /// <returns>Background color as System.Drawing.Color.</returns>
        public virtual Color GetBackground()
        {
            return _item.GetBackground();
        }

        /// <summary>
        /// Setting the name of the item.
        /// </summary>
        /// <param name="name">Item name as System.String.</param>
        public void SetItemName(string name)
        {
            _item.SetItemName(name);
        }
        /// <summary>
        /// Getting the name of the item.
        /// </summary>
        /// <returns>Item name as System.String.</returns>
        public string GetItemName()
        {
            return _item.GetItemName();
        }

        private bool _drawable = true;

        /// <summary>
        /// Getting the drawable (visibility) status of an item. This property used in 
        /// conjunction with the IsVisible() property.
        /// <para/> Explanation: an item can be visible and invisible, in some cases 
        /// the item can be located outside the container (example: SpaceVIL.ListBox), 
        /// and it must be invisible so as not to waste CPU / GPU resources, but in some 
        /// cases you must control the visibility of elements that are inside container 
        /// and should be invisible (example: SpaceVIL.TreeView).
        /// </summary>
        /// <returns>True: if item is drawable (visible). 
        /// False: if item is not drawable (invisible).</returns>
        public virtual bool IsDrawable()
        {
            return _drawable;
        }
        /// <summary>
        /// Setting the drawable (visibility) status of an item. This property used in 
        /// conjunction with the IsVisible() property.
        /// <para/> Explanation: an item can be visible and invisible, in some cases 
        /// the item can be located outside the container (example: SpaceVIL.ListBox), 
        /// and it must be invisible so as not to waste CPU / GPU resources, but in some 
        /// cases you must control the visibility of elements that are inside container 
        /// and should be invisible (example: SpaceVIL.TreeView).
        /// </summary>
        /// <param name="value">True: if item should be drawable (visible). 
        /// False: if item should not be drawable (invisible).</param>
        public virtual void SetDrawable(bool value)
        {
            if (_drawable == value)
                return;
            _drawable = value;
        }

        private bool _visible = true;
        /// <summary>
        /// Getting the visibility status of an item. This property may used in 
        /// conjunction with the IsDrawable() property.
        /// </summary>
        /// <returns>True: if item is visible. False: if item is invisible.</returns>
        public virtual bool IsVisible()
        {
            return _visible;
        }
        /// <summary>
        /// Setting the visibility status of an item. This property may used in 
        /// conjunction with the IsDrawable() property.
        /// </summary>
        /// <param name="value">True: if item should be visible. 
        /// False: if item should be invisible.</param>
        public virtual void SetVisible(bool value)
        {
            if (_visible == value)
                return;
            _visible = value;
        }

        //geometry
        private Geometry _itemGeometry = new Geometry();

        /// <summary>
        /// Setting the minimum width limit. Actual width cannot be less than this limit.
        /// </summary>
        /// <param name="width"> Minimum width limit of the item. </param>
        public void SetMinWidth(int width)
        {
            _itemGeometry.SetMinWidth(width);
        }
        /// <summary>
        /// Setting item width. If the value is greater/less than the maximum/minimum 
        /// value of the width, then the width becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="width"> Width of the item. </param>
        public virtual void SetWidth(int width)
        {
            _itemGeometry.SetWidth(width);
            ItemsRefreshManager.SetRefreshShape(this);
        }
        /// <summary>
        /// Setting the maximum width limit. Actual width cannot be greater than this limit.
        /// </summary>
        /// <param name="width"> Maximum width limit of the item. </param>
        public void SetMaxWidth(int width)
        {
            _itemGeometry.SetMaxWidth(width);
        }
        /// <summary>
        /// Getting the minimum width limit.
        /// </summary>
        /// <returns> Minimum width limit of the item. </returns>
        public int GetMinWidth()
        {
            return _itemGeometry.GetMinWidth();
        }
        /// <summary>
        /// Getting item width.
        /// </summary>
        /// <returns> Width of the item. </returns>
        public virtual int GetWidth()
        {
            return _itemGeometry.GetWidth();
        }
        /// <summary>
        /// Getting the maximum width limit.
        /// </summary>
        /// <returns> Maximum width limit of the item. </returns>
        public int GetMaxWidth()
        {
            return _itemGeometry.GetMaxWidth();
        }
        /// <summary>
        /// Setting the minimum height limit. Actual height cannot be less than this limit.
        /// </summary>
        /// <param name="height"> Minimum height limit of the item. </param>
        public void SetMinHeight(int height)
        {
            _itemGeometry.SetMinHeight(height);
        }
        /// <summary>
        /// Setting item height. If the value is greater/less than the maximum/minimum 
        /// value of the height, then the height becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="height"> Height of the item. </param>
        public virtual void SetHeight(int height)
        {
            _itemGeometry.SetHeight(height);
            ItemsRefreshManager.SetRefreshShape(this);
        }
        /// <summary>
        /// Setting the maximum height limit. Actual height cannot be greater than this limit.
        /// </summary>
        /// <param name="height"> Maximum height limit of the item. </param>
        public void SetMaxHeight(int height)
        {
            _itemGeometry.SetMaxHeight(height);
        }
        /// <summary>
        /// Getting the minimum height limit.
        /// </summary>
        /// <returns> Minimum height limit of the item. </returns>
        public int GetMinHeight()
        {
            return _itemGeometry.GetMinHeight();
        }
        /// <summary>
        /// Getting item height.
        /// </summary>
        /// <returns> Height of the item. </returns>
        public virtual int GetHeight()
        {
            return _itemGeometry.GetHeight();
        }
        /// <summary>
        /// Getting the maximum height limit.
        /// </summary>
        /// <returns> Maximum height limit of the item. </returns>
        public int GetMaxHeight()
        {
            return _itemGeometry.GetMaxHeight();
        }

        /// <summary>
        /// Setting item size (width and height).
        /// </summary>
        /// <param name="width"> Width of the item. </param>
        /// <param name="height"> Height of the item. </param>
        public void SetSize(int width, int height)
        {
            SetWidth(width);
            SetHeight(height);
        }
        /// <summary>
        /// Setting minimum item size limit (width and height limits).
        /// </summary>
        /// <param name="width"> Minimum width limit of the item. </param>
        /// <param name="height"> Minimum height limit of the item. </param>
        public void SetMinSize(int width, int height)
        {
            SetMinWidth(width);
            SetMinHeight(height);
        }
        /// <summary>
        /// Setting maximum item size limit (width and height limits).
        /// </summary>
        /// <param name="width"> Maximum width limit of the item. </param>
        /// <param name="height"> Maximum height limit of the item. </param>
        public void SetMaxSize(int width, int height)
        {
            SetMaxWidth(width);
            SetMaxHeight(height);
        }
        /// <summary>
        /// Getting current item size.
        /// </summary>
        /// <returns>Item size as SpaceVIL.Core.Size.</returns>
        public Core.Size GetSize()
        {
            return _itemGeometry.GetSize();
        }
        /// <summary>
        /// Getting current item minimum size limit.
        /// </summary>
        /// <returns>Minimum item size limit as SpaceVIL.Core.Size.</returns>
        public Core.Size GetMinSize()
        {
            return new Core.Size(_itemGeometry.GetMinWidth(), _itemGeometry.GetMinHeight());
        }
        /// <summary>
        /// Getting current item maximum size limit.
        /// </summary>
        /// <returns>Minimum item size limit as SpaceVIL.Core.Size.</returns>
        public Core.Size GetMaxSize()
        {
            return new Core.Size(_itemGeometry.GetMaxWidth(), _itemGeometry.GetMaxHeight());
        }

        //behavior
        private Behavior _itemBehavior = new Behavior();

        /// <summary>
        /// Setting an alignment of an item's shape relative to its container. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetAlignment(ItemAlignment alignment)
        {
            _itemBehavior.SetAlignment(alignment);
            UpdateGeometry();
            BaseItemStatics.UpdateAllLayout(this);
        }
        /// <summary>
        /// Setting an alignment of an item's shape relative to its container. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
        public void SetAlignment(params ItemAlignment[] alignment)
        {
            // ItemAlignment common = alignment.ElementAt(0);
            // if (alignment.Length > 1)
            //     for (int i = 1; i < alignment.Length; i++)
            //         common |= alignment.ElementAt(i);
            // SetAlignment(common);
            SetAlignment(BaseItemStatics.ComposeFlags(alignment));
        }
        /// <summary>
        /// Getting an alignment of an item's shape relative to its container. 
        /// </summary>
        /// <returns>Alignment as SpaceVIL.Core.ItemAlignment.</returns>
        public ItemAlignment GetAlignment()
        {
            return _itemBehavior.GetAlignment();
        }

        /// <summary>
        /// Setting the size policy of an item's shape. 
        /// Can be Fixed (shape not changes its size) or Expand (shape is stretched to all available space).
        /// </summary>
        /// <param name="width">Width policy of an item's shape as SpaceVIL.Core.SizePolicy.</param>
        /// <param name="height">Height policy of an item's shape as SpaceVIL.Core.SizePolicy.</param>
        public void SetSizePolicy(SizePolicy width, SizePolicy height)
        {
            SetWidthPolicy(width);
            SetHeightPolicy(height);
        }
        /// <summary>
        /// Setting width policy of an item's shape. Can be Fixed (shape not changes its size) 
        /// or Expand (shape is stretched to all available space).
        /// </summary>
        /// <param name="policy">Width policy as SpaceVIL.Core.SizePolicy.</param>
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
        /// <summary>
        /// Getting width policy of an item's shape.Can be Fixed (shape not changes its size) 
        /// or Expand (shape is stretched to all available space).
        /// </summary>
        /// <returns>Width policy as SpaceVIL.Core.SizePolicy.</returns>
        public SizePolicy GetWidthPolicy()
        {
            return _itemBehavior.GetWidthPolicy();
        }
        /// <summary>
        /// Setting height policy of an item's shape. Can be Fixed (shape not changes its size) 
        /// or Expand (shape is stretched to all available space).
        /// </summary>
        /// <param name="policy">Height policy as SpaceVIL.Core.SizePolicy.</param>
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
        /// <summary>
        /// Getting height policy of an item's shape.Can be Fixed (shape not changes its size) 
        /// or Expand (shape is stretched to all available space).
        /// </summary>
        /// <returns>Height policy as SpaceVIL.Core.SizePolicy.</returns>
        public SizePolicy GetHeightPolicy()
        {
            return _itemBehavior.GetHeightPolicy();
        }

        //position
        private Position _itemPosition = new Position();

        /// <summary>
        /// Setting X coordinate of the left-top corner of a shape.
        /// </summary>
        /// <param name="x">X position of the left-top corner.</param>
        public virtual void SetX(int x)
        {
            _itemPosition.SetX(x);
        }
        /// <summary>
        /// Getting X coordinate of the left-top corner of a shape.
        /// </summary>
        /// <returns>X position of the left-top corner.</returns>
        public virtual int GetX()
        {
            return _itemPosition.GetX();
        }
        /// <summary>
        /// Setting Y coordinate of the left-top corner of a shape.
        /// </summary>
        /// <param name="y">Y position of the left-top corner.</param>
        public virtual void SetY(int y)
        {
            _itemPosition.SetY(y);
        }
        /// <summary>
        /// Getting Y coordinate of the left-top corner of a shape.
        /// </summary>
        /// <returns>Y position of the left-top corner.</returns>
        public virtual int GetY()
        {
            return _itemPosition.GetY();
        }

        /// <summary>
        /// Updating an item size or/and position.
        /// </summary>
        /// <param name="type">Type of event as SpaceVIL.Core.GeometryEventType.</param>
        /// <param name="value">Value of a property that was changed.</param>
        public void Update(GeometryEventType type, int value = 0)
        {
            BaseItemStatics.UpdateGeometryAttr(this, type, value);
        }

        internal virtual void UpdateGeometry()
        {
            BaseItemStatics.UpdateGeometry(this);
        }

        /// <summary>
        /// Setting a style that describes the appearance of an item.
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public virtual void SetStyle(Style style) { }
        /// <summary>
        /// Getting the core (only appearance properties without inner styles) style of an item.
        /// </summary>
        /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
        public abstract Style GetCoreStyle();

        /// <summary>
        /// Hovering rule propetry of this item. 
        /// <para/> Can be ItemHoverRule.Lazy or ItemHoverRule.Strict (see SpaceVIL.Core.ItemHoverRule).
        /// </summary>
        public ItemHoverRule HoverRule = ItemHoverRule.Lazy;

        //update
        /// <summary>
        /// Setting the confines of the item relative to its parent's size and position.
        /// <para/> Example: items can be partially (or completely) outside the container (example: ListBox), 
        /// in which case the part that is outside the container should not be visible and should not interact with the user.
        /// </summary>
        public virtual void SetConfines()
        {
            Prototype parent = GetParent();
            if (parent == null)
                return;
            _confinesX0 = parent.GetX() + parent.GetPadding().Left;
            _confinesX1 = parent.GetX() + parent.GetWidth() - parent.GetPadding().Right;
            _confinesY0 = parent.GetY() + parent.GetPadding().Top;
            _confinesY1 = parent.GetY() + parent.GetHeight() - parent.GetPadding().Bottom;
        }
        /// <summary>
        /// Setting the confines of the item relative to specified bounds.
        /// <para/> Example: items can be partially (or completely) outside the container (example: ListBox), 
        /// in which case the part that is outside the container should not be visible and should not 
        /// interact with the user.
        /// </summary>
        /// <param name="x0">Left X begin position.</param>
        /// <param name="x1">Right X end position.</param>
        /// <param name="y0">Top Y begin position.</param>
        /// <param name="y1">Bottom Y end position.</param>
        public void SetConfines(int x0, int x1, int y0, int y1)
        {
            _confinesX0 = x0;
            _confinesX1 = x1;
            _confinesY0 = y0;
            _confinesY1 = y1;
        }
        protected internal int[] GetConfines()
        {
            return new int[] { _confinesX0, _confinesX1, _confinesY0, _confinesY1 };
        }

        /// <summary>
        /// Method to describe disposing item's resources if the item was removed.
        /// </summary>
        public virtual void Release() { }

        private IAppearanceExtension _effects = new Effects();

        /// <summary>
        /// Gettting access to manage visual effects of the item.
        /// </summary>
        /// <returns>Implementation of an SpaceVIL.Core.IAppearanceExtension interface.</returns>
        public IAppearanceExtension Effects()
        {
            return _effects;
        }
    }
}
