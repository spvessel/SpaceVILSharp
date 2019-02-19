package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.ScrollBarVisibility;

import java.awt.*;

public class TextArea extends Prototype {
    private static int count = 0;
    private Grid _grid = new Grid(2, 2);
    private TextBlock _area = new TextBlock();

    public BlankItem menu = new BlankItem();
    private boolean _is_menu_disabled = false;

    /**
     * Set context menu disable true or false
     */
    public void disableMenu(boolean value) {
        _is_menu_disabled = value;
    }

    private ContextMenu _menu;
    public VerticalScrollBar vScrollBar = new VerticalScrollBar();
    public HorizontalScrollBar hScrollBar = new HorizontalScrollBar();

    private ScrollBarVisibility _v_scrollBarPolicy = ScrollBarVisibility.ALWAYS;
    private ScrollBarVisibility _h_scrollBarPolicy = ScrollBarVisibility.ALWAYS;

    /**
     * Vertical scroll bar visibility policy (ALWAYS, AS_NEEDED, NEVER) in the TextArea
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
        updateHorizontalSlider();
        hScrollBar.slider.updateHandler();
    }

    /**
     * Horizontal scroll bar visibility policy (ALWAYS, AS_NEEDED, NEVER) in the TextArea
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
        updateVerticalSlider();
        vScrollBar.slider.updateHandler();
    }

    /**
     * Constructs a TextArea
     */
    public TextArea() {
        setItemName("TextArea_" + count);
        count++;
        setStyle(DefaultsService.getDefaultStyle(TextArea.class));

        // VBar
        vScrollBar.isFocusable = false;
        vScrollBar.setVisible(true);
        vScrollBar.setItemName(getItemName() + "_" + vScrollBar.getItemName());
        
        // HBar
        hScrollBar.isFocusable = false;
        hScrollBar.setVisible(true);
        hScrollBar.setItemName(getItemName() + "_" + hScrollBar.getItemName());

        // Area
        _area.setItemName(getItemName() + "_" + _area.getItemName());
        // _area.setSpacing(0, 5);
    }
    public TextArea(String text) {
        this();
        setText(text);
    }

    private long v_size = 0;
    private long h_size = 0;

    private void updateVListArea() {
        // vertical slider
        float v_value = vScrollBar.slider.getCurrentValue();
        int v_offset = (int) Math.round((float) (v_size * v_value) / 100.0f);
        _area.setScrollYOffset(-v_offset);
    }

    private void updateHListArea() {
        // horizontal slider
        float h_value = hScrollBar.slider.getCurrentValue();
        int h_offset = (int) Math.round((float) (h_size * h_value) / 100.0f);
        _area.setScrollXOffset(-h_offset);
    }

