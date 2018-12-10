package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.awt.*;
import java.util.List;

public class Label extends Prototype {
    private static int count = 0;
    private TextLine _text_object;

    /**
     * Constructs a Label
     */
    public Label() {
        setItemName("Label_" + count);
        count++;
        _text_object = new TextLine();
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.Label"));
        setStyle(DefaultsService.getDefaultStyle(Label.class));
        isFocusable = false;
    }

    /**
     * Constructs a Label with text
     */
    public Label(String text) {
        this();
        setText(text);
    }

    // text init
    /**
     * Text alignment in the Label
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _text_object.setTextAlignment(alignment);
    }
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    /**
     * Text margin in the Label
     */
    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    /**
     * Text font parameters in the Label
     */
    public void setFont(Font font) {
        _text_object.setFont(font);
    }
    public void setFontSize(int size) {
        _text_object.setFontSize(size);
    }
    public void setFontStyle(int style) {
        _text_object.setFontStyle(style);
    }
    public void setFontFamily(String font_family) {
        _text_object.setFontFamily(font_family);
    }
    public Font getFont() {
        return _text_object.getFont();
    }

    /**
     * Set text in the Label
     */
    public void setText(String text) {
        _text_object.setItemText(text);
    }
    public String getText() {
        return _text_object.getItemText();
    }

    /**
     * Text color in the Label
     */
    public void setForeground(Color color) {
        _text_object.setForeground(color);
    }
    public void setForeground(int r, int g, int b) {
        _text_object.setForeground(r, g, b);
    }
    public void setForeground(int r, int g, int b, int a) {
        _text_object.setForeground(r, g, b, a);
    }
    public void setForeground(float r, float g, float b) {
        _text_object.setForeground(r, g, b);
    }
    public void setForeground(float r, float g, float b, float a) {
        _text_object.setForeground(r, g, b, a);
    }
    public Color getForeground() {
        return _text_object.getForeground();
    }

    /**
     * Text width in the Label
     */
    public int getTextWidth() {
        return _text_object.getWidth();
    }

    /**
     * Text height in the Label
     */
    public int getTextHeight() {
        return _text_object.getHeight();
    }

    /**
     * Initialization and adding of all elements in the Label
     */
    @Override
    public void initElements() {
        addItem(_text_object);
    }

    /**
     * Set style of the Label
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        setForeground(style.foreground);
        setFont(style.font);
        setTextAlignment(style.textAlignment);
    }
}