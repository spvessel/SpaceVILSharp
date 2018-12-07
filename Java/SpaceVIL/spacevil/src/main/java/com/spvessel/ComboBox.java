package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.*;
import com.spvessel.Decorations.Indents;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.KeyCode;
import com.spvessel.Flags.MouseButton;
import com.spvessel.Flags.SizePolicy;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

public class ComboBox extends Prototype {
    // Queue<BaseItem> _queue = new Queue<BaseItem>();

    static int count = 0;
    public ButtonCore _selected = new ButtonCore();
    public ButtonCore _dropdown = new ButtonCore();
    public CustomShape _arrow = new CustomShape();
    public ComboBoxDropDown _dropdownarea;
    public EventCommonMethod selectionChanged = new EventCommonMethod();

    /**
     * Constructs a ComboBox
     */
    public ComboBox() {
        setBackground(0, 0, 0, 0);
        setItemName("ComboBox_" + count);
        count++;

        eventKeyPress.add(this::onKeyPress);
        eventMousePress.add((sender, args) -> showDropDownList());

        setStyle(DefaultsService.getDefaultStyle(ComboBox.class));
    }

    void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (args.key == KeyCode.ENTER && eventMouseClick != null) {
            eventMouseClick.execute(this, new MouseArgs());
        }
    }

    // text init
    /**
     * Text alignment in the ComboBox
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _selected.setTextAlignment(alignment);
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        _selected.setTextAlignment(alignment);
    }

    /**
     * Text margin in the ComboBox
     */
    public void setTextMargin(Indents margin) {
        _selected.setMargin(margin);
    }

    /**
     * Text font parameters in the ComboBox
     */
    public void setFont(Font font) {
        _selected.setFont(font);
    }

    public void setFontSize(int size) {
        _selected.setFontSize(size);
    }

    public void setFontStyle(int style) {
        _selected.setFontStyle(style);
    }

    public void setFontFamily(String font_family) {
        _selected.setFontFamily(font_family);
    }

    public Font getFont() {
        return _selected.getFont();
    }

    /**
     * Set text in the ComboBox
     */
    public void setText(String text) {
        _selected.setText(text);
    }

    public String getText() {
        return _selected.getText();
    }

    /**
     * Text color in the ComboBox
     */
    public void setForeground(Color color) {
        _selected.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        _selected.setForeground(r, g, b);
    }

    public void setForeground(int r, int g, int b, int a) {
        _selected.setForeground(r, g, b, a);
    }

    public void setForeground(float r, float g, float b) {
        _selected.setForeground(r, g, b);
    }

    public void setForeground(float r, float g, float b, float a) {
        _selected.setForeground(r, g, b, a);
    }

    public Color getForeground() {
        return _selected.getForeground();
    }

    /**
     * Initialization and adding of all elements in the ComboBox
     */
    @Override
    public void initElements() {
        // adding
        super.addItem(_selected);
        super.addItem(_dropdown);
        _dropdown.addItem(_arrow);

        // dropdownarea
        _dropdownarea = new ComboBoxDropDown(getHandler());
        _dropdownarea.activeButton = MouseButton.BUTTON_LEFT;
        _dropdownarea.setOutsideClickClosable(true);
        _dropdownarea.selectionChanged.add(() -> onSelectionChanged());
    }

    private void showDropDownList() {
        
        // dropdownarea
        _dropdownarea.setPosition(getX(), getY() + getHeight());
        _dropdownarea.setSize(_selected.getWidth(), 100);
        _dropdownarea.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        MouseArgs args = new MouseArgs();
        args.button = MouseButton.BUTTON_LEFT;
        _dropdownarea.show(this, args);
    }

    /**
     * Add item to ComboBox list
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        _dropdownarea.addItem(item);
        // _queue.Enqueue(item);
    }

    /**
     * Remove item from the ComboBox list
     */
    @Override
    public void removeItem(InterfaceBaseItem item) {
        _dropdownarea.removeItem(item);
    }

    /**
     * Current element in the ComboBox by index
     */
    public void setCurrentIndex(int index) {
        _dropdownarea.setCurrentIndex(index);
        _selected.setText(_dropdownarea.getText());
        selectionChanged.execute();
    }

    public int getCurrentIndex() {
        return _dropdownarea.getCurrentIndex();
    }

    private void onSelectionChanged() {
        _selected.setText(_dropdownarea.getText());
        selectionChanged.execute();
    }

    /**
     * Set style of the ComboBox
     */
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;

        super.setStyle(style);
        setForeground(style.foreground);
        setFont(style.font);

        Style inner_style = style.getInnerStyle("selection");
        if (inner_style != null) {
            _selected.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("dropdownbutton");
        if (inner_style != null) {
            _dropdown.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("arrow");
        if (inner_style != null) {
            _arrow.setStyle(inner_style);
        }
    }
}