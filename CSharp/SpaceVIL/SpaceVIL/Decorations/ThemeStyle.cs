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
        private ConcurrentDictionary<String, Style> DefaultItemsStyle = new ConcurrentDictionary<String, Style>();
        private ConcurrentDictionary<BaseItem, Style> SpecificItemsStyle = new ConcurrentDictionary<BaseItem, Style>();

        public ThemeStyle() { }

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

        public bool ReplaceDefaultItemStyle(String class_name, Style style)
        {
            if (DefaultItemsStyle.ContainsKey(class_name))
            {
                DefaultItemsStyle[class_name] = style;
                return true;
            }
            return false;
        }
        public void AddDefaultCustomItemStyle(String class_name, Style style)
        {
            if (DefaultItemsStyle.ContainsKey(class_name))
                DefaultItemsStyle[class_name] = style;
            else
                DefaultItemsStyle.TryAdd(class_name, style);
        }
    }
}