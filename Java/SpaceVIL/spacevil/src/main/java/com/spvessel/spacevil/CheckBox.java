package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;

import java.awt.*;
import java.util.List;

public class CheckBox extends Prototype {
    class CustomIndicator extends Indicator {
        @Override
        public boolean getHoverVerification(float xpos, float ypos) {
            return false;
        }
    }

    static int count = 0;
    private TextLine _text_object;
    private CustomIndicator _indicator;

    /**
     * Returns indicator from the CheckBox for styling
     */
    public Indicator getIndicator() {
        return _indicator;
    }

    /**
     * Constructs a CheckBox
     */
    public CheckBox() {
        setItemName("CheckBox_" + count);
        count++;
        eventKeyPress.add(this::onKeyPress);

        // text
        _text_object = new TextLine();
        _text_object.setItemName(getItemName() + "_text_object");

        // indicator
        _indicator = new CustomIndicator();
        _indicator.isFocusable = false;

        setStyle(DefaultsService.getDefaultStyle(CheckBox.class));
    }

    /**
     * Constructs a CheckBox with text
     */
    public CheckBox(String text) {
        this();
        setText(text);
    }

    void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (eventMouseClick != null && (args.key == KeyCode.ENTER || args.key == KeyCode.SPACE)) {
            eventMouseClick.execute(this, new MouseArgs());
        }
    }

    /**
     * Is mouse hovered on the CheckBox
     */
    @Override
    public void setMouseHover(boolean value) {
        super.setMouseHover(value);
        _indicator.getIndicatorMarker().setMouseHover(value);
        updateState();
    }

    // text init
    /**
     * Text alignment in the CheckBox
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _text_object.setTextAlignment(alignment);
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    /**
     * Text margin in the CheckBox
     */
    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    /**
     * Text font parameters in the CheckBox
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
     * Set text in the CheckBox
     */
    public void setText(String text) {
        _text_object.setItemText(text);
    }

    public String getText() {
        return _text_object.getText();
    }

    /**
     * Text color in the CheckBox
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
     * Initialization and adding of all elements in the CheckBox
     */
    @Override
    public void initElements() {
        // events
        _indicator.getIndicatorMarker().eventToggle = null;
        InterfaceMouseMethodState btn_click = (sender, args) -> _indicator.getIndicatorMarker()
                .setToggled(!_indicator.getIndicatorMarker().isToggled());
        eventMouseClick.add(btn_click);

        // adding
        addItems(_indicator, _text_object);
    }

    /**
     * Is CheckButton checked (boolean)
     */
    public boolean isChecked() {
        return _indicator.getIndicatorMarker().isToggled();
    }

    public void setChecked(boolean value) {
        _indicator.getIndicatorMarker().setToggled(value);
    }

    // style
    /**
     * Set style of the CheckBox
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        setForeground(style.foreground);
        setFont(style.font);

        Style inner_style = style.getInnerStyle("indicator");
        if (inner_style != null) {
            _indicator.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("textline");
        if (inner_style != null) {
            _text_object.setStyle(inner_style);
        }
    }
}