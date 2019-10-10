package com.spvessel.spacevil.Core;

import java.util.List;
import java.util.LinkedList;

public class EventDropMethodState {

    List<InterfaceDropMethodState> events;
    
    public List<InterfaceDropMethodState> getActions() {
        return new LinkedList<>(events);
    }

    public int size() {
        if (events == null)
            return 0;
        return events.size();
    }
    
    public void add(InterfaceDropMethodState action) {
        if (events == null)
            events = new LinkedList<>();
        events.add(action);
    }

    public boolean remove(InterfaceDropMethodState action) {
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

    public void execute(InterfaceItem sender, DropArgs args) {
        if (events == null)
            return;
        for (InterfaceDropMethodState action : events) {
            action.execute(sender, args);
        }
    }
}