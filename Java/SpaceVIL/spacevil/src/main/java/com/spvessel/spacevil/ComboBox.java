package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

public class ComboBox extends Prototype {
    // Queue<BaseItem> _queue = new Queue<BaseItem>();

    static int count = 0;
    public ButtonCore selection = new ButtonCore();
    public ButtonCore dropDown = new ButtonCore();
    public CustomShape arrow = new CustomShape();
    public ComboBoxDropDown dropDownArea;
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
        selection.setTextAlignment(alignment);
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        selection.setTextAlignment(alignment);
    }

    /**
     * Text margin in the ComboBox
     */
    public void setTextMargin(Indents margin) {
        selection.setMargin(margin);
    }

    /**
     * Text font parameters in the ComboBox
     */
    public void setFont(Font font) {
        selection.setFont(font);
    }

    public void setFontSize(int size) {
        selection.setFontSize(size);
    }

    public void setFontStyle(int style) {
        selection.setFontStyle(style);
    }

    public void setFontFamily(String font_family) {
        selection.setFontFamily(font_family);
    }

    public Font getFont() {
        return selection.getFont();
    }

    /**
     * Set text in the ComboBox
     */
    public void setText(String text) {
        selection.setText(text);
    }

    public String getText() {
        return selection.getText();
    }

    /**
     * Text color in the ComboBox
     */
    public void setForeground(Color color) {
        selection.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        selection.setForeground(r, g, b);
    }

    public void setForeground(int r, int g, int b, int a) {
        selection.setForeground(r, g, b, a);
    }

    public void setForeground(float r, float g, float b) {
        selection.setForeground(r, g, b);
    }

    public void setForeground(float r, float g, float b, float a) {
        selection.setForeground(r, g, b, a);
    }

    public Color getForeground() {
        return selection.getForeground();
    }

    /**
     * Initialization and adding of all elements in the ComboBox
     */
    @Override
    public void initElements() {
        // adding
        super.addItem(selection);
        super.addItem(dropDown);
        dropDown.addItem(arrow);

        // dropDownArea
        dropDownArea = new ComboBoxDropDown(getHandler());
        dropDownArea.activeButton = MouseButton.BUTTON_LEFT;
        dropDownArea.setOutsideClickClosable(true);
        dropDownArea.selectionChanged.add(() -> onSelectionChanged());
    }

    private void showDropDownList() {
        
        // dropDownArea
        dropDownArea.setPosition(getX(), getY() + getHeight());
        dropDownArea.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        dropDownArea.setWidth(selection.getWidth());
        MouseArgs args = new MouseArgs();
        args.button = MouseButton.BUTTON_LEFT;
        dropDownArea.show(this, args);
    }

    /**
     * Add item to ComboBox list
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        dropDownArea.addItem(item);
        // _queue.Enqueue(item);
    }

    /**
     * Remove item from the ComboBox list
     */
    @Override
    public void removeItem(InterfaceBaseItem item) {
        dropDownArea.removeItem(item);
    }

    /**
     * Current element in the ComboBox by index
     */
    public void setCurrentIndex(int index) {
        dropDownArea.setCurrentIndex(index);
        selection.setText(dropDownArea.getText());
        selectionChanged.execute();
    }

    public int getCurrentIndex() {
        return dropDownArea.getCurrentIndex();
    }

    private void onSelectionChanged() {
        selection.setText(dropDownArea.getText());
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
            selection.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("dropdownbutton");
        if (inner_style != null) {
            dropDown.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("arrow");
        if (inner_style != null) {
            arrow.setStyle(inner_style);
        }
    }
}