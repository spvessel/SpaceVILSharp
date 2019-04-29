package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.EventDropMethodState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.Side;

import java.util.*;

final class WContainer extends Prototype {
    EventDropMethodState eventDrop = new EventDropMethodState();

    @Override
    public void release() {
        eventDrop.clear();
    }

    private static int count = 0;
    List<Side> _sides = new LinkedList<>();
    boolean _is_fixed = false;
    private Prototype _focus = null;

    WContainer() {
        setItemName("WContainer_" + count);
        count++;
        // setStyle(DefaultsService.getDefaultStyle(WContainer.class));
        setStyle(Style.getWindowContainerStyle());
        setEvents();
        // eventDrop.add((sender, args) -> {
        // for (String item : args.paths) {
        // System.out.println(item);
        // }
        // });
    }

    void setEvents() {
        eventFocusGet.add((sender) -> {
            getHandler().eventFocusGet.execute(sender);
        });
        eventFocusLost.add((sender) -> {
            getHandler().eventFocusLost.execute(sender);
        });
        eventResize.add((sender) -> {
            getHandler().eventResize.execute(sender);
        });
        eventDestroy.add((sender) -> {
            getHandler().eventDestroy.execute(sender);
        });
        eventMouseHover.add((sender, args) -> {
            getHandler().eventMouseHover.execute(sender, args);
        });
        eventMouseLeave.add((sender, args) -> {
            getHandler().eventMouseLeave.execute(sender, args);
        });
        eventMouseClick.add((sender, args) -> {
            getHandler().eventMouseClick.execute(sender, args);
        });
        eventMouseDoubleClick.add((sender, args) -> {
            getHandler().eventMouseDoubleClick.execute(sender, args);
        });
        eventMousePress.add((sender, args) -> {
            getHandler().eventMousePress.execute(sender, args);
        });
        eventMouseDrag.add((sender, args) -> {
            getHandler().eventMouseDrag.execute(sender, args);
        });
        eventMouseDrop.add((sender, args) -> {
            getHandler().eventMouseDrop.execute(sender, args);
        });
        eventScrollUp.add((sender, args) -> {
            getHandler().eventScrollUp.execute(sender, args);
        });
        eventScrollDown.add((sender, args) -> {
            getHandler().eventScrollDown.execute(sender, args);
        });
        eventKeyPress.add((sender, args) -> {
            getHandler().eventKeyPress.execute(sender, args);
        });
        eventKeyRelease.add((sender, args) -> {
            getHandler().eventKeyRelease.execute(sender, args);
        });
        eventTextInput.add((sender, args) -> {
            getHandler().eventTextInput.execute(sender, args);
        });
        eventDrop.add((sender, args) -> {
            getHandler().eventDrop.execute(sender, args);
        });
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
    protected boolean getHoverVerification(float xpos, float ypos) {
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
        if (xpos <= SpaceVILConstants.borderCursorTolerance) {
            _sides.add(Side.LEFT);
        }
        if (xpos >= getWidth() - SpaceVILConstants.borderCursorTolerance) {
            _sides.add(Side.RIGHT);
        }

        if (ypos <= SpaceVILConstants.borderCursorTolerance) {
            _sides.add(Side.TOP);
        }
        if (ypos >= getHeight() - SpaceVILConstants.borderCursorTolerance) {
            _sides.add(Side.BOTTOM);
        }

        return _sides;
    }
}