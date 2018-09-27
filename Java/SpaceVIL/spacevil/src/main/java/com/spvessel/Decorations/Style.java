package com.spvessel.Decorations;

import com.spvessel.Flags.*;
import com.spvessel.Items.*;
import com.spvessel.Common.*;
import com.spvessel.Engine.*;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Style {

    private Map<String, Style> _inner_styles = new HashMap<String, Style>();

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

    private Map<ItemStateType, ItemState> _item_states = new HashMap<ItemStateType, ItemState>();

    public Indents padding;// = new Indents();
    public Spacing spacing;// = new Spacing();
    public Indents margin;// = new Indents();
    public int borderRadius;
    public int borderThickness;
    public Color borderColor;
    public List<float[]> shape;// = new List<float[]>();
    public List<BaseItem> innerShapes;// = new List<float[]>();
    public Boolean isFixedShape;
    public Boolean isVisible;

    public Style()// default
    {
        isVisible = true;
        maxWidth = 65535;
        maxHeight = 65535;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setSizePolicy(SizePolicy width_policy, SizePolicy height_policy) {
        widthPolicy = width_policy;
        heightPolicy = height_policy;
    }

    public void addInnerShape(BaseItem shape) {
        if (innerShapes == null)
            innerShapes = new LinkedList<BaseItem>();
        innerShapes.add(shape);
    }

    public void addInnerStyle(String item_name, Style style) {
        if (_inner_styles.containsKey(item_name))
            _inner_styles.replace(item_name, style);
        else
            _inner_styles.put(item_name, style);
    }

    public Style getInnerStyle(String item_name) {
        if (_inner_styles.containsKey(item_name))
            return _inner_styles.get(item_name);

        return null;
    }

    public void addItemState(ItemStateType type, ItemState state) {
        if (_item_states.containsKey(type)) {
            state.value = true;
            _item_states.replace(type, state);
        } else {
            _item_states.put(type, state);
        }
    }

    public ItemState getState(ItemStateType type) {
        if (_item_states.containsKey(type))
            return _item_states.get(type);
        return null;
    }

    public Map<ItemStateType, ItemState> getAllStates() {
        return _item_states;
    }

    public void removeItemState(ItemStateType type) {
        if (type == ItemStateType.BASE)
            return;
        if (_item_states.containsKey(type))
            _item_states.remove(type);
    }

    public void removeInnerStyle(String item_name, Style style) {
        if (_inner_styles.containsKey(item_name))
            _inner_styles.remove(item_name);
        else
            return;
    }

    // get default styles
    public static Style getButtonCoreStyle() {
        Style style = new Style();
        style.background = new Color(13, 176, 255);
        style.foreground = new Color(32, 32, 32);

        style.font = DefaultsService.getDefaultFont();
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.width = 10;
        style.height = 10;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.textAlignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        style.borderRadius = 6;

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

    public static Style getButtonToggleStyle() {
        Style style = new Style();
        style.background = new Color(13, 176, 255);
        style.foreground = new Color(32, 32, 32);
        style.font = DefaultsService.getDefaultFont();
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.width = 10;
        style.height = 10;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.textAlignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        style.borderRadius = 6;

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 60);
        style.addItemState(ItemStateType.HOVERED, hovered);

        ItemState pressed = new ItemState();
        pressed.background = new Color(30, 0, 0, 60);
        style.addItemState(ItemStateType.PRESSED, pressed);

        ItemState toggled = new ItemState();
        toggled.background = new Color(121, 223, 152);
        style.addItemState(ItemStateType.PRESSED, toggled);

        return style;
    }

    public static Style getCheckBoxStyle() {
        Style style = new Style();

        style.background = new Color(255, 255, 255, 20);
        style.foreground = new Color(150, 150, 150);
        style.font = DefaultsService.getDefaultFont();
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.width = 10;
        style.height = 20;
        style.minHeight = 20;
        style.minWidth = 20;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));

        Style textline_style = new Style();
        textline_style.widthPolicy = SizePolicy.EXPAND;
        textline_style.heightPolicy = SizePolicy.EXPAND;
        textline_style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.VCENTER));
        textline_style.textAlignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        textline_style.margin = new Indents(10, 0, 0, 0);
        style.addInnerStyle("textline", textline_style);

        return style;
    }

    public static Style getIndicatorStyle() {
        Style style = new Style();

        style.background = new Color(32, 32, 32);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.width = 20;
        style.height = 20;
        style.minHeight = 20;
        style.minWidth = 20;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.padding = new Indents(4, 4, 4, 4);

        Style marker_style = new Style();
        marker_style.background = new Color(32, 32, 32);
        marker_style.foreground = new Color(70, 70, 70);
        ;
        marker_style.font = DefaultsService.getDefaultFont();
        marker_style.widthPolicy = SizePolicy.EXPAND;
        marker_style.heightPolicy = SizePolicy.EXPAND;
        marker_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 60);
        marker_style.addItemState(ItemStateType.HOVERED, hovered);

        ItemState toggled = new ItemState();
        toggled.background = new Color(255, 181, 111);
        marker_style.addItemState(ItemStateType.PRESSED, toggled);

        style.addInnerStyle("marker", marker_style);

        return style;
    }

    public static Style getTextLineStyle() {
        Style style = new Style();

        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.margin = new Indents(4, 4, 4, 4);

        return style;
    }

    public static Style getComboBoxStyle() {
        Style style = new Style();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = new Color(70, 70, 70);
        ;
        style.font = DefaultsService.getDefaultFont();
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.width = 10;
        style.height = 30;
        style.minHeight = 10;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));

        Style selection_style = new Style();
        selection_style.background = new Color(220, 220, 220);
        selection_style.foreground = new Color(70, 70, 70);
        ;
        selection_style.font = DefaultsService.getDefaultFont();
        selection_style.widthPolicy = SizePolicy.EXPAND;
        selection_style.heightPolicy = SizePolicy.EXPAND;
        selection_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        selection_style.textAlignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        selection_style.padding = new Indents(10, 0, 0, 0);
        selection_style.margin = new Indents(0, 0, 20, 0);
        style.addInnerStyle("selection", selection_style);

        Style dropdownbutton_style = new Style();
        dropdownbutton_style.width = 20;
        dropdownbutton_style.widthPolicy = SizePolicy.FIXED;
        dropdownbutton_style.heightPolicy = SizePolicy.EXPAND;
        dropdownbutton_style.alignment = new LinkedList<ItemAlignment>(
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
        arrow_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        arrow_style.background = new Color(255, 50, 50, 50);
        arrow_style.shape = GraphicsMathService.getTriangle(100, 100, 0, 0, 180);
        style.addInnerStyle("arrow", arrow_style);

        return style;
    }

    public static Style getComboBoxDropDownStyle() {
        Style style = new Style();
        style.padding = new Indents(2, 2, 2, 2);
        style.background = new Color(255, 255, 255);
        style.maxWidth = 65535;
        style.maxHeight = 65535;

        Style itemlist_style = new Style();
        itemlist_style.background = new Color(0, 0, 0, 0);
        itemlist_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        style.addInnerStyle("itemlist", itemlist_style);

        return style;
    }

    public static Style getMenuItemStyle() {
        Style style = new Style();
        style.background = new Color(0, 0, 0, 0);
        style.foreground = new Color(255, 70, 70, 70);
        ;
        style.font = DefaultsService.getDefaultFont();
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.height = 25;
        style.minHeight = 10;
        style.textAlignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.padding = new Indents(10, 0, 0, 0);

        ItemState hovered = new ItemState();
        hovered.background = new Color(180, 180, 180, 255);
        style.addItemState(ItemStateType.HOVERED, hovered);

        Style arrow_style = new Style();
        arrow_style.width = 6;
        arrow_style.height = 10;
        arrow_style.widthPolicy = SizePolicy.FIXED;
        arrow_style.heightPolicy = SizePolicy.FIXED;
        arrow_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.VCENTER));
        arrow_style.background = new Color(255, 80, 80, 80);
        arrow_style.margin = new Indents(0, 0, 5, 0);
        arrow_style.shape = GraphicsMathService.getTriangle(100, 100, 0, 0, 90);
        style.addInnerStyle("arrow", arrow_style);

        return style;
    }

    public static Style getContextMenuStyle() {
        Style style = new Style();
        style.background = new Color(255, 210, 210, 210);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.padding = new Indents(0, 0, 0, 0);

        Style itemlist_style = new Style();
        itemlist_style.background = new Color(0, 0, 0, 0);
        itemlist_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        style.addInnerStyle("itemlist", itemlist_style);

        return style;
    }

    public static Style getFlowAreaStyle() {
        Style style = new Style();

        // style.background = new Color(0,0,0,0);
        style.background = new Color(255, 70, 70, 70);

        style.padding = new Indents(2, 2, 2, 2);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        return style;
    }

    public static Style getFrameStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.padding = new Indents(2, 2, 2, 2);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        return style;
    }

    public static Style getGridStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.height = 16;
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        return style;
    }

    public static Style getHorizontalScrollBarStyle() {
        Style style = new Style();

        style.background = new Color(255, 50, 50, 50);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.height = 16;

        Style uparrow_style = new Style();
        uparrow_style.widthPolicy = SizePolicy.FIXED;
        uparrow_style.heightPolicy = SizePolicy.FIXED;
        uparrow_style.background = new Color(50, 255, 255, 255);
        uparrow_style.width = 16;
        uparrow_style.height = 16;
        uparrow_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        uparrow_style.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, -90);
        uparrow_style.isFixedShape = true;

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        uparrow_style.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("uparrow", uparrow_style);

        Style downarrow_style = new Style();
        downarrow_style.widthPolicy = SizePolicy.FIXED;
        downarrow_style.heightPolicy = SizePolicy.FIXED;
        downarrow_style.background = new Color(50, 255, 255, 255);
        downarrow_style.width = 16;
        downarrow_style.height = 16;
        downarrow_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        downarrow_style.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, 90);
        downarrow_style.isFixedShape = true;
        downarrow_style.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("downarrow", downarrow_style);

        Style slider_style = new Style();
        slider_style.widthPolicy = SizePolicy.EXPAND;
        slider_style.heightPolicy = SizePolicy.EXPAND;
        slider_style.background = new Color(0, 0, 0, 0);
        style.addInnerStyle("slider", slider_style);

        Style track_style = new Style();
        track_style.widthPolicy = SizePolicy.EXPAND;
        track_style.heightPolicy = SizePolicy.EXPAND;
        track_style.background = new Color(0, 0, 0, 0);
        slider_style.addInnerStyle("track", track_style);

        Style handler_style = new Style();
        handler_style.widthPolicy = SizePolicy.EXPAND;
        handler_style.heightPolicy = SizePolicy.EXPAND;
        handler_style.background = new Color(50, 255, 255, 255);
        handler_style.margin = new Indents(0, 3, 0, 3);
        handler_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        handler_style.addItemState(ItemStateType.HOVERED, hovered);

        slider_style.addInnerStyle("handler", handler_style);

        return style;
    }

    public static Style getVerticalScrollBarStyle() {
        Style style = new Style();

        style.background = new Color(255, 50, 50, 50);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.EXPAND;
        style.width = 16;

        Style uparrow_style = new Style();
        uparrow_style.widthPolicy = SizePolicy.FIXED;
        uparrow_style.heightPolicy = SizePolicy.FIXED;
        uparrow_style.background = new Color(50, 255, 255, 255);
        uparrow_style.width = 16;
        uparrow_style.height = 16;
        uparrow_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        uparrow_style.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, 0);
        uparrow_style.isFixedShape = true;

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        uparrow_style.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("uparrow", uparrow_style);

        Style downarrow_style = new Style();
        downarrow_style.widthPolicy = SizePolicy.FIXED;
        downarrow_style.heightPolicy = SizePolicy.FIXED;
        downarrow_style.background = new Color(50, 255, 255, 255);
        downarrow_style.width = 16;
        downarrow_style.height = 16;
        downarrow_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.HCENTER));
        downarrow_style.shape = GraphicsMathService.getTriangle(10, 8, 3, 4, 180);
        downarrow_style.isFixedShape = true;
        downarrow_style.addItemState(ItemStateType.HOVERED, hovered);
        style.addInnerStyle("downarrow", downarrow_style);

        Style slider_style = new Style();
        slider_style.widthPolicy = SizePolicy.EXPAND;
        slider_style.heightPolicy = SizePolicy.EXPAND;
        slider_style.background = new Color(0, 0, 0, 0);
        style.addInnerStyle("slider", slider_style);

        Style track_style = new Style();
        track_style.widthPolicy = SizePolicy.EXPAND;
        track_style.heightPolicy = SizePolicy.EXPAND;
        track_style.background = new Color(0, 0, 0, 0);
        slider_style.addInnerStyle("track", track_style);

        Style handler_style = new Style();
        handler_style.widthPolicy = SizePolicy.EXPAND;
        handler_style.heightPolicy = SizePolicy.EXPAND;
        handler_style.background = new Color(50, 255, 255, 255);
        handler_style.margin = new Indents(3, 0, 3, 0);
        handler_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.TOP, ItemAlignment.HCENTER));
        handler_style.addItemState(ItemStateType.HOVERED, hovered);
        slider_style.addInnerStyle("handler", handler_style);

        return style;
    }

    public static Style getHorizontalSliderStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.height = 25;

        Style track_style = new Style();
        track_style.widthPolicy = SizePolicy.EXPAND;
        track_style.heightPolicy = SizePolicy.FIXED;
        track_style.height = 5;
        track_style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.VCENTER));
        track_style.background = new Color(255, 100, 100, 100);
        style.addInnerStyle("track", track_style);

        Style handler_style = new Style();
        handler_style.widthPolicy = SizePolicy.FIXED;
        handler_style.heightPolicy = SizePolicy.EXPAND;
        handler_style.width = 10;
        handler_style.background = new Color(255, 255, 181, 111);
        handler_style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT));

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        handler_style.addItemState(ItemStateType.HOVERED, hovered);

        style.addInnerStyle("handler", handler_style);

        return style;
    }

    public static Style getVerticalSliderStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.width = 25;

        Style track_style = new Style();
        track_style.widthPolicy = SizePolicy.FIXED;
        track_style.heightPolicy = SizePolicy.EXPAND;
        track_style.width = 5;
        track_style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.HCENTER));
        track_style.background = new Color(255, 100, 100, 100);
        style.addInnerStyle("track", track_style);

        Style handler_style = new Style();
        handler_style.widthPolicy = SizePolicy.EXPAND;
        handler_style.heightPolicy = SizePolicy.FIXED;
        handler_style.height = 10;
        handler_style.background = new Color(255, 255, 181, 111);
        handler_style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.TOP));
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 80);
        handler_style.addItemState(ItemStateType.HOVERED, hovered);
        style.addInnerStyle("handler", handler_style);

        return style;
    }

    public static Style getHorizontalStackStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        return style;
    }

    public static Style getVerticalStackStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        return style;
    }

    public static Style getHorizontalSplitAreaStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style splitter_style = new Style();
        splitter_style.background = new Color(255, 42, 42, 42);
        splitter_style.width = 6;
        style.addInnerStyle("splitholder", splitter_style);

        return style;
    }

    public static Style getVerticalSplitAreaStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style splitter_style = new Style();
        splitter_style.background = new Color(255, 42, 42, 42);
        splitter_style.height = 6;
        style.addInnerStyle("splitholder", splitter_style);

        return style;
    }

    public static Style getLabelStyle() {
        Style style = new Style();

        style.font = DefaultsService.getDefaultFont();
        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.textAlignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));

        return style;
    }

    public static Style getListAreaStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.padding = new Indents(2, 2, 2, 2);

        Style substrate_style = new Style();
        substrate_style.background = new Color(100, 39, 150, 216);
        substrate_style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        substrate_style.widthPolicy = SizePolicy.EXPAND;
        substrate_style.heightPolicy = SizePolicy.FIXED;
        style.addInnerStyle("substrate", substrate_style);

        return style;
    }

    public static Style getListBoxStyle() {
        Style style = new Style();

        style.background = new Color(255, 70, 70, 70);
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));

        Style vsb_style = getVerticalScrollBarStyle();
        vsb_style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.RIGHT, ItemAlignment.TOP));
        style.addInnerStyle("vscrollbar", vsb_style);

        Style hsb_style = getHorizontalScrollBarStyle();
        hsb_style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.BOTTOM));
        style.addInnerStyle("hscrollbar", hsb_style);

        return style;
    }

    public static Style getWContainerStyle()// нужен ли?
    {
        Style style = new Style();
        return style;
    }

    public static Style getRadioButtonStyle()// нужен ли?
    {
        Style style = new Style();

        style.background = new Color(255, 80, 80, 80);
        style.foreground = new Color(150, 150, 150);
        style.font = DefaultsService.getDefaultFont();
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.height = 20;
        style.minHeight = 20;
        style.minWidth = 20;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.borderRadius = 10;

        Style textline_style = new Style();
        textline_style.widthPolicy = SizePolicy.EXPAND;
        textline_style.heightPolicy = SizePolicy.EXPAND;
        textline_style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.VCENTER));
        textline_style.textAlignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        textline_style.margin = new Indents(10, 0, 0, 0);
        style.addInnerStyle("textline", textline_style);

        Style indicator_style = getIndicatorStyle();
        indicator_style.shape = GraphicsMathService.getEllipse(100, 32);
        style.addInnerStyle("indicator", indicator_style);

        Style marker_style = indicator_style.getInnerStyle("marker");
        marker_style.shape = GraphicsMathService.getEllipse(100, 32);
        indicator_style.addInnerStyle("marker", marker_style);

        return style;
    }

    public static Style getPasswordLineStyle() {
        Style style = new Style();
        style.background = new Color(210, 210, 210);
        style.foreground = new Color(70, 70, 70);
        style.font = DefaultsService.getDefaultFont(16);
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.textAlignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.height = 30;
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.padding = new Indents(5, 0, 5, 0);

        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 30);
        style.addItemState(ItemStateType.HOVERED, hovered);

        Style marker_style = getIndicatorStyle().getInnerStyle("marker");
        marker_style.background = new Color(130, 130, 130);
        marker_style.setSize(16, 16);
        marker_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        marker_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.RIGHT));
        marker_style.textAlignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        marker_style.borderRadius = 4;
        ItemState toggled = new ItemState();
        toggled.background = new Color(60, 60, 60, 255);
        marker_style.addItemState(ItemStateType.TOGGLED, toggled);
        style.addInnerStyle("showmarker", marker_style);

        Style cursor_style = new Style();
        cursor_style.background = new Color(60, 60, 60);
        cursor_style.width = 2;
        cursor_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        cursor_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        cursor_style.margin = new Indents(0, 5, 0, 5);
        cursor_style.isVisible = false;
        style.addInnerStyle("cursor", cursor_style);

        Style selection_style = new Style();
        selection_style.background = new Color(111, 181, 255);
        selection_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        selection_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        style.addInnerStyle("selection", selection_style);

        return style;
    }

    public static Style getTextEditStyle() {
        Style style = new Style();
        style.background = new Color(210, 210, 210);
        style.foreground = new Color(70, 70, 70);
        style.font = DefaultsService.getDefaultFont(16);
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.textAlignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.height = 30;
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.padding = new Indents(5, 0, 5, 0);
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 30);
        style.addItemState(ItemStateType.HOVERED, hovered);

        Style cursor_style = new Style();
        cursor_style.background = new Color(60, 60, 60);
        cursor_style.width = 2;
        cursor_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        cursor_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        cursor_style.margin = new Indents(0, 5, 0, 5);
        cursor_style.isVisible = false;
        style.addInnerStyle("cursor", cursor_style);

        Style selection_style = new Style();
        selection_style.background = new Color(111, 181, 255);
        selection_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        selection_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.LEFT));
        selection_style.margin = new Indents(0, 5, 0, 5);
        style.addInnerStyle("selection", selection_style);

        return style;
    }

    public static Style getTextBlockStyle() {
        Style style = new Style();
        style.background = new Color(210, 210, 210);
        style.foreground = new Color(70, 70, 70);
        style.font = DefaultsService.getDefaultFont(16);
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.textAlignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.EXPAND;
        style.padding = new Indents(5, 5, 5, 5);
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 30);
        style.addItemState(ItemStateType.HOVERED, hovered);

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

    public static Style getPopUpMessageStyle() {
        Style style = new Style();
        style.background = new Color(240, 45, 45, 45);
        style.foreground = new Color(150, 150, 150);
        style.font = DefaultsService.getDefaultFont(16);
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.RIGHT));
        style.textAlignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));
        style.setSize(300, 70);
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.padding = new Indents(5, 5, 5, 5);
        style.margin = new Indents(10, 10, 10, 10);
        ItemState hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 10);
        style.addItemState(ItemStateType.HOVERED, hovered);

        Style close_style = new Style();
        close_style.background = new Color(100, 100, 100);
        close_style.setSize(10, 10);
        close_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        close_style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.TOP, ItemAlignment.RIGHT));
        close_style.margin = new Indents(0, 5, 0, 5);
        ItemState close_hovered = new ItemState();
        hovered.background = new Color(255, 255, 255, 60);
        close_style.addItemState(ItemStateType.HOVERED, close_hovered);
        close_style.shape = GraphicsMathService.getCross(10, 10, 3, 45);
        close_style.isFixedShape = false;
        style.addInnerStyle("closebutton", close_style);

        return style;
    }

    public static Style getProgressBarStyle() {
        Style style = new Style();

        style.font = DefaultsService.getDefaultFont();
        style.background = new Color(70, 70, 70);
        style.height = 20;
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.textAlignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.HCENTER, ItemAlignment.VCENTER));

        Style pgbar_style = new Style();
        pgbar_style.background = new Color(255, 0, 191, 255);
        pgbar_style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        pgbar_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        style.addInnerStyle("progressbar", pgbar_style);

        return style;
    }

    public static Style getToolTipStyle() {
        Style style = new Style();

        style.font = DefaultsService.getDefaultFont();
        style.background = new Color(255, 255, 255);
        style.foreground = new Color(70, 70, 70);
        ;
        style.height = 30;
        style.widthPolicy = SizePolicy.FIXED;
        style.heightPolicy = SizePolicy.FIXED;
        style.textAlignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.padding = new Indents(5, 0, 5, 0);
        style.borderRadius = 4;

        return style;
    }

    public static Style getTitleBarStyle() {
        Style style = new Style();

        style.font = DefaultsService.getDefaultFont();
        style.background = new Color(45, 45, 45);
        style.foreground = new Color(180, 180, 180);
        style.height = 30;
        style.widthPolicy = SizePolicy.EXPAND;
        style.heightPolicy = SizePolicy.FIXED;
        style.alignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.TOP));
        style.textAlignment = new LinkedList<ItemAlignment>(Arrays.asList(ItemAlignment.LEFT, ItemAlignment.VCENTER));
        style.padding = new Indents(10, 0, 5, 0);
        style.spacing = new Spacing(5, 0);

        Style close_style = new Style();
        close_style.background = new Color(255, 100, 100, 100);
        close_style.setSize(15, 15);
        close_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        close_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.VCENTER, ItemAlignment.RIGHT));
        ItemState close_hovered = new ItemState();
        close_hovered.background = new Color(186, 95, 97, 255);
        close_style.addItemState(ItemStateType.HOVERED, close_hovered);

        close_style.shape = GraphicsMathService.getCross(15, 15, 2, 45);
        close_style.isFixedShape = true;
        style.addInnerStyle("closebutton", close_style);

        Style minimize_style = new Style();
        minimize_style.background = new Color(255, 100, 100, 100);
        minimize_style.setSize(12, 15);
        minimize_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        minimize_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.RIGHT));
        minimize_style.margin = new Indents(0, 0, 5, 9);

        ItemState minimize_hovered = new ItemState();
        minimize_hovered.background = new Color(255, 255, 255, 80);
        minimize_style.addItemState(ItemStateType.HOVERED, minimize_hovered);

        minimize_style.shape = GraphicsMathService.getRectangle(15, 2, 0, 13);
        minimize_style.isFixedShape = true;
        style.addInnerStyle("minimizebutton", minimize_style);

        Style maximize_style = new Style();
        maximize_style.background = new Color(255, 100, 100, 100);
        maximize_style.setSize(12, 12);
        maximize_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        maximize_style.alignment = new LinkedList<ItemAlignment>(
                Arrays.asList(ItemAlignment.BOTTOM, ItemAlignment.RIGHT));
        maximize_style.margin = new Indents(0, 0, 0, 9);
        maximize_style.padding = new Indents(2, 2, 2, 2);

        ItemState maximize_hovered = new ItemState();
        maximize_hovered.background = new Color(0, 255, 64, 40);
        maximize_style.addItemState(ItemStateType.HOVERED, maximize_hovered);

        maximize_style.shape = GraphicsMathService.getRectangle(100, 100, 0, 0);

        style.addInnerStyle("maximizebutton", maximize_style);

        return style;
    }

    public static Style getTabViewStyle() {
        Style style = new Style();

        style.background = new Color(0, 0, 0, 0);
        style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);

        Style view_style = new Style();
        view_style.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        view_style.background = new Color(255, 71, 71, 71);
        view_style.isVisible = false;
        view_style.padding = new Indents(2, 2, 2, 2);
        style.addInnerStyle("tabview", view_style);

        Style tab_style = new Style();
        tab_style.font = DefaultsService.getDefaultFont();
        tab_style.background = new Color(255, 45, 45, 45);
        tab_style.foreground = new Color(255, 210, 210, 210);
        tab_style.width = 100;
        tab_style.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        tab_style.textAlignment = new LinkedList<ItemAlignment>(
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
}