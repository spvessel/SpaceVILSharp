package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.KeyCode;

import java.awt.*;
import java.util.List;

public class ButtonToggle extends Prototype {
    private static int count = 0;
    private TextLine _text_object;
    public EventMouseMethodState eventToggle = new EventMouseMethodState();

    @Override
    public void release() {
        eventToggle.clear();
    }

    /**
     * Constructs a ButtonToggle
     */
    public ButtonToggle() {
        setItemName("ButtonToggle_" + count);
        count++;
        _text_object = new TextLine();
        setStyle(DefaultsService.getDefaultStyle(ButtonToggle.class));

        // event
        eventToggle.add((sender, args) -> {
            _toggled = !_toggled;
            setToggled(_toggled);
        });
        eventKeyPress.add(this::onKeyPress);
        eventMouseClick.add((sender, args) -> {
            if (eventToggle != null)
                eventToggle.execute(sender, args); // remember
        });
    }

    /**
     * Constructs a ButtonToggle with text
     */
    public ButtonToggle(String text) {
        this();
        setText(text);
    }

    // private for class
    private boolean _toggled = false;

    /**
     * Is ButtonToggle toggled (boolean)
     */
    public boolean isToggled() {
        return _toggled;
    }

    public void setToggled(boolean value) {
        _toggled = value;
        if (value == true)
            setState(ItemStateType.TOGGLED);
        else
            setState(ItemStateType.BASE);
    }

    void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (args.key == KeyCode.ENTER && eventMouseClick != null) {
            eventMouseClick.execute(this, new MouseArgs());
        }
    }

    // text init
    /**
     * Text alignment in the ButtonToggle
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _text_object.setTextAlignment(alignment);
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    /**
     * Text margin in the ButtonToggle
     */
    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    public Indents getTextMargin() {
        return _text_object.getMargin();
    }
    /**
     * Text font parameters in the ButtonToggle
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
     * Set text in the ButtonToggle
     */
    public void setText(String text) {
        _text_object.setItemText(text);
    }

    public String getText() {
        return _text_object.getItemText();
    }

    public int getTextWidth() {
        return _text_object.getWidth();
    }

    public int getTextHeight() {
        return _text_object.getHeight();
    }

    /**
     * Text color in the ButtonToggle
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
     * Initialization and adding of all elements in the ButtonToggle
     */
    @Override
    public void initElements() {
        addItem(_text_object);
    }

    /**
     * Set style of the ButtonToggle
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