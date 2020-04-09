package com.spvessel.spacevil;

import java.awt.Color;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceFloating;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.LayoutType;
import com.spvessel.spacevil.Flags.Side;
import com.spvessel.spacevil.Flags.SizePolicy;

/**
 * SideArea is a container designed to show when it is needed, and the rest of
 * the time SideArea is hidden. SideArea is a floating item (see
 * com.spvessel.spacevil.InterfaceFloating and enum
 * com.spvessel.spacevil.Flags.LayoutType) Always attached to one of the four
 * sides of window.
 * <p>
 * Contains close button and resizable area.
 * <p>
 * Supports all events except drag and drop.
 * <p>
 * Notice: All floating items render above all others items.
 * <p>
 * SideArea does not pass any input events and invisible by default.
 */
public class SideArea extends Prototype implements InterfaceFloating {

    private boolean _init = false;
    private boolean _outside = false;

    /**
     * Returns True if SideArea (see com.spvessel.spacevil.InterfaceFloating) should
     * closes when mouse click outside the area of SideArea otherwise returns False.
     * 
     * @return True: if SideArea closes when mouse click outside the area. False: if
     *         SideArea stays opened when mouse click outside the area.
     */
    public boolean isOutsideClickClosable() {
        return _outside;
    }

    /**
     * Setting boolean value of item's behavior when mouse click occurs outside the
     * SideArea.
     * 
     * @param value True: SideArea should become invisible if mouse click occurs
     *              outside the item. False: an item should stay visible if mouse
     *              click occurs outside the item.
     */
    public void setOutsideClickClosable(boolean value) {
        _outside = value;
    }

    static int count = 0;
    private ButtonCore _close;

    /**
     * Resizable container area of SideArea.
     */
    public ResizableItem window;

    private Side _attachSide = Side.LEFT;

    /**
     * Getting the side of the window which SideArea is attached.
     * <p>
     * Default: Side.LEFT.
     * 
     * @return Side of the window as com.spvessel.spacevil.Flags.Side.
     */
    public Side getAttachSide() {
        return _attachSide;
    }

    /**
     * Setting the side of the window which SideArea will be attached.
     * <p>
     * Default: Side.LEFT.
     * 
     * @param side Side of the window as com.spvessel.spacevil.Flags.Side.
     */
    public void setAttachSide(Side side) {
        if (_attachSide == side)
            return;

        _attachSide = side;
        applyAttach();
    }

    private void applyAttach() {
        window.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        window.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
        window.isXResizable = false;
        window.isYResizable = false;
        window.clearExcludedSides();

        Color shadowColor = new Color(0, 0, 0, 150);
        int shadowRadius = 5;
        int shadowIndent = 2;

        switch (_attachSide) {
            case LEFT:
                window.isXResizable = true;
                window.setWidthPolicy(SizePolicy.FIXED);
                window.setWidth(_size);
                window.excludeSides(Side.LEFT, Side.BOTTOM, Side.TOP);
                window.setAlignment(ItemAlignment.LEFT);
                window.setShadow(shadowRadius, shadowIndent, 0, shadowColor);
                break;

            case TOP:
                window.isYResizable = true;
                window.setHeightPolicy(SizePolicy.FIXED);
                window.setHeight(_size);
                window.excludeSides(Side.LEFT, Side.RIGHT, Side.TOP);
                window.setAlignment(ItemAlignment.TOP);
                window.setShadow(shadowRadius, 0, shadowIndent, shadowColor);
                break;

            case RIGHT:
                window.isXResizable = true;
                window.setWidthPolicy(SizePolicy.FIXED);
                window.setWidth(_size);
                window.excludeSides(Side.RIGHT, Side.BOTTOM, Side.TOP);
                window.setShadow(shadowRadius, -shadowIndent, 0, shadowColor);
                window.setAlignment(ItemAlignment.RIGHT);
                break;

            case BOTTOM:
                window.isYResizable = true;
                window.setHeightPolicy(SizePolicy.FIXED);
                window.setHeight(_size);
                window.excludeSides(Side.LEFT, Side.RIGHT, Side.BOTTOM);
                window.setShadow(shadowRadius, 0, -shadowIndent, shadowColor);
                window.setAlignment(ItemAlignment.BOTTOM);
                break;

            default:
                window.setWidth(_size);
                window.setAlignment(ItemAlignment.LEFT);
                window.setShadow(shadowRadius, shadowIndent, 0, shadowColor);
                break;
        }
    }

