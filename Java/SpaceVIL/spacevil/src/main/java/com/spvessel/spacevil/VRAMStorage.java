package com.spvessel.spacevil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.spvessel.spacevil.Core.InterfaceImageItem;

class VRAMStorage {

    static Map<InterfaceImageItem, VRAMTexture> imageStorage = new HashMap<>();
    static List<InterfaceImageItem> imageToDelete = new LinkedList<>();

    static boolean addToDelete(InterfaceImageItem image) {
        storageLocker.lock();
        try {
            if (!imageToDelete.contains(image)) {
                imageToDelete.add(image);
                return true;
            }
            return false;
        } finally {
            storageLocker.unlock();
        }
    }

    static void flush() {
        storageLocker.lock();
        try {
            for (InterfaceImageItem image : imageToDelete) {
                if (imageStorage.containsKey(image)) {
                    imageStorage.get(image).deleteTexture();
                    imageStorage.remove(image);
                }
            }
            imageToDelete.clear();
        } finally {
            storageLocker.unlock();
        }
    }

    static Lock storageLocker = new ReentrantLock();

    static VRAMTexture getTexture(InterfaceImageItem image) {
        storageLocker.lock();
        try {
            if (imageStorage.containsKey(image))
                return imageStorage.get(image);
            else
                return null;
        } finally {
            storageLocker.unlock();
        }
    }

    static boolean replaceTexture(InterfaceImageItem image, VRAMTexture tex) {
        storageLocker.lock();
        try {
            if (imageStorage.containsKey(image)) {
                imageStorage.get(image).clear();
                imageStorage.remove(image);
                imageStorage.put(image, tex);
            }
            return true;
        } finally {
            storageLocker.unlock();
        }
    }

    static boolean addTexture(InterfaceImageItem image, VRAMTexture tex) {
        storageLocker.lock();
        try {
            if (imageStorage.containsKey(image))
                return false;
            else
                imageStorage.put(image, tex);
            return true;
        } finally {
            storageLocker.unlock();
        }
    }

    static boolean deleteTexture(InterfaceImageItem image) {
        storageLocker.lock();
        try {
            if (imageStorage.containsKey(image)) {
                imageStorage.get(image).deleteTexture();
                imageStorage.remove(image);
                return true;
            }
            return false;
        } finally {
            storageLocker.unlock();
        }
    }

    static void clear() {
        for (VRAMTexture tex : imageStorage.values()) {
            tex.clear();
        }
        imageStorage.clear();
    }
}