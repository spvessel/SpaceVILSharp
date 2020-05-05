using System;
using System.Collections.Generic;
using System.Linq;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;

namespace SpaceVIL.Decorations
{
   /// <summary>
   /// Style is a class that describes the appearance of an element. Can contains Styles for inner items.
   /// </summary>
   public class Style
   {
      private Dictionary<String, Style> _innerStyles = new Dictionary<String, Style>();

      /// <summary>
      /// Background color of an item's shape. Attention: this property is required.
      /// <para/> This property is System.Drawing.Color.
      /// </summary>
      public Color Background;

      /// <summary>
      /// Color of an item's text. Can be used only if the item contains text and in this case this property is required.
      /// <para/> This property is System.Drawing.Color.
      /// </summary>
      public Color Foreground;

      /// <summary>
      /// Font of an item's text. Can be used only if the item contains text and in this case this property is required.
      /// <para/> This property is System.Drawing.Font.
      /// </summary>
      public Font Font = null;

      /// <summary>
      /// Width policy of an item's shape. Can be Fixed (shape not changes its size) or Expand (shape is stretched to all available space). Attention: this property is required.
      /// <para/> This property is SpaceVIL.Core.SizePolicy.
      /// </summary>
      public SizePolicy WidthPolicy;

      /// <summary>
      /// Height policy of an item's shape. Can be Fixed (shape not changes its size) or Expand (shape is stretched to all available space). Attention: this property is required.
      /// <para/> This property is SpaceVIL.Core.SizePolicy.
      /// </summary>
      public SizePolicy HeightPolicy;

      /// <summary>
      /// Width of an item's shape.
      /// </summary>
      public int Width;

      /// <summary>
      /// Minimum width of an item's shape (shape cannot be smaller this value).
      /// <para/> Default: 0.
      /// </summary>
      public int MinWidth;

      /// <summary>
      /// Maximum width of an item's shape (shape cannot be bigger this value).
      /// <para/> Default: 32767.
      /// </summary>
      public int MaxWidth;

      /// <summary>
      /// Height of an item's shape.
      /// </summary>
      public int Height;

      /// <summary>
      /// Minimum height of an item's shape (shape cannot be smaller this value).
      /// <para/> Default: 0.
      /// </summary>
      public int MinHeight;

      /// <summary>
      /// Maximum height of an item's shape (shape cannot be bigget this value).
      /// <para/> Default: 32767.
      /// </summary>
      public int MaxHeight;

      /// <summary>
      /// Alignment of an item's shape relative to its container. 
      /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
      /// Attention: this property is required.
      /// <para/> This property is SpaceVIL.Core.ItemAlignment.
      /// </summary>
      public ItemAlignment Alignment;

      /// <summary>
      /// Alignment of an item's text. 
      /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
      /// Can be used only if the item contains text and in this case this property is required.
      /// <para/> This property is SpaceVIL.Core.ItemAlignment.
      /// </summary>
      public ItemAlignment TextAlignment;

      /// <summary>
      /// X axis position of left-top cornet of an item's shape. This property itself is mostly ignored. Used only when creating container-type items or with SpaceVIL.FreeArea.
      /// </summary>
      public int X;

      /// <summary>
      /// Y axis position of left-top cornet of an item's shape. This property itself is mostly ignored. Used only when creating container-type items or with SpaceVIL.FreeArea.
      /// </summary>
      public int Y;

      private Dictionary<ItemStateType, ItemState> _itemStates = new Dictionary<ItemStateType, ItemState>();

      /// <summary>
      /// Indents of an item to offset its children. Attention: this property is required.
      /// <para/> This property is SpaceVIL.Decorations.Indents.
      /// </summary>
      public Indents Padding;

      /// <summary>
      /// Indents between children of a container type item. It is used mainly in containers.
      /// <para/> This property is SpaceVIL.Decorations.Spacing.
      /// </summary>
      public Spacing Spacing;

      /// <summary>
      /// Indents of an item to offset itself relative to its container. Attention: this property is required.
      /// <para/> This property is SpaceVIL.Decorations.Indents.
      /// </summary>
      public Indents Margin;

      /// <summary>
      /// Radiuses to round the rectangular shape of the item.
      /// </summary>
      public CornerRadius BorderRadius = new CornerRadius();

      /// <summary>
      /// Thickness of an item's border. 
      /// <para/> Default: 0.
      /// </summary>
      public int BorderThickness = 0;

      /// <summary>
      /// Color of an item's border. 
      /// <para/> This property is System.Drawing.Color.
      /// </summary>
      public Color BorderFill;

      /// <summary>
      /// A form of an item's shape. If not assigned, the shape is rectangular.
      /// <para/> Format: System.Collections.Generic.List&lt;System.Single[]&gt;.
      /// </summary>
      public List<float[]> Shape;

      /// <summary>
      /// A flag that determines if the shape of an item can be changed or not.
      /// <para/>True: if shape can not be resized. False: if shape can be resised. Default: False.
      /// </summary>
      public bool IsFixedShape = false;

      /// <summary>
      /// A storage of shapes for future use. Note: not supported in the current version!
      /// <para/> Format: System.Collections.Generic.List&lt;SpaceVIL.Core.IBaseItem&gt;.
      /// </summary>
      public List<IBaseItem> InnerShapes;

      /// <summary>
      /// Blur radius of a shadow.
      /// <para/> Min value: 0. Max value: 10. Default: 0.
      /// </summary>
      public int ShadowRadius;

      /// <summary>
      /// X shift of a shadow.
      /// </summary>
      public int ShadowXOffset;

      /// <summary>
      /// Y shift of a shadow.
      /// </summary>
      public int ShadowYOffset;

      /// <summary>
      /// Drop shadow flag. True: allow shadow dropping. False: not allow shadow dropping.
      /// <para/> Default: False.
      /// </summary>
      public bool IsShadowDrop = false;

      /// <summary>
      /// Color of a shadow.
      /// <para/> This property is System.Drawing.Color.
      /// </summary>
      public Color ShadowColor;

      /// <summary>
      /// A flag that determines if an item is visible or not.
      /// <para/> True: if visible. False: if not visible. Default: True.
      /// </summary>
      public bool IsVisible;

      /// <summary>
      /// Constructs a default Style. 
      /// </summary>
      public Style()
      {
         IsVisible = true;
         MaxWidth = SpaceVILConstants.SizeMaxValue;
         MaxHeight = SpaceVILConstants.SizeMaxValue;
         Alignment = ItemAlignment.Top | ItemAlignment.Left;
      }

      /// <summary>
      /// Setting this style for all items in sequence.
      /// </summary>
      /// <param name="items">A sequence of items that are SpaceVIL.Core.IBaseItem.</param>
      public void SetStyle(params IBaseItem[] items)
      {
         foreach (IBaseItem item in items)
         {
            item.SetStyle(this);
         }
      }

      /// <summary>
      /// Setting size of an item's shape.
      /// </summary>
      /// <param name="width">Width of a shape.</param>
      /// <param name="height">Height of a shape.</param>
      public void SetSize(int width, int height)
      {
         Width = width;
         Height = height;
      }

      /// <summary>
      /// Setting minimum size of an item's shape (shape can not be smaller than specified width and height).
      /// </summary>
      /// <param name="width">Minimum width of a shape.</param>
      /// <param name="height">Minimum height of a shape.</param>
      public void SetMinSize(int width, int height)
      {
         MinWidth = width;
         MinHeight = height;
      }

      /// <summary>
      /// Setting maximim size of an item's shape (shape can not be bigger than specified width and height).
      /// </summary>
      /// <param name="width">Maximim width of a shape.</param>
      /// <param name="height">Maximim height of a shape.</param>
      public void SetMaxSize(int width, int height)
      {
         MaxWidth = width;
         MaxHeight = height;
      }

      /// <summary>
      /// Setting the size policy of an item's shape. 
      /// Can be Fixed (shape not changes its size) or Expand (shape is stretched to all available space).
      /// </summary>
      /// <param name="widthPolicy">Width policy of an item's shape.</param>
      /// <param name="heightPolicy">Height policy of an item's shape.</param>
      public void SetSizePolicy(SizePolicy widthPolicy, SizePolicy heightPolicy)
      {
         WidthPolicy = widthPolicy;
         HeightPolicy = heightPolicy;
      }

      /// <summary>
      /// Setting background color of an item's shape in byte RGB format.
      /// </summary>
      /// <param name="r">Red color component. Range: (0 - 255)</param>
      /// <param name="g">Green color component. Range: (0 - 255)</param>
      /// <param name="b">Blue color component. Range: (0 - 255)</param>
      public void SetBackground(int r, int g, int b)
      {
         Background = GraphicsMathService.ColorTransform(r, g, b);
      }

      /// <summary>
      /// Setting background color of an item's shape in byte RGBA format.
      /// </summary>
      /// <param name="r">Red color component. Range: (0 - 255)</param>
      /// <param name="g">Green color component. Range: (0 - 255)</param>
      /// <param name="b">Blue color component. Range: (0 - 255)</param>
      /// <param name="a">Alpha color component. Range: (0 - 255)</param>
      public void SetBackground(int r, int g, int b, int a)
      {
         Background = GraphicsMathService.ColorTransform(r, g, b, a);
      }

      /// <summary>
      /// Setting background color of an item's shape in float RGB format.
      /// </summary>
      /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
      /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
      /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
      public void SetBackground(float r, float g, float b)
      {
         Background = GraphicsMathService.ColorTransform(r, g, b);
      }
      /// <summary>
      /// Setting background color of an item's shape in float RGBA format.
      /// </summary>
      /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
      /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
      /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
      /// <param name="a">Alpha color component. Range: (0.0f - 1.0f)</param>
      public void SetBackground(float r, float g, float b, float a)
      {
         Background = GraphicsMathService.ColorTransform(r, g, b, a);
      }

      /// <summary>
      /// Setting text color of an item in byte RGB format.
      /// </summary>
      /// <param name="r">Red color component. Range: (0 - 255)</param>
      /// <param name="g">Green color component. Range: (0 - 255)</param>
      /// <param name="b">Blue color component. Range: (0 - 255)</param>
      public void SetForeground(int r, int g, int b)
      {
         Foreground = GraphicsMathService.ColorTransform(r, g, b);
      }
      /// <summary>
      /// Setting text color of an item in byte RGBA format.
      /// </summary>
      /// <param name="r">Red color component. Range: (0 - 255)</param>
      /// <param name="g">Green color component. Range: (0 - 255)</param>
      /// <param name="b">Blue color component. Range: (0 - 255)</param>
      /// <param name="a">Alpha color component. Range: (0 - 255)</param>
      public void SetForeground(int r, int g, int b, int a)
      {
         Foreground = GraphicsMathService.ColorTransform(r, g, b, a);
      }
      /// <summary>
      /// Setting text color of an item in float RGB format.
      /// </summary>
      /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
      /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
      /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
      public void SetForeground(float r, float g, float b)
      {
         Foreground = GraphicsMathService.ColorTransform(r, g, b);
      }
      /// <summary>
      /// Setting text color of an item in float RGBA format.
      /// </summary>
      /// <param name="r">Red color component. Range: (0.0f - 1.0f)</param>
      /// <param name="g">Green color component. Range: (0.0f - 1.0f)</param>
      /// <param name="b">Blue color component. Range: (0.0f - 1.0f)</param>
      /// <param name="a">Alpha color component. Range: (0.0f - 1.0f)</param>
      public void SetForeground(float r, float g, float b, float a)
      {
         Foreground = GraphicsMathService.ColorTransform(r, g, b, a);
      }

