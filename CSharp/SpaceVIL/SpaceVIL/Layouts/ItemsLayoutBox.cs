using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// A storage-class that provides an access to existing ItemLayouts by Guid
    /// </summary>
    public static class ItemsLayoutBox
    {
        static public List<IBaseItem> GetLayoutItems(Guid id)
        {
            // return layouts[id].Items.Concat(layouts[id].FloatItems).ToList();
            return layouts[id].Items;
        }
        static public List<IBaseItem> GetLayoutFloatItems(Guid id)
        {
            // return layouts[id].Items.Concat(layouts[id].FloatItems).ToList();
            return layouts[id].FloatItems;
        }
        static internal ItemsLayout GetLayout(Guid id)
        {
            return layouts[id];
        }

        static private Dictionary<Guid, ItemsLayout> layouts = new Dictionary<Guid, ItemsLayout>();

        static internal void InitLayout(Guid _layout)
        {
            ItemsLayout l = new ItemsLayout(_layout);
            layouts.Add(l.Id, l);
        }
        static internal void AddItem(CoreWindow layout, IBaseItem item, LayoutType type)
        {
            switch (type)
            {
                case LayoutType.Static:
                    layouts[layout.GetWindowGuid()].Items.Add(item);
                    break;
                case LayoutType.Floating:
                    layouts[layout.GetWindowGuid()].FloatItems.Add(item);
                    break;
                case LayoutType.Dialog:
                    layouts[layout.GetWindowGuid()].DialogItems.Add(item);
                    break;
                default:
                    layouts[layout.GetWindowGuid()].Items.Add(item);
                    break;
            }
        }
        static internal bool RemoveItem(CoreWindow layout, IBaseItem item, LayoutType type)
        {
            switch (type)
            {
                case LayoutType.Static:
                    return layouts[layout.GetWindowGuid()].Items.Remove(item);
                    // break;
                case LayoutType.Floating:
                    return layouts[layout.GetWindowGuid()].FloatItems.Remove(item);
                    // break;
                case LayoutType.Dialog:
                    return layouts[layout.GetWindowGuid()].DialogItems.Remove(item);
                    // break;
                default:
                    return layouts[layout.GetWindowGuid()].Items.Remove(item);
                    // break;
            }
        }

        static public string[] GetListOfItemsNames(CoreWindow layout) => layouts[layout.GetWindowGuid()].Items.Select(_ => _.GetItemName()).ToArray();
        static public string[] GetListOfItemsColors(CoreWindow layout) => layouts[layout.GetWindowGuid()].Items.Select(_ => _.GetBackground().ToString()).ToArray();
        static public void PrintListOfItems(CoreWindow layout)
        {
            string[] list = GetListOfItemsNames(layout);
            foreach (var item in list)
            {
                Console.WriteLine(item);
            }
        }
    }
}
