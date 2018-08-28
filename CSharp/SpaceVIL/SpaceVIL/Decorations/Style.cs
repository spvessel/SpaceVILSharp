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
            // Background = Color.White;
            // Foreground = Color.Black;
            // Font = new Font(new FontFamily("Courier New"), 14, FontStyle.Regular);
            // WidthPolicy = SizePolicy.Fixed;
            // HeightPolicy = SizePolicy.Fixed;
            // Width = 30;
            // Height = 30;
            // MinHeight = 0;
            // MinWidth = 0;
            MaxWidth = 7680;
            MaxHeight = 4320;
            // Alignment = ItemAlignment.Left | ItemAlignment.Top;
            // X = 0;
            // Y = 0;
            // BorderRadius = 0;
            // BorderThickness = 0;
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

        public static Style GetButtonCoreStyle()
        {
            Style style = new Style();
            style.Background = Color.FromArgb(255, 13, 176, 255);
            style.Foreground = Color.Black;
            style.Font = new Font(new FontFamily("Courier New"), 14, FontStyle.Regular);
            style.WidthPolicy = SizePolicy.Fixed;
            style.HeightPolicy = SizePolicy.Fixed;
            style.Width = 60;
            style.Height = 30;
            style.MinHeight = 0;
            style.MinWidth = 0;
            style.MaxWidth = Int16.MaxValue;
            style.MaxHeight = Int16.MaxValue;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
            style.X = 0;
            style.Y = 0;
            style.BorderRadius = 0;
            style.BorderThickness = 0;
            style.ItemStates.Add(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(60, 255, 255, 255)
            });

            return style;
        }
    }
}
