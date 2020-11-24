package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.KeyCode;

/**
 * Abstract class containing all common methods and properties for creating
 * UI element for file browsing.
 */
public abstract class OpenDialog extends DialogItem {

    private TitleBar _titleBar;

    /**
     * Setting a title text of the dialog window.
     * 
     * @param title Title text.
     */
    public void setTitle(String title) {
        _titleBar.setText(title);
    }

    /**
     * Getting a title text of the dialog window.
     * 
     * @return Title text.
     */
    public String getTitle() {
        return _titleBar.getText();
    }

    /**
     * Default common constructor.
     */
    public OpenDialog() {
        setItemName("OpenDialog_" + count);
        count++;
        _titleBar = new TitleBar();
    }

    /**
     * Initializing all elements in the OpenDialog.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        // important!
        super.initElements();

        _titleBar.getMaximizeButton().setVisible(false);

        // adding toolbar
        window.addItems(_titleBar);

        _titleBar.getCloseButton().eventMouseClick.clear();
        _titleBar.getCloseButton().eventMouseClick.add((sender, args) -> {
            close();
        });
        _titleBar.eventMouseDoubleClick.clear();
        _titleBar.eventMouseDoubleClick.add((sender, args) -> {
            updateWindow();
        });
        eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.R && args.mods.contains(CommonService.getOsControlMod()))
                updateWindow();
            else if (args.key == KeyCode.Escape)
                close();
        });
    }

    private void updateWindow() {
        window.update(GeometryEventType.ResizeHeight, 0);
        window.update(GeometryEventType.ResizeWidth, 0);
    }

    /**
     * Shows OpenDialog and attaches it to the specified window (see
     * com.spvessel.spacevil.CoreWindow, com.spvessel.spacevil.ActiveWindow,
     * com.spvessel.spacevil.DialogWindow).
     * 
     * @param handler Window for attaching OpenDialog.
     */
    @Override
    public void show(CoreWindow handler) {
        super.show(handler);
    }

    /**
     * Closes OpenDialog.
     */
    @Override
    public void close() {
        if (onCloseDialog != null)
            onCloseDialog.execute();

        super.close();
    }
}