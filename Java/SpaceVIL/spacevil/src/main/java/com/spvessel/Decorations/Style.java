package com.spvessel.Decorations;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Core.InterfaceBaseItem;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.ItemStateType;
import com.spvessel.Flags.SizePolicy;
import com.spvessel.GraphicsMathService;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

public class Style {
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
    public void setPadding(int left, int top, int right, int bottom) {
        padding.left = left;
        padding.top = top;
        padding.right = right;
        padding.bottom = bottom;
    }

    /**
     * Margin of the object
     * @param margin
     */
    public void setMargin(Indents margin) {
        this.margin = margin;
    }
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
     * @param fill border color
     * @param radius radius of the border corners
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
     * Add inner primitives to the object (as decorations only)
     * note: not supported in current version
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
     * Object changes its state according to the ItemState when ItemStateType happens
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
        style.width = 10;
        style.height = 10;
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        style.borderRadius = new CornerRadius(6);

        // style.borderThickness = 2;
        // style.borderFill = new Color(255, 255, 255);

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 60);
        style.addItemState(ItemStateType.HOVERED, hovered);

        ItemState pressed = new ItemState();
        pressed.background = new Color(30, 0, 0, 60);
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
        style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        style.borderRadius = new CornerRadius(6);

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

        Style textline_style = getLabelStyle();
        textline_style.foreground = new Color(210, 210, 210);
        textline_style.widthPolicy = SizePolicy.EXPAND;
        textline_style.heightPolicy = SizePolicy.EXPAND;
        textline_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER));
        textline_style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        textline_style.margin = new Indents(10, 0, 0, 0);
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
        marker_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        marker_style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));

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

        selection_style.font = DefaultsService.getDefaultFont();
        selection_style.widthPolicy = SizePolicy.EXPAND;
        selection_style.heightPolicy = SizePolicy.EXPAND;
        selection_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        selection_style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        selection_style.padding = new Indents(10, 0, 0, 0);
        selection_style.margin = new Indents(0, 0, 20, 0);
        style.addInnerStyle("selection", selection_style);

        Style dropdownbutton_style = getButtonCoreStyle();
        dropdownbutton_style.borderRadius = new CornerRadius();
        dropdownbutton_style.width = 20;
        dropdownbutton_style.widthPolicy = SizePolicy.FIXED;
        dropdownbutton_style.heightPolicy = SizePolicy.EXPAND;
        dropdownbutton_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.VCENTER));
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
        arrow_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
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
        style.padding = new Indents(2, 2, 2, 2);
        style.background = new Color(255, 255, 255);
        style.maxWidth = 65535;
        style.maxHeight = 65535;

        Style itemlist_style = new Style();
        itemlist_style.background = new Color(0, 0, 0, 0);
        itemlist_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        style.addInnerStyle("itemlist", itemlist_style);

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
        
        Style arrow_style = new Style();
        arrow_style.width = 6;
        arrow_style.height = 10;
        arrow_style.widthPolicy = SizePolicy.FIXED;
        arrow_style.heightPolicy = SizePolicy.FIXED;
        arrow_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.VCENTER));
        arrow_style.background = new Color(80, 80, 80);
        arrow_style.margin = new Indents(0, 0, 5, 0);
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
        itemlist_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        style.addInnerStyle("itemlist", itemlist_style);

        Style area_style = getListAreaStyle();
        
        Style substrate_style = area_style.getInnerStyle("substrate");
        substrate_style.background = new Color(150, 150, 150, 255);

        Style hovercover_style = area_style.getInnerStyle("hovercover");
        hovercover_style.background = new Color(150, 150, 150, 255);

        style.addInnerStyle("listarea", area_style);

        return style;
    }

    /**
     * @return default style for FlowArea objects
     */
    public static Style getFlowAreaStyle() {
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
        uparrow_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        uparrow_style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
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
        downarrow_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.VCENTER));
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
        handler_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        handler_style.addItemState(ItemStateType.HOVERED, hovered);

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
        uparrow_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
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
        ;
        downarrow_style.width = 16;
        downarrow_style.height = 16;
        downarrow_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.HCENTER));
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
        handler_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        handler_style.addItemState(ItemStateType.HOVERED, hovered);
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
        style.foreground = new Color(0, 0, 0);
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

        Style substrate_style = new Style();
        // substrate_style.background = new Color(39, 150, 216, 255);
        substrate_style.background = new Color(100, 100, 100, 255);
        substrate_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        substrate_style.widthPolicy = SizePolicy.EXPAND;
        substrate_style.heightPolicy = SizePolicy.FIXED;
        style.addInnerStyle("substrate", substrate_style);

        Style hover_style = new Style();
        hover_style.background = new Color(255, 255, 255, 30);
        hover_style.setAlignment(ItemAlignment.LEFT , ItemAlignment.TOP);
        hover_style.widthPolicy = SizePolicy.EXPAND;
        hover_style.heightPolicy = SizePolicy.FIXED;
        style.addInnerStyle("hovercover", hover_style);

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

        return style;
    }

    /**
     * @return default style for WContainer objects
     * note: not supported in current version
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

        Style textline_style = getLabelStyle();
        textline_style.foreground = new Color(210, 210, 210);
        textline_style.widthPolicy = SizePolicy.EXPAND;
        textline_style.heightPolicy = SizePolicy.EXPAND;
        textline_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.VCENTER));
        textline_style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        textline_style.margin = new Indents(10, 0, 0, 0);
        style.addInnerStyle("textline", textline_style);

        Style indicator_style = getIndicatorStyle();
        indicator_style.shape = GraphicsMathService.getRoundSquare(20, 20, 10, 0, 0);
        indicator_style.isFixedShape = true;
        style.addInnerStyle("indicator", indicator_style);

        Style marker_style = indicator_style.getInnerStyle("marker");
        marker_style.shape = GraphicsMathService.getEllipse(100, 16);
        indicator_style.addInnerStyle("marker", marker_style);

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
        marker_style.background = new Color(130, 130, 130);
        marker_style.setSize(16, 16);
        marker_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        marker_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.RIGHT));
        marker_style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        marker_style.borderRadius = new CornerRadius(4);
        ItemState toggled = new ItemState();
        toggled.background = new Color(60, 60, 60, 255);
        marker_style.addItemState(ItemStateType.TOGGLED, toggled);
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
        cursor_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        cursor_style.margin = new Indents(0, 5, 0, 5);
        cursor_style.isVisible = false;
        style.addInnerStyle("cursor", cursor_style);

        Style selection_style = new Style();
        selection_style.background = new Color(111, 181, 255);
        selection_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        selection_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        style.addInnerStyle("selection", selection_style);

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
        // style.addItemState(ItemStateType.FOCUSED, focused);

        Style cursor_style = new Style();
        cursor_style.background = new Color(60, 60, 60);
        cursor_style.width = 2;
        cursor_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        cursor_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        cursor_style.margin = new Indents(0, 5, 0, 5);
        cursor_style.isVisible = false;
        style.addInnerStyle("cursor", cursor_style);

        Style selection_style = new Style();
        selection_style.background = new Color(111, 181, 255);
        selection_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        selection_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        selection_style.margin = new Indents(0, 5, 0, 5);
        style.addInnerStyle("selection", selection_style);

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
        style.background = new Color(45, 45, 45, 240);
        style.foreground = new Color(210, 210, 210);
        style.font = DefaultsService.getDefaultFont(16);
        style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.RIGHT));
        style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
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
        style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));

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
        close_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.RIGHT));
        close_style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
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
        minimize_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.RIGHT));
        minimize_style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
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
        maximize_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.RIGHT));
        maximize_style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
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
        view_style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.HCENTER));
        style.addInnerStyle("tabview", view_style);

        Style tab_style = new Style();
        tab_style.font = DefaultsService.getDefaultFont(14);
        tab_style.background = new Color(0, 0, 0, 0);
        tab_style.foreground = new Color(210, 210, 210);
        tab_style.width = 100;
        tab_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        tab_style.alignment = new LinkedList<>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        tab_style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
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
        // ItemState hovered = new ItemState();
        // hovered.background = new Color(255, 255, 255, 130);
        // style.addItemState(ItemStateType.HOVERED, hovered);

        Style indicator_style = new Style();// getButtonToggleStyle();
        indicator_style.background = new Color(32, 32, 32);
        indicator_style.foreground = new Color(210, 210, 210);
        indicator_style.font = DefaultsService.getDefaultFont();
        indicator_style.setSize(15, 15);
        indicator_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        indicator_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        indicator_style.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
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
        branch_icon_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        branch_icon_style.shape = GraphicsMathService.getFolderIconShape(20, 15, 0, 0);
        style.addInnerStyle("branchicon", branch_icon_style);

        Style leaf_icon_style = new Style();
        leaf_icon_style.background = new Color(129, 187, 133);
        leaf_icon_style.setSize(6, 6);
        leaf_icon_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        leaf_icon_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
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
        uparrow_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
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
        downarrow_style.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.HCENTER));
        downarrow_style.shape = GraphicsMathService.getTriangle(12, 6, 0, 0, 180);
        downarrow_style.isFixedShape = true;
        downarrow_style.addItemState(ItemStateType.HOVERED, hovered);
        style.addInnerStyle("downarrow", downarrow_style);

        Style btns_area = getVerticalStackStyle();
        btns_area.widthPolicy = SizePolicy.FIXED;
        btns_area.heightPolicy = SizePolicy.EXPAND;
        btns_area.width = 20;
        btns_area.background = new Color(255, 181, 111);
        btns_area.alignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.RIGHT));
        style.addInnerStyle("buttonsarea", btns_area);

        Style text_input = getTextEditStyle();
        text_input.heightPolicy = SizePolicy.EXPAND;
        text_input.textAlignment = new LinkedList<>(
                Arrays.asList(ItemAlignment.RIGHT));
        style.addInnerStyle("textedit", text_input);

        return style;
    }
}
