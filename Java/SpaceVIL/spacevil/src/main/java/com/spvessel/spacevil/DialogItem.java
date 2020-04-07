package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.Common.DefaultsService;

/**
 * DialogItem is an abstract class for imitation of modal window.
 * <p>
 * DialogItem extends Prototype class.
 */
abstract public class DialogItem extends Prototype {
    static int count = 0;
    /**
     * Window area of DialogItem.
     */
    public ResizableItem window = new ResizableItem();

    /**
     * Constructs a DialogItem.
     */
    public DialogItem() {
        setItemName("DialogItem_" + count);
        count++;
        setPassEvents(false);
        setStyle(DefaultsService.getDefaultStyle(DialogItem.class));
    }

    /**
     * Initializing all elements in the DialogItem.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        addItem(window);
    }

    private CoreWindow _handler = null;

    /**
     * Shows DialogItem and attaches it to the specified window (see
     * com.spvessel.spacevil.CoreWindow, com.spvessel.spacevil.ActiveWindow,
     * com.spvessel.spacevil.DialogWindow).
     * 
     * @param handler Window for attaching InputDialog.
     */
    public void show(CoreWindow handler) {
        _handler = handler;
        _handler.addItem(this);
        this.setFocus();
    }

    /**
     * Close the DialogItem.
     */
    public void close() {
        _handler.removeItem(this);
    }

    /**
     * An event to describe the actions that must be performed after the dialog is
     * closed.
     * <p>
     * Event type: com.spvessel.spacevil.Core.EventCommonMethod.
     * <p>
     * Function arguments: none.
     */
    public EventCommonMethod onCloseDialog = new EventCommonMethod();

    /**
     * Disposing DialogItem resources if the it was removed.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void release() {
        onCloseDialog.clear();
    }

    /**
     * Setting style of the DialogItem.
     * <p>
     * Inner styles: "window".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        Style inner_style = style.getInnerStyle("window");
        if (inner_style != null) {
            window.setStyle(inner_style);
        }
    }
}
