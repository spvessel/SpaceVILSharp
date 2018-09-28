package com.spvessel.Windows;

public abstract class DialogWindow extends CoreWindow {

    public DialogWindow() {
        initWindow();
    }

    String _title = "";

    public String getDialogTitle() {
        return _title;
    }

    public void setDialogTitle(String value) {
        _title = value;
    }
}