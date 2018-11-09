package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.InterfaceBaseItem;
import com.spvessel.Core.InterfaceHLayout;
import com.spvessel.Core.InterfaceItem;
import com.spvessel.Core.InterfaceMouseMethodState;
import com.spvessel.Core.MouseArgs;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.Orientation;

public class VerticalSplitArea extends Prototype implements InterfaceHLayout {
    private static int count = 0;
    private InterfaceBaseItem _leftBlock;
    private InterfaceBaseItem _rightBlock;
    private SplitHolder _splitHolder = new SplitHolder(Orientation.VERTICAL);
    private int _leftWidth = 0;
    private int _diff = 0;
    private int _lMin = 0;
    private int _rMin = 0;

    public void setSplitHolderPosition(int position) {
        if (position < _lMin || position > getWidth() - _splitHolder.getHolderSize() - _rMin)
            return;
        _leftWidth = position;
        _splitHolder.setX(position + getX());
        updateLayout();
    }

    public VerticalSplitArea() {
        setItemName("VSplitArea_" + count);
        count++;
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.VerticalSplitArea"));
        setStyle(DefaultsService.getDefaultStyle(VerticalSplitArea.class));

        InterfaceMouseMethodState h_press = (sender, args) -> onMousePress(sender, args);
        _splitHolder.eventMousePressed.add(h_press);
        InterfaceMouseMethodState h_dragg = (sender, args) -> onDragging(sender, args);
        _splitHolder.eventMouseDrag.add(h_dragg);
    }

    protected void onMousePress(InterfaceItem sender, MouseArgs args) {
        _diff = args.position.getX() - _splitHolder.getX();
    }

    public void onDragging(InterfaceItem sender, MouseArgs args) {
        int offset = args.position.getX() - getX() - _diff;
        setSplitHolderPosition(offset);
    }

    @Override
    public void initElements() {
        setSplitHolderPosition((getWidth() - _splitHolder.getHolderSize()) / 2);

        // adding
        addItem(_splitHolder);
        updateLayout();
    }

    public void assignLeftItem(InterfaceBaseItem item) {
        addItem(item);
        _leftBlock = item;
        _lMin = _leftBlock.getMinWidth();
        updateLayout();
    }

    public void assignRightItem(InterfaceBaseItem item) {
        addItem(item);
        _rightBlock = item;
        _rMin = _rightBlock.getMinWidth();
        updateLayout();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        checkMins();
        updateLayout();
    }

    private void checkMins() {
        int totalSize = getWidth() - _splitHolder.getHolderSize();
        if (totalSize < _lMin) {
            setSplitHolderPosition(totalSize);
        } else if (totalSize <= _lMin + _rMin) {
            setSplitHolderPosition(_lMin);
        } else {
            if (totalSize - _leftWidth < _rMin) {
                setSplitHolderPosition(totalSize - _rMin);
            }
        }
    }

    @Override
    public void setX(int _x) {
        super.setX(_x);
        setSplitHolderPosition(_leftWidth);
        updateLayout();
    }

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

    public void setSpacerWidth(int spWidth) {
        _splitHolder.setSpacerSize(spWidth);
    }

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