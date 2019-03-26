package com.spvessel.spacevil.Decorations;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.GraphicsMathService;
import com.spvessel.spacevil.SelectionItem;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Flags.GeometryEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

public class Style implements Cloneable {
    /**
     * A class that describes style settings of the object
     */
    private Map<String, Style> _inner_styles = new HashMap<>();

    // defaults values
    public Color background;
    public Color foreground;
    public Font font = null;
    public SizePolicy widthPolicy;
    public SizePolicy heightPolicy;
    public int width;
    public int minWidth;
    public int maxWidth;
    public int height;
    public int minHeight;
    public int maxHeight;
    public List<ItemAlignment> alignment;
    public List<ItemAlignment> textAlignment;
    public int x;
    public int y;

    private Map<ItemStateType, ItemState> _item_states = new HashMap<>();

    public Indents padding = new Indents();
    public Spacing spacing = new Spacing();
    public Indents margin = new Indents();
    public CornerRadius borderRadius = null;
    public int borderThickness = 0;
    public Color borderFill = new Color(0, 0, 0, 0);
    public List<float[]> shape;// = new List<float[]>();
    public List<InterfaceBaseItem> innerShapes;// = new List<float[]>();
    public boolean isFixedShape = false;
    public boolean isVisible;

    /**
     * Constructs a default Style
     */
    public Style()// default
    {
        isVisible = true;
        maxWidth = 65535;
        maxHeight = 65535;
        setAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
    }

    /**
     * Sets objects style
     */
    public void setStyle(InterfaceBaseItem... items) {
        for (InterfaceBaseItem item : items)
            item.setStyle(this);
    }

    /**
     * Sets object size (width and height)
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Sets object minimum size (width and height)
     */
    public void setMinSize(int width, int height) {
        minWidth = width;
        minHeight = height;
    }

    /**
     * Sets object maximum size (width and height)
     */
    public void setMaxSize(int width, int height) {
        maxWidth = width;
        maxHeight = height;
    }

    /**
     * Sets object size policy (fixed or expand) for width and height
     */
    public void setSizePolicy(SizePolicy width_policy, SizePolicy height_policy) {
        widthPolicy = width_policy;
        heightPolicy = height_policy;
    }

    /**
     * Sets background color of the object
     */
    public void setBackground(int r, int g, int b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        background = new Color(r, g, b, 255);
    }

    /**
     * Sets background color of the object
     */
    public void setBackground(int r, int g, int b, int a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        background = new Color(r, g, b, a);
    }

