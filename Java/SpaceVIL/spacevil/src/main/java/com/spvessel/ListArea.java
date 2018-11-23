package com.spvessel;

import java.util.LinkedList;
import java.util.List;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.*;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.SizePolicy;
import com.spvessel.Flags.KeyCode;;

public class ListArea extends Prototype implements InterfaceVLayout {
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

    public InterfaceBaseItem getSelectionItem() {
        return getItems().get(_selection + 2);
    }

    public void setSelection(int index) {
        _selection = index;
        if (index < 0)
            _selection = 0;
        if (index >= getItems().size() - 2)
            _selection = getItems().size() - 3;
        updateLayout();
    }

    public void unselect() {
        _selection = -1;
        _substrate.setVisible(false);
        _hover_substrate.setVisible(false);
    }

    public void setSelectionVisibility(boolean visibility) {
        _show_selection = visibility;
        updateLayout();
    }

    public boolean getSelectionVisibility() {
        return _show_selection;
    }

    private Rectangle _substrate = new Rectangle();

    public Rectangle getSubstrate() {
        return _substrate;
    }

    private boolean _show_hover = true;

    public void setHoverVisibility(boolean visibility) {
        _show_hover = visibility;
        updateLayout();
    }

    public boolean getHoverVisibility() {
        return _show_hover;
    }

    private Rectangle _hover_substrate = new Rectangle();

    public Rectangle getHoverSubstrate() {
        return _hover_substrate;
    }

    // public List<ListPosition> areaPosition = new LinkedList<>();
    // public int firstVisibleItem = 0;
    // public int lastVisibleItem = 0;
    List<Integer> _list_of_visible_items = new LinkedList<>();

    static int count = 0;

    public ListArea() {
        setItemName("ListArea_" + count);
        count++;
        eventMouseClick.add((sender, args) -> onMouseClick(sender, args));
        eventMouseDoubleClick.add((sender, args) -> onMouseDoubleClick(sender, args));
        eventMouseHover.add((sender, args) -> onMouseHover(sender, args));

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.ListArea"));
        setStyle(DefaultsService.getDefaultStyle(ListArea.class));

        eventKeyPress.add((sender, args) -> onKeyPress(sender, args));
    }

    // overrides
    @Override
    public void initElements() {
        _substrate.setVisible(false);
        super.addItems(_substrate, _hover_substrate);
    }

    protected void onMouseClick(InterfaceItem sender, MouseArgs args) {
        for (int index : _list_of_visible_items) {
            InterfaceBaseItem item = getItems().get(index);
            int y = item.getY();
            int h = item.getHeight();
            if (args.position.getY() > y && args.position.getY() < (y + h)) {
                setSelection(index - 2);
                updateSubstrate();
                selectionChanged.execute();
                break;
            }
        }
    }

    public void onMouseDoubleClick(InterfaceItem sender, MouseArgs args) {
        if (getSelectionItem() instanceof Prototype) {
            // System.out.println(getSelectionItem().getItemName());
            ((Prototype) getSelectionItem()).eventMouseDoubleClick.execute(getSelectionItem(), args);
        }
    }

    private void updateSubstrate() {
        if (_show_selection && getSelection() >= 0) {
            InterfaceBaseItem item = getItems().get(getSelection() + 2);
            _substrate.setHeight(item.getHeight() + 4);
            _substrate.setPosition(getX() + getPadding().left, item.getY() - 2);
            _substrate.setVisible(true);
        }
    }

    protected void onMouseHover(InterfaceItem sender, MouseArgs args) {
        if (!getHoverVisibility())
            return;
        boolean found = false;
        for (InterfaceBaseItem item : getItems()) {
            if (item.equals(_substrate) || item.equals(_hover_substrate) || !item.isVisible() || !item.isDrawable())
                continue;
            if (args.position.getY() > item.getY() && args.position.getY() < item.getY() + item.getHeight()) {
                _hover_substrate.setHeight(item.getHeight());
                _hover_substrate.setPosition(getX() + getPadding().left, item.getY());
                _hover_substrate.setDrawable(true);
                found = true;
                break;
            }
        }
        if (!found) {
            _hover_substrate.setDrawable(false);
        }
    }

