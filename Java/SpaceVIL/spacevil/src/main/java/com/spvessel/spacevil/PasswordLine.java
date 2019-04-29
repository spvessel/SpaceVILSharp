package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class PasswordLine extends HorizontalStack {
    private static int count = 0;

    private BlankItem _show_pwd_btn;
    private TextEncrypt _textEncrypt;

    /**
     * Constructs a PasswordLine
     */
    public PasswordLine() {
        setItemName("PasswordLine_" + count);
        _show_pwd_btn = new BlankItem();
        _show_pwd_btn.setItemName(getItemName() + "_marker");
        _textEncrypt = new TextEncrypt();
        count++;

        setStyle(DefaultsService.getDefaultStyle(PasswordLine.class));
    }

    private void showPassword(boolean value) {
        _textEncrypt.showPassword(value);
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

    public void setFontSize(int size) {
        _textEncrypt.setFontSize(size);
    }

    public void setFontStyle(int style) {
        _textEncrypt.setFontStyle(style);
    }

    public void setFontFamily(String font_family) {
        _textEncrypt.setFontFamily(font_family);
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
        addItems(_textEncrypt, _show_pwd_btn);

        ImageItem eye = new ImageItem(DefaultsService.getDefaultImage(EmbeddedImage.EYE, EmbeddedImageSize.SIZE_32X32));
        eye.keepAspectRatio(true);
        Color eyeBtnShadeColor = new Color(80, 80, 80);
        eye.setColorOverlay(eyeBtnShadeColor);
        _show_pwd_btn.addItem(eye);

        _show_pwd_btn.setPassEvents(false);
        _show_pwd_btn.eventMousePress.add((sender, args) -> {
            showPassword(true);
            eye.setColorOverlay(new Color(30, 30, 30));
        });
        _show_pwd_btn.eventMouseClick.add((sender, args) -> {
            showPassword(false);
            eye.setColorOverlay(eyeBtnShadeColor);
        });
        _show_pwd_btn.eventMouseLeave.add((sender, args) -> {
                showPassword(false);
                eye.setColorOverlay(eyeBtnShadeColor);
        });
    }

    /**
     * Returns width of the whole text in the PasswordLine (includes visible and
     * invisible parts of the text)
     */
    public int getTextWidth() {
        return _textEncrypt.getWidth();
    }

    /**
     * Returns height of the whole text in the PasswordLine (includes visible and
     * invisible parts of the text)
     */
    public int getTextHeight() {
        return _textEncrypt.getHeight();
    }

    /**
     * Remove all text from the PasswordLine
     */
    @Override
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

    public void setSubstrateText(String substrateText) {
        _textEncrypt.setSubstrateText(substrateText);
    }

    public void setSubstrateFontSize(int size) {
        _textEncrypt.setSubstrateFontSize(size);
    }

    public void setSubstrateFontStyle(int style) {
        _textEncrypt.setSubstrateFontStyle(style);
    }

    public void setSubstrateForeground(Color foreground) {
        _textEncrypt.setSubstrateForeground(foreground);
    }

    public void setSubstrateForeground(int r, int g, int b) {
        _textEncrypt.setSubstrateForeground(r, g, b);
    }

    public void seSubstratetForeground(int r, int g, int b, int a) {
        _textEncrypt.setSubstrateForeground(r, g, b, a);
    }

    public void setSubstrateForeground(float r, float g, float b) {
        _textEncrypt.setSubstrateForeground(r, g, b);
    }

    public void setSubstrateForeground(float r, float g, float b, float a) {
        _textEncrypt.setSubstrateForeground(r, g, b, a);
    }

    public Color getSubstrateForeground() {
        return _textEncrypt.getSubstrateForeground();
    }

    public String getSubstrateText() {
        return _textEncrypt.getSubstrateText();
    }
}
