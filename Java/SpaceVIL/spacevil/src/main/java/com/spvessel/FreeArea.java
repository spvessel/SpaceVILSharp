package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.*;

import java.util.Map;
import java.util.HashMap;

public class FreeArea extends VisualItem implements InterfaceGrid, InterfaceDraggable {
    static int count = 0;
    private int _x_press = 0;
    private int _y_press = 0;
    private int _diff_x = 0;
    private int _diff_y = 0;
    Map<BaseItem, int[]> _stored_crd;

    // public ContextMenu _dropdownmenu = new ContextMenu();
    public FreeArea() {
        setItemName("FreeArea_" + count);
        count++;
        _stored_crd = new HashMap<BaseItem, int[]>();

        InterfaceMouseMethodState click = (sender, args) -> onMouseRelease(sender, args);
        eventMouseClick.add(click);
        InterfaceMouseMethodState press = (sender, args) -> onMousePress(sender, args);
        eventMousePressed.add(press);
        InterfaceMouseMethodState dragg = (sender, args) -> onDragging(sender, args);
        eventMouseDrag.add(dragg);

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.FreeArea"));
        setStyle(DefaultsService.getDefaultStyle(FreeArea.class));
    }

    public void addContextMenu(ContextMenu context_menu) {
        InterfaceMouseMethodState context_show = (sender, args) -> context_menu.show(sender, args);
        eventMouseClick.add(context_show);
    }

    protected void onMousePress(InterfaceItem sender, MouseArgs args) {
        _x_press = args.position.getX();
        _y_press = args.position.getY();
        _diff_x = (int) _xOffset;
        _diff_y = (int) _yOffset;
    }

    protected void onMouseRelease(InterfaceItem sender, MouseArgs args) {
        // PrintArgs.MouseArgs(args);
        // if (args.Button == MouseButton.ButtonRight)
        // ShowDropDownList(args);
    }

    protected void onDragging(InterfaceItem sender, MouseArgs args) {
        _xOffset = _diff_x - _x_press + args.position.getX();
        _yOffset = _diff_y + args.position.getY() - _y_press;
        updateLayout();
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
    @Override
    public void addItem(BaseItem item) {
        super.addItem(item);
        synchronized (this) {
            _stored_crd.put(item, new int[] { item.getX(), item.getY() });
        }
        if (item instanceof ResizableItem) {
            ResizableItem wanted = (ResizableItem) item;
            InterfaceCommonMethod changed = () -> correctPosition(wanted);
            wanted.positionChanged.add(changed);
        }
        updateLayout();
    }

    @Override
    public void removeItem(BaseItem item) {
        super.removeItem(item);
        synchronized (this) {
            _stored_crd.remove(item);
        }
        updateLayout();
    }

    public void updateLayout() {
        // synchronized (this) {
            for (BaseItem child : getItems()) {
                child.setX((int) _xOffset + getX() + getPadding().left + _stored_crd.get(child)[0]
                        + child.getMargin().left);
                child.setY(
                        (int) _yOffset + getY() + getPadding().top + _stored_crd.get(child)[1] + child.getMargin().top);
            }
        // }
    }

    // ContexMenu
    private void correctPosition(ResizableItem item) {
        int actual_x = item.getX();
        // int stored_x = _stored_crd.get(item)[0];
        int actual_y = item.getY();
        // int stored_y = _stored_crd.get(item)[1];
        synchronized (this) {
            _stored_crd.remove(item);
            _stored_crd.put(item,
                    new int[] { actual_x - (int) _xOffset - getX() - getPadding().left - item.getMargin().left,
                            actual_y - (int) _yOffset - getY() - getPadding().top - item.getMargin().top });
        }
    }
}