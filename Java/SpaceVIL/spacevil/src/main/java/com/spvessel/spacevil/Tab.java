package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Core.EventMouseMethodState;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

public class Tab extends Prototype {
    static int count = 0;
    Frame view;
    private Label _text_object;
    private ButtonCore _close;

    public ButtonCore getCloseButton() {
        return _close;
    }

    private boolean _isClosable = false;

    public void setClosable(boolean value) {
        if (_isClosable == value)
            return;
        _isClosable = value;
        _close.setVisible(_isClosable);
        updateTabWidth();
    }

    public boolean isClosable() {
        return _isClosable;
    }

    public Tab(String text) {
        this(text, text);
    }

    public Tab(String text, String name) {
        this();
        setItemName(name);
        setText(text);
    }

    public Tab() {
        super();
        setItemName("Tab_" + count++);
        _close = new ButtonCore();
        _text_object = new Label();
        view = new Frame();
        setStyle(DefaultsService.getDefaultStyle(Tab.class));
    }

    private boolean _toggled;

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

    public EventMouseMethodState eventToggle = new EventMouseMethodState();
    public EventCommonMethod eventOnClose = new EventCommonMethod();
    EventCommonMethod eventTabRemove = new EventCommonMethod();

    @Override
    public void release() {
        eventToggle.clear();
        eventToggle = null;
        eventTabRemove.clear();;
        eventTabRemove = null;
        eventOnClose.clear();;
        eventOnClose = null;
    }

    public void setTextAlignment(ItemAlignment alignment) {
        _text_object.setTextAlignment(alignment);
    }

    public void setTextAlignment(ItemAlignment... alignment) {
        _text_object.setTextAlignment(alignment);
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    public Indents getTextMargin() {
        return _text_object.getMargin();
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
        _text_object.setText(text);
        updateTabWidth();
    }

    public String getText() {
        return _text_object.getText();
    }

    public int getTextWidth() {
        return _text_object.getTextWidth();
    }

    public int getTextHeight() {
        return _text_object.getTextHeight();
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

    private int _labelRightMargin = 0;

    private void updateTabWidth() {
        if (getWidthPolicy() == SizePolicy.FIXED) {
            int w = getPadding().left + getTextWidth() + getPadding().right;
            if (_isClosable) {
                w += getSpacing().horizontal + _close.getWidth();
                applyRightTextMargin(getSpacing().horizontal + _close.getWidth());
            } else {
                applyRightTextMargin(_labelRightMargin);
            }
            setWidth(w);
        } else {
            if (_isClosable) {
                applyRightTextMargin(getSpacing().horizontal + _close.getWidth());
            } else {
                applyRightTextMargin(_labelRightMargin);
            }
        }
    }

    private void applyRightTextMargin(int value) {
        _text_object.setMargin(_text_object.getMargin().left, _text_object.getMargin().top, value,
                _text_object.getMargin().bottom);
    }

    @Override
    public void initElements() {
        super.initElements();
        _close.setVisible(_isClosable);
        addItems(_text_object, _close);
        eventToggle.clear();
        eventToggle.add((sender, args) -> {
            if (isToggled())
                return;
            setToggled(true);
        });

        _close.isFocusable = false;
        _close.eventMouseClick.add((sender, args) -> {
            RemoveTab();
        });
    }

    public void RemoveTab() {
        eventOnClose.execute();
        eventTabRemove.execute();
    }

    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("closebutton");
        if (inner_style != null)
            _close.setStyle(inner_style);
        inner_style = style.getInnerStyle("view");
        if (inner_style != null)
            view.setStyle(inner_style);
        inner_style = style.getInnerStyle("text");
        if (inner_style != null) {
            _text_object.setStyle(inner_style);
            _labelRightMargin = _text_object.getMargin().right;
        }

        setForeground(style.foreground);
        setFont(style.font);
        setTextAlignment(style.textAlignment);
    }
}