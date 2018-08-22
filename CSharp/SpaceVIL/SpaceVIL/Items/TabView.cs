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
            // _tab_view = new Grid(2, 1);
            _tab_view = new VerticalStack();
            // _tab_view.SetSpacing(vertical: 5);
            AddItem(_tab_view);

            //_tab_bar
            _tab_bar = new HorizontalStack();
            _tab_bar.SetBackground(80, 80, 80);
            _tab_bar.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            _tab_bar.SetHeight(30);
            // _tab_bar.SetSpacing(5);

            // _tab_view.InsertItem(_tab_bar, 0, 0);
            _tab_view.AddItem(_tab_bar);
        }

        private void HideOthers(IItem sender, MouseArgs args)
        {
            // _tab_view.InsertItem(_tab_list[sender as ButtonToggle], 1, 0);
            // _tab_view.InsertItem(_tab_list[sender as ButtonToggle], 1, 0);
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

        public void AddTab(String name)
        {
            ButtonToggle tab = new ButtonToggle(name);
            tab.SetItemName(name);
            tab.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            tab.SetWidth(100);
            tab.SetBackground(_tab_bar.GetBackground());
            tab.SetForeground(Color.Black);
            tab.SetTextAlignment(ItemAlignment.HCenter | ItemAlignment.VCenter);
            tab.AddItemState(true, ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            tab.AddItemState(true, ItemStateType.Toggled, new ItemState()
            {
                Background = Color.FromArgb(255, 255, 181, 111)
            });
            tab.EventMouseClick += HideOthers;

            _tab_bar.AddItem(tab);

            Frame view = new Frame();
            view.SetItemName(name + "_view");
            view.SetBackground(Color.Gray);
            view.IsVisible = false;
            // _tab_view.InsertItem(view, 1, 0);
            _tab_view.AddItem(view);
            _tab_list.Add(tab, view);
            if (_tab_bar.GetItems().Count == 1)
            {
                tab.IsToggled = true;
                view.IsVisible = true;
            }
        }
        public void RemoveTab(String name)
        {

        }
        public void AddItemToTab(String tab_name, BaseItem item)
        {
            foreach (var tab in _tab_bar.GetItems())
            {
                if (tab_name == tab.GetItemName())
                {
                    // Console.WriteLine(_tab_list[tab as ButtonToggle].GetItemName());
                    _tab_list[tab as ButtonToggle].AddItem(item);
                }
            }
        }
    }
}
