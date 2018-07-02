using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    static class ItemsLayoutBox
    {
        static public List<IItem> GetLayoutItems(int id)
        {
            return layouts[id].Items.Select(_ => _ as IItem).ToList();
        }

        static private Dictionary<int, ItemsLayout> layouts = new Dictionary<int, ItemsLayout>();

        static internal void InitLayout(int _layout)
        {
            ItemsLayout l = new ItemsLayout(_layout);
            layouts.Add(l.Id, l);
        }
        static internal void AddItem(WindowLayout layout, IItem item)
        {
            layouts[layout.Id].Items.Add(item);

            if (item is IUserItem)
                (item as IUserItem).Init();
        }
        static internal void RemoveItem(WindowLayout layout, IItem item)
        {
            layouts[layout.Id].Items.Remove(item);
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
