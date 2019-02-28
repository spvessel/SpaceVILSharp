package com.spvessel.spacevil.Decorations;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.*;

import java.util.Map;
import java.util.HashMap;

public class ThemeStyle {
    private Map<Class<?>, Style> defaultItemsStyle = new HashMap<>();
    public static Boolean applyEmbedded = true;

    /**
     * Constructs a default ThemeStyle
     */
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
            defaultItemsStyle.put(FreeArea.class, Style.getFreeAreaStyle());
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
            // defaultItemsStyle.put(TextBlock.class, Style.getTextBlockStyle());
            // defaultItemsStyle.put(TextEncrypt.class, Style.getTextEncryptStyle());
            defaultItemsStyle.put(TextEdit.class, Style.getTextEditStyle());
            defaultItemsStyle.put(TitleBar.class, Style.getTitleBarStyle());
            // defaultItemsStyle.put(ToolTip.class, Style.getToolTipStyle());
            defaultItemsStyle.put(VerticalScrollBar.class, Style.getVerticalScrollBarStyle());
            defaultItemsStyle.put(VerticalSlider.class, Style.getVerticalSliderStyle());
            defaultItemsStyle.put(VerticalSplitArea.class, Style.getVerticalSplitAreaStyle());
            defaultItemsStyle.put(VerticalStack.class, Style.getVerticalStackStyle());

            defaultItemsStyle.put(TreeView.class, Style.getTreeViewStyle());
            defaultItemsStyle.put(TreeItem.class, Style.getTreeItemStyle());
            defaultItemsStyle.put(SpinItem.class, Style.getSpinItemStyle());

            //new
            defaultItemsStyle.put(DialogItem.class, Style.getDialogItemStyle());
            defaultItemsStyle.put(MessageItem.class, Style.getMessageItemStyle());
            defaultItemsStyle.put(InputDialog.class, Style.getInputDialogStyle());
            defaultItemsStyle.put(WContainer.class, Style.getWindowContainerStyle());
            defaultItemsStyle.put(OpenEntryDialog.class, Style.getOpenEntryDialogStyle());
            defaultItemsStyle.put(FileSystemEntry.class, Style.getFileSystemEntryStyle());
        }
    }

    // public Style getThemeStyle(String type) {

    /**
     * Returns style of the theme for the object by its class name
     */
    public Style getThemeStyle(Class<?> type) {
        if (defaultItemsStyle.containsKey(type))
            return defaultItemsStyle.get(type);
        return null;
    }

    private Map<InterfaceBaseItem, Style> specificItemsStyle = new HashMap<>();

    /**
     * Set this theme as default
     */
    public void setCurrentAsDefault() {
        DefaultsService.setDefaultTheme(this);
    }

    /**
     * Add unique style for the item
     */
    public void addSpecificItemStyle(InterfaceBaseItem current_item, Style style) {
        if (specificItemsStyle.containsKey(current_item))
            specificItemsStyle.replace(current_item, style);
        else
            specificItemsStyle.put(current_item, style);
    }

    /**
     * Remove unique style for the item
     */
    public void RemoveSpecificItemStyle(InterfaceBaseItem current_item, Style style)
        {
            if (specificItemsStyle.containsKey(current_item))
                specificItemsStyle.remove(current_item);
        }

    // public Boolean ReplaceDefaultItemStyle(String class_type, Style style) {

    /**
     * Replace default style for the items with class name class_type
     */
    public Boolean ReplaceDefaultItemStyle(Class<?> class_type, Style style) {
        if (defaultItemsStyle.containsKey(class_type)) {
            defaultItemsStyle.replace(class_type, style);
            return true;
        }
        return false;
    }

    // public void addDefaultCustomItemStyle(String class_type, Style style) {

    /**
     * Add custom style to default theme for the items with class name class_type
     */
    public void addDefaultCustomItemStyle(Class<?> class_type, Style style) {
        if (defaultItemsStyle.containsKey(class_type))
            defaultItemsStyle.replace(class_type, style);
        else
            defaultItemsStyle.put(class_type, style);
    }
}