      /// <summary>
      /// Setting indents of an item to offset its children.
      /// </summary>
      /// <param name="padding">Padding indents as SpaceVIL.Decorations.Indents.</param>
      public void SetPadding(Indents padding)
      {
         Padding = padding;
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
         Padding.Left = left;
         Padding.Top = top;
         Padding.Right = right;
         Padding.Bottom = bottom;
      }

      /// <summary>
      /// Setting indents of an item to offset itself relative to its container.
      /// </summary>
      /// <param name="margin">Indents as SpaceVIL.Decorations.Indents.</param>
      public void SetMargin(Indents margin)
      {
         Margin = margin;
      }

      /// <summary>
      /// Setting indents of an item to offset itself relative to its container.
      /// </summary>
      /// <param name="left">Indent on the left.</param>
      /// <param name="top">Indent on the top.</param>
      /// <param name="right">Indent on the right.</param>
      /// <param name="bottom">Indent on the bottom.</param>
      public void SetMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
      {
         Margin.Left = left;
         Margin.Top = top;
         Margin.Right = right;
         Margin.Bottom = bottom;
      }

      /// <summary>
      /// Setting indents between children of a container type item.
      /// </summary>
      /// <param name="spacing">Spacing as SpaceVIL.Decorations.Spacing.</param>
      public void SetSpacing(Spacing spacing)
      {
         Spacing = spacing;
      }

      /// <summary>
      /// Setting indents between children of a container type item.
      /// </summary>
      /// <param name="horizontal">Horizontal indent.</param>
      /// <param name="vertical">Vertical indent.</param>
      public void SetSpacing(int horizontal = 0, int vertical = 0)
      {
         Spacing.Horizontal = horizontal;
         Spacing.Vertical = vertical;
      }

      /// <summary>
      /// Setting border of an item's shape. Border consist of corner radiuses, thickness and color.
      /// </summary>
      /// <param name="border">Border as SpaceVIL.Decorations.Border.</param>
      public void SetBorder(Border border)
      {
         BorderFill = border.GetFill();
         BorderRadius = border.GetRadius();
         BorderThickness = border.GetThickness();
      }

      /// <summary>
      /// Setting border for an item's shape. Border consist of corner radiuses, thickness and color.
      /// </summary>
      /// <param name="fill">Border color as System.Drawing.Color.</param>
      /// <param name="radius">Radiuses of an border corners as SpaceVIL.Decorations.CornerRadius.</param>
      /// <param name="thickness">Border thickness.</param>
      public void SetBorder(Color fill, CornerRadius radius, int thickness)
      {
         BorderFill = fill;
         BorderRadius = radius;
         BorderThickness = thickness;
      }

      /// <summary>
      /// Setting shadow for an item's shape. 
      /// </summary>
      /// <param name="shadow">Shadow as SpaceVIL.Decorations.Shadow.</param>
      public void SetShadow(Shadow shadow)
      {
         ShadowColor = shadow.GetColor();
         ShadowRadius = shadow.GetRadius();
         ShadowXOffset = shadow.GetXOffset();
         ShadowYOffset = shadow.GetYOffset();
      }

      /// <summary>
      /// Setting an Alignment of an item's shape relative to its container. 
      /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
      /// Attention: this property is required.
      /// </summary>
      /// <param name="alignment">Alignment as SpaceVIL.Core.ItemAlignment.</param>
      public void SetAlignment(ItemAlignment alignment)
      {
         Alignment = alignment;
      }

      /// <summary>
      /// Setting an Alignment of an item's shape relative to its container. 
      /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
      /// Attention: this property is required.
      /// </summary>
      /// <param name="alignment">Alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
      public void SetAlignment(params ItemAlignment[] alignment)
      {
         Alignment = alignment.ElementAt(0);
         if (alignment.Length > 1)
         {
            for (int i = 1; i < alignment.Length; i++)
            {
               Alignment |= alignment.ElementAt(i);
            }
         }
      }

      /// <summary>
      /// Alignment of an item's text. 
      /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
      /// Can be used only if the item contains text and in this case this property is required.
      /// </summary>
      /// <param name="alignment">Text alignment as SpaceVIL.Core.ItemAlignment.</param>
      public void SetTextAlignment(ItemAlignment alignment)
      {
         TextAlignment = alignment;
      }

      /// <summary>
      /// Alignment of an item's text. 
      /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
      /// Can be used only if the item contains text and in this case this property is required.
      /// </summary>
      /// <param name="alignment">Text alignment as sequence of SpaceVIL.Core.ItemAlignment.</param>
      public void SetTextAlignment(params ItemAlignment[] alignment)
      {
         TextAlignment = alignment.ElementAt(0);
         if (alignment.Length > 1)
         {
            for (int i = 1; i < alignment.Length; i++)
            {
               TextAlignment |= alignment.ElementAt(i);
            }
         }
      }

      /// <summary>
      /// Adding inner primitives to the item (as decorations only).
      /// Note: not supported in the current version!
      /// </summary>
      public void AddInnerShape(IBaseItem shape)
      {
         if (InnerShapes == null)
         {
            InnerShapes = new List<IBaseItem>();
         }
         InnerShapes.Add(shape);
      }

      /// <summary>
      /// Assigning a style for an item's child by key name.
      /// </summary>
      /// <param name="keyName">Key name of a child.</param>
      /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
      public void AddInnerStyle(String keyName, Style style)
      {
         if (_innerStyles.ContainsKey(keyName))
         {
            _innerStyles[keyName] = style;
         }
         else
         {
            _innerStyles.Add(keyName, style);
         }
      }

      /// <summary>
      /// Getting a child’s style by key name.
      /// </summary>
      /// <param name="keyName">Key name of a child.</param>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public Style GetInnerStyle(String keyName)
      {
         if (_innerStyles.ContainsKey(keyName))
         {
            return _innerStyles[keyName];
         }

         return null;
      }

      /// <summary>
      /// Removing a child's style by its key name.
      /// </summary>
      /// <param name="keyName">Key name of a child.</param>
      public void RemoveInnerStyle(String keyName)
      {
         if (_innerStyles.ContainsKey(keyName))
         {
            _innerStyles.Remove(keyName);
         }
      }

      /// <summary>
      /// Adding visual state for an item. 
      /// <para/> Type can be Base, Hovered, Pressed, Toggled, Focused, Disabled.
      /// </summary>
      /// <param name="type">Type as SpaceVIL.Core.ItemStateType.</param>
      /// <param name="state">Visual state as SpaceVIL.Decorations.ItemState.</param>
      public void AddItemState(ItemStateType type, ItemState state)
      {
         if (_itemStates.ContainsKey(type))
         {
            state.Value = true;
            _itemStates[type] = state;
         }
         else
         {
            _itemStates.Add(type, state);
         }
      }

      /// <summary>
      /// Getting visual state of an item by type. 
      /// <para/> Type can be Base, Hovered, Pressed, Toggled, Focused, Disabled.
      /// </summary>
      /// <param name="type">Type as SpaceVIL.Core.ItemStateType.</param>
      /// <returns>Visual state as SpaceVIL.Decorations.ItemState.</returns>
      public ItemState GetState(ItemStateType type)
      {
         if (_itemStates.ContainsKey(type))
         {
            return _itemStates[type];
         }
         return null;
      }

      /// <summary>
      /// Getting all presented in the current style visual states of an item.
      /// </summary>
      /// <returns> Dictionary of an ItemStateTypes and its ItemStates. </returns>
      public Dictionary<ItemStateType, ItemState> GetAllStates()
      {
         return _itemStates;
      }

      /// <summary>
      /// Removing visual state of an item by type.
      /// <para/> Type can be Base, Hovered, Pressed, Toggled, Focused, Disabled.
      /// </summary>
      /// <param name="type">Type as SpaceVIL.Core.ItemStateType.</param>
      public void RemoveItemState(ItemStateType type)
      {
         if (type == ItemStateType.Base)
         {
            return;
         }
         if (_itemStates.ContainsKey(type))
         {
            _itemStates.Remove(type);
         }
      }

      internal static void ItemStatesSyncBase(Style style)
      {
         ItemState coreState = style.GetState(ItemStateType.Base);
         foreach (var state in style.GetAllStates())
         {
            if (coreState == state.Value)
            {
               continue;
            }

            state.Value.Border = coreState.Border;
         }
      }

      /// <summary>
      /// Cloning the current style and returning a new deep copy of Style.
      /// </summary>
      /// <returns>Deep copy of current style as SpaceVIL.Decorations.Style.</returns>
      public Style Clone()
      {
         Style style = new Style();

         if (Background != null)
         {
            style.Background = Color.FromArgb(Background.A, Background.R, Background.G, Background.B);
         }

         if (Foreground != null)
         {
            style.Foreground = Color.FromArgb(Foreground.A, Foreground.R, Foreground.G, Foreground.B);
         }

         if (Font != null)
         {
            style.Font = new Font(Font.FontFamily, Font.Size, Font.Style);
         }
         else
         {
            style.Font = DefaultsService.GetDefaultFont();
         }

         style.SetSizePolicy(WidthPolicy, HeightPolicy);
         style.SetSize(Width, Height);
         style.SetMaxSize(MaxWidth, MaxHeight);
         style.SetMinSize(MinWidth, MinHeight);
         style.Alignment = Alignment;
         style.TextAlignment = TextAlignment;
         style.SetPadding(Padding.Left, Padding.Top, Padding.Right, Padding.Bottom);
         style.SetMargin(Margin.Left, Margin.Top, Margin.Right, Margin.Bottom);
         style.SetSpacing(Spacing.Horizontal, Spacing.Vertical);

         if (BorderFill != null)
         {
            style.BorderFill = Color.FromArgb(BorderFill.A, BorderFill.R, BorderFill.G, BorderFill.B);
         }

         style.BorderThickness = BorderThickness;

         if (BorderRadius != null)
         {
            style.BorderRadius = new CornerRadius(BorderRadius.LeftTop, BorderRadius.RightTop, BorderRadius.LeftBottom,
                    BorderRadius.RightBottom);
         }

         if (ShadowColor != null)
         {
            style.ShadowColor = Color.FromArgb(ShadowColor.A, ShadowColor.R, ShadowColor.G, ShadowColor.B);
         }

         style.ShadowRadius = ShadowRadius;
         style.ShadowXOffset = ShadowXOffset;
         style.ShadowYOffset = ShadowYOffset;
         style.IsShadowDrop = IsShadowDrop;

         if (Shape != null)
         {
            style.Shape = new List<float[]>(Shape);
         }

         if (InnerShapes != null)
         {
            style.InnerShapes = new List<IBaseItem>(InnerShapes);
         }

         style.IsFixedShape = IsFixedShape;
         style.IsVisible = IsVisible;
         style._itemStates = new Dictionary<ItemStateType, ItemState>(_itemStates);

         return style;
      }

      /// <summary>
      /// Getting a default common style. Properly filled in all the necessary properties.
      /// <para/> Use this method to create instance of Style class instead of  using pure constructor (new Style()).
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetDefaultCommonStyle()
      {
         Style style = new Style();

         style.Background = Color.White;
         style.Foreground = Color.Black;
         style.Font = DefaultsService.GetDefaultFont();
         style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         style.SetSize(30, 30);
         style.SetAlignment(ItemAlignment.Left, ItemAlignment.Top);
         style.SetTextAlignment(ItemAlignment.Left, ItemAlignment.Top);
         style.SetPadding(0, 0, 0, 0);
         style.SetMargin(0, 0, 0, 0);
         style.SetSpacing(0, 0);
         style.SetBorder(new Border(Color.Transparent, new CornerRadius(), 0));

         return style;
      }

