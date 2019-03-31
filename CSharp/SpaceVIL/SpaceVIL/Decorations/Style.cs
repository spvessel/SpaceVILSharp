using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using SpaceVIL.Core;
using SpaceVIL.Common;

namespace SpaceVIL.Decorations
{
    /// <summary>
    /// A class that describes style Settings of the object
    /// </summary>
    public class Style
    {
        // private Lazy<ConcurrentDictionary<String, Style>> _inner_styles = new Lazy<ConcurrentDictionary<String, Style>>(() => new ConcurrentDictionary<String, Style>());
        private Dictionary<String, Style> _inner_styles = new Dictionary<String, Style>();

        //defaults values
        public Color Background;
        public Color Foreground;
        public Font Font = null;
        public SizePolicy WidthPolicy;
        public SizePolicy HeightPolicy;
        public int Width;
        public int MinWidth;
        public int MaxWidth;
        public int Height;
        public int MinHeight;
        public int MaxHeight;
        public ItemAlignment Alignment;
        public ItemAlignment TextAlignment;
        public int X;
        public int Y;
        private Dictionary<ItemStateType, ItemState> _item_states = new Dictionary<ItemStateType, ItemState>();
        public Indents Padding;// = new Indents();
        public Spacing Spacing;// = new Spacing();
        public Indents Margin;// = new Indents();
        public CornerRadius BorderRadius;
        public int BorderThickness;
        public Color BorderFill;
        public List<float[]> Shape;// = new List<float[]>();
        public List<IBaseItem> InnerShapes;// = new List<float[]>();
        public bool IsFixedShape;
        public bool IsVisible;

        /// <summary>
        /// Constructs a default Style
        /// </summary>
        public Style()//default
        {
            IsVisible = true;
            MaxWidth = Int16.MaxValue;
            MaxHeight = Int16.MaxValue;
            Alignment = ItemAlignment.Top | ItemAlignment.Left;
        }

        /// <summary>
        /// Sets object size (width and height)
        /// </summary>
        public void SetStyle(params IBaseItem[] items)
        {
            foreach (IBaseItem item in items)
                item.SetStyle(this);
        }

        /// <summary>
        /// Sets object size (width and height)
        /// </summary>
        public void SetSize(int width, int height)
        {
            Width = width;
            Height = height;
        }

        /// <summary>
        /// Sets object minimum size (width and height)
        /// </summary>
        public void SetMinSize(int width, int height)
        {
            MinWidth = width;
            MinHeight = height;
        }

        /// <summary>
        /// Sets object maximum size (width and height)
        /// </summary>
        public void SetMaxSize(int width, int height)
        {
            MaxWidth = width;
            MaxHeight = height;
        }

        /// <summary>
        /// Sets object size policy (fixed or expand) for width and height
        /// </summary>
        public void SetSizePolicy(SizePolicy width_policy, SizePolicy height_policy)
        {
            WidthPolicy = width_policy;
            HeightPolicy = height_policy;
        }

        /// <summary>
        /// Sets background color of the object
        /// </summary>
        /// <overloads> Sets background color of the object </overloads>
        public void SetBackground(int r, int g, int b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            Background = Color.FromArgb(255, r, g, b);
        }
        public void SetBackground(int r, int g, int b, int a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            Background = Color.FromArgb(a, r, g, b);
        }
        public void SetBackground(float r, float g, float b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            Background = Color.FromArgb(255, (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f));
        }
        public void SetBackground(float r, float g, float b, float a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            Background = Color.FromArgb((int)(a * 255.0f), (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f));
        }

        /// <summary>
        /// Sets text color of the object
        /// </summary>
        /// <overloads> Sets text color of the object </overloads>
        public void SetForeground(int r, int g, int b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            Foreground = Color.FromArgb(255, r, g, b);
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            Foreground = Color.FromArgb(a, r, g, b);
        }
        public void SetForeground(float r, float g, float b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            Foreground = Color.FromArgb(255, (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f));
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            Foreground = Color.FromArgb((int)(a * 255.0f), (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f));
        }

        /// <summary>
        /// Padding of the object
        /// </summary>
        public void SetPadding(Indents padding)
        {
            Padding = padding;
        }

        /// <summary>
        /// Padding of the object
        /// </summary>
        public void SetPadding(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            Padding.Left = left;
            Padding.Top = top;
            Padding.Right = right;
            Padding.Bottom = bottom;
        }

        /// <summary>
        /// Margin of the object
        /// </summary>
        public void SetMargin(Indents margin)
        {
            Margin = margin;
        }

        /// <summary>
        /// Margin of the object
        /// </summary>
        public void SetMargin(int left = 0, int top = 0, int right = 0, int bottom = 0)
        {
            Margin.Left = left;
            Margin.Top = top;
            Margin.Right = right;
            Margin.Bottom = bottom;
        }

        /// <summary>
        /// Spacing of the object
        /// </summary>
        public void SetSpacing(Spacing spacing)
        {
            Spacing = spacing;
        }

        /// <summary>
        /// Spacing of the object
        /// </summary>
        public void SetSpacing(int horizontal = 0, int vertical = 0)
        {
            Spacing.Horizontal = horizontal;
            Spacing.Vertical = vertical;
        }

        /// <summary>
        /// Set border of the object
        /// </summary>
        public void SetBorder(Border border)
        {
            BorderFill = border.GetFill();
            BorderRadius = border.GetRadius();
            BorderThickness = border.GetThickness();
        }

        /// <summary>
        /// Set border of the object
        /// </summary>
        /// <param name="fill"> border color </param>
        /// <param name="radius"> radius of the border corners </param>
        /// <param name="thickness"> border thickness </param>
        public void SetBorder(Color fill, CornerRadius radius, int thickness)
        {
            BorderFill = fill;
            BorderRadius = radius;
            BorderThickness = thickness;
        }

        /// <summary>
        /// Set object alignment
        /// </summary>
        public void SetAlignment(ItemAlignment alignment)
        {
            Alignment = alignment;
        }
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

        public void SetTextAlignment(ItemAlignment alignment)
        {
            TextAlignment = alignment;
        }
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

        ///////////////////////////////////////////////////////////////////

        /// <summary>
        /// Add inner primitives to the object (as decorations only) 
        /// note: not supported in the current version
        /// </summary>
        public void AddInnerShape(IBaseItem shape)
        {
            if (InnerShapes == null)
                InnerShapes = new List<IBaseItem>();
            InnerShapes.Add(shape);
        }

        /// <summary>
        /// Set style for the child of the object
        /// </summary>
        public void AddInnerStyle(String item_name, Style style)
        {
            if (_inner_styles.ContainsKey(item_name))
                _inner_styles[item_name] = style;
            else
                _inner_styles.Add(item_name, style);
        }

        /// <summary>
        /// Returns style of the object's child by name
        /// </summary>
        public Style GetInnerStyle(String item_name)
        {
            if (_inner_styles.ContainsKey(item_name))
                return _inner_styles[item_name];

            return null;
        }

        /// <summary>
        /// Object changes its state according to the ItemState when ItemStateType 
        /// happens
        /// </summary>
        public void AddItemState(ItemStateType type, ItemState state)
        {
            if (_item_states.ContainsKey(type))
            {
                state.Value = true;
                _item_states[type] = state;
            }
            else
            {
                _item_states.Add(type, state);
            }
        }

        /// <summary>
        /// Returns ItemState of the object by ItemStateType name
        /// </summary>
        public ItemState GetState(ItemStateType type)
        {
            if (_item_states.ContainsKey(type))
                return _item_states[type];
            return null;
        }

        /// <returns> Dictionary of the ItemStateTypes and its ItemStates </returns>
        public Dictionary<ItemStateType, ItemState> GetAllStates()
        {
            return _item_states;
        }

