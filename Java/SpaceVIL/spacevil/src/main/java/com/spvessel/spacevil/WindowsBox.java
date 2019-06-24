package com.spvessel.spacevil;

import com.spvessel.spacevil.Flags.LayoutType;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.*;

public final class WindowsBox {
    /**
     * A storage-class that provides an access to existing window layouts by name
     * and UUID
     */
    static Set<CoreWindow> windows = new HashSet<>();
    static Map<UUID, CoreWindow> windowsUUID = new HashMap<>();
    static Map<CoreWindow, CoreWindow> pairs = new HashMap<>();
    static CoreWindow lastFocusedWindow;

    static void initWindow(CoreWindow _layout) {
        if (windowsUUID.containsKey(_layout.getWindowGuid()))
            return;

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
        windows.remove(_layout);
        windowsUUID.remove(_layout.getWindowGuid());
        _layout.release();
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
    }

    /**
     * @return WidowLayout object by its UUID
     */
    static public CoreWindow getWindowInstance(UUID guid) {
        return windowsUUID.getOrDefault(guid, null);
    }

    static void createWindowsPair(CoreWindow wnd) {
        addToWindowDispatcher(wnd);
    }

    static void destroyWindowsPair(CoreWindow wnd) {
        removeFromWindowDispatcher(wnd);
    }

    static CoreWindow getWindowPair(CoreWindow wnd) {
        if (pairs.containsKey(wnd))
            return pairs.get(wnd);
        else
            return null;
    }

    static void addToWindowDispatcher(CoreWindow wnd) {
        if (!pairs.containsKey(wnd))
            pairs.put(wnd, lastFocusedWindow);
    }

    static void setCurrentFocusedWindow(CoreWindow wnd) {
        lastFocusedWindow = wnd;
    }

    public static CoreWindow getCurrentFocusedWindow() {
        return lastFocusedWindow;
    }

    static void setFocusedWindow(CoreWindow window) {
        if (window != null)
            window.setFocus(true);
    }

    static void removeFromWindowDispatcher(CoreWindow wnd) {
        if (pairs.containsKey(wnd))
            pairs.remove(wnd);
    }

    /**
     * @return list of all windows names
     */
    static public List<String> getWindowsList() {
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