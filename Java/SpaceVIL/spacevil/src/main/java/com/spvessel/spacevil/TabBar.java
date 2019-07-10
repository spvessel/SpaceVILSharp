package com.spvessel.spacevil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceHLayout;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Flags.SizePolicy;

class TabBar extends Prototype implements InterfaceHLayout {

    private Tab _selectedTab = null;
    Map<Tab, Frame> tabMapView;
    private List<Tab> _tabList;

    public List<Tab> getTabs() {
        return new LinkedList<Tab>(_tabList);
    }

    static int count = 0;
    int scrollOffsetX = 0;
    private int _scrollStep = 30;
    private int _dragScrollStep = 10;
    private int _maxScrollOffsetValue = 0;

    private SizePolicy _contentPolicy = SizePolicy.FIXED;

    public void setContentPolicy(SizePolicy policy) {
        if (_contentPolicy == policy)
            return;
        _contentPolicy = policy;
        scrollOffsetX = 0;
        _maxScrollOffsetValue = 0;
        updateLayout();
    }

    public SizePolicy getContentPolicy() {
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
    public void addItem(InterfaceBaseItem item) {
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

        if (tab.getWidthPolicy() == SizePolicy.EXPAND) {
            tab.setWidthPolicy(SizePolicy.FIXED);
            tab.updateTabWidth();
        }
        updateLayout();
    }

    @Override
    public void insertItem(InterfaceBaseItem item, int index) {
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
        if (tab.getWidthPolicy() == SizePolicy.EXPAND) {
            tab.setWidthPolicy(SizePolicy.FIXED);
            tab.updateTabWidth();
        }
        updateLayout();
    }

    @Override
    public boolean removeItem(InterfaceBaseItem item) {

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
    public void setX(int _x) {
        super.setX(_x);
        updateLayout();
    }

    private boolean _isUpdating = false;

    @Override
    public void updateLayout() {
        List<InterfaceBaseItem> itemList = getItems();
        if (itemList.size() == 0 || _isUpdating)
            return;

        _isUpdating = true;

        setLastTabToActualPlace(itemList);

        int itemOffsetX = getX() + getPadding().left;

        if (_contentPolicy == SizePolicy.EXPAND) {
            int totalSpace = getWidth() - getPadding().left - getPadding().right
                    - (itemList.size() - 1) * getSpacing().horizontal;
            int itemWidth = totalSpace / itemList.size();
            int itemsRemaining = itemList.size();
            for (InterfaceBaseItem item : itemList) {
                itemsRemaining--;
                if (!item.isVisible())
                    continue;

                if (itemsRemaining == 0)
                    itemWidth = totalSpace;

                if (item.getWidth() < itemWidth) { // } && itemsRemaining != 0) {
                    // itemWidth = item.getWidth();
                    totalSpace -= item.getWidth();
                    if (itemsRemaining != 0)
                        itemWidth = totalSpace / itemsRemaining;
                } else { // if (itemsRemaining != 0) {
                    item.setWidth(itemWidth);
                    totalSpace -= item.getWidth();
                }

                if (!(_selectedTab.dragging && _selectedTab == item)) {
                    item.setX(itemOffsetX);
                    item.setConfines();
                }
                // System.out.println(((Tab) item).getText() + " " + itemOffsetX + " " +
                // itemWidth);
                itemOffsetX += item.getWidth() + getSpacing().horizontal;
            }
        } else {
            int startXPos = itemOffsetX;
            itemOffsetX = -scrollOffsetX;
            _maxScrollOffsetValue = 0;
            for (InterfaceBaseItem item : itemList) {
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
            List<Tab> tabList = getTabs();
            tabList.remove(tab);
            for (Tab t : tabList) {
                if (!t.isDraggable())
                    continue;
                int tX = t.getX();
                int tW = t.getWidth();
                if (args.position.getX() > tX + tW / 3 && args.position.getX() < tX + tW - tW / 3) {
                    int index = tabList.indexOf(t);
                    if (_selectedTabIndex <= index)
                        index++;
                    _selectedTabIndex = index;
                    break;
                }
            }
            tab.dragging = false;
            updateLayout();
        }
    }

    void onTabDrag(Tab tab, MouseArgs args) {
        if (tab.dragging) {
            if (tab.getX() == getX())
                addScrollOffset(-_dragScrollStep);
            else if (tab.getX() + tab.getWidth() == getX() + getWidth())
                addScrollOffset(_dragScrollStep);
            List<Tab> tabList = getTabs();
            tabList.remove(tab);
            for (Tab t : tabList) {
                if (!t.isDraggable())
                    continue;
                int tX = t.getX();
                int tW = t.getWidth();
                if (args.position.getX() > tX + tW / 3 && args.position.getX() < tX + tW - tW / 3) {
                    int index = tabList.indexOf(t);
                    if (_selectedTabIndex <= index)
                        index++;
                    _selectedTabIndex = index;
                    updateLayout();
                    break;
                }
            }
        }
    }

    void reindexing() {
        List<InterfaceBaseItem> items = getItems();
        items.remove(items.size() - 1);
        items.add(_selectedTabIndex, _selectedTab);

        List<Tab> tabs = new LinkedList<Tab>();
        for (InterfaceBaseItem item : items) {
            tabs.add((Tab) item);
        }
        _tabList = tabs;
    }

    void onTop(Tab tab) {
        List<InterfaceBaseItem> tabList = getItems();

        InterfaceBaseItem lastSelected = tabList.get(tabList.size() - 1);
        tabList.remove(lastSelected);
        tabList.add(_selectedTabIndex, lastSelected);

        tabList.remove(tab);
        tabList.add(tab);
        setContent(tabList);
    }

    void setLastTabToActualPlace(List<InterfaceBaseItem> tabList) {
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
    }

    public void selectTab(Tab tab) {
        unselectOthers(tab, null);
    }

    private int _selectedTabIndex = 0;

    public void selectTab(int index) {
        if (index < 0 || index >= _tabList.size())
            return;
        unselectOthers(_tabList.get(index), null);
    }

    public int getSelectedTabIndex() {
        return _selectedTabIndex;
    }

    public Tab getSelectedTab() {
        return _selectedTab;
    }

    public Frame getTabFrame(Tab tab) {
        return tabMapView.get(tab);
    }

    public List<InterfaceBaseItem> getTabContent(Tab tab) {
        return tabMapView.get(tab).getItems();
    }

    public void selectTabByName(String tabName) {
        for (Tab tab : tabMapView.keySet()) {
            if (tabName.equals(tab.getItemName())) {
                unselectOthers(tab, null);
                return;
            }
        }
    }

    public void selectTabByText(String tabText) {
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

    public boolean removeTabByName(String tabName) {
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

    public boolean removeTabByText(String tabText) {
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

    public boolean removeTab(Tab tab) {
        if (tab == null)
            return false;
        if (tabMapView.containsKey(tab)) {
            return removeItem(tab);
        }
        return false;
    }

    public boolean removeAllTabs() {
        if (_tabList.size() == 0)
            return false;
        Tab tab = null;
        while (_tabList.size() != 0) {
            tab = _tabList.get(0);
            removeItem(tab);
        }
        return true;
    }

    public void addItemToTabByName(String tabName, InterfaceBaseItem item) {
        for (Tab tab : tabMapView.keySet()) {
            if (tabName.equals(tab.getItemName())) {
                tabMapView.get(tab).addItem(item);
                return;
            }
        }
    }

    public void addItemToTabByText(String tabText, InterfaceBaseItem item) {
        for (Tab tab : tabMapView.keySet()) {
            if (tabText.equals(tab.getText())) {
                tabMapView.get(tab).addItem(item);
                return;
            }
        }
    }

    public void addItemToTab(Tab tab, InterfaceBaseItem item) {
        if (tabMapView.containsKey(tab))
            tabMapView.get(tab).addItem(item);
    }
}