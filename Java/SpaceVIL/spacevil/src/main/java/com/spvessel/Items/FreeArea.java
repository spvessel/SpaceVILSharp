package com.spvessel.Items;

import java.util.Map;
import java.util.HashMap;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Cores.InterfaceCommonMethod;
import com.spvessel.Cores.InterfaceDraggable;
import com.spvessel.Cores.InterfaceGrid;
import com.spvessel.Cores.InterfaceItem;
import com.spvessel.Cores.InterfaceMouseMethodState;
import com.spvessel.Cores.MouseArgs;

public class FreeArea extends VisualItem implements InterfaceGrid, InterfaceDraggable {
    static int count = 0;
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

        setStyle(DefaultsService.getDefaultStyle("SpaceVIL.FreeArea"));
    }

    public void addContextMenu(ContextMenu context_menu) {
        InterfaceMouseMethodState context_show = (sender, args) -> context_menu.show(sender, args);
        eventMouseClick.add(context_show);
    }

    protected void onMousePress(InterfaceItem sender, MouseArgs args) {

    }

    protected void onMouseRelease(InterfaceItem sender, MouseArgs args) {
        // PrintArgs.MouseArgs(args);
        // if (args.Button == MouseButton.ButtonRight)
        // ShowDropDownList(args);
    }

    protected void onDragging(InterfaceItem sender, MouseArgs args) {
        _xOffset -= _mouse_ptr.PrevX - _mouse_ptr.X;
        _yOffset -= _mouse_ptr.PrevY - _mouse_ptr.Y;
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
        _stored_crd.put(item, new int[] { item.getX(), item.getY() });
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
        _stored_crd.remove(item);
    }

    public void updateLayout() {
        for (BaseItem child : getItems()) {
            child.setX(
                    (int) _xOffset + getX() + getPadding().left + _stored_crd.get(child)[0] + child.getMargin().left);
            child.setY((int) _yOffset + getY() + getPadding().top + _stored_crd.get(child)[1] + child.getMargin().top);
        }
    }

    // ContexMenu
    private void correctPosition(ResizableItem item) {
        int actual_x = item.getX();
        // int stored_x = _stored_crd.get(item)[0];
        int actual_y = item.getY();
        // int stored_y = _stored_crd.get(item)[1];

        _stored_crd.remove(item);
        _stored_crd.put(item, new int[] 
        { 
            actual_x - (int) _xOffset - getX() - getPadding().left - item.getMargin().left,
            actual_y - (int) _yOffset - getY() - getPadding().top - item.getMargin().top 
        });
    }
}