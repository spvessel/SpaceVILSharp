using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class ThemeStyle
    {
        private Dictionary<Type, Style> DefaultItemsStyle = new Dictionary<Type, Style>();
        // {
        //     {typeof(SpaceVIL.ButtonCore), Style.GetButtonCoreStyle()},
        //     {typeof(SpaceVIL.ButtonToggle), Style.GetButtonToggleStyle()},
        //     {typeof(SpaceVIL.CheckBox), Style.GetCheckBoxStyle()},
        //     {typeof(SpaceVIL.ComboBox), Style.GetComboBoxStyle()},
        //     {typeof(SpaceVIL.ComboBoxDropDown), Style.GetComboBoxDropDownStyle()},
        //     {typeof(SpaceVIL.ContextMenu), Style.GetContextMenuStyle()},
        //     {typeof(SpaceVIL.FlowArea), Style.GetFlowAreaStyle()},
        //     {typeof(SpaceVIL.Frame), Style.GetFrameStyle()},
        //     {typeof(SpaceVIL.Grid), Style.GetGridStyle()},
        //     {typeof(SpaceVIL.HorizontalScrollBar), Style.GetHorizontalScrollBarStyle()},
        //     {typeof(SpaceVIL.HorizontalSlider), Style.GetHorizontalSliderStyle()},
        //     {typeof(SpaceVIL.HorizontalSplitArea), Style.GetHorizontalSplitAreaStyle()},
        //     {typeof(SpaceVIL.HorizontalStack), Style.GetHorizontalStackStyle()},
        //     {typeof(SpaceVIL.Label), Style.GetLabelStyle()},
        //     {typeof(SpaceVIL.ListArea), Style.GetListAreaStyle()},
        //     {typeof(SpaceVIL.ListBox), Style.GetListBoxStyle()},
        //     {typeof(SpaceVIL.MenuItem), Style.GetMenuItemStyle()},
        //     {typeof(SpaceVIL.Indicator), Style.GetIndicatorStyle()},
        //     {typeof(SpaceVIL.RadioButton), Style.GetRadioButtonStyle()},
        //     {typeof(SpaceVIL.PasswordLine), Style.GetPasswordLineStyle()},
        //     {typeof(SpaceVIL.TextEdit), Style.GetTextEditStyle()},
        //     {typeof(SpaceVIL.TextBlock), Style.GetTextBlockStyle()},
        //     {typeof(SpaceVIL.PopUpMessage), Style.GetPopUpMessageStyle()},
        //     {typeof(SpaceVIL.ProgressBar), Style.GetProgressBarStyle()},
        //     {typeof(SpaceVIL.ToolTip), Style.GetToolTipStyle()},
        //     {typeof(SpaceVIL.TitleBar), Style.GetTitleBarStyle()},
        //     {typeof(SpaceVIL.VerticalScrollBar), Style.GetVerticalScrollBarStyle()},
        //     {typeof(SpaceVIL.VerticalSlider), Style.GetVerticalSliderStyle()},
        //     {typeof(SpaceVIL.VerticalSplitArea), Style.GetVerticalSplitAreaStyle()},
        //     {typeof(SpaceVIL.VerticalStack), Style.GetVerticalStackStyle()},
        //     {typeof(SpaceVIL.TabView), Style.GetTabViewStyle()},
        //     // {typeof(SpaceVIL.TextItem), Style.GetTextItemStyle()},

        //     // {typeof(SpaceVIL.CustomSelector), Style.GetCustomSelectorStyle()},
        //     // {typeof(SpaceVIL.WContainer), Style.GetWContainerStyle()},
        // };

        public static bool ApplyEmbedded = true;
        // private static ThemeStyle _instance;
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
                DefaultItemsStyle.Add(typeof(SpaceVIL.FlowArea), Style.GetFlowAreaStyle());
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
                DefaultItemsStyle.Add(typeof(SpaceVIL.TextBlock), Style.GetTextBlockStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TextArea), Style.GetTextAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.PopUpMessage), Style.GetPopUpMessageStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ProgressBar), Style.GetProgressBarStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.ToolTip), Style.GetToolTipStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TitleBar), Style.GetTitleBarStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.VerticalScrollBar), Style.GetVerticalScrollBarStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.VerticalSlider), Style.GetVerticalSliderStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.VerticalSplitArea), Style.GetVerticalSplitAreaStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.VerticalStack), Style.GetVerticalStackStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TabView), Style.GetTabViewStyle());

                DefaultItemsStyle.Add(typeof(SpaceVIL.TreeView), Style.GetTreeViewStyle());
                DefaultItemsStyle.Add(typeof(SpaceVIL.TreeItem), Style.GetTreeItemStyle());
            }
        }
        // public static ThemeStyle GetInstance()
        // {
        //     if (_instance == null)
        //         _instance = new ThemeStyle();
        //     return _instance;
        // }
        public Style GetThemeStyle(Type type)
        {
            if (DefaultItemsStyle.ContainsKey(type))
                return DefaultItemsStyle[type];
            return null;
        }

        private ConcurrentDictionary<BaseItem, Style> SpecificItemsStyle = new ConcurrentDictionary<BaseItem, Style>();

        public void SetCurrentAsDefault()
        {
            DefaultsService.SetDefaultTheme(this);
        }

        public void AddSpecificItemStyle(BaseItem current_item, Style style)
        {
            if (SpecificItemsStyle.ContainsKey(current_item))
                SpecificItemsStyle[current_item] = style;
            else
                SpecificItemsStyle.TryAdd(current_item, style);
        }

        public void RemoveSpecificItemStyle(BaseItem current_item, Style style)
        {
            if (SpecificItemsStyle.ContainsKey(current_item))
                SpecificItemsStyle.TryRemove(current_item, out style);
        }

        public bool ReplaceDefaultItemStyle(Type class_type, Style style)
        {
            if (DefaultItemsStyle.ContainsKey(class_type))
            {
                DefaultItemsStyle[class_type] = style;
                return true;
            }
            return false;
        }
        public void AddDefaultCustomItemStyle(Type class_type, Style style)
        {
            if (DefaultItemsStyle.ContainsKey(class_type))
                DefaultItemsStyle[class_type] = style;
            else
                DefaultItemsStyle.Add(class_type, style);
        }
    }
}