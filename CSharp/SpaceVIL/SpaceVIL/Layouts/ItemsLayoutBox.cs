using System;
using System.Collections.Generic;
using System.Linq;
using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// ItemsLayoutBox is a storage-class that provides an access to existing items.
    /// </summary>
    public static class ItemsLayoutBox
    {
        /// <summary>
        /// Getting existing static items in specified window by its GUID. 
        /// Static items are items that depend on their parent. Parent controls their size, position and etc.
        /// </summary>
        /// <param name="id">GUID of the window.</param>
        /// <returns>The list of existing static items in specified window by its GUID.</returns>
        public static List<IBaseItem> GetLayoutItems(Guid id)
        {
            // return layouts[id].Items.Concat(layouts[id].FloatItems).ToList();
            return layouts[id].Items;
        }
        
        /// <summary>
        /// Getting existing float items in specified window by its GUID. 
        /// Floating items are independent items that do not have a parent, or their root parent is a floating item. 
        /// Examples: SideArea, FloatItem, ContextMenu and etc.
        /// </summary>
        /// <param name="id">GUID of the window.</param>
        /// <returns>The list of existing float items in specified window by its GUID.</returns>
        public static List<IBaseItem> GetLayoutFloatItems(Guid id)
        {
            // return layouts[id].Items.Concat(layouts[id].FloatItems).ToList();
            return layouts[id].FloatItems;
        }


        internal static ItemsLayout GetLayout(Guid id)
        {
            return layouts[id];
        }

        static private Dictionary<Guid, ItemsLayout> layouts = new Dictionary<Guid, ItemsLayout>();

        internal static void InitLayout(Guid _layout)
        {
            ItemsLayout l = new ItemsLayout(_layout);
            layouts.Add(l.Id, l);
        }

        /// <summary>
        /// Adding an item to global item storage (ItemsLayoutBox). 
        /// In usual situation you do not need to use this function only if you create your own implementation of IBaseItem or 
        /// create a new implementation of IFloatItem.
        /// </summary>
        /// <param name="window">Any CoreWindow instance.</param>
        /// <param name="item">Any IBaseItem instance.</param>
        /// <param name="type">Type of an item: SpaceVIL.Core.LayoutType.Static 
        /// or SpaceVIL.Core.LayoutType.Floating.</param>
        public static void AddItem(CoreWindow window, IBaseItem item, LayoutType type)
        {
            switch (type)
            {
                case LayoutType.Static:
                    layouts[window.GetWindowGuid()].Items.Add(item);
                    break;
                case LayoutType.Floating:
                    {
                        layouts[window.GetWindowGuid()].FloatItems.Add(item);
                        item.SetHandler(window);
                        break;
                    }
                case LayoutType.Dialog:
                    layouts[window.GetWindowGuid()].DialogItems.Add(item);
                    break;
                default:
                    layouts[window.GetWindowGuid()].Items.Add(item);
                    break;
            }
        }

        /// <summary>
        /// Removing an item from global item storage (ItemsLayoutBox). 
        /// In usual situation you do not need to use this function only if you create your own implementation of IBaseItem or 
        /// want to remove IFloatItem instance.
        /// </summary>
        /// <param name="window">Any CoreWindow instance.</param>
        /// <param name="item">Any IBaseItem instance.</param>
        /// <param name="type">Type of an item: SpaceVIL.Core.LayoutType.Static 
        /// or SpaceVIL.Core.LayoutType.Floating</param>
        /// <returns>True: if removal was successfull. False: if the specified item does not exist in the storage.</returns>
        public static bool RemoveItem(CoreWindow window, IBaseItem item, LayoutType type)
        {
            window.FreeVRAMResource(item);
            
            switch (type)
            {
                case LayoutType.Static:
                    return layouts[window.GetWindowGuid()].Items.Remove(item);
                // break;
                case LayoutType.Floating:
                    {
                        UnsubscribeWindowSizeMonitoring(item, GeometryEventType.ResizeWidth);
                        UnsubscribeWindowSizeMonitoring(item, GeometryEventType.ResizeHeight);
                        return layouts[window.GetWindowGuid()].FloatItems.Remove(item);
                    }
                // break;
                case LayoutType.Dialog:
                    return layouts[window.GetWindowGuid()].DialogItems.Remove(item);
                // break;
                default:
                    return layouts[window.GetWindowGuid()].Items.Remove(item);
                    // break;
            }
        }

        internal static void SubscribeWindowSizeMonitoring(IBaseItem item, GeometryEventType type)
        {
            //подписка
            item.SetParent(item.GetHandler().GetLayout().GetContainer());
            item.GetHandler().GetLayout().GetContainer().AddEventListener(type, item);
        }
        internal static void UnsubscribeWindowSizeMonitoring(IBaseItem item, GeometryEventType type)
        {
            //отписка
            item.SetParent(null);
            item.GetHandler().GetLayout().GetContainer().RemoveEventListener(type, item);
        }

        /// <summary>
        /// Getting the list of names of existing items in the specified window.
        /// </summary>
        /// <param name="window">Any CoreWindow instance.</param>
        /// <returns>The list of names of existing items in the specified window.</returns>
        public static List<string> GetListOfItemsNames(CoreWindow window) => 
            layouts[window.GetWindowGuid()].Items.Select(_ => _.GetItemName()).ToList();

        internal static string[] GetListOfItemsColors(CoreWindow layout) => 
            layouts[layout.GetWindowGuid()].Items.Select(_ => _.GetBackground().ToString()).ToArray();
        
        /// <summary>
        /// Printing all existing items in the specified window.
        /// </summary>
        /// <param name="window">Any CoreWindow instance.</param>
        public static void PrintListOfItems(CoreWindow window)
        {
            List<string> list = GetListOfItemsNames(window);
            foreach (var item in list)
            {
                Console.WriteLine(item);
            }
        }
    }
}
