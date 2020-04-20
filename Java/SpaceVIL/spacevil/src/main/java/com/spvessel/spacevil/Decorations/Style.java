package com.spvessel.spacevil.Decorations;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.GraphicsMathService;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.SpaceVILConstants;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * Style is a class that describes the appearance of an element. Can contains Styles for inner items.
 */
public class Style implements Cloneable {
    private Map<String, Style> _innerStyles = new HashMap<>();

    /**
     * Background color of an item's shape. Attention: this property is required.
     * <p> This property is java.awt.Color.
     */
    public Color background;

    /**
     * Color of an item's text. Can be used only if the item contains text and in this case 
     * this property is required.
     * <p> This property is java.awt.Color.
     */
    public Color foreground;

    /**
     * Font of an item's text. Can be used only if the item contains text and in this case 
     * this property is required.
     * <p> This property is java.awt.Font
     */
    public Font font = null;

    /**
     * Width policy of an item's shape. Can be Fixed (shape not changes its size) or 
     * Expand (shape is stretched to all available space). Attention: this property is required.
     * <p> This property is com.spvessel.spacevil.Flags.SizePolicy.
     */
    public SizePolicy widthPolicy;

    /**
     * Height policy of an item's shape. Can be Fixed (shape not changes its size) or 
     * Expand (shape is stretched to all available space). Attention: this property is required.
     * <p> This property is com.spvessel.spacevil.Flags.SizePolicy.
     */
    public SizePolicy heightPolicy;

    /**
     * Width of an item's shape.
     */
    public int width;

    /**
     * Minimum width of an item's shape (shape cannot be smaller this value).
     * <p> Default: 0.
     */
    public int minWidth;

    /**
     * Maximum width of an item's shape (shape cannot be bigger this value).
     * <p> Default: 32767.
     */
    public int maxWidth;

    /**
     * Height of an item's shape.
     */
    public int height;

    /**
     * Minimum height of an item's shape (shape cannot be smaller this value).
     * <p> Default: 0.
     */
    public int minHeight;

    /**
     * Maximum height of an item's shape (shape cannot be bigget this value).
     * <p> Default: 32767.
     */
    public int maxHeight;

    /**
     * Alignment of an item's shape relative to its container. 
     * Combines with alignment by vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT). 
     * Attention: this property is required.
     * <p> This property is com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public List<ItemAlignment> alignment;

    /**
     * Alignment of an item's text. 
     * Combines with alignment by vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT). 
     * Can be used only if the item contains text and in this case this property is required.
     * <p> This property is com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public List<ItemAlignment> textAlignment;

    /**
     * X axis position of left-top cornet of an item's shape. This property itself is mostly ignored. 
     * Used only when creating container-type items or with com.spvessel.spacevil.FreeArea.
     */
    public int x;

    /**
     * Y axis position of left-top cornet of an item's shape. This property itself is mostly ignored. 
     * Used only when creating container-type items or with com.spvessel.spacevil.FreeArea.
     */
    public int y;

    private Map<ItemStateType, ItemState> _itemStates = new HashMap<>();

    /**
     * Indents of an item to offset its children. Attention: this property is required.
     * <p> This property is com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents padding = new Indents();

    /**
     * Indents between children of a container type item. It is used mainly in containers.
     * <p> This property is com.spvessel.spacevil.Decorations.Spacing.
     */
    public Spacing spacing = new Spacing();

    /**
     * Indents of an item to offset itself relative to its container. Attention: this property is required.
     * <p> This property is com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents margin = new Indents();

    /**
     * Radiuses to round the rectangular shape of the item.
     */
    public CornerRadius borderRadius = new CornerRadius();

    /**
     * Thickness of an item's border. 
     * <p> Default: 0.
     */
    public int borderThickness = 0;

    /**
     * Color of an item's border. 
     * <p> This property is java.awt.Color.
     */
    public Color borderFill = new Color(0, 0, 0, 0);

    /**
     * A form of an item's shape. If not assigned, the shape is rectangular.
     * <p> Format: java.util.List&lt;float[]&gt;.
     */
    public List<float[]> shape;// = new List<float[]>();

    /**
     * A flag that determines if the shape of an item can be changed or not.
     * <p> True: if shape can not be resized. False: if shape can be resised. Default: False.
     */
    public boolean isFixedShape = false;

    /**
     * A storage of shapes for future use. Note: not supported in the current version!
     * <p> Format: java.util.List&lt;com.spvessel.spacevil.Core.InterfaceBaseItem&gt;.
     */
    public List<InterfaceBaseItem> innerShapes;// = new List<float[]>();

    /**
     * Blur radius of a shadow.
     * <p> Min value: 0. Max value: 10. Default: 0.
     */
    public int shadowRadius;

    /**
     * X shift of a shadow.
     */
    public int shadowXOffset;

    /**
     * Y shift of a shadow.
     */
    public int shadowYOffset;

    /**
     * Drop shadow flag. True: allow shadow dropping. False: not allow shadow dropping.
     * <p> Default: False.
     */
    public boolean isShadowDrop = false;

    /**
     * Color of a shadow.
     * <p> This property is java.awt.Color.
     */
    public Color shadowColor;

    /**
     * A flag that determines if an item is visible or not.
     * <p> True: if visible. False: if not visible. Default: True.
     */
    public boolean isVisible;

    /**
     * Constructs a default Style.
     */
    public Style()// default
    {
        isVisible = true;
        maxWidth = SpaceVILConstants.sizeMaxValue;
        maxHeight = SpaceVILConstants.sizeMaxValue;
        setAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
    }

    /**
     * Setting this style for all items in sequence.
     * @param items A sequence of items that are com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    public void setStyle(InterfaceBaseItem... items) {
        for (InterfaceBaseItem item : items) {
            item.setStyle(this);
        }
    }

    /**
     * Setting size of an item's shape.
     * @param width Width of a shape.
     * @param height Height of a shape.
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Setting minimum size of an item's shape (shape can not be smaller than specified width and height).
     * @param width Minimum width of a shape.
     * @param height Minimum height of a shape.
     */
    public void setMinSize(int width, int height) {
        minWidth = width;
        minHeight = height;
    }

    /**
     * Setting maximim size of an item's shape (shape can not be bigger than specified width and height).
     * @param width Maximim width of a shape.
     * @param height Maximim height of a shape.
     */
    public void setMaxSize(int width, int height) {
        maxWidth = width;
        maxHeight = height;
    }

    /**
     * Setting the size policy of an item's shape. 
     * Can be FIXED (shape not changes its size) or EXPAND (shape is stretched to all available space).
     * @param widthPolicy Width policy of an item's shape.
     * @param heightPolicy Height policy of an item's shape.
     */
    public void setSizePolicy(SizePolicy widthPolicy, SizePolicy heightPolicy) {
        this.widthPolicy = widthPolicy;
        this.heightPolicy = heightPolicy;
    }

    /**
     * Setting background color of an item's shape in byte RGB format.
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setBackground(int r, int g, int b) {
        background = GraphicsMathService.colorTransform(r, g, b);
    }

    /**
     * Setting background color of an item's shape in byte RGBA format.
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setBackground(int r, int g, int b, int a) {
        background = GraphicsMathService.colorTransform(r, g, b, a);
    }

    /**
     * Setting background color of an item's shape in float RGB format.
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setBackground(float r, float g, float b) {
        background = GraphicsMathService.colorTransform(r, g, b);
    }

    /**
     * Setting background color of an item's shape in float RGBA format.
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setBackground(float r, float g, float b, float a) {
        background = GraphicsMathService.colorTransform(r, g, b, a);
    }

    /**
     * Setting text color of an item in byte RGB format.
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        foreground = GraphicsMathService.colorTransform(r, g, b);
    }

    /**
     * Setting text color of an item in byte RGBA format.
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     * @param a Alpha color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b, int a) {
        foreground = GraphicsMathService.colorTransform(r, g, b, a);
    }

    /**
     * Setting text color of an item in float RGB format.
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        foreground = GraphicsMathService.colorTransform(r, g, b);
    }

    /**
     * Setting text color of an item in float RGBA format.
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     * @param a Alpha color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b, float a) {
        foreground = GraphicsMathService.colorTransform(r, g, b, a);
    }

    /**
     * Setting indents of an item to offset its children.
     * @param padding Padding indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setPadding(Indents padding) {
        this.padding = padding;
    }

    /**
     * Setting indents of an item to offset its children.
     * @param left Indent on the left.
     * @param top Indent on the top.
     * @param right Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setPadding(int left, int top, int right, int bottom) {
        padding.left = left;
        padding.top = top;
        padding.right = right;
        padding.bottom = bottom;
    }

    /**
     * Setting indents of an item to offset itself relative to its container.
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setMargin(Indents margin) {
        this.margin = margin;
    }

    /**
     * Setting indents of an item to offset itself relative to its container.
     * @param left Indent on the left.
     * @param top Indent on the top.
     * @param right Indent on the right.
     * @param bottom Indent on the bottom.
     */
    public void setMargin(int left, int top, int right, int bottom) {
        margin.left = left;
        margin.top = top;
        margin.right = right;
        margin.bottom = bottom;
    }

    /**
     * Setting indents between children of a container type item.
     * @param spacing Spacing as com.spvessel.spacevil.Decorations.Spacing.
     */
    public void setSpacing(Spacing spacing) {
        this.spacing = spacing;
    }

    /**
     * Setting indents between children of a container type item.
     * @param horizontal Horizontal indent.
     * @param vertical Vertical indent.
     */
    public void setSpacing(int horizontal, int vertical) {
        spacing.horizontal = horizontal;
        spacing.vertical = vertical;
    }

    /**
     * Setting border of an item's shape. Border consist of corner radiuses, thickness and color.
     * @param border Border as com.spvessel.spacevil.Decorations.Border.
     */
    public void setBorder(Border border) {
        borderFill = border.getFill();
        borderRadius = border.getRadius();
        borderThickness = border.getThickness();
    }

