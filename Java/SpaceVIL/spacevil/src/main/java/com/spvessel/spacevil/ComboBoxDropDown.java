package com.spvessel.spacevil;

import java.util.LinkedList;
import java.util.List;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Core.EventKeyMethodState;
import com.spvessel.spacevil.Core.EventMouseMethodState;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceFloating;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.InterfaceKeyMethodState;
import com.spvessel.spacevil.Core.InterfaceMouseMethodState;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.VisibilityPolicy;

/**
 * ComboBoxDropDown is drop-down list implementation for ComboBox (see com.spvessel.spacevil.ComboBox). 
 * ComboBox do not contains ComboBoxDropDown in usual way (ComboBox.GetItems() does not 
 * return ComboBoxDropDown), they just connected with each other. Used for selecting 
 * option from the list. ComboBoxDropDown is a floating item (see com.spvessel.spacevil.Core.InterfaceFloating 
 * and  enum com.spvessel.spacevil.Flags.LayoutType) and closes when mouse click outside the 
 * ComboBoxDropDown area.
 * <p> Contains ListBox. 
 * <p> Supports all events except drag and drop.
 * <p> Notice: All floating items render above all others items.
 */
public class ComboBoxDropDown extends Prototype implements InterfaceFloating {

    ComboBox parent = null;

    /**
     * Event that is invoked when one of the options is selected.
     */
    public EventCommonMethod selectionChanged = new EventCommonMethod();

    /**
     * Disposing ComboBoxDropDown resources if it was removed.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void release() {
        selectionChanged.clear();
    }

    /**
     * ListBox for storing a list of options (com.spvessel.spacevil.MenuItem).
     */
    public ListBox itemList = new ListBox();
    private String _textSelection = "";

    /**
     * Getting the text of selected option.
     * @return Text of selected option.
     */
    public String getText() {
        return _textSelection;
    }

    /**
     * Getting index of the current selected option in the list.
     * @return Index of the current selected option
     */
    public int getCurrentIndex() {
        return itemList.getSelection();
    }

    /**
     * Selecting an option from the list at the specified index.
     * @param index Index of option in the list.
     */
    public void setCurrentIndex(int index) {
        // if (!_init) {
            initElements();
        // }

        itemList.setSelection(index);
        if (itemList.getSelectedItem() instanceof MenuItem) {
            MenuItem selection = (MenuItem) itemList.getSelectedItem();
            _textSelection = selection.getText();
        }
    }

    private List<InterfaceBaseItem> _queue = new LinkedList<>();

    private static int count = 0;

    /**
     * You can specify mouse button (see com.spvessel.spacevil.Flags.MouseButton) 
     * that is used to open ComboBoxDropDown.
     * <p> Default: com.spvessel.spacevil.Flags.MouseButton.BUTTON_LEFT.
     */
    public MouseButton activeButton = MouseButton.BUTTON_LEFT;

    private boolean _init = false;
    private boolean _ouside = true;

    /**
     * Returns True if ComboBoxDropDown (see com.spvessel.spacevil.Core.InterfaceFloating)
     * should closes when mouse click outside the area of ComboBoxDropDown otherwise returns False.
     * @return True: if ComboBoxDropDown closes when mouse click outside the area.
     * False: if ComboBoxDropDown stays opened when mouse click outside the area.
     */
    public boolean isOutsideClickClosable() {
        return _ouside;
    }

    /**
     * Setting boolean value of item's behavior when mouse click occurs outside the ComboBoxDropDown.
     * @param value True: ComboBoxDropDown should become invisible if mouse click occurs outside the item.
     * False: an item should stay visible if mouse click occurs outside the item.
     */
    public void setOutsideClickClosable(boolean value) {
        _ouside = value;
    }

    /**
     * Default ComboBoxDropDown constructor. 
     * ComboBoxDropDown does not pass any input events and invisible by default.
     */
    public ComboBoxDropDown() {
        setItemName("ComboBoxDropDown_" + count++);
        setStyle(DefaultsService.getDefaultStyle(ComboBoxDropDown.class));
        setPassEvents(false);
        setVisible(false);
    }

