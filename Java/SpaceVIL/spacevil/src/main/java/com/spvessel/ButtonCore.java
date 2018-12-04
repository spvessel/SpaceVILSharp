package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.InterfaceItem;
import com.spvessel.Core.InterfaceKeyMethodState;
import com.spvessel.Core.KeyArgs;
import com.spvessel.Core.MouseArgs;
import com.spvessel.Decorations.Indents;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.KeyCode;

import java.awt.*;
import java.util.List;

public class ButtonCore extends Prototype {
    private static int count = 0;
    private TextLine _text_object;

    /**
     * Constructs a ButtonCore
     */
    public ButtonCore() {
        setItemName("ButtonCore_" + count);
        count++;
        _text_object = new TextLine();
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
        _text_object.setTextAlignment(alignment);
    }
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    /**
     * Text margin in the ButtonCore
     */
    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    /**
     * Text font parameters in the ButtonCore
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

    /**
     * Initialization and adding of all elements in the ButtonCore
     */
    @Override
    public void initElements() {
        addItem(_text_object);
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