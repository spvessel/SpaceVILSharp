package com.spvessel.Windows;

import java.util.*;

public abstract class CoreWindow {
    private static int count = 0;
    private UUID windowUUID;

    public CoreWindow() {
        windowUUID = UUID.randomUUID();
        count++;
    }

    WindowLayout wnd_handler;

    public WindowLayout getHandler() {
        return wnd_handler;
    }

    public void getHandler(WindowLayout value) {
        wnd_handler = value;
    }

    public void show() {
        wnd_handler.show();
        // WindowLayoutBox.SetFocusedWindow(this);
    }

    public void close() {
        wnd_handler.close();
    }

    abstract public void initWindow();
    // abstract public void InitElements();

    public int getCount() {
        return count;
    }

    public UUID getWindowGuid() {
        return windowUUID;
    }
}