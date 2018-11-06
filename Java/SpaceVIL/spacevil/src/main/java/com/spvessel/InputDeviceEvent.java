package com.spvessel;

import java.util.LinkedList;
import java.util.List;

public class InputDeviceEvent {
    protected Boolean isHappen = false;
    private List<InputEventType> eType = new LinkedList<InputEventType>();

    public synchronized void setEvent(InputEventType type) {
        if (!eType.contains(type))
            eType.add(type);

        isHappen = true;
    }

    public synchronized Boolean isEvent() {
        Boolean tmp = isHappen;
        isHappen = false;
        return tmp;
    }

    public synchronized List<InputEventType> lastEvent() {
        return eType;
    }

    public synchronized void resetEvent(InputEventType type) {
        if (eType.contains(type))
            eType.remove(type);
    }

    public synchronized void resetAllEvents() {
        eType.clear();
    }
}