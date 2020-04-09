package com.spvessel.spacevil.Decorations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.InterfaceEffect;
import com.spvessel.spacevil.Core.InterfaceSubtractFigure;

/**
 * Effects is a static class for controlling the application of effects to a item's shape.
 */
public final class Effects {
    private static Lock _lock = new ReentrantLock();

    private Effects() {
    }

    private static Map<InterfaceBaseItem, Set<InterfaceEffect>> _subtractEffects = new HashMap<>();

    /**
     * Adding effect to specified item.
     * @param item A item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @param effect A effect as com.spvessel.spacevil.Core.InterfaceEffect.
     */
    public static void addEffect(InterfaceBaseItem item, InterfaceEffect effect) {
        _lock.lock();
        try {
            if (effect instanceof InterfaceSubtractFigure) {
                if (_subtractEffects.containsKey(item)) {
                    _subtractEffects.get(item).add(effect);
                    return;
                }
                _subtractEffects.put(item, new HashSet<>());
                _subtractEffects.get(item).add(effect);
            }
        } finally {
            _lock.unlock();
        }
    }

    /**
     * Removing specified effect form item.
     * @param item A item as com.spvessel.spacevil.Core.InterfaceBaseItem.
     * @param effect A effect as com.spvessel.spacevil.Core.InterfaceEffect.
     * @return True: if such effect is presented and removed. False: if item has no such effect.
     */
    public static boolean removeEffect(InterfaceBaseItem item, InterfaceEffect effect) {
        _lock.lock();
        try {
            if (effect instanceof InterfaceSubtractFigure) {
                if (_subtractEffects.containsKey(item)) {
                    return _subtractEffects.get(item).remove(effect);
                }
            }
            return false;
        } finally {
            _lock.unlock();
        }
    }

    /**
     * Getting list of applyed effects on specified item.
     * @param item An item as com.spvessel.spacevil.Core.InterfaceEffect.
     * @return List of effects of specified item as List&lt;com.spvessel.spacevil.Core.InterfaceBaseItem&gt;.
     */
    public static List<InterfaceEffect> getEffects(InterfaceBaseItem item) {
        _lock.lock();
        try {
            if (_subtractEffects.containsKey(item)) {
                return new LinkedList<>(_subtractEffects.get(item));
            }
            return null;
        } finally {
            _lock.unlock();
        }
    }
}
