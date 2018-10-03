package com.spvessel.Items;

import com.spvessel.Common.*;

public class Frame extends VisualItem {
    private static int count = 0;

    public Frame() {
        setItemName("Frame_" + count);
        count++;
        setStyle(DefaultsService.getDefaultStyle("SpaceVIL.Frame"));
    }

    @Override
    public boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }
}