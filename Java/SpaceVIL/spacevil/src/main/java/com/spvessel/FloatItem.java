package com.spvessel;

import com.spvessel.Common.CommonService;
import com.spvessel.Core.*;
import com.spvessel.Flags.LayoutType;
import com.spvessel.Flags.SizePolicy;

public class FloatItem extends Prototype implements InterfaceFloating, InterfaceDraggable {
    private int _stored_offset = 0;
    private boolean IsFloating = true;
    private boolean _init = false;
    static int count = 0;
    private int _diff_x = 0;
    private int _diff_y = 0;

    private boolean _ouside = false;

    public boolean getOutsideClickClosable() {
        return _ouside;
    }

    public void setOutsideClickClosable(boolean value) {
        _ouside = value;
    }

    public FloatItem(WindowLayout handler) {
        setVisible(false);
        setHandler(handler);
        setItemName("FloatItem_" + count);
        setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        InterfaceMouseMethodState press = (sender, args) -> onMousePress(sender, args);
        // eventMouseHover.add(press);
        eventMousePressed.add(press);
        InterfaceMouseMethodState dragg = (sender, args) -> onDragging(sender, args);
        eventMouseDrag.add(dragg);
        count++;

        synchronized (CommonService.GlobalLocker) {
            ItemsLayoutBox.addItem(getHandler(), this, LayoutType.FLOATING);
        }
    }

    @Override
    public void initElements() {
        // fake tests
        setConfines();
        _init = true;
    }

    public void show(InterfaceItem sender, MouseArgs args) {
        if (!_init)
            initElements();
        if (getX() == -getWidth()) // refactor?
            setX(_stored_offset);
        setVisible(true);
    }

    public void hide() {
        _stored_offset = getX();
        setX(-getWidth());
        setVisible(false);
    }

    protected void onMousePress(InterfaceItem sender, MouseArgs args) {
        _diff_x = args.position.getX() - getX();
        _diff_y = args.position.getY() - getY();
    }

    protected void onDragging(InterfaceItem sender, MouseArgs args) {
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

    @Override
    public void setConfines() {
        setConfines(getX(), getX() + getWidth(), getY(), getY() + getHeight());
    }
}