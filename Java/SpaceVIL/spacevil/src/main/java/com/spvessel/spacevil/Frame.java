package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;

/**
 * Frame is the basic container. It groups items based on items alignment,
 * margins, paddings, sizes and size policies.
 * <p>
 * Frame cannot receive any events, so Frame is always in the
 * com.spvessel.spacevil.Flags.ItemStateType.BASE state.
 */
public class Frame extends Prototype {
    private static int count = 0;

    /**
     * Default Frame constructor. Frame cannot get focus.
     */
    public Frame() {
        setItemName("Frame_" + count);
        count++;
        setStyle(DefaultsService.getDefaultStyle(Frame.class));
        isFocusable = false;
    }

    @Override
    protected boolean getHoverVerification(float xpos, float ypos) {
        return false;
    }
}