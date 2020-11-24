package com.spvessel.spacevil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.spvessel.spacevil.Core.IBaseItem;
// import com.spvessel.spacevil.Core.AbstractHorizontalLayout;
import com.spvessel.spacevil.Core.IHLayout;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Flags.SizePolicy;

class TabBar extends Prototype implements IHLayout {

    private Tab _selectedTab = null;
    Map<Tab, Frame> tabMapView;
    private List<Tab> _tabList;

    public List<Tab> getTabs() {
        reindexing();
        return new LinkedList<>(_tabList);
    }

    static int count = 0;
    int scrollOffsetX = 0;
    private int _scrollStep = 30;
    private int _dragScrollStep = 10;
    private int _maxScrollOffsetValue = 0;

    private SizePolicy _contentPolicy = SizePolicy.Fixed;

    void setContentPolicy(SizePolicy policy) {
        if (_contentPolicy == policy)
            return;
        _contentPolicy = policy;
        scrollOffsetX = 0;
        _maxScrollOffsetValue = 0;
        updateLayout();
    }

    SizePolicy getContentPolicy() {
        return _contentPolicy;
    }

    private void addScrollOffset(int value) {
        scrollOffsetX += value;
        if (scrollOffsetX < 0)
            scrollOffsetX = 0;
        if (scrollOffsetX > _maxScrollOffsetValue)
            scrollOffsetX = _maxScrollOffsetValue;
        updateLayout();
    }

    TabBar() {
        setItemName("TabBar_" + count++);
        isFocusable = false;
        _tabList = new LinkedList<Tab>();
        tabMapView = new HashMap<Tab, Frame>();
        eventScrollUp.add((sender, args) -> {
            if (scrollOffsetX == 0)
                return;
            addScrollOffset(-_scrollStep);
        });
        eventScrollDown.add((sender, args) -> {
            if (scrollOffsetX == _maxScrollOffsetValue)
                return;
            addScrollOffset(_scrollStep);
        });
    }

    private void initTab(Tab tab) {
        tab.view.setItemName(tab.getItemName() + "_view");
        tab.eventMousePress.add((sender, args) -> {
            onTop(tab);
            unselectOthers(tab, args);
        });
        tab.eventTabRemove.add(() -> {
            selectBestRightoverTab();
            // removeTab(tab);
        });
        tab.eventMouseDrop.add((sender, args) -> {
            onTabDrop(tab, args);
        });
        tab.eventMouseDrag.add((sender, args) -> {
            onTabDrag(tab, args);
        });
    }

    @Override
    public void addItem(IBaseItem item) {
        if (!(item instanceof Tab))
            return;
        Tab tab = (Tab) item;
        if (_tabList.size() == 0) {
            tab.setToggled(true);
            tab.view.setVisible(true);
            _selectedTab = tab;
            _selectedTabIndex = 0;
        }
        _tabList.add(tab);
        initTab(tab);
        if (_tabList.size() == 1)
            super.addItem(item);
        else
            super.insertItem(item, _tabList.size() - 2);

        if (tab.getWidthPolicy() == SizePolicy.Expand) {
            tab.setWidthPolicy(SizePolicy.Fixed);
            tab.updateTabWidth();
        }
        updateLayout();
    }

    @Override
    public void insertItem(IBaseItem item, int index) {
        if (!(item instanceof Tab))
            return;
        Tab tab = (Tab) item;
        if (_tabList.size() == 0) {
            tab.setToggled(true);
            tab.view.setVisible(true);
            _selectedTab = tab;
            _selectedTabIndex = 0;
        }
        _tabList.add(index, tab);
        initTab(tab);
        super.insertItem(item, _tabList.size() - 2);
        if (tab.getWidthPolicy() == SizePolicy.Expand) {
            tab.setWidthPolicy(SizePolicy.Fixed);
            tab.updateTabWidth();
        }
        updateLayout();
    }

    @Override
    public boolean removeItem(IBaseItem item) {

        boolean result = super.removeItem(item);
        if (result) {
            Tab tab = (Tab) item;
            if (_tabList.indexOf(tab) < _selectedTabIndex)
                _selectedTabIndex--;
            _tabList.remove(tab);
            tabMapView.get(tab).getParent().removeItem(tabMapView.get(tab));
            tabMapView.remove(tab);
            updateLayout();
        }
        return result;
    }

    @Override
    public void setWidth(int width) {
        int diffWidth = getWidth() - width;
        super.setWidth(width);
        if (scrollOffsetX == 0)
            return;
        addScrollOffset(diffWidth);
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        updateLayout();
    }

    private boolean _isUpdating = false;

