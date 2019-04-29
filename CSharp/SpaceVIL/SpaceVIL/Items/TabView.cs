using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class TabView : Prototype
    {
        static int count = 0;

        private VerticalStack _tab_view;
        private HorizontalStack _tab_bar;
        private Dictionary<ButtonToggle, Frame> _tab_list;
        private Style _stored_style;

        /// <summary>
        /// Constructs a TabView
        /// </summary>
        public TabView()
        {
            SetItemName("TabView_" + count);
            count++;

            _tab_view = new VerticalStack();
            _tab_list = new Dictionary<ButtonToggle, Frame>();
            _tab_bar = new HorizontalStack();

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TabView)));
        }

        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }

        /// <summary>
        /// Initialization and adding of all elements in the TabView
        /// </summary>
        public override void InitElements()
        {
            //tab view
            AddItem(_tab_view);

            //_tab_bar
            _tab_bar.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            _tab_bar.SetHeight(30);
            _tab_view.AddItem(_tab_bar);
        }

        private void HideOthers(IItem sender, MouseArgs args)
        {
            foreach (var tab in _tab_list)
            {
                if (!tab.Key.GetItemName().Equals(sender.GetItemName()))
                {
                    tab.Key.SetToggled(false);
                    _tab_list[tab.Key].SetVisible(false);
                    tab.Key.SetShadowDrop(false);
                }
                else
                {
                    tab.Key.SetToggled(true);
                    _tab_list[tab.Key].SetVisible(true);
                    tab.Key.SetShadowDrop(true);
                }
            }
            _tab_view.UpdateLayout();
        }

        /// <summary>
        /// Add new tab to the TabView
        /// </summary>
        /// <param name="tab_name"> name of the new tab </param>
        public void AddTab(String tab_name)
        {
            Style view_style = _stored_style.GetInnerStyle("tabview");
            Style acive_tab_style = _stored_style.GetInnerStyle("tab");

            ButtonToggle tab = new ButtonToggle(tab_name);
            tab.SetItemName(tab_name);
            if (acive_tab_style != null)
                tab.SetStyle(acive_tab_style);
            tab.EventMouseClick += HideOthers;
            tab.SetShadow(5, 2, -1, Color.FromArgb(150, 0, 0, 0));

            Frame view = new Frame();
            view.SetItemName(tab_name + "_view");

            if (view_style != null)
                view.SetStyle(view_style);

            _tab_view.AddItem(view);
            _tab_list.Add(tab, view);

            if (_tab_bar.GetItems().Count == 0)
            {
                tab.SetToggled(true);
                view.SetVisible(true);
                tab.SetShadowDrop(true);
            }
            else
                tab.SetShadowDrop(false);
            // else
            // {
            //     if (inacive_tab_style != null)
            //     {
            //         tab.SetStyle(inacive_tab_style);
            //         view.SetVisible(false);
            //     }
            // }
            _tab_bar.AddItem(tab);
        }

        /// <summary>
        /// Add new tab to the TabView
        /// </summary>
        /// <param name="tab_name"> name of the new tab </param>
        /// <param name="tab_style"> style of the new tab </param>
        public void AddTab(String tab_name, Style tab_style, Style view_style)
        {
            //refactor
        }

        /// <summary>
        /// Remove tab by name
        /// </summary>
        public void RemoveTab(String tab_name)
        {
            if (tab_name == null)
                return;
            foreach (var tab in _tab_bar.GetItems())
            {
                if (tab_name.Equals(tab.GetItemName()))
                {
                    _tab_list.Remove(tab as ButtonToggle);
                }
            }
        }

        /// <summary>
        /// Add InterfaceBaseItem item to the tab with name tab_name
        /// </summary>
        public void AddItemToTab(String tab_name, IBaseItem item)
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

        /// <summary>
        /// Text alignment in the TabView
        /// </summary>
        public void SetTextAlignment(ItemAlignment alignment)
        {
            _textAlignment = alignment;
        }
        public void SetTextAlignment(params ItemAlignment[] alignment)
        {
            //SetTextAlignment(alignment);
        }

        private Indents _textMargin = new Indents();

        /// <summary>
        /// Text margin in the TabView
        /// </summary>
        public void SetTextMargin(Indents margin)
        {
            _textMargin = margin;

        }

        private Font _font;
        private int _font_size = 16;
        private FontStyle _font_style;
        private FontFamily _font_family;

        /// <summary>
        /// Text font parameters in the TabView
        /// </summary>
        public void SetFont(Font font)
        {
            _font = font;
        }
        public void SetFontSize(int size)
        {
            _font_size = size;
        }
        public void SetFontStyle(FontStyle style)
        {
            _font_style = style;
        }
        public void SetFontFamily(FontFamily font_family)
        {
            _font_family = font_family;
        }
        public Font GetFont()
        {
            return _font;
        }

        private Color _foreground = Color.White;

        /// <summary>
        /// Text color in the TabView
        /// </summary>
        public void SetForeground(Color color)
        {
            _foreground = color;
        }
        public void SetForeground(int r, int g, int b)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b));
        }
        public void SetForeground(int r, int g, int b, int a)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b, a));
        }
        public void SetForeground(float r, float g, float b)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b));
        }
        public void SetForeground(float r, float g, float b, float a)
        {
            SetForeground(GraphicsMathService.ColorTransform(r, g, b, a));
        }

        /// <summary>
        /// Set style of the TabView
        /// </summary>
        //style
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;

            base.SetStyle(style);
            _stored_style = style;

            Style tab_style = style.GetInnerStyle("tab");
            Style view_style = style.GetInnerStyle("tabview");
            foreach (var item in _tab_list)
            {
                if (tab_style != null)
                    item.Key.SetStyle(tab_style);
                if (view_style != null)
                    item.Value.SetStyle(view_style);
            }
        }
    }
}
