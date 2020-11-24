package com.spvessel.spacevil.Decorations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Core.IBorder;
import com.spvessel.spacevil.Core.IEffect;
import com.spvessel.spacevil.Core.IShadow;
import com.spvessel.spacevil.Core.ISubtractFigure;
import com.spvessel.spacevil.Flags.EffectType;

/**
 * Effects is a static class for controlling the application of effects to a
 * item's shape.
 */
public final class Effects {
    private static Lock _lock = new ReentrantLock();

    private Effects() {
    }

    private static Map<IBaseItem, Set<IEffect>> _subtractEffects = new HashMap<>();
    private static Map<IBaseItem, IEffect> _shadowEffects = new HashMap<>();
    private static Map<IBaseItem, IEffect> _borderEffects = new HashMap<>();

    /**
     * Adding effect to specified item.
     * 
     * @param item   A item as com.spvessel.spacevil.Core.IBaseItem.
     * @param effect A effect as com.spvessel.spacevil.Core.IEffect.
     */
    public static void addEffect(IBaseItem item, IEffect effect) {
        _lock.lock();
        try {
            if (effect instanceof ISubtractFigure) {
                if (_subtractEffects.containsKey(item)) {
                    _subtractEffects.get(item).add(effect);
                    return;
                }
                _subtractEffects.put(item, new HashSet<>());
                _subtractEffects.get(item).add(effect);
            } else if (effect instanceof IShadow) {
                _shadowEffects.put(item, effect);
                return;
            } else if (effect instanceof IBorder) {
                _borderEffects.put(item, effect);
                return;
            }
        } finally {
            _lock.unlock();
        }
    }

    /**
     * Removing specified effect form item.
     * 
     * @param item   A item as com.spvessel.spacevil.Core.IBaseItem.
     * @param effect A effect as com.spvessel.spacevil.Core.IEffect.
     * @return True: if such effect is presented and removed. False: if item has no
     *         such effect.
     */
    public static boolean removeEffect(IBaseItem item, IEffect effect) {
        _lock.lock();
        try {
            if (effect instanceof ISubtractFigure) {
                if (_subtractEffects.containsKey(item)) {
                    return _subtractEffects.get(item).remove(effect);
                }
            } else if (effect instanceof IShadow) {
                if (_shadowEffects.containsKey(item)) {
                    _shadowEffects.remove(item);
                    return true;
                }
            } else if (effect instanceof IBorder) {
                if (_borderEffects.containsKey(item)) {
                    _borderEffects.remove(item);
                    return true;
                }
            }
            return false;
        } finally {
            _lock.unlock();
        }
    }

    /**
     * Removing specified effect form item.
     * 
     * @param item An item as com.spvessel.spacevil.Core.IBaseItem.
     * @param type An effect type as com.spvessel.spacevil.Flags.EffectType.
     */
    public static void clearEffects(IBaseItem item, EffectType type) {
        _lock.lock();
        try {
            switch (type) {
                case Border:
                    if (_borderEffects.containsKey(item)) {
                        _borderEffects.remove(item);
                    }
                    break;
                case Shadow:
                    if (_shadowEffects.containsKey(item)) {
                        _shadowEffects.remove(item);
                    }
                    break;
                case Subtract:
                    if (_subtractEffects.containsKey(item)) {
                        _subtractEffects.get(item).clear();
                    }
                    break;
            }
        } finally {
            _lock.unlock();
        }
    }

    /**
     * Getting list of applyed effects on specified item.
     * 
     * @param item An item as com.spvessel.spacevil.Core.IEffect.
     * @param type An effect type as com.spvessel.spacevil.Flags.EffectType.
     * @return List of effects of specified item as
     *         List&lt;com.spvessel.spacevil.Core.IBaseItem&gt;.
     */
    public static List<IEffect> getEffects(IBaseItem item, EffectType type) {
        _lock.lock();
        try {
            switch (type) {
                case Border:
                    if (_borderEffects.containsKey(item)) {
                        return new LinkedList<>(Arrays.asList(_borderEffects.get(item)));
                    }
                    break;
                case Shadow:
                    if (_shadowEffects.containsKey(item)) {
                        return new LinkedList<>(Arrays.asList(_shadowEffects.get(item)));
                    }
                    break;
                case Subtract:
                    if (_subtractEffects.containsKey(item)) {
                        return new LinkedList<>(_subtractEffects.get(item));
                    }
                    break;
            }
            return new LinkedList<IEffect>();
        } finally {
            _lock.unlock();
        }
    }
}
