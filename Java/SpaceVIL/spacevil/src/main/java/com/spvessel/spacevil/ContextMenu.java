package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceFloating;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.LayoutType;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.VisibilityPolicy;

import java.util.LinkedList;
import java.util.List;

/**
 * ContextMenu is a menu for selecting one of the available options from the
 * list that perform the assigned action. ContextMenu is a floating item (see
 * com.spvessel.spacevil.Core.InterfaceFloating and enum
 * com.spvessel.spacevil.Flags.LayoutType) and closes when mouse click outside
 * the ContextMenu area.
 * <p>
 * Contains ListBox.
 * <p>
 * Supports all events except drag and drop.
 * <p>
 * Notice: All floating items render above all others items.
 * <p>
 * ContextMenu does not pass any input events and invisible by default.
 */
public class ContextMenu extends Prototype implements InterfaceFloating {
    /**
     * Property that allows to specify what item will be focused after ContextMenu
     * is closed.
     */
    public Prototype returnFocus = null;
    /**
     * ListBox for storing a list of options (com.spvessel.spacevil.MenuItem).
     */
    public ListBox itemList = new ListBox();
    private List<InterfaceBaseItem> _queue = new LinkedList<>();

    private Prototype _sender = null;

    /**
     * Getting the item that invokes ContextMenu.
     * 
     * @return Item as com.spvessel.spacevil.Prototype.
     */
    public Prototype getSender() {
        return _sender;
    }

    private static int count = 0;
    /**
     * You can specify mouse button (see com.spvessel.spacevil.Flags.MouseButton)
     * that is used to open ContextMenu.
     * <p>
     * Default: com.spvessel.spacevil.Flags.MouseButton.BUTTON_RIGHT.
     */
    public MouseButton activeButton = MouseButton.BUTTON_RIGHT;

    private boolean _init = false;
    private boolean _ouside = true;

    /**
     * Returns True if ContextMenu (see
     * com.spvessel.spacevil.Core.InterfaceFloating) should closes when mouse click
     * outside the area of ContextMenu otherwise returns False.
     * 
     * @return True: if ContextMenu closes when mouse click outside the area. False:
     *         if ContextMenu stays opened when mouse click outside the area.
     */
    public boolean isOutsideClickClosable() {
        return _ouside;
    }

    /**
     * Setting boolean value of item's behavior when mouse click occurs outside the
     * ContextMenu.
     * 
     * @param value True: ContextMenu should become invisible if mouse click occurs
     *              outside the item. False: an item should stay visible if mouse
     *              click occurs outside the item.
     */
    public void setOutsideClickClosable(boolean value) {
        _ouside = value;
    }

    /**
     * Constructs a ContextMenu and attaches it to the specified window (see
     * com.spvessel.spacevil.CoreWindow, com.spvessel.spacevil.ActiveWindow,
     * com.spvessel.spacevil.DialogWindow). ContextMenu does not pass any input
     * events and invisible by default.
     * 
     * @param handler Window for attaching ContextMenu.
     */
    public ContextMenu(CoreWindow handler) {
        ItemsLayoutBox.addItem(handler, this, LayoutType.FLOATING);
        setItemName("ContextMenu_" + count++);
        setStyle(DefaultsService.getDefaultStyle(ContextMenu.class));
        setPassEvents(false);
        setVisible(false);
    }

    /**
     * Constructs a ContextMenu with specified options and attaches it to the
     * specified window (see com.spvessel.spacevil.CoreWindow,
     * com.spvessel.spacevil.ActiveWindow, com.spvessel.spacevil.DialogWindow).
     * 
     * @param handler Window for attaching ContextMenu.
     * @param items   Sequence of options as com.spvessel.spacevil.MenuItem.
     */
    public ContextMenu(CoreWindow handler, MenuItem... items) {
        this(handler);
        for (MenuItem item : items)
            addItem(item);
    }

    /**
     * Initializing all elements in the ContextMenu.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        setConfines();
        itemList.disableMenu(true);
        itemList.setVScrollBarPolicy(VisibilityPolicy.NEVER);
        itemList.setHScrollBarPolicy(VisibilityPolicy.NEVER);
        super.addItem(itemList);
        itemList.eventScrollUp.clear();
        itemList.eventScrollDown.clear();
        itemList.eventMouseClick.clear();
        itemList.eventKeyPress.clear();

        itemList.getArea().eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.ESCAPE) {
                hide();
                hideDependentMenus();
            }
            if (args.key == KeyCode.ENTER) {
                if (itemList.getSelectedItem() instanceof Prototype) {
                    Prototype selected = (Prototype) itemList.getSelectedItem();
                    selected.eventMousePress.execute(selected, null);
                    selected.eventMouseClick.execute(selected, null);
                }
            }
        });

        for (InterfaceBaseItem item : _queue) {
            itemList.addItem(item);
        }

        _init = true;
    }

    private void hideDependentMenus() {
        for (InterfaceBaseItem context_menu : ItemsLayoutBox.getLayoutFloatItems(getHandler().getWindowGuid())) {
            if (context_menu instanceof ContextMenu && !context_menu.equals(this)) {
                ContextMenu menu = (ContextMenu) context_menu;
                menu.hide();
            }
        }
    }

    private void onSelectionChanged(InterfaceBaseItem sender) {
        if (sender instanceof MenuItem) {
            MenuItem menu = (MenuItem) sender;
            if (menu.isActionItem)
                return;
        }

        hide();
        hideDependentMenus();
    }

    /**
     * Getting number of options in the list.
     * 
     * @return Number of options in the list.
     */
    public int getListCount() {
        return itemList.getListContent().size();
    }

