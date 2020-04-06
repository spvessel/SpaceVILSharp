using System;
using System.Collections.Generic;
using System.Threading;
using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// ItemsRefreshManager is a manager that allows you to add an item of a certain type to the queue for a forced refresh. 
    /// It can be use with custom implementation of one of the types: shape, text, image. 
    /// For example: a Bitmap change in an IImageItem implementation, a shape change in an IBaseItem implementation.
    /// </summary>
    public static class ItemsRefreshManager
    {
        private static Object _lockShapeObject = new Object();
        private static Object _lockTextObject = new Object();
        private static Object _lockImageObject = new Object();

        private static HashSet<IBaseItem> _shapes = new HashSet<IBaseItem>();
        private static HashSet<ITextContainer> _texts = new HashSet<ITextContainer>();
        private static HashSet<IImageItem> _images = new HashSet<IImageItem>();

        private static VisualItem CastVisualItem(IBaseItem item)
        {
            VisualItem vi = item as VisualItem;
            return vi;
        }

        /// <summary>
        /// Adding an IBaseItem implementation to the queue for a forced refresh.
        /// <para/>Tips: use this function only if you want to refresh shape of an item, 
        /// ITextContainer and IImageItem are not shapes.
        /// </summary>
        /// <param name="item">An item as SpaceVIL.Core.IBaseItem.</param>
        /// <returns>True: if adding is successfull. False: if an item is already in the refresh queue.</returns>
        public static bool SetRefreshShape(IBaseItem item)
        {
            if (item is ITextContainer
            // || (item.GetBackground().A == 0)
            )
            {
                return false;
            }
            VisualItem vi = CastVisualItem(item);

            Monitor.Enter(_lockShapeObject);
            try
            {
                if (vi == null)
                    return _shapes.Add(item);
                return _shapes.Add(vi.prototype);
            }
            finally
            {
                Monitor.Exit(_lockShapeObject);
            }
        }
        /// <summary>
        /// Adding an ITextContainer implementation to the queue for a forced refresh.
        /// <para/>Tips: use this function only if you want to refresh text of an item, 
        /// IBaseItem and IImageItem are not text.
        /// </summary>
        /// <param name="item">An item as SpaceVIL.Core.ITextContainer.</param>
        /// <returns>True: if adding is successfull. False: if an item is already in the refresh queue.</returns>
        public static bool SetRefreshText(ITextContainer item)
        {
            Monitor.Enter(_lockTextObject);
            try
            {
                return _texts.Add(item);
            }
            finally
            {
                Monitor.Exit(_lockTextObject);
            }
        }
        /// <summary>
        /// Adding an IImageItem implementation to the queue for a forced refresh.
        /// <para/>Tips: use this function only if you want to refresh image of an item, 
        /// IBaseItem and ITextContainer are not images.
        /// </summary>
        /// <param name="item">An item as SpaceVIL.Core.IImageItem.</param>
        /// <returns>True: if adding is successfull. False: if an item is already in the refresh queue.</returns>
        public static bool SetRefreshImage(IImageItem item)
        {
            Monitor.Enter(_lockImageObject);
            try
            {
                return _images.Add(item);
            }
            finally
            {
                Monitor.Exit(_lockImageObject);
            }
        }

        // IS SECTION
        internal static bool IsRefreshShape(IBaseItem item)
        {
            Monitor.Enter(_lockShapeObject);
            try
            {
                return _shapes.Contains(item);
            }
            finally
            {
                Monitor.Exit(_lockShapeObject);
            }
        }

        internal static bool IsRefreshText(ITextContainer item)
        {
            Monitor.Enter(_lockTextObject);
            try
            {
                return _texts.Contains(item);
            }
            finally
            {
                Monitor.Exit(_lockTextObject);
            }
        }

        internal static bool IsRefreshImage(IImageItem item)
        {
            Monitor.Enter(_lockImageObject);
            try
            {
                return _images.Contains(item);
            }
            finally
            {
                Monitor.Exit(_lockImageObject);
            }
        }

        // REMOVE SECTION
        internal static bool RemoveShape(IBaseItem item)
        {
            Monitor.Enter(_lockShapeObject);
            try
            {
                return _shapes.Remove(item);
            }
            finally
            {
                Monitor.Exit(_lockShapeObject);
            }
        }

        internal static bool RemoveText(ITextContainer item)
        {
            Monitor.Enter(_lockTextObject);
            try
            {
                return _texts.Remove(item);
            }
            finally
            {
                Monitor.Exit(_lockTextObject);
            }
        }

        internal static bool RemoveImage(IImageItem item)
        {
            Monitor.Enter(_lockImageObject);
            try
            {
                return _images.Remove(item);
            }
            finally
            {
                Monitor.Exit(_lockImageObject);
            }
        }

        internal static void PrintSizeOfShapes()
        {
            foreach (IBaseItem item in _shapes)
            {
                Console.WriteLine(item.GetItemName() + " " + (item is Prototype) + " " + (item.GetBackground().A == 0));
            }
            Console.WriteLine("Shapes: " + _shapes.Count);
            Console.WriteLine("Images: " + _images.Count);
            Console.WriteLine("Text: " + _texts.Count);
        }
    }
}