      /// <summary>
      /// Getting default style for a ButtonCore item. Properly filled in all the necessary properties.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetButtonCoreStyle()
      {
         Style style = new Style();

         style.Background = Color.FromArgb(255, 13, 176, 255);
         style.Foreground = Color.FromArgb(255, 32, 32, 32);
         style.Font = DefaultsService.GetDefaultFont(16);
         style.WidthPolicy = SizePolicy.Fixed;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Width = 30;
         style.Height = 30;
         style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
         style.TextAlignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
         style.BorderRadius = new CornerRadius();
         ItemState hovered = new ItemState(Color.FromArgb(60, 255, 255, 255));
         style.AddItemState(ItemStateType.Hovered, hovered);
         ItemState pressed = new ItemState(Color.FromArgb(60, 0, 0, 0));
         style.AddItemState(ItemStateType.Pressed, pressed);

         return style;
      }

      /// <summary>
      /// Getting default style for a ButtonToggle item. Properly filled in all the necessary properties.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetButtonToggleStyle()
      {
         Style style = new Style();
         style.Background = Color.FromArgb(255, 13, 176, 255);
         style.Foreground = Color.FromArgb(255, 70, 70, 70); ;
         style.Font = DefaultsService.GetDefaultFont(16);
         style.WidthPolicy = SizePolicy.Fixed;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Width = 10;
         style.Height = 10;
         style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
         style.TextAlignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
         style.BorderRadius = new CornerRadius();
         style.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            Background = Color.FromArgb(60, 255, 255, 255)
         });
         style.AddItemState(ItemStateType.Pressed, new ItemState()
         {
            Background = Color.FromArgb(30, 0, 0, 60)
         });
         style.AddItemState(ItemStateType.Toggled, new ItemState()
         {
            Background = Color.FromArgb(121, 223, 152)
         });

