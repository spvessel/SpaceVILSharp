package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceVLayout;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Label is is the basic implementation of a user interface non-editable text
 * item. Label has multiline text support.
 * <p>
 * Supports all events except drag and drop.
 * <p>
 * By default, Label is stretched to all available space in the container.
 */
public class Label extends Prototype implements InterfaceVLayout {
    private static int count = 0;
    private List<TextLine> _textObjects;
    private boolean _init = false;
    /**
     * Property to enable or disable mouse events (hover, click, press, scroll).
     * <p>
     * True: Label can receive mouse events. False: cannot receive mouse events.
     * <p>
     * Default: True.
     */
    public boolean isHover = true;

    @Override
    protected boolean getHoverVerification(float xpos, float ypos) {
        if (isHover) {
            return super.getHoverVerification(xpos, ypos);
        }
        return false;
    }

    /**
     * Default Label constructor.
     */
    public Label() {
        setItemName("Label_" + count);
        count++;
        _textObjects = new ArrayList<>();
        _textObjects.add(new TextLine());
        setStyle(DefaultsService.getDefaultStyle(Label.class));
        isFocusable = false;
    }

    /**
     * Constructs a Label with text.
     * 
     * @param text Label text.
     */
    public Label(String text) {
        this();
        setText(text);
    }

    /**
     * Constructs a Label with text and with the ability to enable or disable mouse
     * events.
     * 
     * @param text  Label text.
     * @param hover True: Label can receive mouse events. False: cannot receive
     *              mouse events.
     */
    public Label(String text, boolean hover) {
        this(text);
        isHover = hover;
    }

    /**
     * Setting alignment of a Label text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(Arrays.asList(alignment));
    }

    /**
     * Setting alignment of a Label text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as List of
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        for (TextLine tl : _textObjects) {
            tl.setTextAlignment(alignment);
        }
        updateLayout();
    }

    /**
     * Getting alignment of a Label text.
     * 
     * @return Text alignment as List of com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public List<ItemAlignment> getTextAlignment() {
        return _textObjects.get(0).getTextAlignment();
    }

    /**
     * Setting indents for the text to offset text relative to Label.
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        for (TextLine tl : _textObjects) {
            tl.setMargin(margin);
        }
        updateLayout();
    }

    /**
     * Setting indents for the text to offset text relative to Label.
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
        return _textObjects.get(0).getMargin();
    }

    /**
     * Setting font of the text.
     * 
     * @param font Font as java.awt.Font.
     */
    public void setFont(Font font) {
        for (TextLine tl : _textObjects) {
            tl.setFont(font);
        }
        updateLayout();
    }

    /**
     * Setting font size of the text.
     * 
     * @param size New size of the font.
     */
    public void setFontSize(int size) {
        for (TextLine tl : _textObjects) {
            tl.setFontSize(size);
        }
        updateLayout();
    }

    /**
     * Setting font style of the text.
     * 
     * @param style New font style (from java.awt.Font package).
     */
    public void setFontStyle(int style) {
        for (TextLine tl : _textObjects) {
            tl.setFontStyle(style);
        }
    }

    /**
     * Setting new font family of the text.
     * 
     * @param fontFamily New font family name.
     */
    public void setFontFamily(String font_family) {
        for (TextLine tl : _textObjects) {
            tl.setFontFamily(font_family);
        }
    }

    /**
     * Getting the current font of the text.
     * 
     * @return Font as java.awt.Font.
     */
    public Font getFont() {
        return _textObjects.get(0).getFont();
    }

    /**
     * Setting the text.
     * 
     * @param text Text as java.lang.String.
     */
    public void setText(String text) {
        if (text == null) {
            text = "";
        }

        if (_textObjects.size() > 1) {
            while (_textObjects.size() > 1) {
                if (_init) {
                    removeItem(_textObjects.get(1));
                }
                _textObjects.remove(1);
            }
        }

        String[] line = text.split("\n", -1);
        String s;

        s = line[0].replaceAll("\r", "");
        _textObjects.get(0).setItemText(s);

        int inc = 0;
        for (int i = 1; i < line.length; i++) {
            inc++;
            s = line[i].replaceAll("\r", "");

            TextLine te = new TextLine();
            if (_init) {
                addItem(te);
            }

            te.setItemText(s);

            _textObjects.add(inc, te);
        }

        setForeground(getForeground());
        setTextAlignment(_textObjects.get(0).getTextAlignment());
        setTextMargin(_textObjects.get(0).getMargin());
        setFont(getFont());
    }