    protected void onKeyPress(InterfaceItem sender, KeyArgs args) {
        InterfaceBaseItem tmp = getSelectionItem();
        int index = getSelection();
        switch (args.key) {
        case UP:
            index--;
            if (index < 0)
                break;
            if (!getItems().get(index + 2).isVisible())
                while (index >= 0 && !getItems().get(index + 2).isVisible())
                    index--;
            if (index >= 0)
                setSelection(index);
            break;
        case DOWN:
            index++;
            if (index >= getItems().size() - 2)
                break;
            if (!getItems().get(index + 2).isVisible())
                while (index < (getItems().size() - 2) && !getItems().get(index + 2).isVisible())
                    index++;
            if (index < getItems().size() - 2)
                setSelection(index);
            break;
        case ESCAPE:
            unselect();
            break;
        case ENTER:
            if (tmp instanceof Prototype) {
                ((Prototype) tmp).eventKeyPress.execute(sender, args);
                selectionChanged.execute();
            }
            break;
        default:
            if (tmp instanceof Prototype)
                ((Prototype) tmp).eventKeyPress.execute(sender, args);
            break;
        }
    }

    @Override
    public void setMouseHover(boolean value) {
        super.setMouseHover(value);
        if (!value)
            _hover_substrate.setDrawable(false);
    }

    @Override
    public void insertItem(InterfaceBaseItem item, int index) {
        if (item instanceof Prototype) {
            if (!(item instanceof InterfaceTextEditable))
                ((Prototype) item).isFocusable = false;
        }
        item.setDrawable(false);
        super.insertItem(item, index);
        updateLayout();
    }

    @Override
    public void addItem(InterfaceBaseItem item) {
        if (item instanceof Prototype)
            ((Prototype) item).isFocusable = false;
        item.setDrawable(false);
        super.addItem(item);
        updateLayout();
    }

    @Override
    public void removeItem(InterfaceBaseItem item) {
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
        _hover_substrate.setDrawable(false);
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
        _list_of_visible_items.clear();

        long offset = (-1) * getVScrollOffset();
        int startY = getY() + getPadding().top;
        int index = -1;
        for (InterfaceBaseItem child : getItems()) {
            index++;
            if (child.equals(_substrate) || child.equals(_hover_substrate) || !child.isVisible())
                continue;

            child.setX((-1) * (int) _xOffset + getX() + getPadding().left + child.getMargin().left);

            long child_Y = startY + offset + child.getMargin().top;
            offset += child.getHeight() + getSpacing().vertical;

            // top checking
            if (child_Y < startY) {
                if (child_Y + child.getHeight() <= startY) {
                    child.setDrawable(false);
                    if (_selection == index)
                        _substrate.setDrawable(false);
                } else {
                    child.setY((int) child_Y);
                    child.setDrawable(true);
                    _list_of_visible_items.add(index);
                    if (_selection == index - 2)
                        _substrate.setDrawable(true);
                }
                continue;
            }

            // bottom checking
            if (child_Y + child.getHeight() + child.getMargin().bottom > getY() + getHeight() - getPadding().bottom) {
                if (child_Y >= getY() + getHeight() - getPadding().bottom) {
                    child.setDrawable(false);
                    if (_selection == index)
                        _substrate.setDrawable(false);
                } else {
                    child.setY((int) child_Y);
                    child.setDrawable(true);
                    _list_of_visible_items.add(index);
                    if (_selection == index - 2)
                        _substrate.setDrawable(true);
                }
                continue;
            }

            child.setY((int) child_Y);
            child.setDrawable(true);
            _list_of_visible_items.add(index);
            if (_selection == index - 2)
                _substrate.setDrawable(true);

            // refactor
            child.setConfines();
        }
        updateSubstrate();
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
        inner_style = style.getInnerStyle("hovercover");
        if (inner_style != null) {
            _hover_substrate.setStyle(inner_style);
        }
    }
}