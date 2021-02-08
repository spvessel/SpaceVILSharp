package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.IItem;
import com.spvessel.spacevil.Core.KeyArgs;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.VisibilityPolicy;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 * ListBox is a container for com.spvessel.spacevil.ListArea (scrollable
 * container for other elements with ability of selection) and scroll bars.
 * ListBox controls scrolling, resizing and other actions of
 * com.spvessel.spacevil.ListArea.
 * <p>
 * Contains list area, scroll bars, menu button, navigation context menu.
 * <p>
 * Supports all events except drag and drop.
 */
public class ListBox extends Prototype {
    private static int count = 0;

    /**
     * Setting scroll movement step.
     * 
     * @param step Scroll step.
     */
    public void setScrollStep(int step) {
        _area.setStep(step);
    }

    /**
     * Getting scroll movement step.
     * 
     * @return Scroll step.
     */
    public int getScrollStep() {
        return _area.getStep();
    }

    /**
     * Getting index of selected item.
     * 
     * @return Index of selected item.
     */
    public int getSelection() {
        return _area.getSelection();
    }

    /**
     * Select item by index.
     * 
     * @param index Index of selection.
     */
    public void setSelection(int index) {
        _area.setSelection(index);
    }

    /**
     * Unselect selected item.
     */
    public void unselect() {
        _area.unselect();
    }

    /**
     * Enable or disable selection ability of ListArea.
     * 
     * @param value True: if you want selection ability of ListArea to be enabled.
     *              False: if you want selection ability of ListArea to be disabled.
     */
    public void setSelectionVisible(boolean value) {
        _area.setSelectionVisible(value);
    }

    /**
     * Returns True if selection ability of ListArea is enabled otherwise returns
     * False.
     * 
     * @return True: selection ability of ListArea is enabled. False: selection
     *         ability of ListArea is disabled.
     */
    public boolean isSelectionVisible() {
        return _area.isSelectionVisible();
    }

    /**
     * Interactive item to show the navigation context menu.
     */
    public BlankItem menu = new BlankItem();
    private boolean _isMenuDisabled = false;

    /**
     * Setting the navigation context menu to disable or enable.
     * 
     * @param value True: if you want to disable navigation context menu. False: if
     *              you want to enable navigation context menu.
     */
    public void disableMenu(boolean value) {
        _isMenuDisabled = value;
    }

    private Grid _grid = new Grid(2, 2);
    private ListArea _area = new ListArea();

    /**
     * Getting list area of ListBox.
     * 
     * @return List area as com.spvessel.spacevil.ListArea.
     */
    public ListArea getArea() {
        return _area;
    }

    private ContextMenu _menu;

    /**
     * Vertical scroll bar of ListBox.
     */
    public VerticalScrollBar vScrollBar = new VerticalScrollBar();

    /**
     * Horizontal scroll bar of ListBox.
     */
    public HorizontalScrollBar hScrollBar = new HorizontalScrollBar();

    private VisibilityPolicy _vScrollBarPolicy = VisibilityPolicy.AsNeeded;
    private VisibilityPolicy _hScrollBarPolicy = VisibilityPolicy.AsNeeded;

    /**
     * Getting vertical scroll bar visibility policy.
     * 
     * @return Visibility policy as com.spvessel.spacevil.Flags.VisibilityPolicy.
     */
    public VisibilityPolicy getVScrollBarPolicy() {
        return _vScrollBarPolicy;
    }

    /**
     * Setting vertical scroll bar visibility policy.
     * <p>
     * Default: com.spvessel.spacevil.Flags.VisibilityPolicy.AS_NEEDED.
     * 
     * @param policy Visibility policy as
     *               com.spvessel.spacevil.Flags.VisibilityPolicy.
     */
    public void setVScrollBarPolicy(VisibilityPolicy policy) {
        _vScrollBarPolicy = policy;

        if (policy == VisibilityPolicy.Never) {
            vScrollBar.setDrawable(false);
            menu.setVisible(false);
        } else if (policy == VisibilityPolicy.AsNeeded) {
            vScrollBar.setDrawable(false);
            menu.setVisible(false);
        } else if (policy == VisibilityPolicy.Always) {
            vScrollBar.setDrawable(true);
            if (!hScrollBar.isDrawable())
                menu.setVisible(false);
            else
                menu.setVisible(true);
        }

        _grid.updateLayout();
        updateVerticalSlider();
        vScrollBar.slider.updateHandler();
    }

