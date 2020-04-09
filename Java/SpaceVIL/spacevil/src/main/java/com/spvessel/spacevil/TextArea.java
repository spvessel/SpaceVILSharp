package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.VisibilityPolicy;

import java.awt.*;

/**
 * TextArea is a basic implementation of a user interface editable text area.
 * <p>
 * Contains text area, scroll bars, menu button, navigation context menu.
 * <p>
 * Supports all events except drag and drop.
 */
public class TextArea extends Prototype {
    private static int count = 0;
    private Grid _grid = new Grid(2, 2);
    private TextBlock _area;// = new TextBlock();

    /**
     * Interactive item to show the navigation context menu.
     */
    public BlankItem menu = new BlankItem();
    private ContextMenu _menu;
    private boolean _isMenuDisabled = false;

    /**
     * Setting the navigation context menu to disable or enable.
     * <p>
     * Default: False.
     * 
     * @param value True: if you want to disable navigation context menu. False: if
     *              you want to enable navigation context menu.
     */
    public void disableMenu(boolean value) {
        _isMenuDisabled = value;
    }

    /**
     * Returns True if TextArea is editable otherwise returns False.
     * 
     * @return True: if TextArea is editable. True: if TextArea is non-editable.
     */
    public boolean isEditable() {
        return _area.isEditable();
    }

    /**
     * Setting TextArea text field be editable or be non-editable.
     * 
     * @param value True: if you want TextArea be editable. True: if you want
     *              TextArea be non-editable.
     */
    public void setEditable(boolean value) {
        _area.setEditable(value);
    }

    /**
     * Vertical scroll bar of TextArea.
     */
    public VerticalScrollBar vScrollBar = new VerticalScrollBar();

    /**
     * Horizontal scroll bar of TextArea.
     */
    public HorizontalScrollBar hScrollBar = new HorizontalScrollBar();

    private VisibilityPolicy _vScrollBarPolicy = VisibilityPolicy.AS_NEEDED;
    private VisibilityPolicy _hScrollBarPolicy = VisibilityPolicy.AS_NEEDED;

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

