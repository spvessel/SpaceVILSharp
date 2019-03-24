package com.spvessel.spacevil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;

public class MessageItem extends DialogItem {
    static int count = 0;

    private boolean _result = false;

    /**
     * Get MessageItem boolean result
     */
    public boolean getResult() {
        return _result;
    }

    Map<ButtonCore, Integer> _userMap = new HashMap<>();
    ButtonCore _userButtonResult = null;

    public int getUserButtonResult() {
        if (_userMap.containsKey(_userButtonResult))
            return _userMap.get(_userButtonResult);
        return -1;
    }

    private TitleBar _titleBar;
    private Frame _msg_layout;
    private Label _msg;
    private ButtonCore _ok;

    public ButtonCore getOkButton() {
        return _ok;
    }

    private ButtonCore _cancel;

    public ButtonCore getCancelButton() {
        return _cancel;
    }

    private HorizontalStack _toolbar;
    private HorizontalStack _userbar;

    /**
     * Constructs a MessageItem
     */
    public MessageItem() {
        setItemName("MessageItem_" + count);
        count++;

        _titleBar = new TitleBar();
        _msg_layout = new Frame();
        _msg = new Label();
        _ok = getButton("OK");
        _cancel = getButton("Cancel");

        _toolbar = new HorizontalStack();
        _userbar = new HorizontalStack();

        setStyle(DefaultsService.getDefaultStyle(MessageItem.class));
    }

    /**
     * Constructs a MessageItem with message and title
     */
    public MessageItem(String message, String title) {
        this();

        _titleBar.setText(title);
        _msg.setText(message);
    }

    /**
     * MessageItem text
     */
    public void setMessageText(String text) {
        _msg.setText(text);
    }

    public String getMessageText() {
        return _msg.getText();
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

        int w_global = 0;

        // toolbar size
        int w = _ok.getWidth() + _ok.getMargin().left + _ok.getMargin().right;
        if (_cancel.isVisible())
            w = w * 2 + 10;
        _toolbar.setSize(w, _ok.getHeight() + _ok.getMargin().top + _ok.getMargin().bottom);
        w_global += w;

        boolean isEmpty = true;
        w = _userbar.getPadding().left + _userbar.getPadding().right;
        if (_queue.size() > 0) {
            isEmpty = false;
            for (ButtonCore btn : _queue) {
                w += btn.getWidth() + _userbar.getSpacing().horizontal;
            }
            w -= _userbar.getSpacing().horizontal;
        }
        _userbar.setSize(w, _toolbar.getHeight());
        w_global += (w + _toolbar.getMargin().left + _toolbar.getMargin().right + 50);
        w_global += (_userbar.getMargin().left + _userbar.getMargin().right);

        window.setMinWidth(w_global);
        if (window.getWidth() < w_global)
            window.setWidth(w_global);
        window.addItems(_titleBar, _msg_layout);
        window.update(GeometryEventType.RESIZE_WIDTH, 0);

        if (!isEmpty) {
            _toolbar.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
            int right = _toolbar.getWidth() + _toolbar.getMargin().left + _toolbar.getMargin().right + 10;
            _userbar.setMargin(0, 0, right / 2, 0);
            _msg_layout.addItems(_msg, _userbar, _toolbar);
        } else {
            _msg_layout.addItems(_msg, _toolbar);
        }

        // queue
        if (!isEmpty) {
            for (ButtonCore btn : _queue) {
                _userbar.addItem(btn);
            }
        }
        _toolbar.addItems(_ok, _cancel);
        _userMap.put(_ok, 1);
        _userMap.put(_titleBar.getCloseButton(), 0);
        _userMap.put(_cancel, -1);
        // buttons
        _ok.setItemName("OK");
        _ok.eventMouseClick.add((sender, args) -> {
            _userButtonResult = _ok;
            _result = true;
            close();
        });
        _cancel.setItemName("Cancel");
        _cancel.eventMouseClick.add((sender, args) -> {
            _userButtonResult = _cancel;
            close();
        });

        _titleBar.getCloseButton().eventMouseClick.clear();
        _titleBar.getCloseButton().eventMouseClick.add((sender, args) -> {
            _userButtonResult = _titleBar.getCloseButton();
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

    private List<ButtonCore> _queue = new LinkedList<>();

    public void addUserButton(ButtonCore button, int id) {
        if (id == -1 || id == 0 || id == 1)
            return;

        button.setStyle(_btnStyle);
        _userMap.put(button, id);
        _queue.add(button);
        button.eventMouseClick.add((sender, args) -> {
            _userButtonResult = button;
            close();
        });
    }

    private ButtonCore getButton(String name) {
        ButtonCore btn = new ButtonCore(name);
        return btn;
    }

    private Style _btnStyle = null;

    public Style getDialogButtonStyle() {
        return _btnStyle;
    }

    public void setDialogButtonStyle(Style style) {
        _btnStyle = style;
    }

    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("message");
        if (inner_style != null) {
            _msg.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("layout");
        if (inner_style != null) {
            _msg_layout.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("toolbar");
        if (inner_style != null) {
            _toolbar.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("userbar");
        if (inner_style != null) {
            _userbar.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("button");
        if (inner_style != null) {
            _btnStyle = inner_style.clone();
            _ok.setStyle(inner_style);
            _cancel.setStyle(inner_style);
        }
    }
}
