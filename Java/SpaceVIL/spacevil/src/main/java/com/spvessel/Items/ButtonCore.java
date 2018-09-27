package com.spvessel.Items;

import com.spvessel.Decorations.*;
import com.spvessel.Common.*;
import com.spvessel.Cores.*;

public class ButtonCore extends VisualItem {
    private static int count = 0;

    public ButtonCore() {
        setItemName("ButtonCore_" + count);
        count++;
        setStyle(Style.getButtonCoreStyle());
    }

    @Override
    public void setStyle(Style style)
        {
            if (style == null)
            return;
            super.setStyle(style);
        }
}