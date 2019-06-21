using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class TabView : VerticalStack
    {
        static int count = 0;

        private HorizontalStack _tabBar;
        private Dictionary<Tab, Frame> _tabMapView;
        private List<Tab> _tabList;
        public List<Tab> GetTabs()
        {
            return new List<Tab>(_tabList);
        }
        private Tab _selectedTab = null;

        /// <summary>
        /// Constructs a TabView
        /// </summary>
        public TabView()
        {
            SetItemName("TabView_" + count++);
            _tabList = new List<Tab>();
            _tabMapView = new Dictionary<Tab, Frame>();
            _tabBar = new HorizontalStack();
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
            //_tab_bar
            AddItem(_tabBar);
        }

        private void HideOthers(Tab sender, MouseArgs args)
        {
            if (_selectedTab.Equals(sender))
                return;

            if (_selectedTab != null)
            {
                _selectedTab.SetToggled(false);
                _selectedTab.SetShadowDrop(false);
                _tabMapView[_selectedTab].SetVisible(false);
            }

            sender.SetToggled(true);
            sender.SetShadowDrop(true);
            _tabMapView[sender].SetVisible(true);

            _selectedTab = sender;

            UpdateLayout();
        }

        public void SelectTab(Tab tab)
        {
            HideOthers(tab, null);
        }

        public void SelectTabByName(String tabName)
        {
            foreach (Tab tab in _tabMapView.Keys)
            {
                if (tabName.Equals(tab.GetItemName()))
                {
                    HideOthers(tab, null);
                    return;
                }
            }
        }

        public void SelectTabByText(String tabText)
        {
            foreach (Tab tab in _tabMapView.Keys)
            {
                if (tabText.Equals(tab.GetText()))
                {
                    HideOthers(tab, null);
                    return;
                }
            }
        }

        private void SelectBestLeftoverTab(Tab tab)
        {
            if (_tabList.Count == 0)
                return;
            int index = _tabList.IndexOf(tab);
            if (index > 0)
                SelectTab(_tabList[index - 1]);
            else if (_tabList.Count != 1)
                SelectTab(_tabList[index + 1]);
        }

        /// <summary>
        /// Add new tab to the TabView
        /// </summary>
        /// <param name="tab">the new tab </param>
        public void AddTab(Tab tab)
        {
            if (_tabMapView.ContainsKey(tab))
                return;

            _tabMapView.Add(tab, tab.View);
            _tabList.Add(tab);

            tab.View.SetItemName(tab.GetItemName() + "_view");
            tab.EventMouseClick += (sender, args) =>
            {
                HideOthers(tab, args);
            };
            tab.EventTabRemoved += () =>
            {
                SelectBestLeftoverTab(tab);
                RemoveTab(tab);
            };

            AddItem(tab.View);

            if (_tabBar.GetItems().Count == 0)
            {
                tab.SetToggled(true);
                tab.View.SetVisible(true);
                tab.SetShadowDrop(true);
                _selectedTab = tab;
            }
            else
                tab.SetShadowDrop(false);

            _tabBar.AddItem(tab);
        }

        public void AddTabs(params Tab[] tabs)
        {
            foreach (Tab tab in tabs)
                AddTab(tab);
        }

        public override bool RemoveItem(IBaseItem item)
        {
            Tab tmpTab = item as Tab;
            if (tmpTab != null)
            {
                if (_tabMapView.ContainsKey(tmpTab))
                {
                    return RemoveTab(tmpTab);
                }
                return false;
            }
            else if (item.Equals(_tabBar))
            {
                Tab tab = null;
                while (_tabList.Count != 0)
                {
                    tab = _tabList[0];
                    _tabBar.RemoveItem(tab);
                    _tabMapView.Remove(tab);
                    _tabList.Remove(tab);
                }
            }
            return base.RemoveItem(item);
        }

        /// <summary>
        /// Remove tab by name
        /// </summary>
        public bool RemoveTabByName(String tabName)
        {
            if (tabName == null)
                return false;
            foreach (var tab in _tabMapView.Keys)
            {
                if (tabName.Equals(tab.GetItemName()))
                {
                    RemoveItem(_tabMapView[tab]);
                    _tabBar.RemoveItem(tab);
                    _tabMapView.Remove(tab);
                    _tabList.Remove(tab);
                    return true;
                }
            }
            return false;
        }

        public bool RemoveTabByText(String tabText)
        {
            if (tabText == null)
                return false;
            foreach (var tab in _tabMapView.Keys)
            {
                if (tabText.Equals(tab.GetText()))
                {
                    RemoveItem(_tabMapView[tab]);
                    _tabBar.RemoveItem(tab);
                    _tabMapView.Remove(tab);
                    _tabList.Remove(tab);
                    return true;
                }
            }
            return false;
        }

        /// <summary>
        /// Remove tab
        /// </summary>
        public bool RemoveTab(Tab tab)
        {
            if (tab == null)
                return false;

            if (_tabMapView.ContainsKey(tab))
            {
                RemoveItem(_tabMapView[tab]);
                _tabBar.RemoveItem(tab);
                _tabMapView.Remove(tab);
                _tabList.Remove(tab);
                return true;
            }
            return false;
        }

        public bool RemoveAllTabs()
        {
            if (_tabList.Count == 0)
                return false;
            Tab tab = null;
            while (_tabList.Count != 0)
            {
                tab = _tabList[0];
                RemoveItem(_tabMapView[tab]);
                _tabBar.RemoveItem(tab);
                _tabMapView.Remove(tab);
                _tabList.Remove(tab);
            }
            return true;
        }

        /// <summary>
        /// Add InterfaceBaseItem item to the tab with name tabName
        /// </summary>
        public void AddItemToTabByName(String tabName, IBaseItem item)
        {
            foreach (var tab in _tabMapView.Keys)
            {
                if (tabName.Equals(tab.GetItemName()))
                {
                    _tabMapView[tab].AddItem(item);
                    return;
                }
            }
        }
        public void AddItemToTabByText(String tabText, IBaseItem item)
        {
            foreach (var tab in _tabMapView.Keys)
            {
                if (tabText.Equals(tab.GetText()))
                {
                    _tabMapView[tab].AddItem(item);
                    return;
                }
            }
        }

        /// <summary>
        /// Add InterfaceBaseItem item to the tab by tab
        /// </summary>
        public void AddItemToTab(Tab tab, IBaseItem item)
        {
            if (_tabMapView.ContainsKey(tab))
                _tabMapView[tab].AddItem(item);
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

            Style innerStyle = style.GetInnerStyle("tabbar");
            if (innerStyle != null)
                _tabBar.SetStyle(innerStyle);
        }
    }
}
