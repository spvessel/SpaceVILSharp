package com.spacevil.Core;

import java.util.List;
import java.util.LinkedList;

public class EventCommonMethod {

    List<InterfaceCommonMethod> events;

    public void add(InterfaceCommonMethod action) {
        if (events == null)
            events = new LinkedList<>();
        events.add(action);
    }

    public boolean remove(InterfaceCommonMethod action) {
        if (events == null)
            return false;
        if (events.contains(action)) {
            events.remove(action);
            return true;
        }
        return false;
    }

    public void clear() {
        if (events == null)
            return;
        events.clear();
    }

    public void execute() {
        if (events == null)
            return;
        for (InterfaceCommonMethod action : events) {
            action.execute();
        }
    }
}