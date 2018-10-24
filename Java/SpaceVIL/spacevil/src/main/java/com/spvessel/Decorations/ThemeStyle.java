package com.spvessel.Decorations;

import com.spvessel.Common.*;
import com.spvessel.Items.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class ThemeStyle {
    private Map<Class<?>, Style> defaultItemsStyle = new HashMap<>();
    public static Boolean applyEmbedded = true;

    public ThemeStyle() {
        // if (ThemeStyle.applyEmbedded) {
        //     defaultItemsStyle.put("SpaceVIL.ButtonCore", Style.getButtonCoreStyle());
        //     defaultItemsStyle.put("SpaceVIL.ButtonToggle", Style.getButtonToggleStyle());
        //     defaultItemsStyle.put("SpaceVIL.CheckBox", Style.getCheckBoxStyle());
        //     defaultItemsStyle.put("SpaceVIL.ComboBox", Style.getComboBoxStyle());
        //     defaultItemsStyle.put("SpaceVIL.ComboBoxDropDown", Style.getComboBoxDropDownStyle());
        //     defaultItemsStyle.put("SpaceVIL.ContextMenu", Style.getContextMenuStyle());
        //     defaultItemsStyle.put("SpaceVIL.FreeArea", Style.getFlowAreaStyle());
        //     defaultItemsStyle.put("SpaceVIL.Frame", Style.getFrameStyle());
        //     defaultItemsStyle.put("SpaceVIL.Grid", Style.getGridStyle());
        //     defaultItemsStyle.put("SpaceVIL.HorizontalScrollBar", Style.getHorizontalScrollBarStyle());
        //     defaultItemsStyle.put("SpaceVIL.HorizontalSlider", Style.getHorizontalSliderStyle());
        //     defaultItemsStyle.put("SpaceVIL.HorizontalSplitArea", Style.getHorizontalSplitAreaStyle());
        //     defaultItemsStyle.put("SpaceVIL.HorizontalStack", Style.getHorizontalStackStyle());
        //     defaultItemsStyle.put("SpaceVIL.Label", Style.getLabelStyle());
        //     defaultItemsStyle.put("SpaceVIL.ListArea", Style.getListAreaStyle());
        //     defaultItemsStyle.put("SpaceVIL.ListBox", Style.getListBoxStyle());
        //     defaultItemsStyle.put("SpaceVIL.MenuItem", Style.getMenuItemStyle());
        //     defaultItemsStyle.put("SpaceVIL.Indicator", Style.getIndicatorStyle());
        //     defaultItemsStyle.put("SpaceVIL.RadioButton", Style.getRadioButtonStyle());
        //     defaultItemsStyle.put("SpaceVIL.PasswordLine", Style.getPasswordLineStyle());
        //     defaultItemsStyle.put("SpaceVIL.TextEdit", Style.getTextEditStyle());
        //     defaultItemsStyle.put("SpaceVIL.TextBlock", Style.getTextBlockStyle());
        //     defaultItemsStyle.put("SpaceVIL.PopUpMessage", Style.getPopUpMessageStyle());
        //     defaultItemsStyle.put("SpaceVIL.ProgressBar", Style.getProgressBarStyle());
        //     defaultItemsStyle.put("SpaceVIL.ToolTip", Style.getToolTipStyle());
        //     defaultItemsStyle.put("SpaceVIL.TitleBar", Style.getTitleBarStyle());
        //     defaultItemsStyle.put("SpaceVIL.VerticalScrollBar", Style.getVerticalScrollBarStyle());
        //     defaultItemsStyle.put("SpaceVIL.VerticalSlider", Style.getVerticalSliderStyle());
        //     defaultItemsStyle.put("SpaceVIL.VerticalSplitArea", Style.getVerticalSplitAreaStyle());
        //     defaultItemsStyle.put("SpaceVIL.VerticalStack", Style.getVerticalStackStyle());
        //     defaultItemsStyle.put("SpaceVIL.TabView", Style.getTabViewStyle());
        // }
        if (ThemeStyle.applyEmbedded) {
            defaultItemsStyle.put(com.spvessel.Items.ButtonCore.class, Style.getButtonCoreStyle());
            defaultItemsStyle.put(com.spvessel.Items.ButtonToggle.class, Style.getButtonToggleStyle());
            defaultItemsStyle.put(com.spvessel.Items.CheckBox.class, Style.getCheckBoxStyle());
            defaultItemsStyle.put(com.spvessel.Items.ComboBox.class, Style.getComboBoxStyle());
            defaultItemsStyle.put(com.spvessel.Windows.ComboBoxDropDown.class, Style.getComboBoxDropDownStyle());
            defaultItemsStyle.put(com.spvessel.Items.ContextMenu.class, Style.getContextMenuStyle());
            defaultItemsStyle.put(com.spvessel.Items.Frame.class, Style.getFrameStyle());
            defaultItemsStyle.put(com.spvessel.Items.FreeArea.class, Style.getFlowAreaStyle());
            defaultItemsStyle.put(com.spvessel.Items.Grid.class, Style.getGridStyle());
            defaultItemsStyle.put(com.spvessel.Items.HorizontalScrollBar.class, Style.getHorizontalScrollBarStyle());
            defaultItemsStyle.put(com.spvessel.Items.HorizontalSlider.class, Style.getHorizontalSliderStyle());
            defaultItemsStyle.put(com.spvessel.Items.HorizontalSplitArea.class, Style.getHorizontalSplitAreaStyle());
            defaultItemsStyle.put(com.spvessel.Items.HorizontalStack.class, Style.getHorizontalStackStyle());
            defaultItemsStyle.put(com.spvessel.Items.Indicator.class, Style.getIndicatorStyle());
            defaultItemsStyle.put(com.spvessel.Items.Label.class, Style.getLabelStyle());
            defaultItemsStyle.put(com.spvessel.Items.ListArea.class, Style.getListAreaStyle());
            defaultItemsStyle.put(com.spvessel.Items.ListBox.class, Style.getListBoxStyle());
            defaultItemsStyle.put(com.spvessel.Items.MenuItem.class, Style.getMenuItemStyle());
            defaultItemsStyle.put(com.spvessel.Items.PasswordLine.class, Style.getPasswordLineStyle());
            defaultItemsStyle.put(com.spvessel.Items.PopUpMessage.class, Style.getPopUpMessageStyle());
            defaultItemsStyle.put(com.spvessel.Items.ProgressBar.class, Style.getProgressBarStyle());
            defaultItemsStyle.put(com.spvessel.Items.RadioButton.class, Style.getRadioButtonStyle());
            defaultItemsStyle.put(com.spvessel.Items.TabView.class, Style.getTabViewStyle());
            defaultItemsStyle.put(com.spvessel.Items.TextBlock.class, Style.getTextBlockStyle());
            defaultItemsStyle.put(com.spvessel.Items.TextEdit.class, Style.getTextEditStyle());
            defaultItemsStyle.put(com.spvessel.Items.TitleBar.class, Style.getTitleBarStyle());
            defaultItemsStyle.put(com.spvessel.Items.ToolTip.class, Style.getToolTipStyle());
            defaultItemsStyle.put(com.spvessel.Items.VerticalScrollBar.class, Style.getVerticalScrollBarStyle());
            defaultItemsStyle.put(com.spvessel.Items.VerticalSlider.class, Style.getVerticalSliderStyle());
            defaultItemsStyle.put(com.spvessel.Items.VerticalSplitArea.class, Style.getVerticalSplitAreaStyle());
            defaultItemsStyle.put(com.spvessel.Items.VerticalStack.class, Style.getVerticalStackStyle());

            defaultItemsStyle.put(com.spvessel.Items.TreeView.class, Style.getTreeViewStyle());
            defaultItemsStyle.put(com.spvessel.Items.TreeItem.class, Style.getTreeItemStyle());
        }
    }

    // public Style getThemeStyle(String type) {
    public Style getThemeStyle(Class<?> type) {
        if (defaultItemsStyle.containsKey(type))
            return defaultItemsStyle.get(type);
        return null;
    }

    private Map<BaseItem, Style> specificItemsStyle = new HashMap<BaseItem, Style>();

    public void setCurrentAsDefault() {
        DefaultsService.setDefaultTheme(this);
    }

    public void addSpecificItemStyle(BaseItem current_item, Style style) {
        if (specificItemsStyle.containsKey(current_item))
            specificItemsStyle.replace(current_item, style);
        else
            specificItemsStyle.put(current_item, style);
    }

    public void RemoveSpecificItemStyle(BaseItem current_item, Style style)
        {
            if (specificItemsStyle.containsKey(current_item))
                specificItemsStyle.remove(current_item);
        }

    // public Boolean ReplaceDefaultItemStyle(String class_type, Style style) {
    public Boolean ReplaceDefaultItemStyle(Class<?> class_type, Style style) {
        if (defaultItemsStyle.containsKey(class_type)) {
            defaultItemsStyle.replace(class_type, style);
            return true;
        }
        return false;
    }

    // public void addDefaultCustomItemStyle(String class_type, Style style) {
    public void addDefaultCustomItemStyle(Class<?> class_type, Style style) {
        if (defaultItemsStyle.containsKey(class_type))
            defaultItemsStyle.replace(class_type, style);
        else
            defaultItemsStyle.put(class_type, style);
    }
}