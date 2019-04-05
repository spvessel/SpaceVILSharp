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

public class Label extends Prototype implements InterfaceVLayout {
    private static int count = 0;
    private List<TextLine> _text_objects;
    private boolean _init = false;

    /**
     * Constructs a Label
     */
    public Label() {
        setItemName("Label_" + count);
        count++;
        _text_objects = new ArrayList<>();
        _text_objects.add(new TextLine());
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.Label"));
        setStyle(DefaultsService.getDefaultStyle(Label.class));
        isFocusable = false;
    }

    /**
     * Constructs a Label with text
     */
    public Label(String text) {
        this();
        setText(text);
    }

    // text init
    /**
     * Text alignment in the Label
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(Arrays.asList(alignment));
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        for (TextLine tl : _text_objects)
            tl.setTextAlignment(alignment);

        int gyshift = 0;
        if (alignment.contains(ItemAlignment.BOTTOM)) {
            gyshift = -(getTextHeight() - getLineY(1));
        } else if (alignment.contains(ItemAlignment.VCENTER)) {
            gyshift = -((getTextHeight() - getLineY(1)) / 2);
        }
        updateLinesYShifts(gyshift);
    }

    /**
     * Text margin in the Label
     */
    public void setTextMargin(Indents margin) {
        for (TextLine tl : _text_objects)
            tl.setMargin(margin);
    }

    /**
     * Text font parameters in the Label
     */
    public void setFont(Font font) {
        for (TextLine tl : _text_objects)
            tl.setFont(font);
    }

    public void setFontSize(int size) {
        for (TextLine tl : _text_objects)
            tl.setFontSize(size);
    }

    public void setFontStyle(int style) {
        for (TextLine tl : _text_objects)
            tl.setFontStyle(style);
    }

    public void setFontFamily(String font_family) {
        for (TextLine tl : _text_objects)
            tl.setFontFamily(font_family);
    }

    public Font getFont() {
        return _text_objects.get(0).getFont();
    }

    private String preInitText = "";

    /**
     * Set text in the Label
     */
    public void setText(String text) {
        if (text == null)
            text = "";
        if (!_init) {
            preInitText = text;
            return;
        }

        if (_text_objects.size() > 1) {
            while (_text_objects.size() > 1) {
                removeItem(_text_objects.get(1));
                _text_objects.remove(1);
            }
        }

        String[] line = text.split("\n", -1);
        String s;

        s = line[0].replaceAll("\r", "");
        _text_objects.get(0).setItemText(s);

        int inc = 0;
        for (int i = 1; i < line.length; i++) {
            inc++;
            s = line[i].replaceAll("\r", "");

            TextLine te = new TextLine();
            addItem(te);

            te.setItemText(s);

            _text_objects.add(inc, te);
        }

        setForeground(getForeground());
        setTextAlignment(_text_objects.get(0).getTextAlignment());
        setTextMargin(_text_objects.get(0).getMargin());
        setFont(getFont());

        // _text_object.setItemText(text);
    }

    private void updateLinesYShifts(int globalYShift) {
        int inc = 0;
//        int y = _text_objects.get(0).getY();
        for (TextLine tl : _text_objects) {
            tl.setLineYShift(getLineY(inc) + globalYShift);
//            tl.setY(y + getLineY(inc) + globalYShift);
            inc++;
        }
    }

    public String getText() {
        if (!_init)
            return preInitText;

        StringBuilder sb = new StringBuilder();
        if (_text_objects == null)
            return "";
        if (_text_objects.size() == 1) {
            sb.append(_text_objects.get(0).getText());
        } else {
            for (int i = 0; i < _text_objects.size() - 1; i++) {
                sb.append(_text_objects.get(i).getText());
                sb.append("\n");
            }
            sb.append(_text_objects.get(_text_objects.size() - 1).getText());
        }
        return sb.toString();
    }

    /**
     * Text color in the Label
     */
    public void setForeground(Color color) {
        for (TextLine tl : _text_objects)
            tl.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        for (TextLine tl : _text_objects)
            tl.setForeground(r, g, b);
    }

    public void setForeground(int r, int g, int b, int a) {
        for (TextLine tl : _text_objects)
            tl.setForeground(r, g, b, a);
    }

    public void setForeground(float r, float g, float b) {
        for (TextLine tl : _text_objects)
            tl.setForeground(r, g, b);
    }

    public void setForeground(float r, float g, float b, float a) {
        for (TextLine tl : _text_objects)
            tl.setForeground(r, g, b, a);
    }

    public Color getForeground() {
        return _text_objects.get(0).getForeground();
    }

    /**
     * Text width in the Label
     */
    public int getTextWidth() {
        int wdt = _text_objects.get(0).getWidth();
        for (int i = 1; i < _text_objects.size(); i++) {
            int w = _text_objects.get(i).getWidth();
            if (w > wdt)
                wdt = w;
        }
        return wdt;
    }

    /**
     * Text height in the Label
     */
    public int getTextHeight() {
        return getLineY(_text_objects.size());
    }

    private int getLineY(int num) {
        int minLineSpacer = _text_objects.get(0).getFontDims()[0];
        int lineHeight = _text_objects.get(0).getHeight();
        return (lineHeight + minLineSpacer) * num;
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        TextLine txtObj = _text_objects.get(0);
        int _cursorXMax = getWidth() - getPadding().left - getPadding().right - txtObj.getMargin().left
                - txtObj.getMargin().right;

        for (TextLine tl : _text_objects) {
            tl.setAllowWidth(_cursorXMax);
            tl.checkXShift(_cursorXMax); // ???
        }
    }

    /**
     * Initialization and adding of all elements in the Label
     */
    @Override
    public void initElements() {
        for (TextLine tl : _text_objects)
            addItem(tl);
        _init = true;
        if (!preInitText.equals("")) {
            setText(preInitText);
        }
    }

    /**
     * Set style of the Label
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        // if (style.font != null)
        setFont(style.font);
        // if (style.foreground != null)
        setForeground(style.foreground);
        // if (style.textAlignment != null)
        setTextAlignment(style.textAlignment);
    }

    @Override
    public void updateLayout() {

    }
}