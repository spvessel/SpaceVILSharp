package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.awt.*;
import java.util.List;

/**
 * SpinItem is designed as a user interface element that can increase and
 * decrease the value by a specific step.
 * <p>
 * Contains increment value button, decrement value button, text field.
 */
public class SpinItem extends Prototype {
    private static int count = 0;
    private HorizontalStack _horzStack; // = new HorizontalStack();
    private VerticalStack _vertStack; // = new VerticalStack();
    private TextEditRestricted _textInput; // = new TextEditRestricted();

    /**
     * Increment value button.
     */
    public ButtonCore upButton; // = new ButtonCore();

    /**
     * Decrement value button.
     */
    public ButtonCore downButton; // = new ButtonCore();

    private CustomShape _upArrow;
    private CustomShape _downArrow;

    /**
     * Default SpinItem constructor.
     */
    public SpinItem() {
        setItemName("SpinItem_" + count);
        count++;

        _horzStack = new HorizontalStack();
        _vertStack = new VerticalStack();
        _textInput = new TextEditRestricted();
        upButton = new ButtonCore();
        downButton = new ButtonCore();
        _upArrow = new CustomShape();
        _downArrow = new CustomShape();
        

        _horzStack.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        _textInput.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        
        upButton.isFocusable = false;
        upButton.eventMouseClick.add(this::onUpClick);
        
        downButton.isFocusable = false;
        downButton.eventMouseClick.add(this::onDownClick);
        
        eventScrollUp.add(this::onUpClick);
        eventScrollDown.add(this::onDownClick);
        
        setStyle(DefaultsService.getDefaultStyle(SpinItem.class));
    }

    @Override
    public void setFocus() {
        _textInput.setFocus();
    }

//    @Override
//    protected void setFocused(boolean value) {
//        _textInput.setFocused(value);
//    }

    private void onUpClick(Object sender, MouseArgs args) {
        _textInput.increaseValue();
    }

    private void onDownClick(Object sender, MouseArgs args) {
        _textInput.decreaseValue();
    }

    /**
     * Getting current value of SpinItem.
     * 
     * @return Current value of SpinItem.
     */
    public double getValue() {
        return _textInput.getValue();
    }

    /**
     * Setting integer value of SpinItem.
     * 
     * @param value Integer value.
     */
    public void setValue(int value) {
        _textInput.setValue(value);
    }

    /**
     * Setting double floating piont value of SpinItem.
     * 
     * @param value Double floating point value.
     */
    public void setValue(double value) {
        _textInput.setValue(value);
    }

    /**
     * Setting integer parameters of SpinItem.
     * 
     * @param currentValue SpinItem current value.
     * @param minValue     Minimum value limit.
     * @param maxValue     Maximum value limit
     * @param step         Step of increment and decrement.
     */
    public void setParameters(int currentValue, int minValue, int maxValue, int step) {
        _textInput.setParameters(currentValue, minValue, maxValue, step);
    }

    /**
     * Setting double floating piont parameters of SpinItem.
     * 
     * @param currentValue SpinItem current value.
     * @param minValue     Minimum value limit.
     * @param maxValue     Maximum value limit
     * @param step         Step of increment and decrement.
     */
    public void setParameters(double currentValue, double minValue, double maxValue, double step) {
        _textInput.setParameters(currentValue, minValue, maxValue, step);
    }

    /**
     * Setting accuracy (decimal places) of SpinItem.
     * 
     * @param accuracy Accuracy value.
     */
    public void setAccuracy(int accuracy) {
        _textInput.setAccuracy(accuracy);
    }