    /**
     * Setting border for an item's shape. Border consist of corner radiuses, thickness and color.
     * @param fill Border color as java.awt.Color.
     * @param radius Radiuses of an border corners as com.spvessel.spacevil.Decorations.CornerRadius.
     * @param thickness Border thickness.
     */
    public void setBorder(Color fill, CornerRadius radius, int thickness) {
        borderFill = fill;
        borderRadius = radius;
        borderThickness = thickness;
    }

    /**
     * Setting shadow for an item's shape. 
     * @param shadow Shadow as com.spvessel.spacevil.Decorations.Shadow.
     */
    public void setShadow(Shadow shadow) {
        shadowColor = shadow.getColor();// GraphicsMathService.cloneColor(shadow.getColor());
        shadowRadius = shadow.getRadius();
        shadowXOffset = shadow.getXOffset();
        shadowYOffset = shadow.getYOffset();
    }

    /**
     * Setting an Alignment of an item's shape relative to its container. 
     * Combines with alignment by vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT). 
     * Attention: this property is required.
     * @param alignment Alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setAlignment(ItemAlignment... alignment) {
        this.alignment = Arrays.asList(alignment);
    }

    /**
     * Alignment of an item's text. 
     * Combines with alignment by vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT). 
     * Can be used only if the item contains text and in this case this property is required.
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        this.textAlignment = Arrays.asList(alignment);
    }

    ////////////////////////////////////////////////////////////////

    /**
     * Add inner primitives to the object (as decorations only). 
     * Note: not supported in the current version!
     * 
     * @param shape Shape as com.spvessel.spacevil.Core.InterfaceBaseItem.
     */
    public void addInnerShape(InterfaceBaseItem shape) {
        if (innerShapes == null) {
            innerShapes = new LinkedList<>();
        }
        innerShapes.add(shape);
    }

    /**
     * Assigning a style for an item's child by key name.
     * @param keyName Key name of a child.
     * @param style Style as com.spvessel.spacevil.Decorations.Style.
     */
    public void addInnerStyle(String keyName, Style style) {
        if (_innerStyles.containsKey(keyName)) {
            _innerStyles.replace(keyName, style);
        } else {
            _innerStyles.put(keyName, style);
        }
    }

    /**
     * Getting a child’s style by key name.
     * @param keyName Key name of a child.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public Style getInnerStyle(String keyName) {
        if (_innerStyles.containsKey(keyName)) {
            return _innerStyles.get(keyName);
        }

        return null;
    }

    /**
     * Removing a child's style by its key name.
     * @param keyName Key name of a child.
     */
    public void removeInnerStyle(String keyName) {
        if (_innerStyles.containsKey(keyName)) {
            _innerStyles.remove(keyName);
        }
    }

    /**
     * Adding visual state for an item. 
     * <p> Type can be BASE, HOVERED, PRESSED, TOGGLED, FOCUSED, DISABLED.
     * @param type Type as com.spvessel.spacevil.Flags.ItemStateType.
     * @param state Visual state as com.spvessel.spacevil.Decorations.ItemState.
     */
    public void addItemState(ItemStateType type, ItemState state) {
        if (_itemStates.containsKey(type)) {
            state.value = true;
            _itemStates.replace(type, state);
        } else {
            _itemStates.put(type, state);
        }
    }

    /**
     * Getting visual state of an item by type. 
     * <p> Type can be BASE, HOVERED, PRESSED, TOGGLED, FOCUSED, DISABLED.
     * @param type Type as com.spvessel.spacevil.Flags.ItemStateType.
     * @return Visual state as com.spvessel.spacevil.Decorations.ItemState.
     */
    public ItemState getState(ItemStateType type) {
        if (_itemStates.containsKey(type)) {
            return _itemStates.get(type);
        }
        return null;
    }

    /**
     * Getting all presented in the current style visual states of an item.
     * @return Map of an ItemStateTypes and its ItemStates.
     */
    public Map<ItemStateType, ItemState> getAllStates() {
        return _itemStates;
    }

    /**
     * Removing visual state of an item by type.
     * <p> Type can be BASE, HOVERED, PRESSED, TOGGLED, FOCUSED, DISABLED.
     * @param type Type as com.spvessel.spacevil.Flags.ItemStateType.
     */
    public void removeItemState(ItemStateType type) {
        if (type == ItemStateType.BASE) {
            return;
        }
        if (_itemStates.containsKey(type)) {
            _itemStates.remove(type);
        }
    }

    /**
     * Cloning the current style and returning a new deep copy of Style.
     * @return Deep copy of current style as com.spvessel.spacevil.Decorations.Style.
     */
    public Style clone() {
        Style style = new Style();

        if (background != null) {
            style.background = new Color(background.getRed(), background.getGreen(), background.getBlue(),
                    background.getAlpha());
        }
        if (foreground != null) {
            style.foreground = new Color(foreground.getRed(), foreground.getGreen(), foreground.getBlue(),
                    foreground.getAlpha());
        }
        if (font != null) {
            style.font = font.deriveFont(font.getStyle());
        } else {
            style.font = DefaultsService.getDefaultFont();
        }

        style.setSizePolicy(widthPolicy, heightPolicy);
        style.setSize(width, height);
        style.setMaxSize(maxWidth, maxHeight);
        style.setMinSize(minWidth, minHeight);

        if (alignment != null) {
            ItemAlignment[] list = new ItemAlignment[alignment.size()];
            alignment.toArray(list);
            style.setAlignment(list);
        }

        if (textAlignment != null) {
            ItemAlignment[] textlist = new ItemAlignment[textAlignment.size()];
            textAlignment.toArray(textlist);
            style.setTextAlignment(textlist);
        }

        if (padding != null) {
            style.setPadding(padding.left, padding.top, padding.right, padding.bottom);
        }

        if (margin != null) {
            style.setMargin(margin.left, margin.top, margin.right, margin.bottom);
        }

        if (spacing != null) {
            style.setSpacing(spacing.horizontal, spacing.vertical);
        }

        if (borderFill != null) {
            style.borderFill = new Color(borderFill.getRed(), borderFill.getGreen(), borderFill.getBlue(),
                    borderFill.getAlpha());
        }

        style.borderThickness = borderThickness;

        if (borderRadius != null) {
            style.borderRadius = new CornerRadius(borderRadius.leftTop, borderRadius.rightTop, borderRadius.leftBottom,
                    borderRadius.rightBottom);
        }

        if (shadowColor != null) {
            style.shadowColor = new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(),
                    shadowColor.getAlpha());
        }

        style.shadowRadius = shadowRadius;
        style.shadowXOffset = shadowXOffset;
        style.shadowYOffset = shadowYOffset;
        style.isShadowDrop = isShadowDrop;

        if (shape != null) {
            style.shape = new LinkedList<>(shape);
        }

        if (innerShapes != null) {
            style.innerShapes = new LinkedList<>(innerShapes);
        }

        style.isFixedShape = isFixedShape;
        style.isVisible = isVisible;
        style._itemStates = new HashMap<>(_itemStates);

