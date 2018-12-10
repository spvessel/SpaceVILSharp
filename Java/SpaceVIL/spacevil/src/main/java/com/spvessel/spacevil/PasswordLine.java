package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class PasswordLine extends HorizontalStack {
    private static int count = 0;

    private ButtonToggle _show_pwd_btn;
    private TextEncrypt _textEncrypt;

    /**
     * Constructs a PasswordLine
     */
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

    /**
     * Set PasswordLine focused/unfocused
     */
    @Override
    public void setFocused(boolean value) {
        super.setFocused(value);
        _textEncrypt.setFocused(value);
    }

    /**
     * Text alignment in the PasswordLine
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(Arrays.asList(alignment));
    }
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textEncrypt.setTextAlignment(alignment);
    }

    /**
     * Text margin in the PasswordLine
     */
    public void setTextMargin(Indents margin) {
        _textEncrypt.setMargin(margin);
    }

    /**
     * Text font in the PasswordLine
     */
    public void setFont(Font font) {
        _textEncrypt.setFont(font);
    }
    public Font getFont() {
        return _textEncrypt.getFont();
    }

    /**
     * @return password string
     */
    public String getPassword() {
        return _textEncrypt.getPassword();
    }

    /**
     * Text color in the PasswordLine
     */
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

    /**
     * Returns if PasswordLine editable or not
     */
    public boolean isEditable() {
        return _textEncrypt.isEditable();
    }

    /**
     * Set PasswordLine editable true or false
     */
    public void setEditable(boolean value) {
        _textEncrypt.setEditable(value);
    }

    /**
     * Initialization and adding of all elements in the PasswordLine
     */
    public void initElements() {
        _show_pwd_btn.setPassEvents(false);
        _show_pwd_btn.eventToggle.add((sender, args) -> showPassword(sender));

        addItems(_textEncrypt, _show_pwd_btn);
    }

    /**
     * Returns width of the whole text in the PasswordLine
     * (includes visible and invisible parts of the text)
     */
    public int getTextWidth() {
        return _textEncrypt.getWidth();
    }

    /**
     * Returns height of the whole text in the PasswordLine
     * (includes visible and invisible parts of the text)
     */
    public int getTextHeight() {
        return _textEncrypt.getHeight();
    }

    /**
     * Remove all text from the PasswordLine
     */
    public void clear() {
        _textEncrypt.clear();
    }

    /**
     * Set style of the PasswordLine
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        Style inner_style = style.getInnerStyle("showmarker");
        if (inner_style != null) {
            _show_pwd_btn.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("textedit");
        if (inner_style != null) {
            _textEncrypt.setStyle(inner_style);
        }
    }
}