    /**
     * Initializing all elements in the SpinItem.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        addItem(_horzStack);
        _horzStack.addItems(_textInput, _vertStack);
        _vertStack.addItems(upButton, downButton);
        upButton.addItem(_upArrow);
        downButton.addItem(_downArrow);
    }

    /**
     * Setting style of the SpinItem.
     * <p>
     * Inner styles: "buttonsarea", "uparrow", "downarrow", "textedit".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null) {
            return;
        }
        super.setStyle(style);
        
        Style innerStyle = style.getInnerStyle("buttonsarea");
        if (innerStyle != null) {
            _vertStack.setStyle(innerStyle);
        }

        innerStyle = style.getInnerStyle("uparrowbutton");
        if (innerStyle != null) {
            upButton.setStyle(innerStyle);
        }

        innerStyle = style.getInnerStyle("uparrow");
        if (innerStyle != null) {
            _upArrow.setStyle(innerStyle);
        }
        
        innerStyle = style.getInnerStyle("downarrowbutton");
        if (innerStyle != null) {
            downButton.setStyle(innerStyle);
        }
        
        innerStyle = style.getInnerStyle("downarrow");
        if (innerStyle != null) {
            _downArrow.setStyle(innerStyle);
        }

        innerStyle = style.getInnerStyle("textedit");
        if (innerStyle != null) {
            _textInput.setStyle(innerStyle);
        }
    }

    /**
     * Setting alignment of a SpinItem text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textInput.setTextAlignment(alignment);
    }

    /**
     * Setting alignment of a SpinItem text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as List of
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(BaseItemStatics.composeFlags(alignment)); //Arrays.asList(alignment));
    }

    /**
     * Getting alignment of a SpinItem text.
     * 
     * @return Text alignment as List of com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public List<ItemAlignment> getTextAlignment() {
        return _textInput.getTextAlignment();
    }

    /**
     * Setting indents for the text to offset text relative to SpinItem.
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        _textInput.setTextMargin(margin);
    }

    /**
     * Setting indents for the text to offset text relative to SpinItem.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setTextMargin(int left, int top, int right, int bottom) {
        // _textInput.setTextMargin(left, top, right, bottom);
        setTextMargin(new Indents(left, top, right, bottom));
    }

    /**
     * Getting indents of the text.
     * 
     * @return Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getTextMargin() {
        return _textInput.getTextMargin();
    }

    /**
     * Setting background color of a SpinItem shape.
     * 
     * @param color Background color as java.awt.Color.
     */
    @Override
    public void setBackground(Color color) {
        _textInput.setBackground(color);
    }

    /**
     * Setting background color of a SpinItem shape in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    @Override
    public void setBackground(int r, int g, int b) {
        setBackground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting background color of a SpinItem in byte RGBA format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    @Override
    public void setBackground(int r, int g, int b, int a) {
        setBackground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Setting background color of a SpinItem in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    @Override
    public void setBackground(float r, float g, float b) {
        setBackground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting background color of a SpinItem in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    @Override
    public void setBackground(float r, float g, float b, float a) {
        setBackground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Getting background color of a SpinItem.
     * 
     * @return Background color as java.awt.Color.
     */
    @Override
    public Color getBackground() {
        return _textInput.getBackground();
    }

    /**
     * Setting font of the text.
     * 
     * @param font Font as java.awt.Font.
     */
    public void setFont(Font font) {
        _textInput.setFont(font);
    }

    /**
     * Setting font size of the text.
     * 
     * @param size New size of the font.
     */
    public void setFontSize(int size) {
        _textInput.setFontSize(size);
    }

    /**
     * Setting font style of the text.
     * 
     * @param style New font style (from java.awt.Font package).
     */
    public void setFontStyle(int style) {
        _textInput.setFontStyle(style);
    }

    /**
     * Setting new font family of the text.
     * 
     * @param fontFamily New font family name.
     */
    public void setFontFamily(String fontFamily) {
        _textInput.setFontFamily(fontFamily);
    }

    /**
     * Getting the current font of the text.
     * 
     * @return Font as java.awt.Font.
     */
    public Font getFont() {
        return _textInput.getFont();
    }

    /**
     * Setting text color of a SpinItem.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        _textInput.setForeground(color);
    }

    /**
     * Setting text color of a SpinItem in byte RGB format.
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
     * Setting text color of a SpinItem in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        setForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting text color of a SpinItem in float RGBA format.
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
        return _textInput.getForeground();
    }
}
