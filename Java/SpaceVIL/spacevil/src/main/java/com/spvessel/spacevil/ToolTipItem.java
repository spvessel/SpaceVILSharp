package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceFloating;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.LayoutType;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.Timer;

public final class ToolTipItem extends Prototype implements InterfaceFloating {

    private Label _textObject;

    Label getTextLabel() {
        return _textObject;
    }

    private Timer _stop = null;
    private int _timeout = 500;

    void setTimeOut(int milliseconds) {
        _timeout = milliseconds;
    }

    int getTimeOut() {
        return _timeout;
    }

    // private static ToolTip _instance = null;

    protected ToolTipItem() {
        setVisible(false);
        _textObject = new Label();
        setItemName("ToolTipItem");
        _textObject.setItemName("ToolTipLabel");
        isFocusable = false;
        _queue = new ArrayDeque<>();
        setStyle(DefaultsService.getDefaultStyle(ToolTipItem.class));
    }

    boolean isInit = false;

    @Override
    public void initElements() {
        ItemsLayoutBox.addItem(getHandler(), this, LayoutType.FLOATING);
        addItem(_textObject);
        if (!_queue.isEmpty()) {
            for (InterfaceBaseItem item : _queue) {
                super.addItem(item);
            }
        }
        isInit = true;
    }

    private Queue<InterfaceBaseItem> _queue = null;

    @Override
    public void addItems(InterfaceBaseItem... items) {
        if (isInit)
            return;
        for (InterfaceBaseItem item : items) {
            _queue.add(item);
        }
    }

    private Lock locker = new ReentrantLock();

    void initTimer(boolean value) {
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

    int getTextWidth() {
        return _textObject.getTextWidth();
    }

    int getTextHeight() {
        return _textObject.getTextHeight();
    }

    // text init
    void setTextAlignment(List<ItemAlignment> alignment) {
        _textObject.setTextAlignment(alignment);
    }

    void setTextMargin(Indents margin) {
        _textObject.setMargin(margin);
    }

    void setFont(Font font) {
        _textObject.setFont(font);
    }

    void setFontSize(int size) {
        _textObject.setFontSize(size);
    }

    void setFontStyle(int style) {
        _textObject.setFontStyle(style);
    }

    void setFontFamily(String font_family) {
        _textObject.setFontFamily(font_family);
    }

    Font getFont() {
        return _textObject.getFont();
    }

    void setText(String text) {
        _textObject.setText(text);
    }

    String getText() {
        return _textObject.getText();
    }

    void setForeground(Color color) {
        _textObject.setForeground(color);
    }

    void setForeground(int r, int g, int b) {
        _textObject.setForeground(r, g, b);
    }

    void setForeground(int r, int g, int b, int a) {
        _textObject.setForeground(r, g, b, a);
    }

    void setForeground(float r, float g, float b) {
        _textObject.setForeground(r, g, b);
    }

    void setForeground(float r, float g, float b, float a) {
        _textObject.setForeground(r, g, b, a);
    }

    Color getForeground() {
        return _textObject.getForeground();
    }

    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style innerStyle = style.getInnerStyle("text");
        if (innerStyle != null)
            _textObject.setStyle(innerStyle);

        setForeground(style.foreground);
        setFont(style.font);
        setTextAlignment(style.textAlignment);
    }

    @Override
    public void show() {
    }

    @Override
    public void show(InterfaceItem sender, MouseArgs args) {
    }

    @Override
    public void hide() {
    }

    @Override
    public void hide(MouseArgs args) {
    }

    @Override
    public boolean isOutsideClickClosable() {
        return false;
    }

    @Override
    public void setOutsideClickClosable(boolean value) {
    }
}
