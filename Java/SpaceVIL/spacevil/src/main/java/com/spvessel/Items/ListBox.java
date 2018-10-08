package com.spvessel.Items;

import java.util.LinkedList;
import java.util.List;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Cores.InterfaceCommonMethod;
import com.spvessel.Cores.InterfaceCommonMethodState;
import com.spvessel.Cores.InterfaceItem;
import com.spvessel.Cores.InterfaceMouseMethodState;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.ScrollBarVisibility;

public class ListBox extends VisualItem {
    static int count = 0;

    public int getSelection() {
        return _area.getSelection();
    }

    public void setSelection(int index) {
        _area.setSelection(index);
    }

    public void unselect() {
        _area.unselect();
    }

    public void setSelectionVisibility(boolean visibility) {
        _area.setSelectionVisibility(visibility);
    }

    public boolean getSelectionVisibility() {
        return _area.getSelectionVisibility();
    }

    private Grid _grid = new Grid(2, 2);
    private ListArea _area = new ListArea();

    public ListArea getArea() {
        return _area;
    }

    public VerticalScrollBar vScrollBar = new VerticalScrollBar();
    public HorizontalScrollBar hScrollBar = new HorizontalScrollBar();
    private ScrollBarVisibility _v_scrollBarPolicy = ScrollBarVisibility.ALWAYS;

    public ScrollBarVisibility getvScrollBarVisible() {
        return _v_scrollBarPolicy;
    }

    public void setVScrollBarVisible(ScrollBarVisibility policy) {
        _v_scrollBarPolicy = policy;

        if (policy == ScrollBarVisibility.NEVER)
            vScrollBar.setVisible(false);
        else
            vScrollBar.setVisible(true);

        _grid.updateLayout();
        updateVerticalSlider();
        vScrollBar.slider.updateHandler();
    }

    private ScrollBarVisibility _h_scrollBarPolicy = ScrollBarVisibility.ALWAYS;

    public ScrollBarVisibility gethScrollBarVisible() {
        return _h_scrollBarPolicy;
    }

    public void setHScrollBarVisible(ScrollBarVisibility policy) {
        _h_scrollBarPolicy = policy;

        if (policy == ScrollBarVisibility.NEVER)
            hScrollBar.setVisible(false);
        else
            hScrollBar.setVisible(true);

        _grid.updateLayout();
        updateHorizontalSlider();
        hScrollBar.slider.updateHandler();
    }

    public ListBox() {
        setItemName("ListBox_" + count);
        count++;
        setStyle(DefaultsService.getDefaultStyle("SpaceVIL.ListBox"));

        // VBar
        vScrollBar.setVisible(true);
        vScrollBar.setItemName(getItemName() + "_" + vScrollBar.getItemName());

        // HBar
        hScrollBar.setVisible(true);
        hScrollBar.setItemName(getItemName() + "_" + hScrollBar.getItemName());

        // Area
        _area.setItemName(getItemName() + "_" + _area.getItemName());
        _area.setSpacing(0, 5);
    }

    private void updateListAreaAttributes(InterfaceItem sender) {
        updateVListArea();
        updateHListArea();
    }

    private long v_size = 0;
    private long h_size = 0;

    private void updateVListArea() {
        // vertical slider
        float v_value = vScrollBar.slider.getCurrentValue();
        int v_offset = (int) ((float) (v_size * v_value) / 100.0f);
        _area.setVScrollOffset(v_offset);
    }

    private void updateHListArea() {
        // horizontal slider
        float h_value = hScrollBar.slider.getCurrentValue();
        int h_offset = (int) ((float) (h_size * h_value) / 100.0f);
        _area.setHScrollOffset(h_offset);
    }

    private void updateVerticalSlider()// vertical slider
    {
        int total_invisible_size = 0;
        int visible_area = _area.getHeight() - _area.getPadding().top - _area.getPadding().bottom;
        for (BaseItem item : _area.getItems()) {
            if (item.equals(_area.getSubstrate()) || !item.isVisible())
                continue;
            total_invisible_size += (item.getHeight() + _area.getSpacing().vertical);
        }
        int total = total_invisible_size - _area.getSpacing().vertical;
        if (total_invisible_size <= visible_area) {
            vScrollBar.slider.handler.setHeight(/* vScrollBar.slider.getHeight() */0);
            vScrollBar.slider.setStep(vScrollBar.slider.getMaxValue());
            v_size = 0;
            vScrollBar.slider.setCurrentValue(0);
            return;
        }
        total_invisible_size -= visible_area;
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
        vScrollBar.slider.setCurrentValue((100.0f / total_invisible_size) * _area.getVScrollOffset());
    }

