package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceMouseMethodState;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PopUpMessage extends Prototype {
    private static int count = 0;
    private TextLine _text_object;
    private ButtonCore _btn_close;
    Timer _stop;
    private int _timeout = 2000;
    boolean _holded = false;

    /**
     * PopUpMessage timeout in milliseconds
     */
    public void setTimeOut(int milliseconds) {
        _timeout = milliseconds;
    }
    public int getTimeOut() {
        return _timeout;
    }

    /**
     * Constructs a PopUpMessage with message and parent window (handler)
     */
    public PopUpMessage(String message, WindowLayout handler) {
        setItemName("PopUpMessage_" + count);
        count++;

        _btn_close = new ButtonCore();
        _btn_close.setItemName("ClosePopUp");
        _text_object = new TextLine();
        _text_object.setItemText(message);

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.PopUpMessage"));
        setStyle(DefaultsService.getDefaultStyle(PopUpMessage.class));
        handler.getWindow().addItem(this);
        setPassEvents(false);
    }

    // text init
    /**
     * Text alignment in the PopUpMessage
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _text_object.setTextAlignment(alignment);
    }
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    /**
     * Text margin in the PopUpMessage
     */
    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    /**
     * Text font parameters in the PopUpMessage
     */
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

    /**
     * Set text in the PopUpMessage
     */
    public void setText(String text) {
        _text_object.setItemText(text);
    }

    /**
     * Get the PopUpMessage text
     */
    public String getText() {
        return _text_object.getItemText();
    }

    /**
     * Text color in the PopUpMessage
     */
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

    /**
     * Initialization and adding of all elements in the PopUpMessage
     */
    @Override
    public void initElements() {
        InterfaceMouseMethodState click = (sender, args) -> removeSelf();
        _btn_close.eventMouseClick.add(click);
        // adding
        addItems(_text_object, _btn_close);
    }

    /**
     * Show the PopUpMessage
     */
    public void show() {
        initTimer();
    }

    private void initTimer() {
        if (_stop != null)
            return;

        _stop = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                removeSelf();
            }
        };
        _stop.schedule(task, _timeout);
    }

    private void removeSelf() {
        if (_stop != null) {
            _stop.cancel();
            _stop = null;
        }
        getParent().removeItem(this);
    }

    void holdSelf(boolean value) {
        _holded = value;
        if (_stop != null) {
            _stop.cancel();
            _stop = null;
        }
    }

    /**
     * Set style of the PopUpMessage
     */
    // style
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        setForeground(style.foreground);
        setFont(style.font);
        setTextAlignment(style.textAlignment);

        Style inner_style = style.getInnerStyle("closebutton");
        if (inner_style != null) {
            _btn_close.setStyle(inner_style);
        }
    }
}