package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;

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
    protected boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }
}