package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.*;
import com.spvessel.Decorations.Style;

public class ListArea extends VisualItem implements InterfaceVLayout {
    public EventCommonMethod selectionChanged = new EventCommonMethod();
    public EventCommonMethod itemListChanged = new EventCommonMethod();

    private int _step = 15;

    public void setStep(int value) {
        _step = value;
    }

    public int getStep() {
        return _step;
    }

    private boolean _show_selection = true;
    private int _selection = -1;

    public int getSelection() {
        return _selection;
    }

    public BaseItem getSelectionItem() {
        return getItems().get(_selection + 1);
    }

    public void setSelection(int index) {
        _selection = index;
        selectionChanged.execute();
        updateLayout();
    }

    public void unselect() {
        _selection = -1;
        _substrate.setVisible(false);
    }

    public void setSelectionVisibility(boolean visibility) {
        _show_selection = visibility;
        updateLayout();
    }

    public boolean getSelectionVisibility() {
        return _show_selection;
    }

    private CustomShape _substrate = new CustomShape();

    public CustomShape getSubstrate() {
        return _substrate;
    }

    public void setSubstrate(CustomShape shape) {
        _substrate = shape;
        updateLayout();
    }

    // public List<ListPosition> areaPosition = new LinkedList<>();
    public int firstVisibleItem = 0;
    public int lastVisibleItem = 0;

    static int count = 0;

    public ListArea() {
        setItemName("ListArea_" + count);
        count++;
        InterfaceMouseMethodState click = (sender, args) -> onMouseClick(sender, args);
        eventMouseClick.add(click);

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.ListArea"));
        setStyle(DefaultsService.getDefaultStyle(ListArea.class));
    }

    // overrides
    @Override
    public void initElements() {
        _substrate.setVisible(false);
        ;
        super.addItem(_substrate);
    }

    public void onMouseClick(InterfaceItem sender, MouseArgs args) {
        for (int i = firstVisibleItem; i <= lastVisibleItem; i++) {
            if (i == getItems().size())
                break;

            if (getItems().get(i).equals(_substrate))
                continue;

            int y = getItems().get(i).getY();
            int h = getItems().get(i).getHeight();
            if (args.position.getY() > y && args.position.getY() < (y + h)) {
                setSelection(i - 1);
                break;
            }
        }
    }

    @Override
    public void insertItem(BaseItem item, int index) {
        item.isDrawable = false;
        super.insertItem(item, index);
        updateLayout();
    }

    @Override
    public void addItem(BaseItem item) {
        item.isDrawable = false;
        super.addItem(item);
        updateLayout();
    }

    @Override
    public void removeItem(BaseItem item) {
        unselect();
        super.removeItem(item);
        updateLayout();
        itemListChanged.execute();
    }

    @Override
    public void setY(int _y) {
        super.setY(_y);
        updateLayout();
    }

    // update content position
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

    public void updateLayout() {
        // areaPosition.clear();

        long offset = (-1) * getVScrollOffset();
        int startY = getY() + getPadding().top;
        int index = 0;
        for (BaseItem child : getItems()) {
            if (child.equals(_substrate) || !child.getVisible())
                continue;

            child.setX((-1) * (int) _xOffset + getX() + getPadding().left + child.getMargin().left);

            long child_Y = startY + offset + child.getMargin().top;
            offset += child.getHeight() + getSpacing().vertical;

            // top checking
            if (child_Y < startY) {
                // areaPosition.add(ListPosition.TOP);
                if (child_Y + child.getHeight() <= startY) {
                    child.isDrawable = false;
                    if (_selection == index)
                        _substrate.isDrawable = false;
                } else {
                    child.setY((int) child_Y);
                    child.isDrawable = true;
                    firstVisibleItem = index + 1;
                    if (_selection == index)
                        setSubstrate(child);
                }
                index++;
                continue;
            }

            // bottom checking
            if (child_Y + child.getHeight() + child.getMargin().bottom > getY() + getHeight() - getPadding().bottom) {
                // areaPosition.add(ListPosition.BOTTOM);
                if (child_Y >= getY() + getHeight() - getPadding().bottom) {
                    child.isDrawable = false;
                    if (_selection == index)
                        _substrate.setVisible(false);
                } else {
                    child.setY((int) child_Y);
                    child.isDrawable = true;
                    lastVisibleItem = index + 1;
                    if (_selection == index)
                        setSubstrate(child);
                }
                index++;
                continue;
            }

            child.setY((int) child_Y);
            child.isDrawable = true;
            lastVisibleItem = index + 1;
            if (_selection == index)
                setSubstrate(child);
            index++;

            // refactor
            child.setConfines();
        }
    }

    private void setSubstrate(BaseItem child) {
        if (!_show_selection) {
            _substrate.setVisible(false);
            return;
        }

        _substrate.setVisible(true);
        _substrate.setHeight(child.getHeight() + 2);
        _substrate.setMargin(-getParent().getPadding().left, -getParent().getPadding().top,
                -getParent().getPadding().right, -getParent().getPadding().bottom);
        _substrate.setTriangles(GraphicsMathService.getRoundSquare(child.getWidth(), _substrate.getHeight(), 0,
                _substrate.getX(), _substrate.getY()));
        _substrate.setY(child.getY() - 1);
    }

    // style
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("substrate");
        if (inner_style != null) {
            _substrate.setStyle(inner_style);
        }
    }
}