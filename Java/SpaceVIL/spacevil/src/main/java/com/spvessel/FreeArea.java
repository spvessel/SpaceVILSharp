package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.*;
import com.spvessel.Flags.MouseButton;

import java.util.Map;
import java.util.HashMap;

public class FreeArea extends Prototype implements InterfaceGrid, InterfaceDraggable {
    private static int count = 0;
    private int _x_press = 0;
    private int _y_press = 0;
    private int _diff_x = 0;
    private int _diff_y = 0;
    private Map<InterfaceBaseItem, int[]> _stored_crd;

    // public ContextMenu _dropdownmenu = new ContextMenu();
    /**
     * Constructs a FreeArea
     */
    public FreeArea() {
        setItemName("FreeArea_" + count);
        count++;
        _stored_crd = new HashMap<>();

        eventMousePress.add(this::onMousePress);
        eventMouseDrag.add(this::onDragging);

        setStyle(DefaultsService.getDefaultStyle(FreeArea.class));
    }

    /**
     * Add ContextMenu to the FreeArea
     */
    public void addContextMenu(ContextMenu context_menu) {
        //InterfaceMouseMethodState context_show = (sender, args) -> context_menu.show(sender, args);
        eventMouseClick.add(context_menu::show);
    }

    private void onMousePress(InterfaceItem sender, MouseArgs args) {
        if (args.button == MouseButton.BUTTON_LEFT) {
            _x_press = args.position.getX();
            _y_press = args.position.getY();
            _diff_x = (int) _xOffset;
            _diff_y = (int) _yOffset;
        }
    }

    private void onDragging(InterfaceItem sender, MouseArgs args) {
        if (args.button == MouseButton.BUTTON_LEFT) {
            _xOffset = _diff_x - _x_press + args.position.getX();
            _yOffset = _diff_y + args.position.getY() - _y_press;
            updateLayout();
        }
    }

    private long _yOffset = 0;
    private long _xOffset = 0;

    public long getVScrollOffset() {
        return _yOffset;
    }

    public void setVScrollOffset(long value) {
        _yOffset = value;
        updateLayout();
    }

    public long getHScrollOffset() {
        return _xOffset;
    }

    public void setHScrollOffset(long value) {
        _xOffset = value;
        updateLayout();
    }

    // overrides
    /**
     * Add item to the FreeArea
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        super.addItem(item);
        _stored_crd.put(item, new int[] { item.getX(), item.getY() });
        if (item instanceof ResizableItem) {
            ResizableItem wanted = (ResizableItem) item;
            wanted.positionChanged.add(() -> correctPosition(wanted));
        }
        updateLayout();
    }

    /**
     * Remove item from the FreeArea
     */
    @Override
    public void removeItem(InterfaceBaseItem item) {
        super.removeItem(item);
        synchronized (this) {
            _stored_crd.remove(item);
        }
        updateLayout();
    }

    /**
     * Update all children elements positions
     */
    public void updateLayout() {
        // synchronized (this) {
        for (InterfaceBaseItem child : getItems()) {
            child.setX(
                    (int) _xOffset + getX() + getPadding().left + _stored_crd.get(child)[0] + child.getMargin().left);
            child.setY((int) _yOffset + getY() + getPadding().top + _stored_crd.get(child)[1] + child.getMargin().top);
        }
        // }
    }

    // ContexMenu
    private void correctPosition(ResizableItem item) {
        int actual_x = item.getX();
        // int stored_x = _stored_crd.get(item)[0];
        int actual_y = item.getY();
        // int stored_y = _stored_crd.get(item)[1];
        // synchronized (this) {
            _stored_crd.remove(item);
            _stored_crd.put(item,
                    new int[] { actual_x - (int) _xOffset - getX() - getPadding().left - item.getMargin().left,
                            actual_y - (int) _yOffset - getY() - getPadding().top - item.getMargin().top });
        // }
    }
}