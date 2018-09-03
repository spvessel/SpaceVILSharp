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
        private Lazy<ConcurrentDictionary<String, Style>> _inner_styles = new Lazy<ConcurrentDictionary<String, Style>>(() => new ConcurrentDictionary<String, Style>());

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
        private Lazy<Dictionary<ItemStateType, ItemState>> _item_states = new Lazy<Dictionary<ItemStateType, ItemState>>(() => new Dictionary<ItemStateType, ItemState>());
        // public Lazy<Padding> Padding = new Lazy<Padding>(() => new Padding());
        // public Lazy<Spacing> Spacing = new Lazy<Spacing>(() => new Spacing());
        // public Lazy<Margin> Margin = new Lazy<Margin>(() => new Margin());
        public Padding Padding = new Padding();
        public Spacing Spacing = new Spacing();
        public Margin Margin = new Margin();
        public int BorderRadius;
        public int BorderThickness;

        public Style()//default
        {
            // Background = Color.White;
            // Foreground = Color.Black;
            Font = new Font(new FontFamily("Courier New"), 14, FontStyle.Regular);
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

        public void AddInnerStyle(String item_name, Style style)
        {
            if (_inner_styles.Value.ContainsKey(item_name))
                _inner_styles.Value[item_name] = style;
            else
                _inner_styles.Value.TryAdd(item_name, style);
        }
        public void AddItemState(ItemStateType type, ItemState state)
        {
            if (_item_states.Value.ContainsKey(type))
            {
                state.Value = true;
                _item_states.Value[type] = state;
            }
            else
            {
                _item_states.Value.Add(type, state);
            }
        }
        public ItemState GetState(ItemStateType type)
        {
            if (_item_states.Value.ContainsKey(type))
                return _item_states.Value[type];
            return null;
        }
        public Dictionary<ItemStateType, ItemState> GetAllStates()
        {
            return _item_states.Value;
        }
        public void RemoveItemState(ItemStateType type)
        {
            if (type == ItemStateType.Base)
                return;
            if (_item_states.Value.ContainsKey(type))
                _item_states.Value.Remove(type);
        }

        public void RemoveInnerStyle(String item_name, Style style)
        {
            if (_inner_styles.Value.ContainsKey(item_name))
                _inner_styles.Value.TryRemove(item_name, out style);
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
            style.Width = 10;
            style.Height = 10;
            style.MinHeight = 0;
            style.MinWidth = 0;
            style.MaxWidth = Int16.MaxValue;
            style.MaxHeight = Int16.MaxValue;
            style.Alignment = ItemAlignment.Left | ItemAlignment.Top;
            style.X = 0;
            style.Y = 0;
            style.BorderRadius = 6;
            style.BorderThickness = 0;
            style.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(60, 255, 255, 255)
            });

            return style;
        }
    }
}
