package com.spvessel.spacevil;

import com.spvessel.spacevil.Exceptions.SpaceVILException;

import java.util.*;

public abstract class CoreWindow {
    private static int count = 0;
    private UUID windowUUID;

    /**
     * Constructs a CoreWindow
     */
    public CoreWindow() {
        windowUUID = UUID.randomUUID();
        count++;
    }

    private WindowLayout wnd_handler;

    /**
     * Parent item for the CoreWindow
     */
    public WindowLayout getHandler() {
        return wnd_handler;
    }

    /**
     * Parent item for the CoreWindow
     */
    public void setHandler(WindowLayout value) {
        if (value == null) {
            System.out.println("Window handler can't be null");
            return;
        }
        wnd_handler = value;
        value.setCoreWindow(this);
    }

    /**
     * Show the CoreWindow
     */
    public void show() {
        wnd_handler.show();
        // WindowLayoutBox.SetFocusedWindow(this);
    }

    /**
     * Close the CoreWindow
     */
    public void close() {
        wnd_handler.close();
    }

    /**
     * Initialize the window
     */
    abstract public void initWindow();
    // abstract public void InitElements();

    /**
     * @return count of all CoreWindows
     */
    public int getCount() {
        return count;
    }

    /**
     * @return CoreWindow unique ID
     */
    public UUID getWindowGuid() {
        return windowUUID;
    }
}