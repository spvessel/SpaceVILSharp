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

public final class Effects {
    private static Lock _lock = new ReentrantLock();

    private Effects() {
    }

    private static Map<InterfaceBaseItem, Set<InterfaceEffect>> _subtractEffects = new HashMap<>();

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