        if (policy == VisibilityPolicy.NEVER) {
            vScrollBar.setDrawable(false);
            menu.setVisible(false);
        } else if (policy == VisibilityPolicy.AS_NEEDED) {
            vScrollBar.setDrawable(false);
            menu.setVisible(false);
        } else if (policy == VisibilityPolicy.ALWAYS) {
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

        if (policy == VisibilityPolicy.NEVER) {
            hScrollBar.setDrawable(false);
            menu.setVisible(false);
        } else if (policy == VisibilityPolicy.AS_NEEDED) {
            hScrollBar.setDrawable(false);
            menu.setVisible(false);
        } else if (policy == VisibilityPolicy.ALWAYS) {
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
     * Default TextArea constructor.
     */
    public TextArea() {
        setItemName("TextArea_" + count);
        count++;
        _area = new TextBlock();
        setStyle(DefaultsService.getDefaultStyle(TextArea.class));

        // VBar
        vScrollBar.setDrawable(true);
        vScrollBar.setItemName(getItemName() + "_" + vScrollBar.getItemName());

        // HBar
        hScrollBar.setDrawable(true);
        hScrollBar.setItemName(getItemName() + "_" + hScrollBar.getItemName());

        // Area
        // _area.setItemName(getItemName() + "_" + _area.getItemName());
        // _area.setSpacing(0, 5);
        eventMouseClick.add((sender, args) -> {
            _area.setFocus();
        });
    }

    /**
     * Constructs TextArea with the specified text.
     * 
     * @param text Text for TextArea.
     */
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
        if (visible_area < 0)
            visible_area = 0;
        int total = _area.getTextHeight();

        int total_invisible_size = total - visible_area;
        if (total <= visible_area) {
            vScrollBar.slider.handler.setHeight(0);
            vScrollBar.slider.setStep(vScrollBar.slider.getMaxValue());
            v_size = 0;
            vScrollBar.slider.setCurrentValue(0);
            if (getVScrollBarPolicy() == VisibilityPolicy.AS_NEEDED) {
                vScrollBar.setDrawable(false);
                menu.setVisible(false);
                _grid.updateLayout();
            }
            return;
        }
        if (getVScrollBarPolicy() == VisibilityPolicy.AS_NEEDED) {
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
        int visible_area = _area.getWidth() - _area.getPadding().left - _area.getPadding().right
                - 2 * _area.getCursorWidth();
        if (visible_area < 0)
            visible_area = 0;
        int total = _area.getTextWidth();

        int total_invisible_size = total - visible_area;
        if (total <= visible_area) {
            hScrollBar.slider.handler.setWidth(0);
            hScrollBar.slider.setStep(hScrollBar.slider.getMaxValue());
            h_size = 0;
            hScrollBar.slider.setCurrentValue(0);
            if (getHScrollBarPolicy() == VisibilityPolicy.AS_NEEDED) {
                hScrollBar.setDrawable(false);
                menu.setVisible(false);
                _grid.updateLayout();
            }
            return;
        }
        if (getHScrollBarPolicy() == VisibilityPolicy.AS_NEEDED) {
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

    private void updateElements() {
        updateVerticalSlider();
        vScrollBar.slider.updateHandler();
        updateHorizontalSlider();
        hScrollBar.slider.updateHandler();
    }

    /**
     * Event that is invoked when text is changed.
     */
    public EventCommonMethod onTextChanged = new EventCommonMethod();

    /**
     * Initializing all elements in the TextArea.
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
        eventScrollUp.add((sender, args) -> vScrollBar.eventScrollUp.execute(sender, args));
        eventScrollDown.add((sender, args) -> vScrollBar.eventScrollDown.execute(sender, args));
        _area.cursorChanged.add(this::updateElements);
        _area.textChanged.add(() -> onTextChanged.execute());

        vScrollBar.slider.eventValueChanged.add((sender) -> {
            updateVListArea();
        });
        hScrollBar.slider.eventValueChanged.add((sender) -> {
            updateHListArea();
        });

        // create menu
        if (!_isMenuDisabled) {
            _menu = new ContextMenu(getHandler());
            _menu.setBackground(60, 60, 60);
            _menu.setPassEvents(false);

            Color menuItemCForeground = new Color(210, 210, 210);

            MenuItem go_up = new MenuItem("Go up");
            go_up.setForeground(menuItemCForeground);
            go_up.eventMouseClick.add((sender, args) -> {
                _area.setScrollYOffset(0);
                updateElements();
                _area.setFocus();
            });

            MenuItem go_down = new MenuItem("Go down");
            go_down.setForeground(menuItemCForeground);
            go_down.eventMouseClick.add((sender, args) -> {
                _area.setScrollYOffset(-_area.getTextHeight());
                updateElements();
                _area.setFocus();
            });

            MenuItem go_up_left = new MenuItem("Go up and left");
            go_up_left.setForeground(menuItemCForeground);
            go_up_left.eventMouseClick.add((sender, args) -> {
                _area.setScrollYOffset(0);
                _area.setScrollXOffset(0);
                updateElements();
                _area.setFocus();
            });

            MenuItem go_down_right = new MenuItem("Go down and right");
            go_down_right.setForeground(menuItemCForeground);
            go_down_right.eventMouseClick.add((sender, args) -> {
                _area.setScrollYOffset(-_area.getTextHeight());
                _area.setScrollXOffset(-_area.getTextWidth());
                updateElements();
                _area.setFocus();
            });
            _menu.addItems(go_up_left, go_down_right, go_up, go_down);
            menu.eventMouseClick.add((sender, args) -> {
                _menu.show(sender, args);
            });
            _menu.activeButton = MouseButton.BUTTON_LEFT;
            _menu.setShadow(10, 0, 0, Color.black);
        }

        updateElements();
    }

    /**
     * Setting the text.
     * 
     * @param text Text as java.lang.String.
     */
    public void setText(String text) {
        _area.setText(text);
        updateElements(); // ??? Возможно нужно добавить еще что-то
    }

    /**
     * Getting the current text of the TextArea.
     * 
     * @return Text as java.lang.String.
     */
    public String getText() {
        return _area.getText();
    }

    /**
     * Setting style of the TextArea.
     * <p>
     * Inner styles: "vscrollbar", "hscrollbar", "textedit", "menu".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
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
     * Setting indent between lines in TextArea.
     * 
     * @param lineSpacer Indent between lines.
     */
    public void setLineSpacer(int lineSpacer) {
        _area.setLineSpacer(lineSpacer);
    }

    /**
     * Setting current indent between lines in TextArea.
     * 
     * @return Indent between lines.
     */
    public int getLineSpacer() {
        return _area.getLineSpacer();
    }

    /**
     * Setting indents for the text to offset text relative to this TextArea.
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        _area.setTextMargin(margin);
    }

    /**
     * Setting indents for the text to offset text relative to TextArea.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setTextMargin(int left, int top, int right, int bottom) {
        _area.setTextMargin(new Indents(left, top, right, bottom));
    }

    /**
     * Getting indents of the text.
     * 
     * @return Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getTextMargin() {
        return _area.getTextMargin();
    }

    /**
     * Setting font of the text.
     * 
     * @param font Font as java.awt.Font.
     */
    public void setFont(Font font) {
        _area.setFont(font);
    }

    /**
     * Setting font size of the text.
     * 
     * @param size New size of the font.
     */
    public void setFontSize(int size) {
        Font oldFont = getFont();
        if (oldFont.getSize() != size) {
            Font newFont = GraphicsMathService.changeFontSize(size, oldFont);
            setFont(newFont);
        }
    }

    /**
     * Setting font style of the text.
     * 
     * @param style New font style (from java.awt.Font package).
     */
    public void setFontStyle(int style) {
        Font oldFont = getFont();
        if (oldFont.getStyle() != style) {
            Font newFont = GraphicsMathService.changeFontStyle(style, oldFont);
            setFont(newFont);
        }
    }

    /**
     * Setting new font family of the text.
     * 
     * @param fontFamily New font family name.
     */
    public void setFontFamily(String fontFamily) {
        if (fontFamily == null)
            return;
        Font oldFont = getFont();
        if (!oldFont.getFamily().equals(fontFamily)) {
            Font newFont = GraphicsMathService.changeFontFamily(fontFamily, oldFont);
            setFont(newFont);
        }
    }

    /**
     * Getting the current font of the text.
     * 
     * @return Font as java.awt.Font.
     */
    public Font getFont() {
        return _area.getFont();
    }

    /**
     * Getting the text width.
     * 
     * @return Text width.
     */
    public int getTextWidth() {
        return _area.getWidth();
    }

    /**
     * Getting the text height.
     * 
     * @return Text height.
     */
    public int getTextHeight() {
        return _area.getTextHeight();
    }

    /**
     * Setting text color of a TextArea.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        _area.setForeground(color);
    }

    /**
     * Setting text color of a TextArea in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        // _area.setForeground(r, g, b);
        setForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting background color of an item in byte RGBA format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b, int a) {
        setForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Setting text color of a TextArea in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        setForeground(GraphicsMathService.colorTransform(r, g, b));
    }

    /**
     * Setting text color of a TextArea in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b, float a) {
        setForeground(GraphicsMathService.colorTransform(r, g, b, a));
    }

    /**
     * Getting current text color.
     * 
     * @return Text color as as java.awt.Color.
     */
    public Color getForeground() {
        return _area.getForeground();
    }

    /**
     * Setting focus on TextArea if it is focusable.
     */
    @Override
    public void setFocus() {
        _area.setFocus();
    }

    /**
     * Deletes all text in the TextArea.
     */
    @Override
    public void clear() {
        _area.clearText();
    }

    /**
     * Adding the specified text to the end of the existing text.
     * 
     * @param text Text for adding.
     */
    public void appendText(String text) {
        _area.appendText(text);
    }

    /**
     * Paste the specified text at the current position of the text cursor (or
     * replace the specified text at the current starting position of the selected
     * text).
     * 
     * @param text Text to insert.
     */
    public void pasteText(String text) {
        _area.pasteText(text);
    }

    /**
     * Cuts and returns the current selected text.
     * 
     * @return Selected text.
     */
    public String cutText() {
        return _area.cutText();
    }

    /**
     * Getting the current selected text.
     * 
     * @return Current selected text.
     */
    public String getSelectedText() {
        return _area.getSelectedText();
    }

    /**
     * Moves text cursor to the text beginning.
     */
    public void rewindText() {
        _area.rewindText();
    }

    /**
     * Returns True if TextArea wraps the contained text to the width of the
     * TextArea otherwise returns False.
     * 
     * @return True: if TextArea wraps the contained text to the width of the
     *         TextArea. False: if TextArea does not wraps the contained text.
     */
    public boolean isWrapText() {
        return _area.isWrapText();
    }

    /**
     * Setting TextArea mode that wraps (or not wraps) input text to the width of
     * the TextArea.
     * 
     * @param value True: if you want to TextArea wraps the contained text to the
     *              width of the TextArea. False: if you want to TextArea does not
     *              wraps the contained text.
     */
    public void setWrapText(boolean value) {
        _area.setWrapText(value);
        updateHorizontalSlider();
    }

    /**
     * Setting scroll step factor. The scroll factor determines how many lines are
     * scrolled in a single scroll request (using a button or mouse wheel).
     * <p>
     * Default: 1.0
     * 
     * @param value Scroll step factor.
     */
    public void setScrollStepFactor(float value) {
        _area.setScrollStepFactor(value);
    }
}