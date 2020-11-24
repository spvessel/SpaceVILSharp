package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.IMouseMethodState;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * PopUpMessage is designed to display short quick messages to the user.
 * PopUpMessage disappears after a specified period of time (default: 2
 * seconds), or you can prevent this by moving the cursor over PopUpMessage and
 * closing it later manually.
 * <p>
 * Contains text, close button.
 * <p>
 * Supports all events except drag and drop.
 * <p>
 * By default PopUpMessage do not pass further any input events (mouse, keyboard
 * and etc.).
 */
public class PopUpMessage extends Prototype {
    private static int count = 0;
    private Label _textObject;
    private ButtonCore _btnClose;
    Timer _stop;
    private int _timeout = 2000;
    boolean _holded = false;

    /**
     * Setting waiting time in milliseconds after which PopUpMessage will be closed.
     * <p>
     * Default: 2000 milliseconds (2 seconds).
     * 
     * @param milliseconds Waiting time in milliseconds.
     */
    public void setTimeOut(int milliseconds) {
        _timeout = milliseconds;
    }

    /**
     * Getting current waiting time in milliseconds after which PopUpMessage will be
     * closed.
     * 
     * @return Current waiting time in milliseconds.
     */
    public int getTimeOut() {
        return _timeout;
    }

    /**
     * Constructs a PopUpMessage with message and parent window (handler)
     * 
     * @param message Text message to an user.
     */
    public PopUpMessage(String message) {
        setItemName("PopUpMessage_" + count);
        count++;

        _btnClose = new ButtonCore();
        _btnClose.setItemName("ClosePopUp");
        _textObject = new Label();
        _textObject.setText(message);

        setStyle(DefaultsService.getDefaultStyle(PopUpMessage.class));
        setPassEvents(false);
    }

    /**
     * Setting alignment of a PopUpMessage text. Combines with alignment by
     * vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Setting alignment of a PopUpMessage text. Combines with alignment by
     * vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as List of
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Setting indents for the text to offset text relative to PopUpMessage.
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        _textObject.setMargin(margin);
    }

    /**
     * Setting indents for the text to offset text relative to PopUpMessage.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setTextMargin(int left, int top, int right, int bottom) {
        _textObject.setMargin(left, top, right, bottom);
    }

    /**
     * Getting indents of the text.
     * 
     * @return Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getTextMargin() {
        return _textObject.getMargin();
    }

    /**
     * Setting font of the text.
     * 
     * @param font Font as java.awt.Font.
     */
    public void setFont(Font font) {
        _textObject.setFont(font);
    }

    /**
     * Setting font size of the text.
     * 
     * @param size New size of the font.
     */
    public void setFontSize(int size) {
        _textObject.setFontSize(size);
    }

    /**
     * Setting font style of the text.
     * 
     * @param style New font style (from java.awt.Font package).
     */
    public void setFontStyle(int style) {
        _textObject.setFontStyle(style);
    }

    /**
     * Setting new font family of the text.
     * 
     * @param fontFamily New font family name.
     */
    public void setFontFamily(String fontFamily) {
        _textObject.setFontFamily(fontFamily);
    }

    /**
     * Getting the current font of the text.
     * 
     * @return Font as java.awt.Font.
     */
    public Font getFont() {
        return _textObject.getFont();
    }

    /**
     * Setting the text.
     * 
     * @param text Text as java.lang.String.
     */
    public void setText(String text) {
        _textObject.setText(text);
    }

    /**
     * Setting the text.
     * 
     * @param text Text as java.lang.Object.
     */
    public void setText(Object text) {
        setText(text.toString());
    }

    /**
     * Getting the current text of the PopUpMessage.
     * 
     * @return Text as java.lang.String.
     */
    public String getText() {
        return _textObject.getText();
    }

    /**
     * Setting text color of a PopUpMessage.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        _textObject.setForeground(color);
    }

    /**
     * Setting text color of a PopUpMessage in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        _textObject.setForeground(r, g, b);
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
        _textObject.setForeground(r, g, b, a);
    }

    /**
     * Setting text color of a PopUpMessage in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        _textObject.setForeground(r, g, b);
    }

    /**
     * Setting text color of a PopUpMessage in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b, float a) {
        _textObject.setForeground(r, g, b, a);
    }

    /**
     * Getting current text color.
     * 
     * @return Text color as as java.awt.Color.
     */
    public Color getForeground() {
        return _textObject.getForeground();
    }

    /**
     * Initializing all elements in the PopUpMessage.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        IMouseMethodState click = (sender, args) -> removeSelf();
        _btnClose.eventMouseClick.add(click);
        // adding
        addItems(_textObject, _btnClose);
    }

    private CoreWindow _handler = null;

    /**
     * Shows PopUpMessage and attaches it to the specified window (see
     * com.spvessel.spacevil.CoreWindow, com.spvessel.spacevil.ActiveWindow,
     * com.spvessel.spacevil.DialogWindow).
     * 
     * @param handler Window for attaching PopUpMessage.
     */
    public void show(CoreWindow handler) {
        _handler = handler;
        _handler.addItem(this);
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
        _handler.removeItem(this);
    }

    void holdSelf(boolean value) {
        _holded = value;
        if (_stop != null) {
            _stop.cancel();
            _stop = null;
        }
    }

    /**
     * Setting style of the PopUpMessage.
     * <p>
     * Inner styles: "closebutton".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
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
            _btnClose.setStyle(inner_style);
        }
    }
}