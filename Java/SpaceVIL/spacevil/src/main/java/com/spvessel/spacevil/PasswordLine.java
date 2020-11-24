package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * PaasswordLine is designed to hide the input of text information.
 * <p>
 * Contains text field, unhide button.
 * <p>
 * Supports all events except drag and drop.
 */
public class PasswordLine extends HorizontalStack {
    private static int count = 0;

    private BlankItem _showPwdBtn;
    private TextEncrypt _textEncrypt;

    /**
     * Default PasswordLine constructor
     */
    public PasswordLine() {
        setItemName("PasswordLine_" + count);
        _showPwdBtn = new BlankItem();
        _showPwdBtn.setItemName(getItemName() + "_marker");
        _textEncrypt = new TextEncrypt();
        count++;

        setStyle(DefaultsService.getDefaultStyle(PasswordLine.class));
    }

    private void showPassword(boolean value) {
        _textEncrypt.showPassword(value);
    }

    /**
     * Setting focus on this PasswordLine if it is focusable.
     */
    @Override
    public void setFocused(boolean value) {
        super.setFocused(value);
        _textEncrypt.setFocused(value);
    }

    /**
     * Setting alignment of a PasswordLine text. Combines with alignment by
     * vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _textEncrypt.setTextAlignment(alignment);
    }

    /**
     * Setting alignment of a PasswordLine text. Combines with alignment by
     * vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as List of
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textEncrypt.setTextAlignment(alignment);
    }

    /**
     * Getting alignment of a PasswordLine text.
     * 
     * @return Text alignment as List of com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public List<ItemAlignment> getTextAlignment() {
        return _textEncrypt.getTextAlignment();
    }


    /**
     * Setting indents for the text to offset text relative to PasswordLine.
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        _textEncrypt.setTextMargin(margin);
    }

    /**
     * Setting indents for the text to offset text relative to PasswordLine.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setTextMargin(int left, int top, int right, int bottom) {
        // _textEncrypt.setTextMargin(left, top, right, bottom);
        setTextMargin(new Indents(left, top, right, bottom));
    }

    /**
     * Getting indents of the text.
     * 
     * @return Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getTextMargin() {
        return _textEncrypt.getTextMargin();
    }

    /**
     * Setting font of the text.
     * 
     * @param font Font as java.awt.Font.
     */
    public void setFont(Font font) {
        _textEncrypt.setFont(font);
    }

    /**
     * Setting font size of the text.
     * 
     * @param size New size of the font.
     */
    public void setFontSize(int size) {
        _textEncrypt.setFontSize(size);
    }

    /**
     * Setting font style of the text.
     * 
     * @param style New font style (from java.awt.Font package).
     */
    public void setFontStyle(int style) {
        _textEncrypt.setFontStyle(style);
    }

    /**
     * Setting new font family of the text.
     * 
     * @param fontFamily New font family name.
     */
    public void setFontFamily(String fontFamily) {
        _textEncrypt.setFontFamily(fontFamily);
    }

    /**
     * Getting the current font of the text.
     * 
     * @return Font as java.awt.Font.
     */
    public Font getFont() {
        return _textEncrypt.getFont();
    }

    /**
     * Getting entered hidden text data.
     * 
     * @return Text data.
     */
    public String getPassword() {
        return _textEncrypt.getPassword();
    }

    /**
     * Setting text color of a PasswordLine.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        _textEncrypt.setForeground(color);
    }

    /**
     * Setting text color of a PasswordLine in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        setForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting text color of an item in byte RGBA format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b, int a) {
        setForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Setting text color of a PasswordLine in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        setForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting text color of a PasswordLine in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b, float a) {
        setForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Getting current text color.
     * 
     * @return Text color as as java.awt.Color.
     */
    public Color getForeground() {
        return _textEncrypt.getForeground();
    }

    /**
     * Returns True if PasswordLine is editable otherwise returns False.
     * 
     * @return True: if PasswordLine is editable. True: if PasswordLine is not
     *         editable.
     */
    public boolean isEditable() {
        return _textEncrypt.isEditable();
    }

    /**
     * Setting PasswordLine text field be editable or be non-editable.
     * 
     * @param value True: if you want PasswordLine be editable. True: if you want
     *              PasswordLine be non-editable.
     */
    public void setEditable(boolean value) {
        _textEncrypt.setEditable(value);
    }

