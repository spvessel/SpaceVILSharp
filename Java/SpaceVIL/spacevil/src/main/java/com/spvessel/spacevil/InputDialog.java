package com.spvessel.spacevil;

import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.TextEdit;
import com.spvessel.spacevil.TitleBar;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.DialogItem;

public class InputDialog extends DialogItem {
    private String _inputResult = null;

    public String getResult() {
        return _inputResult;
    }

    private ButtonCore _add;
    private ButtonCore _cancel;

    public void setCancelVisible(boolean value) {
        _cancel.setVisible(value);
    }

    private TextEdit _input;
    private TitleBar _title;
    private Frame _layout;
    private HorizontalStack _stack;

    public InputDialog(String title, String actionName) {
        this(title, actionName, "");
    }

    public InputDialog(String title, String actionName, String defaultText) {
        setItemName("InputDialog_");
        _layout = new Frame();
        _stack = new HorizontalStack();
        _title = new TitleBar(title);
        _add = new ButtonCore(actionName);
        _cancel = new ButtonCore("Cancel");
        _input = new TextEdit();
        _input.setText(defaultText);
        window.isLocked = true;

        eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.ESCAPE)
                close();
        });

        setStyle(DefaultsService.getDefaultStyle(InputDialog.class));
    }

    @Override
    public void initElements() {
        super.initElements();

        // title
        _title.getMinimizeButton().setVisible(false);
        _title.getMaximizeButton().setVisible(false);

        // adding
        window.addItems(_title, _layout);

        //stack size
        int w = (_add.getWidth() + _add.getMargin().left + _add.getMargin().right);
        if (_cancel.isVisible())
            w = w * 2 + 10;
        _stack.setSize(w, _add.getHeight() + _add.getMargin().top + _add.getMargin().bottom);

        _layout.addItems(_input, _stack);
        _stack.addItems(_add, _cancel);

        _title.getCloseButton().eventMouseClick.clear();
        _title.getCloseButton().eventMouseClick.add((sender, args) -> {
            close();
        });

        _add.eventMouseClick.add((sender, args) -> {
            _inputResult = _input.getText();
            close();
        });
        _cancel.eventMouseClick.add((sender, args) -> {
            close();
        });
        _input.eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.ENTER || args.key == KeyCode.NUMPADENTER) {
                _inputResult = _input.getText();
                close();
            } else if (args.key == KeyCode.ESCAPE) {
                close();
            }
        });
    }

    @Override
    public void show(CoreWindow handler) {
        super.show(handler);
        _input.setFocus();
    }

    @Override
    public void close() {
        if (onCloseDialog != null)
            onCloseDialog.execute();

        super.close();
    }

    public void selectAll() {
        _input.selectAll();
    }

    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("button");
        if (inner_style != null) {
            _add.setStyle(inner_style);
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
