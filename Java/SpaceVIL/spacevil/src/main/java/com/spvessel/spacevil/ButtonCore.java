package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.KeyArgs;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;

import java.awt.*;
import java.util.List;

public class ButtonCore extends Prototype {
    private static int count = 0;
    private TextLine _textObject;

    /**
     * Constructs a ButtonCore
     */
    public ButtonCore() {
        setItemName("ButtonCore_" + count);
        count++;
        _textObject = new TextLine();
        eventKeyPress.add(this::onKeyPress);
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.ButtonCore"));
        setStyle(DefaultsService.getDefaultStyle(ButtonCore.class));
    }

    /**
     * Constructs a ButtonCore with text
     */
    public ButtonCore(String text) {
        this();
        setText(text);
    }

    void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (args.key == KeyCode.ENTER && eventMouseClick != null) {
            eventMouseClick.execute(this, new MouseArgs());
        }
    }

    // text init
    /**
     * Text alignment in the ButtonCore
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _textObject.setTextAlignment(alignment);
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Text margin in the ButtonCore
     */
    public void setTextMargin(Indents margin) {
        _textObject.setMargin(margin);
    }

    public void setTextMargin(int left, int top, int right, int bottom) {
        _textObject.setMargin(left, top, right, bottom);
    }

    public Indents getTextMargin() {
        return _textObject.getMargin();
    }

    /**
     * Text font parameters in the ButtonCore
     */
    public void setFont(Font font) {
        _textObject.setFont(font);
    }

    public void setFontSize(int size) {
        _textObject.setFontSize(size);
    }

    public void setFontStyle(int style) {
        _textObject.setFontStyle(style);
    }

    public void setFontFamily(String font_family) {
        _textObject.setFontFamily(font_family);
    }

    public Font getFont() {
        return _textObject.getFont();
    }

    /**
     * Set text in the ButtonCore
     */
    public void setText(String text) {
        _textObject.setItemText(text);
    }

    public String getText() {
        return _textObject.getItemText();
    }

    public int getTextWidth() {
        return _textObject.getWidth();
    }

    public int getTextHeight() {
        return _textObject.getHeight();
    }

    /**
     * Text color in the ButtonCore
     */
    public void setForeground(Color color) {
        _textObject.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        _textObject.setForeground(r, g, b);
    }

    public void setForeground(int r, int g, int b, int a) {
        _textObject.setForeground(r, g, b, a);
    }

    public void setForeground(float r, float g, float b) {
        _textObject.setForeground(r, g, b);
    }

    public void setForeground(float r, float g, float b, float a) {
        _textObject.setForeground(r, g, b, a);
    }

    public Color getForeground() {
        return _textObject.getForeground();
    }

    /**
     * Initialization and adding of all elements in the ButtonCore
     */
    @Override
    public void initElements() {
        addItem(_textObject);
    }

    /**
     * Set style of the ButtonCore
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