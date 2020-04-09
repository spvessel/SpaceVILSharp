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

/**
 * FileSystemEntry is a class representing file system entry (file, folder and
 * etc.). Used in com.spvessel.spacevil.OpenEntryDialog entry list.
 * <p>
 * Contains text and icon.
 * <p>
 * Supports all events except drag and drop.
 */
public class FileSystemEntry extends Prototype {
    private static int count = 0;
    private TextLine _textObject;
    private ImageItem _icon;
    private FileSystemEntryType _type;

    /**
     * Getting a type of entry (see
     * com.spvessel.spacevil.Flags.FileSystemEntryType).
     * 
     * @return Type of entry as com.spvessel.spacevil.Flags.FileSystemEntryType.
     */
    public FileSystemEntryType getEntryType() {
        return _type;
    }

    /**
     * Getting image icon.
     * 
     * @return Image icon as com.spvessel.spacevil.ImageItem.
     */
    public ImageItem getIcon() {
        return _icon;
    }

    /**
     * Setting image icon of file system entry. Applys smooth scaling the specified
     * image by new size.
     * 
     * @param icon   Bitmap image as java.awt.image.BufferedImage.
     * @param width  New width of the image.
     * @param height New height of the image.
     */
    public void setIcon(BufferedImage icon, int width, int height) {
        _icon.setSize(width, height);
        _icon.setImage(icon);
        _icon.setVisible(true);
    }

    /**
     * Constructs a FileSystemEntry with specified entry type and name.
     * 
     * @param type Type of entry as com.spvessel.spacevil.Flags.FileSystemEntryType.
     * @param text Text of entry.
     */
    public FileSystemEntry(FileSystemEntryType type, String text) {
        setItemName("FileSystemEntry_" + count);
        count++;
        _type = type;
        _icon = new ImageItem();
        _icon.setVisible(false);

        _textObject = new TextLine();
        setText(text);

        setStyle(DefaultsService.getDefaultStyle(FileSystemEntry.class));
    }

    /**
     * Setting alignment of a FileSystemEntry text. Combines with alignment by
     * vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Setting alignment of a FileSystemEntry text. Combines with alignment by
     * vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as List of
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Setting indents for the text to offset text relative to FileSystemEntry.
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        _textObject.setMargin(margin);
    }

    /**
     * Setting indents for the text to offset text relative to FileSystemEntry.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setTextMargin(int left, int top, int right, int bottom) {
        _textObject.setMargin(left, top, right, bottom);
    }

    /**
     * Getting indents of the text.
     * 
     * @return Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getTextMargin() {
        return _textObject.getMargin();
    }

    /**
     * Setting font of the text.
     * 
     * @param font Font as java.awt.Font.
     */
    public void setFont(Font font) {
        _textObject.setFont(font);
    }

    /**
     * Setting font size of the text.
     * 
     * @param size New size of the font.
     */
    public void setFontSize(int size) {
        _textObject.setFontSize(size);
    }

    /**
     * Setting font style of the text.
     * 
     * @param style New font style (from java.awt.Font package).
     */
    public void setFontStyle(int style) {
        _textObject.setFontStyle(style);
    }

    /**
     * Setting new font family of the text.
     * 
     * @param fontFamily New font family name.
     */
    public void setFontFamily(String fontFamily) {
        _textObject.setFontFamily(fontFamily);
    }

    /**
     * Getting the current font of the text.
     * 
     * @return Font as java.awt.Font.
     */
    public Font getFont() {
        return _textObject.getFont();
    }

    /**
     * Setting the text.
     * 
     * @param text Text as java.lang.String.
     */
    public void setText(String text) {
        _textObject.setItemText(text);
    }

    /**
     * Getting the current text of the FileSystemEntry.
     * 
     * @return Text as java.lang.String.
     */
    public String getText() {
        return _textObject.getItemText();
    }

    /**
     * Getting the text width (useful when you need resize FileSystemEntry by text
     * width).
     * 
     * @return Text width.
     */
    public int getTextWidth() {
        return _textObject.getWidth();
    }

    /**
     * Getting the text height (useful when you need resize FileSystemEntry by text
     * height).
     * 
     * @return Text height.
     */
    public int getTextHeight() {
        return _textObject.getHeight();
    }

    /**
     * Setting text color of a FileSystemEntry.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        _textObject.setForeground(color);
    }

    /**
     * Setting text color of a FileSystemEntry in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        _textObject.setForeground(r, g, b);
    }

    /**
     * Setting background color of an item in byte RGBA format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b, int a) {
        _textObject.setForeground(r, g, b, a);
    }

    /**
     * Setting text color of a FileSystemEntry in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        _textObject.setForeground(r, g, b);
    }

    /**
     * Setting text color of a FileSystemEntry in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b, float a) {
        _textObject.setForeground(r, g, b, a);
    }

    /**
     * Getting current text color.
     * 
     * @return Text color as as java.awt.Color.
     */
    public Color getForeground() {
        return _textObject.getForeground();
    }

    /**
     * Initializing all elements in the FileSystemEntry.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        addItems(_icon, _textObject);
    }

    /**
     * Setting style of the FileSystemEntry.
     * <p>
     * Inner styles: "icon", "text".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style
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
            _textObject.setMargin(innerStyle.margin);
    }
}