package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.*;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * TextEdit is a basic implementation of a user interface editable text field
 * that contains only one line.
 * <p>
 * Supports all events except drag and drop.
 */
public class TextEdit extends Prototype {
    static int count = 0;
    private TextEditStorage _textObject;

    /**
     * Default TextEdit constructor.
     */
    public TextEdit() {
        _textObject = new TextEditStorage();

        setItemName("TextEdit_" + count);
        count++;
        setStyle(DefaultsService.getDefaultStyle(TextEdit.class));
    }

    /**
     * Constructs TextEdit eith the given text.
     * 
     * @param text Text for TextEdit.
     */
    public TextEdit(String text) {
        this();
        setText(text);
    }

    @Override
    public void setFocus() {
        _textObject.setFocus();
    }

    /**
     * Setting alignment of thr text. Combines with alignment by vertically (TOP,
     * VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(BaseItemStatics.composeFlags(alignment)); //Arrays.asList(alignment));
    }

    /**
     * Setting alignment of the text. Combines with alignment by vertically (TOP,
     * VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as List of
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Getting alignment of a TextEdit text.
     * 
     * @return Text alignment as List of com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public List<ItemAlignment> getTextAlignment() {
        return _textObject.getTextAlignment();
    }

    /**
     * Setting indents for the text to offset text relative to TextEdit.
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        _textObject.setTextMargin(margin);
    }

    /**
     * Setting indents for the text to offset text relative to TextEdit.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setTextMargin(int left, int top, int right, int bottom) {
        setTextMargin(new Indents(left, top, right, bottom));
    }

    /**
     * Getting indents of the text.
     * 
     * @return Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getTextMargin() {
        return _textObject.getTextMargin();
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
    public final void setText(String text) {
        _textObject.setText(text);
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
     * Getting the current text of the TextEdit.
     * 
     * @return Text as java.lang.String.
     */
    public String getText() {
        return _textObject.getText();
    }

    /**
     * Setting text color of a TextEdit.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        _textObject.setForeground(color);
    }

    /**
     * Setting text color of a TextEdit in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        setForeground(GraphicsMathService.colorTransform(r, g, b));
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
        setForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Setting text color of a TextEdit in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        setForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting text color of a TextEdit in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b, float a) {
        setForeground(GraphicsMathService.colorTransform(r, g, b, a));
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
     * Returns True if TextEdit is editable otherwise returns False.
     * 
     * @return True: if TextEdit is editable. True: if TextEdit is non-editable.
     */
    public boolean isEditable() {
        return _textObject.isEditable();
    }

    /**
     * Setting TextEdit text field be editable or be non-editable.
     * 
     * @param value True: if you want TextEdit be editable. True: if you want
     *              TextEdit be non-editable.
     */
    public void setEditable(boolean value) {
        _textObject.setEditable(value);
    }

    /**
     * Initializing all elements in the TextEdit.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        addItem(_textObject);
    }

    /**
     * Getting the text width.
     * 
     * @return Text width.
     */
    public int getTextWidth() {
        return _textObject.getWidth();
    }

    /**
     * Getting the text height.
     * 
     * @return Text height.
     */
    public int getTextHeight() {
        return _textObject.getHeight();
    }

    /**
     * Getting the current selected text.
     * 
     * @return Current selected text.
     */
    public String getSelectedText() {
        return _textObject.getSelectedText();
    }

    /**
     * Paste the specified text at the current position of the text cursor (or
     * replace the specified text at the current starting position of the selected
     * text).
     * 
     * @param pasteStr Text to insert.
     */
    public void pasteText(String pasteStr) {
        _textObject.pasteText(pasteStr);
    }

    /**
     * Cuts and returns the current selected text.
     * 
     * @return Selected text.
     */
    public String cutText() {
        return _textObject.cutText();
    }

    /**
     * Deletes all text in the TextEdit.
     */
    @Override
    public void clear() {
        _textObject.clear();
    }

    /**
     * Setting style of the TextEdit.
     * <p>
     * Inner styles: "text".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        super.setStyle(style);
        Style innerStyle = style.getInnerStyle("text");
        if (innerStyle != null) {
            _textObject.setStyle(innerStyle);
        }
    }

    // boolean isBeginning() {
    //     return _textObject.isBeginning();
    // }

    /**
     * Selecting entire text of the TextEdit.
     */
    public final void selectAll() {
        _textObject.selectAll();
    }

     /**
     * Method for undo last change.
     */
     public void undo() {
         _textObject.undo();
     }

     /**
     * Method for redo last undo action.
     */
     public void redo() {
         _textObject.redo();
     }

    /**
     * Setting the substrate text (hint under main text, when you start typing
     * substrate becomes invisible).
     * 
     * @param substrateText Substrate text.
     */
    public void setSubstrateText(String substrateText) {
        _textObject.setSubstrateText(substrateText);
    }

    /**
     * Setting font size of the substrate text (hint under main text, when you start
     * typing substrate becomes invisible).
     * <p>
     * Font family of substrate text is the same as main font.
     * 
     * @param size New size of the font.
     */
    public void setSubstrateFontSize(int size) {
        _textObject.setSubstrateFontSize(size);
    }

    /**
     * Setting font style of the substrate text (hint under main text, when you
     * start typing substrate becomes invisible).
     * <p>
     * Font family of substrate text is the same as main font.
     * 
     * @param style New font style (from java.awt.Font package).
     */
    public void setSubstrateFontStyle(int style) {
        _textObject.setSubstrateFontStyle(style);
    }

    /**
     * Setting substrate text (hint under main text, when you start typing substrate
     * becomes invisible) color of a TextEdit.
     * 
     * @param foreground Substrate text color as java.awt.Color.
     */
    public void setSubstrateForeground(Color foreground) {
        _textObject.setSubstrateForeground(foreground);
    }

    /**
     * Setting substrate text (hint under main text, when you start typing substrate
     * becomes invisible) color of a TextEdit in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setSubstrateForeground(int r, int g, int b) {
        setSubstrateForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting substrate text (hint under main text, when you start typing substrate
     * becomes invisible) color of a TextEdit in byte RGBA format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setSubstratetForeground(int r, int g, int b, int a) {
        setSubstrateForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Setting substrate text (hint under main text, when you start typing substrate
     * becomes invisible)color of a TextEdit in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setSubstrateForeground(float r, float g, float b) {
        setSubstrateForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting substrate text (hint under main text, when you start typing substrate
     * becomes invisible)color of a TextEdit in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setSubstrateForeground(float r, float g, float b, float a) {
        setSubstrateForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Getting current substrate text (hint under main text, when you start typing
     * substrate becomes invisible) color.
     * 
     * @return Text color as java.awt.Color.
     */
    public Color getSubstrateForeground() {
        return _textObject.getSubstrateForeground();
    }

    /**
     * Getting substrate text (hint under main text, when you start typing substrate
     * becomes invisible).
     * 
     * @return Substrate text.
     */
    public String getSubstrateText() {
        return _textObject.getSubstrateText();
    }

    /**
     * Adding the specified text to the end of the existing text.
     * 
     * @param text Text for adding.
     */
    public void appendText(String text) {
        _textObject.appendText(text);
    }
}