    private void updateVerticalSlider()// vertical slider
    {
        int visible_area = _area.getHeight() - _area.getPadding().top - _area.getPadding().bottom;
        int total = _area.getTextHeight();

        int total_invisible_size = total - visible_area;
        if (total <= visible_area) {
            vScrollBar.slider.handler.setHeight(0);
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
        int visible_area = _area.getWidth() - _area.getPadding().left - _area.getPadding().right - 2 * _area.getCursorWidth();
        int total = _area.getTextWidth();
        // int total = _area.get;
        // System.out.println("wt: " + total);

        int total_invisible_size = total - visible_area;
        if (total <= visible_area) {
            hScrollBar.slider.handler.setWidth(0);
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

    /**
     * Set width of the TextArea
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateHorizontalSlider();
        hScrollBar.slider.updateHandler();
        // _area.setWidth(width);
    }

    /**
     * Set height of the TextArea
     */
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        updateVerticalSlider();
        vScrollBar.slider.updateHandler();
        // _area.setHeight(height);
    }

    private void updateElements() {
        updateVerticalSlider();
        vScrollBar.slider.updateHandler();
        updateHorizontalSlider();
        hScrollBar.slider.updateHandler();
    }

    /**
     * Initialization and adding of all elements in the TextArea
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
        eventScrollUp.add((sender, args) -> vScrollBar.eventScrollUp.execute(sender, args));
        eventScrollDown.add((sender, args) -> vScrollBar.eventScrollDown.execute(sender, args));
        _area.textChanged.add(() -> updateElements());

        vScrollBar.slider.eventValueChanged.add((sender) -> {
            updateVListArea();
            _area.setFocus();
        });
        hScrollBar.slider.eventValueChanged.add((sender) -> {
            updateHListArea();
            _area.setFocus();
        });
        
        // create menu
        _menu = new ContextMenu(getHandler());
        _menu.setBackground(60, 60, 60);
        _menu.setPassEvents(false);
        
        MenuItem go_up = new MenuItem("Go up");
        go_up.setForeground(new Color(210, 210, 210));
        go_up.eventMouseClick.add((sender, args) -> {
            _area.setScrollYOffset(0);
            updateElements();
            _area.setFocus();
        });
        
        MenuItem go_down = new MenuItem("Go down");
        go_down.setForeground(new Color(210, 210, 210));
        go_down.eventMouseClick.add((sender, args) -> {
            _area.setScrollYOffset(-_area.getTextHeight());
            updateElements();
            _area.setFocus();
        });
        
        MenuItem go_up_left = new MenuItem("Go up and left");
        go_up_left.setForeground(new Color(210, 210, 210));
        go_up_left.eventMouseClick.add((sender, args) -> {
            _area.setScrollYOffset(0);
            _area.setScrollXOffset(0);
            updateElements();
            _area.setFocus();
        });
        
        MenuItem go_down_right = new MenuItem("Go down and right");
        go_down_right.setForeground(new Color(210, 210, 210));
        go_down_right.eventMouseClick.add((sender, args) -> {
            _area.setScrollYOffset(-_area.getTextHeight());
            _area.setScrollXOffset(-_area.getTextWidth());
            updateElements();
            _area.setFocus();
        });
        _menu.addItems(go_up_left, go_down_right, go_up, go_down);
        menu.eventMouseClick.add((sender, args) -> {
            if (!_is_menu_disabled)
            _menu.show(sender, args);
        });
        _menu.activeButton = MouseButton.BUTTON_LEFT;
        _menu.setShadow(10, 0, 0, Color.black);

        updateElements();
    }

    /**
     * Set text in the TextArea
     */
    public void setText(String text) {
        _area.setText(text);
    }

    /**
     * @return text from the TextArea
     */
    public String getText() {
        return _area.getText();
    }

    /**
     * Set style of the TextArea
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;
        super.setStyle(style);
        setForeground(style.foreground);
        setFont(style.font);

        Style inner_style = style.getInnerStyle("vscrollbar");
        if (inner_style != null) {
            vScrollBar.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("hscrollbar");
        if (inner_style != null) {
            hScrollBar.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("textedit");
        if (inner_style != null) {
            _area.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("menu");
        if (inner_style != null) {
            menu.setStyle(inner_style);
        }
    }

    /**
     * Space between lines in the TextArea
     */
    public void setLineSpacer(int lineSpacer) {
        _area.setLineSpacer(lineSpacer);
    }
    public int getLineSpacer() {
        return _area.getLineSpacer();
    }

    /**
     * Text margin in the TextArea
     */
    public void setTextMargin(Indents margin) {
        _area.setTextMargin(margin);
    }
    public Indents getTextMargin() {
        return _area.getTextMargin();
    }

    /**
     * Text font in the TextArea
     */
    public void setFont(Font font) {
        _area.setFont(font);
    }
    public Font getFont() {
        return _area.getFont();
    }

    /**
     * Returns width of the whole text in the TextArea
     * (includes visible and invisible parts of the text)
     */
    public int getTextWidth() {
        return _area.getWidth();
    }

    /**
     * Returns height of the whole text in the TextArea
     * (includes visible and invisible parts of the text)
     */
    public int getTextHeight() {
        return _area.getTextHeight();
    }

    /**
     * Text color in the TextArea
     */
    public void setForeground(Color color) {
        _area.setForeground(color);
    }
    public void setForeground(int r, int g, int b) {
        _area.setForeground(r, g, b);
    }
    public void setForeground(int r, int g, int b, int a) {
        _area.setForeground(r, g, b, a);
    }
    public void setForeground(float r, float g, float b) {
        _area.setForeground(r, g, b);
    }
    public void setForeground(float r, float g, float b, float a) {
        _area.setForeground(r, g, b, a);
    }
    public Color getForeground() {
        return _area.getForeground();
    }

    /**
     * Returns if TextArea editable or not
     */
    public boolean isEditable() {
        return _area.isEditable();
    }

    /**
     * Set TextArea editable true or false
     */
    public void setEditable(boolean value) {
        _area.setEditable(value);
    }

    /**
     * Set TextArea focused/unfocused
     */
    @Override
    public void setFocused(boolean value) {
        super.setFocused(value);
        _area.setFocused(value);
    }

    /**
     * Remove all text from the TextArea
     */
    public void clearArea() {
        _area.clear();
    }
}