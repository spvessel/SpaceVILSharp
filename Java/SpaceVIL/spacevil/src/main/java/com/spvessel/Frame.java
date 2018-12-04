package com.spvessel;

import com.spvessel.Common.DefaultsService;

public class Frame extends Prototype {
    private static int count = 0;

    /**
     * Constructs a Frame
     */
    public Frame() {
        setItemName("Frame_" + count);
        count++;
        // setStyle(DefaultsService.getDefaultStyle("SpaceVIL.Frame"));
        setStyle(DefaultsService.getDefaultStyle(Frame.class));
        isFocusable = false;
    }

    @Override
    public boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }
}