        return style;
    }

    /**
     * Getting a default common style. Properly filled in all the necessary properties.
     * <p> Use this method to create instance of Style class instead of  using pure constructor (new Style()).
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getDefaultCommonStyle() {
        Style style = new Style();

        style.background = Color.white;
        style.foreground = Color.black;
        style.font = DefaultsService.getDefaultFont();
        style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        style.setSize(30, 30);
        style.setAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
        style.setTextAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
        style.setPadding(0, 0, 0, 0);
        style.setMargin(0, 0, 0, 0);
        style.setSpacing(0, 0);
        style.setBorder(new Border(new Color(0, 0, 0, 0), new CornerRadius(), 0));

        return style;
    }

    // get default styles
    /**
     * Getting default style for a ButtonCore item. Properly filled in all the necessary properties.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getButtonCoreStyle() {
        Style style = new Style();

        style.background = new Color(13, 176, 255);
        style.foreground = new Color(32, 32, 32);
        style.font = DefaultsService.getDefaultFont(16);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.width = 30;
        style.height = 30;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        style.borderRadius = new CornerRadius();

        // style.borderThickness = 2;
        // style.borderFill = new Color(255, 255, 255);

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 60);
        style.addItemState(ItemStateType.HOVERED, hovered);

        ItemState pressed = new ItemState();
        pressed.background = new Color(0, 0, 0, 60);
        style.addItemState(ItemStateType.PRESSED, pressed);

        style.padding = new Indents();
        style.margin = new Indents();
        style.spacing = new Spacing();

        return style;
    }

    /**
     * Getting default style for a ButtonToggle item. Properly filled in all the necessary properties.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getButtonToggleStyle() {
        Style style = new Style();
        style.background = new Color(13, 176, 255);
        style.foreground = new Color(32, 32, 32);
        style.font = DefaultsService.getDefaultFont(16);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.width = 10;
        style.height = 10;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        style.borderRadius = new CornerRadius();

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 60);
        style.addItemState(ItemStateType.HOVERED, hovered);

        ItemState pressed = new ItemState();
        pressed.background = new Color(30, 0, 0, 60);
        style.addItemState(ItemStateType.PRESSED, pressed);

        ItemState toggled = new ItemState();
        toggled.background = new Color(121, 223, 152);
        style.addItemState(ItemStateType.TOGGLED, toggled);

        return style;
    }

    /**
     * Getting default style for a CheckBox item. Properly filled in all the necessary properties.
     * <p> Inner styles: "indicator", "text".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getCheckBoxStyle() {
        Style style = new Style();

        style.background = new Color(80, 80, 80, 255);
        style.foreground = new Color(210, 210, 210);
        style.font = DefaultsService.getDefaultFont(12);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.width = 10;
        style.height = 20;
        style.minHeight = 20;
        style.minWidth = 20;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));

        Style indicatorStyle = getIndicatorStyle();
        style.addInnerStyle("indicator", indicatorStyle);

        Style textlineStyle = getLabelStyle();
        textlineStyle.foreground = new Color(210, 210, 210);
        textlineStyle.widthPolicy = SizePolicy.EXPAND;
        textlineStyle.heightPolicy = SizePolicy.EXPAND;
        textlineStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER));
        textlineStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        textlineStyle.margin = new Indents(10 + indicatorStyle.width, 0, 0, 0);
        style.addInnerStyle("text", textlineStyle);

        return style;
    }

    /**
     * Getting default style for a Indicator item. Properly filled in all the necessary properties.
     * <p> Inner styles: "marker".
     * <p> This is part of CheckBox item style.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getIndicatorStyle() {
        Style style = new Style();

        style.background = new Color(32, 32, 32);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.width = 20;
        style.height = 20;
        style.minHeight = 20;
        style.minWidth = 20;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.padding = new Indents(4, 4, 4, 4);

        Style markerStyle = new Style();
        markerStyle.background = new Color(32, 32, 32);
        markerStyle.foreground = new Color(70, 70, 70);
        markerStyle.font = DefaultsService.getDefaultFont();
        markerStyle.widthPolicy = SizePolicy.EXPAND;
        markerStyle.heightPolicy = SizePolicy.EXPAND;
        markerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        markerStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 60);
        markerStyle.addItemState(ItemStateType.HOVERED, hovered);

        ItemState toggled = new ItemState();
        toggled.background = new Color(255, 181, 111);
        markerStyle.addItemState(ItemStateType.TOGGLED, toggled);

        style.addInnerStyle("marker", markerStyle);

        return style;
    }

    /**
     * Getting default style for a text type item. Attention: not all the necessary properties properly filled.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTextLineStyle() {
        Style style = new Style();

        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.margin = new Indents(4, 4, 4, 4);

        return style;
    }

    /**
     * Getting default style for a ComboBox item. Properly filled in all the necessary properties.
     * <p> Inner styles: "selection", "dropdownbutton", "dropdownarea", "arrow".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getComboBoxStyle() {
        Style style = new Style();
        style.background = new Color(220, 220, 220);
        style.foreground = new Color(70, 70, 70);
        style.font = DefaultsService.getDefaultFont();
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.width = 10;
        style.height = 30;
        style.minHeight = 10;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));

        Style selectionStyle = new Style();
        selectionStyle.background = new Color(0, 0, 0, 0);
        selectionStyle.foreground = new Color(70, 70, 70);

        selectionStyle.font = DefaultsService.getDefaultFont(14);
        selectionStyle.widthPolicy = SizePolicy.EXPAND;
        selectionStyle.heightPolicy = SizePolicy.EXPAND;
        selectionStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        selectionStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        selectionStyle.padding = new Indents(10, 0, 0, 0);
        selectionStyle.margin = new Indents(0, 0, 20, 0);
        style.addInnerStyle("selection", selectionStyle);

        Style dropdownbuttonStyle = getButtonCoreStyle();
        dropdownbuttonStyle.borderRadius = new CornerRadius();
        dropdownbuttonStyle.width = 20;
        dropdownbuttonStyle.widthPolicy = SizePolicy.FIXED;
        dropdownbuttonStyle.heightPolicy = SizePolicy.EXPAND;
        dropdownbuttonStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.VCENTER));
        dropdownbuttonStyle.background = new Color(255, 181, 111);

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 40);
        dropdownbuttonStyle.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("dropdownbutton", dropdownbuttonStyle);

        style.addInnerStyle("dropdownarea", getComboBoxDropDownStyle());

        Style arrowStyle = new Style();
        arrowStyle.width = 14;
        arrowStyle.height = 6;
        arrowStyle.widthPolicy = SizePolicy.FIXED;
        arrowStyle.heightPolicy = SizePolicy.FIXED;
        arrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        arrowStyle.background = new Color(50, 50, 50);
        arrowStyle.shape = GraphicsMathService.getTriangle(100, 100, 0, 0, 180);
        style.addInnerStyle("arrow", arrowStyle);

        return style;
    }

    /**
     * Getting default style for a ComboBoxDropDown item. Properly filled in all the necessary properties.
     * <p> Inner styles: "itemlist".
     * <p> Inner styles for "itemlist": "vscrollbar", "hscrollbar", "menu".
     * <p> This is part of ComboBox item style.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getComboBoxDropDownStyle() {
        Style style = new Style();
        style.background = Color.white;
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.padding = new Indents(0, 0, 0, 0);
        style.isVisible = false;

        Style itemlistStyle = getListBoxStyle();
        itemlistStyle.background = new Color(0, 0, 0, 0);
        itemlistStyle.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.addInnerStyle("itemlist", itemlistStyle);

        Style itemlistareaStyle = itemlistStyle.getInnerStyle("area");
        if (itemlistareaStyle != null) {
            itemlistStyle.setPadding(0, 0, 0, 0);
        }

        Style vsbStyle = getSimpleVerticalScrollBarStyle();
        vsbStyle.setAlignment(ItemAlignment.RIGHT, ItemAlignment.TOP);
        itemlistStyle.addInnerStyle("vscrollbar", vsbStyle);

        Style hsbStyle = getHorizontalScrollBarStyle();
        hsbStyle.setAlignment(ItemAlignment.LEFT, ItemAlignment.BOTTOM);
        itemlistStyle.addInnerStyle("hscrollbar", hsbStyle);

        Style menuStyle = new Style();
        menuStyle.background = new Color(50, 50, 50);
        menuStyle.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        menuStyle.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
        itemlistStyle.addInnerStyle("menu", menuStyle);

        return style;
    }

    /**
     * Getting default style for a MenuItem item. Properly filled in all the necessary properties.
     * <p> Inner styles: "text", "arrow".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getMenuItemStyle() {
        Style style = new Style();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = new Color(70, 70, 70);
        style.font = DefaultsService.getDefaultFont();
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.height = 25;
        style.minHeight = 10;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.padding = new Indents(10, 0, 10, 0);
        style.addItemState(ItemStateType.HOVERED, new ItemState(new Color(200, 200, 200)));

        Style textStyle = new Style();
        textStyle.setMargin(0, 0, 0, 0);
        style.addInnerStyle("text", textStyle);

        Style arrowStyle = new Style();
        arrowStyle.width = 6;
        arrowStyle.height = 10;
        arrowStyle.widthPolicy = SizePolicy.FIXED;
        arrowStyle.heightPolicy = SizePolicy.FIXED;
        arrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.VCENTER));
        arrowStyle.background = new Color(80, 80, 80);
        arrowStyle.margin = new Indents(10, 0, 0, 0);
        arrowStyle.shape = GraphicsMathService.getTriangle(100, 100, 0, 0, 90);
        style.addInnerStyle("arrow", arrowStyle);

        return style;
    }

    /**
     * Getting default style for a ContextMenu item. Properly filled in all the necessary properties.
     * <p> Inner styles: "itemlist".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getContextMenuStyle() {
        Style style = getDefaultCommonStyle();
        style.background = new Color(210, 210, 210);
        style.isVisible = false;

        Style itemlistStyle = getListBoxStyle();
        itemlistStyle.background = new Color(0, 0, 0, 0);
        itemlistStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        style.addInnerStyle("itemlist", itemlistStyle);

        Style areaStyle = itemlistStyle.getInnerStyle("area");
        areaStyle.setPadding(0, 0, 0, 0);

        return style;
    }

    /**
     * Getting default style for a FreeArea item. Properly filled in all the necessary properties.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getFreeAreaStyle() {
        Style style = new Style();

        // style.background = new Color(0,0,0,0);
        style.background = new Color(70, 70, 70, 255);

        style.padding = new Indents(2, 2, 2, 2);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        return style;
    }

    /**
     * Getting default style for a Frame item. Properly filled in all the necessary properties.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getFrameStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.padding = new Indents(2, 2, 2, 2);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        return style;
    }

    /**
     * Getting default style for a Grid item. Properly filled in all the necessary properties.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getGridStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        return style;
    }

    /**
     * Getting default style for a HorizontalScrollBar item. Properly filled in all the necessary properties.
     * <p> Inner styles: "uparrow", "downarrow", "slider".
     * <p> Inner styles for "slider": "track", "handler".
     * <p> This is part of many items style.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getHorizontalScrollBarStyle() {
        Style style = new Style();

        style.background = new Color(50, 50, 50);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.height = 16;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));

        Style uparrowStyle = getButtonCoreStyle();
        uparrowStyle.widthPolicy = SizePolicy.FIXED;
        uparrowStyle.heightPolicy = SizePolicy.FIXED;
        uparrowStyle.background = new Color(100, 100, 100, 255);
        uparrowStyle.width = 16;
        uparrowStyle.height = 16;
        uparrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        uparrowStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        uparrowStyle.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, -90);
        uparrowStyle.isFixedShape = true;

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 40);
        uparrowStyle.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("uparrow", uparrowStyle);

        Style downarrowStyle = getButtonCoreStyle();
        downarrowStyle.widthPolicy = SizePolicy.FIXED;
        downarrowStyle.heightPolicy = SizePolicy.FIXED;
        downarrowStyle.background = new Color(100, 100, 100, 255);
        downarrowStyle.width = 16;
        downarrowStyle.height = 16;
        downarrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.VCENTER));
        downarrowStyle.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, 90);
        downarrowStyle.isFixedShape = true;
        downarrowStyle.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("downarrow", downarrowStyle);

        Style sliderStyle = new Style();
        sliderStyle.widthPolicy = SizePolicy.EXPAND;
        sliderStyle.heightPolicy = SizePolicy.EXPAND;
        sliderStyle.background = new Color(0, 0, 0, 0);
        sliderStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        style.addInnerStyle("slider", sliderStyle);

        Style trackStyle = new Style();
        trackStyle.widthPolicy = SizePolicy.EXPAND;
        trackStyle.heightPolicy = SizePolicy.EXPAND;
        trackStyle.background = new Color(0, 0, 0, 0);
        trackStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        sliderStyle.addInnerStyle("track", trackStyle);

        Style handlerStyle = new Style();
        handlerStyle.widthPolicy = SizePolicy.FIXED;
        handlerStyle.heightPolicy = SizePolicy.EXPAND;
        handlerStyle.background = new Color(100, 100, 100, 255);
        handlerStyle.margin = new Indents(0, 3, 0, 3);
        handlerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        handlerStyle.addItemState(ItemStateType.HOVERED, hovered);
        handlerStyle.minWidth = 15;
        sliderStyle.addInnerStyle("handler", handlerStyle);

        return style;
    }

    /**
     * Getting simplified style for a SimpleHorizontalScrollBar item. Properly filled in all the necessary properties.
     * <p> Inner styles: "uparrow", "downarrow", "slider".
     * <p> Inner styles for "slider": "track", "handler".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getSimpleHorizontalScrollBarStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.padding = new Indents(2, 0, 2, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.height = 16;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));

        Style uparrowStyle = getButtonCoreStyle();
        uparrowStyle.isVisible = false;
        style.addInnerStyle("uparrow", uparrowStyle);

        Style downarrowStyle = getButtonCoreStyle();
        downarrowStyle.isVisible = false;
        style.addInnerStyle("downarrow", downarrowStyle);

        Style sliderStyle = new Style();
        sliderStyle.widthPolicy = SizePolicy.EXPAND;
        sliderStyle.heightPolicy = SizePolicy.EXPAND;
        sliderStyle.background = new Color(0, 0, 0, 0);
        sliderStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        style.addInnerStyle("slider", sliderStyle);

        Style trackStyle = new Style();
        trackStyle.widthPolicy = SizePolicy.EXPAND;
        trackStyle.heightPolicy = SizePolicy.EXPAND;
        trackStyle.background = new Color(0, 0, 0, 0);
        trackStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        sliderStyle.addInnerStyle("track", trackStyle);

        Style handlerStyle = new Style();
        handlerStyle.widthPolicy = SizePolicy.FIXED;
        handlerStyle.heightPolicy = SizePolicy.EXPAND;
        handlerStyle.background = new Color(120, 120, 120, 255);
        handlerStyle.margin = new Indents(0, 5, 0, 5);
        handlerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        handlerStyle.borderRadius = new CornerRadius(3);
        handlerStyle.minWidth = 15;
        sliderStyle.addInnerStyle("handler", handlerStyle);

        return style;
    }

    /**
     * Getting default style for a VerticalScrollBar item. Properly filled in all the necessary properties.
     * <p> Inner styles: "uparrow", "downarrow", "slider".
     * <p> Inner styles for "slider": "track", "handler".
     * <p> This is part of many items style.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getVerticalScrollBarStyle() {
        Style style = new Style();

        style.background = new Color(50, 50, 50);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.EXPAND;
        style.width = 16;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));

        Style uparrowStyle = getButtonCoreStyle();
        uparrowStyle.widthPolicy = SizePolicy.FIXED;
        uparrowStyle.heightPolicy = SizePolicy.FIXED;
        uparrowStyle.background = new Color(100, 100, 100, 255);
        uparrowStyle.width = 16;
        uparrowStyle.height = 16;
        uparrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        uparrowStyle.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, 0);
        uparrowStyle.isFixedShape = true;

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 40);
        uparrowStyle.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("uparrow", uparrowStyle);

        Style downarrowStyle = getButtonCoreStyle();
        downarrowStyle.widthPolicy = SizePolicy.FIXED;
        downarrowStyle.heightPolicy = SizePolicy.FIXED;
        downarrowStyle.background = new Color(100, 100, 100, 255);
        downarrowStyle.width = 16;
        downarrowStyle.height = 16;
        downarrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.HCENTER));
        downarrowStyle.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, 180);
        downarrowStyle.isFixedShape = true;
        downarrowStyle.addItemState(ItemStateType.HOVERED, hovered);
        style.addInnerStyle("downarrow", downarrowStyle);

        Style sliderStyle = new Style();
        sliderStyle.widthPolicy = SizePolicy.EXPAND;
        sliderStyle.heightPolicy = SizePolicy.EXPAND;
        sliderStyle.background = new Color(0, 0, 0, 0);
        sliderStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        style.addInnerStyle("slider", sliderStyle);

        Style trackStyle = new Style();
        trackStyle.widthPolicy = SizePolicy.EXPAND;
        trackStyle.heightPolicy = SizePolicy.EXPAND;
        trackStyle.background = new Color(0, 0, 0, 0);
        trackStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        sliderStyle.addInnerStyle("track", trackStyle);

        Style handlerStyle = new Style();
        handlerStyle.widthPolicy = SizePolicy.EXPAND;
        handlerStyle.heightPolicy = SizePolicy.FIXED;
        handlerStyle.background = new Color(100, 100, 100, 255);
        handlerStyle.margin = new Indents(3, 0, 3, 0);
        handlerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        handlerStyle.minHeight = 15;
        handlerStyle.addItemState(ItemStateType.HOVERED, hovered);
        sliderStyle.addInnerStyle("handler", handlerStyle);

        return style;
    }

    /**
     * Getting simplified style for a SimpleVerticalScrollBar item. Properly filled in all the necessary properties.
     * <p> Inner styles: "uparrow", "downarrow", "slider".
     * <p> Inner styles for "slider": "track", "handler".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getSimpleVerticalScrollBarStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.padding = new Indents(0, 2, 0, 2);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.EXPAND;
        style.width = 16;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));

        Style uparrowStyle = getButtonCoreStyle();
        uparrowStyle.isVisible = false;
        style.addInnerStyle("uparrow", uparrowStyle);

        Style downarrowStyle = getButtonCoreStyle();
        downarrowStyle.isVisible = false;
        style.addInnerStyle("downarrow", downarrowStyle);

        Style sliderStyle = new Style();
        sliderStyle.widthPolicy = SizePolicy.EXPAND;
        sliderStyle.heightPolicy = SizePolicy.EXPAND;
        sliderStyle.background = new Color(0, 0, 0, 0);
        sliderStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        style.addInnerStyle("slider", sliderStyle);

        Style trackStyle = new Style();
        trackStyle.widthPolicy = SizePolicy.EXPAND;
        trackStyle.heightPolicy = SizePolicy.EXPAND;
        trackStyle.background = new Color(0, 0, 0, 0);
        trackStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        sliderStyle.addInnerStyle("track", trackStyle);

        Style handlerStyle = new Style();
        handlerStyle.widthPolicy = SizePolicy.EXPAND;
        handlerStyle.heightPolicy = SizePolicy.FIXED;
        handlerStyle.background = new Color(120, 120, 120, 255);
        handlerStyle.margin = new Indents(5, 0, 5, 0);
        handlerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        handlerStyle.borderRadius = new CornerRadius(3);
        handlerStyle.minHeight = 15;
        sliderStyle.addInnerStyle("handler", handlerStyle);

        return style;
    }

    /**
     * Getting default style for a HorizontalSlider item. Properly filled in all the necessary properties.
     * <p> Inner styles: "track", "handler".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getHorizontalSliderStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        style.height = 25;

        Style trackStyle = new Style();
        trackStyle.widthPolicy = SizePolicy.EXPAND;
        trackStyle.heightPolicy = SizePolicy.FIXED;
        trackStyle.height = 5;
        trackStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER));
        trackStyle.background = new Color(100, 100, 100);
        style.addInnerStyle("track", trackStyle);

        Style handlerStyle = new Style();
        handlerStyle.widthPolicy = SizePolicy.FIXED;
        handlerStyle.heightPolicy = SizePolicy.EXPAND;
        handlerStyle.width = 10;
        handlerStyle.background = new Color(255, 181, 111);
        handlerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT));

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        handlerStyle.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("handler", handlerStyle);

        return style;
    }

    /**
     * Getting default style for a VerticalSlider item. Properly filled in all the necessary properties.
     * <p> Inner styles: "track", "handler".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getVerticalSliderStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        style.width = 25;

        Style trackStyle = new Style();
        trackStyle.widthPolicy = SizePolicy.FIXED;
        trackStyle.heightPolicy = SizePolicy.EXPAND;
        trackStyle.width = 5;
        trackStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER));
        trackStyle.background = new Color(100, 100, 100);
        style.addInnerStyle("track", trackStyle);

        Style handlerStyle = new Style();
        handlerStyle.widthPolicy = SizePolicy.EXPAND;
        handlerStyle.heightPolicy = SizePolicy.FIXED;
        handlerStyle.height = 10;
        handlerStyle.background = new Color(255, 181, 111);
        handlerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP));
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        handlerStyle.addItemState(ItemStateType.HOVERED, hovered);
        style.addInnerStyle("handler", handlerStyle);

        return style;
    }

    /**
     * Getting default style for a HorizontalStack item. Properly filled in all the necessary properties.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getHorizontalStackStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.spacing = new Spacing(0, 0);
        style.setPadding(new Indents());
        style.setMargin(new Indents());

        return style;
    }

    /**
     * Getting default style for a VerticalStack item. Properly filled in all the necessary properties.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getVerticalStackStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.spacing = new Spacing(0, 0);

        return style;
    }

    /**
     * Getting default style for a HorizontalSplitArea item. Properly filled in all the necessary properties.
     * <p> Inner styles: "splitholder".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getHorizontalSplitAreaStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style splitterStyle = new Style();
        splitterStyle.background = new Color(42, 42, 42);
        splitterStyle.width = 6;
        style.addInnerStyle("splitholder", splitterStyle);

        return style;
    }

    /**
     * Getting default style for a VerticalSplitArea item. Properly filled in all the necessary properties.
     * <p> Inner styles: "splitholder".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getVerticalSplitAreaStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style splitterStyle = new Style();
        splitterStyle.background = new Color(42, 42, 42);
        splitterStyle.height = 6;
        style.addInnerStyle("splitholder", splitterStyle);

        return style;
    }

    /**
     * Getting default style for a Label item. Properly filled in all the necessary properties.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getLabelStyle() {
        Style style = new Style();

        style.font = DefaultsService.getDefaultFont();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = new Color(210, 210, 210);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));

        return style;
    }

    /**
     * Getting default style for a ListArea item. Properly filled in all the necessary properties.
     * <p> Inner styles: "selection".
     * <p> This is part of many items style.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getListAreaStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.padding = new Indents(2, 2, 2, 2);
        style.spacing = new Spacing(0, 4);

        Style selectionStyle = getSelectionItemStyle();
        style.addInnerStyle("selection", selectionStyle);

        return style;
    }

    /**
     * Getting default style for a ListBox item. Properly filled in all the necessary properties.
     * <p> Inner styles: "area", "vscrollbar", "hscrollbar", "menu".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getListBoxStyle() {
        Style style = new Style();

        style.background = new Color(70, 70, 70);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style vsbStyle = getVerticalScrollBarStyle();
        vsbStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.TOP));
        style.addInnerStyle("vscrollbar", vsbStyle);

        Style hsbStyle = getHorizontalScrollBarStyle();
        hsbStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.BOTTOM));
        style.addInnerStyle("hscrollbar", hsbStyle);

        Style menuStyle = new Style();
        menuStyle.background = new Color(50, 50, 50);
        menuStyle.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        menuStyle.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
        style.addInnerStyle("menu", menuStyle);

        Style areaStyle = getListAreaStyle();
        style.addInnerStyle("area", areaStyle);

        return style;
    }

    /**
     * Note: not supported in current version.
     * @return default style for WContainer objects.
     */
    public static Style getWContainerStyle()// нужен ли?
    {
        Style style = new Style();
        return style;
    }

    /**
     * Getting default style for a RadioButton item. Properly filled in all the necessary properties.
     * <p> Inner styles: "indicator", "text".
     * <p> Inner styles of "indicator": "marker".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getRadioButtonStyle() {
        Style style = new Style();

        style.background = new Color(80, 80, 80, 255);
        style.foreground = new Color(210, 210, 210);
        style.font = DefaultsService.getDefaultFont();
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.height = 20;
        style.minHeight = 20;
        style.minWidth = 20;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.borderRadius = new CornerRadius(10);

        Style indicatorStyle = getIndicatorStyle();
        indicatorStyle.shape = GraphicsMathService.getRoundSquare(20, 20, 10, 0, 0);
        indicatorStyle.isFixedShape = true;
        style.addInnerStyle("indicator", indicatorStyle);

        Style markerStyle = indicatorStyle.getInnerStyle("marker");
        markerStyle.shape = GraphicsMathService.getEllipse(100, 16);
        indicatorStyle.addInnerStyle("marker", markerStyle);

        Style textlineStyle = getLabelStyle();
        textlineStyle.foreground = new Color(210, 210, 210);
        textlineStyle.widthPolicy = SizePolicy.EXPAND;
        textlineStyle.heightPolicy = SizePolicy.EXPAND;
        textlineStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER));
        textlineStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        textlineStyle.margin = new Indents(10 + indicatorStyle.width, 0, 0, 0);
        style.addInnerStyle("text", textlineStyle);

        return style;
    }

    /**
     * Getting default style for a PasswordLine item. Properly filled in all the necessary properties.
     * <p> Inner styles: "showmarker", "textedit".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getPasswordLineStyle() {
        Style style = new Style();
        style.background = new Color(210, 210, 210);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.height = 30;
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.padding = new Indents(5, 0, 5, 0);
        style.spacing = new Spacing(5, 0);

        // ItemState hovered = new ItemState();
        // hovered.background = new Color(255, 255, 255, 30);
        // style.addItemState(ItemStateType.HOVERED, hovered);

        Style markerStyle = getIndicatorStyle().getInnerStyle("marker");
        markerStyle.background = new Color(100, 100, 100, 0);
        markerStyle.setSize(20, 20);
        markerStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        markerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.RIGHT));
        markerStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        // marker_style.borderRadius = new CornerRadius(5);
        markerStyle.removeItemState(ItemStateType.HOVERED);
        // ItemState toggled = new ItemState();
        // toggled.background = new Color(40, 40, 40, 255);
        // marker_style.addItemState(ItemStateType.TOGGLED, toggled);
        // marker_style.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255,
        // 255, 255, 50)));
        style.addInnerStyle("showmarker", markerStyle);
        style.addInnerStyle("textedit", getTextEncryptStyle());

        return style;
    }

    private static Style getTextEncryptStyle() {
        Style style = new Style();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = new Color(25, 25, 25);
        style.font = DefaultsService.getDefaultFont(16);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;

        Style cursorStyle = new Style();
        cursorStyle.background = new Color(60, 60, 60);
        cursorStyle.width = 2;
        cursorStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        cursorStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        cursorStyle.margin = new Indents(0, 5, 0, 5);
        cursorStyle.isVisible = false;
        style.addInnerStyle("cursor", cursorStyle);

        Style selectionStyle = new Style();
        selectionStyle.background = new Color(111, 181, 255);
        selectionStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        selectionStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        style.addInnerStyle("selection", selectionStyle);

        Style substrateStyle = new Style();
        substrateStyle.font = DefaultsService.getDefaultFont(Font.ITALIC, 14);
        substrateStyle.foreground = new Color(150, 150, 150);
        style.addInnerStyle("substrate", substrateStyle);

        return style;
    }

    /**
     * Getting default style for a TextEdit item. Properly filled in all the necessary properties.
     * <p> Inner styles: "text".
     * <p> Inner styles for "text": "cursor", "selection", "substrate".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTextEditStyle() {
        Style style = new Style();
        style.font = DefaultsService.getDefaultFont(16);
        style.background = new Color(210, 210, 210);
        style.height = 30;
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;

        Style textStyle = new Style();
        textStyle.background = new Color(0, 0, 0, 0);
        textStyle.foreground = new Color(70, 70, 70);
        textStyle.font = DefaultsService.getDefaultFont(16);
        textStyle.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        textStyle.setAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
        textStyle.setTextAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        textStyle.padding = new Indents(5, 0, 5, 0);
        style.addInnerStyle("text", textStyle);

        Style cursorStyle = new Style();
        cursorStyle.background = new Color(60, 60, 60);
        cursorStyle.width = 2;
        cursorStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        cursorStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        cursorStyle.margin = new Indents(0, 5, 0, 5);
        cursorStyle.isVisible = false;
        textStyle.addInnerStyle("cursor", cursorStyle);

        Style selectionStyle = new Style();
        selectionStyle.background = new Color(111, 181, 255);
        selectionStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        selectionStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        selectionStyle.margin = new Indents(0, 5, 0, 5);
        textStyle.addInnerStyle("selection", selectionStyle);

        Style substrateStyle = new Style();
        substrateStyle.font = DefaultsService.getDefaultFont(Font.ITALIC, 14);
        substrateStyle.foreground = new Color(150, 150, 150);
        textStyle.addInnerStyle("substrate", substrateStyle);

        return style;
    }

    /**
     * Getting default style for a sealed TextBlock item. Properly filled in all the necessary properties.
     * <p> Inner styles: "cursor", "selection".
     * <p> This is part of TextArea item style as "textedit".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTextBlockStyle() {
        Style style = new Style();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = new Color(70, 70, 70);
        style.font = DefaultsService.getDefaultFont(16);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.padding = new Indents(5, 5, 5, 5);

        Style cursorStyle = new Style();
        cursorStyle.background = new Color(60, 60, 60);
        cursorStyle.width = 2;
        cursorStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        cursorStyle.isVisible = false;
        style.addInnerStyle("cursor", cursorStyle);

        Style selectionStyle = new Style();
        selectionStyle.background = new Color(111, 181, 255);
        selectionStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        selectionStyle.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        style.addInnerStyle("selection", selectionStyle);

        return style;
    }

    /**
     * Getting default style for a TextArea item. Properly filled in all the necessary properties.
     * <p> Inner styles: "textedit", "vscrollbar", "hscrollbar", "menu".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTextAreaStyle() {
        Style style = new Style();
        style.background = new Color(210, 210, 210);
        style.foreground = new Color(70, 70, 70);
        style.font = DefaultsService.getDefaultFont(16);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style textStyle = getTextBlockStyle();
        style.addInnerStyle("textedit", textStyle);

        Style vsbStyle = getVerticalScrollBarStyle();
        vsbStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.TOP));
        style.addInnerStyle("vscrollbar", vsbStyle);

        Style hsbStyle = getHorizontalScrollBarStyle();
        hsbStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.BOTTOM));
        style.addInnerStyle("hscrollbar", hsbStyle);

        Style menuStyle = new Style();
        menuStyle.background = new Color(50, 50, 50);
        menuStyle.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        menuStyle.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
        style.addInnerStyle("menu", menuStyle);

        return style;
    }

    /**
     * Getting default style for a TextView item. Properly filled in all the necessary properties.
     * <p> Inner styles: "selection".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTextViewStyle() {
        Style style = new Style();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = new Color(210, 210, 210);
        style.font = DefaultsService.getDefaultFont(16);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.setAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
        style.setPadding(5, 5, 5, 5);

        Style selectionStyle = new Style();
        selectionStyle.background = new Color(255, 255, 255, 40);
        selectionStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        style.addInnerStyle("selection", selectionStyle);

        return style;
    }

    /**
     * Getting default style for a PopUpMessage item. Properly filled in all the necessary properties.
     * <p> Inner styles: "closebutton".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getPopUpMessageStyle() {
        Style style = new Style();
        style.background = new Color(45, 45, 45, 255);
        style.foreground = new Color(210, 210, 210);
        style.font = DefaultsService.getDefaultFont(14);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.RIGHT));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        style.setSize(300, 70);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.padding = new Indents(5, 5, 5, 5);
        style.margin = new Indents(10, 10, 10, 10);
        style.setShadow(new Shadow(10, 3, 3, new Color(0, 0, 0, 140)));
        style.isShadowDrop = true;
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 3);
        style.addItemState(ItemStateType.HOVERED, hovered);

        Style closeStyle = getButtonCoreStyle();
        closeStyle.background = new Color(100, 100, 100);
        closeStyle.foreground = new Color(210, 210, 210);
        closeStyle.setSize(10, 10);
        closeStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        closeStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.RIGHT));
        closeStyle.margin = new Indents(0, 5, 0, 5);
        ItemState close_hovered = new ItemState();
        close_hovered.background = new Color(255, 255, 255, 60);
        closeStyle.addItemState(ItemStateType.HOVERED, close_hovered);
        closeStyle.shape = GraphicsMathService.getCross(10, 10, 3, 45);
        closeStyle.isFixedShape = false;
        style.addInnerStyle("closebutton", closeStyle);

        return style;
    }

    /**
     * Getting default style for a ProgressBar item. Properly filled in all the necessary properties.
     * <p> Inner styles: "progressbar".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getProgressBarStyle() {
        Style style = new Style();

        style.font = DefaultsService.getDefaultFont(12);
        style.background = new Color(70, 70, 70);
        style.foreground = new Color(0, 0, 0);
        style.height = 20;
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));

        Style pgbarStyle = new Style();
        pgbarStyle.background = new Color(0, 191, 255);
        pgbarStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        pgbarStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        style.addInnerStyle("progressbar", pgbarStyle);

        return style;
    }

    /**
     * Getting default style for a ToolTip item. Properly filled in all the necessary properties.
     * <p> Inner styles: "text".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getToolTipStyle() {
        Style style = new Style();

        style.font = DefaultsService.getDefaultFont();
        style.background = new Color(255, 255, 255);
        style.foreground = new Color(70, 70, 70);

        style.height = 30;
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.padding = new Indents(5, 5, 5, 5);
        style.borderRadius = new CornerRadius(4);

        Style textStyle = new Style();
        textStyle.background = new Color(0, 0, 0, 0);
        textStyle.widthPolicy = SizePolicy.EXPAND;
        textStyle.heightPolicy = SizePolicy.EXPAND;
        textStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.HCENTER));
        textStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.HCENTER));
        style.addInnerStyle("text", textStyle);

        return style;
    }

    /**
     * Getting default style for a TitleBar item. Properly filled in all the necessary properties.
     * <p> Inner styles: "closebutton", "minimizebutton", "maximizebutton", "title".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTitleBarStyle() {
        Style style = new Style();

        style.font = DefaultsService.getDefaultFont();
        style.background = new Color(45, 45, 45);
        style.foreground = new Color(180, 180, 180);
        style.height = 30;
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.padding = new Indents(10, 0, 5, 0);
        style.spacing = new Spacing(5, 0);

        Style closeStyle = new Style();
        closeStyle.font = DefaultsService.getDefaultFont();
        closeStyle.background = new Color(100, 100, 100);
        closeStyle.foreground = new Color(0, 0, 0, 0);
        closeStyle.setSize(15, 15);
        closeStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        closeStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.RIGHT));
        closeStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        ItemState closeHovered = new ItemState();
        closeHovered.background = new Color(186, 95, 97, 255);
        closeStyle.addItemState(ItemStateType.HOVERED, closeHovered);

        closeStyle.shape = GraphicsMathService.getCross(15, 15, 2, 45);
        closeStyle.isFixedShape = true;
        style.addInnerStyle("closebutton", closeStyle);

        Style minimizeStyle = new Style();
        minimizeStyle.font = DefaultsService.getDefaultFont();
        minimizeStyle.background = new Color(100, 100, 100);
        minimizeStyle.foreground = new Color(0, 0, 0, 0);
        minimizeStyle.setSize(12, 15);
        minimizeStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        minimizeStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.RIGHT));
        minimizeStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        minimizeStyle.margin = new Indents(0, 0, 5, 9);

        ItemState minimizeHovered = new ItemState();
        minimizeHovered.background = new Color(255, 255, 255, 80);
        minimizeStyle.addItemState(ItemStateType.HOVERED, minimizeHovered);

        minimizeStyle.shape = GraphicsMathService.getRectangle(15, 2, 0, 13);
        minimizeStyle.isFixedShape = true;
        style.addInnerStyle("minimizebutton", minimizeStyle);

        Style maximizeStyle = new Style();
        maximizeStyle.font = DefaultsService.getDefaultFont();
        maximizeStyle.background = new Color(0, 0, 0, 0);

        maximizeStyle.borderThickness = 2;
        maximizeStyle.borderFill = new Color(100, 100, 100);

        maximizeStyle.foreground = new Color(0, 0, 0, 0);
        maximizeStyle.setSize(12, 12);
        maximizeStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        maximizeStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.RIGHT));
        maximizeStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        maximizeStyle.margin = new Indents(0, 0, 0, 9);
        maximizeStyle.padding = new Indents(0, 0, 0, 0);

        ItemState maximizeHovered = new ItemState();
        maximizeHovered.background = new Color(0, 0, 0, 0);
        maximizeHovered.border.setFill(new Color(84, 124, 94));

        maximizeStyle.addItemState(ItemStateType.HOVERED, maximizeHovered);
        style.addInnerStyle("maximizebutton", maximizeStyle);

        Style titleStyle = new Style();
        titleStyle.margin = new Indents(10, 0, 0, 0);
        style.addInnerStyle("title", titleStyle);

        return style;
    }

    /**
     * Getting default style for a TreeView item. Properly filled in all the necessary properties.
     * <p> Inner styles: "area", "vscrollbar", "hscrollbar", "menu".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTreeViewStyle() {
        Style style = getListBoxStyle();
        return style;
    }

    /**
     * Getting default style for a TreeItem item. Properly filled in all the necessary properties.
     * <p> Inner styles: "indicator", "branchicon", "leaficon".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTreeItemStyle() {
        Style style = new Style();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = new Color(210, 210, 210);
        style.font = DefaultsService.getDefaultFont();
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        style.height = 25;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        style.spacing = new Spacing(5, 0);
        style.padding = new Indents(5, 0, 0, 0);
        style.margin = new Indents(0, 0, 0, 0);
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 30);
        style.addItemState(ItemStateType.HOVERED, hovered);

        Style indicatorStyle = new Style();
        indicatorStyle.background = new Color(32, 32, 32);
        indicatorStyle.foreground = new Color(210, 210, 210);
        indicatorStyle.font = DefaultsService.getDefaultFont();
        indicatorStyle.setSize(15, 15);
        indicatorStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        indicatorStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        indicatorStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        indicatorStyle.shape = GraphicsMathService.getTriangle(10, 8, 0, 3, 90);
        indicatorStyle.isFixedShape = true;
        ItemState toggled = new ItemState();
        toggled.background = new Color(160, 160, 160);
        toggled.shape = new Figure(true, GraphicsMathService.getTriangle(10, 8, 0, 3, 180));
        indicatorStyle.addItemState(ItemStateType.TOGGLED, toggled);
        style.addInnerStyle("indicator", indicatorStyle);

        Style branchIconStyle = new Style();
        branchIconStyle.background = new Color(106, 185, 255);
        branchIconStyle.setSize(14, 9);
        branchIconStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        branchIconStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        branchIconStyle.shape = GraphicsMathService.getFolderIconShape(20, 15, 0, 0);
        style.addInnerStyle("branchicon", branchIconStyle);

        Style leafIconStyle = new Style();
        leafIconStyle.background = new Color(129, 187, 133);
        leafIconStyle.setSize(6, 6);
        leafIconStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        leafIconStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        leafIconStyle.shape = GraphicsMathService.getEllipse(3, 16);
        leafIconStyle.margin = new Indents(2, 0, 0, 0);
        style.addInnerStyle("leaficon", leafIconStyle);

        return style;
    }

    /**
     * Getting default style for a SpinItem item. Properly filled in all the necessary properties.
     * <p> Inner styles: "uparrow", "downarrow", "buttonsarea", "textedit".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getSpinItemStyle() {
        Style style = new Style();

        style.background = new Color(50, 50, 50);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.height = 30;
        style.minHeight = 10;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style uparrowStyle = getButtonCoreStyle();
        uparrowStyle.widthPolicy = SizePolicy.EXPAND;
        uparrowStyle.heightPolicy = SizePolicy.EXPAND;
        uparrowStyle.setMargin(4, 4, 4, 5);
        uparrowStyle.background = new Color(50, 50, 50, 255);
        uparrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        uparrowStyle.shape = GraphicsMathService.getTriangle(12, 6, 0, 0, 0);
        uparrowStyle.isFixedShape = true;

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        uparrowStyle.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("uparrow", uparrowStyle);

        Style downarrowStyle = getButtonCoreStyle();
        downarrowStyle.widthPolicy = SizePolicy.EXPAND;
        downarrowStyle.heightPolicy = SizePolicy.EXPAND;
        downarrowStyle.setMargin(4, 5, 4, 4);
        downarrowStyle.background = new Color(50, 50, 50, 255);
        downarrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.HCENTER));
        downarrowStyle.shape = GraphicsMathService.getTriangle(12, 6, 0, 0, 180);
        downarrowStyle.isFixedShape = true;
        downarrowStyle.addItemState(ItemStateType.HOVERED, hovered);
        style.addInnerStyle("downarrow", downarrowStyle);

        Style btnsArea = getVerticalStackStyle();
        btnsArea.widthPolicy = SizePolicy.FIXED;
        btnsArea.heightPolicy = SizePolicy.EXPAND;
        btnsArea.width = 20;
        btnsArea.background = new Color(255, 181, 111);
        btnsArea.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.RIGHT));
        style.addInnerStyle("buttonsarea", btnsArea);

        Style textInput = getTextEditStyle();
        textInput.heightPolicy = SizePolicy.EXPAND;
        textInput.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT));
        style.addInnerStyle("textedit", textInput);

        return style;
    }

    /**
     * Getting default style for a DialogItem item. Properly filled in all the necessary properties.
     * <p> Inner styles: "window".
     * <p> This is part of OpenEntryDialog item style.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getDialogItemStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        style.setBackground(0, 0, 0, 150);
        style.borderRadius = new CornerRadius(0);
        style.padding = new Indents();
        style.margin = new Indents();
        style.spacing = new Spacing();

        Style windowStyle = getFrameStyle();
        windowStyle.setSize(300, 150);
        windowStyle.setMinSize(300, 150);
        windowStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        windowStyle.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        windowStyle.setPadding(2, 2, 2, 2);
        windowStyle.setBackground(45, 45, 45);
        windowStyle.setShadow(new Shadow(5, 3, 3, new Color(0, 0, 0, 180)));
        windowStyle.isShadowDrop = true;

        style.addInnerStyle("window", windowStyle);

        return style;
    }

    /**
     * Getting default style for a MessageItem item. Properly filled in all the necessary properties.
     * <p> Inner styles: "window", "button", "toolbar", "userbar", "message", "layout".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getMessageItemStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        style.setBackground(0, 0, 0, 150);
        style.borderRadius = new CornerRadius();
        style.padding = new Indents();
        style.margin = new Indents();
        style.spacing = new Spacing();

        Style windowStyle = getFrameStyle();
        windowStyle.setSize(300, 150);
        windowStyle.setMinSize(300, 150);
        windowStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        windowStyle.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        windowStyle.setPadding(2, 2, 2, 2);
        windowStyle.setBackground(45, 45, 45);
        style.addInnerStyle("window", windowStyle);

        Style btnStyle = getButtonCoreStyle();
        btnStyle.setBackground(100, 255, 150);
        btnStyle.setSize(100, 30);
        btnStyle.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        btnStyle.setShadow(new Shadow(5, 2, 2, new Color(0, 0, 0, 120)));
        btnStyle.isShadowDrop = true;
        style.addInnerStyle("button", btnStyle);

        Style toolbarStyle = getHorizontalStackStyle();
        toolbarStyle.setAlignment(ItemAlignment.HCENTER, ItemAlignment.BOTTOM);
        toolbarStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        toolbarStyle.setSpacing(10, 0);
        toolbarStyle.setPadding(0, 0, 0, 0);
        toolbarStyle.setMargin(0, 0, 0, 0);
        style.addInnerStyle("toolbar", toolbarStyle);

        Style userbarStyle = getHorizontalStackStyle();
        userbarStyle.setAlignment(ItemAlignment.HCENTER, ItemAlignment.BOTTOM);
        userbarStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        userbarStyle.setSpacing(10, 0);
        userbarStyle.setPadding(0, 0, 0, 0);
        userbarStyle.setMargin(0, 0, 0, 0);
        style.addInnerStyle("userbar", userbarStyle);

        Style msgStyle = getLabelStyle();
        msgStyle.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        msgStyle.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
        msgStyle.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        msgStyle.setMargin(10, 0, 10, 40);
        style.addInnerStyle("message", msgStyle);

        Style layoutStyle = getFrameStyle();
        layoutStyle.setMargin(0, 30, 0, 0);
        layoutStyle.setPadding(6, 6, 6, 15);
        layoutStyle.setBackground(255, 255, 255, 20);
        style.addInnerStyle("layout", layoutStyle);

        return style;
    }

    /**
     * Getting default style for a window itself. Properly filled in all the necessary properties.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getWindowContainerStyle() {
        Style style = new Style();

        style.setBackground(45, 45, 45);
        style.setMinSize(200, 200);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.setPadding(2, 2, 2, 2);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        return style;
    }

    /**
     * Getting default style for a FileSystemEntry item. Properly filled in all the necessary properties.
     * <p> Inner styles: "icon", "text".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getFileSystemEntryStyle() {
        Style style = new Style();
        style.height = 25;
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        style.setBackground(0, 0, 0, 0);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.setTextAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        style.font = DefaultsService.getDefaultFont();
        style.setForeground(210, 210, 210);
        style.setPadding(10, 0, 0, 0);
        style.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 30)));

        Style iconStyle = getFrameStyle();
        iconStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        iconStyle.setAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);

        style.addInnerStyle("icon", iconStyle);

        Style textStyle = new Style();
        textStyle.setMargin(24, 0, 0, 0);

        style.addInnerStyle("text", textStyle);

        return style;
    }

    /**
     * Getting default style for a OpenEntryDialog item. Properly filled in all the necessary properties.
     * <p> Inner styles: "window", "layout", "toolbar", "toolbarbutton", 
     * "addressline", "filenameline", "list", "controlpanel", "okbutton", 
     * "cancelbutton", "filter", "filtertext", "divider".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getOpenEntryDialogStyle() {
        // common
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        style.setBackground(0, 0, 0, 150);
        style.borderRadius = new CornerRadius(0);
        style.padding = new Indents();
        style.margin = new Indents();
        style.spacing = new Spacing();

        // window
        Style windowStyle = getDialogItemStyle().getInnerStyle("window");
        windowStyle.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        windowStyle.setMargin(150, 20, 150, 20);
        style.addInnerStyle("window", windowStyle);
        // layout
        Style layoutStyle = getVerticalStackStyle();
        layoutStyle.setMargin(0, 30, 0, 0);
        layoutStyle.setPadding(6, 6, 6, 6);
        layoutStyle.setSpacing(0, 2);
        layoutStyle.setBackground(255, 255, 255, 20);
        style.addInnerStyle("layout", layoutStyle);
        // toolbar
        Style toolbarStyle = getHorizontalStackStyle();
        toolbarStyle.heightPolicy = SizePolicy.FIXED;
        toolbarStyle.height = 30;
        toolbarStyle.setBackground(40, 40, 40);
        toolbarStyle.setSpacing(3, 0);
        toolbarStyle.setPadding(6, 0, 0, 0);
        style.addInnerStyle("toolbar", toolbarStyle);
        // toolbarbutton
        Style toolbarbuttonStyle = Style.getButtonCoreStyle();
        toolbarbuttonStyle.setSize(24, 30);
        toolbarbuttonStyle.background = toolbarStyle.background;
        toolbarbuttonStyle.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 60)));
        toolbarbuttonStyle.addItemState(ItemStateType.PRESSED, new ItemState(new Color(255, 255, 255, 30)));
        toolbarbuttonStyle.borderRadius = new CornerRadius();
        toolbarbuttonStyle.setPadding(3, 6, 3, 6);
        style.addInnerStyle("toolbarbutton", toolbarbuttonStyle);
        // buttonhidden
        Style buttonhiddenStyle = getButtonToggleStyle();
        buttonhiddenStyle.setSize(24, 30);
        buttonhiddenStyle.borderRadius = new CornerRadius();
        buttonhiddenStyle.background = toolbarStyle.background;
        buttonhiddenStyle.setPadding(4, 6, 4, 6);
        buttonhiddenStyle.addItemState(ItemStateType.TOGGLED, new ItemState(new Color(30, 153, 91)));
        style.addInnerStyle("buttonhidden", buttonhiddenStyle);
        // addressline
        Style addresslineStyle = getTextEditStyle();
        addresslineStyle.font = DefaultsService.getDefaultFont(12);
        addresslineStyle.getInnerStyle("text").font = DefaultsService.getDefaultFont(12);
        addresslineStyle.getInnerStyle("text").setForeground(210, 210, 210);
        addresslineStyle.setBackground(50, 50, 50);
        addresslineStyle.height = 24;
        addresslineStyle.setMargin(0, 5, 0, 0);
        style.addInnerStyle("addressline", addresslineStyle);
        // filenameline
        Style filenamelineStyle = getTextEditStyle();
        filenamelineStyle.font = DefaultsService.getDefaultFont(12);
        filenamelineStyle.getInnerStyle("text").font = DefaultsService.getDefaultFont(12);
        filenamelineStyle.getInnerStyle("text").setForeground(210, 210, 210);
        filenamelineStyle.setBackground(50, 50, 50);
        filenamelineStyle.height = 24;
        filenamelineStyle.setMargin(0, 2, 0, 0);
        style.addInnerStyle("filenameline", filenamelineStyle);
        // list
        Style listStyle = getListBoxStyle();
        style.addInnerStyle("list", listStyle);
        // controlpanel
        Style controlpanelStyle = getFrameStyle();
        controlpanelStyle.heightPolicy = SizePolicy.FIXED;
        controlpanelStyle.height = 45;
        controlpanelStyle.setBackground(45, 45, 45);
        controlpanelStyle.setPadding(6, 6, 6, 6);
        style.addInnerStyle("controlpanel", controlpanelStyle);
        // button
        Style okbuttonStyle = getButtonCoreStyle();
        okbuttonStyle.setSize(100, 30);
        okbuttonStyle.setAlignment(ItemAlignment.VCENTER, ItemAlignment.RIGHT);
        okbuttonStyle.setMargin(0, 0, 110, 0);
        okbuttonStyle.setShadow(new Shadow(5, 2, 2, new Color(0, 0, 0, 180)));
        okbuttonStyle.isShadowDrop = true;
        style.addInnerStyle("okbutton", okbuttonStyle);

        Style cancelbuttonStyle = getButtonCoreStyle();
        cancelbuttonStyle.setSize(100, 30);
        cancelbuttonStyle.setAlignment(ItemAlignment.VCENTER, ItemAlignment.RIGHT);
        cancelbuttonStyle.setShadow(new Shadow(5, 2, 2, new Color(0, 0, 0, 180)));
        cancelbuttonStyle.isShadowDrop = true;
        style.addInnerStyle("cancelbutton", cancelbuttonStyle);

        Style filterStyle = getButtonCoreStyle();
        filterStyle.setSize(24, 30);
        filterStyle.setBackground(35, 35, 35);
        filterStyle.setPadding(4, 6, 4, 6);
        filterStyle.setMargin(5, 0, 0, 0);
        style.addInnerStyle("filter", filterStyle);

        Style filtertextStyle = getLabelStyle();
        filtertextStyle.widthPolicy = SizePolicy.FIXED;
        filtertextStyle.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
        filtertextStyle.setPadding(10, 2, 10, 0);
        filtertextStyle.setMargin(-3, 0, 0, 0);
        filtertextStyle.setBackground(55, 55, 55);
        filtertextStyle.font = DefaultsService.getDefaultFont();
        style.addInnerStyle("filtertext", filtertextStyle);

        Style dividerStyle = getFrameStyle();
        dividerStyle.widthPolicy = SizePolicy.FIXED;
        dividerStyle.width = 1;
        dividerStyle.setBackground(55, 55, 55);
        dividerStyle.setMargin(0, 3, 0, 3);
        style.addInnerStyle("divider", dividerStyle);

        return style;
    }

    /**
     * Getting default style for a InputDialog item. Properly filled in all the necessary properties.
     * <p> Inner styles: "window", "button", "textedit", "layout", "toolbar".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getInputDialogStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        style.setBackground(0, 0, 0, 150);
        style.borderRadius = new CornerRadius();
        style.padding = new Indents();
        style.margin = new Indents();
        style.spacing = new Spacing();

        Style windowStyle = getFrameStyle();
        windowStyle.setSize(300, 150);
        windowStyle.setMinSize(300, 150);
        windowStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        windowStyle.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        windowStyle.setPadding(2, 2, 2, 2);
        windowStyle.setBackground(45, 45, 45);
        style.addInnerStyle("window", windowStyle);

        Style okStyle = getButtonCoreStyle();
        okStyle.setBackground(100, 255, 150);
        okStyle.foreground = Color.black;
        okStyle.setSize(100, 30);
        okStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        okStyle.setAlignment(ItemAlignment.LEFT, ItemAlignment.BOTTOM);
        okStyle.setMargin(0, 0, 0, 0);
        okStyle.borderRadius = new CornerRadius();
        okStyle.setShadow(new Shadow(5, 2, 2, new Color(0, 0, 0, 120)));
        okStyle.isShadowDrop = true;
        okStyle.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 80)));
        style.addInnerStyle("button", okStyle);

        Style textStyle = getTextEditStyle();
        textStyle.setAlignment(ItemAlignment.HCENTER, ItemAlignment.TOP);
        textStyle.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
        textStyle.setMargin(0, 15, 0, 0);
        style.addInnerStyle("textedit", textStyle);

        Style layoutStyle = getFrameStyle();
        layoutStyle.setMargin(0, 30, 0, 0);
        layoutStyle.setPadding(6, 6, 6, 15);
        layoutStyle.setBackground(255, 255, 255, 20);
        style.addInnerStyle("layout", layoutStyle);

        Style toolbarStyle = getHorizontalStackStyle();
        toolbarStyle.setAlignment(ItemAlignment.HCENTER, ItemAlignment.BOTTOM);
        toolbarStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        toolbarStyle.setSpacing(10, 0);
        style.addInnerStyle("toolbar", toolbarStyle);

        return style;
    }

    /**
     * Getting default style for a SelectionItem item. Properly filled in all the necessary properties.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getSelectionItemStyle() {
        Style style = new Style();
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        style.setBackground(0, 0, 0, 0);
        style.setPadding(0, 1, 0, 1);
        style.setAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
        style.addItemState(ItemStateType.TOGGLED, new ItemState(new Color(255, 255, 255, 50)));
        return style;
    }

    /**
     * Getting default style for a WrapArea item. Properly filled in all the necessary properties.
     * <p> Inner styles: "selection".
     * <p> This is part of WrapGrid item style as "area".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getWrapAreaStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.padding = new Indents(2, 2, 2, 2);
        style.spacing = new Spacing(0, 5);

        Style selectionStyle = getSelectionItemStyle();
        style.addInnerStyle("selection", selectionStyle);

        return style;
    }

    /**
     * Getting default style for a WrapGrid item. Properly filled in all the necessary properties.
     * <p> Inner styles: "area", "vscrollbar", "hscrollbar".
     * <p> Inner styles for "area": see Style.getWrapAreaStyle().
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getWrapGridStyle() {
        Style style = new Style();

        style.background = new Color(70, 70, 70);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style vsbStyle = getVerticalScrollBarStyle();
        vsbStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.TOP));
        style.addInnerStyle("vscrollbar", vsbStyle);

        Style hsbStyle = getHorizontalScrollBarStyle();
        hsbStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.BOTTOM));
        style.addInnerStyle("hscrollbar", hsbStyle);

        Style areaStyle = getWrapAreaStyle();
        style.addInnerStyle("area", areaStyle);

        return style;
    }

    /**
     * Getting default style for a SideArea item. Properly filled in all the necessary properties.
     * <p> Inner styles: "window", "closebutton".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getSideAreaStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        style.setBackground(0, 0, 0, 130);
        style.borderRadius = new CornerRadius(0);
        style.padding = new Indents();
        style.margin = new Indents();
        style.spacing = new Spacing();

        Style windowStyle = getFrameStyle();
        windowStyle.setPadding(2, 2, 2, 2);
        windowStyle.setBackground(40, 40, 40);
        windowStyle.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
        style.addInnerStyle("window", windowStyle);

        Style closeStyle = new Style();
        closeStyle.setMargin(0, 5, 0, 0);
        closeStyle.font = DefaultsService.getDefaultFont();
        closeStyle.background = new Color(100, 100, 100);
        closeStyle.foreground = new Color(0, 0, 0, 0);
        closeStyle.setSize(15, 15);
        closeStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        closeStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.RIGHT));
        closeStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.TOP));
        ItemState close_hovered = new ItemState();
        close_hovered.background = new Color(186, 95, 97, 255);
        closeStyle.addItemState(ItemStateType.HOVERED, close_hovered);

        closeStyle.shape = GraphicsMathService.getCross(15, 15, 2, 45);
        closeStyle.isFixedShape = true;
        style.addInnerStyle("closebutton", closeStyle);

        return style;
    }

    /**
     * Getting default style for a ImageItem item. Properly filled in all the necessary properties.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getImageItemStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        style.setBackground(0, 0, 0, 0);
        return style;
    }

    /**
     * Getting default style for a LoadingScreen item. Properly filled in all the necessary properties.
     * <p> Inner styles: "text", "image".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getLoadingScreenStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        style.setBackground(0, 0, 0, 150);

        Style textStyle = getLabelStyle();
        textStyle.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        textStyle.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        textStyle.font = DefaultsService.getDefaultFont(Font.BOLD, 14);
        style.addInnerStyle("text", textStyle);

        Style imageStyle = getImageItemStyle();
        imageStyle.setMaxSize(64, 64);
        style.addInnerStyle("image", imageStyle);

        return style;
    }

    /**
     * Getting default style for a Tab item. Properly filled in all the necessary properties.
     * <p> Inner styles: "text", "closebutton", "view".
     * <p> This is part of TabView item style as "tab".
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTabStyle() {
        Style style = new Style();
        style.borderRadius = new CornerRadius(3, 3, 0, 0);
        style.font = DefaultsService.getDefaultFont(14);
        style.background = new Color(255, 255, 255, 10);
        // style.background = new Color(60, 60, 60);
        style.setForeground(210, 210, 210);
        style.minWidth = 30;
        style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        style.setTextAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        style.padding = new Indents(0, 0, 0, 0);
        style.padding = new Indents(10, 2, 5, 2);
        style.spacing = new Spacing(5, 0);
        style.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 60)));
        // style.addItemState(ItemStateType.TOGGLED, new ItemState(new Color(71, 71,
        // 71)));
        style.addItemState(ItemStateType.TOGGLED, new ItemState(new Color(255, 255, 255, 25)));
        style.setShadow(new Shadow(5, 0, 0, new Color(0, 0, 0, 150)));
        style.isShadowDrop = false;

        Style textStyle = getLabelStyle();
        style.addInnerStyle("text", textStyle);

        Style closeStyle = new Style();
        closeStyle.setBackground(100, 100, 100);
        closeStyle.setSize(10, 10);
        closeStyle.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        closeStyle.setAlignment(ItemAlignment.VCENTER, ItemAlignment.RIGHT);
        closeStyle.addItemState(ItemStateType.HOVERED, new ItemState(new Color(0, 162, 232)));
        closeStyle.shape = GraphicsMathService.getCross(10, 10, 2, 45);
        closeStyle.isFixedShape = true;
        style.addInnerStyle("closebutton", closeStyle);

        Style viewStyle = new Style();
        viewStyle.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        viewStyle.background = new Color(71, 71, 71);
        viewStyle.isVisible = false;
        viewStyle.padding = new Indents(2, 2, 2, 2);
        style.addInnerStyle("view", viewStyle);

        return style;
    }

    /**
     * Getting default style for a TabBar item. Properly filled in all the necessary properties.
     * <p> This is part of TabView item style.
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTabBarStyle() {
        Style style = getHorizontalStackStyle();
        style.setSpacing(1, 0);
        return style;
    }

    /**
     * Getting default style for a *** item. Properly filled in all the necessary properties.
     * <p> Inner styles: "tabbar", "tab", "viewarea".
     * <p> Inner styles for "tab": see Style.GetTabStyle().
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTabViewStyle() {
        Style style = getVerticalStackStyle();
        style.background = new Color(50, 50, 50);

        Style tabBarStyle = getTabBarStyle();
        tabBarStyle.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        tabBarStyle.height = 30;
        style.addInnerStyle("tabbar", tabBarStyle);

        Style tabStyle = getTabStyle();
        style.addInnerStyle("tab", tabStyle);

        // Style view_style = tab_style.getInnerStyle("view");
        // if (view_style != null)
        // style.addInnerStyle("view", view_style);

        Style areaStyle = getFrameStyle();
        areaStyle.setPadding(0, 0, 0, 0);
        style.addInnerStyle("viewarea", areaStyle);

        return style;
    }
}
