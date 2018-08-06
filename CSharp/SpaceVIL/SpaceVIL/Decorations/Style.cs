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
        private ConcurrentDictionary<BaseItem, Style> _inner_styles = new ConcurrentDictionary<BaseItem, Style>();

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
        public int X;
        public int Y;
        public Dictionary<ItemStateType, ItemState> ItemStates = new Dictionary<ItemStateType, ItemState>();
        public Padding Padding = new Padding();
        public Spacing Spacing = new Spacing();
        public Margin Margin = new Margin();
        public int BorderRadius;
        public int BorderThickness;

        public Style()//default
        {
            Background = Color.White;
            Foreground = Color.Black;
            Font = new Font(new FontFamily("Courier New"), 14, FontStyle.Regular);
            WidthPolicy = SizePolicy.Fixed;
            HeightPolicy = SizePolicy.Fixed;
            Width = 30;
            Height = 30;
            MinHeight = 0;
            MinWidth = 0;
            MaxWidth = 7680;
            MaxHeight = 4320;
            Alignment = ItemAlignment.Left | ItemAlignment.Top;
            X = 0;
            Y = 0;
            BorderRadius = 0;
            BorderThickness = 0;
        }

        public void AddInnerStyle(BaseItem item, Style style)
        {
            if (_inner_styles.ContainsKey(item))
                _inner_styles[item] = style;
            else
                _inner_styles.TryAdd(item, style);
        }
        
        public void RemoveInnerStyle(BaseItem item, Style style)
        {
            if (_inner_styles.ContainsKey(item))
                _inner_styles.TryRemove(item, out style);
            else
                return;
        }
    }
}
