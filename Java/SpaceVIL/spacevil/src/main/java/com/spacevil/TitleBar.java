package com.spacevil;

import com.spacevil.Flags.HorizontalDirection;
import com.spacevil.Core.InterfaceMouseMethodState;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class TitleBar extends WindowAnchor {

    private static int count = 0;
    private HorizontalStack _layout;
    public HorizontalDirection Direction = HorizontalDirection.FROM_LEFT_TO_RIGHT;
    private Label _text_object;
    private ImageItem _icon;

    /**
     * @return TitleBar icon ImageItem type
     */
    public ImageItem getIcon() {
        return _icon;
    }

    private ButtonCore _close;

    /**
     * @return close button ButtonCore type
     */
    public ButtonCore getCloseButton() {
        return _close;
    }

    private ButtonCore _minimize;

    /**
     * @return minimize button ButtonCore type
     */
    public ButtonCore getMinimizeButton() {
        return _minimize;
    }

    private ButtonCore _maximize;

    /**
     * @return maximize button ButtonCore type
     */
    public ButtonCore getMaximizeButton() {
        return _maximize;
    }

    /**
     * Constructs a TitleBar
     */
    public TitleBar() {
        setItemName("TitleBar_" + count);
        count++;

        _layout = new HorizontalStack();
        _text_object = new Label();
        _minimize = new ButtonCore();
        _minimize.isFocusable = false;
        _maximize = new ButtonCore();
        _maximize.isFocusable = false;
        _maximize.setItemName("MAXIMIZE");
        _close = new ButtonCore();
        _close.isFocusable = false;
        _icon = new ImageItem();
        _icon.isFocusable = false;

        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.TitleBar"));
        setStyle(com.spacevil.Common.DefaultsService.getDefaultStyle(TitleBar.class));
        // ItemState state = new ItemState(new Color(255, 255, 255, 100));
        // _text_object.addItemState(ItemStateType.HOVERED, state);
    }

    /**
     * Constructs a TitleBar with text
     */
    public TitleBar(String text) {
        this();
        setText(text);
    }

    /**
     * Set TitleBar icon
     * @param icon BufferedImage icon image
     * @param width icon image width
     * @param height icon image height
     */
    public void setIcon(BufferedImage icon, int width, int height) {
        _icon.setSize(width, height);
        _icon.setImage(icon);
        _icon.setVisible(true);
    }

    /**
     * Set TitleBar icon
     * @param url the path to the icon file
     * @param width icon width
     * @param height icon height
     */
    public void setIcon(String url, int width, int height) {
        _icon.setSize(width, height);
        _icon.setImageUrl(url);
        _icon.setVisible(true);
    }


    // text init
    /**
     * Text alignment in the TitleBar
     */
    public void setTextAlignment(com.spacevil.Flags.ItemAlignment... alignment) {
        _text_object.setTextAlignment(alignment);
    }
    public void setTextAlignment(List<com.spacevil.Flags.ItemAlignment> alignment) {
        _text_object.setTextAlignment(alignment);
    }

    /**
     * Text margin in the TitleBar
     */
    public void setTextMargin(com.spacevil.Decorations.Indents margin) {
        _text_object.setMargin(margin);
    }

    /**
     * Text font parameters in the TitleBar
     */
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

    /**
     * TitleBar text
     */
    public void setText(String text) {
        _text_object.setText(text);
    }
    public String getText() {
        return _text_object.getText();
    }

    /**
     * Text color in the TitleBar
     */
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

    /**
     * Initialization and adding of all elements in the TitleBar
     */
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
            //System.out.println(_maximize.getBorderThickness() + " " + _maximize.getBorderFill());
        };
        _maximize.eventMouseClick.add(maximize_click);

        // adding
        switch (Direction) {
        case FROM_LEFT_TO_RIGHT:
            _layout.addItems(_icon, _text_object, _minimize, _maximize, _close);
            break;
        case FROM_RIGHT_TO_LEFT:
            _layout.addItems(_close, _maximize, _minimize, _icon, _text_object);
            break;
        default:
            _layout.addItems(_icon, _text_object, _minimize, _maximize, _close);
            break;
        }
        _minimize.setPassEvents(false);
        _maximize.setPassEvents(false);
        _close.setPassEvents(false);

        // _maximize.setBorderFill(new Color(255, 255, 255));
        // _maximize.setBorderThickness(2);

        // Rectangle center = new Rectangle();
        // center.setBackground(getBackground());
        // center.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        // _maximize.addItem(center);

        // update text data
        // _text_object.UpdateData(UpdateType.Critical);
        // LogService.Log().LogBaseItem(_close, LogProps.Behavior |
        // LogProps.AllGeometry);
    }

    /**
     * Set style of the TitleBar
     */
    // style
    @Override
    public void setStyle(com.spacevil.Decorations.Style style) {

        if (style == null)
            return;

        super.setStyle(style);
        setForeground(style.foreground);
        setFont(style.font);
        setTextAlignment(style.textAlignment);
        _layout.setSpacing(style.spacing);

        com.spacevil.Decorations.Style inner_style = style.getInnerStyle("closebutton");
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
            // System.out.println(_maximize.getBorderThickness() + " " +_maximize.getBorderFill());
        }
        inner_style = style.getInnerStyle("title");
        if (inner_style != null) {
            setTextMargin(inner_style.margin);
        }

        // icon
        _icon.setVisible(false);
        _icon.setBackground(0, 0, 0, 0);
        _icon.setSizePolicy(com.spacevil.Flags.SizePolicy.FIXED, com.spacevil.Flags.SizePolicy.FIXED);
        _icon.setAlignment(com.spacevil.Flags.ItemAlignment.VCENTER, com.spacevil.Flags.ItemAlignment.LEFT);
    }
}
