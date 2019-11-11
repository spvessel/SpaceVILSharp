package com.spvessel.spacevil;

import com.spvessel.spacevil.Flags.HorizontalDirection;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class TitleBar extends WindowAnchor {

    private static int count = 0;
    private HorizontalStack _layout;
    public HorizontalDirection direction = HorizontalDirection.FROM_LEFT_TO_RIGHT;
    private Label _textObject;
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
        _textObject = new Label();
        _textObject.isFocusable = false;
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
        setStyle(DefaultsService.getDefaultStyle(TitleBar.class));
        // ItemState state = new ItemState(new Color(255, 255, 255, 100));
        // _text_object.addItemState(ItemStateType.HOVERED, state);

        eventMouseDoubleClick.add((sender, args) -> {
            getHandler().maximize();
        });
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
     * 
     * @param icon   BufferedImage icon image
     * @param width  icon image width
     * @param height icon image height
     */
    public void setIcon(BufferedImage icon, int width, int height) {
        _icon.setSize(width, height);
        _icon.setImage(icon);
        _icon.setVisible(true);
    }

    // text init
    /**
     * Text alignment in the TitleBar
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _textObject.setTextAlignment(alignment);
    }

    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textObject.setTextAlignment(alignment);
    }

    /**
     * Text margin in the TitleBar
     */
    public void setTextMargin(Indents margin) {
        _textObject.setMargin(margin);
    }

    /**
     * Text font parameters in the TitleBar
     */
    public void setFont(Font font) {
        _textObject.setFont(font);
    }

    public void setFontSize(int size) {
        _textObject.setFontSize(size);
    }

    public void setFontStyle(int style) {
        _textObject.setFontStyle(style);
    }

    public void setFontFamily(String font_family) {
        _textObject.setFontFamily(font_family);
    }

    public Font getFont() {
        return _textObject.getFont();
    }

    /**
     * TitleBar text
     */
    public void setText(String text) {
        _textObject.setText(text);
    }

    public String getText() {
        return _textObject.getText();
    }

    /**
     * Text color in the TitleBar
     */
    public void setForeground(Color color) {
        _textObject.setForeground(color);
    }

    public void setForeground(int r, int g, int b) {
        _textObject.setForeground(r, g, b);
    }

    public void setForeground(int r, int g, int b, int a) {
        _textObject.setForeground(r, g, b, a);
    }

    public void setForeground(float r, float g, float b) {
        _textObject.setForeground(r, g, b);
    }

    public void setForeground(float r, float g, float b, float a) {
        _textObject.setForeground(r, g, b, a);
    }

    public Color getForeground() {
        return _textObject.getForeground();
    }

    /**
     * Initialization and adding of all elements in the TitleBar
     */
    @Override
    public void initElements() {
        addItem(_layout);

        // _close
        _close.eventMouseClick.add((sender, args) -> {
            getHandler().eventClose.execute();
        });

        // _minimize
        _minimize.eventMouseClick.add((sender, args) -> {
            getHandler().minimize();
        });

        // _maximize
        _maximize.eventMouseClick.add((sender, args) -> {
            getHandler().maximize();
        });

        // adding
        switch (direction) {
        case FROM_LEFT_TO_RIGHT:
            _layout.addItems(_icon, _textObject, _minimize, _maximize, _close);
            break;
        case FROM_RIGHT_TO_LEFT:
            _textObject.setTextAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
            _layout.addItems(_close, _minimize, _maximize, _icon, _textObject);
            break;
        default:
            _layout.addItems(_icon, _textObject, _minimize, _maximize, _close);
            break;
        }
        _minimize.setPassEvents(false);
        _maximize.setPassEvents(false);
        _close.setPassEvents(false);
    }

    /**
     * Set style of the TitleBar
     */
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
        inner_style = style.getInnerStyle("title");
        if (inner_style != null) {
            setTextMargin(inner_style.margin);
        }

        _icon.setVisible(false);
        _icon.setBackground(0, 0, 0, 0);
        _icon.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        _icon.setAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
    }
}
