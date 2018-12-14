package com.spvessel.spacevil;

abstract public class DropDown extends DialogWindow {

    /**
     * Constructs a DropDown
     */
    public DropDown() {
    }

    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout("DropDown_" + getCount(), "DropDown_" + getCount());
        Handler.setWindowTitle(getDialogTitle());
        Handler.isBorderHidden = true;
        Handler.isAlwaysOnTop = true;
        Handler.isCentered = false;
        Handler.isResizable = false;
    }
}