package com.spvessel.Items;

import com.spvessel.Cores.*;
import java.awt.*;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.List;
import com.spvessel.Decorations.*;
import com.spvessel.Common.*;
import com.spvessel.Flags.*;
import com.spvessel.Flags.SizePolicy;

public class ToolTip extends VisualItem {
    class TooltipVisibility extends Thread {
        private int _ms = 500;

        TooltipVisibility() {
        }

        TooltipVisibility(int ms) {
            _ms = ms;
        }

        boolean isCanceled = false;

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            while (!isCanceled) {
                if (System.currentTimeMillis() - startTime >= _ms)
                    break;
            }
            // System.out.println(isCanceled);
            if (!isCanceled) {
                visibleSelf();
            }
        }
    }

    private static int count = 0;

    private TextLine _text_object;

    public TextLine getTextLine() {
        return _text_object;
    }

    protected TooltipVisibility _stop;
    private int _timeout = 500;

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

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.ToolTip"));
        setStyle(DefaultsService.getDefaultStyle(com.spvessel.Items.ToolTip.class));
    }

    @Override
    public void initElements() {
    }

    public void initTimer(boolean value) {
        if (value) {
            if (_stop != null)
                return;

            _stop = new TooltipVisibility(_timeout);
            _stop.start();

        } else {
            setVisible(false);
            if (_stop == null)
                return;
            _stop.isCanceled = true;
            _stop = null;
        }
    }

    private void visibleSelf() {
        if (_stop == null)
            return;
        // System.out.println("visible");
        setVisible(true);
        _stop.isCanceled = true;
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