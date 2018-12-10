package com.spacevil;

import com.spacevil.Flags.LayoutType;
import com.spacevil.Flags.SizePolicy;

import java.util.*;
import java.util.stream.*;

public final class WindowLayoutBox {
    static Map<String, WindowLayout> windowsName = new HashMap<>();
    static Map<UUID, WindowLayout> windowsUUID = new HashMap<>();
    static List<WindowsPair> currentCallingPair = new LinkedList<>();
    static WindowLayout lastFocusedWindow;
    //static private Object locker = new Object();

    static void initWindow(WindowLayout _layout) {
        windowsName.put(_layout.getWindowName(), _layout);
        windowsUUID.put(_layout.getId(), _layout);

        ItemsLayoutBox.initLayout(_layout.getId());

        // add filling frame
        // ALL ATTRIBUTES STRICTLY NEEDED!!!
        WContainer container = new WContainer();
        container.setHandler(_layout);
        // System.out.println(_layout.getWindowName());
        container.setItemName(_layout.getWindowName());
        container.setWidth(_layout.getWidth());
        container.setMinWidth(_layout.getMinWidth());
        container.setMaxWidth(_layout.getMaxWidth());
        container.setHeight(_layout.getHeight());
        container.setMinHeight(_layout.getMinHeight());
        container.setMaxHeight(_layout.getMaxHeight());
        container.setWidthPolicy(SizePolicy.EXPAND);
        container.setHeightPolicy(SizePolicy.EXPAND);

        _layout.setWindow(container);
        ItemsLayoutBox.addItem(_layout, container, LayoutType.STATIC);
    }

    static void removeWindow(WindowLayout _layout) {
        windowsName.remove(_layout.getWindowName());
        windowsUUID.remove(_layout.getId());
    }

    /**
     * Try to show WindowLayout object using its UUID
     * @return if showing successful
     */
    static public boolean tryShow(UUID guid) {
        WindowLayout wnd = WindowLayoutBox.getWindowInstance(guid);
        if (wnd != null) {
            wnd.show();
            return true;
        }
        return false;
    }

    /**
     * Try to show WindowLayout object using its name
     * @return if showing successful
     */
    static public boolean tryShow(String name) {
        WindowLayout wnd = WindowLayoutBox.getWindowInstance(name);
        if (wnd != null) {
            wnd.show();
            return true;
        }
        return false;
    }

    /**
     * @return WidowLayout object by its name
     */
    static public WindowLayout getWindowInstance(String name) {
        if (windowsName.containsKey(name))
            return windowsName.get(name);
        else
            return null;
    }

    /**
     * @return WidowLayout object by its UUID
     */
    static public WindowLayout getWindowInstance(UUID guid) {
        if (windowsUUID.containsKey(guid))
            return windowsUUID.get(guid);
        else
            return null;
    }

    static void addToWindowDispatcher(WindowLayout sender_wnd) {
        WindowsPair pair = new WindowsPair();
        pair.WINDOW = sender_wnd;
        if (lastFocusedWindow == null) {
            pair.GUID = sender_wnd.getId();// root
            lastFocusedWindow = sender_wnd;
        } else
            pair.GUID = lastFocusedWindow.getId();
        currentCallingPair.add(pair);
    }

    static void setCurrentFocusedWindow(WindowLayout wnd) {
        lastFocusedWindow = wnd;
    }

    static void setFocusedWindow(CoreWindow window) {
        window.getHandler().setFocus(true);
    }

    static void removeFromWindowDispatcher(WindowLayout sender_wnd) {
        List<WindowsPair> pairs_to_delete = new LinkedList<WindowsPair>();
        for (WindowsPair windows_pair : currentCallingPair) {
            if (windows_pair.WINDOW.equals(sender_wnd)) {
                pairs_to_delete.add(windows_pair);
            }
        }

        for (WindowsPair pairs : pairs_to_delete) {
            currentCallingPair.remove(pairs);
        }

        pairs_to_delete = null;
    }

    /**
     * @return list of all windows names
     */
    static public String[] getListOfWindows() {
        String[] result = new String[windowsName.size()];
        windowsName.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList()).toArray(result);
        return result;
    }

    /**
     * Print all windows names
     */
    static public void printStoredWindows() {
        for (String item : getListOfWindows()) {
            System.out.println(item);
        }
    }
}