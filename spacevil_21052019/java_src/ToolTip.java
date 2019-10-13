package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.Timer;

public final class ToolTip extends Prototype {

    private static int count = 0;

    private TextLine _text_object;

    public TextLine getTextLine() {
        return _text_object;
    }

    private Timer _stop = null;
    private int _timeout = 500;

    public void setTimeOut(int milliseconds) {
        _timeout = milliseconds;
    }

    public int getTimeOut() {
        return _timeout;
    }

    // private static ToolTip _instance = null;

    protected ToolTip() {
        setVisible(false);
        _text_object = new TextLine();
        setItemName("ToolTip_" + count);
        count++;
        isFocusable = false;

        setStyle(DefaultsService.getDefaultStyle(ToolTip.class));
    }

    // public static ToolTip getInstance() {
    // if (_instance == null)
    // _instance = new ToolTip();
    // return _instance;
    // }

    @Override
    public void initElements() {
        // addItem(_text_object);
    }

    private Lock locker = new ReentrantLock();

    public void initTimer(boolean value) {
        locker.lock();
        try {
            if (value) {
                if (_stop != null)
                    return;
                ActionListener taskPerformer = new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        visibleSelf();
                    }
                };
                _stop = new Timer(_timeout, taskPerformer);
                _stop.start();
            } else {
                setVisible(value);
                if (_stop == null)
                    return;
                _stop.stop();
                _stop = null;
            }
        } finally {
            locker.unlock();
        }
    }

    private void visibleSelf() {
        locker.lock();
        try {
            if (_stop == null)
                return;
            setVisible(true);
            _stop.stop();
            _stop = null;
        } finally {
            locker.unlock();
        }
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