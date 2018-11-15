package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.InterfaceBaseItem;
import com.spvessel.Core.InterfaceCommonMethod;
import com.spvessel.Core.InterfaceFloating;
import com.spvessel.Core.InterfaceItem;
import com.spvessel.Core.MouseArgs;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.LayoutType;
import com.spvessel.Flags.MouseButton;
import com.spvessel.Flags.ScrollBarVisibility;

import java.util.LinkedList;
import java.util.List;

public class ContextMenu extends Prototype implements InterfaceFloating {
    public ListBox itemList = new ListBox();
    private List<InterfaceBaseItem> _queue = new LinkedList<>();

    private static int count = 0;
    public MouseButton activeButton = MouseButton.BUTTON_RIGHT;

    private boolean _init = false;
    private boolean _ouside = true;

    public boolean isOutsideClickClosable() {
        return _ouside;
    }

    public void setOutsideClickClosable(boolean value) {
        _ouside = value;
    }

    // private boolean _lock_ouside = true;

    // public boolean isLockOutside() {
    //     return _lock_ouside;
    // }

    // public void setLockOutside(boolean value) {
    //     _lock_ouside = true;
    // }

    public ContextMenu(WindowLayout handler) {
        setItemName("ContextMenu_" + count);
        count++;
        setPassEvents(false);
        setVisible(false);
        setHandler(handler);
        ItemsLayoutBox.addItem(getHandler(), this, LayoutType.FLOATING);
        setStyle(DefaultsService.getDefaultStyle(ContextMenu.class));
    }

    @Override
    public void initElements() {
        setConfines();
        itemList.setSelectionVisibility(false);
        itemList.setVScrollBarVisible(ScrollBarVisibility.NEVER);
        itemList.setHScrollBarVisible(ScrollBarVisibility.NEVER);
        itemList.getArea().setHoverVisibility(false);
        InterfaceCommonMethod selectionChanged = () -> onSelectionChanged();
        itemList.getArea().selectionChanged.add(selectionChanged);

        super.addItem(itemList);

        itemList.eventScrollUp.clear();
        itemList.eventScrollDown.clear();

        for (InterfaceBaseItem item : _queue) {
            itemList.addItem(item);
        }
        _queue = null;
        _init = true;

    }

    protected void onSelectionChanged() {
        if (itemList.getSelectionItem() instanceof MenuItem) {
            MenuItem item = (MenuItem) itemList.getSelectionItem();
            if (item.isActionItem) {
                return;
            }
        }
        hide();
        MouseArgs args = new MouseArgs();
        args.position.setPosition(-100, -100);
        for (InterfaceBaseItem context_menu : ItemsLayoutBox.getLayoutFloatItems(getHandler().getId())) {
            if (context_menu instanceof ContextMenu) {
                ContextMenu menu = (ContextMenu) context_menu;
                menu.hide();
            }
        }
    }

    public int getListCount() {
        return itemList.getListContent().size();
    }

    public List<InterfaceBaseItem> getListContent() {
        return itemList.getListContent();
    }

    @Override
    public void addItem(InterfaceBaseItem item) {
        // (item as MenuItem)._invoked_menu = this;
        if (item instanceof MenuItem) {
            MenuItem tmp = (MenuItem) item;
            tmp._context_menu = this;
        }
        _queue.add(item);
    }

    @Override
    public void removeItem(InterfaceBaseItem item) {
        itemList.removeItem(item);
    }

    public boolean closeDependencies(MouseArgs args) {
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

    void updateSize() {
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
        }
    }

    public void hide() {
        setX(-getWidth());
        setVisible(false);
    }

    @Override
    public void setConfines() {
        setConfines(getX(), getX() + getWidth(), getY(), getY() + getHeight());
    }

    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        setPadding(style.padding);
        setSizePolicy(style.widthPolicy, style.heightPolicy);
        setBackground(style.background);

        Style itemlist_style = style.getInnerStyle("itemlist");
        if (itemlist_style != null) {
            itemList.setBackground(itemlist_style.background);
            itemList.setAlignment(itemlist_style.alignment);
            itemList.setPadding(itemlist_style.padding);
        }
    }
}