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
        internal Object StorageLocker = new Object();

        internal bool AddForFlushing(TKey item)
        {
            Monitor.Enter(StorageLocker);
            try
            {
                if (!FlushList.Contains(item))
                {
                    FlushList.Add(item);
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
                foreach (TKey item in FlushList)
                {
                    if (ResourceStorage.ContainsKey(item))
                    {
                        ResourceStorage[item].Clear();
                        ResourceStorage.Remove(item);
                    }
                }
                FlushList.Clear();
            }
            finally
            {
                Monitor.Exit(StorageLocker);
            }
        }

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