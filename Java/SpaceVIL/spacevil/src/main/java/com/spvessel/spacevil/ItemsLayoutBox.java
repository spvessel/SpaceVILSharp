package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Flags.LayoutType;

import java.util.*;

public class ItemsLayoutBox {
    /**
     * A storage-class that provides an access to existing ItemLayouts by UUID
     */
    private ItemsLayoutBox() {

    }

    static public List<InterfaceBaseItem> getLayoutItems(UUID id) {
        // return layouts[id].Items.Concat(layouts[id].FloatItems).ToList();
        return layouts.get(id).getItems();
    }

    static public List<InterfaceBaseItem> getLayoutFloatItems(UUID id) {
        // return layouts[id].Items.Concat(layouts[id].FloatItems).ToList();
        return layouts.get(id).getFloatItems();
    }
    static public List<InterfaceBaseItem> getLayoutDialogItems(UUID id) {
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

    static void addItem(CoreWindow layout, InterfaceBaseItem item, LayoutType type) {
        switch (type) {
        case STATIC:
            layouts.get(layout.getWindowGuid()).getItems().add(item);
            break;
        case FLOATING:
            layouts.get(layout.getWindowGuid()).getFloatItems().add(item);
            break;
        case DIALOG:
            layouts.get(layout.getWindowGuid()).getDialogItems().add(item);
            break;
        default:
            layouts.get(layout.getWindowGuid()).getItems().add(item);
            break;
        }
    }

    static boolean removeItem(CoreWindow layout, InterfaceBaseItem item, LayoutType type) {
        switch (type) {
        case STATIC:
            return layouts.get(layout.getWindowGuid()).getItems().remove(item);
//            break;
        case FLOATING:
            return layouts.get(layout.getWindowGuid()).getFloatItems().remove(item);
//            break;
        case DIALOG:
            return layouts.get(layout.getWindowGuid()).getDialogItems().remove(item);
//            break;
        default:
            return layouts.get(layout.getWindowGuid()).getItems().remove(item);
//            break;
        }
    }

    static public String[] getListOfItemsNames(CoreWindow layout) {
        String[] result = new String[layouts.get(layout.getWindowGuid()).getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = layouts.get(layout.getWindowGuid()).getItems().get(i).getItemName();
        }
        return result;
    }

    static public String[] getListOfItemsColors(CoreWindow layout) {
        String[] result = new String[layouts.get(layout.getWindowGuid()).getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = layouts.get(layout.getWindowGuid()).getItems().get(i).getBackground().toString();
        }
        return result;
    }

    static public void printListOfItems(CoreWindow layout) {
        String[] list = getListOfItemsNames(layout);
        for (String item : list) {
            System.out.println(item);
        }
    }
}