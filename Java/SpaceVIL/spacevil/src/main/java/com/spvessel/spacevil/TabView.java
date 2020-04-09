package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.List;

/**
 * TabView is a special container designed to group items within specific tab
 * page.
 * <p>
 * TabView cannot receive any events, so TabView is always in the
 * com.spvessel.spacevil.Flags.ItemStateType.BASE state.
 */
public class TabView extends VerticalStack {
    private static int count = 0;

    /**
     * Setting the width policy of tabs. Can be FIXED (tab shape not changes its
     * size) or EXPAND (tab shape is stretched to all available space).
     * 
     * @param policy Width policy of tab shapes.
     */
    public void setTabPolicy(SizePolicy policy) {
        _tabBar.setContentPolicy(policy);
    }

    /**
     * Getting the current width policy of tabs. Can be FIXED (tab shape not changes
     * its size) or EXPAND (tab shape is stretched to all available space).
     * 
     * @return Width policy of tab shapes.
     */
    public SizePolicy getTabPolicy() {
        return _tabBar.getContentPolicy();
    }

    private TabBar _tabBar;
    private Frame _viewArea;

    /**
     * Getting all existing tabs in TabView.
     * 
     * @return Tabs as List&lt;com.spvessel.spacevil.Tab&gt;.
     */
    public List<Tab> getTabs() {
        return _tabBar.getTabs();
    }

    /**
     * Default TabView constructor.
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
     * Initializing all elements in the TabView.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        addItems(_tabBar, _viewArea);
    }

    /**
     * Selecting a tab by its Tab instance.
     * 
     * @param tab Tab to select.
     */
    public void selectTab(Tab tab) {
        _tabBar.selectTab(tab);
    }

    /**
     * Selecting a tab by its index.
     * <p>
     * If index is out of range then this method does nothing.
     * 
     * @param index Index of a tab.
     */
    public void selectTab(int index) {
        _tabBar.selectTab(index);
    }

    /**
     * Selecting a tab by its name.
     * <p>
     * Tab name is ID of UI element. Tab name is uniq but tab text is not.
     * <p>
     * Explanation: the tab text can be a web address, and there can be several open
     * tabs with the same address.
     * 
     * @param tabName Tab name.
     */
    public void selectTabByName(String tabName) {
        _tabBar.selectTabByName(tabName);
    }

    /**
     * Selecting a first-found tab by its text.
     * <p>
     * Tab name is ID of UI element. Tab name is uniq but tab text is not.
     * <p>
     * Explanation: the tab text can be a web address, and there can be several open
     * tabs with the same address.
     * 
     * @param tabText Tab text.
     */
    public void selectTabByText(String tabText) {
        _tabBar.selectTabByText(tabText);
    }

    /**
     * Getting the index of the current selected tab.
     * 
     * @return Index of the current selected tab.
     */
    public int getSelectedTabIndex() {
        return _tabBar.getSelectedTabIndex();
    }

    /**
     * Getting the current selected tab.
     * 
     * @return Current selected tab as com.spvessel.spacevil.Tab.
     */
    public Tab getSelectedTab() {
        return _tabBar.getSelectedTab();
    }

    /**
     * Getting a page of a tab.
     * 
     * @param tab Tab as com.spvessel.spacevil.Tab.
     * @return Page of a tab as com.spvessel.spacevil.Frame.
     */
    public Frame getTabFrame(Tab tab) {
        return _tabBar.getTabFrame(tab);
    }

    /**
     * Getting content of a page by its tab.
     * 
     * @param tab Tab as com.spvessel.spacevil.Tab.
     * @return Page content as
     *         List&lt;com.spvessel.spacevil.Core.InterfaceBaseItem&gt;.
     */
    public List<InterfaceBaseItem> getTabContent(Tab tab) {
        return _tabBar.getTabContent(tab);
    }

    /**
     * Adding a new tab to the TabView.
     * 
     * @param tab The new tab as com.spvessel.spacevil.Tab.
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

    /**
     * Adding new tabs to the TabView.
     * 
     * @param tabs Tab sequence as com.spvessel.spacevil.Tab.
     */
    public void addTabs(Tab... tabs) {
        for (Tab tab : tabs) {
            addTab(tab);
        }
    }

    /**
     * Removing the specified item from TabView.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
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
     * Removing a tab by its name.
     * <p>
     * Tab name is ID of UI element. Tab name is uniq but tab text is not.
     * <p>
     * Explanation: the tab text can be a web address, and there can be several open
     * tabs with the same address.
     * 
     * @param tabName Tab name.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    public boolean removeTabByName(String tabName) {
        return _tabBar.removeTabByName(tabName);
    }

    /**
     * Removing a first-found tab by its text.
     * <p>
     * Tab name is ID of UI element. Tab name is uniq but tab text is not.
     * <p>
     * Explanation: the tab text can be a web address, and there can be several open
     * tabs with the same address.
     * 
     * @param tabText Tab text.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    public boolean removeTabByText(String tabText) {
        return _tabBar.removeTabByText(tabText);
    }

    /**
     * Removing a tab.
     * 
     * @param tab Tab as com.spvessel.spacevil.Tab.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    public boolean removeTab(Tab tab) {
        return _tabBar.removeTab(tab);
    }

    /**
     * Removing all existing tabs.
     * 
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    public boolean removeAllTabs() {
        return _tabBar.removeAllTabs();
    }

    /**
     * Adding an item to page by its tab name.
     * <p>
     * Tab name is ID of UI element. Tab name is uniq but tab text is not.
     * <p>
     * Explanation: the tab text can be a web address, and there can be several open
     * tabs with the same address.
     * 
     * @param tabName Tab name.
     * @param item    Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    public void addItemToTabByName(String tabName, InterfaceBaseItem item) {
        _tabBar.addItemToTabByName(tabName, item);
    }

    /**
     * Adding an item to page by its tab text.
     * <p>
     * Tab name is ID of UI element. Tab name is uniq but tab text is not.
     * <p>
     * Explanation: the tab text can be a web address, and there can be several open
     * tabs with the same address.
     * 
     * @param tabText Tab text.
     * @param item    Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    public void addItemToTabByText(String tabText, InterfaceBaseItem item) {
        _tabBar.addItemToTabByText(tabText, item);
    }

    /**
     * Adding an item to page by its tab.
     * 
     * @param tab  Tab as com.spvessel.spacevil.Tab.
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    public void addItemToTab(Tab tab, InterfaceBaseItem item) {
        _tabBar.addItemToTab(tab, item);
    }

    /**
     * Setting style of the TabView.
     * <p>
     * Inner styles: Inner styles: "tabbar", "viewarea".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
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