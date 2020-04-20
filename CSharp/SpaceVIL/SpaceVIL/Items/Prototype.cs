using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// The Prototype is an abstract implementation of IBaseItem for complex interactive items.
    /// <para/> Contains all the necessary methods for rendering objects and interacting with them.
    /// <para/> Examples of subclasses: SpaceVIL.ButtonCore, SpaceVIL.TextEdit, SpaceVIL.ListBox and etc.
    /// </summary>
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

        /// <summary>
        /// Default constructor of Prototype class.
        /// </summary>
        public Prototype()
        {
            _core.SetItemName("VisualItem_" + count);
            count++;
            _core.prototype = this;
        }

        internal EventCommonMethodState EventFocusGet;

        internal EventCommonMethodState EventFocusLost;

        /// <summary>
        /// Event that is invoked when an item is resizing.
        /// </summary>
        public EventCommonMethodState EventResize;

        /// <summary>
        /// Event that is invoked when an item is destroyed (removed).
        /// </summary>
        public EventCommonMethodState EventDestroy;

        /// <summary>
        /// Event that is invoked when mouse cursor enters inside an item area.
        /// </summary>
        public EventMouseMethodState EventMouseHover;

        /// <summary>
        /// Event that is invoked when mouse cursor leaves inside an item area.
        /// </summary>
        public EventMouseMethodState EventMouseLeave;

        /// <summary>
        /// Event that is invoked when mouse click (release) on an item.
        /// </summary>
        public EventMouseMethodState EventMouseClick;

        /// <summary>
        /// Event that is invoked when mouse double click on an item.
        /// </summary>
        public EventMouseMethodState EventMouseDoubleClick;

        /// <summary>
        /// Event that is invoked when mouse press on an item.
        /// </summary>
        public EventMouseMethodState EventMousePress;

        /// <summary>
        /// Event that is invoked when mouse drag on an item.
        /// </summary>
        public EventMouseMethodState EventMouseDrag;

        /// <summary>
        /// Event that is invoked when mouse move on an item.
        /// </summary>
        public EventMouseMethodState EventMouseMove;

        /// <summary>
        /// Event that is invoked when mouse drop on an item.
        /// </summary>
        public EventMouseMethodState EventMouseDrop;

        /// <summary>
        /// Event that is invoked when mouse wheel scrolls up on an item.
        /// </summary>
        public EventMouseMethodState EventScrollUp;

        /// <summary>
        /// Event that is invoked when mouse wheel scrolls down on an item.
        /// </summary>
        public EventMouseMethodState EventScrollDown;

        /// <summary>
        /// Event that is invoked when key of keyboard is pressed.
        /// </summary>
        public EventKeyMethodState EventKeyPress;

        /// <summary>
        /// Event that is invoked when key of keyboard is released.
        /// </summary>
        public EventKeyMethodState EventKeyRelease;

        /// <summary>
        /// Event that is invoked when typing text on the keyboard.
        /// </summary>
        public EventInputTextMethodState EventTextInput;

        internal void FreeEvents()
        {
            // Console.WriteLine("free events " + GetItemName());

            EventFocusGet = null;
            EventFocusLost = null;
            EventResize = null;
            EventDestroy = null;

            EventMouseHover = null;
            EventMouseLeave = null;
            EventMouseClick = null;
            EventMouseDoubleClick = null;
            EventMousePress = null;
            EventMouseDrag = null;
            EventMouseDrop = null;
            EventScrollUp = null;
            EventScrollDown = null;

            EventKeyPress = null;
            EventKeyRelease = null;

            EventTextInput = null;
        }
        /// <summary>
        /// Method to describe disposing item's resources if the item was removed.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public virtual void Release() { }

        /// <summary>
        /// Setting the window to which the item will belong.
        /// </summary>
        /// <param name="handler">Window as SpaceVIL.CoreWindow.</param>
        public void SetHandler(CoreWindow handler)
        {
            _core.SetHandler(handler);
        }
        /// <summary>
        /// Getting the window to which the item belongs.
        /// </summary>
        /// <returns>Window as SpaceVIL.CoreWindow.</returns>
        public CoreWindow GetHandler()
        {
            return _core.GetHandler();
        }

        /// <summary>
        /// Getting tooltip text of the item.
        /// <para/> Tooltip is hint about an item that appears 
        /// when you hold the mouse cursor over an item long enough.
        /// </summary>
        /// </summary>
        /// <returns>Tooltip text.</returns>
        public String GetToolTip()
        {
            return _core.GetToolTip();
        }
        /// <summary>
        /// Setting tooltip text of the item.
        /// <para/> Tooltip is hint about an item that appears 
        /// when you hold the mouse cursor over an item long enough.
        /// </summary>
        /// <param name="text">Tooltip text.</param>
        public void SetToolTip(String text)
        {
            _core.SetToolTip(text);
        }

        /// <summary>
        /// Getting the parent of the item.
        /// </summary>
        /// <returns>Parent as SpaceVIL.Prototype 
        /// (Prototype is container and can contains children).</returns>
        public Prototype GetParent()
        {
            return _core.GetParent();
        }
        /// <summary>
        /// Setting the parent of the item.
        /// </summary>
        /// <param name="parent">Parent as SpaceVIL.Prototype 
        /// (Prototype is container and can contains children).</param>
        public void SetParent(Prototype parent)
        {
            _core.SetParent(parent);
        }

        /// <summary>
        /// Getting indents between children of a container type item.
        /// </summary>
        /// <returns>Indents between children as SpaceVIL.Decorations.Spacing.</returns>
        public Spacing GetSpacing()
        {
            return _core.GetSpacing();
        }
        /// <summary>
        /// Setting indents between children of a container type item.
        /// </summary>
        /// <param name="spacing">Spacing as SpaceVIL.Decorations.Spacing.</param>
        public void SetSpacing(Spacing spacing)
        {
            _core.SetSpacing(spacing);
        }
        /// <summary>
        /// Setting indents between children of a container type item.
        /// </summary>
        /// <param name="horizontal">Horizontal indent. Default: 0.</param>
        /// <param name="vertical">Vertical indent. Default: 0.</param>
        public void SetSpacing(int horizontal = 0, int vertical = 0)
        {
            _core.SetSpacing(horizontal, vertical);
        }

        /// <summary>
        /// Getting indents of an item for offset its children.
        /// </summary>
        /// <returns>Padding indents as SpaceVIL.Decorations.Indents.</returns>
        public Indents GetPadding()
        {
            return _core.GetPadding();
        }
        /// <summary>
        /// Setting indents of an item to offset its children.
        /// </summary>
        /// <param name="padding">Padding indents as SpaceVIL.Decorations.Indents.</param>
        public void SetPadding(Indents padding)
        {
            _core.SetPadding(padding);
        }
        /// <summary>
        /// Setting indents of an item to offset its children.
        /// </summary>
        /// <param name="left">Indent on the left.</param>
        /// <param name="top">Indent on the top.</param>
        /// <param name="right">Indent on the right.</param>
        /// <param name="bottom">Indent on the bottom.</param>
        public void SetPadding(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            _core.SetPadding(left, top, right, bottom);
        }
        /// <summary>
        /// Getting the indents of an item to offset itself relative to its container.
        /// </summary>
        /// <returns>Margin as SpaceVIL.Decorations.Indents.</returns>
        public Indents GetMargin()
        {
            return _core.GetMargin();
        }
        /// <summary>
        /// Setting the indents of an item to offset itself relative to its container.
        /// </summary>
        /// <param name="margin">Margin as SpaceVIL.Decorations.Indents.</param>
        public void SetMargin(Indents margin)
        {
            _core.SetMargin(margin);
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
            _core.SetMargin(left, top, right, bottom);
        }

        /// <summary>
        /// Setting border of an item's shape. Border consist of corner radiuses, thickness and color.
        /// </summary>
        /// <param name="border">Border as SpaceVIL.Decorations.Border.</param>
        public void SetBorder(Border border)
        {
            _core.SetBorder(border);
        }

        /// <summary>
        /// Setting the border color of an item's shape.
        /// </summary>
        /// <param name="fill">Border color as System.Drawing.Color.</param>
        public void SetBorderFill(Color fill)
        {
            _core.SetBorderFill(fill);
        }
        /// <summary>
        /// Getting the border color oa an item's shape.
        /// </summary>
        /// <returns>Border color as System.Drawing.Color.</returns>
        public Color GetBorderFill()
        {
            return _core.GetBorderFill();
        }
        /// <summary>
        /// Setting the border color of an item's shape in byte RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        /// <param name="a">Alpha color component. Range: (0 - 255)</param>
        public void SetBorderFill(int r, int g, int b, int a = 255)
        {
            _core.SetBorderFill(r, g, b, a);
        }
        /// <summary>
        /// Setting the border color of an item's shape in float RGBA format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        /// <param name="a">Alpha color component. Range: (0.0f - 1.0f)</param>
        public void SetBorderFill(float r, float g, float b, float a = 1.0f)
        {
            _core.SetBorderFill(r, g, b, a);
        }

        /// <summary>
        /// Setting radius of the border's corners.
        /// </summary>
        /// <param name="radius">Radiuses of the border's corners as SpaceVIL.Decorations.CornerRadius.</param>
        public void SetBorderRadius(CornerRadius radius)
        {
            _core.SetBorderRadius(radius);
        }
        /// <summary>
        /// Setting border radius with the same values for each corner of the rectangle object.
        /// </summary>
        /// <param name="radius">Radius of the border's corners.</param>
        public void SetBorderRadius(int radius)
        {
            _core.SetBorderRadius(new CornerRadius(radius));
        }
        /// <summary>
        /// Getting border radiuses.
        /// </summary>
        /// <returns>Border radiuses as SpaceVIL.Decorations.CornerRadius.</returns>
        public CornerRadius GetBorderRadius()
        {
            return _core.GetBorderRadius();
        }

        /// <summary>
        /// Setting border thickness of an item's shape.
        /// </summary>
        /// <param name="thickness">Border thickness.</param>
        public void SetBorderThickness(int thickness)
        {
            _core.SetBorderThickness(thickness);
        }
        /// <summary>
        /// Getting border thickness of an item's shape.
        /// </summary>
        /// <returns>Border thickness.</returns>
        public int GetBorderThickness()
        {
            return _core.GetBorderThickness();
        }
        /// <summary>
        /// Initializing children and their attributes.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public virtual void InitElements()
        {
            _core.InitElements();
        }
        /// <summary>
        /// Getting triangles of item's shape.
        /// </summary>
        /// <returns>Points list of the shape as List of float[2] array (2D).</returns>
        public List<float[]> GetTriangles()
        {
            return _core.GetTriangles();
        }
        /// <summary>
        /// Setting triangles as item's shape.
        /// </summary>
        /// <param name="triangles">Points list of the shape as List of float[2] array (2D).</param>
        public virtual void SetTriangles(List<float[]> triangles)
        {
            _core.SetTriangles(triangles);
        }
        /// <summary>
        /// Making default item's shape. Use in conjunction with 
        /// GetTriangles() and SetTriangles() methods.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public virtual void MakeShape()
        {
            _core.MakeShape();
        }

        /// <summary>
        /// Setting background color of an item's shape.
        /// </summary>
        /// <param name="color">Background color as System.Drawing.Color.</param>
        public virtual void SetBackground(Color color)
        {
            _core.SetBackground(color);
        }
        /// <summary>
        /// Setting background color of an item's shape in byte RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0 - 255)</param>
        /// <param name="g">Green color component. Range: (0 - 255)</param>
        /// <param name="b">Blue color component. Range: (0 - 255)</param>
        public virtual void SetBackground(int r, int g, int b)
        {
            _core.SetBackground(r, g, b);
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
            _core.SetBackground(r, g, b, a);
        }
        /// <summary>
        /// Setting background color of an item in float RGB format.
        /// </summary>
        /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
        /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
        /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
        public virtual void SetBackground(float r, float g, float b)
        {
            _core.SetBackground(r, g, b);
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
            _core.SetBackground(r, g, b, a);
        }
        /// <summary>
        /// Getting background color of an item.
        /// </summary>
        /// <returns>Background color as System.Drawing.Color.</returns>
        public virtual Color GetBackground()
        {
            return _core.GetBackground();
        }

        /// <summary>
        /// Setting the name of the item.
        /// </summary>
        /// <param name="name">Item name as System.String.</param>
        public void SetItemName(string name)
        {
            _core.SetItemName(name);
        }
        /// <summary>
        /// Getting the name of the item.
        /// </summary>
        /// <returns>Item name as System.String.</returns>
        public string GetItemName()
        {
            return _core.GetItemName();
        }

        /// <summary>
        /// Setting the minimum width limit. Actual width cannot be less than this limit.
        /// </summary>
        /// <param name="width"> Minimum width limit of the item. </param>
        public void SetMinWidth(int width)
        {
            _core.SetMinWidth(width);
        }
        /// <summary>
        /// Getting the minimum width limit.
        /// </summary>
        /// <returns> Minimum width limit of the item. </returns>
        public int GetMinWidth()
        {
            return _core.GetMinWidth();
        }
        /// <summary>
        /// Setting item width. If the value is greater/less than the maximum/minimum 
        /// value of the width, then the width becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="width"> Width of the item. </param>
        public virtual void SetWidth(int width)
        {
            _core.SetWidth(width);
        }
        /// <summary>
        /// Getting item width.
        /// </summary>
        /// <returns> Width of the item. </returns>
        public virtual int GetWidth()
        {
            return _core.GetWidth();
        }
        /// <summary>
        /// Setting the maximum width limit. Actual width cannot be greater than this limit.
        /// </summary>
        /// <param name="width"> Maximum width limit of the item. </param>
        public void SetMaxWidth(int width)
        {
            _core.SetMaxWidth(width);
        }
        /// <summary>
        /// Getting the maximum width limit.
        /// </summary>
        /// <returns> Maximum width limit of the item. </returns>
        public int GetMaxWidth()
        {
            return _core.GetMaxWidth();
        }

        /// <summary>
        /// Setting the minimum height limit. Actual height cannot be less than this limit.
        /// </summary>
        /// <param name="height"> Minimum height limit of the item. </param>
        public void SetMinHeight(int height)
        {
            _core.SetMinHeight(height);
        }
        /// <summary>
        /// Getting the minimum height limit.
        /// </summary>
        /// <returns> Minimum height limit of the item. </returns>
        public int GetMinHeight()
        {
            return _core.GetMinHeight();
        }
        /// <summary>
        /// Setting item height. If the value is greater/less than the maximum/minimum 
        /// value of the height, then the height becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="height"> Height of the item. </param>
        public virtual void SetHeight(int height)
        {
            _core.SetHeight(height);
        }
        /// <summary>
        /// Getting item height.
        /// </summary>
        /// <returns> Height of the item. </returns>
        public virtual int GetHeight()
        {
            return _core.GetHeight();
        }
        /// <summary>
        /// Setting the maximum height limit. Actual height cannot be greater than this limit.
        /// </summary>
        /// <param name="height"> Maximum height limit of the item. </param>
        public void SetMaxHeight(int height)
        {
            _core.SetMaxHeight(height);
        }
        /// <summary>
        /// Getting the maximum height limit.
        /// </summary>
        /// <returns> Maximum height limit of the item. </returns>
        public int GetMaxHeight()
        {
            return _core.GetMaxHeight();
        }
        /// <summary>
        /// Setting item size (width and height).
        /// </summary>
        /// <param name="width"> Width of the item. </param>
        /// <param name="height"> Height of the item. </param>
        public virtual void SetSize(int width, int height)
        {
            _core.SetSize(width, height);
        }
        /// <summary>
        /// Getting current item size.
        /// </summary>
        /// <returns>Item size as SpaceVIL.Core.Size.</returns>
        public Core.Size GetSize()
        {
            return _core.GetSize();
        }

        /// <summary>
        /// Setting minimum item size limit (width and height limits).
        /// </summary>
        /// <param name="width"> Minimum width limit of the item. </param>
        /// <param name="height"> Minimum height limit of the item. </param>
        public void SetMinSize(int width, int height)
        {
            _core.SetMinSize(width, height);
        }
        /// <summary>
        /// Getting current item minimum size limit.
        /// </summary>
        /// <returns>Minimum item size limit as SpaceVIL.Core.Size.</returns>
        public Core.Size GetMinSize()
        {
            return _core.GetMinSize();
        }
        /// <summary>
        /// Setting maximum item size limit (width and height limits).
        /// </summary>
        /// <param name="width"> Maximum width limit of the item. </param>
        /// <param name="height"> Maximum height limit of the item. </param>
        public void SetMaxSize(int width, int height)
        {
            _core.SetMaxSize(width, height);
        }
        /// <summary>
        /// Getting current item maximum size limit.
        /// </summary>
        /// <returns>Minimum item size limit as SpaceVIL.Core.Size.</returns>
        public Core.Size GetMaxSize()
        {
            return _core.GetMaxSize();
        }

        /// <summary>
        /// Setting an alignment of an item's shape relative to its container. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Alignment as SpaceVIL.Core.ItemAlignment.</param>
        public void SetAlignment(ItemAlignment alignment)
        {
            _core.SetAlignment(alignment);
        }
        /// <summary>
        /// Setting an alignment of an item's shape relative to its container. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
        public void SetAlignment(params ItemAlignment[] alignment)
        {
            _core.SetAlignment(alignment);
        }
        /// <summary>
        /// Getting an alignment of an item's shape relative to its container. 
        /// </summary>
        /// <returns>Alignment as SpaceVIL.Core.ItemAlignment.</returns>
        public ItemAlignment GetAlignment()
        {
            return _core.GetAlignment();
        }

        /// <summary>
        /// Setting the size policy of an item's shape. 
        /// Can be Fixed (shape not changes its size) or Expand (shape is stretched to all available space).
        /// </summary>
        /// <param name="width">Width policy of an item's shape.</param>
        /// <param name="height">Height policy of an item's shape.</param>
        public void SetSizePolicy(SizePolicy width, SizePolicy height)
        {
            _core.SetSizePolicy(width, height);
        }
        /// <summary>
        /// Setting width policy of an item's shape. Can be Fixed (shape not changes its size) 
        /// or Expand (shape is stretched to all available space).
        /// </summary>
        /// <param name="policy">Width policy as SpaceVIL.Core.SizePolicy.</param>
        public void SetWidthPolicy(SizePolicy policy)
        {
            _core.SetWidthPolicy(policy);
        }
        /// <summary>
        /// Getting width policy of an item's shape.Can be Fixed (shape not changes its size) 
        /// or Expand (shape is stretched to all available space).
        /// </summary>
        /// <returns>Width policy as SpaceVIL.Core.SizePolicy.</returns>
        public SizePolicy GetWidthPolicy()
        {
            return _core.GetWidthPolicy();
        }
        /// <summary>
        /// Setting height policy of an item's shape. Can be Fixed (shape not changes its size) 
        /// or Expand (shape is stretched to all available space).
        /// </summary>
        /// <param name="policy">Height policy as SpaceVIL.Core.SizePolicy.</param>
        public void SetHeightPolicy(SizePolicy policy)
        {
            _core.SetHeightPolicy(policy);
        }
        /// <summary>
        /// Getting height policy of an item's shape.Can be Fixed (shape not changes its size) 
        /// or Expand (shape is stretched to all available space).
        /// </summary>
        /// <returns>Height policy as SpaceVIL.Core.SizePolicy.</returns>
        public SizePolicy GetHeightPolicy()
        {
            return _core.GetHeightPolicy();
        }

        /// <summary>
        /// Setting item position.
        /// </summary>
        /// <param name="x"> X position of the left-top corner. </param>
        /// <param name="y"> Y position of the left-top corner. </param>
        public virtual void SetPosition(int x, int y)
        {
            _core.SetX(x);
            _core.SetY(y);
        }
        /// <summary>
        /// Setting X coordinate of the left-top corner of a shape.
        /// </summary>
        /// <param name="x">X position of the left-top corner.</param>
        public virtual void SetX(int x)
        {
            _core.SetX(x);
        }
        /// <summary>
        /// Getting X coordinate of the left-top corner of a shape.
        /// </summary>
        /// <returns>X position of the left-top corner.</returns>
        public virtual int GetX()
        {
            return _core.GetX();
        }
        /// <summary>
        /// Setting Y coordinate of the left-top corner of a shape.
        /// </summary>
        /// <param name="y">Y position of the left-top corner.</param>
        public virtual void SetY(int y)
        {
            _core.SetY(y);
        }
        /// <summary>
        /// Getting Y coordinate of the left-top corner of a shape.
        /// </summary>
        /// <returns>Y position of the left-top corner.</returns>
        public virtual int GetY()
        {
            return _core.GetY();
        }
        /// <summary>
        /// Setting the confines of the item relative to its parent's size and position.
        /// <para/> Example: items can be partially (or completely) outside the container (example: ListBox), 
        /// in which case the part that is outside the container should not be visible and should not interact with the user.
        /// </summary>
        public virtual void SetConfines()
        {
            _core.SetConfines();
        }
        /// <summary>
        /// Setting the confines of the item relative to specified bounds.
        /// <para/> Example: items can be partially (or completely) outside the container (example: ListBox), 
        /// in which case the part that is outside the container should not be visible and should not interact with the user.
        /// </summary>
        /// <param name="x0">Left X begin position.</param>
        /// <param name="x1">Right X end position.</param>
        /// <param name="y0">Top Y begin position.</param>
        /// <param name="y1">Bottom Y end position.</param>
        public virtual void SetConfines(int x0, int x1, int y0, int y1)
        {
            _core.SetConfines(x0, x1, y0, y1);
        }

        /// <summary>
        /// Setting a style that describes the appearance of an item.
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public virtual void SetStyle(Style style)
        {
            _core.SetStyle(style);
        }
        /// <summary>
        /// Getting the core (only appearance properties without inner styles) style of an item.
        /// </summary>
        /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
        public virtual Style GetCoreStyle()
        {
            return _core.GetCoreStyle();
        }
        /// <summary>
        /// Getting the shadow visibility status of an item.
        /// </summary>
        /// <returns>True: if shadow is visible. False: if shadow is invisible.</returns>
        public bool IsShadowDrop()
        {
            return _core.IsShadowDrop();
        }
        /// <summary>
        /// Setting the shadow visibility status of an item.
        /// </summary>
        /// <param name="value">True: if shadow should be visible. 
        /// False: if shadow should be invisible.</param>
        public void SetShadowDrop(bool value)
        {
            _core.SetShadowDrop(value);
        }
        /// <summary>
        /// Setting the specified blur radius of the shadow.
        /// <para/>Default: 0.
        /// </summary>
        /// <param name="radius">Blur radius of the shadow.</param>
        public void SetShadowRadius(int radius)
        {
            _core.SetShadowRadius(radius);
        }
        /// <summary>
        /// Getting the shadow blur raduis.
        /// </summary>
        /// <returns>The blur radius of the shadow.</returns>
        public int GetShadowRadius()
        {
            return _core.GetShadowRadius();
        }
        /// <summary>
        /// Getting shadow color.
        /// </summary>
        /// <returns>Returns the shadow color as System.Drawing.Color.</returns>
        public Color GetShadowColor()
        {
            return _core.GetShadowColor();
        }
        /// <summary>
        /// Setting shadow color.
        /// </summary>
        /// <param name="color">Shadow color as System.Drawing.Color.</param>
        public void SetShadowColor(Color color)
        {
            _core.SetShadowColor(color);
        }

        /// <summary>
        /// Getting the offset of the shadow relative to the position of the item.
        /// </summary>
        /// <returns>Shadow offset as SpaceVIL.Core.Position.</returns>
        public Position GetShadowPos()
        {
            return _core.GetShadowPos();
        }
        /// <summary>
        /// Getting the values of shadow extensions in pixels.
        /// </summary>
        /// <returns>The values of shadow extensions. 
        /// 0 - width extension, 1 - height extension.</returns>
        public int[] GetShadowExtension()
        {
            return _core.GetShadowExtension();
        }
        /// <summary>
        /// Setting the values of shadow extensions in pixels.
        /// </summary>
        /// <param name="wExtension">Extension by width.</param>
        /// <param name="hExtension">Extension by height.</param>
        public void SetShadowExtension(int wExtension, int hExtension)
        {
            _core.SetShadowExtension(wExtension, hExtension);
        }

        /// <summary>
        /// Setting the shadow with specified blur radius, axis shifts, shadow color.
        /// </summary>
        /// <param name="radius">A blur radius of the shadow.</param>
        /// <param name="x">X shift of the shadow.</param>
        /// <param name="y">Y shift of the shadow.</param>
        /// <param name="color">A shadow color as System.Drawing.Color.</param>
        public void SetShadow(int radius, int x, int y, Color color)
        {
            _core.SetShadow(radius, x, y, color);
        }

        /// <summary>
        /// Adding visual state for an item. 
        /// <para/> Type can be Base, Hovered, Pressed, Toggled, Focused, Disabled.
        /// </summary>
        /// <param name="type">Type as SpaceVIL.Core.ItemStateType.</param>
        /// <param name="state">Visual state as SpaceVIL.Decorations.ItemState.</param>
        public void AddItemState(ItemStateType type, ItemState state)
        {
            _core.AddItemState(type, state);
        }

        /// <summary>
        /// Removing visual state of an item by type.
        /// <para/> Type can be Base, Hovered, Pressed, Toggled, Focused, Disabled.
        /// </summary>
        /// <param name="type">Type as SpaceVIL.Core.ItemStateType.</param>
        public void RemoveItemState(ItemStateType type)
        {
            _core.RemoveItemState(type);
        }

        /// <summary>
        /// Removing all item visual states.
        /// </summary>
        public void RemoveAllItemStates()
        {
            _core.RemoveAllItemStates();
        }

        /// <summary>
        /// Getting item visual state by its type.
        /// <para/> Type can be Base, Hovered, Pressed, Toggled, Focused, Disabled.
        /// </summary>
        /// <param name="type">Type as SpaceVIL.Core.ItemStateType.</param>
        /// <returns>Item visual state as SpaceVIL.Decorations.ItemState.</returns>
        public ItemState GetState(ItemStateType type)
        {
            return _core.GetState(type);
        }
        /// <summary>
        /// Updating Prototype's state according to its ItemStateType.
        /// </summary>
        protected virtual void UpdateState()
        {
            _core.UpdateState();
        }

        /// <summary>
        /// Inserting item to the container (this). 
        /// If the count of container elements is less than the index, 
        /// then the element is added to the end of the list.
        /// </summary>
        /// <param name="item">Child as SpaceVIL.Core.IBaseItem.</param>
        /// <param name="index">Index of insertion.</param>
        public virtual void InsertItem(IBaseItem item, Int32 index)
        {
            _core.InsertItem(item, index);
        }

        /// <summary>
        /// Adding sequence of items into the container (this).
        /// </summary>
        /// <param name="items">Sequence of items.</param>
        public virtual void AddItems(params IBaseItem[] items)
        {
            foreach (var item in items)
            {
                this.AddItem(item);
            }
        }
        /// <summary>
        /// Adding item into the container (this).
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public virtual void AddItem(IBaseItem item)
        {
            _core.AddItem(item);
        }

        /// <summary>
        /// Updating an item size or/and position.
        /// </summary>
        /// <param name="type">Type of event as SpaceVIL.Core.GeometryEventType.</param>
        /// <param name="value">Value of a property that was changed.</param>
        public void Update(GeometryEventType type, int value = 0)
        {
            _core.Update(type, value);
        }

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
            return _core.IsDrawable();
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
        public void SetDrawable(bool value)
        {
            _core.SetDrawable(value);
        }
        /// <summary>
        /// Getting the visibility status of an item. This property may used in 
        /// conjunction with the IsDrawable() property.
        /// </summary>
        /// <returns>True: if item is visible. False: if item is invisible.</returns>
        public virtual bool IsVisible()
        {
            return _core.IsVisible();
        }
        /// <summary>
        /// Setting the visibility status of an item. This property may used in 
        /// conjunction with the IsDrawable() property.
        /// </summary>
        /// <param name="value">True: if item should be visible. 
        /// False: if item should be invisible.</param>
        public virtual void SetVisible(bool value)
        {
            _core.SetVisible(value);
        }

        /// <summary>
        /// Getting boolean value to know if this item can pass further 
        /// any input events (mouse, keyboard and etc.).
        /// <para/> Tip: Need for filtering input events.
        /// </summary>
        /// <returns>True: if this item pass on any input events.
        /// False: If this item do not pass any input events.</returns>
        public virtual bool IsPassEvents()
        {
            return _core.IsPassEvents();
        }

        /// <summary>
        /// Getting boolean value to know if this item can pass further 
        /// the specified type of input events (mouse, keyboard and etc.).
        /// </summary>
        /// <param name="e">Type of input events as SpaceVIL.Core.InputEventType.</param>
        /// <returns>True: if this item pass on the specified type of input events.
        /// False: If this item do not pass the specified type of input events.</returns>
        public bool IsPassEvents(InputEventType e)
        {
            return _core.IsPassEvents(e);
        }
        /// <summary>
        /// Getting all allowed input events.
        /// </summary>
        /// <returns>Allowed input events as List&lt;SpaceVIL.Core.InputEventType&gt;</returns>
        public List<InputEventType> GetPassEvents()
        {
            return _core.GetPassEvents();
        }
        /// <summary>
        /// Getting all blocked input events.
        /// </summary>
        /// <returns>Blocked input events as List&lt;SpaceVIL.Core.InputEventType&gt;</returns>
        public List<InputEventType> GetBlockedEvents()
        {
            return _core.GetBlockedEvents();
        }

        /// <summary>
        /// Setting on or off so that this item can pass further 
        /// any input events (mouse, keyboard and etc.).
        /// </summary>
        /// <param name="value">True: if you want that this item may to pass on any input events.
        /// False: if you want that this item cannot to pass on any input events.</param>
        public void SetPassEvents(bool value)
        {
            _core.SetPassEvents(value);
        }

        /// <summary>
        /// Setting on or off so that this item can pass further 
        /// the specified type of input events (mouse, keyboard and etc.).
        /// </summary>
        /// <param name="value">True: if you want this item can pass further the specified type of input events.
        /// False: if you want this item connot pass further the specified type of input events.</param>
        /// <param name="e">Type of input events as SpaceVIL.Core.InputEventType.</param>
        public void SetPassEvents(bool value, InputEventType e)
        {
            _core.SetPassEvents(value, e);
        }
        /// <summary>
        /// Setting on or off so that this item can pass further 
        /// the specified types of input events (mouse, keyboard and etc.).
        /// </summary>
        /// <param name="value">True: if you want this item can pass further the specified types of input events.
        /// False: if you want this item connot pass further the specified types of input events.</param>
        /// <param name="events">Sequence of input event types as SpaceVIL.Core.InputEventType.</param>
        public void SetPassEvents(bool value, params InputEventType[] events)
        {
            _core.SetPassEvents(value, events);
        }

        /// <summary>
        /// Returns True if this item is disabled (non-interactive) otherwise returns False.
        /// </summary>
        /// <returns>True: this item is disabled. False: this item is enabled.</returns>
        public virtual bool IsDisabled()
        {
            return _core.IsDisabled();
        }
        /// <summary>
        /// Setting this item disabled (become non-interactive) or enabled.
        /// </summary>
        /// <param name="value">True: if you want to disable this item. 
        /// False: if you want to enable this item.</param>
        public virtual void SetDisabled(bool value)
        {
            _core.SetDisabled(value);
        }

        /// <summary>
        /// Returns True if this item is hovered otherwise returns False.
        /// </summary>
        /// <returns>True: this item is hovered. 
        /// False: this item is not hovered.</returns>
        public virtual bool IsMouseHover()
        {
            return _core.IsMouseHover();
        }
        /// <summary>
        /// Setting this item hovered (mouse cursor located within item's shape).
        /// </summary>
        /// <param name="value">True: if you want this item be hovered. 
        /// False: if you want this item be not hovered.</param>
        public virtual void SetMouseHover(bool value)
        {
            _core.SetMouseHover(value);
        }

        /// <summary>
        /// Returns True if mouse is pressed on this item (mouse cursor located within 
        /// item's shape and any of the mouse button is pressed) otherwise False.
        /// </summary>
        /// <returns>True: if mouse is pressed on this item. 
        /// False: if mouse is not pressed on this item.</returns>
        public virtual bool IsMousePressed()
        {
            return _core.IsMousePressed();
        }
        /// <summary>
        /// Setting True if you want that mouse is pressed on this item (mouse cursor located 
        /// within item's shape and any of the mouse button is pressed) otherwise False.
        /// </summary>
        /// <param name="value">True: if you want this item be mouse pressed. 
        /// False: if you want this item be not mouse pressed.</param>
        public virtual void SetMousePressed(bool value)
        {
            _core.SetMousePressed(value);
        }

        /// <summary>
        /// Item's focusable property.
        /// <para/> True: this item can get focus. False: this item cannot get focus.
        /// </summary>
        public bool IsFocusable = true;

        /// <summary>
        /// Returns True if this item gets focus otherwise False.
        /// </summary>
        /// <returns>True: if this item is focused. False: if this item is not focused.</returns>
        public virtual bool IsFocused()
        {
            return _core.IsFocused();
        }
        protected internal virtual void SetFocused(bool value)
        {
            if (IsFocusable)
            {
                _core.SetFocused(value);
            }
        }

        /// <summary>
        /// Setting focus on this item if it is focusable.
        /// </summary>
        public virtual void SetFocus()
        {
            if (IsFocusable)
                GetHandler().SetFocusedItem(this);
        }

        protected internal virtual bool GetHoverVerification(float xpos, float ypos)
        {
            return _core.GetHoverVerification(xpos, ypos);
        }

        /// <summary>
        /// Getting list of the Prototype's inner items (children).
        /// </summary>
        /// <returns>List of children as List&lt;SpaceVIL.Core.IBaseItem&gt;</returns>
        public virtual List<IBaseItem> GetItems()
        {
            return _core.GetItems();
        }

        /// <summary>
        /// Removing the specified item from container (this).
        /// </summary>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
        public virtual bool RemoveItem(IBaseItem item)
        {
            return _core.RemoveItem(item);
        }
        /// <summary>
        /// Removing all children.
        /// </summary>
        public virtual void Clear()
        {
            _core.Clear();
        }

        internal void AddEventListener(GeometryEventType type, IBaseItem listener)
        {
            _core.AddEventListener(type, listener);
        }
        internal void RemoveEventListener(GeometryEventType type, IBaseItem listener)
        {
            _core.RemoveEventListener(type, listener);
        }

        protected internal int[] GetConfines()
        {
            return _core.GetConfines();
        }

        internal ItemStateType GetCurrentStateType()
        {
            return _core.GetCurrentStateType();
        }

        protected void SetState(ItemStateType state)
        {
            _core.SetState(state);
        }

        /// <summary>
        /// Setting content for this item.
        /// <para/> Note: this method is only for sorting children 
        /// i.e. Prototype.GetItems() contains equal set of children 
        /// as input argument: List&lt;SpaceVIL.Core.IBaseItem&gt; content.
        /// If content is different this method do nothing.
        /// </summary>
        /// <param name="content">Sorted (in any way) content of this item.</param>
        public void SetContent(List<IBaseItem> content)
        {
            List<IBaseItem> oldContent = GetItems();
            if (oldContent.Count != content.Count)
                return;

            foreach (IBaseItem ibi in oldContent)
            {
                if (!content.Contains(ibi))
                    return;
            }

            _core.SetContent(content);
        }

        /// <summary>
        /// Getting the custom shape if it is set. 
        /// You can set any shape using Prototype.SetCustomFigure(Figure) 
        /// and it will replace the default rectangle shape.
        /// </summary>
        /// <returns>Custom shape as SpaceVIL.Decorations.Figure.</returns>
        public Figure IsCustomFigure()
        {
            return _core.IsCustomFigure();
        }

        /// <summary>
        /// Setting the custom shape to replace the default rectangle shape.
        /// </summary>
        /// <param name="figure">Custom shape as SpaceVIL.Decorations.Figure.</param>
        public void SetCustomFigure(Figure figure)
        {
            _core.SetCustomFigure(figure);
        }

        /// <summary>
        /// Getting the hovering rule of this item.
        /// <para/> Can be ItemHoverRule.Lazy or ItemHoverRule.Strict (see SpaceVIL.Core.ItemHoverRule).
        /// </summary>
        /// <returns>Hovering rule as SpaceVIL.Core.ItemHoverRule.</returns>
        public ItemHoverRule GetHoverRule()
        {
            return _core.HoverRule;
        }
        /// <summary>
        /// Setting the hovering rule for this item.
        /// </summary>
        /// <param name="rule">Hovering rule as SpaceVIL.Core.ItemHoverRule.</param>
        public void SetHoverRule(ItemHoverRule rule)
        {
            _core.HoverRule = rule;
        }

        private CursorImage _cursor = DefaultsService.GetDefaultCursor();
        /// <summary>
        /// Getting the mouse cursor image of this item.
        /// </summary>
        /// <returns>Mouse cursor image as SpaceVIL.Decorations.CursorImage.</returns>
        public CursorImage GetCursor()
        {
            return _cursor;
        }
        /// <summary>
        /// Setting mouse cursor image for this item from embedded cursors.
        /// </summary>
        /// <param name="type">Mouse cursor type as SpaceVIL.Core.EmbeddedCursor.</param>
        public void SetCursor(EmbeddedCursor type)
        {
            _cursor = new CursorImage(type);
        }
        /// <summary>
        /// Setting mouse cursor image for this item.
        /// </summary>
        /// <param name="cursor">Mouse cursor image as SpaceVIL.Decorations.CursorImage.</param>
        public void SetCursor(CursorImage cursor)
        {
            _cursor = cursor;
        }
        /// <summary>
        /// Creating and setting mouse cursor image for this item from specified bitmap image.
        /// </summary>
        /// <param name="bitmap">Bitmap for mouse cursor image as System.Drawing.Bitmap.</param>
        public void SetCursor(Bitmap bitmap)
        {
            _cursor = new CursorImage(bitmap);
        }
        /// <summary>
        /// Creating and setting mouse cursor image for this item from specified scaled bitmap image.
        /// </summary>
        /// <param name="bitmap">Bitmap for mouse cursor image as System.Drawing.Bitmap.</param>
        /// <param name="width">New width of mouse cursor image.</param>
        /// <param name="height">New height of mouse cursor image.</param>
        public void SetCursor(Bitmap bitmap, int width, int height)
        {
            _cursor = new CursorImage(bitmap, width, height);
        }
    }
}
