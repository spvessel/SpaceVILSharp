package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceOpenGLLayer;
import com.spvessel.spacevil.Flags.LayoutType;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.util.*;

/**
 * A storage-class that provides an access to existing window layouts by name
 * and UUID.
 */
public final class WindowsBox {
    /**
     * Try to show CoreWindow object using its UUID.
     * @param guid UUID of the window.
     * @return True: if window with such UUID is exist. False: if window with such UUID is not exist.
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
     * Try to show a window by its name.
     * @param name Name of the window.
     * @return True: if window with such name is exist. False: if window with such name is not exist.
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
     * Getting a window instance by its name.
     * @param name Name of the window.
     * @return CoreWindow link: if window with such name is exist. NULL: if window with such name is not exist.
     */
    static public CoreWindow getWindowInstance(String name) {
        for (CoreWindow wnd : windows) {
            if (wnd.getWindowName().equals(name)) {
                return wnd;
            }
        }
        return null;
    }

    /**
     * Getting a window instance by its UUID.
     * @param guid GUID of the window.
     * @return CoreWindow link: if window with such GUID is exist. NULL: if window with such GUID is not exist.
     */
    static public CoreWindow getWindowInstance(UUID guid) {
        return windowsUUID.getOrDefault(guid, null);
    }

    /**
     * Getting the current focused window.
     * @return The current focused window as com.spvessel.spacevil.CoreWindow.
     */
    public static CoreWindow getCurrentFocusedWindow() {
        return lastFocusedWindow;
    }

    /**
     * Getting the list of existing windows in the application.
     * @return The list of existing windows.
     */
    static public List<String> getWindowsList() {
        List<String> result = new LinkedList<>();
        for (CoreWindow wl : windows) {
            result.add(wl.getWindowName());
        }
        return result;
    }

    /**
     * Printing all existing windows in the application.
     */
    static public void printStoredWindows() {
        for (String item : getWindowsList()) {
            System.out.println(item);
        }
    }
    
    static Set<CoreWindow> windows = new HashSet<>();
    static Map<UUID, CoreWindow> windowsUUID = new HashMap<>();
    static Map<CoreWindow, CoreWindow> pairs = new HashMap<>();
    static CoreWindow lastFocusedWindow;

    static void initWindow(CoreWindow _layout) {
        if (windowsUUID.containsKey(_layout.getWindowGuid())) {
            return;
        }

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


    static void createWindowsPair(CoreWindow wnd) {
        addToWindowDispatcher(wnd);
    }

    static void destroyWindowsPair(CoreWindow wnd) {
        removeFromWindowDispatcher(wnd);
    }

    static CoreWindow getWindowPair(CoreWindow wnd) {
        if (pairs.containsKey(wnd)) {
            return pairs.get(wnd);
        }
        return null;
    }

    static void addToWindowDispatcher(CoreWindow wnd) {
        if (!pairs.containsKey(wnd)) {
            pairs.put(wnd, lastFocusedWindow);
        }
    }

    static void setCurrentFocusedWindow(CoreWindow wnd) {
        lastFocusedWindow = wnd;
    }

    static void setFocusedWindow(CoreWindow window) {
        if (window != null) {
            window.setFocus();
        }
    }

    static void removeFromWindowDispatcher(CoreWindow wnd) {
        if (pairs.containsKey(wnd)) {
            pairs.remove(wnd);
        }
    }

    public static void restoreCommonGLSettings(CoreWindow window) {
        window.getLayout().restoreCommonGLSettings();
    }

    public static void restoreViewport(CoreWindow window) {
        window.getLayout().restoreView();
    }

    public static void setGLLayerViewport(CoreWindow window, InterfaceOpenGLLayer layer) {
        window.getLayout().setGLLayerViewport(layer);
    }
}