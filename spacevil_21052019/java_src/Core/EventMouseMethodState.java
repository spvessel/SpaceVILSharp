package com.spvessel.spacevil.Core;

import java.util.List;
import java.util.LinkedList;

public class EventMouseMethodState {

    List<InterfaceMouseMethodState> events;

    public int size() {
        if (events == null)
            return 0;
        return events.size();
    }

    public void add(InterfaceMouseMethodState action) {
        if (events == null)
            events = new LinkedList<>();
        events.add(action);
    }

    public boolean remove(InterfaceMouseMethodState action) {
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

    public void execute(InterfaceItem sender, MouseArgs args) {
        if (events == null)
            return;
        for (InterfaceMouseMethodState action : events) {
            action.execute(sender, args);
        }
    }
}