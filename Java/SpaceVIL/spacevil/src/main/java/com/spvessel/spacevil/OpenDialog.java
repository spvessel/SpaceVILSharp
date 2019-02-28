package com.spvessel.spacevil;

import com.spvessel.spacevil.Decorations.Style;

public abstract class OpenDialog extends DialogItem {

    private TitleBar titleBar;

    public void setTitle(String title) {
        titleBar.setText(title);
    }

    public String getTitle() {
        return titleBar.getText();
    }

    public OpenDialog() {
        setItemName("OpenDialog_" + count);
        count++;
        titleBar = new TitleBar();
    }

    @Override
    public void initElements() {
        // important!
        super.initElements();

        titleBar.getMaximizeButton().setVisible(false);

        // adding toolbar
        window.addItems(titleBar);

        titleBar.getCloseButton().eventMouseClick.clear();
        titleBar.getCloseButton().eventMouseClick.add((sender, args) -> {
            close();
        });
    }

    @Override
    public void show(WindowLayout handler) {
        super.show(handler);
    }

    @Override
    public void close() {
        if (onCloseDialog != null)
            onCloseDialog.execute();

        super.close();
    }

    // @Override
    // public void setStyle(Style style) {
    //     if (style == null)
    //         return;
    //     window.setStyle(style);
    // }
}