    /**
     * Getting horizontal scroll bar visibility policy.
     * 
     * @return Visibility policy as com.spvessel.spacevil.Flags.VisibilityPolicy.
     */
    public VisibilityPolicy getHScrollBarPolicy() {
        return _hScrollBarPolicy;
    }

    /**
     * Setting horizontal scroll bar visibility policy.
     * <p>
     * Default: com.spvessel.spacevil.Flags.VisibilityPolicy.AS_NEEDED.
     * 
     * @param policy Visibility policy as
     *               com.spvessel.spacevil.Flags.VisibilityPolicy.
     */
    public void setHScrollBarPolicy(VisibilityPolicy policy) {
        _hScrollBarPolicy = policy;

        if (policy == VisibilityPolicy.Never) {
            hScrollBar.setDrawable(false);
            menu.setVisible(false);
        } else if (policy == VisibilityPolicy.AsNeeded) {
            hScrollBar.setDrawable(false);
            menu.setVisible(false);
        } else if (policy == VisibilityPolicy.Always) {
            hScrollBar.setDrawable(true);
            if (!vScrollBar.isDrawable())
                menu.setVisible(false);
            else
                menu.setVisible(true);
        }

        _grid.updateLayout();
        updateHorizontalSlider();
        hScrollBar.slider.updateHandler();
    }

    /**
     * Default ListBox constructor.
     */
    public ListBox() {
        setItemName("ListBox_" + count);
        count++;
        setStyle(DefaultsService.getDefaultStyle(ListBox.class));

        // VBar
        vScrollBar.setDrawable(true);
        vScrollBar.setItemName(getItemName() + "_vScrollBar");
        // HBar
        hScrollBar.setDrawable(true);
        hScrollBar.setItemName(getItemName() + "_hScrollBar");
        // Area
        _area.setItemName(getItemName() + "_area");
        // Menu
        menu.setItemName(getItemName() + "_menu");

        eventMouseClick.add((sender, args) -> {
            // зачем нужен этот if???
            if (_area.getSelection() >= 0)
                _area.setSelection(_area.getSelection());
            _area.setFocus();
        });
        eventKeyPress.add(this::onKeyPress);
    }

    private boolean isOutsideArea(long y, int h, Indents margin) {
        int startY = _area.getY() + getPadding().top;
        int endY = _area.getY() + getHeight() - getPadding().bottom;
        if (y + h < startY || y - margin.top > endY)
            return true;
        return false;
    }

    private void onKeyPress(IItem sender, KeyArgs args) {
        SelectionItem selection = _area.getTrueSelection();
        if (selection == null)
            return;
        long offset = _area.getVScrollOffset();
        int startY = _area.getY() + getPadding().top;
        long selectionY = selection.getY() + selection.getMargin().top;

        switch (args.key) {
            case Up:
                if (isOutsideArea(selectionY, selection.getHeight(), selection.getMargin())) {
                    _area.setVScrollOffset(offset - (startY - selectionY));
                    updateVerticalSlider();
                    break;
                }
                if (selectionY < startY) {
                    _area.setVScrollOffset(offset - (startY - selectionY));
                    updateVerticalSlider();
                }
                break;
            case Down:
                if (isOutsideArea(selectionY, selection.getHeight(), selection.getMargin())) {
                    // _area.setVScrollOffset(offset - (startY - selection_Y));
                    _area.setVScrollOffset(
                            offset + selection.getHeight() + (selection.getY() - (_area.getY() + _area.getHeight())));
                    updateVerticalSlider();
                    break;
                }
                if (selectionY + selection.getHeight() + selection.getMargin().bottom > getY() + getHeight()
                        - getPadding().bottom) {
                    _area.setVScrollOffset(offset + ((selectionY + selection.getHeight() + selection.getMargin().bottom
                            + _area.getSpacing().vertical) - (getY() + getHeight() - getPadding().bottom)));
                    updateVerticalSlider();
                }
                break;
            default:
                break;
        }
    }

