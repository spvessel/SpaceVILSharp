package com.spvessel.buildtool;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.ContextMenu;
import com.spvessel.spacevil.ImageItem;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.MenuItem;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.Rectangle;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.InterfaceMouseMethodState;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.MouseButton;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;

public class SwitchItem extends Prototype {
    private Label _name;
    private ButtonCore _btn;
    private ContextMenu _menu;
    private Rectangle _line;

    private ImageItem _icon;

    public void setIcon(BufferedImage icon, int width, int height) {
        _icon.setSize(width, height);
        _icon.setImage(icon);
    }

    public SwitchItem() {
        _name = new Label();
        _btn = new ButtonCore();
        _icon = new ImageItem();
        _line = new Rectangle();
        setAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
        setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        setSize(146, 30);
        setBackground(0, 0, 0, 50);
    }

    public void AddMenuItem(MenuItem item) {
        item.eventMouseClick.add((sender, args) -> {
            _name.setText(item.getText());
        });
        _menu.addItem(item);
        if (_name.getText().isEmpty())
            _name.setText(item.getText());
    }

    @Override
    public void initElements() {
        _btn.setSize(24, 30);
        _btn.setPadding(4, 6, 4, 6);
        _btn.setBackground(35, 35, 35);
        _btn.setMargin(2, 0, 0, 0);

        _name.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
        _name.setPadding(10, 2, 10, 0);
        _name.setMargin(26, 0, 0, 0);
        _name.setBackground(55, 55, 55);
        _name.setFont(DefaultsService.getDefaultFont());

        _menu = new ContextMenu(getHandler());
        _menu.setShadow(5, 0, 3, new Color(0, 0, 0, 150));
        _menu.activeButton = MouseButton.BUTTON_LEFT;
        _menu.setWidth(120);

        _line.setAlignment(ItemAlignment.LEFT, ItemAlignment.BOTTOM);
        _line.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        _line.setSize(2, 30);
        _line.setBackground(new Color(210, 210, 210));

        addItems(_btn, _name, _line);

        _icon.setImage(DefaultsService.getDefaultImage(EmbeddedImage.FILTER, EmbeddedImageSize.SIZE_32X32));
        _icon.setStyle(Style.getFrameStyle());
        _btn.addItem(_icon);

        eventMouseClick.add((sender, args) -> {
            MouseArgs margs = args;
            margs.position.setPosition(_name.getX(), getY() + getHeight());
            _menu.show(sender, margs);
        });
    }

    public static MenuItem getSwitchItem(String name, InterfaceMouseMethodState func) {
        MenuItem entry = new MenuItem();
        entry.setText(name);
        entry.eventMouseClick.add(func);
        return entry;
    }
}