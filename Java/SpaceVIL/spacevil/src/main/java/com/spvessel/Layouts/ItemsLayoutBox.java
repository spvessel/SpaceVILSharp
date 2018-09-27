package com.spvessel.Layouts;

import com.spvessel.Items.*;
import com.spvessel.Flags.*;
import com.spvessel.Windows.*;
import java.util.*;
import java.util.stream.Collectors;

public class ItemsLayoutBox {
    private ItemsLayoutBox() {

    }

    static public List<BaseItem> getLayoutItems(UUID id) {
        // return layouts[id].Items.Concat(layouts[id].FloatItems).ToList();
        return layouts.get(id).getItems();
    }

    static public List<BaseItem> getLayoutFloatItems(UUID id) {
        // return layouts[id].Items.Concat(layouts[id].FloatItems).ToList();
        return layouts.get(id).getFloatItems();
    }

    static public ItemsLayout getLayout(UUID id) {
        return layouts.get(id);
    }

    static private Map<UUID, ItemsLayout> layouts = new HashMap<UUID, ItemsLayout>();

    static protected void initLayout(UUID _layout) {
        ItemsLayout l = new ItemsLayout(_layout);
        layouts.put(l.getId(), l);
    }

    static protected void addItem(WindowLayout layout, BaseItem item, LayoutType type) {
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

    static protected void removeItem(WindowLayout layout, BaseItem item, LayoutType type) {
        // lock (CommonService.GlobalLocker)
        {
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

    static public void PrintListOfItems(WindowLayout layout) {
        String[] list = getListOfItemsNames(layout);
        for (String item : list) {
            System.out.println(item);
        }
    }
}