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
        private static ThemeStyle _instance;
        private ThemeStyle() { }
        public static ThemeStyle GetInstance()
        {
            if(_instance == null)
                _instance = new ThemeStyle();
            return _instance;
        }
        private Dictionary<Type, Style> DefaultItemsStyle = new Dictionary<Type, Style>()
        {
            {typeof(SpaceVIL.ButtonCore), Style.GetButtonCoreStyle()},
            {typeof(SpaceVIL.ButtonToggle), Style.GetButtonToggleStyle()},
            {typeof(SpaceVIL.CheckBox), Style.GetCheckBoxStyle()},
            {typeof(SpaceVIL.ComboBox), Style.GetComboBoxStyle()},
            {typeof(SpaceVIL.ComboBoxDropDown), Style.GetComboBoxDropDownStyle()},
            {typeof(SpaceVIL.ContextMenu), Style.GetContextMenuStyle()},
            // {typeof(SpaceVIL.CustomSelector), Style.GetCustomSelectorStyle()},
            {typeof(SpaceVIL.FlowArea), Style.GetFlowAreaStyle()},
            {typeof(SpaceVIL.Frame), Style.GetFrameStyle()},
            {typeof(SpaceVIL.Grid), Style.GetGridStyle()},
            {typeof(SpaceVIL.HorizontalScrollBar), Style.GetHorizontalScrollBarStyle()},
            {typeof(SpaceVIL.HorizontalSlider), Style.GetHorizontalSliderStyle()},
            {typeof(SpaceVIL.HorizontalSplitArea), Style.GetHorizontalSplitAreaStyle()},
            {typeof(SpaceVIL.HorizontalStack), Style.GetHorizontalStackStyle()},
            {typeof(SpaceVIL.Label), Style.GetLabelStyle()},
            {typeof(SpaceVIL.ListArea), Style.GetListAreaStyle()},
            {typeof(SpaceVIL.ListBox), Style.GetListBoxStyle()},
            {typeof(SpaceVIL.MenuItem), Style.GetMenuItemStyle()},
            {typeof(SpaceVIL.Indicator), Style.GetIndicatorStyle()},
            {typeof(SpaceVIL.VerticalScrollBar), Style.GetVerticalScrollBarStyle()},
            {typeof(SpaceVIL.VerticalSlider), Style.GetVerticalSliderStyle()},
            {typeof(SpaceVIL.VerticalSplitArea), Style.GetVerticalSplitAreaStyle()},
            {typeof(SpaceVIL.VerticalStack), Style.GetVerticalStackStyle()},
            {typeof(SpaceVIL.RadioButton), Style.GetRadioButtonStyle()},
            {typeof(SpaceVIL.WContainer), Style.GetWContainerStyle()},


            // {typeof(SpaceVIL.PasswordLine), Style.GetPasswordLineStyle()},
            // {typeof(SpaceVIL.PopUpMessage), Style.GetPopUpMessageStyle()},
            // {typeof(SpaceVIL.ProgressBar), Style.GetProgressBarStyle()},
            // {typeof(SpaceVIL.TabView), Style.GetTabViewStyle()},
            // {typeof(SpaceVIL.TextBlock), Style.GetTextBlockStyle()},
            // {typeof(SpaceVIL.TextEdit), Style.GetTextEditStyle()},
            // {typeof(SpaceVIL.TitleBar), Style.GetTitleBarStyle()},
            // {typeof(SpaceVIL.ToolTip), Style.GetToolTipStyle()},
        };
        public Style GetThemeStyle(Type type)
        {
            return DefaultItemsStyle[type];
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