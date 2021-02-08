package com.spvessel.spacevil.Core;

/**
 * An interface that define visual effect that can be attached to an item.
 */
public interface IEffect {
    /**
     * Getting the name of the current implementation of the visual effect.
     * @return Name of the visual effect as java.lang.String.
     */
    public String getEffectName();

    /**
     * Returns True if the effect is applied, False otherwise.
     * @return True: if effect is applied. False: if effect is not applied.
     */
    public boolean isApplied();
}