package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;

import java.awt.*;
import java.util.List;

/**
 * RadioButton is the basic implementation of a user interface radio button with
 * the ability to be checked (others radio button becomes unchecked) or be
 * unchecked. Contains text and indicator.
 * <p>
 * Supports all events except drag and drop.
 */
public class RadioButton extends Prototype {
    class CustomIndicator extends Indicator {
        @Override
        protected boolean getHoverVerification(float xpos, float ypos) {
            return false;
        }
    }

    private static int count = 0;
    private TextLine _textObject;
    private CustomIndicator _indicator;

    /**
     * Getting indicator item of the .
     * 
     * @return Indicator as com.spvessel.spacevil.Indicator.
     */
    public Indicator getIndicator() {
        return _indicator;
    }

    /**
     * Default  constructor. Text is empty.
     */
    public RadioButton() {
        setItemName("RadioButton_" + count);
        count++;
        eventKeyPress.add(this::onKeyPress);

        // text
        _textObject = new TextLine();
        _textObject.setItemName(getItemName() + "_textObject");

        // indicator
        _indicator = new CustomIndicator();
        _indicator.isFocusable = false;

        setStyle(DefaultsService.getDefaultStyle(RadioButton.class));
    }

    /**
     * Constructs a  with the specified text.
     * 
     * @param text  text as java.lang.String.
     */
    public RadioButton(String text) {
        this();
        setText(text);
    }

    private void onKeyPress(IItem sender, KeyArgs args) {
        if (eventMouseClick != null && (args.key == KeyCode.Enter || args.key == KeyCode.Space)) {
            eventMouseClick.execute(this, new MouseArgs());
        }
    }

    /**
     * Overrided Prototype.setMouseHover(bool) method.
     * <p>
     * Setting this item hovered (mouse cursor located within item's shape).
     * 
     * @param value True: if you want this item be hovered. False: if you want this
     *              item be not hovered.
     */
    @Override
    public void setMouseHover(boolean value) {
        super.setMouseHover(value);
        _indicator.getIndicatorMarker().setMouseHover(value);
        updateState();
    }

    /**
     * Setting alignment of a  text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Setting alignment of a  text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as List of
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Setting indents for the text to offset text relative to .
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        _textObject.setMargin(margin);
    }

    /**
     * Setting indents for the text to offset text relative to .
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
     * Setting the text.
     * 
     * @param text Text as java.lang.String.
     */
    public void setText(String text) {
        _textObject.setItemText(text);
    }

    /**
     * Setting the text.
     * 
     * @param text Text as java.lang.Object.
     */
    public void setText(Object text) {
        setText(text.toString());
    }

    /**
     * Getting the current text of the .
     * 
     * @return Text as java.lang.String.
     */
    public String getText() {
        return _textObject.getItemText();
    }

    /**
     * Getting the text width (useful when you need resize  by text width).
     * 
     * @return Text width.
     */
    public int getTextWidth() {
        return _textObject.getWidth();
    }

    /**
     * Getting the text height (useful when you need resize  by text
     * height).
     * 
     * @return Text height.
     */
    public int getTextHeight() {
        return _textObject.getHeight();
    }

    /**
     * Setting text color of a .
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        _textObject.setForeground(color);
    }

    /**
     * Setting text color of a  in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        _textObject.setForeground(r, g, b);
    }

    /**
     * Setting background color of an item in byte RGBA format.
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
     * Setting text color of a  in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        _textObject.setForeground(r, g, b);
    }

    /**
     * Setting text color of a  in float RGBA format.
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

    /**
     * Initializing all elements in the .
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        // events
        _indicator.getIndicatorMarker().eventToggle = null;
        IMouseMethodState btn_click = (sender, args) -> {
            if (_indicator.getIndicatorMarker().isToggled())
                return;
            _indicator.getIndicatorMarker().setToggled(!_indicator.getIndicatorMarker().isToggled());
            if (_indicator.getIndicatorMarker().isToggled())
                uncheckOthers(sender);
        };
        eventMouseClick.add(btn_click);

        // adding
        addItems(_indicator, _textObject);
    }

    /**
     * Returns True if  is checked otherwise returns False.
     * 
     * @return True:  is checked. False:  is unchecked.
     */
    public boolean isChecked() {
        return _indicator.getIndicatorMarker().isToggled();
    }

    public void setChecked(boolean value) {
        _indicator.getIndicatorMarker().setToggled(value);
    }
/**
     * Setting  checked or unchecked.
     * 
     * @param value True: if you want  to be checked. False: if you want
     *               to be unchecked.
     */
    private void uncheckOthers(IItem sender) {
        List<IBaseItem> items = getParent().getItems();
        for (IBaseItem item : items) {
            if (item instanceof RadioButton && !item.equals(this)) {
                ((RadioButton) item).getIndicator().getIndicatorMarker().setToggled(false);
            }
        }
    }

    /**
     * Setting style of the .
     * <p>
     * Inner styles: "indicator", "text".
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

        Style innerStyle = style.getInnerStyle("indicator");
        if (innerStyle != null) {
            _indicator.setStyle(innerStyle);
        }
        innerStyle = style.getInnerStyle("text");
        if (innerStyle != null) {
            _textObject.setStyle(innerStyle);
        }
    }
}