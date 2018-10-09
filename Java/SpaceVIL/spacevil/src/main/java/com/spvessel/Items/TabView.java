package com.spvessel.Items;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Cores.*;
import com.spvessel.Decorations.Indents;
import com.spvessel.Decorations.Style;
import com.spvessel.Flags.*;

public class TabView extends VisualItem {
    static int count = 0;

    // private Grid _tab_view;
    private VerticalStack _tab_view;
    private HorizontalStack _tab_bar;
    private Map<ButtonToggle, Frame> _tab_list;
    private Style _stored_style;

    public TabView() {
        // setBackground(Color.Transparent);
        // setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
        setItemName("TabView_" + count);
        count++;

        _tab_view = new VerticalStack();
        _tab_list = new HashMap<ButtonToggle, Frame>();
        _tab_bar = new HorizontalStack();

        setStyle(DefaultsService.getDefaultStyle("SpaceVIL.TabView"));
    }

    @Override
    public boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }

    @Override
    public void initElements() {
        // tab view
        addItem(_tab_view);

        // _tab_bar
        _tab_bar.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        _tab_bar.setHeight(30);
        _tab_view.addItem(_tab_bar);
    }

    private void hideOthers(InterfaceItem sender, MouseArgs args) {
        for (Map.Entry<ButtonToggle, Frame> tab : _tab_list.entrySet()) {
            if (tab.getKey().getItemName() != sender.getItemName()) {
                tab.getKey().setToggled(false);
                tab.getValue().setVisible(false);
            } else {
                tab.getValue().setVisible(true);
                tab.getKey().setVisible(true);
            }
        }
        _tab_view.updateLayout();
    }

    public void addTab(String tab_name) {
        Style tab_style = _stored_style.getInnerStyle("tab");
        Style view_style = _stored_style.getInnerStyle("tabview");

        ButtonToggle tab = new ButtonToggle(tab_name);
        tab.setItemName(tab_name);
        if (tab_style != null) {
            // tab.RemoveItemState(ItemStateType.Pressed);
            tab.setStyle(tab_style);
        }
        //InterfaceMouseMethodState click = (sender, args) -> hideOthers(sender, args);
        tab.eventMouseClick.add(this::hideOthers); //click);
        _tab_bar.addItem(tab);

        Frame view = new Frame();
        view.setItemName(tab_name + "_view");

        if (view_style != null)
            view.setStyle(view_style);

        _tab_view.addItem(view);
        _tab_list.put(tab, view);

        if (_tab_bar.getItems().size() == 1) {
            tab.setToggled(true);
            view.setVisible(true);
        }
    }

    public void addTab(String tab_name, Style tab_style, Style view_style) {
        // refactor
    }

    public void removeTab(String tab_name) {
        for (BaseItem tab : _tab_bar.getItems()) {
            if (tab_name == tab.getItemName()) {
                _tab_list.remove(tab);
            }
        }
    }

    public void addItemToTab(String tab_name, BaseItem item) {
        for (BaseItem tab : _tab_bar.getItems()) {
            if (tab_name == tab.getItemName()) {
                _tab_list.get(tab).addItem(item);
            }
        }
    }

    // text init
    private List<ItemAlignment> _textAlignment = new LinkedList<>();

    public void setTextAlignment(List<ItemAlignment> alignment) {
        _textAlignment = alignment;
    }
    public void setTextAlignment(ItemAlignment... alignment) {
        List<ItemAlignment> list = Arrays.stream(alignment).collect(Collectors.toList());
        _textAlignment = list;
    }

    private Indents _textMargin = new Indents();

    public void setTextMargin(Indents margin) {
        _textMargin = margin;
    }

    private Font _font = DefaultsService.getDefaultFont(16);

    public void setFont(Font font) {
        _font = font;
    }

    private int _font_size = 16;

    public void setFontSize(int size) {
        _font_size = size;
    }

    private int _font_style = Font.BOLD;

    public void setFontStyle(int style) {
        _font_style = style;
    }

    private String _font_family = "Ubuntu";

    public void setFontFamily(String font_family) {
        _font_family = font_family;
    }

    public Font getFont() {
        return _font;
    }

    private Color _foreground = new Color(255, 255, 255);

    public void setForeground(Color color) {
        _foreground = color;
    }

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
        setForeground(new Color(r, g, b));
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
        setForeground(new Color(r, g, b, a));
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
        setForeground(new Color(r, g, b));
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
        setForeground(new Color(r, g, b, a));
    }

    // style
    @Override
    public void setStyle(Style style) {
        if (style == null)
            return;

        super.setStyle(style);
        _stored_style = style;

        setForeground(style.foreground);
        setFont(style.font);
        setTextAlignment(style.textAlignment);

        Style tab_style = style.getInnerStyle("tab");
        Style view_style = style.getInnerStyle("tabview");
        for (Map.Entry<ButtonToggle, Frame> item : _tab_list.entrySet()) {
            if (tab_style != null)
                item.getKey().setStyle(tab_style);
            if (view_style != null)
                item.getValue().setStyle(view_style);
        }
    }
}