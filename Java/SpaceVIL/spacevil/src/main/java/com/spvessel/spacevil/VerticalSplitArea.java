package com.spvessel.spacevil;

import java.awt.Color;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceHLayout;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.Orientation;

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
     * Sets position of the SplitHolder
     */
    public void setSplitPosition(int position) {
        if (_init) {
            if (position < _lMin || position > getWidth() - _splitHolder.getHolderSize() - _rMin)
                return;
            _leftWidth = position;
            _splitHolder.setX(position + getX());
            updateLayout();
        } else
            _leftWidth = position;
    }

    public void setSplitColor(Color color) {
        _splitHolder.setBackground(color);
    }

    /**
     * Constructs a VerticalSplitArea
     */
    public VerticalSplitArea() {
        setItemName("VSplitArea_" + count);
        count++;
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.VerticalSplitArea"));
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
     * Initialization and adding of all elements in the VerticalSplitArea
     */
    @Override
    public void initElements() {
        // adding
        addItem(_splitHolder);
        _init = true;
        if (_leftWidth < 0)
            setSplitPosition((getWidth() - _splitHolder.getHolderSize()) / 2);
        else
            setSplitPosition(_leftWidth);
    }

    /**
     * Assign item on the left of the VerticalSplitArea
     */
    public void assignLeftItem(InterfaceBaseItem item) {
        addItem(item);
        _leftBlock = item;
        _lMin = _leftBlock.getMinWidth();
        updateLayout();
    }

    /**
     * Assign item on the right of the VerticalSplitArea
     */
    public void assignRightItem(InterfaceBaseItem item) {
        addItem(item);
        _rightBlock = item;
        _rMin = _rightBlock.getMinWidth();
        updateLayout();
    }

    /**
     * Set width of the VerticalSplitArea
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        checkMins();
        updateLayout();
    }

    private void checkMins() {
        int totalSize = getWidth() - _splitHolder.getHolderSize();
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
     * Set X position of the VerticalSplitArea
     */
    @Override
    public void setX(int _x) {
        super.setX(_x);
        setSplitPosition(_leftWidth);
        updateLayout();
    }

    /**
     * Update all children and VSplitArea sizes and positions according to confines
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

        tmpWidth = getWidth() - tmpWidth - _splitHolder.getHolderSize();

        if (_rightBlock != null) {
            _rightBlock.setX(_leftWidth + getX() + _splitHolder.getHolderSize());
            if (tmpWidth > 0)
                _rightBlock.setWidth(tmpWidth);
            else
                _rightBlock.setWidth(0);
        }

        for (InterfaceBaseItem item : getItems())
            item.setConfines();
    }

    /**
     * Set width of the SplitHolder
     */
    public void SetSplitThickness(int spWidth) {
        _splitHolder.setSpacerSize(spWidth);
    }

    /**
     * Set style of the VerticalSplitArea
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