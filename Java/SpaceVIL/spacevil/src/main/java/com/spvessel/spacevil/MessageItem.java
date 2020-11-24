package com.spvessel.spacevil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;

/**
 * MessageItem - an imitation of modal window for displaying any messages with
 * ability to reply to them. It supports custom toolbar to make user's reply
 * flexible.
 * <p>
 * Contains ok button, cancel button, titlebar, toolbar.
 * <p>
 * Supports all events except drag and drop.
 */
public class MessageItem extends DialogItem {
    static int count = 0;

    private boolean _result = false;

    /**
     * Getting MessageItem result.
     * 
     * @return True: OK button was clicked. False: any button other than OK was
     *         pressed.
     */
    public boolean getResult() {
        return _result;
    }

    private Map<ButtonCore, Integer> _userMap = new HashMap<>();
    private ButtonCore _userButtonResult = null;

    /**
     * Getting result from custom toolbar (if it was created).
     * 
     * @return Id of clicked button (see addUserButton(ButtonCore button, int id)).
     */
    public int getUserButtonResult() {
        if (_userButtonResult != null && _userMap.containsKey(_userButtonResult))
            return _userMap.get(_userButtonResult);
        return -1;
    }

    private TitleBar _titleBar;
    private Frame _msgLayout;
    private Label _msgLabel;
    private ButtonCore _okButton;

    /**
     * Getting OK button for appearance customizing or assigning new actions.
     * 
     * @return MessageItem's OK button as com.spvessel.spacevil.ButtonCore.
     */
    public ButtonCore getOkButton() {
        return _okButton;
    }

    private ButtonCore _cancelButton;

    /**
     * Getting CANCEL button for appearance customizing or assigning new actions.
     * 
     * @return MessageItem's CANCEL button as com.spvessel.spacevil.ButtonCore.
     */
    public ButtonCore getCancelButton() {
        return _cancelButton;
    }

    /**
     * Setting CANCEL button visible of invisible.
     * 
     * @param value True: if you want CANCEL button to be visible. False:if you want
     *              CANCEL button to be invisible.
     */
    public void setCancelVisible(boolean value) {
        _cancelButton.setVisible(value);
    }

    private HorizontalStack _toolbar;
    private HorizontalStack _userbar;

