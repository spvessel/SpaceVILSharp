package com.spvessel.spacevil;

import java.util.ArrayDeque;
import java.util.Deque;

import com.spvessel.spacevil.Flags.InputEventType;

final class MouseScrollProcessor {

    private CommonProcessor _commonProcessor;

    MouseScrollProcessor(CommonProcessor processor) {
        _commonProcessor = processor;
    }

    void process(long wnd, double dx, double dy) {
        if (_commonProcessor.underHoveredItems.size() == 0)
            return;

        Deque<Prototype> tmp = new ArrayDeque<>(_commonProcessor.underHoveredItems);
        
        _commonProcessor.margs.scrollValue.setValues(Math.abs(dx), Math.abs(dy));

        while (!tmp.isEmpty()) {
            Prototype item = tmp.pollLast();
            if (dy > 0 || dx < 0) {
                item.eventScrollUp.execute(item, _commonProcessor.margs);
            }
            if (dy < 0 || dx > 0) {
                item.eventScrollDown.execute(item, _commonProcessor.margs);
            }
            if (!item.isPassEvents(InputEventType.MouseScroll)) {
                break;
            }
        }
        _commonProcessor.events.setEvent(InputEventType.MouseScroll);
    }
}