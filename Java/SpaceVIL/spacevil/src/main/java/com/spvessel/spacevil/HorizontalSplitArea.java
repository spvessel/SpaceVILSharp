package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.InterfaceVLayout;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.Orientation;

public class HorizontalSplitArea extends Prototype implements InterfaceVLayout {
    private static int count = 0;
    private InterfaceBaseItem _topBlock;
    private InterfaceBaseItem _bottomBlock;
    private SplitHolder _splitHolder = new SplitHolder(Orientation.HORIZONTAL);
    private int _topHeight = 0;
    private int _diff = 0;
    private int _tMin = 0;
    private int _bMin = 0;

    /**
     * Sets position of the SplitHolder
     */
    public void setSplitPosition(int position) {
        if (position < _tMin || position > getHeight() - _splitHolder.getHolderSize() - _bMin)
            return;
        _topHeight = position;
        _splitHolder.setY(position + getY());
        updateLayout();
    }

    /**
     * Constructs a HorizontalSplitArea
     */
    public HorizontalSplitArea() {
        setItemName("HSplitArea_" + count);
        count++;
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.HorizontalSplitArea"));
        setStyle(DefaultsService.getDefaultStyle(HorizontalSplitArea.class));
        isFocusable = false;
        _splitHolder.eventMousePress.add(this::onMousePress);
        _splitHolder.eventMouseDrag.add(this::onDragging);
    }

    private void onMousePress(InterfaceItem sender, MouseArgs args) {
        _diff = args.position.getY() - _splitHolder.getY();
    }

    private void onDragging(InterfaceItem sender, MouseArgs args) {
        int offset = args.position.getY() - getY() - _diff;
        setSplitPosition(offset);
    }

    /**
     * Initialization and adding of all elements in the HorizontalSplitArea
     */
    @Override
    public void initElements() {
        setSplitPosition((getHeight() - _splitHolder.getHolderSize()) / 2);

        // Adding
        addItem(_splitHolder);
        updateLayout();
    }

    /**
     * Assign item on the top of the HorizontalSplitArea
     */
    public void assignTopItem(InterfaceBaseItem item) {
        addItem(item);
        _topBlock = item;
        _tMin = _topBlock.getMinHeight();
        updateLayout();
    }

    /**
     * Assign item on the bottom of the HorizontalSplitArea
     */
    public void assignBottomItem(InterfaceBaseItem item) {
        addItem(item);
        _bottomBlock = item;
        _bMin = _bottomBlock.getMinHeight();
        updateLayout();
    }

    /**
     * Set height of the HorizontalSplitArea
     */
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        checkMins();
        updateLayout();
    }

    private void checkMins() {
        int totalSize = getHeight() - _splitHolder.getHolderSize();
        if (totalSize < _tMin) {
            setSplitPosition(totalSize);
        } else if (totalSize <= _tMin + _bMin) {
            setSplitPosition(_tMin);
        } else {
            if (totalSize - _topHeight < _bMin) {
                setSplitPosition(totalSize - _bMin);
            }
        }
    }

    /**
     * Set Y position of the HorizontalSplitArea
     */
    @Override
    public void setY(int _y) {
        super.setY(_y);
        setSplitPosition(_topHeight);
        updateLayout();
    }

    /**
     * Update all children and HSplitArea sizes and positions
     * according to confines
     */
    public void updateLayout() {
        _splitHolder.setWidth(getWidth());

        int tmpHeight = _topHeight;

        if (_topBlock != null) {
            _topBlock.setY(getY() + getPadding().top);
            if (tmpHeight >= 0)
                _topBlock.setHeight(tmpHeight);
            else
                _topBlock.setHeight(0);
        }

        tmpHeight = getHeight() - tmpHeight - _splitHolder.getHolderSize();

        if (_bottomBlock != null) {
            _bottomBlock.setY(_topHeight + getY() + _splitHolder.getHolderSize());
            if (tmpHeight >= 0)
                _bottomBlock.setHeight(tmpHeight);
            else
                _bottomBlock.setHeight(0);
        }

        for (InterfaceBaseItem item : getItems())
            item.setConfines();
    }

    /**
     * Set height of the SplitHolder
     */
    public void setSplitThickness(int spHeight) {
        _splitHolder.setSpacerSize(spHeight);
    }

    /**
     * Set style of the HorizontalSplitArea
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