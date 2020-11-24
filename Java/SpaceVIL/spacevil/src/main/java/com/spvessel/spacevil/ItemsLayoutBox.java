package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.LayoutType;

import java.util.*;

/**
 * ItemsLayoutBox is a storage-class that provides an access to existing items.
 */
public class ItemsLayoutBox {
    private ItemsLayoutBox() {

    }

    /**
     * Getting existing static items in specified window by its GUID. 
     * Static items are items that depend on their parent. Parent controls their size, position and etc.
     * @param id UUID of the window.
     * @return The list of existing static items in specified window by its UUID.
     */
    static public List<IBaseItem> getLayoutItems(UUID id) {
        // return layouts[id].Items.Concat(layouts[id].FloatItems).ToList();
        return layouts.get(id).getItems();
    }

    /**
     * Getting existing float items in specified window by its UUID. 
     * Floating items are independent items that do not have a parent, or their root parent is a floating item. 
     * Examples: SideArea, FloatItem, ContextMenu and etc.
     * @param id UUID of the window.
     * @return The list of existing float items in specified window by its UUID.
     */
    static public List<IBaseItem> getLayoutFloatItems(UUID id) {
        // return layouts[id].Items.Concat(layouts[id].FloatItems).ToList();
        return layouts.get(id).getFloatItems();
    }

    static List<IBaseItem> getLayoutDialogItems(UUID id) {
        // return layouts[id].Items.Concat(layouts[id].FloatItems).ToList();
        return layouts.get(id).getDialogItems();
    }

    static ItemsLayout getLayout(UUID id) {
        return layouts.get(id);
    }

    static private Map<UUID, ItemsLayout> layouts = new HashMap<>();

    static void initLayout(UUID _layout) {
        ItemsLayout l = new ItemsLayout(_layout);
        layouts.put(l.getId(), l);
    }

    /**
     * Adding an item to global item storage (ItemsLayoutBox). 
     * In usual situation you do not need to use this function only if you create your own 
     * implementation of IBaseItem or create a new implementation of IFloatItem.
     * @param layout Any CoreWindow instance.
     * @param item Any IBaseItem instance.
     * @param type Type of an item: com.spvessel.spacevil.Flags.LayoutType.STATIC 
     * or com.spvessel.spacevil.Flags.LayoutType.FLOATING.
     */
    static public void addItem(CoreWindow layout, IBaseItem item, LayoutType type) {
        switch (type) {
        case Static:
            layouts.get(layout.getWindowGuid()).getItems().add(item);
            break;
        case Floating: {
            layouts.get(layout.getWindowGuid()).getFloatItems().add(item);
            item.setHandler(layout);
            break;
        }
        case Dialog:
            layouts.get(layout.getWindowGuid()).getDialogItems().add(item);
            break;
        default:
            layouts.get(layout.getWindowGuid()).getItems().add(item);
            break;
        }
    }

    /**
     * Removing an item from global item storage (ItemsLayoutBox). 
     * In usual situation you do not need to use this function only if you create your own 
     * implementation of IBaseItem or want to remove IFloatItem instance.
     * @param layout Any CoreWindow instance.
     * @param item Any IBaseItem instance.
     * @param type Type of an item: com.spvessel.spacevil.Flags.LayoutType.STATIC 
     * or com.spvessel.spacevil.Flags.LayoutType.FLOATING.
     * @return True: if removal was successfull. False: if the specified item does not exist in the storage.
     */
    static public boolean removeItem(CoreWindow layout, IBaseItem item, LayoutType type) {
        layout.freeVRAMResource(item);

        switch (type) {
        case Static:
            return layouts.get(layout.getWindowGuid()).getItems().remove(item);
        // break;
        case Floating: {
            unsubscribeWindowSizeMonitoring(item, GeometryEventType.ResizeWidth);
            unsubscribeWindowSizeMonitoring(item, GeometryEventType.ResizeHeight);
            return layouts.get(layout.getWindowGuid()).getFloatItems().remove(item);
        }
        // break;
        case Dialog:
            return layouts.get(layout.getWindowGuid()).getDialogItems().remove(item);
        // break;
        default:
            return layouts.get(layout.getWindowGuid()).getItems().remove(item);
        // break;
        }
    }

    static void subscribeWindowSizeMonitoring(IBaseItem item, GeometryEventType type) {
        // подписка
        item.setParent(item.getHandler().getLayout().getContainer());
        item.getHandler().getLayout().getContainer().addEventListener(type, item);
    }

    static void unsubscribeWindowSizeMonitoring(IBaseItem item, GeometryEventType type) {
        // отписка
        item.setParent(null);
        item.getHandler().getLayout().getContainer().removeEventListener(type, item);
    }

    /**
     * Getting the list of names of existing items in the specified window.
     * @param layout Any CoreWindow instance.
     * @return The list of names of existing items in the specified window.
     */
    static public String[] getListOfItemsNames(CoreWindow layout) {
        String[] result = new String[layouts.get(layout.getWindowGuid()).getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = layouts.get(layout.getWindowGuid()).getItems().get(i).getItemName();
        }
        return result;
    }

    static String[] getListOfItemsColors(CoreWindow layout) {
        String[] result = new String[layouts.get(layout.getWindowGuid()).getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = layouts.get(layout.getWindowGuid()).getItems().get(i).getBackground().toString();
        }
        return result;
    }

    /**
     * Printing all existing items in the specified window.
     * @param layout Any CoreWindow instance.
     */
    static public void printListOfItems(CoreWindow layout) {
        String[] list = getListOfItemsNames(layout);
        for (String item : list) {
            System.out.println(item);
        }
    }
}