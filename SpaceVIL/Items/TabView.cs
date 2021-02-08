using System;
using System.Collections.Generic;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    /// <summary>
    /// TabView is a special container designed to group items within specific tab page.
    /// <para/> TabView cannot receive any events, 
    /// so TabView is always in the SpaceVIL.Core.ItemStateType.Base state.
    /// </summary>
    public class TabView : VerticalStack
    {
        static int count = 0;

        /// <summary>
        /// Setting the width policy of tabs. 
        /// Can be Fixed (tab shape not changes its size) or 
        /// Expand (tab shape is stretched to all available space).
        /// </summary>
        /// <param name="policy">Width policy of tab shapes.</param>
        public void SetTabPolicy(SizePolicy policy)
        {
            _tabBar.SetContentPolicy(policy);
        }

        /// <summary>
        /// Getting the current width policy of tabs. 
        /// Can be Fixed (tab shape not changes its size) or 
        /// Expand (tab shape is stretched to all available space).
        /// </summary>
        /// <returns>Width policy of tab shapes.</returns>
        public SizePolicy GetTabPolicy()
        {
            return _tabBar.GetContentPolicy();
        }

        private TabBar _tabBar;
        private Frame _viewArea;

        /// <summary>
        /// Getting all existing tabs in TabView.
        /// </summary>
        /// <returns>Tabs as List&lt;SpaceVIL.Tab&gt;.</returns>
        public List<Tab> GetTabs()
        {
            return _tabBar.GetTabs();
        }

        /// <summary>
        /// Default TabView constructor.
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
        /// Initializing all elements in the TabView.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            AddItems(_tabBar, _viewArea);
        }

        /// <summary>
        /// Selecting a tab by its Tab instance.
        /// </summary>
        /// <param name="tab">Tab to select.</param>
        public void SelectTab(Tab tab)
        {
            _tabBar.SelectTab(tab);
        }

        /// <summary>
        /// Selecting a tab by its index. 
        /// <para/> If index is out of range then this method does nothing.
        /// </summary>
        /// <param name="index">Index of a tab.</param>
        public void SelectTab(int index)
        {
            _tabBar.SelectTab(index);
        }

        /// <summary>
        /// Selecting a tab by its name. 
        /// <para/> Tab name is ID of UI element. Tab name is uniq but tab text is not.
        /// <para/> Explanation: the tab text can be a web address, 
        /// and there can be several open tabs with the same address.
        /// </summary>
        /// <param name="tabName">Tab name.</param>
        public void SelectTabByName(String tabName)
        {
            _tabBar.SelectTabByName(tabName);
        }

        /// <summary>
        /// Selecting a first-found tab by its text. 
        /// <para/> Tab name is ID of UI element. Tab name is uniq but tab text is not.
        /// <para/> Explanation: the tab text can be a web address, 
        /// and there can be several open tabs with the same address.
        /// </summary>
        /// <param name="tabText">Tab text.</param>
        public void SelectTabByText(String tabText)
        {
            _tabBar.SelectTabByText(tabText);
        }

        /// <summary>
        /// Getting the index of the current selected tab.
        /// </summary>
        /// <returns>Index of the current selected tab.</returns>
        public int GetSelectedTabIndex()
        {
            return _tabBar.GetSelectedTabIndex();
        }

        /// <summary>
        /// Getting the current selected tab.
        /// </summary>
        /// <returns>Current selected tab as SpaceVIL.Tab.</returns>
        public Tab GetSelectedTab()
        {
            return _tabBar.GetSelectedTab();
        }

        /// <summary>
        /// Getting a page of a tab. 
        /// </summary>
        /// <param name="tab">Tab as SpaceVIL.Tab.</param>
        /// <returns>Page of a tab as SpaceVIL.Frame.</returns>
        public Frame GetTabFrame(Tab tab)
        {
            return _tabBar.GetTabFrame(tab);
        }

        /// <summary>
        /// Getting content of a page by its tab.
        /// </summary>
        /// <param name="tab">Tab as SpaceVIL.Tab.</param>
        /// <returns>Page content as List&lt;SpaceVIL.IBaseItem&gt;.</returns>
        public List<IBaseItem> GetTabContent(Tab tab)
        {
            return _tabBar.GetTabContent(tab);
        }

        /// <summary>
        /// Adding a new tab to the TabView.
        /// </summary>
        /// <param name="tab">The new tab as SpaceVIL.Tab.</param>
        public void AddTab(Tab tab)
        {
            if (_tabBar.TabMapView.ContainsKey(tab))
            {
                return;
            }

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

        /// <summary>
        ///  Adding new tabs to the TabView.
        /// </summary>
        /// <param name="tabs">Tab sequence as SpaceVIL.Tab.</param>
        public void AddTabs(params Tab[] tabs)
        {
            foreach (Tab tab in tabs)
            {
                AddTab(tab);
            }
        }

        /// <summary>
        /// Removing the specified item from TabView.
        /// </summary>
        /// <param name="item"></param>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
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
        /// Removing a tab by its name.
        /// <para/> Tab name is ID of UI element. Tab name is uniq but tab text is not.
        /// <para/> Explanation: the tab text can be a web address, 
        /// and there can be several open tabs with the same address.
        /// </summary>
        /// <param name="tabName">Tab name.</param>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
        public bool RemoveTabByName(String tabName)
        {
            return _tabBar.RemoveTabByName(tabName);
        }

        /// <summary>
        /// Removing a first-found tab by its text.
        /// <para/> Tab name is ID of UI element. Tab name is uniq but tab text is not.
        /// <para/> Explanation: the tab text can be a web address, 
        /// and there can be several open tabs with the same address.
        /// </summary>
        /// <param name="tabText">Tab text.</param>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
        public bool RemoveTabByText(String tabText)
        {
            return _tabBar.RemoveTabByText(tabText);
        }

        /// <summary>
        /// Removing a tab.
        /// </summary>
        /// <param name="tab">Tab as SpaceVIL.Tab.</param>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
        public bool RemoveTab(Tab tab)
        {
            return _tabBar.RemoveTab(tab);
        }

        /// <summary>
        /// Removing all existing tabs.
        /// </summary>
        /// <returns>True: if the removal was successful. 
        /// False: if the removal was unsuccessful.</returns>
        public bool RemoveAllTabs()
        {
            return _tabBar.RemoveAllTabs();
        }

        /// <summary>
        /// Adding an item to page by its tab name.
        /// <para/> Tab name is ID of UI element. Tab name is uniq but tab text is not.
        /// <para/> Explanation: the tab text can be a web address, 
        /// and there can be several open tabs with the same address.
        /// </summary>
        /// <param name="tabName">Tab name.</param>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public void AddItemToTabByName(String tabName, IBaseItem item)
        {
            _tabBar.AddItemToTabByName(tabName, item);
        }

        /// <summary>
        /// Adding an item to page by its tab text.
        /// <para/> Tab name is ID of UI element. Tab name is uniq but tab text is not.
        /// <para/> Explanation: the tab text can be a web address, 
        /// and there can be several open tabs with the same address.
        /// </summary>
        /// <param name="tabText">Tab text.</param>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public void AddItemToTabByText(String tabText, IBaseItem item)
        {
            _tabBar.AddItemToTabByText(tabText, item);
        }

        /// <summary>
        /// Adding an item to page by its tab.
        /// </summary>
        /// <param name="tab">Tab as SpaceVIL.Core.Tab.</param>
        /// <param name="item">Item as SpaceVIL.Core.IBaseItem.</param>
        public void AddItemToTab(Tab tab, IBaseItem item)
        {
            _tabBar.AddItemToTab(tab, item);
        }

        /// <summary>
        /// Setting style of the TabView.
        /// <para/> Inner styles: "tabbar", "viewarea".
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
            {
                return;
            }
            base.SetStyle(style);

            Style innerStyle = style.GetInnerStyle("tabbar");
            if (innerStyle != null)
            {
                _tabBar.SetStyle(innerStyle);
            }
            innerStyle = style.GetInnerStyle("viewarea");
            if (innerStyle != null)
            {
                _viewArea.SetStyle(innerStyle);
            }
        }
    }
}
