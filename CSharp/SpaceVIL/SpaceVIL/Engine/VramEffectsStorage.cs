using System;
using System.Collections.Generic;
using System.Threading;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal sealed class VramEffectsStorage<TKey, TEffect, TResource> where TEffect : IEffect where TResource : IVramResource
    {
        internal Dictionary<TKey, Dictionary<TEffect, TResource>> ResourceStorage = new Dictionary<TKey, Dictionary<TEffect, TResource>>();
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
                foreach (var item in FlushList)
                {
                    if (ResourceStorage.ContainsKey(item))
                    {
                        var effects = ResourceStorage[item];
                        foreach (var value in effects)
                        {
                            value.Value.Clear();
                        }
                        effects.Clear();
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

        internal Dictionary<TEffect, TResource> GetResources(TKey item)
        {
            if (ResourceStorage.ContainsKey(item))
            {
                return ResourceStorage[item];
            }
            return null;
        }

        internal bool AddResource(TKey item, TEffect effect, TResource resource)
        {
            if (ResourceStorage.ContainsKey(item))
            {
                ResourceStorage[item].Add(effect, resource);
            }
            else
            {
                var effects = new Dictionary<TEffect, TResource>();
                effects.Add(effect, resource);
                ResourceStorage.Add(item, effects);
            }
            return true;
        }

        internal bool DeleteResource(TKey item, TEffect effect)
        {
            if (ResourceStorage.ContainsKey(item))
            {
                var effects = ResourceStorage[item];
                if (effects.ContainsKey(effect))
                {
                    effects[effect].Clear();
                    effects.Remove(effect);
                    return true;
                }
            }
            return false;
        }

        internal void Clear()
        {
            foreach (var resource in ResourceStorage.Values)
            {
                foreach (var value in resource)
                {
                    value.Value.Clear();
                }
                resource.Clear();
            }
            ResourceStorage.Clear();
        }
    }
}