    private long _vSize = 0;
    private long _hSize = 0;

    private void updateVListArea() {
        // vertical slider
        float vValue = vScrollBar.slider.getCurrentValue();
        int vOffset = (int) Math.round((float) (_vSize * vValue) / 100.0f);
        _area.setVScrollOffset(vOffset);
    }

    private void updateHListArea() {
        // horizontal slider
        float hValue = hScrollBar.slider.getCurrentValue();
        int hOffset = (int) Math.round((float) (_hSize * hValue) / 100.0f);
        _area.setHScrollOffset(hOffset);
    }

    // vertical slider
    private void updateVerticalSlider() {
        int totalInvisibleSize = 0;
        int visibleArea = _area.getHeight() - _area.getPadding().top - _area.getPadding().bottom;
        if (visibleArea < 0)
            visibleArea = 0;
        List<IBaseItem> list = _area.getItems();
        for (IBaseItem item : list) {
            if (!item.isVisible())
                continue;
            totalInvisibleSize += (item.getHeight() + _area.getSpacing().vertical);
        }
        int total = totalInvisibleSize - _area.getSpacing().vertical;
        if (totalInvisibleSize <= visibleArea) {
            vScrollBar.slider.handler.setDrawable(false);
            vScrollBar.slider.setStep(vScrollBar.slider.getMaxValue());
            _vSize = 0;
            vScrollBar.slider.setCurrentValue(0);
            if (getVScrollBarPolicy() == VisibilityPolicy.AsNeeded) {
                vScrollBar.setDrawable(false);
                menu.setVisible(false);
                _grid.updateLayout();
            }
            return;
        }
        if (getVScrollBarPolicy() == VisibilityPolicy.AsNeeded) {
            vScrollBar.setDrawable(true);
            if (!hScrollBar.isDrawable())
                menu.setVisible(false);
            else
                menu.setVisible(true);
            _grid.updateLayout();
        }
        vScrollBar.slider.handler.setDrawable(true);
        totalInvisibleSize -= visibleArea;
        _vSize = totalInvisibleSize;

        if (totalInvisibleSize > 0) {
            float sizeHandler = (float) (visibleArea) / (float) total * 100.0f;
            sizeHandler = (float) vScrollBar.slider.getHeight() / 100.0f * sizeHandler;
            // size of handler
            vScrollBar.slider.handler.setHeight((int) sizeHandler);
        }
        // step of slider
        float stepCount = totalInvisibleSize / _area.getStep();
        vScrollBar.slider.setStep((vScrollBar.slider.getMaxValue() - vScrollBar.slider.getMinValue()) / stepCount);
        vScrollBar.slider.setCurrentValue((100.0f / totalInvisibleSize) * _area.getVScrollOffset());
    }