    private void updateHorizontalSlider()// horizontal slider
    {
        int max_size = 0;
        int visible_area = _area.getWidth() - _area.getPadding().left - _area.getPadding().right;
        for (BaseItem item : _area.getItems()) {
            if (item.equals(_area.getSubstrate()))
                continue;
            if (max_size < item.getWidth() + item.getMargin().left + item.getMargin().right)
                max_size = item.getWidth() + item.getMargin().left + item.getMargin().right;
        }
        if (max_size <= visible_area) {
            hScrollBar.slider.handler.setWidth(/* hScrollBar.slider.getWidth() */0);
            hScrollBar.slider.setStep(hScrollBar.slider.getMaxValue());
            h_size = 0;
            hScrollBar.slider.setCurrentValue(0);
            return;
        }
        int total_invisible_size = max_size - visible_area;
        h_size = total_invisible_size;

        if (total_invisible_size > 0) {
            float size_handler = (float) (visible_area) / (float) max_size * 100.0f;
            size_handler = (float) hScrollBar.slider.getWidth() / 100.0f * size_handler;
            // size of handler
            hScrollBar.slider.handler.setWidth((int) size_handler);
        }
        // step of slider
        int step_count = (int) ((float) total_invisible_size / (float) _area.getStep());
        if (step_count == 0)
            hScrollBar.slider.setStep(hScrollBar.slider.getMaxValue());
        else
            hScrollBar.slider.setStep((hScrollBar.slider.getMaxValue() - hScrollBar.slider.getMinValue()) / step_count);

        float f = (100.0f / (float) total_invisible_size) * (float) _area.getHScrollOffset();
        hScrollBar.slider.setCurrentValue(f);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateHorizontalSlider();
        hScrollBar.slider.updateHandler();
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        updateVerticalSlider();
        vScrollBar.slider.updateHandler();
    }

    @Override
    public void addItem(BaseItem item) {
        _area.addItem(item);
        updateElements();
    }

    @Override
    public void insertItem(BaseItem item, int index) {
        _area.insertItem(item, index);
        updateElements();
    }

    @Override
    public void removeItem(BaseItem item) {
        _area.removeItem(item);
        updateElements();
    }

    public void updateElements() {
        updateVerticalSlider();
        vScrollBar.slider.updateHandler();
        updateHorizontalSlider();
        hScrollBar.slider.updateHandler();
    }

    @Override
    public void initElements() {
        // Adding
        super.addItem(_grid);
        _grid.insertItem(_area, 0, 0);
        _grid.insertItem(vScrollBar, 0, 1);
        _grid.insertItem(hScrollBar, 1, 0);

        // Events Connections
        //InterfaceCommonMethod listChanged = () -> updateElements();
        _area.itemListChanged.add(this::updateElements); //listChanged);

        //InterfaceMouseMethodState scroll_up = (sender, args) -> vScrollBar.eventScrollUp.execute(sender, args);
        eventScrollUp.add(vScrollBar.eventScrollUp::execute); //scroll_up);
        //InterfaceMouseMethodState scroll_down = (sender, args) -> vScrollBar.eventScrollDown.execute(sender, args);
        eventScrollDown.add(vScrollBar.eventScrollDown::execute); //scroll_down);

        //InterfaceCommonMethodState v_changed = (sender) -> updateVListArea();
        vScrollBar.slider.eventValueChanged.add((sender) -> updateVListArea()); //v_changed);
        //InterfaceCommonMethodState h_changed = (sender) -> updateHListArea();
        hScrollBar.slider.eventValueChanged.add((sender) -> updateHListArea()); //h_changed);
    }

    public List<BaseItem> getListContent() {
        List<BaseItem> result = new LinkedList<>();
        for (BaseItem item : _area.getItems()) {
            if (item instanceof CustomShape)
                continue;
            result.add(item);
        }
        return result;
    }

    public void setListContent(List<BaseItem> content) {
        content.add(0, _area.getSubstrate());
        _area.setContent(content);
    }

    public BaseItem getSelectionItem() {
        List<BaseItem> result = new LinkedList<>();
        return _area.getSelectionItem();
    }

    // style
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
    }
}