    /**
     * Initializing all elements in the PasswordLine.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    public void initElements() {
        addItems(_textEncrypt, _showPwdBtn);

        ImageItem eye = new ImageItem(DefaultsService.getDefaultImage(EmbeddedImage.Eye, EmbeddedImageSize.Size64x64));
        eye.keepAspectRatio(true);
        Color eyeBtnShadeColor = new Color(80, 80, 80);
        eye.setColorOverlay(eyeBtnShadeColor);
        _showPwdBtn.addItem(eye);

        _showPwdBtn.setPassEvents(false);
        _showPwdBtn.eventMousePress.add((sender, args) -> {
            showPassword(true);
            eye.setColorOverlay(new Color(30, 30, 30));
        });
        _showPwdBtn.eventMouseClick.add((sender, args) -> {
            showPassword(false);
            eye.setColorOverlay(eyeBtnShadeColor);
        });
        _showPwdBtn.eventMouseLeave.add((sender, args) -> {
            showPassword(false);
            eye.setColorOverlay(eyeBtnShadeColor);
        });
    }

    /**
     * Getting the text width (useful when you need resize PasswordLine by text
     * width).
     * 
     * @return Text width.
     */
    public int getTextWidth() {
        return _textEncrypt.getWidth();
    }

    /**
     * Getting the text height (useful when you need resize PasswordLine by text
     * height).
     * 
     * @return Text height.
     */
    public int getTextHeight() {
        return _textEncrypt.getHeight();
    }

    /**
     * Remove all text from the PasswordLine.
     */
    @Override
    public void clear() {
        _textEncrypt.clear();
    }

    /**
     * Setting style of the PasswordLine.
     * <p>
     * Inner styles: "showmarker", "textedit".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        Style inner_style = style.getInnerStyle("showmarker");
        if (inner_style != null) {
            _showPwdBtn.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("textedit");
        if (inner_style != null) {
            _textEncrypt.setStyle(inner_style);
        }
    }

    /**
     * Setting the substrate text (hint under main text, when you start typing
     * substrate becomes invisible).
     * 
     * @param substrateText Substrate text.
     */
    public void setSubstrateText(String substrateText) {
        _textEncrypt.setSubstrateText(substrateText);
    }

    /**
     * Setting font size of the substrate text (hint under main text, when you start
     * typing substrate becomes invisible).
     * <p>
     * Font family of substrate text is the same as main font.
     * 
     * @param size New size of the font.
     */
    public void setSubstrateFontSize(int size) {
        _textEncrypt.setSubstrateFontSize(size);
    }

    /**
     * Setting font style of the substrate text (hint under main text, when you
     * start typing substrate becomes invisible).
     * <p>
     * Font family of substrate text is the same as main font.
     * 
     * @param style New font style (from java.awt.Font package).
     */
    public void setSubstrateFontStyle(int style) {
        _textEncrypt.setSubstrateFontStyle(style);
    }

    /**
     * Setting substrate text (hint under main text, when you start typing substrate
     * becomes invisible) color of a PasswordLine.
     * 
     * @param foreground Substrate text color as java.awt.Color.
     */
    public void setSubstrateForeground(Color foreground) {
        _textEncrypt.setSubstrateForeground(foreground);
    }

    /**
     * Setting substrate text (hint under main text, when you start typing substrate
     * becomes invisible) color of a PasswordLine in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setSubstrateForeground(int r, int g, int b) {
        setSubstrateForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting substrate text (hint under main text, when you start typing substrate
     * becomes invisible) color of a PasswordLine in byte RGBA format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setSubstratetForeground(int r, int g, int b, int a) {
        setSubstrateForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Setting substrate text (hint under main text, when you start typing substrate
     * becomes invisible)color of a PasswordLine in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setSubstrateForeground(float r, float g, float b) {
        setSubstrateForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting substrate text (hint under main text, when you start typing substrate
     * becomes invisible)color of a PasswordLine in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setSubstrateForeground(float r, float g, float b, float a) {
        setSubstrateForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Getting current substrate text (hint under main text, when you start typing
     * substrate becomes invisible) color.
     * 
     * @return Text color as java.awt.Color.
     */
    public Color getSubstrateForeground() {
        return _textEncrypt.getSubstrateForeground();
    }

    /**
     * Getting substrate text (hint under main text, when you start typing substrate
     * becomes invisible).
     * 
     * @return Substrate text.
     */
    public String getSubstrateText() {
        return _textEncrypt.getSubstrateText();
    }
}
