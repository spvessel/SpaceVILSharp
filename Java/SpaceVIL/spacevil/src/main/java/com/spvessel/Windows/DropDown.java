package com.spvessel.Windows;

abstract public class DropDown extends DialogWindow {

    public DropDown() {
    }

    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout(this, "DropDown_" + getCount(), "DropDown_" + getCount());
        Handler.setWindowTitle(getDialogTitle());
        Handler.isBorderHidden = true;
        Handler.isAlwaysOnTop = true;
        Handler.isCentered = false;
        Handler.isResizable = false;
    }
}