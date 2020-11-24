package com.spvessel.spacevil.Core;

import java.util.List;
import java.util.LinkedList;

public class EventDropMethodState {

    List<IDropMethodState> events;
    
    public List<IDropMethodState> getActions() {
        return new LinkedList<>(events);
    }

    public int size() {
        if (events == null)
            return 0;
        return events.size();
    }
    
    public void add(IDropMethodState action) {
        if (events == null)
            events = new LinkedList<>();
        events.add(action);
    }

    public boolean remove(IDropMethodState action) {
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

    public void execute(IItem sender, DropArgs args) {
        if (events == null)
            return;
        for (IDropMethodState action : events) {
            action.execute(sender, args);
        }
    }
}