    private EventMouseMethodState linkEventScrollUp = new EventMouseMethodState();
    private EventMouseMethodState linkEventScrollDown = new EventMouseMethodState();
    private EventMouseMethodState linkEventMouseClick = new EventMouseMethodState();
    private EventKeyMethodState linkEventKeyPress = new EventKeyMethodState();

    private void disableAdditionalControls() {
        itemList.setVScrollBarPolicy(VisibilityPolicy.NEVER);
        itemList.setHScrollBarPolicy(VisibilityPolicy.NEVER);
        if (itemList.eventScrollUp.size() != 0) {
            itemList.eventScrollUp.clear();
        }
        if (itemList.eventScrollDown.size() != 0) {
            itemList.eventScrollDown.clear();
        }
        if (itemList.eventMouseClick.size() != 0) {
            itemList.eventMouseClick.clear();
        }
        if (itemList.eventKeyPress.size() != 0) {
            itemList.eventKeyPress.clear();
        }
    }

    private void enableAdditionalControls() {
        itemList.setVScrollBarPolicy(VisibilityPolicy.AS_NEEDED);
        itemList.setHScrollBarPolicy(VisibilityPolicy.AS_NEEDED);

        for (InterfaceMouseMethodState action : linkEventScrollUp.getActions()) {
            itemList.eventScrollUp.add(action);
        }

        for (InterfaceMouseMethodState action : linkEventScrollDown.getActions()) {
            itemList.eventScrollDown.add(action);
        }

        for (InterfaceMouseMethodState action : linkEventMouseClick.getActions()) {
            itemList.eventMouseClick.add(action);
        }

        for (InterfaceKeyMethodState action : linkEventKeyPress.getActions()) {
            itemList.eventKeyPress.add(action);
        }
    }

    private void saveAdditionalControls() {
        for (InterfaceMouseMethodState action : itemList.eventScrollUp.getActions()) {
            linkEventScrollUp.add(action);
        }

        for (InterfaceMouseMethodState action : itemList.eventScrollDown.getActions()) {
            linkEventScrollDown.add(action);
        }

        for (InterfaceMouseMethodState action : itemList.eventMouseClick.getActions()) {
            linkEventMouseClick.add(action);
        }

        for (InterfaceKeyMethodState action : itemList.eventKeyPress.getActions()) {
            linkEventKeyPress.add(action);
        }
    }

    private int _selectionIndexStore = -1;

    /**
     * Initializing all elements in the ComboBoxDropDown. 
     * <p> Notice: This method is mainly for overriding only. SpaceVIL calls 
     * this method if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        if (!_init) {
            itemList.disableMenu(true);
            super.addItem(itemList);
            saveAdditionalControls();
            disableAdditionalControls();
            // itemList.getArea().selectionChanged.add(() -> onSelectionChanged());
            itemList.getArea().eventMouseClick.add((sender, args) -> {
                if (itemList.getSelection() != _selectionIndexStore) {
                    onSelectionChanged();
                    _selectionIndexStore = itemList.getSelection();
                }
            });

            itemList.getArea().eventKeyPress.add((sender, args) -> {
                if (args.key == KeyCode.ESCAPE) {
                    hide();
                } else if (args.key == KeyCode.ENTER && args.mods.contains(KeyMods.NO)) {
                    onSelectionChanged();
                }
            });
            for (InterfaceBaseItem item : _queue) {
                itemList.addItem(item);
            }
            _queue = null;
            _init = true;
        }
        updateSize();
    }

    private void onSelectionChanged() {
        if (itemList.getSelectedItem() instanceof MenuItem) {
            MenuItem selection = (MenuItem) itemList.getSelectedItem();
            _textSelection = selection.getText();
        }
        hide();
        selectionChanged.execute();
    }

    /**
     * Getting number of options in the list.
     * @return Number of options in the list.
     */
    public int getListCount() {
        return itemList.getListContent().size();
    }

