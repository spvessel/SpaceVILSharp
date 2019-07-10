package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Core.EventMouseMethodState;
import com.spvessel.spacevil.Core.InterfaceDraggable;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

public class Tab extends Prototype implements InterfaceDraggable {
    static int count = 0;
    Frame view;
    private Label _textLabel;
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
        _textLabel = new Label();
        _textLabel.isHover = false;
        view = new Frame();
        setStyle(DefaultsService.getDefaultStyle(Tab.class));

        // draggable tabs
        setPassEvents(false, InputEventType.MOUSE_DOUBLE_CLICK, InputEventType.MOUSE_PRESS,
                InputEventType.MOUSE_RELEASE);
        isFocusable = false;

        eventMousePress.add(this::onMousePress);
        eventMouseDrag.add(this::onDragging);
    }

    private int _xClick = 0;
    private int _xDiff = 0;
    boolean dragging = false;
    private boolean _isDraggable = true;

    public void setDraggable(boolean value) {
        _isDraggable = value;
    }

    public boolean isDraggable() {
        return _isDraggable;
    }

    private void onMousePress(InterfaceItem sender, MouseArgs args) {
        if (!isDraggable())
            return;
        _xClick = args.position.getX();
        _xDiff = args.position.getX() - getX();
    }

    private void onDragging(InterfaceItem sender, MouseArgs args) {
        if (!isDraggable())
            return;
        if (dragging) {
            Prototype parent = getParent();
            int offset = args.position.getX() - parent.getX() - _xDiff;
            int x = offset + parent.getX();
            if (x <= parent.getX()) {
                x = parent.getX();
            }
            if (x >= parent.getX() + parent.getWidth() - getWidth()) {
                x = parent.getX() + parent.getWidth() - getWidth();
            }
            setX(x);
        } else {
            if (Math.abs(_xClick - args.position.getX()) <= 20)
                return;
            dragging = true;
        }
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

    public EventMouseMethodState eventOnSelect = new EventMouseMethodState();
    public EventCommonMethod eventOnClose = new EventCommonMethod();
    EventCommonMethod eventTabRemove = new EventCommonMethod();

    @Override
    public void release() {
        eventOnSelect.clear();
        eventOnSelect = null;
        eventOnClose.clear();
        eventOnClose = null;
        eventTabRemove.clear();
        eventTabRemove = null;
    }

    public void setTextAlignment(ItemAlignment alignment) {
        _textLabel.setTextAlignment(alignment);
    }

    public void setTextAlignment(ItemAlignment... alignment) {
        _textLabel.setTextAlignment(alignment);
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textLabel.setTextAlignment(alignment);
    }

    public void setTextMargin(Indents margin) {
        _textLabel.setMargin(margin);
    }

    public Indents getTextMargin() {
        return _textLabel.getMargin();
    }

    public void setFont(Font font) {
        _textLabel.setFont(font);
    }

    public void setFontSize(int size) {
        _textLabel.setFontSize(size);
    }

    public void setFontStyle(int style) {
        _textLabel.setFontStyle(style);
    }

    public void setFontFamily(String font_family) {
        _textLabel.setFontFamily(font_family);
    }

    public Font getFont() {
        return _textLabel.getFont();
    }

    public void setText(String text) {
        _textLabel.setText(text);
        updateTabWidth();
    }

    public String getText() {
        return _textLabel.getText();
    }

    public int getTextWidth() {
        return _textLabel.getTextWidth();
    }

    public int getTextHeight() {
        return _textLabel.getTextHeight();
    }

    public void setForeground(Color color) {
        _textLabel.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        _textLabel.setForeground(r, g, b);
    }

    public void setForeground(int r, int g, int b, int a) {
        _textLabel.setForeground(r, g, b, a);
    }

    public void setForeground(float r, float g, float b) {
        _textLabel.setForeground(r, g, b);
    }

    public void setForeground(float r, float g, float b, float a) {
        _textLabel.setForeground(r, g, b, a);
    }

    public Color getForeground() {
        return _textLabel.getForeground();
    }

    private int _labelRightMargin = 0;

    void updateTabWidth() {
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
        _textLabel.setMargin(_textLabel.getMargin().left, _textLabel.getMargin().top, value,
                _textLabel.getMargin().bottom);
    }

    @Override
    public void initElements() {
        super.initElements();
        _close.setVisible(_isClosable);
        addItems(_textLabel, _close);
        eventOnSelect.clear();
        eventOnSelect.add((sender, args) -> {
            if (isToggled())
                return;
            setToggled(true);
        });

        _close.isFocusable = false;
        _close.eventMouseClick.add((sender, args) -> {
            removeTab();
        });
    }

    public void removeTab() {
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
            _textLabel.setStyle(inner_style);
            _labelRightMargin = _textLabel.getMargin().right;
        }

        setForeground(style.foreground);
        setFont(style.font);
        setTextAlignment(style.textAlignment);
    }
}