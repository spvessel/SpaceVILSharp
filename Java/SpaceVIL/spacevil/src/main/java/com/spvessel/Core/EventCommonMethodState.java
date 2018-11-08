package com.spvessel.Core;

import com.spvessel.Core.InterfaceCommonMethodState;
import com.spvessel.Core.InterfaceItem;

import java.util.List;
import java.util.LinkedList;

public class EventCommonMethodState {

    List<InterfaceCommonMethodState> events;

    public void add(InterfaceCommonMethodState action) {
        if (events == null)
            events = new LinkedList<>();
        events.add(action);
    }

    public boolean remove(InterfaceCommonMethodState action) {
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

    public void execute(InterfaceItem sender) {
        if (events == null)
            return;
        for (InterfaceCommonMethodState action : events) {
            action.execute(sender);
        }
    }
}