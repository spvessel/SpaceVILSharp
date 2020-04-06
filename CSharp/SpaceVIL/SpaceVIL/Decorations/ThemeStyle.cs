using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using SpaceVIL.Core;
using SpaceVIL.Common;

namespace SpaceVIL.Decorations
{
    /// <summary>
    /// A style theme to store styles for items in the current application.
    /// </summary>
    public class ThemeStyle
    {
        private Dictionary<Type, Style> DefaultItemsStyle = new Dictionary<Type, Style>();

        public static bool ApplyEmbedded = true;

        /// <summary>
        /// Constructs a default ThemeStyle.
        /// </summary>
        public ThemeStyle()
        {
            if (ThemeStyle.ApplyEmbedded)
            {
                DefaultItemsStyle.Add(typeof(SpaceVIL.ButtonCore), Style.GetButtonCoreStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ButtonToggle), Style.GetButtonToggleStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.CheckBox), Style.GetCheckBoxStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ComboBox), Style.GetComboBoxStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ComboBoxDropDown), Style.GetComboBoxDropDownStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ContextMenu), Style.GetContextMenuStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.FreeArea), Style.GetFreeAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.Frame), Style.GetFrameStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.Grid), Style.GetGridStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.HorizontalScrollBar), Style.GetHorizontalScrollBarStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.HorizontalSlider), Style.GetHorizontalSliderStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.HorizontalSplitArea), Style.GetHorizontalSplitAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.HorizontalStack), Style.GetHorizontalStackStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.Label), Style.GetLabelStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ListArea), Style.GetListAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ListBox), Style.GetListBoxStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.MenuItem), Style.GetMenuItemStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.Indicator), Style.GetIndicatorStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.RadioButton), Style.GetRadioButtonStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.PasswordLine), Style.GetPasswordLineStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TextEdit), Style.GetTextEditStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TextArea), Style.GetTextAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TextView), Style.GetTextViewStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.PopUpMessage), Style.GetPopUpMessageStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ProgressBar), Style.GetProgressBarStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ToolTipItem), Style.GetToolTipStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TitleBar), Style.GetTitleBarStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.VerticalScrollBar), Style.GetVerticalScrollBarStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.VerticalSlider), Style.GetVerticalSliderStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.VerticalSplitArea), Style.GetVerticalSplitAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.VerticalStack), Style.GetVerticalStackStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TabView), Style.GetTabViewStyle());

                DefaultItemsStyle.Add(typeof(SpaceVIL.TreeView), Style.GetTreeViewStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TreeItem), Style.GetTreeItemStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.SpinItem), Style.GetSpinItemStyle());

                DefaultItemsStyle.Add(typeof(SpaceVIL.DialogItem), Style.GetDialogItemStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.MessageItem), Style.GetMessageItemStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.InputDialog), Style.GetInputDialogStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.WContainer), Style.GetWindowContainerStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.OpenEntryDialog), Style.GetOpenEntryDialogStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.FileSystemEntry), Style.GetFileSystemEntryStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.WrapArea), Style.GetWrapAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.WrapGrid), Style.GetWrapGridStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.SideArea), Style.GetSideAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ImageItem), Style.GetImageItemStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.LoadingScreen), Style.GetLoadingScreenStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.Tab), Style.GetTabStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TabBar), Style.GetTabBarStyle());
            }
        }

        /// <summary>
        /// Returns style of the theme for the object by its class name.
        /// </summary>
        /// <param name="type">Type of an item as System.Type.</param>
        /// <returns>Assigned style as SpaceVIL.Decorations.Style.</returns>
        public Style GetThemeStyle(Type type)
        {
            if (DefaultItemsStyle.ContainsKey(type))
                return DefaultItemsStyle[type];
            return null;
        }

        private ConcurrentDictionary<IBaseItem, Style> SpecificItemsStyle = new ConcurrentDictionary<IBaseItem, Style>();

        /// <summary>
        /// Setting this theme as default.
        /// </summary>
        public void SetCurrentAsDefault()
        {
            DefaultsService.SetDefaultTheme(this);
        }

        /// <summary>
        /// Add unique style for the specified item (specific item, not type of item).
        /// </summary>
        /// <param name="item">An item as SpaceVIL.Core.IBaseItem.</param>
        /// <param name="style">A style as SpaceVIL.Decorations.Style.</param>
        public void AddSpecificItemStyle(IBaseItem item, Style style)
        {
            if (SpecificItemsStyle.ContainsKey(item))
                SpecificItemsStyle[item] = style;
            else
                SpecificItemsStyle.TryAdd(item, style);
        }

        /// <summary>
        /// Remove unique style for the item (specific item, not type of item).
        /// </summary>
        /// <param name="item">An item as SpaceVIL.Core.IBaseItem.</param>
        /// <param name="style">A style as SpaceVIL.Decorations.Style.</param>
        public void RemoveSpecificItemStyle(IBaseItem item, Style style)
        {
            if (SpecificItemsStyle.ContainsKey(item))
                SpecificItemsStyle.TryRemove(item, out style);
        }

        /// <summary>
        /// Replace default style for the items with specified class type.
        /// </summary>
        /// <param name="type">Type of an item as System.Type.</param>
        /// <param name="style">A style as SpaceVIL.Decorations.Style.</param>
        /// <returns></returns>
        public bool ReplaceDefaultItemStyle(Type type, Style style)
        {
            if (DefaultItemsStyle.ContainsKey(type))
            {
                DefaultItemsStyle[type] = style;
                return true;
            }
            return false;
        }

        /// <summary>
        /// Add custom style to default theme for the items with specified class type.
        /// </summary>
        /// <param name="type">Type of an item as System.Type.</param>
        /// <param name="style">A style as SpaceVIL.Decorations.Style.</param>
        public void AddDefaultCustomItemStyle(Type type, Style style)
        {
            if (DefaultItemsStyle.ContainsKey(type))
                DefaultItemsStyle[type] = style;
            else
                DefaultItemsStyle.Add(type, style);
        }
    }
}