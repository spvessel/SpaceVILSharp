package com.spvessel.Items;

import com.spvessel.Cores.*;

public class ToolTip extends VisualItem {
    private static int count = 0;

    private TextLine _text_object;

    public TextLine getTextLine() {
        return _text_object;
    }

    public ToolTip() {
        setItemName("ToolTip_" + count);
        count++;
    }

    public void initTimer(boolean value) {

    }
}