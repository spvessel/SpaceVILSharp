package com.spvessel.Items;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Cores.InterfaceItem;
import com.spvessel.Cores.InterfaceKeyMethodState;
import com.spvessel.Cores.InterfaceMouseMethodState;
import com.spvessel.Cores.KeyArgs;
import com.spvessel.Cores.MouseArgs;
import com.spvessel.Decorations.Indents;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.MouseButton;

public class MenuItem extends VisualItem {
    public boolean isActionItem = false;
    static int count = 0;
    private TextLine _text_object;
    protected ContextMenu _context_menu;
    private ContextMenu _sub_context_menu;

    public ContextMenu getSubCintextMenu() {
        return _sub_context_menu;
    }

    public boolean isReadyToClose(MouseArgs args) {
        if (_sub_context_menu != null) {
            if (!_sub_context_menu.getHoverVerification(args.position.getX(), args.position.getY())
                    && _sub_context_menu.closeDependencies(args))
                return true;
        }
        return false;
    }

    CustomShape _arrow;

    public void assignContexMenu(ContextMenu context_menu) {
        _sub_context_menu = context_menu;
        _sub_context_menu.setOutsideClickClosable(false);
        isActionItem = true;
    }

    public MenuItem() {
        setItemName("MenuItem_" + count);
        count++;
        InterfaceKeyMethodState key_press = (sender, args) -> onKeyPress(sender, args);
        eventKeyPress.add(key_press);

        InterfaceMouseMethodState m_press = (sender, args) -> onMouseAction();
        eventMousePressed.add(m_press);
        _text_object = new TextLine();

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.MenuItem"));
        setStyle(DefaultsService.getDefaultStyle(com.spvessel.Items.MenuItem.class));
    }

    public MenuItem(String text) {
        this();
        setText(text);
    }

    public MenuItem(ContextMenu context_menu, String text) {
        this();
        assignContexMenu(context_menu);
        setText(text);
    }

    protected void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (args.scancode == 0x1C && eventMouseClick != null)
            eventMouseClick.execute(this, new MouseArgs());
    }

    // text init
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    public void setTextAlignment(ItemAlignment... alignment) {
        List<ItemAlignment> list = Arrays.stream(alignment).collect(Collectors.toList());
        _text_object.setTextAlignment(list);
    }

    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    public void setFont(Font font) {
        _text_object.setFont(font);
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
    public void initElements() {
        // adding
        addItem(_text_object);
        if (isActionItem)
            addItem(_arrow);
    }

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
    }

    public void addArrow(CustomShape arrow) {
        _arrow = arrow;
    }

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

    public void hide() {
        if (_sub_context_menu != null)
            _sub_context_menu.hide();
    }

    private void onMouseAction() {
        if (_sub_context_menu != null) {
            if (_sub_context_menu.getVisible()) {
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