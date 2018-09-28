package com.spvessel.Cores;

import com.spvessel.Flags.*;
import com.spvessel.Items.BaseItem;

import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class EventManager {
    protected static Boolean isLocked = true;
    Map<GeometryEventType, List<InterfaceEventUpdate>> listeners = new HashMap<GeometryEventType, List<InterfaceEventUpdate>>();

    public void setListeners(GeometryEventType... events) {
        List<GeometryEventType> list = Arrays.stream(events).collect(Collectors.toList());
        for (GeometryEventType s : list) {
            listeners.put(s, new LinkedList<InterfaceEventUpdate>());
        }
    }

    public void subscribe(GeometryEventType type, InterfaceEventUpdate listener) {
        if (!listeners.containsKey(type))
            listeners.put(type, new LinkedList<InterfaceEventUpdate>());

        if (!listeners.get(type).contains(listener))
            listeners.get(type).add(listener);
    }

    public void unsubscribe(GeometryEventType type, InterfaceEventUpdate listener) {
        if (listeners.containsKey(type)) {
            if (listeners.get(type).contains(listener)) {
                listeners.get(type).remove(listener);
            }
        }
    }

    public void notifyListeners(GeometryEventType type, int value) {
        if (listeners.containsKey(type)) {
            for (InterfaceEventUpdate item : listeners.get(type)) {
                item.update(type, value);
            }
        }
    }
}