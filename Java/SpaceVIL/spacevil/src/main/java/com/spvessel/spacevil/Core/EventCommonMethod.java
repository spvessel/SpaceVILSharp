package com.spvessel.spacevil.Core;

import java.util.LinkedList;
import java.util.List;

public class EventCommonMethod {

    List<ICommonMethod> events = null;

    public List<ICommonMethod> getActions() {
        return new LinkedList<>(events);
    }

    public int size() {
        if (events == null)
            return 0;
        return events.size();
    }
    
    public void add(ICommonMethod action) {
        if (events == null)
            events = new LinkedList<>();
        events.add(action);
    }

    public boolean remove(ICommonMethod action) {
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
        for (ICommonMethod action : events) {
            action.execute();
        }
    }
}