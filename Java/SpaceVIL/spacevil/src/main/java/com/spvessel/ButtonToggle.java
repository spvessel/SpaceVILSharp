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

public class ButtonToggle extends VisualItem {
    private static int count = 0;
    private TextLine _text_object;
    public EventMouseMethodState eventToggle = new EventMouseMethodState();

    public ButtonToggle() {
        setItemName("ButtonToggle_" + count);
        count++;
        _text_object = new TextLine();
        InterfaceKeyMethodState key_press = (sender, args) -> onKeyPress(sender, args);
        eventKeyPress.add(key_press);
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

    public ButtonToggle(String text) {
        this();
        setText(text);
    }

    // private for class
    private boolean _toggled = false;

    public boolean isToggled() {
        return _toggled;
    }

    public void setToggled(boolean value) {
        _toggled = value;
        if (value == true)
            _state = ItemStateType.TOGGLED;
        else
            _state = ItemStateType.BASE;
        updateState();
    }

    protected void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (args.key == KeyCode.ENTER && eventMouseClick != null) {
            eventMouseClick.execute(this, new MouseArgs());
        }
    }

    // text init
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

    public void setText(String text) {
        _text_object.setItemText(text);
    }

    public String getText() {
        return _text_object.getItemText();
    }

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
        addItem(_text_object);
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