package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.List;

public class TabView extends VerticalStack {
    private static int count = 0;

    public void setContentPolicy(SizePolicy policy) {
        _tabBar.setContentPolicy(policy);
    }

    public SizePolicy getContentPolicy() {
        return _tabBar.getContentPolicy();
    }

    private TabBar _tabBar;
    private Frame _viewArea;

    public List<Tab> getTabs() {
        return _tabBar.getTabs();
    }

    /**
     * Constructs a TabView
     */
    public TabView() {
        setItemName("TabView_" + count++);
        _tabBar = new TabBar();
        _viewArea = new Frame();
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
        addItems(_tabBar, _viewArea);
    }

    public void selectTab(Tab tab) {
        _tabBar.selectTab(tab);
    }

    public Tab getSelectedTab() {
        return _tabBar.getSelectedTab();
    }

    public int getSelectedTabIndex() {
        return _tabBar.getSelectedTabIndex();
    }

    public Frame getTabFrame(Tab tab) {
        return _tabBar.getTabFrame(tab);
    }

    public List<InterfaceBaseItem> getTabContent(Tab tab) {
        return _tabBar.getTabContent(tab);
    }

    public void selectTabByName(String tabName) {
        _tabBar.selectTabByName(tabName);
    }

    public void selectTabByText(String tabText) {
        _tabBar.selectTabByText(tabText);
    }

    /**
     * Add new tab to the TabView
     * 
     * @param tab_name name of the new tab
     */
    public void addTab(Tab tab) {
        if (_tabBar.tabMapView.containsKey(tab))
            return;

        _tabBar.tabMapView.put(tab, tab.view);
        _viewArea.addItem(tab.view);
        _tabBar.addItem(tab);
        tab.eventMousePress.add((sender, args) -> {
            updateLayout();
        });
        tab.eventTabRemove.add(() -> {
            removeTab(tab);
            updateLayout();
        });
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
            if (_tabBar.tabMapView.containsKey(tmpTab)) {
                return removeTab(tmpTab);
            }
            return false;
        }
        if (item.equals(_tabBar)) {
            _tabBar.removeAllTabs();
        }
        return super.removeItem(item);
    }

    /**
     * Remove tab by name
     */
    public boolean removeTabByName(String tabName) {
        return _tabBar.removeTabByName(tabName);
    }

    /**
     * Remove tab by tab text
     */
    public boolean removeTabByText(String tabText) {
        return _tabBar.removeTabByText(tabText);
    }

    /**
     * Remove tab
     */
    public boolean removeTab(Tab tab) {
        return _tabBar.removeTab(tab);
    }

    public boolean removeAllTabs() {
        return _tabBar.removeAllTabs();
    }

    public void addItemToTabByName(String tabName, InterfaceBaseItem item) {
        _tabBar.addItemToTabByName(tabName, item);
    }

    public void addItemToTabByText(String tabText, InterfaceBaseItem item) {
        _tabBar.addItemToTabByText(tabText, item);
    }

    /**
     * Add InterfaceBaseItem item to the tab by tab
     */
    public void addItemToTab(Tab tab, InterfaceBaseItem item) {
        _tabBar.addItemToTab(tab, item);
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
            innerStyle = style.getInnerStyle("viewarea");
        if (innerStyle != null)
            _viewArea.setStyle(innerStyle);
    }
}