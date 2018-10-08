package com.spvessel.Items;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import com.spvessel.Decorations.*;
import com.spvessel.Common.*;
import com.spvessel.Flags.*;

public class ToolTip extends VisualItem {
    private static int count = 0;

    private TextLine _text_object;

    public TextLine getTextLine() {
        return _text_object;
    }

    protected Timer _stop;
    private int _timeout = 300;

    public void setTimeOut(int milliseconds) {
        _timeout = milliseconds;
    }

    public int getTimeOut() {
        return _timeout;
    }

    public ToolTip() {
        setVisible(false);
        _text_object = new TextLine();
        setItemName("ToolTip_" + count);
        count++;

        setStyle(DefaultsService.getDefaultStyle("SpaceVIL.ToolTip"));
    }

    @Override
    public void initElements() {
    }

    public void initTimer(boolean value) {
        if (value) {
            if (_stop != null)
                return;

            _stop = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    visibleSelf();
                }
            };
            _stop.schedule(task, _timeout);
        } else {
            setVisible(false);

            if (_stop == null)
                return;

            _stop.cancel();
            _stop = null;
        }
    }

    private void visibleSelf() {
        setVisible(true);

        _stop.cancel();
        _stop = null;
    }

    public int getTextWidth() {
        return _text_object.getWidth();
    }

    // text init
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    public void setFont(Font font) {
        _text_object.setFont(font);
    }

    public void setFontSize(int size) {
        _text_object.setFontSize(size);
    }

    public void setFontStyle(int style) {
        _text_object.setFontStyle(style);
    }

    public void setFontFamily(String font_family) {
        _text_object.setFontFamily(font_family);
    }

    public Font getFont() {
        return _text_object.getFont();
    }

    public void setText(String text) {
        _text_object.setItemText(text);
    }

    public String getText() {
        return _text_object.getItemText();
    }

    public void setForeground(Color color) {
        _text_object.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        _text_object.setForeground(r, g, b);
    }

    public void setForeground(int r, int g, int b, int a) {
        _text_object.setForeground(r, g, b, a);
    }

    public void setForeground(float r, float g, float b) {
        _text_object.setForeground(r, g, b);
    }

    public void setForeground(float r, float g, float b, float a) {
        _text_object.setForeground(r, g, b, a);
    }

    public Color getForeground() {
        return _text_object.getForeground();
    }

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