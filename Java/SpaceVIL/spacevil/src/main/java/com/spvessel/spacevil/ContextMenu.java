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
import com.spvessel.spacevil.Flags.ScrollBarVisibility;

import java.util.LinkedList;
import java.util.List;

public class ContextMenu extends Prototype implements InterfaceFloating {
    public Prototype returnFocus = null;
    public ListBox itemList = new ListBox();
    private List<InterfaceBaseItem> _queue = new LinkedList<>();

    private static int count = 0;
    public MouseButton activeButton = MouseButton.BUTTON_RIGHT;

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

    // private boolean _lock_ouside = true;

    // public boolean isLockOutside() {
    // return _lock_ouside;
    // }

    // public void setLockOutside(boolean value) {
    // _lock_ouside = true;
    // }
    /**
     * Constructs a ContextMenu
     * @param handler parent window for the ContextMenu
     */
    public ContextMenu(WindowLayout handler) {
        setItemName("ContextMenu_" + count);
        count++;
        setPassEvents(false);
        setVisible(false);
        setHandler(handler);
        ItemsLayoutBox.addItem(getHandler(), this, LayoutType.FLOATING);
        setStyle(DefaultsService.getDefaultStyle(ContextMenu.class));
    }

    /**
     * Initialization and adding of all elements in the ContextMenu
     */
    @Override
    public void initElements() {
        setConfines();
        itemList.setVScrollBarVisible(ScrollBarVisibility.NEVER);
        itemList.setHScrollBarVisible(ScrollBarVisibility.NEVER);
        itemList.getArea().selectionChanged.add(this::onSelectionChanged);

        super.addItem(itemList);

        itemList.eventScrollUp.clear();
        itemList.eventScrollDown.clear();

        for (InterfaceBaseItem item : _queue) {
            itemList.addItem(item);
        }
        _queue = null;
        _init = true;

        itemList.getArea().eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.ESCAPE) {
                hide();
                hideDependentMenus();
            }
        });
    }

    private void hideDependentMenus() {
        for (InterfaceBaseItem context_menu : ItemsLayoutBox.getLayoutFloatItems(getHandler().getId())) {
            if (context_menu instanceof ContextMenu) {
                ContextMenu menu = (ContextMenu) context_menu;
                menu.hide();
                menu.itemList.unselect();
            }
        }
    }

    private void onSelectionChanged() {
        if (itemList.getSelectionItem() instanceof MenuItem) {
            MenuItem item = (MenuItem) itemList.getSelectionItem();
            if (item.isActionItem) {
                return;
            }
        }
        hide();
        hideDependentMenus();
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
        // (item as MenuItem)._invoked_menu = this;
        if (item instanceof MenuItem) {
            MenuItem tmp = (MenuItem) item;
            tmp._context_menu = this;
        }
        _queue.add(item);
    }

    /**
     * Remove item from the ContextMenu
     */
    @Override
    public void removeItem(InterfaceBaseItem item) {
        itemList.removeItem(item);
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
            height += (item.getHeight() + itemList.getArea().getSpacing().vertical);

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
        setSize(width, height);
    }

    /**
     * Show the ContextMenu
     * @param sender the item from which the show request is sent
     * @param args mouse click arguments (cursor position, mouse button,
     *             mouse button press/release, etc.)
     */
    public void show(InterfaceItem sender, MouseArgs args) {
        if (args.button.getValue() == activeButton.getValue()) {
            if (!_init) {
                initElements();
                updateSize();
            }

            setVisible(true);

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
            itemList.getArea().setFocus();
        }
    }

    /**
     * Hide the ContextMenu without destroying
     */
    public void hide() {
        setX(-getWidth());
        setVisible(false);
        itemList.unselect();
        if (returnFocus != null)
            returnFocus.setFocus();
    }

    /**
     * Set confines according to position and size of the ContextMenu
     */
    @Override
    public void setConfines() {
        setConfines(getX(), getX() + getWidth(), getY(), getY() + getHeight());
    }

    /**
     * Set style of the ContextMenu
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        setPadding(style.padding);
        setSizePolicy(style.widthPolicy, style.heightPolicy);
        setBackground(style.background);

        Style inner_style = style.getInnerStyle("itemlist");
        if (inner_style != null) {
            itemList.setBackground(inner_style.background);
            itemList.setAlignment(inner_style.alignment);
            itemList.setPadding(inner_style.padding);
        }
        inner_style = style.getInnerStyle("listarea");
        if (inner_style != null) {
            itemList.getArea().setStyle(inner_style);
        }
    }
}