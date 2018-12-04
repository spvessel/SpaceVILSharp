package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.EventCommonMethod;
import com.spvessel.Core.InterfaceBaseItem;
import com.spvessel.Core.InterfaceCommonMethod;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.ScrollBarVisibility;

public class ComboBoxDropDown extends DialogWindow {
    public ListBox itemList; // = new ListBox();
    public ButtonCore selection;
    public EventCommonMethod selectionChanged = new EventCommonMethod();

    /**
     * Constructs a ComboBoxDropDown
     */
    public ComboBoxDropDown() {
    }

    @Override
    public void initWindow() {
        itemList = new ListBox();
        WindowLayout Handler = new WindowLayout(this, "DropDown_" + getCount(), "DropDown_" + getCount());
        setHandler(Handler);
        Handler.isOutsideClickClosable = true;
        Handler.isBorderHidden = true;
        Handler.isAlwaysOnTop = true;
        Handler.isCentered = false;
        Handler.isResizable = false;
        
        itemList.setVScrollBarVisible(ScrollBarVisibility.NEVER);
        itemList.setHScrollBarVisible(ScrollBarVisibility.NEVER);
        itemList.getArea().selectionChanged.add(this::onSelectionChanged);
        
        Handler.addItem(itemList);

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.ComboBoxDropDown"));
        setStyle(DefaultsService.getDefaultStyle(ComboBoxDropDown.class));
    }

    /**
     * Add item to the ComboBoxDropDown list
     */
    public void add(InterfaceBaseItem item) {
        itemList.addItem(item);
    }

    /**
     * Remove item from the ComboBoxDropDown
     */
    public void remove(InterfaceBaseItem item) {
        itemList.removeItem(item);
    }

    private void onSelectionChanged() {
        if (itemList.getListContent().get(itemList.getSelection()) instanceof MenuItem) {
            MenuItem l = (MenuItem) itemList.getListContent().get(itemList.getSelection());
            selection.setText(l.getText());
            getHandler().resetItems();
            close();
            selectionChanged.execute();
        }
    }

    /**
     * Current element in the ComboBoxDropDown by index
     */
    public void setCurrentIndex(int index) {
        itemList.setSelection(index);
    }

    public int getCurrentIndex() {
        return itemList.getSelection();
    }

    /**
     * Show the ComboBoxDropDown
     */
    @Override
    public void show() {
        super.show();
        WindowLayoutBox.setFocusedWindow(this);
    }

    /**
     * Dispose the ComboBoxDropDown
     */
    public void Dispose() {
        getHandler().close();
    }

    /**
     * Set style of the ComboBoxDropDown
     */
    public void setStyle(Style style) {
        if (style == null)
            return;

        getHandler().setBackground(style.background);
        getHandler().setPadding(style.padding);

        Style itemlist_style = style.getInnerStyle("itemlist");
        if (itemlist_style != null) {
            itemList.setBackground(itemlist_style.background);
            itemList.setAlignment(itemlist_style.alignment);
        }
    }
}