package com.spvessel.spacevil.Decorations;

import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.GraphicsMathService;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.SpaceVILConstants;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * Style is a class that describes the appearance of an element. Can contains
 * Styles for inner items.
 */
public class Style implements Cloneable {
    private Map<String, Style> _innerStyles = new HashMap<>();

    /**
     * Background color of an item's shape. Attention: this property is required.
     * <p>
     * This property is java.awt.Color.
     */
    public Color background;

    /**
     * Color of an item's text. Can be used only if the item contains text and in
     * this case this property is required.
     * <p>
     * This property is java.awt.Color.
     */
    public Color foreground;

    /**
     * Font of an item's text. Can be used only if the item contains text and in
     * this case this property is required.
     * <p>
     * This property is java.awt.Font
     */
    public Font font = null;

    /**
     * Width policy of an item's shape. Can be Fixed (shape not changes its size) or
     * Expand (shape is stretched to all available space). Attention: this property
     * is required.
     * <p>
     * This property is com.spvessel.spacevil.Flags.SizePolicy.
     */
    public SizePolicy widthPolicy;

    /**
     * Height policy of an item's shape. Can be Fixed (shape not changes its size)
     * or Expand (shape is stretched to all available space). Attention: this
     * property is required.
     * <p>
     * This property is com.spvessel.spacevil.Flags.SizePolicy.
     */
    public SizePolicy heightPolicy;

    /**
     * Width of an item's shape.
     */
    public int width;

    /**
     * Minimum width of an item's shape (shape cannot be smaller this value).
     * <p>
     * Default: 0.
     */
    public int minWidth;

    /**
     * Maximum width of an item's shape (shape cannot be bigger this value).
     * <p>
     * Default: 32767.
     */
    public int maxWidth;

    /**
     * Height of an item's shape.
     */
    public int height;

    /**
     * Minimum height of an item's shape (shape cannot be smaller this value).
     * <p>
     * Default: 0.
     */
    public int minHeight;

    /**
     * Maximum height of an item's shape (shape cannot be bigget this value).
     * <p>
     * Default: 32767.
     */
    public int maxHeight;

    /**
     * Alignment of an item's shape relative to its container. Combines with
     * alignment by vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT,
     * HCENTER, RIGHT). Attention: this property is required.
     * <p>
     * This property is com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public List<ItemAlignment> alignment;

    /**
     * Alignment of an item's text. Combines with alignment by vertically (TOP,
     * VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT). Can be used only if
     * the item contains text and in this case this property is required.
     * <p>
     * This property is com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public List<ItemAlignment> textAlignment;

    /**
     * X axis position of left-top cornet of an item's shape. This property itself
     * is mostly ignored. Used only when creating container-type items or with
     * com.spvessel.spacevil.FreeArea.
     */
    public int x;

    /**
     * Y axis position of left-top cornet of an item's shape. This property itself
     * is mostly ignored. Used only when creating container-type items or with
     * com.spvessel.spacevil.FreeArea.
     */
    public int y;

    private Map<ItemStateType, ItemState> _itemStates = new HashMap<>();

    /**
     * Indents of an item to offset its children. Attention: this property is
     * required.
     * <p>
     * This property is com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents padding = new Indents();

    /**
     * Indents between children of a container type item. It is used mainly in
     * containers.
     * <p>
     * This property is com.spvessel.spacevil.Decorations.Spacing.
     */
    public Spacing spacing = new Spacing();

    /**
     * Indents of an item to offset itself relative to its container. Attention:
     * this property is required.
     * <p>
     * This property is com.spvessel.spacevil.Decorations.Indents.
     */
    public Indents margin = new Indents();

    /**
     * Border for an item's shape.
     */
    public Border border = new Border();

    /**
     * A form of an item's shape. If not assigned, the shape is rectangular.
     * <p>
     * Format: java.util.List&lt;float[]&gt;.
     */
    public List<float[]> shape;// = new List<float[]>();

    /**
     * A flag that determines if the shape of an item can be changed or not.
     * <p>
     * True: if shape can not be resized. False: if shape can be resised. Default:
     * False.
     */
    public boolean isFixedShape = false;

    /**
     * A storage of shapes for future use. Note: not supported in the current
     * version!
     * <p>
     * Format: java.util.List&lt;com.spvessel.spacevil.Core.IBaseItem&gt;.
     */
    public List<IBaseItem> innerShapes;// = new List<float[]>();

    /**
     * Shadow of an item's shape.
     */
    public Shadow shadow = null;

    /**
     * A flag that determines if an item is visible or not.
     * <p>
     * True: if visible. False: if not visible. Default: True.
     */
    public boolean isVisible;

    /**
     * Constructs a default Style.
     */
    public Style() {
        isVisible = true;
        maxWidth = SpaceVILConstants.sizeMaxValue;
        maxHeight = SpaceVILConstants.sizeMaxValue;
        setAlignment(ItemAlignment.Left, ItemAlignment.Top);
    }

    /**
     * Setting this style for all items in sequence.
     * 
     * @param items A sequence of items that are
     *              com.spvessel.spacevil.Core.IBaseItem.
     */
    public void setStyle(IBaseItem... items) {
        for (IBaseItem item : items) {
            item.setStyle(this);
        }
    }

    /**
     * Setting size of an item's shape.
     * 
     * @param width  Width of a shape.
     * @param height Height of a shape.
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Setting minimum size of an item's shape (shape can not be smaller than
     * specified width and height).
     * 
     * @param width  Minimum width of a shape.
     * @param height Minimum height of a shape.
     */
    public void setMinSize(int width, int height) {
        minWidth = width;
        minHeight = height;
    }

    /**
     * Setting maximim size of an item's shape (shape can not be bigger than
     * specified width and height).
     * 
     * @param width  Maximim width of a shape.
     * @param height Maximim height of a shape.
     */
    public void setMaxSize(int width, int height) {
        maxWidth = width;
        maxHeight = height;
    }

    /**
     * Setting the size policy of an item's shape. Can be FIXED (shape not changes
     * its size) or EXPAND (shape is stretched to all available space).
     * 
     * @param widthPolicy  Width policy of an item's shape.
     * @param heightPolicy Height policy of an item's shape.
     */
    public void setSizePolicy(SizePolicy widthPolicy, SizePolicy heightPolicy) {
        this.widthPolicy = widthPolicy;
        this.heightPolicy = heightPolicy;
    }

    /**
     * Setting background color of an item's shape in byte RGB format.
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setBackground(int r, int g, int b) {
        background = GraphicsMathService.colorTransform(r, g, b);
    }

    /**
     * Setting background color of an item's shape in byte RGBA format.
     * 
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
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setBackground(float r, float g, float b) {
        background = GraphicsMathService.colorTransform(r, g, b);
    }

    /**
     * Setting background color of an item's shape in float RGBA format.
     * 
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
     * 
     * @param r Red color component. Range: (0 - 255)
     * @param g Green color component. Range: (0 - 255)
     * @param b Blue color component. Range: (0 - 255)
     */
    public void setForeground(int r, int g, int b) {
        foreground = GraphicsMathService.colorTransform(r, g, b);
    }

    /**
     * Setting text color of an item in byte RGBA format.
     * 
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
     * 
     * @param r Red color component. Range: (0.0f - 1.0f)
     * @param g Green color component. Range: (0.0f - 1.0f)
     * @param b Blue color component. Range: (0.0f - 1.0f)
     */
    public void setForeground(float r, float g, float b) {
        foreground = GraphicsMathService.colorTransform(r, g, b);
    }

    /**
     * Setting text color of an item in float RGBA format.
     * 
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
     * 
     * @param padding Padding indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setPadding(Indents padding) {
        this.padding = padding;
    }

    /**
     * Setting indents of an item to offset its children.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
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
     * 
     * @param margin Indents as com.spvessel.spacevil.Decorations.Indents.
     */
    public void setMargin(Indents margin) {
        this.margin = margin;
    }

    /**
     * Setting indents of an item to offset itself relative to its container.
     * 
     * @param left   Indent on the left.
     * @param top    Indent on the top.
     * @param right  Indent on the right.
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
     * 
     * @param spacing Spacing as com.spvessel.spacevil.Decorations.Spacing.
     */
    public void setSpacing(Spacing spacing) {
        this.spacing = spacing;
    }

    /**
     * Setting indents between children of a container type item.
     * 
     * @param horizontal Horizontal indent.
     * @param vertical   Vertical indent.
     */
    public void setSpacing(int horizontal, int vertical) {
        spacing.horizontal = horizontal;
        spacing.vertical = vertical;
    }

    /**
     * Setting border of an item's shape. Border consist of corner radiuses,
     * thickness and color.
     * 
     * @param border Border as com.spvessel.spacevil.Decorations.Border.
     */
    public void setBorder(Border border) {
        this.border = border.clone();
    }

    /**
     * Setting border for an item's shape. Border consist of corner radiuses,
     * thickness and color.
     * 
     * @param fill      Border color as java.awt.Color.
     * @param radius    Radiuses of an border corners as
     *                  com.spvessel.spacevil.Decorations.CornerRadius.
     * @param thickness Border thickness.
     */
    public void setBorder(Color color, CornerRadius radius, int thickness) {
        border.setColor(color);
        border.setRadius(radius);
        border.setThickness(thickness);
    }

    /**
     * Setting shadow for an item's shape.
     * 
     * @param shadow Shadow as com.spvessel.spacevil.Decorations.Shadow.
     */
    public void setShadow(Shadow shadow) {
        this.shadow = shadow;
    }