    /**
     * Getting the current text of the Label.
     * 
     * @return Text as java.lang.String.
     */
    public String getText() {
        StringBuilder sb = new StringBuilder();
        if (_textObjects == null) {
            return "";
        }
        if (_textObjects.size() == 1) {
            sb.append(_textObjects.get(0).getText());
        } else {
            for (int i = 0; i < _textObjects.size() - 1; i++) {
                sb.append(_textObjects.get(i).getText());
                sb.append("\n");
            }
            sb.append(_textObjects.get(_textObjects.size() - 1).getText());
        }
        return sb.toString();
    }

    /**
     * Setting text color of a Label.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        for (TextLine tl : _textObjects) {
            tl.setForeground(color);
        }
    }

    /**
     * Setting text color of a Label in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        for (TextLine tl : _textObjects) {
            tl.setForeground(r, g, b);
        }
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
        for (TextLine tl : _textObjects) {
            tl.setForeground(r, g, b, a);
        }
    }

    /**
     * Setting text color of a Label in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        for (TextLine tl : _textObjects) {
            tl.setForeground(r, g, b);
        }
    }

    /**
     * Setting text color of a Label in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b, float a) {
        for (TextLine tl : _textObjects) {
            tl.setForeground(r, g, b, a);
        }
    }

    /**
     * Getting current text color.
     * 
     * @return Text color as as java.awt.Color.
     */
    public Color getForeground() {
        return _textObjects.get(0).getForeground();
    }

    /**
     * Getting the text width (useful when you need resize Label by text width).
     * 
     * @return Text width.
     */
    public int getTextWidth() {
        int wdt = _textObjects.get(0).getWidth();
        for (int i = 1; i < _textObjects.size(); i++) {
            int w = _textObjects.get(i).getWidth();
            if (w > wdt) {
                wdt = w;
            }
        }
        return wdt;
    }

    /**
     * Getting the text height (useful when you need resize Label by text height).
     * 
     * @return Text height.
     */
    public int getTextHeight() {
        return getLineY(_textObjects.size());
    }

    private int getLineY(int num) {
        int minLineSpacer = _textObjects.get(0).getFontDims().lineSpacer; // [0];
        int lineHeight = _textObjects.get(0).getHeight();
        return (lineHeight + minLineSpacer) * num;
    }

    /**
     * Setting Label width. If the value is greater/less than the maximum/minimum
     * value of the width, then the width becomes equal to the maximum/minimum
     * value.
     * 
     * @param width Width of the Label.
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        TextLine txtObj = _textObjects.get(0);
        int _cursorXMax = getWidth() - getPadding().left - getPadding().right - txtObj.getMargin().left
                - txtObj.getMargin().right;

        for (TextLine tl : _textObjects) {
            tl.setAllowWidth(_cursorXMax);
            tl.checkXShift(_cursorXMax); // ???
        }
    }

    /**
     * Initializing all elements in the Label.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        for (TextLine tl : _textObjects) {
            addItem(tl);
        }
        _init = true;
    }

    /**
     * Setting style of the Label.
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null) {
            return;
        }
        super.setStyle(style);
        setFont(style.font);
        setForeground(style.foreground);
        setTextAlignment(style.textAlignment);
    }

    /**
     * Updating all children positions (implementation of
     * com.spvessel.spacevil.Core.InterfaceVLayout).
     */
    @Override
    public void updateLayout() {
        // updateLinesYShifts
        List<ItemAlignment> alignment = getTextAlignment();
        int globalYShift = 0;
        if (alignment.contains(ItemAlignment.BOTTOM)) {
            globalYShift = -(getTextHeight() - getLineY(1));
        } else if (alignment.contains(ItemAlignment.VCENTER)) {
            globalYShift = -((getTextHeight() - getLineY(1)) / 2);
        }
        int inc = 0;
        for (TextLine tl : _textObjects) {
            tl.setLineYShift(getLineY(inc) + globalYShift);
            inc++;
        }
    }
}