package com.spvessel.Items;

import com.spvessel.Cores.*;

public class Frame extends VisualItem {
    private static int count = 0;

    public Frame() {
        setItemName("Frame_" + count);
        count++;
    }
}