package com.spvessel.Items;

import com.spvessel.Cores.*;

public class WContainer extends VisualItem {
    private static int count = 0;

    public WContainer() {
        setItemName("WContainer_" + count);
        count++;
    }
}