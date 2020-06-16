package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * MessageBox - a modal window for displaying any messages with ability to reply
 * to them. It supports custom toolbar to make user's reply flexible.
 */
public class MessageBox extends DialogWindow {
    static int count = 0;

    private boolean _result = false;

    /**
     * Get MessageBox boolean result
     * <p>
     * Default: False
     * 
     * @return True: OK button was clicked. False: Close button or Cancel button was
     *         clicked.
     */
    public boolean getResult() {
        return _result;
    }

    private Map<Integer, ButtonCore> _userMap = new HashMap<>();
    private int _userButtonResult = -1;

    /**
     * Getting result from custom toolbar (if it was created).
     * 
     * @return Id of clicked button (see addUserButton(ButtonCore button, int id)).
     */
    public int getUserButtonResult() {
        return _userButtonResult;
    }

    private TitleBar _titleBar;
    private Frame _msgLayout;
    private Label _msgLabel;
    private ButtonCore _okButton;

    /**
     * Getting OK button for appearance customizing or assigning new actions.
     * 
     * @return MessageBox's OK button as MessageBox's OK button as
     *         com.spvessel.spacevil.ButtonCore.
     */
    public ButtonCore getOkButton() {
        return _okButton;
    }

    private ButtonCore _cancel;

    /**
     * Getting CANCEL button for appearance customizing or assigning new actions.
     * 
     * @return MessageBox's CANCEL button as com.spvessel.spacevil.ButtonCore.
     */
    public ButtonCore getCancelButton() {
        return _cancel;
    }

    private HorizontalStack _toolbar;
    private HorizontalStack _userbar;

