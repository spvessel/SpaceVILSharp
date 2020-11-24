package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Core.EventMouseMethodState;
import com.spvessel.spacevil.Core.IDraggable;
import com.spvessel.spacevil.Core.IItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

/**
 * Tab is used in com.spvessel.spacevil.TabView. Represents tab of one page.
 * <p>
 * Contains close button.
 * <p>
 * Supports all including drag and drop.
 */
public class Tab extends Prototype implements IDraggable {
    static int count = 0;
    Frame view;
    private Label _textLabel;
    private ButtonCore _close;

    /**
     * Getting close button.
     * 
     * @return Close button as com.spvessel.spacevil.ButtonCore.
     */
    public ButtonCore getCloseButton() {
        return _close;
    }

    private boolean _isClosable = false;

    /**
     * Setting tab to support closing or not support closing.
     * 
     * @param value True: if you want to tab support closing. False: if you want to
     *              tab do not support closing.
     */
    public void setClosable(boolean value) {
        if (_isClosable == value)
            return;
        _isClosable = value;
        _close.setVisible(_isClosable);
        updateTabWidth();
    }

    /**
     * Returns True if tab support closing otherwise returns False.
     * 
     * @return True: if tab support closing. False: if tab do not support closing.
     */
    public boolean isClosable() {
        return _isClosable;
    }

    /**
     * Constructs Tab with the specified text.
     * 
     * @param text Text of Tab.
     */
    public Tab(String text) {
        this(text, text);
    }

    /**
     * Constructs Tab with specified text and name of the Tab.
     * 
     * @param text Text of the Tab.
     * @param name Name of the Tab.
     */
    public Tab(String text, String name) {
        this();
        setItemName(name);
        setText(text);
    }

    /**
     * Default Tab constructor.
     */
    public Tab() {
        super();
        setItemName("Tab_" + count++);
        _close = new ButtonCore();
        _textLabel = new Label();
        _textLabel.isHover = false;
        view = new Frame();
        setStyle(DefaultsService.getDefaultStyle(Tab.class));

        // draggable tabs
        setPassEvents(false, InputEventType.MouseDoubleClick, InputEventType.MousePress,
                InputEventType.MouseRelease);
        isFocusable = false;

        eventMousePress.add(this::onMousePress);
        eventMouseDrag.add(this::onDragging);
    }

    private int _xClick = 0;
    private int _xDiff = 0;
    boolean dragging = false;
    private boolean _isDraggable = true;

    /**
     * Setting tab to support drag and drop or not.
     * 
     * @param value True: if you want to tab support drag and drop. False: if you
     *              want to tab do not support drag and drop.
     */
    public void setDraggable(boolean value) {
        _isDraggable = value;
    }

    /**
     * Returns True if tab support drag and drop otherwise returns False.
     * 
     * @return True: if tab support drag and drop. False: if tab do not support drag
     *         and drop.
     */
    public boolean isDraggable() {
        return _isDraggable;
    }

    private void onMousePress(IItem sender, MouseArgs args) {
        if (!isDraggable())
            return;
        _xClick = args.position.getX();
        _xDiff = args.position.getX() - getX();
    }

    private void onDragging(IItem sender, MouseArgs args) {
        if (!isDraggable())
            return;
        if (dragging) {
            Prototype parent = getParent();
            int offset = args.position.getX() - parent.getX() - _xDiff;
            int x = offset + parent.getX();
            if (x <= parent.getX()) {
                x = parent.getX();
            }
            if (x >= parent.getX() + parent.getWidth() - getWidth()) {
                x = parent.getX() + parent.getWidth() - getWidth();
            }
            setX(x);
        } else {
            if (Math.abs(_xClick - args.position.getX()) <= 20)
                return;
            dragging = true;
        }
    }

    private boolean _toggled;

    /**
     * Returns True if Tab is selected otherwise returns False.
     * 
     * @return True: Tab is selected. False: Tab is unselected.
     */
    public boolean isToggled() {
        return _toggled;
    }

    /**
     * Setting Tab selected or unselected.
     * 
     * @param value True: if you want Tab to be selected. False: if you want Tab to
     *              be unselected.
     */
    public void setToggled(boolean value) {
        _toggled = value;
        if (value == true)
            setState(ItemStateType.Toggled);
        else
            setState(ItemStateType.Base);
    }

    /**
     * Event that is invoked when Tab become selected.
     */
    public EventMouseMethodState eventOnSelect = new EventMouseMethodState();

    /**
     * Event that is invoked when Tab is closed.
     */
    public EventCommonMethod eventOnClose = new EventCommonMethod();

    EventCommonMethod eventTabRemove = new EventCommonMethod();

