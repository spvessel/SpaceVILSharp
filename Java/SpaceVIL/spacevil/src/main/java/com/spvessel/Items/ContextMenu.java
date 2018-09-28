package com.spvessel.Items;

import com.spvessel.Cores.*;

public class ContextMenu extends VisualItem {
    private static int count = 0;

    public ContextMenu() {
        setItemName("ContextMenu_" + count);
        count++;
    }

    public boolean closeDependencies(MouseArgs args) {
        return true;
    }
}