    /**
     * Setting an Alignment of an item's shape relative to its container. Combines
     * with alignment by vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT,
     * HCENTER, RIGHT). Attention: this property is required.
     * 
     * @param alignment Alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setAlignment(ItemAlignment... alignment) {
        setAlignment(Arrays.asList(alignment)); //this.alignment = Arrays.asList(alignment);
    }

    /**
     * Setting an Alignment of an item's shape relative to its container. Combines
     * with alignment by vertically (TOP, VCENTER, BOTTOM) and horizontally (LEFT,
     * HCENTER, RIGHT). Attention: this property is required.
     * 
     * @param alignment Alignment as List of com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setAlignment(List<ItemAlignment> alignment) {
        this.alignment = new ArrayList<>(alignment); //Arrays.asList(alignment);
    }

    /**
     * Alignment of an item's text. Combines with alignment by vertically (TOP,
     * VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT). Can be used only if
     * the item contains text and in this case this property is required.
     * 
     * @param alignment Text alignment as com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        setTextAlignment(Arrays.asList(alignment)); //this.textAlignment = Arrays.asList(alignment);
    }

    /**
     * Alignment of an item's text. Combines with alignment by vertically (TOP,
     * VCENTER, BOTTOM) and horizontally (LEFT, HCENTER, RIGHT). Can be used only if
     * the item contains text and in this case this property is required.
     * 
     * @param alignment Text alignment as List of com.spvessel.spacevil.Flags.ItemAlignment.
     */
    public void setTextAlignment(List<ItemAlignment> alignment) {
        this.textAlignment = new ArrayList<>(alignment); //Arrays.asList(alignment);
    }

    ////////////////////////////////////////////////////////////////

    /**
     * Add inner primitives to the object (as decorations only). Note: not supported
     * in the current version!
     * 
     * @param shape Shape as com.spvessel.spacevil.Core.IBaseItem.
     */
    public void addInnerShape(IBaseItem shape) {
        if (innerShapes == null) {
            innerShapes = new LinkedList<>();
        }
        innerShapes.add(shape);
    }

    /**
     * Assigning a style for an item's child by key name.
     * 
     * @param keyName Key name of a child.
     * @param style   Style as com.spvessel.spacevil.Decorations.Style.
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
     * 
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
     * 
     * @param keyName Key name of a child.
     */
    public void removeInnerStyle(String keyName) {
        if (_innerStyles.containsKey(keyName)) {
            _innerStyles.remove(keyName);
        }
    }

    /**
     * Adding visual state for an item.
     * <p>
     * Type can be BASE, HOVERED, PRESSED, TOGGLED, FOCUSED, DISABLED.
     * 
     * @param type  Type as com.spvessel.spacevil.Flags.ItemStateType.
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
     * <p>
     * Type can be BASE, HOVERED, PRESSED, TOGGLED, FOCUSED, DISABLED.
     * 
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
     * 
     * @return Map of an ItemStateTypes and its ItemStates.
     */
    public Map<ItemStateType, ItemState> getAllStates() {
        return _itemStates;
    }

    /**
     * Removing visual state of an item by type.
     * <p>
     * Type can be BASE, HOVERED, PRESSED, TOGGLED, FOCUSED, DISABLED.
     * 
     * @param type Type as com.spvessel.spacevil.Flags.ItemStateType.
     */
    public void removeItemState(ItemStateType type) {
        if (type == ItemStateType.Base) {
            return;
        }
        if (_itemStates.containsKey(type)) {
            _itemStates.remove(type);
        }
    }