    /**
     * Disposing Tab resources if the Tab was removed.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void release() {
        eventOnSelect.clear();
        eventOnSelect = null;
        eventOnClose.clear();
        eventOnClose = null;
        eventTabRemove.clear();
        eventTabRemove = null;
    }

    /**
     * Setting alignment of a Tab text. Combines with alignment by vertically (TOP,
     * VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _textLabel.setTextAlignment(alignment);
    }

    /**
     * Setting alignment of a Tab text. Combines with alignment by vertically (TOP,
     * VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as List of
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textLabel.setTextAlignment(alignment);
    }

    /**
     * Setting indents for the text to offset text relative to Tab.
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        _textLabel.setMargin(margin);
    }

    /**
     * Setting indents for the text to offset text relative to Tab.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setTextMargin(int left, int top, int right, int bottom) {
        _textLabel.setMargin(left, top, right, bottom);
    }

    /**
     * Getting indents of the text.
     * 
     * @return Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getTextMargin() {
        return _textLabel.getMargin();
    }

    /**
     * Setting font of the text.
     * 
     * @param font Font as java.awt.Font.
     */
    public void setFont(Font font) {
        _textLabel.setFont(font);
    }

    /**
     * Setting font size of the text.
     * 
     * @param size New size of the font.
     */
    public void setFontSize(int size) {
        _textLabel.setFontSize(size);
    }

    /**
     * Setting font style of the text.
     * 
     * @param style New font style (from java.awt.Font package).
     */
    public void setFontStyle(int style) {
        _textLabel.setFontStyle(style);
    }

    /**
     * Setting new font family of the text.
     * 
     * @param fontFamily New font family name.
     */
    public void setFontFamily(String fontFamily) {
        _textLabel.setFontFamily(fontFamily);
    }

    /**
     * Getting the current font of the text.
     * 
     * @return Font as java.awt.Font.
     */
    public Font getFont() {
        return _textLabel.getFont();
    }

    /**
     * Setting the text.
     * 
     * @param text Text as java.lang.String.
     */
    public void setText(String text) {
        _textLabel.setText(text);
        updateTabWidth();
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
     * Getting the current text of the Tab.
     * 
     * @return Text as java.lang.String.
     */
    public String getText() {
        return _textLabel.getText();
    }

    /**
     * Getting the text width (useful when you need resize Tab by text width).
     * 
     * @return Text width.
     */
    public int getTextWidth() {
        return _textLabel.getTextWidth();
    }

    /**
     * Getting the text height (useful when you need resize Tab by text height).
     * 
     * @return Text height.
     */
    public int getTextHeight() {
        return _textLabel.getTextHeight();
    }

    /**
     * Setting text color of a Tab.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        _textLabel.setForeground(color);
    }

    /**
     * Setting text color of a Tab in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        _textLabel.setForeground(r, g, b);
    }

    /**
     * Setting background color of an item in byte RGBA format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b, int a) {
        _textLabel.setForeground(r, g, b, a);
    }

    /**
     * Setting text color of a Tab in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        _textLabel.setForeground(r, g, b);
    }

    /**
     * Setting text color of a Tab in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b, float a) {
        _textLabel.setForeground(r, g, b, a);
    }

    /**
     * Getting current text color.
     * 
     * @return Text color as as java.awt.Color.
     */
    public Color getForeground() {
        return _textLabel.getForeground();
    }

    private int _labelRightMargin = 0;

    void updateTabWidth() {
        if (getWidthPolicy() == SizePolicy.Fixed) {
            int w = getPadding().left + getTextWidth() + getPadding().right;
            if (_isClosable) {
                w += getSpacing().horizontal + _close.getWidth();
                applyRightTextMargin(getSpacing().horizontal + _close.getWidth());
            } else {
                applyRightTextMargin(_labelRightMargin);
            }
            setWidth(w);
        } else {
            if (_isClosable) {
                applyRightTextMargin(getSpacing().horizontal + _close.getWidth());
            } else {
                applyRightTextMargin(_labelRightMargin);
            }
        }
    }

    private void applyRightTextMargin(int value) {
        _textLabel.setMargin(_textLabel.getMargin().left, _textLabel.getMargin().top, value,
                _textLabel.getMargin().bottom);
    }

    /**
     * Initializing all elements in the Tab.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        super.initElements();
        _close.setVisible(_isClosable);
        addItems(_textLabel, _close);
        eventOnSelect.clear();
        eventOnSelect.add((sender, args) -> {
            if (isToggled())
                return;
            setToggled(true);
        });

        _close.isFocusable = false;
        _close.eventMouseClick.add((sender, args) -> {
            removeTab();
        });
    }

    /**
     * Removing Tab.
     */
    public void removeTab() {
        eventOnClose.execute();
        eventTabRemove.execute();
    }

    /**
     * Setting style of the Tab.
     * <p>
     * Inner styles: "closebutton", "view", "text".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("closebutton");
        if (inner_style != null)
            _close.setStyle(inner_style);
        inner_style = style.getInnerStyle("view");
        if (inner_style != null)
            view.setStyle(inner_style);
        inner_style = style.getInnerStyle("text");
        if (inner_style != null) {
            _textLabel.setStyle(inner_style);
            _labelRightMargin = _textLabel.getMargin().right;
        }

        setForeground(style.foreground);
        setFont(style.font);
        setTextAlignment(style.textAlignment);
    }
}