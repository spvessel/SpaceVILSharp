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
import java.util.stream.Collectors;

public class MenuItem extends Prototype {
    public boolean isActionItem = false;
    private static int count = 0;
    private Label _text_object;
    ContextMenu _context_menu;
    private ContextMenu _sub_context_menu;

    public Prototype getSender() {
        return _context_menu.getSender();
    }

    /**
     * @return sub context menu
     */
    public ContextMenu getSubContextMenu() {
        return _sub_context_menu;
    }

    /**
     * Is MenuItem ready to close
     */
    boolean isReadyToClose(MouseArgs args) {
        if (_sub_context_menu != null) {
            if (!_sub_context_menu.getHoverVerification(args.position.getX(), args.position.getY())
                    && _sub_context_menu.closeDependencies(args))
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
    public void assignContextMenu(ContextMenu context_menu) {
        _sub_context_menu = context_menu;
        _sub_context_menu.setOutsideClickClosable(false);
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
        _text_object = new Label();

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.MenuItem"));
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
    public MenuItem(ContextMenu context_menu, String text) {
        this();
        assignContextMenu(context_menu);
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
        _text_object.setTextAlignment(alignment);
    }

    public void setTextAlignment(ItemAlignment... alignment) {
        List<ItemAlignment> list = Arrays.stream(alignment).collect(Collectors.toList());
        _text_object.setTextAlignment(list);
    }

    /**
     * Text margin in the MenuItem
     */
    public Indents getTextMargin() {
        return _text_object.getMargin();
    }

    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    /**
     * Text font in the MenuItem
     */
    public void setFont(Font font) {
        _text_object.setFont(font);
    }

    public Font getFont() {
        return _text_object.getFont();
    }

    /**
     * MenuItem text
     */
    public void setText(String text) {
        _text_object.setText(text);
    }

    public String getText() {
        return _text_object.getText();
    }

    /**
     * Text color in the MenuItem
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
     * @return text width in the MenuItem
     */
    public int getTextWidth() {
        return _text_object.getTextWidth();
    }

    /**
     * @return text height in the MenuItem
     */
    public int getTextHeight() {
        return _text_object.getTextHeight();
    }

    /**
     * Initialization and adding of all elements in the MenuItem
     */
    @Override
    public void initElements() {
        // adding
        addItem(_text_object);
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
            _text_object.setMargin(inner_style.margin);
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
        if (_sub_context_menu == null)
            return;

        MouseArgs args = new MouseArgs();
        args.button = MouseButton.BUTTON_RIGHT;

        // проверка справа
        args.position.setX((_context_menu.getX() + _context_menu.getWidth() + 2));

        if (args.position.getX() + _sub_context_menu.getWidth() > getHandler().getWidth()) {
            args.position.setX((_context_menu.getX() - _sub_context_menu.getWidth() - 2));
        }
        // проверка снизу
        args.position.setY(getY());
        if (args.position.getY() + _sub_context_menu.getHeight() > getHandler().getHeight()) {
            args.position.setY(_context_menu.getY() + _context_menu.getHeight() - _sub_context_menu.getHeight());
        }

        _sub_context_menu.show(this, args);
    }

    /**
     * Hide the MenuItem
     */
    public void hide() {
        if (_sub_context_menu != null)
            _sub_context_menu.hide();
    }

    private void onMouseAction() {
        if (_sub_context_menu != null) {
            if (_sub_context_menu.isVisible()) {
                hide();
                MouseArgs args = new MouseArgs();
                args.button = MouseButton.BUTTON_RIGHT;
                args.position.setPosition(getX(), getY());
                _sub_context_menu.closeDependencies(args);
            } else
                show();
        }
    }
}