package com.spvessel.Items;

import com.spvessel.Cores.*;
import com.spvessel.Flags.Orientation;

public class PopUpMessage extends VisualItem {
    private static int count = 0;

    public PopUpMessage() {
        setItemName("PopUpMessage_" + count);
        count++;
    }

    public void holdSelf(boolean value) {

    }
}