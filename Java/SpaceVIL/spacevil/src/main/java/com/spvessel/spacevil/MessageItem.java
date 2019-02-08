package com.spvessel.spacevil;

import java.awt.Color;

import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

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
    // private String _title = "";
    private TitleBar titleBar = new TitleBar();
    private Label msg = new Label();

    /**
     * Constructs a MessageItem
     */
    public MessageItem() {
        // super();
        setItemName("MessageItem_" + count);
        count++;
    }

    /**
     * Constructs a MessageItem with message and title
     */
    public MessageItem(String m, String t) {
        this();
        _message = m;
        // _title = t;

        titleBar.setText(t);
        msg.setText(_message);
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
        titleBar.setText(title);
    }
    public String getTitle() {
        return titleBar.getText();
    }

    @Override
    public void initElements() {
        // important!
        super.initElements();

        VerticalStack layout = new VerticalStack();
        layout.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
        layout.setMargin(0, 30, 0, 0);
        layout.setPadding(6, 6, 6, 6);
        layout.setSpacing(0, 10);
        layout.setBackground(255, 255, 255, 20);

        // adding toolbar
        titleBar.getMaximizeButton().setVisible(false);
        window.addItems(titleBar, layout);

        // message
        msg.setForeground(new Color(210, 210, 210));
        msg.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        msg.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        msg.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);

        // ok
        ButtonCore ok = new ButtonCore("OK");
        ok.setBackground(100, 255, 150);
        ok.setForeground(Color.black);
        ok.setItemName("OK");
        ok.setSize(100, 30);
        ok.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        ok.setAlignment(ItemAlignment.HCENTER, ItemAlignment.BOTTOM);
        ok.setBorderRadius(new CornerRadius(6));
        ok.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 80)));
        ok.eventMouseClick.add((sender, args) -> {
            _result = true;
            close();
        });
        layout.addItems(msg, ok);

        titleBar.getCloseButton().eventMouseClick.clear();
        titleBar.getCloseButton().eventMouseClick.add((sender, args) -> {
            close();
        });
    }

    /// <summary>
    /// Show MessageBox
    /// </summary>
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

    }
}
