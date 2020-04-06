package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;

import java.awt.*;
import java.util.List;

public class RadioButton extends Prototype {
    class CustomIndicator extends Indicator {
        @Override
        protected boolean getHoverVerification(float xpos, float ypos) {
            return false;
        }
    }

    private static int count = 0;
    private TextLine _text_object;
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
        _text_object = new TextLine();
        _text_object.setItemName(getItemName() + "_text_object");

        // indicator
        _indicator = new CustomIndicator();
        _indicator.isFocusable = false;

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.RadioButton"));
        setStyle(DefaultsService.getDefaultStyle(RadioButton.class));
    }

    /**
     * Constructs a RadioButton with text
     */
    public RadioButton(String text) {
        this();
        setText(text);
    }

    private void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (eventMouseClick != null && (args.key == KeyCode.ENTER || args.key == KeyCode.SPACE)) {
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

    // text init
    /**
     * Text alignment in the RadioButton
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _text_object.setTextAlignment(alignment);
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    /**
     * Text margin in the RadioButton
     */
    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    public Indents getTextMargin() {
        return _text_object.getMargin();
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
        _text_object.setItemText(text);
    }

    public String getText() {
        return _text_object.getText();
    }

    public int getTextWidth() {
        return _text_object.getWidth();
    }

    public int getTextHeight() {
        return _text_object.getHeight();
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
            if (_indicator.getIndicatorMarker().isToggled())
                return;
            _indicator.getIndicatorMarker().setToggled(!_indicator.getIndicatorMarker().isToggled());
            if (_indicator.getIndicatorMarker().isToggled())
                uncheckOthers(sender);
        };
        eventMouseClick.add(btn_click);

        // adding
        addItems(_indicator, _text_object);
    }

    /**
     * Is RadioButton checked (boolean)
     */
    public boolean isChecked() {
        return _indicator.getIndicatorMarker().isToggled();
    }

    public void setChecked(boolean value) {
        _indicator.getIndicatorMarker().setToggled(value);
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
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        setForeground(style.foreground);
        setFont(style.font);

        Style innerStyle = style.getInnerStyle("indicator");
        if (innerStyle != null) {
            _indicator.setStyle(innerStyle);
        }
        innerStyle = style.getInnerStyle("text");
        if (innerStyle != null) {
            _text_object.setStyle(innerStyle);
        }
    }
}