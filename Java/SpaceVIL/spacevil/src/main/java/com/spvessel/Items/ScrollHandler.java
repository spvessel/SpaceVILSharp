package com.spvessel.Items;

import com.spvessel.Cores.InterfaceDraggable;
import com.spvessel.Cores.InterfaceItem;
import com.spvessel.Cores.InterfaceMouseMethodState;
import com.spvessel.Cores.MouseArgs;
import com.spvessel.Flags.*;

public class ScrollHandler extends VisualItem implements InterfaceDraggable {
    static int count = 0;
    public Orientation direction;
    private int _offset = 0;
    private int _diff = 0;

    public ScrollHandler() {
        setItemName("ScrollHandler_" + count);
        //InterfaceMouseMethodState h_press = (sender, args) -> onMousePress(sender, args);
        eventMousePressed.add(this::onMousePress); //h_press);
        //InterfaceMouseMethodState h_dragg = (sender, args) -> onDragging(sender, args);
        eventMouseDrag.add(this::onDragging); //h_dragg);
        count++;
    }

    protected void onMousePress(InterfaceItem sender, MouseArgs args) {
        if (direction == Orientation.HORIZONTAL)
            _diff = args.position.X - getX();
        else
            _diff = args.position.Y - getY();
    }

    protected void onDragging(InterfaceItem sender, MouseArgs args) {
        int offset;

        if (direction == Orientation.HORIZONTAL)
            offset = args.position.X - getParent().getX() - _diff;
        else
            offset = args.position.Y - getParent().getY() - _diff;

        setOffset(offset);
    }

    public void setOffset(int offset) {
        if (getParent() == null)
            return;

        _offset = offset;

        if (direction == Orientation.HORIZONTAL)
            setX(_offset + getParent().getX());
        else
            setY(_offset + getParent().getY());
    }
}