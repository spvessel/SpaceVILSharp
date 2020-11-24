package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.KeyCode;

/**
 * InputBox - a dialog window for entering text and perform assigned actions.
 * <p>
 * Contains ACTION button, CANCEL button, titlebar.
 */
public class InputBox extends DialogWindow {

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
     * @return InputBox's ACTION button as com.spvessel.spacevil.ButtonCore.
     */
    public ButtonCore getActionButton() {
        return _action;
    }

    /**
     * Getting CANCEL button for appearance customizing or assigning new actions.
     * 
     * @return InputBox's CANCEL button as com.spvessel.spacevil.ButtonCore.
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
     * Constructs a InputBox with specified title and name of ACTION button.
     * 
     * @param title      Title of InputBox as java.lang.String.
     * @param actionName Name of ACTION button as java.lang.String.
     */
    public InputBox(String title, String actionName) {
        this(title, actionName, "");
    }

    /**
     * Constructs a InputBox with specified default text, title and name of ACTION
     * button.
     * 
     * @param title       Title of InputBox as java.lang.String.
     * @param actionName  Name of ACTION button as java.lang.String.
     * @param defaultText Default text of text field as java.lang.String.
     */
    public InputBox(String title, String actionName, String defaultText) {
        setWindowName("InputBox");
        _layout = new Frame();
        _stack = new HorizontalStack();
        _title = new TitleBar(title);
        _action = new ButtonCore(actionName);
        _cancel = new ButtonCore("Cancel");
        _input = new TextEdit();
        _input.setText(defaultText);

        eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.Escape) {
                _inputResult = "";
                close();
            }
        });

        setStyle(DefaultsService.getDefaultStyle(InputDialog.class));
    }

    /**
     * Initializing all elements in the InputBox.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initWindow() {
        isBorderHidden = true;
        isAlwaysOnTop = true;

        // title
        _title.getMinimizeButton().setVisible(false);
        _title.getMaximizeButton().setVisible(false);

        // adding
        addItems(_title, _layout);

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
            if (args.key == KeyCode.Enter || args.key == KeyCode.NumpadEnter) {
                _inputResult = _input.getText();
                close();
            } else if (args.key == KeyCode.Escape) {
                _inputResult = "";
                close();
            }
        });
    }

    /**
     * Show InputBox window.
     */
    @Override
    public void show() {
        super.show();

        _input.setFocus();
        _input.selectAll();
    }

    /**
     * Closes InputBox window.
     */
    @Override
    public void close() {
        super.close();

        if (onCloseDialog != null)
            onCloseDialog.execute();
    }

    /**
     * Select all text in the text field.
     */
    public void selectAll() {
        _input.selectAll();
    }

    /**
     * Setting style of the InputBox.
     * <p>
     * Inner styles: "window", "textedit", "layout", "toolbar", "button".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    public void setStyle(Style style) {
        if (style == null)
            return;

        Style innerStyle = style.getInnerStyle("window");
        if (innerStyle != null) {
            getLayout().getContainer().setStyle(innerStyle);
            setMinSize(innerStyle.minWidth, innerStyle.minHeight);
            setSize(innerStyle.width, innerStyle.height);
        }

        innerStyle = style.getInnerStyle("button");
        if (innerStyle != null) {
            _action.setStyle(innerStyle);
            _cancel.setStyle(innerStyle);
        }
        innerStyle = style.getInnerStyle("textedit");
        if (innerStyle != null) {
            _input.setStyle(innerStyle);
        }
        innerStyle = style.getInnerStyle("layout");
        if (innerStyle != null) {
            _layout.setStyle(innerStyle);
        }
        innerStyle = style.getInnerStyle("toolbar");
        if (innerStyle != null) {
            _stack.setStyle(innerStyle);
        }
    }
}