    /**
     * Sets background color of the object
     */
    public void setBackground(float r, float g, float b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        background = new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), 255);
    }

    /**
     * Sets background color of the object
     */
    public void setBackground(float r, float g, float b, float a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        background = new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), 255);
    }

    /**
     * Sets text color of the object
     */
    public void setForeground(int r, int g, int b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;

        foreground = new Color(r, g, b, 255);
    }

    /**
     * Sets text color of the object
     */
    public void setForeground(int r, int g, int b, int a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 255)
            r = 255;
        if (g < 0)
            g = Math.abs(g);
        if (g > 255)
            g = 255;
        if (b < 0)
            b = Math.abs(b);
        if (b > 255)
            b = 255;
        foreground = new Color(r, g, b, a);
    }

    /**
     * Sets text color of the object
     */
    public void setForeground(float r, float g, float b) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        foreground = new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), 255);
    }

    /**
     * Sets text color of the object
     */
    public void setForeground(float r, float g, float b, float a) {
        if (r < 0)
            r = Math.abs(r);
        if (r > 1.0f)
            r = 1.0f;
        if (g < 0)
            g = Math.abs(g);
        if (g > 1.0f)
            g = 1.0f;
        if (b < 0)
            b = Math.abs(b);
        if (b > 1.0f)
            b = 1.0f;
        foreground = new Color((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    /**
     * Padding of the object
     */
    public void setPadding(Indents padding) {
        this.padding = padding;
    }

    /**
     * Padding of the object
     */
    public void setPadding(int left, int top, int right, int bottom) {
        padding.left = left;
        padding.top = top;
        padding.right = right;
        padding.bottom = bottom;
    }

    /**
     * Margin of the object
     */
    public void setMargin(Indents margin) {
        this.margin = margin;
    }

    /**
     * Margin of the object
     */
    public void setMargin(int left, int top, int right, int bottom) {
        margin.left = left;
        margin.top = top;
        margin.right = right;
        margin.bottom = bottom;
    }

    /**
     * Spacing of the object
     */
    public void setSpacing(Spacing spacing) {
        this.spacing = spacing;
    }

    /**
     * Spacing of the object
     */
    public void setSpacing(int horizontal, int vertical) {
        spacing.horizontal = horizontal;
        spacing.vertical = vertical;
    }

    /**
     * Set border of the object
     */
    public void setBorder(Border border) {
        borderFill = border.getFill();
        borderRadius = border.getRadius();
        borderThickness = border.getThickness();
    }

    /**
     * Set border of the object
     * 
     * @param fill      border color
     * @param radius    radius of the border corners
     * @param thickness border thickness
     */
    public void setBorder(Color fill, CornerRadius radius, int thickness) {
        borderFill = fill;
        borderRadius = radius;
        borderThickness = thickness;
    }

    /**
     * Set object alignment
     */
    public void setAlignment(ItemAlignment... alignment) {
        List<ItemAlignment> list = Arrays.stream(alignment).collect(Collectors.toList());
        this.alignment = list;
    }

    /**
     * Set text alignment in the object
     */
    public void setTextAlignment(ItemAlignment... alignment) {
        List<ItemAlignment> list = Arrays.stream(alignment).collect(Collectors.toList());
        this.textAlignment = list;
    }

    ////////////////////////////////////////////////////////////////

    /**
     * Add inner primitives to the object (as decorations only) note: not supported
     * in the current version
     */
    public void addInnerShape(InterfaceBaseItem shape) {
        if (innerShapes == null)
            innerShapes = new LinkedList<>();
        innerShapes.add(shape);
    }

    /**
     * Set style for the child of the object
     */
    public void addInnerStyle(String item_name, Style style) {
        if (_inner_styles.containsKey(item_name))
            _inner_styles.replace(item_name, style);
        else
            _inner_styles.put(item_name, style);
    }

    /**
     * Returns style of the object's child by name
     */
    public Style getInnerStyle(String item_name) {
        if (_inner_styles.containsKey(item_name))
            return _inner_styles.get(item_name);

        return null;
    }

    /**
     * Object changes its state according to the ItemState when ItemStateType
     * happens
     */
    public void addItemState(ItemStateType type, ItemState state) {
        if (_item_states.containsKey(type)) {
            state.value = true;
            _item_states.replace(type, state);
        } else {
            _item_states.put(type, state);
        }
    }

    /**
     * Returns ItemState of the object by ItemStateType name
     */
    public ItemState getState(ItemStateType type) {
        if (_item_states.containsKey(type))
            return _item_states.get(type);
        return null;
    }

    /**
     * @return Map of the ItemStateTypes and its ItemStates
     */
    public Map<ItemStateType, ItemState> getAllStates() {
        return _item_states;
    }

    /**
     * Remove ItemState by the ItemStateType
     */
    public void removeItemState(ItemStateType type) {
        if (type == ItemStateType.BASE)
            return;
        if (_item_states.containsKey(type))
            _item_states.remove(type);
    }

    /**
     * Remove the object's child style by name of the child
     */
    public void removeInnerStyle(String item_name) {
        if (_inner_styles.containsKey(item_name))
            _inner_styles.remove(item_name);
        else
            return;
    }

    public Style clone() {
        Style style = new Style();

        if (background != null)
            style.background = new Color(background.getRed(), background.getGreen(), background.getBlue(),
                    background.getAlpha());
        if (foreground != null)
            style.foreground = new Color(foreground.getRed(), foreground.getGreen(), foreground.getBlue(),
                    foreground.getAlpha());
        if (font != null)
            style.font = font.deriveFont(font.getStyle());
        else
            style.font = DefaultsService.getDefaultFont();
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
        if (padding != null)
            style.setPadding(padding.left, padding.top, padding.right, padding.bottom);
        if (margin != null)
            style.setMargin(margin.left, margin.top, margin.right, margin.bottom);
        if (spacing != null)
            style.setSpacing(spacing.horizontal, spacing.vertical);

        if (borderFill != null)
            style.borderFill = new Color(borderFill.getRed(), borderFill.getGreen(), borderFill.getBlue(),
                    borderFill.getAlpha());
        style.borderThickness = borderThickness;
        if (borderRadius != null)
            style.borderRadius = new CornerRadius(borderRadius.leftTop, borderRadius.rightTop, borderRadius.leftBottom,
                    borderRadius.rightBottom);
        if (shape != null)
            style.shape = new LinkedList<>(shape);
        if (innerShapes != null)
            style.innerShapes = new LinkedList<>(innerShapes);
        style.isFixedShape = isFixedShape;
        style.isVisible = isVisible;
        style._item_states = new HashMap<>(_item_states);

        return style;
    }

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
     * @return default style for ButtonCore objects
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
     * @return default style for ButtonToggle objects
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
     * @return default style for CheckBox objects
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

        Style indicator_style = getIndicatorStyle();
        style.addInnerStyle("indicator", indicator_style);

        Style textline_style = getLabelStyle();
        textline_style.foreground = new Color(210, 210, 210);
        textline_style.widthPolicy = SizePolicy.EXPAND;
        textline_style.heightPolicy = SizePolicy.EXPAND;
        textline_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER));
        textline_style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        textline_style.margin = new Indents(10 + indicator_style.width, 0, 0, 0);
        style.addInnerStyle("textline", textline_style);

        return style;
    }

    /**
     * @return default style for Indicator objects
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

        Style marker_style = new Style();
        marker_style.background = new Color(32, 32, 32);
        marker_style.foreground = new Color(70, 70, 70);
        marker_style.font = DefaultsService.getDefaultFont();
        marker_style.widthPolicy = SizePolicy.EXPAND;
        marker_style.heightPolicy = SizePolicy.EXPAND;
        marker_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        marker_style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 60);
        marker_style.addItemState(ItemStateType.HOVERED, hovered);

        ItemState toggled = new ItemState();
        toggled.background = new Color(255, 181, 111);
        marker_style.addItemState(ItemStateType.TOGGLED, toggled);

        style.addInnerStyle("marker", marker_style);

        return style;
    }

    /**
     * @return default style for TextLine objects
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
     * @return default style for ComboBox objects
     */
    public static Style getComboBoxStyle() {
        Style style = new Style();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = new Color(70, 70, 70);
        style.font = DefaultsService.getDefaultFont();
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.width = 10;
        style.height = 30;
        style.minHeight = 10;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));

        Style selection_style = new Style();
        selection_style.background = new Color(220, 220, 220);
        selection_style.foreground = new Color(70, 70, 70);

        selection_style.font = DefaultsService.getDefaultFont(14);
        selection_style.widthPolicy = SizePolicy.EXPAND;
        selection_style.heightPolicy = SizePolicy.EXPAND;
        selection_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        selection_style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        selection_style.padding = new Indents(10, 0, 0, 0);
        selection_style.margin = new Indents(0, 0, 20, 0);
        style.addInnerStyle("selection", selection_style);

        Style dropdownbutton_style = getButtonCoreStyle();
        dropdownbutton_style.borderRadius = new CornerRadius();
        dropdownbutton_style.width = 20;
        dropdownbutton_style.widthPolicy = SizePolicy.FIXED;
        dropdownbutton_style.heightPolicy = SizePolicy.EXPAND;
        dropdownbutton_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.VCENTER));
        dropdownbutton_style.background = new Color(255, 181, 111);

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 40);
        dropdownbutton_style.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("dropdownbutton", dropdownbutton_style);

        Style arrow_style = new Style();
        arrow_style.width = 14;
        arrow_style.height = 6;
        arrow_style.widthPolicy = SizePolicy.FIXED;
        arrow_style.heightPolicy = SizePolicy.FIXED;
        arrow_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        arrow_style.background = new Color(50, 50, 50);
        arrow_style.shape = GraphicsMathService.getTriangle(100, 100, 0, 0, 180);
        style.addInnerStyle("arrow", arrow_style);

        return style;
    }

    /**
     * @return default style for ComboBoxDropDown objects
     */
    public static Style getComboBoxDropDownStyle() {
        Style style = new Style();
        style.background = Color.white;
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.padding = new Indents(0, 0, 0, 0);

        Style itemlist_style = getListBoxStyle();
        itemlist_style.background = new Color(0, 0, 0, 0);
        itemlist_style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.addInnerStyle("itemlist", itemlist_style);

        Style vsb_style = getSimpleVerticalScrollBarStyle();
        vsb_style.setAlignment(ItemAlignment.RIGHT, ItemAlignment.TOP);
        itemlist_style.addInnerStyle("vscrollbar", vsb_style);

        Style hsb_style = getHorizontalScrollBarStyle();
        hsb_style.setAlignment(ItemAlignment.LEFT, ItemAlignment.BOTTOM);
        itemlist_style.addInnerStyle("hscrollbar", hsb_style);

        Style menu_style = new Style();
        menu_style.background = new Color(50, 50, 50);
        menu_style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        menu_style.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
        itemlist_style.addInnerStyle("menu", menu_style);

        Style area_style = getListAreaStyle();
        style.addInnerStyle("listarea", area_style);

        return style;
    }

    /**
     * @return default style for MenuItem objects
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

        style.addItemState(ItemStateType.HOVERED, new ItemState(new Color(150, 150, 150)));

        Style text_style = new Style();
        text_style.setMargin(0, 0, 0, 0);
        style.addInnerStyle("text", text_style);

        Style arrow_style = new Style();
        arrow_style.width = 6;
        arrow_style.height = 10;
        arrow_style.widthPolicy = SizePolicy.FIXED;
        arrow_style.heightPolicy = SizePolicy.FIXED;
        arrow_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.VCENTER));
        arrow_style.background = new Color(80, 80, 80);
        arrow_style.margin = new Indents(10, 0, 0, 0);
        arrow_style.shape = GraphicsMathService.getTriangle(100, 100, 0, 0, 90);
        style.addInnerStyle("arrow", arrow_style);

        return style;
    }

    /**
     * @return default style for ContextMenu objects
     */
    public static Style getContextMenuStyle() {
        Style style = new Style();
        style.background = new Color(210, 210, 210);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.padding = new Indents(0, 0, 0, 0);

        Style itemlist_style = new Style();
        itemlist_style.background = new Color(0, 0, 0, 0);
        itemlist_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        style.addInnerStyle("itemlist", itemlist_style);

        Style area_style = getListAreaStyle();
        area_style.setPadding(0, 0, 0, 0);
        style.addInnerStyle("listarea", area_style);

        return style;
    }

    /**
     * @return default style for FreeArea objects
     */
    public static Style getFreeAreaStyle() {
        Style style = new Style();

        // style.background = new Color(0,0,0,0);
        style.background = new Color(255, 70, 70, 70);

        style.padding = new Indents(2, 2, 2, 2);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        return style;
    }

    /**
     * @return default style for Frame objects
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
     * @return default style for Grid objects
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
     * @return default style for HorizontalScrollBar objects
     */
    public static Style getHorizontalScrollBarStyle() {
        Style style = new Style();

        style.background = new Color(50, 50, 50);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.height = 16;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));

        Style uparrow_style = getButtonCoreStyle();
        uparrow_style.widthPolicy = SizePolicy.FIXED;
        uparrow_style.heightPolicy = SizePolicy.FIXED;
        uparrow_style.background = new Color(100, 100, 100, 255);
        uparrow_style.width = 16;
        uparrow_style.height = 16;
        uparrow_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        uparrow_style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        uparrow_style.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, -90);
        uparrow_style.isFixedShape = true;

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 40);
        uparrow_style.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("uparrow", uparrow_style);

        Style downarrow_style = getButtonCoreStyle();
        downarrow_style.widthPolicy = SizePolicy.FIXED;
        downarrow_style.heightPolicy = SizePolicy.FIXED;
        downarrow_style.background = new Color(100, 100, 100, 255);
        downarrow_style.width = 16;
        downarrow_style.height = 16;
        downarrow_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.VCENTER));
        downarrow_style.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, 90);
        downarrow_style.isFixedShape = true;
        downarrow_style.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("downarrow", downarrow_style);

        Style slider_style = new Style();
        slider_style.widthPolicy = SizePolicy.EXPAND;
        slider_style.heightPolicy = SizePolicy.EXPAND;
        slider_style.background = new Color(0, 0, 0, 0);
        slider_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        style.addInnerStyle("slider", slider_style);

        Style track_style = new Style();
        track_style.widthPolicy = SizePolicy.EXPAND;
        track_style.heightPolicy = SizePolicy.EXPAND;
        track_style.background = new Color(0, 0, 0, 0);
        track_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        slider_style.addInnerStyle("track", track_style);

        Style handler_style = new Style();
        handler_style.widthPolicy = SizePolicy.FIXED;
        handler_style.heightPolicy = SizePolicy.EXPAND;
        handler_style.background = new Color(100, 100, 100, 255);
        handler_style.margin = new Indents(0, 3, 0, 3);
        handler_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        handler_style.addItemState(ItemStateType.HOVERED, hovered);
        handler_style.minWidth = 15;
        slider_style.addInnerStyle("handler", handler_style);

        return style;
    }

    /**
     * @return default simple style for HorizontalScrollBar objects
     */
    public static Style getSimpleHorizontalScrollBarStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.padding = new Indents(2, 0, 2, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.height = 16;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));

        Style uparrow_style = getButtonCoreStyle();
        uparrow_style.isVisible = false;
        style.addInnerStyle("uparrow", uparrow_style);

        Style downarrow_style = getButtonCoreStyle();
        downarrow_style.isVisible = false;
        style.addInnerStyle("downarrow", downarrow_style);

        Style slider_style = new Style();
        slider_style.widthPolicy = SizePolicy.EXPAND;
        slider_style.heightPolicy = SizePolicy.EXPAND;
        slider_style.background = new Color(0, 0, 0, 0);
        slider_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        style.addInnerStyle("slider", slider_style);

        Style track_style = new Style();
        track_style.widthPolicy = SizePolicy.EXPAND;
        track_style.heightPolicy = SizePolicy.EXPAND;
        track_style.background = new Color(0, 0, 0, 0);
        track_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        slider_style.addInnerStyle("track", track_style);

        Style handler_style = new Style();
        handler_style.widthPolicy = SizePolicy.FIXED;
        handler_style.heightPolicy = SizePolicy.EXPAND;
        handler_style.background = new Color(120, 120, 120, 255);
        handler_style.margin = new Indents(0, 5, 0, 5);
        handler_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        handler_style.borderRadius = new CornerRadius(3);
        handler_style.minWidth = 15;
        slider_style.addInnerStyle("handler", handler_style);

        return style;
    }

    /**
     * @return default style for VerticalScrollBar objects
     */
    public static Style getVerticalScrollBarStyle() {
        Style style = new Style();

        style.background = new Color(50, 50, 50);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.EXPAND;
        style.width = 16;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));

        Style uparrow_style = getButtonCoreStyle();
        uparrow_style.widthPolicy = SizePolicy.FIXED;
        uparrow_style.heightPolicy = SizePolicy.FIXED;
        uparrow_style.background = new Color(100, 100, 100, 255);
        uparrow_style.width = 16;
        uparrow_style.height = 16;
        uparrow_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        uparrow_style.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, 0);
        uparrow_style.isFixedShape = true;

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 40);
        uparrow_style.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("uparrow", uparrow_style);

        Style downarrow_style = getButtonCoreStyle();
        downarrow_style.widthPolicy = SizePolicy.FIXED;
        downarrow_style.heightPolicy = SizePolicy.FIXED;
        downarrow_style.background = new Color(100, 100, 100, 255);
        downarrow_style.width = 16;
        downarrow_style.height = 16;
        downarrow_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.HCENTER));
        downarrow_style.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, 180);
        downarrow_style.isFixedShape = true;
        downarrow_style.addItemState(ItemStateType.HOVERED, hovered);
        style.addInnerStyle("downarrow", downarrow_style);

        Style slider_style = new Style();
        slider_style.widthPolicy = SizePolicy.EXPAND;
        slider_style.heightPolicy = SizePolicy.EXPAND;
        slider_style.background = new Color(0, 0, 0, 0);
        slider_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        style.addInnerStyle("slider", slider_style);

        Style track_style = new Style();
        track_style.widthPolicy = SizePolicy.EXPAND;
        track_style.heightPolicy = SizePolicy.EXPAND;
        track_style.background = new Color(0, 0, 0, 0);
        track_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        slider_style.addInnerStyle("track", track_style);

        Style handler_style = new Style();
        handler_style.widthPolicy = SizePolicy.EXPAND;
        handler_style.heightPolicy = SizePolicy.FIXED;
        handler_style.background = new Color(100, 100, 100, 255);
        handler_style.margin = new Indents(3, 0, 3, 0);
        handler_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        handler_style.minHeight = 15;
        handler_style.addItemState(ItemStateType.HOVERED, hovered);
        slider_style.addInnerStyle("handler", handler_style);

        return style;
    }

    /**
     * @return default simple style for VerticalScrollBar objects
     */
    public static Style getSimpleVerticalScrollBarStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.padding = new Indents(0, 2, 0, 2);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.EXPAND;
        style.width = 16;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));

        Style uparrow_style = getButtonCoreStyle();
        uparrow_style.isVisible = false;
        style.addInnerStyle("uparrow", uparrow_style);

        Style downarrow_style = getButtonCoreStyle();
        downarrow_style.isVisible = false;
        style.addInnerStyle("downarrow", downarrow_style);

        Style slider_style = new Style();
        slider_style.widthPolicy = SizePolicy.EXPAND;
        slider_style.heightPolicy = SizePolicy.EXPAND;
        slider_style.background = new Color(0, 0, 0, 0);
        slider_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        style.addInnerStyle("slider", slider_style);

        Style track_style = new Style();
        track_style.widthPolicy = SizePolicy.EXPAND;
        track_style.heightPolicy = SizePolicy.EXPAND;
        track_style.background = new Color(0, 0, 0, 0);
        track_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        slider_style.addInnerStyle("track", track_style);

        Style handler_style = new Style();
        handler_style.widthPolicy = SizePolicy.EXPAND;
        handler_style.heightPolicy = SizePolicy.FIXED;
        handler_style.background = new Color(120, 120, 120, 255);
        handler_style.margin = new Indents(5, 0, 5, 0);
        handler_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        handler_style.borderRadius = new CornerRadius(3);
        handler_style.minHeight = 15;
        slider_style.addInnerStyle("handler", handler_style);

        return style;
    }

    /**
     * @return default style for HorizontalSlider objects
     */
    public static Style getHorizontalSliderStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        style.height = 25;

        Style track_style = new Style();
        track_style.widthPolicy = SizePolicy.EXPAND;
        track_style.heightPolicy = SizePolicy.FIXED;
        track_style.height = 5;
        track_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER));
        track_style.background = new Color(100, 100, 100);
        style.addInnerStyle("track", track_style);

        Style handler_style = new Style();
        handler_style.widthPolicy = SizePolicy.FIXED;
        handler_style.heightPolicy = SizePolicy.EXPAND;
        handler_style.width = 10;
        handler_style.background = new Color(255, 181, 111);
        handler_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT));

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        handler_style.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("handler", handler_style);

        return style;
    }

    /**
     * @return default style for VerticalSlider objects
     */
    public static Style getVerticalSliderStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        style.width = 25;

        Style track_style = new Style();
        track_style.widthPolicy = SizePolicy.FIXED;
        track_style.heightPolicy = SizePolicy.EXPAND;
        track_style.width = 5;
        track_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER));
        track_style.background = new Color(100, 100, 100);
        style.addInnerStyle("track", track_style);

        Style handler_style = new Style();
        handler_style.widthPolicy = SizePolicy.EXPAND;
        handler_style.heightPolicy = SizePolicy.FIXED;
        handler_style.height = 10;
        handler_style.background = new Color(255, 181, 111);
        handler_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP));
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        handler_style.addItemState(ItemStateType.HOVERED, hovered);
        style.addInnerStyle("handler", handler_style);

        return style;
    }

    /**
     * @return default style for HorizontalStack objects
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
     * @return default style for VerticalStack objects
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
     * @return default style for HorizontalSplitArea objects
     */
    public static Style getHorizontalSplitAreaStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style splitter_style = new Style();
        splitter_style.background = new Color(42, 42, 42);
        splitter_style.width = 6;
        style.addInnerStyle("splitholder", splitter_style);

        return style;
    }

    /**
     * @return default style for VerticalSplitArea objects
     */
    public static Style getVerticalSplitAreaStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style splitter_style = new Style();
        splitter_style.background = new Color(42, 42, 42);
        splitter_style.height = 6;
        style.addInnerStyle("splitholder", splitter_style);

        return style;
    }

    /**
     * @return default style for Label objects
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
     * @return default style for ListArea objects
     */
    public static Style getListAreaStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.padding = new Indents(2, 2, 2, 2);
        style.spacing = new Spacing(0, 5);

        Style selection_style = getSelectionItemStyle();
        style.addInnerStyle("selection", selection_style);

        return style;
    }

    /**
     * @return default style for ListBox objects
     */
    public static Style getListBoxStyle() {
        Style style = new Style();

        style.background = new Color(70, 70, 70);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style vsb_style = getVerticalScrollBarStyle();
        vsb_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.TOP));
        style.addInnerStyle("vscrollbar", vsb_style);

        Style hsb_style = getHorizontalScrollBarStyle();
        hsb_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.BOTTOM));
        style.addInnerStyle("hscrollbar", hsb_style);

        Style menu_style = new Style();
        menu_style.background = new Color(50, 50, 50);
        menu_style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        menu_style.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
        style.addInnerStyle("menu", menu_style);

        Style area_style = getListAreaStyle();
        style.addInnerStyle("area", area_style);

        return style;
    }

    /**
     * @return default style for WContainer objects note: not supported in current
     *         version
     */
    public static Style getWContainerStyle()// нужен ли?
    {
        Style style = new Style();
        return style;
    }

    /**
     * @return default style for RadioButton objects
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

        Style indicator_style = getIndicatorStyle();
        indicator_style.shape = GraphicsMathService.getRoundSquare(20, 20, 10, 0, 0);
        indicator_style.isFixedShape = true;
        style.addInnerStyle("indicator", indicator_style);

        Style marker_style = indicator_style.getInnerStyle("marker");
        marker_style.shape = GraphicsMathService.getEllipse(100, 16);
        indicator_style.addInnerStyle("marker", marker_style);

        Style textline_style = getLabelStyle();
        textline_style.foreground = new Color(210, 210, 210);
        textline_style.widthPolicy = SizePolicy.EXPAND;
        textline_style.heightPolicy = SizePolicy.EXPAND;
        textline_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER));
        textline_style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        textline_style.margin = new Indents(10 + indicator_style.width, 0, 0, 0);
        style.addInnerStyle("textline", textline_style);

        return style;
    }

    /**
     * @return default style for PasswordLine objects
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

        Style marker_style = getIndicatorStyle().getInnerStyle("marker");
        marker_style.background = new Color(100, 100, 100);
        marker_style.setSize(16, 16);
        marker_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        marker_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.RIGHT));
        marker_style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        marker_style.borderRadius = new CornerRadius(5);
        ItemState toggled = new ItemState();
        toggled.background = new Color(40, 40, 40, 255);
        marker_style.addItemState(ItemStateType.TOGGLED, toggled);
        marker_style.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 50)));
        style.addInnerStyle("showmarker", marker_style);
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

        Style cursor_style = new Style();
        cursor_style.background = new Color(60, 60, 60);
        cursor_style.width = 2;
        cursor_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        cursor_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        cursor_style.margin = new Indents(0, 5, 0, 5);
        cursor_style.isVisible = false;
        style.addInnerStyle("cursor", cursor_style);

        Style selection_style = new Style();
        selection_style.background = new Color(111, 181, 255);
        selection_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        selection_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        style.addInnerStyle("selection", selection_style);

        Style substrate_style = new Style();
        substrate_style.font = DefaultsService.getDefaultFont(Font.ITALIC, 14);
        substrate_style.foreground = new Color(150, 150, 150);
        style.addInnerStyle("substrate", substrate_style);

        return style;
    }

    /**
     * @return default style for TextEdit objects
     */
    public static Style getTextEditStyle() {
        Style style = new Style();
        style.background = new Color(210, 210, 210);
        style.foreground = new Color(25, 25, 25);
        style.font = DefaultsService.getDefaultFont(16);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.height = 30;
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.padding = new Indents(5, 0, 5, 0);
        // ItemState hovered = new ItemState();
        // hovered.background = new Color(220, 220, 220, 255);
        // style.addItemState(ItemStateType.HOVERED, hovered);
        // ItemState focused = new ItemState();
        // focused.background = new Color(220, 220, 220, 255);
        // focused.border = new Border(new Color(58,156,209), new CornerRadius(), 2);
        // style.addItemState(ItemStateType.FOCUSED, focused);

        Style cursor_style = new Style();
        cursor_style.background = new Color(60, 60, 60);
        cursor_style.width = 2;
        cursor_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        cursor_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        cursor_style.margin = new Indents(0, 5, 0, 5);
        cursor_style.isVisible = false;
        style.addInnerStyle("cursor", cursor_style);

        Style selection_style = new Style();
        selection_style.background = new Color(111, 181, 255);
        selection_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        selection_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        selection_style.margin = new Indents(0, 5, 0, 5);
        style.addInnerStyle("selection", selection_style);

        Style substrate_style = new Style();
        substrate_style.font = DefaultsService.getDefaultFont(Font.ITALIC, 14);
        substrate_style.foreground = new Color(150, 150, 150);
        style.addInnerStyle("substrate", substrate_style);

        return style;
    }

    /**
     * @return default style for TextBlock objects
     */
    public static Style getTextBlockStyle() {
        Style style = new Style();
        style.background = new Color(210, 210, 210, 0);
        style.foreground = new Color(25, 25, 25);
        style.font = DefaultsService.getDefaultFont(16);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.padding = new Indents(5, 5, 5, 5);
        // ItemState hovered = new ItemState();
        // hovered.background = new Color(220, 220, 220, 255);
        // style.addItemState(ItemStateType.HOVERED, hovered);
        // ItemState focused = new ItemState();
        // focused.background = new Color(220, 220, 220, 255);
        // style.addItemState(ItemStateType.FOCUSED, focused);

        Style cursor_style = new Style();
        cursor_style.background = new Color(60, 60, 60);
        cursor_style.width = 2;
        cursor_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        // cursor_style.alignment = new
        // LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.VCENTER,
        // ItemAlignment.LEFT));
        cursor_style.margin = new Indents(0, 5, 0, 5);
        cursor_style.isVisible = false;
        style.addInnerStyle("cursor", cursor_style);

        Style selection_style = new Style();
        selection_style.background = new Color(111, 181, 255);
        selection_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        // selection_style.alignment = new
        // LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.VCENTER,
        // ItemAlignment.LEFT));
        style.addInnerStyle("selection", selection_style);

        return style;
    }

    /**
     * @return default style for TextArea objects
     */
    public static Style getTextAreaStyle() {
        Style style = new Style();
        style.background = new Color(210, 210, 210);
        style.foreground = new Color(25, 25, 25);
        style.font = DefaultsService.getDefaultFont(16);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style text_style = getTextBlockStyle();
        style.addInnerStyle("textedit", text_style);

        Style vsb_style = getVerticalScrollBarStyle();
        vsb_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.TOP));
        style.addInnerStyle("vscrollbar", vsb_style);

        Style hsb_style = getHorizontalScrollBarStyle();
        hsb_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.BOTTOM));
        style.addInnerStyle("hscrollbar", hsb_style);

        Style menu_style = new Style();
        menu_style.background = new Color(50, 50, 50);
        menu_style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        menu_style.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
        style.addInnerStyle("menu", menu_style);

        return style;
    }

    /**
     * @return default style for PopUpMessage objects
     */
    public static Style getPopUpMessageStyle() {
        Style style = new Style();
        style.background = new Color(45, 45, 45, 255);
        style.foreground = new Color(210, 210, 210);
        style.font = DefaultsService.getDefaultFont(16);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.RIGHT));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        style.setSize(300, 70);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.padding = new Indents(5, 5, 5, 5);
        style.margin = new Indents(10, 10, 10, 10);
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 10);
        style.addItemState(ItemStateType.HOVERED, hovered);

        Style close_style = getButtonCoreStyle();
        close_style.background = new Color(100, 100, 100);
        close_style.foreground = new Color(210, 210, 210);
        close_style.setSize(10, 10);
        close_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        close_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.RIGHT));
        close_style.margin = new Indents(0, 5, 0, 5);
        ItemState close_hovered = new ItemState();
        close_hovered.background = new Color(255, 255, 255, 60);
        close_style.addItemState(ItemStateType.HOVERED, close_hovered);
        close_style.shape = GraphicsMathService.getCross(10, 10, 3, 45);
        close_style.isFixedShape = false;
        style.addInnerStyle("closebutton", close_style);

        return style;
    }

    /**
     * @return default style for ProgressBar objects
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

        Style pgbar_style = new Style();
        pgbar_style.background = new Color(0, 191, 255);
        pgbar_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        pgbar_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        style.addInnerStyle("progressbar", pgbar_style);

        return style;
    }

    /**
     * @return default style for ToolTip objects
     */
    public static Style getToolTipStyle() {
        Style style = new Style();

        style.font = DefaultsService.getDefaultFont();
        style.background = new Color(255, 255, 255);
        style.foreground = new Color(70, 70, 70);
        ;
        style.height = 30;
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.padding = new Indents(5, 0, 5, 0);
        style.borderRadius = new CornerRadius(4);

        return style;
    }

    /**
     * @return default style for TitleBar objects
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

        Style close_style = new Style();
        close_style.font = DefaultsService.getDefaultFont();
        close_style.background = new Color(100, 100, 100);
        close_style.foreground = new Color(0, 0, 0, 0);
        close_style.setSize(15, 15);
        close_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        close_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.RIGHT));
        close_style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        ItemState close_hovered = new ItemState();
        close_hovered.background = new Color(186, 95, 97, 255);
        close_style.addItemState(ItemStateType.HOVERED, close_hovered);

        close_style.shape = GraphicsMathService.getCross(15, 15, 2, 45);
        close_style.isFixedShape = true;
        style.addInnerStyle("closebutton", close_style);

        Style minimize_style = new Style();
        minimize_style.font = DefaultsService.getDefaultFont();
        minimize_style.background = new Color(100, 100, 100);
        minimize_style.foreground = new Color(0, 0, 0, 0);
        minimize_style.setSize(12, 15);
        minimize_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        minimize_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.RIGHT));
        minimize_style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        minimize_style.margin = new Indents(0, 0, 5, 9);

        ItemState minimize_hovered = new ItemState();
        minimize_hovered.background = new Color(255, 255, 255, 80);
        minimize_style.addItemState(ItemStateType.HOVERED, minimize_hovered);

        minimize_style.shape = GraphicsMathService.getRectangle(15, 2, 0, 13);
        minimize_style.isFixedShape = true;
        style.addInnerStyle("minimizebutton", minimize_style);

        Style maximize_style = new Style();
        maximize_style.font = DefaultsService.getDefaultFont();
        maximize_style.background = new Color(0, 0, 0, 0);

        maximize_style.borderThickness = 2;
        maximize_style.borderFill = new Color(100, 100, 100);

        maximize_style.foreground = new Color(0, 0, 0, 0);
        maximize_style.setSize(12, 12);
        maximize_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        maximize_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.RIGHT));
        maximize_style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        maximize_style.margin = new Indents(0, 0, 0, 9);
        maximize_style.padding = new Indents(0, 0, 0, 0);

        ItemState maximize_hovered = new ItemState();
        maximize_hovered.background = new Color(0, 0, 0, 0);
        maximize_hovered.border.setFill(new Color(84, 124, 94));

        maximize_style.addItemState(ItemStateType.HOVERED, maximize_hovered);
        style.addInnerStyle("maximizebutton", maximize_style);

        Style title_style = new Style();
        title_style.margin = new Indents(10, 0, 0, 0);
        style.addInnerStyle("title", title_style);

        return style;
    }

    /**
     * @return default style for TabView objects
     */
    public static Style getTabViewStyle() {
        Style style = new Style();

        style.background = new Color(50, 50, 50);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        style.spacing = new Spacing(0, 0);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style view_style = new Style();
        view_style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        view_style.background = new Color(71, 71, 71);
        view_style.isVisible = false;
        view_style.padding = new Indents(2, 2, 2, 2);
        view_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        view_style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.HCENTER));
        style.addInnerStyle("tabview", view_style);

        Style tab_style = new Style();
        tab_style.font = DefaultsService.getDefaultFont(14);
        tab_style.background = new Color(0, 0, 0, 0);
        tab_style.foreground = new Color(210, 210, 210);
        tab_style.width = 100;
        tab_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        tab_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        tab_style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        tab_style.padding = new Indents(2, 2, 2, 2);

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        tab_style.addItemState(ItemStateType.HOVERED, hovered);
        ItemState toggled = new ItemState();
        toggled.background = new Color(71, 71, 71);
        tab_style.addItemState(ItemStateType.TOGGLED, toggled);

        style.addInnerStyle("tab", tab_style);

        return style;
    }

    /**
     * @return default style for TreeView objects
     */
    public static Style getTreeViewStyle() {
        Style style = getListBoxStyle();
        return style;
    }

    /**
     * @return default style for TreeItem objects
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
        hovered.background = new Color(255, 255, 255, 130);
        style.addItemState(ItemStateType.HOVERED, hovered);

        Style indicator_style = new Style();
        indicator_style.background = new Color(32, 32, 32);
        indicator_style.foreground = new Color(210, 210, 210);
        indicator_style.font = DefaultsService.getDefaultFont();
        indicator_style.setSize(15, 15);
        indicator_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        indicator_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        indicator_style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        indicator_style.shape = GraphicsMathService.getTriangle(10, 8, 0, 3, 90);
        indicator_style.isFixedShape = true;
        ItemState toggled = new ItemState();
        toggled.background = new Color(160, 160, 160);
        toggled.shape = new CustomFigure(true, GraphicsMathService.getTriangle(10, 8, 0, 3, 180));
        indicator_style.addItemState(ItemStateType.TOGGLED, toggled);
        style.addInnerStyle("indicator", indicator_style);

        Style branch_icon_style = new Style();
        branch_icon_style.background = new Color(106, 185, 255);
        branch_icon_style.setSize(14, 9);
        branch_icon_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        branch_icon_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        branch_icon_style.shape = GraphicsMathService.getFolderIconShape(20, 15, 0, 0);
        style.addInnerStyle("branchicon", branch_icon_style);

        Style leaf_icon_style = new Style();
        leaf_icon_style.background = new Color(129, 187, 133);
        leaf_icon_style.setSize(6, 6);
        leaf_icon_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        leaf_icon_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        leaf_icon_style.shape = GraphicsMathService.getEllipse(3, 16);
        leaf_icon_style.margin = new Indents(2, 0, 0, 0);
        style.addInnerStyle("leaficon", leaf_icon_style);

        return style;
    }

    /**
     * @return default style for SpinItem objects
     */
    public static Style getSpinItemStyle() {
        Style style = new Style();

        style.background = new Color(50, 50, 50);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.height = 30;
        style.minHeight = 10;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style uparrow_style = getButtonCoreStyle();
        uparrow_style.widthPolicy = SizePolicy.EXPAND;
        uparrow_style.heightPolicy = SizePolicy.EXPAND;
        uparrow_style.setMargin(4, 4, 4, 5);
        uparrow_style.background = new Color(50, 50, 50, 255);
        uparrow_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        uparrow_style.shape = GraphicsMathService.getTriangle(12, 6, 0, 0, 0);
        uparrow_style.isFixedShape = true;

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        uparrow_style.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("uparrow", uparrow_style);

        Style downarrow_style = getButtonCoreStyle();
        downarrow_style.widthPolicy = SizePolicy.EXPAND;
        downarrow_style.heightPolicy = SizePolicy.EXPAND;
        downarrow_style.setMargin(4, 5, 4, 4);
        downarrow_style.background = new Color(50, 50, 50, 255);
        downarrow_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.HCENTER));
        downarrow_style.shape = GraphicsMathService.getTriangle(12, 6, 0, 0, 180);
        downarrow_style.isFixedShape = true;
        downarrow_style.addItemState(ItemStateType.HOVERED, hovered);
        style.addInnerStyle("downarrow", downarrow_style);

        Style btns_area = getVerticalStackStyle();
        btns_area.widthPolicy = SizePolicy.FIXED;
        btns_area.heightPolicy = SizePolicy.EXPAND;
        btns_area.width = 20;
        btns_area.background = new Color(255, 181, 111);
        btns_area.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.RIGHT));
        style.addInnerStyle("buttonsarea", btns_area);

        Style text_input = getTextEditStyle();
        text_input.heightPolicy = SizePolicy.EXPAND;
        text_input.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT));
        style.addInnerStyle("textedit", text_input);

        return style;
    }

    public static Style getDialogItemStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        style.setBackground(0, 0, 0, 150);
        style.borderRadius = new CornerRadius(0);
        style.padding = new Indents();
        style.margin = new Indents();
        style.spacing = new Spacing();

        Style window_style = getFrameStyle();
        window_style.setSize(300, 150);
        window_style.setMinSize(300, 150);
        window_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        window_style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        window_style.setPadding(2, 2, 2, 2);
        window_style.setBackground(45, 45, 45);

        style.addInnerStyle("window", window_style);

        return style;
    }

    public static Style getMessageItemStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        style.setBackground(0, 0, 0, 150);
        style.borderRadius = new CornerRadius();
        style.padding = new Indents();
        style.margin = new Indents();
        style.spacing = new Spacing();

        Style window_style = getFrameStyle();
        window_style.setSize(300, 150);
        window_style.setMinSize(300, 150);
        window_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        window_style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        window_style.setPadding(2, 2, 2, 2);
        window_style.setBackground(45, 45, 45);
        style.addInnerStyle("window", window_style);

        Style ok_style = getButtonCoreStyle();
        ok_style.setBackground(100, 255, 150);
        ok_style.setSize(100, 30);
        ok_style.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        style.addInnerStyle("button", ok_style);

        Style toolbar_style = getHorizontalStackStyle();
        toolbar_style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.BOTTOM);
        toolbar_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        toolbar_style.setSpacing(10, 0);
        toolbar_style.setPadding(0, 0, 0, 0);
        toolbar_style.setMargin(0, 0, 0, 0);
        style.addInnerStyle("toolbar", toolbar_style);

        Style userbar_style = getHorizontalStackStyle();
        userbar_style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.BOTTOM);
        userbar_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        userbar_style.setSpacing(10, 0);
        userbar_style.setPadding(0, 0, 0, 0);
        userbar_style.setMargin(0, 0, 0, 0);
        style.addInnerStyle("userbar", userbar_style);

        Style msg_style = getLabelStyle();
        msg_style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        msg_style.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
        msg_style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        msg_style.setMargin(10, 0, 10, 40);
        style.addInnerStyle("message", msg_style);

        Style layout_style = getFrameStyle();
        layout_style.setMargin(0, 30, 0, 0);
        layout_style.setPadding(6, 6, 6, 6);
        layout_style.setBackground(255, 255, 255, 20);
        style.addInnerStyle("layout", layout_style);

        return style;
    }

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
        style.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 130)));

        Style icon_style = getFrameStyle();
        icon_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        icon_style.setAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);

        style.addInnerStyle("icon", icon_style);

        Style text_style = new Style();
        text_style.setMargin(24, 0, 0, 0);

        style.addInnerStyle("text", text_style);

        return style;
    }

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
        Style window_style = getDialogItemStyle().getInnerStyle("window");
        window_style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        window_style.setMargin(150, 20, 150, 20);
        style.addInnerStyle("window", window_style);
        // layout
        Style layout_style = getVerticalStackStyle();
        layout_style.setMargin(0, 30, 0, 0);
        layout_style.setPadding(6, 6, 6, 6);
        layout_style.setSpacing(0, 2);
        layout_style.setBackground(255, 255, 255, 20);
        style.addInnerStyle("layout", layout_style);
        // toolbar
        Style toolbar_style = getHorizontalStackStyle();
        toolbar_style.heightPolicy = SizePolicy.FIXED;
        toolbar_style.height = 30;
        toolbar_style.setBackground(40, 40, 40);
        toolbar_style.setSpacing(3, 0);
        toolbar_style.setPadding(6, 0, 0, 0);
        style.addInnerStyle("toolbar", toolbar_style);
        // toolbarbutton
        Style toolbarbutton_style = Style.getButtonCoreStyle();
        toolbarbutton_style.setSize(24, 30);
        toolbarbutton_style.background = toolbar_style.background;
        toolbarbutton_style.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 60)));
        toolbarbutton_style.addItemState(ItemStateType.PRESSED, new ItemState(new Color(255, 255, 255, 30)));
        toolbarbutton_style.borderRadius = new CornerRadius();
        toolbarbutton_style.setPadding(3, 6, 3, 6);
        style.addInnerStyle("toolbarbutton", toolbarbutton_style);
        // buttonhidden
        Style buttonhidden_style = getButtonToggleStyle();
        buttonhidden_style.setSize(24, 30);
        buttonhidden_style.borderRadius = new CornerRadius();
        buttonhidden_style.background = toolbar_style.background;
        buttonhidden_style.setPadding(4, 6, 4, 6);
        buttonhidden_style.addItemState(ItemStateType.TOGGLED, new ItemState(new Color(30, 153, 91)));
        style.addInnerStyle("buttonhidden", buttonhidden_style);
        // addressline
        Style addressline_style = getTextEditStyle();
        addressline_style.font = DefaultsService.getDefaultFont(12);
        addressline_style.setForeground(210, 210, 210);
        addressline_style.setBackground(50, 50, 50);
        addressline_style.height = 24;
        addressline_style.setMargin(0, 5, 0, 0);
        style.addInnerStyle("addressline", addressline_style);
        // filenameline
        Style filenameline_style = getTextEditStyle();
        filenameline_style.font = DefaultsService.getDefaultFont(12);
        filenameline_style.setForeground(210, 210, 210);
        filenameline_style.setBackground(50, 50, 50);
        filenameline_style.height = 24;
        filenameline_style.setMargin(0, 2, 0, 0);
        style.addInnerStyle("filenameline", filenameline_style);
        // list
        Style list_style = getListBoxStyle();
        style.addInnerStyle("list", list_style);
        // controlpanel
        Style controlpanel_style = getFrameStyle();
        controlpanel_style.heightPolicy = SizePolicy.FIXED;
        controlpanel_style.height = 40;
        controlpanel_style.setBackground(45, 45, 45);
        controlpanel_style.setPadding(6, 6, 6, 6);
        style.addInnerStyle("controlpanel", controlpanel_style);
        // button
        Style okbutton_style = getButtonCoreStyle();
        okbutton_style.setSize(100, 30);
        okbutton_style.setAlignment(ItemAlignment.VCENTER, ItemAlignment.RIGHT);
        okbutton_style.setMargin(0, 0, 105, 0);
        okbutton_style.borderRadius = new CornerRadius();
        style.addInnerStyle("okbutton", okbutton_style);

        Style cancelbutton_style = getButtonCoreStyle();
        cancelbutton_style.setSize(100, 30);
        cancelbutton_style.setAlignment(ItemAlignment.VCENTER, ItemAlignment.RIGHT);
        cancelbutton_style.borderRadius = new CornerRadius();
        style.addInnerStyle("cancelbutton", cancelbutton_style);

        Style filter_style = getButtonCoreStyle();
        filter_style.setSize(24, 30);
        filter_style.setBackground(35, 35, 35);
        filter_style.setPadding(4, 6, 4, 6);
        filter_style.setMargin(5, 0, 0, 0);
        style.addInnerStyle("filter", filter_style);

        Style filtertext_style = getLabelStyle();
        filtertext_style.widthPolicy = SizePolicy.FIXED;
        filtertext_style.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
        filtertext_style.setPadding(10, 2, 10, 0);
        filtertext_style.setMargin(-3, 0, 0, 0);
        filtertext_style.setBackground(55, 55, 55);
        filtertext_style.font = DefaultsService.getDefaultFont();
        style.addInnerStyle("filtertext", filtertext_style);

        Style divider_style = getFrameStyle();
        divider_style.widthPolicy = SizePolicy.FIXED;
        divider_style.width = 1;
        divider_style.setBackground(55, 55, 55);
        divider_style.setMargin(0, 3, 0, 3);
        style.addInnerStyle("divider", divider_style);

        return style;
    }

    public static Style getInputDialogStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        style.setBackground(0, 0, 0, 150);
        style.borderRadius = new CornerRadius();
        style.padding = new Indents();
        style.margin = new Indents();
        style.spacing = new Spacing();

        Style window_style = getFrameStyle();
        window_style.setSize(300, 150);
        window_style.setMinSize(300, 150);
        window_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        window_style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        window_style.setPadding(2, 2, 2, 2);
        window_style.setBackground(45, 45, 45);
        style.addInnerStyle("window", window_style);

        Style ok_style = getButtonCoreStyle();
        ok_style.setBackground(100, 255, 150);
        ok_style.foreground = Color.black;
        ok_style.setSize(100, 30);
        ok_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        ok_style.setAlignment(ItemAlignment.LEFT, ItemAlignment.BOTTOM);
        ok_style.setMargin(0, 0, 0, 0);
        ok_style.borderRadius = new CornerRadius();
        ok_style.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 80)));
        style.addInnerStyle("button", ok_style);

        Style text_style = getTextEditStyle();
        text_style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.TOP);
        text_style.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
        text_style.setMargin(0, 15, 0, 0);
        style.addInnerStyle("textedit", text_style);

        Style layout_style = getFrameStyle();
        layout_style.setMargin(0, 30, 0, 0);
        layout_style.setPadding(6, 6, 6, 6);
        layout_style.setBackground(255, 255, 255, 20);
        style.addInnerStyle("layout", layout_style);

        Style toolbar_style = getHorizontalStackStyle();
        toolbar_style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.BOTTOM);
        toolbar_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        toolbar_style.setSpacing(10, 0);
        style.addInnerStyle("toolbar", toolbar_style);

        return style;
    }

    public static Style getSelectionItemStyle() {
        Style style = new Style();
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        style.setBackground(0, 0, 0, 0);
        style.setPadding(0, 1, 0, 1);
        style.setAlignment(ItemAlignment.LEFT, ItemAlignment.TOP);
        style.addItemState(ItemStateType.TOGGLED, new ItemState(new Color(255, 255, 255, 50)));
        return style;
    }

    public static Style getWrapAreaStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.padding = new Indents(2, 2, 2, 2);
        style.spacing = new Spacing(0, 5);

        Style selection_style = getSelectionItemStyle();
        // Style selection_style = DefaultsService.getDefaultStyle(SelectionItem.class);
        style.addInnerStyle("selection", selection_style);

        return style;
    }

    public static Style getWrapGridStyle() {
        Style style = new Style();

        style.background = new Color(70, 70, 70);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style vsb_style = getVerticalScrollBarStyle();
        vsb_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.TOP));
        style.addInnerStyle("vscrollbar", vsb_style);

        Style hsb_style = getHorizontalScrollBarStyle();
        hsb_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.BOTTOM));
        style.addInnerStyle("hscrollbar", hsb_style);

        Style area_style = getWrapAreaStyle();
        style.addInnerStyle("area", area_style);

        return style;
    }

    public static Style getSideAreaStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        style.setBackground(0, 0, 0, 130);
        style.borderRadius = new CornerRadius(0);
        style.padding = new Indents();
        style.margin = new Indents();
        style.spacing = new Spacing();

        Style window_style = getFrameStyle();
        window_style.setPadding(2, 2, 2, 2);
        window_style.setBackground(40, 40, 40);
        window_style.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
        style.addInnerStyle("window", window_style);

        Style close_style = new Style();
        close_style.setMargin(0, 5, 0, 0);
        close_style.font = DefaultsService.getDefaultFont();
        close_style.background = new Color(100, 100, 100);
        close_style.foreground = new Color(0, 0, 0, 0);
        close_style.setSize(15, 15);
        close_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        close_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.RIGHT));
        close_style.textAlignment = new LinkedList<>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.TOP));
        ItemState close_hovered = new ItemState();
        close_hovered.background = new Color(186, 95, 97, 255);
        close_style.addItemState(ItemStateType.HOVERED, close_hovered);

        close_style.shape = GraphicsMathService.getCross(15, 15, 2, 45);
        close_style.isFixedShape = true;
        style.addInnerStyle("closebutton", close_style);

        return style;
    }

    public static Style getImageItemStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        style.setBackground(0, 0, 0, 0);
        return style;
    }

    public static Style getLoadingScreenStyle() {
        Style style = new Style();
        style.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        style.setBackground(0, 0, 0, 150);

        Style text_style = getLabelStyle();
        text_style.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        text_style.setTextAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        text_style.font = DefaultsService.getDefaultFont(Font.BOLD, 14);
        style.addInnerStyle("text", text_style);

        Style image_style = getImageItemStyle();
        image_style.setMaxSize(64, 64);
        style.addInnerStyle("image", image_style);

        return style;
    }
}
