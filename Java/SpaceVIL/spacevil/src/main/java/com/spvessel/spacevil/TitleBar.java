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

/**
 * TitleBar is the basic implementation of a user interface tittle bar of
 * window.
 * <p>
 * Extended from com.spvessel.spacevil.WindowAnchor. WindowAnchor is class
 * representing the draggable window type of an item.
 * <p>
 * Contains icon, text, minimize button, maximize button, close button.
 * <p>
 * Supports all events except drag and drop despite that this class is draggable
 * type.
 */
public class TitleBar extends WindowAnchor {

    private static int count = 0;
    private HorizontalStack _layout;

    /**
     * Direction of title bar (in Mac OS is HorizontalDirection.FROM_LEFT_TO_RIGHT,
     * in others OS HorizontalDirection.FROM_RIGHT_TO_LEFT).
     */
    public HorizontalDirection direction = HorizontalDirection.FROM_LEFT_TO_RIGHT;

    private Label _texLabel;
    private ImageItem _icon;

    /**
     * Getting icon of title bar.
     * 
     * @return Icon of title bar as com.spvessel.spacevil.ImageItem.
     */
    public ImageItem getIcon() {
        return _icon;
    }

    private ButtonCore _close;

    /**
     * Getting close button of title bar. This button closes the current window.
     * 
     * @return Close button of title bar as com.spvessel.spacevil.TitleBar.
     */
    public ButtonCore getCloseButton() {
        return _close;
    }

    private ButtonCore _minimize;

    /**
     * Getting minimize button of title bar. This button minimizes the window to the
     * taskbar.
     * 
     * @return Minimize button of title bar as com.spvessel.spacevil.TitleBar.
     */
    public ButtonCore getMinimizeButton() {
        return _minimize;
    }

    private ButtonCore _maximize;

    /**
     * Getting maximize button of title bar. This button maximizes/restores the
     * window to all available space of the current display (display size without
     * task bar and other OS elements).
     * 
     * @return Maximize button of title bar as com.spvessel.spacevil.TitleBar.
     */
    public ButtonCore getMaximizeButton() {
        return _maximize;
    }

    /**
     * Default TitleBar constructor.
     */
    public TitleBar() {
        setItemName("TitleBar_" + count);
        count++;

        _layout = new HorizontalStack();
        _texLabel = new Label();
        _texLabel.isFocusable = false;
        _minimize = new ButtonCore();
        _minimize.isFocusable = false;
        _maximize = new ButtonCore();
        _maximize.isFocusable = false;
        _maximize.setItemName("MAXIMIZE");
        _close = new ButtonCore();
        _close.isFocusable = false;
        _icon = new ImageItem();
        _icon.isFocusable = false;

        setStyle(DefaultsService.getDefaultStyle(TitleBar.class));

        eventMouseDoubleClick.add((sender, args) -> {
            getHandler().maximize();
        });
    }

    /**
     * Constructs TitleBar with the specified title text.
     * 
     * @param text Title text
     */
    public TitleBar(String text) {
        this();
        setText(text);
    }

    /**
     * Setting image icon for title bar. The image is scaled to the specified width
     * and height.
     * 
     * @param icon   Image icon as java.awt.image.BufferedImage.
     * @param width  New width of the bitmap.
     * @param height New height of the bitmap.
     */
    public void setIcon(BufferedImage icon, int width, int height) {
        _icon.setSize(width, height);
        _icon.setImage(icon);
        _icon.setVisible(true);
    }

    /**
     * Setting alignment of a TitleBar text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        _texLabel.setTextAlignment(alignment);
    }

    /**
     * Setting alignment of a TitleBar text. Combines with alignment by vertically
     * (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT).
     * 
     * @param alignment Text alignment as List of
     *                  com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        _texLabel.setTextAlignment(alignment);
    }

    /**
     * Setting indents for the text to offset text relative to TitleBar.
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setTextMargin(Indents margin) {
        _texLabel.setMargin(margin);
    }

    /**
     * Setting indents for the text to offset text relative to TitleBar.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setTextMargin(int left, int top, int right, int bottom) {
        _texLabel.setMargin(left, top, right, bottom);
    }

    /**
     * Getting indents of the text.
     * 
     * @return Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents getTextMargin() {
        return _texLabel.getMargin();
    }

    /**
     * Setting font of the text.
     * 
     * @param font Font as java.awt.Font.
     */
    public void setFont(Font font) {
        _texLabel.setFont(font);
    }

    /**
     * Setting font size of the text.
     * 
     * @param size New size of the font.
     */
    public void setFontSize(int size) {
        _texLabel.setFontSize(size);
    }

    /**
     * Setting font style of the text.
     * 
     * @param style New font style (from java.awt.Font package).
     */
    public void setFontStyle(int style) {
        _texLabel.setFontStyle(style);
    }

    /**
     * Setting new font family of the text.
     * 
     * @param fontFamily New font family name.
     */
    public void setFontFamily(String fontFamily) {
        _texLabel.setFontFamily(fontFamily);
    }

    /**
     * Getting the current font of the text.
     * 
     * @return Font as java.awt.Font.
     */
    public Font getFont() {
        return _texLabel.getFont();
    }

    /**
     * Setting the text.
     * 
     * @param text Text as java.lang.String.
     */
    public void setText(String text) {
        _texLabel.setText(text);
    }

    /**
     * Getting the current text of the TitleBar.
     * 
     * @return Text as java.lang.String.
     */
    public String getText() {
        return _texLabel.getText();
    }

    /**
     * Getting the text width (useful when you need resize TitleBar by text width).
     * 
     * @return Text width.
     */
    public int getTextWidth() {
        return _texLabel.getWidth();
    }

    /**
     * Getting the text height (useful when you need resize TitleBar by text
     * height).
     * 
     * @return Text height.
     */
    public int getTextHeight() {
        return _texLabel.getHeight();
    }

    /**
     * Setting text color of a TitleBar.
     * 
     * @param color Text color as java.awt.Color.
     */
    public void setForeground(Color color) {
        _texLabel.setForeground(color);
    }

    /**
     * Setting text color of a TitleBar in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        _texLabel.setForeground(r, g, b);
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
        _texLabel.setForeground(r, g, b, a);
    }

    /**
     * Setting text color of a TitleBar in float RGB format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        _texLabel.setForeground(r, g, b);
    }

    /**
     * Setting text color of a TitleBar in float RGBA format.
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b, float a) {
        _texLabel.setForeground(r, g, b, a);
    }

    /**
     * Getting current text color.
     * 
     * @return Text color as as java.awt.Color.
     */
    public Color getForeground() {
        return _texLabel.getForeground();
    }

    /**
     * Initializing all elements in the TitleBar.
     * <p>
     * Notice: This method is mainly for overriding only. SpaceVIL calls this method
     * if necessary and no need to call it manually.
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
                _layout.addItems(_icon, _texLabel, _minimize, _maximize, _close);
                break;
            case FROM_RIGHT_TO_LEFT:
                _texLabel.setTextAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
                _layout.addItems(_close, _minimize, _maximize, _icon, _texLabel);
                break;
            default:
                _layout.addItems(_icon, _texLabel, _minimize, _maximize, _close);
                break;
        }
        _minimize.setPassEvents(false);
        _maximize.setPassEvents(false);
        _close.setPassEvents(false);
    }

    /**
     * Setting style of the TitleBar.
     * <p>
     * Inner styles: "closebutton", "minimizebutton", "maximizebutton", "title".
     * 
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
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