    /**
     * Cloning the current style and returning a new deep copy of Style.
     * 
     * @return Deep copy of current style as
     *         com.spvessel.spacevil.Decorations.Style.
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
            // ItemAlignment[] list = new ItemAlignment[alignment.size()];
            // alignment.toArray(list);
            // style.setAlignment(list);
            style.setAlignment(alignment);
        }

        if (textAlignment != null) {
            // ItemAlignment[] textlist = new ItemAlignment[textAlignment.size()];
            // textAlignment.toArray(textlist);
            // style.setTextAlignment(textlist);
            style.setTextAlignment(textAlignment);
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

        if (border != null) {
            style.border = border.clone();
        }

        if (shadow != null) {
            style.shadow = shadow.clone();
        }

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
     * Getting a default common style. Properly filled in all the necessary
     * properties.
     * <p>
     * Use this method to create instance of Style class instead of using pure
     * constructor (new Style()).
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getDefaultCommonStyle() {
        Style style = new Style();

        style.background = Color.white;
        style.foreground = Color.black;
        style.font = DefaultsService.getDefaultFont();
        style.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        style.setSize(30, 30);
        style.setAlignment(ItemAlignment.Left, ItemAlignment.Top);
        style.setTextAlignment(ItemAlignment.Left, ItemAlignment.Top);
        style.setPadding(0, 0, 0, 0);
        style.setMargin(0, 0, 0, 0);
        style.setSpacing(0, 0);
        style.setBorder(new Border(new Color(0, 0, 0, 0), new CornerRadius(), 0));

        return style;
    }

    // get default styles
    /**
     * Getting default style for a ButtonCore item. Properly filled in all the
     * necessary properties.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getButtonCoreStyle() {
        Style style = new Style();

        style.background = new Color(13, 176, 255);
        style.foreground = new Color(32, 32, 32);
        style.font = DefaultsService.getDefaultFont(16);
        style.widthPolicy = SizePolicy.Fixed;
        style.heightPolicy = SizePolicy.Fixed;
        style.width = 30;
        style.height = 30;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCenter, ItemAlignment.VCenter));
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 60);
        style.addItemState(ItemStateType.Hovered, hovered);
        ItemState pressed = new ItemState();
        pressed.background = new Color(0, 0, 0, 60);
        style.addItemState(ItemStateType.Pressed, pressed);

        return style;
    }

    /**
     * Getting default style for a ButtonToggle item. Properly filled in all the
     * necessary properties.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getButtonToggleStyle() {
        Style style = new Style();
        style.background = new Color(13, 176, 255);
        style.foreground = new Color(32, 32, 32);
        style.font = DefaultsService.getDefaultFont(16);
        style.widthPolicy = SizePolicy.Fixed;
        style.heightPolicy = SizePolicy.Fixed;
        style.width = 10;
        style.height = 10;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCenter, ItemAlignment.VCenter));

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 60);
        style.addItemState(ItemStateType.Hovered, hovered);

        ItemState pressed = new ItemState();
        pressed.background = new Color(30, 0, 0, 60);
        style.addItemState(ItemStateType.Pressed, pressed);

        ItemState toggled = new ItemState();
        toggled.background = new Color(121, 223, 152);
        style.addItemState(ItemStateType.Toggled, toggled);

        return style;
    }

    /**
     * Getting default style for a CheckBox item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "indicator", "text".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getCheckBoxStyle() {
        Style style = new Style();

        style.background = new Color(80, 80, 80, 255);
        style.foreground = new Color(210, 210, 210);
        style.font = DefaultsService.getDefaultFont(12);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Fixed;
        style.width = 10;
        style.height = 20;
        style.minHeight = 20;
        style.minWidth = 20;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));

        Style indicatorStyle = getIndicatorStyle();
        style.addInnerStyle("indicator", indicatorStyle);

        Style textlineStyle = getLabelStyle();
        textlineStyle.foreground = new Color(210, 210, 210);
        textlineStyle.widthPolicy = SizePolicy.Expand;
        textlineStyle.heightPolicy = SizePolicy.Expand;
        textlineStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter));
        textlineStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        textlineStyle.margin = new Indents(10 + indicatorStyle.width, 0, 0, 0);
        style.addInnerStyle("text", textlineStyle);

        return style;
    }

    /**
     * Getting default style for a Indicator item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "marker".
     * <p>
     * This is part of CheckBox item style.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getIndicatorStyle() {
        Style style = new Style();

        style.background = new Color(32, 32, 32);
        style.widthPolicy = SizePolicy.Fixed;
        style.heightPolicy = SizePolicy.Fixed;
        style.width = 20;
        style.height = 20;
        style.minHeight = 20;
        style.minWidth = 20;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        style.padding = new Indents(4, 4, 4, 4);

        Style markerStyle = new Style();
        markerStyle.background = new Color(32, 32, 32);
        markerStyle.foreground = new Color(70, 70, 70);
        markerStyle.font = DefaultsService.getDefaultFont();
        markerStyle.widthPolicy = SizePolicy.Expand;
        markerStyle.heightPolicy = SizePolicy.Expand;
        markerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCenter, ItemAlignment.VCenter));
        markerStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCenter, ItemAlignment.VCenter));

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 60);
        markerStyle.addItemState(ItemStateType.Hovered, hovered);

        ItemState toggled = new ItemState();
        toggled.background = new Color(255, 181, 111);
        markerStyle.addItemState(ItemStateType.Toggled, toggled);

        style.addInnerStyle("marker", markerStyle);

        return style;
    }

    /**
     * Getting default style for a text type item. Attention: not all the necessary
     * properties properly filled.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTextLineStyle() {
        Style style = new Style();

        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        style.margin = new Indents(4, 4, 4, 4);

        return style;
    }

    /**
     * Getting default style for a ComboBox item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "selection", "dropdownbutton", "dropdownarea", "arrow".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getComboBoxStyle() {
        Style style = new Style();
        style.background = new Color(220, 220, 220);
        style.foreground = new Color(70, 70, 70);
        style.font = DefaultsService.getDefaultFont();
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Fixed;
        style.width = 10;
        style.height = 30;
        style.minHeight = 10;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));

        Style selectionStyle = new Style();
        selectionStyle.background = new Color(0, 0, 0, 0);
        selectionStyle.foreground = new Color(70, 70, 70);

        selectionStyle.font = DefaultsService.getDefaultFont(14);
        selectionStyle.widthPolicy = SizePolicy.Expand;
        selectionStyle.heightPolicy = SizePolicy.Expand;
        selectionStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        selectionStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        selectionStyle.padding = new Indents(10, 0, 0, 0);
        selectionStyle.margin = new Indents(0, 0, 20, 0);
        style.addInnerStyle("selection", selectionStyle);

        Style dropdownbuttonStyle = getButtonCoreStyle();
        dropdownbuttonStyle.width = 20;
        dropdownbuttonStyle.widthPolicy = SizePolicy.Fixed;
        dropdownbuttonStyle.heightPolicy = SizePolicy.Expand;
        dropdownbuttonStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Right, ItemAlignment.VCenter));
        dropdownbuttonStyle.background = new Color(255, 181, 111);

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 40);
        dropdownbuttonStyle.addItemState(ItemStateType.Hovered, hovered);

        style.addInnerStyle("dropdownbutton", dropdownbuttonStyle);

        style.addInnerStyle("dropdownarea", getComboBoxDropDownStyle());

        Style arrowStyle = new Style();
        arrowStyle.width = 14;
        arrowStyle.height = 6;
        arrowStyle.widthPolicy = SizePolicy.Fixed;
        arrowStyle.heightPolicy = SizePolicy.Fixed;
        arrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCenter, ItemAlignment.VCenter));
        arrowStyle.background = new Color(50, 50, 50);
        arrowStyle.shape = GraphicsMathService.getTriangle(100, 100, 0, 0, 180);
        style.addInnerStyle("arrow", arrowStyle);

        return style;
    }

    /**
     * Getting default style for a ComboBoxDropDown item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "itemlist".
     * <p>
     * Inner styles for "itemlist": "vscrollbar", "hscrollbar", "menu".
     * <p>
     * This is part of ComboBox item style.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getComboBoxDropDownStyle() {
        Style style = new Style();
        style.background = Color.white;
        style.widthPolicy = SizePolicy.Fixed;
        style.heightPolicy = SizePolicy.Fixed;
        style.padding = new Indents(0, 0, 0, 0);
        style.isVisible = false;

        Style itemlistStyle = getListBoxStyle();
        itemlistStyle.background = new Color(0, 0, 0, 0);
        itemlistStyle.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        style.addInnerStyle("itemlist", itemlistStyle);

        Style itemlistareaStyle = itemlistStyle.getInnerStyle("area");
        if (itemlistareaStyle != null) {
            itemlistStyle.setPadding(0, 0, 0, 0);
        }

        Style vsbStyle = getSimpleVerticalScrollBarStyle();
        vsbStyle.setAlignment(ItemAlignment.Right, ItemAlignment.Top);
        itemlistStyle.addInnerStyle("vscrollbar", vsbStyle);

        Style hsbStyle = getHorizontalScrollBarStyle();
        hsbStyle.setAlignment(ItemAlignment.Left, ItemAlignment.Bottom);
        itemlistStyle.addInnerStyle("hscrollbar", hsbStyle);

        Style menuStyle = new Style();
        menuStyle.background = new Color(50, 50, 50);
        menuStyle.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        menuStyle.setAlignment(ItemAlignment.Right, ItemAlignment.Bottom);
        itemlistStyle.addInnerStyle("menu", menuStyle);

        return style;
    }

    /**
     * Getting default style for a MenuItem item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "text", "arrow".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getMenuItemStyle() {
        Style style = new Style();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = new Color(70, 70, 70);
        style.font = DefaultsService.getDefaultFont();
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Fixed;
        style.height = 25;
        style.minHeight = 10;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        style.padding = new Indents(10, 0, 10, 0);
        style.addItemState(ItemStateType.Hovered, new ItemState(new Color(200, 200, 200)));

        Style textStyle = new Style();
        textStyle.setMargin(0, 0, 0, 0);
        style.addInnerStyle("text", textStyle);

        Style arrowStyle = new Style();
        arrowStyle.width = 6;
        arrowStyle.height = 10;
        arrowStyle.widthPolicy = SizePolicy.Fixed;
        arrowStyle.heightPolicy = SizePolicy.Fixed;
        arrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Right, ItemAlignment.VCenter));
        arrowStyle.background = new Color(80, 80, 80);
        arrowStyle.margin = new Indents(10, 0, 0, 0);
        arrowStyle.shape = GraphicsMathService.getTriangle(100, 100, 0, 0, 90);
        style.addInnerStyle("arrow", arrowStyle);

        return style;
    }

    /**
     * Getting default style for a ContextMenu item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "itemlist".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getContextMenuStyle() {
        Style style = getDefaultCommonStyle();
        style.background = new Color(210, 210, 210);
        style.isVisible = false;

        Style itemlistStyle = getListBoxStyle();
        itemlistStyle.background = new Color(0, 0, 0, 0);
        itemlistStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCenter, ItemAlignment.VCenter));
        style.addInnerStyle("itemlist", itemlistStyle);

        Style areaStyle = itemlistStyle.getInnerStyle("area");
        areaStyle.setPadding(0, 0, 0, 0);

        return style;
    }

    /**
     * Getting default style for a FreeArea item. Properly filled in all the
     * necessary properties.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getFreeAreaStyle() {
        Style style = new Style();

        // style.background = new Color(0,0,0,0);
        style.background = new Color(70, 70, 70, 255);

        style.padding = new Indents(2, 2, 2, 2);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));

        return style;
    }

    /**
     * Getting default style for a Frame item. Properly filled in all the necessary
     * properties.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getFrameStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.padding = new Indents(2, 2, 2, 2);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));

        return style;
    }

    /**
     * Getting default style for a Grid item. Properly filled in all the necessary
     * properties.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getGridStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));

        return style;
    }

    /**
     * Getting default style for a HorizontalScrollBar item. Properly filled in all
     * the necessary properties.
     * <p>
     * Inner styles: "uparrow", "downarrow", "slider".
     * <p>
     * Inner styles for "slider": "track", "handler".
     * <p>
     * This is part of many items style.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getHorizontalScrollBarStyle() {
        Style style = new Style();

        style.background = new Color(50, 50, 50);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Fixed;
        style.height = 16;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));

        Style uparrowStyle = getButtonCoreStyle();
        uparrowStyle.widthPolicy = SizePolicy.Fixed;
        uparrowStyle.heightPolicy = SizePolicy.Fixed;
        uparrowStyle.background = new Color(100, 100, 100, 255);
        uparrowStyle.width = 16;
        uparrowStyle.height = 16;
        uparrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        uparrowStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        uparrowStyle.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, -90);
        uparrowStyle.isFixedShape = true;

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 40);
        uparrowStyle.addItemState(ItemStateType.Hovered, hovered);

        style.addInnerStyle("uparrow", uparrowStyle);

        Style downarrowStyle = getButtonCoreStyle();
        downarrowStyle.widthPolicy = SizePolicy.Fixed;
        downarrowStyle.heightPolicy = SizePolicy.Fixed;
        downarrowStyle.background = new Color(100, 100, 100, 255);
        downarrowStyle.width = 16;
        downarrowStyle.height = 16;
        downarrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Right, ItemAlignment.VCenter));
        downarrowStyle.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, 90);
        downarrowStyle.isFixedShape = true;
        downarrowStyle.addItemState(ItemStateType.Hovered, hovered);

        style.addInnerStyle("downarrow", downarrowStyle);

        Style sliderStyle = new Style();
        sliderStyle.widthPolicy = SizePolicy.Expand;
        sliderStyle.heightPolicy = SizePolicy.Expand;
        sliderStyle.background = new Color(0, 0, 0, 0);
        sliderStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Top, ItemAlignment.HCenter));
        style.addInnerStyle("slider", sliderStyle);

        Style trackStyle = new Style();
        trackStyle.widthPolicy = SizePolicy.Expand;
        trackStyle.heightPolicy = SizePolicy.Expand;
        trackStyle.background = new Color(0, 0, 0, 0);
        trackStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        sliderStyle.addInnerStyle("track", trackStyle);

        Style handlerStyle = new Style();
        handlerStyle.widthPolicy = SizePolicy.Fixed;
        handlerStyle.heightPolicy = SizePolicy.Expand;
        handlerStyle.background = new Color(100, 100, 100, 255);
        handlerStyle.margin = new Indents(0, 3, 0, 3);
        handlerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        handlerStyle.addItemState(ItemStateType.Hovered, hovered);
        handlerStyle.minWidth = 15;
        sliderStyle.addInnerStyle("handler", handlerStyle);

        return style;
    }

    /**
     * Getting simplified style for a SimpleHorizontalScrollBar item. Properly
     * filled in all the necessary properties.
     * <p>
     * Inner styles: "uparrow", "downarrow", "slider".
     * <p>
     * Inner styles for "slider": "track", "handler".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getSimpleHorizontalScrollBarStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.padding = new Indents(2, 0, 2, 0);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Fixed;
        style.height = 16;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));

        Style uparrowStyle = getButtonCoreStyle();
        uparrowStyle.isVisible = false;
        style.addInnerStyle("uparrow", uparrowStyle);

        Style downarrowStyle = getButtonCoreStyle();
        downarrowStyle.isVisible = false;
        style.addInnerStyle("downarrow", downarrowStyle);

        Style sliderStyle = new Style();
        sliderStyle.widthPolicy = SizePolicy.Expand;
        sliderStyle.heightPolicy = SizePolicy.Expand;
        sliderStyle.background = new Color(0, 0, 0, 0);
        sliderStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Top, ItemAlignment.HCenter));
        style.addInnerStyle("slider", sliderStyle);

        Style trackStyle = new Style();
        trackStyle.widthPolicy = SizePolicy.Expand;
        trackStyle.heightPolicy = SizePolicy.Expand;
        trackStyle.background = new Color(0, 0, 0, 0);
        trackStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        sliderStyle.addInnerStyle("track", trackStyle);

        Style handlerStyle = new Style();
        handlerStyle.widthPolicy = SizePolicy.Fixed;
        handlerStyle.heightPolicy = SizePolicy.Expand;
        handlerStyle.background = new Color(120, 120, 120, 255);
        handlerStyle.margin = new Indents(0, 5, 0, 5);
        handlerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        handlerStyle.border.setRadius(new CornerRadius(3));
        handlerStyle.minWidth = 15;
        sliderStyle.addInnerStyle("handler", handlerStyle);

        return style;
    }

    /**
     * Getting default style for a VerticalScrollBar item. Properly filled in all
     * the necessary properties.
     * <p>
     * Inner styles: "uparrow", "downarrow", "slider".
     * <p>
     * Inner styles for "slider": "track", "handler".
     * <p>
     * This is part of many items style.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getVerticalScrollBarStyle() {
        Style style = new Style();

        style.background = new Color(50, 50, 50);
        style.widthPolicy = SizePolicy.Fixed;
        style.heightPolicy = SizePolicy.Expand;
        style.width = 16;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));

        Style uparrowStyle = getButtonCoreStyle();
        uparrowStyle.widthPolicy = SizePolicy.Fixed;
        uparrowStyle.heightPolicy = SizePolicy.Fixed;
        uparrowStyle.background = new Color(100, 100, 100, 255);
        uparrowStyle.width = 16;
        uparrowStyle.height = 16;
        uparrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Top, ItemAlignment.HCenter));
        uparrowStyle.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, 0);
        uparrowStyle.isFixedShape = true;

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 40);
        uparrowStyle.addItemState(ItemStateType.Hovered, hovered);

        style.addInnerStyle("uparrow", uparrowStyle);

        Style downarrowStyle = getButtonCoreStyle();
        downarrowStyle.widthPolicy = SizePolicy.Fixed;
        downarrowStyle.heightPolicy = SizePolicy.Fixed;
        downarrowStyle.background = new Color(100, 100, 100, 255);
        downarrowStyle.width = 16;
        downarrowStyle.height = 16;
        downarrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Bottom, ItemAlignment.HCenter));
        downarrowStyle.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, 180);
        downarrowStyle.isFixedShape = true;
        downarrowStyle.addItemState(ItemStateType.Hovered, hovered);
        style.addInnerStyle("downarrow", downarrowStyle);

        Style sliderStyle = new Style();
        sliderStyle.widthPolicy = SizePolicy.Expand;
        sliderStyle.heightPolicy = SizePolicy.Expand;
        sliderStyle.background = new Color(0, 0, 0, 0);
        sliderStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Top, ItemAlignment.HCenter));
        style.addInnerStyle("slider", sliderStyle);

        Style trackStyle = new Style();
        trackStyle.widthPolicy = SizePolicy.Expand;
        trackStyle.heightPolicy = SizePolicy.Expand;
        trackStyle.background = new Color(0, 0, 0, 0);
        trackStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        sliderStyle.addInnerStyle("track", trackStyle);

        Style handlerStyle = new Style();
        handlerStyle.widthPolicy = SizePolicy.Expand;
        handlerStyle.heightPolicy = SizePolicy.Fixed;
        handlerStyle.background = new Color(100, 100, 100, 255);
        handlerStyle.margin = new Indents(3, 0, 3, 0);
        handlerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Top, ItemAlignment.HCenter));
        handlerStyle.minHeight = 15;
        handlerStyle.addItemState(ItemStateType.Hovered, hovered);
        sliderStyle.addInnerStyle("handler", handlerStyle);

        return style;
    }

    /**
     * Getting simplified style for a SimpleVerticalScrollBar item. Properly filled
     * in all the necessary properties.
     * <p>
     * Inner styles: "uparrow", "downarrow", "slider".
     * <p>
     * Inner styles for "slider": "track", "handler".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getSimpleVerticalScrollBarStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.padding = new Indents(0, 2, 0, 2);
        style.widthPolicy = SizePolicy.Fixed;
        style.heightPolicy = SizePolicy.Expand;
        style.width = 16;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));

        Style uparrowStyle = getButtonCoreStyle();
        uparrowStyle.isVisible = false;
        style.addInnerStyle("uparrow", uparrowStyle);

        Style downarrowStyle = getButtonCoreStyle();
        downarrowStyle.isVisible = false;
        style.addInnerStyle("downarrow", downarrowStyle);

        Style sliderStyle = new Style();
        sliderStyle.widthPolicy = SizePolicy.Expand;
        sliderStyle.heightPolicy = SizePolicy.Expand;
        sliderStyle.background = new Color(0, 0, 0, 0);
        sliderStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Top, ItemAlignment.HCenter));
        style.addInnerStyle("slider", sliderStyle);

        Style trackStyle = new Style();
        trackStyle.widthPolicy = SizePolicy.Expand;
        trackStyle.heightPolicy = SizePolicy.Expand;
        trackStyle.background = new Color(0, 0, 0, 0);
        trackStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        sliderStyle.addInnerStyle("track", trackStyle);

        Style handlerStyle = new Style();
        handlerStyle.widthPolicy = SizePolicy.Expand;
        handlerStyle.heightPolicy = SizePolicy.Fixed;
        handlerStyle.background = new Color(120, 120, 120, 255);
        handlerStyle.margin = new Indents(5, 0, 5, 0);
        handlerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Top, ItemAlignment.HCenter));
        handlerStyle.border.setRadius(new CornerRadius(3));
        handlerStyle.minHeight = 15;
        sliderStyle.addInnerStyle("handler", handlerStyle);

        return style;
    }

    /**
     * Getting default style for a HorizontalSlider item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "track", "handler".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getHorizontalSliderStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Fixed;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Left));
        style.height = 25;

        Style trackStyle = new Style();
        trackStyle.widthPolicy = SizePolicy.Expand;
        trackStyle.heightPolicy = SizePolicy.Fixed;
        trackStyle.height = 5;
        trackStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter));
        trackStyle.background = new Color(100, 100, 100);
        style.addInnerStyle("track", trackStyle);

        Style handlerStyle = new Style();
        handlerStyle.widthPolicy = SizePolicy.Fixed;
        handlerStyle.heightPolicy = SizePolicy.Expand;
        handlerStyle.width = 10;
        handlerStyle.background = new Color(255, 181, 111);
        handlerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left));

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        handlerStyle.addItemState(ItemStateType.Hovered, hovered);

        style.addInnerStyle("handler", handlerStyle);

        return style;
    }

    /**
     * Getting default style for a VerticalSlider item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "track", "handler".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getVerticalSliderStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.Fixed;
        style.heightPolicy = SizePolicy.Expand;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Left));
        style.width = 25;

        Style trackStyle = new Style();
        trackStyle.widthPolicy = SizePolicy.Fixed;
        trackStyle.heightPolicy = SizePolicy.Expand;
        trackStyle.width = 5;
        trackStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCenter));
        trackStyle.background = new Color(100, 100, 100);
        style.addInnerStyle("track", trackStyle);

        Style handlerStyle = new Style();
        handlerStyle.widthPolicy = SizePolicy.Expand;
        handlerStyle.heightPolicy = SizePolicy.Fixed;
        handlerStyle.height = 10;
        handlerStyle.background = new Color(255, 181, 111);
        handlerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Top));
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        handlerStyle.addItemState(ItemStateType.Hovered, hovered);
        style.addInnerStyle("handler", handlerStyle);

        return style;
    }

    /**
     * Getting default style for a HorizontalStack item. Properly filled in all the
     * necessary properties.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getHorizontalStackStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));
        style.spacing = new Spacing(0, 0);
        style.setPadding(new Indents());
        style.setMargin(new Indents());

        return style;
    }

    /**
     * Getting default style for a VerticalStack item. Properly filled in all the
     * necessary properties.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getVerticalStackStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));
        style.spacing = new Spacing(0, 0);

        return style;
    }

    /**
     * Getting default style for a HorizontalSplitArea item. Properly filled in all
     * the necessary properties.
     * <p>
     * Inner styles: "splitholder".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getHorizontalSplitAreaStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));

        Style splitterStyle = new Style();
        splitterStyle.background = new Color(42, 42, 42);
        splitterStyle.width = 6;
        style.addInnerStyle("splitholder", splitterStyle);

        return style;
    }

    /**
     * Getting default style for a VerticalSplitArea item. Properly filled in all
     * the necessary properties.
     * <p>
     * Inner styles: "splitholder".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getVerticalSplitAreaStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));

        Style splitterStyle = new Style();
        splitterStyle.background = new Color(42, 42, 42);
        splitterStyle.height = 6;
        style.addInnerStyle("splitholder", splitterStyle);

        return style;
    }

    /**
     * Getting default style for a Label item. Properly filled in all the necessary
     * properties.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getLabelStyle() {
        Style style = new Style();

        style.font = DefaultsService.getDefaultFont();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = new Color(210, 210, 210);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));

        return style;
    }

    /**
     * Getting default style for a ListArea item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "selection".
     * <p>
     * This is part of many items style.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getListAreaStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));
        style.padding = new Indents(2, 2, 2, 2);
        style.spacing = new Spacing(0, 4);

        Style selectionStyle = getSelectionItemStyle();
        style.addInnerStyle("selection", selectionStyle);

        return style;
    }

    /**
     * Getting default style for a ListBox item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "area", "vscrollbar", "hscrollbar", "menu".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getListBoxStyle() {
        Style style = new Style();

        style.background = new Color(70, 70, 70);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));

        Style vsbStyle = getVerticalScrollBarStyle();
        vsbStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Right, ItemAlignment.Top));
        style.addInnerStyle("vscrollbar", vsbStyle);

        Style hsbStyle = getHorizontalScrollBarStyle();
        hsbStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Bottom));
        style.addInnerStyle("hscrollbar", hsbStyle);

        Style menuStyle = new Style();
        menuStyle.background = new Color(50, 50, 50);
        menuStyle.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        menuStyle.setAlignment(ItemAlignment.Right, ItemAlignment.Bottom);
        style.addInnerStyle("menu", menuStyle);

        Style areaStyle = getListAreaStyle();
        style.addInnerStyle("area", areaStyle);

        return style;
    }

    /**
     * Note: not supported in current version.
     * 
     * @return default style for WContainer objects.
     */
    public static Style getWContainerStyle()// нужен ли?
    {
        Style style = new Style();
        return style;
    }

