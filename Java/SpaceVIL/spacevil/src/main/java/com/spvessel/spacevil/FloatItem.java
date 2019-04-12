package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceDraggable;
import com.spvessel.spacevil.Core.InterfaceFloating;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Flags.LayoutType;
import com.spvessel.spacevil.Flags.SizePolicy;

public class FloatItem extends Prototype implements InterfaceFloating, InterfaceDraggable {
    private int _stored_offset = 0;
    private boolean IsFloating = true;
    private boolean _init = false;
    private static int count = 0;
    private int _diff_x = 0;
    private int _diff_y = 0;

    private boolean _ouside = false;

    /**
     * Close the FloatItem it mouse click is outside (true or false)
     */
    public boolean isOutsideClickClosable() {
        return _ouside;
    }

    public void setOutsideClickClosable(boolean value) {
        _ouside = value;
    }

    /**
     * Constructs a FloatItem
     * @param handler parent window for the FloatItem
     */
    public FloatItem(CoreWindow handler) {
        setVisible(false);
        setHandler(handler);
        setItemName("FloatItem_" + count);
        setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        eventMousePress.add(this::onMousePress);
        eventMouseDrag.add(this::onDragging);
        count++;
        
        ItemsLayoutBox.addItem(getHandler(), this, LayoutType.FLOATING);
    }

    /**
     * Initialization and adding of all elements in the FloatItem
     */
    @Override
    public void initElements() {
        // fake tests
        setConfines();
        _init = true;
    }

    /**
     * Show the FloatItem
     * @param sender the item from which the show request is sent
     * @param args mouse click arguments (cursor position, mouse button,
     *             mouse button press/release, etc.)
     */
    public void show(InterfaceItem sender, MouseArgs args) {
        if (!_init)
            initElements();
        if (getX() == -getWidth()) // refactor?
            setX(_stored_offset);
        setVisible(true);
    }
    public void show() {
        if (!_init)
            initElements();
        if (getX() == -getWidth()) // refactor?
            setX(_stored_offset);
        setVisible(true);
    }

    /**
     * Hide the FloatItem
     */
    public void hide() {
        _stored_offset = getX();
        setX(-getWidth());
        setVisible(false);
    }

    private void onMousePress(InterfaceItem sender, MouseArgs args) {
        _diff_x = args.position.getX() - getX();
        _diff_y = args.position.getY() - getY();
    }

    private void onDragging(InterfaceItem sender, MouseArgs args) {
        if (!IsFloating)
            return;

        int offset_x;
        int offset_y;

        offset_x = args.position.getX() - _diff_x;
        offset_y = args.position.getY() - _diff_y;

        setX(offset_x);
        setY(offset_y);
        setConfines();
    }

    /**
     * Set confines according to position and size of the FloatItem
     */
    @Override
    public void setConfines() {
        setConfines(getX(), getX() + getWidth(), getY(), getY() + getHeight());
    }
}