        /// <summary>
        /// Remove ItemState by the ItemStateType
        /// </summary>
        public void RemoveItemState(ItemStateType type)
        {
            if (type == ItemStateType.Base)
                return;
            if (_item_states.ContainsKey(type))
                _item_states.Remove(type);
        }

        /// <summary>
        /// Remove the object's child style by name of the child
        /// </summary>
        public void RemoveInnerStyle(String item_name)
        {
            if (_inner_styles.ContainsKey(item_name))
                _inner_styles.Remove(item_name);
            else
                return;
        }

        internal static void ItemStatesSyncBase(Style style)
        {
            ItemState core_state = style.GetState(ItemStateType.Base);
            foreach (var state in style.GetAllStates())
            {
                if (core_state == state.Value)
                    continue;

                state.Value.Border = core_state.Border;
            }
        }

        public Style Clone()
        {
            Style style = new Style();

            if (Background != null)
                style.Background = Color.FromArgb(Background.R, Background.G, Background.B, Background.A);
            if (Foreground != null)
                style.Foreground = Color.FromArgb(Foreground.R, Foreground.G, Foreground.B, Foreground.A);
            if (Font != null)
                style.Font = new Font(Font.FontFamily, Font.Size, Font.Style);
            else
                style.Font = DefaultsService.GetDefaultFont();
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
                style.BorderFill = Color.FromArgb(BorderFill.R, BorderFill.G, BorderFill.B, BorderFill.A);
            style.BorderThickness = BorderThickness;
            if (BorderRadius != null)
                style.BorderRadius = new CornerRadius(BorderRadius.LeftTop, BorderRadius.RightTop, BorderRadius.LeftBottom,
                        BorderRadius.RightBottom);
            if (Shape != null)
                style.Shape = new List<float[]>(Shape);
            if (InnerShapes != null)
                style.InnerShapes = new List<IBaseItem>(InnerShapes);
            style.IsFixedShape = IsFixedShape;
            style.IsVisible = IsVisible;
            style._item_states = new Dictionary<ItemStateType, ItemState>(_item_states);

            return style;
        }

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

        /// <returns> default style for ButtonCore objects </returns>
        public static Style GetButtonCoreStyle()
        {
            Style style = new Style();

            style.Background = Color.FromArgb(255, 13, 176, 255);
            style.Foreground = Color.FromArgb(255, 32, 32, 32);
            style.Font = DefaultsService.GetDefaultFont(16);
            style.WidthPolicy = SizePolicy.Fixed;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Width = 10;
            style.Height = 10;
            style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            style.TextAlignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
            style.BorderRadius = new CornerRadius();
            ItemState hovered = new ItemState(Color.FromArgb(60, 255, 255, 255));
            style.AddItemState(ItemStateType.Hovered, hovered);
            ItemState pressed = new ItemState(Color.FromArgb(60, 0, 0, 0));
            style.AddItemState(ItemStateType.Pressed, pressed);

            return style;
        }

        /// <returns> default style for ButtonToggle objects </returns>
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

        /// <returns> default style for CheckBox objects </returns>
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

            Style indicator_style = GetIndicatorStyle();
            style.AddInnerStyle("indicator", indicator_style);

            Style textline_style = GetLabelStyle();
            textline_style.WidthPolicy = SizePolicy.Expand;
            textline_style.HeightPolicy = SizePolicy.Expand;
            textline_style.Alignment = ItemAlignment.VCenter;
            textline_style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
            textline_style.Margin = new Indents(10 + indicator_style.Width, 0, 0, 0);
            style.AddInnerStyle("textline", textline_style);

            return style;
        }

        /// <returns> default style for Indicator objects </returns>
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

