package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.IDraggable;
import com.spvessel.spacevil.Core.IFloating;
import com.spvessel.spacevil.Core.IItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Flags.LayoutType;
import com.spvessel.spacevil.Flags.SizePolicy;

/**
 * FloatItem is a floating container for other items (see
 * com.spvessel.spacevil.Core.IFloating). Can be moved using drag and
 * drop events.
 * <p>
 * Supports all events including drag and drop.
 * <p>
 * Notice: All floating items render above all others items.
 */
public class FloatItem extends Prototype implements IFloating, IDraggable {
    /**
     * Property for enabling/disabling drag and drop.
     * <p>
     * True: Drag and drop is enabled. False: Drag and drop is disabled.
     * <p>
     * Default: True.
     */
    public boolean isFloating = true;
    private int _storedOffset = 0;
    private boolean _init = false;
    private static int count = 0;
    private int _diffX = 0;
    private int _diffY = 0;
    private boolean _ouside = false;

    /**
     * Returns True if FloatItem (see com.spvessel.spacevil.Core.IFloating)
     * should closes when mouse click outside the area of FloatItem otherwise
     * returns False.
     * 
     * @return True: if FloatItem closes when mouse click outside the area. False:
     *         if FloatItem stays opened when mouse click outside the area.
     */
    public boolean isOutsideClickClosable() {
        return _ouside;
    }

    /**
     * Setting boolean value of item's behavior when mouse click occurs outside the
     * FloatItem.
     * 
     * @param value True: FloatItem should become invisible if mouse click occurs
     *              outside the item. False: an item should stay visible if mouse
     *              click occurs outside the item.
     */
    public void setOutsideClickClosable(boolean value) {
        _ouside = value;
    }

    /**
     * Constructs a FloatItem and attaches it to the specified window (see
     * com.spvessel.spacevil.CoreWindow, com.spvessel.spacevil.ActiveWindow,
     * com.spvessel.spacevil.DialogWindow). FloatItem invisible by default.
     * 
     * @param handler Window for attaching FloatItem.
     */
    public FloatItem(CoreWindow handler) {
        ItemsLayoutBox.addItem(handler, this, LayoutType.Floating);
        setVisible(false);
        setItemName("FloatItem_" + count++);
        setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        eventMousePress.add(this::onMousePress);
        eventMouseDrag.add(this::onDragging);
    }

    /**
     * Initializing FloatItem.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        setConfines();
        _init = true;
    }

    /**
     * Shows the FloatItem at the proper position.
     * 
     * @param sender The item from which the show request is sent.
     * @param args   Mouse click arguments (cursor position, mouse button, mouse
     *               button press/release, etc.).
     */
    public void show(IItem sender, MouseArgs args) {
        if (!_init)
            initElements();
        if (getX() == -getWidth()) // refactor?
            setX(_storedOffset);
        setVisible(true);
    }

    /**
     * Shows the FloatItem at the position (0, 0).
     */
    public void show() {
        if (!_init)
            initElements();
        if (getX() == -getWidth()) // refactor?
            setX(_storedOffset);
        setVisible(true);
    }

    /**
     * Hides the FloatItem without destroying.
     */
    public void hide() {
        _storedOffset = getX();
        setX(-getWidth());
        setVisible(false);
    }

    /**
     * Hides the FloatItem without destroying.
     * <p>
     * This method do exactly as hide() method without arguments.
     * 
     * @param args Mouse click arguments (cursor position, mouse button, mouse
     *             button press/release, etc.).
     */
    public void hide(MouseArgs args) {
        hide();
    }

    private void onMousePress(IItem sender, MouseArgs args) {
        _diffX = args.position.getX() - getX();
        _diffY = args.position.getY() - getY();
    }

    private void onDragging(IItem sender, MouseArgs args) {
        if (!isFloating)
            return;

        int offset_x;
        int offset_y;

        offset_x = args.position.getX() - _diffX;
        offset_y = args.position.getY() - _diffY;

        setX(offset_x);
        setY(offset_y);
        setConfines();
    }

    /**
     * Overridden method for setting confines according to position and size of the
     * FloatItem (see Prototype.setConfines()).
     */
    @Override
    public void setConfines() {
        setConfines(getX(), getX() + getWidth(), getY(), getY() + getHeight());
    }
}