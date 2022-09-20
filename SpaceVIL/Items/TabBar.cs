using System;
using System.Collections.Generic;
using SpaceVIL.Common;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal class TabBar : Prototype, IHLayout
    {
        private Tab _selectedTab = null;
        internal Dictionary<Tab, Frame> TabMapView;
        private List<Tab> _tabList;
        internal List<Tab> GetTabs()
        {
            Reindexing();
            return new List<Tab>(_tabList);
        }

        static int count = 0;
        internal int ScrollOffsetX = 0;
        private int _scrollStep = 30;
        private int _dragScrollStep = 10;
        private int _maxScrollOffsetValue = 0;

        private SizePolicy _contentPolicy = SizePolicy.Fixed;

        internal void SetContentPolicy(SizePolicy policy)
        {
            if (_contentPolicy == policy)
                return;
            _contentPolicy = policy;
            ScrollOffsetX = 0;
            _maxScrollOffsetValue = 0;
            UpdateLayout();
        }

        internal SizePolicy GetContentPolicy()
        {
            return _contentPolicy;
        }

        private void AddScrollOffset(int value)
        {
            ScrollOffsetX += value;
            if (ScrollOffsetX < 0)
                ScrollOffsetX = 0;
            if (ScrollOffsetX > _maxScrollOffsetValue)
                ScrollOffsetX = _maxScrollOffsetValue;
            UpdateLayout();
        }

        internal TabBar()
        {
            SetItemName("TabBar_" + count++);
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TabBar)));
            IsFocusable = false;
            _tabList = new List<Tab>();
            TabMapView = new Dictionary<Tab, Frame>();
            EventScrollUp += (sender, args) =>
            {
                if (ScrollOffsetX == 0)
                    return;
                AddScrollOffset(-_scrollStep);
            };
            EventScrollDown += (sender, args) =>
            {
                if (ScrollOffsetX == _maxScrollOffsetValue)
                    return;
                AddScrollOffset(_scrollStep);
            };
        }

        private void InitTab(Tab tab)
        {
            tab.View.SetItemName(tab.GetItemName() + "_view");
            tab.EventMousePress += (sender, args) =>
            {
                if (args.Button == MouseButton.ButtonLeft)
                {
                    OnTop(tab);
                    UnselectOthers(tab, args);
                }
            };
            tab.EventTabRemove += () =>
            {
                SelectBestRightoverTab();
            };
            tab.EventMouseDrop += (sender, args) =>
            {
                if (args.Button == MouseButton.ButtonLeft)
                {
                    OnTabDrop(tab, args);
                }
            };
            tab.EventMouseDrag += (sender, args) =>
            {
                if (args.Button == MouseButton.ButtonLeft)
                {
                    OnTabDrag(tab, args);
                }
            };
        }

        public override void AddItem(IBaseItem item)
        {
            Tab tab = item as Tab;
            if (tab == null)
                return;

            if (_tabList.Count == 0)
            {
                tab.SetSelected(true);
                tab.View.SetVisible(true);
                _selectedTab = tab;
                _selectedTabIndex = 0;
            }
            _tabList.Add(tab);

            InitTab(tab);

            if (_tabList.Count == 1)
                base.AddItem(item);
            else
                base.InsertItem(item, _tabList.Count - 2);

            if (tab.GetWidthPolicy() == SizePolicy.Expand)
            {
                tab.SetWidthPolicy(SizePolicy.Fixed);
                tab.UpdateTabWidth();
            }
            UpdateLayout();
        }

        public override void InsertItem(IBaseItem item, int index)
        {
            Tab tab = item as Tab;
            if (tab == null)
                return;

            if (_tabList.Count == 0)
            {
                tab.SetSelected(true);
                tab.View.SetVisible(true);
                _selectedTab = tab;
            }
            _tabList.Insert(index, tab);

            InitTab(tab);

            base.InsertItem(item, index);
            if (tab.GetWidthPolicy() == SizePolicy.Expand)
            {
                tab.SetWidthPolicy(SizePolicy.Fixed);
                tab.UpdateTabWidth();
            }
            UpdateLayout();
        }

        public override bool RemoveItem(IBaseItem item)
        {
            bool result = base.RemoveItem(item);
            if (result)
            {
                Tab tab = item as Tab;
                if (_tabList.IndexOf(tab) < _selectedTabIndex)
                    _selectedTabIndex--;
                _tabList.Remove(tab);
                TabMapView[tab].GetParent().RemoveItem(TabMapView[tab]);
                TabMapView.Remove(tab);
                UpdateLayout();
            }
            return result;
        }

        public override void SetWidth(int width)
        {
            int diffWidth = GetWidth() - width;
            base.SetWidth(width);
            if (ScrollOffsetX == 0)
                return;
            AddScrollOffset(diffWidth);
        }

        public override void SetX(int x)
        {
            base.SetX(x);
            UpdateLayout();
        }

        private bool _isUpdating = false;
        public void UpdateLayout()
        {
            List<IBaseItem> itemList = GetItems();
            if (itemList.Count == 0 || _isUpdating)
                return;

            _isUpdating = true;

            SetLastTabToActualPlace(itemList);

            int itemOffsetX = GetX() + GetPadding().Left;

            if (_contentPolicy == SizePolicy.Expand)
            {
                Dictionary<IBaseItem, Int32> sizes = GetMapItemSize(itemList);
                foreach (var item in itemList)
                {
                    if (!sizes.ContainsKey(item))
                        continue;
                    item.SetWidth(sizes[item]);
                    if (!(_selectedTab.Dragging && _selectedTab == item))
                    {
                        item.SetX(itemOffsetX);
                        item.SetConfines();
                    }
                    itemOffsetX += item.GetWidth() + GetSpacing().Horizontal;
                }
            }
            else
            {
                int startXPos = itemOffsetX;
                itemOffsetX = -ScrollOffsetX;
                _maxScrollOffsetValue = 0;
                foreach (var item in itemList)
                {
                    if (!item.IsVisible())
                        continue;
                    int itemXPos = startXPos + itemOffsetX;// + item.GetMargin().Left;
                    if (!(_selectedTab.Dragging && _selectedTab == item))
                    {
                        item.SetX(itemXPos);
                        item.SetConfines();
                    }

                    itemOffsetX += item.GetWidth() + GetSpacing().Horizontal;
                    _maxScrollOffsetValue += item.GetWidth() + GetSpacing().Horizontal;

                    //left
                    if (itemXPos < startXPos)
                    {
                        if (itemXPos + item.GetWidth() <= startXPos)
                            item.SetDrawable(false);
                        else
                            item.SetDrawable(true);
                        continue;
                    }

                    //right
                    if (itemXPos + item.GetWidth() + item.GetMargin().Right > GetX() + GetWidth() - GetPadding().Right)
                    {
                        if (itemXPos >= GetX() + GetWidth() - GetPadding().Right)
                            item.SetDrawable(false);
                        else
                            item.SetDrawable(true);
                        continue;
                    }

                    item.SetDrawable(true);
                }
            }
            _maxScrollOffsetValue -= GetWidth();
            if (_maxScrollOffsetValue < 0)
                _maxScrollOffsetValue = 0;
            _isUpdating = false;
        }

        internal void OnTabDrop(Tab tab, MouseArgs args)
        {
            if (tab.Dragging)
            {
                tab.Dragging = false;
                UpdateLayout();
            }
        }

        internal void OnTabDrag(Tab tab, MouseArgs args)
        {
            if (tab.Dragging)
            {
                if (_tabList.Count < 2)
                    return;

                if (tab.GetX() == GetX())
                    AddScrollOffset(-_dragScrollStep);
                else if (tab.GetX() + tab.GetWidth() == GetX() + GetWidth())
                    AddScrollOffset(_dragScrollStep);

                List<Tab> tabList = GetTabs();
                int tabCount = tabList.Count;
                tabList.Remove(tab);

                // check first
                if (args.Position.GetX() < tabList[0].GetX() + tabList[0].GetWidth() / 2)
                {
                    if (!tabList[0].IsDraggable())
                        return;
                    _selectedTabIndex = 0;
                    UpdateLayout();
                    return;
                }
                // check last
                if (args.Position.GetX() > tabList[tabList.Count - 1].GetX()
                    + tabList[tabList.Count - 1].GetWidth() / 2)
                {
                    if (!tabList[tabList.Count - 1].IsDraggable())
                        return;
                    _selectedTabIndex = tabCount - 1;
                    UpdateLayout();
                    return;
                }

                int startIndex = 1;
                int lastIndex = tabList.Count - 1;
                if (tabList.Count == 2)
                {
                    startIndex = 0;
                    lastIndex = 1;
                }
                for (int index = startIndex; index < lastIndex; index++)
                {
                    Tab currnetTab = tabList[index];
                    if (!currnetTab.IsDraggable() && tabList.Count > 2)
                        continue;

                    int tX = currnetTab.GetX();
                    int tW = currnetTab.GetWidth();

                    // check move left
                    if (args.Position.GetX() < tX + tW / 3)
                    {
                        Tab prevTab = tabList[index - 1];
                        if (args.Position.GetX() > prevTab.GetX() + prevTab.GetWidth() - prevTab.GetWidth() / 3)
                        {
                            _selectedTabIndex = index;
                            UpdateLayout();
                            return;
                        }
                    }
                    // check move right
                    if (args.Position.GetX() > tX + tW - tW / 3)
                    {
                        Tab nextTab = tabList[index + 1];
                        if (args.Position.GetX() < nextTab.GetX() + nextTab.GetWidth() / 3)
                        {
                            _selectedTabIndex = index + 1;
                            UpdateLayout();
                            return;
                        }
                    }
                }
            }
        }

        internal void Reindexing()
        {
            List<IBaseItem> items = GetItems();
            items.RemoveAt(items.Count - 1);
            items.Insert(_selectedTabIndex, _selectedTab);

            List<Tab> tabs = new List<Tab>();
            foreach (IBaseItem item in items)
            {
                tabs.Add(item as Tab);
            }
            _tabList = tabs;
        }

        internal void OnTop(Tab tab)
        {
            List<IBaseItem> tabList = GetItems();

            IBaseItem lastSelected = tabList[tabList.Count - 1];
            tabList.Remove(lastSelected);
            tabList.Insert(_selectedTabIndex, lastSelected);

            tabList.Remove(tab);
            tabList.Add(tab);
            SetContent(tabList);
        }

        internal void SetLastTabToActualPlace(List<IBaseItem> tabList)
        {
            int index = GetSelectedTabIndex();
            if (index < 0 || index == (tabList.Count - 1))
                return;
            tabList.Remove(GetSelectedTab());
            tabList.Insert(index, GetSelectedTab());
        }

        private void UnselectOthers(Tab sender, MouseArgs args)
        {
            if (_selectedTab == sender)
                return;
            sender.SetSelected(true);
            TabMapView[sender].SetVisible(true);
            if (_selectedTab != null)
            {
                _selectedTab.SetSelected(false);
                TabMapView[_selectedTab].SetVisible(false);
            }
            _selectedTab = sender;
            _selectedTabIndex = _tabList.IndexOf(sender);
            Reindexing();
        }

        internal void SelectTab(Tab tab)
        {
            OnTop(tab);
            UnselectOthers(tab, null);
        }

        private int _selectedTabIndex = 0;
        internal void SelectTab(int index)
        {
            if (index < 0 || index >= _tabList.Count)
                return;
            OnTop(_tabList[index]);
            UnselectOthers(_tabList[index], null);
        }

        internal int GetSelectedTabIndex()
        {
            return _selectedTabIndex;
        }

        internal Tab GetSelectedTab()
        {
            return _selectedTab;
        }
        internal Frame GetTabFrame(Tab tab)
        {
            return TabMapView[tab];
        }

        internal List<IBaseItem> GetTabContent(Tab tab)
        {
            return TabMapView[tab].GetItems();
        }

        internal void SelectTabByName(String tabName)
        {
            foreach (Tab tab in TabMapView.Keys)
            {
                if (tabName.Equals(tab.GetItemName()))
                {
                    UnselectOthers(tab, null);
                    return;
                }
            }
        }

        internal void SelectTabByText(String tabText)
        {
            foreach (Tab tab in TabMapView.Keys)
            {
                if (tabText.Equals(tab.GetText()))
                {
                    UnselectOthers(tab, null);
                    return;
                }
            }
        }

        private void SelectBestRightoverTab()
        {
            _selectedTabIndex++;
            if (_selectedTabIndex > _tabList.Count - 1)
                _selectedTabIndex = _tabList.Count - 2;
            if (_selectedTabIndex < 0)
            {
                _selectedTabIndex = 0;
                return;
            }
            SelectTab(_selectedTabIndex);
            OnTop(_tabList[_selectedTabIndex]);
        }

        /// <summary>
        /// Remove tab by name
        /// </summary>
        internal bool RemoveTabByName(String tabName)
        {
            if (tabName == null)
                return false;
            foreach (var tab in TabMapView.Keys)
            {
                if (tabName.Equals(tab.GetItemName()))
                {
                    RemoveItem(tab);
                    return true;
                }
            }
            return false;
        }

        internal bool RemoveTabByText(String tabText)
        {
            if (tabText == null)
                return false;
            foreach (var tab in TabMapView.Keys)
            {
                if (tabText.Equals(tab.GetText()))
                {
                    RemoveItem(tab);
                    return true;
                }
            }
            return false;
        }

        /// <summary>
        /// Remove tab
        /// </summary>
        internal bool RemoveTab(Tab tab)
        {
            if (tab == null)
                return false;
            if (TabMapView.ContainsKey(tab))
                return RemoveItem(tab);
            return false;
        }

        internal bool RemoveAllTabs()
        {
            if (_tabList.Count == 0)
                return false;
            Tab tab = null;
            while (_tabList.Count != 0)
            {
                tab = _tabList[0];
                RemoveItem(tab);
            }
            return true;
        }

        /// <summary>
        /// Add IBaseItem item to the tab with name tabName
        /// </summary>
        internal void AddItemToTabByName(String tabName, IBaseItem item)
        {
            foreach (var tab in TabMapView.Keys)
            {
                if (tabName.Equals(tab.GetItemName()))
                {
                    TabMapView[tab].AddItem(item);
                    return;
                }
            }
        }
        internal void AddItemToTabByText(String tabText, IBaseItem item)
        {
            foreach (var tab in TabMapView.Keys)
            {
                if (tabText.Equals(tab.GetText()))
                {
                    TabMapView[tab].AddItem(item);
                    return;
                }
            }
        }

        /// <summary>
        /// Add IBaseItem item to the tab by tab
        /// </summary>
        internal void AddItemToTab(Tab tab, IBaseItem item)
        {
            if (TabMapView.ContainsKey(tab))
                TabMapView[tab].AddItem(item);
        }

        private Dictionary<IBaseItem, Int32> GetMapItemSize(List<IBaseItem> list)
        {
            int totalSpace = GetWidth() - GetPadding().Left - GetPadding().Right - (list.Count - 1) * GetSpacing().Horizontal;
            Dictionary<IBaseItem, Int32> mapExpand = new Dictionary<IBaseItem, Int32>();
            Dictionary<IBaseItem, Int32> mapFixed = new Dictionary<IBaseItem, Int32>();
            List<IBaseItem> visibleList = new List<IBaseItem>();
            foreach (IBaseItem item in list)
            {
                if (!item.IsVisible())
                    continue;
                visibleList.Add(item);
            }
            int itemWidth = totalSpace / visibleList.Count;
            int lastIndex = 0, orderInd = -1;
            foreach (IBaseItem item in visibleList)
            {
                orderInd++;
                if (item.GetMaxWidth() < itemWidth)
                {
                    mapFixed.Add(item, item.GetMaxWidth());
                    totalSpace -= item.GetMaxWidth();
                    continue;
                }
                if (item.GetMinWidth() > itemWidth)
                {
                    mapFixed.Add(item, item.GetMinWidth());
                    totalSpace -= item.GetMinWidth();
                    continue;
                }
                mapExpand.Add(item, itemWidth);
                lastIndex = orderInd;
            }
            if (mapExpand.Count > 0)
                itemWidth = totalSpace / mapExpand.Count;
            foreach (IBaseItem item in mapExpand.Keys)
            {
                mapFixed.Add(item, itemWidth);
            }
            if (mapExpand.Count > 0)
                itemWidth += totalSpace % mapExpand.Count;
            mapFixed[list[lastIndex]] = itemWidth;
            return mapFixed;
        }
    }
}
