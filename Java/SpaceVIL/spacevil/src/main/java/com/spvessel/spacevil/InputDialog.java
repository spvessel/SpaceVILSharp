package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.DialogItem;

/**
 * InputDialog - an imitation of modal window for entering text and perform
 * assigned actions.
 * <p>
 * Contains ACTION button, CANCEL button, titlebar.
 * <p>
 * Supports all events except drag and drop.
 */
public class InputDialog extends DialogItem {
    private String _inputResult = null;

    /**
     * Getting text input result. Default: empty
     * 
     * @return Text result as java.lang.String.
     */
    public String getResult() {
        return _inputResult;
    }

    private ButtonCore _action;
    private ButtonCore _cancel;

    /**
     * Getting ACTION button for appearance customizing or assigning new actions.
     * 
     * @return InputDialog's ACTION button as com.spvessel.spacevil.ButtonCore.
     */
    public ButtonCore getActionButton() {
        return _action;
    }

    /**
     * Getting CANCEL button for appearance customizing or assigning new actions.
     * 
     * @return InputDialog's CANCEL button as com.spvessel.spacevil.ButtonCore.
     */
    public ButtonCore GetCancelButton() {
        return _cancel;
    }

    /**
     * Setting CANCEL button visible of invisible.
     * 
     * @param value True: if you want CANCEL button to be visible. False: if you
     *              want CANCEL button to be invisible.
     */
    public void setCancelVisible(boolean value) {
        _cancel.setVisible(value);
    }

    private TextEdit _input;
    private TitleBar _title;
    private Frame _layout;
    private HorizontalStack _stack;

    /**
     * Constructs a InputDialog with specified title and name of ACTION button.
     * 
     * @param title      Title of InputDialog as java.lang.String.
     * @param actionName Name of ACTION button as java.lang.String.
     */
    public InputDialog(String title, String actionName) {
        this(title, actionName, "");
    }

    /**
     * Constructs a InputDialog with specified default text, title and name of
     * ACTION button.
     * 
     * @param title       Title of InputDialog as java.lang.String.
     * @param actionName  Name of ACTION button as java.lang.String.
     * @param defaultText Default text of text field as java.lang.String.
     */
    public InputDialog(String title, String actionName, String defaultText) {
        setItemName("InputDialog_");
        _layout = new Frame();
        _stack = new HorizontalStack();
        _title = new TitleBar(title);
        _action = new ButtonCore(actionName);
        _cancel = new ButtonCore("Cancel");
        _input = new TextEdit();
        _input.setText(defaultText);
        window.isLocked = true;

        eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.ESCAPE) {
                _inputResult = "";
                close();
            }
        });

        setStyle(DefaultsService.getDefaultStyle(InputDialog.class));
    }

    /**
     * Initializing all elements in the InputDialog.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        super.initElements();

        // title
        _title.getMinimizeButton().setVisible(false);
        _title.getMaximizeButton().setVisible(false);

        // adding
        window.addItems(_title, _layout);

        // stack size
        int w = (_action.getWidth() + _action.getMargin().left + _action.getMargin().right);
        if (_cancel.isVisible())
            w = w * 2 + 10;
        _stack.setSize(w, _action.getHeight() + _action.getMargin().top + _action.getMargin().bottom);

        _layout.addItems(_input, _stack);
        _stack.addItems(_action, _cancel);

        _title.getCloseButton().eventMouseClick.clear();
        _title.getCloseButton().eventMouseClick.add((sender, args) -> {
            _inputResult = "";
            close();
        });

        _action.eventMouseClick.add((sender, args) -> {
            _inputResult = _input.getText();
            close();
        });
        _cancel.eventMouseClick.add((sender, args) -> {
            _inputResult = "";
            close();
        });
        _input.eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.ENTER || args.key == KeyCode.NUMPADENTER) {
                _inputResult = _input.getText();
                close();
            } else if (args.key == KeyCode.ESCAPE) {
                _inputResult = "";
                close();
            }
        });
    }

    /**
     * Shows InputDialog and attaches it to the specified window (see
     * com.spvessel.spacevil.CoreWindow, com.spvessel.spacevil.ActiveWindow,
     * com.spvessel.spacevil.DialogWindow).
     * 
     * @param handler Window for attaching InputDialog.
     */
    @Override
    public void show(CoreWindow handler) {
        super.show(handler);
        _input.setFocus();
        _input.selectAll();
    }

    /**
     * Closes InputDialog.
     */
    @Override
    public void close() {
        if (onCloseDialog != null)
            onCloseDialog.execute();

        super.close();
    }

    /**
     * Select all text in the text field.
     */
    public void selectAll() {
        _input.selectAll();
    }

    /**
     * Setting style of the InputDialog.
     * <p>
     * Inner styles: "textedit", "layout", "toolbar", "button".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("button");
        if (inner_style != null) {
            _action.setStyle(inner_style);
            _cancel.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("textedit");
        if (inner_style != null) {
            _input.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("layout");
        if (inner_style != null) {
            _layout.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("toolbar");
        if (inner_style != null) {
            _stack.setStyle(inner_style);
        }
    }
}
