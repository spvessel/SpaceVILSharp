package com.spvessel;

import com.spvessel.Common.DefaultsService;
import com.spvessel.Decorations.Style;

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
            defaultItemsStyle.put(ButtonCore.class, Style.getButtonCoreStyle());
            defaultItemsStyle.put(ButtonToggle.class, Style.getButtonToggleStyle());
            defaultItemsStyle.put(CheckBox.class, Style.getCheckBoxStyle());
            defaultItemsStyle.put(ComboBox.class, Style.getComboBoxStyle());
            defaultItemsStyle.put(ComboBoxDropDown.class, Style.getComboBoxDropDownStyle());
            defaultItemsStyle.put(ContextMenu.class, Style.getContextMenuStyle());
            defaultItemsStyle.put(Frame.class, Style.getFrameStyle());
            defaultItemsStyle.put(FreeArea.class, Style.getFlowAreaStyle());
            defaultItemsStyle.put(Grid.class, Style.getGridStyle());
            defaultItemsStyle.put(HorizontalScrollBar.class, Style.getHorizontalScrollBarStyle());
            defaultItemsStyle.put(HorizontalSlider.class, Style.getHorizontalSliderStyle());
            defaultItemsStyle.put(HorizontalSplitArea.class, Style.getHorizontalSplitAreaStyle());
            defaultItemsStyle.put(HorizontalStack.class, Style.getHorizontalStackStyle());
            defaultItemsStyle.put(Indicator.class, Style.getIndicatorStyle());
            defaultItemsStyle.put(Label.class, Style.getLabelStyle());
            defaultItemsStyle.put(ListArea.class, Style.getListAreaStyle());
            defaultItemsStyle.put(ListBox.class, Style.getListBoxStyle());
            defaultItemsStyle.put(MenuItem.class, Style.getMenuItemStyle());
            defaultItemsStyle.put(PasswordLine.class, Style.getPasswordLineStyle());
            defaultItemsStyle.put(PopUpMessage.class, Style.getPopUpMessageStyle());
            defaultItemsStyle.put(ProgressBar.class, Style.getProgressBarStyle());
            defaultItemsStyle.put(RadioButton.class, Style.getRadioButtonStyle());
            defaultItemsStyle.put(TabView.class, Style.getTabViewStyle());
            defaultItemsStyle.put(TextArea.class, Style.getTextAreaStyle());
            defaultItemsStyle.put(TextBlock.class, Style.getTextBlockStyle());
            defaultItemsStyle.put(TextEdit.class, Style.getTextEditStyle());
            defaultItemsStyle.put(TitleBar.class, Style.getTitleBarStyle());
            defaultItemsStyle.put(ToolTip.class, Style.getToolTipStyle());
            defaultItemsStyle.put(VerticalScrollBar.class, Style.getVerticalScrollBarStyle());
            defaultItemsStyle.put(VerticalSlider.class, Style.getVerticalSliderStyle());
            defaultItemsStyle.put(VerticalSplitArea.class, Style.getVerticalSplitAreaStyle());
            defaultItemsStyle.put(VerticalStack.class, Style.getVerticalStackStyle());

            defaultItemsStyle.put(TreeView.class, Style.getTreeViewStyle());
            defaultItemsStyle.put(TreeItem.class, Style.getTreeItemStyle());
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