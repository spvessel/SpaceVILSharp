package com.spvessel;

public abstract class DialogWindow extends CoreWindow {

    /**
     * Constructs a DialogWindow
     */
    public DialogWindow() {
        initWindow();
    }

    private String _title = "";

    /**
     * DialogWindow title text
     */
    public String getDialogTitle() {
        return _title;
    }
    public void setDialogTitle(String value) {
        _title = value;
    }
}