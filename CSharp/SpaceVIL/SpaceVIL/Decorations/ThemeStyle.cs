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
            {typeof(SpaceVIL.ButtonCore), Style.GetButtonCoreStyle()}
        };

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