    /**
     * Default MessageItem constructor.
     */
    public MessageItem() {
        setItemName("MessageItem_" + count);
        count++;

        _titleBar = new TitleBar();
        _msgLayout = new Frame();
        _msgLabel = new Label();
        _okButton = getButton("OK");
        _cancelButton = getButton("Cancel");

        _toolbar = new HorizontalStack();
        _userbar = new HorizontalStack();
        window.isLocked = true;

        eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.Escape)
                close();
        });

        setStyle(DefaultsService.getDefaultStyle(MessageItem.class));
    }

    /**
     * Constructs a MessageItem with specified message and title.
     * 
     * @param message Message to a user.
     * @param title   Title of MessageItem.
     */
    public MessageItem(String message, String title) {
        this();

        _titleBar.setText(title);
        _msgLabel.setText(message);
    }

    /**
     * Setting a text of message of MessageItem.
     * 
     * @param text Text of message.
     */
    public void setMessageText(String text) {
        _msgLabel.setText(text);
    }

    /**
     * Getting the current text of message of MessageItem.
     * 
     * @return The current text of message.
     */
    public String getMessageText() {
        return _msgLabel.getText();
    }

    /**
     * Setting a text of title of MessageItem.
     * 
     * @param title Text of title.
     */
    public void setTitle(String title) {
        _titleBar.setText(title);
    }

    /**
     * Getting the current text of title of MessageItem.
     * 
     * @return The current text of title.
     */
    public String getTitle() {
        return _titleBar.getText();
    }

    /**
     * Initializing all elements in the MessageItem.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        // important!
        super.initElements();

        // adding toolbar
        _titleBar.getMaximizeButton().setVisible(false);

        int w_global = 0;

        // toolbar size
        int w = _okButton.getWidth() + _okButton.getMargin().left + _okButton.getMargin().right;
        if (_cancelButton.isVisible())
            w = w * 2 + 10;
        _toolbar.setSize(w, _okButton.getHeight() + _okButton.getMargin().top + _okButton.getMargin().bottom);
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
        int w_text = _msgLabel.getTextWidth() + _msgLayout.getMargin().left + _msgLayout.getMargin().right
                + _msgLayout.getPadding().left + _msgLayout.getPadding().right + _msgLabel.getMargin().left
                + _msgLabel.getMargin().right + _msgLabel.getPadding().left + _msgLabel.getPadding().right + 10;
        if (window.getWidth() < w_text)
            window.setWidth(w_text);
        window.addItems(_titleBar, _msgLayout);
        window.update(GeometryEventType.ResizeWidth, 0);

        if (!isEmpty) {
            _toolbar.setAlignment(ItemAlignment.Right, ItemAlignment.Bottom);
            int right = _toolbar.getWidth() + _toolbar.getMargin().left + _toolbar.getMargin().right + 10;
            _userbar.setMargin(0, 0, right / 2, 0);
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
        _toolbar.addItems(_okButton, _cancelButton);
        _userMap.put(_okButton, 1);
        _userMap.put(_titleBar.getCloseButton(), 0);
        _userMap.put(_cancelButton, -1);
        // buttons
        _okButton.setItemName("OK");
        _okButton.eventMouseClick.add((sender, args) -> {
            _userButtonResult = _okButton;
            _result = true;
            close();
        });
        _cancelButton.setItemName("Cancel");
        _cancelButton.eventMouseClick.add((sender, args) -> {
            _userButtonResult = _cancelButton;
            close();
        });

        _titleBar.getCloseButton().eventMouseClick.clear();
        _titleBar.getCloseButton().eventMouseClick.add((sender, args) -> {
            _userButtonResult = _titleBar.getCloseButton();
            close();
        });
    }

    /**
     * Shows MessageItem and attaches it to the specified window (see
     * com.spvessel.spacevil.CoreWindow, com.spvessel.spacevil.ActiveWindow,
     * com.spvessel.spacevil.DialogWindow).
     * 
     * @param handler Window for attaching MessageItem.
     */
    @Override
    public void show(CoreWindow handler) {
        _result = false;
        super.show(handler);
    }

    /**
     * Closes MessageItem.
     */
    @Override
    public void close() {
        if (onCloseDialog != null)
            onCloseDialog.execute();

        super.close();
    }

    private List<ButtonCore> _queue = new LinkedList<>();

    /**
     * Adding a custom user button to toolbal with the specified ID.
     * 
     * @param button User button as com.spvessel.spacevil.ButtonCore.
     * @param id     Button's ID as integer.
     */
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

    /**
     * Getting the current style of a custom user button (that placed into user's
     * toolbar).
     * 
     * @return The current style of custom user buttons as
     *         com.spvessel.spacevil.Decorations.Style.
     */
    public Style getDialogButtonStyle() {
        return _btnStyle;
    }

    /**
     * Setting a style for a custom user button (that placed into user's toolbar).
     * 
     * @param style A style for custom user buttons as
     *              com.spvessel.spacevil.Decorations.Style.
     */
    public void setDialogButtonStyle(Style style) {
        _btnStyle = style;
    }

    /**
     * Setting style of the MessageItem.
     * 
     * <p>
     * Inner styles: "message", "layout", "toolbar", "userbar" (custom toolbar),
     * "button".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("message");
        if (inner_style != null) {
            _msgLabel.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("layout");
        if (inner_style != null) {
            _msgLayout.setStyle(inner_style);
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
            _okButton.setStyle(inner_style);
            _cancelButton.setStyle(inner_style);
        }
    }
}
