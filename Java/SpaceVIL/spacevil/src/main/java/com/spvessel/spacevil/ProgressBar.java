package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.awt.*;
import java.util.List;

public class ProgressBar extends Prototype {
    private static int count = 0;
    private TextLine _textObject;

    public void setValueVisible(boolean value) {
        _textObject.setVisible(value);
    }

    public boolean isValueVisible() {
        return _textObject.isVisible();
    }

    private Rectangle _rect;
    private int _maxValue = 100;
    private int _minValue = 0;
    private int _currentValue = 0;

    /**
     * Constructs a ProgressBar
     */
    public ProgressBar() {
        isFocusable = false;
        setItemName("ProgressBar_" + count);
        count++;

        _textObject = new TextLine();
        _textObject.setItemName(getItemName() + "_text_object");
        _textObject.setItemText("0%");
        _rect = new Rectangle();

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.ProgressBar"));
        setStyle(DefaultsService.getDefaultStyle(ProgressBar.class));
    }

    /**
     * Initialization and adding of all elements in the ProgressBar
     */
    @Override
    public void initElements() {
        // text
        addItems(_rect, _textObject);
    }

    /**
     * @param value maximum value of the ProgressBar
     */
    public void setMaxValue(int value) {
        _maxValue = value;
    }

    public int getMaxValue() {
        return _maxValue;
    }

    /**
     * @param value minimum value of the ProgressBar
     */
    public void setMinValue(int value) {
        _minValue = value;
    }

    public int getMinValue() {
        return _minValue;
    }

    /**
     * @param currentValue current value of the ProgressBar
     */
    public void setCurrentValue(int currentValue) {
        _currentValue = currentValue;
        updateProgressBar();
    }

    public int getCurrentValue() {
        return _currentValue;
    }

    private void updateProgressBar() {
        float AllLength = _maxValue - _minValue;
        float DonePercent;
        _currentValue = (_currentValue > _maxValue) ? _maxValue : _currentValue;
        _currentValue = (_currentValue < _minValue) ? _minValue : _currentValue;
        DonePercent = (_currentValue - _minValue) / AllLength;
        String text = Math.round(DonePercent * 100f) + "%";
        _textObject.setItemText(text);
        _rect.setWidth((int) Math.round(getWidth() * DonePercent));
    }

    // text init
    /**
     * Text alignment in the ProgressBar
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _textObject.setTextAlignment(alignment);
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Text margin in the ProgressBar
     */
    public void setTextMargin(Indents margin) {
        _textObject.setMargin(margin);
    }

    /**
     * Text font parameters in the ProgressBar
     */
    public void setFont(Font font) {
        _textObject.setFont(font);
    }

    public void setFontSize(int size) {
        _textObject.setFontSize(size);
    }

    public void setFontStyle(int style) {
        _textObject.setFontStyle(style);
    }

    public void setFontFamily(String font_family) {
        _textObject.setFontFamily(font_family);
    }

    public Font getFont() {
        return _textObject.getFont();
    }

    /**
     * Text color in the ProgressBar
     */
    public void setForeground(Color color) {
        _textObject.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        _textObject.setForeground(r, g, b);
    }

    public void setForeground(int r, int g, int b, int a) {
        _textObject.setForeground(r, g, b, a);
    }

    public void setForeground(float r, float g, float b) {
        _textObject.setForeground(r, g, b);
    }

    public void setForeground(float r, float g, float b, float a) {
        _textObject.setForeground(r, g, b, a);
    }

    public Color getForeground() {
        return _textObject.getForeground();
    }

    @Override
    protected boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }

    // Layout rules

    /**
     * Add item to the ProgressBar
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        super.addItem(item);
        updateLayout();
    }

    /**
     * Set width of the ProgressBar
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateLayout();
    }

    /**
     * Set X position of the ProgressBar left top corner
     */
    @Override
    public void setX(int x) {
        super.setX(x);
        updateLayout();
    }

    /**
     * Update ProgressBar states
     */
    void updateLayout() {
        updateProgressBar();
    }

    /**
     * Set style of the ProgressBar
     */
    // style
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        setForeground(style.foreground);
        setFont(style.font);
        setTextAlignment(style.textAlignment);

        Style inner_style = style.getInnerStyle("progressbar");
        if (inner_style != null) {
            _rect.setStyle(inner_style);
        }
    }
}