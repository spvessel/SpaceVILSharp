package com.spvessel.spacevil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.spvessel.spacevil.Core.IEffect;

final class VramEffectsStorage<TKey, TEffect extends IEffect, TResource extends IVramResource> {

    Map<TKey, Map<TEffect, TResource>> resourceStorage = new HashMap<>();
    List<TKey> flushList = new LinkedList<>();
    Lock storageLocker = new ReentrantLock();

    boolean addForFlushing(TKey item) {
        storageLocker.lock();
        try {
            if (!flushList.contains(item)) {
                flushList.add(item);
                return true;
            }
            return false;
        } finally {
            storageLocker.unlock();
        }
    }

    void flush() {
        storageLocker.lock();
        try {
            for (TKey item : flushList) {
                if (resourceStorage.containsKey(item)) {
                    Map<TEffect, TResource> effects = resourceStorage.get(item);
                    for (Map.Entry<TEffect, TResource> value : effects.entrySet()) {
                        value.getValue().clear();
                    }
                    effects.clear();
                    resourceStorage.remove(item);
                }
            }
            flushList.clear();
        } finally {
            storageLocker.unlock();
        }
    }

    Map<TEffect, TResource> getResources(TKey item) {
        if (resourceStorage.containsKey(item)) {
            return resourceStorage.get(item);
        }
        return null;
    }

    boolean addResource(TKey item, TEffect effect, TResource resource) {
        if (resourceStorage.containsKey(item)) {
            resourceStorage.get(item).put(effect, resource);
        } else {
            Map<TEffect, TResource> effects = new HashMap<>();
            effects.put(effect, resource);
            resourceStorage.put(item, effects);
        }
        return true;
    }

    boolean deleteResource(TKey item, TEffect effect) {
        if (resourceStorage.containsKey(item)) {
            Map<TEffect, TResource> effects = resourceStorage.get(item);
            if (effects.containsKey(effect)) {
                effects.get(effect).clear();
                effects.remove(effect);
                return true;
            }
        }
        return false;
    }

    void clear() {
        for (Map<TEffect, TResource> resource : resourceStorage.values()) {
            for (Map.Entry<TEffect, TResource> value : resource.entrySet()) {
                value.getValue().clear();
            }
            resource.clear();
        }
        resourceStorage.clear();
    }
}