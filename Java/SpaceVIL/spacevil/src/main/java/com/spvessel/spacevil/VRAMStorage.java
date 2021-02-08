package com.spvessel.spacevil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class VramStorage<TKey, TValue extends IVramResource> {

    Map<TKey, TValue> resourceStorage = new HashMap<>();
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
                    resourceStorage.get(item).clear();
                    resourceStorage.remove(item);
                }
            }
            flushList.clear();
        } finally {
            storageLocker.unlock();
        }
    }

    TValue getResource(TKey resource) {
        if (resourceStorage.containsKey(resource))
            return resourceStorage.get(resource);
        else
            return null;
    }

    boolean addResource(TKey resource, TValue vramObject) {
        if (resourceStorage.containsKey(resource))
            return false;
        else
            resourceStorage.put(resource, vramObject);
        return true;
    }

    boolean deleteResource(TKey resource) {
        if (resourceStorage.containsKey(resource)) {
            resourceStorage.get(resource).clear();
            resourceStorage.remove(resource);
            return true;
        }
        return false;
    }

    void clear() {
        for (TValue resource : resourceStorage.values()) {
            resource.clear();
        }
        resourceStorage.clear();
    }
}