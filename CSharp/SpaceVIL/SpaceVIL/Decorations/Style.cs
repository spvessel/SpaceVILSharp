using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class Style
    {
        // private Lazy<ConcurrentDictionary<String, Style>> _inner_styles = new Lazy<ConcurrentDictionary<String, Style>>(() => new ConcurrentDictionary<String, Style>());
        private Dictionary<String, Style> _inner_styles = new Dictionary<String, Style>();

        //defaults values
        public Color Background;
        public Color Foreground;
        public Font Font;
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
        //private Lazy<Dictionary<ItemStateType, ItemState>> _item_states = new Lazy<Dictionary<ItemStateType, ItemState>>(() => new Dictionary<ItemStateType, ItemState>());
        private Dictionary<ItemStateType, ItemState> _item_states = new Dictionary<ItemStateType, ItemState>();
        // public Lazy<Padding> Padding = new Lazy<Padding>(() => new Padding());
        // public Lazy<Spacing> Spacing = new Lazy<Spacing>(() => new Spacing());
        // public Lazy<Margin> Margin = new Lazy<Margin>(() => new Margin());
        public Indents Padding;// = new Indents();
        public Spacing Spacing;// = new Spacing();
        public Indents Margin;// = new Indents();
        public int BorderRadius;
        public int BorderThickness;
        public List<float[]> Shape;// = new List<float[]>();
        public bool IsFixedShape;

        public Style()//default
        {
            MaxWidth = Int16.MaxValue;
            MaxHeight = Int16.MaxValue;
            // Background = Color.White;
            // Foreground = Color.Black;
            //Font = new Font(new FontFamily("Courier New"), 14, FontStyle.Regular);
            // WidthPolicy = SizePolicy.Fixed;
            // HeightPolicy = SizePolicy.Fixed;
            // Width = 30;
            // Height = 30;
            // MinHeight = 0;
            // MinWidth = 0;
            // MaxWidth = 7680;
            // MaxHeight = 4320;
            // Alignment = ItemAlignment.Left | ItemAlignment.Top;
            // X = 0;
            // Y = 0;
            // BorderRadius = 0;
            // BorderThickness = 0;
        }

        public void AddInnerStyle(String item_name, Style style)
        {
            if (_inner_styles.ContainsKey(item_name))
                _inner_styles[item_name] = style;
            else
                _inner_styles.Add(item_name, style);
        }
        public Style GetInnerStyle(String item_name)
        {
            if (_inner_styles.ContainsKey(item_name))
                return _inner_styles[item_name];

            return null;
        }
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
        public ItemState GetState(ItemStateType type)
        {
            if (_item_states.ContainsKey(type))
                return _item_states[type];
            return null;
        }

        public Dictionary<ItemStateType, ItemState> GetAllStates()
        {
            return _item_states;
        }

        public void RemoveItemState(ItemStateType type)
        {
            if (type == ItemStateType.Base)
                return;
            if (_item_states.ContainsKey(type))
                _item_states.Remove(type);
        }

        public void RemoveInnerStyle(String item_name, Style style)
        {
            if (_inner_styles.ContainsKey(item_name))
                _inner_styles.Remove(item_name);
            else
                return;
        }

        public static Style GetButtonCoreStyle()
        {
            Style style = new Style();
            style.Background = Color.FromArgb(255, 13, 176, 255);
            style.Foreground = Color.Black;
            style.Font = DefaultsService.GetDefaultFont();
            style.WidthPolicy = SizePolicy.Fixed;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Width = 10;
            style.Height = 10;
            style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            style.TextAlignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
            style.BorderRadius = 6;
            style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(60, 255, 255, 255)
            });
            style.AddItemState(ItemStateType.Pressed, new ItemState()
            {
                Background = Color.FromArgb(30, 0, 0, 60)
            });

            return style;
        }

        public static Style GetButtonToggleStyle()
        {
            Style style = new Style();
            style.Background = Color.FromArgb(255, 13, 176, 255);
            style.Foreground = Color.Black;
            style.Font = DefaultsService.GetDefaultFont();
            style.WidthPolicy = SizePolicy.Fixed;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Width = 10;
            style.Height = 10;
            style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            style.BorderRadius = 6;
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

        public static Style GetCheckBoxStyle()
        {
            Style style = new Style();

            style.Background = Color.FromArgb(20, 255, 255, 255);
            style.Foreground = Color.LightGray;
            style.Font = DefaultsService.GetDefaultFont();
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Width = 10;
            style.Height = 20;
            style.MinHeight = 20;
            style.MinWidth = 20;
            style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;

            Style textline_style = new Style();
            textline_style.WidthPolicy = SizePolicy.Expand;
            textline_style.HeightPolicy = SizePolicy.Expand;
            textline_style.Alignment = ItemAlignment.VCenter;
            textline_style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
            textline_style.Margin = new Indents(10, 0, 0, 0);
            style.AddInnerStyle("textline", textline_style);

            return style;
        }

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
            marker_style.Foreground = Color.Black;
            marker_style.Font = DefaultsService.GetDefaultFont();
            marker_style.WidthPolicy = SizePolicy.Expand;
            marker_style.HeightPolicy = SizePolicy.Expand;
            marker_style.Alignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
            marker_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(125, 255, 255, 255)
            });
            marker_style.AddItemState(ItemStateType.Toggled, new ItemState()
            {
                Background = Color.FromArgb(255, 255, 181, 111)
            });
            style.AddInnerStyle("marker", marker_style);

            return style;
        }
        public static Style GetTextLineStyle()
        {
            Style style = new Style();

            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            style.Margin = new Indents(4, 4, 4, 4);

            return style;
        }

        public static Style GetComboBoxStyle()
        {
            Style style = new Style();
            style.Background = Color.Transparent;
            style.Foreground = Color.Black;
            style.Font = DefaultsService.GetDefaultFont();
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Width = 10;
            style.Height = 30;
            style.MinHeight = 10;
            style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;

            Style selection_style = new Style();
            selection_style.Background = Color.FromArgb(255, 220, 220, 220);
            selection_style.Foreground = Color.Black;
            selection_style.Font = DefaultsService.GetDefaultFont();
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

        public static Style GetComboBoxDropDownStyle()
        {
            Style style = new Style();
            style.Padding = new Indents(2, 2, 2, 2);
            style.Background = Color.White;
            style.MaxWidth = Int16.MaxValue;
            style.MaxHeight = Int16.MaxValue;

            Style itemlist_style = new Style();
            itemlist_style.Background = Color.Transparent;
            itemlist_style.Alignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
            style.AddInnerStyle("itemlist", itemlist_style);

            return style;
        }
        public static Style GetMenuItemStyle()
        {
            Style style = new Style();
            style.Background = Color.Transparent;
            style.Foreground = Color.Black;
            style.Font = DefaultsService.GetDefaultFont();
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Height = 25;
            style.MinHeight = 10;
            style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
            style.Padding = new Indents(10);
            style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(255, 180, 180, 180)
            });

            Style arrow_style = new Style();
            arrow_style.Width = 6;
            arrow_style.Height = 10;
            arrow_style.WidthPolicy = SizePolicy.Fixed;
            arrow_style.HeightPolicy = SizePolicy.Fixed;
            arrow_style.Alignment = ItemAlignment.Right | ItemAlignment.VCenter;
            arrow_style.Background = Color.FromArgb(255, 80, 80, 80);
            arrow_style.Margin = new Indents(0, 0, 5, 0);
            arrow_style.Shape = GraphicsMathService.GetTriangle(a: 90);
            style.AddInnerStyle("arrow", arrow_style);

            return style;
        }

        public static Style GetContextMenuStyle()
        {
            Style style = new Style();
            style.Padding = new Indents(2, 2, 2, 2);
            style.Background = Color.FromArgb(255, 210, 210, 210);
            style.WidthPolicy = SizePolicy.Fixed;
            style.HeightPolicy = SizePolicy.Fixed;

            Style itemlist_style = new Style();
            itemlist_style.Background = Color.Transparent;
            itemlist_style.Alignment = ItemAlignment.HCenter | ItemAlignment.VCenter;
            style.AddInnerStyle("itemlist", itemlist_style);

            return style;
        }

        public static Style GetCustomSelectorStyle()
        {
            throw new NotImplementedException();
        }

        public static Style GetFlowAreaStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.Padding = new Indents(2, 2, 2, 2);
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

            return style;
        }

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
            uparrow_style.Background = Color.FromArgb(50, 255, 255, 255);
            uparrow_style.Width = 16;
            uparrow_style.Height = 16;
            uparrow_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            uparrow_style.Shape = GraphicsMathService.GetTriangle(10, 8, 3, 4, -90);
            uparrow_style.IsFixedShape = true;
            uparrow_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            style.AddInnerStyle("uparrow", uparrow_style);

            Style downarrow_style = new Style();
            downarrow_style.WidthPolicy = SizePolicy.Fixed;
            downarrow_style.HeightPolicy = SizePolicy.Fixed;
            downarrow_style.Background = Color.FromArgb(50, 255, 255, 255);
            downarrow_style.Width = 16;
            downarrow_style.Height = 16;
            downarrow_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            downarrow_style.Shape = GraphicsMathService.GetTriangle(10, 8, 3, 4, 90);
            downarrow_style.IsFixedShape = true;
            downarrow_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
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
            handler_style.HeightPolicy = SizePolicy.Expand;
            handler_style.Background = Color.FromArgb(50, 255, 255, 255);
            handler_style.Margin = new Indents(0, 3, 0, 3);
            handler_style.Alignment = ItemAlignment.Left | ItemAlignment.VCenter;
            handler_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            slider_style.AddInnerStyle("handler", handler_style);

            return style;
        }

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
            uparrow_style.Background = Color.FromArgb(50, 255, 255, 255);
            uparrow_style.Width = 16;
            uparrow_style.Height = 16;
            uparrow_style.Alignment = ItemAlignment.Top | ItemAlignment.HCenter;
            uparrow_style.Shape = GraphicsMathService.GetTriangle(10, 8, 3, 4, 0);
            uparrow_style.IsFixedShape = true;
            uparrow_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            style.AddInnerStyle("uparrow", uparrow_style);

            Style downarrow_style = new Style();
            downarrow_style.WidthPolicy = SizePolicy.Fixed;
            downarrow_style.HeightPolicy = SizePolicy.Fixed;
            downarrow_style.Background = Color.FromArgb(50, 255, 255, 255);
            downarrow_style.Width = 16;
            downarrow_style.Height = 16;
            downarrow_style.Alignment = ItemAlignment.Bottom | ItemAlignment.HCenter;
            downarrow_style.Shape = GraphicsMathService.GetTriangle(10, 8, 3, 4, 180);
            downarrow_style.IsFixedShape = true;
            downarrow_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
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
            handler_style.HeightPolicy = SizePolicy.Expand;
            handler_style.Background = Color.FromArgb(50, 255, 255, 255);
            handler_style.Margin = new Indents(3, 0, 3, 0);
            handler_style.Alignment = ItemAlignment.Top | ItemAlignment.HCenter;
            handler_style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            slider_style.AddInnerStyle("handler", handler_style);

            return style;
        }

        public static Style GetHorizontalSliderStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Height = 25;

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

        public static Style GetVerticalSliderStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Fixed;
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

        public static Style GetHorizontalStackStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

            return style;
        }

        public static Style GetVerticalStackStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

            return style;
        }

        public static Style GetHorizontalSplitAreaStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

            Style splitter_style = new Style();
            splitter_style.Background = Color.FromArgb(255, 32, 32, 32);
            splitter_style.Width = 6;
            style.AddInnerStyle("splitholder", splitter_style);

            return style;
        }

        public static Style GetVerticalSplitAreaStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

            Style splitter_style = new Style();
            splitter_style.Background = Color.FromArgb(255, 32, 32, 32);
            splitter_style.Height = 6;
            style.AddInnerStyle("splitholder", splitter_style);

            return style;
        }

        public static Style GetLabelStyle()
        {
            Style style = new Style();

            style.Font = DefaultsService.GetDefaultFont();
            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
            style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;

            return style;
        }

        public static Style GetListAreaStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
            style.Padding = new Indents(2, 2, 2, 2);

            Style substrate_style = new Style();
            substrate_style.Background = Color.FromArgb(0, 45, 45, 45);
            substrate_style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
            substrate_style.WidthPolicy = SizePolicy.Expand;
            substrate_style.HeightPolicy = SizePolicy.Fixed;
            style.AddInnerStyle("substrate", substrate_style);

            return style;
        }

        public static Style GetListBoxStyle()
        {
            Style style = new Style();

            style.Background = Color.Transparent;
            style.WidthPolicy = SizePolicy.Expand;
            style.HeightPolicy = SizePolicy.Expand;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;

            Style vsb_style = GetVerticalScrollBarStyle();
            vsb_style.Alignment = ItemAlignment.Right | ItemAlignment.Top;
            style.AddInnerStyle("vscrollbar", vsb_style);

            Style hsb_style = GetHorizontalScrollBarStyle();
            hsb_style.Alignment = ItemAlignment.Left | ItemAlignment.Bottom;
            style.AddInnerStyle("hscrollbar", hsb_style);

            return style;
        }

        public static Style GetWContainerStyle()//нужен ли?
        {
            Style style = new Style();
            return style;
        }
        public static Style GetRadioButtonStyle()//нужен ли?
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
            style.BorderRadius = 10;

            Style textline_style = new Style();
            textline_style.WidthPolicy = SizePolicy.Expand;
            textline_style.HeightPolicy = SizePolicy.Expand;
            textline_style.Alignment = ItemAlignment.VCenter;
            textline_style.TextAlignment = ItemAlignment.Left | ItemAlignment.VCenter;
            textline_style.Margin = new Indents(10, 0, 0, 0);
            style.AddInnerStyle("textline", textline_style);

            Style indicator_style = GetIndicatorStyle();
            indicator_style.Shape = GraphicsMathService.GetEllipse();
            style.AddInnerStyle("indicator", indicator_style);

            Style marker_style = indicator_style.GetInnerStyle("marker");
            marker_style.Shape = GraphicsMathService.GetEllipse();
            indicator_style.AddInnerStyle("marker", marker_style);

            return style;
        }
    }
}
