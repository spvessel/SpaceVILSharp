package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.KeyArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.Orientation;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;

import java.util.LinkedList;
import java.util.List;

public class WrapGrid extends Prototype {
    static int count = 0;

    public boolean isStretch() {
        return _area.isStretch();
    }

    public void setStretch(boolean value) {
        _area.setStretch(value);
    }

    public int getRowCount() {
        return _area._rows;
    }

    public int getColumnCount() {
        return _area._columns;
    }

    public int getCellWidth() {
        return _area._cellWidth;
    }

    public int getCellHeight() {
        return _area._cellHeight;
    }

    public void setCellWidth(int cellWidth) {
        _area._cellWidth = cellWidth;
        _area.updateLayout();
        updateSlider();
    }

    public void setCellHeight(int cellHeight) {
        _area._cellHeight = cellHeight;
        _area.updateLayout();
        updateSlider();
    }

    public void setCellSize(int cellWidth, int cellHeight) {
        _area.setCellSize(cellWidth, cellHeight);
        updateSlider();
    }

    /**
     * ScrollBar moving step
     */
    public void setScrollStep(int step) {
        _area.setStep(step);
    }

    public int getScrollStep() {
        return _area.getStep();
    }

    /**
     * Selection index
     */
    public int getSelection() {
        return _area.getSelection();
    }

    /**
     * Set selected item of the WrapGrid by index
     */
    public void setSelection(int index) {
        _area.setSelection(index);
    }

    /**
     * Unselect all items
     */
    public void unselect() {
        _area.unselect();
    }

    public void setSelectionVisibility(boolean value) {
        _area.setSelectionVisible(value);
    }

    public boolean isSelectionVisibility() {
        return _area.isSelectionVisible();
    }

    private VerticalStack _vlayout;
    private HorizontalStack _hlayout;
    private WrapArea _area;

    /**
     * @return ListArea
     */
    public WrapArea getArea() {
        return _area;
    }

    public VerticalScrollBar vScrollBar;
    public HorizontalScrollBar hScrollBar;
    private ScrollBarVisibility _scrollBarPolicy = ScrollBarVisibility.AS_NEEDED;

    public Orientation getOrientation() {
        return _area._orientation;
    }

    /**
     * Is vertical scroll bar visible
     */
    public ScrollBarVisibility getScrollBarVisible() {
        return _scrollBarPolicy;
    }

    public void setScrollBarVisible(ScrollBarVisibility policy) {
        _scrollBarPolicy = policy;

        if (getOrientation() == Orientation.HORIZONTAL) {
            if (policy == ScrollBarVisibility.NEVER)
                vScrollBar.setDrawable(false);
            else if (policy == ScrollBarVisibility.AS_NEEDED)
                vScrollBar.setDrawable(false);
            else if (policy == ScrollBarVisibility.ALWAYS)
                vScrollBar.setDrawable(true);
            _hlayout.updateLayout();
        } else {
            if (policy == ScrollBarVisibility.NEVER)
                hScrollBar.setDrawable(false);
            else if (policy == ScrollBarVisibility.AS_NEEDED)
                hScrollBar.setDrawable(false);
            else if (policy == ScrollBarVisibility.ALWAYS)
                hScrollBar.setDrawable(true);
            _vlayout.updateLayout();
        }
    }

    /**
     * Constructs a WrapGrid
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
        setScrollBarVisible(_scrollBarPolicy);

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
            int total = (_area._cellHeight + _area.getSpacing().vertical) * _area._rows - _area.getSpacing().vertical;
            if (total <= visible_area) {
                vScrollBar.slider.handler.setDrawable(false);
                vScrollBar.slider.setStep(vScrollBar.slider.getMaxValue());
                v_size = 0;
                vScrollBar.slider.setCurrentValue(0);
                if (getScrollBarVisible() == ScrollBarVisibility.AS_NEEDED) {
                    vScrollBar.setDrawable(false);
                    update();
                }
                return;
            }
            if (getScrollBarVisible() == ScrollBarVisibility.AS_NEEDED) {
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
            int total = (_area._cellWidth + _area.getSpacing().horizontal) * _area._columns
                    - _area.getSpacing().horizontal;
            if (total <= visible_area) {
                hScrollBar.slider.handler.setDrawable(false);
                hScrollBar.slider.setStep(hScrollBar.slider.getMaxValue());
                h_size = 0;
                hScrollBar.slider.setCurrentValue(0);
                if (getScrollBarVisible() == ScrollBarVisibility.AS_NEEDED) {
                    hScrollBar.setDrawable(false);
                    update();
                }
                return;
            }
            if (getScrollBarVisible() == ScrollBarVisibility.AS_NEEDED) {
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
     * Set width of the ListBox
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateSlider();
    }

    /**
     * Set height of the ListBox
     */
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        updateSlider();
    }

    /**
     * Add item to the ListBox
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        _area.addItem(item);
        updateSlider();
    }

    /**
     * Insert item to the ListBox by index
     */
    @Override
    public void insertItem(InterfaceBaseItem item, int index) {
        _area.insertItem(item, index);
        updateSlider();
    }

    /**
     * Remove item from the ListBox
     */
    @Override
    public boolean removeItem(InterfaceBaseItem item) {
        List<InterfaceBaseItem> list = new LinkedList<>(getItems());
        if (list.contains(item)) {
            return super.removeItem(item);
        }
        boolean b = _area.removeItem(item);
        updateSlider();
        return b;
    }

    @Override
    public void clear() {
        _area.removeAllItems();
        // updateSlider();
    }

    /**
     * Update states of the all ListBox inner items
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
     * @return list of all ListBox items
     */
    public List<InterfaceBaseItem> getListContent() {
        List<InterfaceBaseItem> result = new LinkedList<>();
        for (InterfaceBaseItem item : _area.getItems()) {
            result.add(((SelectionItem) item).getContent());
        }
        return result;
    }

    /**
     * Set list of items
     */
    public void setListContent(List<InterfaceBaseItem> content) {
        _area.removeAllItems();
        for (InterfaceBaseItem item : content) {
            addItem(item);
        }
    }

    public InterfaceBaseItem getWrapper(InterfaceBaseItem item) {
        return getArea()._mapContent.get(item);
    }

    /**
     * @return selection item
     */
    public InterfaceBaseItem getSelectionItem() {
        return _area.getSelectionItem();
    }

    /**
     * Set style of the ListBox
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
