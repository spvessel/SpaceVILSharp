package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.InterfaceMouseMethodState;
import com.spvessel.Core.InterfaceTextShortcuts;
import com.spvessel.Decorations.Indents;
import com.spvessel.Decorations.ItemState;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.ItemStateType;
import com.spvessel.Flags.MouseButton;
import com.spvessel.Flags.ScrollBarVisibility;

import java.awt.*;

public class TextArea extends Prototype {
    static int count = 0;
    private Grid _grid = new Grid(2, 2);
    private TextBlock _area = new TextBlock();

    public BlankItem menu = new BlankItem();
    private boolean _is_menu_disabled = false;

    public void disableMenu(boolean value) {
        _is_menu_disabled = value;
    }

    private ContextMenu _menu;
    public VerticalScrollBar vScrollBar = new VerticalScrollBar();
    public HorizontalScrollBar hScrollBar = new HorizontalScrollBar();
    private ScrollBarVisibility _v_scrollBarPolicy = ScrollBarVisibility.ALWAYS;

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

    private ScrollBarVisibility _h_scrollBarPolicy = ScrollBarVisibility.ALWAYS;

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

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        updateHorizontalSlider();
        hScrollBar.slider.updateHandler();
        // _area.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        updateVerticalSlider();
        vScrollBar.slider.updateHandler();
        // _area.setHeight(height);
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

    public void setLineSpacer(int lineSpacer) {
        _area.setLineSpacer(lineSpacer);
    }

    public int getLineSpacer() {
        return _area.getLineSpacer();
    }

    public void setTextMargin(Indents margin) {
        _area.setTextMargin(margin);
    }

    public Indents getTextMargin() {
        return _area.getTextMargin();
    }

    public void setFont(Font font) {
        _area.setFont(font);
    }

    public Font getFont() {
        return _area.getFont();
    }

    public int getTextWidth() {
        return _area.getWidth();
    }

    public int getTextHeight() {
        return _area.getTextHeight();
    }

    public void setForeground(Color color) {
        _area.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        _area.setForeground(new Color(r, g, b, 255));
    }

    public void setForeground(int r, int g, int b, int a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        _area.setForeground(new Color(r, g, b, a));
    }

    public void setForeground(float r, float g, float b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        _area.setForeground(new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), 255));
    }

    public void setForeground(float r, float g, float b, float a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        _area.setForeground(new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f)));
    }

    public Color getForeground() {
        return _area.getForeground();
    }

    public boolean isEditable() {
        return _area.isEditable();
    }

    public void setEditable(boolean value) {
        _area.setEditable(value);
    }

    @Override
    public void setFocused(boolean value) {
        super.setFocused(value);
        _area.setFocused(value);
    }

    public void clearArea() {
        _area.clear();
    }
}