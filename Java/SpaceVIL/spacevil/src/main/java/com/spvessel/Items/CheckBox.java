package com.spvessel.Items;

import java.awt.*;
import java.util.List;
import com.spvessel.Decorations.*;
import com.spvessel.Common.*;
import com.spvessel.Cores.*;
import com.spvessel.Flags.*;
import com.spvessel.Flags.SizePolicy;

public class CheckBox extends VisualItem implements InterfaceHLayout {
    class CustomIndicator extends Indicator {
        @Override
        public boolean getHoverVerification(float xpos, float ypos) {
            return false;
        }
    }

    static int count = 0;
    private TextLine _text_object;
    private CustomIndicator _indicator;

    public Indicator getIndicator() {
        return _indicator;
    }

    public CheckBox() {
        setItemName("CheckBox_" + count);
        count++;
        InterfaceKeyMethodState key_press = (sender, args) -> onKeyPress(sender, args);
        eventKeyPress.add(key_press);

        // text
        _text_object = new TextLine();
        _text_object.setItemName(getItemName() + "_text_object");

        // indicator
        _indicator = new CustomIndicator();

        setStyle(DefaultsService.getDefaultStyle("SpaceVIL.CheckBox"));
    }

    public CheckBox(String text) {
        this();
        setText(text);
    }

    protected void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (args.scancode == 0x1C && eventMouseClick != null)
            eventMouseClick.execute(this, new MouseArgs());
    }

    @Override
    public void setMouseHover(boolean value) {
        super.setMouseHover(value);
        _indicator.getIndicatorMarker().setMouseHover(value);
        updateState();
    }

    // Layout rules
    @Override
    public void addItem(BaseItem item) {
        super.addItem(item);
        updateLayout();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateLayout();
    }

    @Override
    public void setX(int _x) {
        super.setX(_x);
        updateLayout();
    }

    public void updateLayout() {
        int offset = 0;
        int startX = getX() + getPadding().left;

        for (BaseItem child : getItems()) {
            child.setX(startX + offset + child.getMargin().left);
            if (child.getWidthPolicy() == SizePolicy.EXPAND) {
                child.setWidth(getWidth() - offset /*- child.getMargin().left - child.getMargin().right*/);
            }
            offset += child.getWidth() + getSpacing().horizontal;
        }
    }

    // text init
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
        // events
        _indicator.getIndicatorMarker().eventToggle = null;
        InterfaceMouseMethodState btn_click = (sender, args) -> _indicator.getIndicatorMarker()
                .setToggled(!_indicator.getIndicatorMarker().getToggled());
        eventMouseClick.add(btn_click);

        // adding
        addItem(_indicator);
        addItem(_text_object);
    }

    // style
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