package com.spvessel.Windows;

import com.spvessel.Flags.LayoutType;
import com.spvessel.Flags.SizePolicy;
import com.spvessel.Items.WContainer;
import com.spvessel.Layouts.ItemsLayoutBox;

import java.util.*;
import java.util.stream.*;

public class WindowLayoutBox {
    static public Map<String, WindowLayout> windowsName = new HashMap<String, WindowLayout>();
    static public Map<UUID, WindowLayout> windowsUUID = new HashMap<UUID, WindowLayout>();
    static public List<WindowPair> currentCallingPair = new LinkedList<WindowPair>();
    static public WindowLayout lastFocusedWindow;

    static public void initWindow(WindowLayout _layout) {
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

    static public void removeWindow(WindowLayout _layout) {
        windowsName.remove(_layout.getWindowName());
        windowsUUID.remove(_layout.getId());
    }

    static public boolean tryShow(UUID guid) {
        WindowLayout wnd = WindowLayoutBox.getWindowInstance(guid);
        if (wnd != null) {
            wnd.show();
            return true;
        }
        return false;
    }

    static public boolean tryShow(String name) {
        WindowLayout wnd = WindowLayoutBox.getWindowInstance(name);
        if (wnd != null) {
            wnd.show();
            return true;
        }
        return false;
    }

    static public WindowLayout getWindowInstance(String name) {
        if (windowsName.containsKey(name))
            return windowsName.get(name);
        else
            return null;
    }

    static public WindowLayout getWindowInstance(UUID guid) {
        if (windowsUUID.containsKey(guid))
            return windowsUUID.get(guid);
        else
            return null;
    }

    static protected void addToWindowDispatcher(WindowLayout sender_wnd) {
        WindowPair pair = new WindowPair();
        pair.WINDOW = sender_wnd;
        if (lastFocusedWindow == null) {
            pair.GUID = sender_wnd.getId();// root
            lastFocusedWindow = sender_wnd;
        } else
            pair.GUID = lastFocusedWindow.getId();
        currentCallingPair.add(pair);
    }

    static public void setCurrentFocusedWindow(WindowLayout wnd) {
        lastFocusedWindow = wnd;
        // Console.WriteLine(LastFocusedWindow.GetWindowName());
    }

    static protected void setFocusedWindow(CoreWindow window) {
        window.getHandler().setFocus(true);
    }

    static protected void removeFromWindowDispatcher(WindowLayout sender_wnd) {
        List<WindowPair> pairs_to_delete = new LinkedList<WindowPair>();
        for (WindowPair windows_pair : currentCallingPair) {
            if (windows_pair.WINDOW.equals(sender_wnd)) {
                pairs_to_delete.add(windows_pair);
            }
        }

        for (WindowPair pairs : pairs_to_delete) {
            currentCallingPair.remove(pairs);
        }

        pairs_to_delete = null;
    }

    static public String[] getListOfWindows() {
        String[] result = new String[windowsName.size()];
        windowsName.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList()).toArray(result);
        return result;
    }

    static public void printStoredWindows() {
        for (String item : getListOfWindows()) {
            System.out.println(item);
        }
    }
}