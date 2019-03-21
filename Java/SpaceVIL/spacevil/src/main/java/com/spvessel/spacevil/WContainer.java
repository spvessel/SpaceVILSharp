package com.spvessel.spacevil;

import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.EventDropMethodState;
import com.spvessel.spacevil.Flags.Side;

import java.util.*;

public class WContainer extends Prototype {
    public EventDropMethodState eventDrop = new EventDropMethodState();

    @Override
    public void release() {
        eventDrop.clear();
    }

    private static int count = 0;
    public List<Side> _sides = new LinkedList<>();
    boolean _is_fixed = false;
    private Prototype _focus = null;

    public WContainer() {
        setItemName("WContainer_" + count);
        count++;
        setStyle(DefaultsService.getDefaultStyle(WContainer.class));
        // eventDrop.add((sender, args) -> {
        //     for (String item : args.paths) {
        //         System.out.println(item);
        //     }
        // });
    }

    void saveLastFocus(Prototype focused) {
        _focus = focused;
    }

    void restoreFocus() {
        if (_focus != null)
            _focus.setFocus();
        _focus = null;
    }

    @Override
    boolean getHoverVerification(float xpos, float ypos) {
        boolean result = false;
        if (_is_fixed)
            return false;

        if (xpos > 0 && xpos <= getHandler().getWidth() && ypos > 0 && ypos <= getHandler().getHeight()) {
            result = true;
            // _mouse_ptr.setPosition(xpos, ypos);
        } else {
            // _mouse_ptr.clear();
        }
        return result;
    }

    List<Side> getSides(float xpos, float ypos) {
        if (xpos <= 5) {
            _sides.add(Side.LEFT);
        }
        if (xpos >= getWidth() - 5) {
            _sides.add(Side.RIGHT);
        }

        if (ypos <= 5) {
            _sides.add(Side.TOP);
        }
        if (ypos >= getHeight() - 5) {
            _sides.add(Side.BOTTOM);
        }

        return _sides;
    }
}