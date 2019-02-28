package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.List;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.FileSystemEntryType;
import com.spvessel.spacevil.Flags.ItemAlignment;

public class FileSystemEntry extends Prototype {
    private static int count = 0;
    private TextLine _text_object;
    private ImageItem _icon;
    private FileSystemEntryType _type;

    public FileSystemEntryType getEntryType() {
        return _type;
    }

    public ImageItem getIcon() {
        return _icon;
    }

    public void setIcon(BufferedImage icon, int width, int height) {
        _icon.setSize(width, height);
        _icon.setImage(icon);
        _icon.setVisible(true);
    }

    public void setIcon(String url, int width, int height) {
        _icon.setSize(width, height);
        _icon.setImageUrl(url);
        _icon.setVisible(true);
    }

    public FileSystemEntry(FileSystemEntryType type, String text) {
        setItemName("FileSystemEntry_" + count);
        count++;
        _type = type;
        _icon = new ImageItem();
        _icon.setVisible(false);

        _text_object = new TextLine();
        setText(text);

        setStyle(DefaultsService.getDefaultStyle(FileSystemEntry.class));
    }

    // text init
    /**
     * Text alignment in the FileSystemEntry
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _text_object.setTextAlignment(alignment);
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    /**
     * Text margin in the FileSystemEntry
     */
    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    /**
     * Text font parameters in the FileSystemEntry
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
     * Set text in the FileSystemEntry
     */
    public void setText(String text) {
        _text_object.setItemText(text);
    }

    public String getText() {
        return _text_object.getItemText();
    }

    /**
     * Text color in the FileSystemEntry
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
     * Initialization and adding of all elements in the FileSystemEntry
     */
    @Override
    public void initElements() {
        addItems(_icon, _text_object);
    }

    /**
     * Set style of the FileSystemEntry
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        setForeground(style.foreground);
        setFont(style.font);
        setTextAlignment(style.textAlignment);

        Style innerStyle = style.getInnerStyle("icon");
        if (innerStyle != null)
            _icon.setStyle(innerStyle);
        innerStyle = style.getInnerStyle("text");
        if (innerStyle != null)
            _text_object.setMargin(innerStyle.margin);
    }
}