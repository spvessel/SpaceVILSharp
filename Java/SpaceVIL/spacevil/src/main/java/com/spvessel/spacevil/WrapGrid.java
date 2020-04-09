package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.KeyArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.Orientation;
import com.spvessel.spacevil.Flags.VisibilityPolicy;

import java.util.LinkedList;
import java.util.List;

/**
 * WrapGrid is a container and manager of com.spvessel.spacevil.WrapArea
 * (scrollable container for other elements with ability of selection, groups
 * elements in cells of a certain size) and scroll bars. WrapGrid controls
 * scrolling, resizing and other actions of com.spvessel.spacevil.WrapArea.
 * <p>
 * Contains list area, scroll bars, scroll menu.
 * <p>
 * Supports all events except drag and drop.
 */
public class WrapGrid extends Prototype {
    static int count = 0;

    /**
     * Returns True if internal com.spvessel.spacevil.WrapArea allocates all
     * available space between cells to achieve smooth streching, otherwise returns
     * False.
     * 
     * @return True: if com.spvessel.spacevil.WrapArea allocates all available space
     *         between cells. False: if space between cells is fixed.
     */
    public boolean isStretch() {
        return _area.isStretch();
    }

    /**
     * Setting strech mode for internal com.spvessel.spacevil.WrapArea.
     * com.spvessel.spacevil.WrapArea can allocates all available space between
     * cells or uses fixed space between cells.
     * 
     * @param value True: if you want to com.spvessel.spacevil.WrapArea allocates
     *              all available space between cells. False: if you want space
     *              between cells to be fixed.
     */
    public void setStretch(boolean value) {
        _area.setStretch(value);
    }

    /**
     * Getting current row count.
     * 
     * @return Row count.
     */
    public int getRowCount() {
        return _area.rows;
    }

    /**
     * Getting current column count.
     * 
     * @return Column count.
     */
    public int getColumnCount() {
        return _area.columns;
    }

    /**
     * Getting current cell width.
     * 
     * @return Cell width.
     */
    public int getCellWidth() {
        return _area._cellWidth;
    }

    /**
     * Getting current cell height.
     * 
     * @return Cell height.
     */
    public int getCellHeight() {
        return _area._cellHeight;
    }

    /**
     * Setting cell width.
     * 
     * @param cellWidth Cell width.
     */
    public void setCellWidth(int cellWidth) {
        _area._cellWidth = cellWidth;
        _area.updateLayout();
        updateSlider();
    }

    /**
     * Setting cell height.
     * 
     * @param cellHeight Cell height.
     */
    public void setCellHeight(int cellHeight) {
        _area._cellHeight = cellHeight;
        _area.updateLayout();
        updateSlider();
    }

    /**
     * Setting cell size.
     * 
     * @param cellWidth  Cell width.
     * @param cellHeight Cell height.
     */
    public void setCellSize(int cellWidth, int cellHeight) {
        _area.setCellSize(cellWidth, cellHeight);
        updateSlider();
    }

