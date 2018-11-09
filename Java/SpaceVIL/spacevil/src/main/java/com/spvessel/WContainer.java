package com.spvessel;

import com.spvessel.Flags.ItemAlignment;

import java.util.*;

public class WContainer extends Prototype {
    private static int count = 0;
    public List<ItemAlignment> _sides = new LinkedList<>();
    public boolean _is_fixed = false;
    public boolean _resizing = false;

    public WContainer() {
        setItemName("WContainer_" + count);
        count++;
    }

    @Override
    public boolean getHoverVerification(float xpos, float ypos) {
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

    public List<ItemAlignment> getSides(float xpos, float ypos) {
        if (xpos <= 5) {
            _sides.add(ItemAlignment.LEFT);
        }
        if (xpos > getWidth() - 5) {
            _sides.add(ItemAlignment.RIGHT);
        }

        if (ypos <= 5) {
            _sides.add(ItemAlignment.TOP);
        }
        if (ypos > getHeight() - 5) {
            _sides.add(ItemAlignment.BOTTOM);
        }

        return _sides;
    }
}