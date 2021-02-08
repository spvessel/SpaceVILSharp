package com.spvessel.spacevil;

import java.awt.Color;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.IFloating;
import com.spvessel.spacevil.Core.IItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.EffectType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.LayoutType;
import com.spvessel.spacevil.Flags.Side;
import com.spvessel.spacevil.Flags.SizePolicy;

/**
 * SideArea is a container designed to show when it is needed, and the rest of
 * the time SideArea is hidden. SideArea is a floating item (see
 * com.spvessel.spacevil.Core.IFloating and enum
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
public class SideArea extends Prototype implements IFloating {

    static int count = 0;
    private boolean _init = false;

    private final Color _shadowColor = new Color(0, 0, 0, 150);
    private final int _shadowRadius = 5;
    private final int _shadowIndent = 3;

    private Shadow _shadowLeftArea = null;
    private Shadow _shadowTopArea = null;
    private Shadow _shadowRightArea = null;
    private Shadow _shadowBottomArea = null;

    private ButtonCore _close;

    /**
     * Resizable container area of SideArea.
     */
    public ResizableItem window;

    private Side _attachSide = Side.Left;

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
        window.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        window.setAlignment(ItemAlignment.Top, ItemAlignment.Left);
        window.isXResizable = false;
        window.isYResizable = false;
        window.clearExcludedSides();
        window.effects().clear(EffectType.Shadow);

        switch (_attachSide) {
            case Left:
                window.isXResizable = true;
                window.setWidthPolicy(SizePolicy.Fixed);
                window.setWidth(_size);
                window.excludeSides(Side.Left, Side.Bottom, Side.Top);
                window.setAlignment(ItemAlignment.Left);
                window.effects().add(_shadowLeftArea);
                break;

            case Top:
                window.isYResizable = true;
                window.setHeightPolicy(SizePolicy.Fixed);
                window.setHeight(_size);
                window.excludeSides(Side.Left, Side.Right, Side.Top);
                window.setAlignment(ItemAlignment.Top);
                window.effects().add(_shadowTopArea);
                break;

            case Right:
                window.isXResizable = true;
                window.setWidthPolicy(SizePolicy.Fixed);
                window.setWidth(_size);
                window.excludeSides(Side.Right, Side.Bottom, Side.Top);
                window.effects().add(_shadowRightArea);
                break;

            case Bottom:
                window.isYResizable = true;
                window.setHeightPolicy(SizePolicy.Fixed);
                window.setHeight(_size);
                window.excludeSides(Side.Left, Side.Right, Side.Bottom);
                window.setAlignment(ItemAlignment.Bottom);
                window.effects().add(_shadowBottomArea);
                break;

            default:
                window.setWidth(_size);
                window.setAlignment(ItemAlignment.Left);
                window.effects().add(_shadowLeftArea);
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
        ItemsLayoutBox.addItem(handler, this, LayoutType.Floating);

        _shadowLeftArea = new Shadow(_shadowRadius, new Position(_shadowIndent, 0), _shadowColor);
        _shadowTopArea = new Shadow(_shadowRadius, new Position(0, _shadowIndent), _shadowColor);
        _shadowRightArea = new Shadow(_shadowRadius, new Position(-_shadowIndent, 0), _shadowColor);
        _shadowBottomArea = new Shadow(_shadowRadius, new Position(0, -_shadowIndent), _shadowColor);

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
            if (args.key == KeyCode.Escape)
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
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     */
    @Override
    public void addItem(IBaseItem item) {
        window.addItem(item);
    }

    /**
     * Inserting item to the SideArea.
     * 
     * @param item  Item as com.spvessel.spacevil.Core.IBaseItem.
     * @param index Index of insertion.
     */
    @Override
    public void insertItem(IBaseItem item, int index) {
        window.insertItem(item, index);
    }

    /**
     * Removing the specified item from SideArea.
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    @Override
    public boolean removeItem(IBaseItem item) {
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
    public void show(IItem sender, MouseArgs args) {
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

    private boolean _outside = false;

    /**
     * Returns True if SideArea (see com.spvessel.spacevil.Core.IFloating) should
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
}