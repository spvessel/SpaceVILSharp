package com.spvessel.Items;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import com.spvessel.Common.*;
import com.spvessel.Cores.*;
import com.spvessel.Decorations.*;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.KeyCode;
import com.spvessel.Windows.ComboBoxDropDown;

public class ComboBox extends VisualItem {
    // Queue<BaseItem> _queue = new Queue<BaseItem>();

    static int count = 0;
    public ButtonCore _selected = new ButtonCore();
    public ButtonCore _dropdown = new ButtonCore();
    public CustomShape _arrow = new CustomShape();
    public ComboBoxDropDown _dropdownarea = new ComboBoxDropDown();
    public EventCommonMethod selectionChanged = new EventCommonMethod();

    public ComboBox() {
        setBackground(0, 0, 0, 0);
        setItemName("ComboBox_" + count);
        count++;

        InterfaceKeyMethodState key_press = (sender, args) -> onKeyPress(sender, args);
        eventKeyPress.add(key_press);

        InterfaceMouseMethodState press = (sender, args) -> showDropDownList();
        eventMousePressed.add(press);

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.ComboBox"));
        setStyle(DefaultsService.getDefaultStyle(com.spvessel.Items.ComboBox.class));
    }

    protected void onKeyPress(InterfaceItem sender, KeyArgs args) {
        if (args.key == KeyCode.ENTER && eventMouseClick != null) {
            eventMouseClick.execute(this, new MouseArgs());
        }
    }

    // text init
    public void setTextAlignment(ItemAlignment... alignment) {
        _selected.setTextAlignment(alignment);
    }
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _selected.setTextAlignment(alignment);
    }

    public void setTextMargin(Indents margin) {
        _selected.setMargin(margin);
    }

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

    public void setText(String text) {
        _selected.setText(text);
    }

    public String getText() {
        return _selected.getText();
    }

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

    @Override
    public void initElements() {
        // adding
        addItems(_selected, _dropdown);

        _dropdown.addItem(_arrow);
        // _selected.setTextAlignment(ItemAlignment.Left | ItemAlignment.VCenter);

        // dropdownarea
        _dropdownarea.selection = _selected;
        InterfaceCommonMethod selection_changed = () -> onSelectionChanged();
        _dropdownarea.selectionChanged.add(selection_changed);
    }

    private void showDropDownList() {
        // dropdownarea
        _dropdownarea.getHandler().setWidth(_selected.getWidth());
        _dropdownarea.getHandler().setHeight(100);

        _dropdownarea.getHandler().setX(getHandler().getX() + _selected.getX());
        _dropdownarea.getHandler().setY(getHandler().getY() + _selected.getY() + _selected.getHeight());

        _dropdownarea.show();
    }

    public void addToList(BaseItem item) {
        _dropdownarea.add(item);
        // _queue.Enqueue(item);
    }

    public void removeFromLst(BaseItem item) {
        _dropdownarea.remove(item);
    }

    public void setCurrentIndex(int index) {
        _dropdownarea.setCurrentIndex(index);
    }

    public int getCurrentIndex() {
        return _dropdownarea.getCurrentIndex();
    }

    private void onSelectionChanged() {
        selectionChanged.execute();
    }

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