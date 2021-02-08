package com.spvessel.spacevil.Core;

import java.util.List;
import com.spvessel.spacevil.Flags.EffectType;

/**
 * IAppearanceExtension is an interface for managing effects of the item's shape.
 */
public interface IAppearanceExtension {

    /**
     * Adding effect to the item.
     * 
     * @param effect An effect as com.spvessel.spacevil.Core.IEffect.
     */
    void add(IEffect effect);

    /**
     * Removing specified effect from item.
     * 
     * @param effect An effect as com.spvessel.spacevil.Core.IEffect.
     */
    void remove(IEffect effect);

    /**
     * Getting list of applyed effects on the item.
     * 
     * @param type An effect type as com.spvessel.spacevil.Flags.EffectType.
     * @return List of effects of specified item as
     *         List&lt;com.spvessel.spacevil.Core.IBaseItem&gt;.
     */
    List<IEffect> get(EffectType type);

    /**
     * Clearing all specified effects from the item.
     * 
     * @param type An effect type as com.spvessel.spacevil.Flags.EffectType.
     */
    void clear(EffectType type);

    /**
     * Clearing all effects.
     */
    void clear();
}