    @Override
    public void updateLayout() {
        List<IBaseItem> itemList = getItems();
        if (itemList.size() == 0 || _isUpdating)
            return;

        _isUpdating = true;

        setLastTabToActualPlace(itemList);

        int itemOffsetX = getX() + getPadding().left;

        if (_contentPolicy == SizePolicy.Expand) {
            Map<IBaseItem, Integer> sizes = getMapItemSize(itemList);
            for (IBaseItem item : itemList) {
                if (!sizes.containsKey(item))
                    continue;

                item.setWidth(sizes.get(item));

                if (!(_selectedTab.dragging && _selectedTab == item)) {
                    item.setX(itemOffsetX);
                    item.setConfines();
                }
                itemOffsetX += item.getWidth() + getSpacing().horizontal;
            }
        } else {
            int startXPos = itemOffsetX;
            itemOffsetX = -scrollOffsetX;
            _maxScrollOffsetValue = 0;
            for (IBaseItem item : itemList) {
                if (!item.isVisible())
                    continue;
                int itemXPos = startXPos + itemOffsetX;// + item.getMargin().Left;
                if (!(_selectedTab.dragging && _selectedTab == item)) {
                    item.setX(itemXPos);
                    item.setConfines();
                }

                itemOffsetX += item.getWidth() + getSpacing().horizontal;
                _maxScrollOffsetValue += item.getWidth() + getSpacing().horizontal;

                // left
                if (itemXPos < startXPos) {
                    if (itemXPos + item.getWidth() <= startXPos)
                        item.setDrawable(false);
                    else
                        item.setDrawable(true);
                    continue;
                }

                // right
                if (itemXPos + item.getWidth() + item.getMargin().right > getX() + getWidth() - getPadding().right) {
                    if (itemXPos >= getX() + getWidth() - getPadding().right)
                        item.setDrawable(false);
                    else
                        item.setDrawable(true);
                    continue;
                }
                item.setDrawable(true);
            }
        }
        _maxScrollOffsetValue -= getWidth();
        if (_maxScrollOffsetValue < 0)
            _maxScrollOffsetValue = 0;
        _isUpdating = false;
    }

    void onTabDrop(Tab tab, MouseArgs args) {
        if (tab.dragging) {
            tab.dragging = false;
            updateLayout();
        }
    }

    void onTabDrag(Tab tab, MouseArgs args) {
        if (tab.dragging) {
            if (_tabList.size() < 2)
                return;

            if (tab.getX() == getX())
                addScrollOffset(-_dragScrollStep);
            else if (tab.getX() + tab.getWidth() == getX() + getWidth())
                addScrollOffset(_dragScrollStep);

            //////////////////
            List<Tab> tabList = getTabs();
            int tabCount = tabList.size();
            tabList.remove(tab);

            // check first
            if (args.position.getX() < tabList.get(0).getX() + tabList.get(0).getWidth() / 2) {
                if (!tabList.get(0).isDraggable())
                    return;
                _selectedTabIndex = 0;
                updateLayout();
                return;
            }
            // check last
            if (args.position.getX() > tabList.get(tabList.size() - 1).getX()
                    + tabList.get(tabList.size() - 1).getWidth() / 2) {
                if (!tabList.get(tabList.size() - 1).isDraggable())
                    return;
                _selectedTabIndex = tabCount - 1;
                updateLayout();
                return;
            }

            int startIndex = 1;
            int lastIndex = tabList.size() - 1;
            if (tabList.size() == 2) {
                startIndex = 0;
                lastIndex = 1;
            }
            for (int index = startIndex; index < lastIndex; index++) {
                Tab currnetTab = tabList.get(index);
                if (!currnetTab.isDraggable() && tabList.size() > 2)
                    continue;

                int tX = currnetTab.getX();
                int tW = currnetTab.getWidth();

                // check move left
                if (args.position.getX() < tX + tW / 3) {
                    Tab prevTab = tabList.get(index - 1);
                    if (args.position.getX() > prevTab.getX() + prevTab.getWidth() - prevTab.getWidth() / 3) {
                        _selectedTabIndex = index;
                        updateLayout();
                        return;
                    }
                }
                // check move right
                if (args.position.getX() > tX + tW - tW / 3) {
                    Tab nextTab = tabList.get(index + 1);
                    if (args.position.getX() < nextTab.getX() + nextTab.getWidth() / 3) {
                        _selectedTabIndex = index + 1;
                        updateLayout();
                        return;
                    }
                }
            }
        }
    }

    void reindexing() {
        List<IBaseItem> items = getItems();
        items.remove(items.size() - 1);
        items.add(_selectedTabIndex, _selectedTab);

        List<Tab> tabs = new LinkedList<Tab>();
        for (IBaseItem item : items) {
            tabs.add((Tab) item);
        }
        _tabList = tabs;
    }

    void onTop(Tab tab) {
        List<IBaseItem> tabList = getItems();

        IBaseItem lastSelected = tabList.get(tabList.size() - 1);
        tabList.remove(lastSelected);
        tabList.add(_selectedTabIndex, lastSelected);

        tabList.remove(tab);
        tabList.add(tab);
        setContent(tabList);
    }

    void setLastTabToActualPlace(List<IBaseItem> tabList) {
        int index = getSelectedTabIndex();
        if (index < 0 || index == (tabList.size() - 1))
            return;
        tabList.remove(getSelectedTab());
        tabList.add(index, getSelectedTab());
    }

