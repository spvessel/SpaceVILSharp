using System;
using System.Collections.Generic;
using System.Drawing;

namespace SpaceVIL
{
    public class TabView : VisualItem
    {
        static int count = 0;

        // private Grid _tab_view;
        private VerticalStack _tab_view;
        private HorizontalStack _tab_bar;
        private Dictionary<ButtonToggle, Frame> _tab_list;

        public TabView()
        {
            SetBackground(Color.Transparent);
            SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            SetItemName("TabView_" + count);
            count++;
            _tab_list = new Dictionary<ButtonToggle, Frame>();
        }

        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }

        public override void InitElements()
        {
            //tab view
            _tab_view = new VerticalStack();
            AddItem(_tab_view);

            //_tab_bar
            _tab_bar = new HorizontalStack();
            _tab_bar.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            _tab_bar.SetHeight(30);
            _tab_view.AddItem(_tab_bar);
        }

        private void HideOthers(IItem sender, MouseArgs args)
        {
            foreach (var tab in _tab_bar.GetItems())
            {
                if (tab.GetItemName() != sender.GetItemName())
                {
                    (tab as ButtonToggle).IsToggled = false;
                    _tab_list[(tab as ButtonToggle)].IsVisible = false;
                }
                else
                {
                    _tab_list[(tab as ButtonToggle)].IsVisible = true;
                }
            }
            _tab_view.UpdateLayout();
        }

        public void AddTab(String tab_name)
        {
            ButtonToggle tab = new ButtonToggle(tab_name);
            tab.SetItemName(tab_name);
            tab.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            tab.SetWidth(100);
            tab.SetBackground(45, 45, 45);
            tab.SetForeground(180, 180, 180);
            tab.SetFont(_font);
            tab.SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            tab.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            tab.AddItemState(ItemStateType.Toggled, new ItemState()
            {
                Background = Color.FromArgb(71, 71, 71)
            });
            tab.EventMouseClick += HideOthers;

            _tab_bar.AddItem(tab);

            Frame view = new Frame();
            view.SetPadding(2, 2, 2, 2);
            view.SetItemName(tab_name + "_view");
            view.SetBackground(71, 71, 71);
            view.IsVisible = false;
            _tab_view.AddItem(view);
            _tab_list.Add(tab, view);

            if (_tab_bar.GetItems().Count == 1)
            {
                tab.IsToggled = true;
                view.IsVisible = true;
            }
        }
        public void AddTab(String tab_name, Style tab_style, Style view_style)
        {
            //refactor
        }
        public void RemoveTab(String tab_name)
        {
            foreach (var tab in _tab_bar.GetItems())
            {
                if (tab_name == tab.GetItemName())
                {
                    _tab_list.Remove(tab as ButtonToggle);
                }
            }
        }
        public void AddItemToTab(String tab_name, BaseItem item)
        {
            foreach (var tab in _tab_bar.GetItems())
            {
                if (tab_name == tab.GetItemName())
                {
                    _tab_list[tab as ButtonToggle].AddItem(item);
                }
            }
        }

        //text init
        private ItemAlignment _textAlignment;
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _textAlignment = alignment;
        }
        private Margin _textMargin = new Margin();
        public void SetTextMargin(Margin margin)
        {
            _textMargin = margin;
        }
        private Font _font =  new Font(new FontFamily("Open Sans Light"), 16, FontStyle.Bold);
        public void SetFont(Font font)
        {
            _font = font;
        }
        private int _font_size = 16;
        public void SetFontSize(int size)
        {
            _font_size = size;
        }
        private FontStyle _font_style = FontStyle.Bold;
        public void SetFontStyle(FontStyle style)
        {
            _font_style = style;
        }
        private FontFamily _font_family =  new FontFamily("Open Sans Light");
        public void SetFontFamily(FontFamily font_family)
        {
            _font_family = font_family;
        }
        public Font GetFont()
        {
            return _font;
        }
        private Color _foreground = Color.White;
        public void SetForeground(Color color)
        {
            _foreground = color;
        }
        public void SetForeground(int r, int g, int b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            SetForeground(Color.FromArgb(255, r, g, b));
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 255) r = 255;
            if (g < 0) g = Math.Abs(g); if (g > 255) g = 255;
            if (b < 0) b = Math.Abs(b); if (b > 255) b = 255;
            SetForeground(Color.FromArgb(a, r, g, b));
        }
        public void SetForeground(float r, float g, float b)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            SetForeground(Color.FromArgb(255, (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f)));
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            if (r < 0) r = Math.Abs(r); if (r > 1.0f) r = 1.0f;
            if (g < 0) g = Math.Abs(g); if (g > 1.0f) g = 1.0f;
            if (b < 0) b = Math.Abs(b); if (b > 1.0f) b = 1.0f;
            SetForeground(Color.FromArgb((int)(a * 255.0f), (int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f)));
        }
    }
}
