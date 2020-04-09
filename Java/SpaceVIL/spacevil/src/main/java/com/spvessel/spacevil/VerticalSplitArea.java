package com.spvessel.spacevil;

import java.awt.Color;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceHLayout;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.Orientation;

/**
 * VerticalSplitArea is a container with two divided areas (on top and on
 * bottom). VerticalSplitArea implements
 * com.spvessel.spacevil.Core.InterfaceHLayout.
 * <p>
 * Contains com.spvessel.spacevil.SplitHolder.
 * <p>
 * By default ability to get focus is disabled.
 * <p>
 * Supports all events except drag and drop.
 */
public class VerticalSplitArea extends Prototype implements InterfaceHLayout {
    private static int count = 0;
    private InterfaceBaseItem _leftBlock;
    private InterfaceBaseItem _rightBlock;
    private SplitHolder _splitHolder = new SplitHolder(Orientation.VERTICAL);
    private int _leftWidth = -1;
    private int _diff = 0;
    private int _lMin = 0;
    private int _rMin = 0;

    /**
     * Setting position of the split holder.
     * 
     * @param position Position of the split holder.
     */
    public void setSplitPosition(int position) {
        if (_init) {
            if (position < _lMin || position > getWidth() - _splitHolder.getDividerSize() - _rMin)
                return;
            _leftWidth = position;
            _splitHolder.setX(position + getX());
            updateLayout();
        } else
            _leftWidth = position;
    }

    /**
     * Setting split holder color.
     * 
     * @param color Split holder color as java.awt.Color.
     */
    public void setSplitColor(Color color) {
        _splitHolder.setBackground(color);
    }

    /**
     * Defaults VerticalSplitArea constructor.
     */
    public VerticalSplitArea() {
        setItemName("VSplitArea_" + count);
        count++;
        setStyle(DefaultsService.getDefaultStyle(VerticalSplitArea.class));
        isFocusable = false;
        _splitHolder.eventMousePress.add(this::onMousePress);
        _splitHolder.eventMouseDrag.add(this::onDragging);
    }

    private void onMousePress(InterfaceItem sender, MouseArgs args) {
        _diff = args.position.getX() - _splitHolder.getX();
    }

    private void onDragging(InterfaceItem sender, MouseArgs args) {
        int offset = args.position.getX() - getX() - _diff;
        setSplitPosition(offset);
    }

    private boolean _init = false;

    /**
     * Initializing all elements in the VerticalSplitArea.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        // adding
        addItem(_splitHolder);
        _init = true;
        if (_leftWidth < 0)
            setSplitPosition((getWidth() - _splitHolder.getDividerSize()) / 2);
        else
            setSplitPosition(_leftWidth);
    }

    /**
     * Assign item on the left area of the VerticalSplitArea.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    public void assignLeftItem(InterfaceBaseItem item) {
        addItem(item);
        _leftBlock = item;
        _lMin = _leftBlock.getMinWidth();
        updateLayout();
    }

    /**
     * Assign item on the right area of the VerticalSplitArea.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    public void assignRightItem(InterfaceBaseItem item) {
        addItem(item);
        _rightBlock = item;
        _rMin = _rightBlock.getMinWidth();
        updateLayout();
    }

    /**
     * Setting VerticalSplitArea width. If the value is greater/less than the
     * maximum/minimum value of the width, then the width becomes equal to the
     * maximum/minimum value.
     * 
     * @param width Width of the VerticalSplitArea.
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        checkMins();
        updateLayout();
    }

    private void checkMins() {
        int totalSize = getWidth() - _splitHolder.getDividerSize();
        if (totalSize < _lMin) {
            setSplitPosition(totalSize);
        } else if (totalSize <= _lMin + _rMin) {
            setSplitPosition(_lMin);
        } else {
            if (totalSize - _leftWidth < _rMin) {
                setSplitPosition(totalSize - _rMin);
            }
        }
    }

    /**
     * Setting X coordinate of the left-top corner of the VerticalSplitArea.
     * 
     * @param x X position of the left-top corner.
     */
    @Override
    public void setX(int x) {
        super.setX(x);
        setSplitPosition(_leftWidth);
        updateLayout();
    }

    /**
     * Updating all children positions (implementation of
     * com.spvessel.spacevil.Core.InterfaceHLayout).
     */
    public void updateLayout() {
        _splitHolder.setHeight(getHeight());

        int tmpWidth = _leftWidth - getPadding().left;

        if (_leftBlock != null) {
            _leftBlock.setX(getX() + getPadding().left);
            if (tmpWidth > 0)
                _leftBlock.setWidth(tmpWidth);
            else
                _leftBlock.setWidth(0);
        }

        tmpWidth = getWidth() - tmpWidth - _splitHolder.getDividerSize();

        if (_rightBlock != null) {
            _rightBlock.setX(_leftWidth + getX() + _splitHolder.getDividerSize());
            if (tmpWidth > 0)
                _rightBlock.setWidth(tmpWidth);
            else
                _rightBlock.setWidth(0);
        }

        for (InterfaceBaseItem item : getItems())
            item.setConfines();
    }

    /**
     * Setting thickness of SplitHolder divider.
     * 
     * @param thickness Thickness of SplitHolder divider.
     */
    public void SetSplitThickness(int thickness) {
        _splitHolder.setDividerSize(thickness);
    }

    /**
     * Setting style of the VerticalSplitArea.
     * <p>
     * Inner styles: "splitholder".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("splitholder");
        if (inner_style != null) {
            _splitHolder.setStyle(inner_style);
        }
    }
}