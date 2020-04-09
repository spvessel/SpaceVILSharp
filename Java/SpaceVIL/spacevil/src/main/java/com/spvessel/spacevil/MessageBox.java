package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * MessageBox - a modal window for displaying any messages with ability to reply to them. 
 * It supports custom toolbar to make user's reply flexible.
 */
public class MessageBox extends DialogWindow {
    /**
     * An event to describe the actions that must be performed after the window is closed.
     * <p> Event type: com.spvessel.spacevil.Core.EventCommonMethod.
     * <p> Function arguments: none.
     */
    public EventCommonMethod onCloseDialog = new EventCommonMethod();

    static int count = 0;

    private boolean _result = false;

    /**
     * Get MessageBox boolean result
     * <p> Default: False
     * @return True: OK button was clicked. False: Close button or Cancel button was clicked.
     */
    public boolean getResult() {
        return _result;
    }

    Map<ButtonCore, Integer> _userMap = new HashMap<>();
    ButtonCore _userButtonResult = null;

    /**
     * Getting result from custom toolbar (if it was created).
     * @return Id of clicked button (see addUserButton(ButtonCore button, int id)).
     */
    public int getUserButtonResult() {
        if (_userButtonResult != null && _userMap.containsKey(_userButtonResult)) {
            return _userMap.get(_userButtonResult);
        }
        return -1;
    }

    private TitleBar _titleBar;
    private Frame _msgLayout;
    private Label _msgLabel;
    private ButtonCore _okButton;

    /**
     * Getting OK button for appearance customizing or assigning new actions.
     * @return MessageBox's OK button as MessageBox's OK button as com.spvessel.spacevil.ButtonCore.
     */
    public ButtonCore getOkButton() {
        return _okButton;
    }

    private ButtonCore _cancel;

    /**
     * Getting CANCEL button for appearance customizing or assigning new actions.
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
     * @param message Message to a user as java.lang.String.
     * @param title Title of MessageBox as java.lang.String.
     */
    public MessageBox(String message, String title) {
        this();
        setWindowTitle(title);
        _titleBar.setText(title);
        _msgLabel.setText(message);
    }

    /**
     * Setting a text of message of MessageBox.
     * @param text Text of message as java.lang.String.
     */
    public void setMessageText(String text) {
        _msgLabel.setText(text);
    }

    /**
     * Getting the current text of message of MessageBox.
     * @return The current text of message as java.lang.String.
     */
    public String getMessageText() {
        return _msgLabel.getText();
    }

    /**
     * Setting a text of title of MessageBox.
     * @param title Text of title as java.lang.String.
     */
    public void setTitle(String title) {
        _titleBar.setText(title);
    }

    /**
     * Getting the current text of title of MessageBox.
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

        int w_global = 0;

        // toolbar size
        int w = _okButton.getWidth() + _okButton.getMargin().left + _okButton.getMargin().right;
        if (_cancel.isVisible()) {
            w = w * 2 + 10;
        }
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

        setMinWidth(w_global);
        if (getWidth() < w_global) {
            setWidth(w_global);
        }
        int w_text = _msgLabel.getTextWidth() + _msgLayout.getMargin().left + _msgLayout.getMargin().right
                + _msgLayout.getPadding().left + _msgLayout.getPadding().right + _msgLabel.getMargin().left
                + _msgLabel.getMargin().right + _msgLabel.getPadding().left + _msgLabel.getPadding().right + 10;
        if (getWidth() < w_text) {
            setWidth(w_text);
        }
        addItems(_titleBar, _msgLayout);
        getLayout().getContainer().update(GeometryEventType.RESIZE_WIDTH, 0);

        if (!isEmpty) {
            _toolbar.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
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
        _toolbar.addItems(_okButton, _cancel);
        _userMap.put(_okButton, 1);
        _userMap.put(_titleBar.getCloseButton(), 0);
        _userMap.put(_cancel, -1);
        // buttons
        _okButton.setItemName("OK");
        _okButton.eventMouseClick.add((sender, args) -> {
            _userButtonResult = _okButton;
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
     * @param button User button as com.spvessel.spacevil.ButtonCore.
     * @param id Button's ID as integer value.
     */
    public void addUserButton(ButtonCore button, int id) {
        if (id == -1 || id == 0 || id == 1) {
            return;
        }

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
     * Getting the current style of a custom user button (that placed into user's toolbar).
     * @return The current style of custom user button as com.spvessel.spacevil.Decorations.Style.
     */
    public Style getDialogButtonStyle() {
        return _btnStyle;
    }

    /**
     * Setting a style for a custom user button (that placed into user's toolbar).
     * @param style A style for custom user button as com.spvessel.spacevil.Decorations.Style.
     */
    public void setDialogButtonStyle(Style style) {
        _btnStyle = style;
    }

    /**
     * Setting a style for entire MessageBox.
     * <p> Inner styles: "window", "message", "layout", "toolbar", "userbar" (custom toolbar), "button".
     * @param style A style for MessageBox as com.spvessel.spacevil.Decorations.Style.
     */
    public void setStyle(Style style) {
        if (style == null) {
            return;
        }
        
        Style inner_style = style.getInnerStyle("window");
        if (inner_style != null) {
            getLayout().getContainer().setStyle(inner_style);
            setMinSize(inner_style.minWidth, inner_style.minHeight);
            setSize(inner_style.width, inner_style.height);
        }

        inner_style = style.getInnerStyle("message");
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
            _cancel.setStyle(inner_style);
        }
    }
}