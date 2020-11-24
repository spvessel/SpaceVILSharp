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

    boolean flushResource(TKey resource) {
        storageLocker.lock();
        try {
            if (!flushList.contains(resource)) {
                flushList.add(resource);
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
            flushStorage(flushList, resourceStorage);
        } finally {
            storageLocker.unlock();
        }
    }

    private <T, F extends IVramResource> void flushStorage(List<T> flushList, Map<T, F> storage) {
        for (T resource : flushList) {
            if (storage.containsKey(resource)) {
                storage.get(resource).clear();
                storage.remove(resource);
            }
        }
        flushList.clear();
    }

    Lock storageLocker = new ReentrantLock();

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