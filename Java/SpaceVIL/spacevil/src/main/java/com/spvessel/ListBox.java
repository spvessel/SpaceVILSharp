package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.InterfaceBaseItem;
import com.spvessel.Core.InterfaceCommonMethod;
import com.spvessel.Core.InterfaceCommonMethodState;
import com.spvessel.Core.InterfaceItem;
import com.spvessel.Core.InterfaceMouseMethodState;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.MouseButton;
import com.spvessel.Flags.ScrollBarVisibility;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public class ListBox extends Prototype {
    private static int count = 0;

    /**
     * Selection area of the ListBox
     */
    public int getSelection() {
        return _area.getSelection();
    }

    /**
     * Set selected item of the ListBox by index
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

    /**
     * Is selection changes view of the item or not
     */
    public void setSelectionVisibility(boolean visibility) {
        _area.setSelectionVisibility(visibility);
    }
    public boolean getSelectionVisibility() {
        return _area.getSelectionVisibility();
    }

    private Grid _grid = new Grid(2, 2);
    private ListArea _area = new ListArea();

    /**
     * @return ListArea
     */
    public ListArea getArea() {
        return _area;
    }

    public BlankItem menu = new BlankItem();
    private boolean _is_menu_disabled = false;

    /**
     * Is ListBox menu disabled
     */
    public void disableMenu(boolean value) {
        _is_menu_disabled = value;
    }

    private ContextMenu _menu;
    public VerticalScrollBar vScrollBar = new VerticalScrollBar();
    public HorizontalScrollBar hScrollBar = new HorizontalScrollBar();
    private ScrollBarVisibility _v_scrollBarPolicy = ScrollBarVisibility.ALWAYS;

    /**
     * Is vertical scroll bar visible
     */
    public ScrollBarVisibility getVScrollBarVisible() {
        return _v_scrollBarPolicy;
    }
    public void setVScrollBarVisible(ScrollBarVisibility policy) {
        _v_scrollBarPolicy = policy;

        if (policy == ScrollBarVisibility.NEVER) {
            vScrollBar.setDrawable(false);
            menu.setVisible(false);
        } else if (policy == ScrollBarVisibility.AS_NEEDED) {
            vScrollBar.setDrawable(false);
            menu.setVisible(false);
        } else if (policy == ScrollBarVisibility.ALWAYS) {
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

    private ScrollBarVisibility _h_scrollBarPolicy = ScrollBarVisibility.ALWAYS;

    /**
     * Is horizontal scroll bar visible
     */
    public ScrollBarVisibility getHScrollBarVisible() {
        return _h_scrollBarPolicy;
    }
    public void setHScrollBarVisible(ScrollBarVisibility policy) {
        _h_scrollBarPolicy = policy;

        if (policy == ScrollBarVisibility.NEVER) {
            hScrollBar.setDrawable(false);
            menu.setVisible(false);
        } else if (policy == ScrollBarVisibility.AS_NEEDED) {
            hScrollBar.setDrawable(false);
            menu.setVisible(false);
        } else if (policy == ScrollBarVisibility.ALWAYS) {
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
     * Constructs a ListBox
     */
    public ListBox() {
        setItemName("ListBox_" + count);
        count++;
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.ListBox"));
        setStyle(DefaultsService.getDefaultStyle(ListBox.class));

        // VBar
        vScrollBar.setDrawable(true);
        vScrollBar.setItemName(getItemName() + "_vScrollBar");

        // HBar
        hScrollBar.setDrawable(true);
        hScrollBar.setItemName(getItemName() + "_hScrollBar");

        // Area
        _area.setItemName(getItemName() + "_area");

        menu.setItemName(getItemName() + "_menu");
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

    // vertical slider
    private void updateVerticalSlider() {
        int total_invisible_size = 0;
        int visible_area = _area.getHeight() - _area.getPadding().top - _area.getPadding().bottom;
        for (InterfaceBaseItem item : _area.getItems()) {
            if (item.equals(_area.getSubstrate()) || item.equals(_area.getHoverSubstrate()) || !item.isVisible())
                continue;
            total_invisible_size += (item.getHeight() + _area.getSpacing().vertical);
        }
        int total = total_invisible_size - _area.getSpacing().vertical;
        if (total_invisible_size <= visible_area) {
            vScrollBar.slider.handler.setDrawable(false);
            vScrollBar.slider.setStep(vScrollBar.slider.getMaxValue());
            v_size = 0;
            vScrollBar.slider.setCurrentValue(0);
            if (getVScrollBarVisible() == ScrollBarVisibility.AS_NEEDED) {
                vScrollBar.setDrawable(false);
                menu.setVisible(false);
                _grid.updateLayout();
            }
            return;
        }
        if (getVScrollBarVisible() == ScrollBarVisibility.AS_NEEDED) {
            vScrollBar.setDrawable(true);
            if (!hScrollBar.isDrawable())
                menu.setVisible(false);
            else
                menu.setVisible(true);
            _grid.updateLayout();
        }
        vScrollBar.slider.handler.setDrawable(true);
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

    // horizontal slider
    private void updateHorizontalSlider() {
        int max_size = 0;
        int visible_area = _area.getWidth() - _area.getPadding().left - _area.getPadding().right;
        for (InterfaceBaseItem item : _area.getItems()) {
            if (item.equals(_area.getSubstrate()) || item.equals(_area.getHoverSubstrate()) || !item.isVisible())
                continue;
            if (max_size < item.getWidth() + item.getMargin().left + item.getMargin().right)
                max_size = item.getWidth() + item.getMargin().left + item.getMargin().right;
        }
        if (max_size <= visible_area) {
            // hScrollBar.slider.handler.setWidth(/* hScrollBar.slider.getWidth() */0);
            hScrollBar.slider.handler.setDrawable(false);
            hScrollBar.slider.setStep(hScrollBar.slider.getMaxValue());
            h_size = 0;
            hScrollBar.slider.setCurrentValue(0);
            if (getHScrollBarVisible() == ScrollBarVisibility.AS_NEEDED) {
                hScrollBar.setDrawable(false);
                menu.setVisible(false);
                _grid.updateLayout();
            }
            return;
        }
        if (getHScrollBarVisible() == ScrollBarVisibility.AS_NEEDED) {
            hScrollBar.setDrawable(true);
            if (!vScrollBar.isDrawable())
                menu.setVisible(false);
            else
                menu.setVisible(true);
            _grid.updateLayout();
        }
        hScrollBar.slider.handler.setDrawable(true);
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

    /**
     * Set width of the ListBox
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateHorizontalSlider();
        hScrollBar.slider.updateHandler();
    }

    /**
     * Set height of the ListBox
     */
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        updateVerticalSlider();
        vScrollBar.slider.updateHandler();
    }

    /**
     * Add item to the ListBox
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        _area.addItem(item);
        updateElements();
    }

    /**
     * Insert item to the ListBox by index
     */
    @Override
    public void insertItem(InterfaceBaseItem item, int index) {
        _area.insertItem(item, index);
        updateElements();
    }

    /**
     * Remove item from the ListBox
     */
    @Override
    public void removeItem(InterfaceBaseItem item) {
        _area.removeItem(item);
        updateElements();
    }

    /**
     * Update states of the all ListBox inner items
     */
    public void updateElements() {
        updateVerticalSlider();
        vScrollBar.slider.updateHandler();
        updateHorizontalSlider();
        hScrollBar.slider.updateHandler();
    }

    /**
     * Initialization and adding of all elements in the ListBox
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

        //InterfaceMouseMethodState scroll_up = (sender, args) -> vScrollBar.eventScrollUp.execute(sender, args);
        eventScrollUp.add(vScrollBar.eventScrollUp::execute);
        //InterfaceMouseMethodState scroll_down = (sender, args) -> vScrollBar.eventScrollDown.execute(sender, args);
        eventScrollDown.add(vScrollBar.eventScrollDown::execute);

        InterfaceCommonMethodState v_changed = (sender) -> updateVListArea();
        vScrollBar.slider.eventValueChanged.add(v_changed);
        InterfaceCommonMethodState h_changed = (sender) -> updateHListArea();
        hScrollBar.slider.eventValueChanged.add(h_changed);

        // create menu
        _menu = new ContextMenu(getHandler());
        _menu.setBackground(60, 60, 60);
        _menu.setPassEvents(false);

        MenuItem go_up = new MenuItem("Go up");
        go_up.setForeground(new Color(210, 210, 210));
        go_up.eventMouseClick.add((sender, args) -> {
            vScrollBar.slider.setCurrentValue(vScrollBar.slider.getMinValue());
        });

        MenuItem go_down = new MenuItem("Go down");
        go_down.setForeground(new Color(210, 210, 210));
        go_down.eventMouseClick.add((sender, args) -> {
            vScrollBar.slider.setCurrentValue(vScrollBar.slider.getMaxValue());
        });

        MenuItem go_up_left = new MenuItem("Go up and left");
        go_up_left.setForeground(new Color(210, 210, 210));
        go_up_left.eventMouseClick.add((sender, args) -> {
            hScrollBar.slider.setCurrentValue(hScrollBar.slider.getMinValue());
            vScrollBar.slider.setCurrentValue(vScrollBar.slider.getMinValue());
        });

        MenuItem go_down_right = new MenuItem("Go down and right");
        go_down_right.setForeground(new Color(210, 210, 210));
        go_down_right.eventMouseClick.add((sender, args) -> {
            hScrollBar.slider.setCurrentValue(hScrollBar.slider.getMaxValue());
            vScrollBar.slider.setCurrentValue(vScrollBar.slider.getMaxValue());
        });
        _menu.addItems(go_up_left, go_down_right, go_up, go_down);
        menu.eventMouseClick.add((sender, args) -> {
            if (!_is_menu_disabled)
                _menu.show(sender, args);
        });
        _menu.activeButton = MouseButton.BUTTON_LEFT;
        _menu.setShadow(10, 0, 0, Color.black);
    }

    /**
     * @return list of all ListBox items
     */
    public List<InterfaceBaseItem> getListContent() {
        List<InterfaceBaseItem> result = new LinkedList<>();
        for (InterfaceBaseItem item : _area.getItems()) {
            if (item.equals(_area.getSubstrate()) || item.equals(_area.getHoverSubstrate()))
                continue;
            result.add(item);
        }
        return result;
    }

    /**
     * Set list of items
     */
    public void setListContent(List<InterfaceBaseItem> content) {
        content.add(0, _area.getSubstrate());
        content.add(1, _area.getHoverSubstrate());
        _area.setContent(content);
    }

    /**
     * @return selection item
     */
    public InterfaceBaseItem getSelectionItem() {
        //List<InterfaceBaseItem> result = new LinkedList<>();
        return _area.getSelectionItem();
    }

    /**
     * Set style of the ListBox
     */
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
        inner_style = style.getInnerStyle("menu");
        if (inner_style != null) {
            menu.setStyle(inner_style);
        }
    }
}