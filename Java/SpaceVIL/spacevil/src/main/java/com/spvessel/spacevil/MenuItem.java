package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.InterfaceMouseMethodState;
import com.spvessel.spacevil.Core.KeyArgs;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.MouseButton;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MenuItem extends Prototype {
    public boolean isActionItem = false;
    private static int count = 0;
    private Label _textObject;
    ContextMenu contextMenu;
    private ContextMenu _subContextMenu;

    public Prototype getSender() {
        return contextMenu.getSender();
    }

    /**
     * @return sub context menu
     */
    public ContextMenu getSubContextMenu() {
        return _subContextMenu;
    }

    /**
     * Is MenuItem ready to close
     */
    boolean isReadyToClose(MouseArgs args) {
        if (_subContextMenu != null) {
            if (!_subContextMenu.getHoverVerification(args.position.getX(), args.position.getY())
                    && _subContextMenu.closeDependencies(args))
                return true;
        }
        return false;
    }

    private CustomShape _arrow;

    public CustomShape getArrow() {
        return _arrow;
    }

    /**
     * Assign the context menu
     */
    public void assignContextMenu(ContextMenu contextMenu) {
        _subContextMenu = contextMenu;
        _subContextMenu.setOutsideClickClosable(false);
        isActionItem = true;
    }

    /**
     * Constructs a MenuItem
     */
    public MenuItem() {
        setItemName("MenuItem_" + count);
        count++;
        eventKeyPress.add(this::onKeyPress);

        InterfaceMouseMethodState m_press = (sender, args) -> onMouseAction();
        eventMousePress.add(m_press);
        _textObject = new Label();
        _textObject.isHover = false;

        setStyle(DefaultsService.getDefaultStyle(MenuItem.class));
    }

    /**
     * Constructs a MenuItem with text
     */
    public MenuItem(String text) {
        this();
        setText(text);
    }

    /**
     * Constructs a MenuItem with assigned context menu and text
     */
    public MenuItem(ContextMenu contextMenu, String text) {
        this();
        assignContextMenu(contextMenu);
        setText(text);
    }

    void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (args.scancode == 0x1C && eventMouseClick != null)
            eventMouseClick.execute(this, new MouseArgs());
    }

    // text init
    /**
     * Text alignment in the MenuItem
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textObject.setTextAlignment(alignment);
    }

    public void setTextAlignment(ItemAlignment... alignment) {
        // List<ItemAlignment> list = Arrays.stream(alignment).collect(Collectors.toList());
        // _text_object.setTextAlignment(list);
        _textObject.setTextAlignment(Arrays.asList(alignment));
    }

    /**
     * Text margin in the MenuItem
     */
    public Indents getTextMargin() {
        return _textObject.getMargin();
    }

    public void setTextMargin(Indents margin) {
        _textObject.setMargin(margin);
    }

    public void setTextMargin(int left, int top, int right, int bottom) {
        _textObject.setMargin(left, top, right, bottom);
    }

    /**
     * Text font in the MenuItem
     */
    public void setFont(Font font) {
        _textObject.setFont(font);
    }

    public Font getFont() {
        return _textObject.getFont();
    }

    /**
     * MenuItem text
     */
    public void setText(String text) {
        _textObject.setText(text);
    }

    public String getText() {
        return _textObject.getText();
    }

    /**
     * Text color in the MenuItem
     */
    public void setForeground(Color color) {
        _textObject.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        _textObject.setForeground(r, g, b);
    }

    public void setForeground(int r, int g, int b, int a) {
        _textObject.setForeground(r, g, b, a);
    }

    public void setForeground(float r, float g, float b) {
        _textObject.setForeground(r, g, b);
    }

    public void setForeground(float r, float g, float b, float a) {
        _textObject.setForeground(r, g, b, a);
    }

    public Color getForeground() {
        return _textObject.getForeground();
    }

    /**
     * @return text width in the MenuItem
     */
    public int getTextWidth() {
        return _textObject.getTextWidth();
    }

    /**
     * @return text height in the MenuItem
     */
    public int getTextHeight() {
        return _textObject.getTextHeight();
    }

    /**
     * Initialization and adding of all elements in the MenuItem
     */
    @Override
    public void initElements() {
        // adding
        addItem(_textObject);
        if (isActionItem)
            addItem(_arrow);
        for (InterfaceBaseItem item : _queue) {
            super.addItem(item);
        }
    }

    private List<InterfaceBaseItem> _queue = new LinkedList<>();

    @Override
    public void addItem(InterfaceBaseItem item) {
        _queue.add(item);
    }

    /**
     * Set style of the MenuItem
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

        Style inner_style = style.getInnerStyle("arrow");
        if (inner_style != null) {
            if (_arrow == null)
                _arrow = new CustomShape();
            _arrow.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("text");
        if (inner_style != null) {
            _textObject.setMargin(inner_style.margin);
        }
    }

    /**
     * Customize shape of the MenuItem's arrow
     */
    public void addArrow(CustomShape arrow) {
        _arrow = arrow;
    }

    /**
     * Show the MenuItem
     */
    public void show() {
        if (_subContextMenu == null)
            return;

        MouseArgs args = new MouseArgs();
        args.button = MouseButton.BUTTON_RIGHT;

        // проверка справа
        args.position.setX((contextMenu.getX() + contextMenu.getWidth() + 2));

        if (args.position.getX() + _subContextMenu.getWidth() > getHandler().getWidth()) {
            args.position.setX((contextMenu.getX() - _subContextMenu.getWidth() - 2));
        }
        // проверка снизу
        args.position.setY(getY());
        if (args.position.getY() + _subContextMenu.getHeight() > getHandler().getHeight()) {
            args.position.setY(contextMenu.getY() + contextMenu.getHeight() - _subContextMenu.getHeight());
        }

        _subContextMenu.show(this, args);
    }

    /**
     * Hide the MenuItem
     */
    public void hide() {
        if (_subContextMenu != null)
            _subContextMenu.hide();
    }

    private void onMouseAction() {
        if (_subContextMenu != null) {
            if (_subContextMenu.isVisible()) {
                hide();
                MouseArgs args = new MouseArgs();
                args.button = MouseButton.BUTTON_RIGHT;
                args.position.setPosition(getX(), getY());
                _subContextMenu.closeDependencies(args);
            } else
                show();
        }
    }
}