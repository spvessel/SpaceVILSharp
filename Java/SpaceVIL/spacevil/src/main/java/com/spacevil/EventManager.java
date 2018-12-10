package com.spacevil;

import com.spacevil.Core.InterfaceEventUpdate;

import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

class EventManager {
    //protected static Boolean isLocked = true;
    private Map<com.spacevil.Flags.GeometryEventType, List<InterfaceEventUpdate>> listeners = new HashMap<>();

    void setListeners(com.spacevil.Flags.GeometryEventType... events) {
        List<com.spacevil.Flags.GeometryEventType> list = Arrays.stream(events).collect(Collectors.toList());
        for (com.spacevil.Flags.GeometryEventType s : list) {
            listeners.put(s, new LinkedList<InterfaceEventUpdate>());
        }
    }

    void subscribe(com.spacevil.Flags.GeometryEventType type, InterfaceEventUpdate listener) {
        if (!listeners.containsKey(type))
            listeners.put(type, new LinkedList<InterfaceEventUpdate>());

        if (!listeners.get(type).contains(listener))
            listeners.get(type).add(listener);
    }

    void unsubscribe(com.spacevil.Flags.GeometryEventType type, InterfaceEventUpdate listener) {
        if (listeners.containsKey(type)) {
            if (listeners.get(type).contains(listener)) {
                listeners.get(type).remove(listener);
            }
        }
    }

    void notifyListeners(com.spacevil.Flags.GeometryEventType type, int value) {
        if (listeners.containsKey(type)) {
            for (InterfaceEventUpdate item : listeners.get(type)) {
                item.update(type, value);
            }
        }
    }
}