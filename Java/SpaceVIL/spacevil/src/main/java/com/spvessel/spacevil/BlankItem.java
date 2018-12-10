package com.spvessel.spacevil;

public class BlankItem extends Prototype {
    static int count = 0;

    public BlankItem() {
        setItemName("BlankItem_" + count);
        count++;
    }
}