    private int _size = 300;

    /**
     * Getting actual size of SideArea.
     * <p>
     * If SideArea is attached to Side.LEFT or Side.RIGHT, then this value is the
     * width of the area, otherwise, the height.
     * <p>
     * Default: 300.
     * 
     * @return Actual size of SideArea.
     */
    public int getAreaSize() {
        return _size;
    }

    /**
     * Setting actual size of SideArea.
     * <p>
     * If SideArea is attached to Side.LEFT or Side.RIGHT, then this value is the
     * width of the area, otherwise, the height.
     * <p>
     * Default: 300.
     * 
     * @param size Actual size of SideArea.
     */
    public void setAreaSize(int size) {
        if (size == _size)
            return;
        _size = size;
        applyAttach();
    }

    /**
     * Constructs SideArea with the specified side and the specified window for
     * attachment. (see com.spvessel.spacevil.CoreWindow,
     * com.spvessel.spacevil.ActiveWindow, com.spvessel.spacevil.DialogWindow).
     * <p>
     * SideArea does not pass any input events and invisible by default.
     * 
     * @param handler    Window for attaching SideArea.
     * @param attachSide Side of the window as com.spvessel.spacevil.Flags.Side.
     */
    public SideArea(CoreWindow handler, Side attachSide) {
        ItemsLayoutBox.addItem(handler, this, LayoutType.FLOATING);
        setItemName("SideArea_" + count++);
        _close = new ButtonCore();
        window = new ResizableItem();
        setStyle(DefaultsService.getDefaultStyle(SideArea.class));
        _attachSide = attachSide;
        applyAttach();
        eventMouseClick.add((sender, args) -> {
            hide();
        });
        setVisible(false);
        setPassEvents(false);

        eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.ESCAPE)
                hide();
        });
    }

    /**
     * Initializing all elements in the SideArea.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        super.addItem(window);
        window.addItem(_close);
        window.setPassEvents(false);
        window.isXFloating = false;
        window.isYFloating = false;
        _close.eventMouseClick.add((sender, args) -> {
            hide();
        });
        _init = true;
    }

    /**
     * Adding item to the SideArea.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        window.addItem(item);
    }

    /**
     * Inserting item to the SideArea.
     * 
     * @param item  Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @param index Index of insertion.
     */
    @Override
    public void insertItem(InterfaceBaseItem item, int index) {
        window.insertItem(item, index);
    }

    /**
     * Removing the specified item from SideArea.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    @Override
    public boolean removeItem(InterfaceBaseItem item) {
        return window.removeItem(item);
    }

    /**
     * Setting SideArea width. If the value is greater/less than the maximum/minimum
     * value of the width, then the width becomes equal to the maximum/minimum
     * value.
     * 
     * @param width Width of the SideArea.
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
    }

    /**
     * Setting SideArea height. If the value is greater/less than the
     * maximum/minimum value of the height, then the height becomes equal to the
     * maximum/minimum value.
     * 
     * @param height Height of the SideArea.
     */
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
    }

    /**
     * Shows the SideArea at the proper position.
     */
    public void show() {
        if (!_init)
            initElements();
        setVisible(true);
        setFocus();
    }

    /**
     * Shows the SideArea at the proper position. This method do exactly as show()
     * method without arguments.
     * 
     * @param sender The item from which the show request is sent.
     * @param args   Mouse click arguments (cursor position, mouse button, mouse
     *               button press/release, etc.).
     */
    public void show(InterfaceItem sender, MouseArgs args) {
        show();
    }

    /**
     * Hide the SideArea without destroying.
     */
    public void hide() {
        setVisible(false);
    }

    /**
     * Hide the SideArea without destroying with using specified mouse arguments.
     * This method do exactly as hide() method without arguments.
     * 
     * @param args Arguments as com.spvessel.spacevil.Core.MouseArgs.
     */
    public void hide(MouseArgs args) {
        hide();
    }

    /**
     * Setting style of the SideArea.
     * <p>
     * Inner styles: "window", "closebutton".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        Style inner_style = style.getInnerStyle("window");
        if (inner_style != null) {
            window.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("closebutton");
        if (inner_style != null) {
            _close.setStyle(inner_style);
        }
    }
}