    // horizontal slider
    private void updateHorizontalSlider() {
        int maxSize = 0;
        int visibleArea = _area.getWidth() - _area.getPadding().left - _area.getPadding().right;
        if (visibleArea < 0)
            visibleArea = 0;
        List<IBaseItem> list = _area.getItems();
        for (IBaseItem item : list) {
            if (!item.isVisible())
                continue;
            if (maxSize < item.getWidth() + item.getMargin().left + item.getMargin().right)
                maxSize = item.getWidth() + item.getMargin().left + item.getMargin().right;
        }
        if (maxSize <= visibleArea) {
            hScrollBar.slider.handler.setDrawable(false);
            hScrollBar.slider.setStep(hScrollBar.slider.getMaxValue());
            _hSize = 0;
            hScrollBar.slider.setCurrentValue(0);
            if (getHScrollBarPolicy() == VisibilityPolicy.AsNeeded) {
                hScrollBar.setDrawable(false);
                menu.setVisible(false);
                _grid.updateLayout();
            }
            return;
        }
        if (getHScrollBarPolicy() == VisibilityPolicy.AsNeeded) {
            hScrollBar.setDrawable(true);
            if (!vScrollBar.isDrawable())
                menu.setVisible(false);
            else
                menu.setVisible(true);
            _grid.updateLayout();
        }
        hScrollBar.slider.handler.setDrawable(true);
        int totalInvisibleSize = maxSize - visibleArea;
        _hSize = totalInvisibleSize;

        if (totalInvisibleSize > 0) {
            float sizeHandler = (float) (visibleArea) / (float) maxSize * 100.0f;
            sizeHandler = (float) hScrollBar.slider.getWidth() / 100.0f * sizeHandler;
            // size of handler
            hScrollBar.slider.handler.setWidth((int) sizeHandler);
        }
        // step of slider
        int stepCount = (int) ((float) totalInvisibleSize / (float) _area.getStep());
        if (stepCount == 0)
            hScrollBar.slider.setStep(hScrollBar.slider.getMaxValue());
        else
            hScrollBar.slider.setStep((hScrollBar.slider.getMaxValue() - hScrollBar.slider.getMinValue()) / stepCount);

        float f = (100.0f / (float) totalInvisibleSize) * (float) _area.getHScrollOffset();
        hScrollBar.slider.setCurrentValue(f);
    }

