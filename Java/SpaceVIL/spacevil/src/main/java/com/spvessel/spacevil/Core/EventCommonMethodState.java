package com.spvessel.spacevil.Core;

import java.util.List;
import java.util.LinkedList;

public class EventCommonMethodState {

    List<ICommonMethodState> events;

    public List<ICommonMethodState> getActions() {
        return new LinkedList<>(events);
    }

    public int size() {
        if (events == null)
            return 0;
        return events.size();
    }
    
    public void add(ICommonMethodState action) {
        if (events == null)
            events = new LinkedList<>();
        events.add(action);
    }

    public boolean remove(ICommonMethodState action) {
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

    public void execute(IItem sender) {
        if (events == null)
            return;
        for (ICommonMethodState action : events) {
            action.execute(sender);
        }
    }
}