package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.List;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;

public class SelectionItem extends Prototype {
    private static int count = 0;

    ImageItem _image_icon;
    TextLine _text_object;

    public SelectionItem() {
        setItemName("SelectionItem_" + count);
        count++;

        _image_icon = new ImageItem();
        _text_object = new TextLine();

        setStyle(DefaultsService.getDefaultStyle(SelectionItem.class));
    }

    public SelectionItem(BufferedImage icon, String text) {
        this();
        setText(text);
        _image_icon.setImage(icon);
    }

    public void setIcon(BufferedImage icon)
    {
        _image_icon.setImage(icon);
    }

    public void setTextAlignment(ItemAlignment... alignment) {
        _text_object.setTextAlignment(alignment);
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

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
     * Set text in the ButtonCore
     */
    public void setText(String text) {
        _text_object.setItemText(text);
    }

    public String getText() {
        return _text_object.getItemText();
    }

    /**
     * Text color in the ButtonCore
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
    
    @Override
    public void initElements() {
        addItems(_image_icon, _text_object);
    }

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