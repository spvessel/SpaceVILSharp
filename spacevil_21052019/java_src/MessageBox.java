package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceMouseMethodState;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.awt.Color;

public class MessageBox extends DialogWindow {
    private boolean _result = false;

    /**
     * Get MessageBox boolean result
     */
    public boolean getResult() {
        return _result;
    }

    private String _message = "";

    /**
     * MessageBox text
     */
    public void setMessageText(String text) {
        _message = text;
    }
    public String getMessageText() {
        return _message;
    }

    private TitleBar titleBar;
    private Label msg;

    /**
     * Constructs a MessageBox with message and title
     */
    public MessageBox(String message, String title) {
        _message = message;
        setDialogTitle(title);

        // Title
        titleBar.setText(getDialogTitle());
        titleBar.setPadding(0, 0, 10, 0);
        msg.setText(getMessageText());
    }

    /**
     * Initialize MessageBox window
     */
    @Override
    public void initWindow() {
        titleBar = new TitleBar();
        msg = new Label();

        // window's attr
        setWindowName("MessageBox_" + getCount());
        setWindowTitle("MessageBox_" + getCount());
        setWidth(300);
        setMinWidth(300);
        setHeight(150);
        setMinHeight(150);
        setPadding(2, 2, 2, 2);
        setBackground(new Color(45, 45, 45));
        isBorderHidden = true;
        isDialog = true;
        isAlwaysOnTop = true;
        isCentered = true;
        addItem(titleBar);

        VerticalStack layout = new VerticalStack();
        layout.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
        layout.setMargin(0, 30, 0, 0);
        layout.setPadding(6, 6, 6, 6);
        layout.setSpacing(0, 10);
        layout.setBackground(255, 255, 255, 20);

        // adding toolbar
        addItem(layout);

        // message
        // msg.setFont(new Font(new FontFamily("Courier New"), 14, FontStyle.Regular));
        msg.setForeground(new Color(210, 210, 210));
        msg.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        msg.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        msg.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);

        // ok
        ButtonCore ok = new ButtonCore("OK");
        ok.setBackground(100, 255, 150);
        ok.setForeground(new Color(0, 0, 0));
        ok.setItemName("OK");
        ok.setSize(100, 30);
        ok.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        ok.setAlignment(ItemAlignment.HCENTER, ItemAlignment.BOTTOM);
        ok.setBorderRadius(6);
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        ok.addItemState(ItemStateType.HOVERED, hovered);
        InterfaceMouseMethodState ok_click = (sender, args) -> {
            _result = true;
            close();
        };
        ok.eventMouseClick.add(ok_click);
        layout.addItems(msg, ok);
    }

    /**
     * Show MessageBox
     */
    @Override
    public void show() {
        _result = false;
        super.show();
    }
}