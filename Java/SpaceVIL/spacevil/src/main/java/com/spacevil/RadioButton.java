package com.spacevil;

import com.spacevil.Core.*;

import java.awt.*;
import java.util.List;

public class RadioButton extends Prototype implements InterfaceHLayout {
    class CustomIndicator extends Indicator {
        @Override
        boolean getHoverVerification(float xpos, float ypos) {
            return false;
        }
    }

    private static int count = 0;
    private Label _text_object;
    private CustomIndicator _indicator;

    /**
     * @return RadioButton's indicator
     */
    public Indicator getIndicator() {
        return _indicator;
    }

    /**
     * Constructs a RadioButton
     */
    public RadioButton() {
        setItemName("RadioButton_" + count);
        count++;
        eventKeyPress.add(this::onKeyPress);

        // text
        _text_object = new Label();
        _text_object.isFocusable = false;
        _text_object.setItemName(getItemName() + "_text_object");

        // indicator
        _indicator = new CustomIndicator();
        _indicator.isFocusable = false;

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.RadioButton"));
        setStyle(com.spacevil.Common.DefaultsService.getDefaultStyle(RadioButton.class));
    }

    /**
     * Constructs a RadioButton with text
     */
    public RadioButton(String text) {
        this();
        setText(text);
    }

    private void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (eventMouseClick != null && (args.key == com.spacevil.Flags.KeyCode.ENTER || args.key == com.spacevil.Flags.KeyCode.SPACE)) {
            eventMouseClick.execute(this, new MouseArgs());
        }
    }

    /**
     * Set is mouse hover on the RadioButton
     */
    @Override
    public void setMouseHover(boolean value) {
        super.setMouseHover(value);
        _indicator.getIndicatorMarker().setMouseHover(value);
        updateState();
    }

    // Layout rules
    /**
     * Add item to the RadioButton
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        super.addItem(item);
        updateLayout();
    }

    /**
     * Set width of the RadioButton
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateLayout();
    }

    /**
     * Set X position of the RadioButton
     */
    @Override
    public void setX(int _x) {
        super.setX(_x);
        updateLayout();
    }

    /**
     * Update RadioButton's states (size and position)
     */
    public void updateLayout() {
        int offset = 0;
        int startX = getX() + getPadding().left;

        for (InterfaceBaseItem child : getItems()) {
            child.setX(startX + offset + child.getMargin().left);
            if (child.getWidthPolicy() == com.spacevil.Flags.SizePolicy.EXPAND) {
                child.setWidth(getWidth() - offset /*- child.getMargin().left - child.getMargin().right*/);
            }
            offset += child.getWidth() + getSpacing().horizontal;
        }
    }

    // text init
    /**
     * Text alignment in the RadioButton
     */
    public void setTextAlignment(com.spacevil.Flags.ItemAlignment... alignment) {
        _text_object.setTextAlignment(alignment);
    }
    public void setTextAlignment(List<com.spacevil.Flags.ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    /**
     * Text margin in the RadioButton
     */
    public void setTextMargin(com.spacevil.Decorations.Indents margin) {
        _text_object.setMargin(margin);
    }

    /**
     * Text font parameters in the RadioButton
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
     * Text in the RadioButton
     */
    public void setText(String text) {
        _text_object.setText(text);
    }
    public String getText() {
        return _text_object.getText();
    }

    /**
     * Text color in the RadioButton
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
     * Initialization and adding of all elements in the RadioButton
     */
    @Override
    public void initElements() {
        // events
        _indicator.getIndicatorMarker().eventToggle = null;
        InterfaceMouseMethodState btn_click = (sender, args) -> {
            _indicator.getIndicatorMarker().setToggled(!_indicator.getIndicatorMarker().isToggled());
            if (_indicator.getIndicatorMarker().isToggled())
                uncheckOthers(sender);
        };
        eventMouseClick.add(btn_click);

        // adding
        addItem(_indicator);
        addItem(_text_object);
    }

    private void uncheckOthers(InterfaceItem sender) {
        List<InterfaceBaseItem> items = getParent().getItems();
        for (InterfaceBaseItem item : items) {
            if (item instanceof RadioButton && !item.equals(this)) {
                ((RadioButton) item).getIndicator().getIndicatorMarker().setToggled(false);
            }
        }
    }

    /**
     * Set style of the RadioButton
     */
    // style
    @Override
    public void setStyle(com.spacevil.Decorations.Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        setForeground(style.foreground);
        setFont(style.font);

        com.spacevil.Decorations.Style inner_style = style.getInnerStyle("indicator");
        if (inner_style != null) {
            _indicator.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("textline");
        if (inner_style != null) {
            _text_object.setStyle(inner_style);
        }
    }
}