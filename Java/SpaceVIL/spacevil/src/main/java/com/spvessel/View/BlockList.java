package com.spvessel.View;

import com.spvessel.*;
import com.spvessel.Decorations.CustomFigure;
import com.spvessel.Decorations.ItemState;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.ItemStateType;
import com.spvessel.Flags.MouseButton;
import com.spvessel.Flags.SizePolicy;

import java.awt.Color;

public class BlockList extends ResizableItem {
    static int count = 0;

    private ButtonCore _palette;
    private ButtonToggle _lock;
    private TextBlock _text;
    private Label _note;
    private ButtonCore _btn_close;
    private ContextMenu _palette_menu;

    public BlockList() {
        setPassEvents(false);
        setMinSize(200, 100);
        setItemName("BlockList_" + count);
        setPadding(6, 6, 6, 6);
        count++;

        _palette = new ButtonCore();
        _lock = new ButtonToggle();
        _text = new TextBlock();
        _note = new Label();
    }

    @Override
    public void initElements() {
        _palette.setAlignment(ItemAlignment.RIGHT, ItemAlignment.TOP);
        _palette.setMargin(0, 40, 0, 0);
        _palette.setSize(20, 15);
        _palette.setBackground(255, 128, 128);
        _palette.border.setRadius(0);
        CustomShape arrow = new CustomShape();
        arrow.setTriangles(GraphicsMathService.getTriangle(30, 30, 0, 0, 180));
        arrow.setBackground(50, 50, 50);
        arrow.setSize(14, 6);
        arrow.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        arrow.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);

        _lock.setAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
        _lock.setSize(15, 15);
        _lock.border.setRadius(0);
        _lock.eventToggle.add((sender, args) -> {
            isLocked = !isLocked;
            _text.setEditable(!_text.isEditable());
        });

        _text.setHeight(25);
        _text.setAlignment(ItemAlignment.LEFT, ItemAlignment.BOTTOM);
        _text.setBackground(151, 203, 255);
        _text.setMargin(0, 60, 0, 0);

        _note.setForeground(180, 180, 180);
        _note.setHeight(25);
        _note.setAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
        _note.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
        _note.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        _note.setText("Add a Note:");
        _note.setMargin(0, 30, 0, 0);

        _btn_close = new ButtonCore();
        _btn_close.setBackground(100, 100, 100);
        _btn_close.setItemName("Close_" + getItemName());
        _btn_close.setSize(10, 10);
        _btn_close.setMargin(0, 0, 0, 0);
        _btn_close.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        _btn_close.setAlignment(ItemAlignment.TOP, ItemAlignment.RIGHT);
        ItemState hovered = new ItemState(new Color(255, 255, 255, 80));
        _btn_close.addItemState(ItemStateType.HOVERED, hovered);
        _btn_close.isCustom = new CustomFigure(false, GraphicsMathService.getCross(10, 10, 3, 45));
        _btn_close.eventMouseClick.add((sender, args) -> Dispose());

        addItems(_lock, _note, _text, _palette, _btn_close);

        _palette.addItem(arrow);

        _palette_menu = new ContextMenu(getHandler());
        _palette_menu.setBackground(60, 60, 60);
        _palette_menu.setSize(100, 123);
        MenuItem red = new MenuItem("Red");
        red.setForeground(210, 210, 210);
        red.addItemState(ItemStateType.HOVERED, hovered);
        red.eventMouseClick.add((sender, args) -> _text.setBackground(255, 196, 196));
        MenuItem green = new MenuItem("Green");
        green.setForeground(210, 210, 210);
        green.addItemState(ItemStateType.HOVERED, hovered);
        green.eventMouseClick.add((sender, args) -> _text.setBackground(138, 255, 180));
        MenuItem blue = new MenuItem("Blue");
        blue.setForeground(210, 210, 210);
        blue.addItemState(ItemStateType.HOVERED, hovered);
        blue.eventMouseClick.add((sender, args) -> _text.setBackground(151, 203, 255));
        MenuItem yellow = new MenuItem("Yellow");
        yellow.setForeground(210, 210, 210);
        yellow.addItemState(ItemStateType.HOVERED, hovered);
        yellow.eventMouseClick.add((sender, args) -> _text.setBackground(234, 232, 162));
        _palette_menu.addItems(red, green, blue, yellow);
        _palette.eventMouseClick.add((sender, args) -> _palette_menu.show(sender, args));
        _palette_menu.activeButton = MouseButton.BUTTON_LEFT;
    }

    public void Dispose() {
        getParent().removeItem(this);
    }
}