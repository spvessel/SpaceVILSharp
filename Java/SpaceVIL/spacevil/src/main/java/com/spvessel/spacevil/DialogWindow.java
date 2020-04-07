package com.spvessel.spacevil;

/**
 * DialogWindow is an abstract class for modal window instances.
 * <p> DialogWindow extends CoreWindow class. 
 * CoreWindow is an abstract class containing an implementation of common functionality for a window.
 */
public abstract class DialogWindow extends CoreWindow {

    /**
     * Constructs a DialogWindow
     */
    public DialogWindow() {
        super();
        isDialog = true;
        isCentered = true;
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