package com.spvessel.spacevil;

/**
 * ActiveWindow is an abstract class for base window instances.
 * <p>ActiveWindow extends CoreWindow class. 
 * CoreWindow is an abstract class containing an implementation of common functionality for a window.
 */
public abstract class ActiveWindow extends CoreWindow {
    /**
     * Constructs an ActiveWindow
     */
    public ActiveWindow() {
        super();
        initWindow();
    }
}