    /**
     * Setting scroll movement step.
     * 
     * @param value Scroll step.
     */
    public void setScrollStep(int value) {
        _area.setStep(value);
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
     * Enable or disable selection ability of com.spvessel.spacevil.WrapArea.
     * 
     * @param value True: if you want selection ability of
     *              com.spvessel.spacevil.WrapArea to be enabled. False: if you want
     *              selection ability of com.spvessel.spacevil.WrapArea to be
     *              disabled.
     */
    public void setSelectionVisible(boolean value) {
        _area.setSelectionVisible(value);
    }

    /**
     * Returns True if selection ability of com.spvessel.spacevil.WrapArea is
     * enabled otherwise returns False.
     * 
     * @return True: selection ability of com.spvessel.spacevil.WrapArea is enabled.
     *         False: selection ability of com.spvessel.spacevil.WrapArea is
     *         disabled.
     */
    public boolean isSelectionVisible() {
        return _area.isSelectionVisible();
    }

    private VerticalStack _vlayout;
    private HorizontalStack _hlayout;
    private WrapArea _area;

    /**
     * Getting list area of WrapGrid.
     * 
     * @return List area as com.spvessel.spacevil.WrapArea.
     */
    public WrapArea getArea() {
        return _area;
    }

    /**
     * Vertical scroll bar of WrapGrid.
     */
    public VerticalScrollBar vScrollBar;

    /**
     * Horizontal scroll bar of WrapGrid.
     */
    public HorizontalScrollBar hScrollBar;

    private VisibilityPolicy _scrollBarPolicy = VisibilityPolicy.AS_NEEDED;

    /**
     * Getting internal com.spvessel.spacevil.WrapArea orientation.
     * <p>
     * Orientation can be Orientation.HORIZONTAL or Orientation.VERTICAL.
     * 
     * @return Current com.spvessel.spacevil.WrapArea orientation.
     */
    public Orientation getOrientation() {
        return _area.orientation;
    }

    /**
     * Getting scroll bar visibility policy.
     * 
     * @return Visibility policy as com.spvessel.spacevil.Flags.VisibilityPolicy.
     */
    public VisibilityPolicy getScrollBarPolicy() {
        return _scrollBarPolicy;
    }

    /**
     * Setting scroll bar visibility policy.
     * <p>
     * Default: com.spvessel.spacevil.Flags.VisibilityPolicy.AS_NEEDED.
     * 
     * @param policy Visibility policy as
     *               com.spvessel.spacevil.Flags.VisibilityPolicy.
     */
    public void setScrollBarPolicy(VisibilityPolicy policy) {
        _scrollBarPolicy = policy;

        if (getOrientation() == Orientation.HORIZONTAL) {
            if (policy == VisibilityPolicy.NEVER)
                vScrollBar.setDrawable(false);
            else if (policy == VisibilityPolicy.AS_NEEDED)
                vScrollBar.setDrawable(false);
            else if (policy == VisibilityPolicy.ALWAYS)
                vScrollBar.setDrawable(true);
            _hlayout.updateLayout();
        } else {
            if (policy == VisibilityPolicy.NEVER)
                hScrollBar.setDrawable(false);
            else if (policy == VisibilityPolicy.AS_NEEDED)
                hScrollBar.setDrawable(false);
            else if (policy == VisibilityPolicy.ALWAYS)
                hScrollBar.setDrawable(true);
            _vlayout.updateLayout();
        }
    }

    /**
     * Constructs a WrapGrid with specified cell width, height and orientation.
     * 
     * @param cellWidth   Width of each cell.
     * @param cellHeight  Height of each cell.
     * @param orientation Orientation of layout as
     *                    com.spvessel.spacevil.Flags.Orientation.
     */
    public WrapGrid(int cellWidth, int cellHeight, Orientation orientation) {
        setItemName("WrapGrid_" + count);
        count++;
        _area = new WrapArea(cellWidth, cellHeight, orientation);

        if (getOrientation() == Orientation.HORIZONTAL) {
            _hlayout = new HorizontalStack();
            vScrollBar = new VerticalScrollBar();
        } else {
            _vlayout = new VerticalStack();
            hScrollBar = new HorizontalScrollBar();
        }

        setStyle(DefaultsService.getDefaultStyle(WrapGrid.class));
        setScrollBarPolicy(_scrollBarPolicy);

        // events
        eventMouseClick.add((sender, args) -> {
            if (_area.getSelection() >= 0)
                _area.setSelection(_area.getSelection());
            else
                _area.setFocus();
        });
        eventKeyPress.add(this::onKeyPress);
    }

    private boolean isOutsideArea(SelectionItem selection) {

        if (getOrientation() == Orientation.HORIZONTAL) {
            int startY = _area.getY() + getPadding().top;
            int endY = _area.getY() + getHeight() - getPadding().bottom;
            if (selection.getY() + selection.getHeight() < startY || selection.getY() > endY)
                return true;
            return false;
        } else {
            int startX = _area.getX() + getPadding().left;
            int endX = _area.getX() + getWidth() - getPadding().right;
            if (selection.getX() + selection.getWidth() < startX || selection.getX() > endX)
                return true;
            return false;
        }
    }

    private void onKeyPress(InterfaceItem sender, KeyArgs args) {
        SelectionItem selection = _area.getTrueSelection();
        if (selection == null)
            return;
        long offset = _area.getScrollOffset();
        int startY = _area.getY() + getPadding().top;
        int startX = _area.getX() + getPadding().left;
        long selection_Y = selection.getY() + selection.getMargin().top;
        long selection_X = selection.getX() + selection.getMargin().left;

        switch (args.key) {
            case UP:
                if (isOutsideArea(selection) || (selection_Y < startY)) {
                    _area.setScrollOffset(offset - (startY - selection_Y));
                    updateSlider();
                }
                break;
            case DOWN:
                if (isOutsideArea(selection)) {
                    _area.setScrollOffset(offset - (startY - selection_Y));
                    updateSlider();
                    break;
                }
                if (selection_Y + selection.getHeight() + selection.getMargin().bottom > getY() + getHeight()
                        - getPadding().bottom) {
                    _area.setScrollOffset(offset + ((selection_Y + selection.getHeight() + selection.getMargin().bottom
                            + _area.getSpacing().vertical) - (getY() + getHeight() - getPadding().bottom)));
                    updateSlider();
                }
                break;

            case LEFT:
                if (isOutsideArea(selection) || (selection_X < startX)) {
                    _area.setScrollOffset(offset - (startX - selection_X));
                    updateSlider();
                }
                break;
            case RIGHT:
                if (isOutsideArea(selection)) {
                    _area.setScrollOffset(offset - (startX - selection_X));
                    updateSlider();
                    break;
                }
                if (selection_X + selection.getWidth() + selection.getMargin().right > getX() + getWidth()
                        - getPadding().right) {
                    _area.setScrollOffset(offset + ((selection_X + selection.getWidth() + selection.getMargin().right
                            + _area.getSpacing().horizontal) - (getX() + getWidth() - getPadding().right)));
                    updateSlider();
                }
                break;

            case ESCAPE:
                unselect();
                break;
            default:
                break;
        }
    }

    private long v_size = 0;
    private long h_size = 0;

    private void updateWrapArea() {
        if (getOrientation() == Orientation.HORIZONTAL) {
            // vertical slider
            float v_value = vScrollBar.slider.getCurrentValue();
            int v_offset = (int) Math.round((float) (v_size * v_value) / 100.0f);
            _area.setScrollOffset(v_offset);
        } else {
            // horizontal slider
            float h_value = hScrollBar.slider.getCurrentValue();
            int h_offset = (int) Math.round((float) (h_size * h_value) / 100.0f);
            _area.setScrollOffset(h_offset);
        }
    }

    private void updateSlider()// vertical slider
    {
        int total_invisible_size = 0;
        if (getOrientation() == Orientation.HORIZONTAL) {
            int visible_area = _area.getHeight() - _area.getPadding().top - _area.getPadding().bottom;
            if (visible_area < 0)
                visible_area = 0;
            int total = (_area._cellHeight + _area.getSpacing().vertical) * _area.rows - _area.getSpacing().vertical;
            if (total <= visible_area) {
                vScrollBar.slider.handler.setDrawable(false);
                vScrollBar.slider.setStep(vScrollBar.slider.getMaxValue());
                v_size = 0;
                vScrollBar.slider.setCurrentValue(0);
                if (getScrollBarPolicy() == VisibilityPolicy.AS_NEEDED) {
                    vScrollBar.setDrawable(false);
                    update();
                }
                return;
            }
            if (getScrollBarPolicy() == VisibilityPolicy.AS_NEEDED) {
                vScrollBar.setDrawable(true);
                update();
            }
            vScrollBar.slider.handler.setDrawable(true);
            total_invisible_size = total - visible_area;
            v_size = total_invisible_size;
            if (total_invisible_size > 0) {
                float size_handler = (float) (visible_area) / (float) total * 100.0f;
                size_handler = (float) vScrollBar.slider.getHeight() / 100.0f * size_handler;
                // size of handler
                vScrollBar.slider.handler.setHeight((int) size_handler);
            }
            // step of slider
            float step_count = total_invisible_size / _area.getStep();
            vScrollBar.slider.setStep((vScrollBar.slider.getMaxValue() - vScrollBar.slider.getMinValue()) / step_count);
            vScrollBar.slider.setCurrentValue((100.0f / total_invisible_size) * _area.getScrollOffset());
        } else {
            int visible_area = _area.getWidth() - _area.getPadding().left - _area.getPadding().right;
            if (visible_area < 0)
                visible_area = 0;
            int total = (_area._cellWidth + _area.getSpacing().horizontal) * _area.columns
                    - _area.getSpacing().horizontal;
            if (total <= visible_area) {
                hScrollBar.slider.handler.setDrawable(false);
                hScrollBar.slider.setStep(hScrollBar.slider.getMaxValue());
                h_size = 0;
                hScrollBar.slider.setCurrentValue(0);
                if (getScrollBarPolicy() == VisibilityPolicy.AS_NEEDED) {
                    hScrollBar.setDrawable(false);
                    update();
                }
                return;
            }
            if (getScrollBarPolicy() == VisibilityPolicy.AS_NEEDED) {
                hScrollBar.setDrawable(true);
                update();
            }
            hScrollBar.slider.handler.setDrawable(true);
            total_invisible_size = total - visible_area;
            h_size = total_invisible_size;
            if (total_invisible_size > 0) {
                float size_handler = (float) (visible_area) / (float) total * 100.0f;
                size_handler = (float) hScrollBar.slider.getWidth() / 100.0f * size_handler;
                // size of handler
                hScrollBar.slider.handler.setWidth((int) size_handler);
            }
            // step of slider
            float step_count = total_invisible_size / _area.getStep();
            hScrollBar.slider.setStep((hScrollBar.slider.getMaxValue() - hScrollBar.slider.getMinValue()) / step_count);
            hScrollBar.slider.setCurrentValue((100.0f / total_invisible_size) * _area.getScrollOffset());
        }
    }

    private void update() {
        if (getOrientation() == Orientation.HORIZONTAL)
            _hlayout.updateLayout();
        else
            _vlayout.updateLayout();
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
        updateSlider();
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
        updateSlider();
    }

    /**
     * Adding item to the list area of WrapGrid.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        _area.addItem(item);
        updateSlider();
    }

    /**
     * Insert item into the list area of WrapGrid by index.
     * 
     * @param item  Child as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @param index Index of insertion.
     */
    @Override
    public void insertItem(InterfaceBaseItem item, int index) {
        _area.insertItem(item, index);
        updateSlider();
    }

    /**
     * Removing the specified item from the list area of WrapGrid.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    @Override
    public boolean removeItem(InterfaceBaseItem item) {
        List<InterfaceBaseItem> list = getItems();
        if (list.contains(item)) {
            return super.removeItem(item);
        }
        boolean b = _area.removeItem(item);
        updateSlider();
        return b;
    }

    /**
     * Removing all items from the list area of WrapGrid.
     */
    @Override
    public void clear() {
        _area.clear();
    }

    /**
     * Initializing all elements in the WrapGrid.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void initElements() {
        // Adding
        _area.itemListChanged.add(this::updateSlider);

        if (getOrientation() == Orientation.HORIZONTAL) {
            super.addItem(_hlayout);
            _hlayout.addItems(_area, vScrollBar);
            eventScrollUp.add((sender, args) -> {
                if (args.mods.contains(KeyMods.NO) && args.mods.size() == 1)
                    vScrollBar.eventScrollUp.execute(sender, args);
            });
            eventScrollDown.add((sender, args) -> {
                if (args.mods.contains(KeyMods.NO) && args.mods.size() == 1)
                    vScrollBar.eventScrollDown.execute(sender, args);
            });
            vScrollBar.slider.eventValueChanged.add((sender) -> updateWrapArea());
        } else {
            super.addItem(_vlayout);
            _vlayout.addItems(_area, hScrollBar);
            eventScrollUp.add((sender, args) -> {
                if (args.mods.contains(KeyMods.NO) && args.mods.size() == 1)
                    hScrollBar.eventScrollUp.execute(sender, args);
            });
            eventScrollDown.add((sender, args) -> {
                if (args.mods.contains(KeyMods.NO) && args.mods.size() == 1)
                    hScrollBar.eventScrollDown.execute(sender, args);
            });
            hScrollBar.slider.eventValueChanged.add((sender) -> updateWrapArea());
        }
    }

    /**
     * Getting content of the list area of WrapGrid.
     * 
     * @return Content of the list area as
     *         List&lt;com.spvessel.spacevil.Core.InterfaceBaseItem&gt;
     */
    public List<InterfaceBaseItem> getListContent() {
        List<InterfaceBaseItem> result = new LinkedList<>();
        List<InterfaceBaseItem> list = _area.getItems();
        for (InterfaceBaseItem item : list) {
            result.add(((SelectionItem) item).getContent());
        }
        return result;
    }

    /**
     * Adding all elements in the list area of WrapGrid from the given list.
     * 
     * @param content List of items as
     *                List&lt;com.spvessel.spacevil.Core.InterfaceBaseItem&gt;
     */
    public void setListContent(List<InterfaceBaseItem> content) {
        _area.setListContent(content);
        // updateSlider();
    }

    /**
     * Getting wrapper of item.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @return Wrapper of given item as com.spvessel.spacevil.SelectionItem.
     */
    public InterfaceBaseItem getWrapper(InterfaceBaseItem item) {
        return getArea()._mapContent.get(item);
    }

    /**
     * Getting selected item.
     * 
     * @return selection item Selected item as
     *         com.spvessel.spacevil.Core.InterfaceBaseItem
     */
    public InterfaceBaseItem getSelectedItem() {
        return _area.getSelectedItem();
    }

    /**
     * Setting style of the CheckBox.
     * <p>
     * Inner styles: "area", "vscrollbar", "hscrollbar".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);

        Style inner_style = style.getInnerStyle("vscrollbar");
        if (inner_style != null && vScrollBar != null) {
            vScrollBar.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("hscrollbar");
        if (inner_style != null && hScrollBar != null) {
            hScrollBar.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("area");
        if (inner_style != null) {
            _area.setStyle(inner_style);
        }
    }
}