    /**
     * Setting item width. If the value is greater/less than the maximum/minimum
     * value of the width, then the width becomes equal to the maximum/minimum
     * value.
     * 
     * @param width Width of the item.
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateHorizontalSlider();
        hScrollBar.slider.updateHandler();
    }

    /**
     * Setting item height. If the value is greater/less than the maximum/minimum
     * value of the height, then the height becomes equal to the maximum/minimum
     * value.
     * 
     * @param height Height of the item.
     */
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        updateVerticalSlider();
        vScrollBar.slider.updateHandler();
    }

    /**
     * Adding item to the list area of ListBox.
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     */
    @Override
    public void addItem(IBaseItem item) {
        _area.addItem(item);
        updateElements();
    }

    /**
     * Insert item into the list area of ListBox by index.
     * 
     * @param item  Item as com.spvessel.spacevil.Core.IBaseItem.
     * @param index Index of insertion.
     */
    @Override
    public void insertItem(IBaseItem item, int index) {
        _area.insertItem(item, index);
        updateElements();
    }

    /**
     * Removing the specified item from the list area of ListBox.
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    @Override
    public boolean removeItem(IBaseItem item) {
        List<IBaseItem> list = getItems();
        if (list.contains(item)) {
            return super.removeItem(item);
        }
        boolean b = _area.removeItem(item);
        updateElements();
        _area.setFocus();
        return b;
    }

    /**
     * Removing all items from the list area of ListBox.
     */
    @Override
    public void clear() {
        _area.clear();
    }

    /**
     * Updating all ListBox inner items.
     */
    public void updateElements() {
        updateVerticalSlider();
        vScrollBar.slider.updateHandler();
        updateHorizontalSlider();
        hScrollBar.slider.updateHandler();
    }

    /**
     * Initializing all elements in the ListBox.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        // Adding
        super.addItem(_grid);

        _grid.insertItem(_area, 0, 0);
        _grid.insertItem(vScrollBar, 0, 1);
        _grid.insertItem(hScrollBar, 1, 0);
        _grid.insertItem(menu, 1, 1);

        // Events Connections
        _area.itemListChanged.add(this::updateElements);

        eventScrollUp.add((sender, args) -> {
            if (args.mods.contains(KeyMods.No) && args.mods.size() == 1)
                vScrollBar.eventScrollUp.execute(sender, args);
        });
        eventScrollDown.add((sender, args) -> {
            if (args.mods.contains(KeyMods.No) && args.mods.size() == 1)
                vScrollBar.eventScrollDown.execute(sender, args);
        });

        vScrollBar.slider.eventValueChanged.add((sender) -> updateVListArea());
        hScrollBar.slider.eventValueChanged.add((sender) -> updateHListArea());

        // create menu
        if (!_isMenuDisabled) {
            _menu = new ContextMenu(getHandler());
            _menu.setBackground(60, 60, 60);
            _menu.setPassEvents(false);

            Color menuItemForeground = new Color(210, 210, 210);

            MenuItem goTop = new MenuItem("Go up");
            goTop.setForeground(menuItemForeground);
            goTop.eventMouseClick.add((sender, args) -> {
                vScrollBar.slider.setCurrentValue(vScrollBar.slider.getMinValue());
            });

            MenuItem goDown = new MenuItem("Go down");
            goDown.setForeground(menuItemForeground);
            goDown.eventMouseClick.add((sender, args) -> {
                vScrollBar.slider.setCurrentValue(vScrollBar.slider.getMaxValue());
            });

            MenuItem goTopLeft = new MenuItem("Go up and left");
            goTopLeft.setForeground(menuItemForeground);
            goTopLeft.eventMouseClick.add((sender, args) -> {
                hScrollBar.slider.setCurrentValue(hScrollBar.slider.getMinValue());
                vScrollBar.slider.setCurrentValue(vScrollBar.slider.getMinValue());
            });

            MenuItem goBottomRight = new MenuItem("Go down and right");
            goBottomRight.setForeground(menuItemForeground);
            goBottomRight.eventMouseClick.add((sender, args) -> {
                hScrollBar.slider.setCurrentValue(hScrollBar.slider.getMaxValue());
                vScrollBar.slider.setCurrentValue(vScrollBar.slider.getMaxValue());
            });
            _menu.addItems(goTopLeft, goBottomRight, goTop, goDown);
            menu.eventMouseClick.add((sender, args) -> {
                if (!_isMenuDisabled)
                    _menu.show(sender, args);
            });
            _menu.activeButton = MouseButton.ButtonLeft;
            _menu.effects().add(new Shadow(10));
        }
    }

    /**
     * Getting content of the list area of ListBox.
     * 
     * @return List of items as
     *         List&lt;com.spvessel.spacevil.Core.IBaseItem&gt;
     */
    public List<IBaseItem> getListContent() {
        List<IBaseItem> result = new LinkedList<>();
        List<IBaseItem> list = _area.getItems();
        for (IBaseItem item : list) {
            result.add(((SelectionItem) item).getContent());
        }
        return result;
    }

    /**
     * Adding all elements in the list area of ListBox from the given list.
     * 
     * @param content List of items as
     *                List&lt;com.spvessel.spacevil.Core.IBaseItem&gt;
     */
    public void setListContent(List<IBaseItem> content) {
        _area.setListContent(content);
        updateElements();
    }

    /**
     * Getting selected item.
     * 
     * @return Selected item as com.spvessel.spacevil.Core.IBaseItem.
     */
    public IBaseItem getSelectedItem() {
        return _area.getSelectedItem();
    }

    /**
     * Getting wrapper of item.
     * 
     * @param item Item as com.spvessel.spacevil.Core.IBaseItem.
     * @return Wrapper of given item as com.spvessel.spacevil.SelectionItem.
     */
    public SelectionItem getWrapper(IBaseItem item) {
        return getArea()._mapContent.get(item);
    }

    /**
     * Setting style of the ButtonCore.
     * <p>
     * Inner styles: "area", "vscrollbar", "hscrollbar", "menu".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("vscrollbar");
        if (inner_style != null) {
            vScrollBar.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("hscrollbar");
        if (inner_style != null) {
            hScrollBar.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("menu");
        if (inner_style != null) {
            menu.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("area");
        if (inner_style != null) {
            _area.setStyle(inner_style);
        }
    }
}