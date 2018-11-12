package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.*;
import com.spvessel.Decorations.Indents;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.KeyCode;
import com.spvessel.Flags.KeyMods;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PasswordLine extends HorizontalStack {
    static int count = 0;

    private ButtonToggle _show_pwd_btn;
    private TextEncrypt _textEncrypt;

    public PasswordLine() {
        setItemName("PasswordLine_" + count);
        _show_pwd_btn = new ButtonToggle();
        _show_pwd_btn.setItemName(getItemName() + "_marker");
        _textEncrypt = new TextEncrypt();
        count++;

        setStyle(DefaultsService.getDefaultStyle(PasswordLine.class));
    }

    private void showPassword(InterfaceItem sender) {
        _textEncrypt.showPassword(_show_pwd_btn.isToggled());
    }

    @Override
    public void setFocused(boolean value) {
        super.setFocused(value);
        _textEncrypt.setFocused(value);
    }

    public void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(Arrays.asList(alignment));
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textEncrypt.setTextAlignment(alignment);
    }

    public void setTextMargin(Indents margin) {
        _textEncrypt.setMargin(margin);
    }

    public void setFont(Font font) {
        _textEncrypt.setFont(font);
    }

    public Font getFont() {
        return _textEncrypt.getFont();
    }

    public String getPassword() {
        return _textEncrypt.getPassword();
    }

    public void setForeground(Color color) {
        _textEncrypt.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        _textEncrypt.setForeground(r, g, b);
    }

    public void setForeground(int r, int g, int b, int a) {
        _textEncrypt.setForeground(r, g, b, a);
    }

    public void setForeground(float r, float g, float b) {
        _textEncrypt.setForeground(r, g, b);
    }

    public void setForeground(float r, float g, float b, float a) {
        _textEncrypt.setForeground(r, g, b, a);
    }

    public Color getForeground() {
        return _textEncrypt.getForeground();
    }

    public boolean isEditable() {
        return _textEncrypt.isEditable();
    }

    public void setEditable(boolean value) {
        _textEncrypt.setEditable(value);
    }

    public void initElements() {
        _show_pwd_btn.setPassEvents(false);
        _show_pwd_btn.eventToggle.add((sender, args) -> showPassword(sender));

        addItems(_textEncrypt, _show_pwd_btn);
    }

    public int getTextWidth() {
        return _textEncrypt.getWidth();
    }

    public int getTextHeight() {
        return _textEncrypt.getHeight();
    }

    public void clear() {
        _textEncrypt.clear();
    }

    // style
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        Style inner_style = style.getInnerStyle("showmarker");
        if (inner_style != null) {
            _show_pwd_btn.setStyle(inner_style);
        }
        setSpacing(5, 0);
    }
}