            Style marker_style = new Style();
            marker_style.Background = Color.FromArgb(255, 32, 32, 32);
            marker_style.Foreground = Color.FromArgb(255, 70, 70, 70); ;
            marker_style.Font = DefaultsService.GetDefaultFont();
            marker_style.WidthPolicy = SizePolicy.Expand;
            marker_style.HeightPolicy = SizePolicy.Expand;
            marker_style.Alignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
            marker_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(60, 255, 255, 255)
            });
            marker_style.AddItemState(ItemStateType.Toggled, new ItemState()
            {
                Background = Color.FromArgb(255, 255, 181, 111)
            });
            style.AddInnerStyle("marker", marker_style);

            return style;
        }

        /// <returns> default style for TextLine objects </returns>
        public static Style GetTextLineStyle()
        {
            Style style = new Style();

            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            style.Margin = new Indents(4, 4, 4, 4);

            return style;
        }

        /// <returns> default style for ComboBox objects </returns>
        public static Style GetComboBoxStyle()
        {
            Style style = new Style();
            style.Background = Color.Transparent;
            style.Foreground = Color.FromArgb(255, 70, 70, 70); ;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Width = 10;
            style.Height = 30;
            style.MinHeight = 10;
            style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;

            Style selection_style = new Style();
            selection_style.Background = Color.FromArgb(255, 220, 220, 220);
            selection_style.Foreground = Color.FromArgb(255, 70, 70, 70); ;
            selection_style.Font = DefaultsService.GetDefaultFont(14);
            selection_style.WidthPolicy = SizePolicy.Expand;
            selection_style.HeightPolicy = SizePolicy.Expand;
            selection_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            selection_style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
            selection_style.Padding = new Indents(10, 0, 0, 0);
            selection_style.Margin = new Indents(0, 0, 20, 0);
            style.AddInnerStyle("selection", selection_style);

            Style dropdownbutton_style = new Style();
            dropdownbutton_style.Width = 20;
            dropdownbutton_style.WidthPolicy = SizePolicy.Fixed;
            dropdownbutton_style.HeightPolicy = SizePolicy.Expand;
            dropdownbutton_style.Alignment = ItemAlignment.Right | ItemAlignment.VCenter;
            dropdownbutton_style.Background = Color.FromArgb(255, 255, 181, 111);
            dropdownbutton_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(40, 255, 255, 255)
            });
            style.AddInnerStyle("dropdownbutton", dropdownbutton_style);

            Style arrow_style = new Style();
            arrow_style.Width = 14;
            arrow_style.Height = 6;
            arrow_style.WidthPolicy = SizePolicy.Fixed;
            arrow_style.HeightPolicy = SizePolicy.Fixed;
            arrow_style.Alignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
            arrow_style.Background = Color.FromArgb(255, 50, 50, 50);
            arrow_style.Shape = GraphicsMathService.GetTriangle(a: 180);
            style.AddInnerStyle("arrow", arrow_style);

            return style;
        }

        /// <returns> default style for ComboBoxDropDown objects </returns>
        public static Style GetComboBoxDropDownStyle()
        {
            Style style = new Style();
            style.Background = Color.White;
            style.WidthPolicy = SizePolicy.Fixed;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Padding = new Indents(0, 0, 0, 0);

            Style itemlist_style = GetListBoxStyle();
            itemlist_style.Background = Color.Transparent;
            itemlist_style.Alignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
            style.AddInnerStyle("itemlist", itemlist_style);

            Style vsb_style = GetSimpleVerticalScrollBarStyle();
            vsb_style.Alignment = ItemAlignment.Right | ItemAlignment.Top;
            itemlist_style.AddInnerStyle("vscrollbar", vsb_style);

            Style hsb_style = GetHorizontalScrollBarStyle();
            hsb_style.Alignment = ItemAlignment.Left | ItemAlignment.Bottom;
            itemlist_style.AddInnerStyle("hscrollbar", hsb_style);

            Style menu_style = new Style();
            menu_style.Background = Color.FromArgb(50, 50, 50);
            menu_style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            menu_style.SetAlignment(ItemAlignment.Right | ItemAlignment.Bottom);
            itemlist_style.AddInnerStyle("menu", menu_style);

            Style area_style = GetListAreaStyle();
            style.AddInnerStyle("listarea", area_style);

            return style;
        }

        /// <returns> default style for MenuItem objects </returns>
        public static Style GetMenuItemStyle()
        {
            Style style = new Style();
            style.Background = Color.Transparent;
            style.Foreground = Color.FromArgb(70, 70, 70); ;
            style.Font = DefaultsService.GetDefaultFont();
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Height = 25;
            style.MinHeight = 10;
            style.SetAlignment(ItemAlignment.Left | ItemAlignment.Top);
            style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
            style.Padding = new Indents(10, 0, 10, 0);
            style.AddItemState(ItemStateType.Hovered, new ItemState(Color.FromArgb(255, 150, 150, 150)));

            Style text_style = new Style();
            text_style.SetMargin(0, 0, 0, 0);
            style.AddInnerStyle("text", text_style);

            Style arrow_style = new Style();
            arrow_style.Width = 6;
            arrow_style.Height = 10;
            arrow_style.WidthPolicy = SizePolicy.Fixed;
            arrow_style.HeightPolicy = SizePolicy.Fixed;
            arrow_style.Alignment = ItemAlignment.Right | ItemAlignment.VCenter;
            arrow_style.Background = Color.FromArgb(255, 80, 80, 80);
            arrow_style.Margin = new Indents(10, 0, 0, 0);
            arrow_style.Shape = GraphicsMathService.GetTriangle(a: 90);
            style.AddInnerStyle("arrow", arrow_style);

            return style;
        }

        /// <returns> default style for ContextMenu objects </returns>
        public static Style GetContextMenuStyle()
        {
            Style style = new Style();
            style.Background = Color.FromArgb(255, 210, 210, 210);
            style.WidthPolicy = SizePolicy.Fixed;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Padding = new Indents(0, 0, 0, 0);

            Style itemlist_style = new Style();
            itemlist_style.Background = Color.Transparent;
            itemlist_style.Alignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
            style.AddInnerStyle("itemlist", itemlist_style);

            Style area_style = GetListAreaStyle();
            area_style.SetPadding(0, 0, 0, 0);
            style.AddInnerStyle("listarea", area_style);

            return style;
        }

        internal static Style GetCustomSelectorStyle()
        {
            throw new NotImplementedException();
        }

        /// <returns> default style for FreeArea objects </returns>
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

        /// <returns> default style for Frame objects </returns>
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

        /// <returns> default style for Grid objects </returns>
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

        /// <returns> default style for HorizontalScrollBar objects </returns>
        public static Style GetHorizontalScrollBarStyle()
        {
            Style style = new Style();

            style.Background = Color.FromArgb(255, 50, 50, 50);
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Height = 16;

            Style uparrow_style = new Style();
            uparrow_style.WidthPolicy = SizePolicy.Fixed;
            uparrow_style.HeightPolicy = SizePolicy.Fixed;
            uparrow_style.Background = Color.FromArgb(255, 100, 100, 100);
            uparrow_style.Width = 16;
            uparrow_style.Height = 16;
            uparrow_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            uparrow_style.Shape = GraphicsMathService.GetTriangle(10, 8, 3, 4, -90);
            uparrow_style.IsFixedShape = true;
            uparrow_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(40, 255, 255, 255)
            });
            style.AddInnerStyle("uparrow", uparrow_style);

            Style downarrow_style = new Style();
            downarrow_style.WidthPolicy = SizePolicy.Fixed;
            downarrow_style.HeightPolicy = SizePolicy.Fixed;
            downarrow_style.Background = Color.FromArgb(255, 100, 100, 100);
            downarrow_style.Width = 16;
            downarrow_style.Height = 16;
            downarrow_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            downarrow_style.Shape = GraphicsMathService.GetTriangle(10, 8, 3, 4, 90);
            downarrow_style.IsFixedShape = true;
            downarrow_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(40, 255, 255, 255)
            });
            style.AddInnerStyle("downarrow", downarrow_style);

            Style slider_style = new Style();
            slider_style.WidthPolicy = SizePolicy.Expand;
            slider_style.HeightPolicy = SizePolicy.Expand;
            slider_style.Background = Color.Transparent;
            style.AddInnerStyle("slider", slider_style);

            Style track_style = new Style();
            track_style.WidthPolicy = SizePolicy.Expand;
            track_style.HeightPolicy = SizePolicy.Expand;
            track_style.Background = Color.Transparent;
            slider_style.AddInnerStyle("track", track_style);

            Style handler_style = new Style();
            handler_style.WidthPolicy = SizePolicy.Fixed;
            handler_style.HeightPolicy = SizePolicy.Expand;
            handler_style.Background = Color.FromArgb(255, 100, 100, 100);
            handler_style.Margin = new Indents(0, 3, 0, 3);
            handler_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            handler_style.MinWidth = 15;
            handler_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(40, 255, 255, 255)
            });
            slider_style.AddInnerStyle("handler", handler_style);

            return style;
        }

        /// <returns> default simple style for HorizontalScrollBar objects </returns>
        public static Style GetSimpleHorizontalScrollBarStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Padding = new Indents(2, 0, 2, 0);
            style.Height = 16;

            Style uparrow_style = new Style();
            uparrow_style.IsVisible = false;
            style.AddInnerStyle("uparrow", uparrow_style);

            Style downarrow_style = new Style();
            downarrow_style.IsVisible = false;
            style.AddInnerStyle("downarrow", downarrow_style);

            Style slider_style = new Style();
            slider_style.WidthPolicy = SizePolicy.Expand;
            slider_style.HeightPolicy = SizePolicy.Expand;
            slider_style.Background = Color.Transparent;
            style.AddInnerStyle("slider", slider_style);

            Style track_style = new Style();
            track_style.WidthPolicy = SizePolicy.Expand;
            track_style.HeightPolicy = SizePolicy.Expand;
            track_style.Background = Color.Transparent;
            slider_style.AddInnerStyle("track", track_style);

            Style handler_style = new Style();
            handler_style.WidthPolicy = SizePolicy.Fixed;
            handler_style.HeightPolicy = SizePolicy.Expand;
            handler_style.Background = Color.FromArgb(255, 120, 120, 120);
            handler_style.Margin = new Indents(0, 5, 0, 5);
            handler_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            handler_style.BorderRadius = new CornerRadius(3);
            handler_style.MinWidth = 15;
            slider_style.AddInnerStyle("handler", handler_style);

            return style;
        }

        /// <returns> default style for VerticalScrollBar objects </returns>
        public static Style GetVerticalScrollBarStyle()
        {
            Style style = new Style();

            style.Background = Color.FromArgb(255, 50, 50, 50);
            style.WidthPolicy = SizePolicy.Fixed;
            style.HeightPolicy = SizePolicy.Expand;
            style.Width = 16;

            Style uparrow_style = new Style();
            uparrow_style.WidthPolicy = SizePolicy.Fixed;
            uparrow_style.HeightPolicy = SizePolicy.Fixed;
            uparrow_style.Background = Color.FromArgb(255, 100, 100, 100);
            uparrow_style.Width = 16;
            uparrow_style.Height = 16;
            uparrow_style.Alignment = ItemAlignment.Top | ItemAlignment.HCenter;
            uparrow_style.Shape = GraphicsMathService.GetTriangle(10, 8, 3, 4, 0);
            uparrow_style.IsFixedShape = true;
            uparrow_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(40, 255, 255, 255)
            });
            style.AddInnerStyle("uparrow", uparrow_style);

            Style downarrow_style = new Style();
            downarrow_style.WidthPolicy = SizePolicy.Fixed;
            downarrow_style.HeightPolicy = SizePolicy.Fixed;
            downarrow_style.Background = Color.FromArgb(255, 100, 100, 100);
            downarrow_style.Width = 16;
            downarrow_style.Height = 16;
            downarrow_style.Alignment = ItemAlignment.Bottom | ItemAlignment.HCenter;
            downarrow_style.Shape = GraphicsMathService.GetTriangle(10, 8, 3, 4, 180);
            downarrow_style.IsFixedShape = true;
            downarrow_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(40, 255, 255, 255)
            });
            style.AddInnerStyle("downarrow", downarrow_style);

            Style slider_style = new Style();
            slider_style.WidthPolicy = SizePolicy.Expand;
            slider_style.HeightPolicy = SizePolicy.Expand;
            slider_style.Background = Color.Transparent;
            style.AddInnerStyle("slider", slider_style);

            Style track_style = new Style();
            track_style.WidthPolicy = SizePolicy.Expand;
            track_style.HeightPolicy = SizePolicy.Expand;
            track_style.Background = Color.Transparent;
            slider_style.AddInnerStyle("track", track_style);

            Style handler_style = new Style();
            handler_style.WidthPolicy = SizePolicy.Expand;
            handler_style.HeightPolicy = SizePolicy.Fixed;
            handler_style.Background = Color.FromArgb(255, 100, 100, 100);
            handler_style.Margin = new Indents(3, 0, 3, 0);
            handler_style.Alignment = ItemAlignment.Top | ItemAlignment.HCenter;
            handler_style.MinHeight = 15;
            handler_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(40, 255, 255, 255)
            });
            slider_style.AddInnerStyle("handler", handler_style);

            return style;
        }

        /// <returns> default simple style for VerticalScrollBar objects </returns>
        public static Style GetSimpleVerticalScrollBarStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.Padding = new Indents(0, 2, 0, 2);
            style.WidthPolicy = SizePolicy.Fixed;
            style.HeightPolicy = SizePolicy.Expand;
            style.Width = 16;

            Style uparrow_style = new Style();
            uparrow_style.IsVisible = false;
            style.AddInnerStyle("uparrow", uparrow_style);

            Style downarrow_style = new Style();
            downarrow_style.IsVisible = false;
            style.AddInnerStyle("downarrow", downarrow_style);

            Style slider_style = new Style();
            slider_style.WidthPolicy = SizePolicy.Expand;
            slider_style.HeightPolicy = SizePolicy.Expand;
            slider_style.Background = Color.Transparent;
            style.AddInnerStyle("slider", slider_style);

            Style track_style = new Style();
            track_style.WidthPolicy = SizePolicy.Expand;
            track_style.HeightPolicy = SizePolicy.Expand;
            track_style.Background = Color.Transparent;
            slider_style.AddInnerStyle("track", track_style);

            Style handler_style = new Style();
            handler_style.WidthPolicy = SizePolicy.Expand;
            handler_style.HeightPolicy = SizePolicy.Fixed;
            handler_style.Background = Color.FromArgb(255, 120, 120, 120);
            handler_style.Margin = new Indents(5, 0, 5, 0);
            handler_style.Alignment = ItemAlignment.Top | ItemAlignment.HCenter;
            handler_style.BorderRadius = new CornerRadius(3);
            handler_style.MinHeight = 15;
            slider_style.AddInnerStyle("handler", handler_style);

            return style;
        }

        /// <returns> default style for HorizontalSlider objects </returns>
        public static Style GetHorizontalSliderStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Height = 25;
            style.SetAlignment(ItemAlignment.Top, ItemAlignment.Left);

            Style track_style = new Style();
            track_style.WidthPolicy = SizePolicy.Expand;
            track_style.HeightPolicy = SizePolicy.Fixed;
            track_style.Height = 5;
            track_style.Alignment = ItemAlignment.VCenter;
            track_style.Background = Color.FromArgb(255, 100, 100, 100);
            style.AddInnerStyle("track", track_style);

            Style handler_style = new Style();
            handler_style.WidthPolicy = SizePolicy.Fixed;
            handler_style.HeightPolicy = SizePolicy.Expand;
            handler_style.Width = 10;
            handler_style.Background = Color.FromArgb(255, 255, 181, 111);
            handler_style.Alignment = ItemAlignment.Left;
            handler_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            style.AddInnerStyle("handler", handler_style);

            return style;
        }

        /// <returns> default style for VerticalSlider objects </returns>
        public static Style GetVerticalSliderStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Fixed;
            style.HeightPolicy = SizePolicy.Expand;
            style.Width = 25;

            Style track_style = new Style();
            track_style.WidthPolicy = SizePolicy.Fixed;
            track_style.HeightPolicy = SizePolicy.Expand;
            track_style.Width = 5;
            track_style.Alignment = ItemAlignment.HCenter;
            track_style.Background = Color.FromArgb(255, 100, 100, 100);
            style.AddInnerStyle("track", track_style);

            Style handler_style = new Style();
            handler_style.WidthPolicy = SizePolicy.Expand;
            handler_style.HeightPolicy = SizePolicy.Fixed;
            handler_style.Height = 10;
            handler_style.Background = Color.FromArgb(255, 255, 181, 111);
            handler_style.Alignment = ItemAlignment.Top;
            handler_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            style.AddInnerStyle("handler", handler_style);

            return style;
        }

        /// <returns> default style for HorizontalStack objects </returns>
        public static Style GetHorizontalStackStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

            return style;
        }

        /// <returns> default style for VerticalStack objects </returns>
        public static Style GetVerticalStackStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

            return style;
        }

        /// <returns> default style for HorizontalSplitArea objects </returns>
        public static Style GetHorizontalSplitAreaStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

            Style splitter_style = new Style();
            splitter_style.Background = Color.FromArgb(255, 42, 42, 42);
            splitter_style.Width = 6;
            style.AddInnerStyle("splitholder", splitter_style);

            return style;
        }

        /// <returns> default style for VerticalSplitArea objects </returns>
        public static Style GetVerticalSplitAreaStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

            Style splitter_style = new Style();
            splitter_style.Background = Color.FromArgb(255, 42, 42, 42);
            splitter_style.Height = 6;
            style.AddInnerStyle("splitholder", splitter_style);

            return style;
        }

        /// <returns> default style for Label objects </returns>
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

        /// <returns> default style for ListArea objects </returns>
        public static Style GetListAreaStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
            style.Padding = new Indents(2, 2, 2, 2);
            style.Spacing = new Spacing(0, 5);

            Style selection_style = GetSelectionItemStyle();
            style.AddInnerStyle("selection", selection_style);

            return style;
        }

        /// <returns> default style for ListBox objects </returns>
        public static Style GetListBoxStyle()
        {
            Style style = new Style();

            style.Background = Color.FromArgb(255, 70, 70, 70);
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

            Style vsb_style = GetVerticalScrollBarStyle();
            vsb_style.Alignment = ItemAlignment.Right | ItemAlignment.Top;
            style.AddInnerStyle("vscrollbar", vsb_style);

            Style hsb_style = GetHorizontalScrollBarStyle();
            hsb_style.Alignment = ItemAlignment.Left | ItemAlignment.Bottom;
            style.AddInnerStyle("hscrollbar", hsb_style);

            Style menu_style = new Style();
            menu_style.Background = Color.FromArgb(50, 50, 50);
            menu_style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            menu_style.SetAlignment(ItemAlignment.Right | ItemAlignment.Bottom);
            style.AddInnerStyle("menu", menu_style);

            Style area_style = GetListAreaStyle();
            style.AddInnerStyle("area", area_style);

            return style;
        }

        /// <summary>
        /// note: not supported in current version
        /// </summary>
        /// <returns> default style for WContainer objects  </returns>
        public static Style GetWContainerStyle()//нужен ли?
        {
            Style style = new Style();
            return style;
        }

        /// <returns> default style for RadioButton objects </returns>
        public static Style GetRadioButtonStyle()//нужен ли?
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

            Style indicator_style = GetIndicatorStyle();
            indicator_style.Shape = GraphicsMathService.GetEllipse();
            style.AddInnerStyle("indicator", indicator_style);

            Style marker_style = indicator_style.GetInnerStyle("marker");
            marker_style.Shape = GraphicsMathService.GetEllipse();
            indicator_style.AddInnerStyle("marker", marker_style);

            Style textline_style = GetLabelStyle();
            textline_style.WidthPolicy = SizePolicy.Expand;
            textline_style.HeightPolicy = SizePolicy.Expand;
            textline_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            textline_style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
            textline_style.Margin = new Indents(10 + indicator_style.Width, 0, 0, 0);
            style.AddInnerStyle("textline", textline_style);

            return style;
        }

        /// <returns> default style for PasswordLine objects </returns>
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

            Style marker_style = GetIndicatorStyle().GetInnerStyle("marker");
            marker_style.Background = Color.FromArgb(0, 100, 100, 100);
            marker_style.SetSize(20, 20);
            marker_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            marker_style.Alignment = ItemAlignment.VCenter | ItemAlignment.Right;
            marker_style.TextAlignment = ItemAlignment.VCenter | ItemAlignment.Left;
            marker_style.RemoveItemState(ItemStateType.Hovered);
            // marker_style.BorderRadius = new CornerRadius(5);

            // marker_style.AddItemState(ItemStateType.Hovered, new ItemState()
            // {
            //     Background = Color.FromArgb(50, 255, 255, 255)
            // });
            // marker_style.AddItemState(ItemStateType.Toggled, new ItemState()
            // {
            //     Background = Color.FromArgb(255, 40, 40, 40)
            // });
            style.AddInnerStyle("showmarker", marker_style);
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

            Style cursor_style = new Style();
            cursor_style.Background = Color.FromArgb(255, 60, 60, 60);
            cursor_style.Width = 2;
            cursor_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            cursor_style.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
            cursor_style.Margin = new Indents(0, 5, 0, 5);
            cursor_style.IsVisible = false;
            style.AddInnerStyle("cursor", cursor_style);

            Style selection_style = new Style();
            selection_style.Background = Color.FromArgb(255, 111, 181, 255);
            selection_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            selection_style.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
            style.AddInnerStyle("selection", selection_style);

            Style substrate_style = new Style();
            substrate_style.Font = DefaultsService.GetDefaultFont(FontStyle.Italic, 14);
            substrate_style.Foreground = Color.FromArgb(255, 150, 150, 150);
            style.AddInnerStyle("substrate", substrate_style);

            return style;
        }

        /// <returns> default style for TextEdit objects </returns>
        public static Style GetTextEditStyle()
        {
            Style style = new Style();
            style.Background = Color.FromArgb(255, 210, 210, 210);
            style.Foreground = Color.FromArgb(255, 70, 70, 70);
            style.Font = DefaultsService.GetDefaultFont(16);
            // style.Font = new Font(style.Font.FontFamily, 14, style.Font.Style);
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
            style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
            style.Height = 30;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Padding = new Indents(5, 0, 5, 0);
            // style.AddItemState(ItemStateType.Hovered, new ItemState()
            // {
            //     Background = Color.FromArgb(255, 220, 220, 220)
            // });
            // style.AddItemState(ItemStateType.Focused, new ItemState()
            // {
            //     Background = Color.FromArgb(255, 220, 220, 220)
            // });

            Style cursor_style = new Style();
            cursor_style.Background = Color.FromArgb(255, 60, 60, 60);
            cursor_style.Width = 2;
            cursor_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            cursor_style.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
            cursor_style.Margin = new Indents(0, 5, 0, 5);
            cursor_style.IsVisible = false;
            style.AddInnerStyle("cursor", cursor_style);

            Style selection_style = new Style();
            selection_style.Background = Color.FromArgb(255, 111, 181, 255);
            selection_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            selection_style.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
            selection_style.Margin = new Indents(0, 5, 0, 5);
            style.AddInnerStyle("selection", selection_style);

            Style substrate_style = new Style();
            substrate_style.Font = DefaultsService.GetDefaultFont(FontStyle.Italic, 14);
            substrate_style.Foreground = Color.FromArgb(255, 150, 150, 150);
            style.AddInnerStyle("substrate", substrate_style);

            return style;
        }

        /// <returns> default style for TextBlock objects </returns>
        public static Style GetTextBlockStyle()
        {
            Style style = new Style();
            style.Background = Color.Transparent;
            style.Foreground = Color.FromArgb(255, 70, 70, 70);
            style.Font = DefaultsService.GetDefaultFont(16);
            // style.Font = new Font(style.Font.FontFamily, 16, style.Font.Style);
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
            style.TextAlignment = ItemAlignment.Left | ItemAlignment.Top;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Padding = new Indents(5, 5, 5, 5);
            // style.AddItemState(ItemStateType.Hovered, new ItemState()
            // {
            //     Background = Color.FromArgb(255, 220, 220, 220)
            // });
            // style.AddItemState(ItemStateType.Focused, new ItemState()
            // {
            //     Background = Color.FromArgb(255, 220, 220, 220)
            // });

            Style cursor_style = new Style();
            cursor_style.Background = Color.FromArgb(255, 60, 60, 60);
            cursor_style.Width = 2;
            cursor_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            // cursor_style.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
            cursor_style.Margin = new Indents(0, 5, 0, 5);
            cursor_style.IsVisible = false;
            style.AddInnerStyle("cursor", cursor_style);

            Style selection_style = new Style();
            selection_style.Background = Color.FromArgb(255, 111, 181, 255);
            selection_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            // selection_style.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
            style.AddInnerStyle("selection", selection_style);

            return style;
        }

        /// <returns> default style for TextArea objects </returns>
        public static Style GetTextAreaStyle()
        {
            Style style = new Style();
            style.Background = Color.FromArgb(255, 210, 210, 210);
            style.Foreground = Color.FromArgb(25, 25, 25);
            style.Font = DefaultsService.GetDefaultFont(16);
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

            Style text_style = GetTextBlockStyle();
            style.AddInnerStyle("textedit", text_style);

            Style vsb_style = GetVerticalScrollBarStyle();
            vsb_style.Alignment = ItemAlignment.Right | ItemAlignment.Top;
            style.AddInnerStyle("vscrollbar", vsb_style);

            Style hsb_style = GetHorizontalScrollBarStyle();
            hsb_style.Alignment = ItemAlignment.Left | ItemAlignment.Bottom;
            style.AddInnerStyle("hscrollbar", hsb_style);

            Style menu_style = new Style();
            menu_style.Background = Color.FromArgb(50, 50, 50);
            menu_style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            menu_style.SetAlignment(ItemAlignment.Right | ItemAlignment.Bottom);
            style.AddInnerStyle("menu", menu_style);

            return style;
        }

        /// <returns> default style for PopUpMessage objects </returns>
        public static Style GetPopUpMessageStyle()
        {
            Style style = new Style();
            style.Background = Color.FromArgb(255, 45, 45, 45);
            style.Foreground = Color.LightGray;
            style.Font = DefaultsService.GetDefaultFont(14);
            // style.Font = new Font(style.Font.FontFamily, 14, style.Font.Style);
            style.Alignment = ItemAlignment.Bottom | ItemAlignment.Right;
            style.TextAlignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
            style.SetSize(300, 70);
            style.WidthPolicy = SizePolicy.Fixed;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Padding = new Indents(5, 5, 5, 5);
            style.Margin = new Indents(10, 10, 10, 10);
            style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(10, 255, 255, 255)
            });

            Style close_style = new Style();
            close_style.Background = Color.FromArgb(255, 100, 100, 100);
            close_style.SetSize(10, 10);
            close_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            close_style.Alignment = ItemAlignment.Top | ItemAlignment.Right;
            close_style.Margin = new Indents(0, 5, 0, 5);
            close_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(60, 255, 255, 255)
            });
            close_style.Shape = GraphicsMathService.GetCross(10, 10, 3, 45);
            close_style.IsFixedShape = false;
            style.AddInnerStyle("closebutton", close_style);

            return style;
        }

        /// <returns> default style for ProgressBar objects </returns>
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

            Style pgbar_style = new Style();
            pgbar_style.Background = Color.FromArgb(255, 0, 191, 255);
            pgbar_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            pgbar_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            style.AddInnerStyle("progressbar", pgbar_style);

            return style;
        }

        /// <returns> default style for ToolTip objects </returns>
        public static Style GetToolTipStyle()
        {
            Style style = new Style();

            style.Font = DefaultsService.GetDefaultFont();
            style.Background = Color.White;
            style.Foreground = Color.FromArgb(255, 70, 70, 70);
            style.Height = 30;
            style.WidthPolicy = SizePolicy.Fixed;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
            style.Padding = new Indents(5, 0, 5, 0);
            style.BorderRadius = new CornerRadius(4);

            return style;
        }

        /// <returns> default style for TitleBar objects </returns>
        public static Style GetTitleBarStyle()
        {
            Style style = new Style();

            style.Font = DefaultsService.GetDefaultFont(12);
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

            Style close_style = new Style();
            close_style.Background = Color.FromArgb(255, 100, 100, 100);
            close_style.SetSize(15, 15);
            close_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            close_style.Alignment = ItemAlignment.VCenter | ItemAlignment.Right;
            close_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(255, 186, 95, 97)
            });
            close_style.Shape = GraphicsMathService.GetCross(15, 15, 2, 45);
            close_style.IsFixedShape = true;
            style.AddInnerStyle("closebutton", close_style);

            Style minimize_style = new Style();
            minimize_style.Background = Color.FromArgb(255, 100, 100, 100);
            minimize_style.SetSize(12, 15);
            minimize_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            minimize_style.Alignment = ItemAlignment.Bottom | ItemAlignment.Right;
            minimize_style.Margin = new Indents(0, 0, 5, 9);
            minimize_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            minimize_style.Shape = GraphicsMathService.GetRectangle(15, 2, 0, 13);
            minimize_style.IsFixedShape = true;
            style.AddInnerStyle("minimizebutton", minimize_style);

            Style maximize_style = new Style();
            // maximize_style.Background = Color.Transparent;

            maximize_style.BorderThickness = 2;
            maximize_style.BorderFill = Color.FromArgb(255, 100, 100, 100);

            maximize_style.SetSize(12, 12);
            maximize_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            maximize_style.Alignment = ItemAlignment.Bottom | ItemAlignment.Right;
            maximize_style.Margin = new Indents(0, 0, 0, 9);
            maximize_style.Padding = new Indents(2, 2, 2, 2);

            ItemState hovered = new ItemState();
            // hovered.Background = Color.Transparent;
            hovered.Border.SetFill(Color.FromArgb(255, 84, 124, 94));
            maximize_style.AddItemState(ItemStateType.Hovered, hovered);
            maximize_style.Shape = GraphicsMathService.GetRectangle();
            style.AddInnerStyle("maximizebutton", maximize_style);

            Style title_style = new Style();
            title_style.Margin = new Indents(10, 0, 0, 0);
            style.AddInnerStyle("title", title_style);

            return style;
        }

        /// <returns> default style for TabView objects </returns>
        public static Style GetTabViewStyle()
        {
            Style style = new Style();

            style.Background = Color.FromArgb(255, 50, 50, 50);
            style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);

            Style view_style = new Style();
            view_style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            view_style.Background = Color.FromArgb(255, 71, 71, 71);
            view_style.IsVisible = false;
            view_style.Padding = new Indents(2, 2, 2, 2);
            style.AddInnerStyle("tabview", view_style);

            Style tab_style = new Style();
            tab_style.BorderRadius = new CornerRadius(3, 3, 0, 0);
            tab_style.Font = DefaultsService.GetDefaultFont(14);
            tab_style.Background = Color.Transparent;
            tab_style.Foreground = Color.FromArgb(255, 210, 210, 210);
            tab_style.Width = 100;
            tab_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            tab_style.TextAlignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
            tab_style.Padding = new Indents(2, 2, 2, 2);
            tab_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            tab_style.AddItemState(ItemStateType.Toggled, new ItemState()
            {
                Background = Color.FromArgb(71, 71, 71)
            });
            style.AddInnerStyle("tab", tab_style);

            // Style inactive_style = new Style();
            // inactive_style.BorderRadius = new CornerRadius(3, 3, 0, 0);
            // inactive_style.Font = DefaultsService.GetDefaultFont(14);
            // inactive_style.Background = Color.Transparent;
            // inactive_style.Foreground = Color.FromArgb(255, 210, 210, 210);
            // inactive_style.Width = 100;
            // inactive_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            // inactive_style.TextAlignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
            // inactive_style.Padding = new Indents(2, 2, 2, 2);
            // inactive_style.AddItemState(ItemStateType.Hovered, new ItemState()
            // {
            //     Background = Color.FromArgb(100, 255, 255, 255)
            // });
            // inactive_style.AddItemState(ItemStateType.Toggled, new ItemState()
            // {
            //     Background = Color.FromArgb(71, 71, 71)
            // });
            // style.AddInnerStyle("inactive", inactive_style);

            return style;
        }

        // public static Style GetTextItemStyle()
        // {
        //     Style style = new Style();

        //     style.Background = Color.Transparent;
        //     style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        //     style.Font = DefaultsService.GetDefaultFont();
        //     style.Foreground = Color.FromArgb(255, 70, 70, 70);;

        //     return style;
        // }

        /// <returns> default style for TreeView objects </returns>
        public static Style GetTreeViewStyle()
        {
            Style style = GetListBoxStyle();
            return style;
        }

        /// <returns> default style for TreeItem objects </returns>
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
            hovered.Background = Color.FromArgb(130, 255, 255, 255);
            style.AddItemState(ItemStateType.Hovered, hovered);

            Style indicator_style = new Style();//GetButtonToggleStyle();
            indicator_style.Background = Color.FromArgb(255, 32, 32, 32);
            indicator_style.Foreground = Color.FromArgb(255, 210, 210, 210);
            indicator_style.Font = DefaultsService.GetDefaultFont();
            indicator_style.SetSize(15, 15);
            indicator_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            indicator_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            indicator_style.TextAlignment = ItemAlignment.VCenter | ItemAlignment.Left;
            indicator_style.Shape = GraphicsMathService.GetTriangle(10, 8, 0, 3, 90);
            indicator_style.IsFixedShape = true;
            ItemState toggled = new ItemState();
            toggled.Background = Color.FromArgb(255, 160, 160, 160);
            toggled.Shape = new CustomFigure(true, GraphicsMathService.GetTriangle(10, 8, 0, 3, 180));
            indicator_style.AddItemState(ItemStateType.Toggled, toggled);
            style.AddInnerStyle("indicator", indicator_style);

            Style branch_icon_style = new Style();
            branch_icon_style.Background = Color.FromArgb(255, 106, 185, 255);
            branch_icon_style.SetSize(14, 9);
            branch_icon_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            branch_icon_style.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
            branch_icon_style.Shape = GraphicsMathService.GetFolderIconShape(20, 15, 0, 0);
            style.AddInnerStyle("branchicon", branch_icon_style);

            Style leaf_icon_style = new Style();
            leaf_icon_style.Background = Color.FromArgb(255, 129, 187, 133);
            leaf_icon_style.SetSize(6, 6);
            leaf_icon_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            leaf_icon_style.Alignment = ItemAlignment.VCenter | ItemAlignment.Left;
            leaf_icon_style.Shape = GraphicsMathService.GetEllipse(3, 16);
            leaf_icon_style.Margin = new Indents(2, 0, 0, 0);
            style.AddInnerStyle("leaficon", leaf_icon_style);

            return style;
        }

        /// <returns> default style for SpinItem objects </returns>
        public static Style GetSpinItemStyle()
        {
            Style style = new Style();

            style.Background = Color.FromArgb(50, 50, 50);
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Height = 30;
            style.MinHeight = 10;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

            Style uparrow_style = GetButtonCoreStyle();
            uparrow_style.WidthPolicy = SizePolicy.Expand;
            uparrow_style.HeightPolicy = SizePolicy.Expand;
            uparrow_style.SetMargin(4, 4, 4, 5);
            uparrow_style.Background = Color.FromArgb(255, 50, 50, 50);
            uparrow_style.Alignment = ItemAlignment.Top | ItemAlignment.VCenter;
            uparrow_style.Shape = GraphicsMathService.GetTriangle(12, 6, 0, 0, 0);
            uparrow_style.IsFixedShape = true;

            ItemState hovered = new ItemState();
            hovered.Background = Color.FromArgb(30, 255, 255, 255);
            uparrow_style.AddItemState(ItemStateType.Hovered, hovered);

            style.AddInnerStyle("uparrow", uparrow_style);

            Style downarrow_style = GetButtonCoreStyle();
            downarrow_style.WidthPolicy = SizePolicy.Expand;
            downarrow_style.HeightPolicy = SizePolicy.Expand;
            downarrow_style.SetMargin(4, 5, 4, 4);
            downarrow_style.Background = Color.FromArgb(255, 50, 50, 50);
            downarrow_style.Alignment = ItemAlignment.Bottom | ItemAlignment.VCenter;
            downarrow_style.Shape = GraphicsMathService.GetTriangle(12, 6, 0, 0, 180);
            downarrow_style.IsFixedShape = true;
            downarrow_style.AddItemState(ItemStateType.Hovered, hovered);
            style.AddInnerStyle("downarrow", downarrow_style);

            Style btns_area = GetVerticalStackStyle();
            btns_area.WidthPolicy = SizePolicy.Fixed;
            btns_area.HeightPolicy = SizePolicy.Expand;
            btns_area.Width = 20;
            btns_area.Background = Color.FromArgb(255, 255, 181, 111);
            btns_area.Alignment = ItemAlignment.Right | ItemAlignment.VCenter;
            style.AddInnerStyle("buttonsarea", btns_area);

            Style text_input = GetTextEditStyle();
            text_input.HeightPolicy = SizePolicy.Expand;
            text_input.TextAlignment = ItemAlignment.Right;
            style.AddInnerStyle("textedit", text_input);

            return style;
        }

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

            Style window_style = GetFrameStyle();
            window_style.SetSize(300, 150);
            window_style.SetMinSize(300, 150);
            window_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            window_style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            window_style.SetPadding(2, 2, 2, 2);
            window_style.SetBackground(45, 45, 45);

            style.AddInnerStyle("window", window_style);

            return style;
        }

        public static Style GetMessageItemStyle()
        {
            Style style = new Style();
            style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            style.SetBackground(0, 0, 0, 150);
            style.BorderRadius = new CornerRadius();

            Style window_style = GetFrameStyle();
            window_style.SetSize(300, 150);
            window_style.SetMinSize(300, 150);
            window_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            window_style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            window_style.SetPadding(2, 2, 2, 2);
            window_style.SetBackground(45, 45, 45);
            style.AddInnerStyle("window", window_style);

            Style ok_style = GetButtonCoreStyle();
            ok_style.SetBackground(100, 255, 150);
            ok_style.SetSize(100, 30);
            ok_style.SetAlignment(ItemAlignment.Left, ItemAlignment.VCenter);
            style.AddInnerStyle("button", ok_style);

            Style toolbar_style = GetHorizontalStackStyle();
            toolbar_style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.Bottom);
            toolbar_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            toolbar_style.SetSpacing(10, 0);
            toolbar_style.SetPadding(0, 0, 0, 0);
            toolbar_style.SetMargin(0, 0, 0, 0);
            style.AddInnerStyle("toolbar", toolbar_style);

            Style userbar_style = GetHorizontalStackStyle();
            userbar_style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.Bottom);
            userbar_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            userbar_style.SetSpacing(10, 0);
            userbar_style.SetPadding(0, 0, 0, 0);
            userbar_style.SetMargin(0, 0, 0, 0);
            style.AddInnerStyle("userbar", userbar_style);

            Style msg_style = GetLabelStyle();
            msg_style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            msg_style.SetTextAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
            msg_style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            msg_style.SetMargin(10, 0, 10, 40);
            style.AddInnerStyle("message", msg_style);

            Style layout_style = GetFrameStyle();
            layout_style.SetMargin(0, 30, 0, 0);
            layout_style.SetPadding(6, 6, 6, 6);
            layout_style.SetBackground(255, 255, 255, 20);
            style.AddInnerStyle("layout", layout_style);

            return style;
        }

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
            style.AddItemState(ItemStateType.Hovered, new ItemState(Color.FromArgb(130, 255, 255, 255)));

            Style icon_style = GetFrameStyle();
            icon_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            icon_style.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Left);

            style.AddInnerStyle("icon", icon_style);

            Style text_style = new Style();
            text_style.SetMargin(24, 0, 0, 0);

            style.AddInnerStyle("text", text_style);

            return style;
        }

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
            Style window_style = GetDialogItemStyle().GetInnerStyle("window");
            window_style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            window_style.SetMargin(150, 20, 150, 20);
            style.AddInnerStyle("window", window_style);
            // layout
            Style layout_style = GetVerticalStackStyle();
            layout_style.SetMargin(0, 30, 0, 0);
            layout_style.SetPadding(6, 6, 6, 6);
            layout_style.SetSpacing(0, 2);
            layout_style.SetBackground(255, 255, 255, 20);
            style.AddInnerStyle("layout", layout_style);
            // toolbar
            Style toolbar_style = GetHorizontalStackStyle();
            toolbar_style.HeightPolicy = SizePolicy.Fixed;
            toolbar_style.Height = 30;
            toolbar_style.SetBackground(40, 40, 40);
            toolbar_style.SetSpacing(3, 0);
            toolbar_style.SetPadding(6, 0, 0, 0);
            style.AddInnerStyle("toolbar", toolbar_style);
            // toolbarbutton
            Style toolbarbutton_style = Style.GetButtonCoreStyle();
            toolbarbutton_style.SetSize(24, 30);
            toolbarbutton_style.Background = toolbar_style.Background;
            toolbarbutton_style.AddItemState(ItemStateType.Hovered, new ItemState(Color.FromArgb(60, 255, 255, 255)));
            toolbarbutton_style.AddItemState(ItemStateType.Pressed, new ItemState(Color.FromArgb(30, 255, 255, 255)));
            toolbarbutton_style.BorderRadius = new CornerRadius();
            toolbarbutton_style.SetPadding(3, 6, 3, 6);
            style.AddInnerStyle("toolbarbutton", toolbarbutton_style);
            // buttonhidden
            Style buttonhidden_style = GetButtonToggleStyle();
            buttonhidden_style.SetSize(24, 30);
            buttonhidden_style.BorderRadius = new CornerRadius();
            buttonhidden_style.Background = toolbar_style.Background;
            buttonhidden_style.SetPadding(4, 6, 4, 6);
            buttonhidden_style.AddItemState(ItemStateType.Toggled, new ItemState(Color.FromArgb(30, 153, 91)));
            style.AddInnerStyle("buttonhidden", buttonhidden_style);
            // addressline
            Style addressline_style = GetTextEditStyle();
            addressline_style.Font = DefaultsService.GetDefaultFont(12);
            addressline_style.SetForeground(210, 210, 210);
            addressline_style.SetBackground(50, 50, 50);
            addressline_style.Height = 24;
            addressline_style.SetMargin(0, 5, 0, 0);
            style.AddInnerStyle("addressline", addressline_style);
            // filenameline
            Style filenameline_style = GetTextEditStyle();
            filenameline_style.Font = DefaultsService.GetDefaultFont(12);
            filenameline_style.SetForeground(210, 210, 210);
            filenameline_style.SetBackground(50, 50, 50);
            filenameline_style.Height = 24;
            filenameline_style.SetMargin(0, 2, 0, 0);
            style.AddInnerStyle("filenameline", filenameline_style);
            // list
            Style list_style = GetListBoxStyle();
            style.AddInnerStyle("list", list_style);
            // controlpanel
            Style controlpanel_style = GetFrameStyle();
            controlpanel_style.HeightPolicy = SizePolicy.Fixed;
            controlpanel_style.Height = 40;
            controlpanel_style.SetBackground(45, 45, 45);
            controlpanel_style.SetPadding(6, 6, 6, 6);
            style.AddInnerStyle("controlpanel", controlpanel_style);
            // button
            Style okbutton_style = GetButtonCoreStyle();
            okbutton_style.SetSize(100, 30);
            okbutton_style.SetAlignment(ItemAlignment.VCenter, ItemAlignment.Right);
            okbutton_style.SetMargin(0, 0, 105, 0);
            okbutton_style.BorderRadius = new CornerRadius();
            style.AddInnerStyle("okbutton", okbutton_style);

            Style cancelbutton_style = GetButtonCoreStyle();
            cancelbutton_style.SetSize(100, 30);
            cancelbutton_style.SetAlignment(ItemAlignment.VCenter, ItemAlignment.Right);
            cancelbutton_style.BorderRadius = new CornerRadius();
            style.AddInnerStyle("cancelbutton", cancelbutton_style);

            Style filter_style = GetButtonCoreStyle();
            filter_style.SetSize(24, 30);
            filter_style.BorderRadius = new CornerRadius();
            filter_style.SetBackground(35, 35, 35);
            filter_style.SetPadding(4, 6, 4, 6);
            filter_style.SetMargin(5, 0, 0, 0);
            style.AddInnerStyle("filter", filter_style);

            Style filtertext_style = GetLabelStyle();
            filtertext_style.WidthPolicy = SizePolicy.Fixed;
            filtertext_style.SetTextAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
            filtertext_style.SetPadding(10, 2, 10, 0);
            filtertext_style.SetMargin(-3, 0, 0, 0);
            filtertext_style.SetBackground(55, 55, 55);
            filtertext_style.Font = DefaultsService.GetDefaultFont();
            style.AddInnerStyle("filtertext", filtertext_style);

            Style divider_style = GetFrameStyle();
            divider_style.WidthPolicy = SizePolicy.Fixed;
            divider_style.Width = 1;
            divider_style.SetBackground(55, 55, 55);
            divider_style.SetMargin(0, 3, 0, 3);
            style.AddInnerStyle("divider", divider_style);

            return style;
        }

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

            Style window_style = GetFrameStyle();
            window_style.SetSize(300, 150);
            window_style.SetMinSize(300, 150);
            window_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            window_style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            window_style.SetPadding(2, 2, 2, 2);
            window_style.SetBackground(45, 45, 45);
            style.AddInnerStyle("window", window_style);

            Style ok_style = GetButtonCoreStyle();
            ok_style.SetBackground(100, 255, 150);
            ok_style.Foreground = Color.Black;
            ok_style.SetSize(100, 30);
            ok_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            ok_style.SetAlignment(ItemAlignment.Left, ItemAlignment.Bottom);
            ok_style.SetMargin(0, 0, 0, 0);
            ok_style.BorderRadius = new CornerRadius();
            ok_style.AddItemState(ItemStateType.Hovered, new ItemState(Color.FromArgb(80, 255, 255, 255)));
            style.AddInnerStyle("button", ok_style);

            Style text_style = GetTextEditStyle();
            text_style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.Top);
            text_style.SetTextAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
            text_style.SetMargin(0, 15, 0, 0);
            style.AddInnerStyle("textedit", text_style);

            Style layout_style = GetFrameStyle();
            layout_style.SetMargin(0, 30, 0, 0);
            layout_style.SetPadding(6, 6, 6, 6);
            layout_style.SetBackground(255, 255, 255, 20);
            style.AddInnerStyle("layout", layout_style);

            Style toolbar_style = GetHorizontalStackStyle();
            toolbar_style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.Bottom);
            toolbar_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            toolbar_style.SetSpacing(10, 0);
            style.AddInnerStyle("toolbar", toolbar_style);

            return style;
        }

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
        public static Style GetWrapAreaStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
            style.Padding = new Indents(2, 2, 2, 2);
            style.Spacing = new Spacing(0, 5);

            Style selection_style = GetSelectionItemStyle();
            style.AddInnerStyle("selection", selection_style);

            return style;
        }
        public static Style GetWrapGridStyle()
        {
            Style style = new Style();

            style.Background = Color.FromArgb(255, 70, 70, 70);
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

            Style vsb_style = GetVerticalScrollBarStyle();
            vsb_style.Alignment = ItemAlignment.Right | ItemAlignment.Top;
            style.AddInnerStyle("vscrollbar", vsb_style);

            Style hsb_style = GetHorizontalScrollBarStyle();
            hsb_style.Alignment = ItemAlignment.Left | ItemAlignment.Bottom;
            style.AddInnerStyle("hscrollbar", hsb_style);

            Style area_style = GetWrapAreaStyle();
            style.AddInnerStyle("area", area_style);

            return style;
        }

        public static Style GetSideAreaStyle()
        {
            Style style = new Style();
            style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            style.SetBackground(0, 0, 0, 130);
            style.BorderRadius = new CornerRadius(0);

            Style window_style = GetFrameStyle();
            window_style.SetPadding(2, 2, 2, 2);
            window_style.SetBackground(40, 40, 40);
            window_style.SetAlignment(ItemAlignment.Top, ItemAlignment.Left);
            style.AddInnerStyle("window", window_style);

            Style close_style = new Style();
            close_style.SetMargin(0, 5, 0, 0);
            close_style.Font = DefaultsService.GetDefaultFont();
            close_style.Background = Color.FromArgb(100, 100, 100);
            close_style.Foreground = Color.Transparent;
            close_style.SetSize(15, 15);
            close_style.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            close_style.Alignment = ItemAlignment.Top | ItemAlignment.Right;
            close_style.TextAlignment = ItemAlignment.Right | ItemAlignment.Top;
            ItemState close_hovered = new ItemState();
            close_hovered.Background = Color.FromArgb(186, 95, 97);
            close_style.AddItemState(ItemStateType.Hovered, close_hovered);

            close_style.Shape = GraphicsMathService.GetCross(15, 15, 2, 45);
            close_style.IsFixedShape = true;
            style.AddInnerStyle("closebutton", close_style);

            return style;
        }
        public static Style GetImageItemStyle()
        {
            Style style = new Style();
            style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            style.SetBackground(0, 0, 0, 0);

            return style;
        }

        public static Style GetLoadingScreenStyle()
        {
            Style style = new Style();
            style.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            style.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            style.SetBackground(0, 0, 0, 150);

            Style text_style = GetLabelStyle();
            text_style.SetAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
            // text_style.WidthPolicy = SizePolicy.Fixed;
            // text_style.Width = 64;
            // text_style.SetMargin(0, 0, 15, 0);
            text_style.SetTextAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
            text_style.Font = DefaultsService.GetDefaultFont(FontStyle.Bold, 14);
            style.AddInnerStyle("text", text_style);

            Style image_style = GetImageItemStyle();
            image_style.SetMaxSize(64, 64);
            style.AddInnerStyle("image", image_style);

            return style;
        }
    }
}