         return style;
      }

      /// <summary>
      /// Getting default style for a CheckBox item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "indicator", "text".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetCheckBoxStyle()
      {
         Style style = new Style();

         style.Background = Color.FromArgb(255, 80, 80, 80);
         style.Foreground = Color.LightGray;
         style.Font = DefaultsService.GetDefaultFont();
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Width = 10;
         style.Height = 20;
         style.MinHeight = 20;
         style.MinWidth = 20;
         style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;

         Style indicatorStyle = GetIndicatorStyle();
         style.AddInnerStyle("indicator", indicatorStyle);

         Style textlineStyle = GetLabelStyle();
         textlineStyle.WidthPolicy = SizePolicy.Expand;
         textlineStyle.HeightPolicy = SizePolicy.Expand;
         textlineStyle.Alignment = ItemAlignment.VCenter;
         textlineStyle.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
         textlineStyle.Margin = new Indents(10 + indicatorStyle.Width, 0, 0, 0);
         style.AddInnerStyle("text", textlineStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a Indicator item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "marker".
      /// <para/> This is part of CheckBox item style.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetIndicatorStyle()
      {
         Style style = new Style();

         style.Background = Color.FromArgb(255, 32, 32, 32);
         style.WidthPolicy = SizePolicy.Fixed;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Width = 20;
         style.Height = 20;
         style.MinHeight = 20;
         style.MinWidth = 20;
         style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
         style.Padding = new Indents(4, 4, 4, 4);

         Style markerStyle = new Style();
         markerStyle.Background = Color.FromArgb(255, 32, 32, 32);
         markerStyle.Foreground = Color.FromArgb(255, 70, 70, 70); ;
         markerStyle.Font = DefaultsService.GetDefaultFont();
         markerStyle.WidthPolicy = SizePolicy.Expand;
         markerStyle.HeightPolicy = SizePolicy.Expand;
         markerStyle.Alignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
         markerStyle.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            Background = Color.FromArgb(60, 255, 255, 255)
         });
         markerStyle.AddItemState(ItemStateType.Toggled, new ItemState()
         {
            Background = Color.FromArgb(255, 255, 181, 111)
         });
         style.AddInnerStyle("marker", markerStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a text type item. Attention: not all the necessary properties properly filled.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetTextLineStyle()
      {
         Style style = new Style();

         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
         style.Margin = new Indents(4, 4, 4, 4);

         return style;
      }

      /// <summary>
      /// Getting default style for a ComboBox item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "selection", "dropdownbutton", "dropdownarea", "arrow".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetComboBoxStyle()
      {
         Style style = new Style();
         style.Background = Color.FromArgb(255, 220, 220, 220);
         style.Foreground = Color.FromArgb(255, 70, 70, 70); ;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Width = 10;
         style.Height = 30;
         style.MinHeight = 10;
         style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;

         Style selectionStyle = new Style();
         selectionStyle.Background = Color.Transparent;
         selectionStyle.Foreground = Color.FromArgb(255, 70, 70, 70); ;
         selectionStyle.Font = DefaultsService.GetDefaultFont(14);
         selectionStyle.WidthPolicy = SizePolicy.Expand;
         selectionStyle.HeightPolicy = SizePolicy.Expand;
         selectionStyle.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
         selectionStyle.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
         selectionStyle.Padding = new Indents(10, 0, 0, 0);
         selectionStyle.Margin = new Indents(0, 0, 20, 0);
         style.AddInnerStyle("selection", selectionStyle);

         Style dropdownbuttonStyle = new Style();
         dropdownbuttonStyle.Width = 20;
         dropdownbuttonStyle.WidthPolicy = SizePolicy.Fixed;
         dropdownbuttonStyle.HeightPolicy = SizePolicy.Expand;
         dropdownbuttonStyle.Alignment = ItemAlignment.Right | ItemAlignment.VCenter;
         dropdownbuttonStyle.Background = Color.FromArgb(255, 255, 181, 111);
         dropdownbuttonStyle.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            Background = Color.FromArgb(40, 255, 255, 255)
         });
         style.AddInnerStyle("dropdownbutton", dropdownbuttonStyle);

         style.AddInnerStyle("dropdownarea", GetComboBoxDropDownStyle());

         Style arrowStyle = new Style();
         arrowStyle.Width = 14;
         arrowStyle.Height = 6;
         arrowStyle.WidthPolicy = SizePolicy.Fixed;
         arrowStyle.HeightPolicy = SizePolicy.Fixed;
         arrowStyle.Alignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
         arrowStyle.Background = Color.FromArgb(255, 50, 50, 50);
         arrowStyle.Shape = GraphicsMathService.GetTriangle(angle: 180);
         style.AddInnerStyle("arrow", arrowStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a ComboBoxDropDown item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "itemlist".
      /// <para/> Inner styles for "itemlist": "vscrollbar", "hscrollbar", "menu".
      /// <para/> This is part of ComboBox item style.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetComboBoxDropDownStyle()
      {
         Style style = new Style();
         style.Background = Color.White;
         style.WidthPolicy = SizePolicy.Fixed;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Padding = new Indents(0, 0, 0, 0);
         style.IsVisible = false;

         Style itemlistStyle = GetListBoxStyle();
         itemlistStyle.Background = Color.Transparent;
         itemlistStyle.Alignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
         style.AddInnerStyle("itemlist", itemlistStyle);

         Style itemlistareaFtyle = itemlistStyle.GetInnerStyle("area");
         if (itemlistareaFtyle != null)
         {
            itemlistStyle.SetPadding(0, 0, 0, 0);
         }

         Style vsbStyle = GetSimpleVerticalScrollBarStyle();
         vsbStyle.Alignment = ItemAlignment.Right | ItemAlignment.Top;
         itemlistStyle.AddInnerStyle("vscrollbar", vsbStyle);

         Style hsbStyle = GetHorizontalScrollBarStyle();
         hsbStyle.Alignment = ItemAlignment.Left | ItemAlignment.Bottom;
         itemlistStyle.AddInnerStyle("hscrollbar", hsbStyle);

         Style menuStyle = new Style();
         menuStyle.Background = Color.FromArgb(50, 50, 50);
         menuStyle.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
         menuStyle.SetAlignment(ItemAlignment.Right | ItemAlignment.Bottom);
         itemlistStyle.AddInnerStyle("menu", menuStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a MenuItem item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "text", "arrow".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetMenuItemStyle()
      {
         Style style = new Style();
         style.Background = Color.Transparent;
         style.Foreground = Color.FromArgb(70, 70, 70);
         style.Font = DefaultsService.GetDefaultFont();
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Height = 25;
         style.MinHeight = 10;
         style.SetAlignment(ItemAlignment.Left | ItemAlignment.Top);
         style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
         style.Padding = new Indents(10, 0, 10, 0);
         style.AddItemState(ItemStateType.Hovered, new ItemState(Color.FromArgb(200, 200, 200)));

         Style textStyle = new Style();
         textStyle.SetMargin(0, 0, 0, 0);
         style.AddInnerStyle("text", textStyle);

         Style arrowStyle = new Style();
         arrowStyle.Width = 6;
         arrowStyle.Height = 10;
         arrowStyle.WidthPolicy = SizePolicy.Fixed;
         arrowStyle.HeightPolicy = SizePolicy.Fixed;
         arrowStyle.Alignment = ItemAlignment.Right | ItemAlignment.VCenter;
         arrowStyle.Background = Color.FromArgb(255, 80, 80, 80);
         arrowStyle.Margin = new Indents(10, 0, 0, 0);
         arrowStyle.Shape = GraphicsMathService.GetTriangle(angle: 90);
         style.AddInnerStyle("arrow", arrowStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a ContextMenu item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "itemlist".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetContextMenuStyle()
      {
         Style style = GetDefaultCommonStyle();
         style.Background = Color.FromArgb(210, 210, 210);
         style.IsVisible = false;
         style.SetShadow(new Shadow(10, 3, 3, Color.FromArgb(180, 0, 0, 0)));
         style.IsShadowDrop = true;

         Style itemlistStyle = GetListBoxStyle();
         itemlistStyle.Background = Color.Transparent;
         itemlistStyle.Alignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
         style.AddInnerStyle("itemlist", itemlistStyle);

         Style areaStyle = itemlistStyle.GetInnerStyle("area");
         areaStyle.SetPadding(0, 0, 0, 0);

         return style;
      }

      internal static Style GetCustomSelectorStyle()
      {
         throw new NotImplementedException();
      }

      /// <summary>
      /// Getting default style for a FreeArea item. Properly filled in all the necessary properties.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetFreeAreaStyle()
      {
         Style style = new Style();

         // style.Background = Color.Transparent;
         style.Background = Color.FromArgb(255, 70, 70, 70);

         style.Padding = new Indents(2, 2, 2, 2);
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

         return style;
      }

      /// <summary>
      /// Getting default style for a Frame item. Properly filled in all the necessary properties.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetFrameStyle()
      {
         Style style = new Style();

         style.Background = Color.Transparent;
         style.Padding = new Indents(2, 2, 2, 2);
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

         return style;
      }

      /// <summary>
      /// Getting default style for a Grid item. Properly filled in all the necessary properties.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetGridStyle()
      {
         Style style = new Style();

         style.Background = Color.Transparent;
         style.Height = 16;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

         return style;
      }

      /// <summary>
      /// Getting default style for a HorizontalScrollBar item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "uparrow", "downarrow", "slider".
      /// <para/> Inner styles for "slider": "track", "handler".
      /// <para/> This is part of many items style.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetHorizontalScrollBarStyle()
      {
         Style style = new Style();

         style.Background = Color.FromArgb(255, 50, 50, 50);
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Height = 16;

         Style uparrowStyle = new Style();
         uparrowStyle.WidthPolicy = SizePolicy.Fixed;
         uparrowStyle.HeightPolicy = SizePolicy.Fixed;
         uparrowStyle.Background = Color.FromArgb(255, 100, 100, 100);
         uparrowStyle.Width = 16;
         uparrowStyle.Height = 16;
         uparrowStyle.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
         uparrowStyle.Shape = GraphicsMathService.GetTriangle(10, 8, 3, 4, -90);
         uparrowStyle.IsFixedShape = true;
         uparrowStyle.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            Background = Color.FromArgb(40, 255, 255, 255)
         });
         style.AddInnerStyle("uparrow", uparrowStyle);

         Style downarrowStyle = new Style();
         downarrowStyle.WidthPolicy = SizePolicy.Fixed;
         downarrowStyle.HeightPolicy = SizePolicy.Fixed;
         downarrowStyle.Background = Color.FromArgb(255, 100, 100, 100);
         downarrowStyle.Width = 16;
         downarrowStyle.Height = 16;
         downarrowStyle.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
         downarrowStyle.Shape = GraphicsMathService.GetTriangle(10, 8, 3, 4, 90);
         downarrowStyle.IsFixedShape = true;
         downarrowStyle.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            Background = Color.FromArgb(40, 255, 255, 255)
         });
         style.AddInnerStyle("downarrow", downarrowStyle);

         Style sliderStyle = new Style();
         sliderStyle.WidthPolicy = SizePolicy.Expand;
         sliderStyle.HeightPolicy = SizePolicy.Expand;
         sliderStyle.Background = Color.Transparent;
         style.AddInnerStyle("slider", sliderStyle);

         Style trackStyle = new Style();
         trackStyle.WidthPolicy = SizePolicy.Expand;
         trackStyle.HeightPolicy = SizePolicy.Expand;
         trackStyle.Background = Color.Transparent;
         sliderStyle.AddInnerStyle("track", trackStyle);

         Style handlerStyle = new Style();
         handlerStyle.WidthPolicy = SizePolicy.Fixed;
         handlerStyle.HeightPolicy = SizePolicy.Expand;
         handlerStyle.Background = Color.FromArgb(255, 100, 100, 100);
         handlerStyle.Margin = new Indents(0, 3, 0, 3);
         handlerStyle.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
         handlerStyle.MinWidth = 15;
         handlerStyle.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            Background = Color.FromArgb(40, 255, 255, 255)
         });
         sliderStyle.AddInnerStyle("handler", handlerStyle);

         return style;
      }

      /// <summary>
      /// Getting simplified style for a SimpleHorizontalScrollBar item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "uparrow", "downarrow", "slider".
      /// <para/> Inner styles for "slider": "track", "handler".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetSimpleHorizontalScrollBarStyle()
      {
         Style style = new Style();

         style.Background = Color.Transparent;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Padding = new Indents(2, 0, 2, 0);
         style.Height = 16;

         Style uparrowStyle = new Style();
         uparrowStyle.IsVisible = false;
         style.AddInnerStyle("uparrow", uparrowStyle);

         Style downarrowStyle = new Style();
         downarrowStyle.IsVisible = false;
         style.AddInnerStyle("downarrow", downarrowStyle);

         Style sliderStyle = new Style();
         sliderStyle.WidthPolicy = SizePolicy.Expand;
         sliderStyle.HeightPolicy = SizePolicy.Expand;
         sliderStyle.Background = Color.Transparent;
         style.AddInnerStyle("slider", sliderStyle);

         Style trackStyle = new Style();
         trackStyle.WidthPolicy = SizePolicy.Expand;
         trackStyle.HeightPolicy = SizePolicy.Expand;
         trackStyle.Background = Color.Transparent;
         sliderStyle.AddInnerStyle("track", trackStyle);

         Style handlerStyle = new Style();
         handlerStyle.WidthPolicy = SizePolicy.Fixed;
         handlerStyle.HeightPolicy = SizePolicy.Expand;
         handlerStyle.Background = Color.FromArgb(255, 120, 120, 120);
         handlerStyle.Margin = new Indents(0, 5, 0, 5);
         handlerStyle.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
         handlerStyle.BorderRadius = new CornerRadius(3);
         handlerStyle.MinWidth = 15;
         sliderStyle.AddInnerStyle("handler", handlerStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a VerticalScrollBar item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "uparrow", "downarrow", "slider".
      /// <para/> Inner styles for "slider": "track", "handler".
      /// <para/> This is part of many items style.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetVerticalScrollBarStyle()
      {
         Style style = new Style();

         style.Background = Color.FromArgb(255, 50, 50, 50);
         style.WidthPolicy = SizePolicy.Fixed;
         style.HeightPolicy = SizePolicy.Expand;
         style.Width = 16;

         Style uparrowStyle = new Style();
         uparrowStyle.WidthPolicy = SizePolicy.Fixed;
         uparrowStyle.HeightPolicy = SizePolicy.Fixed;
         uparrowStyle.Background = Color.FromArgb(255, 100, 100, 100);
         uparrowStyle.Width = 16;
         uparrowStyle.Height = 16;
         uparrowStyle.Alignment = ItemAlignment.Top | ItemAlignment.HCenter;
         uparrowStyle.Shape = GraphicsMathService.GetTriangle(10, 8, 3, 4, 0);
         uparrowStyle.IsFixedShape = true;
         uparrowStyle.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            Background = Color.FromArgb(40, 255, 255, 255)
         });
         style.AddInnerStyle("uparrow", uparrowStyle);

         Style downarrowStyle = new Style();
         downarrowStyle.WidthPolicy = SizePolicy.Fixed;
         downarrowStyle.HeightPolicy = SizePolicy.Fixed;
         downarrowStyle.Background = Color.FromArgb(255, 100, 100, 100);
         downarrowStyle.Width = 16;
         downarrowStyle.Height = 16;
         downarrowStyle.Alignment = ItemAlignment.Bottom | ItemAlignment.HCenter;
         downarrowStyle.Shape = GraphicsMathService.GetTriangle(10, 8, 3, 4, 180);
         downarrowStyle.IsFixedShape = true;
         downarrowStyle.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            Background = Color.FromArgb(40, 255, 255, 255)
         });
         style.AddInnerStyle("downarrow", downarrowStyle);

         Style sliderStyle = new Style();
         sliderStyle.WidthPolicy = SizePolicy.Expand;
         sliderStyle.HeightPolicy = SizePolicy.Expand;
         sliderStyle.Background = Color.Transparent;
         style.AddInnerStyle("slider", sliderStyle);

         Style trackStyle = new Style();
         trackStyle.WidthPolicy = SizePolicy.Expand;
         trackStyle.HeightPolicy = SizePolicy.Expand;
         trackStyle.Background = Color.Transparent;
         sliderStyle.AddInnerStyle("track", trackStyle);

         Style handlerStyle = new Style();
         handlerStyle.WidthPolicy = SizePolicy.Expand;
         handlerStyle.HeightPolicy = SizePolicy.Fixed;
         handlerStyle.Background = Color.FromArgb(255, 100, 100, 100);
         handlerStyle.Margin = new Indents(3, 0, 3, 0);
         handlerStyle.Alignment = ItemAlignment.Top | ItemAlignment.HCenter;
         handlerStyle.MinHeight = 15;
         handlerStyle.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            Background = Color.FromArgb(40, 255, 255, 255)
         });
         sliderStyle.AddInnerStyle("handler", handlerStyle);

         return style;
      }

      /// <summary>
      /// Getting simplified style for a SimpleVerticalScrollBar item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "uparrow", "downarrow", "slider".
      /// <para/> Inner styles for "slider": "track", "handler".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetSimpleVerticalScrollBarStyle()
      {
         Style style = new Style();

         style.Background = Color.Transparent;
         style.Padding = new Indents(0, 2, 0, 2);
         style.WidthPolicy = SizePolicy.Fixed;
         style.HeightPolicy = SizePolicy.Expand;
         style.Width = 16;

         Style uparrowStyle = new Style();
         uparrowStyle.IsVisible = false;
         style.AddInnerStyle("uparrow", uparrowStyle);

         Style downarrowStyle = new Style();
         downarrowStyle.IsVisible = false;
         style.AddInnerStyle("downarrow", downarrowStyle);

         Style sliderStyle = new Style();
         sliderStyle.WidthPolicy = SizePolicy.Expand;
         sliderStyle.HeightPolicy = SizePolicy.Expand;
         sliderStyle.Background = Color.Transparent;
         style.AddInnerStyle("slider", sliderStyle);

         Style trackStyle = new Style();
         trackStyle.WidthPolicy = SizePolicy.Expand;
         trackStyle.HeightPolicy = SizePolicy.Expand;
         trackStyle.Background = Color.Transparent;
         sliderStyle.AddInnerStyle("track", trackStyle);

         Style handlerStyle = new Style();
         handlerStyle.WidthPolicy = SizePolicy.Expand;
         handlerStyle.HeightPolicy = SizePolicy.Fixed;
         handlerStyle.Background = Color.FromArgb(255, 120, 120, 120);
         handlerStyle.Margin = new Indents(5, 0, 5, 0);
         handlerStyle.Alignment = ItemAlignment.Top | ItemAlignment.HCenter;
         handlerStyle.BorderRadius = new CornerRadius(3);
         handlerStyle.MinHeight = 15;
         sliderStyle.AddInnerStyle("handler", handlerStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a HorizontalSlider item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "track", "handler".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetHorizontalSliderStyle()
      {
         Style style = new Style();

         style.Background = Color.Transparent;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Height = 25;
         style.SetAlignment(ItemAlignment.Top, ItemAlignment.Left);

         Style trackStyle = new Style();
         trackStyle.WidthPolicy = SizePolicy.Expand;
         trackStyle.HeightPolicy = SizePolicy.Fixed;
         trackStyle.Height = 5;
         trackStyle.Alignment = ItemAlignment.VCenter;
         trackStyle.Background = Color.FromArgb(255, 100, 100, 100);
         style.AddInnerStyle("track", trackStyle);

         Style handlerStyle = new Style();
         handlerStyle.WidthPolicy = SizePolicy.Fixed;
         handlerStyle.HeightPolicy = SizePolicy.Expand;
         handlerStyle.Width = 10;
         handlerStyle.Background = Color.FromArgb(255, 255, 181, 111);
         handlerStyle.Alignment = ItemAlignment.Left;
         handlerStyle.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            Background = Color.FromArgb(80, 255, 255, 255)
         });
         style.AddInnerStyle("handler", handlerStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a VerticalSlider item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "track", "handler".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetVerticalSliderStyle()
      {
         Style style = new Style();

         style.Background = Color.Transparent;
         style.WidthPolicy = SizePolicy.Fixed;
         style.HeightPolicy = SizePolicy.Expand;
         style.Width = 25;

         Style trackStyle = new Style();
         trackStyle.WidthPolicy = SizePolicy.Fixed;
         trackStyle.HeightPolicy = SizePolicy.Expand;
         trackStyle.Width = 5;
         trackStyle.Alignment = ItemAlignment.HCenter;
         trackStyle.Background = Color.FromArgb(255, 100, 100, 100);
         style.AddInnerStyle("track", trackStyle);

         Style handlerStyle = new Style();
         handlerStyle.WidthPolicy = SizePolicy.Expand;
         handlerStyle.HeightPolicy = SizePolicy.Fixed;
         handlerStyle.Height = 10;
         handlerStyle.Background = Color.FromArgb(255, 255, 181, 111);
         handlerStyle.Alignment = ItemAlignment.Top;
         handlerStyle.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            Background = Color.FromArgb(80, 255, 255, 255)
         });
         style.AddInnerStyle("handler", handlerStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a HorizontalStack item. Properly filled in all the necessary properties.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetHorizontalStackStyle()
      {
         Style style = new Style();

         style.Background = Color.Transparent;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

         return style;
      }

      /// <summary>
      /// Getting default style for a VerticalStack item. Properly filled in all the necessary properties.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetVerticalStackStyle()
      {
         Style style = new Style();

         style.Background = Color.Transparent;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

         return style;
      }

      /// <summary>
      /// Getting default style for a HorizontalSplitArea item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "splitholder".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetHorizontalSplitAreaStyle()
      {
         Style style = new Style();

         style.Background = Color.Transparent;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

         Style splitterStyle = new Style();
         splitterStyle.Background = Color.FromArgb(255, 42, 42, 42);
         splitterStyle.Width = 6;
         style.AddInnerStyle("splitholder", splitterStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a VerticalSplitArea item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "splitholder".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetVerticalSplitAreaStyle()
      {
         Style style = new Style();

         style.Background = Color.Transparent;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

         Style splitterStyle = new Style();
         splitterStyle.Background = Color.FromArgb(255, 42, 42, 42);
         splitterStyle.Height = 6;
         style.AddInnerStyle("splitholder", splitterStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a Label item. Properly filled in all the necessary properties.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetLabelStyle()
      {
         Style style = new Style();

         style.Font = DefaultsService.GetDefaultFont();
         style.SetForeground(210, 210, 210);
         style.Background = Color.Transparent;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
         style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;

         return style;
      }

      /// <summary>
      /// Getting default style for a ListArea item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "selection".
      /// <para/> This is part of many items style.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetListAreaStyle()
      {
         Style style = new Style();

         style.Background = Color.Transparent;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
         style.Padding = new Indents(2, 2, 2, 2);
         style.Spacing = new Spacing(0, 4);

         Style selectionStyle = GetSelectionItemStyle();
         style.AddInnerStyle("selection", selectionStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a ListBox item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "area", "vscrollbar", "hscrollbar", "menu".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetListBoxStyle()
      {
         Style style = new Style();

         style.Background = Color.FromArgb(255, 70, 70, 70);
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

         Style vsbStyle = GetVerticalScrollBarStyle();
         vsbStyle.Alignment = ItemAlignment.Right | ItemAlignment.Top;
         style.AddInnerStyle("vscrollbar", vsbStyle);

         Style hsbStyle = GetHorizontalScrollBarStyle();
         hsbStyle.Alignment = ItemAlignment.Left | ItemAlignment.Bottom;
         style.AddInnerStyle("hscrollbar", hsbStyle);

         Style menuStyle = new Style();
         menuStyle.Background = Color.FromArgb(50, 50, 50);
         menuStyle.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
         menuStyle.SetAlignment(ItemAlignment.Right | ItemAlignment.Bottom);
         style.AddInnerStyle("menu", menuStyle);

         Style areaStyle = GetListAreaStyle();
         style.AddInnerStyle("area", areaStyle);

         return style;
      }

      /// <summary>
      /// Note: not supported in current version.
      /// </summary>
      /// <returns> default style for WContainer objects. </returns>
      public static Style GetWContainerStyle()//нужен ли?
      {
         Style style = new Style();
         return style;
      }

      /// <summary>
      /// Getting default style for a RadioButton item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "indicator", "text".
      /// <para/> Inner styles of "indicator": "marker".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetRadioButtonStyle()
      {
         Style style = new Style();

         style.Background = Color.FromArgb(255, 80, 80, 80);
         style.Foreground = Color.LightGray;
         style.Font = DefaultsService.GetDefaultFont(0);
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Height = 20;
         style.MinHeight = 20;
         style.MinWidth = 20;
         style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
         style.BorderRadius = new CornerRadius(10);

         Style indicatorStyle = GetIndicatorStyle();
         indicatorStyle.Shape = GraphicsMathService.GetEllipse();
         style.AddInnerStyle("indicator", indicatorStyle);

         Style markerStyle = indicatorStyle.GetInnerStyle("marker");
         markerStyle.Shape = GraphicsMathService.GetEllipse();
         indicatorStyle.AddInnerStyle("marker", markerStyle);

         Style textlineStyle = GetLabelStyle();
         textlineStyle.WidthPolicy = SizePolicy.Expand;
         textlineStyle.HeightPolicy = SizePolicy.Expand;
         textlineStyle.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
         textlineStyle.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
         textlineStyle.Margin = new Indents(10 + indicatorStyle.Width, 0, 0, 0);
         style.AddInnerStyle("text", textlineStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a PasswordLine item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "showmarker", "textedit".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetPasswordLineStyle()
      {
         Style style = new Style();
         style.Background = Color.FromArgb(255, 210, 210, 210);
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
         style.Height = 30;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Spacing = new Spacing(5, 0);
         style.Padding = new Indents(5, 0, 5, 0);
         // style.AddItemState(ItemStateType.Hovered, new ItemState()
         // {
         //     Background = Color.FromArgb(30, 255, 255, 255)
         // });
         // style.AddItemState(ItemStateType.Focused, new ItemState()
         // {
         //     Background = Color.FromArgb(30, 255, 255, 255)
         // });

         Style markerStyle = GetIndicatorStyle().GetInnerStyle("marker");
         markerStyle.Background = Color.FromArgb(0, 100, 100, 100);
         markerStyle.SetSize(20, 20);
         markerStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         markerStyle.Alignment = ItemAlignment.VCenter | ItemAlignment.Right;
         markerStyle.TextAlignment = ItemAlignment.VCenter | ItemAlignment.Left;
         markerStyle.RemoveItemState(ItemStateType.Hovered);
         // marker_style.BorderRadius = new CornerRadius(5);

         // marker_style.AddItemState(ItemStateType.Hovered, new ItemState()
         // {
         //     Background = Color.FromArgb(50, 255, 255, 255)
         // });
         // marker_style.AddItemState(ItemStateType.Toggled, new ItemState()
         // {
         //     Background = Color.FromArgb(255, 40, 40, 40)
         // });
         style.AddInnerStyle("showmarker", markerStyle);
         style.AddInnerStyle("textedit", GetTextEncryptStyle());

         return style;
      }

      private static Style GetTextEncryptStyle()
      {
         Style style = new Style();
         style.Background = Color.FromArgb(0, 0, 0, 0);
         style.Foreground = Color.FromArgb(255, 70, 70, 70);
         style.Font = DefaultsService.GetDefaultFont(16);
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
         style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;

         Style cursorStyle = new Style();
         cursorStyle.Background = Color.FromArgb(255, 60, 60, 60);
         cursorStyle.Width = 2;
         cursorStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
         cursorStyle.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
         cursorStyle.Margin = new Indents(0, 5, 0, 5);
         cursorStyle.IsVisible = false;
         style.AddInnerStyle("cursor", cursorStyle);

         Style selectionStyle = new Style();
         selectionStyle.Background = Color.FromArgb(255, 111, 181, 255);
         selectionStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
         selectionStyle.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
         style.AddInnerStyle("selection", selectionStyle);

         Style substrateStyle = new Style();
         substrateStyle.Font = DefaultsService.GetDefaultFont(FontStyle.Italic, 14);
         substrateStyle.Foreground = Color.FromArgb(255, 150, 150, 150);
         style.AddInnerStyle("substrate", substrateStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a TextEdit item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "text".
      /// <para/> Inner styles for "text": "cursor", "selection", "substrate".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetTextEditStyle()
      {
         Style style = new Style();
         style.Font = DefaultsService.GetDefaultFont(16);
         style.Background = Color.FromArgb(255, 210, 210, 210);
         style.Height = 30;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Fixed;

         Style textStyle = new Style();
         textStyle.Background = Color.Transparent;
         textStyle.Foreground = Color.FromArgb(255, 70, 70, 70);
         textStyle.Font = DefaultsService.GetDefaultFont(16);
         textStyle.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
         textStyle.Alignment = ItemAlignment.Left | ItemAlignment.Top;
         textStyle.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
         textStyle.Padding = new Indents(5, 0, 5, 0);
         style.AddInnerStyle("text", textStyle);

         Style cursorStyle = new Style();
         cursorStyle.Background = Color.FromArgb(255, 60, 60, 60);
         cursorStyle.Width = 2;
         cursorStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
         cursorStyle.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
         cursorStyle.Margin = new Indents(0, 5, 0, 5);
         cursorStyle.IsVisible = false;
         textStyle.AddInnerStyle("cursor", cursorStyle);

         Style selectionStyle = new Style();
         selectionStyle.Background = Color.FromArgb(255, 111, 181, 255);
         selectionStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
         selectionStyle.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
         selectionStyle.Margin = new Indents(0, 5, 0, 5);
         textStyle.AddInnerStyle("selection", selectionStyle);

         Style substrateStyle = new Style();
         substrateStyle.Font = DefaultsService.GetDefaultFont(FontStyle.Italic, 14);
         substrateStyle.Foreground = Color.FromArgb(255, 150, 150, 150);
         textStyle.AddInnerStyle("substrate", substrateStyle);

         return style;
      }

      private static Style GetTextFieldStyle()
      {
         Style style = new Style();
         style.Background = Color.Transparent;
         style.Foreground = Color.FromArgb(255, 70, 70, 70);
         style.Font = DefaultsService.GetDefaultFont(16);
         style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
         style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
         style.Padding = new Indents(5, 0, 5, 0);
         style.AddInnerStyle("text", style);

         Style cursorStyle = new Style();
         cursorStyle.Background = Color.FromArgb(255, 60, 60, 60);
         cursorStyle.Width = 2;
         cursorStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
         cursorStyle.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
         cursorStyle.Margin = new Indents(0, 5, 0, 5);
         cursorStyle.IsVisible = false;
         style.AddInnerStyle("cursor", cursorStyle);

         Style selectionStyle = new Style();
         selectionStyle.Background = Color.FromArgb(255, 111, 181, 255);
         selectionStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
         selectionStyle.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
         selectionStyle.Margin = new Indents(0, 5, 0, 5);
         style.AddInnerStyle("selection", selectionStyle);

         Style substrateStyle = new Style();
         substrateStyle.Font = DefaultsService.GetDefaultFont(FontStyle.Italic, 14);
         substrateStyle.Foreground = Color.FromArgb(255, 150, 150, 150);
         style.AddInnerStyle("substrate", substrateStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a sealed TextBlock item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "cursor", "selection".
      /// <para/> This is part of TextArea item style as "textedit".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetTextBlockStyle()
      {
         Style style = new Style();
         style.Background = Color.Transparent;
         style.Foreground = Color.FromArgb(40, 40, 40);
         style.Font = DefaultsService.GetDefaultFont(16);
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
         style.TextAlignment = ItemAlignment.Left | ItemAlignment.Top;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Padding = new Indents(5, 5, 5, 5);

         Style cursorStyle = new Style();
         cursorStyle.Background = Color.FromArgb(255, 60, 60, 60);
         cursorStyle.Width = 2;
         cursorStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         cursorStyle.IsVisible = false;
         style.AddInnerStyle("cursor", cursorStyle);

         Style selectionStyle = new Style();
         selectionStyle.Background = Color.FromArgb(255, 111, 181, 255);
         selectionStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         style.AddInnerStyle("selection", selectionStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a TextArea item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "textedit", "vscrollbar", "hscrollbar", "menu".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetTextAreaStyle()
      {
         Style style = new Style();
         style.Background = Color.FromArgb(210, 210, 210);
         style.Foreground = Color.FromArgb(40, 40, 40);
         style.Font = DefaultsService.GetDefaultFont(16);
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

         Style textStyle = GetTextBlockStyle();
         style.AddInnerStyle("textedit", textStyle);

         Style vsbStyle = GetVerticalScrollBarStyle();
         vsbStyle.Alignment = ItemAlignment.Right | ItemAlignment.Top;
         style.AddInnerStyle("vscrollbar", vsbStyle);

         Style hsbStyle = GetHorizontalScrollBarStyle();
         hsbStyle.Alignment = ItemAlignment.Left | ItemAlignment.Bottom;
         style.AddInnerStyle("hscrollbar", hsbStyle);

         Style menuStyle = new Style();
         menuStyle.Background = Color.FromArgb(50, 50, 50);
         menuStyle.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
         menuStyle.SetAlignment(ItemAlignment.Right | ItemAlignment.Bottom);
         style.AddInnerStyle("menu", menuStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a TextView item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "selection".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetTextViewStyle()
      {
         Style style = new Style();
         style.Background = Color.Transparent;
         style.Foreground = Color.FromArgb(210, 210, 210);
         style.Font = DefaultsService.GetDefaultFont(16);
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
         style.SetPadding(5, 5, 5, 5);

         Style selectionStyle = new Style();
         selectionStyle.Background = Color.FromArgb(40, 255, 255, 255);
         selectionStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         style.AddInnerStyle("selection", selectionStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a PopUpMessage item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "closebutton".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetPopUpMessageStyle()
      {
         Style style = new Style();
         style.Background = Color.FromArgb(255, 45, 45, 45);
         style.Foreground = Color.LightGray;
         style.Font = DefaultsService.GetDefaultFont(14);
         style.Alignment = ItemAlignment.Bottom | ItemAlignment.Right;
         style.TextAlignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
         style.SetSize(300, 70);
         style.WidthPolicy = SizePolicy.Fixed;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Padding = new Indents(5, 5, 5, 5);
         style.Margin = new Indents(10, 10, 10, 10);
         style.SetShadow(new Shadow(10, 3, 3, Color.FromArgb(140, 0, 0, 0)));
         style.IsShadowDrop = true;
         style.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            Background = Color.FromArgb(3, 255, 255, 255)
         });

         Style closeStyle = new Style();
         closeStyle.Background = Color.FromArgb(255, 100, 100, 100);
         closeStyle.SetSize(10, 10);
         closeStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         closeStyle.Alignment = ItemAlignment.Top | ItemAlignment.Right;
         closeStyle.Margin = new Indents(0, 5, 0, 5);
         closeStyle.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            Background = Color.FromArgb(60, 255, 255, 255)
         });
         closeStyle.Shape = GraphicsMathService.GetCross(10, 10, 3, 45);
         closeStyle.IsFixedShape = false;
         style.AddInnerStyle("closebutton", closeStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a ProgressBar item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "progressbar".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetProgressBarStyle()
      {
         Style style = new Style();

         style.Font = DefaultsService.GetDefaultFont();
         style.Background = Color.FromArgb(255, 70, 70, 70);
         style.Height = 20;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
         style.TextAlignment = ItemAlignment.HCenter | ItemAlignment.VCenter;

         Style pgbarStyle = new Style();
         pgbarStyle.Background = Color.FromArgb(255, 0, 191, 255);
         pgbarStyle.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
         pgbarStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
         style.AddInnerStyle("progressbar", pgbarStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a ToolTip item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "text".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetToolTipStyle()
      {
         Style style = new Style();

         style.Font = DefaultsService.GetDefaultFont(12);
         style.Background = Color.White;
         style.Foreground = Color.FromArgb(255, 70, 70, 70);
         style.Height = 30;
         style.WidthPolicy = SizePolicy.Fixed;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
         style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
         style.Padding = new Indents(5, 5, 5, 5);
         style.BorderRadius = new CornerRadius(4);

         Style textStyle = new Style();
         textStyle.Background = Color.Transparent;
         textStyle.WidthPolicy = SizePolicy.Expand;
         textStyle.HeightPolicy = SizePolicy.Expand;
         textStyle.Alignment = ItemAlignment.VCenter | ItemAlignment.HCenter;
         textStyle.TextAlignment = ItemAlignment.VCenter | ItemAlignment.HCenter;
         style.AddInnerStyle("text", textStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a TitleBar item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "closebutton", "minimizebutton", "maximizebutton", "title".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetTitleBarStyle()
      {
         Style style = new Style();

         style.Font = DefaultsService.GetDefaultFont();
         // style.Font = new Font(style.Font.FontFamily, 16, style.Font.Style);
         style.Background = Color.FromArgb(255, 45, 45, 45);
         style.Foreground = Color.FromArgb(255, 180, 180, 180);
         style.Height = 30;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
         style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
         style.Padding = new Indents(10, 0, 5, 0);
         style.Spacing = new Spacing(5);

         Style closeStyle = new Style();
         closeStyle.Background = Color.FromArgb(255, 100, 100, 100);
         closeStyle.SetSize(15, 15);
         closeStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         closeStyle.Alignment = ItemAlignment.VCenter | ItemAlignment.Right;
         closeStyle.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            Background = Color.FromArgb(255, 186, 95, 97)
         });
         closeStyle.Shape = GraphicsMathService.GetCross(15, 15, 2, 45);
         closeStyle.IsFixedShape = true;
         style.AddInnerStyle("closebutton", closeStyle);

         Style minimizeStyle = new Style();
         minimizeStyle.Background = Color.FromArgb(255, 100, 100, 100);
         minimizeStyle.SetSize(12, 15);
         minimizeStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         minimizeStyle.Alignment = ItemAlignment.Bottom | ItemAlignment.Right;
         minimizeStyle.Margin = new Indents(0, 0, 5, 9);
         minimizeStyle.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            Background = Color.FromArgb(80, 255, 255, 255)
         });
         minimizeStyle.Shape = GraphicsMathService.GetRectangle(15, 2, 0, 13);
         minimizeStyle.IsFixedShape = true;
         style.AddInnerStyle("minimizebutton", minimizeStyle);

         Style maximizeStyle = new Style();

         maximizeStyle.BorderThickness = 2;
         maximizeStyle.BorderFill = Color.FromArgb(255, 100, 100, 100);

         maximizeStyle.SetSize(12, 12);
         maximizeStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         maximizeStyle.Alignment = ItemAlignment.Bottom | ItemAlignment.Right;
         maximizeStyle.Margin = new Indents(0, 0, 0, 9);
         maximizeStyle.Padding = new Indents(2, 2, 2, 2);

         ItemState hovered = new ItemState();
         hovered.Border.SetFill(Color.FromArgb(255, 84, 124, 94));
         maximizeStyle.AddItemState(ItemStateType.Hovered, hovered);
         maximizeStyle.Shape = GraphicsMathService.GetRectangle();
         style.AddInnerStyle("maximizebutton", maximizeStyle);

         Style titleStyle = new Style();
         titleStyle.Margin = new Indents(10, 0, 0, 0);
         style.AddInnerStyle("title", titleStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a TreeView item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "area", "vscrollbar", "hscrollbar", "menu".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetTreeViewStyle()
      {
         Style style = GetListBoxStyle();
         return style;
      }

      /// <summary>
      /// Getting default style for a TreeItem item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "indicator", "branchicon", "leaficon".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetTreeItemStyle()
      {
         Style style = new Style();
         style.Background = Color.FromArgb(0, 0, 0, 0);
         style.Foreground = Color.FromArgb(255, 210, 210, 210);
         style.Font = DefaultsService.GetDefaultFont();
         style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
         style.Height = 25;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
         style.TextAlignment = ItemAlignment.VCenter | ItemAlignment.Left;
         style.Spacing = new Spacing(5, 0);
         style.Padding = new Indents(5, 0, 0, 0);
         ItemState hovered = new ItemState();
         hovered.Background = Color.FromArgb(30, 255, 255, 255);
         style.AddItemState(ItemStateType.Hovered, hovered);

         Style indicatorStyle = new Style();
         indicatorStyle.Background = Color.FromArgb(255, 32, 32, 32);
         indicatorStyle.Foreground = Color.FromArgb(255, 210, 210, 210);
         indicatorStyle.Font = DefaultsService.GetDefaultFont();
         indicatorStyle.SetSize(15, 15);
         indicatorStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         indicatorStyle.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
         indicatorStyle.TextAlignment = ItemAlignment.VCenter | ItemAlignment.Left;
         indicatorStyle.Shape = GraphicsMathService.GetTriangle(10, 8, 0, 3, 90);
         indicatorStyle.IsFixedShape = true;
         ItemState toggled = new ItemState();
         toggled.Background = Color.FromArgb(255, 160, 160, 160);
         toggled.Shape = new Figure(true, GraphicsMathService.GetTriangle(10, 8, 0, 3, 180));
         indicatorStyle.AddItemState(ItemStateType.Toggled, toggled);
         style.AddInnerStyle("indicator", indicatorStyle);

         Style branchIconStyle = new Style();
         branchIconStyle.Background = Color.FromArgb(255, 106, 185, 255);
         branchIconStyle.SetSize(14, 9);
         branchIconStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         branchIconStyle.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
         branchIconStyle.Shape = GraphicsMathService.GetFolderIconShape(20, 15, 0, 0);
         style.AddInnerStyle("branchicon", branchIconStyle);

         Style leafIconStyle = new Style();
         leafIconStyle.Background = Color.FromArgb(255, 129, 187, 133);
         leafIconStyle.SetSize(6, 6);
         leafIconStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         leafIconStyle.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
         leafIconStyle.Shape = GraphicsMathService.GetEllipse(3, 16);
         leafIconStyle.Margin = new Indents(2, 0, 0, 0);
         style.AddInnerStyle("leaficon", leafIconStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a SpinItem item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "uparrow", "downarrow", "buttonsarea", "textedit".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetSpinItemStyle()
      {
         Style style = new Style();

         style.Background = Color.FromArgb(50, 50, 50);
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Fixed;
         style.Height = 30;
         style.MinHeight = 10;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

         Style uparrowStyle = GetButtonCoreStyle();
         uparrowStyle.WidthPolicy = SizePolicy.Expand;
         uparrowStyle.HeightPolicy = SizePolicy.Expand;
         uparrowStyle.SetMargin(4, 4, 4, 5);
         uparrowStyle.Background = Color.FromArgb(255, 50, 50, 50);
         uparrowStyle.Alignment = ItemAlignment.Top | ItemAlignment.VCenter;
         uparrowStyle.Shape = GraphicsMathService.GetTriangle(12, 6, 0, 0, 0);
         uparrowStyle.IsFixedShape = true;

         ItemState hovered = new ItemState();
         hovered.Background = Color.FromArgb(30, 255, 255, 255);
         uparrowStyle.AddItemState(ItemStateType.Hovered, hovered);

         style.AddInnerStyle("uparrow", uparrowStyle);

         Style downarrowStyle = GetButtonCoreStyle();
         downarrowStyle.WidthPolicy = SizePolicy.Expand;
         downarrowStyle.HeightPolicy = SizePolicy.Expand;
         downarrowStyle.SetMargin(4, 5, 4, 4);
         downarrowStyle.Background = Color.FromArgb(255, 50, 50, 50);
         downarrowStyle.Alignment = ItemAlignment.Bottom | ItemAlignment.VCenter;
         downarrowStyle.Shape = GraphicsMathService.GetTriangle(12, 6, 0, 0, 180);
         downarrowStyle.IsFixedShape = true;
         downarrowStyle.AddItemState(ItemStateType.Hovered, hovered);
         style.AddInnerStyle("downarrow", downarrowStyle);

         Style btnsArea = GetVerticalStackStyle();
         btnsArea.WidthPolicy = SizePolicy.Fixed;
         btnsArea.HeightPolicy = SizePolicy.Expand;
         btnsArea.Width = 20;
         btnsArea.Background = Color.FromArgb(255, 255, 181, 111);
         btnsArea.Alignment = ItemAlignment.Right | ItemAlignment.VCenter;
         style.AddInnerStyle("buttonsarea", btnsArea);

         Style textInput = GetTextFieldStyle();
         textInput.Background = Color.FromArgb(210, 210, 210);
         textInput.HeightPolicy = SizePolicy.Expand;
         textInput.TextAlignment = ItemAlignment.Right;
         textInput.Padding.Right = 10;
         style.AddInnerStyle("textedit", textInput);

         return style;
      }

      /// <summary>
      /// Getting default style for a DialogItem item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "window".
      /// <para/> This is part of OpenEntryDialog item style.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetDialogItemStyle()
      {
         Style style = new Style();
         style.SetAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
         style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
         style.SetBackground(0, 0, 0, 150);
         style.BorderRadius = new CornerRadius(0);
         style.Padding = new Indents();
         style.Margin = new Indents();
         style.Spacing = new Spacing();

         Style windowStyle = GetFrameStyle();
         windowStyle.SetSize(300, 150);
         windowStyle.SetMinSize(300, 150);
         windowStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         windowStyle.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
         windowStyle.SetPadding(2, 2, 2, 2);
         windowStyle.SetBackground(45, 45, 45);
         windowStyle.SetShadow(new Shadow(5, 3, 3, Color.FromArgb(180, 0, 0, 0)));
         windowStyle.IsShadowDrop = true;

         style.AddInnerStyle("window", windowStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a MessageItem item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "window", "button", "toolbar", "userbar", "message", "layout".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetMessageItemStyle()
      {
         Style style = new Style();
         style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
         style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
         style.SetBackground(0, 0, 0, 150);
         style.BorderRadius = new CornerRadius();

         Style windowStyle = GetFrameStyle();
         windowStyle.SetSize(300, 150);
         windowStyle.SetMinSize(300, 150);
         windowStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         windowStyle.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
         windowStyle.SetPadding(2, 2, 2, 2);
         windowStyle.SetBackground(45, 45, 45);
         style.AddInnerStyle("window", windowStyle);

         Style okStyle = GetButtonCoreStyle();
         okStyle.SetBackground(100, 255, 150);
         okStyle.SetSize(100, 30);
         okStyle.SetAlignment(ItemAlignment.Left, ItemAlignment.VCenter);
         okStyle.SetShadow(new Shadow(5, 2, 2, Color.FromArgb(120, 0, 0, 0)));
         okStyle.IsShadowDrop = true;
         style.AddInnerStyle("button", okStyle);

         Style toolbarStyle = GetHorizontalStackStyle();
         toolbarStyle.SetAlignment(ItemAlignment.HCenter, ItemAlignment.Bottom);
         toolbarStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         toolbarStyle.SetSpacing(10, 0);
         toolbarStyle.SetPadding(0, 0, 0, 0);
         toolbarStyle.SetMargin(0, 0, 0, 0);
         style.AddInnerStyle("toolbar", toolbarStyle);

         Style userbarStyle = GetHorizontalStackStyle();
         userbarStyle.SetAlignment(ItemAlignment.HCenter, ItemAlignment.Bottom);
         userbarStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         userbarStyle.SetSpacing(10, 0);
         userbarStyle.SetPadding(0, 0, 0, 0);
         userbarStyle.SetMargin(0, 0, 0, 0);
         style.AddInnerStyle("userbar", userbarStyle);

         Style msgStyle = GetLabelStyle();
         msgStyle.SetAlignment(ItemAlignment.Top, ItemAlignment.VCenter);
         msgStyle.SetTextAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
         msgStyle.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
         msgStyle.SetMargin(10, 0, 10, 40);
         style.AddInnerStyle("message", msgStyle);

         Style layoutStyle = GetFrameStyle();
         layoutStyle.SetMargin(0, 30, 0, 0);
         layoutStyle.SetPadding(6, 6, 6, 15);
         layoutStyle.SetBackground(255, 255, 255, 20);
         style.AddInnerStyle("layout", layoutStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a window itself. Properly filled in all the necessary properties.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetWindowContainerStyle()
      {
         Style style = new Style();

         style.SetBackground(45, 45, 45);
         style.SetMinSize(200, 200);
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.SetPadding(2, 2, 2, 2);
         style.SetAlignment(ItemAlignment.Left, ItemAlignment.Top);

         return style;
      }

      /// <summary>
      /// Getting default style for a FileSystemEntry item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "icon", "text".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetFileSystemEntryStyle()
      {
         Style style = new Style();
         style.Height = 25;
         style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
         style.SetBackground(0, 0, 0, 0);
         style.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Left);
         style.SetTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);
         style.Font = DefaultsService.GetDefaultFont();
         style.SetForeground(210, 210, 210);
         style.SetPadding(10, 0, 0, 0);
         style.AddItemState(ItemStateType.Hovered, new ItemState(Color.FromArgb(30, 255, 255, 255)));

         Style iconStyle = GetFrameStyle();
         iconStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         iconStyle.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Left);

         style.AddInnerStyle("icon", iconStyle);

         Style textStyle = new Style();
         textStyle.SetMargin(24, 0, 0, 0);

         style.AddInnerStyle("text", textStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a OpenEntryDialog item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "window", "layout", "toolbar", "toolbarbutton", 
      /// "addressline", "filenameline", "list", "controlpanel", "okbutton", 
      /// "cancelbutton", "filter", "filtertext", "divider".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetOpenEntryDialogStyle()
      {
         // common
         Style style = new Style();
         style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
         style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
         style.SetBackground(0, 0, 0, 150);
         style.BorderRadius = new CornerRadius();
         style.Padding = new Indents();
         style.Margin = new Indents();
         style.Spacing = new Spacing();

         // window
         Style windowStyle = GetDialogItemStyle().GetInnerStyle("window");
         windowStyle.SetSize(500, 700);
         windowStyle.SetMinSize(400, 400);
         windowStyle.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
         windowStyle.SetMargin(150, 20, 150, 20);
         style.AddInnerStyle("window", windowStyle);
         // layout
         Style layoutStyle = GetVerticalStackStyle();
         layoutStyle.SetMargin(0, 30, 0, 0);
         layoutStyle.SetPadding(6, 6, 6, 6);
         layoutStyle.SetSpacing(0, 2);
         layoutStyle.SetBackground(255, 255, 255, 20);
         style.AddInnerStyle("layout", layoutStyle);
         // toolbar
         Style toolbarStyle = GetHorizontalStackStyle();
         toolbarStyle.HeightPolicy = SizePolicy.Fixed;
         toolbarStyle.Height = 30;
         toolbarStyle.SetBackground(40, 40, 40);
         toolbarStyle.SetSpacing(3, 0);
         toolbarStyle.SetPadding(6, 0, 0, 0);
         style.AddInnerStyle("toolbar", toolbarStyle);
         // toolbarbutton
         Style toolbarbuttonStyle = Style.GetButtonCoreStyle();
         toolbarbuttonStyle.SetSize(24, 30);
         toolbarbuttonStyle.Background = toolbarStyle.Background;
         toolbarbuttonStyle.AddItemState(ItemStateType.Hovered, new ItemState(Color.FromArgb(60, 255, 255, 255)));
         toolbarbuttonStyle.AddItemState(ItemStateType.Pressed, new ItemState(Color.FromArgb(30, 255, 255, 255)));
         toolbarbuttonStyle.BorderRadius = new CornerRadius();
         toolbarbuttonStyle.SetPadding(3, 6, 3, 6);
         style.AddInnerStyle("toolbarbutton", toolbarbuttonStyle);
         // buttonhidden
         Style buttonhiddenStyle = GetButtonToggleStyle();
         buttonhiddenStyle.SetSize(24, 30);
         buttonhiddenStyle.BorderRadius = new CornerRadius();
         buttonhiddenStyle.Background = toolbarStyle.Background;
         buttonhiddenStyle.SetPadding(4, 6, 4, 6);
         buttonhiddenStyle.AddItemState(ItemStateType.Toggled, new ItemState(Color.FromArgb(30, 153, 91)));
         style.AddInnerStyle("buttonhidden", buttonhiddenStyle);
         // addressline
         Style addresslineStyle = GetTextEditStyle();
         addresslineStyle.Font = DefaultsService.GetDefaultFont(12);
         addresslineStyle.GetInnerStyle("text").Font = DefaultsService.GetDefaultFont(12);
         addresslineStyle.GetInnerStyle("text").SetForeground(210, 210, 210);
         addresslineStyle.SetBackground(50, 50, 50);
         addresslineStyle.Height = 24;
         addresslineStyle.SetMargin(0, 5, 0, 0);
         style.AddInnerStyle("addressline", addresslineStyle);
         // filenameline
         Style filenamelineStyle = GetTextEditStyle();
         filenamelineStyle.Font = DefaultsService.GetDefaultFont(12);
         filenamelineStyle.GetInnerStyle("text").Font = DefaultsService.GetDefaultFont(12);
         filenamelineStyle.GetInnerStyle("text").SetForeground(210, 210, 210);
         filenamelineStyle.SetBackground(50, 50, 50);
         filenamelineStyle.Height = 24;
         filenamelineStyle.SetMargin(0, 2, 0, 0);
         style.AddInnerStyle("filenameline", filenamelineStyle);
         // list
         Style listStyle = GetListBoxStyle();
         style.AddInnerStyle("list", listStyle);
         // controlpanel
         Style controlpanelStyle = GetFrameStyle();
         controlpanelStyle.HeightPolicy = SizePolicy.Fixed;
         controlpanelStyle.Height = 45;
         controlpanelStyle.SetBackground(45, 45, 45);
         controlpanelStyle.SetPadding(6, 6, 6, 6);
         style.AddInnerStyle("controlpanel", controlpanelStyle);
         // button
         Style okbuttonStyle = GetButtonCoreStyle();
         okbuttonStyle.SetSize(100, 30);
         okbuttonStyle.SetAlignment(ItemAlignment.VCenter, ItemAlignment.Right);
         okbuttonStyle.SetMargin(0, 0, 110, 0);
         okbuttonStyle.SetShadow(new Shadow(5, 2, 2, Color.FromArgb(180, 0, 0, 0)));
         okbuttonStyle.IsShadowDrop = true;
         style.AddInnerStyle("okbutton", okbuttonStyle);

         Style cancelbuttonStyle = GetButtonCoreStyle();
         cancelbuttonStyle.SetSize(100, 30);
         cancelbuttonStyle.SetAlignment(ItemAlignment.VCenter, ItemAlignment.Right);
         cancelbuttonStyle.SetShadow(new Shadow(5, 2, 2, Color.FromArgb(180, 0, 0, 0)));
         cancelbuttonStyle.IsShadowDrop = true;
         style.AddInnerStyle("cancelbutton", cancelbuttonStyle);

         Style filterStyle = GetButtonCoreStyle();
         filterStyle.SetSize(24, 30);
         filterStyle.BorderRadius = new CornerRadius();
         filterStyle.SetBackground(35, 35, 35);
         filterStyle.SetPadding(4, 6, 4, 6);
         filterStyle.SetMargin(5, 0, 0, 0);
         style.AddInnerStyle("filter", filterStyle);

         Style filtertextStyle = GetLabelStyle();
         filtertextStyle.WidthPolicy = SizePolicy.Fixed;
         filtertextStyle.SetTextAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
         filtertextStyle.SetPadding(10, 2, 10, 0);
         filtertextStyle.SetMargin(-3, 0, 0, 0);
         filtertextStyle.SetBackground(55, 55, 55);
         filtertextStyle.Font = DefaultsService.GetDefaultFont();
         style.AddInnerStyle("filtertext", filtertextStyle);

         Style dividerStyle = GetFrameStyle();
         dividerStyle.WidthPolicy = SizePolicy.Fixed;
         dividerStyle.Width = 1;
         dividerStyle.SetBackground(55, 55, 55);
         dividerStyle.SetMargin(0, 3, 0, 3);
         style.AddInnerStyle("divider", dividerStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a InputDialog item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "window", "button", "textedit", "layout", "toolbar".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetInputDialogStyle()
      {
         Style style = new Style();
         style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
         style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
         style.SetBackground(0, 0, 0, 150);
         style.BorderRadius = new CornerRadius();
         style.Padding = new Indents();
         style.Margin = new Indents();
         style.Spacing = new Spacing();

         Style windowStyle = GetFrameStyle();
         windowStyle.SetSize(300, 150);
         windowStyle.SetMinSize(300, 150);
         windowStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         windowStyle.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
         windowStyle.SetPadding(2, 2, 2, 2);
         windowStyle.SetBackground(45, 45, 45);
         style.AddInnerStyle("window", windowStyle);

         Style okStyle = GetButtonCoreStyle();
         okStyle.SetBackground(100, 255, 150);
         okStyle.Foreground = Color.Black;
         okStyle.SetSize(100, 30);
         okStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         okStyle.SetAlignment(ItemAlignment.Left, ItemAlignment.Bottom);
         okStyle.SetMargin(0, 0, 0, 0);
         okStyle.BorderRadius = new CornerRadius();
         okStyle.SetShadow(new Shadow(5, 2, 2, Color.FromArgb(120, 0, 0, 0)));
         okStyle.IsShadowDrop = true;
         okStyle.AddItemState(ItemStateType.Hovered, new ItemState(Color.FromArgb(80, 255, 255, 255)));
         style.AddInnerStyle("button", okStyle);

         Style textStyle = GetTextEditStyle();
         textStyle.SetAlignment(ItemAlignment.HCenter, ItemAlignment.Top);
         textStyle.SetTextAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
         textStyle.SetMargin(0, 15, 0, 0);
         style.AddInnerStyle("textedit", textStyle);

         Style layoutStyle = GetFrameStyle();
         layoutStyle.SetMargin(0, 30, 0, 0);
         layoutStyle.SetPadding(6, 6, 6, 15);
         layoutStyle.SetBackground(255, 255, 255, 20);
         style.AddInnerStyle("layout", layoutStyle);

         Style toolbarStyle = GetHorizontalStackStyle();
         toolbarStyle.SetAlignment(ItemAlignment.HCenter, ItemAlignment.Bottom);
         toolbarStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         toolbarStyle.SetSpacing(10, 0);
         style.AddInnerStyle("toolbar", toolbarStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a SelectionItem item. Properly filled in all the necessary properties.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetSelectionItemStyle()
      {
         Style style = new Style();
         style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
         style.SetPadding(0, 1, 0, 1);
         style.SetAlignment(ItemAlignment.Left, ItemAlignment.Top);
         style.SetBackground(0, 0, 0, 0);
         style.AddItemState(ItemStateType.Toggled, new ItemState(Color.FromArgb(50, 255, 255, 255)));
         return style;
      }

      /// <summary>
      /// Getting default style for a WrapArea item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "selection".
      /// <para/> This is part of WrapGrid item style as "area".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetWrapAreaStyle()
      {
         Style style = new Style();

         style.Background = Color.Transparent;
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
         style.Padding = new Indents(2, 2, 2, 2);
         style.Spacing = new Spacing(0, 5);

         Style selectionStyle = GetSelectionItemStyle();
         style.AddInnerStyle("selection", selectionStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a WrapGrid item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "area", "vscrollbar", "hscrollbar".
      /// <para/> Inner styles for "area": see Style.GetWrapAreaStyle().
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetWrapGridStyle()
      {
         Style style = new Style();

         style.Background = Color.FromArgb(255, 70, 70, 70);
         style.WidthPolicy = SizePolicy.Expand;
         style.HeightPolicy = SizePolicy.Expand;
         style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

         Style vsbStyle = GetVerticalScrollBarStyle();
         vsbStyle.Alignment = ItemAlignment.Right | ItemAlignment.Top;
         style.AddInnerStyle("vscrollbar", vsbStyle);

         Style hsbStyle = GetHorizontalScrollBarStyle();
         hsbStyle.Alignment = ItemAlignment.Left | ItemAlignment.Bottom;
         style.AddInnerStyle("hscrollbar", hsbStyle);

         Style areaStyle = GetWrapAreaStyle();
         style.AddInnerStyle("area", areaStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a SideArea item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "window", "closebutton".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetSideAreaStyle()
      {
         Style style = new Style();
         style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
         style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
         style.SetBackground(0, 0, 0, 130);
         style.BorderRadius = new CornerRadius(0);

         Style windowStyle = GetFrameStyle();
         windowStyle.SetPadding(2, 2, 2, 2);
         windowStyle.SetBackground(40, 40, 40);
         windowStyle.SetAlignment(ItemAlignment.Top, ItemAlignment.Left);
         style.AddInnerStyle("window", windowStyle);

         Style closeStyle = new Style();
         closeStyle.SetMargin(0, 5, 0, 0);
         closeStyle.Font = DefaultsService.GetDefaultFont();
         closeStyle.Background = Color.FromArgb(100, 100, 100);
         closeStyle.Foreground = Color.Transparent;
         closeStyle.SetSize(15, 15);
         closeStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         closeStyle.Alignment = ItemAlignment.Top | ItemAlignment.Right;
         closeStyle.TextAlignment = ItemAlignment.Right | ItemAlignment.Top;
         ItemState close_hovered = new ItemState();
         close_hovered.Background = Color.FromArgb(186, 95, 97);
         closeStyle.AddItemState(ItemStateType.Hovered, close_hovered);

         closeStyle.Shape = GraphicsMathService.GetCross(15, 15, 2, 45);
         closeStyle.IsFixedShape = true;
         style.AddInnerStyle("closebutton", closeStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a ImageItem item. Properly filled in all the necessary properties.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetImageItemStyle()
      {
         Style style = new Style();
         style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
         style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
         style.SetBackground(0, 0, 0, 0);

         return style;
      }

      /// <summary>
      /// Getting default style for a LoadingScreen item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "text", "image".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetLoadingScreenStyle()
      {
         Style style = new Style();
         style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
         style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
         style.SetBackground(0, 0, 0, 150);

         Style textStyle = GetLabelStyle();
         textStyle.SetAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
         // text_style.WidthPolicy = SizePolicy.Fixed;
         // text_style.Width = 64;
         // text_style.SetMargin(0, 0, 15, 0);
         textStyle.SetTextAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
         textStyle.Font = DefaultsService.GetDefaultFont(FontStyle.Bold, 14);
         style.AddInnerStyle("text", textStyle);

         Style imageStyle = GetImageItemStyle();
         imageStyle.SetMaxSize(64, 64);
         style.AddInnerStyle("image", imageStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a Tab item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "text", "closebutton", "view".
      /// <para/> This is part of TabView item style as "tab".
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetTabStyle()
      {
         Style style = new Style();
         style.BorderRadius = new CornerRadius(3, 3, 0, 0);
         style.Font = DefaultsService.GetDefaultFont(14);
         // style.Background = Color.Transparent;
         // style.Background = Color.FromArgb(60, 60, 60);
         style.Background = Color.FromArgb(10, 255, 255, 255);
         style.Foreground = Color.FromArgb(255, 210, 210, 210);
         style.MinWidth = 30;
         style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
         style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
         style.Padding = new Indents(10, 2, 5, 2);
         style.Spacing = new Spacing(5, 0);
         style.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            // Background = Color.FromArgb(20, 255, 255, 255)
            Background = Color.FromArgb(60, 255, 255, 255)
         });
         style.AddItemState(ItemStateType.Toggled, new ItemState()
         {
            // Background = Color.FromArgb(71, 71, 71)
            Background = Color.FromArgb(25, 255, 255, 255)
         });
         style.SetShadow(new Shadow(5, 0, 0, Color.FromArgb(150, 0, 0, 0)));
         style.IsShadowDrop = false;

         Style textStyle = GetLabelStyle();
         style.AddInnerStyle("text", textStyle);

         Style closeStyle = new Style();
         closeStyle.Background = Color.FromArgb(255, 100, 100, 100);
         closeStyle.SetSize(10, 10);
         closeStyle.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
         closeStyle.Alignment = ItemAlignment.VCenter | ItemAlignment.Right;
         closeStyle.AddItemState(ItemStateType.Hovered, new ItemState()
         {
            // Background = Color.FromArgb(255, 186, 95, 97)
            Background = Color.FromArgb(255, 0, 162, 232)
         });
         closeStyle.Shape = GraphicsMathService.GetCross(10, 10, 2, 45);
         closeStyle.IsFixedShape = true;
         style.AddInnerStyle("closebutton", closeStyle);

         Style viewStyle = new Style();
         viewStyle.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
         viewStyle.Background = Color.FromArgb(255, 71, 71, 71);
         viewStyle.IsVisible = false;
         viewStyle.Padding = new Indents(2, 2, 2, 2);
         style.AddInnerStyle("view", viewStyle);

         return style;
      }

      /// <summary>
      /// Getting default style for a TabBar item. Properly filled in all the necessary properties.
      /// <para/> This is part of TabView item style.
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetTabBarStyle()
      {
         Style style = GetHorizontalStackStyle();
         style.SetSpacing(1, 0);
         return style;
      }

      /// <summary>
      /// Getting default style for a *** item. Properly filled in all the necessary properties.
      /// <para/> Inner styles: "tabbar", "tab", "viewarea".
      /// <para/> Inner styles for "tab": see Style.GetTabStyle().
      /// </summary>
      /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
      public static Style GetTabViewStyle()
      {
         Style style = GetVerticalStackStyle();
         style.Background = Color.FromArgb(255, 50, 50, 50);

         Style tabBarStyle = GetTabBarStyle();
         tabBarStyle.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
         tabBarStyle.Height = 30;
         style.AddInnerStyle("tabbar", tabBarStyle);

         Style tabStyle = GetTabStyle();
         style.AddInnerStyle("tab", tabStyle);

         // Style view_style = tab_style.GetInnerStyle("view");
         // if (view_style != null)
         //     style.AddInnerStyle("view", view_style);

         Style areaStyle = GetFrameStyle();
         areaStyle.SetPadding(0, 0, 0, 0);
         style.AddInnerStyle("viewarea", areaStyle);

         return style;
      }
   }
}
