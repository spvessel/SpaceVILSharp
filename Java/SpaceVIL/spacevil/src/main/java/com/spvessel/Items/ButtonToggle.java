package com.spvessel.Items;

import com.spvessel.Cores.*;

public class ButtonToggle extends VisualItem {
    private static int count = 0;

    public ButtonToggle() {
        setItemName("ButtonToggle_" + count);
        count++;
    }
}