package com.spvessel.spacevil;

import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.Common.DefaultsService;

import java.awt.*;
import java.util.*;
import java.util.List;

abstract class TextItem extends Primitive {
    private String _itemText = "";

    private Font _font = DefaultsService.getDefaultFont();

    private static int count = 0;

    TextItem() {
        setItemName("TextItem_" + count);
        setBackground(new Color(0, 0, 0, 0));
        setWidthPolicy(SizePolicy.EXPAND);
        setHeightPolicy(SizePolicy.EXPAND);
        count++;
    }

    TextItem(String text, Font font) {
        this();
        if (text == null) {
            text = "";
        }
        _itemText = text;
        if (font != null) {
            _font = font;
        }
    }

    TextItem(String text, Font font, String name) {
        this(text, font);
        setItemName(name);
    }

    String getItemText() {
        return _itemText;
    }

    void setItemText(String itemText) {
        if (itemText == null) {
            itemText = "";
        }
        if (!_itemText.equals(itemText)) {
            _itemText = itemText;
            updateData();
        }
    }

    Font getFont() {
        return _font;
    }

    void setFont(Font font) {
        if (font == null) {
            return;
        }
        if (!_font.equals(font)) {
            _font = font;
            updateData();
        }
    }

    void setFontSize(int size) {
        if (_font.getSize() != size) {
            _font = GraphicsMathService.changeFontSize(size, _font); //new Font(_font.getFamily(), _font.getStyle(), size);
            updateData();
        }
    }

    void setFontStyle(int style) {
        if (_font.getStyle() != style) {
            _font = GraphicsMathService.changeFontStyle(style, _font); //new Font(_font.getFamily(), style, _font.getSize());
            updateData();
        }
    }

    void setFontFamily(String font_family) {
        if (font_family == null) {
            return;
        }
        if (!_font.getFamily().equals(font_family)) {
            _font = GraphicsMathService.changeFontFamily(font_family, _font); //new Font(font_family, _font.getStyle(), _font.getSize());
            updateData();
        }
    }

    public abstract void updateData();

    private Color _foreground = Color.BLACK; // default

    public Color getForeground() {
        return _foreground;
    }

    public void setForeground(Color foreground) {
        if (foreground != null && !_foreground.equals(foreground)) {
            _foreground = foreground;
        }
    }

    public void setForeground(int r, int g, int b) {
        setForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    public void setForeground(int r, int g, int b, int a) {
        setForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    public void setForeground(float r, float g, float b) {
        setForeground(GraphicsMathService.colorTransform(r, g ,b));
    }

    public void setForeground(float r, float g, float b, float a) {
        setForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    private List<ItemAlignment> _textAlignment = new LinkedList<>();

    public List<ItemAlignment> getTextAlignment() {
        return _textAlignment;
    }

    public void setTextAlignment(ItemAlignment... value) {
        setTextAlignment(Arrays.asList(value));
    }

    public void setTextAlignment(List<ItemAlignment> list) {
        if (list != null && !_textAlignment.equals(list)) {
            _textAlignment = list;
        }
    }
}