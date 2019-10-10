using System;
using System.Collections.Generic;
using System.Threading;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal sealed class VramStorage<TKey, TValue> where TValue : IVramResource
    {
        internal Dictionary<TKey, TValue> ResourceStorage = new Dictionary<TKey, TValue>();
        internal List<TKey> FlushList = new List<TKey>();

        internal bool FlushResource(TKey resource)
        {
            Monitor.Enter(StorageLocker);
            try
            {
                if (!FlushList.Contains(resource))
                {
                    FlushList.Add(resource);
                    return true;
                }
                return false;
            }
            finally
            {
                Monitor.Exit(StorageLocker);
            }
        }

        internal void Flush()
        {
            Monitor.Enter(StorageLocker);
            try
            {
                FlushStorage(FlushList, ResourceStorage);
            }
            finally
            {
                Monitor.Exit(StorageLocker);
            }
        }

        private void FlushStorage<T, F>(List<T> flushList, Dictionary<T, F> storage)
            where F : IVramResource
        {
            foreach (T item in flushList)
            {
                if (storage.ContainsKey(item))
                {
                    storage[item].Clear();
                    storage.Remove(item);
                }
            }
            flushList.Clear();
        }

        internal Object StorageLocker = new Object();

        internal TValue GetResource(TKey resource)
        {
            Monitor.Enter(StorageLocker);
            try
            {
                if (ResourceStorage.ContainsKey(resource))
                    return ResourceStorage[resource];
                else
                    return default(TValue);
            }
            finally
            {
                Monitor.Exit(StorageLocker);
            }
        }

        internal bool AddResource(TKey resource, TValue vramObject)
        {
            if (ResourceStorage.ContainsKey(resource))
                return false;
            else
                ResourceStorage.Add(resource, vramObject);
            return true;
        }

        internal bool DeleteResource(TKey resource)
        {
            if (ResourceStorage.ContainsKey(resource))
            {
                ResourceStorage[resource].Clear();
                ResourceStorage.Remove(resource);
                return true;
            }
            return false;
        }

        internal void Clear()
        {
            foreach (TValue resource in ResourceStorage.Values)
            {
                resource.Clear();
            }
            ResourceStorage.Clear();
        }
    }
}