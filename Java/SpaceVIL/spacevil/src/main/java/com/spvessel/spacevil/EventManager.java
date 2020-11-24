package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.IEventUpdate;
import com.spvessel.spacevil.Flags.GeometryEventType;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashSet;

class EventManager {
    //protected static Boolean isLocked = true;
    private Map<GeometryEventType, LinkedHashSet<IEventUpdate>> listeners = new HashMap<>();

    void subscribe(GeometryEventType type, IEventUpdate listener) {
        if (!listeners.containsKey(type))
            listeners.put(type, new LinkedHashSet<IEventUpdate>());

        if (!listeners.get(type).contains(listener))
            listeners.get(type).add(listener);
    }

    void unsubscribe(GeometryEventType type, IEventUpdate listener) {
        if (listeners.containsKey(type)) {
            if (listeners.get(type).contains(listener)) {
                listeners.get(type).remove(listener);
            }
        }
    }

    void notifyListeners(GeometryEventType type, int value) {
        if (listeners.containsKey(type)) {
            for (IEventUpdate item : listeners.get(type)) {
                item.update(type, value);
            }
        }
    }
}