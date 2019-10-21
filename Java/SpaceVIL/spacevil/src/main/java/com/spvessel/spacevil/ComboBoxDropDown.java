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
import com.spvessel.spacevil.Flags.ScrollBarVisibility;

public class ComboBoxDropDown extends Prototype implements InterfaceFloating {

    ComboBox parent = null;
    public EventCommonMethod selectionChanged = new EventCommonMethod();

    @Override
    public void release() {
        selectionChanged.clear();
    }

    public ListBox itemList = new ListBox();
    private String _text_selection = "";

    public String getText() {
        return _text_selection;
    }

    public int getCurrentIndex() {
        return itemList.getSelection();
    }

    public void setCurrentIndex(int index) {
        if (!_init)
            initElements();

        itemList.setSelection(index);
        if (itemList.getSelectedItem() instanceof MenuItem) {
            MenuItem selection = (MenuItem) itemList.getSelectedItem();
            _text_selection = selection.getText();
        }
    }

    private List<InterfaceBaseItem> _queue = new LinkedList<>();

    private static int count = 0;
    public MouseButton activeButton = MouseButton.BUTTON_LEFT;

    private boolean _init = false;
    private boolean _ouside = true;

    /**
     * Close the ContextMenu it mouse click is outside (true or false)
     */
    public boolean isOutsideClickClosable() {
        return _ouside;
    }

    public void setOutsideClickClosable(boolean value) {
        _ouside = value;
    }

    /**
     * Constructs a ContextMenu
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
        itemList.setVScrollBarVisible(ScrollBarVisibility.NEVER);
        itemList.setHScrollBarVisible(ScrollBarVisibility.NEVER);
        if (itemList.eventScrollUp.size() != 0)
            itemList.eventScrollUp.clear();
        if (itemList.eventScrollDown.size() != 0)
            itemList.eventScrollDown.clear();
        if (itemList.eventMouseClick.size() != 0)
            itemList.eventMouseClick.clear();
        if (itemList.eventKeyPress.size() != 0)
            itemList.eventKeyPress.clear();
    }

    private void enableAdditionalControls() {
        itemList.setVScrollBarVisible(ScrollBarVisibility.AS_NEEDED);
        itemList.setHScrollBarVisible(ScrollBarVisibility.AS_NEEDED);

        for (InterfaceMouseMethodState action : linkEventScrollUp.getActions())
            itemList.eventScrollUp.add(action);

        for (InterfaceMouseMethodState action : linkEventScrollDown.getActions())
            itemList.eventScrollDown.add(action);

        for (InterfaceMouseMethodState action : linkEventMouseClick.getActions())
            itemList.eventMouseClick.add(action);

        for (InterfaceKeyMethodState action : linkEventKeyPress.getActions())
            itemList.eventKeyPress.add(action);
    }

    private void saveAdditionalControls() {
        for (InterfaceMouseMethodState action : itemList.eventScrollUp.getActions())
            linkEventScrollUp.add(action);

        for (InterfaceMouseMethodState action : itemList.eventScrollDown.getActions())
            linkEventScrollDown.add(action);

        for (InterfaceMouseMethodState action : itemList.eventMouseClick.getActions())
            linkEventMouseClick.add(action);

        for (InterfaceKeyMethodState action : itemList.eventKeyPress.getActions())
            linkEventKeyPress.add(action);
    }

    /**
     * Initialization and adding of all elements in the ContextMenu
     */
    @Override
    public void initElements() {
        if (!_init) {
            itemList.disableMenu(true);
            super.addItem(itemList);
            saveAdditionalControls();
            disableAdditionalControls();
            // itemList.getArea().selectionChanged.add(this::onSelectionChanged);
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
            _text_selection = selection.getText();
        }
        hide();
        selectionChanged.execute();
    }

    /**
     * Returns count of the ContextMenu lines
     */
    public int getListCount() {
        return itemList.getListContent().size();
    }

    /**
     * Returns ContextMenu items list
     */
    public List<InterfaceBaseItem> getListContent() {
        return itemList.getListContent();
    }

    /**
     * Add item to the ContextMenu
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        if (_init)
            itemList.addItem(item);
        else
            _queue.add(item);
    }

    /**
     * Remove item from the ContextMenu
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
            } else
                tmp = tmp + item.getWidth() + item.getMargin().left + item.getMargin().right;

            if (width < tmp)
                width = tmp;
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
     * Show the ContextMenu
     * 
     * @param sender the item from which the show request is sent
     * @param args   mouse click arguments (cursor position, mouse button, mouse
     *               button press/release, etc.)
     */
    public void show(InterfaceItem sender, MouseArgs args) {
        if (args.button.getValue() == activeButton.getValue()) {
            initElements();
            setVisible(true);
            setConfines();
            itemList.getArea().setFocus();
        }
    }

    public void show() {
        MouseArgs args = new MouseArgs();
        args.button = activeButton;
        show(this, args);
    }

    /**
     * Hide the ContextMenu without destroying
     */
    public void hide() {
        setVisible(false);
        itemList.unselect();
        if (parent.returnFocus != null) {
            parent.setFocus();
        } else {
            getHandler().setFocus();
        }
    }

    public void hide(MouseArgs args) {
        if (!isVisible())
            return;

        hide();
        parent.isDropDownAreaOutsideClicked(args);
    }

    /**
     * Set confines according to position and size of the ContextMenu
     */
    @Override
    public void setConfines() {
        super.setConfines(getX(), getX() + getWidth(), getY(), getY() + getHeight());
    }

    /**
     * Set style of the ContextMenu
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        Style inner_style = style.getInnerStyle("itemlist");
        if (inner_style != null) {
            itemList.setStyle(inner_style);
        }
    }
}