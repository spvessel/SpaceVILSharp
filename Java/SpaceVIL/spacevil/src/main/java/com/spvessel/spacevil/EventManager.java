package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.InterfaceEventUpdate;
import com.spvessel.spacevil.Flags.GeometryEventType;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashSet;

class EventManager {
    //protected static Boolean isLocked = true;
    private Map<GeometryEventType, LinkedHashSet<InterfaceEventUpdate>> listeners = new HashMap<>();

    void subscribe(GeometryEventType type, InterfaceEventUpdate listener) {
        if (!listeners.containsKey(type))
            listeners.put(type, new LinkedHashSet<InterfaceEventUpdate>());

        if (!listeners.get(type).contains(listener))
            listeners.get(type).add(listener);
    }

    void unsubscribe(GeometryEventType type, InterfaceEventUpdate listener) {
        if (listeners.containsKey(type)) {
            if (listeners.get(type).contains(listener)) {
                listeners.get(type).remove(listener);
            }
        }
    }

    void notifyListeners(GeometryEventType type, int value) {
        if (listeners.containsKey(type)) {
            for (InterfaceEventUpdate item : listeners.get(type)) {
                item.update(type, value);
            }
        }
    }
}