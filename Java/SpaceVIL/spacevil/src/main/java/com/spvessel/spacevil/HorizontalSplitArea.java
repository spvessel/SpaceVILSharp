package com.spvessel.spacevil;

import java.awt.Color;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.IItem;
import com.spvessel.spacevil.Core.IVLayout;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.Orientation;

/**
 * HorizontalSplitArea is a container with two divided areas (on top and on
 * bottom). HorizontalSplitArea implements
 * com.spvessel.spacevil.Core.IVLayout.
 * <p>
 * Contains com.spvessel.spacevil.SplitHolder.
 * <p>
 * By default ability to get focus is disabled.
 * <p>
 * Supports all events except drag and drop.
 */
public class HorizontalSplitArea extends Prototype implements IVLayout {
    private static int count = 0;
    private IBaseItem _topBlock;
    private IBaseItem _bottomBlock;
    private SplitHolder _splitHolder = new SplitHolder(Orientation.Horizontal);
    private int _topHeight = -1;
    private int _diff = 0;
    private int _tMin = 0;
    private int _bMin = 0;

    /**
     * Setting position of the split holder.
     * 
     * @param position Position of the split holder.
     */
    public void setSplitPosition(int position) {
        if (_init) {
            if (position < _tMin || position > getHeight() - _splitHolder.getDividerSize() - _bMin)
                return;
            _topHeight = position;
            _splitHolder.setY(position + getY());
            updateLayout();
        } else
            _topHeight = position;
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
     * Defaults HorizontalSplitArea constructor.
     */
    public HorizontalSplitArea() {
        setItemName("HSplitArea_" + count);
        count++;
        setStyle(DefaultsService.getDefaultStyle(HorizontalSplitArea.class));
        isFocusable = false;
        _splitHolder.eventMousePress.add(this::onMousePress);
        _splitHolder.eventMouseDrag.add(this::onDragging);
    }

    private void onMousePress(IItem sender, MouseArgs args) {
        _diff = args.position.getY() - _splitHolder.getY();
    }

    private void onDragging(IItem sender, MouseArgs args) {
        int offset = args.position.getY() - getY() - _diff;
        setSplitPosition(offset);
    }

    private boolean _init = false;

    /**
     * Initializing all elements in the HorizontalSplitArea.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        // Adding
        addItem(_splitHolder);
        _init = true;
        if (_topHeight < 0)
            setSplitPosition((getHeight() - _splitHolder.getDividerSize()) / 2);
        else
            setSplitPosition(_topHeight);
    }

    /**
     * Assign item on the top area of the HorizontalSplitArea.
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     */
    public void assignTopItem(IBaseItem item) {
        addItem(item);
        _topBlock = item;
        _tMin = _topBlock.getMinHeight();
        updateLayout();
    }

    /**
     * Assign item on the bottom area of the HorizontalSplitArea.
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     */
    public void assignBottomItem(IBaseItem item) {
        addItem(item);
        _bottomBlock = item;
        _bMin = _bottomBlock.getMinHeight();
        updateLayout();
    }

    /**
     * Setting HorizontalSplitArea height. If the value is greater/less than the
     * maximum/minimum value of the height, then the height becomes equal to the
     * maximum/minimum value.
     * 
     * @param height Height of the HorizontalSplitArea.
     */
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        checkMins();
        updateLayout();
    }

    private void checkMins() {
        int totalSize = getHeight() - _splitHolder.getDividerSize();
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
     * Setting Y coordinate of the left-top corner of the HorizontalSplitArea.
     * 
     * @param y Y position of the left-top corner.
     */
    @Override
    public void setY(int y) {
        super.setY(y);
        setSplitPosition(_topHeight);
        updateLayout();
    }

    /**
     * Updating all children positions (implementation of
     * com.spvessel.spacevil.Core.IVLayout).
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

        tmpHeight = getHeight() - tmpHeight - _splitHolder.getDividerSize();

        if (_bottomBlock != null) {
            _bottomBlock.setY(_topHeight + getY() + _splitHolder.getDividerSize());
            if (tmpHeight >= 0)
                _bottomBlock.setHeight(tmpHeight);
            else
                _bottomBlock.setHeight(0);
        }

        for (IBaseItem item : getItems())
            item.setConfines();
    }

    /**
     * Setting thickness of SplitHolder divider.
     * 
     * @param spHeight Thickness of SplitHolder divider.
     */
    public void setSplitThickness(int spHeight) {
        _splitHolder.setDividerSize(spHeight);
    }

    /**
     * Setting style of the ButtonCore.
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