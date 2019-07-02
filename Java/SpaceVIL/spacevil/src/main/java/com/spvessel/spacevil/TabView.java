package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Style;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

public class TabView extends VerticalStack {
    private static int count = 0;
    
    // private Grid _tab_view;
    private HorizontalStack _tabBar;
    private Map<Tab, Frame> _tabMapView;
    private List<Tab> _tabList;

    public List<Tab> getTabs() {
        return new LinkedList<Tab>(_tabList);
    }

    private Tab _selectedTab = null;

    /**
     * Constructs a TabView
     */
    public TabView() {
        setItemName("TabView_" + count++);
        _tabList = new LinkedList<>();
        _tabMapView = new LinkedHashMap<>();
        _tabBar = new HorizontalStack();
        setStyle(DefaultsService.getDefaultStyle(TabView.class));
    }

    @Override
    protected boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }

    /**
     * Initialization and adding of all elements in the TabView
     */
    @Override
    public void initElements() {
        // tab view
        addItem(_tabBar);
    }

    private void hideOthers(Tab sender, MouseArgs args) {

        if (_selectedTab.equals(sender))
            return;
        if (_selectedTab != null) {
            _selectedTab.setToggled(false);
            _tabMapView.get(_selectedTab).setVisible(false);
        }

        sender.setToggled(true);
        _tabMapView.get(sender).setVisible(true);

        _selectedTab = sender;

        updateLayout();
    }

    public void selectTab(Tab tab) {
        hideOthers(tab, null);
    }

    public Tab getSelectedTab() {
        return _selectedTab;
    }

    public Frame getTabFrame(Tab tab) {
        return _tabMapView.get(tab);
    }

    public List<InterfaceBaseItem> getTabContent(Tab tab) {
        return _tabMapView.get(tab).getItems();
    }

    public void selectTabByName(String tabName) {
        for (Tab tab : _tabMapView.keySet()) {
            if (tabName.equals(tab.getItemName())) {
                hideOthers(tab, null);
                return;
            }
        }
    }

    public void selectTabByText(String tabText) {
        for (Tab tab : _tabMapView.keySet()) {
            if (tabText.equals(tab.getText())) {
                hideOthers(tab, null);
                return;
            }
        }
    }

    private void selectBestLeftoverTab(Tab tab) {
        if (_tabList.size() == 0)
            return;
        int index = _tabList.indexOf(tab);
        if (index > 0)
            selectTab(_tabList.get(index - 1));
        else if (_tabList.size() != 1)
            selectTab(_tabList.get(index + 1));
    }

    /**
     * Add new tab to the TabView
     * 
     * @param tab_name name of the new tab
     */
    public void addTab(Tab tab) {
        if (_tabMapView.containsKey(tab))
            return;

        _tabMapView.put(tab, tab.view);
        _tabList.add(tab);
        tab.view.setItemName(tab.getItemName() + "_view");
        tab.eventMouseClick.add((sender, args) -> {
            hideOthers(tab, args);
        });
        
        tab.eventTabRemove.add(() -> {
            selectBestLeftoverTab(tab);
            removeTab(tab);
        });

        addItem(tab.view);
        if (_tabBar.getItems().size() == 0) {
            tab.setToggled(true);
            tab.view.setVisible(true);
            _selectedTab = tab;
        }

        _tabBar.addItem(tab);
    }

    public void addTabs(Tab... tabs) {
        for (Tab tab : tabs) {
            addTab(tab);
        }
    }

    @Override
    public boolean removeItem(InterfaceBaseItem item) {
        if (item instanceof Tab) {
            Tab tmpTab = (Tab) item;
            if (_tabMapView.containsKey(tmpTab)) {
                return removeTab(tmpTab);
            }
            return false;
        }
        if (item.equals(_tabBar)) {
            Tab tab = null;
            while (!_tabList.isEmpty()) {
                tab = _tabList.get(0);
                _tabBar.removeItem(tab);
                _tabMapView.remove(tab);
                _tabList.remove(tab);
            }
        }
        return super.removeItem(item);
    }

    /**
     * Remove tab by name
     */
    public boolean removeTabByName(String tabName) {
        if (tabName == null)
            return false;
        for (Tab tab : _tabMapView.keySet()) {
            if (tabName.equals(tab.getItemName())) {
                removeItem(_tabMapView.get(tab));
                _tabBar.removeItem(tab);
                _tabMapView.remove(tab);
                _tabList.remove(tab);
                return true;
            }
        }
        return false;

    }

    /**
     * Remove tab by tab text
     */
    public boolean removeTabByText(String tabText) {
        if (tabText == null)
            return false;
        for (Tab tab : _tabMapView.keySet()) {
            if (tabText.equals(tab.getText())) {
                removeItem(_tabMapView.get(tab));
                _tabBar.removeItem(tab);
                _tabMapView.remove(tab);
                _tabList.remove(tab);
                return true;
            }
        }
        return false;
    }

    /**
     * Remove tab
     */
    public boolean removeTab(Tab tab) {
        if (tab == null)
            return false;
        if (_tabMapView.containsKey(tab)) {
            removeItem(_tabMapView.get(tab));
            _tabBar.removeItem(tab);
            _tabMapView.remove(tab);
            _tabList.remove(tab);
            return true;
        }
        return false;
    }

    public boolean removeAllTabs() {
        if (_tabList.size() == 0)
            return false;
        Tab tab = null;
        while (!_tabList.isEmpty()) {
            tab = _tabList.get(0);
            removeItem(_tabMapView.get(tab));
            _tabBar.removeItem(tab);
            _tabMapView.remove(tab);
            _tabList.remove(tab);
        }
        return true;
    }

    public void addItemToTabByName(String tabName, InterfaceBaseItem item) {
        for (Tab tab : _tabMapView.keySet()) {
            if (tabName.equals(tab.getItemName())) {
                _tabMapView.get(tab).addItem(item);
                return;
            }
        }
    }

    public void addItemToTabByText(String tabText, InterfaceBaseItem item) {
        for (Tab tab : _tabMapView.keySet()) {
            if (tabText.equals(tab.getText())) {
                _tabMapView.get(tab).addItem(item);
                return;
            }
        }
    }

    /**
     * Add InterfaceBaseItem item to the tab by tab
     */
    public void addItemToTab(Tab tab, InterfaceBaseItem item) {
        if (_tabMapView.containsKey(tab))
            _tabMapView.get(tab).addItem(item);
    }

    /**
     * Set style of the TabView
     */
    // style
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;

        super.setStyle(style);

        Style innerStyle = style.getInnerStyle("tabbar");
        if (innerStyle != null)
            _tabBar.setStyle(innerStyle);
    }
}