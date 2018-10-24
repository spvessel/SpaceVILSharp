package com.spvessel.Items;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import com.spvessel.Decorations.*;
import com.spvessel.Common.*;
import com.spvessel.Cores.*;
import com.spvessel.Flags.*;
import com.spvessel.Flags.SizePolicy;

public class TitleBar extends WindowAnchor {

    static int count = 0;
    private HorizontalStack _layout;
    public HDirection Direction = HDirection.FROM_LEFT_TO_RIGHT;
    private Label _text_object;
    //private ImageItem _icon;

    // public ImageItem getIcon() {
    //     return _icon;
    // }

    private ButtonCore _close;

    public ButtonCore getCloseButton() {
        return _close;
    }

    private ButtonCore _minimize;

    public ButtonCore getMinimizeButton() {
        return _minimize;
    }

    private ButtonCore _maximize;

    public ButtonCore getMaximizeButton() {
        return _maximize;
    }

    public TitleBar() {
        setItemName("TitleBar_" + count);
        count++;

        _layout = new HorizontalStack();
        _text_object = new Label();
        _minimize = new ButtonCore();
        _maximize = new ButtonCore();
        _close = new ButtonCore();
        // _icon = new ImageItem();

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.TitleBar"));
        setStyle(DefaultsService.getDefaultStyle(com.spvessel.Items.TitleBar.class));
        // ItemState state = new ItemState(new Color(255, 255, 255, 100));
        // _text_object.addItemState(ItemStateType.HOVERED, state);
    }

    public TitleBar(String text) {
        this();
        setText(text);
    }

    public void setIcon(BufferedImage icon, int width, int height) {
        // _icon.setSize(width, height);
        // _icon.setImage(icon);
        // _icon.setVisible(false);
    }

    public void setIcon(String url, int width, int height) {
        // _icon.setSize(width, height);
        // _icon.setImageUrl(url);
        // _icon.setVisible(false);
    }

    // text init
    public void setTextAlignment(ItemAlignment... alignment) {
        _text_object.setTextAlignment(alignment);
    }
    
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    public void setTextMargin(Indents margin) {
        _text_object.setMargin(margin);
    }

    public void setFont(Font font) {
        _text_object.setFont(font);
    }

    public void setFontSize(int size) {
        _text_object.setFontSize(size);
    }

    public void setFontStyle(int style) {
        _text_object.setFontStyle(style);
    }

    public void setFontFamily(String font_family) {
        _text_object.setFontFamily(font_family);
    }

    public Font getFont() {
        return _text_object.getFont();
    }

    public void setText(String text) {
        _text_object.setText(text);
    }

    public String getText() {
        return _text_object.getText();
    }

    public void setForeground(Color color) {
        _text_object.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        _text_object.setForeground(r, g, b);
    }

    public void setForeground(int r, int g, int b, int a) {
        _text_object.setForeground(r, g, b, a);
    }

    public void setForeground(float r, float g, float b) {
        _text_object.setForeground(r, g, b);
    }

    public void setForeground(float r, float g, float b, float a) {
        _text_object.setForeground(r, g, b, a);
    }

    public Color getForeground() {
        return _text_object.getForeground();
    }

    @Override
    public void initElements() {
        addItem(_layout);

        // text
        // setFont(new Font(new FontFamily("Open Sans Light"), 16, FontStyle.Bold));

        // _close
        InterfaceMouseMethodState close_click = (sender, args) -> {
            getHandler().close();
        };
        _close.eventMouseClick.add(close_click);

        // _minimize
        InterfaceMouseMethodState minimize_click = (sender, args) -> {
            getHandler().minimize();
        };
        _minimize.eventMouseClick.add(minimize_click);

        // _maximize
        InterfaceMouseMethodState maximize_click = (sender, args) -> {
            getHandler().maximize();
        };
        _maximize.eventMouseClick.add(maximize_click);

        // adding
        switch (Direction) {
        case FROM_LEFT_TO_RIGHT:
            _layout.addItems(/*_icon,*/ _text_object, _minimize, _maximize, _close);
            break;
        case FROM_RIGHT_TO_LEFT:
            _layout.addItems(_close, _maximize, _minimize, /*_icon,*/ _text_object);
            break;
        default:
            _layout.addItems(/*_icon,*/ _text_object, _minimize, _maximize, _close);
            break;
        }
        _minimize.setPassEvents(false);
        _maximize.setPassEvents(false);
        _close.setPassEvents(false);
        Rectangle center = new Rectangle();
        center.setBackground(getBackground());
        center.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        _maximize.addItem(center);

        // update text data
        // _text_object.UpdateData(UpdateType.Critical);
        // LogService.Log().LogBaseItem(_close, LogProps.Behavior |
        // LogProps.AllGeometry);
    }

    // style
    @Override
    public void setStyle(Style style) {

        if (style == null)
            return;

        super.setStyle(style);
        setForeground(style.foreground);
        setFont(style.font);
        setTextAlignment(style.textAlignment);
        _layout.setSpacing(style.spacing);

        Style inner_style = style.getInnerStyle("closebutton");
        if (inner_style != null) {
            _close.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("minimizebutton");
        if (inner_style != null) {
            _minimize.setStyle(inner_style);
        }
        inner_style = style.getInnerStyle("maximizebutton");
        if (inner_style != null) {
            _maximize.setStyle(inner_style);
        }

        // icon
        // _icon.setVisible(false);
        // _icon.setBackground(0, 0, 0, 0);
        // _icon.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        // _icon.setAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
    }
}