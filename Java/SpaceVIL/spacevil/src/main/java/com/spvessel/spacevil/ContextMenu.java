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
    private Prototype _returnFocus = null;

    public void setReturnFocus(Prototype item) {
        _returnFocus = item;
    }

    public Prototype getReturnFocusItem() {
        return _returnFocus;
    }

    public ListBox itemList = new ListBox();
    private List<InterfaceBaseItem> _queue = new LinkedList<>();

    private Prototype _sender = null;

    public Prototype getSender() {
        return _sender;
    }

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

    /**
     * Constructs a ContextMenu
     * 
     * @param handler parent window for the ContextMenu
     */
    public ContextMenu(CoreWindow handler) {
        ItemsLayoutBox.addItem(handler, this, LayoutType.FLOATING);
        setItemName("ContextMenu_" + count++);
        setStyle(DefaultsService.getDefaultStyle(ContextMenu.class));
        setPassEvents(false);
        setVisible(false);
    }

    public ContextMenu(CoreWindow handler, MenuItem... items) {
        this(handler);
        for (MenuItem item : items)
            addItem(item);
    }

    /**
     * Initialization and adding of all elements in the ContextMenu
     */
    @Override
    public void initElements() {
        setConfines();
        itemList.disableMenu(true);
        itemList.setVScrollBarVisible(ScrollBarVisibility.NEVER);
        itemList.setHScrollBarVisible(ScrollBarVisibility.NEVER);
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
     * Remove item from the ContextMenu
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
     * Show the ContextMenu
     * 
     * @param sender the item from which the show request is sent
     * @param args   mouse click arguments (cursor position, mouse button, mouse
     *               button press/release, etc.)
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

    public void show() {
        MouseArgs margs = new MouseArgs();
        margs.button = MouseButton.BUTTON_RIGHT;
        show(this, margs);
    }

    private boolean _added = false;

    @Override
    public void clear() {
        itemList.clear();
        _queue.clear();
        _added = false;
    }

    /**
     * Hide the ContextMenu without destroying
     */
    public void hide() {
        itemList.unselect();
        setVisible(false);
        setX(-getWidth());
        
        if (_returnFocus != null)
            _returnFocus.setFocus();
        // else
        // getHandler().getWindow().setFocus();
    }

    public void hide(MouseArgs args) {
        hide();
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
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("itemlist");
        if (inner_style != null) {
            itemList.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("listarea");
        if (inner_style != null) {
            itemList.getArea().setStyle(inner_style);
        }
    }
}