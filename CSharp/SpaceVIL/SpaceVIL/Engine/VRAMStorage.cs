using System;
using System.Collections.Generic;
using System.Threading;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal static class VRAMStorage
    {
        internal static Dictionary<IImageItem, VRAMTexture> ImageStorage = new Dictionary<IImageItem, VRAMTexture>();
        internal static List<IImageItem> ImageToDelete = new List<IImageItem>();
        
        internal static  bool AddToDelete(IImageItem image)
        {
            if (!ImageToDelete.Contains(image))
            {
                ImageToDelete.Add(image);
                return true;
            }
            return false;
        }

        internal static void Flush()
        {
            foreach (IImageItem image in ImageToDelete)
            {
                if (ImageStorage.ContainsKey(image))
                {
                    ImageStorage[image].DeleteTexture();
                    ImageStorage.Remove(image);
                }
            }
            ImageToDelete.Clear();
        }

        internal static Object StorageLocker = new Object();
        internal static VRAMTexture GetTexture(IImageItem image)
        {
            Monitor.Enter(StorageLocker);
            try
            {
                if (ImageStorage.ContainsKey(image))
                    return ImageStorage[image];
                else
                    return null;
            }
            finally
            {
                Monitor.Exit(StorageLocker);
            }
        }
        internal static bool ReplaceTexture(IImageItem image, VRAMTexture tex)
        {
            Monitor.Enter(StorageLocker);
            try
            {
                if (ImageStorage.ContainsKey(image))
                {
                    ImageStorage[image].Clear();
                    ImageStorage.Remove(image);
                    ImageStorage.Add(image, tex);
                }
                return true;
            }
            finally
            {
                Monitor.Exit(StorageLocker);
            }
        }

        internal static bool AddTexture(IImageItem image, VRAMTexture tex)
        {
            Monitor.Enter(StorageLocker);
            try
            {
                if (ImageStorage.ContainsKey(image))
                    return false;
                else
                    ImageStorage.Add(image, tex);
                return true;
            }
            finally
            {
                Monitor.Exit(StorageLocker);
            }
        }

        internal static bool DeleteTexture(IImageItem image)
        {
            Monitor.Enter(StorageLocker);
            try
            {
                if (ImageStorage.ContainsKey(image))
                {                 
                    ImageStorage[image].DeleteTexture();
                    ImageStorage.Remove(image);
                    return true;
                }
                return false;
            }
            finally
            {
                Monitor.Exit(StorageLocker);
            }
        }

        internal static void Clear()
        {
            foreach (VRAMTexture tex in ImageStorage.Values)
            {
                tex.Clear();
            }
            ImageStorage.Clear();
        }
    }
}