    private void unselectOthers(Tab sender, MouseArgs args) {
        if (_selectedTab == sender)
            return;
        sender.setToggled(true);
        tabMapView.get(sender).setVisible(true);

        if (_selectedTab != null) {
            _selectedTab.setToggled(false);
            tabMapView.get(_selectedTab).setVisible(false);
        }

        _selectedTab = sender;
        _selectedTabIndex = _tabList.indexOf(sender);
        reindexing();
    }

    void selectTab(Tab tab) {
        onTop(tab);
        unselectOthers(tab, null);
    }

    private int _selectedTabIndex = 0;

    void selectTab(int index) {
        if (index < 0 || index >= _tabList.size())
            return;
        onTop(_tabList.get(index));
        unselectOthers(_tabList.get(index), null);
    }

    int getSelectedTabIndex() {
        return _selectedTabIndex;
    }

    Tab getSelectedTab() {
        return _selectedTab;
    }

    Frame getTabFrame(Tab tab) {
        return tabMapView.get(tab);
    }

    List<IBaseItem> getTabContent(Tab tab) {
        return tabMapView.get(tab).getItems();
    }

    void selectTabByName(String tabName) {
        for (Tab tab : tabMapView.keySet()) {
            if (tabName.equals(tab.getItemName())) {
                unselectOthers(tab, null);
                return;
            }
        }
    }

    void selectTabByText(String tabText) {
        for (Tab tab : tabMapView.keySet()) {
            if (tabText.equals(tab.getText())) {
                unselectOthers(tab, null);
                return;
            }
        }
    }

    private void selectBestRightoverTab() {
        _selectedTabIndex++;
        if (_selectedTabIndex > _tabList.size() - 1)
            _selectedTabIndex = _tabList.size() - 2;
        if (_selectedTabIndex < 0) {
            _selectedTabIndex = 0;
            return;
        }
        selectTab(_selectedTabIndex);
        onTop(_tabList.get(_selectedTabIndex));
    }

    boolean removeTabByName(String tabName) {
        if (tabName == null)
            return false;
        for (Tab tab : tabMapView.keySet()) {
            if (tabName.equals(tab.getItemName())) {
                removeItem(tab);
                return true;
            }
        }
        return false;
    }

    boolean removeTabByText(String tabText) {
        if (tabText == null)
            return false;
        for (Tab tab : tabMapView.keySet()) {
            if (tabText.equals(tab.getText())) {
                removeItem(tab);
                return true;
            }
        }
        return false;
    }

    boolean removeTab(Tab tab) {
        if (tab == null)
            return false;
        if (tabMapView.containsKey(tab)) {
            return removeItem(tab);
        }
        return false;
    }

    boolean removeAllTabs() {
        if (_tabList.size() == 0)
            return false;
        Tab tab = null;
        while (_tabList.size() != 0) {
            tab = _tabList.get(0);
            removeItem(tab);
        }
        return true;
    }

    void addItemToTabByName(String tabName, IBaseItem item) {
        for (Tab tab : tabMapView.keySet()) {
            if (tabName.equals(tab.getItemName())) {
                tabMapView.get(tab).addItem(item);
                return;
            }
        }
    }

    void addItemToTabByText(String tabText, IBaseItem item) {
        for (Tab tab : tabMapView.keySet()) {
            if (tabText.equals(tab.getText())) {
                tabMapView.get(tab).addItem(item);
                return;
            }
        }
    }

    void addItemToTab(Tab tab, IBaseItem item) {
        if (tabMapView.containsKey(tab))
            tabMapView.get(tab).addItem(item);
    }

    private Map<IBaseItem, Integer> getMapItemSize(List<IBaseItem> list) {
        int totalSpace = getWidth() - getPadding().left - getPadding().right
                - (list.size() - 1) * getSpacing().horizontal;
        Map<IBaseItem, Integer> mapExpand = new HashMap<>();
        Map<IBaseItem, Integer> mapFixed = new HashMap<>();
        List<IBaseItem> visibleList = new LinkedList<>();
        for (IBaseItem item : list) {
            if (!item.isVisible())
                continue;
            visibleList.add(item);
        }
        int itemWidth = totalSpace / visibleList.size();
        int lastIndex = 0, orderInd = -1;
        for (IBaseItem item : visibleList) {
            orderInd++;
            if (item.getMaxWidth() < itemWidth) {
                mapFixed.put(item, item.getMaxWidth());
                totalSpace -= item.getMaxWidth();
                continue;
            }
            if (item.getMinWidth() > itemWidth) {
                mapFixed.put(item, item.getMinWidth());
                totalSpace -= item.getMinWidth();
                continue;
            }
            mapExpand.put(item, itemWidth);
            lastIndex = orderInd;
        }
        if (!mapExpand.isEmpty())
            itemWidth = totalSpace / mapExpand.size();
        for (IBaseItem item : mapExpand.keySet()) {
            mapFixed.put(item, itemWidth);
        }
        if (!mapExpand.isEmpty())
            itemWidth += totalSpace % mapExpand.size();
        mapFixed.replace(list.get(lastIndex), itemWidth);
        return mapFixed;
    }
}