    /**
     * Default MessageBox constructor.
     */
    public MessageBox() {
        setWindowName("MessageBox_" + count);
        count++;

        _titleBar = new TitleBar();
        _msgLayout = new Frame();
        _msgLabel = new Label();
        _okButton = getButton("OK");
        _cancel = getButton("Cancel");

        _toolbar = new HorizontalStack();
        _userbar = new HorizontalStack();

        eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.ESCAPE) {
                close();
            }
        });

        setStyle(DefaultsService.getDefaultStyle(MessageItem.class));
    }

    /**
     * Constructs a MessageBox with specified message and title.
     * 
     * @param message Message to a user as java.lang.String.
     * @param title   Title of MessageBox as java.lang.String.
     */
    public MessageBox(String message, String title) {
        this();
        setWindowTitle(title);
        _titleBar.setText(title);
        _msgLabel.setText(message);
    }

    /**
     * Setting a text of message of MessageBox.
     * 
     * @param text Text of message as java.lang.String.
     */
    public void setMessageText(String text) {
        _msgLabel.setText(text);
    }

    /**
     * Getting the current text of message of MessageBox.
     * 
     * @return The current text of message as java.lang.String.
     */
    public String getMessageText() {
        return _msgLabel.getText();
    }

    /**
     * Setting a text of title of MessageBox.
     * 
     * @param title Text of title as java.lang.String.
     */
    public void setTitle(String title) {
        _titleBar.setText(title);
    }

    /**
     * Getting the current text of title of MessageBox.
     * 
     * @return The current text of title as java.lang.String.
     */
    public String getTitle() {
        return _titleBar.getText();
    }

    /**
     * Initialize MessageBox window.
     */
    @Override
    public void initWindow() {
        isBorderHidden = true;
        isAlwaysOnTop = true;

        // adding toolbar
        _titleBar.getMaximizeButton().setVisible(false);

        int wGlobal = 0;

        // toolbar size
        int w = _okButton.getWidth() + _okButton.getMargin().left + _okButton.getMargin().right;
        if (_cancel.isVisible()) {
            w = w * 2 + 10;
        }
        _toolbar.setSize(w, _okButton.getHeight() + _okButton.getMargin().top + _okButton.getMargin().bottom);
        wGlobal += w;

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

        wGlobal += _toolbar.getMargin().left + _toolbar.getMargin().right;
        wGlobal += _userbar.getMargin().left + _userbar.getMargin().right + _userbar.getWidth();
        wGlobal += _msgLayout.getPadding().left + _msgLayout.getPadding().right;

        if (getWidth() < wGlobal) {
            setWidth(wGlobal);
        }
        int wText = _msgLabel.getTextWidth() + _msgLayout.getMargin().left + _msgLayout.getMargin().right
                + _msgLayout.getPadding().left + _msgLayout.getPadding().right + _msgLabel.getMargin().left
                + _msgLabel.getMargin().right + _msgLabel.getPadding().left + _msgLabel.getPadding().right + 10;
        if (getWidth() < wText) {
            setWidth(wText);
        }
        addItems(_titleBar, _msgLayout);
        getLayout().getContainer().update(GeometryEventType.RESIZE_WIDTH, 0);

        if (!isEmpty) {
            _toolbar.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
            _msgLayout.addItems(_msgLabel, _userbar, _toolbar);
        } else {
            _msgLayout.addItems(_msgLabel, _toolbar);
        }

        // queue
        if (!isEmpty) {
            for (ButtonCore btn : _queue) {
                _userbar.addItem(btn);
            }
        }

        _toolbar.addItems(_okButton, _cancel);

        setMinWidth(wGlobal);

        tryAdd(_userMap, 1, _okButton);
        tryAdd(_userMap, 0, _titleBar.getCloseButton());
        tryAdd(_userMap, -1, _cancel);
        // buttons
        _okButton.setItemName("OK");
        _okButton.eventMouseClick.add((sender, args) -> {
            _userButtonResult = 1;
            _result = true;
            close();
        });

        _titleBar.getCloseButton().eventMouseClick.clear();
        _titleBar.getCloseButton().eventMouseClick.add((sender, args) -> {
            _userButtonResult = 0;
            close();
        });

        _cancel.setItemName("Cancel");
        _cancel.eventMouseClick.add((sender, args) -> {
            _userButtonResult = -1;
            close();
        });
    }

    /**
     * Show MessageBox window.
     */
    @Override
    public void show() {
        _result = false;
        super.show();
    }

    /**
     * Close MessageBox window.
     */
    @Override
    public void close() {
        super.close();

        if (onCloseDialog != null) {
            onCloseDialog.execute();
        }
    }

    private List<ButtonCore> _queue = new LinkedList<>();

    /**
     * Adding a custom user button to toolbal with the specified ID.
     * 
     * @param button User button as com.spvessel.spacevil.ButtonCore.
     * @param id     Button's ID as integer value.
     */
    public void addUserButton(ButtonCore button, int id) {
        if (id == -1 || id == 0 || id == 1) {
            return;
        }

        if (!tryAdd(_userMap, id, button))
            return;

        _queue.add(button);

        button.setStyle(_btnStyle);
        button.eventMouseClick.add((sender, args) -> {
            _userButtonResult = id;
            close();
        });
    }

    private boolean tryAdd(Map<Integer, ButtonCore> map, int id, ButtonCore btn) {
        if (map.containsKey(id))
            return false;

        map.put(id, btn);
        return true;
    }

    private ButtonCore getButton(String name) {
        ButtonCore btn = new ButtonCore(name);
        return btn;
    }

    private Style _btnStyle = null;

    /**
     * Getting the current style of a custom user button (that placed into user's
     * toolbar).
     * 
     * @return The current style of custom user button as
     *         com.spvessel.spacevil.Decorations.Style.
     */
    public Style getDialogButtonStyle() {
        return _btnStyle;
    }

    /**
     * Setting a style for a custom user button (that placed into user's toolbar).
     * 
     * @param style A style for custom user button as
     *              com.spvessel.spacevil.Decorations.Style.
     */
    public void setDialogButtonStyle(Style style) {
        _btnStyle = style;
    }

    /**
     * Setting a style for entire MessageBox.
     * <p>
     * Inner styles: "window", "message", "layout", "toolbar", "userbar" (custom
     * toolbar), "button".
     * 
     * @param style A style for MessageBox as
     *              com.spvessel.spacevil.Decorations.Style.
     */
    public void setStyle(Style style) {
        if (style == null) {
            return;
        }

        Style innerStyle = style.getInnerStyle("window");
        if (innerStyle != null) {
            getLayout().getContainer().setStyle(innerStyle);
            setMinSize(innerStyle.minWidth, innerStyle.minHeight);
            setSize(innerStyle.width, innerStyle.height);
        }

        innerStyle = style.getInnerStyle("message");
        if (innerStyle != null) {
            _msgLabel.setStyle(innerStyle);
        }
        innerStyle = style.getInnerStyle("layout");
        if (innerStyle != null) {
            _msgLayout.setStyle(innerStyle);
        }
        innerStyle = style.getInnerStyle("toolbar");
        if (innerStyle != null) {
            _toolbar.setStyle(innerStyle);
        }
        innerStyle = style.getInnerStyle("userbar");
        if (innerStyle != null) {
            _userbar.setStyle(innerStyle);
        }
        innerStyle = style.getInnerStyle("button");
        if (innerStyle != null) {
            _btnStyle = innerStyle.clone();
            _okButton.setStyle(innerStyle);
            _cancel.setStyle(innerStyle);
        }
    }
}