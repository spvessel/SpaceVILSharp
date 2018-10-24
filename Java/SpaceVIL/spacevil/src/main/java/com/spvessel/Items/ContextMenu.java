package com.spvessel.Items;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Cores.*;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.LayoutType;
import com.spvessel.Flags.MouseButton;
import com.spvessel.Flags.ScrollBarVisibility;
import com.spvessel.Layouts.ItemsLayoutBox;
import com.spvessel.Windows.WindowLayout;

public class ContextMenu extends VisualItem implements InterfaceFloating {
    public ListBox itemList = new ListBox();
    private List<BaseItem> _queue = new LinkedList<>();

    private static int count = 0;
    public MouseButton activeButton = MouseButton.BUTTON_RIGHT;

    private boolean _init = false;
    private boolean _ouside = true;

    public boolean getOutsideClickClosable() {
        return _ouside;
    }

    public void setOutsideClickClosable(boolean value) {
        _ouside = value;
    }

    public ContextMenu(WindowLayout handler) {
        setItemName("ContextMenu_" + count);
        count++;
        setPassEvents(false);
        setVisible(false);
        setHandler(handler);
        ItemsLayoutBox.addItem(getHandler(), this, LayoutType.FLOATING);

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.ContextMenu"));
        setStyle(DefaultsService.getDefaultStyle(com.spvessel.Items.ContextMenu.class));
    }

    @Override
    public void initElements() {
        setConfines();
        itemList.setSelectionVisibility(false);
        itemList.setVScrollBarVisible(ScrollBarVisibility.NEVER);
        itemList.setHScrollBarVisible(ScrollBarVisibility.NEVER);
        InterfaceCommonMethod selectionChanged = () -> onSelectionChanged();
        itemList.getArea().selectionChanged.add(selectionChanged);

        super.addItem(itemList);

        itemList.eventScrollUp.clear();
        itemList.eventScrollDown.clear();

        for (BaseItem item : _queue)
            itemList.addItem(item);
        _queue = null;

        _init = true;
    }

    protected void onSelectionChanged() {
        System.out.println(itemList.getSelectionItem().getItemName());
        if (itemList.getSelectionItem() instanceof MenuItem) {
            MenuItem item = (MenuItem) itemList.getSelectionItem();
            if (item.isActionItem) {
                return;
            }
        }
        hide();
        MouseArgs args = new MouseArgs();
        args.position.setPosition(-100, -100);
        for (BaseItem context_menu : ItemsLayoutBox.getLayoutFloatItems(getHandler().getId())) {
            if (context_menu instanceof ContextMenu) {
                ContextMenu menu = (ContextMenu) context_menu;
                menu.hide();
            }
        }
    }

    public int getListCount() {
        return itemList.getListContent().size();
    }

    public List<BaseItem> getListContent() {
        return itemList.getListContent();
    }

    @Override
    public void addItem(BaseItem item) {
        // (item as MenuItem)._invoked_menu = this;
        if (item instanceof MenuItem) {
            MenuItem tmp = (MenuItem) item;
            tmp._context_menu = this;
        }
        _queue.add(item);

        BaseItem[] list = _queue.toArray(new BaseItem[_queue.size()]);
        int height = 0;
        for (BaseItem h : list)
            if (h.getVisible() && h.isDrawable)
                height += (h.getHeight() + itemList.getArea().getSpacing().vertical);
        setHeight(getPadding().top + getPadding().bottom + height);
    }

    @Override
    public void removeItem(BaseItem item) {
        itemList.removeItem(item);
    }

    public boolean closeDependencies(MouseArgs args) {
        for (BaseItem item : getListContent()) {
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

    public void show(InterfaceItem sender, MouseArgs args) {
        // System.out.println("Show? " + args.button + " " + activeButton);
        if (args.button.getValue() == activeButton.getValue()) {
            if (!_init)
                initElements();

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
        // System.out.println("4");
        setX(-getWidth());
        setVisible(false);
    }

    @Override
    public void setConfines() {
        _confines_x_0 = getX();
        _confines_x_1 = getX() + getWidth();
        _confines_y_0 = getY();
        _confines_y_1 = getY() + getHeight();
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