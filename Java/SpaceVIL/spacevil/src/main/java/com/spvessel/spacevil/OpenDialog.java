package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.KeyCode;

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
        titleBar.eventMouseDoubleClick.add((sender, args) -> {
            updateWindow();
        });
        eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.R && args.mods.contains(CommonService.getOsControlMod()))
                updateWindow();
            else if (args.key == KeyCode.ESCAPE)
                close();
        });
    }

    private void updateWindow() {
        window.update(GeometryEventType.RESIZE_HEIGHT, 0);
        window.update(GeometryEventType.RESIZE_WIDTH, 0);
    }

    @Override
    public void show(CoreWindow handler) {
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
    // if (style == null)
    // return;
    // window.setStyle(style);
    // }
}