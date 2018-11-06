package com.spvessel.Items;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Cores.InterfaceItem;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.ScrollBarVisibility;

import org.lwjgl.system.Pointer.Default;

public class TextArea extends VisualItem {
    static int count = 0;
    private Grid _grid = new Grid(2, 2);
    private TextBlock _area = new TextBlock();

    public VerticalScrollBar vScrollBar = new VerticalScrollBar();
    public HorizontalScrollBar hScrollBar = new HorizontalScrollBar();
    private ScrollBarVisibility _v_scrollBarPolicy = ScrollBarVisibility.ALWAYS;

    public ScrollBarVisibility getvScrollBarVisible() {
        return _v_scrollBarPolicy;
    }

    public void setvScrollBarVisible(ScrollBarVisibility policy) {
        _v_scrollBarPolicy = policy;

        if (policy == ScrollBarVisibility.NEVER)
            vScrollBar.setVisible(false);
        else
            vScrollBar.setVisible(true);

        _grid.updateLayout();
        updateHorizontalSlider();
        hScrollBar.slider.updateHandler();
    }

    private ScrollBarVisibility _h_scrollBarPolicy = ScrollBarVisibility.ALWAYS;

    public ScrollBarVisibility gethScrollBarVisible() {
        return _h_scrollBarPolicy;
    }

    public void sethScrollBarVisible(ScrollBarVisibility policy) {
        _h_scrollBarPolicy = policy;

        if (policy == ScrollBarVisibility.NEVER)
            hScrollBar.setVisible(false);
        else
            hScrollBar.setVisible(true);

        _grid.updateLayout();
        updateVerticalSlider();
        vScrollBar.slider.updateHandler();
    }

    public TextArea() {
        setItemName("TextArea_" + count);
        count++;
        setStyle(DefaultsService.getDefaultStyle(com.spvessel.Items.TextArea.class));

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

    private long v_size = 0;
    private long h_size = 0;

    private void updateVListArea() {
        // vertical slider
        float v_value = vScrollBar.slider.getCurrentValue();
        int v_offset = (int) ((float) (v_size * v_value) / 100.0f);
        _area.setScrollYOffset(-v_offset);
    }

    private void updateHListArea() {
        // horizontal slider
        float h_value = hScrollBar.slider.getCurrentValue();
        int h_offset = (int) ((float) (h_size * h_value) / 100.0f);
        _area.setScrollXOffset(-h_offset);
    }

    private void updateVerticalSlider()// vertical slider
    {
        // 1. собрать всю высоту всех элементов
        // 2. собрать видимую высоту
        // 3. связать видимую и всю высоту с скроллом
        // 4. выставить размеры и позицию в %

        int visible_area = _area.getHeight() - _area.getPadding().top - _area.getPadding().bottom;
        int total = _area.getTextHeight();

        int total_invisible_size = total - visible_area;
        if (total <= visible_area) {
            vScrollBar.slider.handler.setHeight(0);
            vScrollBar.slider.setStep(vScrollBar.slider.getMaxValue());
            v_size = 0;
            vScrollBar.slider.setCurrentValue(0);
            return;
        }
        v_size = total_invisible_size;

        if (total_invisible_size > 0) {
            float size_handler = (float) (visible_area) / (float) total * 100.0f;
            size_handler = (float) vScrollBar.slider.getHeight() / 100.0f * size_handler;
            // size of handler
            vScrollBar.slider.handler.setHeight((int) size_handler);
        }
        // step of slider
        float step_count = (float) total_invisible_size / (float) _area.getScrollYStep();
        vScrollBar.slider.setStep((vScrollBar.slider.getMaxValue() - vScrollBar.slider.getMinValue()) / step_count);
        vScrollBar.slider.setCurrentValue((100.0f / total_invisible_size) * Math.abs(_area.getScrollYOffset()));
    }

    private void updateHorizontalSlider()// horizontal slider
    {
        // 1. найти самый широкий из всех элементов
        // 2. определить видимую ширину
        // 3. связать видимую и всю ширину с скроллом
        // 4. выставить размеры и позицию в %

        int visible_area = _area.getWidth() - _area.getPadding().left - _area.getPadding().right;
        int total = _area.getTextWidth();

        int total_invisible_size = total - visible_area;
        if (total <= visible_area) {
            hScrollBar.slider.handler.setWidth(0);
            hScrollBar.slider.setStep(hScrollBar.slider.getMaxValue());
            h_size = 0;
            hScrollBar.slider.setCurrentValue(0);
            return;
        }
        h_size = total_invisible_size;

        if (total_invisible_size > 0) {
            float size_handler = (float) (visible_area) / (float) total * 100.0f;
            size_handler = (float) hScrollBar.slider.getWidth() / 100.0f * size_handler;
            // size of handler
            hScrollBar.slider.handler.setWidth((int) size_handler);
        }
        // step of slider
        float step_count = (float) total_invisible_size / (float) _area.getScrollXStep();
        hScrollBar.slider.setStep((hScrollBar.slider.getMaxValue() - hScrollBar.slider.getMinValue()) / step_count);
        hScrollBar.slider.setCurrentValue((100.0f / total_invisible_size) * Math.abs(_area.getScrollXOffset()));
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

    public void updateElements() {
        updateVerticalSlider();
        vScrollBar.slider.updateHandler();
        updateHorizontalSlider();
        hScrollBar.slider.updateHandler();
    }

    @Override
    public void initElements() {
            //Adding
            super.addItem(_grid);
            _grid.insertItem(_area, 0, 0);
            _grid.insertItem(vScrollBar, 0, 1);
            _grid.insertItem(hScrollBar, 1, 0);

            //Events Connections
            eventScrollUp.add((sender, args) -> vScrollBar.eventScrollUp.execute(sender, args));
            eventScrollDown.add((sender, args) -> vScrollBar.eventScrollDown.execute(sender, args));
            _area.textChanged.add(() -> updateElements());

            vScrollBar.slider.eventValueChanged.add((sender) -> updateVListArea());
            hScrollBar.slider.eventValueChanged.add((sender) -> updateHListArea());
            updateElements();
        }

    public void setText(String text) {
        _area.setText(text);
    }

    public String getText() {
        return _area.getText();
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