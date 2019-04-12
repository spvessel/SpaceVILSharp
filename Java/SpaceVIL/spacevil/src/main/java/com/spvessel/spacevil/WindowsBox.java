package com.spvessel.spacevil;

import com.spvessel.spacevil.Flags.LayoutType;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.*;
import java.util.stream.*;

public final class WindowsBox {
    /**
     * A storage-class that provides an access to existing window layouts by name
     * and UUID
     */
    // static Map<String, CoreWindow> windowsName = new HashMap<>();
    static Set<CoreWindow> windows = new HashSet<>();
    static Map<UUID, CoreWindow> windowsUUID = new HashMap<>();
    static List<WindowsPair> currentCallingPair = new LinkedList<>();
    static CoreWindow lastFocusedWindow;
    // static private Object locker = new Object();

    static void initWindow(CoreWindow _layout) {
        if (windowsUUID.containsKey(_layout.getWindowGuid())) // windowsName.containsKey(_layout.getWindowName()) ||
            return;

        // windowsName.put(_layout.getWindowName(), _layout);
        windows.add(_layout);
        windowsUUID.put(_layout.getWindowGuid(), _layout);

        ItemsLayoutBox.initLayout(_layout.getWindowGuid());

        // add filling frame
        // ALL ATTRIBUTES STRICTLY NEEDED!!!
        WContainer container = new WContainer();
        container.setHandler(_layout);
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

    static void removeWindow(CoreWindow _layout) {
        // windowsName.remove(_layout.getWindowName());
        windows.remove(_layout);
        windowsUUID.remove(_layout.getWindowGuid());
        if (_is_main_running == _layout)
            _is_main_running = null;
        _layout.release();
    }

    private static CoreWindow _is_main_running = null;

    static boolean isAnyWindowRunning() {
        if (_is_main_running != null)
            return true;
        return false;
    }

    static void setWindowRunning(CoreWindow window) {
        _is_main_running = window;
    }

    /**
     * Try to show CoreWindow object using its UUID
     * 
     * @return if showing successful
     */
    static public boolean tryShow(UUID guid) {
        CoreWindow wnd = WindowsBox.getWindowInstance(guid);
        if (wnd != null) {
            wnd.show();
            return true;
        }
        return false;
    }

    /**
     * Try to show CoreWindow object using its name
     * 
     * @return if showing successful
     */
    static public boolean tryShow(String name) {
        CoreWindow wnd = WindowsBox.getWindowInstance(name);
        if (wnd != null) {
            wnd.show();
            return true;
        }
        return false;
    }

    /**
     * @return WidowLayout object by its name
     */
    static private CoreWindow getWindowInstance(String name) {
        for (CoreWindow wnd : windows) {
            if (wnd.getWindowName().equals(name))
                return wnd;
        }
        return null;
        // return windowsName.getOrDefault(name, null);

        // if (windowsName.containsKey(name))
        // return windowsName.get(name);
        // else
        // return null;
    }

    /**
     * @return WidowLayout object by its UUID
     */
    static public CoreWindow getWindowInstance(UUID guid) {
        return windowsUUID.getOrDefault(guid, null);

        // if (windowsUUID.containsKey(guid))
        // return windowsUUID.get(guid);
        // else
        // return null;
    }

    static void addToWindowDispatcher(CoreWindow sender_wnd) {
        WindowsPair pair = new WindowsPair();
        pair.WINDOW = sender_wnd;
        if (lastFocusedWindow == null) {
            pair.GUID = sender_wnd.getWindowGuid();// root
            lastFocusedWindow = sender_wnd;
        } else
            pair.GUID = lastFocusedWindow.getWindowGuid();
        currentCallingPair.add(pair);
    }

    static void setCurrentFocusedWindow(CoreWindow wnd) {
        lastFocusedWindow = wnd;
    }

    static void setFocusedWindow(CoreWindow window) {
        window.setFocus(true);
    }

    static void removeFromWindowDispatcher(CoreWindow sender_wnd) {
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
    static public List<String> getWindowsList() {
        // String[] result = new String[windowsName.size()];
        // windowsName.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList()).toArray(result);
        List<String> result = new LinkedList<>();

        for (CoreWindow wl : windows) {
            result.add(wl.getWindowName());
        }

        return result;
    }

    /**
     * Print all windows names
     */
    static public void printStoredWindows() {
        for (String item : getWindowsList()) {
            System.out.println(item);
        }
    }
}