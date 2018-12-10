package com.spacevil.Core;

import java.util.List;
import java.util.LinkedList;

public class EventInputTextMethodState {

    List<InterfaceInputTextMethodState> events;

    public void add(InterfaceInputTextMethodState action) {
        if (events == null)
            events = new LinkedList<>();
        events.add(action);
    }

    public boolean remove(InterfaceInputTextMethodState action) {
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

    public void execute(InterfaceItem sender, TextInputArgs args) {
        if (events == null)
            return;
        for (InterfaceInputTextMethodState action : events) {
            action.execute(sender, args);
        }
    }
}