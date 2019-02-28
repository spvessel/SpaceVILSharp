package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Style;

public class MessageItem extends DialogItem {
    static int count = 0;

    private boolean _result = false;

    /**
     * Get MessageItem boolean result
     */
    public boolean getResult() {
        return _result;
    }

    private String _message = "";
    private TitleBar _titleBar;
    private VerticalStack _layout;
    private Label _msg;
    private ButtonCore _ok;

    /**
     * Constructs a MessageItem
     */
    public MessageItem() {
        setItemName("MessageItem_" + count);
        count++;

        _titleBar = new TitleBar();
        _layout = new VerticalStack();
        _msg = new Label();
        _ok = new ButtonCore("OK");

        setStyle(DefaultsService.getDefaultStyle(MessageItem.class));
    }

    /**
     * Constructs a MessageItem with message and title
     */
    public MessageItem(String message, String title) {
        this();
        _message = message;

        _titleBar.setText(title);
        _msg.setText(_message);
    }

    /**
     * MessageItem text
     */
    public void setMessageText(String text) {
        _message = text;
    }

    public String getMessageText() {
        return _message;
    }

    public void setTitle(String title) {
        _titleBar.setText(title);
    }

    public String getTitle() {
        return _titleBar.getText();
    }

    @Override
    public void initElements() {
        // important!
        super.initElements();

        // adding toolbar
        _titleBar.getMaximizeButton().setVisible(false);
        window.addItems(_titleBar, _layout);

        // ok
        _ok.setItemName("OK");
        _ok.eventMouseClick.add((sender, args) -> {
            _result = true;
            close();
        });
        _layout.addItems(_msg, _ok);

        _titleBar.getCloseButton().eventMouseClick.clear();
        _titleBar.getCloseButton().eventMouseClick.add((sender, args) -> {
            close();
        });
    }

    @Override
    public void show(WindowLayout handler) {
        _result = false;
        super.show(handler);
    }

    @Override
    public void close() {
        if (onCloseDialog != null)
            onCloseDialog.execute();

        super.close();
    }

    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("okbutton");
        if (inner_style != null) {
            _ok.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("message");
        if (inner_style != null) {
            _msg.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("layout");
        if (inner_style != null) {
            _layout.setStyle(inner_style);
        }
    }
}
