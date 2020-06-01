package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.EventCommonMethod;

/**
 * DialogWindow is an abstract class for modal window instances.
 * <p>
 * DialogWindow extends CoreWindow class. CoreWindow is an abstract class
 * containing an implementation of common functionality for a window.
 */
public abstract class DialogWindow extends CoreWindow {

    /**
     * An event to describe the actions that must be performed after the window is
     * closed.
     * <p>
     * Event type: com.spvessel.spacevil.Core.EventCommonMethod.
     * <p>
     * Function arguments: none.
     */
    public EventCommonMethod onCloseDialog = new EventCommonMethod();

    /**
     * Constructs a DialogWindow
     */
    public DialogWindow() {
        super();
        isDialog = true;
        isCentered = true;
        isAlwaysOnTop = true;
    }

    /**
     * Show the DialogWindow.
     */
    @Override
    public void show() {
        initWindow();
        super.show();
    }
}