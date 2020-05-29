package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.KeyCode;

import java.awt.*;
import java.util.List;

/**
 * ButtonToggle is the basic implementation of a user interface button with the
 * ability to be enabled or be disabled.
 * <p>
 * Contains text.
 * <p>
 * Supports all events except drag and drop.
 */
public class ButtonToggle extends Prototype {
    private static int count = 0;
    private TextLine _textObject;
    /**
     * Event that is invoked when a button toggles.
     */
    public EventMouseMethodState eventToggle = new EventMouseMethodState();

    /**
     * Disposing ButtonToggle resources if the item was removed.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void release() {
        eventToggle.clear();
    }

    /**
     * Default ButtonToggle constructor. Text is empty.
     */
    public ButtonToggle() {
        setItemName("ButtonToggle_" + count);
        count++;
        _textObject = new TextLine();
        setStyle(DefaultsService.getDefaultStyle(ButtonToggle.class));

        // event
        eventToggle.add((sender, args) -> {
            _toggled = !_toggled;
            setToggled(_toggled);
        });
        eventKeyPress.add(this::onKeyPress);
        eventMouseClick.add((sender, args) -> {
            if (eventToggle != null)
                eventToggle.execute(sender, args); // remember
        });
    }

    /**
     * Constructs a ButtonToggle with the specified text.
     * 
     * @param text Button text as java.lang.String.
     */
    public ButtonToggle(String text) {
        this();
        setText(text);
    }

    private boolean _toggled = false;

    /**
     * Returns True if this button is toggled otherwise returns False.
     * 
     * @return True: this item is toggled. False: this item is untoggled.
     */
    public boolean isToggled() {
        return _toggled;
    }

    /**
     * Toggles this button between on or off.
     * 
     * @param value True: if you want this item to be toggled. False: if you want
     *              this item to be untoggled.
     */
    public void setToggled(boolean value) {
        _toggled = value;
        if (value == true)
            setState(ItemStateType.TOGGLED);
        else
            setState(ItemStateType.BASE);
    }

    private void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (args.key == KeyCode.ENTER && eventMouseClick != null) {
            eventMouseClick.execute(this, new MouseArgs());
        }
    }

    /**
     * Setting alignment of a ButtonToggle text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Setting alignment of a ButtonToggle text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as List of
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Setting indents for the text to offset text relative to ButtonToggle.
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        _textObject.setMargin(margin);
    }

    /**
     * Setting indents for the text to offset text relative to ButtonToggle.
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
     * Getting the current text of the ButtonToggle.
     * 
     * @return Text as java.lang.String.
     */
    public String getText() {
        return _textObject.getItemText();
    }

    /**
     * Getting the text width (useful when you need resize ButtonToggle by text
     * width).
     * 
     * @return Text width.
     */
    public int getTextWidth() {
        return _textObject.getWidth();
    }

    /**
     * Getting the text height (useful when you need resize ButtonToggle by text
     * height).
     * 
     * @return Text height.
     */
    public int getTextHeight() {
        return _textObject.getHeight();
    }

    /**
     * Setting text color of a ButtonToggle.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        _textObject.setForeground(color);
    }

    /**
     * Setting text color of a ButtonToggle in byte RGB format.
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
     * Setting text color of a ButtonToggle in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        _textObject.setForeground(r, g, b);
    }

    /**
     * Setting text color of a ButtonToggle in float RGBA format.
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
     * Initializing text in the ButtonToggle.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        addItem(_textObject);
    }

    /**
     * Setting style of the ButtonToggle.
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
    }
}