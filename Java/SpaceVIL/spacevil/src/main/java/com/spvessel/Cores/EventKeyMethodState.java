package com.spvessel.Cores;

import java.util.List;
import java.util.LinkedList;

public class EventKeyMethodState {

    List<InterfaceKeyMethodState> events;

    public void add(InterfaceKeyMethodState action) {
        if (events == null)
            events = new LinkedList<>();
        events.add(action);
    }

    public boolean remove(InterfaceKeyMethodState action) {
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

    public void execute(InterfaceItem sender, KeyArgs args) {
        if (events == null)
            return;
        for (InterfaceKeyMethodState action : events) {
            action.execute(sender, args);
        }
    }
}