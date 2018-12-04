package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.*;
import com.spvessel.Decorations.Indents;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.ItemStateType;
import com.spvessel.Flags.KeyCode;

import java.awt.*;
import java.util.List;

public class ButtonToggle extends Prototype {
    private static int count = 0;
    private TextLine _text_object;
    public EventMouseMethodState eventToggle = new EventMouseMethodState();

    /**
     * Constructs a ButtonToggle
     */
    public ButtonToggle() {
        setItemName("ButtonToggle_" + count);
        count++;
        _text_object = new TextLine();
        eventKeyPress.add(this::onKeyPress);
        InterfaceMouseMethodState btn_click = (sender, args) -> {
            if (eventToggle != null)
                eventToggle.execute(sender, args); // remember
        };
        eventMouseClick.add(btn_click);

        InterfaceMouseMethodState toggle = (sender, args) -> {
            _toggled = !_toggled;
            setToggled(_toggled);
        };
        eventToggle.add(toggle);
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.ButtonToggle"));
        setStyle(DefaultsService.getDefaultStyle(ButtonToggle.class));
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