    /**
     * Getting all existing options (list of com.spvessel.spacevil.MenuItem
     * objects).
     * 
     * @return Options as List&lt;com.spvessel.spacevil.MenuItem&gt;
     */
    public List<InterfaceBaseItem> getListContent() {
        return itemList.getListContent();
    }

    /**
     * Adding option (or any com.spvessel.spacevil.Core.InterfaceBaseItem
     * implementation) to the ComboBoxDropDown.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        if (item instanceof MenuItem) {
            MenuItem tmp = (MenuItem) item;
            tmp.contextMenu = this;
            tmp.eventMouseClick.add((sender, args) -> {
                onSelectionChanged(tmp);
            });
        }
        if (_init) {
            itemList.addItem(item);
        } else {
            _queue.add(item);
        }
        _added = false;
    }

    /**
     * Removing option (or any com.spvessel.spacevil.Core.InterfaceBaseItem
     * implementation) from the ComboBoxDropDown.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    @Override
    public boolean removeItem(InterfaceBaseItem item) {
        return itemList.removeItem(item);
    }

    boolean closeDependencies(MouseArgs args) {
        for (InterfaceBaseItem item : getListContent()) {
            if (item instanceof MenuItem) {
                MenuItem menu_item = (MenuItem) item;
                if (menu_item.isActionItem) {
                    if (menu_item.isReadyToClose(args)) {
                        menu_item.hide();
                    } else
                        return false;
                }
            }
        }
        return true;
    }

    private void updateSize() {
        int height = 0;
        int width = getWidth();
        List<InterfaceBaseItem> list = itemList.getListContent();
        for (InterfaceBaseItem item : list) {
            InterfaceBaseItem wrapper = itemList.getWrapper(item);
            height += (wrapper.getHeight() + itemList.getArea().getSpacing().vertical);

            int tmp = getPadding().left + getPadding().right + item.getMargin().left + item.getMargin().right;
            if (item instanceof MenuItem) {
                MenuItem m = (MenuItem) item;
                tmp += m.getTextWidth() + m.getPadding().left + m.getPadding().right + m.getTextMargin().left
                        + m.getTextMargin().right;
                if (m.isActionItem)
                    tmp += m.getArrow().getWidth() + m.getArrow().getMargin().left + m.getArrow().getMargin().right;
            } else
                tmp = tmp + item.getWidth() + item.getMargin().left + item.getMargin().right;

            if (width < tmp)
                width = tmp;
        }
        if (height == 0)
            height = getHeight();
        else
            height -= itemList.getArea().getSpacing().vertical;
        setSize(width, height);
    }

    /**
     * Shows the ContextMenu at the proper position.
     * 
     * @param sender The item from which the show request is sent.
     * @param args   Mouse click arguments (cursor position, mouse button, mouse
     *               button press/release, etc.).
     */
    public void show(InterfaceItem sender, MouseArgs args) {
        if (args.button.equals(activeButton)) {
            if (!_init)
                initElements();
            if (!_added) {
                updateSize();
                _added = true;
            }

            if (sender instanceof Prototype)
                _sender = (Prototype) sender;

            // проверка снизу
            if (args.position.getY() + getHeight() > getHandler().getHeight()) {
                setY(args.position.getY() - getHeight());
            } else {
                setY(args.position.getY());
            }
            // проверка справа
            if (args.position.getX() + getWidth() > getHandler().getWidth()) {
                setX(args.position.getX() - getWidth());
            } else {
                setX(args.position.getX());
            }
            setConfines();
            setVisible(true);
            itemList.getArea().setFocus();
        }
    }

    /**
     * Shows the ContextMenu at the position (0, 0).
     */
    public void show() {
        MouseArgs margs = new MouseArgs();
        margs.button = MouseButton.BUTTON_RIGHT;
        show(this, margs);
    }

    private boolean _added = false;

    /**
     * Remove all content in the ContextMenu.
     */
    @Override
    public void clear() {
        itemList.clear();
        _queue.clear();
        _added = false;
    }

    /**
     * Hide the ContextMenu without destroying.
     */
    public void hide() {
        itemList.unselect();
        setVisible(false);
        setX(-getWidth());

        if (returnFocus != null) {
            returnFocus.setFocus();
        } else {
            getHandler().resetFocus();
        }
    }

    /**
     * Hide the ContextMenu without destroying with using specified mouse arguments.
     * 
     * @param args Arguments as com.spvessel.spacevil.Core.MouseArgs.
     */
    public void hide(MouseArgs args) {
        hide();
    }

    /**
     * Overridden method for setting confines according to position and size of the
     * ContextMenu (see Prototype.setConfines()).
     */
    @Override
    public void setConfines() {
        setConfines(getX(), getX() + getWidth(), getY(), getY() + getHeight());
    }

    /**
     * Setting style of the ContextMenu.
     * <p>
     * Inner styles: "itemlist".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style innerStyle = style.getInnerStyle("itemlist");
        if (innerStyle != null) {
            itemList.setStyle(innerStyle);
        }
    }
}