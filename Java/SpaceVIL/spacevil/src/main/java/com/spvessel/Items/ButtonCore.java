package com.spvessel.Items;

import com.spvessel.Cores.*;

public class ButtonCore extends VisualItem {
    private static int count = 0;

    public ButtonCore() {
        setItemName("ButtonCore_" + count);
        count++;
    }
}