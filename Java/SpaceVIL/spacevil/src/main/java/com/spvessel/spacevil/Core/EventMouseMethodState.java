package com.spvessel.spacevil.Core;

import java.util.List;
import java.util.LinkedList;

public class EventMouseMethodState {

    List<IMouseMethodState> events;

    public List<IMouseMethodState> getActions() {
        return new LinkedList<>(events);
    }

    public int size() {
        if (events == null)
            return 0;
        return events.size();
    }

    public void add(IMouseMethodState action) {
        if (events == null)
            events = new LinkedList<>();
        events.add(action);
    }

    public boolean remove(IMouseMethodState action) {
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

    public void execute(IItem sender, MouseArgs args) {
        if (events == null)
            return;
        for (IMouseMethodState action : events) {
            action.execute(sender, args);
            // if (_cancel)
            //     break;
        }
        // _cancel = false;
    }

    // boolean _cancel = false;

    // public void cancel() {
    //     _cancel = true;
    // }
}