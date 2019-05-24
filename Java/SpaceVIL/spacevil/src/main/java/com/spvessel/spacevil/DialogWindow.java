package com.spvessel.spacevil;

public abstract class DialogWindow extends CoreWindow {

    /**
     * Constructs a DialogWindow
     */
    public DialogWindow() {
        super();
        isDialog = true;
        isCentered = true;
    }

    @Override
    public void show() {
        initWindow();
        super.show();
    }
}