package com.spvessel.spacevil;

import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.Common.DefaultsService;

import java.awt.*;
import java.util.*;
import java.util.List;

abstract class TextItem extends Primitive {
//    private List<Float> _alphas;
//    private List<Float> _interCoords;
//    private float[] _coordinates;
//    private float[] _colors;
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
        if (text == null)
            text = "";
        _itemText = text;
        if (font != null)
            _font = font;
    }

    TextItem(String text, Font font, String name) {
        this(text, font);
        setItemName(name);
    }

//    void setRealCoords(List<Float> realCoords) {
//        _coordinates = toGL(realCoords);
//    }
//
//    void setAlphas(List<Float> alphas) {
//        _alphas = alphas;
//        //setColor(alphas);
//    }

    String getItemText() {
        return _itemText;
    }

    void setItemText(String itemText) {
        if (itemText == null)
            itemText = "";
        if (!_itemText.equals(itemText)) {
            _itemText = itemText;
            updateData();
        }
    }

    Font getFont() {
        // if(_font == null)
        // _font = DefaultsService.GetDefaultFont();
        return _font;
    }

    void setFont(Font font) {
        if (font == null)
            return;
        if (!_font.equals(font)) {
        // if (_font != font)
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
        if (font_family == null)
            return;
        if (!_font.getFamily().equals(font_family)) {
            _font = GraphicsMathService.changeFontFamily(font_family, _font); //new Font(font_family, _font.getStyle(), _font.getSize());
            updateData();
        }
    }

    public abstract void updateData();

    //protected abstract void updateCoords();

//    float[] getCoordinates() {
//        return _coordinates;
//    }

    // public float[] getColors() {
    //     return _colors;
    // }

//    private float[] toGL(List<Float> coord) {
//        float[] outCoord = new float[coord.size()];
//        float f;
//        float x0 = getX();
//        float y0 = getY();
//        float windowH = getHandler().getHeight() / 2f;
//        float windowW = getHandler().getWidth() / 2f;
//
//        for (int i = 0; i < coord.size(); i += 3) {
//            f = coord.get(i);
//            f += x0;
//            f = f / windowW - 1.0f;
//            outCoord[i] = f;
//
//            f = coord.get(i + 1);
//            f += y0;
//            f = -(f / windowH - 1.0f);
//            outCoord[i + 1] = f;
//
//            f = coord.get(i + 2);
//            outCoord[i + 2] = f;
//        }
//
//        return outCoord;
//    }

    private Color _foreground = Color.BLACK; // default

    public Color getForeground() {
        return _foreground;
    }

    public void setForeground(Color foreground) {
        if (foreground != null && !_foreground.equals(foreground)) {
            _foreground = foreground;
            //setColor(_alphas); // _colorFlag = true;
        }
    }

    public void setForeground(int r, int g, int b) {
//        if (r < 0)
//            r = Math.abs(r);
//        if (r > 255)
//            r = 255;
//        if (g < 0)
//            g = Math.abs(g);
//        if (g > 255)
//            g = 255;
//        if (b < 0)
//            b = Math.abs(b);
//        if (b > 255)
//            b = 255;

        setForeground(GraphicsMathService.colorTransform(r, g, b)); //new Color(r, g, b, 255));
    }

    public void setForeground(int r, int g, int b, int a) {
//        if (r < 0)
//            r = Math.abs(r);
//        if (r > 255)
//            r = 255;
//        if (g < 0)
//            g = Math.abs(g);
//        if (g > 255)
//            g = 255;
//        if (b < 0)
//            b = Math.abs(b);
//        if (b > 255)
//            b = 255;
        setForeground(GraphicsMathService.colorTransform(r, g, b, a)); //new Color(r, g, b, a));
    }

    public void setForeground(float r, float g, float b) {
//        if (r < 0)
//            r = Math.abs(r);
//        if (r > 1.0f)
//            r = 1.0f;
//        if (g < 0)
//            g = Math.abs(g);
//        if (g > 1.0f)
//            g = 1.0f;
//        if (b < 0)
//            b = Math.abs(b);
//        if (b > 1.0f)
//            b = 1.0f;
        setForeground(GraphicsMathService.colorTransform(r, g ,b)); //new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), 255));
    }

    public void setForeground(float r, float g, float b, float a) {
//        if (r < 0)
//            r = Math.abs(r);
//        if (r > 1.0f)
//            r = 1.0f;
//        if (g < 0)
//            g = Math.abs(g);
//        if (g > 1.0f)
//            g = 1.0f;
//        if (b < 0)
//            b = Math.abs(b);
//        if (b > 1.0f)
//            b = 1.0f;
        setForeground(GraphicsMathService.colorTransform(r, g, b, a)); //new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f)));
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
            //updateCoords(); // _coordsFlag = true;
        }
    }

//    public float[] shape() {
//        return getCoordinates();
//    }
}