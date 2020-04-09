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
            defaultItemsStyle.put(TextView.class, Style.getTextViewStyle());
            // defaultItemsStyle.put(TextBlock.class, Style.getTextBlockStyle());
            // defaultItemsStyle.put(TextEncrypt.class, Style.getTextEncryptStyle());
            defaultItemsStyle.put(TextEdit.class, Style.getTextEditStyle());
            defaultItemsStyle.put(TitleBar.class, Style.getTitleBarStyle());
            defaultItemsStyle.put(ToolTipItem.class, Style.getToolTipStyle());
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
            // defaultItemsStyle.put(WContainer.class, Style.getWindowContainerStyle());
            defaultItemsStyle.put(OpenEntryDialog.class, Style.getOpenEntryDialogStyle());
            defaultItemsStyle.put(FileSystemEntry.class, Style.getFileSystemEntryStyle());
            // defaultItemsStyle.put(SelectionItem.class, Style.getSelectionItemStyle());
            defaultItemsStyle.put(WrapArea.class, Style.getWrapAreaStyle());
            defaultItemsStyle.put(WrapGrid.class, Style.getWrapGridStyle());
            defaultItemsStyle.put(SideArea.class, Style.getSideAreaStyle());
            defaultItemsStyle.put(ImageItem.class, Style.getImageItemStyle());
            defaultItemsStyle.put(LoadingScreen.class, Style.getLoadingScreenStyle());
            defaultItemsStyle.put(Tab.class, Style.getTabStyle());
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
    public void removeSpecificItemStyle(InterfaceBaseItem current_item, Style style)
        {
            if (specificItemsStyle.containsKey(current_item))
                specificItemsStyle.remove(current_item);
        }

    // public Boolean ReplaceDefaultItemStyle(String class_type, Style style) {

    /**
     * Replace default style for the items with class name class_type
     */
    public Boolean replaceDefaultItemStyle(Class<?> class_type, Style style) {
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