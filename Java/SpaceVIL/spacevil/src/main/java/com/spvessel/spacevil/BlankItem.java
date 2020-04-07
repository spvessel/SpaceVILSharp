package com.spvessel.spacevil;

/**
 * BlankItem is pure subclass of Prototype without any extensions.
 * <p>
 * Example: used as cheap version of com.spvessel.spacevil.ButtonCore
 * (com.spvessel.spacevil.ButtonCore contains text and additional methods
 * extensions).
 */
public class BlankItem extends Prototype {
    static int count = 0;

    /**
     * Default BlankItem constructor.
     */
    public BlankItem() {
        setItemName("BlankItem_" + count);
        count++;
    }
}