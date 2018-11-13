using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SpaceVIL.Core;

namespace SpaceVIL
{
    static class ItemsLayoutBox
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
        static public ItemsLayout GetLayout(Guid id)
        {
            return layouts[id];
        }

        static private Dictionary<Guid, ItemsLayout> layouts = new Dictionary<Guid, ItemsLayout>();

        static internal void InitLayout(Guid _layout)
        {
            ItemsLayout l = new ItemsLayout(_layout);
            layouts.Add(l.Id, l);
        }
        static internal void AddItem(WindowLayout layout, IBaseItem item, LayoutType type)
        {
            switch (type)
            {
                case LayoutType.Static:
                    layouts[layout.Id].Items.Add(item);
                    break;
                case LayoutType.Floating:
                    layouts[layout.Id].FloatItems.Add(item);
                    break;
                default:
                    layouts[layout.Id].Items.Add(item);
                    break;
            }
        }
        static internal void RemoveItem(WindowLayout layout, IBaseItem item, LayoutType type)
        {
            switch (type)
            {
                case LayoutType.Static:
                    layouts[layout.Id].Items.Remove(item);
                    break;
                case LayoutType.Floating:
                    layouts[layout.Id].FloatItems.Remove(item);
                    break;
                default:
                    layouts[layout.Id].Items.Remove(item);
                    break;
            }
        }

        static public string[] GetListOfItemsNames(WindowLayout layout) => layouts[layout.Id].Items.Select(_ => _.GetItemName()).ToArray();
        static public string[] GetListOfItemsColors(WindowLayout layout) => layouts[layout.Id].Items.Select(_ => _.GetBackground().ToString()).ToArray();
        static public void PrintListOfItems(WindowLayout layout)
        {
            string[] list = GetListOfItemsNames(layout);
            foreach (var item in list)
            {
                Console.WriteLine(item);
            }
        }
    }
}