    /**
     * Getting default style for a RadioButton item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "indicator", "text".
     * <p>
     * Inner styles of "indicator": "marker".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getRadioButtonStyle() {
        Style style = new Style();

        style.background = new Color(80, 80, 80, 255);
        style.foreground = new Color(210, 210, 210);
        style.font = DefaultsService.getDefaultFont();
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Fixed;
        style.height = 20;
        style.minHeight = 20;
        style.minWidth = 20;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        style.border.setRadius(new CornerRadius(10));

        Style indicatorStyle = getIndicatorStyle();
        indicatorStyle.shape = GraphicsMathService.getRoundSquare(10, 20, 20, 0, 0);
        indicatorStyle.isFixedShape = true;
        style.addInnerStyle("indicator", indicatorStyle);

        Style markerStyle = indicatorStyle.getInnerStyle("marker");
        markerStyle.shape = GraphicsMathService.getEllipse(100, 16);
        indicatorStyle.addInnerStyle("marker", markerStyle);

        Style textlineStyle = getLabelStyle();
        textlineStyle.foreground = new Color(210, 210, 210);
        textlineStyle.widthPolicy = SizePolicy.Expand;
        textlineStyle.heightPolicy = SizePolicy.Expand;
        textlineStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter));
        textlineStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        textlineStyle.margin = new Indents(10 + indicatorStyle.width, 0, 0, 0);
        style.addInnerStyle("text", textlineStyle);

        return style;
    }

    /**
     * Getting default style for a PasswordLine item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "showmarker", "textedit".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getPasswordLineStyle() {
        Style style = new Style();
        style.background = new Color(210, 210, 210);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));
        style.height = 30;
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Fixed;
        style.padding = new Indents(5, 0, 5, 0);
        style.spacing = new Spacing(5, 0);

        // ItemState hovered = new ItemState();
        // hovered.background = new Color(255, 255, 255, 30);
        // style.addItemState(ItemStateType.HOVERED, hovered);

        Style markerStyle = getIndicatorStyle().getInnerStyle("marker");
        markerStyle.background = new Color(100, 100, 100, 0);
        markerStyle.setSize(20, 20);
        markerStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        markerStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Right));
        markerStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Left));
        // marker_style.borderRadius = new CornerRadius(5);
        markerStyle.removeItemState(ItemStateType.Hovered);
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
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;

        Style cursorStyle = new Style();
        cursorStyle.background = new Color(60, 60, 60);
        cursorStyle.width = 2;
        cursorStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
        cursorStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Left));
        cursorStyle.margin = new Indents(0, 5, 0, 5);
        cursorStyle.isVisible = false;
        style.addInnerStyle("cursor", cursorStyle);

        Style selectionStyle = new Style();
        selectionStyle.background = new Color(111, 181, 255);
        selectionStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
        selectionStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Left));
        style.addInnerStyle("selection", selectionStyle);

        Style substrateStyle = new Style();
        substrateStyle.font = DefaultsService.getDefaultFont(Font.ITALIC, 14);
        substrateStyle.foreground = new Color(150, 150, 150);
        style.addInnerStyle("substrate", substrateStyle);

        return style;
    }

    /**
     * Getting default style for a TextEdit item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "text".
     * <p>
     * Inner styles for "text": "cursor", "selection", "substrate".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTextEditStyle() {
        Style style = new Style();
        style.font = DefaultsService.getDefaultFont(16);
        style.background = new Color(210, 210, 210);
        style.height = 30;
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Fixed;

        Style textStyle = new Style();
        // textStyle.height = 30; //here
        textStyle.background = new Color(0, 0, 0, 0); //here
        textStyle.foreground = new Color(70, 70, 70);
        textStyle.font = DefaultsService.getDefaultFont(16);
        textStyle.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand); //here
        textStyle.setAlignment(ItemAlignment.Left, ItemAlignment.Top);
        textStyle.setTextAlignment(ItemAlignment.Left, ItemAlignment.VCenter);
        textStyle.padding = new Indents(5, 0, 5, 0);
        style.addInnerStyle("text", textStyle);

        Style cursorStyle = new Style();
        cursorStyle.background = new Color(60, 60, 60);
        cursorStyle.width = 2;
        cursorStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
        cursorStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Left));
        cursorStyle.margin = new Indents(0, 5, 0, 5);
        cursorStyle.isVisible = false;
        textStyle.addInnerStyle("cursor", cursorStyle);

        Style selectionStyle = new Style();
        selectionStyle.background = new Color(111, 181, 255);
        selectionStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
        selectionStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Left));
        selectionStyle.margin = new Indents(0, 5, 0, 5);
        textStyle.addInnerStyle("selection", selectionStyle);

        Style substrateStyle = new Style();
        substrateStyle.font = DefaultsService.getDefaultFont(Font.ITALIC, 14);
        substrateStyle.foreground = new Color(150, 150, 150);
        textStyle.addInnerStyle("substrate", substrateStyle);

        return style;
    }

    private static Style getTextFieldStyle() {
        Style style = new Style();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = new Color(70, 70, 70);
        style.font = DefaultsService.getDefaultFont(16);
        style.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        style.setAlignment(ItemAlignment.Left, ItemAlignment.Top);
        style.setTextAlignment(ItemAlignment.Left, ItemAlignment.VCenter);
        style.padding = new Indents(5, 0, 5, 0);

        Style cursorStyle = new Style();
        cursorStyle.background = new Color(60, 60, 60);
        cursorStyle.width = 2;
        cursorStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
        cursorStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Left));
        cursorStyle.margin = new Indents(0, 5, 0, 5);
        cursorStyle.isVisible = false;
        style.addInnerStyle("cursor", cursorStyle);

        Style selectionStyle = new Style();
        selectionStyle.background = new Color(111, 181, 255);
        selectionStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
        selectionStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Left));
        selectionStyle.margin = new Indents(0, 5, 0, 5);
        style.addInnerStyle("selection", selectionStyle);

        Style substrateStyle = new Style();
        substrateStyle.font = DefaultsService.getDefaultFont(Font.ITALIC, 14);
        substrateStyle.foreground = new Color(150, 150, 150);
        style.addInnerStyle("substrate", substrateStyle);

        return style;
    }

    /**
     * Getting default style for a sealed TextBlock item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "cursor", "selection".
     * <p>
     * This is part of TextArea item style as "textedit".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTextBlockStyle() {
        Style style = new Style();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = new Color(70, 70, 70);
        style.font = DefaultsService.getDefaultFont(16);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.padding = new Indents(5, 5, 5, 5);

        Style cursorStyle = new Style();
        cursorStyle.background = new Color(60, 60, 60);
        cursorStyle.width = 2;
        cursorStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        cursorStyle.isVisible = false;
        style.addInnerStyle("cursor", cursorStyle);

        Style selectionStyle = new Style();
        selectionStyle.background = new Color(111, 181, 255);
        selectionStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        selectionStyle.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Left));
        style.addInnerStyle("selection", selectionStyle);

        return style;
    }

    /**
     * Getting default style for a TextArea item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "textedit", "vscrollbar", "hscrollbar", "menu".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTextAreaStyle() {
        Style style = new Style();
        style.background = new Color(210, 210, 210);
        style.foreground = new Color(70, 70, 70);
        style.font = DefaultsService.getDefaultFont(16);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));

        Style textStyle = getTextBlockStyle();
        style.addInnerStyle("textedit", textStyle);

        Style vsbStyle = getVerticalScrollBarStyle();
        vsbStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Right, ItemAlignment.Top));
        style.addInnerStyle("vscrollbar", vsbStyle);

        Style hsbStyle = getHorizontalScrollBarStyle();
        hsbStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Bottom));
        style.addInnerStyle("hscrollbar", hsbStyle);

        Style menuStyle = new Style();
        menuStyle.background = new Color(50, 50, 50);
        menuStyle.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        menuStyle.setAlignment(ItemAlignment.Right, ItemAlignment.Bottom);
        style.addInnerStyle("menu", menuStyle);

        return style;
    }

    /**
     * Getting default style for a TextView item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "selection".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTextViewStyle() {
        Style style = new Style();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = new Color(210, 210, 210);
        style.font = DefaultsService.getDefaultFont(16);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.setAlignment(ItemAlignment.Left, ItemAlignment.Top);
        style.setPadding(5, 5, 5, 5);

        Style selectionStyle = new Style();
        selectionStyle.background = new Color(255, 255, 255, 40);
        selectionStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        style.addInnerStyle("selection", selectionStyle);

        return style;
    }

    /**
     * Getting default style for a PopUpMessage item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "closebutton".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getPopUpMessageStyle() {
        Style style = new Style();
        style.background = new Color(45, 45, 45, 255);
        style.foreground = new Color(210, 210, 210);
        style.font = DefaultsService.getDefaultFont(14);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Bottom, ItemAlignment.Right));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCenter, ItemAlignment.VCenter));
        style.setSize(300, 70);
        style.widthPolicy = SizePolicy.Fixed;
        style.heightPolicy = SizePolicy.Fixed;
        style.padding = new Indents(5, 5, 5, 5);
        style.margin = new Indents(10, 10, 10, 10);
        style.setShadow(new Shadow(10, new Position(3, 3), new Color(0, 0, 0, 140)));
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 3);
        style.addItemState(ItemStateType.Hovered, hovered);

        Style closeStyle = getButtonCoreStyle();
        closeStyle.background = new Color(100, 100, 100);
        closeStyle.foreground = new Color(210, 210, 210);
        closeStyle.setSize(10, 10);
        closeStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        closeStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Top, ItemAlignment.Right));
        closeStyle.margin = new Indents(0, 5, 0, 5);
        ItemState close_hovered = new ItemState();
        close_hovered.background = new Color(255, 255, 255, 60);
        closeStyle.addItemState(ItemStateType.Hovered, close_hovered);
        closeStyle.shape = GraphicsMathService.getCross(10, 10, 3, 45);
        closeStyle.isFixedShape = false;
        style.addInnerStyle("closebutton", closeStyle);

        return style;
    }

    /**
     * Getting default style for a ProgressBar item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "progressbar".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getProgressBarStyle() {
        Style style = new Style();

        style.font = DefaultsService.getDefaultFont(12);
        style.background = new Color(70, 70, 70);
        style.foreground = new Color(0, 0, 0);
        style.height = 20;
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Fixed;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCenter, ItemAlignment.VCenter));

        Style pgbarStyle = new Style();
        pgbarStyle.background = new Color(0, 191, 255);
        pgbarStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        pgbarStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
        style.addInnerStyle("progressbar", pgbarStyle);

        return style;
    }

    /**
     * Getting default style for a ToolTip item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "text".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getToolTipStyle() {
        Style style = new Style();

        style.font = DefaultsService.getDefaultFont();
        style.background = new Color(255, 255, 255);
        style.foreground = new Color(70, 70, 70);

        style.height = 30;
        style.widthPolicy = SizePolicy.Fixed;
        style.heightPolicy = SizePolicy.Fixed;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        style.padding = new Indents(5, 5, 5, 5);
        style.border.setRadius(new CornerRadius(4));

        Style textStyle = new Style();
        textStyle.background = new Color(0, 0, 0, 0);
        textStyle.widthPolicy = SizePolicy.Expand;
        textStyle.heightPolicy = SizePolicy.Expand;
        textStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.HCenter));
        textStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.HCenter));
        style.addInnerStyle("text", textStyle);

        return style;
    }

    /**
     * Getting default style for a TitleBar item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "closebutton", "minimizebutton", "maximizebutton", "title".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTitleBarStyle() {
        Style style = new Style();

        style.font = DefaultsService.getDefaultFont();
        style.background = new Color(45, 45, 45);
        style.foreground = new Color(180, 180, 180);
        style.height = 30;
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Fixed;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        style.padding = new Indents(10, 0, 5, 0);
        style.spacing = new Spacing(5, 0);

        Style closeStyle = new Style();
        closeStyle.font = DefaultsService.getDefaultFont();
        closeStyle.background = new Color(100, 100, 100);
        closeStyle.foreground = new Color(0, 0, 0, 0);
        closeStyle.setSize(15, 15);
        closeStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        closeStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Right));
        closeStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        ItemState closeHovered = new ItemState();
        closeHovered.background = new Color(186, 95, 97, 255);
        closeStyle.addItemState(ItemStateType.Hovered, closeHovered);

        closeStyle.shape = GraphicsMathService.getCross(15, 15, 2, 45);
        closeStyle.isFixedShape = true;
        style.addInnerStyle("closebutton", closeStyle);

        Style minimizeStyle = new Style();
        minimizeStyle.font = DefaultsService.getDefaultFont();
        minimizeStyle.background = new Color(100, 100, 100);
        minimizeStyle.foreground = new Color(0, 0, 0, 0);
        minimizeStyle.setSize(12, 15);
        minimizeStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        minimizeStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Bottom, ItemAlignment.Right));
        minimizeStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        minimizeStyle.margin = new Indents(0, 0, 5, 9);

        ItemState minimizeHovered = new ItemState();
        minimizeHovered.background = new Color(255, 255, 255, 80);
        minimizeStyle.addItemState(ItemStateType.Hovered, minimizeHovered);

        minimizeStyle.shape = GraphicsMathService.getRectangle(15, 2, 0, 13);
        minimizeStyle.isFixedShape = true;
        style.addInnerStyle("minimizebutton", minimizeStyle);

        Style maximizeStyle = new Style();
        maximizeStyle.font = DefaultsService.getDefaultFont();
        maximizeStyle.background = new Color(0, 0, 0, 0);

        maximizeStyle.border.setThickness(2);
        maximizeStyle.border.setColor(new Color(100, 100, 100));

        maximizeStyle.foreground = new Color(0, 0, 0, 0);
        maximizeStyle.setSize(12, 12);
        maximizeStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        maximizeStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Bottom, ItemAlignment.Right));
        maximizeStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCenter, ItemAlignment.VCenter));
        maximizeStyle.margin = new Indents(0, 0, 0, 9);
        maximizeStyle.padding = new Indents(0, 0, 0, 0);

        ItemState maximizeHovered = new ItemState();
        maximizeHovered.background = new Color(0, 0, 0, 0);
        maximizeHovered.border.setColor(new Color(84, 124, 94));

        maximizeStyle.addItemState(ItemStateType.Hovered, maximizeHovered);
        style.addInnerStyle("maximizebutton", maximizeStyle);

        Style titleStyle = new Style();
        titleStyle.margin = new Indents(10, 0, 0, 0);
        style.addInnerStyle("title", titleStyle);

        return style;
    }

    /**
     * Getting default style for a TreeView item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "area", "vscrollbar", "hscrollbar", "menu".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTreeViewStyle() {
        Style style = getListBoxStyle();
        return style;
    }

    /**
     * Getting default style for a TreeItem item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "indicator", "branchicon", "leaficon".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTreeItemStyle() {
        Style style = new Style();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = new Color(210, 210, 210);
        style.font = DefaultsService.getDefaultFont();
        style.setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
        style.height = 25;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Left));
        style.spacing = new Spacing(5, 0);
        style.padding = new Indents(5, 0, 0, 0);
        style.margin = new Indents(0, 0, 0, 0);
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 30);
        style.addItemState(ItemStateType.Hovered, hovered);

        Style indicatorStyle = new Style();
        indicatorStyle.background = new Color(32, 32, 32);
        indicatorStyle.foreground = new Color(210, 210, 210);
        indicatorStyle.font = DefaultsService.getDefaultFont();
        indicatorStyle.setSize(15, 15);
        indicatorStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        indicatorStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.VCenter));
        indicatorStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Left));
        indicatorStyle.shape = GraphicsMathService.getTriangle(10, 8, 0, 3, 90);
        indicatorStyle.isFixedShape = true;
        ItemState toggled = new ItemState();
        toggled.background = new Color(160, 160, 160);
        toggled.shape = new Figure(true, GraphicsMathService.getTriangle(10, 8, 0, 3, 180));
        indicatorStyle.addItemState(ItemStateType.Toggled, toggled);
        style.addInnerStyle("indicator", indicatorStyle);

        Style branchIconStyle = new Style();
        branchIconStyle.background = new Color(106, 185, 255);
        branchIconStyle.setSize(14, 9);
        branchIconStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        branchIconStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Left));
        branchIconStyle.shape = GraphicsMathService.getFolderIconShape(20, 15, 0, 0);
        style.addInnerStyle("branchicon", branchIconStyle);

        Style leafIconStyle = new Style();
        leafIconStyle.background = new Color(129, 187, 133);
        leafIconStyle.setSize(6, 6);
        leafIconStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        leafIconStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Left));
        leafIconStyle.shape = GraphicsMathService.getEllipse(3, 16);
        leafIconStyle.margin = new Indents(2, 0, 0, 0);
        style.addInnerStyle("leaficon", leafIconStyle);

        return style;
    }

    /**
     * Getting default style for a SpinItem item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "uparrow", "downarrow", "buttonsarea", "textedit".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getSpinItemStyle() {

        Style style = new Style();
        style.background = new Color(210, 210, 210);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Fixed;
        style.height = 30;
        style.minHeight = 10;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));

        Style uparrowButtonStyle = getButtonCoreStyle();
        uparrowButtonStyle.widthPolicy = SizePolicy.Expand;
        uparrowButtonStyle.heightPolicy = SizePolicy.Expand;
        uparrowButtonStyle.background = new Color(255, 181, 111); // new Color(50, 50, 50, 255);
        uparrowButtonStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Top, ItemAlignment.HCenter));
        uparrowButtonStyle.isFixedShape = true;

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        uparrowButtonStyle.addItemState(ItemStateType.Hovered, hovered);

        style.addInnerStyle("uparrowbutton", uparrowButtonStyle);

        Style upArrowStyle = new Style();
        upArrowStyle.width = 10;
        upArrowStyle.height = 6;
        upArrowStyle.widthPolicy = SizePolicy.Fixed;
        upArrowStyle.heightPolicy = SizePolicy.Fixed;
        upArrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCenter, ItemAlignment.VCenter));
        upArrowStyle.background = new Color(50, 50, 50);
        upArrowStyle.shape = GraphicsMathService.getTriangle(100, 100, 0, 0, 0);
        style.addInnerStyle("uparrow", upArrowStyle);

        Style downarrowButtonStyle = getButtonCoreStyle();
        downarrowButtonStyle.widthPolicy = SizePolicy.Expand;
        downarrowButtonStyle.heightPolicy = SizePolicy.Expand;
        downarrowButtonStyle.background = new Color(255, 181, 111); // new Color(50, 50, 50, 255);
        downarrowButtonStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Bottom, ItemAlignment.HCenter));
        downarrowButtonStyle.isFixedShape = true;
        downarrowButtonStyle.addItemState(ItemStateType.Hovered, hovered);
        style.addInnerStyle("downarrowbutton", downarrowButtonStyle);

        Style downArrowStyle = new Style();
        downArrowStyle.width = 10;
        downArrowStyle.height = 6;
        downArrowStyle.widthPolicy = SizePolicy.Fixed;
        downArrowStyle.heightPolicy = SizePolicy.Fixed;
        downArrowStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCenter, ItemAlignment.VCenter));
        downArrowStyle.background = new Color(50, 50, 50);
        downArrowStyle.shape = GraphicsMathService.getTriangle(100, 100, 0, 0, 180);
        style.addInnerStyle("downarrow", downArrowStyle);

        Style btnsArea = getVerticalStackStyle();
        btnsArea.widthPolicy = SizePolicy.Fixed;
        btnsArea.heightPolicy = SizePolicy.Expand;
        btnsArea.width = 20;
        // btnsArea.background = new Color(210, 210, 210); //new Color(255, 181, 111);
        btnsArea.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCenter, ItemAlignment.Right));
        style.addInnerStyle("buttonsarea", btnsArea);

        Style textInput = getTextFieldStyle();
        textInput.background = new Color(210, 210, 210);
        textInput.heightPolicy = SizePolicy.Expand;
        textInput.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.Right));
        textInput.padding.right = 10;
        style.addInnerStyle("textedit", textInput);

        return style;
    }

    /**
     * Getting default style for a DialogItem item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "window".
     * <p>
     * This is part of OpenEntryDialog item style.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getDialogItemStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        style.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        style.setBackground(0, 0, 0, 150);

        Style windowStyle = getFrameStyle();
        windowStyle.setSize(300, 150);
        windowStyle.setMinSize(300, 150);
        windowStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        windowStyle.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        windowStyle.setPadding(2, 2, 2, 2);
        windowStyle.setBackground(45, 45, 45);
        windowStyle.setShadow(new Shadow(5, new Position(3, 3), new Color(0, 0, 0, 180)));
        style.addInnerStyle("window", windowStyle);

        return style;
    }

    /**
     * Getting default style for a MessageItem item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "window", "button", "toolbar", "userbar", "message", "layout".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getMessageItemStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        style.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        style.setBackground(0, 0, 0, 150);
        style.padding = new Indents();
        style.margin = new Indents();
        style.spacing = new Spacing();

        Style windowStyle = getFrameStyle();
        windowStyle.setSize(300, 150);
        windowStyle.setMinSize(300, 150);
        windowStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        windowStyle.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        windowStyle.setPadding(2, 2, 2, 2);
        windowStyle.setBackground(45, 45, 45);
        style.addInnerStyle("window", windowStyle);

        Style btnStyle = getButtonCoreStyle();
        btnStyle.setBackground(100, 255, 150);
        btnStyle.setSize(100, 30);
        btnStyle.setAlignment(ItemAlignment.Left, ItemAlignment.VCenter);
        btnStyle.setShadow(new Shadow(5, new Position(2, 2), new Color(0, 0, 0, 120)));
        style.addInnerStyle("button", btnStyle);

        Style toolbarStyle = getHorizontalStackStyle();
        toolbarStyle.setAlignment(ItemAlignment.HCenter, ItemAlignment.Bottom);
        toolbarStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        toolbarStyle.setSpacing(10, 0);
        toolbarStyle.setPadding(0, 0, 0, 0);
        toolbarStyle.setMargin(0, 0, 10, 0);
        style.addInnerStyle("toolbar", toolbarStyle);

        Style userbarStyle = getHorizontalStackStyle();
        userbarStyle.setAlignment(ItemAlignment.Left, ItemAlignment.Bottom);
        userbarStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        userbarStyle.setSpacing(10, 0);
        userbarStyle.setPadding(0, 0, 0, 0);
        userbarStyle.setMargin(25, 0, 30, 0);
        style.addInnerStyle("userbar", userbarStyle);

        Style msgStyle = getLabelStyle();
        msgStyle.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        msgStyle.setTextAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
        msgStyle.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
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
     * Getting default style for a window itself. Properly filled in all the
     * necessary properties.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getWindowContainerStyle() {
        Style style = new Style();

        style.setBackground(45, 45, 45);
        style.setMinSize(200, 200);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.setPadding(2, 2, 2, 2);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));

        return style;
    }

    /**
     * Getting default style for a FileSystemEntry item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "icon", "text".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getFileSystemEntryStyle() {
        Style style = new Style();
        style.height = 25;
        style.setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
        style.setBackground(0, 0, 0, 0);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));
        style.setTextAlignment(ItemAlignment.Left, ItemAlignment.VCenter);
        style.font = DefaultsService.getDefaultFont();
        style.setForeground(210, 210, 210);
        style.setPadding(10, 0, 0, 0);
        style.addItemState(ItemStateType.Hovered, new ItemState(new Color(255, 255, 255, 30)));

        Style iconStyle = getFrameStyle();
        iconStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        iconStyle.setAlignment(ItemAlignment.VCenter, ItemAlignment.Left);

        style.addInnerStyle("icon", iconStyle);

        Style textStyle = new Style();
        textStyle.setMargin(24, 0, 0, 0);

        style.addInnerStyle("text", textStyle);

        return style;
    }

    /**
     * Getting default style for a OpenEntryDialog item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "window", "layout", "toolbar", "toolbarbutton", "addressline",
     * "filenameline", "list", "controlpanel", "okbutton", "cancelbutton", "filter",
     * "filtertext", "divider".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getOpenEntryDialogStyle() {
        // common
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        style.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        style.setBackground(0, 0, 0, 150);
        style.padding = new Indents();
        style.margin = new Indents();
        style.spacing = new Spacing();

        // window
        Style windowStyle = getDialogItemStyle().getInnerStyle("window");
        windowStyle.setSize(500, 700);
        windowStyle.setMinSize(400, 400);
        windowStyle.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
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
        toolbarStyle.heightPolicy = SizePolicy.Fixed;
        toolbarStyle.height = 30;
        toolbarStyle.setBackground(40, 40, 40);
        toolbarStyle.setSpacing(3, 0);
        toolbarStyle.setPadding(6, 0, 0, 0);
        style.addInnerStyle("toolbar", toolbarStyle);
        // toolbarbutton
        Style toolbarbuttonStyle = Style.getButtonCoreStyle();
        toolbarbuttonStyle.setSize(24, 30);
        toolbarbuttonStyle.background = toolbarStyle.background;
        toolbarbuttonStyle.addItemState(ItemStateType.Hovered, new ItemState(new Color(255, 255, 255, 40)));
        toolbarbuttonStyle.addItemState(ItemStateType.Pressed, new ItemState(new Color(255, 255, 255, 30)));

        toolbarbuttonStyle.setPadding(3, 6, 3, 6);
        style.addInnerStyle("toolbarbutton", toolbarbuttonStyle);
        // buttonhidden
        Style buttonhiddenStyle = getButtonToggleStyle();
        buttonhiddenStyle.setSize(24, 30);
        buttonhiddenStyle.background = toolbarStyle.background;
        buttonhiddenStyle.setPadding(4, 6, 4, 6);
        buttonhiddenStyle.addItemState(ItemStateType.Hovered, new ItemState(new Color(255, 255, 255, 40)));
        buttonhiddenStyle.addItemState(ItemStateType.Toggled, new ItemState(new Color(30, 153, 91)));
        style.addInnerStyle("buttonhidden", buttonhiddenStyle);
        // addressline
        Style addresslineStyle = getTextEditStyle();
        addresslineStyle.font = DefaultsService.getDefaultFont(12);
        addresslineStyle.getInnerStyle("text").font = DefaultsService.getDefaultFont(12);
        addresslineStyle.getInnerStyle("text").setForeground(210, 210, 210);
        addresslineStyle.getInnerStyle("text").getInnerStyle("cursor").setBackground(10, 132, 232);
        addresslineStyle.getInnerStyle("text").getInnerStyle("selection").setBackground(0, 162, 232, 60);
        addresslineStyle.setBackground(50, 50, 50);
        addresslineStyle.height = 24;
        addresslineStyle.setMargin(0, 5, 0, 0);
        style.addInnerStyle("addressline", addresslineStyle);
        // filenameline
        Style filenamelineStyle = getTextEditStyle();
        filenamelineStyle.font = DefaultsService.getDefaultFont(12);
        filenamelineStyle.getInnerStyle("text").font = DefaultsService.getDefaultFont(12);
        filenamelineStyle.getInnerStyle("text").setForeground(210, 210, 210);
        filenamelineStyle.getInnerStyle("text").getInnerStyle("cursor").setBackground(10, 132, 232);
        filenamelineStyle.getInnerStyle("text").getInnerStyle("selection").setBackground(0, 162, 232, 60);
        filenamelineStyle.setBackground(50, 50, 50);
        filenamelineStyle.height = 24;
        filenamelineStyle.setMargin(0, 2, 0, 0);
        style.addInnerStyle("filenameline", filenamelineStyle);
        // list
        Style listStyle = getListBoxStyle();
        style.addInnerStyle("list", listStyle);
        // controlpanel
        Style controlpanelStyle = getFrameStyle();
        controlpanelStyle.heightPolicy = SizePolicy.Fixed;
        controlpanelStyle.height = 45;
        controlpanelStyle.setBackground(45, 45, 45);
        controlpanelStyle.setPadding(6, 6, 6, 6);
        style.addInnerStyle("controlpanel", controlpanelStyle);
        // button
        Style okbuttonStyle = getButtonCoreStyle();
        okbuttonStyle.setSize(100, 30);
        okbuttonStyle.setAlignment(ItemAlignment.VCenter, ItemAlignment.Right);
        okbuttonStyle.setMargin(0, 0, 110, 0);
        okbuttonStyle.setShadow(new Shadow(5, new Position(2, 2), new Color(0, 0, 0, 180)));
        style.addInnerStyle("okbutton", okbuttonStyle);

        Style cancelbuttonStyle = getButtonCoreStyle();
        cancelbuttonStyle.setSize(100, 30);
        cancelbuttonStyle.setAlignment(ItemAlignment.VCenter, ItemAlignment.Right);
        cancelbuttonStyle.setShadow(new Shadow(5, new Position(2, 2), new Color(0, 0, 0, 180)));
        style.addInnerStyle("cancelbutton", cancelbuttonStyle);

        Style filterStyle = getButtonCoreStyle();
        filterStyle.setSize(24, 30);
        filterStyle.setBackground(35, 35, 35);
        filterStyle.setPadding(4, 6, 4, 6);
        filterStyle.setMargin(5, 0, 0, 0);
        filterStyle.addItemState(ItemStateType.Hovered, new ItemState(new Color(255, 255, 255, 40)));
        filterStyle.addItemState(ItemStateType.Pressed, new ItemState(new Color(255, 255, 255, 30)));
        style.addInnerStyle("filter", filterStyle);

        Style filtertextStyle = getLabelStyle();
        filtertextStyle.widthPolicy = SizePolicy.Fixed;
        filtertextStyle.setTextAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
        filtertextStyle.setPadding(10, 2, 10, 0);
        filtertextStyle.setMargin(-3, 0, 0, 0);
        filtertextStyle.setBackground(55, 55, 55);
        filtertextStyle.font = DefaultsService.getDefaultFont();
        style.addInnerStyle("filtertext", filtertextStyle);

        Style dividerStyle = getFrameStyle();
        dividerStyle.widthPolicy = SizePolicy.Fixed;
        dividerStyle.width = 1;
        dividerStyle.setBackground(55, 55, 55);
        dividerStyle.setMargin(0, 3, 0, 3);
        style.addInnerStyle("divider", dividerStyle);

        return style;
    }

    /**
     * Getting default style for a InputDialog item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "window", "button", "textedit", "layout", "toolbar".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getInputDialogStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        style.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        style.setBackground(0, 0, 0, 150);
        style.padding = new Indents();
        style.margin = new Indents();
        style.spacing = new Spacing();

        Style windowStyle = getFrameStyle();
        windowStyle.setSize(300, 150);
        windowStyle.setMinSize(300, 150);
        windowStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        windowStyle.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        windowStyle.setPadding(2, 2, 2, 2);
        windowStyle.setBackground(45, 45, 45);
        style.addInnerStyle("window", windowStyle);

        Style btnStyle = getButtonCoreStyle();
        btnStyle.setBackground(100, 255, 150);
        btnStyle.foreground = Color.black;
        btnStyle.setSize(100, 30);
        btnStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        btnStyle.setAlignment(ItemAlignment.Left, ItemAlignment.Bottom);
        btnStyle.setMargin(0, 0, 0, 0);
        btnStyle.setShadow(new Shadow(5, new Position(2, 2), new Color(0, 0, 0, 120)));
        btnStyle.addItemState(ItemStateType.Hovered, new ItemState(new Color(255, 255, 255, 80)));
        style.addInnerStyle("button", btnStyle);

        Style textStyle = getTextEditStyle();
        textStyle.setAlignment(ItemAlignment.HCenter, ItemAlignment.Top);
        textStyle.setTextAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
        textStyle.setMargin(0, 15, 0, 0);
        style.addInnerStyle("textedit", textStyle);

        Style layoutStyle = getFrameStyle();
        layoutStyle.setMargin(0, 30, 0, 0);
        layoutStyle.setPadding(6, 6, 6, 15);
        layoutStyle.setBackground(255, 255, 255, 20);
        style.addInnerStyle("layout", layoutStyle);

        Style toolbarStyle = getHorizontalStackStyle();
        toolbarStyle.setAlignment(ItemAlignment.HCenter, ItemAlignment.Bottom);
        toolbarStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        toolbarStyle.setSpacing(10, 0);
        style.addInnerStyle("toolbar", toolbarStyle);

        return style;
    }

    /**
     * Getting default style for a SelectionItem item. Properly filled in all the
     * necessary properties.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getSelectionItemStyle() {
        Style style = new Style();
        style.setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
        style.setBackground(0, 0, 0, 0);
        style.setPadding(0, 1, 0, 1);
        style.setAlignment(ItemAlignment.Left, ItemAlignment.Top);
        style.addItemState(ItemStateType.Toggled, new ItemState(new Color(255, 255, 255, 50)));
        return style;
    }

    /**
     * Getting default style for a WrapArea item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "selection".
     * <p>
     * This is part of WrapGrid item style as "area".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getWrapAreaStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));
        style.padding = new Indents(2, 2, 2, 2);
        style.spacing = new Spacing(0, 5);

        Style selectionStyle = getSelectionItemStyle();
        style.addInnerStyle("selection", selectionStyle);

        return style;
    }

    /**
     * Getting default style for a WrapGrid item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "area", "vscrollbar", "hscrollbar".
     * <p>
     * Inner styles for "area": see Style.getWrapAreaStyle().
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getWrapGridStyle() {
        Style style = new Style();

        style.background = new Color(70, 70, 70);
        style.widthPolicy = SizePolicy.Expand;
        style.heightPolicy = SizePolicy.Expand;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Top));

        Style vsbStyle = getVerticalScrollBarStyle();
        vsbStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Right, ItemAlignment.Top));
        style.addInnerStyle("vscrollbar", vsbStyle);

        Style hsbStyle = getHorizontalScrollBarStyle();
        hsbStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Left, ItemAlignment.Bottom));
        style.addInnerStyle("hscrollbar", hsbStyle);

        Style areaStyle = getWrapAreaStyle();
        style.addInnerStyle("area", areaStyle);

        return style;
    }

    /**
     * Getting default style for a SideArea item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "window", "closebutton".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getSideAreaStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        style.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        style.setBackground(0, 0, 0, 130);
        style.padding = new Indents();
        style.margin = new Indents();
        style.spacing = new Spacing();

        Style windowStyle = getFrameStyle();
        windowStyle.setPadding(2, 2, 2, 2);
        windowStyle.setBackground(40, 40, 40);
        windowStyle.setAlignment(ItemAlignment.Top, ItemAlignment.Left);
        style.addInnerStyle("window", windowStyle);

        Style closeStyle = new Style();
        closeStyle.setMargin(0, 5, 0, 0);
        closeStyle.font = DefaultsService.getDefaultFont();
        closeStyle.background = new Color(100, 100, 100);
        closeStyle.foreground = new Color(0, 0, 0, 0);
        closeStyle.setSize(15, 15);
        closeStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        closeStyle.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.Top, ItemAlignment.Right));
        closeStyle.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.Right, ItemAlignment.Top));
        ItemState close_hovered = new ItemState();
        close_hovered.background = new Color(186, 95, 97, 255);
        closeStyle.addItemState(ItemStateType.Hovered, close_hovered);

        closeStyle.shape = GraphicsMathService.getCross(15, 15, 2, 45);
        closeStyle.isFixedShape = true;
        style.addInnerStyle("closebutton", closeStyle);

        return style;
    }

    /**
     * Getting default style for a ImageItem item. Properly filled in all the
     * necessary properties.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getImageItemStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        style.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        style.setBackground(0, 0, 0, 0);
        return style;
    }

    /**
     * Getting default style for a LoadingScreen item. Properly filled in all the
     * necessary properties.
     * <p>
     * Inner styles: "text", "image".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getLoadingScreenStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
        style.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        style.setBackground(0, 0, 0, 150);

        Style textStyle = getLabelStyle();
        textStyle.setAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
        textStyle.setTextAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
        textStyle.font = DefaultsService.getDefaultFont(Font.BOLD, 14);
        style.addInnerStyle("text", textStyle);

        Style imageStyle = getImageItemStyle();
        imageStyle.setMaxSize(64, 64);
        style.addInnerStyle("image", imageStyle);

        return style;
    }

    /**
     * Getting default style for a Tab item. Properly filled in all the necessary
     * properties.
     * <p>
     * Inner styles: "text", "closebutton", "view".
     * <p>
     * This is part of TabView item style as "tab".
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTabStyle() {
        Style style = new Style();
        style.border.setRadius(new CornerRadius(3, 3, 0, 0));
        style.font = DefaultsService.getDefaultFont(14);
        style.background = new Color(255, 255, 255, 10);
        // style.background = new Color(60, 60, 60);
        style.setForeground(210, 210, 210);
        style.minWidth = 30;
        style.setSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
        style.setTextAlignment(ItemAlignment.Left, ItemAlignment.VCenter);
        style.padding = new Indents(0, 0, 0, 0);
        style.padding = new Indents(10, 2, 5, 2);
        style.spacing = new Spacing(5, 0);
        style.addItemState(ItemStateType.Hovered, new ItemState(new Color(255, 255, 255, 60)));
        style.addItemState(ItemStateType.Toggled, new ItemState(new Color(255, 255, 255, 25)));
        style.setShadow(new Shadow(5, new Color(0, 0, 0, 150)));
        style.shadow.setApplied(false);

        Style textStyle = getLabelStyle();
        style.addInnerStyle("text", textStyle);

        Style closeStyle = new Style();
        closeStyle.setBackground(100, 100, 100);
        closeStyle.setSize(10, 10);
        closeStyle.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        closeStyle.setAlignment(ItemAlignment.VCenter, ItemAlignment.Right);
        closeStyle.addItemState(ItemStateType.Hovered, new ItemState(new Color(0, 162, 232)));
        closeStyle.shape = GraphicsMathService.getCross(10, 10, 2, 45);
        closeStyle.isFixedShape = true;
        style.addInnerStyle("closebutton", closeStyle);

        Style viewStyle = new Style();
        viewStyle.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        viewStyle.background = new Color(71, 71, 71);
        viewStyle.isVisible = false;
        viewStyle.padding = new Indents(2, 2, 2, 2);
        style.addInnerStyle("view", viewStyle);

        return style;
    }

    /**
     * Getting default style for a TabBar item. Properly filled in all the necessary
     * properties.
     * <p>
     * This is part of TabView item style.
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTabBarStyle() {
        Style style = getHorizontalStackStyle();
        style.setSpacing(1, 0);
        return style;
    }

    /**
     * Getting default style for a *** item. Properly filled in all the necessary
     * properties.
     * <p>
     * Inner styles: "tabbar", "tab", "viewarea".
     * <p>
     * Inner styles for "tab": see Style.GetTabStyle().
     * 
     * @return Style as com.spvessel.spacevil.Decorations.Style.
     */
    public static Style getTabViewStyle() {
        Style style = getVerticalStackStyle();
        style.background = new Color(50, 50, 50);

        Style tabBarStyle = getTabBarStyle();
        tabBarStyle.setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
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
