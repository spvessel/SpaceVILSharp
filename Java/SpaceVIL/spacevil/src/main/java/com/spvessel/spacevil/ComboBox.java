package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.LayoutType;
import com.spvessel.spacevil.Flags.MouseButton;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

public class ComboBox extends Prototype {

    static int count = 0;
    ButtonCore selection;
    ButtonCore dropDown;
    CustomShape arrow;
    ComboBoxDropDown dropDownArea;
    public EventCommonMethod selectionChanged = new EventCommonMethod();

    @Override
    public void release() {
        selectionChanged.clear();
    }

    private List<MenuItem> preItemList;

    /**
     * Constructs a ComboBox
     */
    public ComboBox() {
        setItemName("ComboBox_" + count);
        count++;

        eventKeyPress.add(this::onKeyPress);
        eventMousePress.add((sender, args) -> showDropDownList());

        selection = new ButtonCore();
        dropDown = new ButtonCore();
        arrow = new CustomShape();
        dropDownArea = new ComboBoxDropDown();

        setStyle(DefaultsService.getDefaultStyle(ComboBox.class));
    }

    public ComboBox(MenuItem... items) {
        this();
        preItemList = Arrays.asList(items);
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
        selection.setTextMargin(margin);
    }

    public void setTextMargin(int left, int top, int right, int bottom) {
        selection.setTextMargin(new Indents(left, top, right, bottom));
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
        ItemsLayoutBox.addItem(getHandler(), dropDownArea, LayoutType.FLOATING);
        dropDownArea.parent = this;
        dropDownArea.selectionChanged.add(() -> onSelectionChanged());

        if (preItemList != null) {
            for (MenuItem item : preItemList)
                dropDownArea.addItem(item);
            preItemList = null;
        }
    }

    boolean isOpened = false;

    private void showDropDownList() {

        if (isOpened) {
            isOpened = false;
        } else {
            dropDownArea.setPosition(getX(), getY() + getHeight());
            dropDownArea.setWidth(selection.getWidth());
            MouseArgs args = new MouseArgs();
            args.button = MouseButton.BUTTON_LEFT;
            dropDownArea.show(this, args);
        }
    }

    void isDropDownAreaOutsideClicked(MouseArgs args) {
        if (getHoverVerification(args.position.getX(), args.position.getY())) {
            isOpened = true;
        }
    }

    /**
     * Add item to ComboBox list
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        if (item instanceof MenuItem)
            dropDownArea.addItem(item);
        else
            super.addItem(item);
    }

    /**
     * Remove item from the ComboBox list
     */
    @Override
    public boolean removeItem(InterfaceBaseItem item) {
        return dropDownArea.removeItem(item);
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

        Style innerStyle = style.getInnerStyle("selection");
        if (innerStyle != null)
            selection.setStyle(innerStyle);

        innerStyle = style.getInnerStyle("dropdownbutton");
        if (innerStyle != null)
            dropDown.setStyle(innerStyle);

        innerStyle = style.getInnerStyle("arrow");
        if (innerStyle != null)
            arrow.setStyle(innerStyle);

        innerStyle = style.getInnerStyle("dropdownarea");
        if (innerStyle != null)
            dropDownArea.setStyle(innerStyle);

        setForeground(style.foreground);
        setFont(style.font);
    }
}