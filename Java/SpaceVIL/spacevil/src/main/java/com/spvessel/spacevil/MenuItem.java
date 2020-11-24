package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.IItem;
import com.spvessel.spacevil.Core.IMouseMethodState;
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

/**
 * MenuItem is designed to be an option in menu type items such as
 * com.spvessel.spacevil.ContextMenu and ComboBoxDropDown.
 * <p>
 * Contains text and arrow.
 * <p>
 * Supports all events except drag and drop.
 */
public class MenuItem extends Prototype {
    /**
     * Property to mark this MenuItem as active type (such MenuItem can show another
     * com.spvessel.spacevil.ContextMenu). True this MenuItem is active otherwise
     * False.
     * <p>
     * Default: False.
     */
    public boolean isActionItem = false;
    private static int count = 0;
    private Label _textObject;
    ContextMenu contextMenu;
    private ContextMenu _subContextMenu;

    /**
     * Getting the item that invokes ContextMenu of this MenuItem.
     * 
     * @return Item as com.spvessel.spacevil.Prototype.
     */
    public Prototype getSender() {
        return contextMenu.getSender();
    }

    /**
     * Getting the assigned com.spvessel.spacevil.ContextMenu. If MenuItem is active
     * type it can invoke assigned com.spvessel.spacevil.ContextMenu.
     * 
     * @return Assigned context menu as com.spvessel.spacevil.ContextMenu.
     */
    public ContextMenu getSubContextMenu() {
        return _subContextMenu;
    }

    boolean isReadyToClose(MouseArgs args) {
        if (_subContextMenu != null) {
            if (!_subContextMenu.getHoverVerification(args.position.getX(), args.position.getY())
                    && _subContextMenu.closeDependencies(args))
                return true;
        }
        return false;
    }

    private CustomShape _arrow;

    /**
     * Getting arrow for styling appearance.
     * 
     * @return Arrow as com.spvessel.spacevil.CustomShape.
     */
    public CustomShape getArrow() {
        return _arrow;
    }

    /**
     * Assigning com.spvessel.spacevil.ContextMenu to this MenuItem. In this case
     * MenuItem becomes active type and can invoke assigned
     * com.spvessel.spacevil.ContextMenu.
     * 
     * @param contextMenu Assigned context menu as
     *                    com.spvessel.spacevil.ContextMenu.
     */
    public void assignContextMenu(ContextMenu contextMenu) {
        _subContextMenu = contextMenu;
        _subContextMenu.setOutsideClickClosable(false);
        isActionItem = true;
    }

    /**
     * Default MenuItem constructor.
     */
    public MenuItem() {
        setItemName("MenuItem_" + count);
        count++;
        eventKeyPress.add(this::onKeyPress);

        IMouseMethodState m_press = (sender, args) -> onMouseAction();
        eventMousePress.add(m_press);
        _textObject = new Label();
        _textObject.isHover = false;

        setStyle(DefaultsService.getDefaultStyle(MenuItem.class));
    }

    /**
     * Constructs a MenuItem with text.
     * 
     * @param text Text for MenuItem.
     */
    public MenuItem(String text) {
        this();
        setText(text);
    }

    /**
     * Constructs a MenuItem with text and assigns context menu.
     * 
     * @param contextMenu Assigned context menu as
     *                    com.spvessel.spacevil.ContextMenu.
     * @param text        Text for MenuItem.
     */
    public MenuItem(ContextMenu contextMenu, String text) {
        this();
        assignContextMenu(contextMenu);
        setText(text);
    }

    private void onKeyPress(IItem sender, KeyArgs args) {
        if (args.scancode == 0x1C && eventMouseClick != null)
            eventMouseClick.execute(this, new MouseArgs());
    }

    /**
     * Setting alignment of a MenuItem text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Setting alignment of a MenuItem text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as List of
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Setting indents for the text to offset text relative to MenuItem.
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        _textObject.setMargin(margin);
    }

    /**
     * Setting indents for the text to offset text relative to MenuItem.
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
     * Getting the current text of the MenuItem.
     * 
     * @return Text as java.lang.String.
     */
    public String getText() {
        return _textObject.getText();
    }

    /**
     * Setting text color of a MenuItem.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        _textObject.setForeground(color);
    }

    /**
     * Setting text color of a MenuItem in byte RGB format.
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
     * Setting text color of a MenuItem in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        _textObject.setForeground(r, g, b);
    }

    /**
     * Setting text color of a MenuItem in float RGBA format.
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
     * Getting the text width (useful when you need resize MenuItem by text width).
     * 
     * @return Text width.
     */
    public int getTextWidth() {
        return _textObject.getTextWidth();
    }

    /**
     * Getting the text height (useful when you need resize MenuItem by text
     * height).
     * 
     * @return Text height.
     */
    public int getTextHeight() {
        return _textObject.getTextHeight();
    }

    /**
     * Initializing all elements in the MenuItem.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        // adding
        addItem(_textObject);
        if (isActionItem)
            addItem(_arrow);
        for (IBaseItem item : _queue) {
            super.addItem(item);
        }
    }

    private List<IBaseItem> _queue = new LinkedList<>();

    /**
     * Adding item into the container (this).
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     */
    @Override
    public void addItem(IBaseItem item) {
        _queue.add(item);
    }

    /**
     * Setting style of the MenuItem.
     * <p>
     * Inner styles: "arrow", "text".
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
     * Adding custom arrow shape.
     * 
     * @param arrow Arrow shape as com.spvessel.spacevil.CustomShape.
     */
    public void addArrow(CustomShape arrow) {
        _arrow = arrow;
    }

    /**
     * Shows the assigned ContextMenu at the proper position. Only if this MeniItem
     * is active type.
     */
    public void show() {
        if (_subContextMenu == null)
            return;

        MouseArgs args = new MouseArgs();
        args.button = MouseButton.ButtonRight;

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
     * Hides the assigned ContextMenu. Only if this MeniItem is active type.
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
                args.button = MouseButton.ButtonRight;
                args.position.setPosition(getX(), getY());
                _subContextMenu.closeDependencies(args);
            } else
                show();
        }
    }
}