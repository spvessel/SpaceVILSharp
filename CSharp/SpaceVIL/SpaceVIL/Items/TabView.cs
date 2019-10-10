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

        public void SetContentPolicy(SizePolicy policy)
        {
            _tabBar.SetContentPolicy(policy);
        }
        public SizePolicy GetContentPolicy()
        {
            return _tabBar.GetContentPolicy();
        }

        private TabBar _tabBar;
        private Frame _viewArea;

        public List<Tab> GetTabs()
        {
            return _tabBar.GetTabs();
        }


        /// <summary>
        /// Constructs a TabView
        /// </summary>
        public TabView()
        {
            SetItemName("TabView_" + count++);
            _tabBar = new TabBar();
            _viewArea = new Frame();
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
            AddItems(_tabBar, _viewArea);
        }

        public void SelectTab(Tab tab)
        {
            _tabBar.SelectTab(tab);
        }

        public void SelectTab(int index)
        {
            _tabBar.SelectTab(index);
        }

        public int GetSelectedTabIndex()
        {
            return _tabBar.GetSelectedTabIndex();
        }

        public Tab GetSelectedTab()
        {
            return _tabBar.GetSelectedTab();
        }
        public Frame GetTabFrame(Tab tab)
        {
            return _tabBar.GetTabFrame(tab);
        }

        public List<IBaseItem> GetTabContent(Tab tab)
        {
            return _tabBar.GetTabContent(tab);
        }

        public void SelectTabByName(String tabName)
        {
            _tabBar.SelectTabByName(tabName);
        }

        public void SelectTabByText(String tabText)
        {
            _tabBar.SelectTabByText(tabText);
        }

        /// <summary>
        /// Add new tab to the TabView
        /// </summary>
        /// <param name="tab">the new tab </param>
        public void AddTab(Tab tab)
        {
            if (_tabBar.TabMapView.ContainsKey(tab))
                return;

            _tabBar.TabMapView.Add(tab, tab.View);
            _viewArea.AddItem(tab.View);
            _tabBar.AddItem(tab);
            tab.EventMousePress += (sender, args) =>
            {
                UpdateLayout();
            };
            tab.EventTabRemove += () =>
            {
                RemoveTab(tab);
                UpdateLayout();
            };
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
                if (_tabBar.TabMapView.ContainsKey(tmpTab))
                {
                    return RemoveTab(tmpTab);
                }
                return false;
            }
            else if (item.Equals(_tabBar))
            {
                _tabBar.RemoveAllTabs();
            }
            return base.RemoveItem(item);
        }

        /// <summary>
        /// Remove tab by name
        /// </summary>
        public bool RemoveTabByName(String tabName)
        {
            return _tabBar.RemoveTabByName(tabName);
        }

        public bool RemoveTabByText(String tabText)
        {
            return _tabBar.RemoveTabByText(tabText);
        }

        /// <summary>
        /// Remove tab
        /// </summary>
        public bool RemoveTab(Tab tab)
        {
            return _tabBar.RemoveTab(tab);
        }

        public bool RemoveAllTabs()
        {
            return _tabBar.RemoveAllTabs();
        }

        /// <summary>
        /// Add InterfaceBaseItem item to the tab with name tabName
        /// </summary>
        public void AddItemToTabByName(String tabName, IBaseItem item)
        {
            _tabBar.AddItemToTabByName(tabName, item);
        }
        public void AddItemToTabByText(String tabText, IBaseItem item)
        {
            _tabBar.AddItemToTabByText(tabText, item);
        }

        /// <summary>
        /// Add InterfaceBaseItem item to the tab by tab
        /// </summary>
        public void AddItemToTab(Tab tab, IBaseItem item)
        {
            _tabBar.AddItemToTab(tab, item);
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
            innerStyle = style.GetInnerStyle("viewarea");
            if (innerStyle != null)
                _viewArea.SetStyle(innerStyle);
        }
    }
}
