package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.awt.*;
import java.util.List;

/**
 * LoadingScreen is designed to show progress the execution of any long time
 * task.
 * <p>
 * Contains image and text.
 * <p>
 * Supports all events except drag and drop.
 * <p>
 * By default ProgressBar cannot get focus.
 */
public class ProgressBar extends Prototype {
    private static int count = 0;
    private TextLine _textObject;

    /**
     * Setting the text that represents the progress of the unfinished task, visible
     * or invisible.
     * 
     * @param value True: if text should be visible. False: if text should be
     *              invisible.
     */
    public void setValueVisible(boolean value) {
        _textObject.setVisible(value);
    }

    /**
     * Returns True if text that represents the progress of the unfinished task is
     * visible, otherwise returns False.
     * 
     * @return True: if text is visible. False: if text is invisible.
     */
    public boolean isValueVisible() {
        return _textObject.isVisible();
    }

    private Rectangle _rect;
    private int _maxValue = 100;
    private int _minValue = 0;
    private int _currentValue = 0;

    /**
     * Default ProgressBar constructor.
     */
    public ProgressBar() {
        isFocusable = false;
        setItemName("ProgressBar_" + count);
        count++;

        _textObject = new TextLine();
        _textObject.setItemName(getItemName() + "_textObject");
        _textObject.setItemText("0%");
        _rect = new Rectangle();

        setStyle(DefaultsService.getDefaultStyle(ProgressBar.class));
    }

    /**
     * Initializing all elements in the ProgressBar.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        addItems(_rect, _textObject);
    }

    /**
     * Setting the maximum progress value of the unfinished task limit. Progress
     * value cannot be greater than this limit.
     * 
     * @param value Maximum progress value of the unfinished task limit.
     */
    public void setMaxValue(int value) {
        _maxValue = value;
    }

    /**
     * Getting the current maximum progress value of the unfinished task limit.
     * 
     * @return Maximum progress value of the unfinished task limit.
     */
    public int getMaxValue() {
        return _maxValue;
    }

    /**
     * Setting the minimum progress value of the unfinished task limit. Progress
     * value cannot be less than this limit.
     * 
     * @param value Minimum progress value of the unfinished task limit.
     */
    public void setMinValue(int value) {
        _minValue = value;
    }

    /**
     * Getting the current minimum sprogress value of the unfinished task limit.
     * 
     * @return Minimum progress value of the unfinished task limit.
     */
    public int getMinValue() {
        return _minValue;
    }

    /**
     * Setting the current progress value of the unfinished task. If the value is
     * greater/less than the maximum/minimum progress value, then the progress value
     * becomes equal to the maximum/minimum value.
     * 
     * @param currentValue Progress value of of the unfinished task.
     */
    public void setCurrentValue(int currentValue) {
        _currentValue = currentValue;
        updateProgressBar();
    }

    /**
     * Getting the progress value of the unfinished task.
     * 
     * @return Progress value of the unfinished task.
     */
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

    /**
     * Setting alignment of a ProgressBar text. Combines with alignment by
     * vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Setting alignment of a ProgressBar text. Combines with alignment by
     * vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as List of
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Setting indents for the text to offset text relative to ProgressBar.
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        _textObject.setMargin(margin);
    }

    /**
     * Setting indents for the text to offset text relative to ProgressBar.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setTextMargin(int left, int top, int right, int bottom) {
        _textObject.setMargin(left, top, right, bottom);
    }

    /**
     * Getting indents of the text.
     * 
     * @return Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getTextMargin() {
        return _textObject.getMargin();
    }

    /**
     * Setting font of the text.
     * 
     * @param font Font as java.awt.Font.
     */
    public void setFont(Font font) {
        _textObject.setFont(font);
    }

    /**
     * Setting font size of the text.
     * 
     * @param size New size of the font.
     */
    public void setFontSize(int size) {
        _textObject.setFontSize(size);
    }

    /**
     * Setting font style of the text.
     * 
     * @param style New font style (from java.awt.Font package).
     */
    public void setFontStyle(int style) {
        _textObject.setFontStyle(style);
    }

    /**
     * Setting new font family of the text.
     * 
     * @param fontFamily New font family name.
     */
    public void setFontFamily(String fontFamily) {
        _textObject.setFontFamily(fontFamily);
    }

    /**
     * Getting the current font of the text.
     * 
     * @return Font as java.awt.Font.
     */
    public Font getFont() {
        return _textObject.getFont();
    }

    /**
     * Getting the text width (useful when you need resize ProgressBar by text
     * width).
     * 
     * @return Text width.
     */
    public int getTextWidth() {
        return _textObject.getWidth();
    }

    /**
     * Getting the text height (useful when you need resize ProgressBar by text
     * height).
     * 
     * @return Text height.
     */
    public int getTextHeight() {
        return _textObject.getHeight();
    }

    /**
     * Setting text color of a ProgressBar.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        _textObject.setForeground(color);
    }

    /**
     * Setting text color of a ProgressBar in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        _textObject.setForeground(r, g, b);
    }

    /**
     * Setting text color of an item in byte RGBA format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b, int a) {
        _textObject.setForeground(r, g, b, a);
    }

    /**
     * Setting text color of a ProgressBar in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        _textObject.setForeground(r, g, b);
    }

    /**
     * Setting text color of a ProgressBar in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b, float a) {
        _textObject.setForeground(r, g, b, a);
    }

    /**
     * Getting current text color.
     * 
     * @return Text color as as java.awt.Color.
     */
    public Color getForeground() {
        return _textObject.getForeground();
    }

    @Override
    protected boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }

    /**
     * Adding item into the container (this).
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     */
    @Override
    public void addItem(IBaseItem item) {
        super.addItem(item);
        updateLayout();
    }

    /**
     * Setting item width. If the value is greater/less than the maximum/minimum
     * value of the width, then the width becomes equal to the maximum/minimum
     * value.
     * 
     * @param width Width of the item.
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateLayout();
    }

    /**
     * Setting X coordinate of the left-top corner of a shape.
     * 
     * @param x X position of the left-top corner.
     */
    @Override
    public void setX(int x) {
        super.setX(x);
        updateLayout();
    }

    void updateLayout() {
        updateProgressBar();
    }

    /**
     * Setting style of the ProgressBar.
     * <p>
     * Inner styles: "progressbar".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
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