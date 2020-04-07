package com.spvessel.spacevil;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceItem;
import com.spvessel.spacevil.Core.KeyArgs;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.LayoutType;
import com.spvessel.spacevil.Flags.MouseButton;

/**
 * ComboBox is an item allowing to select one of the many options from the list.
 * <p>
 * Contains text, drop-down button, drop-down list.
 * <p>
 * Supports all events except drag and drop.
 */
public class ComboBox extends Prototype {

    static int count = 0;
    ButtonCore selection;
    ButtonCore dropDown;
    CustomShape arrow;
    ComboBoxDropDown dropDownArea;
    /**
     * Event that is invoked when one of the options is selected.
     */
    public EventCommonMethod selectionChanged = new EventCommonMethod();
    /**
     * Property that allows to specify what item will be focused after drop-down
     * list is closed.
     */
    public Prototype returnFocus = null;

    /**
     * Disposing ComboBox resources if it was removed.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
     */
    @Override
    public void release() {
        selectionChanged.clear();
    }

    private List<MenuItem> preItemList;

    /**
     * Default ComboBox constructor. Options list is empty.
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

    /**
     * Constructs ComboBox with spesified sequence of options (as
     * com.spvessel.spacevil.MenuItem).
     * 
     * @param items Sequence of options as com.spvessel.spacevil.MenuItem.
     */
    public ComboBox(MenuItem... items) {
        this();
        preItemList = Arrays.asList(items);
    }

    private void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (args.key == KeyCode.ENTER && eventMouseClick != null) {
            eventMouseClick.execute(this, new MouseArgs());
        }
    }

    /**
     * Setting alignment of a ComboBox text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        selection.setTextAlignment(alignment);
    }

    /**
     * Setting alignment of a ComboBox text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as List of
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        selection.setTextAlignment(alignment);
    }

    /**
     * Setting indents for the text to offset text relative to ComboBox.
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        selection.setMargin(margin);
    }

    /**
     * Setting indents for the text to offset text relative to ComboBox.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setTextMargin(int left, int top, int right, int bottom) {
        selection.setMargin(left, top, right, bottom);
    }

    /**
     * Getting indents of the text.
     * 
     * @return Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getTextMargin() {
        return selection.getMargin();
    }

    /**
     * Setting font of the text.
     * 
     * @param font Font as java.awt.Font.
     */
    public void setFont(Font font) {
        selection.setFont(font);
    }

    /**
     * Setting font size of the text.
     * 
     * @param size New size of the font.
     */
    public void setFontSize(int size) {
        selection.setFontSize(size);
    }

    /**
     * Setting font style of the text.
     * 
     * @param style New font style (from java.awt.Font package).
     */
    public void setFontStyle(int style) {
        selection.setFontStyle(style);
    }

    /**
     * Setting new font family of the text.
     * 
     * @param fontFamily New font family name.
     */
    public void setFontFamily(String fontFamily) {
        selection.setFontFamily(fontFamily);
    }

    /**
     * Getting the current font of the text.
     * 
     * @return Font as java.awt.Font.
     */
    public Font getFont() {
        return selection.getFont();
    }

    /**
     * Setting the text.
     * 
     * @param text Text as java.lang.String.
     */
    public void setText(String text) {
        selection.setText(text);
    }

    /**
     * Getting the current text of the ComboBox.
     * 
     * @return Text as java.lang.String.
     */
    public String getText() {
        return selection.getText();
    }

    /**
     * Getting the text width (useful when you need resize ComboBox by text width).
     * 
     * @return Text width.
     */
    public int getTextWidth() {
        return selection.getWidth();
    }

    /**
     * Getting the text height (useful when you need resize ComboBox by text
     * height).
     * 
     * @return Text height.
     */
    public int getTextHeight() {
        return selection.getHeight();
    }

    /**
     * Setting text color of a ComboBox.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        selection.setForeground(color);
    }

    /**
     * Setting text color of a ComboBox in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        selection.setForeground(r, g, b);
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
        selection.setForeground(r, g, b, a);
    }

    /**
     * Setting text color of a ComboBox in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        selection.setForeground(r, g, b);
    }

    /**
     * Setting text color of a ComboBox in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b, float a) {
        selection.setForeground(r, g, b, a);
    }

    /**
     * Getting current text color.
     * 
     * @return Text color as as java.awt.Color.
     */
    public Color getForeground() {
        return selection.getForeground();
    }

    /**
     * Initializing and adding of all elements in the ComboBox (drop-down list, drop
     * bown button, selection, options and etc.).
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
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

    /**
     * Opens drop-down list.
     */
    public void open() {
        showDropDownList();
    }

    void isDropDownAreaOutsideClicked(MouseArgs args) {
        if (getHoverVerification(args.position.getX(), args.position.getY())) {
            isOpened = true;
        }
    }

    /**
     * Adding item to ComboBox. If item is com.spvessel.spacevil.MenuItem then it is
     * added to the drop-down list as an option.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    @Override
    public void addItem(InterfaceBaseItem item) {
        if (item instanceof MenuItem)
            dropDownArea.addItem(item);
        else
            super.addItem(item);
    }

    /**
     * Removing item from ComboBox. If item is com.spvessel.spacevil.MenuItem then
     * it is removed from the drop-down list.
     * 
     * @param item Item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @return True: if the removal was successful. False: if the removal was
     *         unsuccessful.
     */
    @Override
    public boolean removeItem(InterfaceBaseItem item) {
        return dropDownArea.removeItem(item);
    }

    /**
     * Selecting option by its index in the drop-down list.
     * 
     * @param index Index in the drop-down list.
     */
    public void setCurrentIndex(int index) {
        Prototype currentFocus = getHandler().getFocusedItem();
        dropDownArea.setCurrentIndex(index);
        currentFocus.setFocus();
        selection.setText(dropDownArea.getText());
        selectionChanged.execute();
    }

    /**
     * Getting index of selected option in the drop-down list.
     * 
     * @return Index of selected option.
     */
    public int getCurrentIndex() {
        return dropDownArea.getCurrentIndex();
    }

    private void onSelectionChanged() {
        selection.setText(dropDownArea.getText());
        selectionChanged.execute();
    }

    /**
     * Setting style of the ComboBox.
     * <p>
     * Inner styles: "selection", "dropdownbutton", "arrow", "dropdownarea".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
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