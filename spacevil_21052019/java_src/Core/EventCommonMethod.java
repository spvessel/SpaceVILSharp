package com.spvessel.spacevil.Core;

import java.util.Set;
import java.util.LinkedHashSet;

public class EventCommonMethod {

    Set<InterfaceCommonMethod> events = null;

    public int size() {
        if (events == null)
            return 0;
        return events.size();
    }
    
    public void add(InterfaceCommonMethod action) {
        if (events == null)
            events = new LinkedHashSet<>();
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