    /**
     * Getting all existing options (list of com.spvessel.spacevil.MenuItem objects).
     * @return Options as List&lt;com.spvessel.spacevil.MenuItem&gt;
     */
    public List<InterfaceBaseItem> getListContent() {
        return itemList.getListContent();
    }

    /**
     * Adding option (or any com.spvessel.spacevil.Core.InterfaceBaseItem implementation) to the ComboBoxDropDown.
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        if (_init) {
            itemList.addItem(item);
        } else {
            _queue.add(item);
        }
    }

    /**
     * Removing option (or any com.spvessel.spacevil.Core.InterfaceBaseItem implementation) from the ComboBoxDropDown.
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem
     * @return True: if the removal was successful. 
     * False: if the removal was unsuccessful.
     */
    @Override
    public boolean removeItem(InterfaceBaseItem item) {
        return itemList.removeItem(item);
    }

    private void updateSize() {
        int height = itemList.getPadding().top + itemList.getPadding().bottom;
        int width = getWidth();
        List<InterfaceBaseItem> list = itemList.getListContent();
        for (InterfaceBaseItem item : list) {
            InterfaceBaseItem wrapper = itemList.getWrapper(item);
            height += (wrapper.getHeight() + itemList.getArea().getSpacing().vertical);

            int tmp = getPadding().left + getPadding().right + item.getMargin().left + item.getMargin().right;

            if (item instanceof MenuItem) {
                MenuItem m = (MenuItem) item;
                tmp += m.getTextWidth() + m.getMargin().left + m.getMargin().right + m.getPadding().left
                        + m.getPadding().right;
            } else {
                tmp = tmp + item.getWidth() + item.getMargin().left + item.getMargin().right;
            }

            if (width < tmp) {
                width = tmp;
            }
        }
        if ((getY() + height) > getHandler().getHeight()) {
            enableAdditionalControls();
            setHeight(getHandler().getHeight() - getY() - 10);
        } else {
            disableAdditionalControls();
            setHeight(height);
            itemList.vScrollBar.slider.setCurrentValue(itemList.vScrollBar.slider.getMinValue());
        }
        setWidth(width);
    }

    /**
     * Shows the ComboBoxDropDown at the proper position.
     * 
     * @param sender The item from which the show request is sent.
     * @param args   Mouse click arguments (cursor position, mouse button, mouse
     *               button press/release, etc.).
     */
    public void show(InterfaceItem sender, MouseArgs args) {
        if (args.button.getValue() == activeButton.getValue()) {
            initElements();
            setVisible(true);
            setConfines();
            itemList.getArea().setFocus();
        }
    }

    /**
     * Shows the ComboBoxDropDown at the position (0, 0).
     */
    public void show() {
        MouseArgs args = new MouseArgs();
        args.button = activeButton;
        show(this, args);
    }

    /**
     * Hide the ContextMenu without destroying.
     */
    public void hide() {
        setVisible(false);
        itemList.unselect();
        if (parent.returnFocus != null) {
            parent.setFocus();
        } else {
            getHandler().resetFocus();
        }
    }

    /**
     * Hide the ComboBoxDropDown without destroying with using specified mouse arguments.
     * @param args Arguments as com.spvessel.spacevil.Core.MouseArgs.
     */
    public void hide(MouseArgs args) {
        if (!isVisible()) {
            return;
        }

        hide();
        parent.isDropDownAreaOutsideClicked(args);
    }

    /**
     * Overridden method for setting confines according 
     * to position and size of the ComboBoxDropDown (see Prototype.setConfines()).
     */
    @Override
    public void setConfines() {
        super.setConfines(getX(), getX() + getWidth(), getY(), getY() + getHeight());
    }

    /**
     * Setting style of the ComboBoxDropDown.
     * <p> Inner styles: "itemlist".
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null) {
            return;
        }
        super.setStyle(style);
        Style innerStyle = style.getInnerStyle("itemlist");
        if (innerStyle != null) {
            itemList.setStyle(innerStyle);
        }
    }
}