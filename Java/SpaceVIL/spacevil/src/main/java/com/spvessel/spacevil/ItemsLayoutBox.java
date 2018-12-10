package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Flags.LayoutType;

import java.util.*;

public class ItemsLayoutBox {
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

    static ItemsLayout getLayout(UUID id) {
        return layouts.get(id);
    }

    static private Map<UUID, ItemsLayout> layouts = new HashMap<UUID, ItemsLayout>();

    static void initLayout(UUID _layout) {
        ItemsLayout l = new ItemsLayout(_layout);
        layouts.put(l.getId(), l);
    }

    static void addItem(WindowLayout layout, InterfaceBaseItem item, LayoutType type) {
        switch (type) {
        case STATIC:
            layouts.get(layout.getId()).getItems().add(item);
            break;
        case FLOATING:
            layouts.get(layout.getId()).getFloatItems().add(item);
            break;
        default:
            layouts.get(layout.getId()).getItems().add(item);
            break;
        }
    }

    static void removeItem(WindowLayout layout, InterfaceBaseItem item, LayoutType type) {
        switch (type) {
        case STATIC:
            layouts.get(layout.getId()).getItems().remove(item);
            break;
        case FLOATING:
            layouts.get(layout.getId()).getFloatItems().remove(item);
            break;
        default:
            layouts.get(layout.getId()).getItems().remove(item);
            break;
        }
    }

    static public String[] getListOfItemsNames(WindowLayout layout) {
        String[] result = new String[layouts.get(layout.getId()).getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = layouts.get(layout.getId()).getItems().get(i).getItemName();
        }
        return result;
    }

    static public String[] getListOfItemsColors(WindowLayout layout) {
        String[] result = new String[layouts.get(layout.getId()).getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = layouts.get(layout.getId()).getItems().get(i).getBackground().toString();
        }
        return result;
    }

    static public void printListOfItems(WindowLayout layout) {
        String[] list = getListOfItemsNames(layout);
        for (String item : list) {
            System.out.println(item);
        }
    }
}